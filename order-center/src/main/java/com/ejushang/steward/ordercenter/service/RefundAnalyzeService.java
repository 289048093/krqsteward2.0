package com.ejushang.steward.ordercenter.service;


import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * User: liubin
 * Date: 14-4-14
 * Time: 上午9:19
 */
@Service
@Transactional
public class RefundAnalyzeService {

    private static final Logger log = LoggerFactory.getLogger(RefundAnalyzeService.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private PaymentService paymentService;




    /**
     *
     * @param originalRefund
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void analyze(OriginalRefund originalRefund) {
        //一些校验
        checkPreconditions(originalRefund);
        //
        setOriginalRefundPhase(originalRefund);

        //根据外部退款单号查询系统内的退款单,判断是新增还是更新
        List<Refund> refunds = refundService.findByPlatformRefundNo(originalRefund.getPlatformType(), originalRefund.getRefundId());

        if(refunds.isEmpty()) {
            //新增
            createNewRefund(originalRefund);

        } else {

            //更新
            refundService.updateByOriginalRefund(refunds, originalRefund);

        }

    }

    /**
     * 退款新增
     * @param originalRefund
     */
    private void createNewRefund(OriginalRefund originalRefund) {

        if(log.isDebugEnabled()) {
            log.debug("开始根据原始退款生成新的智库城退款,原始退款信息[id={}]", originalRefund.getId());
        }

        //首先判断是天猫还是京东的退款
        //如果是天猫的,先根据退款中的子订单号查订单项,如果查询为空,则查预收款记录.
        //如果是京东的,先根据退款中的交易号查订单,如果查询为空,则查预收款记录.
        List<Refund> refunds = new ArrayList<Refund>();
        switch (originalRefund.getPlatformType()) {
            case TAO_BAO: {
                log.debug("淘宝退款,原始退款信息[id={}]", originalRefund.getId());
                String platformSubOrderNo = originalRefund.getOid();
                List<OrderItem> orderItems = orderService.findOrderItemByPlatformSubOrderNo(originalRefund.getPlatformType(), platformSubOrderNo);
                if(orderItems.isEmpty()) {
                    List<Payment> payments = paymentService.findByPlatformSubOrderNo(originalRefund.getPlatformType(), platformSubOrderNo);
                    if(payments.isEmpty()) {
                        throw new StewardBusinessException(String.format("天猫退款,根据外部平台子订单号既没有找到订单项,也没有找到预收款记录, platformSubOrderNo[%s]", platformSubOrderNo));
                    } else {
                        refunds.addAll(refundService.createByPayments(originalRefund, payments));
                    }
                }  else {
                    refunds.addAll(refundService.createByOrderItems(originalRefund, orderItems));
                }

                break;
            }
            case JING_DONG: {
                log.debug("京东退款,原始退款信息[id={}]", originalRefund.getId());
                String platformOrderNo = originalRefund.getTid();
                List<Order> orders = orderService.findByPlatformOrderNo(originalRefund.getPlatformType(), platformOrderNo);
                List<OrderItem> orderItems = new ArrayList<OrderItem>();
                for(Order order : orders) {
                    orderItems.addAll(order.getOrderItemList());
                }
                if(!orderItems.isEmpty()) {
                    refunds.addAll(refundService.createByOrderItems(originalRefund, orderItems));
                }
                List<Payment> payments = paymentService.findByPlatformOrderNo(originalRefund.getPlatformType(), platformOrderNo);
                if(!payments.isEmpty()) {
                    refunds.addAll(refundService.createByPayments(originalRefund, payments));
                }

                if(orders.isEmpty() && payments.isEmpty()) {
                    throw new StewardBusinessException(String.format("京东退款,根据外部平台订单号既没有找到订单,也没有找到预收款记录, platformOrderNo[%s]", platformOrderNo));
                }

                break;
            }
            default: {
                throw new StewardBusinessException(String.format("进行退款分析时发现不支持的平台类型, platformType[%s]", originalRefund.getPlatformType()));
            }
        }

        if(refunds.isEmpty()) {
            throw new StewardBusinessException(String.format("进行退款记录新增,但是发现结束后并没有新增任何退款记录, platformType[%s]", originalRefund.getPlatformType()));
        }
    }

    /**
     * 做一些校验工作
     * @param originalRefund
     */
    private void checkPreconditions(OriginalRefund originalRefund) {
        //平台不能为空
        PlatformType platformType = originalRefund.getPlatformType();
        if(platformType == null) {
            throw new StewardBusinessException("原始退款中的外部平台类型为空");
        }
        //状态不能为空
        OriginalRefundStatus originalRefundStatus = originalRefund.getStatus();
        if(originalRefundStatus == null) {
            throw new StewardBusinessException("原始退款的状态为空");
        }

        if(StringUtils.isBlank(originalRefund.getRefundId())) {
            throw new StewardBusinessException("原始退款的退款单编号为空");
        }
    }

    /**
     * 如果是淘宝,根据交易状态判断是售前退款还是售后,如果是京东,则默认为售前
     * @param originalRefund
     */
    private void setOriginalRefundPhase(OriginalRefund originalRefund) {

        PlatformType platformType = originalRefund.getPlatformType();
        RefundPhase refundPhase = RefundPhase.ON_SALE;
        OriginalRefundTradeStatus originalRefundTradeStatus;

        switch (platformType) {
            case TAO_BAO_2:
            case TAO_BAO: {
                try {
                    originalRefundTradeStatus = OriginalRefundTradeStatus.valueOf(originalRefund.getTradeStatus());
                } catch (IllegalArgumentException e) {
                    throw new StewardBusinessException("原始退款信息有误,不能识别的退款的交易状态:" + originalRefund.getTradeStatus());
                }
                //退款阶段是根据订单交易状态来区分的
                switch (originalRefundTradeStatus) {
                    case WAIT_SEND_GOOD:
                        refundPhase = RefundPhase.ON_SALE;
                        break;
                    default:
                        refundPhase = RefundPhase.AFTER_SALE;
                        break;

                }
                break;
            }
            case JING_DONG: {
                //京东默认为售前
                break;
            }
        }

        originalRefund.setRefundPhase(refundPhase);

    }


}


