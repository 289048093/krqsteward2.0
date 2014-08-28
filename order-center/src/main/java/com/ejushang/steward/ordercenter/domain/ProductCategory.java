package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_product_category")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class ProductCategory implements EntityClass<Integer>, OperableData {

    private Integer id;

    private String name;

    private Date createTime;

    private Date updateTime;

    private Integer operatorId;

    private List<ProductCategory> children = new ArrayList<ProductCategory>();

    private Integer parentCategoryId;

    private ProductCategory parentCategory;

    @Column(name = "parent_category_id")
    @Basic
    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id", insertable = false, updatable = false)
    public ProductCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(ProductCategory parentCategory) {
        this.parentCategory = parentCategory;
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


    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    @Transient
    public List<ProductCategory> getChildren() {
        return children;
    }

    public void setChildren(List<ProductCategory> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductCategory) {
            ProductCategory prodCat = (ProductCategory) obj;
            return prodCat.getId().equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id << 5 - id;
    }

}
