package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 订单明细类型
 * User: liubin
 * Date: 13-12-30
 */
@JsonSerialize(using = EnumSerializer.class)
public enum OrderItemType implements ViewEnum {

    PRODUCT("商品"),

    MEALSET("套餐商品"),

    GIFT("赠品"),

    REPLENISHMENT("补货"),

    EXCHANGE_AFTERSALE("售后换货");

    public String value;

    OrderItemType(String value) {
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
