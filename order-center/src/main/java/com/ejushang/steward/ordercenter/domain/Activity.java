package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.ActivityType;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: liubin
 * Date: 14-3-28
 */
@Table(name = "t_activity")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class Activity implements EntityClass<Integer>, OperableData {

    private Integer id;

    private ActivityType type;

    /**
     * 参与活动的品牌ID
     */
    private Integer brandId;
    /**
     * 参与活动的品牌对象
     */
    private Brand brand;

    /**
     * 参与活动的产品ID
     */
    private Integer productId;



    /**
     * 参与活动的产品对象
     */
    private Product product;

    private Money actualFeeBegin = Money.valueOf(0);

    private Money actualFeeEnd = Money.valueOf(0);

    private Boolean inUse = true;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private Integer operatorId;


    private List<ActivityItem> activityItems=new ArrayList<ActivityItem>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brand_id", insertable = false, updatable = false)
    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Column(name = "brand_id")
    @Basic
    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }


    @Column(name = "in_use")
    @Basic
    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }


    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);
    }


    @Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    @javax.persistence.Column(name = "type")
    @Enumerated(EnumType.STRING)
    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    @javax.persistence.Column(name = "product_id")
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", insertable = false, updatable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @javax.persistence.Column(name = "actual_fee_begin")
    public Money getActualFeeBegin() {
        return actualFeeBegin;
    }

    public void setActualFeeBegin(Money actualFeeBegin) {
        this.actualFeeBegin = actualFeeBegin;
    }

    @javax.persistence.Column(name = "actual_fee_end")
    public Money getActualFeeEnd() {
        return actualFeeEnd;
    }

    public void setActualFeeEnd(Money actualFeeEnd) {
        this.actualFeeEnd = actualFeeEnd;
    }

    @javax.persistence.Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @OneToMany (fetch = FetchType.LAZY, mappedBy = "activity")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
    public List<ActivityItem> getActivityItems() {
        return activityItems;
    }

    public void setActivityItems(List<ActivityItem> activityItems) {
        this.activityItems = activityItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (id != null ? !id.equals(activity.id) : activity.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", type=" + type +
                ", brandId=" + brandId +
                ", productId=" + productId +
                ", actualFeeBegin=" + actualFeeBegin +
                ", actualFeeEnd=" + actualFeeEnd +
                ", inUse=" + inUse +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                '}';
    }

}
