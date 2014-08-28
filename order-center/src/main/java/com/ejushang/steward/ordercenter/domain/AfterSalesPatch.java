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

import com.ejushang.steward.ordercenter.constant.OrderItemType;

/**
 * 补货表
 * @Author Channel
 * @Date 2014-08-22
 */
@Table(name = "t_after_sales_patch")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSalesPatch implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 所属售后工单ID
     */
    private Integer afterSalesId;
    /**
     * 售后工单项ID
     */
    private Integer afterSalesItemId;
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 补货数量（用来支持1*N，N*N，N*1）
     */
    private Integer count;
    /**
     * !OrderItemType:订单类型方便生成发货单;
     */
    private OrderItemType orderItemType;
    /**
     * 服务补差金额
     */
    private Money paymentServiceFee = Money.valueOf(0);
    /**
     * 邮费补差金额
     */
    private Money paymentShippingFee = Money.valueOf(0);
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
    * 获取"售后工单项ID"
    */
    @javax.persistence.Column(name = "after_sales_item_id")
    @Basic
    public Integer getAfterSalesItemId() {
        return afterSalesItemId;
    }

    /**
     * 设置"售后工单项ID"
     */
    public void setAfterSalesItemId(Integer afterSalesItemId) {
        this.afterSalesItemId = afterSalesItemId;
    }
    /**
    * 获取"商品id"
    */
    @javax.persistence.Column(name = "product_id")
    @Basic
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置"商品id"
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    /**
    * 获取"补货数量（用来支持1*N，N*N，N*1）"
    */
    @javax.persistence.Column(name = "count")
    @Basic
    public Integer getCount() {
        return count;
    }

    /**
     * 设置"补货数量（用来支持1*N，N*N，N*1）"
     */
    public void setCount(Integer count) {
        this.count = count;
    }
    /**
    * 获取"!OrderItemType:订单类型方便生成发货单;"
    */
    @javax.persistence.Column(name = "order_item_type")
    @Basic
    public OrderItemType getOrderItemType() {
        return orderItemType;
    }

    /**
     * 设置"!OrderItemType:订单类型方便生成发货单;"
     */
    public void setOrderItemType(OrderItemType orderItemType) {
        this.orderItemType = orderItemType;
    }
    /**
    * 获取"服务补差金额"
    */
    @javax.persistence.Column(name = "payment_service_fee")
    @Basic
    public Money getPaymentServiceFee() {
        return paymentServiceFee;
    }

    /**
     * 设置"服务补差金额"
     */
    public void setPaymentServiceFee(Money paymentServiceFee) {
        this.paymentServiceFee = paymentServiceFee;
    }
    /**
    * 获取"邮费补差金额"
    */
    @javax.persistence.Column(name = "payment_shipping_fee")
    @Basic
    public Money getPaymentShippingFee() {
        return paymentShippingFee;
    }

    /**
     * 设置"邮费补差金额"
     */
    public void setPaymentShippingFee(Money paymentShippingFee) {
        this.paymentShippingFee = paymentShippingFee;
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
     * 售后单实体
     */
    @JsonIgnore
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        StringBuilder strBuf = new StringBuilder("AfterSalesPatch{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("afterSalesId=").append(this.afterSalesId);
        strBuf.append(", ");
        strBuf.append("afterSalesItemId=").append(this.afterSalesItemId);
        strBuf.append(", ");
        strBuf.append("productId=").append(this.productId);
        strBuf.append(", ");
        strBuf.append("count=").append(this.count);
        strBuf.append(", ");
        strBuf.append("orderItemType=").append(this.orderItemType);
        strBuf.append(", ");
        strBuf.append("paymentServiceFee=").append(this.paymentServiceFee);
        strBuf.append(", ");
        strBuf.append("paymentShippingFee=").append(this.paymentShippingFee);
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