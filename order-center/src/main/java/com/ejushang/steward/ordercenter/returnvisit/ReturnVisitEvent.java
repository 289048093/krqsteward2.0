package com.ejushang.steward.ordercenter.returnvisit;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.exception.StewardException;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.constant.ReturnVisitType;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.domain.OrderItem;

import java.util.List;

/**
 * Created by: codec.yang
 * Date: 2014/8/6 11:53
 */
public class ReturnVisitEvent {
    private ReturnVisitType type;
    private Integer orderId;
    private String afterSalesNo;

    private Order order;
    private Receiver receiver;

    public ReturnVisitEvent(ReturnVisitType type, Integer orderId){
        this.type=type;
        this.orderId=orderId;
    }

    public ReturnVisitEvent(ReturnVisitType type, Integer orderId, String afterSalesNo) {
        this(type,orderId);
        this.afterSalesNo=afterSalesNo;
    }

    public ReturnVisitType getType() {
        return type;
    }

    public String getPhone(){
        return this.receiver.getReceiverPhone();
    }

    public List<OrderItem> getOrderItemList(){
        return this.order.getOrderItemList();
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public String getPlatformOrderNo() {
        return this.order.getPlatformOrderNo();
    }

    public PlatformType getPlatformType(){
        return this.order.getPlatformType();
    }

    public String getAfterSaleNo() {
        return this.afterSalesNo;
    }

    public String getMobile() {
        return this.receiver.getReceiverMobile();
    }

    @Override
    public String toString(){
        return "ReturnVisitEvent{ type="+this.type+","
                +"orderId="+this.orderId
                +"afterSalesNo="+this.afterSalesNo
                +"}";
    }

    public void init(Order order) {
        if(order==null){
            throw new RuntimeException("Order instance cannot be null");
        }
        if(order.getInvoice()==null||order.getInvoice().getReceiver()==null){
            throw new RuntimeException("Order's receiver property cannot be null ");
        }

        this.order=order;
        this.receiver=order.getInvoice().getReceiver();
    }
}
