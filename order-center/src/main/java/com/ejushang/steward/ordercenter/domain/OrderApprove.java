package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.ordercenter.constant.OrderStatus;
import com.ejushang.steward.ordercenter.util.OrderUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单审核信息
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_order_approve")
@Entity
public class OrderApprove implements EntityClass<Integer>, OperableData {

    private Integer id;

    private OrderStatus orderStatus;

    private Date createTime;

    private Date updateTime;

    private Integer operatorId;

    private Integer orderId;

    private String operatorName;

    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", insertable = false, updatable = false)
    public Order getOrder() {
        return order;
    }
    @Transient
    public String getOperatorName() {
        return OrderUtil.getOperatorName(operatorId);
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

    @javax.persistence.Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @javax.persistence.Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }




    @javax.persistence.Column(name = "order_id")
    @Basic
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderApprove{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                ", orderId=" + orderId +
                ", order=" + order +
                '}';
    }
}
