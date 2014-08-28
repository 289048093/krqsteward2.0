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


/**
 * 售后原因表
 * @Author Channel
 * @Date 2014-08-22
 */
@Table(name = "t_after_sales_reason")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSalesReason implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 售后工单ID
     */
    private Integer afterSalesId;
    /**
     * 原因码ID
     */
    private Integer reasonId;
    /**
     * 原因码
     */
    private String reasonCode;
    /**
     * 原因描述
     */
    private String reasonText;
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
    * 获取"售后工单ID"
    */
    @javax.persistence.Column(name = "after_sales_id")
    @Basic
    public Integer getAfterSalesId() {
        return afterSalesId;
    }

    /**
     * 设置"售后工单ID"
     */
    public void setAfterSalesId(Integer afterSalesId) {
        this.afterSalesId = afterSalesId;
    }
    /**
    * 获取"原因码ID"
    */
    @javax.persistence.Column(name = "reason_id")
    @Basic
    public Integer getReasonId() {
        return reasonId;
    }

    /**
     * 设置"原因码ID"
     */
    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }
    /**
    * 获取"原因码"
    */
    @javax.persistence.Column(name = "reason_code")
    @Basic
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * 设置"原因码"
     */
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    /**
    * 获取"原因描述"
    */
    @javax.persistence.Column(name = "reason_text")
    @Basic
    public String getReasonText() {
        return reasonText;
    }

    /**
     * 设置"原因描述"
     */
    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
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


    @Override
    public String toString() {
        StringBuilder strBuf = new StringBuilder("AfterSalesReason{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("afterSalesId=").append(this.afterSalesId);
        strBuf.append(", ");
        strBuf.append("reasonId=").append(this.reasonId);
        strBuf.append(", ");
        strBuf.append("reasonCode=").append(this.reasonCode);
        strBuf.append(", ");
        strBuf.append("reasonText=").append(this.reasonText);
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