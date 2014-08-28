package com.ejushang.steward.customercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 15:27
 */

@Table(name = "t_platform_user")
@Entity
public class PlatformUser implements EntityClass<Integer> {

    private Integer id;
    private Integer customerId;

    private Customer customer;
    private PlatformType platformType;
    private String buyerId;

    public PlatformUser(){}

    public PlatformUser(PlatformType platformType,String buyerId){
        this.platformType=platformType;
        this.buyerId=buyerId;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name ="customer_id")
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    @JsonIgnore
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Enumerated(EnumType.STRING)
    @javax.persistence.Column(name ="platform_type")
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @javax.persistence.Column(name ="buyer_id")
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
}
