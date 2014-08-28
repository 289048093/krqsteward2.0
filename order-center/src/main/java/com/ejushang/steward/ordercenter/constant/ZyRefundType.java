package com.ejushang.steward.ordercenter.constant;

/**
 * User: Shiro
 * Date: 14-8-12
 * Time: 下午5:18
 */
public enum ZyRefundType {
    REFUND("退款","refund"),
    RETURN("退款退货","return");

    public String value;
    public String zyValue;

    ZyRefundType(String value,String zyValue){
        this.value = value;
        this.zyValue = zyValue;
    }
}
