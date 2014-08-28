package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.util.CalculableOrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_order_item")
@Entity
public class OrderItem implements EntityClass<Integer>, CalculableOrderItem {

    private Integer id;

    /**
     * 原始订单项ID
     */
    private Integer originalOrderItemId;

    @JsonIgnore
    private OriginalOrderItem originalOrderItem;

    /**
     * 外部平台子订单编号
     */
    private String platformSubOrderNo;

    /**
     * 平台类型
     */
    private PlatformType platformType;
    //外部订单编号
    private String platformOrderNo;


    /**
     * 产品ID
     */
    private Integer productId;
    //产品实体
    private Product product;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品SKU
     */
    private String productSku;

    /**
     * 外部平台商品条形码（京东）
     */
    private String outerSku;

    /**
     * 产品名称
     */
    private String productName;
    //价格描述
    private String priceDescription;


    /**
     * 该订单项来源项ID
     * 如果订单项来源为套餐,该属性为套餐ID
     * 其他情况为null
     */
    private Integer sourceItemId;
    //订单ID
    private Integer orderId;

    @JsonIgnore
    private Order order;

    /**
     * 订单项状态
     */
    private OrderItemStatus status;

    /**
     * 订单项类型
     */
    private OrderItemType type;
    //订单项线上退货状态
    private OrderItemReturnStatus returnStatus = OrderItemReturnStatus.NORMAL;
    //订单项线上退货状态
    private OrderItemReturnStatus offlineReturnStatus = OrderItemReturnStatus.NORMAL;
    //是否被换货
    private Boolean exchanged = false;
    //是否有效
    private Boolean valid = true;
    //是否正在申请退款
    private Boolean refunding = false;


    /**
     * 一口价
     */
    private Money price = Money.valueOf(0);

    /**
     * 购买数量
     */
    private Integer buyCount;

    /**
     * 促销价
     */
    private Money discountPrice = Money.valueOf(0);

    /**
     * 建议使用trade.promotion_details查询系统优惠 系统优惠金额（如打折，VIP，满就送等）
     */
    private Money discountFee = Money.valueOf(0);

    /**
     * 分摊折扣
     */
    private Money sharedDiscountFee = Money.valueOf(0);

    /**
     * 分摊邮费
     */
    private Money sharedPostFee = Money.valueOf(0);

    /**
     * 实付金额
     */
    private Money actualFee = Money.valueOf(0);

    /**
     * 货款
     */
    private Money goodsFee = Money.valueOf(0);


    /**
     * 线上账面退款金额
     */
    private Money refundFee = Money.valueOf(0);

    /**
     * 线上实际退款金额
     */
    private Money actualRefundFee = Money.valueOf(0);


    /**
     * 线下退款金额
     */
    private Money offlineRefundFee = Money.valueOf(0);

    /**
     * 服务补差金额
     */
    private Money serviceCoverFee = Money.valueOf(0);

    /**
     * 服务补差退款金额
     */
    private Money serviceCoverRefundFee = Money.valueOf(0);

    /**
     * 邮费补差金额
     */
    private Money postCoverFee = Money.valueOf(0);

    /**
     * 邮费补差退款金额
     */
    private Money postCoverRefundFee = Money.valueOf(0);

    /**
     * 线上退货邮费
     */
    private Money returnPostFee = Money.valueOf(0);

    /**
     * 线下退货邮费
     */
    private Money offlineReturnPostFee = Money.valueOf(0);

    /**
     * 换货邮费
     */
    private Money exchangePostFee = Money.valueOf(0);

    /**
     * 线上退货邮费承担方
     */
    private PostPayer returnPostPayer;

    /**
     * 线下退货邮费承担方
     */
    private PostPayer offlineReturnPostPayer;

    /**
     * 换货邮费承担方
     */
    private PostPayer exchangePostPayer;

    /**
     * 被售后换货的OrderItemId
     */
    private Integer exchangeSourceId;

    @JsonIgnore
    private OrderItem exchangeSource;

    /**
     * 售前换货原始的订单项信息ID
     */
    private Integer exchangeOrderItemId;

    /**
     * 售前换货原始的订单项信息
     */
    @JsonIgnore
    private ExchangeOrderItem exchangeOrderItem;

    /**
     * 库存数量
     */
    private Integer repoNum = 0;

    private String specInfo;

    @javax.persistence.Column(name = "spec_info")
    public String getSpecInfo() {
        return specInfo;
    }

    public void setSpecInfo(String specInfo) {
        this.specInfo = specInfo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "price")
    @Basic
    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }


    @javax.persistence.Column(name = "buy_count")
    @Basic
    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    @javax.persistence.Column(name = "actual_fee")
    @Basic
    public Money getActualFee() {
        return actualFee;
    }

    public void setActualFee(Money actualFee) {
        this.actualFee = actualFee;
    }

    @javax.persistence.Column(name = "goods_fee")
    public Money getGoodsFee() {
        return goodsFee;
    }

    public void setGoodsFee(Money goodsFee) {
        this.goodsFee = goodsFee;
    }

    @javax.persistence.Column(name = "discount_fee")
    public Money getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Money discountFee) {
        this.discountFee = discountFee;
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

    @javax.persistence.Column(name = "product_id")
    @Basic
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }


    @javax.persistence.Column(name = "product_code")
    @Basic
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }


    @javax.persistence.Column(name = "product_sku")
    @Basic
    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    @javax.persistence.Column(name = "outer_sku")
    @Basic
    public String getOuterSku() {
        return outerSku;
    }

    public void setOuterSku(String outerSku) {
        this.outerSku = outerSku;
    }

    @javax.persistence.Column(name = "product_name")
    @Basic
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    @javax.persistence.Column(name = "original_order_item_id")
    @Basic
    public Integer getOriginalOrderItemId() {
        return originalOrderItemId;
    }

    public void setOriginalOrderItemId(Integer originalOrderItemId) {
        this.originalOrderItemId = originalOrderItemId;
    }


    @javax.persistence.Column(name = "type")
    @Enumerated(EnumType.STRING)
    public OrderItemType getType() {
        return type;
    }

    public void setType(OrderItemType type) {
        this.type = type;
    }


    @javax.persistence.Column(name = "price_description")
    @Basic
    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }


    @javax.persistence.Column(name = "order_id")
    @Basic
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }


    @javax.persistence.Column(name = "source_item_id")
    @Basic
    public Integer getSourceItemId() {
        return sourceItemId;
    }

    public void setSourceItemId(Integer sourceItemId) {
        this.sourceItemId = sourceItemId;
    }


    @Transient
    public Integer getRepoNum() {
        if (order.getRepoId() != null) {
            if (product != null) {
                List<Storage> storageList = product.getStorage();
                for (Storage storage : storageList) {
                    if (storage.getRepositoryId().equals(order.getRepoId())) {
                        repoNum = storage.getAmount();
                    }
                }       //TODO   remove storagteList field
            }
        }
        return repoNum;
    }

//    @Transient
//    public Integer getRepoNum() {
//        return repoNum;
//    }
//
//    public void setRepoNum(Integer repoNum) {
//        this.repoNum = repoNum;
//    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_order_item_id", insertable = false, updatable = false)
    public OriginalOrderItem getOriginalOrderItem() {
        return originalOrderItem;
    }

    public void setOriginalOrderItem(OriginalOrderItem originalOrderItem) {
        this.originalOrderItem = originalOrderItem;
    }

    @javax.persistence.Column(name = "platform_sub_order_no")
    public String getPlatformSubOrderNo() {
        return platformSubOrderNo;
    }

    public void setPlatformSubOrderNo(String platformSubOrderNo) {
        this.platformSubOrderNo = platformSubOrderNo;
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
    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    @javax.persistence.Column(name = "status")
    @Enumerated(EnumType.STRING)
    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    @javax.persistence.Column(name = "return_status")
    @Enumerated(EnumType.STRING)
    public OrderItemReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(OrderItemReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }

    @javax.persistence.Column(name = "offline_return_status")
    @Enumerated(EnumType.STRING)
    public OrderItemReturnStatus getOfflineReturnStatus() {
        return offlineReturnStatus;
    }

    public void setOfflineReturnStatus(OrderItemReturnStatus offlineReturnStatus) {
        this.offlineReturnStatus = offlineReturnStatus;
    }

    @javax.persistence.Column(name = "exchanged")
    public Boolean getExchanged() {
        return exchanged;
    }

    public void setExchanged(Boolean exchanged) {
        this.exchanged = exchanged;
    }

    @javax.persistence.Column(name = "valid")
    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @javax.persistence.Column(name = "refunding")
    public Boolean getRefunding() {
        return refunding;
    }

    public void setRefunding(Boolean refunding) {
        this.refunding = refunding;
    }

    @javax.persistence.Column(name = "discount_price")
    public Money getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Money discountPrice) {
        this.discountPrice = discountPrice;
    }

    @javax.persistence.Column(name = "refund_fee")
    public Money getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Money refundFee) {
        this.refundFee = refundFee;
    }

    @javax.persistence.Column(name = "actual_refund_fee")
    public Money getActualRefundFee() {
        return actualRefundFee;
    }

    public void setActualRefundFee(Money actualRefundFee) {
        this.actualRefundFee = actualRefundFee;
    }

    @javax.persistence.Column(name = "offline_refund_fee")
    public Money getOfflineRefundFee() {
        return offlineRefundFee;
    }

    public void setOfflineRefundFee(Money offlineRefundFee) {
        this.offlineRefundFee = offlineRefundFee;
    }

    @javax.persistence.Column(name = "service_cover_fee")
    public Money getServiceCoverFee() {
        return serviceCoverFee;
    }

    public void setServiceCoverFee(Money serviceCoverFee) {
        this.serviceCoverFee = serviceCoverFee;
    }

    @javax.persistence.Column(name = "service_cover_refund_fee")
    public Money getServiceCoverRefundFee() {
        return serviceCoverRefundFee;
    }

    public void setServiceCoverRefundFee(Money serviceCoverRefundFee) {
        this.serviceCoverRefundFee = serviceCoverRefundFee;
    }

    @javax.persistence.Column(name = "post_cover_fee")
    public Money getPostCoverFee() {
        return postCoverFee;
    }

    public void setPostCoverFee(Money postCoverFee) {
        this.postCoverFee = postCoverFee;
    }

    @javax.persistence.Column(name = "post_cover_refund_fee")
    public Money getPostCoverRefundFee() {
        return postCoverRefundFee;
    }

    public void setPostCoverRefundFee(Money postCoverRefundFee) {
        this.postCoverRefundFee = postCoverRefundFee;
    }

    @javax.persistence.Column(name = "return_post_fee")
    public Money getReturnPostFee() {
        return returnPostFee;
    }

    public void setReturnPostFee(Money returnPostFee) {
        this.returnPostFee = returnPostFee;
    }

    @javax.persistence.Column(name = "offline_return_post_fee")
    public Money getOfflineReturnPostFee() {
        return offlineReturnPostFee;
    }

    public void setOfflineReturnPostFee(Money offlineReturnPostFee) {
        this.offlineReturnPostFee = offlineReturnPostFee;
    }

    @javax.persistence.Column(name = "exchange_post_fee")
    public Money getExchangePostFee() {
        return exchangePostFee;
    }

    public void setExchangePostFee(Money exchangePostFee) {
        this.exchangePostFee = exchangePostFee;
    }

    @javax.persistence.Column(name = "return_post_payer")
    @Enumerated(EnumType.STRING)
    public PostPayer getReturnPostPayer() {
        return returnPostPayer;
    }

    public void setReturnPostPayer(PostPayer returnPostPayer) {
        this.returnPostPayer = returnPostPayer;
    }

    @javax.persistence.Column(name = "offline_return_post_payer")
    @Enumerated(EnumType.STRING)
    public PostPayer getOfflineReturnPostPayer() {
        return offlineReturnPostPayer;
    }

    public void setOfflineReturnPostPayer(PostPayer offlineReturnPostPayer) {
        this.offlineReturnPostPayer = offlineReturnPostPayer;
    }

    @javax.persistence.Column(name = "exchange_post_payer")
    @Enumerated(EnumType.STRING)
    public PostPayer getExchangePostPayer() {
        return exchangePostPayer;
    }

    public void setExchangePostPayer(PostPayer exchangePostPayer) {
        this.exchangePostPayer = exchangePostPayer;
    }

    @javax.persistence.Column(name = "exchange_source_id")
    public Integer getExchangeSourceId() {
        return exchangeSourceId;
    }

    public void setExchangeSourceId(Integer exchangeSourceId) {
        this.exchangeSourceId = exchangeSourceId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_source_id", insertable = false, updatable = false)
    public OrderItem getExchangeSource() {
        return exchangeSource;
    }

    public void setExchangeSource(OrderItem exchangeSource) {
        this.exchangeSource = exchangeSource;
    }

    @javax.persistence.Column(name = "exchange_order_item_id")
    public Integer getExchangeOrderItemId() {
        return exchangeOrderItemId;
    }

    public void setExchangeOrderItemId(Integer exchangeOrderItemId) {
        this.exchangeOrderItemId = exchangeOrderItemId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_order_item_id", insertable = false, updatable = false)
    public ExchangeOrderItem getExchangeOrderItem() {
        return exchangeOrderItem;
    }

    public void setExchangeOrderItem(ExchangeOrderItem exchangeOrderItem) {
        this.exchangeOrderItem = exchangeOrderItem;
    }

    /**
     * 计算促销价
     */
    public Money calculateDiscountPrice() {
        return calculateOrderItemFee().divide(buyCount);
    }

    /**
     * 计算订单项金额, 一口价 * 数量 - 订单项优惠
     */
    public Money calculateOrderItemFee() {
        return price.multiply(buyCount).subtract(discountFee);
    }

    /**
     * 获取买家实付金额（发货单上订单项实付金额）
     *
     * @return
     */
    @Transient
    public Money getUserActualPrice() {

        return discountPrice.multiply(buyCount).subtract(sharedDiscountFee);

    }

    /**
     * 得到订单项的邮费
     * 分摊邮费 + 邮费补差 - 邮费补差退款 + 换货邮费
     */
    @Transient
    public Money getInvoicePostFee() {
        Money invoicePostFee = getSharedPostFee()
                    .add(getPostCoverFee())
                    .subtract(getPostCoverRefundFee())
                    .add(getExchangePostFee());
        return invoicePostFee;
    }

    /**
     * 拆单的时候复制订单项信息
     *
     * @return
     */
    public OrderItem copyForSplit() {

        OrderItem copiedOrderItem = new OrderItem();

        copiedOrderItem.setPlatformSubOrderNo(getPlatformSubOrderNo());
        copiedOrderItem.setOriginalOrderItemId(getOriginalOrderItemId());
        copiedOrderItem.setPlatformType(getPlatformType());
        copiedOrderItem.setPlatformOrderNo(getPlatformOrderNo());

        copiedOrderItem.setType(getType());
        copiedOrderItem.setStatus(getStatus());
        copiedOrderItem.setExchanged(false);
        copiedOrderItem.setRefunding(getRefunding());

        copiedOrderItem.setProductId(getProductId());
        copiedOrderItem.setProductCode(getProductCode());
        copiedOrderItem.setProductName(getProductName());
        copiedOrderItem.setProductSku(getProductSku());
        copiedOrderItem.setOuterSku(getOuterSku());
        copiedOrderItem.setPriceDescription(getPriceDescription());
        copiedOrderItem.setSpecInfo(getSpecInfo());

        copiedOrderItem.setBuyCount(getBuyCount());
        copiedOrderItem.setPrice(getPrice());
        copiedOrderItem.setDiscountFee(getDiscountFee());
        copiedOrderItem.setDiscountPrice(getDiscountPrice());
        copiedOrderItem.setSharedDiscountFee(getSharedDiscountFee());
        copiedOrderItem.setSharedPostFee(getSharedPostFee());
        copiedOrderItem.setActualFee(getActualFee());
        copiedOrderItem.setGoodsFee(getGoodsFee());
        copiedOrderItem.setRefundFee(getRefundFee());
        copiedOrderItem.setExchangePostFee(getExchangePostFee());
        copiedOrderItem.setExchangePostPayer(getExchangePostPayer());

        return copiedOrderItem;

    }
}
