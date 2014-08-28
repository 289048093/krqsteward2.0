package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

/**
 * User: Shiro
 * Date: 14-8-6
 * Time: 下午3:53
 */
public class Order extends ZiYouObject {
    private static final long serialVersionUID = -1223314607478192031L;
    /**
     * 订单项编号
     */
    @ApiField("oid")
     private String oid;
    /**
     * 商品条形码
     */
    @ApiField("sku")
     private String sku;
    /**
     * 商品标题
     */
    @ApiField("title")
    private String title;
    /**
     *  商品单价
     */
    @ApiField("price")
    private Long price;
    /**
     * 购买数量
     */
    @ApiField("buy_count")
    private Long buyCount;
    /**
     *  商品总金额。单位：分
     */
    @ApiField("total_fee")
    private Long totalFee;
    /**
     * 应付总金额。单位：分
     */
    @ApiField("payable_fee")
    private Long payableFee;
    /**
     *实付总金额。单位：分
     */
    @ApiField("actual_fee")
    private Long actualFee;
    /**
     * 订单项优惠金。单位：分
     */
    @ApiField("discount_fee")
    private Long discountFee;
    /**
     * 手工调整金额。单位：分
     */
    @ApiField("adjust_fee")
    private Long adjustFee;
    /**
     *  分摊订单优惠之后的实付金额。
     *  单位：分。
     *  计算公式：订单项应付总金额 - （订单项应付总金额/所有订单项应付总金额*需要外部平台承担的订单优惠）
     */
    @ApiField("divide_order_fee")
    private Long divideOrderFee;
    /**
     *  需要外部平台承担的订单优惠的优惠分摊。单位：分。
     *  计算公式：订单项应付总金额/所有订单项应付总金额*需要外部平台承担的订单优惠
     */
    @ApiField("part_mjz_discount")
    private Long partMjzDiscount;
    /**
     * 包含所有订单优惠的优惠分摊。单位：分。
     * 计算公式：订单项应付总金额/所有订单项应付总金额*所有订单优惠金额
     */
    @ApiField("all_part_mjz_discount")
    private Long allPartMjzDiscount;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Long buyCount) {
        this.buyCount = buyCount;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public Long getPayableFee() {
        return payableFee;
    }

    public void setPayableFee(Long payableFee) {
        this.payableFee = payableFee;
    }

    public Long getActualFee() {
        return actualFee;
    }

    public void setActualFee(Long actualFee) {
        this.actualFee = actualFee;
    }

    public Long getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Long discountFee) {
        this.discountFee = discountFee;
    }

    public Long getAdjustFee() {
        return adjustFee;
    }

    public void setAdjustFee(Long adjustFee) {
        this.adjustFee = adjustFee;
    }

    public Long getDivideOrderFee() {
        return divideOrderFee;
    }

    public void setDivideOrderFee(Long divideOrderFee) {
        this.divideOrderFee = divideOrderFee;
    }

    public Long getPartMjzDiscount() {
        return partMjzDiscount;
    }

    public void setPartMjzDiscount(Long partMjzDiscount) {
        this.partMjzDiscount = partMjzDiscount;
    }

    public Long getAllPartMjzDiscount() {
        return allPartMjzDiscount;
    }

    public void setAllPartMjzDiscount(Long allPartMjzDiscount) {
        this.allPartMjzDiscount = allPartMjzDiscount;
    }
}
