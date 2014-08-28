package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.common.util.Money;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-8-7
 * Time: 下午4:29
 * To change this template use File | Settings | File Templates.
 */
public class ShopProductVo {

    //id
    private Integer id;

    //产品名称
    private String name;

    //品牌
    private String brand;

    //sku
    private String sku;

    //商家编码
    private String productNo;

    //平台名称
    private String platformName;

    //店铺名称
    private String shopName;

    //一口价
    private Money price;

    //连接
    private String chaining;

    //是否上架
    private Boolean isPutaway;

    //总库存数量
    private Integer allNum;

    //店铺库存数量
    private Integer storageNum;

    //同步店铺库存数量
    private Integer synNum;

    //同步店铺库存占比
    private double synProportion;

    //是否自动同步库存
    private Boolean synStatus;

    //有库存是否自动上架
    private Boolean autoPutaway;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public String getChaining() {
        return chaining;
    }

    public void setChaining(String chaining) {
        this.chaining = chaining;
    }

    public Boolean getPutaway() {
        return isPutaway;
    }

    public void setPutaway(Boolean putaway) {
        isPutaway = putaway;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public Integer getStorageNum() {
        return storageNum;
    }

    public void setStorageNum(Integer storageNum) {
        this.storageNum = storageNum;
    }

    public Integer getSynNum() {
        return synNum;
    }

    public void setSynNum(Integer synNum) {
        this.synNum = synNum;
    }

    public double getSynProportion() {
        return synProportion;
    }

    public void setSynProportion(double synProportion) {
        this.synProportion = synProportion;
    }

    public Boolean getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(Boolean synStatus) {
        this.synStatus = synStatus;
    }

    public Boolean getAutoPutaway() {
        return autoPutaway;
    }

    public void setAutoPutaway(Boolean autoPutaway) {
        this.autoPutaway = autoPutaway;
    }
}
