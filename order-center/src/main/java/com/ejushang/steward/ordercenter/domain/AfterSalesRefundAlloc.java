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

import com.ejushang.steward.ordercenter.constant.RefundClass;

/**
 * 
 * @Author Channel
 * @Date 2014-08-22
 */
@Table(name = "t_after_sales_refund_alloc")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSalesRefundAlloc implements EntityClass<Integer>, OperableData {

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
     * 售后工单退款ID
     */
    private Integer afterSalesRefundId;
    /**
     * RefundClass:退款类型;GOODS:货款,POST:运费,REFUND_POST:退款运费;
     */
    private RefundClass type;
    /**
     * 总金额
     */
    private Money fee = Money.valueOf(0);
    /**
     * 平台承担金额
     */
    private Money platformFee = Money.valueOf(0);
    /**
     * 品牌商家承担金额
     */
    private Money supplierFee = Money.valueOf(0);
    /**
     * 是否在线退款
     */
    private Boolean online;
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
    * 获取"售后工单退款ID"
    */
    @javax.persistence.Column(name = "after_sales_refund_id")
    @Basic
    public Integer getAfterSalesRefundId() {
        return afterSalesRefundId;
    }

    /**
     * 设置"售后工单退款ID"
     */
    public void setAfterSalesRefundId(Integer afterSalesRefundId) {
        this.afterSalesRefundId = afterSalesRefundId;
    }
    /**
    * 获取"RefundClass:退款类型;GOODS:货款,POST:运费,REFUND_POST:退款运费;"
    */
    @javax.persistence.Column(name = "type")
    @Enumerated(EnumType.STRING)
    public RefundClass getType() {
        return type;
    }

    /**
     * 设置"RefundClass:退款类型;GOODS:货款,POST:运费,REFUND_POST:退款运费;"
     */
    public void setType(RefundClass type) {
        this.type = type;
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
    * 获取"平台承担金额"
    */
    @javax.persistence.Column(name = "platform_fee")
    @Basic
    public Money getPlatformFee() {
        return platformFee;
    }

    /**
     * 设置"平台承担金额"
     */
    public void setPlatformFee(Money platformFee) {
        this.platformFee = platformFee;
    }
    /**
    * 获取"品牌商家承担金额"
    */
    @javax.persistence.Column(name = "supplier_fee")
    @Basic
    public Money getSupplierFee() {
        return supplierFee;
    }

    /**
     * 设置"品牌商家承担金额"
     */
    public void setSupplierFee(Money supplierFee) {
        this.supplierFee = supplierFee;
    }
    /**
    * 获取"是否在线退款"
    */
    @javax.persistence.Column(name = "online")
    @Basic
    public Boolean isOnline() {
        return online;
    }

    /**
     * 设置"是否在线退款"
     */
    public void setOnline(Boolean online) {
        this.online = online;
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
     * 售后单退款实体
     */
    @JsonIgnore
    private AfterSalesRefund afterSalesRefund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_sales_refund_id", insertable = false, updatable = false)
    public AfterSalesRefund getAfterSalesRefund() {
        return afterSalesRefund;
    }

    public void setAfterSalesRefund(AfterSalesRefund afterSalesRefund) {
        this.afterSalesRefund = afterSalesRefund;
    }


    @Override
    public String toString() {
        StringBuilder strBuf = new StringBuilder("AfterSalesRefundAlloc{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("afterSalesId=").append(this.afterSalesId);
        strBuf.append(", ");
        strBuf.append("afterSalesItemId=").append(this.afterSalesItemId);
        strBuf.append(", ");
        strBuf.append("afterSalesRefundId=").append(this.afterSalesRefundId);
        strBuf.append(", ");
        strBuf.append("type=").append(this.type);
        strBuf.append(", ");
        strBuf.append("fee=").append(this.fee);
        strBuf.append(", ");
        strBuf.append("platformFee=").append(this.platformFee);
        strBuf.append(", ");
        strBuf.append("supplierFee=").append(this.supplierFee);
        strBuf.append(", ");
        strBuf.append("online=").append(this.online);
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