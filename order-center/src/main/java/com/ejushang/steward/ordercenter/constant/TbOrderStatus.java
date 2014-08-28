package com.ejushang.steward.ordercenter.constant;

/**
 * 淘宝平台订单状态
 * User: Baron.Zhang
 * Date: 14-4-14
 * Time: 下午7:27
 */
public enum TbOrderStatus {
    TRADE_NO_CREATE_PAY("没有创建支付宝交易"),
    WAIT_BUYER_PAY("等待买家付款"),
    SELLER_CONSIGNED_PART("卖家部分发货"),
    WAIT_SELLER_SEND_GOODS("等待卖家发货,即:买家已付款"),
    WAIT_BUYER_CONFIRM_GOODS("等待买家确认收货,即:卖家已发货"),
    TRADE_BUYER_SIGNED("买家已签收,货到付款专用"),
    TRADE_FINISHED("交易成功"),
    TRADE_CLOSED("付款以后用户退款成功，交易自动关闭"),
    TRADE_CLOSED_BY_TAOBAO("付款以前，卖家或买家主动关闭交易")
    ;

    public String value;
    private TbOrderStatus(String value){
        this.value = value;
    }

}
