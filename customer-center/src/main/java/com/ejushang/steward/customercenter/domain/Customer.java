package com.ejushang.steward.customercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 11:39
 */
@Table(name = "t_customer")
@Entity
public class Customer implements EntityClass<Integer>,OperableData{

    private Integer id;
    private String realName;
    private String phone;
    private Date birthday;
    private String state;
    private String city;
    private String district;
    private String address;
    private Integer bonusPoint;
    private Integer grade;
    private Money totalTradeFee;
    private Integer tradeCount;
    private Date lastTradeTime;
    private Date createTime;
    private Date updateTime;
    //操作人ID
    private Integer operatorId;
    private String email;
    private Set<CustomerMobile> mobiles=new HashSet<CustomerMobile>();
    private Set<CustomerTag> tags=new HashSet<CustomerTag>();
    private Set<Shop> shops=new HashSet<Shop>();
    private Set<PlatformUser> platformUsers =new HashSet<PlatformUser>();


    @OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,targetEntity=PlatformUser.class, mappedBy = "customer")
    public Set<PlatformUser> getPlatformUsers() {
        return platformUsers;
    }

    public void setPlatformUsers(Set<PlatformUser> platformUsers) {
        this.platformUsers = platformUsers;
    }

    @ManyToMany(fetch=FetchType.LAZY,targetEntity=Shop.class)
    @JoinTable(
            name="t_customer_shop",
            joinColumns=@JoinColumn(name="customer_id",referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="shop_id",referencedColumnName="id")
    )
    @JsonIgnore
    public Set<Shop> getShops() {
        return shops;
    }

    public void setShops(Set<Shop> shops) {
        this.shops = shops;
    }

    @ManyToMany(fetch=FetchType.LAZY,targetEntity=CustomerTag.class)
    @JoinTable(
            name="t_customer_customertag",
            joinColumns=@JoinColumn(name="customer_id"),
            inverseJoinColumns=@JoinColumn(name="tag_id")
    )
    public Set<CustomerTag> getTags() {
        return tags;
    }

    public void setTags(Set<CustomerTag> tags) {
        this.tags = tags;
    }

    @OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,targetEntity=CustomerMobile.class)
    @JoinColumn(name="customer_id")
    public Set<CustomerMobile> getMobiles() {
        return mobiles;
    }

    public void setMobiles(Set<CustomerMobile> mobiles) {
        this.mobiles = mobiles;
    }

    @javax.persistence.Column(name ="real_name")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @javax.persistence.Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @javax.persistence.Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @javax.persistence.Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @javax.persistence.Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @javax.persistence.Column(name = "district")
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @javax.persistence.Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @javax.persistence.Column(name = "bonus_point")
    public Integer getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(Integer bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    @javax.persistence.Column(name = "grade")
    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @javax.persistence.Column(name = "total_trade_fee")
    public Money getTotalTradeFee() {
        return totalTradeFee;
    }

    public void setTotalTradeFee(Money totalTradeFee) {
        this.totalTradeFee = totalTradeFee;
    }

    @javax.persistence.Column(name = "trade_count")
    public Integer getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }

    @javax.persistence.Column(name = "last_trade_time")
    public Date getLastTradeTime() {
        return lastTradeTime;
    }

    public void setLastTradeTime(Date lastTradeTime) {
        this.lastTradeTime = lastTradeTime;
    }

    @JsonIgnore
    @javax.persistence.Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonIgnore
    @javax.persistence.Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @JsonIgnore
    @javax.persistence.Column(name = "operator_id")
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    @javax.persistence.Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public void increaseTradeCount(){
        if(this.tradeCount==null){
            this.tradeCount=0;
        }

        this.tradeCount++;
    }

    public void increaseTotalTradeFee(Money fee) {
        if(this.totalTradeFee==null){
            this.totalTradeFee=Money.valueOf(0);
        }
        if(fee!=null){
            this.totalTradeFee=this.totalTradeFee.add(fee);
        }
    }

    public void decreaseTotalTradeFee(Money fee) {
        if(this.totalTradeFee==null){
            this.totalTradeFee=Money.valueOf(0);
        }
        if(fee!=null){
            this.totalTradeFee=this.totalTradeFee.subtract(fee);
        }
    }

    public void addMobileIfNotExist(String mobile) {
        if(this.mobiles==null){
            this.mobiles=new HashSet<CustomerMobile>(1);
        }
        for(CustomerMobile m:this.mobiles){
            if(mobile.equals(m.getMobile())){
                return;
            }
        }
        this.mobiles.add(new CustomerMobile(mobile));

    }

    public void addPlatformUserIfNotExist(PlatformType platformType, String buyerId) {
        if(this.platformUsers==null){
            this.platformUsers=new HashSet<PlatformUser>();
        }

        for(PlatformUser user:this.platformUsers){
            if(platformType==user.getPlatformType()&&buyerId.equalsIgnoreCase(user.getBuyerId())){
                return;
            }
        }

        this.platformUsers.add(new PlatformUser(platformType,buyerId));

    }

    public void addShopIfNotExist(Integer shopId) {
        if(this.shops==null){
            this.shops=new HashSet<Shop>(1);
        }

        for(Shop shop:this.shops){
            if(shop.getId().equals(shopId)){
                return;
            }
        }

        Shop shop=new Shop();
        shop.setId(shopId);
        this.shops.add(shop);

    }
}
