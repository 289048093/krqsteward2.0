package com.ejushang.steward.ordercenter.domain.taobao;

import java.util.Date;

/**
 * User: Baron.Zhang
 * Date: 14-2-27
 * Time: 上午11:31
 */
public class JdpTbItem {
    public Long numIid;
    public String nick;
    public String approveStatus;
    public String cid;
    public String hasShowcase;
    public String hasDiscount;
    public Date created;
    public Date modified;
    public Date jdpCreated;
    public Date jdpModified;
    public Integer jdpDelete;
    public String jdpHashcode;
    public String jdpResponse;

    public Long getNumIid() {
        return numIid;
    }

    public void setNumIid(Long numIid) {
        this.numIid = numIid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getHasShowcase() {
        return hasShowcase;
    }

    public void setHasShowcase(String hasShowcase) {
        this.hasShowcase = hasShowcase;
    }

    public String getHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(String hasDiscount) {
        this.hasDiscount = hasDiscount;
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

    public Integer getJdpDelete() {
        return jdpDelete;
    }

    public void setJdpDelete(Integer jdpDelete) {
        this.jdpDelete = jdpDelete;
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
