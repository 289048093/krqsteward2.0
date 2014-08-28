package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * User: Shiro
 * Date: 14-8-12
 * Time: 下午5:10
 */
public enum ZyRefundStatus implements ViewEnum {
    WAIT_SELLER_AGREE("买家申请，等待卖家同意"),
    SELLER_REFUSE("卖家拒绝"),
    GOODS_RETURNING("退货中"),
    CLOSED("退款失败"),
    SUCCESS("退款成功")
            ;


    public String value;

    ZyRefundStatus(String value){
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
