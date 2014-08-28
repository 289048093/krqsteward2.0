package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * User: Baron.Zhang
 * Date: 2014/6/18
 * Time: 18:44
 */
@JsonSerialize(using = EnumSerializer.class)
public enum OriginalRefundTradeStatus implements ViewEnum{
    WAIT_SEND_GOOD("等待卖家发货"),
    WAIT_CONFIRM_GOOD("卖家已发货，等待买家确认收货"),
    FINISHED("交易完成");

    private String value;

    OriginalRefundTradeStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName(){
        return this.toString();
    }
}
