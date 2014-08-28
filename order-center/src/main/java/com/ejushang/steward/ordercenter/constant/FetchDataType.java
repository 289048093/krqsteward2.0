package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * User: Baron.Zhang
 * Date: 2014/5/28
 * Time: 16:47
 */
public enum FetchDataType implements ViewEnum{
    FETCH_ORDER("订单抓取"),
    FETCH_REFUND("退款单抓取"),
    FETCH_RETURN("退货单抓取")
    ;


    private String value;

    FetchDataType(String value){
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
