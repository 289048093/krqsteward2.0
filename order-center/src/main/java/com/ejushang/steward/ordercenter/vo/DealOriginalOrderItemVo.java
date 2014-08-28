package com.ejushang.steward.ordercenter.vo;

/**
 * User: JBoss.WU
 * Date: 14-7-8
 * Time: 下午6:45
 * To change this template use File | Settings | File Templates.
 */
public class DealOriginalOrderItemVo {
    //主键ID
    private Integer id;
    //外部平台订单项编号
    private String platformSubOrderNo;
    //sku
    private String sku;
    //外部平台sku
    private String outSku;
    //品牌名称
    private String brandName;
    //原价（一口价）
    private String price;
    //订货数量
    private Integer  buyCount;
    //实付金额
    private String actualFee;
    //订单项优惠金额
    private String discountFee;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getOutSku() {
        return outSku;
    }

    public void setOutSku(String outSku) {
        this.outSku = outSku;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public String getActualFee() {
        return actualFee;
    }

    public void setActualFee(String actualFee) {
        this.actualFee = actualFee;
    }

    public String getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(String discountFee) {
        this.discountFee = discountFee;
    }

}
