package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * User: JBoss.WU
 * Date: 14-7-9
 * Time: 上午10:08
 * To change this template use File | Settings | File Templates.
 */
public class OrderAnalyzeLogVo {

    private String message;

    private Date createTime;

    private String processed;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }
}
