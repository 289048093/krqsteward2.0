package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * 原始订单解析记录
 * User: liubin
 * Date: 14-7-7
 */
@Table(name = "t_original_order_brand")
@Entity
public class OriginalOrderBrand implements EntityClass<Integer>{

    private Integer id;

    /**
     * 品牌ID
     */
    private Integer brandId;
    /**
     * 品牌对象
     */
    private Brand brand;

    /**
     * 原始订单项ID
     */
    private Integer originalOrderItemId;

    @JsonIgnore
    private OriginalOrderItem originalOrderItem;

    private Integer originalOrderId;

    @JsonIgnore
    private OriginalOrder originalOrder;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="original_order_id", insertable = false, updatable = false)
    public OriginalOrder getOriginalOrder() {
        return originalOrder;
    }

    public void setOriginalOrder(OriginalOrder originalOrder) {
        this.originalOrder = originalOrder;
    }


    @Column(name = "original_order_id")
    public Integer getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Integer originalOrderId) {
        this.originalOrderId = originalOrderId;
    }


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", insertable = false, updatable = false)
    public Brand getBrand() {
        return brand;
    }


    public void setBrand(Brand brand) {
        this.brand = brand;
    }


    @javax.persistence.Column(name = "brand_id")
    @Basic
    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_order_item_id", insertable = false, updatable = false)
    public OriginalOrderItem getOriginalOrderItem() {
        return originalOrderItem;
    }

    public void setOriginalOrderItem(OriginalOrderItem originalOrderItem) {
        this.originalOrderItem = originalOrderItem;
    }

    @javax.persistence.Column(name = "original_order_item_id")
    @Basic
    public Integer getOriginalOrderItemId() {
        return originalOrderItemId;
    }

    public void setOriginalOrderItemId(Integer originalOrderItemId) {
        this.originalOrderItemId = originalOrderItemId;
    }



}
