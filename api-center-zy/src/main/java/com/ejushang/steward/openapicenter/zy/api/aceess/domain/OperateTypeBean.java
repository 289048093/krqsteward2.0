package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

import java.util.Date;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-27
 * Time: 上午11:16
 */
public abstract class OperateTypeBean  extends ZiYouObject{

    private  OperateType type;
    /**
     * 添加/修改/删除时间
     */
    @ApiField("operate_time")
    private Date operateTime;

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public OperateType getType() {
        return type;
    }

    public void setType(OperateType type) {
        this.type = type;
    }
}
