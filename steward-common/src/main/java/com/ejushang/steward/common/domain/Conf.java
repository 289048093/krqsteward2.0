package com.ejushang.steward.common.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_conf")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class Conf implements EntityClass<Integer>, OperableData {

    private Integer id;

    private String name;

    private String value;

    private String description;

    private Date createTime;

    private Date updateTime;

    private Integer operatorId;



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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


    @javax.persistence.Column(name = "value")
    @Basic
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @javax.persistence.Column(name = "description")
    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}
