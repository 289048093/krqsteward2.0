package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.EmployeeUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_brand")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class Brand implements EntityClass<Integer>, OperableData {

    private Integer id;
    /**
     * 品牌代码
     */
    private String code;

    private String name;
    /**
     * 结算方式
     */
    private String paymentType;
    /**
     * 结算规则，时间要求等
     */
    private String paymentRule;


    private boolean deleted;

    private Date createTime;

    private Date updateTime;
    /**
     * 操作人ID
     */
    private Integer operatorId;

    private Integer supplierId;
    /**
     * 供应商信息
     */
    private Supplier supplier;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false)
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @javax.persistence.Column(name = "supplier_id")
    @Basic
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @javax.persistence.Column(name = "code")
    @Basic
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    @javax.persistence.Column(name = "deleted")
    @Basic
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    @Transient
    public String getOperatorName() {
        return EmployeeUtil.getOperatorName(operatorId);
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", paymentRule='" + paymentRule + '\'' +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                ", supplierId=" + supplierId +
                ", supplier=" + supplier +
                '}';
    }
}
