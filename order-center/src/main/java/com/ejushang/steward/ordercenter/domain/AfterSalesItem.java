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

import com.ejushang.steward.ordercenter.constant.AfterSalesType;

/**
 * 售后工单项表
 * @Author Channel
 * @Date 2014-08-22
 */
@Table(name = "t_after_sales_item")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSalesItem implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 售后工单ID
     */
    private Integer afterSalesId;
    /**
     * 订单项ID
     */
    private Integer orderItemId;
    /**
     * AfterSalesType:售后类型;REFUND:退款,SWAP:换货,REFUND_GOODS:退货,PATCH:补货;
     */
    private AfterSalesType type;
    /**
     * 是否退款
     */
    private Boolean refund;
    /**
     * 是否退货
     */
    private Boolean refundGoods;
    /**
     * 是否补货
     */
    private Boolean patch;
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
    * 获取"订单项ID"
    */
    @javax.persistence.Column(name = "order_item_id")
    @Basic
    public Integer getOrderItemId() {
        return orderItemId;
    }

    /**
     * 设置"订单项ID"
     */
    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }
    /**
    * 获取"AfterSalesType:售后类型;REFUND:退款,SWAP:换货,REFUND_GOODS:退货,PATCH:补货;"
    */
    @javax.persistence.Column(name = "type")
    @Enumerated(EnumType.STRING)
    public AfterSalesType getType() {
        return type;
    }

    /**
     * 设置"AfterSalesType:售后类型;REFUND:退款,SWAP:换货,REFUND_GOODS:退货,PATCH:补货;"
     */
    public void setType(AfterSalesType type) {
        this.type = type;
    }
    /**
    * 获取"是否退款"
    */
    @javax.persistence.Column(name = "refund")
    @Basic
    public Boolean isRefund() {
        return refund;
    }

    /**
     * 设置"是否退款"
     */
    public void setRefund(Boolean refund) {
        this.refund = refund;
    }
    /**
    * 获取"是否退货"
    */
    @javax.persistence.Column(name = "refund_goods")
    @Basic
    public Boolean isRefundGoods() {
        return refundGoods;
    }

    /**
     * 设置"是否退货"
     */
    public void setRefundGoods(Boolean refundGoods) {
        this.refundGoods = refundGoods;
    }
    /**
    * 获取"是否补货"
    */
    @javax.persistence.Column(name = "patch")
    @Basic
    public Boolean isPatch() {
        return patch;
    }

    /**
     * 设置"是否补货"
     */
    public void setPatch(Boolean patch) {
        this.patch = patch;
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

    /**
     * 售后退款记录
     */
    private AfterSalesRefund afterSalesRefund;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "afterSalesItem")
    public AfterSalesRefund getAfterSalesRefund() {
        return afterSalesRefund;
    }

    public void setAfterSalesRefund(AfterSalesRefund afterSalesRefund) {
        this.afterSalesRefund = afterSalesRefund;
    }

    /**
     * 售后退货记录
     */
    private AfterSalesRefundGoods afterSalesRefundGoods;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "afterSalesItem")
    public AfterSalesRefundGoods getAfterSalesRefundGoods() {
        return afterSalesRefundGoods;
    }

    public void setAfterSalesRefundGoods(AfterSalesRefundGoods afterSalesRefundGoods) {
        this.afterSalesRefundGoods = afterSalesRefundGoods;
    }

    /**
     * 售后补货记录
     */
    private List<AfterSalesPatch> afterSalesPatchList = new LinkedList<AfterSalesPatch>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "afterSalesItem")
    public List<AfterSalesPatch> getAfterSalesPatchList() {
        return afterSalesPatchList;
    }

    public void setAfterSalesPatchList(List<AfterSalesPatch> afterSalesPatchList) {
        this.afterSalesPatchList = afterSalesPatchList;
    }
    @Override
    public String toString() {
        StringBuilder strBuf = new StringBuilder("AfterSalesItem{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("afterSalesId=").append(this.afterSalesId);
        strBuf.append(", ");
        strBuf.append("orderItemId=").append(this.orderItemId);
        strBuf.append(", ");
        strBuf.append("type=").append(this.type);
        strBuf.append(", ");
        strBuf.append("refund=").append(this.refund);
        strBuf.append(", ");
        strBuf.append("refundGoods=").append(this.refundGoods);
        strBuf.append(", ");
        strBuf.append("patch=").append(this.patch);
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