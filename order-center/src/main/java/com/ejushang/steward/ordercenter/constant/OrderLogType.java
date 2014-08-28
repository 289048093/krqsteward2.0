package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * User: JBoss.WU
 * Date: 14-8-6
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
public enum OrderLogType implements ViewEnum {
    PAYMENT("预收款订单"),

    ORIGINAL("原始订单"),

    CONFIRMED("确认订单"),

    REFUND("退款订单");

    public String value;

    OrderLogType(String value) {
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
