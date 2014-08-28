package com.ejushang.steward.customercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;

import javax.persistence.*;

/**
 * User:moon
 * Date: 14-8-8
 * Time: 下午3:23
 */
@Table(name = "t_customer_shop")
@Entity
public class CustomerShop  implements EntityClass<Integer> {

    private Integer id;
    private Integer customerId;
    private Integer shopId;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "customer_id")
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @javax.persistence.Column(name = "shop_id")
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}
