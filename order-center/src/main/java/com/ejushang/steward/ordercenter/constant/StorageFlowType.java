package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 库存流水类型
 * User: liubin
 * Date: 13-12-30
 */
@JsonSerialize(using = EnumSerializer.class)
public enum StorageFlowType implements ViewEnum {

    INIT_STORAGE("初始化库存"),

    IN_STOCK("入库"),

    OUT_STOCK("出库");

    public String value;

    StorageFlowType(String value) {
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
