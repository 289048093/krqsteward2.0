package com.ejushang.steward.ordercenter.constant;

/**
 * User: Shiro
 * Date: 14-8-12
 * Time: 下午5:12
 */
public enum ZyDateType {
    CREATED("创建时间","created"),
    MODIFIED("更新时间","modified");

    public String value;
    public String zyValue;

    ZyDateType(String value,String zyValue){
        this.value = value;
        this.zyValue = zyValue;
    }
}
