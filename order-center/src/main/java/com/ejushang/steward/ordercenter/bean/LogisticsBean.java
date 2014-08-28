package com.ejushang.steward.ordercenter.bean;

/**
 * User: Baron.Zhang
 * Date: 14-1-17
 * Time: 上午10:11
 */
public class LogisticsBean {

    /** 订单号 */
    private String orderNo;
    /** 原始订单号 */
    private String outOrderNo;
    /** 物流公司名 */
    private String expressCompany;
    /** 物流单号 */
    private String expressNo;
    /** 收货人地址 */
    private String receiveAddress;
    /** 店铺id */
    private Long shopId;
    /** 店铺对应的sessionKey */
    private String sessionKey;
    /** 外部平台 */
    private String outPlatform;
    /** 卖家昵称 **/
    private String sellerNick;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getOutPlatform() {
        return outPlatform;
    }

    public void setOutPlatform(String outPlatform) {
        this.outPlatform = outPlatform;
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public String toString() {
        return "LogisticsBean={" +
                "orderNo = " + orderNo + "," +
                "outOrderNo = " + outOrderNo + "," +
                "expressCompany = " + expressCompany + "," +
                "expressNo = " + expressNo +
                "}";
    }
}
