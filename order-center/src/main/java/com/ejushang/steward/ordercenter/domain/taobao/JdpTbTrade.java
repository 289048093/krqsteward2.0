package com.ejushang.steward.ordercenter.domain.taobao;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Baron.Zhang
 * Date: 14-2-27
 * Time: 上午11:21
 */
@javax.persistence.Table(name = "jdp_tb_trade")
@Entity
public class JdpTbTrade {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Column(name = "tid")
    @Id
    public Long tid;

    @Column(name="status")
    @Basic
    public String status;

    @Column(name="type")
    @Basic
    public String type;

    @Column(name="seller_nick")
    @Basic
    public String sellerNick;

    @Column(name="buyer_nick")
    @Basic
    public String buyerNick;

    @Column(name="created")
    @Basic
    public Date created;

    @Column(name="modified")
    @Basic
    public Date modified;

    @Column(name="jdp_created")
    @Basic
    public Date jdpCreated;

    @Column(name="jdp_modified")
    @Basic
    public Date jdpModified;

    @Column(name="jdp_hashcode")
    @Basic
    public String jdpHashcode;

    @Column(name="jdp_response")
    @Basic
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


}
