package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.EmployeeUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-28
 */
@Table(name = "t_payment")
@Entity
public class Payment implements EntityClass<Integer>, OperableData {

    private Integer id;

    /**
     * 原始订单项ID
     */
    private Integer originalOrderItemId;

    @JsonIgnore
    private OriginalOrderItem originalOrderItem;

    /**
     * 外部平台子订单编号
     */
    private String platformSubOrderNo;

    private PlatformType platformType;

    private String platformOrderNo;

    /**
     * 原始订单ID
     */
    private Integer originalOrderId;

    private OriginalOrder originalOrder;

    private Date buyTime;

    private Date payTime;

    private String buyerId;

    private String buyerMessage;

    private String remark;

    private PaymentAllocateStatus allocateStatus;

    private PaymentType type;

    private Money paymentFee = Money.valueOf(0);

    private Money refundFee = Money.valueOf(0);

    /**
     * 店铺ID
     */
    private Integer shopId;

    private Shop shop;

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

    @javax.persistence.Column(name = "original_order_item_id")
    public Integer getOriginalOrderItemId() {
        return originalOrderItemId;
    }

    public void setOriginalOrderItemId(Integer originalOrderItemId) {
        this.originalOrderItemId = originalOrderItemId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="original_order_item_id", insertable = false, updatable = false)
    public OriginalOrderItem getOriginalOrderItem() {
        return originalOrderItem;
    }

    public void setOriginalOrderItem(OriginalOrderItem originalOrderItem) {
        this.originalOrderItem = originalOrderItem;
    }

    @javax.persistence.Column(name = "platform_sub_order_no")
    public String getPlatformSubOrderNo() {
        return platformSubOrderNo;
    }

    public void setPlatformSubOrderNo(String platformSubOrderNo) {
        this.platformSubOrderNo = platformSubOrderNo;
    }

    @javax.persistence.Column(name = "platform_type")
    @Enumerated(EnumType.STRING)
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @javax.persistence.Column(name = "platform_order_no")
    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    @javax.persistence.Column(name = "original_order_id")
    public Integer getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Integer originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="original_order_id", insertable = false, updatable = false)
    public OriginalOrder getOriginalOrder() {
        return originalOrder;
    }

    public void setOriginalOrder(OriginalOrder originalOrder) {
        this.originalOrder = originalOrder;
    }

    @javax.persistence.Column(name = "buy_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = UglyTimestampUtil.convertTimestampToDate(buyTime);
    }


    @javax.persistence.Column(name = "pay_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = UglyTimestampUtil.convertTimestampToDate(payTime);
    }

    @javax.persistence.Column(name = "buyer_id")
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    @javax.persistence.Column(name = "buyer_message")
    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    @javax.persistence.Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @javax.persistence.Column(name = "allocate_status")
    @Enumerated(EnumType.STRING)
    public PaymentAllocateStatus getAllocateStatus() {
        return allocateStatus;
    }

    public void setAllocateStatus(PaymentAllocateStatus allocateStatus) {
        this.allocateStatus = allocateStatus;
    }

    @javax.persistence.Column(name = "type")
    @Enumerated(EnumType.STRING)
    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
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

    @javax.persistence.Column(name = "shop_id")
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shop_id", insertable = false, updatable = false)
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
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
        return "Payment{" +
                "id=" + id +
                ", originalOrderItemId=" + originalOrderItemId +
                ", originalOrderItem=" + originalOrderItem +
                ", platformSubOrderNo='" + platformSubOrderNo + '\'' +
                ", platformType=" + platformType +
                ", platformOrderNo='" + platformOrderNo + '\'' +
                ", originalOrderId=" + originalOrderId +
                ", originalOrder=" + originalOrder +
                ", buyTime=" + buyTime +
                ", payTime=" + payTime +
                ", buyerId='" + buyerId + '\'' +
                ", buyerMessage='" + buyerMessage + '\'' +
                ", remark='" + remark + '\'' +
                ", allocateStatus=" + allocateStatus +
                ", type=" + type +
                ", paymentFee=" + paymentFee +
                ", refundFee=" + refundFee +
                ", shopId=" + shopId +
                ", shop=" + shop +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                '}';
    }
}
