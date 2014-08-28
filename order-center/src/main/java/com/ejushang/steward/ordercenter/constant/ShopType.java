package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * User: Baron.Zhang
 * Date: 14-4-22
 * Time: 下午5:03
 */
@JsonSerialize(using = EnumSerializer.class)
public enum ShopType implements ViewEnum {
    SHOP("店铺"),
    VENDOR("供应商");

    public String value;

    ShopType(String value) {
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
