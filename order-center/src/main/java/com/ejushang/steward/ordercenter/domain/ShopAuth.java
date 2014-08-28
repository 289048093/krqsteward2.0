package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.ordercenter.constant.PlatformType;

import javax.persistence.*;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_shop_auth")
@Entity
public class ShopAuth implements EntityClass<Integer>, OperableData {

    private Integer id;

    private String sessionKey;

    private String tokenType;

    private String expiresIn;

    private String refreshToken;

    private String reExpiresIn;

    private String r1ExpiresIn;

    private String r2ExpiresIn;

    private String w1ExpiresIn;

    private String w2ExpiresIn;

    private String userNick;

    private String userId;

    private PlatformType platformType;

    private java.util.Date createTime;

    private java.util.Date updateTime;

    private Boolean isDelete = false;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "session_key")
    @Basic
    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }


    @javax.persistence.Column(name = "token_type")
    @Basic
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }


    @javax.persistence.Column(name = "expires_in")
    @Basic
    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }


    @javax.persistence.Column(name = "refresh_token")
    @Basic
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    @javax.persistence.Column(name = "re_expires_in")
    @Basic
    public String getReExpiresIn() {
        return reExpiresIn;
    }

    public void setReExpiresIn(String reExpiresIn) {
        this.reExpiresIn = reExpiresIn;
    }


    @javax.persistence.Column(name = "r1_expires_in")
    @Basic
    public String getR1ExpiresIn() {
        return r1ExpiresIn;
    }

    public void setR1ExpiresIn(String r1ExpiresIn) {
        this.r1ExpiresIn = r1ExpiresIn;
    }


    @javax.persistence.Column(name = "r2_expires_in")
    @Basic
    public String getR2ExpiresIn() {
        return r2ExpiresIn;
    }

    public void setR2ExpiresIn(String r2ExpiresIn) {
        this.r2ExpiresIn = r2ExpiresIn;
    }


    @javax.persistence.Column(name = "w1_expires_in")
    @Basic
    public String getW1ExpiresIn() {
        return w1ExpiresIn;
    }

    public void setW1ExpiresIn(String w1ExpiresIn) {
        this.w1ExpiresIn = w1ExpiresIn;
    }


    @javax.persistence.Column(name = "w2_expires_in")
    @Basic
    public String getW2ExpiresIn() {
        return w2ExpiresIn;
    }

    public void setW2ExpiresIn(String w2ExpiresIn) {
        this.w2ExpiresIn = w2ExpiresIn;
    }


    @javax.persistence.Column(name = "user_nick")
    @Basic
    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }


    @javax.persistence.Column(name = "user_id")
    @Basic
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @javax.persistence.Column(name = "platform_type")
    @Enumerated(EnumType.STRING)
    @Basic
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }


    @javax.persistence.Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setOperatorId(Integer operatorId) {
    }

    @Override
    @Transient
    public Integer getOperatorId() {
        return null;
    }

    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);
    }

    @Column(name = "is_delete")
    @Basic
    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "ShopAuth{" +
                "id=" + id +
                ", sessionKey='" + sessionKey + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", reExpiresIn='" + reExpiresIn + '\'' +
                ", r1ExpiresIn='" + r1ExpiresIn + '\'' +
                ", r2ExpiresIn='" + r2ExpiresIn + '\'' +
                ", w1ExpiresIn='" + w1ExpiresIn + '\'' +
                ", w2ExpiresIn='" + w2ExpiresIn + '\'' +
                ", userNick='" + userNick + '\'' +
                ", userId='" + userId + '\'' +
                ", platformType=" + platformType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
