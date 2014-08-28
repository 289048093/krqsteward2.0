package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 售后来源
 * @Author Channel
 * @Date ${date}
*/
@JsonSerialize(using = EnumSerializer.class)
public enum AfterSalesSource implements ViewEnum {

    ORDER("普通订单"),
    VISIT("回访单");

    public String value;

    AfterSalesSource(String value) {
    this.value = value;
    }

    @Override
    public String getName() {
    return this.toString();
    }

    @Override
    public String getValue() {
    return value;
    }

}