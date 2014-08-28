package com.ejushang.steward.ordercenter.constant;

/**
 * User: Shiro
 * Date: 14-8-22
 * Time: 下午3:53
 */
public enum TbRateType {
    GET("卖家","get"),
    GIVE("买家","give")
            ;

    public String value;
    public String tbValue;

    TbRateType(String value,String tbValue){
        this.value = value;
        this.tbValue = tbValue;
    }
}
