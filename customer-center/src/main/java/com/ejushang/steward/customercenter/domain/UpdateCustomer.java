package com.ejushang.steward.customercenter.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User:moon
 * Date: 14-8-15
 * Time: 下午2:46
 */
public class UpdateCustomer {

    private Integer id;
    private String realName;
    private String phone;
    private Date birthday;
    private String address;
    private String email;
    private Set<CustomerMobile> mobiles=new HashSet<CustomerMobile>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<CustomerMobile> getMobiles() {
        return mobiles;
    }

    public void setMobiles(Set<CustomerMobile> mobiles) {
        this.mobiles = mobiles;
    }
}
