package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.domain.Product;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-3
 * Time: 下午2:39
 * To change this template use File | Settings | File Templates.
 */
public class CollectInvoiceOrderVo {
    private String productNo;
    private Integer buyCount;
    private String shippingNos;
    private Money goodsFee;
    private String productName;

    public Money getGoodsFee() {
        return goodsFee;
    }

    public void setGoodsFee(Money goodsFee) {
        this.goodsFee = goodsFee;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public String getShippingNos() {
        return shippingNos;
    }

    public void setShippingNos(String shippingNos) {
        this.shippingNos = shippingNos;
    }
}
