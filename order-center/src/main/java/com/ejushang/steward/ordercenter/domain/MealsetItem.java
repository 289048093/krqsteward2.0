package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.util.Money;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_mealset_item")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class MealsetItem implements EntityClass<Integer> {

    private Integer id;

    /**
     * 套餐价
     */
    private Money price = Money.valueOf(0);

    /**
     * 套餐中的数量
     */
    private Integer amount;

    /**
     * 套餐id
     */
    private Integer mealsetId;


    @JsonIgnore
    private Mealset mealset;

    /**
     * 商品id
     */
    private Integer productId;

    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mealset_id", insertable = false, updatable = false)
    @JsonIgnore
    public Mealset getMealset() {
        return mealset;
    }

    public void setMealset(Mealset mealset) {
        this.mealset = mealset;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "mealset_id")
    @Basic
    public Integer getMealsetId() {
        return mealsetId;
    }

    public void setMealsetId(Integer mealsetId) {
        this.mealsetId = mealsetId;
    }


    @javax.persistence.Column(name = "product_id")
    @Basic
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }


    @javax.persistence.Column(name = "price")
    @Basic
    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }


    @javax.persistence.Column(name = "amount")
    @Basic
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MealsetItem{" +
                "id=" + id +
                ", price=" + price +
                ", amount=" + amount +
                ", mealsetId=" + mealsetId +
                ", mealset=" + mealset.getName() +
                ", productId=" + productId +
                ", product=" + product.getName() +
                '}';
    }
}
