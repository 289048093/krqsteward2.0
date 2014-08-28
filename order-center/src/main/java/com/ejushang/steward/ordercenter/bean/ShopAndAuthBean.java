package com.ejushang.steward.ordercenter.bean;

import com.ejushang.steward.ordercenter.constant.PlatformType;

/**
 * User: Baron.Zhang
 * Date: 14-4-9
 * Time: 下午3:31
 */
public class ShopAndAuthBean {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String expiresIn;
    private String reExpiresIn;
    private String r1ExpiresIn;
    private String r2ExpiresIn;
    private String w1ExpiresIn;
    private String w2ExpiresIn;
    private String userNick;
    private String userId;
    private String subUserNick;
    private String subUserId;
    private PlatformType platformType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getReExpiresIn() {
        return reExpiresIn;
    }

    public void setReExpiresIn(String reExpiresIn) {
        this.reExpiresIn = reExpiresIn;
    }

    public String getR1ExpiresIn() {
        return r1ExpiresIn;
    }

    public void setR1ExpiresIn(String r1ExpiresIn) {
        this.r1ExpiresIn = r1ExpiresIn;
    }

    public String getR2ExpiresIn() {
        return r2ExpiresIn;
    }

    public void setR2ExpiresIn(String r2ExpiresIn) {
        this.r2ExpiresIn = r2ExpiresIn;
    }

    public String getW1ExpiresIn() {
        return w1ExpiresIn;
    }

    public void setW1ExpiresIn(String w1ExpiresIn) {
        this.w1ExpiresIn = w1ExpiresIn;
    }

    public String getW2ExpiresIn() {
        return w2ExpiresIn;
    }

    public void setW2ExpiresIn(String w2ExpiresIn) {
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

    public String getSubUserNick() {
        return subUserNick;
    }

    public void setSubUserNick(String subUserNick) {
        this.subUserNick = subUserNick;
    }

    public String getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(String subUserId) {
        this.subUserId = subUserId;
    }

    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }
}
