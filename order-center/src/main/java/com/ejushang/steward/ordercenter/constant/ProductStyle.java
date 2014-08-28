package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 产品类型(316结构)
 */
@JsonSerialize(using = EnumSerializer.class)
public enum ProductStyle implements ViewEnum {

    //1
    A("A"),

    //3
    B("B"),

    //6
    C("C");

    public String value;

    ProductStyle(String value) {
        this.value = value;
    }

    /**
     * 根据值取枚举
     *
     * @param value
     * @return
     */
    public static ProductStyle enumValueOf(String value) {
        if (value == null) {
            return null;
        }
        for (ProductStyle enumValue : values()) {
            if (value.equalsIgnoreCase(enumValue.value)) {
                return enumValue;
            }
        }
        return null;
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
