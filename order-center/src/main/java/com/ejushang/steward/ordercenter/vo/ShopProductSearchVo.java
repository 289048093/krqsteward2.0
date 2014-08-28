package com.ejushang.steward.ordercenter.vo;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-8-12
 * Time: 上午9:35
 * To change this template use File | Settings | File Templates.
 */
public class ShopProductSearchVo {

    private Integer id;

    private String platform;

    private String shop;

    private Integer storagePercent;

    private Boolean synStatus;

    private Boolean autoPutaway;

    private String productName;

    private String brand;

    private String sku;

    private String productNo;

    private String color;

    private String specification;

    private Integer allAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Integer getStoragePercent() {
        return storagePercent;
    }

    public void setStoragePercent(Integer storagePercent) {
        this.storagePercent = storagePercent;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Integer getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(Integer allAmount) {
        this.allAmount = allAmount;
    }
}
