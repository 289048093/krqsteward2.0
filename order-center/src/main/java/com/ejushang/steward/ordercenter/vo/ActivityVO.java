package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.ActivityType;
import com.ejushang.steward.ordercenter.domain.ActivityItem;
import com.ejushang.steward.ordercenter.domain.ActivityShop;
import com.ejushang.steward.ordercenter.domain.Brand;
import com.ejushang.steward.ordercenter.domain.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Shiro
 * Date: 14-7-7
 * Time: 下午3:49
 */
public class ActivityVO {
    private Integer id;

    private ActivityType type;

    private Brand brand;

    private Product product;

    private Money actualFeeBegin = Money.valueOf(0);

    private Money actualFeeEnd = Money.valueOf(0);

    private Boolean inUse = true;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private Integer operatorId;

    private List<Integer> shopIds = new ArrayList<Integer>(0);

    private List<ActivityItem> activityItems = new ArrayList<ActivityItem>(0);


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public Money getActualFeeBegin() {
        return actualFeeBegin;
    }

    public void setActualFeeBegin(Money actualFeeBegin) {
        this.actualFeeBegin = actualFeeBegin;
    }

    public Money getActualFeeEnd() {
        return actualFeeEnd;
    }

    public void setActualFeeEnd(Money actualFeeEnd) {
        this.actualFeeEnd = actualFeeEnd;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public List<Integer> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<Integer> shopIds) {
        this.shopIds = shopIds;
    }

    public List<ActivityItem> getActivityItems() {
        return activityItems;
    }

    public void setActivityItems(List<ActivityItem> activityItems) {
        this.activityItems = activityItems;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "ActivityVO{" +
                "id=" + id +
                ", type=" + type +
                ", actualFeeBegin=" + actualFeeBegin +
                ", actualFeeEnd=" + actualFeeEnd +
                ", inUse=" + inUse +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                ", shopIds=" + shopIds +
                ", activityItems=" + activityItems +
                '}';
    }


}
