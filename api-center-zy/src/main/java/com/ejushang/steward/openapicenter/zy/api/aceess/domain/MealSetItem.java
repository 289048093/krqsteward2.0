package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-25
 * Time: 下午5:31
 */
public class MealSetItem {
    /**
     * 套餐价
     */
    @ApiField("price")
    private Money price;

    /**
     * 套餐中的数量
     */
    @ApiField("amount")
    private Integer amount;

    /**
     * 套餐id
     */
    @ApiField("mealset_sku")
    private String mealsetSku;

    /**
     * 商品id
     */
    @ApiField("product_sku")
    private String productSku;


    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getMealsetSku() {
        return mealsetSku;
    }

    public void setMealsetSku(String mealsetSku) {
        this.mealsetSku = mealsetSku;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    @Override
    public String toString() {
        return JsonUtil.object2Json(this);
    }
}
