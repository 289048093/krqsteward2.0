package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.ordercenter.constant.AfterSalesStatus;
import com.ejushang.steward.ordercenter.domain.AfterSales;
import com.ejushang.steward.ordercenter.domain.AfterSalesItem;
import com.ejushang.steward.ordercenter.domain.AfterSalesRefund;
import com.ejushang.steward.ordercenter.returnvisit.ReturnVisitManager;
import com.ejushang.steward.ordercenter.vo.ReceiveGoodsVo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Channel
 * @Date 2014/8/26
 * @Version: 1.0
 */
@Component
@Transactional
@Aspect
@Order(value = 1)
public class AfterSalesAspect {

    private static final Logger log = LoggerFactory.getLogger(AfterSalesAspect.class);

    @Autowired
    private AfterSalesService afterSalesService;

    @Autowired
    private GeneralDAO generalDAO;


    /**
     * 售后单经仓库同意后触发检测订单是否已结束
     *
     * @param joinPoint
     */
    @AfterReturning("execution(* com.ejushang.steward.ordercenter.service.AfterSalesService.flow(..))")
    public void flow(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Integer afterSalesId = (Integer) args[0];
        AfterSalesStatus status = (AfterSalesStatus) args[1];
        if (AfterSalesStatus.ACCEPT.equals(status)) {
            finish(afterSalesService.get(afterSalesId), "售后单仓库审核通过");
        }
    }

    /**
     * 售后单确认支付后触发检测订单是否已结束
     *
     * @param joinPoint
     */
    @AfterReturning("execution(* com.ejushang.steward.ordercenter.service.AfterSalesService.onPayAfterSalesRefund(..))")
    public void onPayAfterSalesRefund(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Integer refundId = (Integer) args[0];
        AfterSalesRefund afterSalesRefund = afterSalesService.findAfterSalesRefundById(refundId);
        Integer afterSalesId = afterSalesRefund.getAfterSalesId();
        finish(afterSalesService.get(afterSalesId), "售后单确认支付");
    }

    /**
     * 售后单确认收货后触发检测订单是否已结束
     *
     * @param joinPoint
     */
    @AfterReturning("execution(* com.ejushang.steward.ordercenter.service.AfterSalesService.receiveGoods(..))")
    public void receiveGoods(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        ReceiveGoodsVo receiveGoodsVo = (ReceiveGoodsVo) args[0];
        boolean confirm = (Boolean) args[1];
        Integer afterSalesItemId = receiveGoodsVo.getAfterSalesItemId();
        AfterSalesItem afterSalesItem = generalDAO.get(AfterSalesItem.class, afterSalesItemId);
        Integer afterSalesId = afterSalesItem.getAfterSalesId();
        finish(afterSalesService.get(afterSalesId), "售后单确认收货");
    }

    /**
     * 发货单确认签收检测订单是否已结束
     *
     * @param joinPoint
     */
    @AfterReturning("execution(* com.ejushang.steward.ordercenter.service.InvoiceService.buyerReceive(..))")
    public void buyerReceive(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Integer[] orderIds = (Integer[]) args[0];
        if (orderIds != null) {
            for (Integer orderId : orderIds) {
                AfterSales afterSales = afterSalesService.getAfterSalesBySendOrderId(orderId);
                finish(afterSales, "售后发货单签收");
            }
        }
    }


    /**
     * 处理已结束售后单
     *
     * @param afterSales
     */
    private void finish(AfterSales afterSales, String remarks) {
        if (afterSales == null) {
            return;
        }
        boolean finish = afterSalesService.finish(afterSales);
        log.info("订单结束状态检查 orderId:{}, finish:{}, remarks:{}", new Object[]{afterSales.getId(), finish, remarks});
        if (finish) {
            Integer orderId = afterSales.getOrderId();
            String code = afterSales.getCode();
            log.info("订单已结束，触发生成售后回访单。 ~ orderID:{}, afterSalesCode:{}", orderId, code);
            ReturnVisitManager.fromAfterSales(orderId, code);
        }
    }


}
