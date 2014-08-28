package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * Created by: codec.yang
 * Date: 2014/8/5 13:51
 */
public enum ReturnVisitType implements ViewEnum {

    SIGNED("签收回访"),
    AFTER_SALE("售后回访"),
    NEGATIVE_COMMENT("差评回访");

    private String value;
    private ReturnVisitType(String value){
        this.value=value;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
