package com.ejushang.steward.ordercenter.constant;

/**
 * User: Baron.Zhang
 * Date: 2014/5/8
 * Time: 17:39
 */
public enum TbRefundPhase {
    ONSALE("售中","onsale"),
    AFTERSALE("售后","aftersale"),
    ;

    public String value;
    public String tbValue;

    TbRefundPhase(String value,String tbValue){
        this.value = value;
        this.tbValue = tbValue;
    }

}
