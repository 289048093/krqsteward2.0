package com.ejushang.steward.ordercenter.vo;

/**
 * User: Baron.Zhang
 * Date: 2014/7/9
 * Time: 16:22
 */
public class OrderHandVo {

    private Integer originalOrderId;

    private String platformOrderNo;

    private String status;

    private String description;

    public Integer getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Integer originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
