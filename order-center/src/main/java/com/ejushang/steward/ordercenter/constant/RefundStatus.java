package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 退款状态
 * User: liubin
 * Date: 13-12-30
 */
@JsonSerialize(using = EnumSerializer.class)
public enum RefundStatus implements ViewEnum {

    IN_PROCESS("正在申请"),

    SUCCESS("成功"),

    CLOSED("失败");


    public String value;

    RefundStatus(String value) {
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
