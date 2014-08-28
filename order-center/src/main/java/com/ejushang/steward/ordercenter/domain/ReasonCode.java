package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 16:06
 */

@Table(name = "t_reason_code")
@Entity
public class ReasonCode implements EntityClass<Integer> ,OperableData{

    private Integer id;
    private String name;
    private String code;
    private String remark;
    private Boolean deleted=false;
    private Date createTime;
    private Date updateTime;
    private Integer operatorId;

    private Integer firstLevelCategoryId;
    private Integer secondLevelCategoryId;

    private ReasonCodeCategory firstLevelCategory;
    private ReasonCodeCategory secondLevelCategory;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonIgnore
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Column(name="create_time")
    @JsonIgnore
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name="update_time")
    @JsonIgnore
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name="operator_id")
    @JsonIgnore
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    @Column(name="first_level_category_id")
    @JsonIgnore
    public Integer getFirstLevelCategoryId() {
        return firstLevelCategoryId;
    }

    public void setFirstLevelCategoryId(Integer firstLevelCategoryId) {
        this.firstLevelCategoryId = firstLevelCategoryId;
    }

    @Column(name="second_level_category_id")
    @JsonIgnore
    public Integer getSecondLevelCategoryId() {
        return secondLevelCategoryId;
    }

    public void setSecondLevelCategoryId(Integer secondLevelCategoryId) {
        this.secondLevelCategoryId = secondLevelCategoryId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_level_category_id", insertable = false, updatable = false)
    @JsonIgnore
    public ReasonCodeCategory getFirstLevelCategory() {
        return firstLevelCategory;
    }

    public void setFirstLevelCategory(ReasonCodeCategory firsitLevelCategory) {
        this.firstLevelCategory = firsitLevelCategory;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_level_category_id", insertable = false, updatable = false)
    @JsonIgnore
    public ReasonCodeCategory getSecondLevelCategory() {
        return secondLevelCategory;
    }

    public void setSecondLevelCategory(ReasonCodeCategory secondLevelCategory) {
        this.secondLevelCategory = secondLevelCategory;
    }

}
