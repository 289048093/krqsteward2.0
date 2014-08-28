package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.util.Money;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_original_order_item")
@Entity
public class OriginalOrderItem implements EntityClass<Integer>{

    private Integer id;

    private String sku;

    private String outerSku;

    private String title;

    private Money price = Money.valueOf(0);

    private Long buyCount;

    /** 商品金额 */
    private Money totalFee = Money.valueOf(0);
    /** 应付金额 */
    private Money payableFee = Money.valueOf(0);
    /** 实付金额 */
    private Money actualFee = Money.valueOf(0);

    /** 子订单级订单优惠金额 */
    private Money discountFee = Money.valueOf(0);
    /** 手工调整金额 */
    private Money adjustFee = Money.valueOf(0);
    /** 分摊之后的实付金额 */
    private Money divideOrderFee = Money.valueOf(0);
    /** 优惠分摊 */
    private Money partMjzDiscount = Money.valueOf(0);
    /** 订单所有优惠的优惠分摊 */
    private Money allPartMjzDiscount = Money.valueOf(0);

    /**
     * 交易平台子订单ID
     */
    private String platformSubOrderNo;

    private Integer originalOrderId;

    @JsonIgnore
    private OriginalOrder originalOrder;

    private List<OriginalOrderBrand> originalOrderBrands=new ArrayList<OriginalOrderBrand>(0);

    @OneToMany (fetch = FetchType.LAZY, mappedBy = "originalOrderItem")
    public List<OriginalOrderBrand> getOriginalOrderBrands() {
        return originalOrderBrands;
    }

    public void setOriginalOrderBrands(List<OriginalOrderBrand> originalOrderBrands) {
        this.originalOrderBrands = originalOrderBrands;
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


    @javax.persistence.Column(name = "sku")
    @Basic
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @javax.persistence.Column(name = "outer_sku")
    @Basic
    public String getOuterSku() {
        return outerSku;
    }

    public void setOuterSku(String outerSku) {
        this.outerSku = outerSku;
    }

    @javax.persistence.Column(name = "title")
    @Basic
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @javax.persistence.Column(name = "price")
    @Basic
    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }


    @javax.persistence.Column(name = "buy_count")
    @Basic
    public Long getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Long buyCount) {
        this.buyCount = buyCount;
    }


    @javax.persistence.Column(name = "total_fee")
    @Basic
    public Money getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Money totalFee) {
        this.totalFee = totalFee;
    }

    @javax.persistence.Column(name = "payable_fee")
    @Basic
    public Money getPayableFee() {
        return payableFee;
    }

    public void setPayableFee(Money payableFee) {
        this.payableFee = payableFee;
    }

    @javax.persistence.Column(name = "actual_fee")
    @Basic
    public Money getActualFee() {
        return actualFee;
    }

    public void setActualFee(Money actualFee) {
        this.actualFee = actualFee;
    }

    @javax.persistence.Column(name = "discount_fee")
    public Money getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Money discountFee) {
        this.discountFee = discountFee;
    }

    @javax.persistence.Column(name = "adjust_fee")
    public Money getAdjustFee() {
        return adjustFee;
    }

    public void setAdjustFee(Money adjustFee) {
        this.adjustFee = adjustFee;
    }

    @javax.persistence.Column(name = "divide_order_fee")
    public Money getDivideOrderFee() {
        return divideOrderFee;
    }

    public void setDivideOrderFee(Money divideOrderFee) {
        this.divideOrderFee = divideOrderFee;
    }

    @javax.persistence.Column(name = "part_mjz_discount")
    public Money getPartMjzDiscount() {
        return partMjzDiscount;
    }

    public void setPartMjzDiscount(Money partMjzDiscount) {
        this.partMjzDiscount = partMjzDiscount;
    }

    @javax.persistence.Column(name = "all_part_mjz_discount")
    public Money getAllPartMjzDiscount() {
        return allPartMjzDiscount;
    }

    public void setAllPartMjzDiscount(Money allPartMjzDiscount) {
        this.allPartMjzDiscount = allPartMjzDiscount;
    }

    @javax.persistence.Column(name = "platform_sub_order_no")
    @Basic
    public String getPlatformSubOrderNo() {
        return platformSubOrderNo;
    }

    public void setPlatformSubOrderNo(String platformSubOrderNo) {
        this.platformSubOrderNo = platformSubOrderNo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="original_order_id", insertable = false, updatable = false)
    public OriginalOrder getOriginalOrder() {
        return originalOrder;
    }

    public void setOriginalOrder(OriginalOrder originalOrder) {
        this.originalOrder = originalOrder;
    }


    /**
     * 计算订单项金额, 一口价 * 数量 - 订单项优惠
     */
    public Money calculateOrderItemFee() {
        return price.multiply(buyCount).subtract(discountFee);
    }

}
