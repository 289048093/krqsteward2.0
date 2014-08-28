package com.ejushang.steward.ordercenter.constant;

/**
 * User: Shiro
 * Date: 14-8-12
 * Time: 下午5:23
 */
public enum ZyRefundPhase {
    ONSALE("售中","onsale"),
    AFTERSALE("售后","aftersale"),
    ;

    public String value;
    public String tbValue;

    ZyRefundPhase(String value,String tbValue){
        this.value = value;
        this.tbValue = tbValue;
    }
}
