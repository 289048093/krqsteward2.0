package com.ejushang.steward.customercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 16:47
 */
public enum CommentResult implements ViewEnum {

    BAD("差评"),
    NEUTRAL("中评"),
    GOOD("好评");

    private String value;

    private CommentResult(String value){
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
