package com.ejushang.steward.ordercenter.service;


import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.domain.Area;
import com.ejushang.steward.common.domain.City;
import com.ejushang.steward.common.domain.Conf;
import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.keygenerator.SystemConfConstant;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * User: liubin
 * Date: 14-4-14
 * Time: 上午9:19
 */
@Service
@Transactional
public class OrderAnalyzeTestService {

    private static final Logger log = LoggerFactory.getLogger(OrderAnalyzeTestService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductService productService;

    @Autowired
    private ActivityService activityService;




    public Product initProduct(String sku, Repository repository) {

        ProductCategory category = new ProductCategory();
        category.setName(randomString("分类", 6));
//            category.setCode(randomString("code", 6));
        generalDAO.saveOrUpdate(category);

        Brand brand = new Brand();
        brand.setName(randomString("品牌", 6));
        brand.setCode(randomString("code", 6));
        generalDAO.saveOrUpdate(brand);

        if(repository == null) {
            repository = initRepository();
        }

        Product product = new Product();
        product.setBrandId(brand.getId());
        product.setCategoryId(category.getId());
        product.setName(randomString("产品", 6));
        product.setProductNo(randomString("code", 6));
        if(StringUtils.isBlank(sku)) {
            sku = randomString("sku", 10);
        }
        product.setSku(sku);
        product.setDescription(randomString("desc", 10));
        product.setPicUrl(null);
        //1<=importPrice<=minimumPrice<=marketPrice<10000,单位分
        product.setMarketPrice(Money.valueOfCent(randomInt(1, 10000).longValue()));
        product.setMinimumPrice(Money.valueOfCent(randomInt(1, (int) product.getMarketPrice().getCent()).longValue()));
//        product.setImportPrice(Money.valueOfCent(randomInt(1, (int) product.getMinimumPrice().getCent()).longValue()));
        product.setLocation(ProductLocation.NORMAL);
        product.setStyle(ProductStyle.A);
        product.setSpeci(randomString("spec", 6));
        product.setWeight(randomNumericString(3) + "kg");
        generalDAO.saveOrUpdate(product);

        Storage storage = new Storage();
        storage.setProductId(product.getId());
        storage.setRepositoryId(repository.getId());
        storage.setAmount(10000);
        generalDAO.saveOrUpdate(storage);

        return product;

    }

    public Repository initRepository() {
        Repository repository;
        repository = new Repository();
        repository.setCode(randomString("code", 6));
        repository.setName(randomString("仓库", 6));
        repository.setShippingComp("shunfeng");
        //随机取得一个县
        Area area = jdbcTemplate.queryForObject("select * from t_area order by rand() limit 1", new AreaMapper());
        repository.setAreaId(area.getId());
        repository.setCityId(area.getCityId());
        repository.setProvinceId(generalDAO.get(City.class, area.getCityId()).getProvinceId());
        generalDAO.saveOrUpdate(repository);
        return repository;
    }

    public Object[] initMealset() {
        Mealset mealset = new Mealset();
        mealset.setName(randomString("套餐", 6));
        mealset.setSku(randomString("sku", 10));
        mealset.setSellDescription(randomString("desc", 12));

        long mealsetPriceCent = 0L;

        List<MealsetItem> mealsetItems = new ArrayList<MealsetItem>();
        for (int i = 0; i < 2; i++) {
            MealsetItem mealsetItem = new MealsetItem();
            mealsetItem.setPrice(Money.valueOfCent(randomInt(1, 1000).longValue()));
            mealsetItem.setAmount(2);
            mealsetPriceCent += mealsetItem.getPrice().getCent() * mealsetItem.getAmount();
            mealsetItem.setProductId(initProduct(null, null).getId());

            mealsetItems.add(mealsetItem);
        }

        generalDAO.saveOrUpdate(mealset);
        for(MealsetItem mealsetItem : mealsetItems) {
            mealsetItem.setMealsetId(mealset.getId());
            generalDAO.saveOrUpdate(mealsetItem);
        }

        //返回套餐总的单价
        return new Object[]{mealset, Money.valueOfCent(mealsetPriceCent)};
    }


    public Shop initShop(PlatformType platformType) {
        Shop shop = new Shop();
        shop.setPlatformType(platformType);
        shop.setBulletin(randomString("bulletin", 8));
        shop.setCatId(randomNumericString(8));
        shop.setDeExpress("shunfeng");
        shop.setDeliveryScore(randomNumericString(2));
        shop.setDescription(randomString("desc", 10));
        shop.setEnableMsg(true);
        shop.setItemScore(randomNumericString(2));
        shop.setNick(randomString("nickname", 6));
        shop.setServiceScore(randomNumericString(2));
        shop.setOutShopId(randomNumericString(8));
        shop.setShopType(ShopType.SHOP);
        shop.setTitle(randomString("title", 8));
        shop.setUid(randomString("uid", 12));

        generalDAO.saveOrUpdate(shop);
        return shop;
    }


    public Activity initActivity(Shop shop, ActivityType activityType, OriginalOrderItem originalOrderItem, int giftProductItemSize, int giftCount) {

        assertThat(giftProductItemSize, greaterThan(0));

        Activity activity = new Activity();
        Product product = productService.findProductBySKU(originalOrderItem.getSku());
        activity.setInUse(true);
        activity.setType(activityType);
        activity.setRemark(randomString("title", 6));
        switch (activityType) {
            case PRODUCT:
                activity.setProductId(product.getId());

                break;
            case BRAND:
                activity.setBrandId(product.getBrandId());
                activity.setActualFeeBegin(originalOrderItem.calculateOrderItemFee().subtract(Money.valueOf(1)));
                activity.setActualFeeEnd(originalOrderItem.calculateOrderItemFee().add(Money.valueOf(1)));

                break;
        }
        generalDAO.saveOrUpdate(activity);

        ActivityShop activityShop = new ActivityShop();
        activityShop.setShopId(shop.getId());
        activityShop.setActivityId(activity.getId());
        generalDAO.saveOrUpdate(activityShop);

        for (int i = 0; i < giftProductItemSize; i++) {

            ActivityItem activityItem = new ActivityItem();
            activityItem.setAmount(giftCount);
            Product giftProduct = initProduct(null, null);
            activityItem.setProductId(giftProduct.getId());
            activityItem.setActivityId(activity.getId());
            generalDAO.saveOrUpdate(activityItem);
            activity.getActivityItems().add(activityItem);
        }


        return activity;
    }



    @Transactional(propagation = Propagation.REQUIRED)
    public OriginalOrderBuilder createBuilder() {
        return new OriginalOrderBuilder();
    }



    /**
     * 校验分析结果是否正确
     * @param originalOrder
     * @param exceptedResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void checkAnalyzeResult(OriginalOrder originalOrder, OrderAnalyzeServiceTest.OriginalOrderAnalyzeExceptedResult exceptedResult) {
        //清空session
//        generalDAO.getSession().flush();
//        generalDAO.getSession().clear();

        List<Payment> payments = paymentService.findByPlatformOrderNo(originalOrder.getPlatformType(), originalOrder.getPlatformOrderNo());

        List<Payment> orderPostPayments = new ArrayList<Payment>();
        List<Payment> postCoverPayments = new ArrayList<Payment>();
        List<Payment> serviceCoverPayments = new ArrayList<Payment>();

        for(Payment payment : payments) {
            switch (payment.getType()) {
                case ORDER_POST_FEE:
                    orderPostPayments.add(payment);
                    break;
                case POST_COVER:
                    postCoverPayments.add(payment);
                    break;
                case SERVICE_COVER:
                    serviceCoverPayments.add(payment);
                    break;
            }
        }

        if(originalOrder.getPostFee().getCent() != 0L) {
            //校验是否创建了邮费预收款
            assertThat(orderPostPayments.size(), is(1));
            Payment payment = orderPostPayments.get(0);
            assertThat(payment.getPaymentFee(), is(originalOrder.getPostFee()));
            assertThat(payment.getAllocateStatus(), is(PaymentAllocateStatus.WAIT_ALLOCATE));
            assertThat(payment.getType(), is(PaymentType.ORDER_POST_FEE));
            assertThat(payment.getOriginalOrderId(), is(originalOrder.getId()));
        }

        //校验生成的邮费和服务补差预收款
        assertThat(String.format("生成的邮费补差预收款不匹配, originalOrderId[%d]", originalOrder.getId()), postCoverPayments.size(), is(exceptedResult.getPostCoverPaymentCount()));
        assertThat(String.format("生成的服务补差预收款不匹配, originalOrderId[%d]", originalOrder.getId()), serviceCoverPayments.size(), is(exceptedResult.getServiceCoverPaymentCount()));

        //校验生成的订单数量
        List<Order> orders = orderService.findByPlatformOrderNo(originalOrder.getPlatformType(), originalOrder.getPlatformOrderNo());
        assertThat(String.format("生成的订单数量不匹配, originalOrderId[%d]", originalOrder.getId()), orders.size(), is(exceptedResult.getOrderCount()));

        List<Activity> activities = exceptedResult.getActivities();
        Map<Integer, Integer> giftAmountMap = new HashMap<Integer, Integer>();
        if(activities != null) {
            for(Activity activity : activities) {

                List<ActivityShop> activityShopList = activityService.findActivityShop(activity.getId());
                assertThat(activityShopList.isEmpty(), is(false));
                boolean isActivitySuited = false;
                for(ActivityShop activityShop : activityShopList) {
                    if(activityShop.getShopId().equals(originalOrder.getShopId())) {
                        isActivitySuited = true;
                        break;
                    }
                }
                if(!isActivitySuited) {
                    //该原始订单对应的店铺不适用于该活动
                    continue;
                }

                for(ActivityItem activityItem : activity.getActivityItems()) {
                    Integer productId = activityItem.getProductId();
                    Integer productAmount = giftAmountMap.get(productId);
                    if(productAmount == null) productAmount = 0;
                    giftAmountMap.put(productId, productAmount + activityItem.getAmount());
                }
            }
        }

        for(Order order : orders) {

            //校验订单下面的订单项都属于该订单对应的仓库
            Set<Integer> productIdSet = new HashSet<Integer>();
            for(OrderItem orderItem : order.getOrderItemList()) {
                productIdSet.add(orderItem.getProductId());
                //校验订单项的specInfo属性不为空
                assertThat(orderItem.getSpecInfo(), containsString(";"));
                //校验优惠活动增加的赠品
                if(orderItem.getType().equals(OrderItemType.GIFT)) {
                    assertThat("订单项是赠品,但是并没有查找到对应的优惠活动", giftAmountMap.containsKey(orderItem.getProductId()), is(true));
//                    assertThat("赠品的订单项数量与优惠活动中设置的不一致", orderItem.getBuyCount(), is(giftAmountMap.get(orderItem.getProductId())));
                    //完全匹配,从优惠活动map中删除此项
                    giftAmountMap.remove(orderItem.getProductId());
                }
            }
            Map<Integer, List<Storage>> productsWithStorage = storageService.findProductsWithStorage(productIdSet);
            assertThat(productsWithStorage.size(), greaterThan(0));
            for(Map.Entry<Integer, List<Storage>> entry : productsWithStorage.entrySet()) {
                boolean repoFind = false;
                Integer productId = entry.getKey();
                List<Storage> storages = entry.getValue();
                for(Storage storage : storages) {
                    if(storage.getRepositoryId().equals(order.getRepoId())) {
                        repoFind = true;
                        break;
                    }
                }
                assertThat(String.format("发现订单[id=%d]下面的订单项对应的产品[id=%d]不属于该订单对应的仓库", order.getId(), productId), repoFind, is(true));
            }
        }

        if(activities != null && !activities.isEmpty()) {
            assertThat("已设置了优惠活动,但是并没有将赠品正确加入订单", giftAmountMap.isEmpty(), is(true));
        }

        //校验原始订单是否已经被处理
        Boolean processed = jdbcTemplate.queryForObject("select processed from t_original_order where id = ?", new Object[]{originalOrder.getId()}, Boolean.class);
        assertThat(String.format("原始订单被处理过了,但是processed还没变为true, originalOrderId[%d]", originalOrder.getId()), processed, is(true));
    }


    public String randomString(String prefix, int count) {
        return (prefix == null ? "" : prefix) + RandomStringUtils.randomAlphabetic(count);
    }

    public String randomNumericString(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    public Integer randomInt(int start, int end) {
        if(start == end) {
            return start;
        }
        Random random = new Random();
        int result;
        do {
            result = random.nextInt(end);
        } while(result < start);
        return result;
    }

    private class AreaMapper implements RowMapper<Area> {

        public Area mapRow(ResultSet rs, int rowNum) throws SQLException {
            Area area = new Area();
            area.setId(rs.getString("id"));
            area.setName(rs.getString("name"));
            area.setCityId(rs.getString("city_id"));

            return area;
        }

    }


}


