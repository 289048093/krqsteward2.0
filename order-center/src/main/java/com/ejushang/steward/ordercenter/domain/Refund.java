package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.EmployeeUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.vo.OrderItemVo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Shiro
 * Date: 14-5-8
 * Time: 上午10:08
 */
@javax.persistence.Table(name = "t_refund")
@Entity
public class Refund implements EntityClass<Integer>, OperableData {
    private Integer id;
    /**
     * 退款单号
     */
    private String platformRefundNo;
    /**
     * 平台类型
     */
    private PlatformType platformType;

    /**
     * 原始退款ID
     */
    private Integer originalRefundId;

    private OriginalRefund originalRefund;

    /**
     * 退款状态(正在申请,成功,失败)
     */
    private RefundStatus status;
    /**
     * 售前还是售后退款
     */
    private RefundPhase phase;
    /**
     * 退款类型(订单退款,预收款退款)
     */
    private RefundType type;
    /**
     * 订单项ID
     */
    private Integer orderItemId;
    /**
     * 订单项
     */
    private OrderItem orderItem;
    /**
     * 预收款ID
     */
    private Integer paymentId;
    /**
     * 预收款
     */
    @JsonIgnore
    private Payment payment;
    /**
     * 是否是线上退款
     */
    private boolean online = false;
    /**
     * 账面退款金额
     */
    private Money refundFee = Money.valueOf(0);
    /**
     * 实际退款金额
     */
    private Money actualRefundFee = Money.valueOf(0);
    /**
     * 退款时间
     */
    private Date refundTime;
    /**
     * 买家ID
     */
    private String buyerId;
    /**
     * 买家昵称
     */
    private String buyerName;
    /**
     * 退款原因
     */
    private String reason;
    /**
     * 备注
     */
    private String remark;
    /**
     * 退款说明
     */
    private String description;
    /**
     * 是否同时退货
     */
    private boolean alsoReturn=false;
    /**
     * 运费
     */
    private Money postFee = Money.valueOf(0);
    /**
     * 运费承担方
     */
    private PostPayer postPayer;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    private Integer operatorId;

    private Integer shopId;

    private Shop shop;

    /**
     * 货运单号
     */
    private String shippingNo;

    /**
     * 物流公司
     */
    private String shippingComp;
    /**
     * 回访时间
     */
    private Date revisitTime;

    private String buyerAlipayNo;



    @javax.persistence.Column(name = "shipping_no")
    @Basic
    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }


    @javax.persistence.Column(name = "shipping_comp")
    @Basic
    public String getShippingComp() {
        return shippingComp;
    }

    public void setShippingComp(String shippingComp) {
        this.shippingComp = shippingComp;
    }

    @javax.persistence.Column(name = "buyer_alipay_no")
    @Basic
    public String getBuyerAlipayNo() {
        return buyerAlipayNo;
    }

    public void setBuyerAlipayNo(String buyerAlipayNo) {
        this.buyerAlipayNo = buyerAlipayNo;
    }
    @Transient
    public String getOperatorName() {
        return EmployeeUtil.getOperatorName(operatorId);
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "platform_refund_no")
    @Basic
    public String getPlatformRefundNo() {
        return platformRefundNo;
    }

    public void setPlatformRefundNo(String platformRefundNo) {
        this.platformRefundNo = platformRefundNo;
    }

    @javax.persistence.Column(name = "platform_type")
    @Enumerated(EnumType.STRING)
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }
    @javax.persistence.Column(name = "original_refund_id")
    @Basic
    public Integer getOriginalRefundId() {
        return originalRefundId;
    }

    public void setOriginalRefundId(Integer originalRefundId) {
        this.originalRefundId = originalRefundId;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_refund_id", insertable = false, updatable = false)
    public OriginalRefund getOriginalRefund() {
        return originalRefund;
    }

    public void setOriginalRefund(OriginalRefund originalRefund) {
        this.originalRefund = originalRefund;
    }

    @javax.persistence.Column(name = "status")
    @Enumerated(EnumType.STRING)
    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }
    @javax.persistence.Column(name = "phase")
    @Enumerated(EnumType.STRING)
    public RefundPhase getPhase() {
        return phase;
    }

    public void setPhase(RefundPhase phase) {
        this.phase = phase;
    }
    @javax.persistence.Column(name = "type")
    @Enumerated(EnumType.STRING)
    public RefundType getType() {
        return type;
    }

    public void setType(RefundType type) {
        this.type = type;
    }
    @javax.persistence.Column(name = "order_item_id")
    @Basic
    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", insertable = false, updatable = false)
    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
    @javax.persistence.Column(name = "payment_id")
    @Basic
    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", insertable = false, updatable = false)
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @javax.persistence.Column(name = "online")
    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    @javax.persistence.Column(name = "refund_fee")
    public Money getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Money refundFee) {
        this.refundFee = refundFee;
    }
    @javax.persistence.Column(name = "actual_refund_fee")
    public Money getActualRefundFee() {
        return actualRefundFee;
    }

    public void setActualRefundFee(Money actualRefundFee) {
        this.actualRefundFee = actualRefundFee;
    }

    @Column(name = "refund_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = UglyTimestampUtil.convertTimestampToDate(refundTime);
    }
    @javax.persistence.Column(name = "buyer_id")
    @Basic
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
    @javax.persistence.Column(name = "buyer_name")
    @Basic
    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
    @javax.persistence.Column(name = "reason")
    @Basic
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    @javax.persistence.Column(name = "remark")
    @Basic
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @javax.persistence.Column(name = "description")
    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @javax.persistence.Column(name = "also_return")
    public boolean isAlsoReturn() {
        return alsoReturn;
    }

    public void setAlsoReturn(boolean alsoReturn) {
        this.alsoReturn = alsoReturn;
    }
    @javax.persistence.Column(name = "post_fee")
    public Money getPostFee() {
        return postFee;
    }

    public void setPostFee(Money postFee) {
        this.postFee = postFee;
    }
    @javax.persistence.Column(name = "post_payer")
    @Enumerated(EnumType.STRING)
    public PostPayer getPostPayer() {
        return postPayer;
    }

    public void setPostPayer(PostPayer postPayer) {
        this.postPayer = postPayer;
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
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);;
    }
    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    @javax.persistence.Column(name = "shop_id")
    @Basic
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", insertable = false, updatable = false)
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Column(name = "revisit_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRevisitTime() {
        return revisitTime;
    }

    public void setRevisitTime(Date revisitTime) {
        this.revisitTime = UglyTimestampUtil.convertTimestampToDate(revisitTime);;
    }

    @Override
    public String toString() {
        return "Refund{" +
                "id=" + id +
                ", platformRefundNo=" + platformRefundNo +
                ", platformType=" + platformType +
                ", originalRefundId=" + originalRefundId +
                ", status=" + status +
                ", phase=" + phase +
                ", type=" + type +
                ", orderItemId=" + orderItemId +
                ", orderItem=" + orderItem +
                ", paymentId=" + paymentId +
                ", online=" + online +
                ", refundFee=" + refundFee +
                ", actualRefundFee=" + actualRefundFee +
                ", refundTime=" + refundTime +
                ", buyerId='" + buyerId + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", reason='" + reason + '\'' +
                ", remark='" + remark + '\'' +
                ", description='" + description + '\'' +
                ", alsoReturn=" + alsoReturn +
                ", postFee=" + postFee +
                ", postPayer='" + postPayer + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                '}';
    }
    private OrderItemVo orderItemVo;
     @Transient
    public OrderItemVo getOrderItemVo() {
        return orderItemVo;
    }

    public void setOrderItemVo(OrderItemVo orderItemVo) {
        this.orderItemVo = orderItemVo;
    }

    private String receiverPhone;

    private String receiverMobile;

    @Transient
    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    @Transient
    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }
}