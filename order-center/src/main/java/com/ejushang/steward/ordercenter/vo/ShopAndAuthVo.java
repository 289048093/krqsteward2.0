package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.ordercenter.constant.PlatformType;

import java.util.Date;

/**
 * User: Baron.Zhang
 * Date: 2014/5/13
 * Time: 17:17
 */
public class ShopAndAuthVo {
    /** 店铺id */
    private Integer shopId;

    /** 外部平台店铺id */
    private String outShopId;

    /** 店铺标题（名称） */
    private String title;

    /** 卖家用户id */
    private String userId;

    /** 卖家用户昵称 */
    private String sellerNick;

    /** sessionKey */
    private String sessionKey;

    /** refreshToken */
    private String refreshToken;

    /** 平台类型 */
    private PlatformType platformType;

    /** 店铺账号类型（店铺||供应商） */
    private String shopType;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getOutShopId() {
        return outShopId;
    }

    public void setOutShopId(String outShopId) {
        this.outShopId = outShopId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }
}
