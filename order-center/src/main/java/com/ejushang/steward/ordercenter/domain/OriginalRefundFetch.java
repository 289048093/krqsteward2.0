package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.ordercenter.constant.PlatformType;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Baron.Zhang
 * Date: 2014/5/10
 * Time: 11:48
 */
@Entity
@Table(name = "t_original_refund_fetch")
public class OriginalRefundFetch implements EntityClass<Integer> {
    private Integer id;
    private Date fetchTime;
    private PlatformType platformType;
    private Date createTime;
    private Integer shopId;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "fetch_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(Date fetchTime) {
        this.fetchTime = UglyTimestampUtil.convertTimestampToDate(fetchTime);
    }

    @Column(name = "platform_type")
    @Enumerated(EnumType.STRING)
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "shop_id")
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "OriginalRefundFetch{" +
                "id=" + id +
                ", fetchTime=" + fetchTime +
                ", platformType=" + platformType +
                ", createTime=" + createTime +
                ", shopId=" + shopId +
                '}';
    }
}
