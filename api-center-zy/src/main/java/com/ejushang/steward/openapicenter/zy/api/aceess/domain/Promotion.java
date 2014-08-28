package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

/**
 * User: Shiro
 * Date: 14-8-6
 * Time: 下午4:02
 */
public class Promotion extends ZiYouObject {
    private static final long serialVersionUID = 9088928468131516898L;
    /**
     * 优惠编号
     */
    @ApiField("promotion_id")
    private String promotionId;
    /**
     * （交易/订单）编号
     */
    @ApiField("tid")
    private String tid;
    /**
     * 订单项编号(整单优惠时，这个字段为null）
     */
    @ApiField("oid")
    private String oid;
    /**
     * 优惠信息的名称
     */
    @ApiField("promotion_name")
    private String promotionName;
    /**
     * 优惠信息的类型
     */
    @ApiField("promotion_type")
    private String promotionType;
    /**
     * 优惠金额。单位：分
     */
    @ApiField("discount_fee")
    private Long discountFee;

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public Long getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Long discountFee) {
        this.discountFee = discountFee;
    }
}
