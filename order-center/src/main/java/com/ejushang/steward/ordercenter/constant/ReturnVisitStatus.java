package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;

/**
 * Created by: codec.yang
 * Date: 2014/8/5 13:57
 */
public enum ReturnVisitStatus implements ViewEnum {

    UNASSIGNED("待分配"),
    ASSIGNED("待回访"),
    SUCCEED("回访成功"),
    FAILED("回访失败"),
    COMPLETED("回访结束"),
    APPOINTMENT("已预约"),
    REJECTED("拒访");

    private String value;
    private ReturnVisitStatus(String value){
        this.value=value;
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
