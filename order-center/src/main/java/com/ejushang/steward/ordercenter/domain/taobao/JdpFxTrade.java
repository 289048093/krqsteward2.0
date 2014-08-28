package com.ejushang.steward.ordercenter.domain.taobao;

import java.util.Date;

/**
 * User: Baron.Zhang
 * Date: 14-2-27
 * Time: 上午11:38
 */
public class JdpFxTrade {
    public Long fenxiaoId;
    public String tcOrderId;
    public String status;
    public String supplierUsername;
    public String distributorUsername;
    public Date created;
    public Date modified;
    public Date jdpCreated;
    public Date jdpModified;
    public String jdpHashcode;
    public String jdpResponse;

    public Long getFenxiaoId() {
        return fenxiaoId;
    }

    public void setFenxiaoId(Long fenxiaoId) {
        this.fenxiaoId = fenxiaoId;
    }

    public String getTcOrderId() {
        return tcOrderId;
    }

    public void setTcOrderId(String tcOrderId) {
        this.tcOrderId = tcOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplierUsername() {
        return supplierUsername;
    }

    public void setSupplierUsername(String supplierUsername) {
        this.supplierUsername = supplierUsername;
    }

    public String getDistributorUsername() {
        return distributorUsername;
    }

    public void setDistributorUsername(String distributorUsername) {
        this.distributorUsername = distributorUsername;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getJdpCreated() {
        return jdpCreated;
    }

    public void setJdpCreated(Date jdpCreated) {
        this.jdpCreated = jdpCreated;
    }

    public Date getJdpModified() {
        return jdpModified;
    }

    public void setJdpModified(Date jdpModified) {
        this.jdpModified = jdpModified;
    }

    public String getJdpHashcode() {
        return jdpHashcode;
    }

    public void setJdpHashcode(String jdpHashcode) {
        this.jdpHashcode = jdpHashcode;
    }

    public String getJdpResponse() {
        return jdpResponse;
    }

    public void setJdpResponse(String jdpResponse) {
        this.jdpResponse = jdpResponse;
    }
}
