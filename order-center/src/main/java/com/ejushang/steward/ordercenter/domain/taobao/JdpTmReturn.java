package com.ejushang.steward.ordercenter.domain.taobao;

import java.util.Date;

/**
 * User: Baron.Zhang
 * Date: 14-2-27
 * Time: 上午11:43
 */
public class JdpTmReturn {
    public Long refundId;
    public String status;
    public String sid;
    public String refundPhase;
    public Long tid;
    public Long oid;
    public Date created;
    public Date modified;
    public Date jdpCreated;
    public Date jdpModified;
    public String jdpHashcode;
    public String jdpResponse;

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getRefundPhase() {
        return refundPhase;
    }

    public void setRefundPhase(String refundPhase) {
        this.refundPhase = refundPhase;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
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
