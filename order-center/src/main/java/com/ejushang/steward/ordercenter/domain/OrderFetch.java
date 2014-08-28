package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.ordercenter.constant.*;

import javax.persistence.*;
import javax.persistence.FetchType;
import java.util.Date;

/**
 * 订单抓单记录
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_order_fetch")
@Entity
public class OrderFetch implements EntityClass<Integer>{

    private Integer id;

    private Date fetchStartTime;

    private Date fetchTime;

    private PlatformType platformType;

    private Date createTime;

    private Integer shopId;

    private FetchDataType fetchDataType;

    private FetchOptType fetchOptType;

    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shop_id", insertable = false, updatable = false)
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "fetch_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFetchStartTime() {
        return fetchStartTime;
    }

    public void setFetchStartTime(Date fetchStartTime) {
        this.fetchStartTime = fetchStartTime;
    }

    @javax.persistence.Column(name = "fetch_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(Date fetchTime) {
        this.fetchTime = UglyTimestampUtil.convertTimestampToDate(fetchTime);;
    }


    @javax.persistence.Column(name = "platform_type")
    @Enumerated(EnumType.STRING)
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }

    @javax.persistence.Column(name = "shop_id")
    @Basic
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Column(name = "fetch_data_type")
    @Basic
    @Enumerated(EnumType.STRING)
    public FetchDataType getFetchDataType() {
        return fetchDataType;
    }

    public void setFetchDataType(FetchDataType fetchDataType) {
        this.fetchDataType = fetchDataType;
    }

    @Column(name = "fetch_opt_type")
    @Basic
    @Enumerated(EnumType.STRING)
    public FetchOptType getFetchOptType() {
        return fetchOptType;
    }

    public void setFetchOptType(FetchOptType fetchOptType) {
        this.fetchOptType = fetchOptType;
    }

    @Override
    public String toString() {
        return "OrderFetch{" +
                "id=" + id +
                ", fetchTime=" + fetchTime +
                ", platformType=" + platformType +
                ", createTime=" + createTime +
                ", shop=" + shop +
                '}';
    }
}
