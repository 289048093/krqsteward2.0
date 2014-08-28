package com.ejushang.steward.ordercenter.service;


import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.domain.OrderItem;
import com.ejushang.steward.ordercenter.util.CalculableOrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * User: liubin
 * Date: 14-4-14
 * Time: 上午9:19
 */
@Service
@Transactional
public class OrderFeeService {

    private static final Logger log = LoggerFactory.getLogger(OrderFeeService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private OrderService orderService;

    /**
     * 计算订单金额
     * @param order
     * @param orderItems
     */
    public void calculateOrderFee(Order order, List<OrderItem> orderItems) {
        Money orderSharedPostFee = Money.valueOf(0);
        Money orderSharedDiscountFee = Money.valueOf(0);
        Money orderActualFee = Money.valueOf(0);
        Money orderGoodsFee = Money.valueOf(0);

        for (OrderItem oi : orderItems) {
            orderSharedPostFee = orderSharedPostFee.add(oi.getSharedPostFee());
            orderSharedDiscountFee = orderSharedDiscountFee.add(oi.getSharedDiscountFee());
            orderActualFee = orderActualFee.add(oi.getActualFee());
            orderGoodsFee = orderGoodsFee.add(oi.getGoodsFee());
        }

        order.setSharedPostFee(orderSharedPostFee);
        order.setSharedDiscountFee(orderSharedDiscountFee);
        order.setActualFee(orderActualFee);
        order.setGoodsFee(orderGoodsFee);
    }


    /**
     * 计算订单项金额
     *
     * @param calculableOrderItem
     */
    public void calculateOrderItemFee(CalculableOrderItem calculableOrderItem) {
        Money actualFee = calculableOrderItem.getPrice()
                .multiply(calculableOrderItem.getBuyCount())
                .subtract(calculableOrderItem.getDiscountFee())
                .add(calculableOrderItem.getSharedPostFee())
                .subtract(calculableOrderItem.getRefundFee());

        if(calculableOrderItem.getPlatformType().equals(PlatformType.TAO_BAO)) {
            //天猫的整单优惠不会给我们结算,例如爱东爱西的镜子
            actualFee = actualFee.subtract(calculableOrderItem.getSharedDiscountFee());
        }

        Money goodsFee = calculableOrderItem.getPrice()
                .multiply(calculableOrderItem.getBuyCount())
                .subtract(calculableOrderItem.getDiscountFee())
                .subtract(calculableOrderItem.getActualRefundFee())
                .add(calculableOrderItem.getServiceCoverFee())
                .subtract(calculableOrderItem.getServiceCoverRefundFee())
                .subtract(calculableOrderItem.getOfflineRefundFee());
        if(calculableOrderItem.getPlatformType().equals(PlatformType.TAO_BAO)) {
            //天猫的整单优惠不会给我们结算,例如爱东爱西的镜子
            goodsFee = goodsFee.subtract(calculableOrderItem.getSharedDiscountFee());
        }

        calculableOrderItem.setActualFee(actualFee);
        calculableOrderItem.setGoodsFee(goodsFee);
    }

    /**
     * 判断订单对应的所有订单项是否已全部退货
     *
     * @param order
     * @return
     */
    public void checkOrderReturnStatus(Order order) {
        int allItemCount = order.getOrderItemList().size();
        //退货数量
        int returnItemCount= 0;
        for (OrderItem item : order.getOrderItemList()) {
            if (item.getOfflineReturnStatus().equals(OrderItemReturnStatus.RETURNED) || item.getReturnStatus().equals(OrderItemReturnStatus.RETURNED)) {
                returnItemCount++;
            }
        }

        if (returnItemCount == 0) {
            //正常
            order.setOrderReturnStatus(OrderReturnStatus.NORMAL);
        } else if (returnItemCount < allItemCount) {
            //部分退货
            order.setOrderReturnStatus(OrderReturnStatus.PART_RETURN);
        } else {
            //已退货
            order.setOrderReturnStatus(OrderReturnStatus.RETURNED);
        }

    }




}
