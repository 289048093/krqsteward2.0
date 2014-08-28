package com.ejushang.steward.ordercenter.bean;

import com.ejushang.steward.ordercenter.constant.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 14-1-18
 * Time: 下午1:09
 */
public class ShopBean {

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

    /** 店铺平台 */
    private Integer platformId;

    /** 平台类型 */
    private PlatformType platformType;

    /** 订单状态 */
    private String orderStatus;

    /** 退款单状态 */
    private String refundStatus;

    /** 退货单状态 */
    private String returnStatus;

    /** 店铺账号类型（店铺||供应商） */
    private ShopType shopType;

    //*********************用于抓单的参数字段****************//

    /** 订单抓取开始时间 */
    private Date fetchOrderStartDate;

    /** 订单抓取结束时间 */
    private Date fetchOrderEndDate;

    /** 退款单抓取开始时间 */
    private Date fetchRefundStartDate;
    /** 退款单抓取结束时间 */
    private Date fetchRefundEndDate;
    /**评价抓取开始时间*/
    private Date fetchTradeRateStartDate;
    /** 退款单抓取结束时间 */
    private Date fetchReturnEndDate;

    /** 每一个店铺每一次抓取只能有一个抓取操作类型（如AUTO、MANUAL、HAND） */
    private FetchOptType fetchOptType = FetchOptType.AUTO;

    /** 每一个店铺每一次可以抓取多种数据（如订单、退款单、退货单） */
    private List<FetchDataType> fetchDataTypeList = new ArrayList<FetchDataType>();

    /** 每个店铺的抓取参数 */
    private List<ShopFetchBean> shopFetchBeanList = new ArrayList<ShopFetchBean>();

    //*********************用于抓取评价的参数字段****************//

    /**评价抓取结束时间*/
    private Date fetchTradeRateEndDate;
    /** 退款单抓取开始时间 */
    private Date fetchReturnStartDate;

    private String rateType;

    private String role;

    private String result;

    private boolean useHasNext;

    private Integer platformOrderNo;

    private Integer numIid;

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

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public ShopType getShopType() {
        return shopType;
    }

    public void setShopType(ShopType shopType) {
        this.shopType = shopType;
    }

    public Date getFetchOrderStartDate() {
        return fetchOrderStartDate;
    }

    public void setFetchOrderStartDate(Date fetchOrderStartDate) {
        this.fetchOrderStartDate = fetchOrderStartDate;
    }

    public Date getFetchOrderEndDate() {
        return fetchOrderEndDate;
    }

    public void setFetchOrderEndDate(Date fetchOrderEndDate) {
        this.fetchOrderEndDate = fetchOrderEndDate;
    }

    public Date getFetchRefundStartDate() {
        return fetchRefundStartDate;
    }

    public void setFetchRefundStartDate(Date fetchRefundStartDate) {
        this.fetchRefundStartDate = fetchRefundStartDate;
    }

    public Date getFetchRefundEndDate() {
        return fetchRefundEndDate;
    }

    public void setFetchRefundEndDate(Date fetchRefundEndDate) {
        this.fetchRefundEndDate = fetchRefundEndDate;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    public Date getFetchReturnStartDate() {
        return fetchReturnStartDate;
    }

    public void setFetchReturnStartDate(Date fetchReturnStartDate) {
        this.fetchReturnStartDate = fetchReturnStartDate;
    }

    public Date getFetchReturnEndDate() {
        return fetchReturnEndDate;
    }

    public void setFetchReturnEndDate(Date fetchReturnEndDate) {
        this.fetchReturnEndDate = fetchReturnEndDate;
    }

    public Date getFetchTradeRateStartDate() {
        return fetchTradeRateStartDate;
    }

    public void setFetchTradeRateStartDate(Date fetchTradeRateStartDate) {
        this.fetchTradeRateStartDate = fetchTradeRateStartDate;
    }

    public Date getFetchTradeRateEndDate() {
        return fetchTradeRateEndDate;
    }

    public void setFetchTradeRateEndDate(Date fetchTradeRateEndDate) {
        this.fetchTradeRateEndDate = fetchTradeRateEndDate;
    }

    public FetchOptType getFetchOptType() {
        return fetchOptType;
    }

    public void setFetchOptType(FetchOptType fetchOptType) {
        this.fetchOptType = fetchOptType;
    }

    public List<FetchDataType> getFetchDataTypeList() {
        return fetchDataTypeList;
    }

    public void setFetchDataTypeList(List<FetchDataType> fetchDataTypeList) {
        this.fetchDataTypeList = fetchDataTypeList;
    }

    public List<ShopFetchBean> getShopFetchBeanList() {
        return shopFetchBeanList;
    }

    public void setShopFetchBeanList(List<ShopFetchBean> shopFetchBeanList) {
        this.shopFetchBeanList = shopFetchBeanList;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isUseHasNext() {
        return useHasNext;
    }

    public void setUseHasNext(boolean useHasNext) {
        this.useHasNext = useHasNext;
    }

    public Integer getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(Integer platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    public Integer getNumIid() {
        return numIid;
    }

    public void setNumIid(Integer numIid) {
        this.numIid = numIid;
    }

    @Override
    public String toString() {
        return "ShopBean{" +
                "shopId=" + shopId +
                ", outShopId='" + outShopId + '\'' +
                ", title='" + title + '\'' +
                ", userId='" + userId + '\'' +
                ", sellerNick='" + sellerNick + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", platformId=" + platformId +
                ", platformType=" + platformType +
                ", orderStatus='" + orderStatus + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", returnStatus='" + returnStatus + '\'' +
                ", shopType=" + shopType +
                ", fetchOrderStartDate=" + fetchOrderStartDate +
                ", fetchOrderEndDate=" + fetchOrderEndDate +
                ", fetchRefundStartDate=" + fetchRefundStartDate +
                ", fetchRefundEndDate=" + fetchRefundEndDate +
                ", fetchTradeRateStartDate=" + fetchTradeRateStartDate +
                ", fetchReturnEndDate=" + fetchReturnEndDate +
                ", fetchOptType=" + fetchOptType +
                ", fetchDataTypeList=" + fetchDataTypeList +
                ", shopFetchBeanList=" + shopFetchBeanList +
                ", fetchTradeRateEndDate=" + fetchTradeRateEndDate +
                ", fetchReturnStartDate=" + fetchReturnStartDate +
                ", rateType='" + rateType + '\'' +
                ", role='" + role + '\'' +
                ", result='" + result + '\'' +
                ", useHasNext=" + useHasNext +
                '}';
    }
}
