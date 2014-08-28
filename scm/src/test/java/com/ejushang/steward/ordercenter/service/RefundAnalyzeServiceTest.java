package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.OriginalOrderStatus;
import com.ejushang.steward.ordercenter.constant.OriginalRefundStatus;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.constant.RefundPhase;
import com.ejushang.steward.ordercenter.domain.*;
import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


/**
 * User: liubin
 * Date: 14-3-10
 */
public class RefundAnalyzeServiceTest extends BaseAnalyzeTest {


    @Autowired
    private MessageAnalyzeService messageAnalyzeService;

    @Autowired
    private OrderAnalyzeTestService orderAnalyzeTestService;

    @Autowired
    private RefundAnalyzeTestService refundAnalyzeTestService;


    /**
     * Test Case.
     *
     * 1.新增的天猫退款记录
     *  1.1 无邮费,无服务补差和邮费补差,2个订单项,待处理的原始订单,其中一个订单项发生售前退款,状态为正在申请
     *  1.2 无邮费,无服务补差和邮费补差,2个订单项,待处理的原始订单,其中一个订单项发生售前退款,状态为退款成功
     *  1.3 无邮费,无服务补差和邮费补差,2个订单项,已发货的原始订单,其中一个订单项发生售后退款,状态为正在申请
     *  1.4 无邮费,无服务补差和邮费补差,2个订单项,已发货的原始订单,其中一个订单项发生售后退款,状态为退款成功
     *  1.5 无邮费.只带1个邮费补差产品,已收货的原始订单,发生售后退款,状态为正在申请
     *  1.6 无邮费.只带1个服务补差产品,已收货的原始订单,发生售后退款,状态为退款成功
     *  --1.7 无邮费,无服务补差和邮费补差,2个普通产品订单项,1个套餐产品订单项(2个产品),套餐产品订单项发生售后退款,状态为正在申请
     *  --1.8 无邮费,无服务补差和邮费补差,2个普通产品订单项,1个套餐产品订单项(2个产品),套餐产品订单项发生售后退款,状态为退款成功
     *
     * 2.新增的京东退款记录
     *  2.1 无邮费,1个服务补差,1个邮费补差,2个订单项,待处理的原始订单,整个订单发生售前退款,状态为正在申请
     *  2.2 无邮费,1个服务补差,1个邮费补差,2个订单项,待处理的原始订单,整个订单发生售前退款,状态为退款成功
     *  2.3 无邮费.只带1个邮费补差产品,待处理的原始订单,发生售前退款,状态为正在申请
     *  2.4 无邮费.只带1个服务补差产品,待处理的原始订单,发生售前退款,状态为退款成功
     *
     * 3.修改的天猫退款记录
     *  3.1 1.1的退款记录状态变更为退款失败
     *  3.2 1.1的退款记录状态变更为退款成功

     * 4.修改的京东退款记录
     *  3.1 2.1的退款记录状态变更为退款失败
     *  3.2 2.1的退款记录状态变更为退款成功
     *
     */
    @Test
    public void test11() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        Repository repository = orderAnalyzeTestService.initRepository();
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(1, 0, 0));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, OriginalRefundStatus.WAIT_SELLER_AGREE);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 1));

    }

    @Test
    public void test12() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        Repository repository = orderAnalyzeTestService.initRepository();
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(1, 0, 0));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, OriginalRefundStatus.SUCCESS);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 1));

    }

    @Test
    public void test13() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        orderBuilder.setOriginalOrderStatus(OriginalOrderStatus.WAIT_BUYER_CONFIRM_GOODS);
        Repository repository = orderAnalyzeTestService.initRepository();
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(1, 0, 0));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.AFTER_SALE, OriginalRefundStatus.WAIT_SELLER_AGREE);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 1));

    }

    @Test
    public void test14() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        orderBuilder.setOriginalOrderStatus(OriginalOrderStatus.WAIT_BUYER_CONFIRM_GOODS);
        Repository repository = orderAnalyzeTestService.initRepository();
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(1, 0, 0));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.AFTER_SALE, OriginalRefundStatus.SUCCESS);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 1));

    }

    @Test
    public void test15() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        orderBuilder.setOriginalOrderStatus(OriginalOrderStatus.TRADE_FINISHED);
        orderBuilder.addPostCoverItem(2);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(0, 1, 0));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.AFTER_SALE, OriginalRefundStatus.WAIT_SELLER_AGREE);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(0, 0, 1));

    }

    @Test
    public void test16() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        orderBuilder.setOriginalOrderStatus(OriginalOrderStatus.TRADE_FINISHED);
        orderBuilder.addServiceCoverItem(2);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(0, 0, 1));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.AFTER_SALE, OriginalRefundStatus.SUCCESS);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(0, 0, 1));

    }


    @Test
    public void test21() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        orderBuilder.setPlatformType(PlatformType.JING_DONG);
        Repository repository = orderAnalyzeTestService.initRepository();
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addPostCoverItem(2);
        orderBuilder.addServiceCoverItem(2);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(1, 1, 1));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, OriginalRefundStatus.WAIT_SELLER_AGREE);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 4));

    }

    @Test
    public void test22() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        orderBuilder.setPlatformType(PlatformType.JING_DONG);
        Repository repository = orderAnalyzeTestService.initRepository();
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addPostCoverItem(2);
        orderBuilder.addServiceCoverItem(2);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(1, 1, 1));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, OriginalRefundStatus.SUCCESS);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 4));

    }

    @Test
    public void test23() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        orderBuilder.setPlatformType(PlatformType.JING_DONG);
        orderBuilder.addPostCoverItem(2);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(0, 1, 0));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, OriginalRefundStatus.WAIT_SELLER_AGREE);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(0, 0, 1));

    }

    @Test
    public void test24() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        orderBuilder.setPlatformType(PlatformType.JING_DONG);
        orderBuilder.addServiceCoverItem(2);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(0, 0, 1));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, OriginalRefundStatus.SUCCESS);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(0, 0, 1));

    }

    @Test
    public void test31() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        Repository repository = orderAnalyzeTestService.initRepository();
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(1, 0, 0));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, OriginalRefundStatus.WAIT_SELLER_AGREE);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 1));

        originalRefund.setStatus(OriginalRefundStatus.CLOSED);
        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 1));

    }

    @Test
    public void test32() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        Repository repository = orderAnalyzeTestService.initRepository();
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(1, 0, 0));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, OriginalRefundStatus.WAIT_SELLER_AGREE);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 1));

        originalRefund.setStatus(OriginalRefundStatus.SUCCESS);
        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 1));

    }

    @Test
    public void test41() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        orderBuilder.setPlatformType(PlatformType.JING_DONG);
        Repository repository = orderAnalyzeTestService.initRepository();
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addPostCoverItem(2);
        orderBuilder.addServiceCoverItem(2);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(1, 1, 1));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, OriginalRefundStatus.WAIT_SELLER_AGREE);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 4));

        originalRefund.setStatus(OriginalRefundStatus.CLOSED);
        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 4));

    }

    @Test
    public void test42() {
        OriginalOrderBuilder orderBuilder = orderAnalyzeTestService.createBuilder();
        orderBuilder.setPlatformType(PlatformType.JING_DONG);
        Repository repository = orderAnalyzeTestService.initRepository();
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, repository);
        orderBuilder.addPostCoverItem(2);
        orderBuilder.addServiceCoverItem(2);
        OriginalOrder originalOrder = orderBuilder.build();
        analyzeAndCheckOriginalOrder(originalOrder, new OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult(1, 1, 1));

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, OriginalRefundStatus.SUCCESS);
        OriginalRefund originalRefund = refundBuilder.build();

        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 4));

        originalRefund.setStatus(OriginalRefundStatus.SUCCESS);
        analyzeAndCheckResult(originalOrder, originalRefund, new OriginalRefundAnalyzeExceptedResult(1, 2, 4));

    }


    private void analyzeAndCheckOriginalOrder(OriginalOrder originalOrder, OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult exceptedResult) {
        messageAnalyzeService.analyzeOriginalOrders(Lists.newArrayList(originalOrder));
        orderAnalyzeTestService.checkAnalyzeResult(originalOrder, exceptedResult);
    }

    private void analyzeAndCheckResult(OriginalOrder originalOrder, OriginalRefund originalRefund, OriginalRefundAnalyzeExceptedResult exceptedResult) {

        messageAnalyzeService.analyzeOriginalRefunds(Lists.newArrayList(originalRefund));
        refundAnalyzeTestService.checkAnalyzeResult(originalOrder, originalRefund, exceptedResult);

    }




    static class OriginalRefundAnalyzeExceptedResult {

        int orderCount;
        int orderItemCount;
        int refundCount;

        OriginalRefundAnalyzeExceptedResult(int orderCount, int orderItemCount, int refundCount) {
            this.orderCount = orderCount;
            this.orderItemCount = orderItemCount;
            this.refundCount = refundCount;
        }

        int getOrderCount() {
            return orderCount;
        }

        int getOrderItemCount() {
            return orderItemCount;
        }

        int getRefundCount() {
            return refundCount;
        }
    }


}
