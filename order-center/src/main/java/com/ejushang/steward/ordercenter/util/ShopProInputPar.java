package com.ejushang.steward.ordercenter.util;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-8-8
 * Time: 上午10:05
 * To change this template use File | Settings | File Templates.
 */

//查询shopProduct实体的入参，构造成一个实体
public class ShopProInputPar {

    //商品名称
    private String productName;

    //sku
    private String sku;

    //商家编码
    private String productNo;

    //是否上架
    private Boolean isPutaway;

    //店铺id
    private Integer shopId;

    //是否自动同步库存
    private Boolean synStatus;

    //库存总数量(最小值)
    private Integer allNumMin;

    //库存总数量(最大值)
    private Integer allNumMax;

    //店铺库存数量(最小值)
    private Integer storageNumMin;

    //店铺库存数量(最大值)
    private Integer storageNumMax;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public Boolean getPutaway() {
        return isPutaway;
    }

    public void setPutaway(Boolean putaway) {
        isPutaway = putaway;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Boolean getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(Boolean synStatus) {
        this.synStatus = synStatus;
    }

    public Integer getAllNumMin() {
        return allNumMin;
    }

    public void setAllNumMin(Integer allNumMin) {
        this.allNumMin = allNumMin;
    }

    public Integer getAllNumMax() {
        return allNumMax;
    }

    public void setAllNumMax(Integer allNumMax) {
        this.allNumMax = allNumMax;
    }

    public Integer getStorageNumMin() {
        return storageNumMin;
    }

    public void setStorageNumMin(Integer storageNumMin) {
        this.storageNumMin = storageNumMin;
    }

    public Integer getStorageNumMax() {
        return storageNumMax;
    }

    public void setStorageNumMax(Integer storageNumMax) {
        this.storageNumMax = storageNumMax;
    }

    @Override
    public String toString() {
        return "ShopProInputPar{" +
                "productName='" + productName + '\'' +
                ", sku='" + sku + '\'' +
                ", productNo='" + productNo + '\'' +
                ", isPutaway=" + isPutaway +
                ", shopId=" + shopId +
                ", synStatus=" + synStatus +
                ", allNumMin=" + allNumMin +
                ", allNumMax=" + allNumMax +
                ", storageNumMin=" + storageNumMin +
                ", storageNumMax=" + storageNumMax +
                '}';
    }
}
