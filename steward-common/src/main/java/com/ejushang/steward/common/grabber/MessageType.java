package com.ejushang.steward.common.grabber;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 消息类型
 * User: liubin
 * Date: 13-12-30
 */
public enum MessageType {

    ORDER("订单"),

    REFUND("退款订单"),

    ORDER_ID("订单主键");


    public String value;

    MessageType(String value) {
        this.value = value;
    }

    /**
     * 使返回的json中枚举的值转换成String
     *
     * @return
     */
    @JsonValue
    public String getValue() {
        return this.value;
    }
}
