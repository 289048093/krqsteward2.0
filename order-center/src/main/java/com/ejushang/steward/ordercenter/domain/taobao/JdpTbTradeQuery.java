package com.ejushang.steward.ordercenter.domain.taobao;

import java.util.Date;

/**
 * User: Baron.Zhang
 * Date: 14-2-27
 * Time: 上午11:21
 */
public class JdpTbTradeQuery {
    public Long tid;
    public String status;
    public String type;
    public String sellerNick;
    public String buyerNick;
    public Date created;
    public Date startCreated;
    public Date endCreated;
    public Date modified;
    public Date jdpCreated;
    public Date startJdpCreated;
    public Date endJdpCreated;
    public Date jdpModified;
    public Date startJdpModified;
    public Date endJdpModified;
    public String jdpHashcode;
    public String jdpResponse;

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
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

    public Date getStartCreated() {
        return startCreated;
    }

    public void setStartCreated(Date startCreated) {
        this.startCreated = startCreated;
    }

    public Date getEndCreated() {
        return endCreated;
    }

    public void setEndCreated(Date endCreated) {
        this.endCreated = endCreated;
    }

    public Date getStartJdpModified() {
        return startJdpModified;
    }

    public void setStartJdpModified(Date startJdpModified) {
        this.startJdpModified = startJdpModified;
    }

    public Date getEndJdpModified() {
        return endJdpModified;
    }

    public void setEndJdpModified(Date endJdpModified) {
        this.endJdpModified = endJdpModified;
    }

    public Date getStartJdpCreated() {
        return startJdpCreated;
    }

    public void setStartJdpCreated(Date startJdpCreated) {
        this.startJdpCreated = startJdpCreated;
    }

    public Date getEndJdpCreated() {
        return endJdpCreated;
    }

    public void setEndJdpCreated(Date endJdpCreated) {
        this.endJdpCreated = endJdpCreated;
    }
}
