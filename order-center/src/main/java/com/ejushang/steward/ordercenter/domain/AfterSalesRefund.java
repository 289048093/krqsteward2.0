package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.Money;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.Date;
import java.util.List;

import com.ejushang.steward.ordercenter.constant.RefundMethod;

/**
 * 售后退款中间表
 * @Author Channel
 * @Date 2014-08-22
 */
@Table(name = "t_after_sales_refund")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSalesRefund implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 所属售后工单ID
     */
    private Integer afterSalesId;
    /**
     * 订单项ID
     */
    private Integer afterSalesItemId;
    /**
     * 总金额
     */
    private Money fee = Money.valueOf(0);
    /**
     * 线上退款ID
     */
    private Integer onlineRefundId;
    /**
     * 线上退款总金额
     */
    private Money onlineFee = Money.valueOf(0);
    /**
     * 线下退款ID
     */
    private Integer offlineRefundId;
    /**
     * 线下退款总金额
     */
    private Money offlineFee = Money.valueOf(0);
    /**
     * 退款是否已支付
     */
    private Boolean payment;
    /**
     * 确认支付时间
     */
    private Date paymentTime;
    /**
     * RefundMethod:退款方式;ALIPAY:支付宝,BANK:银行;
     */
    private RefundMethod refundMethod;
    /**
     * 退款买家支付宝账号（淘宝）
     */
    private String alipayNo;
    /**
     * 退款银行
     */
    private String bank;
    /**
     * 退款银行账号
     */
    private String bankAccout;
    /**
     * 退款银行收款人姓名
     */
    private String bankUser;
    /**
     * 退款时间
     */
    private Date refundTime;
    /**
     * 操作者ID
     */
    private Integer operatorId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 版本号
     */
    private Integer version;

    /**
    * 获取"id"
    */
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    /**
     * 设置"id"
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
    * 获取"所属售后工单ID"
    */
    @javax.persistence.Column(name = "after_sales_id")
    @Basic
    public Integer getAfterSalesId() {
        return afterSalesId;
    }

    /**
     * 设置"所属售后工单ID"
     */
    public void setAfterSalesId(Integer afterSalesId) {
        this.afterSalesId = afterSalesId;
    }
    /**
    * 获取"订单项ID"
    */
    @javax.persistence.Column(name = "after_sales_item_id")
    @Basic
    public Integer getAfterSalesItemId() {
        return afterSalesItemId;
    }

    /**
     * 设置"订单项ID"
     */
    public void setAfterSalesItemId(Integer afterSalesItemId) {
        this.afterSalesItemId = afterSalesItemId;
    }
    /**
    * 获取"总金额"
    */
    @javax.persistence.Column(name = "fee")
    @Basic
    public Money getFee() {
        return fee;
    }

    /**
     * 设置"总金额"
     */
    public void setFee(Money fee) {
        this.fee = fee;
    }
    /**
    * 获取"线上退款ID"
    */
    @javax.persistence.Column(name = "online_refund_id")
    @Basic
    public Integer getOnlineRefundId() {
        return onlineRefundId;
    }

    /**
     * 设置"线上退款ID"
     */
    public void setOnlineRefundId(Integer onlineRefundId) {
        this.onlineRefundId = onlineRefundId;
    }
    /**
    * 获取"线上退款总金额"
    */
    @javax.persistence.Column(name = "online_fee")
    @Basic
    public Money getOnlineFee() {
        return onlineFee;
    }

    /**
     * 设置"线上退款总金额"
     */
    public void setOnlineFee(Money onlineFee) {
        this.onlineFee = onlineFee;
    }
    /**
    * 获取"线下退款ID"
    */
    @javax.persistence.Column(name = "offline_refund_id")
    @Basic
    public Integer getOfflineRefundId() {
        return offlineRefundId;
    }

    /**
     * 设置"线下退款ID"
     */
    public void setOfflineRefundId(Integer offlineRefundId) {
        this.offlineRefundId = offlineRefundId;
    }
    /**
    * 获取"线下退款总金额"
    */
    @javax.persistence.Column(name = "offline_fee")
    @Basic
    public Money getOfflineFee() {
        return offlineFee;
    }

    /**
     * 设置"线下退款总金额"
     */
    public void setOfflineFee(Money offlineFee) {
        this.offlineFee = offlineFee;
    }
    /**
    * 获取"退款是否已支付"
    */
    @javax.persistence.Column(name = "payment")
    @Basic
    public Boolean isPayment() {
        return payment;
    }

    /**
     * 设置"退款是否已支付"
     */
    public void setPayment(Boolean payment) {
        this.payment = payment;
    }
    /**
    * 获取"确认支付时间"
    */
    @javax.persistence.Column(name = "payment_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPaymentTime() {
        return paymentTime;
    }

    /**
     * 设置"确认支付时间"
     */
    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = UglyTimestampUtil.convertTimestampToDate(paymentTime);
    }
    /**
    * 获取"RefundMethod:退款方式;ALIPAY:支付宝,BANK:银行;"
    */
    @javax.persistence.Column(name = "refund_method")
    @Enumerated(EnumType.STRING)
    public RefundMethod getRefundMethod() {
        return refundMethod;
    }

    /**
     * 设置"RefundMethod:退款方式;ALIPAY:支付宝,BANK:银行;"
     */
    public void setRefundMethod(RefundMethod refundMethod) {
        this.refundMethod = refundMethod;
    }
    /**
    * 获取"退款买家支付宝账号（淘宝）"
    */
    @javax.persistence.Column(name = "alipay_no")
    @Basic
    public String getAlipayNo() {
        return alipayNo;
    }

    /**
     * 设置"退款买家支付宝账号（淘宝）"
     */
    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }
    /**
    * 获取"退款银行"
    */
    @javax.persistence.Column(name = "bank")
    @Basic
    public String getBank() {
        return bank;
    }

    /**
     * 设置"退款银行"
     */
    public void setBank(String bank) {
        this.bank = bank;
    }
    /**
    * 获取"退款银行账号"
    */
    @javax.persistence.Column(name = "bank_accout")
    @Basic
    public String getBankAccout() {
        return bankAccout;
    }

    /**
     * 设置"退款银行账号"
     */
    public void setBankAccout(String bankAccout) {
        this.bankAccout = bankAccout;
    }
    /**
    * 获取"退款银行收款人姓名"
    */
    @javax.persistence.Column(name = "bank_user")
    @Basic
    public String getBankUser() {
        return bankUser;
    }

    /**
     * 设置"退款银行收款人姓名"
     */
    public void setBankUser(String bankUser) {
        this.bankUser = bankUser;
    }
    /**
    * 获取"退款时间"
    */
    @javax.persistence.Column(name = "refund_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRefundTime() {
        return refundTime;
    }

    /**
     * 设置"退款时间"
     */
    public void setRefundTime(Date refundTime) {
        this.refundTime = UglyTimestampUtil.convertTimestampToDate(refundTime);
    }
    /**
    * 获取"操作者ID"
    */
    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    /**
     * 设置"操作者ID"
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
    /**
    * 获取"创建时间"
    */
    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置"创建时间"
     */
    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }
    /**
    * 获取"更新时间"
    */
    @javax.persistence.Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置"更新时间"
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);
    }
    /**
    * 获取"版本号"
    */
    @Version
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置"版本号"
     */
    void setVersion(Integer version) {
        this.version = version;
    }
    /**
     * 售后单项目实体
     */
    @JsonIgnore
    private AfterSalesItem afterSalesItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_sales_item_id", insertable = false, updatable = false)
    public AfterSalesItem getAfterSalesItem() {
        return afterSalesItem;
    }

    public void setAfterSalesItem(AfterSalesItem afterSalesItem) {
        this.afterSalesItem = afterSalesItem;
    }

    /**
     * 售后单实体
     */
    @JsonIgnore
    private AfterSales afterSales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_sales_id", insertable = false, updatable = false)
    public AfterSales getAfterSales() {
        return afterSales;
    }

    public void setAfterSales(AfterSales afterSales) {
        this.afterSales = afterSales;
    }

    /**
     * 退款分配记录
     */
    private List<AfterSalesRefundAlloc> afterSalesRefundAllocList = new LinkedList<AfterSalesRefundAlloc>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "afterSalesRefund")
    public List<AfterSalesRefundAlloc> getAfterSalesRefundAllocList() {
        return afterSalesRefundAllocList;
    }

    public void setAfterSalesRefundAllocList(List<AfterSalesRefundAlloc> afterSalesRefundAllocList) {
        this.afterSalesRefundAllocList = afterSalesRefundAllocList;
    }
    @Override
    public String toString() {
        StringBuilder strBuf = new StringBuilder("AfterSalesRefund{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("afterSalesId=").append(this.afterSalesId);
        strBuf.append(", ");
        strBuf.append("afterSalesItemId=").append(this.afterSalesItemId);
        strBuf.append(", ");
        strBuf.append("fee=").append(this.fee);
        strBuf.append(", ");
        strBuf.append("onlineRefundId=").append(this.onlineRefundId);
        strBuf.append(", ");
        strBuf.append("onlineFee=").append(this.onlineFee);
        strBuf.append(", ");
        strBuf.append("offlineRefundId=").append(this.offlineRefundId);
        strBuf.append(", ");
        strBuf.append("offlineFee=").append(this.offlineFee);
        strBuf.append(", ");
        strBuf.append("payment=").append(this.payment);
        strBuf.append(", ");
        strBuf.append("paymentTime=").append(this.paymentTime);
        strBuf.append(", ");
        strBuf.append("refundMethod=").append(this.refundMethod);
        strBuf.append(", ");
        strBuf.append("alipayNo=").append(this.alipayNo);
        strBuf.append(", ");
        strBuf.append("bank=").append(this.bank);
        strBuf.append(", ");
        strBuf.append("bankAccout=").append(this.bankAccout);
        strBuf.append(", ");
        strBuf.append("bankUser=").append(this.bankUser);
        strBuf.append(", ");
        strBuf.append("refundTime=").append(this.refundTime);
        strBuf.append(", ");
        strBuf.append("operatorId=").append(this.operatorId);
        strBuf.append(", ");
        strBuf.append("createTime=").append(this.createTime);
        strBuf.append(", ");
        strBuf.append("updateTime=").append(this.updateTime);
        strBuf.append(", ");
        strBuf.append("version=").append(this.version);
        strBuf.append("}");
        return strBuf.toString();
    }

}