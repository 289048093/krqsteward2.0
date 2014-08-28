package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 预收款分配状态
 * User: liubin
 * Date: 13-12-30
 */
@JsonSerialize(using = EnumSerializer.class)
public enum PaymentAllocateStatus implements ViewEnum {

    WAIT_ALLOCATE("待分配"),

    ALLOCATED("已分配"),

    REFUND_ONSALE("售前退款");

    public String value;

    PaymentAllocateStatus(String value) {
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
