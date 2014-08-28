package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.OrderItemReturnStatus;
import com.ejushang.steward.ordercenter.constant.OrderItemStatus;
import com.ejushang.steward.ordercenter.constant.OrderItemType;
import com.ejushang.steward.ordercenter.constant.PostPayer;
import com.ejushang.steward.ordercenter.domain.OrderApprove;

import java.util.List;

/**
 * User: tin
 * Date: 14-5-8
 * Time: 下午5:29
 */
public class OrderItemVo {
    //智库城订单编号
    private Integer id;
    //外部订单编号
    private String platformSubOrderNo;
    //订单项类型
    private String type;
    //订单项状态
    private String status;
    //线上退货状态
    private OrderItemReturnStatus returnStatus;
    //线下退货状态
    private OrderItemReturnStatus offlineReturnStatus;
    //售前换货状态
    private Boolean exchanged=false;
    //换货订单描述
    private String exchangedGoods;
    //商品编号
    private String productCode="";
    //商品名称
    private String productName;
    //sku
    private String productSku;
    //品牌
    private String brandName;
    //类别
    private String cateName;
    //原价（一口价）
    private String price="0.00";
    //促销价
    private String discountPrice="0.00";
    //订货数量
    private String buyCount="1";
    //库存
    private String repoNum="0";
    //订单项优惠金额
    private String discountFee="0.00";
    //分摊优惠金额
    private String sharedDiscountFee="0.00";
    //分摊邮费
    private String sharedPostFee="0.00";
    //平台结算金额
    private String actualFee="0.00";
    //真正实付金额
    private String userActualPrice="0.00";
    //邮费补差金额
    private String postCoverFee="0.00";
    //邮费补差退款金额
    private String postCoverRefundFee="0.00";
    //服务补差金额
    private String serviceCoverFee="0.00";
    //服务补差退款金额
    private String serviceCoverRefundFee="0.00";
    //线上退款金额
    private String refundFee="0.00";
    //线下退款金额
    private String offlineRefundFee="0.00";
    //线上退货邮费
    private String returnPostFee="0.00";
    //线上退货邮费承担方
    private PostPayer returnPostPayer;
    //线下退货邮费
    private String offlineReturnPostFee;
    //线下退货邮费承担方
    private PostPayer offlineReturnPostPayer;
    //线下换货邮费
    private String exchangePostFee="0.00";
    //线下换货邮费承担方
    private PostPayer exchangePostPayer;
    //货款
    private Money goodsFee=Money.valueOf("0.00");
    //价格描述
    private String priceDescription;
    //预收款分配
    private String feesString;
    //预收款退款分配
    private String refundFeesString;
    /** 被换货的OrderItemId */
    private Integer exchangeSourceId;
   //规格
    private String specInfo;

    private String color;

    private Money postFee=Money.valueOf("0.00");

    public Money getPostFee() {
        return postFee;
    }

    public void setPostFee(Money postFee) {
        this.postFee = postFee;
    }

    /**
     * 外部平台商品条形码（京东）
     */
    private String outerSku;

    public String getUserActualPrice() {
        return userActualPrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setUserActualPrice(String userActualPrice) {
        this.userActualPrice = userActualPrice;
    }

    public String getOuterSku() {
        return outerSku;
    }

    public void setOuterSku(String outerSku) {
        this.outerSku = outerSku;
    }

    public String getSpecInfo() {
        return specInfo;
    }

    public void setSpecInfo(String specInfo) {
        this.specInfo = specInfo;
    }

    public Integer getExchangeSourceId() {
        return exchangeSourceId;
    }

    public void setExchangeSourceId(Integer exchangeSourceId) {
        this.exchangeSourceId = exchangeSourceId;
    }

    public String getFeesString() {
        return feesString;
    }

    public void setFeesString(String feesString) {
        this.feesString = feesString;
    }

    public String getRefundFeesString() {
        return refundFeesString;
    }

    public void setRefundFeesString(String refundFeesString) {
        this.refundFeesString = refundFeesString;
    }

    private String buyerAlipayNo;

    //支付宝账号
    public String getBuyerAlipayNo() {
        return buyerAlipayNo;
    }

    public void setBuyerAlipayNo(String buyerAlipayNo) {
        this.buyerAlipayNo = buyerAlipayNo;
    }

    public Boolean getExchanged() {
        return exchanged;
    }

    public void setExchanged(Boolean exchanged) {
        this.exchanged = exchanged;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlatformSubOrderNo() {
        return platformSubOrderNo;
    }

    public void setPlatformSubOrderNo(String platformSubOrderNo) {
        this.platformSubOrderNo = platformSubOrderNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getExchangedGoods() {
        return exchangedGoods;
    }

    public void setExchangedGoods(String exchangedGoods) {
        this.exchangedGoods = exchangedGoods;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }

    public String getRepoNum() {
        return repoNum;
    }

    public void setRepoNum(String repoNum) {
        this.repoNum = repoNum;
    }

    public String getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(String discountFee) {
        this.discountFee = discountFee;
    }

    public String getSharedDiscountFee() {
        return sharedDiscountFee;
    }

    public void setSharedDiscountFee(String sharedDiscountFee) {
        this.sharedDiscountFee = sharedDiscountFee;
    }

    public String getSharedPostFee() {
        return sharedPostFee;
    }

    public void setSharedPostFee(String sharedPostFee) {
        this.sharedPostFee = sharedPostFee;
    }

    public String getActualFee() {
        return actualFee;
    }

    public void setActualFee(String actualFee) {
        this.actualFee = actualFee;
    }

    public String getPostCoverFee() {
        return postCoverFee;
    }

    public void setPostCoverFee(String postCoverFee) {
        this.postCoverFee = postCoverFee;
    }

    public String getPostCoverRefundFee() {
        return postCoverRefundFee;
    }

    public void setPostCoverRefundFee(String postCoverRefundFee) {
        this.postCoverRefundFee = postCoverRefundFee;
    }

    public String getServiceCoverFee() {
        return serviceCoverFee;
    }

    public void setServiceCoverFee(String serviceCoverFee) {
        this.serviceCoverFee = serviceCoverFee;
    }

    public String getServiceCoverRefundFee() {
        return serviceCoverRefundFee;
    }

    public void setServiceCoverRefundFee(String serviceCoverRefundFee) {
        this.serviceCoverRefundFee = serviceCoverRefundFee;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getOfflineRefundFee() {
        return offlineRefundFee;
    }

    public void setOfflineRefundFee(String offlineRefundFee) {
        this.offlineRefundFee = offlineRefundFee;
    }

    public String getReturnPostFee() {
        return returnPostFee;
    }

    public void setReturnPostFee(String returnPostFee) {
        this.returnPostFee = returnPostFee;
    }


    public String getOfflineReturnPostFee() {
        return offlineReturnPostFee;
    }

    public void setOfflineReturnPostFee(String offlineReturnPostFee) {
        this.offlineReturnPostFee = offlineReturnPostFee;
    }


    public String getExchangePostFee() {
        return exchangePostFee;
    }

    public void setExchangePostFee(String exchangePostFee) {
        this.exchangePostFee = exchangePostFee;
    }

    public OrderItemReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(OrderItemReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }

    public OrderItemReturnStatus getOfflineReturnStatus() {
        return offlineReturnStatus;
    }

    public void setOfflineReturnStatus(OrderItemReturnStatus offlineReturnStatus) {
        this.offlineReturnStatus = offlineReturnStatus;
    }

    public PostPayer getReturnPostPayer() {
        return returnPostPayer;
    }

    public void setReturnPostPayer(PostPayer returnPostPayer) {
        this.returnPostPayer = returnPostPayer;
    }

    public PostPayer getOfflineReturnPostPayer() {
        return offlineReturnPostPayer;
    }

    public void setOfflineReturnPostPayer(PostPayer offlineReturnPostPayer) {
        this.offlineReturnPostPayer = offlineReturnPostPayer;
    }

    public PostPayer getExchangePostPayer() {
        return exchangePostPayer;
    }

    public void setExchangePostPayer(PostPayer exchangePostPayer) {
        this.exchangePostPayer = exchangePostPayer;
    }

    public Money getGoodsFee() {
        return goodsFee;
    }

    public void setGoodsFee(Money goodsFee) {
        this.goodsFee = goodsFee;
    }

    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }


}
