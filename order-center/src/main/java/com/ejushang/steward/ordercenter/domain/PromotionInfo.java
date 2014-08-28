package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.PlatformType;

import javax.persistence.*;

/**
 * User: Baron.Zhang
 * Date: 14-4-16
 * Time: 下午3:37
 */

@Table(name = "t_promotion_info")
@Entity
public class PromotionInfo implements EntityClass<Integer> {

    private Integer id;
    private String promotionName;
    private Money discountFee;
    private String giftItemName;
    private String giftItemId;
    private Integer giftItemNum;
    private String promotionDesc;
    private String promotionId;
    private String couponType;
    private String skuId;
    private PlatformType platformType;

    private Integer originalOrderId;
    private String platformOrderNo;

    @Column(name = "original_order_id")
    public Integer getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Integer originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    @Column(name = "out_platform_type")
    @Basic
    @Enumerated(EnumType.STRING)
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @Column(name = "platform_order_no")
    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "promotion_name")
    @Basic
    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    @Column(name = "discount_fee")
    public Money getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Money discountFee) {
        this.discountFee = discountFee;
    }

    @Column(name = "gift_item_name")
    @Basic
    public String getGiftItemName() {
        return giftItemName;
    }

    public void setGiftItemName(String giftItemName) {
        this.giftItemName = giftItemName;
    }

    @Column(name = "gift_item_id")
    @Basic
    public String getGiftItemId() {
        return giftItemId;
    }

    public void setGiftItemId(String giftItemId) {
        this.giftItemId = giftItemId;
    }

    @Column(name = "gift_item_num")
    @Basic
    public Integer getGiftItemNum() {
        return giftItemNum;
    }

    public void setGiftItemNum(Integer giftItemNum) {
        this.giftItemNum = giftItemNum;
    }

    @Column(name = "promotion_desc")
    @Basic
    public String getPromotionDesc() {
        return promotionDesc;
    }

    public void setPromotionDesc(String promotionDesc) {
        this.promotionDesc = promotionDesc;
    }

    @Column(name = "promotion_id")
    @Basic
    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    @Column(name = "coupon_type")
    @Basic
    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    @Column(name = "sku_id")
    @Basic
    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}
