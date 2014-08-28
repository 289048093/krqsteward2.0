package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.constant.ShopType;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_shop")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class Shop implements EntityClass<Integer>, OperableData {

    private Integer id;

    private Integer shopAuthId;

    private String outShopId;

    private String catId;

    private String uid;

    private String nick;

    private String title;

    private String description;

    private String bulletin;

    private String picPath;

    private String itemScore;

    private String serviceScore;

    private String deliveryScore;

    private String deExpress;

    private Boolean enableMsg;

    private String msgTemp;

    private String msgSign;

    private PlatformType platformType;

    private ShopType shopType;

    private Date createTime;

    private Date updateTime;

    private Integer operatorId;

    private Boolean isDelete = false;

    private ShopAuth shopAuth;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_auth_id",unique = true,insertable = false,updatable = false)
    public ShopAuth getShopAuth() {
        return shopAuth;
    }

    public void setShopAuth(ShopAuth shopAuth) {
        this.shopAuth = shopAuth;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "shop_auth_id")
    public Integer getShopAuthId() {
        return shopAuthId;
    }

    public void setShopAuthId(Integer shopAuthId) {
        this.shopAuthId = shopAuthId;
    }

    @javax.persistence.Column(name = "out_shop_id")
    @Basic
    public String getOutShopId() {
        return outShopId;
    }

    public void setOutShopId(String outShopId) {
        this.outShopId = outShopId;
    }


    @javax.persistence.Column(name = "cat_id")
    @Basic
    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    @javax.persistence.Column(name = "uid")
    @Basic
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @javax.persistence.Column(name = "nick")
    @Basic
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


    @javax.persistence.Column(name = "title")
    @Basic
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @javax.persistence.Column(name = "description")
    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @javax.persistence.Column(name = "bulletin")
    @Basic
    public String getBulletin() {
        return bulletin;
    }

    public void setBulletin(String bulletin) {
        this.bulletin = bulletin;
    }


    @javax.persistence.Column(name = "pic_path")
    @Basic
    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }


    @javax.persistence.Column(name = "item_score")
    @Basic
    public String getItemScore() {
        return itemScore;
    }

    public void setItemScore(String itemScore) {
        this.itemScore = itemScore;
    }


    @javax.persistence.Column(name = "service_score")
    @Basic
    public String getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(String serviceScore) {
        this.serviceScore = serviceScore;
    }


    @javax.persistence.Column(name = "delivery_score")
    @Basic
    public String getDeliveryScore() {
        return deliveryScore;
    }

    public void setDeliveryScore(String deliveryScore) {
        this.deliveryScore = deliveryScore;
    }


    @javax.persistence.Column(name = "de_express")
    @Basic
    public String getDeExpress() {
        return deExpress;
    }

    public void setDeExpress(String deExpress) {
        this.deExpress = deExpress;
    }


    @javax.persistence.Column(name = "enable_msg")
    @Basic
    public Boolean getEnableMsg() {
        return enableMsg;
    }

    public void setEnableMsg(Boolean enableMsg) {
        this.enableMsg = enableMsg;
    }


    @javax.persistence.Column(name = "msg_temp")
    @Basic
    public String getMsgTemp() {
        return msgTemp;
    }

    public void setMsgTemp(String msgTemp) {
        this.msgTemp = msgTemp;
    }


    @javax.persistence.Column(name = "msg_sign")
    @Basic
    public String getMsgSign() {
        return msgSign;
    }

    public void setMsgSign(String msgSign) {
        this.msgSign = msgSign;
    }


    @javax.persistence.Column(name = "platform_type")
    @Basic
    @Enumerated(EnumType.STRING)
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @javax.persistence.Column(name = "shop_type")
    @Basic
    @Enumerated(EnumType.STRING)
    public ShopType getShopType() {
        return shopType;
    }

    public void setShopType(ShopType shopType) {
        this.shopType = shopType;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }


    @javax.persistence.Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);
    }

    @Column(name = "is_delete")
    @Basic
    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }



    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", shopAuthId='" + shopAuthId + '\'' +
                ", outShopId='" + outShopId + '\'' +
                ", catId='" + catId + '\'' +
                ", nick='" + nick + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", bulletin='" + bulletin + '\'' +
                ", picPath='" + picPath + '\'' +
                ", itemScore='" + itemScore + '\'' +
                ", serviceScore='" + serviceScore + '\'' +
                ", deliveryScore='" + deliveryScore + '\'' +
                ", deExpress='" + deExpress + '\'' +
                ", enableMsg=" + enableMsg +
                ", msgTemp='" + msgTemp + '\'' +
                ", msgSign='" + msgSign + '\'' +
                ", platformType='" + platformType + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                '}';
    }
}
