package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * Created by: codec.yang
 * Date: 2014/8/5 14:19
 */
public enum DialStatus implements ViewEnum{
    CONNECTED("接通"),
    REJECTED("拒访"),
    DISCONNECTED("无法接通"),
    NO_ANSWER("无人接听"),
    EMPTY_NUMBER("空号"),
    CLOSED("关机");

    private String value;
    private DialStatus(String value){
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
