package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.EmployeeUtil;
import com.ejushang.steward.ordercenter.constant.InOutStockType;
import com.ejushang.steward.ordercenter.constant.StorageFlowType;

import javax.persistence.*;
import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_storage_flow")
@Entity
public class StorageFlow implements EntityClass<Integer>, OperableData {

    private Integer id;

    /**
     * 类型(出/入库)
     */
    private StorageFlowType type;

    /**
     * 出入库类型(采购、调拨...)
     */
    private InOutStockType inOutStockType;

    /**
     * 操作数量
     */
    private Integer amount;
    /**
     * 操作前的库存
     */
    private Integer beforeAmount;

    private Date createTime;

    private Integer operatorId;


    /**
     * 订单
     */
    private Order order;

    private Integer orderId;

    /**
     * 备注描述信息
     */
    private String desc;

    /**
     * 库存
     */
    private Integer storageId;

    private Storage storage;

    @Column(name = "before_amount")
    public Integer getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(Integer beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    /**
     * 操作后数量
     *
     * @return
     */
    @Transient
    public Integer getAfterAmount() {
        return StorageFlowType.OUT_STOCK.equals(type)
                ? beforeAmount - amount
                : beforeAmount + amount;
    }

    @Column(name = "storage_id")
    @Basic
    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_id", insertable = false, updatable = false)
    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }


    @Column(name = "description")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @javax.persistence.Column(name = "in_out_stock_type")
    @Enumerated(EnumType.STRING)
    public InOutStockType getInOutStockType() {
        return inOutStockType;
    }

    public void setInOutStockType(InOutStockType inOutStockType) {
        this.inOutStockType = inOutStockType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @javax.persistence.Column(name = "order_id")
    @Basic
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "type")
    @Enumerated(EnumType.STRING)
    public StorageFlowType getType() {
        return type;
    }

    public void setType(StorageFlowType type) {
        this.type = type;
    }


    @javax.persistence.Column(name = "amount")
    @Basic
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
    }

    @Override
    @Transient
    public Date getUpdateTime() {
        return null;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }


    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    @Transient
    public Employee getOperator() {
        return EmployeeUtil.getOperator(operatorId);
    }

    @Transient
    public String getInOutStockTypeValue() {
        return inOutStockType.toString();
    }

}
