package com.ejushang.steward.ordercenter.constant;

/**
 * User: Shiro
 * Date: 14-8-22
 * Time: 下午3:50
 */
public enum TbTradeRateRole {
    SELLER("卖家","seller"),
    BUYER("买家","buyer")
            ;

    public String value;
    public String tbValue;

    TbTradeRateRole(String value,String tbValue){
        this.value = value;
        this.tbValue = tbValue;
    }
}
