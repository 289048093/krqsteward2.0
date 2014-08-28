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

import com.ejushang.steward.ordercenter.constant.AfterSalesStatus;

/**
 * 售后流程日志表

 * @Author Channel
 * @Date 2014-08-22
 */
@Table(name = "t_after_sales_flowlog")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSalesFlowLog implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 售后工单ID
     */
    private Integer afterSalesId;
    /**
     * !AfterSalesStatus:当前状态（修改后状态）;
     */
    private AfterSalesStatus status;
    /**
     * !AfterSalesStatus:修改前状态;
     */
    private AfterSalesStatus statusBefore;
    /**
     * 流程转向原因备注
     */
    private String remark;
    /**
     * 操作人员ID
     */
    private Integer operatorId;
    /**
     * 操作人员姓名
     */
    private String operatorName;
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
    * 获取"!AfterSalesStatus:当前状态（修改后状态）;"
    */
    @javax.persistence.Column(name = "status")
    @Basic
    public AfterSalesStatus getStatus() {
        return status;
    }

    /**
     * 设置"!AfterSalesStatus:当前状态（修改后状态）;"
     */
    public void setStatus(AfterSalesStatus status) {
        this.status = status;
    }
    /**
    * 获取"!AfterSalesStatus:修改前状态;"
    */
    @javax.persistence.Column(name = "status_before")
    @Basic
    public AfterSalesStatus getStatusBefore() {
        return statusBefore;
    }

    /**
     * 设置"!AfterSalesStatus:修改前状态;"
     */
    public void setStatusBefore(AfterSalesStatus statusBefore) {
        this.statusBefore = statusBefore;
    }
    /**
    * 获取"流程转向原因备注"
    */
    @javax.persistence.Column(name = "remark")
    @Basic
    public String getRemark() {
        return remark;
    }

    /**
     * 设置"流程转向原因备注"
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
    * 获取"操作人员ID"
    */
    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    /**
     * 设置"操作人员ID"
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
    /**
    * 获取"操作人员姓名"
    */
    @javax.persistence.Column(name = "operator_name")
    @Basic
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * 设置"操作人员姓名"
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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
        StringBuilder strBuf = new StringBuilder("AfterSalesFlowLog{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("afterSalesId=").append(this.afterSalesId);
        strBuf.append(", ");
        strBuf.append("status=").append(this.status);
        strBuf.append(", ");
        strBuf.append("statusBefore=").append(this.statusBefore);
        strBuf.append(", ");
        strBuf.append("remark=").append(this.remark);
        strBuf.append(", ");
        strBuf.append("operatorId=").append(this.operatorId);
        strBuf.append(", ");
        strBuf.append("operatorName=").append(this.operatorName);
        strBuf.append(", ");
        strBuf.append("createTime=").append(this.createTime);
        strBuf.append(", ");
        strBuf.append("updateTime=").append(this.updateTime);
        strBuf.append("}");
        return strBuf.toString();
    }

}