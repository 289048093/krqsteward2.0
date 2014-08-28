package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.Money;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-5-8
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "t_payment_allocation")
@Entity
public class PaymentAllocation implements EntityClass<Integer>, OperableData {

    private Integer id;

    private Integer orderItemId;

    private Integer paymentId;

    private Money paymentFee = Money.valueOf(0);

    private Money refundFee = Money.valueOf(0);

    private Date createTime;

    private Date updateTime;

    private Integer operatorId;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "order_item_id")
    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    @javax.persistence.Column(name = "payment_id")
    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    @javax.persistence.Column(name = "payment_fee")
    public Money getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(Money paymentFee) {
        this.paymentFee = paymentFee;
    }

    @javax.persistence.Column(name = "refund_fee")
    public Money getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Money refundFee) {
        this.refundFee = refundFee;
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

    @Override
    public String toString() {
        return "PaymentAllocation{" +
                "id=" + id +
                ", orderItemId=" + orderItemId +
                ", paymentId=" + paymentId +
                ", paymentFee=" + paymentFee +
                ", refundFee=" + refundFee +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                '}';
    }
}
