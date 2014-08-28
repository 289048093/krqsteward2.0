package com.ejushang.steward.ordercenter.constant;

/**
 * User: Baron.Zhang
 * Date: 2014/5/8
 * Time: 17:51
 */
public enum TbBillType {
    REFUND_BILL("退款单"),
    RETURN_BILL("退货单")
    ;

    public String value;

    TbBillType(String value){
        this.value = value;
    }

}
