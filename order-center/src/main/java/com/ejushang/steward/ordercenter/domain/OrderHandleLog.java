package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.ordercenter.constant.OrderStatus;
import com.ejushang.steward.ordercenter.util.OrderUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单状态变更记录
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_order_handle_log")
@Entity
public class OrderHandleLog implements EntityClass<Integer>, OperableData {

    private Integer id;

    /**
     * 原状态
     */
    private OrderStatus fromStatus;

    /**
     * 变更后状态
     */
    private OrderStatus toStatus;

    private Date createTime;

    private Integer operatorId;

    private Integer orderId;

    private Order order;
 //拼接日志字段
    private String log;
    @Transient
    public String getLog() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(createTime!=null?simpleDateFormat.format(createTime):"").append(operatorId != null ? ("操作人 " + OrderUtil.getOperatorName(operatorId)) + " ": " 系统 ");
        stringBuilder.append("将订单从").append(fromStatus!=null?fromStatus.getValue():"").append("改变到").append(toStatus!=null?toStatus.getValue():"");
        return stringBuilder.toString();
    }


     @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", insertable = false, updatable = false)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "from_status")
    @Enumerated(EnumType.STRING)
    public OrderStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(OrderStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    @javax.persistence.Column(name = "to_status")
    @Enumerated(EnumType.STRING)
    public OrderStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(OrderStatus toStatus) {
        this.toStatus = toStatus;
    }


    @javax.persistence.Column(name = "order_id")
    @Basic
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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
