package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author Channel
 * @Date 2014/8/6
 * @Version: 1.0
 */
public class AfterSalesVo {

    /**
     * id
     */
    private Integer id;
    /**
     * AfterSalesSource:售后来源;ORDER:普通订单,VISIT:回访单,PAYMENT:预收款;
     */
    private AfterSalesSource source;
    /**
     * 订单ID
     */
    private Integer orderId;
    //售后编号
    private String code;
    //售后类型
    /**
     * AfterSalesStatus:售后单状态;SAVE:处理中,CHECK:待审批,ACCEPT:审批通过,REJECT:审批驳回,CANCEL:已作废;
     */
    private AfterSalesType type;
    //售后状态
    private AfterSalesStatus status;
    //退款状态
    private String refundStatus;
    //退货状态
    private String refundGoodsStatus;
    //到货状态
    private String receiverGoodsStatus;
    //生成订单状态
    private String createOrderStatus;
    //退款方式
    private String refundWay;
    /**
     * 回访单ID
     */
    private Integer revisitId;
    /**
     * 预收款ID
     */
    private Integer paymentId;
    /**
     * 售后原因码
     */
    private String reasonCode;
    /**
     * 售后原因
     */
    private String reason;
    /**
     * 售后备注
     */
    private String remark;

    //平台
    private PlatformType platformType;
    //店铺
    private String shopName;
    //品牌
    private String brandName;
    //买家ID
    private String buyerId;

    private String platformOrderNo;

    /**
     * 售后项
     */
    private List<Item> itemList;

    /**
     * 是否发货
     */
    private Boolean send;

    public PlatformType getPlatformType() {
        return platformType;
    }

    /**
     * 发货单
     */
    private Send sendInfo;

    //退货状态
    //到货状态
    //退款状态


    /**
     * 附件列表
     */
    private List<String> attachmentList = new LinkedList<String>();

    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AfterSalesType getType() {
        return type;
    }

    public void setType(AfterSalesType type) {
        this.type = type;
    }

    public AfterSalesStatus getStatus() {
        return status;
    }

    public void setStatus(AfterSalesStatus status) {
        this.status = status;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundGoodsStatus() {
        return refundGoodsStatus;
    }

    public void setRefundGoodsStatus(String refundGoodsStatus) {
        this.refundGoodsStatus = refundGoodsStatus;
    }

    public String getReceiverGoodsStatus() {
        return receiverGoodsStatus;
    }

    public void setReceiverGoodsStatus(String receiverGoodsStatus) {
        this.receiverGoodsStatus = receiverGoodsStatus;
    }

    public String getCreateOrderStatus() {
        return createOrderStatus;
    }

    public void setCreateOrderStatus(String createOrderStatus) {
        this.createOrderStatus = createOrderStatus;
    }

    public String getRefundWay() {
        return refundWay;
    }

    public void setRefundWay(String refundWay) {
        this.refundWay = refundWay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getRevisitId() {
        return revisitId;
    }

    public void setRevisitId(Integer revisitId) {
        this.revisitId = revisitId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public AfterSalesSource getSource() {
        return source;
    }

    public void setSource(AfterSalesSource source) {
        this.source = source;
    }

    public Boolean isSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
    }

    public Send getSendInfo() {
        return sendInfo;
    }

    public void setSendInfo(Send sendInfo) {
        this.sendInfo = sendInfo;
    }

    public Boolean getSend() {
        return send;
    }

    public List<String> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<String> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @Override
    public String toString() {
        return "AfterSalesVo{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", revisitId=" + revisitId +
                ", reasonCode=" + reasonCode +
                ", reason='" + reason + '\'' +
                ", remark='" + remark + '\'' +
                ", itemList=" + itemList +
                ", send=" + send +
                ", sendInfo=" + sendInfo +
                ", attachmentList=" + attachmentList +
                '}';
    }

    /**
     * 发货表
     */
    public static class Send {

        /**
         * 物流公司
         */
        private String shippingComp;
        /**
         * 收货人姓名
         */
        private String receiverName;
        /**
         * 收货人手机号(与收货人电话在业务上必须存在一个)
         */
        private String receiverPhone;
        /**
         * 收货人电话(与收货人手机号在业务上必须存在一个)
         */
        private String receiverMobile;
        /**
         * 收货人邮编
         */
        private String receiverZip;
        /**
         * 收货人省份
         */
        private String receiverState;
        /**
         * 收货人城市
         */
        private String receiverCity;
        /**
         * 收货人地区
         */
        private String receiverDistrict;
        /**
         * 不包含省市区的详细地址
         */
        private String receiverAddress;
        /**
         * 备注信息（卖家留言）
         */
        private String receiverRemark;

        public String getShippingComp() {
            return shippingComp;
        }

        public void setShippingComp(String shippingComp) {
            this.shippingComp = shippingComp;
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

        public String getReceiverRemark() {
            return receiverRemark;
        }

        public void setReceiverRemark(String receiverRemark) {
            this.receiverRemark = receiverRemark;
        }

        @Override
        public String toString() {
            return "Send{" +
                    ", shippingComp='" + shippingComp + '\'' +
                    ", receiverName='" + receiverName + '\'' +
                    ", receiverPhone='" + receiverPhone + '\'' +
                    ", receiverMobile='" + receiverMobile + '\'' +
                    ", receiverZip='" + receiverZip + '\'' +
                    ", receiverState='" + receiverState + '\'' +
                    ", receiverCity='" + receiverCity + '\'' +
                    ", receiverDistrict='" + receiverDistrict + '\'' +
                    ", receiverAddress='" + receiverAddress + '\'' +
                    ", receiverRemark='" + receiverRemark + '\'' +
                    '}';
        }
    }

    /**
     * 售后项
     */
    public static class Item {

        /**
         * id
         */
        private Integer id;

        /**
         * 订单项ID
         */
        private Integer orderItemId;

        /**
         * 售后退款记录
         */
        private Refund refund;

        /**
         * 售后退货记录
         */
        private RefundGoods refundGoods;


        /**
         * 售后补货记录
         */
        private List<Patch> patchList;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(Integer orderItemId) {
            this.orderItemId = orderItemId;
        }

        public Refund getRefund() {
            return refund;
        }

        public void setRefund(Refund refund) {
            this.refund = refund;
        }

        public RefundGoods getRefundGoods() {
            return refundGoods;
        }

        public void setRefundGoods(RefundGoods refundGoods) {
            this.refundGoods = refundGoods;
        }

        public List<Patch> getPatchList() {
            return patchList;
        }

        public void setPatchList(List<Patch> patchList) {
            this.patchList = patchList;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    ", orderItemId=" + orderItemId +
                    ", refund=" + refund +
                    ", refundGoods=" + refundGoods +
                    ", patchList=" + patchList +
                    '}';
        }
    }

    /**
     * 售后退款
     */
    public static class Refund {

        /**
         * id
         */
        private Integer id;

        /**
         * RefundMethod:退款方式;ALIPAY:支付宝,BANK:银行;
         */
        private RefundMethod refundMethod;
        /**
         * 退款买家支付宝账号（淘宝）
         */
        private String alipayNo;
        /**
         * 退款银行
         */
        private String bank;
        /**
         * 退款银行账号
         */
        private String bankAccout;
        /**
         * 退款银行收款人姓名
         */
        private String bankUser;

        /**
         * 退款分配记录
         */
        private List<RefundAlloc> refundAllocList;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<RefundAlloc> getRefundAllocList() {
            return refundAllocList;
        }

        public void setRefundAllocList(List<RefundAlloc> refundAllocList) {
            this.refundAllocList = refundAllocList;
        }

        public RefundMethod getRefundMethod() {
            return refundMethod;
        }

        public void setRefundMethod(RefundMethod refundMethod) {
            this.refundMethod = refundMethod;
        }

        public String getAlipayNo() {
            return alipayNo;
        }

        public void setAlipayNo(String alipayNo) {
            this.alipayNo = alipayNo;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBankAccout() {
            return bankAccout;
        }

        public void setBankAccout(String bankAccout) {
            this.bankAccout = bankAccout;
        }

        public String getBankUser() {
            return bankUser;
        }

        public void setBankUser(String bankUser) {
            this.bankUser = bankUser;
        }

        @Override
        public String toString() {
            return "Refund{" +
                    "id=" + id +
                    ", refundMethod=" + refundMethod +
                    ", alipayNo='" + alipayNo + '\'' +
                    ", bank='" + bank + '\'' +
                    ", bankAccout='" + bankAccout + '\'' +
                    ", bankUser='" + bankUser + '\'' +
                    ", refundAllocList=" + refundAllocList +
                    '}';
        }
    }

    /**
     * 退款分配
     */
    public static class RefundAlloc {

        /**
         * id
         */
        private Integer id;

        /**
         * RefundClass:退款类型;GOODS:货款,POST:运费,REFUND_POST:退款运费;
         */
        private RefundClass type;
        /**
         * 平台承担金额
         */
        private Money platformFee = Money.valueOf(0);
        /**
         * 品牌商家承担金额
         */
        private Money supplierFee = Money.valueOf(0);
        /**
         * 是否在线退款
         */
        private Boolean online;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public RefundClass getType() {
            return type;
        }

        public void setType(RefundClass type) {
            this.type = type;
        }

        public Money getPlatformFee() {
            return platformFee;
        }

        public void setPlatformFee(Money platformFee) {
            this.platformFee = platformFee;
        }

        public Money getSupplierFee() {
            return supplierFee;
        }

        public void setSupplierFee(Money supplierFee) {
            this.supplierFee = supplierFee;
        }

        public Boolean getOnline() {
            return online;
        }

        public void setOnline(Boolean online) {
            this.online = online;
        }

        @Override
        public String toString() {
            return "RefundAlloc{" +
                    "id=" + id +
                    ", type=" + type +
                    ", platformFee=" + platformFee +
                    ", supplierFee=" + supplierFee +
                    ", online=" + online +
                    '}';
        }
    }


    /**
     * 退货
     */
    public static class RefundGoods {

        /**
         * id
         */
        private Integer id;
        /**
         * 退货数量
         */
        private Integer count;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "RefundGoods{" +
                    "id=" + id +
                    ", count=" + count +
                    '}';
        }
    }

    /**
     * 补货
     */
    public static class Patch {

        /**
         * id
         */
        private Integer id;

        /**
         * 商品id
         */
        private Integer productId;
        /**
         * 补货数量（用来支持1*N，N*N，N*1）
         */
        private Integer patchCount;

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getPatchCount() {
            return patchCount;
        }

        public void setPatchCount(Integer patchCount) {
            this.patchCount = patchCount;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Patch{" +
                    "id=" + id +
                    ", productId=" + productId +
                    ", patchCount=" + patchCount +
                    '}';
        }
    }

}
