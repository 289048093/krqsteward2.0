package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * User: 龙清华
 * Date: 14-4-16
 * Time: 下午5:25
 */
@javax.persistence.Table(name = "t_shop_product")
@Entity
public class ShopProduct implements EntityClass<Integer>,OperableData {
    private Integer id;
    /**
     * 平台ID
     */
    private Integer shopId;

    private Shop shop;
    /**
     * 商品ID
     */
    private Integer prodId;

    private Product product;
    /**
     * 一口价，掉牌价
     */
    private Money price;
    /**
     * 促销价
     */
    private Money discountPrice;
    /**
     * 库存占比，如果设置将覆写掉t_platform的占比值
     */
    private Integer storagePercent;
    /**
     * 库存数量
     */
    private Integer storageNum;
    /**
     * 是否上架
     */
    private boolean isPutaway = false;
    /**
     * 同步状态
     */
    private boolean synStatus = false;
    /**
     * 平台连接地址
     */
    private String platformUrl;

    /**
     * 有库存时是否自动上架
     */
    private boolean autoPutaway;


    private Date createTime;

    private Integer operatorId;


    @Column(name = "auto_putaway")
    public boolean isAutoPutaway() {
        return autoPutaway;
    }

    public void setAutoPutaway(boolean autoPutaway) {
        this.autoPutaway = autoPutaway;
    }



    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", insertable = false, updatable = false)
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @javax.persistence.Column(name = "shop_id")
    @Basic
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @javax.persistence.Column(name = "platform_url")
    @Basic
    public String getPlatformUrl() {
        return platformUrl;
    }

    public void setPlatformUrl(String platformUrl) {
        this.platformUrl = platformUrl;
    }

    @javax.persistence.Column(name = "prod_id")
    @Basic
    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_id", insertable = false, updatable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    @javax.persistence.Column(name = "price")
    @Basic
    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }


    @javax.persistence.Column(name = "discount_price")
    @Basic
    public Money getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Money discountPrice) {
        this.discountPrice = discountPrice;
    }


    @javax.persistence.Column(name = "storage_percent")
    @Basic
    public Integer getStoragePercent() {
        return storagePercent;
    }

    public void setStoragePercent(Integer storagePercent) {
        this.storagePercent = storagePercent;
    }


    @javax.persistence.Column(name = "storage_num")
    @Basic
    public Integer getStorageNum() {
        return storageNum;
    }

    public void setStorageNum(Integer storageNum) {
        this.storageNum = storageNum;
    }

    @javax.persistence.Column(name = "is_putaway")
    @Basic
    public boolean getPutaway() {
        return isPutaway;
    }

    public void setPutaway(boolean putaway) {
        isPutaway = putaway;
    }

    @javax.persistence.Column(name = "syn_status")
    @Basic
    public boolean getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(boolean synStatus) {
        this.synStatus = synStatus;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
    }

    @Override
    @Transient
    public Date getUpdateTime() {
        return null;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
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
        return "ShopProduct{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", shop=" + shop +
                ", prodId=" + prodId +
                ", product=" + product +
                ", price=" + price +
                ", discountPrice=" + discountPrice +
                ", storagePercent=" + storagePercent +
                ", storageNum=" + storageNum +
                ", isPutaway=" + isPutaway +
                ", synStatus=" + synStatus +
                ", platformUrl='" + platformUrl + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ShopProduct) {
            ShopProduct tmp = (ShopProduct) obj;
            return NumberUtil.equals(tmp.getId(), getId())
                    && getPutaway() == tmp.getPutaway()
                    && isAutoPutaway() == tmp.isAutoPutaway()
                    && getSynStatus() == tmp.getSynStatus()
                    && NumberUtil.equals(getStoragePercent(), tmp.getStoragePercent())
                    && StringUtils.equals(getPlatformUrl(), tmp.getPlatformUrl())
                    && (getDiscountPrice() != null && getDiscountPrice().equals(tmp.getDiscountPrice())) || (getDiscountPrice() == tmp.getDiscountPrice())
                    && NumberUtil.equals(getShopId(), tmp.getShopId())
                    && NumberUtil.equals(getProdId(), tmp.getProdId())
                    && NumberUtil.equals(getStorageNum(), tmp.getStorageNum());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (getId() << 5) - getId() ;
    }

}
