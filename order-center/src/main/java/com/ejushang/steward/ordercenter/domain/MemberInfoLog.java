package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.OrderLogType;
import com.ejushang.steward.ordercenter.constant.PlatformType;

import javax.persistence.*;
import java.util.Date;

/**
 * User: JBoss.WU
 * Date: 14-8-6
 * Time: 下午3:04
 */
@Table(name = "t_member_info_log")
@Entity
public class MemberInfoLog implements Cloneable , EntityClass<Integer> {

    private Integer id;
   //日志类型
    private OrderLogType orderLogType;
   //是否手动订单
    private Boolean offline;
    //外部平台编号
    private String platformOrderNo;
    //外部平台类型
    private PlatformType platformType;
    //买家ID
    private String buyerId;
    //收货信息
    private Receiver receiver;
    //快递编号
    private String shippingNo;
    //快递公司
    private String shippingComp;
    //购买次数
    private Integer buyCount;
     //创建时间
    private Date createTime;
     //更新时间
    private Date updateTime;

    private Money refundOrderFee;

    private Money refundPaymentFee;

    private Money paymentFee;

    private Money actualFee;

    private Integer refundId;

    private Boolean processed=false;

    private Integer shopId;

     @Column(name="shop_id")
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Column(name = "refund_id")
    public Integer getRefundId() {
        return refundId;
    }

    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }

    @Column(name = "refund_order_fee")
    public Money getRefundOrderFee() {
        return refundOrderFee;
    }

    public void setRefundOrderFee(Money refundOrderFee) {
        this.refundOrderFee = refundOrderFee;
    }
    @Column(name = "refund_payment_fee")
    public Money getRefundPaymentFee() {
        return refundPaymentFee;
    }

    public void setRefundPaymentFee(Money refundPaymentFee) {
        this.refundPaymentFee = refundPaymentFee;
    }

    @Column(name = "payment_fee")
    public Money getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(Money paymentFee) {
        this.paymentFee = paymentFee;
    }
    @Column(name = "actual_fee")
    public Money getActualFee() {
        return actualFee;
    }

    public void setActualFee(Money actualFee) {
        this.actualFee = actualFee;
    }
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "order_log_type")
    @Enumerated(EnumType.STRING)
    public OrderLogType getOrderLogType() {
        return orderLogType;
    }

    public void setOrderLogType(OrderLogType orderLogType) {
        this.orderLogType = orderLogType;
    }
    @Column(name = "is_offline")
    public Boolean getOffline() {
        return offline;
    }

    public void setOffline(Boolean offline) {
        this.offline = offline;
    }

    @Column(name = "platform_order_no")
    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }
    @Column(name = "platform_type")
    @Enumerated(EnumType.STRING)
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }
    @Column(name = "buyer_id")
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Column(name = "shipping_no")
    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }
    @Column(name = "shipping_comp")
    public String getShippingComp() {
        return shippingComp;
    }

    public void setShippingComp(String shippingComp) {
        this.shippingComp = shippingComp;
    }
    @Column(name = "buy_count")
    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Column(name="processed")
    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    /**
     * No need to deep copy in my case. Pls feel free to change it to deep copy if required.
     * Author: Codec.yang
     * @return
     */
    @Override
    public MemberInfoLog clone(){

        MemberInfoLog memberInfoLog=null;
        try {
            memberInfoLog= (MemberInfoLog) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return memberInfoLog;
    }

}
