package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 订单退货状态
 * User: liubin
 * Date: 13-12-30
 */
@JsonSerialize(using = EnumSerializer.class)
public enum OrderReturnStatus implements ViewEnum {

    NORMAL("正常"),

    PART_RETURN("部分退货"),

    RETURNED("已退货");

    public String value;

    OrderReturnStatus(String value) {
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
