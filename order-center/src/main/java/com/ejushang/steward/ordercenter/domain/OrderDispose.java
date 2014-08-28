package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.ordercenter.constant.OrderDisposeType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 订单拆分合并记录
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_order_dispose")
@Entity
public class OrderDispose implements EntityClass<Integer>, OperableData {

    private Integer id;

    /**
     * 原始订单ID列表,ID以","结尾,例如"123,124,125,"
     */
    private String sourceIds;

    /**
     * 结果订单ID列表,ID以","结尾,例如"123,124,125,"
     */
    private String targetIds;

    /**
     * 类型,拆分还是合并
     */
    private OrderDisposeType type;

    /**
     * 是否是手动进行的操作
     */
    private Boolean manual = false;

    private Date createTime;

    private Integer operatorId;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "source_ids")
    @Basic
    public String getSourceIds() {
        return sourceIds;
    }

    public void setSourceIds(String sourceIds) {
        this.sourceIds = sourceIds;
    }


    @javax.persistence.Column(name = "target_ids")
    @Basic
    public String getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(String targetIds) {
        this.targetIds = targetIds;
    }


    @javax.persistence.Column(name = "type")
    @Enumerated(EnumType.STRING)
    public OrderDisposeType getType() {
        return type;
    }

    public void setType(OrderDisposeType type) {
        this.type = type;
    }


    @javax.persistence.Column(name = "manual")
    @Basic
    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
    }

    @Override
    @Transient
    public Date getUpdateTime() {
        return null;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
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
