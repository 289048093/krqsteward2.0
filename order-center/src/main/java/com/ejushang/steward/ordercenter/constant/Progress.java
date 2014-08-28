package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * Created by: codec.yang
 * Date: 2014/8/15 10:31
 */
public enum Progress implements ViewEnum{
    WAITING("待处理"),
    PROCESSING("处理中"),
    COMPLETED("已处理")
    ;

    private String value;
    private Progress(String value){
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
