package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.domain.Conf;
import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.keygenerator.SystemConfConstant;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: liubin
 * Date: 14-5-26
 */
public class OriginalRefundBuilder {

    private Shop tbShop;

    private Shop jdShop;

    private OriginalRefundStatus status;

    private RefundPhase refundPhase;

    private Money refundFee;

    private Date refundTime;

    private String buyerAlipayNo;

    private OriginalOrder originalOrder;

    private OrderAnalyzeTestService orderAnalyzeTestService = Application.getBean(OrderAnalyzeTestService.class);

    private ConfService confService = Application.getBean(ConfService.class);

    private ShopService shopService = Application.getBean(ShopService.class);

    private OriginalOrderService originalOrderService = Application.getBean(OriginalOrderService.class);

    private OriginalRefundService originalRefundService = Application.getBean(OriginalRefundService.class);

    private OrderService orderService = Application.getBean(OrderService.class);

    private PaymentService paymentService = Application.getBean(PaymentService.class);

    public OriginalRefundBuilder(OriginalOrder originalOrder, RefundPhase refundPhase, OriginalRefundStatus status) {

        this.originalOrder = originalOrder;
        this.refundPhase = refundPhase;
        this.status = status;

        List<Shop> shops = shopService.findByPlatformType(PlatformType.TAO_BAO);
        if(shops.isEmpty()) {
            tbShop = orderAnalyzeTestService.initShop(PlatformType.TAO_BAO);
        } else {
            tbShop = shops.get(0);
        }
        shops = shopService.findByPlatformType(PlatformType.JING_DONG);
        if(shops.isEmpty()) {
            jdShop = orderAnalyzeTestService.initShop(PlatformType.JING_DONG);
        } else {
            jdShop = shops.get(0);
        }

    }

//    public OriginalRefundBuilder addPayment(Payment payment) {
//        this.refundType = RefundType.PAYMENT;
//        this.payment = payment;
//        return this;
//    }
//
//    public OriginalRefundBuilder addOrderItem(OrderItem orderItem) {
//        this.refundType = RefundType.PAYMENT;
//        this.orderItem = orderItem;
//        return this;
//    }


    public OriginalRefund build() {

        OriginalRefund originalRefund = new OriginalRefund();

        switch (originalOrder.getPlatformType()) {
            case TAO_BAO: {

                List<Order> orders = orderService.findByPlatformOrderNo(originalOrder.getPlatformType(), originalOrder.getPlatformOrderNo());
                if(!orders.isEmpty()) {
                    Order order = orders.get(0);
                    List<OrderItem> orderItemList = orderService.findOrderItemByOrderId(order.getId());
                    OrderItem orderItem = orderItemList.get(0);

                    originalRefund.setOid(orderItem.getPlatformSubOrderNo());
                } else {
                    List<Payment> payments = paymentService.findByPlatformOrderNo(originalOrder.getPlatformType(), originalOrder.getPlatformOrderNo());
                    Payment payment = payments.get(0);

                    originalRefund.setOid(payment.getPlatformSubOrderNo());
                }

                break;
            }

            case JING_DONG: {

                originalRefund.setTid(originalOrder.getPlatformOrderNo());

                break;
            }
        }

        if(refundFee == null) {
            refundFee = Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue());
        }
        if(buyerAlipayNo == null) {
            orderAnalyzeTestService.randomString(null, 6);
        }
        originalRefund.setRefundFee(refundFee);
        originalRefund.setReason(orderAnalyzeTestService.randomString("reason", 6));
        originalRefund.setActualRefundFee(originalRefund.getActualRefundFee());
        originalRefund.setAlipayNo(orderAnalyzeTestService.randomString("alipayNo", 6));
//        originalRefund.setBillType();
        originalRefund.setBuyerId(orderAnalyzeTestService.randomString("buyerId", 6));
        originalRefund.setBuyerNick(orderAnalyzeTestService.randomString("buyerNick", 6));
        originalRefund.setCheckTime(new Date());
//        originalRefund.setCheckUsername();
//        originalRefund.setCompanyName();
        if(refundTime == null) {
            refundTime = new Date();
        }
        originalRefund.setCreated(refundTime);
//        originalRefund.setCsStatus();
//        originalRefund.setCurrentPhaseTimeout();
        originalRefund.setModified(new Date());
        originalRefund.setPlatformType(originalOrder.getPlatformType());
        originalRefund.setProcessed(false);
        originalRefund.setStatus(status);
        originalRefund.setRefundId(orderAnalyzeTestService.randomNumericString(12));
        originalRefund.setRefundPhase(refundPhase);
        //退款阶段是根据订单交易状态来区分的
        switch (refundPhase) {
            case ON_SALE:
                originalRefund.setTradeStatus(OriginalRefundTradeStatus.WAIT_SEND_GOOD.toString());
                break;
            case AFTER_SALE:
                originalRefund.setTradeStatus(OriginalRefundTradeStatus.FINISHED.toString());
                break;
        }

        switch (originalOrder.getPlatformType()) {
            case TAO_BAO:
                originalRefund.setShopId(tbShop.getId());
                break;
            case JING_DONG:
                originalRefund.setShopId(jdShop.getId());
                break;
        }

        originalRefundService.saveOriginalRefund(originalRefund);

        return originalRefund;
    }

    public Money getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Money refundFee) {
        this.refundFee = refundFee;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public String getBuyerAlipayNo() {
        return buyerAlipayNo;
    }

    public void setBuyerAlipayNo(String buyerAlipayNo) {
        this.buyerAlipayNo = buyerAlipayNo;
    }


}
