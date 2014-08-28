package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * Created by: codec.yang
 * Date: 2014/8/15 16:09
 */
public enum SolutionType implements ViewEnum {

    SOLVED_BY_PHONE("已电话解决"),
    SOLVED_BY_AFTER_SALE("新建售后单")
    ;

    private String value;

    private SolutionType(String value){
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
