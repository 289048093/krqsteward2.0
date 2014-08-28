package com.ejushang.steward.ordercenter.bean;

import java.util.Date;

/**
 * User: Baron.Zhang
 * Date: 14-1-6
 * Time: 下午5:24
 */
public class ShopAuthQuery {
    /** 主键id */
    private String id;

    /** 外部平台店铺id */
    private String shopId;

    /** 店铺对应的session key(即Access Token) */
    private String sessionKey;

    /** Access token的类型目前只支持bearer */
    private String tokenType;

    /** Access token过期时间 */
    private Long expiresIn;

    /** Refresh token */
    private String refreshToken;

    /** Refresh token过期时间 */
    private Long reExpiresIn;

    /** r1级别API或字段的访问过期时间 */
    private Long r1ExpiresIn;

    /** r2级别API或字段的访问过期时间 */
    private Long r2ExpiresIn;

    /** w1级别API或字段的访问过期时间 */
    private Long w1ExpiresIn;

    /** w2级别API或字段的访问过期时间 */
    private Long w2ExpiresIn;

    /** 外部平台账号昵称 */
    private String userNick;

    /** 外部平台帐号对应id */
    private String userId;

    /** 当前授权用户来自哪个平台（如天猫，京东） */
    private String outPlatformType;

    /** Session key第一次授权时间 */
    private Date createTime;

    /** Session key最后修改时间 */
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getReExpiresIn() {
        return reExpiresIn;
    }

    public void setReExpiresIn(Long reExpiresIn) {
        this.reExpiresIn = reExpiresIn;
    }

    public Long getR1ExpiresIn() {
        return r1ExpiresIn;
    }

    public void setR1ExpiresIn(Long r1ExpiresIn) {
        this.r1ExpiresIn = r1ExpiresIn;
    }

    public Long getR2ExpiresIn() {
        return r2ExpiresIn;
    }

    public void setR2ExpiresIn(Long r2ExpiresIn) {
        this.r2ExpiresIn = r2ExpiresIn;
    }

    public Long getW1ExpiresIn() {
        return w1ExpiresIn;
    }

    public void setW1ExpiresIn(Long w1ExpiresIn) {
        this.w1ExpiresIn = w1ExpiresIn;
    }

    public Long getW2ExpiresIn() {
        return w2ExpiresIn;
    }

    public void setW2ExpiresIn(Long w2ExpiresIn) {
        this.w2ExpiresIn = w2ExpiresIn;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOutPlatformType() {
        return outPlatformType;
    }

    public void setOutPlatformType(String outPlatformType) {
        this.outPlatformType = outPlatformType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
