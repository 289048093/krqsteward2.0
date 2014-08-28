package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.ordercenter.constant.PlatformType;

import java.util.Date;

/**
 * User:moon
 * Date: 14-6-4
 * Time: 下午4:57
 */
public class FinancialOrderVo {

    /**订单来自那个平台（如天猫，京东）*/
    private PlatformType platformType;
    /**店铺名称*/
    private String shopName;
    /**外部系统的订单号（如天猫）*/
    private String platformOrderNo;
    /**订单编号*/
    private String orderNo;
    /**订单id*/
    private Integer orderId;
    private FinancialOrderItemVo orderItemVo;
    /**下单时间*/
    private Date buyTime;
    /**付款时间*/
    private Date payTime;
    /**打印时间*/
    private Date printTime;
    /**发生日期*/
    private  Date happenTime;
    /**数据类型*/
    private String financialType;
    /** 整单优惠金额*/
    private String OrderSharedDiscountFee;
    /**邮费*/
    private String postFee;
    /**买家ID，即买家的淘宝号*/
    private String buyerId;
    /**收货人的姓名*/
    private String receiverName;
    /**物流公司*/
    private String shippingComp;
    /**物流编号*/
    private String shippingNo;

    private String repoName;

    private  String status;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 外部平台商品条形码（京东）
     */
    private String outerSku;

    public String getOuterSku() {
        return outerSku;
    }

    public void setOuterSku(String outerSku) {
        this.outerSku = outerSku;
    }

    public Date getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(Date happenTime) {
        this.happenTime = happenTime;
    }

    public String getFinancialType() {
        return financialType;
    }

    public void setFinancialType(String financialType) {
        this.financialType = financialType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public FinancialOrderItemVo getOrderItemVo() {
        return orderItemVo;
    }

    public void setOrderItemVo(FinancialOrderItemVo orderItemVo) {
        this.orderItemVo = orderItemVo;
    }

    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Date printTime) {
        this.printTime = printTime;
    }

    public String getOrderSharedDiscountFee() {
        return OrderSharedDiscountFee;
    }

    public void setOrderSharedDiscountFee(String orderSharedDiscountFee) {
        OrderSharedDiscountFee = orderSharedDiscountFee;
    }

    public String getPostFee() {
        return postFee;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getShippingComp() {
        return shippingComp;
    }

    public void setShippingComp(String shippingComp) {
        this.shippingComp = shippingComp;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }
}
