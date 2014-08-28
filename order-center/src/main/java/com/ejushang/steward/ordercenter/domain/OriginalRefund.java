package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 原始退款单
 * User: Baron.Zhang
 * Date: 2014/5/9
 * Time: 14:22
 */
@Table(name = "t_original_refund")
@Entity
public class OriginalRefund implements EntityClass<Integer> {

    private Integer id;

    /** 退款单编号 */
	private String refundId;
    /** 退款协议版本 */
	private String refundVersion;
    /** refund:仅退款 return:退款退货 */
	private String refundType;
    /** cannot_refuse: 不允许操作 refund_onweb: 需要到网页版操作 */
	private String operationConstraint;
    /** wait_send_good:等待卖家发货 wait_confirm_good：卖家已发货，等待买家确认收货 finish: 交易完成 */
	private String tradeStatus;
    /** 申请退款金额，单位：分 */
	private Money refundFee = Money.valueOf(0);
    /** 申请退款原因 */
	private String reason;
    /** 实际退款金额 */
	private Money actualRefundFee = Money.valueOf(0);
    /** 退款创建时间 */
	private Date created;
    /** 当前状态超时时间 */
	private Date currentPhaseTimeout;
    /** 支付宝交易号 */
	private String alipayNo;
    /** 买家昵称 */
	private String buyerNick;
    /** 卖家昵称 */
	private String sellerNick;
    /** 交易主订单号 */
	private String tid;
    /** 交易子订单号 */
	private String oid;
    /** 淘宝小二是否介入 */
	private String csStatus;
    /** 退款单 状态 wait_seller_agree：买家申请，等待卖家同意 seller_refuse：卖家拒绝 goods_returning：退货中 closed：退款失败 success：退款成功 */
	private OriginalRefundStatus status;
    /** onsale:售中 aftersale：售后 */
	private RefundPhase refundPhase;
    /** 退款单的相关标签信息 */
	private List<OriginalRefundTag> originalRefundTagList = new ArrayList<OriginalRefundTag>();
    /** 退款商品信息 */
	private List<OriginalRefundItem> originalRefundItemList = new ArrayList<OriginalRefundItem>();
    /** 最后一次修改时间 */
	private Date modified;
    /** 单据类型，退款单 */
	private String billType;
    /** 物流公司 */
	private DeliveryType companyName;
    /** 物流运单号 */
	private String sid;
    /** 退货单操作日志 */
	private String operationLog;
    /** 退款说明 */
	private String description;
    /** 客户帐号 */
	private String buyerId;
    /** 审核日期 */
	private Date checkTime;
    /** 审核人  */
	private String checkUsername;
    /** 平台类型 */
    private PlatformType platformType;
    /** 店铺id */
    private Integer ShopId;
    /** 是否被处理过 */
    private Boolean processed = false;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "original_refund_id",referencedColumnName = "id",insertable = false,updatable = false)})
    public List<OriginalRefundTag> getOriginalRefundTagList() {
        return originalRefundTagList;
    }

    public void setOriginalRefundTagList(List<OriginalRefundTag> originalRefundTagList) {
        this.originalRefundTagList = originalRefundTagList;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "original_refund_id",referencedColumnName = "id",insertable = false,updatable = false)})
    public List<OriginalRefundItem> getOriginalRefundItemList() {
        return originalRefundItemList;
    }

    public void setOriginalRefundItemList(List<OriginalRefundItem> originalRefundItemList) {
        this.originalRefundItemList = originalRefundItemList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "refund_id")
    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    @Column(name = "refund_version")
    public String getRefundVersion() {
        return refundVersion;
    }

    public void setRefundVersion(String refundVersion) {
        this.refundVersion = refundVersion;
    }

    @Column(name = "refund_type")
    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    @Column(name = "operation_constraint")
    public String getOperationConstraint() {
        return operationConstraint;
    }

    public void setOperationConstraint(String operationConstraint) {
        this.operationConstraint = operationConstraint;
    }

    @Column(name = "trade_status")
    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    @Column(name = "refund_fee")
    public Money getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Money refundFee) {
        this.refundFee = refundFee;
    }

    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name = "actual_refund_fee")
    public Money getActualRefundFee() {
        return actualRefundFee;
    }

    public void setActualRefundFee(Money actualRefundFee) {
        this.actualRefundFee = actualRefundFee;
    }

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = UglyTimestampUtil.convertTimestampToDate(created);
    }

    @Column(name = "current_phase_timeout")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCurrentPhaseTimeout() {
        return currentPhaseTimeout;
    }

    public void setCurrentPhaseTimeout(Date currentPhaseTimeout) {
        this.currentPhaseTimeout = UglyTimestampUtil.convertTimestampToDate(currentPhaseTimeout);
    }

    @Column(name = "alipay_no")
    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    @Column(name = "buyer_nick")
    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    @Column(name = "seller_nick")
    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    @Column(name = "tid")
    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    @Column(name = "oid")
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    @Column(name = "cs_status")
    public String getCsStatus() {
        return csStatus;
    }

    public void setCsStatus(String csStatus) {
        this.csStatus = csStatus;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public OriginalRefundStatus getStatus() {
        return status;
    }

    public void setStatus(OriginalRefundStatus status) {
        this.status = status;
    }

    @Column(name = "refund_phase")
    @Enumerated(EnumType.STRING)
    @Basic
    public RefundPhase getRefundPhase() {
        return refundPhase;
    }

    public void setRefundPhase(RefundPhase refundPhase) {
        this.refundPhase = refundPhase;
    }

    @Column(name = "modified")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = UglyTimestampUtil.convertTimestampToDate(modified);
    }

    @Column(name = "bill_type")
    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    @Column(name = "company_name")
    @Enumerated(EnumType.STRING)
    @Basic
    public DeliveryType getCompanyName() {
        return companyName;
    }

    public void setCompanyName(DeliveryType companyName) {
        this.companyName = companyName;
    }

    @Column(name = "sid")
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Column(name = "operation_log")
    public String getOperationLog() {
        return operationLog;
    }

    public void setOperationLog(String operationLog) {
        this.operationLog = operationLog;
    }


    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "buyer_id")
    @Basic
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    @Column(name = "check_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = UglyTimestampUtil.convertTimestampToDate(checkTime);
    }

    @Column(name = "check_username")
    @Basic
    public String getCheckUsername() {
        return checkUsername;
    }

    public void setCheckUsername(String checkUsername) {
        this.checkUsername = checkUsername;
    }

    @Column(name="platform_type")
    @Enumerated(EnumType.STRING)
    @Basic
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @Column(name = "shop_id")
    @Basic
    public Integer getShopId() {
        return ShopId;
    }

    public void setShopId(Integer shopId) {
        ShopId = shopId;
    }

    @Column(name = "processed")
    @Basic
    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "OriginalRefund{" +
                "id=" + id +
                ", refundId='" + refundId + '\'' +
                ", refundVersion='" + refundVersion + '\'' +
                ", refundType='" + refundType + '\'' +
                ", operationConstraint='" + operationConstraint + '\'' +
                ", tradeStatus='" + tradeStatus + '\'' +
                ", refundFee=" + refundFee +
                ", reason='" + reason + '\'' +
                ", actualRefundFee=" + actualRefundFee +
                ", created=" + created +
                ", currentPhaseTimeout=" + currentPhaseTimeout +
                ", alipayNo='" + alipayNo + '\'' +
                ", buyerNick='" + buyerNick + '\'' +
                ", sellerNick='" + sellerNick + '\'' +
                ", tid='" + tid + '\'' +
                ", oid='" + oid + '\'' +
                ", csStatus='" + csStatus + '\'' +
                ", status=" + status +
                ", refundPhase=" + refundPhase +
                ", originalRefundTagList=" + originalRefundTagList +
                ", originalRefundItemList=" + originalRefundItemList +
                ", modified=" + modified +
                ", billType='" + billType + '\'' +
                ", companyName='" + companyName + '\'' +
                ", sid='" + sid + '\'' +
                ", operationLog='" + operationLog + '\'' +
                ", description='" + description + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", checkTime=" + checkTime +
                ", checkUsername='" + checkUsername + '\'' +
                ", platformType=" + platformType +
                ", ShopId=" + ShopId +
                ", processed=" + processed +
                '}';
    }
}
