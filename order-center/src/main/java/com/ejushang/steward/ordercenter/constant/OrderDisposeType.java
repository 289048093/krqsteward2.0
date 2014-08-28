package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 订单拆分合并类型
 * User: liubin
 * Date: 13-12-30
 */
@JsonSerialize(using = EnumSerializer.class)
public enum OrderDisposeType implements ViewEnum {

    SPLIT("拆分"),

    MERGE("合并");

    public String value;

    OrderDisposeType(String value) {
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
