package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-22
 * Time: 下午2:32
 */
public class Product extends OperateTypeBean {

    @ApiField("name")
    private String name;

    @ApiField("sku_code")
    private String sku;

    @ApiField("product_no")
    private String productNo;

    @ApiField("market_price")
    private Money marketPrice;

    @ApiField("minimum_price")
    private Money minimumPrice;

    @ApiField("color")
    private String color;

    @ApiField("weight")
    private String weight;

    @ApiField("box_size")
    private String boxSize;

    @ApiField("speci")
    private String speci;

    @ApiField("brand_name")
    private String brandName;

    @ApiField("category_name")
    private String categoryName;

    @ApiField("orgin")
    private String orgin;

    @ApiField("location")
    private String location;

    @ApiField("description")
    private String description;

    @ApiField("repository_code")
    private String repositoryCode;

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

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

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public Money getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Money marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Money getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Money minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(String boxSize) {
        this.boxSize = boxSize;
    }

    public String getSpeci() {
        return speci;
    }

    public void setSpeci(String speci) {
        this.speci = speci;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getOrgin() {
        return orgin;
    }

    public void setOrgin(String orgin) {
        this.orgin = orgin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return JsonUtil.object2Json(this);
    }
}
