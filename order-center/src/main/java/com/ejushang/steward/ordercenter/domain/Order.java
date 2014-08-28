package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_order")
@Entity
public class Order implements EntityClass<Integer>, OperableData {

    private Integer id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单类型
     */
    private OrderType type = OrderType.NORMAL;

    /**
     * 订单状态
     */
    private OrderStatus status;

    /**
     * 建议使用trade.promotion_details查询系统优惠 系统优惠金额（如打折，VIP，满就送等）
     */
    private Money sharedDiscountFee = Money.valueOf(0);

    /**
     * 邮费
     */
    private Money sharedPostFee = Money.valueOf(0);

    /**
     * 实付金额
     */
    private Money actualFee = Money.valueOf(0);

    /**
     * 手工调整金额
     */
    private Money goodsFee = Money.valueOf(0);

    /**
     * 订单退货状态
     */
    private OrderReturnStatus orderReturnStatus = OrderReturnStatus.NORMAL;
    /**
     * 买家ID
     */
    private String buyerId;

    /**
     * 买家支付宝账号（淘宝）
     */
    private String buyerAlipayNo;

    /**
     * 买家留言
     */
    private String buyerMessage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 下单时间
     */
    private Date buyTime;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 是否需要发票
     */
    private Boolean needReceipt = false;

    /**
     * 发票抬头
     */
    private String receiptTitle;

    /**
     * 发票内容
     */
    private String receiptContent;

    /**
     * 生成类型
     */
    private OrderGenerateType generateType;

    /**
     * 是否有效
     */
    private Boolean valid = true;

    private Date createTime;

    private Date updateTime;
    //操作人ID
    private Integer operatorId;
    //线下备注
    private String offlineRemark;
    /**
     * 发货ID
     */
    private Integer invoiceId;
    /**
     * 发货实体
     */
    private Invoice invoice;

    /**
     * 店铺ID
     */
    private Integer shopId;
    /**
     * 店铺实体
     */
    private Shop shop;

    /**
     * 发货仓库ID
     */
    private Integer repoId;
    /**
     * 仓库实体
     */
    private Repository repo;
    /**
     * 平台类型
     */
    private PlatformType platformType;
    //外部平台订单编号
    private String platformOrderNo;
    /**
     * 原始订单ID
     */
    private Integer originalOrderId;
    /**
     * 原始订单实体
     */
    private OriginalOrder originalOrder;
     //是否线上抓取订单（true为线上，false线下）
    private Boolean online=false;
    //订单项
    private List<OrderItem> orderItemList = new ArrayList<OrderItem>(0);
    //操作日志
    private List<OrderHandleLog> orderHandleLogs = new ArrayList<OrderHandleLog>(0);
    //审核信息
    private List<OrderApprove> orderApproveList = new ArrayList<OrderApprove>(0);

    @Column(name = "online")
    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    @Column(name = "offline_remark")
    public String getOfflineRemark() {
        return offlineRemark;
    }

    public void setOfflineRemark(String offlineRemark) {
        this.offlineRemark = offlineRemark;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    public List<OrderHandleLog> getOrderHandleLogs() {
        return orderHandleLogs;
    }

    public void setOrderHandleLogs(List<OrderHandleLog> orderHandleLogs) {
        this.orderHandleLogs = orderHandleLogs;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    public List<OrderApprove> getOrderApproveList() {
        return orderApproveList;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    public void setOrderApproveList(List<OrderApprove> orderApproveList) {
        this.orderApproveList = orderApproveList;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", insertable = false, updatable = false)
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repo_id", insertable = false, updatable = false)
    public Repository getRepo() {
        return repo;
    }

    public void setRepo(Repository repo) {
        this.repo = repo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_order_id", insertable = false, updatable = false)
    public OriginalOrder getOriginalOrder() {
        return originalOrder;
    }

    public void setOriginalOrder(OriginalOrder originalOrder) {
        this.originalOrder = originalOrder;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "order_no")
    @Basic
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


    @javax.persistence.Column(name = "type")
    @Enumerated(EnumType.STRING)
    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    @javax.persistence.Column(name = "return_status")
    @Enumerated(EnumType.STRING)
    public OrderReturnStatus getOrderReturnStatus() {
        return orderReturnStatus;
    }

    public void setOrderReturnStatus(OrderReturnStatus orderReturnStatus) {
        this.orderReturnStatus = orderReturnStatus;
    }

    @javax.persistence.Column(name = "status")
    @Enumerated(EnumType.STRING)
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @javax.persistence.Column(name = "shared_discount_fee")
    public Money getSharedDiscountFee() {
        return sharedDiscountFee;
    }

    public void setSharedDiscountFee(Money sharedDiscountFee) {
        this.sharedDiscountFee = sharedDiscountFee;
    }

    @javax.persistence.Column(name = "shared_post_fee")
    public Money getSharedPostFee() {
        return sharedPostFee;
    }

    public void setSharedPostFee(Money sharedPostFee) {
        this.sharedPostFee = sharedPostFee;
    }

    @javax.persistence.Column(name = "goods_fee")
    public Money getGoodsFee() {
        return goodsFee;
    }

    public void setGoodsFee(Money goodsFee) {
        this.goodsFee = goodsFee;
    }

    @javax.persistence.Column(name = "actual_fee")
    public Money getActualFee() {
        return actualFee;
    }

    public void setActualFee(Money actualFee) {
        this.actualFee = actualFee;
    }


    @javax.persistence.Column(name = "buyer_id")
    @Basic
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    @javax.persistence.Column(name = "buyer_alipay_no")
    @Basic
    public String getBuyerAlipayNo() {
        return buyerAlipayNo;
    }

    public void setBuyerAlipayNo(String buyerAlipayNo) {
        this.buyerAlipayNo = buyerAlipayNo;
    }

    @javax.persistence.Column(name = "buyer_message")
    @Basic
    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }


    @javax.persistence.Column(name = "remark")
    @Basic
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @javax.persistence.Column(name = "repo_id")
    @Basic
    public Integer getRepoId() {
        return repoId;
    }

    public void setRepoId(Integer repoId) {
        this.repoId = repoId;
    }


    @javax.persistence.Column(name = "buy_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = UglyTimestampUtil.convertTimestampToDate(buyTime);
    }


    @javax.persistence.Column(name = "pay_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = UglyTimestampUtil.convertTimestampToDate(payTime);
    }


    @javax.persistence.Column(name = "shop_id")
    @Basic
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }


    @javax.persistence.Column(name = "need_receipt")
    @Basic
    public Boolean getNeedReceipt() {
        return needReceipt;
    }

    public void setNeedReceipt(Boolean needReceipt) {
        this.needReceipt = needReceipt;
    }


    @javax.persistence.Column(name = "receipt_title")
    @Basic
    public String getReceiptTitle() {
        return receiptTitle;
    }

    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }


    @javax.persistence.Column(name = "receipt_content")
    @Basic
    public String getReceiptContent() {
        return receiptContent;
    }

    public void setReceiptContent(String receiptContent) {
        this.receiptContent = receiptContent;
    }


    @javax.persistence.Column(name = "original_order_id")
    @Basic
    public Integer getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Integer originalOrderId) {
        this.originalOrderId = originalOrderId;
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

    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }


    @javax.persistence.Column(name = "invoice_id")
    @Basic
    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }


    @javax.persistence.Column(name = "generate_type")
    @Enumerated(EnumType.STRING)
    public OrderGenerateType getGenerateType() {
        return generateType;
    }

    public void setGenerateType(OrderGenerateType generateType) {
        this.generateType = generateType;
    }

    @javax.persistence.Column(name = "valid")
    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @javax.persistence.Column(name = "platform_type")
    @Enumerated(EnumType.STRING)
    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    @javax.persistence.Column(name = "platform_order_no")
    @Basic
    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }


    /** =============== 无对应属性的get方法 =============== **/

    /**
     * 订单是否正在申请退款
     *
     * @return
     */
    @Transient
    public Boolean getRefunding() {
        boolean refunding = false;
        for (OrderItem orderItem : orderItemList) {
            if (orderItem.getRefunding()) {
                refunding = true;
                break;
            }
        }
        return refunding;
    }


    /**
     * 得到发货单应该显示的邮费
     * 分摊邮费 + 邮费补差 - 邮费补差退款 + 换货邮费
     */
    @Transient
    public Money getInvoicePostFee() {
        Money invoicePostFee = Money.valueOf(0);
        for (OrderItem orderItem : orderItemList) {
            invoicePostFee = invoicePostFee.add(orderItem.getInvoicePostFee());
        }
        return invoicePostFee;
    }

    /**
     * 拆单的时候复制订单信息
     *
     * @return
     */
    public Order copyForSplit() {
        Order copiedOrder = new Order();
        copiedOrder.setType(getType());
        copiedOrder.setStatus(getStatus());
        copiedOrder.setOrderReturnStatus(getOrderReturnStatus());
        copiedOrder.setGenerateType(getGenerateType());
        copiedOrder.setOnline(getOnline());

        copiedOrder.setSharedDiscountFee(getSharedDiscountFee());
        copiedOrder.setSharedPostFee(getSharedPostFee());
        copiedOrder.setActualFee(getActualFee());
        copiedOrder.setGoodsFee(getGoodsFee());

        copiedOrder.setBuyerId(getBuyerId());
        copiedOrder.setBuyerMessage(getBuyerMessage());
        copiedOrder.setBuyerAlipayNo(getBuyerAlipayNo());
        copiedOrder.setBuyTime(getBuyTime());
        copiedOrder.setPayTime(getPayTime());
        copiedOrder.setRemark(getRemark());

        copiedOrder.setNeedReceipt(getNeedReceipt());
        copiedOrder.setReceiptTitle(getReceiptTitle());
        copiedOrder.setReceiptContent(getReceiptContent());

        copiedOrder.setOriginalOrderId(getOriginalOrderId());
        copiedOrder.setPlatformOrderNo(getPlatformOrderNo());
        copiedOrder.setPlatformType(getPlatformType());
        copiedOrder.setShopId(getShopId());

        copiedOrder.setRepoId(getRepoId());
        copiedOrder.setInvoice(getInvoice().copyForSplit());

        return copiedOrder;

    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'';
    }
}
