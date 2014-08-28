package com.ejushang.steward.common.domain.util;

import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-12
 */
public interface OperableData {

    void setCreateTime(Date createTime);

    Date getCreateTime();

    void setUpdateTime(Date updateTime);

    Date getUpdateTime();

    void setOperatorId(Integer operatorId);

    Integer getOperatorId();


}
