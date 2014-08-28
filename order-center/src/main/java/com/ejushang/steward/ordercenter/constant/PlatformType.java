package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * User: Baron.Zhang
 * Date: 14-1-7
 * Time: 下午5:14
 */
@JsonSerialize(using = EnumSerializer.class)
public enum PlatformType implements ViewEnum {

    TAO_BAO("天猫"),
    JING_DONG("京东"),
    EJS("易居尚"),
    TM_GYS("天猫供应商"),
    TAO_BAO_2("淘宝"),
    JIAN_HANG("建行商城"),
    QQ_WG("QQ网购"),
    QQ_TG("QQ团购"),
    WEI_XIN("微信"),
    WEI_BO("微博"),
    ;


    public String value;

    PlatformType(String value) {
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
    public static PlatformType enumValueOf(String value) {
        if(value == null) {
            return null;
        }
        for(PlatformType enumValue : values()) {
            if(value.equalsIgnoreCase(enumValue.value)) {
                return enumValue;
            }
        }
        return null;
    }

}
