package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 原始退款状态枚举
 * User: Baron.Zhang
 * Date: 14-1-6
 * Time: 下午2:19
 */
@JsonSerialize(using = EnumSerializer.class)
public enum OriginalRefundStatus implements ViewEnum {

    WAIT_SELLER_AGREE("买家申请，等待卖家同意"),

    SELLER_REFUSE("卖家拒绝"),

    GOODS_RETURNING("退货中"),

    CLOSED("退款失败"),

    SUCCESS("退款成功");

    public String value;

    OriginalRefundStatus(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getValue() {
        return value;
    }

}