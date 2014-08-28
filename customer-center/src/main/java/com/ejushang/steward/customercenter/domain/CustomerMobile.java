package com.ejushang.steward.customercenter.domain;


import com.ejushang.steward.common.domain.util.EntityClass;

import javax.persistence.*;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 14:37
 */
@Table(name = "t_customer_mobile")
@Entity
public class CustomerMobile implements EntityClass<Integer> {

    private Integer id;
    private String mobile;

    public CustomerMobile(){}

    public CustomerMobile(String mobile){
        this.mobile=mobile;
    }


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name ="mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
