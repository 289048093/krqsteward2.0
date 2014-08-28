package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 订单生成类型
 * User: liubin
 * Date: 13-12-30
 */
@JsonSerialize(using = EnumSerializer.class)
public enum OrderGenerateType implements ViewEnum {

    MANUAL_CREATE("手动创建"),

    MANUAL_SPLIT("手动拆分"),

    MANUAL_MERGE("手动合并"),

    AUTO_CREATE("自动抓单"),

    AUTO_SPLIT("自动拆分"),

    AUTO_MERGE("自动合并");


    public String value;

    OrderGenerateType(String value) {
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
