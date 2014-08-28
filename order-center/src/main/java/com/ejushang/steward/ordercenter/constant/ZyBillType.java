package com.ejushang.steward.ordercenter.constant;

/**
 * User: Shiro
 * Date: 14-8-12
 * Time: 下午5:24
 */
public enum ZyBillType {
    REFUND_BIL("退款单","refund_bil"),
    RETURN_BILL("退货单","return_bill"),
    ;

    public String value;
    public String tbValue;

    ZyBillType(String value,String tbValue){
        this.value = value;
        this.tbValue = tbValue;
    }
}
