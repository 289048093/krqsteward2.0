package com.ejushang.steward.ordercenter.constant;

/**
 * User: Baron.Zhang
 * Date: 2014/5/15
 * Time: 15:51
 */
public enum JdRefundStatus {
    PENDING_APPROVAL("0","待审核"),
    MERCHANT_APPROVAL_PASS("1","商家审核通过"),
    MERCHANT_APPROVAL_NO_PASS("2","商家审核不通过"),
    FINANCE_APPROVAL_PASS("3","财务审核通过"),
    FINANCE_APPROVAL_NO_PASS("4","财务审核不通过"),
    MANUAL_APPROVAL_PASS("5","人工审核通过")
    ;

    private String value;
    private String desc;

    JdRefundStatus(String value,String desc){
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
