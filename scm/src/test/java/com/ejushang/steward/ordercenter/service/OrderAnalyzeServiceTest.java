package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.ActivityType;
import com.ejushang.steward.ordercenter.constant.OriginalOrderStatus;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


/**
 * User: liubin
 * Date: 14-3-10
 */

public class OrderAnalyzeServiceTest extends BaseAnalyzeTest {

    @Autowired
    private MessageAnalyzeService messageAnalyzeService;

    @Autowired
    private OrderAnalyzeTestService orderAnalyzeTestService;


    @Before
    public void init() {
        super.init();
    }


    /**
     * Test Case.该测试类不能加Transactional注解,因为初始化测试数据和校验测试结果要在不同的事务里
     *
     * 1.新增的原始订单
     *  1.1 无邮费,无服务补差和邮费补差,1个订单项,待处理的原始订单
     *  1.2 带邮费.无服务补差和邮费补差,1个订单项,待处理的原始订单
     *  1.3 带邮费.带1个服务补差产品和1个订单项,待处理的原始订单
     *  1.4 带邮费.只带1个邮费补差产品,已发货的原始订单
     *  1.5 带邮费,1个服务补差,1个邮费补差,4个订单项,分属3个仓库,待处理的原始订单
     *  1.6 带邮费,1个服务补差,1个邮费补差,2个普通产品订单项,2个套餐产品订单项(2个产品),分属5个仓库,待处理的原始订单
     *  1.7 带邮费,无服务补差和邮费补差,2个订单项,分属2个仓库,已签收的原始订单
     *  1.8 无邮费,1个服务补差,1个邮费补差,2个订单项,待处理的天猫原始订单,其中一个订单项对应的产品同时满足一个天猫产品优惠(对应2个赠品项)和一个天猫品牌优惠活动(对应一个赠品项),自动添加多项赠品
     *  1.9 无邮费,1个服务补差,1个邮费补差,2个订单项,待处理的天猫原始订单,其中一个订单项对应的产品同时满足一个京东产品优惠(对应2个赠品项)和一个天猫品牌优惠活动(对应一个赠品项),自动添加多项赠品
     *
     * 2.更新的原始订单(略)
     *  2.1 1.6的原始订单,状态为已签收,同时备注也有改变.
     *  2.2 1.4的原始订单,状态为已签收,同时备注也有改变.
     *
     * 3.队列中的订单按修改时间倒序排列与去重
     *  1. 构造3个原始订单,主键相同.第一个是1.6的原始订单a,第二个订单b和第三个订单c各比第一个订单的修改时间早1和2个小时.按照[b,a,c]的顺序构造队列并解析
     *      测试目标是只解析中间a的原始订单,把b和c从队列删除.
     *
     */
    @Test
    public void test11() {
        OriginalOrderBuilder builder = orderAnalyzeTestService.createBuilder();
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1,1000).longValue()), 2, null);
        OriginalOrder originalOrder = builder.build();

        analyzeAndCheckResult(originalOrder, new OriginalOrderAnalyzeExceptedResult(1, 0, 0));
    }

    @Test
    public void test12() {
        OriginalOrderBuilder builder = orderAnalyzeTestService.createBuilder();
        builder.setPostFee(Money.valueOfCent(10000L));
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, null);
        OriginalOrder originalOrder = builder.build();

        analyzeAndCheckResult(originalOrder, new OriginalOrderAnalyzeExceptedResult(1, 0, 0));
    }

    @Test
    public void test13() {
        OriginalOrderBuilder builder = orderAnalyzeTestService.createBuilder();
        builder.setPostFee(Money.valueOfCent(10000L));
        builder.addPostCoverItem(10);
        builder.addServiceCoverItem(10);
        OriginalOrder originalOrder = builder.build();

        analyzeAndCheckResult(originalOrder, new OriginalOrderAnalyzeExceptedResult(0, 1, 1));
    }

    @Test
    public void test14() {
        OriginalOrderBuilder builder = orderAnalyzeTestService.createBuilder();
        builder.setOriginalOrderStatus(OriginalOrderStatus.WAIT_BUYER_CONFIRM_GOODS);
        builder.setPostFee(Money.valueOfCent(10000L));
        builder.addPostCoverItem(10);
        OriginalOrder originalOrder = builder.build();

        analyzeAndCheckResult(originalOrder, new OriginalOrderAnalyzeExceptedResult(0, 1, 0));
    }

    @Test
    public void test15() {
        OriginalOrderBuilder builder = orderAnalyzeTestService.createBuilder();
        builder.setPostFee(Money.valueOfCent(10000L));
        builder.addPostCoverItem(10);
        builder.addServiceCoverItem(10);

        Repository repository = orderAnalyzeTestService.initRepository();
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, repository);
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, repository);
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, null);
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, null);

        OriginalOrder originalOrder = builder.build();

        analyzeAndCheckResult(originalOrder, new OriginalOrderAnalyzeExceptedResult(3, 1, 1));
    }

    @Test
    public void test16() {
        OriginalOrderBuilder builder = orderAnalyzeTestService.createBuilder();
        builder.setPostFee(Money.valueOfCent(10000L));
        builder.addPostCoverItem(10);
        builder.addServiceCoverItem(10);

        Repository repository = orderAnalyzeTestService.initRepository();
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, repository);
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, repository);
        builder.addMealsetItem(2);
        builder.addMealsetItem(2);

        OriginalOrder originalOrder = builder.build();

        analyzeAndCheckResult(originalOrder, new OriginalOrderAnalyzeExceptedResult(5, 1, 1));
    }

    @Test
    public void test17() {
        OriginalOrderBuilder builder = orderAnalyzeTestService.createBuilder();
        builder.setPostFee(Money.valueOfCent(10000L));
        builder.setOriginalOrderStatus(OriginalOrderStatus.TRADE_FINISHED);

        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, null);
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, null);

        OriginalOrder originalOrder = builder.build();

        analyzeAndCheckResult(originalOrder, new OriginalOrderAnalyzeExceptedResult(2, 0, 0));
    }

    @Test
    public void test18() {
        OriginalOrderBuilder builder = orderAnalyzeTestService.createBuilder();
        Shop shop = builder.getShop(PlatformType.TAO_BAO);
        builder.addPostCoverItem(10);
        builder.addServiceCoverItem(10);

        int giftCount = 2;
        int buyCount = 2;
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), buyCount, null);
        OriginalOrderItem originalOrderItem = builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), buyCount, null);
        Activity activity1 = orderAnalyzeTestService.initActivity(shop, ActivityType.PRODUCT, originalOrderItem, 2, giftCount);
        Activity activity2 = orderAnalyzeTestService.initActivity(shop, ActivityType.BRAND, originalOrderItem, 1, giftCount);

        OriginalOrder originalOrder = builder.build();

        analyzeAndCheckResult(originalOrder, new OriginalOrderAnalyzeExceptedResult(5, 1, 1, Lists.newArrayList(activity1, activity2)));

    }

    @Test
    public void test19() {
        OriginalOrderBuilder builder = orderAnalyzeTestService.createBuilder();
        Shop shop = builder.getShop(PlatformType.TAO_BAO);
        Shop jdShop = builder.getShop(PlatformType.JING_DONG);
        builder.addPostCoverItem(10);
        builder.addServiceCoverItem(10);

        int giftCount = 2;
        int buyCount = 2;
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), buyCount, null);
        OriginalOrderItem originalOrderItem = builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), buyCount, null);
        Activity activity1 = orderAnalyzeTestService.initActivity(shop, ActivityType.BRAND, originalOrderItem, 1, giftCount);
        Activity activity2 = orderAnalyzeTestService.initActivity(jdShop, ActivityType.PRODUCT, originalOrderItem, 2, giftCount);

        OriginalOrder originalOrder = builder.build();

        analyzeAndCheckResult(originalOrder, new OriginalOrderAnalyzeExceptedResult(3, 1, 1, Lists.newArrayList(activity1, activity2)));

    }



    @Test
    public void test31() {

        OriginalOrderBuilder builder = orderAnalyzeTestService.createBuilder();
        builder.setPostFee(Money.valueOfCent(10000L));
        builder.addPostCoverItem(10);
        builder.addServiceCoverItem(10);

        Repository repository = orderAnalyzeTestService.initRepository();
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, repository);
        builder.addProductItem(Money.valueOfCent(orderAnalyzeTestService.randomInt(1, 1000).longValue()), 2, repository);
        builder.addMealsetItem(2);
        builder.addMealsetItem(2);

        OriginalOrder originalOrder = builder.build();
        OriginalOrder originalOrderShadow1 = new OriginalOrder();
        originalOrderShadow1.setId(originalOrder.getId());
        MutableDateTime modifyTime = new MutableDateTime(originalOrder.getModifiedTime());
        modifyTime.addHours(-1);
        originalOrderShadow1.setModifiedTime(modifyTime.toDate());

        OriginalOrder originalOrderShadow2 = new OriginalOrder();
        originalOrderShadow2.setId(originalOrder.getId());
        modifyTime = new MutableDateTime(originalOrder.getModifiedTime());
        modifyTime.addHours(-2);
        originalOrderShadow2.setModifiedTime(modifyTime.toDate());

        messageAnalyzeService.analyzeOriginalOrders(Lists.newArrayList(originalOrderShadow2, originalOrder, originalOrderShadow1));

        orderAnalyzeTestService.checkAnalyzeResult(originalOrder, new OriginalOrderAnalyzeExceptedResult(5, 1, 1));

    }

    @Test
    @Transactional
    public void testBatchUpdateProcessed() {
        String tableName = "t_original_order";
        messageAnalyzeService.updateProcessed(createNotExistIdArray(1), tableName);
        messageAnalyzeService.updateProcessed(createNotExistIdArray(11), tableName);
        messageAnalyzeService.updateProcessed(createNotExistIdArray(21), tableName);
        messageAnalyzeService.updateProcessed(createNotExistIdArray(39), tableName);
        messageAnalyzeService.updateProcessed(createNotExistIdArray(40), tableName);
        messageAnalyzeService.updateProcessed(createNotExistIdArray(50), tableName);
        messageAnalyzeService.updateProcessed(createNotExistIdArray(61), tableName);
        messageAnalyzeService.updateProcessed(createNotExistIdArray(1001), tableName);

    }

    private int[] createNotExistIdArray(int count) {
        int[] array = new int[count];
        for (int i = 0; i < count; i++) {
            array[i] = Integer.parseInt(RandomStringUtils.randomNumeric(8));
        }

        return array;
    }

    private void analyzeAndCheckResult(OriginalOrder originalOrder, OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult exceptedResult) {

        messageAnalyzeService.analyzeOriginalOrders(Lists.newArrayList(originalOrder));
        orderAnalyzeTestService.checkAnalyzeResult(originalOrder, exceptedResult);

    }

    static class ActivityGift {
        Activity activity;
        int count;

        ActivityGift(Activity activity, int count) {
            this.activity = activity;
            this.count = count;
        }
    }

    static class OriginalOrderAnalyzeExceptedResult {
        int orderCount;
        int postCoverPaymentCount;
        int serviceCoverPaymentCount;
        List<Activity> activities;

        public OriginalOrderAnalyzeExceptedResult(int orderCount, int postCoverPaymentCount, int serviceCoverPaymentCount) {
            this.orderCount = orderCount;
            this.postCoverPaymentCount = postCoverPaymentCount;
            this.serviceCoverPaymentCount = serviceCoverPaymentCount;
        }

        public OriginalOrderAnalyzeExceptedResult(int orderCount, int postCoverPaymentCount, int serviceCoverPaymentCount, List<Activity> activities) {
            this(orderCount, postCoverPaymentCount, serviceCoverPaymentCount);
            this.activities = activities;
        }


        public int getOrderCount() {
            return orderCount;
        }

        public int getPostCoverPaymentCount() {
            return postCoverPaymentCount;
        }

        public int getServiceCoverPaymentCount() {
            return serviceCoverPaymentCount;
        }

        public List<Activity> getActivities() {
            return activities;
        }
    }

}
