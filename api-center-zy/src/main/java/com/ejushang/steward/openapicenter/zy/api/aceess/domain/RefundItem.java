package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

/**
 * User: Shiro
 * Date: 14-8-6
 * Time: 下午5:36
 */
public class RefundItem extends ZiYouObject {
    private static final long serialVersionUID = -8826244982436912647L;
    /**
     * 退款单编号
     */
    @ApiField("refund_id")
    private String refundId;

    /**
     * 商品sku编号
     */
    @ApiField("sku")
    private String sku;

    /**
     * sku信息，如30004447689|颜色分类:军绿色;尺码:XS
     */
    @ApiField("sku_info")
    private String skuInfo;

    /**
     * 商品价格。单位：分。
     */
    @ApiField("price")
    private Long price;

    /**
     * 商品数量
     */
    @ApiField("num")
    private Long num;

    /**
     * 商品id
     */
    @ApiField("num_iid")
    private String numIid;

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(String skuInfo) {
        this.skuInfo = skuInfo;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumId(String numIid) {
        this.numIid = numIid;
    }
}
