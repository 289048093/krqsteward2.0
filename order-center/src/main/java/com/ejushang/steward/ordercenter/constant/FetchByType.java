package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * User: Baron.Zhang
 * Date: 2014/5/28
 * Time: 16:47
 */
public enum FetchByType implements ViewEnum{
    FETCH_BY_DATE("根据时间段查询"),
    FETCH_BY_PLATFORM_NOS("根据编号查询")
    ;


    private String value;

    FetchByType(String value){
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
