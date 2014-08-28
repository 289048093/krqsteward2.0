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
@Table(name = "t_refund_alloc")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class RefundAlloc implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 原退款单ID
     */
    private Integer refundId;
    /**
     * !RefundClass:退款类型;
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
    * 获取"原退款单ID"
    */
    @javax.persistence.Column(name = "refund_id")
    @Basic
    public Integer getRefundId() {
        return refundId;
    }

    /**
     * 设置"原退款单ID"
     */
    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }
    /**
    * 获取"!RefundClass:退款类型;"
    */
    @javax.persistence.Column(name = "type")
    @Basic
    public RefundClass getType() {
        return type;
    }

    /**
     * 设置"!RefundClass:退款类型;"
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

    @Override
    public String toString() {
        StringBuilder strBuf = new StringBuilder("RefundAlloc{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("refundId=").append(this.refundId);
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
        strBuf.append("refundTime=").append(this.refundTime);
        strBuf.append(", ");
        strBuf.append("operatorId=").append(this.operatorId);
        strBuf.append(", ");
        strBuf.append("createTime=").append(this.createTime);
        strBuf.append(", ");
        strBuf.append("updateTime=").append(this.updateTime);
        strBuf.append("}");
        return strBuf.toString();
    }

}