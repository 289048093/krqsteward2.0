package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.PlatformType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_original_order")
@Entity
public class OriginalOrder implements EntityClass<Integer>{

    private Integer id;

    private String status;

    private Money totalFee = Money.valueOf(0);

    /** 订单应付金额（减去各类优惠后的金额） */
    private Money payableFee = Money.valueOf(0);

    /** 建议使用trade.promotion_details查询系统优惠 系统优惠金额（如打折，VIP，满就送等） */
    private Money discountFee = Money.valueOf(0);
    /** 所有订单级优惠金额 */
    private Money allDiscountFee = Money.valueOf(0);
    /** 京东商家优惠金额，包含整单以及订单项优惠） */
    private Money sellerDiscountFee = Money.valueOf(0);
    /** 订单货款金额（订单总金额-商家优惠金额）*/
    private Money sellerFee = Money.valueOf(0);
    /** 买家使用积分,下单时生成，且一直不变 */
    private Long pointFee;
    /** 是否包含邮费 */
    private Boolean hasPostFee = false;
    /** 交易中剩余的确认收货金额（这个金额会随着子订单确认收货而不断减少，交易成功后会变为零） */
    private Money availableConfirmFee = Money.valueOf(0);
    /** 买家实际使用积分（扣除部分退款使用的积分），交易完成后生成（交易成功或关闭），交易未完成时该字段值为0 */
    private Long realPointFee;
    /** 实付金额 */
    private Money actualFee = Money.valueOf(0);
    /** 邮费 */
    private Money postFee = Money.valueOf(0);
    /** 卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额） */
    private Money receivedPayment = Money.valueOf(0);
    /** 手工调整金额 */
    private Money adjustFee = Money.valueOf(0);
    /** 余额支付金额 */
    private Money balancedUsed = Money.valueOf(0);



    private String deliveryType;

    private String buyerMessage;

    private String remark;

    private String buyerId;

    private String buyerAlipayNo;

    private Receiver receiver;

    private Date buyTime;

    private Date payTime;

    private Date endTime;

    private Date modifiedTime;

    private PlatformType platformType;

    private String platformOrderNo;

    private Boolean needReceipt = false;

    private String receiptTitle;

    private String receiptContent;

    private Boolean processed = false;

    private Date createTime;

    private String outShopId;

    private List<OriginalOrderItem> originalOrderItemList = new ArrayList<OriginalOrderItem>(0);

    private List<PromotionInfo> promotionInfoList = new ArrayList<PromotionInfo>(0);

    private Boolean discard = false;

    private List<OriginalOrderBrand> originalOrderBrands=new ArrayList<OriginalOrderBrand>(0);
    @OneToMany (fetch = FetchType.LAZY, mappedBy = "originalOrder")
    public List<OriginalOrderBrand> getOriginalOrderBrands() {
        return originalOrderBrands;
    }

    public void setOriginalOrderBrands(List<OriginalOrderBrand> originalOrderBrands) {
        this.originalOrderBrands = originalOrderBrands;
    }

    @javax.persistence.Column(name = "discard")
    public Boolean getDiscard() {
        return discard;
    }

    public void setDiscard(Boolean discard) {
        this.discard = discard;
    }

    @OneToMany (fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "original_order_id",referencedColumnName = "id",insertable = false,updatable = false)})
    public List<OriginalOrderItem> getOriginalOrderItemList() {
        return originalOrderItemList;
    }

    public void setOriginalOrderItemList(List<OriginalOrderItem> originalOrderItemList) {
        this.originalOrderItemList = originalOrderItemList;
    }

    @OneToMany (fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "original_order_id",referencedColumnName = "id",insertable = false,updatable = false)})
    public List<PromotionInfo> getPromotionInfoList() {
        return promotionInfoList;
    }

    public void setPromotionInfoList(List<PromotionInfo> promotionInfoList) {
        this.promotionInfoList = promotionInfoList;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "status")
    @Basic
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @javax.persistence.Column(name = "total_fee")
    @Basic
    public Money getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Money totalFee) {
        this.totalFee = totalFee;
    }

    @javax.persistence.Column(name = "payable_fee")
    @Basic
    public Money getPayableFee() {
        return payableFee;
    }

    public void setPayableFee(Money payableFee) {
        this.payableFee = payableFee;
    }

    @javax.persistence.Column(name = "discount_fee")
    public Money getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Money discountFee) {
        this.discountFee = discountFee;
    }

    @javax.persistence.Column(name = "all_discount_fee")
    public Money getAllDiscountFee() {
        return allDiscountFee;
    }

    public void setAllDiscountFee(Money allDiscountFee) {
        this.allDiscountFee = allDiscountFee;
    }

    @javax.persistence.Column(name = "seller_discount_fee")
    public Money getSellerDiscountFee() {
        return sellerDiscountFee;
    }

    public void setSellerDiscountFee(Money sellerDiscountFee) {
        this.sellerDiscountFee = sellerDiscountFee;
    }

    @javax.persistence.Column(name = "seller_fee")
    public Money getSellerFee() {
        return sellerFee;
    }

    public void setSellerFee(Money sellerFee) {
        this.sellerFee = sellerFee;
    }

    @javax.persistence.Column(name = "point_fee")
    @Basic
    public Long getPointFee() {
        return pointFee;
    }

    public void setPointFee(Long pointFee) {
        this.pointFee = pointFee;
    }

    @javax.persistence.Column(name = "has_post_fee")
    public Boolean getHasPostFee() {
        return hasPostFee;
    }

    public void setHasPostFee(Boolean hasPostFee) {
        this.hasPostFee = hasPostFee;
    }

    @javax.persistence.Column(name = "available_confirm_fee")
    public Money getAvailableConfirmFee() {
        return availableConfirmFee;
    }

    public void setAvailableConfirmFee(Money availableConfirmFee) {
        this.availableConfirmFee = availableConfirmFee;
    }

    @javax.persistence.Column(name = "real_point_fee")
    @Basic
    public Long getRealPointFee() {
        return realPointFee;
    }

    public void setRealPointFee(Long realPointFee) {
        this.realPointFee = realPointFee;
    }

    @javax.persistence.Column(name = "actual_fee")
    public Money getActualFee() {
        return actualFee;
    }

    public void setActualFee(Money actualFee) {
        this.actualFee = actualFee;
    }

    @javax.persistence.Column(name = "post_fee")
    public Money getPostFee() {
        return postFee;
    }

    public void setPostFee(Money postFee) {
        this.postFee = postFee;
    }

    @javax.persistence.Column(name = "received_payment")
    public Money getReceivedPayment() {
        return receivedPayment;
    }

    public void setReceivedPayment(Money receivedPayment) {
        this.receivedPayment = receivedPayment;
    }

    @javax.persistence.Column(name = "adjust_fee")
    public Money getAdjustFee() {
        return adjustFee;
    }

    public void setAdjustFee(Money adjustFee) {
        this.adjustFee = adjustFee;
    }

    @javax.persistence.Column(name = "balance_used")
    public Money getBalancedUsed() {
        return balancedUsed;
    }

    public void setBalancedUsed(Money balancedUsed) {
        this.balancedUsed = balancedUsed;
    }

    @javax.persistence.Column(name = "delivery_type")
    @Basic
    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    @javax.persistence.Column(name = "buyer_message")
    @Basic
    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    @javax.persistence.Column(name = "remark")
    @Basic
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @javax.persistence.Column(name = "buyer_id")
    @Basic
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    @Column(name = "buyer_alipay_no")
    @Basic
    public String getBuyerAlipayNo() {
        return buyerAlipayNo;
    }

    public void setBuyerAlipayNo(String buyerAlipayNo) {
        this.buyerAlipayNo = buyerAlipayNo;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
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

    @javax.persistence.Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = UglyTimestampUtil.convertTimestampToDate(endTime);
    }

    @javax.persistence.Column(name = "modified_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = UglyTimestampUtil.convertTimestampToDate(modifiedTime);
    }

    @javax.persistence.Column(name = "platform_type")
    @Enumerated(EnumType.STRING)
    @Basic
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @javax.persistence.Column(name = "platform_order_no")
    @Basic
    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    private Integer shopId;

    @javax.persistence.Column(name = "shop_id")
    @Basic
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }


    @javax.persistence.Column(name = "need_receipt")
    @Basic
    public Boolean getNeedReceipt() {
        return needReceipt;
    }

    public void setNeedReceipt(Boolean needReceipt) {
        this.needReceipt = needReceipt;
    }


    @javax.persistence.Column(name = "receipt_title")
    @Basic
    public String getReceiptTitle() {
        return receiptTitle;
    }

    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }


    @javax.persistence.Column(name = "receipt_content")
    @Basic
    public String getReceiptContent() {
        return receiptContent;
    }

    public void setReceiptContent(String receiptContent) {
        this.receiptContent = receiptContent;
    }

    @javax.persistence.Column(name="out_shop_id")
    @Basic
    public String getOutShopId() {
        return outShopId;
    }

    public void setOutShopId(String outShopId) {
        this.outShopId = outShopId;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }

    @javax.persistence.Column(name = "processed")
    @Basic
    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "OriginalOrder{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", totalFee=" + totalFee +
                ", payableFee=" + payableFee +
                ", discountFee=" + discountFee +
                ", sellerDiscountFee=" + sellerDiscountFee +
                ", sellerFee=" + sellerFee +
                ", pointFee=" + pointFee +
                ", hasPostFee=" + hasPostFee +
                ", availableConfirmFee=" + availableConfirmFee +
                ", realPointFee=" + realPointFee +
                ", actualFee=" + actualFee +
                ", postFee=" + postFee +
                ", receivedPayment=" + receivedPayment +
                ", adjustFee=" + adjustFee +
                ", balancedUsed=" + balancedUsed +
                ", deliveryType='" + deliveryType + '\'' +
                ", buyerMessage='" + buyerMessage + '\'' +
                ", remark='" + remark + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", receiver=" + receiver +
                ", buyTime=" + buyTime +
                ", payTime=" + payTime +
                ", endTime=" + endTime +
                ", modifiedTime=" + modifiedTime +
                ", platformType=" + platformType +
                ", platformOrderNo='" + platformOrderNo + '\'' +
                ", needReceipt=" + needReceipt +
                ", receiptTitle='" + receiptTitle + '\'' +
                ", receiptContent='" + receiptContent + '\'' +
                ", processed=" + processed +
                ", createTime=" + createTime +
                ", outShopId='" + outShopId + '\'' +
                ", originalOrderItemList=" + originalOrderItemList +
                ", promotionInfoList=" + promotionInfoList +
                ", shopId=" + shopId +
                '}';
    }
}
