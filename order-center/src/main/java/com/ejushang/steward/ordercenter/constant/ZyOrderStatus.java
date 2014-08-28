package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * User: Shiro
 * Date: 14-8-12
 * Time: 下午5:28
 */
public enum ZyOrderStatus implements ViewEnum {
    WAIT_BUYER_PAY("等待买家付款"),
    WAIT_SELLER_SEND_GOODS("等待卖家发货,即:买家已付款"),
    WAIT_BUYER_CONFIRM_GOODS("等待买家确认收货,即:卖家已发货"),
    TRADE_FINISHED("交易成功");

    public String value;
    private ZyOrderStatus(String value){
        this.value = value;
    }
    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
