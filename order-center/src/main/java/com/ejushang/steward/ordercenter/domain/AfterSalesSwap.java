package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.Money;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 换货表
 * @Author Channel
 * @Date 2014-08-07
 */
@Table(name = "t_after_sales_swap")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSalesSwap implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 订单ID
     */
    private Integer orderId;
    /**
     * 订单项ID
     */
    private Integer orderItemId;
    /**
     * 售后工单ID
     */
    private Integer afterSalesId;
    /**
     * 退货ID
     */
    private Integer afterSalesRefundGoodsId;
    /**
     * 换货ID
     */
    private Integer afterSalesSwapId;
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
    * 获取"退货ID"
    */
    @javax.persistence.Column(name = "after_sales_refund_goods_id")
    @Basic
    public Integer getAfterSalesRefundGoodsId() {
        return afterSalesRefundGoodsId;
    }

    /**
     * 设置"退货ID"
     */
    public void setAfterSalesRefundGoodsId(Integer afterSalesRefundGoodsId) {
        this.afterSalesRefundGoodsId = afterSalesRefundGoodsId;
    }
    /**
    * 获取"换货ID"
    */
    @javax.persistence.Column(name = "after_sales_swap_id")
    @Basic
    public Integer getAfterSalesSwapId() {
        return afterSalesSwapId;
    }

    /**
     * 设置"换货ID"
     */
    public void setAfterSalesSwapId(Integer afterSalesSwapId) {
        this.afterSalesSwapId = afterSalesSwapId;
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
        StringBuilder strBuf = new StringBuilder("AfterSalesSwap{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("orderId=").append(this.orderId);
        strBuf.append(", ");
        strBuf.append("orderItemId=").append(this.orderItemId);
        strBuf.append(", ");
        strBuf.append("afterSalesId=").append(this.afterSalesId);
        strBuf.append(", ");
        strBuf.append("afterSalesRefundGoodsId=").append(this.afterSalesRefundGoodsId);
        strBuf.append(", ");
        strBuf.append("afterSalesSwapId=").append(this.afterSalesSwapId);
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