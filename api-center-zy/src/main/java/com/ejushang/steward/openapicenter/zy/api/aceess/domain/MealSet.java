package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiListField;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-25
 * Time: 下午5:31
 */
public class MealSet extends OperateTypeBean {

    /**
     * 套餐名
     */
    @ApiField("name")
    private String name;

    /**
     * 套餐SKU
     */
    @ApiField("sku")
    private String sku;

    /**
     * 卖点描述
     */
    @ApiField("sell_description")
    private String sellDescription;

    /**
     * 套餐项
     */
    @ApiListField("meal_set_items")
    private List<MealSetItem> mealsetItemList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSellDescription() {
        return sellDescription;
    }

    public void setSellDescription(String sellDescription) {
        this.sellDescription = sellDescription;
    }

    public List<MealSetItem> getMealsetItemList() {
        return mealsetItemList;
    }

    public void setMealsetItemList(List<MealSetItem> mealsetItemList) {
        this.mealsetItemList = mealsetItemList;
    }

    @Override
    public String toString() {
        return JsonUtil.object2Json(this);
    }
}
