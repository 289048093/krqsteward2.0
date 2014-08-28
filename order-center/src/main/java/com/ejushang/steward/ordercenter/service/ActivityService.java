package com.ejushang.steward.ordercenter.service;


import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.dao.hibernate.QueryPrepare;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.ActivityType;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.vo.ActivityVO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * User: liubin
 * Date: 14-4-14
 * Time: 上午9:19
 */
@Service
@Transactional
public class ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    @Autowired
    private GeneralDAO generalDAO;

    /**
     * 分页查询优惠活动
     *
     * @param activity
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public Page getByKey(Activity activity, Page page, String searchType, String searchValue) {
        if (log.isInfoEnabled()) {
            log.info(String.format("ActivityService类中的getByKey方法参数Activity[%s]", activity));
        }
        Search search = new Search(Activity.class).setCacheable(true);
        if (activity != null) {
            if (activity.getType() != null && activity.getType().equals(ActivityType.BRAND)) {
                search.addFilterEqual("type", ActivityType.BRAND);
            }
            if (activity.getType() != null && activity.getType().equals(ActivityType.PRODUCT)) {
                search.addFilterEqual("type", ActivityType.PRODUCT);
            }
        }
        if (!StringUtils.isBlank(searchType) && !StringUtils.isBlank(searchValue)) {
            search.addFilterLike(searchType, "%" + searchValue + "%");
        }
        search.addSortDesc("createTime");
        search.addPagination(page);
        List<ActivityVO> activityVOs = new ArrayList<ActivityVO>();
        List<Activity> activities = generalDAO.search(search);
        for (Activity activity1 : activities) {
            ActivityVO activityVO = activityToVo(activity1);
            activityVOs.add(activityVO);
        }
        page.setResult(activityVOs);

        return page;
    }

    /**
     * 添加优惠活动
     *
     * @param
     */
    @Transactional
    public void saveActivity(Activity activity, Integer[] prodId, Integer[] amount, Integer shopIds[]) {
        if (log.isInfoEnabled()) {
            log.info(String.format("ActivityService类中的saveActivity方法参数Activity[%s]", activity));
        }
        isProductExist(activity);

        if (activity.getId() == null) {
            generalDAO.saveOrUpdate(activity);
            for (int i = 0; i < shopIds.length; i++) {
                ActivityShop activityShop = new ActivityShop();
                activityShop.setShopId(shopIds[i]);
                activityShop.setActivityId(activity.getId());
                generalDAO.saveOrUpdate(activityShop);
            }
        }
        List<ActivityItem> activityItems = new ArrayList<ActivityItem>(prodId.length);
        for (int i = 0; i < prodId.length; i++) {
            ActivityItem activityItem = new ActivityItem();
            activityItem.setActivityId(activity.getId());
            activityItem.setProductId(prodId[i]);
            activityItem.setAmount(amount[i]);
            activityItems.add(activityItem);
        }
        activity.setActivityItems(activityItems);

        if (activity.getId() != null) {
            generalDAO.saveOrUpdate(activity);
            List<ActivityItem> activityItems1 = deleteActivityItem(activity);
            for (ActivityItem activityItem : activityItems1) {
                generalDAO.removeById(ActivityItem.class, activityItem.getId());
            }
            List<ActivityShop> activityShops = findActivityShop(activity.getId());

            if (shopIds.length <= activityShops.size()) {
                int i = 0;
                for (ActivityShop activityShop : activityShops) {
                    if (i < shopIds.length) {
                        activityShop.setShopId(shopIds[i]);
                        activityShop.setActivityId(activity.getId());
                        generalDAO.saveOrUpdate(activityShop);
                    } else {
                        generalDAO.removeById(ActivityShop.class, activityShop.getId());
                    }
                    i++;
                }
            } else {
                int i = 0;
                int k=0;
                for (ActivityShop activityShop : activityShops) {
                    activityShop.setShopId(shopIds[i]);
                    activityShop.setActivityId(activity.getId());
                    generalDAO.saveOrUpdate(activityShop);
                    i++;
                    k++;
                }
                for(int j=0;j<shopIds.length-i;j++){
                    ActivityShop activityShop=new ActivityShop();
                    activityShop.setShopId(shopIds[k]);
                    activityShop.setActivityId(activity.getId());
                    generalDAO.saveOrUpdate(activityShop);
                    k++;
                }

            }

        }
        generalDAO.saveOrUpdate(activityItems);


    }

    @Transactional(readOnly = true)
    public List<ActivityShop> findActivityShop(Integer id) {

        Search search = new Search(ActivityShop.class).setCacheable(true).addFilterEqual("activityId", id);
        return generalDAO.search(search);

    }

    /**
     * 查询符合该订单的优惠活动
     *
     * @param order
     * @return
     */
    @Transactional(readOnly = true)
    public Map<Activity, List<OrderItem>> findInUseActivityByOrder(Order order) {
        List<Activity> activities = new ArrayList<Activity>();
        Set<Integer> productIds = new HashSet<Integer>();
        Map<Integer, Money> brandMoneyMap = new HashMap<Integer, Money>();
        List<OrderItem> orderItems = order.getOrderItemList();
        if (orderItems == null || orderItems.isEmpty()) {
            return new HashMap<Activity, List<OrderItem>>();
        }

        for (OrderItem orderItem : orderItems) {
            productIds.add(orderItem.getProductId());
            Integer brandId = orderItem.getProduct().getBrandId();
            if (brandId == null) continue;
            Money money = brandMoneyMap.get(brandId);
            if (money == null) money = Money.valueOf(0d);
            brandMoneyMap.put(brandId, money.add(orderItem.calculateOrderItemFee()));
        }

        //如果单笔订单买了某个产品,加赠品.
        activities.addAll(findProductActivity(true, order.getShopId(), productIds));

        //单笔订单某个品牌总额满足一定金额,加赠品.
        for (Map.Entry<Integer, Money> entry : brandMoneyMap.entrySet()) {
            Money money = entry.getValue();
            Integer brandId = entry.getKey();

            activities.addAll(findBrandActivity(true, order.getShopId(), brandId, money));
        }

        //key-活动,value-适用的订单项
        Map<Activity, List<OrderItem>> activityToOrderItemMap = new HashMap<Activity, List<OrderItem>>();

        //查询这些活动适用于哪些订单项
        for(Activity activity : activities) {
            List<OrderItem> orderItemList = new ArrayList<OrderItem>();
            for(OrderItem orderItem : order.getOrderItemList()) {
                switch (activity.getType()) {
                    case PRODUCT: {
                        if(activity.getProductId().equals(orderItem.getProductId())) {
                            orderItemList.add(orderItem);
                        }
                        break;
                    }
                    case BRAND: {
                        if(activity.getBrandId().equals(orderItem.getProduct().getBrandId())) {
                            double orderItemFee = orderItem.calculateOrderItemFee().getAmount();
                            if(activity.getActualFeeBegin().getAmount() <= orderItemFee && activity.getActualFeeEnd().getAmount() >= orderItemFee) {
                                orderItemList.add(orderItem);
                            }
                        }
                        break;
                    }
                }
            }

            if(orderItemList.isEmpty()) {
                throw new StewardBusinessException(String.format("为优惠活动按照订单项分类的时候,发现优惠活动[id=%d]没有对应任何订单项,originalOrderId=[%d]",
                        activity.getId(), order.getOriginalOrderId()));
            }

            activityToOrderItemMap.put(activity, orderItemList);
        }

        //打印日志
        if (log.isDebugEnabled() && !activities.isEmpty()) {
            log.debug("为订单添加赠品,订单项信息:", order.getId());
            for (OrderItem orderItem : orderItems) {
                log.debug("订单项[productId={}, brandId={}, orderItemFee={}]", new Object[]{orderItem.getProductId(),
                        orderItem.getProduct().getBrandId(), orderItem.calculateOrderItemFee()});
            }

            for(Map.Entry<Activity, List<OrderItem>> entry : activityToOrderItemMap.entrySet()) {
                Activity activity = entry.getKey();
                List<OrderItem> orderItemList = entry.getValue();
                StringBuilder activityItemInfo = new StringBuilder();
                for (ActivityItem activityItem : activity.getActivityItems()) {
                    activityItemInfo.append(String.format("赠品id=%d,amount=%d;", activityItem.getProductId(), activityItem.getAmount()));
                }
                StringBuilder orderItemInfo = new StringBuilder();
                for (OrderItem orderItem : orderItemList) {
                    orderItemInfo.append(String.format("订单项[productId=%d, brandId=%d, orderItemFee=%s]", orderItem.getProductId(),
                            orderItem.getProduct().getBrandId(), orderItem.calculateOrderItemFee()));
                }

                log.debug("根据订单查找到满足条件的优惠活动,优惠活动信息[id={}],赠品信息[{}],订单项信息[{}]:",
                        new Object[]{activity.getId(), activityItemInfo.toString(), orderItemInfo.toString()});
            }

        }


        return activityToOrderItemMap;

    }

    /**
     * 查找产品活动
     * @param inUse
     * @param shopId
     * @param productIds
     * @return
     */
    public List<Activity> findProductActivity(boolean inUse, Integer shopId, Set<Integer> productIds) {
        if(productIds == null || productIds.isEmpty()) {
            throw new StewardBusinessException("查询产品对应的活动的时候发现产品集合为null");
        }
        if(shopId == null) {
            throw new StewardBusinessException("查询产品对应的活动的时候发现店铺ID为null");
        }

        StringBuilder hql = new StringBuilder("select a from ActivityShop s join s.activity a where a.inUse = ? and a.type = ? and s.shopId = ? ");
        List<Object> params = new ArrayList<Object>();
        params.add(inUse);
        params.add(ActivityType.PRODUCT);
        params.add(shopId);
        hql.append(" and a.productId in (");
        for(Integer productId : productIds) {
            hql.append("?,");
            params.add(productId);
        }
        hql.replace(hql.length()-1, hql.length(), ")");
        //使用查询缓存
        return generalDAO.queryWithPrepare(hql.toString(), null, new QueryPrepare() {
            @Override
            public void prepare(Query query) {
                query.setCacheable(true);
            }
        }, params.toArray());
    }

    /**
     * 查找品牌活动
     * @param inUse
     * @param shopId
     * @param brandId
     * @param orderItemFee
     * @return
     */
    public List<Activity> findBrandActivity(boolean inUse, Integer shopId, Integer brandId, Money orderItemFee) {
        if(orderItemFee == null) {
            throw new StewardBusinessException("查询品牌对应的活动的时候发现订单项金额为null");
        }
        if(shopId == null) {
            throw new StewardBusinessException("查询品牌对应的活动的时候发现店铺ID为null");
        }

        StringBuilder hql = new StringBuilder("select a from ActivityShop s join s.activity a where a.inUse = ? and a.type = ? and s.shopId = ? and a.brandId = ? " +
                "and a.actualFeeBegin <= ? and a.actualFeeEnd >= ? ");
        List<Object> params = new ArrayList<Object>();
        params.add(inUse);
        params.add(ActivityType.BRAND);
        params.add(shopId);
        params.add(brandId);
        params.add(orderItemFee);
        params.add(orderItemFee);

        return generalDAO.query(hql.toString(), null, params.toArray());
    }



    /**
     * 找出要删除的活动详情
     *
     * @param activity
     * @return
     */
    public List<ActivityItem> deleteActivityItem(Activity activity) {
        List<ActivityItem> activityItems = getByActivityId(activity.getId());
        List<ActivityItem> activityItems1 = activity.getActivityItems();
        for (ActivityItem activityItem : activityItems1) {
            if (activityItems.contains(activityItem)) {
                activityItems.remove(activityItem);
            }
        }
        return activityItems;
    }

    /**
     * 根据ID查询优惠活动详情
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<ActivityItem> getByActivityId(Integer id) {
        if (log.isInfoEnabled()) {
            log.info(String.format("ActivityService类中的getByActivityId方法参数id[%s]", id));
        }
        Search search = new Search(ActivityItem.class).setCacheable(true);
        search.addFilterEqual("activityId", id);
        return generalDAO.search(search);
    }

    /**
     * 根据ID查询优惠活动详情
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Activity getById(Integer id) {
        if (log.isInfoEnabled()) {
            log.info(String.format("ActivityService类中的getById方法参数id[%s]", id));
        }
        Activity activity = generalDAO.get(Activity.class, id);
        if (activity != null) {
            return activity;
        } else {
            throw new StewardBusinessException("没找到对应的优惠活动，请刷新后再进行操作");
        }
    }

    /**
     * 删除优惠活动
     *
     * @param ids
     */
    @Transactional
    public List<Activity> deleteActivity(Integer[] ids) {
        if (log.isInfoEnabled()) {
            log.info(String.format("ActivityService类中的deleteActivity方法参数id[%s]", ids));
        }
        List<Activity> activities = new ArrayList<Activity>();
        for (Integer id : ids) {
            Activity activity = getById(id);
            activities.add(activity);
        }
        for (Activity activity : activities) {
            for (ActivityItem activityItem : activity.getActivityItems()) {
                ActivityItem activityItem1 = activityItem;
                generalDAO.removeById(ActivityItem.class, activityItem.getId());
            }
            for (ActivityShop activityShop : findActivityShop(activity.getId())) {
                generalDAO.removeById(ActivityShop.class, activityShop.getId());
            }
            generalDAO.removeById(Activity.class, activity.getId());
        }

        return activities;
    }

    /**
     * 校验优惠活动
     *
     * @param activity
     */
    public void isProductExist(Activity activity) {
        Search search = new Search(Activity.class);
        if (activity.getType().equals(ActivityType.PRODUCT)) {
            search.addFilterEqual("productId", activity.getProductId());
            int count = generalDAO.count(search);
            if (count > 0 && activity.getId() == null || count > 1 && activity.getId() != null) {
                throw new StewardBusinessException("该商品已设置优惠活动，请检查后再进行操作");
            }
        }
        if (activity.getType().equals(ActivityType.BRAND)) {
            search.addFilterEqual("brandId", activity.getBrandId());
            List<Activity> activities = generalDAO.search(search);

            for (Activity activity1 : activities) {
                if (activity.getId() != null && activity.getId() == activity1.getId()) {
                    continue;
                }
                if (activity.getActualFeeBegin().compareTo(activity1.getActualFeeEnd()) == -1
                        && activity.getActualFeeBegin().compareTo(activity1.getActualFeeBegin()) == 1
                        || activity.getActualFeeBegin().compareTo(activity1.getActualFeeBegin()) == 0
                        || activity.getActualFeeEnd().compareTo(activity1.getActualFeeEnd()) == 0
                        || activity.getActualFeeEnd().compareTo(activity1.getActualFeeBegin()) == 0
                        || activity.getActualFeeBegin().compareTo(activity1.getActualFeeEnd()) == 0
                        || activity.getActualFeeEnd().compareTo(activity1.getActualFeeEnd()) == -1
                        && activity.getActualFeeEnd().compareTo(activity1.getActualFeeBegin()) == 1
                        || activity.getActualFeeBegin().compareTo(activity1.getActualFeeBegin()) == -1
                        && activity.getActualFeeEnd().compareTo(activity1.getActualFeeEnd()) == 1
                        ) {
                    throw new StewardBusinessException("金额范围有重叠，请检查后再进行操作");
                }
            }
        }
    }

    public ActivityVO activityToVo(Activity activity) {
        ActivityVO activityVO = new ActivityVO();
        activityVO.setActivityItems(activity.getActivityItems());
        List<Integer> shopIds = new ArrayList<Integer>();
        List<ActivityShop> activityShops = findActivityShop(activity.getId());
        for (ActivityShop activityShop : activityShops) {
            Integer shopId = activityShop.getShopId();
            shopIds.add(shopId);
        }
        activityVO.setShopIds(shopIds);
        activityVO.setActualFeeBegin(activity.getActualFeeBegin());
        activityVO.setActualFeeEnd(activity.getActualFeeEnd());
        activityVO.setBrand(activity.getBrand() == null ? null : activity.getBrand());
        activityVO.setCreateTime(activity.getCreateTime());
        activityVO.setId(activity.getId());
        activityVO.setInUse(activity.getInUse());
        activityVO.setOperatorId(activity.getOperatorId());
        activityVO.setProduct(activity.getProduct() == null ? null : activity.getProduct());
        activityVO.setRemark(activity.getRemark());
        activityVO.setType(activity.getType());
        activityVO.setUpdateTime(activity.getUpdateTime());
        return activityVO;
    }


}
