package com.ejushang.steward.ordercenter.constant;

/**
 * User: Baron.Zhang
 * Date: 2014/5/8
 * Time: 15:07
 */
public enum TbRefundStatus {
    WAIT_SELLER_AGREE("买家申请，等待卖家同意"),
    SELLER_REFUSE("卖家拒绝"),
    GOODS_RETURNING("退货中"),
    CLOSED("退款失败"),
    SUCCESS("退款成功")
    ;


    public String value;

    TbRefundStatus(String value){
        this.value = value;
    }

}
