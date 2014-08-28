package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * Created by: codec.yang
 * Date: 2014/8/5 14:30
 */
public enum Satisfaction implements ViewEnum {

    BAD("不满意"),
    NEUTRAL("一般"),
    GOOD("满意");

    private String value;
    private Satisfaction(String value){
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
