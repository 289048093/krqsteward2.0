package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * User: Baron.Zhang
 * Date: 2014/7/3
 * Time: 15:04
 */
public enum FetchOptType implements ViewEnum {
    AUTO("自动抓单"),
    MANUAL("手动设定时间自动抓单"),
    HAND("手动抓单")
    ;


    private String value;

    FetchOptType(String value){
        this.value = value;
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
