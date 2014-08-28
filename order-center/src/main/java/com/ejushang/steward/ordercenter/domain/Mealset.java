package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_mealset")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class Mealset implements EntityClass<Integer>, OperableData {

    private Integer id;

    /**
     * 套餐名
     */
    private String name;

    /**
     * 套餐SKU
     */
    private String sku;

    /**
     * 卖点描述
     */
    private String sellDescription;

    /**
     * 逻辑删除标志位
     */
    private boolean deleted = false;

    /**
     * 增加时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 操作ID
     */
    private Integer operatorId;

    /**
     * 套餐下的套餐项
     */
    private List<MealsetItem> mealsetItemList = new ArrayList<MealsetItem>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mealset")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = Constants.ENTITY_CACHE_NAME)
    public List<MealsetItem> getMealsetItemList() {
        return mealsetItemList;
    }

    public void setMealsetItemList(List<MealsetItem> mealsetItemList) {
        this.mealsetItemList = mealsetItemList;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @javax.persistence.Column(name = "sku")
    @Basic
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }


    @javax.persistence.Column(name = "sell_description")
    @Basic
    public String getSellDescription() {
        return sellDescription;
    }

    public void setSellDescription(String sellDescription) {
        this.sellDescription = sellDescription;
    }

    @javax.persistence.Column(name = "deleted")
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }


    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }


    @javax.persistence.Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);
    }

    @Override
    public String toString() {
        return "Mealset{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", sellDescription='" + sellDescription + '\'' +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                ", mealsetItemList=" + mealsetItemList +
                '}';
    }
}
