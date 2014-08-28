package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * User: Baron.Zhang
 * Date: 2014/5/28
 * Time: 14:57
 */
public enum TbReturnStatus implements ViewEnum{
    WAIT_BUYER_RETURN_GOODS("卖家同意退货"),
    WAIT_SELLER_CONFIRM_GOODS("买家已退款等待卖家确认"),
    CONFIRM_SUCCESS("卖家已同意退款"),
    CONFIRM_FAILED("卖家已拒绝退款")
    ;


    private String value;

    TbReturnStatus(String value){
        this.value = value;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
