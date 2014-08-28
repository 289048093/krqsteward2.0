package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 售后单状态
 * @Author Channel
 * @Date ${date}
*/
@JsonSerialize(using = EnumSerializer.class)
public enum AfterSalesStatus implements ViewEnum {

    SAVE("处理中"),
    CHECK("待审批"),
    ACCEPT("审批通过"),
    REJECT("审批驳回"),
    FINISH("已结束"),
    CANCEL("已作废");

    public String value;

    AfterSalesStatus(String value) {
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