package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.OrderLogType;
import com.ejushang.steward.ordercenter.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import javax.transaction.Transactional;

/**
 * User: JBoss.WU
 * Date: 14-8-6
 * Time: 下午3:39
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class MemberInfoLogService {

    private static Logger log= LoggerFactory.getLogger(MemberInfoLogService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Transactional
    public void save(Order order, Refund refund, Payment payment, OriginalOrder originalOrder, OrderLogType orderLogType, boolean isRefundPayment) {
        MemberInfoLog memberInfoLog = new MemberInfoLog();
        switch (orderLogType) {
            //确认订单时插入会员
            case CONFIRMED:
                memberInfoLog.setActualFee(order.getActualFee());
                Invoice invoice = order.getInvoice();
                memberInfoLog.setCreateTime(new Date());
                memberInfoLog.setBuyerId(order.getBuyerId());
                memberInfoLog.setOrderLogType(OrderLogType.CONFIRMED);
                memberInfoLog.setOffline(order.getOnline() ? false : true);
                memberInfoLog.setShopId(order.getShopId());
                memberInfoLog.setPlatformOrderNo(order.getPlatformOrderNo());
                memberInfoLog.setPlatformType(order.getPlatformType());
                if (invoice != null) {
                    memberInfoLog.setReceiver(invoice.getReceiver());
                    memberInfoLog.setShippingComp(invoice.getShippingComp());
                    memberInfoLog.setShippingNo(invoice.getShippingNo());
                }
                break;
            //退款时生成退款
            case REFUND:
                Refund refund1 = generalDAO.get(Refund.class, refund.getId());
                Order order1 = refund1.getOrderItem().getOrder();
                memberInfoLog.setShopId(order1.getShopId());
                memberInfoLog.setPlatformOrderNo(order1.getPlatformOrderNo());
                memberInfoLog.setPlatformType(order1.getPlatformType());
                if (isRefundPayment) {
                    memberInfoLog.setRefundPaymentFee(refund1.getRefundFee());
                } else {
                    memberInfoLog.setRefundOrderFee(refund1.getActualRefundFee());
                }
                memberInfoLog.setRefundId(refund1.getId());
                Invoice invoice1 = order1.getInvoice();
                if (invoice1 != null) {
                    memberInfoLog.setReceiver(invoice1.getReceiver());
                    memberInfoLog.setShippingComp(invoice1.getShippingComp());
                    memberInfoLog.setShippingNo(invoice1.getShippingNo());
                }
                memberInfoLog.setBuyerId(order1.getBuyerId());
                memberInfoLog.setOrderLogType(OrderLogType.REFUND);
                break;
            //预收款时
            case PAYMENT:
                OriginalOrder originalOrder1 = payment.getOriginalOrder();
                memberInfoLog.setPaymentFee(payment.getPaymentFee());
                memberInfoLog.setShopId(originalOrder1.getShopId());
                memberInfoLog.setPlatformOrderNo(originalOrder1.getPlatformOrderNo());
                memberInfoLog.setPlatformType(originalOrder1.getPlatformType());
                Receiver receiver1 = originalOrder1.getReceiver();
                if (receiver1 != null) {
                    memberInfoLog.setReceiver(receiver1);
                }
                memberInfoLog.setBuyerId(originalOrder1.getBuyerId());
                memberInfoLog.setOrderLogType(OrderLogType.PAYMENT);
                break;
            //原始单时
            case ORIGINAL:
                memberInfoLog.setPlatformOrderNo(originalOrder.getPlatformOrderNo());
                memberInfoLog.setPlatformType(originalOrder.getPlatformType());
                memberInfoLog.setPlatformOrderNo(originalOrder.getPlatformOrderNo());
                Receiver receiver = originalOrder.getReceiver();
                memberInfoLog.setShopId(originalOrder.getShopId());
                if (receiver != null) {
                    memberInfoLog.setReceiver(receiver);
                }
                memberInfoLog.setBuyerId(originalOrder.getBuyerId());
                memberInfoLog.setOrderLogType(OrderLogType.ORIGINAL);
                break;
        }
        memberInfoLog.setCreateTime(new Date());
        generalDAO.saveOrUpdate(memberInfoLog);
    }


    //更新退款金额
    public void updateRefund(Refund refund) {
        MemberInfoLog memberInfoLog = (MemberInfoLog) generalDAO.searchUnique(new Search(MemberInfoLog.class).addFilterEqual("refundId", refund.getId()));
        if(memberInfoLog==null){
            log.warn("No MemberInfoLog found by refund id:"+refund.getId());
            return;
        }

        //抵消之前退款日志产生的影响
        if(memberInfoLog.getRefundOrderFee()!=null) {
            MemberInfoLog m1 = memberInfoLog.clone();
            m1.setId(null);
            m1.setRefundOrderFee(Money.valueOf(0).subtract(memberInfoLog.getRefundOrderFee()));
            m1.setProcessed(false);

            generalDAO.saveOrUpdate(m1);
        }

        MemberInfoLog m2=memberInfoLog.clone();
        m2.setId(null);
        m2.setRefundOrderFee(refund.getActualRefundFee());
        m2.setProcessed(false);

        generalDAO.saveOrUpdate(m2);

    }


}
