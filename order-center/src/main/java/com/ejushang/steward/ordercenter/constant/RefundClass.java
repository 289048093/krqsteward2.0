package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 退款类型
 * @Author Channel
 * @Date ${date}
*/
@JsonSerialize(using = EnumSerializer.class)
public enum RefundClass implements ViewEnum {

    GOODS("货款"),
    POST("运费"),
    REFUND_POST("退款运费");

    public String value;

    RefundClass(String value) {
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