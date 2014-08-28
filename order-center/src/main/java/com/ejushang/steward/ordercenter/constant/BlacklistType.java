package com.ejushang.steward.ordercenter.constant;


import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * Created by: codec.yang
 * Date: 2014/8/5 9:09
 */
public enum  BlacklistType implements ViewEnum {
    SMS("短信黑名单"),
    MAIL("邮件黑名单"),
    PHONE("电话黑名单");

    private String value;
    private  BlacklistType(String value){
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
