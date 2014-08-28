package com.ejushang.steward.ordercenter.vo;


import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.OrderApprove;
import com.ejushang.steward.ordercenter.domain.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * User: tin
 * Date: 14-1-2
 * Time: 下午4:59
 */
public class OrderVo {

    /**主键编号*/
    private Integer id;

    /**订单编号*/
    private String orderNo;

    /**订单类型*/
    private OrderType orderType;

    /**订单状态*/
    private OrderStatus orderStatus;

    /**买家ID，即买家的淘宝号*/
    private String buyerId;

    /**买家留言*/
    private String buyerMessage;

    /**客服人员的备注*/
    private String remark;

    /**收货人的姓名*/
    private String receiverName;

    /**收货人的电话号码*/
    private String receiverPhone;

    /**收货人的手机号码*/
    private String receiverMobile;

    /**收货人的邮编*/
    private String receiverZip;

    /**收货人的所在省份*/
    private String receiverState;

    /**收货人的所在城市*/
    private String receiverCity;

    /**收货人的所在地区*/
    private String receiverDistrict;

    /**收货人的详细地址*/
    private String receiverAddress;

    /**物流编号*/
    private String shippingNo;

    /**物流公司*/
    private String shippingComp;

    /**库房ID*/
    private Integer repoId;

    /**库房名称*/
    private String repoName;

    /**下单时间*/
    private String buyTime;

    /**付款时间*/
    private String payTime;

    /**订单来自那个平台（如天猫，京东）*/
    private PlatformType outPlatformType;


    /**外部系统的订单号（如天猫）*/
   private String platformOrderNo;

    /**店铺id*/
    private Integer shopId;

    /**店铺名称*/
    private String shopName;

    /**是否发票*/
    private Boolean needReceipt;

    /**发票抬头*/
    private String receiptTitle;

    /**发票内容*/
    private String receiptContent;

    /**记录创建时间*/
    private Date createTime;

    /**原始订单ID*/
    private Integer originalOrderId;

    /**商品名称*/
    private String itemName;

    /**邮费*/
    private String postFee;

    /**平台结算金额*/
    private String actualFee;

    /**订单生成类型*/
    private OrderGenerateType generateType;

    /** 外部订单实付金额*/
    private String outActualFee;

    /** 整单优惠金额*/
    private String sharedDiscountFee;

    /** 货款*/
    private String goodsFee;
    //线下备注
    private String offlineRemark;

    /**退货状态*/
    private OrderReturnStatus orderReturnStatus;
    /** 买家支付宝账号（淘宝） */
    private String buyerAlipayNo;
    //退款状态
    private String refunding;
        //审单时间（导入进销存的时间）
    private String  confirmedTime;
           //打印时间
    private String printedTime;

    public String getConfirmedTime() {
        return confirmedTime;
    }

    public void setConfirmedTime(String confirmedTime) {
        this.confirmedTime = confirmedTime;
    }

    public String getPrintedTime() {
        return printedTime;
    }

    public void setPrintedTime(String printedTime) {
        this.printedTime = printedTime;
    }

    public String getOfflineRemark() {
        return offlineRemark;
    }

    public void setOfflineRemark(String offlineRemark) {
        this.offlineRemark = offlineRemark;
    }

    public String getRefunding() {
        return refunding;
    }

    public void setRefunding(String refunding) {
        this.refunding = refunding;
    }

    public String getGoodsFee() {
        return goodsFee;
    }

    public String getBuyerAlipayNo() {
        return buyerAlipayNo;
    }

    public void setBuyerAlipayNo(String buyerAlipayNo) {
        this.buyerAlipayNo = buyerAlipayNo;
    }

    public void setGoodsFee(String goodsFee) {
        this.goodsFee = goodsFee;
    }

    public String getSharedDiscountFee() {
        return sharedDiscountFee;
    }

    public void setSharedDiscountFee(String sharedDiscountFee) {
        this.sharedDiscountFee = sharedDiscountFee;
    }

    public String getOutActualFee() {
        return outActualFee;
    }

    public void setOutActualFee(String outActualFee) {
        this.outActualFee = outActualFee;
    }


    public String getPostFee() {
        return postFee;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getActualFee() {
        return actualFee;
    }

    public void setActualFee(String actualFee) {
        this.actualFee = actualFee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
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

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public String getShippingComp() {
        return shippingComp;
    }

    public void setShippingComp(String shippingComp) {
        this.shippingComp = shippingComp;
    }

    public Integer getRepoId() {
        return repoId;
    }

    public void setRepoId(Integer repoId) {
        this.repoId = repoId;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PlatformType getOutPlatformType() {
        return outPlatformType;
    }

    public void setOutPlatformType(PlatformType outPlatformType) {
        this.outPlatformType = outPlatformType;
    }

    public OrderGenerateType getGenerateType() {
        return generateType;
    }

    public void setGenerateType(OrderGenerateType generateType) {
        this.generateType = generateType;
    }

    public OrderReturnStatus getOrderReturnStatus() {
        return orderReturnStatus;
    }

    public void setOrderReturnStatus(OrderReturnStatus orderReturnStatus) {
        this.orderReturnStatus = orderReturnStatus;
    }

    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getReceiptContent() {
        return receiptContent;
    }

    public void setReceiptContent(String receiptContent) {
        this.receiptContent = receiptContent;
    }

    public String getReceiptTitle() {
        return receiptTitle;
    }

    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }

    public Boolean getNeedReceipt() {
        return needReceipt;
    }

    public void setNeedReceipt(Boolean needReceipt) {
        this.needReceipt = needReceipt;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }



    public Integer getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Integer originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    /**
     * orderItem 实体
     */

    private List<OrderItem> orderItem;
    /**
     * order的item条数
     */
    private Integer itemCount = 0;
    /**
     * order的所购商品总数量
     */
    private Integer itemNumCount = 0;
    @JsonIgnore
    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getItemNumCount() {
        return itemNumCount;
    }

    public void setItemNumCount(Integer itemNumCount) {
        this.itemNumCount = itemNumCount;
    }




    public void queryOthers(Boolean  type) {
        int count = 0;
        int itemCount=0;
        for (OrderItem o : orderItem) {
            count = count + o.getBuyCount();
            itemCount=itemCount+1;
        }
        if (orderItem.size() > 0) {
            this.itemName = orderItem.get(0).getProductName();
        }

        if(type&&orderType.equals(OrderType.CHEAT)){
            goodsFee="0.00";
            actualFee="0.00";
            itemName="嘻唰唰专用";
            count=1;
            itemCount=1;
            if(StringUtils.isBlank(offlineRemark)){
                offlineRemark="刷单";
            }
        }
        this.itemNumCount = count;
        this.itemCount = itemCount;


    }





    /**
     * 得到发货单应该显示的邮费
     * 分摊邮费 + 邮费补差 - 邮费补差退款 + 换货邮费
     *
     */
    public Money getInvoicePostFee() {
        Money invoicePostFee = Money.valueOf(0);
        for(OrderItem item : orderItem) {
            invoicePostFee = invoicePostFee.add(item.getInvoicePostFee());
        }
        return invoicePostFee;
    }

}
