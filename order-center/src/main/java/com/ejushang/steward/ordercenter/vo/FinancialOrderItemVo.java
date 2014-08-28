package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.OrderItemReturnStatus;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.constant.PostPayer;
import com.ejushang.steward.ordercenter.util.CalculableOrderItem;

/**
 * User:moon
 * Date: 14-7-30
 * Time: 下午4:55
 */
public class FinancialOrderItemVo  implements  CalculableOrderItem {

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
    private String productCode;
    //商品名称
    private String productName;
    //sku
    private String productSku;
    //品牌
    private String brandName;
    //类别
    private String cateName;
    //原价（一口价）
    private Money price=Money.valueOf(0);
    //促销价
    private Money discountPrice=Money.valueOf(0);
    //订货数量
    private Integer buyCount;
    //库存
    private String repoNum;
    //订单项优惠金额
    private Money discountFee=Money.valueOf(0);
    //分摊优惠金额
    private Money sharedDiscountFee=Money.valueOf(0);
    //分摊邮费
    private Money sharedPostFee=Money.valueOf(0);
    //平台结算金额
    private Money actualFee=Money.valueOf(0);
    //真正实付金额
    private Money userActualPrice=Money.valueOf(0);
    //邮费补差金额
    private Money postCoverFee=Money.valueOf(0);
    //邮费补差退款金额
    private Money postCoverRefundFee=Money.valueOf(0);
    //服务补差金额
    private Money serviceCoverFee=Money.valueOf(0);
    //服务补差退款金额
    private Money serviceCoverRefundFee=Money.valueOf(0);
    //线上退款金额
    private Money refundFee=Money.valueOf(0);
    //线下退款金额
    private Money offlineRefundFee=Money.valueOf(0);
    //线上退货邮费
    private Money returnPostFee=Money.valueOf(0);
    private Money actualRefundFee=Money.valueOf(0);
    //线上退货邮费承担方
    private PostPayer returnPostPayer;
    //线下退货邮费
    private Money offlineReturnPostFee=Money.valueOf(0);
    //线下退货邮费承担方
    private PostPayer offlineReturnPostPayer;
    //线下换货邮费
    private Money exchangePostFee=Money.valueOf(0);
    //线下换货邮费承担方
    private PostPayer exchangePostPayer;
    //货款
    private Money goodsFee=Money.valueOf(0);
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

    private PlatformType platformType;

    private Money postFee=Money.valueOf(0);

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

    public Boolean getExchanged() {
        return exchanged;
    }

    public void setExchanged(Boolean exchanged) {
        this.exchanged = exchanged;
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

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public Money getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Money discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public String getRepoNum() {
        return repoNum;
    }

    public void setRepoNum(String repoNum) {
        this.repoNum = repoNum;
    }

    public Money getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Money discountFee) {
        this.discountFee = discountFee;
    }

    public Money getSharedDiscountFee() {
        return sharedDiscountFee;
    }

    public void setSharedDiscountFee(Money sharedDiscountFee) {
        this.sharedDiscountFee = sharedDiscountFee;
    }

    public Money getSharedPostFee() {
        return sharedPostFee;
    }

    public void setSharedPostFee(Money sharedPostFee) {
        this.sharedPostFee = sharedPostFee;
    }

    public Money getActualFee() {
        return actualFee;
    }

    public void setActualFee(Money actualFee) {
        this.actualFee = actualFee;
    }

    public Money getUserActualPrice() {
        return userActualPrice;
    }

    public void setUserActualPrice(Money userActualPrice) {
        this.userActualPrice = userActualPrice;
    }

    public Money getPostCoverFee() {
        return postCoverFee;
    }

    public void setPostCoverFee(Money postCoverFee) {
        this.postCoverFee = postCoverFee;
    }

    public Money getPostCoverRefundFee() {
        return postCoverRefundFee;
    }

    public void setPostCoverRefundFee(Money postCoverRefundFee) {
        this.postCoverRefundFee = postCoverRefundFee;
    }

    public Money getServiceCoverFee() {
        return serviceCoverFee;
    }

    public void setServiceCoverFee(Money serviceCoverFee) {
        this.serviceCoverFee = serviceCoverFee;
    }

    public Money getServiceCoverRefundFee() {
        return serviceCoverRefundFee;
    }

    public void setServiceCoverRefundFee(Money serviceCoverRefundFee) {
        this.serviceCoverRefundFee = serviceCoverRefundFee;
    }

    public Money getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Money refundFee) {
        this.refundFee = refundFee;
    }

    public Money getOfflineRefundFee() {
        return offlineRefundFee;
    }

    public void setOfflineRefundFee(Money offlineRefundFee) {
        this.offlineRefundFee = offlineRefundFee;
    }

    public Money getReturnPostFee() {
        return returnPostFee;
    }

    public void setReturnPostFee(Money returnPostFee) {
        this.returnPostFee = returnPostFee;
    }

    public Money getActualRefundFee() {
        return actualRefundFee;
    }

    public void setActualRefundFee(Money actualRefundFee) {
        this.actualRefundFee = actualRefundFee;
    }

    public PostPayer getReturnPostPayer() {
        return returnPostPayer;
    }

    public void setReturnPostPayer(PostPayer returnPostPayer) {
        this.returnPostPayer = returnPostPayer;
    }

    public Money getOfflineReturnPostFee() {
        return offlineReturnPostFee;
    }

    public void setOfflineReturnPostFee(Money offlineReturnPostFee) {
        this.offlineReturnPostFee = offlineReturnPostFee;
    }

    public PostPayer getOfflineReturnPostPayer() {
        return offlineReturnPostPayer;
    }

    public void setOfflineReturnPostPayer(PostPayer offlineReturnPostPayer) {
        this.offlineReturnPostPayer = offlineReturnPostPayer;
    }

    public Money getExchangePostFee() {
        return exchangePostFee;
    }

    public void setExchangePostFee(Money exchangePostFee) {
        this.exchangePostFee = exchangePostFee;
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

    public Integer getExchangeSourceId() {
        return exchangeSourceId;
    }

    public void setExchangeSourceId(Integer exchangeSourceId) {
        this.exchangeSourceId = exchangeSourceId;
    }

    public String getSpecInfo() {
        return specInfo;
    }

    public void setSpecInfo(String specInfo) {
        this.specInfo = specInfo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    public String getOuterSku() {
        return outerSku;
    }

    public void setOuterSku(String outerSku) {
        this.outerSku = outerSku;
    }
}
