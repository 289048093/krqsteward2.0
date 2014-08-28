package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiListField;

import javax.net.ssl.SSLEngineResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 2014/8/6
 * Time: 10:31
 */
public class Trade extends ZiYouObject {
    private static final long serialVersionUID = 7650962330010046928L;
    /**
     * 订单编号
     */
    @ApiField("tid")
    private String tid;
    /**
     *  订单状态。可选值：WAIT_BUYER_PAY（等待买家付款），
     *  WAIT_SELLER_SEND_GOODS（等待卖家发货,即:买家已付款），
     *  WAIT_BUYER_CONFIRM_GOODS（等待买家确认收货,即:卖家已发货），
     *  TRADE_FINISHED（交易成功）
     */
    @ApiField("status")
    private String status;

    /**
     * 订单商品总金额（SUM(一口价*数量)）。单位：分
     */
    @ApiField("total_fee")
    private Long totalFee;

    /**
     * 订单实付金额（卖家应收）。单位：分
     */
    @ApiField("actual_fee")
    private Long actualFee;

    /**
     * 需要外部平台承担的订单优惠金额（如打折，VIP，满就送等）。单位：分
     */
    @ApiField("discount_fee")
    private Long discountFee;

    /**
     * 所有订单优惠金额。单位：分
     */
    @ApiField("all_discount_fee")
    private Long allDiscountFee;

    /**
     *  是否包含邮费。与available_confirm_fee同时使用。单位：分
     */
    @ApiField("has_post_fee")
    private boolean hasPostFee;

   /*
    * 邮费。单位：分
    */
    @ApiField("post_fee")
    private Long postFee;

    /**
     * 交易中剩余的确认收货金额（这个金额会随着子订单确认收货而不断减少，交易成功后会变为零）。单位：分
     */
    @ApiField("available_confirm_fee")
    private Long availableConfirmFee;

    /**
     * 卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而
     * 不断增加，交易成功后等于买家实付款减去退款金额）。单位：分
     */
    @ApiField("received_payment")
    private Long receivedPayment;

    /**
     * 手工调整金额。单位：分
     */
    @ApiField("adjust_fee")
    private Long adjustFee;

    /**
     * 买家使用积分,下单时生成，且一直不变
     */
    @ApiField("point_fee")
    private Long pointFee;

    /**
     * 买家实际使用积分（扣除部分退款使用的积分），
     * 交易完成后生成（交易成功或关闭），
     * 交易未完成时该字段值为0
     */
    @ApiField("real_point_fee")
    private Long realPointFee;

    /**
     * 送货（日期）类型
     * （1-只工作日送货(双休日、假日不用送);
     *  2-只双休日、假日送货(工作日不用送);
     * 3-工作日、双休日与假日均可送货;其他值-返回"任意时间"）
     */
    @ApiField("delivery_type")
    private String deliveryType;

    /**
     * 买家留言
     */
    @ApiField("buyer_message")
    private String buyerMessage;

    /**
     * 客服备注
     */
    @ApiField("remark")
    private String remark;

    /**
     * 买家Id
     */
    @ApiField("buyer_id")
    private String buyerId;
    /**
     *  买家支付宝账号
     */
    @ApiField("buyer_alipay_no")
    private String buyerAlipayNo;
    /**
     * 收货人姓名
     */
    @ApiField("receiver_name")
    private String receiverName;
    /**
     * 收货人手机号(与收货人电话在业务上必须存在一个)
     */
    @ApiField("receiver_phone")
    private String receiverPhone;
    /**
     * 收货人电话(与收货人手机号在业务上必须存在一个)
     */
    @ApiField("receiver_mobile")
    private String receiverMobile;
    /**
     * 收货人邮编
     */
    @ApiField("receiver_zip")
    private String receiverZip;
    /**
     * 收货人省份
     */
    @ApiField("receiver_state")
    private String receiverState;
    /**
     * 收货人城市
     */
    @ApiField("receiver_city")
    private String receiverCity;
    /**
     * 收货人地区
     */
    @ApiField("receiver_district")
    private String receiverDistrict;
    /**
     * 不包含省市区的详细地址
     */
    @ApiField("receiver_address")
    private String receiverAddress;
    /**
     * 下单时间。格式:yyyy-MM-dd HH:mm:ss
     */
    @ApiField("buy_time")
    private Date buyTime;
    /**
     *支付时间。格式:yyyy-MM-dd HH:mm:ss
     */
    @ApiField("pay_time")
    private Date payTime;
    /**
     * 结单时间。格式:yyyy-MM-dd HH:mm:ss
     */
    @ApiField("end_time")
    private Date endTime;
    /**
     * 订单更新时间。格式:yyyy-MM-dd HH:mm:ss
     */
    @ApiField("modified_time")
    private Date modifiedTime;
    /**
     *是否需要发票
     */
    @ApiField("need_receipt")
    private String needReceipt;
    /**
     *  发票抬头
     */
    @ApiField("receipt_title")
    private String receiptTitle;
    /**
     * 发票内容
     */
    @ApiField("receipt_content")
    private String receiptContent;
    /**
     * 订单创建时间（与下单时间相同）。格式:yyyy-MM-dd HH:mm:ss
     */
    @ApiField("create_time")
    private Date createTime;
    /**
     *  交易的订单项集合
     */
    @ApiListField("orders")
    private List<Order> orders = new ArrayList<Order>(0);
    /**
     * 交易的订单优惠集合
     */
    @ApiListField("promotions")
    private List<Promotion> promotions = new ArrayList<Promotion>(0);

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = status;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
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

    public Long getAllDiscountFee() {
        return allDiscountFee;
    }

    public void setAllDiscountFee(Long allDiscountFee) {
        this.allDiscountFee = allDiscountFee;
    }

    public boolean getHasPostFee() {
        return hasPostFee;
    }

    public void setHasPostFee(boolean hasPostFee) {
        this.hasPostFee = hasPostFee;
    }

    public Long getPostFee() {
        return postFee;
    }

    public void setPostFee(Long postFee) {
        this.postFee = postFee;
    }

    public Long getAvailableConfirmFee() {
        return availableConfirmFee;
    }

    public void setAvailableConfirmFee(Long availableConfirmFee) {
        this.availableConfirmFee = availableConfirmFee;
    }

    public Long getReceivedPayment() {
        return receivedPayment;
    }

    public void setReceivedPayment(Long receivedPayment) {
        this.receivedPayment = receivedPayment;
    }

    public Long getAdjustFee() {
        return adjustFee;
    }

    public void setAdjustFee(Long adjustFee) {
        this.adjustFee = adjustFee;
    }

    public Long getPointFee() {
        return pointFee;
    }

    public void setPointFee(Long pointFee) {
        this.pointFee = pointFee;
    }

    public Long getRealPointFee() {
        return realPointFee;
    }

    public void setRealPointFee(Long realPointFee) {
        this.realPointFee = realPointFee;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerAlipayNo() {
        return buyerAlipayNo;
    }

    public void setBuyerAlipayNo(String buyerAlipayNo) {
        this.buyerAlipayNo = buyerAlipayNo;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getNeedReceipt() {
        return needReceipt;
    }

    public void setNeedReceipt(String needReceipt) {
        this.needReceipt = needReceipt;
    }

    public String getReceiptTitle() {
        return receiptTitle;
    }

    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }

    public String getReceiptContent() {
        return receiptContent;
    }

    public void setReceiptContent(String receiptContent) {
        this.receiptContent = receiptContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }
}
