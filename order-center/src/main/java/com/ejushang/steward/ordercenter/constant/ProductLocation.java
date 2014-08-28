package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 产品库位
 */
@JsonSerialize(using = EnumSerializer.class)
public enum ProductLocation implements ViewEnum {

    NORMAL("正常商品"),

    GIFT("赠品"),

    DEFECTIVE("缺件商品")
    ;

    public String value;

    ProductLocation(String value){
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


    /**
     * 根据值取枚举
     * @param value
     * @return
     */
    public static ProductLocation enumValueOf(String value) {
        if(value == null) {
            return null;
        }
        for(ProductLocation enumValue : values()) {
            if(value.equalsIgnoreCase(enumValue.value)) {
                return enumValue;
            }
        }
        return null;
    }

}
