package com.ejushang.steward.customercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;

import javax.persistence.*;

/**
 * Created by: codec.yang
 * Date: 2014/8/6 15:20
 */

@Table(name = "t_customer_customertag")
@Entity
public class CustomerCustomerTag implements EntityClass<Integer>{

    private Integer id;
    private Integer customerId;
    private Integer tagId;

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

    @javax.persistence.Column(name = "tag_id")
    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}
