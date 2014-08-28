package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.ordercenter.constant.OnOffLine;

import java.util.Date;

/**
 * 确认支付实体
 * @Author Channel
 * @Date 2014/8/7
 * @Version: 1.0
 */
public class ConfirmPaymentVo {

    /**
     * 售后单ID
     */
    private Integer afterSalesId;

    /**
     * OnOffLine:线上线下状态;ONLINE:线上,OFFLINE:线下;
     */
    private OnOffLine onofflineType;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 退款备注信息
     */
    private String remarks;

    /**
     * 原退款单ID
     */
    private Integer refundId;

    public OnOffLine getOnofflineType() {
        return onofflineType;
    }

    public void setOnofflineType(OnOffLine onofflineType) {
        this.onofflineType = onofflineType;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getRefundId() {
        return refundId;
    }

    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }

    public Integer getAfterSalesId() {
        return afterSalesId;
    }

    public void setAfterSalesId(Integer afterSalesId) {
        this.afterSalesId = afterSalesId;
    }
}
