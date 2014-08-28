package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 退款方式
 * @Author Channel
 * @Date ${date}
*/
@JsonSerialize(using = EnumSerializer.class)
public enum RefundMethod implements ViewEnum {

    ALIPAY("支付宝"),
    BANK("银行");

    public String value;

    RefundMethod(String value) {
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