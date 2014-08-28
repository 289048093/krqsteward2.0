package com.ejushang.steward.ordercenter.constant;

/**
 * User: Shiro
 * Date: 14-8-22
 * Time: 下午3:32
 */
public enum TbTradeRateResult {
    good("售中","good"),
    neutral("售后","neutral"),
    bad("售后","bad")
    ;

    public String value;
    public String tbValue;

    TbTradeRateResult(String value,String tbValue){
        this.value = value;
        this.tbValue = tbValue;
    }

}