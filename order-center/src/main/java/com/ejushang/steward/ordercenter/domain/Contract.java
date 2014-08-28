package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.util.Money;

import javax.persistence.*;
import java.util.Date;

/**
 * User: 龙清华
 * Date: 14-4-12
 * Time: 下午1:27
 */
@javax.persistence.Table(name = "t_contract")
@Entity
public class Contract implements EntityClass<Integer>, OperableData {
    private Integer id;
    /**
     * 合同编号
     */
    private String code;
    /**
     * 保证金
     */
    private Money deposit = Money.valueOf(0);
    /**
     * 服务费
     */
    private Money serviceFee = Money.valueOf(0);
    /**
     * 给客户开发票时，易居尚平台的抬头
     */
    private String invoiceEJSTitle;
    /**
     * 给客户开发票时，非易居尚平台的抬头
     */
    private String invoiceOtherTitle;
    /**
     * 第三方平台销售是否补开发票给易居尚
     */
    private String invoiceToEJS;
    /**
     * 其它条款
     */
    private String otherRule;
    /**
     * 补充协议
     */
    private String remark;
    /**
     * 滞纳金情况
     */
    private String overdueFine;
    /**
     * 拍摄费用情况
     */
    private String shotFeeType;
    /**
     * 物流补贴
     */
    private String shippingFeeType;
    /**
     * 佣金
     */
    private String commission;

//    /**
//     * 第三方平台费用情况
//     */
//    private String thirdPlatformFeeType;
//    /**
//     * 到易居尚仓库的费用情况
//     */
//    private String toEJSFeeType;

    /**
     * 合同开始时间
     */
    private Date beginTime;
    /**
     * 合同结束时间
     */
    private Date endTime;
    /**
     * 实际中止时间
     */
    private Date realEndTime;
    /**
     * 终止原因
     */
    private String endReason;
    /**
     * 合同录入时间
     */
    private Date createTime;
    /**
     * 合同修改时间
     */
    private Date updateTime;
    /**
     * 供应商ID
     */
    private Integer supplierId;
    /**
     * 供应商信息
     */
    private Supplier supplier;

    /**
     * 采购商
     *
     * @return
     */
    private String ejsCompName;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 结算方式
     */
    private String paymentType;
    /**
     * 结算规则，时间要求等
     */
    private String paymentRule;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false)
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "SUPPLIER_ID")
    @Basic
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @javax.persistence.Column(name = "CODE")
    @Basic
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @javax.persistence.Column(name = "payment_type")
    @Basic
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @javax.persistence.Column(name = "payment_rule")
    @Basic
    public String getPaymentRule() {
        return paymentRule;
    }

    public void setPaymentRule(String paymentRule) {
        this.paymentRule = paymentRule;
    }

    @javax.persistence.Column(name = "DEPOSIT")
    @Basic
    public Money getDeposit() {
        return deposit;
    }

    public void setDeposit(Money deposit) {
        this.deposit = deposit;
    }

    @javax.persistence.Column(name = "SERVICE_FEE")
    @Basic
    public Money getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Money serviceFee) {
        this.serviceFee = serviceFee;
    }


    @javax.persistence.Column(name = "INVOICE_EJS_TITLE")
    @Basic
    public String getInvoiceEJSTitle() {
        return invoiceEJSTitle;
    }

    public void setInvoiceEJSTitle(String invoiceEJSTitle) {
        this.invoiceEJSTitle = invoiceEJSTitle;
    }


    @javax.persistence.Column(name = "INVOICE_OTHER_TITLE")
    @Basic
    public String getInvoiceOtherTitle() {
        return invoiceOtherTitle;
    }

    public void setInvoiceOtherTitle(String invoiceOtherTitle) {
        this.invoiceOtherTitle = invoiceOtherTitle;
    }


    @javax.persistence.Column(name = "INVOICE_TO_EJS")
    @Basic
    public String getInvoiceToEJS() {
        return invoiceToEJS;
    }

    public void setInvoiceToEJS(String invoiceToEJS) {
        this.invoiceToEJS = invoiceToEJS;
    }

    @javax.persistence.Column(name = "OTHER_RULE")
    @Basic

    public String getOtherRule() {
        return otherRule;
    }

    public void setOtherRule(String otherRule) {
        this.otherRule = otherRule;
    }

    @javax.persistence.Column(name = "REMARK")
    @Basic
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @javax.persistence.Column(name = "OVERDUE_FINE")
    @Basic
    public String getOverdueFine() {
        return overdueFine;
    }

    public void setOverdueFine(String overdueFine) {
        this.overdueFine = overdueFine;
    }


    @javax.persistence.Column(name = "EJS_COMP_NAME")
    @Basic
    public String getEjsCompName() {
        return ejsCompName;
    }

    public void setEjsCompName(String ejsCompName) {
        this.ejsCompName = ejsCompName;
    }

    @javax.persistence.Column(name = "BEGIN_TIME")
    @Temporal(TemporalType.TIMESTAMP)

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @javax.persistence.Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)


    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @javax.persistence.Column(name = "REAL_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)

    public Date getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(Date realEndTime) {
        this.realEndTime = realEndTime;
    }

    @javax.persistence.Column(name = "END_REASON")
    @Basic
    public String getEndReason() {
        return endReason;
    }

    public void setEndReason(String endReason) {
        this.endReason = endReason;
    }

    @javax.persistence.Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @javax.persistence.Column(name = "UPDATE_TIME")
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

    @javax.persistence.Column(name = "shot_fee_type")
    @Basic
    public String getShotFeeType() {
        return shotFeeType;
    }

    public void setShotFeeType(String shotFeeType) {
        this.shotFeeType = shotFeeType;
    }

    @javax.persistence.Column(name = "shipping_fee_type")
    @Basic
    public String getShippingFeeType() {
        return shippingFeeType;
    }

    public void setShippingFeeType(String shippingFeeType) {
        this.shippingFeeType = shippingFeeType;
    }

//    @javax.persistence.Column(name = "third_platform_fee_type")
//    @Basic
//    public String getThirdPlatformFeeType() {
//        return thirdPlatformFeeType;
//    }
//
//    public void setThirdPlatformFeeType(String thirdPlatformFeeType) {
//        this.thirdPlatformFeeType = thirdPlatformFeeType;
//    }
//
//    @javax.persistence.Column(name = "to_ejs_fee_type")
//    @Basic
//    public String getToEJSFeeType() {
//        return toEJSFeeType;
//    }
//
//    public void setToEJSFeeType(String toEJSFeeType) {
//        this.toEJSFeeType = toEJSFeeType;
//    }


    @javax.persistence.Column(name = "commission")
    @Basic
    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", deposit=" + deposit +
                ", serviceFee=" + serviceFee +
                ", invoiceEJSTitle='" + invoiceEJSTitle + '\'' +
                ", invoiceOtherTitle='" + invoiceOtherTitle + '\'' +
                ", invoiceToEJS='" + invoiceToEJS + '\'' +
                ", otherRule='" + otherRule + '\'' +
                ", remark='" + remark + '\'' +
                ", overdueFine=" + overdueFine +
                ", shotFeeType=" + shotFeeType +
                ", shippingFeeType=" + shippingFeeType +
                ", commission=" + commission +
//                ", thirdPlatformFeeType=" + thirdPlatformFeeType +
//                ", toEJSFeeType=" + toEJSFeeType +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", realEndTime=" + realEndTime +
                ", endReason='" + endReason + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", supplierId=" + supplierId +
                ", supplier=" + supplier +
                ", ejsCompName='" + ejsCompName + '\'' +
                ", operatorId=" + operatorId +
                ", paymentType='" + paymentType + '\'' +
                ", paymentRule='" + paymentRule + '\'' +
                '}';
    }
}
