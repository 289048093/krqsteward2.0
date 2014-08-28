package com.ejushang.steward.ordercenter.vo;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-5-14
 * Time: 上午11:52
 * To change this template use File | Settings | File Templates.
 */
public class PaymentOrderItemVo {

    //智库城订单编号
    private String id;
    //外部订单编号
    private String platformSubOrderNo;
    //订单项类型
    private String type;
    //订单项状态
    private String status;
    //线上退货状态
    private String returnStatus;
    //线下退货状态
    private String offlineReturnStatus;
    //售前换货状态
    private Boolean exchanged;
    //商品
    private ProductVo product;
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
    private String price;
    //促销价
    private String discountPrice;
    //订货数量
    private String buyCount;
    //库存
    private String repoNum;
    //订单项优惠金额
    private String discountFee;
    //分摊优惠金额
    private String sharedDiscountFee;
    //分摊邮费
    private String sharedPostFee;
    //成交金额
    private String actualFee;
    //邮费补差金额
    private String postCoverFee;
    //邮费补差退款金额
    private String postCoverRefundFee;
    //服务补差金额
    private String serviceCoverFee;
    //服务补差退款金额
    private String serviceCoverRefundFee;
    //线上退款金额
    private String refundFee;
    //线下退款金额
    private String offlineRefundFee;
    //线上退货邮费
    private String returnPostFee;
    //线上退货邮费承担方
    private String returnPostPayer;
    //线下退货邮费
    private String offlineReturnPostFee ;
    //线下退货邮费承担方
    private String offlineReturnPostPayer;
    //线下换货邮费
    private String exchangePostFee;
    //线下换货邮费承担方
    private String exchangePostPayer;
    //货款
    private String goodsFee;
    //预收款分配
    private String feesString;
    //预收款退款分配
    private String refundFeesString;
    //价格描述
    private String priceDescription;
    //商品类型名称
    private  String categoryName;
    //规格
    private String specInfo;

    public String getSpecInfo() {
        return specInfo;
    }

    public void setSpecInfo(String specInfo) {
        this.specInfo = specInfo;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    public String getOfflineReturnStatus() {
        return offlineReturnStatus;
    }

    public void setOfflineReturnStatus(String offlineReturnStatus) {
        this.offlineReturnStatus = offlineReturnStatus;
    }

    public Boolean getExchanged() {
        return exchanged;
    }

    public void setExchanged(Boolean exchanged) {
        this.exchanged = exchanged;
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

    public String getReturnPostPayer() {
        return returnPostPayer;
    }

    public void setReturnPostPayer(String returnPostPayer) {
        this.returnPostPayer = returnPostPayer;
    }

    public String getOfflineReturnPostFee() {
        return offlineReturnPostFee;
    }

    public void setOfflineReturnPostFee(String offlineReturnPostFee) {
        this.offlineReturnPostFee = offlineReturnPostFee;
    }

    public String getOfflineReturnPostPayer() {
        return offlineReturnPostPayer;
    }

    public void setOfflineReturnPostPayer(String offlineReturnPostPayer) {
        this.offlineReturnPostPayer = offlineReturnPostPayer;
    }

    public String getExchangePostFee() {
        return exchangePostFee;
    }

    public void setExchangePostFee(String exchangePostFee) {
        this.exchangePostFee = exchangePostFee;
    }

    public String getExchangePostPayer() {
        return exchangePostPayer;
    }

    public void setExchangePostPayer(String exchangePostPayer) {
        this.exchangePostPayer = exchangePostPayer;
    }

    public String getGoodsFee() {
        return goodsFee;
    }

    public void setGoodsFee(String goodsFee) {
        this.goodsFee = goodsFee;
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

    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }
}
