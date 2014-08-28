package com.ejushang.steward.ordercenter.service;


import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.keygenerator.SequenceGenerator;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import com.ejushang.steward.ordercenter.util.OrderUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


/**
 * User: liubin
 * Date: 14-4-14
 * Time: 上午9:19
 */
@Service
@Transactional
public class OrderGenerateService {

    private static final Logger log = LoggerFactory.getLogger(OrderGenerateService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderFeeService orderFeeService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderFlowService orderFlowService;


    /**
     * 根据原始订单项创建订单项
     * @param orderItemType
     * @param originalOrderItem
     * @param product
     * @param mealsetItem
     * @return
     */
    public OrderItem createOrderItemByOriginalOrderItem(OrderItemType orderItemType, OriginalOrderItem originalOrderItem, Product product, MealsetItem mealsetItem) {

        OriginalOrder originalOrder = originalOrderItem.getOriginalOrder();

        OrderItem orderItem = new OrderItem();

        orderItem.setPlatformSubOrderNo(originalOrderItem.getPlatformSubOrderNo());
        orderItem.setOriginalOrderItemId(originalOrderItem.getId());

        orderItem.setPlatformType(originalOrder.getPlatformType());
        orderItem.setPlatformOrderNo(originalOrder.getPlatformOrderNo());

        orderItem.setValid(true);
        orderItem.setType(orderItemType);
        //根据原始订单的状态,设置订单项状态
        switch (OriginalOrderStatus.valueOf(originalOrder.getStatus())) {
            case WAIT_SELLER_SEND_GOODS:
            case WAIT_BUYER_CONFIRM_GOODS:
                orderItem.setStatus(OrderItemStatus.NOT_SIGNED);
                break;
            case TRADE_FINISHED:
            case FINISHED_L:
                orderItem.setStatus(OrderItemStatus.SIGNED);
                break;
            default:
                throw new StewardBusinessException(String.format("原始订单中的存在无法处理的状态, originalOrderId[%d], originalOrderStatus[%s]",
                        originalOrder.getId(), originalOrder.getStatus()));

        }


        orderItem.setProductId(product.getId());
        orderItem.setProductCode(product.getProductNo());
        orderItem.setProductName(product.getName());
        orderItem.setProductSku(product.getSku());
        orderItem.setProduct(product);
        orderItem.setSpecInfo(OrderUtil.getSpecInfo(product));
        orderItem.setOuterSku(originalOrderItem.getOuterSku());

        //抓取订单只会有普通产品和套餐
        switch (orderItemType) {
            case PRODUCT: {

                orderItem.setBuyCount(originalOrderItem.getBuyCount().intValue());
                orderItem.setPrice(originalOrderItem.getPrice());
                orderItem.setDiscountFee(originalOrderItem.getDiscountFee());
                orderItem.setDiscountPrice(orderItem.calculateDiscountPrice());
                orderItem.setSharedDiscountFee(originalOrderItem.getAllPartMjzDiscount());

                break;
            }
            case MEALSET: {

                //如果是套餐,则订单明细购买数量=套餐订购数量 * 套餐中该明细数量
                orderItem.setBuyCount(originalOrderItem.getBuyCount().intValue() * mealsetItem.getAmount());
                orderItem.setPrice(mealsetItem.getPrice());
                //套餐没有订单项优惠
                orderItem.setDiscountFee(Money.valueOf(0));
                //套餐促销价就是一口价
                orderItem.setDiscountPrice(mealsetItem.getPrice());
                orderItem.setSourceItemId(mealsetItem.getId());

                //根据当前套餐项的占比,计算分摊的优惠金额
                BigDecimal mealsetItemTotalPrice = mealsetItem.getPrice().multiply(mealsetItem.getAmount()).getAmountWithBigDecimal();
                BigDecimal percent = new BigDecimal(0d);
                if(originalOrderItem.getPrice().getAmount() != 0d) {
                    percent = mealsetItemTotalPrice.divide(originalOrderItem.getPrice().getAmountWithBigDecimal(), 2, RoundingMode.HALF_UP);
                }
                Money sharedDiscountFee = Money.valueOf(originalOrderItem.getAllPartMjzDiscount().getAmountWithBigDecimal().setScale(2, RoundingMode.HALF_UP).multiply(percent).doubleValue());
                orderItem.setSharedDiscountFee(sharedDiscountFee);

                break;
            }
            default: {
                throw new StewardBusinessException(String.format("根据原始订单项创建订单项的时候传入的类型不正确, originalOrderItemId[%d], type[%s]",
                        originalOrderItem.getId(), orderItemType));
            }
        }


        return orderItem;

    }

    /**
     * 抓单的时候添加优惠活动所送的赠品订单项
     * @param order
     * @param productId
     * @param amount
     * @return
     */
    public OrderItem createOrderItemByGift(Order order, Integer productId, Integer amount) {

        OrderItem orderItem = new OrderItem();

        orderItem.setPlatformType(order.getPlatformType());
        orderItem.setPlatformOrderNo(order.getPlatformOrderNo());

        orderItem.setValid(true);
        orderItem.setType(OrderItemType.GIFT);
        orderItem.setStatus(OrderItemStatus.NOT_SIGNED);

        orderItem.setProductId(productId);
        Product product = productService.get(productId);
        orderItem.setProductCode(product.getProductNo());
        orderItem.setProductName(product.getName());
        orderItem.setProductSku(product.getSku());
        orderItem.setProduct(product);
        orderItem.setSpecInfo(OrderUtil.getSpecInfo(product));

        orderItem.setBuyCount(amount);

        return orderItem;

    }


    /**
     * 订单拆分
     * repoOrderItemListMap代表订单项所要拆分的目标仓库
     * 例如{repo1:[[item1,item2],[item3,item4]], repo2:[[item5,item6]]}
     * 将拆分成3个订单,第一个订单:仓库1,订单项1和2;第二个订单:仓库2,订单项3和4;第三个订单:仓库3,订单项5和6;
     *
     * @param manual
     * @param order
     * @param repoOrderItemListMap 订单项根据仓库的分组,key-repo, value-属于该仓库的订单项列表的列表
     * @return
     */
    public List<Order> splitOrder(boolean manual, Order order, Map<Repository, List<List<OrderItem>>> repoOrderItemListMap) {

        List<Order> results = new ArrayList<Order>();
        if(repoOrderItemListMap.isEmpty()) {
            log.warn("拆分订单的时候传进的仓库分组为空,orderId[{}]", order.getId());
            return results;
        }

        if(log.isDebugEnabled()) {
            int count = 0;
            for(Map.Entry<Repository, List<List<OrderItem>>> entry : repoOrderItemListMap.entrySet()) {
                List<List<OrderItem>> orderItemsInThisRepoList = entry.getValue();
                count += orderItemsInThisRepoList.size();
            }
            log.debug("将对订单[id={}]拆分成{}个订单", order.getId(), count);
        }


        for(Map.Entry<Repository, List<List<OrderItem>>> entry : repoOrderItemListMap.entrySet()) {
            Repository repository = entry.getKey();
            List<List<OrderItem>> orderItemsInThisRepoList = entry.getValue();

            for(List<OrderItem> orderItemsInThisRepo : orderItemsInThisRepoList) {

                Order copiedOrder = order.copyForSplit();
                copiedOrder.setRepoId(repository.getId());
                if(StringUtils.isBlank(order.getInvoice().getShippingComp())) {
                    copiedOrder.getInvoice().setShippingComp(repository.getShippingComp());
                }
                for(OrderItem orderItem : orderItemsInThisRepo) {
                    OrderItem copiedOrderItem = orderItem.copyForSplit();
                    copiedOrder.getOrderItemList().add(copiedOrderItem);
                }

                //保存拆分的订单
                OrderGenerateType orderGenerateType = manual ? OrderGenerateType.MANUAL_SPLIT : OrderGenerateType.AUTO_SPLIT;
                createOrder(orderGenerateType, copiedOrder);
                results.add(copiedOrder);
            }

        }

        //将被拆分的订单设为已被拆分
        markSplit(order);
        //记录拆分记录
        createSplitOrderDispose(manual, order, results);

        return results;
    }

    /**
     * 创建拆合记录
     * @param sourceOrder
     * @param targetOrders
     */
    private void createSplitOrderDispose(boolean manual, Order sourceOrder, List<Order> targetOrders) {
        OrderDispose orderDispose = new OrderDispose();
        orderDispose.setManual(manual);
        orderDispose.setType(OrderDisposeType.SPLIT);
        orderDispose.setSourceIds(sourceOrder.getId().toString());
        StringBuilder targetIds = new StringBuilder();
        for(Order order : targetOrders) {
            targetIds.append(order.getId()).append(",");
        }
        orderDispose.setTargetIds(targetIds.toString());
        generalDAO.saveOrUpdate(orderDispose);

    }

    /**
     * 创建订单,可能会产生订单拆分
     * 自动抓取的订单和手动下订单都会调用该方法
     *
     * @param order
     * @param productStorageCache
     * @return
     */
    public List<Order> createOrderWithSplit(boolean manual, Order order, Map<Integer, List<Storage>> productStorageCache) {

        if(productStorageCache == null) {
            //产品库存缓存为null,则主动查询订单下产品的库存
            Set<Integer> productIdSet = new HashSet<Integer>();
            for (OrderItem orderItem : order.getOrderItemList()) {
                Integer productId = orderItem.getProductId();
                productIdSet.add(productId);
            }

            productStorageCache = storageService.findProductsWithStorage(productIdSet);
            if(productStorageCache.size() != productStorageCache.size()) {
                //找出哪些产品没有库存信息,抛出错误消息
                for(Iterator<Integer> iterator = productIdSet.iterator(); iterator.hasNext();) {
                    Integer productId = iterator.next();
                    if(productStorageCache.containsKey(productId)) {
                        //如果产品有库存信息,则从集合删除.最后剩下的就是没有库存信息的产品
                        iterator.remove();
                    }
                }

                String errorMsg = String.format("订单ID[%d]拆单的时候发生错误,产品id[%s]找不到对应的库存信息", order.getId(), productIdSet.toString());
                log.error(errorMsg);
                throw new StewardBusinessException(errorMsg);
            }

        }


        //key为repo, value为productId列表
        Map<Repository, Set<Integer>> repoProductMap = classifyProductByRepo(order, productStorageCache);
        //设置订单对应仓库
        if(repoProductMap.size() == 1) {
            Repository repository = repoProductMap.entrySet().iterator().next().getKey();
            order.setRepoId(repository.getId());
            if(StringUtils.isBlank(order.getInvoice().getShippingComp())) {
                order.getInvoice().setShippingComp(repository.getShippingComp());
            }
        }

        //保存订单
        OrderGenerateType orderGenerateType = manual ? OrderGenerateType.MANUAL_CREATE : OrderGenerateType.AUTO_CREATE;
        createOrder(orderGenerateType, order);

        //判断是否需要拆单
        if(repoProductMap.size() <= 1) {
            log.debug("订单[id={}]只对应一个仓库,无需拆分", order.getId());

            return Lists.newArrayList(order);

        } else {
            //将订单项根据仓库ID分好组
            //key-repoId, value-属于该仓库的订单项列表
            Map<Repository, List<List<OrderItem>>> repoOrderItemListMap = new HashMap<Repository, List<List<OrderItem>>>();
            for(Map.Entry<Repository, Set<Integer>> entry : repoProductMap.entrySet()) {
                Repository repository = entry.getKey();
                Set<Integer> productSet = entry.getValue();
                for(OrderItem orderItem : order.getOrderItemList()) {
                    if(productSet.contains(orderItem.getProductId())) {
                        List<List<OrderItem>> orderItemsInRepoList = repoOrderItemListMap.get(repository);
                        if(orderItemsInRepoList == null) {
                            orderItemsInRepoList = new ArrayList<List<OrderItem>>();
                            orderItemsInRepoList.add(new ArrayList<OrderItem>());
                            repoOrderItemListMap.put(repository, orderItemsInRepoList);
                        }
                        List<OrderItem> orderItemsInRepo = orderItemsInRepoList.get(0);
                        orderItemsInRepo.add(orderItem);
                    }
                }
            }

            //进行拆单,并设置订单仓库
            return splitOrder(manual, order, repoOrderItemListMap);
        }
    }


    /**
     * 创建订单
     * @param orderGenerateType
     * @param order
     * @return
     */
    public void createOrder(OrderGenerateType orderGenerateType, Order order) {
        order.setGenerateType(orderGenerateType);
        for (OrderItem orderItem : order.getOrderItemList()) {
            orderFeeService.calculateOrderItemFee(orderItem);
        }
        //计算订单金额
        orderFeeService.calculateOrderFee(order, order.getOrderItemList());
        //生成订单号
        order.setOrderNo(SequenceGenerator.getInstance().getNextOrderNo());
        invoiceService.save(order.getInvoice());
        order.setInvoiceId(order.getInvoice().getId());
        order.setOnline(true);
        generalDAO.saveOrUpdate(order);
        for (OrderItem orderItem : order.getOrderItemList()) {
            orderItem.setOrderId(order.getId());
            generalDAO.saveOrUpdate(orderItem);
        }

    }

    /**
     * 根据仓库对产品进行分组
     * @param order
     * @param productStorageCache 系统所有产品与库存的缓存,自动拆单才需要传,手动传null
     * @return
     */
    private Map<Repository, Set<Integer>> classifyProductByRepo(Order order, Map<Integer, List<Storage>> productStorageCache) {

        Map<Repository, Set<Integer>> repoProductMap = new LinkedHashMap<Repository, Set<Integer>>();

        List<OrderItem> orderItems = order.getOrderItemList();

        for (OrderItem orderItem : orderItems) {

            Integer productId = orderItem.getProductId();
            Repository repository = pickProductRepo(productId, productStorageCache.get(productId));
            if(repository == null) {
                throw new StewardBusinessException(String.format("在拆分订单的时候,发现产品[id=%d]对应库存为null", productId));
            }

            Set<Integer> productIds = repoProductMap.get(repository);
            if (productIds == null) {
                productIds = new LinkedHashSet<Integer>();
                repoProductMap.put(repository, productIds);
            }
            productIds.add(productId);
        }
        return repoProductMap;
    }

    /**
     * 选择产品的发货仓库
     * @param productId
     * @param storages
     * @return
     */
    private Repository pickProductRepo(Integer productId, List<Storage> storages) {
        //当前版本,一个产品只会在一个仓库中有库存,所以直接选择一个就好
        if(storages == null || storages.isEmpty()) return null;
        return storages.get(0).getRepository();
    }

    /**
     * 标记订单为已拆分
     * @param order
     */
    private void markSplit(Order order) {
        order.setValid(false);
        generalDAO.saveOrUpdate(order);
        for(OrderItem orderItem : order.getOrderItemList()) {
            orderItem.setValid(false);
            generalDAO.saveOrUpdate(orderItem);
        }

    }




    /**
     * 根据原始订单信息,更新智库城订单
     * @param originalOrder
     * @param orders
     */
    public void updateOrders(OriginalOrder originalOrder, List<Order> orders) {

        OriginalOrderStatus originalOrderStatus = OriginalOrderStatus.valueOf(originalOrder.getStatus());
        OrderStatus orderStatus = null;
        switch (originalOrderStatus) {
            case WAIT_SELLER_SEND_GOODS:
                orderStatus = OrderStatus.WAIT_PROCESS;
                break;
            case WAIT_BUYER_CONFIRM_GOODS:
                orderStatus = OrderStatus.INVOICED;
                break;
            case TRADE_FINISHED:
            case FINISHED_L:
                orderStatus = OrderStatus.SIGNED;
                break;
        }

        for(Order order : orders) {

            log.debug("根据原始订单[id={}, status={}]更新订单信息[id={}, status={}], 目标状态[{}]", new Object[]{originalOrder.getId(), originalOrder.getStatus(),
                order.getId(), order.getStatus(), orderStatus});

            order.setRemark(originalOrder.getRemark());

            switch (orderStatus) {
                case WAIT_PROCESS:
                    break;

                case INVOICED:
                    //如果订单状态为已验货之前的状态,则更新订单的状态,否则不更新.
                    //因为智库城是在已验货的时候就向天猫发送发货的消息了,所以从已验货变到已发货需要智库城自己根据物流信息判断
//                    if(order.getStatus() != null && !OrderStatus.PRINTED.isAncestorOf(order.getStatus())) {
//                        order.setStatus(OrderStatus.INVOICED);
//                    }
                    break;

                case SIGNED:

                    if(OrderStatus.EXAMINED.equals(order.getStatus()) || OrderStatus.INVOICED.equals(order.getStatus())) {
                        //如果是已验货或者已发货,才变成已签收
                        orderFlowService.changeStatus(order, order.getStatus(), OrderStatus.SIGNED, false);
                    }
                    break;

//                    if(!oldOrderStatus.isAncestorOf(OrderStatus.SIGNED)) {
//                        log.warn("根据原始订单[id={}]更改订单[id={}]状态失败,当前订单状态不满足条件.当前状态[{}],目标状态[{}],", new Object[]{originalOrder.getId(),
//                                order.getId(), oldOrderStatus, OrderStatus.SIGNED});
//                    } else {
//                    }

                default:
                    break;

            }

            //保存订单的更新
            generalDAO.saveOrUpdate(order);

        }

        //根据外部订单号,查询该外部订单号是否对应了服务补差或邮费补差预收款,有的话也要更新备注
        List<Payment> payments = paymentService.findByPlatformOrderNo(originalOrder.getPlatformType(), originalOrder.getPlatformOrderNo());
        if(!payments.isEmpty()) {
            paymentService.updateByOriginalOrder(originalOrder, payments);
        }

    }



}
