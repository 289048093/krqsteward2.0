package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 退款类型
 * User: liubin
 * Date: 13-12-30
 */
@JsonSerialize(using = EnumSerializer.class)
public enum RefundPhase implements ViewEnum {

    ON_SALE("售前退款"),

    AFTER_SALE("售后退款");

    public String value;

    RefundPhase(String value) {
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
