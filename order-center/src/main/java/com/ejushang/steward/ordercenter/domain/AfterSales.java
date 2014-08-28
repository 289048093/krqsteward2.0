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

import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.constant.AfterSalesStatus;
import com.ejushang.steward.ordercenter.constant.AfterSalesSource;

/**
 * 售后工单表
 * @Author Channel
 * @Date 2014-08-22
 */
@Table(name = "t_after_sales")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSales implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 售后工单编号
     */
    private String code;
    /**
     * AfterSalesSource:售后来源;ORDER:普通订单,VISIT:回访单;
     */
    private AfterSalesSource source;
    /**
     * 订单ID
     */
    private Integer orderId;
    /**
     * 回访单ID
     */
    private Integer revisitId;
    /**
     * 客服人员Id
     */
    private Integer serviceUserId;
    /**
     * 客服人员名称
     */
    private String serviceUserName;
    /**
     * 售后原因码
     */
    private String reasonCode;
    /**
     * 售后原因
     */
    private String reason;
    /**
     * 售后备注
     */
    private String remark;
    /**
     * AfterSalesStatus:售后单状态;SAVE:处理中,CHECK:待审批,ACCEPT:审批通过,REJECT:审批驳回,FINISH:已结束,CANCEL:已作废;
     */
    private AfterSalesStatus status;
    /**
     * !AfterSalesStatus:修改前状态;
     */
    private AfterSalesStatus statusBefore;
    /**
     * 最后一次状态流转原因
     */
    private String statusRemark;
    /**
     * 是否发货
     */
    private Boolean send;
    /**
     * 是否已生成发货单
     */
    private Boolean buildOrder;
    /**
     * 品牌id
     */
    private Integer brandId;
    /**
     * 品牌名
     */
    private String brandName;
    /**
     * !PlatformType:外部平台类型(天猫还是京东);
     */
    private PlatformType platformType;
    /**
     * 外部订单号
     */
    private String platformOrderNo;
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
    * 获取"售后工单编号"
    */
    @javax.persistence.Column(name = "code")
    @Basic
    public String getCode() {
        return code;
    }

    /**
     * 设置"售后工单编号"
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
    * 获取"AfterSalesSource:售后来源;ORDER:普通订单,VISIT:回访单;"
    */
    @javax.persistence.Column(name = "source")
    @Enumerated(EnumType.STRING)
    public AfterSalesSource getSource() {
        return source;
    }

    /**
     * 设置"AfterSalesSource:售后来源;ORDER:普通订单,VISIT:回访单;"
     */
    public void setSource(AfterSalesSource source) {
        this.source = source;
    }
    /**
    * 获取"订单ID"
    */
    @javax.persistence.Column(name = "order_id")
    @Basic
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置"订单ID"
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    /**
    * 获取"回访单ID"
    */
    @javax.persistence.Column(name = "revisit_id")
    @Basic
    public Integer getRevisitId() {
        return revisitId;
    }

    /**
     * 设置"回访单ID"
     */
    public void setRevisitId(Integer revisitId) {
        this.revisitId = revisitId;
    }
    /**
    * 获取"客服人员Id"
    */
    @javax.persistence.Column(name = "service_user_id")
    @Basic
    public Integer getServiceUserId() {
        return serviceUserId;
    }

    /**
     * 设置"客服人员Id"
     */
    public void setServiceUserId(Integer serviceUserId) {
        this.serviceUserId = serviceUserId;
    }
    /**
    * 获取"客服人员名称"
    */
    @javax.persistence.Column(name = "service_user_name")
    @Basic
    public String getServiceUserName() {
        return serviceUserName;
    }

    /**
     * 设置"客服人员名称"
     */
    public void setServiceUserName(String serviceUserName) {
        this.serviceUserName = serviceUserName;
    }
    /**
    * 获取"售后原因码"
    */
    @javax.persistence.Column(name = "reason_code")
    @Basic
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * 设置"售后原因码"
     */
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    /**
    * 获取"售后原因"
    */
    @javax.persistence.Column(name = "reason")
    @Basic
    public String getReason() {
        return reason;
    }

    /**
     * 设置"售后原因"
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
    /**
    * 获取"售后备注"
    */
    @javax.persistence.Column(name = "remark")
    @Basic
    public String getRemark() {
        return remark;
    }

    /**
     * 设置"售后备注"
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
    * 获取"AfterSalesStatus:售后单状态;SAVE:处理中,CHECK:待审批,ACCEPT:审批通过,REJECT:审批驳回,FINISH:已结束,CANCEL:已作废;"
    */
    @javax.persistence.Column(name = "status")
    @Enumerated(EnumType.STRING)
    public AfterSalesStatus getStatus() {
        return status;
    }

    /**
     * 设置"AfterSalesStatus:售后单状态;SAVE:处理中,CHECK:待审批,ACCEPT:审批通过,REJECT:审批驳回,FINISH:已结束,CANCEL:已作废;"
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
    * 获取"最后一次状态流转原因"
    */
    @javax.persistence.Column(name = "status_remark")
    @Basic
    public String getStatusRemark() {
        return statusRemark;
    }

    /**
     * 设置"最后一次状态流转原因"
     */
    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark;
    }
    /**
    * 获取"是否发货"
    */
    @javax.persistence.Column(name = "send")
    @Basic
    public Boolean isSend() {
        return send;
    }

    /**
     * 设置"是否发货"
     */
    public void setSend(Boolean send) {
        this.send = send;
    }
    /**
    * 获取"是否已生成发货单"
    */
    @javax.persistence.Column(name = "build_order")
    @Basic
    public Boolean isBuildOrder() {
        return buildOrder;
    }

    /**
     * 设置"是否已生成发货单"
     */
    public void setBuildOrder(Boolean buildOrder) {
        this.buildOrder = buildOrder;
    }
    /**
    * 获取"品牌id"
    */
    @javax.persistence.Column(name = "brand_id")
    @Basic
    public Integer getBrandId() {
        return brandId;
    }

    /**
     * 设置"品牌id"
     */
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }
    /**
    * 获取"品牌名"
    */
    @javax.persistence.Column(name = "brand_name")
    @Basic
    public String getBrandName() {
        return brandName;
    }

    /**
     * 设置"品牌名"
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    /**
    * 获取"!PlatformType:外部平台类型(天猫还是京东);"
    */
    @javax.persistence.Column(name = "platform_type")
    @Basic
    public PlatformType getPlatformType() {
        return platformType;
    }

    /**
     * 设置"!PlatformType:外部平台类型(天猫还是京东);"
     */
    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }
    /**
    * 获取"外部订单号"
    */
    @javax.persistence.Column(name = "platform_order_no")
    @Basic
    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    /**
     * 设置"外部订单号"
     */
    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
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
     * 售后流程日志
     */
    private List<AfterSalesFlowLog> afterSalesFlowLogList = new LinkedList<AfterSalesFlowLog>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "afterSales")
    public List<AfterSalesFlowLog> getAfterSalesFlowLogList() {
        return afterSalesFlowLogList;
    }

    public void setAfterSalesFlowLogList(List<AfterSalesFlowLog> afterSalesFlowLogList) {
        this.afterSalesFlowLogList = afterSalesFlowLogList;
    }

    /**
     * 售后项
     */
    private List<AfterSalesItem> afterSalesItemList = new LinkedList<AfterSalesItem>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "afterSales")
    public List<AfterSalesItem> getAfterSalesItemList() {
        return afterSalesItemList;
    }

    public void setAfterSalesItemList(List<AfterSalesItem> afterSalesItemList) {
        this.afterSalesItemList = afterSalesItemList;
    }

    /**
     * 售后原因列表
     */
    private List<AfterSalesReason> afterSalesReasonList = new LinkedList<AfterSalesReason>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "afterSales")
    public List<AfterSalesReason> getAfterSalesReasonList() {
        return afterSalesReasonList;
    }

    public void setAfterSalesReasonList(List<AfterSalesReason> afterSalesReasonList) {
        this.afterSalesReasonList = afterSalesReasonList;
    }

    /**
     * 附件列表
     */
    private List<AfterSalesAttachment> afterSalesAttachmentList = new LinkedList<AfterSalesAttachment>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "afterSales")
    public List<AfterSalesAttachment> getAfterSalesAttachmentList() {
        return afterSalesAttachmentList;
    }

    public void setAfterSalesAttachmentList(List<AfterSalesAttachment> afterSalesAttachmentList) {
        this.afterSalesAttachmentList = afterSalesAttachmentList;
    }

    /**
     * 原始订单实体
     */
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 售后工单所有补货项
     */
    @JsonIgnore
    private List<AfterSalesPatch> afterSalesPatchList = new LinkedList<AfterSalesPatch>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "afterSales")
    public List<AfterSalesPatch> getAfterSalesPatchList() {
        return afterSalesPatchList;
    }

    public void setAfterSalesPatchList(List<AfterSalesPatch> afterSalesPatchList) {
        this.afterSalesPatchList = afterSalesPatchList;
    }

    /**
     * 售后工单所有退款项
     */
    @JsonIgnore
    private List<AfterSalesRefund> afterSalesRefundList = new LinkedList<AfterSalesRefund>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "afterSales")
    public List<AfterSalesRefund> getAfterSalesRefundList() {
        return afterSalesRefundList;
    }

    public void setAfterSalesRefundList(List<AfterSalesRefund> afterSalesRefundList) {
        this.afterSalesRefundList = afterSalesRefundList;
    }

    /**
     * 售后工单所有退货项
     */
    @JsonIgnore
    private List<AfterSalesRefundGoods> afterSalesRefundGoodsList = new LinkedList<AfterSalesRefundGoods>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "afterSales")
    public List<AfterSalesRefundGoods> getAfterSalesRefundGoodsList() {
        return afterSalesRefundGoodsList;
    }

    public void setAfterSalesRefundGoodsList(List<AfterSalesRefundGoods> afterSalesRefundGoodsList) {
        this.afterSalesRefundGoodsList = afterSalesRefundGoodsList;
    }



    /**
     * 发货单
     */
    private AfterSalesSend afterSalesSend;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "afterSales")
    public AfterSalesSend getAfterSalesSend() {
        return afterSalesSend;
    }

    public void setAfterSalesSend(AfterSalesSend afterSalesSend) {
        this.afterSalesSend = afterSalesSend;
    }

    @Override
    public String toString() {
        StringBuilder strBuf = new StringBuilder("AfterSales{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("code=").append(this.code);
        strBuf.append(", ");
        strBuf.append("source=").append(this.source);
        strBuf.append(", ");
        strBuf.append("orderId=").append(this.orderId);
        strBuf.append(", ");
        strBuf.append("revisitId=").append(this.revisitId);
        strBuf.append(", ");
        strBuf.append("serviceUserId=").append(this.serviceUserId);
        strBuf.append(", ");
        strBuf.append("serviceUserName=").append(this.serviceUserName);
        strBuf.append(", ");
        strBuf.append("reasonCode=").append(this.reasonCode);
        strBuf.append(", ");
        strBuf.append("reason=").append(this.reason);
        strBuf.append(", ");
        strBuf.append("remark=").append(this.remark);
        strBuf.append(", ");
        strBuf.append("status=").append(this.status);
        strBuf.append(", ");
        strBuf.append("statusBefore=").append(this.statusBefore);
        strBuf.append(", ");
        strBuf.append("statusRemark=").append(this.statusRemark);
        strBuf.append(", ");
        strBuf.append("send=").append(this.send);
        strBuf.append(", ");
        strBuf.append("buildOrder=").append(this.buildOrder);
        strBuf.append(", ");
        strBuf.append("brandId=").append(this.brandId);
        strBuf.append(", ");
        strBuf.append("brandName=").append(this.brandName);
        strBuf.append(", ");
        strBuf.append("platformType=").append(this.platformType);
        strBuf.append(", ");
        strBuf.append("platformOrderNo=").append(this.platformOrderNo);
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