package com.ejushang.steward.ordercenter.service;


import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.keygenerator.SystemConfConstant;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * User: liubin
 * Date: 14-4-14
 * Time: 上午9:19
 */
@Service
@Transactional
public class OrderAnalyzeService {

    private static final Logger log = LoggerFactory.getLogger(OrderAnalyzeService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderGenerateService orderGenerateService;

    @Autowired
    private ConfService confService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MealSetService mealSetService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private OriginalOrderService originalOrderService;


    /**
     *
     * @param originalOrder
     * @param productStorageCache
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void analyze(OriginalOrder originalOrder, Map<Integer, List<Storage>> productStorageCache) {
        //一些校验
        checkPreconditions(originalOrder);

        //根据外部订单号,查询该外部订单号是否在智库城系统已经生成订单
        List<Order> oldOrders = orderService.findByPlatformOrderNo(originalOrder.getPlatformType(), originalOrder.getPlatformOrderNo());
        if(oldOrders.isEmpty()) {
            //根据外部订单号,查询该外部订单号是否对应一笔服务补差或邮费补差预收款
            List<Payment> payments = paymentService.findByPlatformOrderNo(originalOrder.getPlatformType(), originalOrder.getPlatformOrderNo());
            if(payments.isEmpty()) {
                //该原始订单在系统未被处理过,进行新增
                createNewOrder(originalOrder, productStorageCache);
                //试着查询该原始订单对应的品牌并记录
                originalOrderService.guessOriginalOrderBrand(originalOrder, true);

            } else {
                //该外部订单号是否对应了服务补差或邮费补差预收款,也要更新备注
                paymentService.updateByOriginalOrder(originalOrder, payments);
            }
        } else {
            //该外部订单号在智库城系统已经生成订单, 进行更新
            orderGenerateService.updateOrders(originalOrder, oldOrders);
        }
    }

    /**
     * 进行订单分析前,对原始订单做一些校验工作
     * @param originalOrder
     */
    private void checkPreconditions(OriginalOrder originalOrder) {
        //平台不能为空
        PlatformType platformType = originalOrder.getPlatformType();
        if(platformType == null) {
            throw new StewardBusinessException("原始订单中的外部平台类型为空");
        }
        //原始订单状态不能为空
        OriginalOrderStatus originalOrderStatus;
        try {
            originalOrderStatus = OriginalOrderStatus.valueOf(originalOrder.getStatus());
        } catch (IllegalArgumentException e) {
            throw new StewardBusinessException(String.format("原始订单中的状态无法识别, originalOrderStatus[%s]", originalOrder.getStatus()));
        }
        //判断状态是否能够处理
        switch (originalOrderStatus) {
            case WAIT_SELLER_SEND_GOODS:
            case WAIT_BUYER_CONFIRM_GOODS:
            case TRADE_FINISHED:
            case FINISHED_L:
                break;
            default:
                throw new StewardBusinessException(String.format("原始订单中存在无法处理的状态, originalOrderStatus[%s]", originalOrder.getStatus()));
        }
        //判断原始订单项不能为空
        List<OriginalOrderItem> originalOrderItems = originalOrder.getOriginalOrderItemList();
        if(originalOrderItems.isEmpty()) {
            throw new StewardBusinessException("原始订单中没有查询到原始订单项");
        }
    }

    /**
     * 根据原始订单新增订单
     * @param originalOrder
     * @param productStorageCache
     * @return
     */
    private List<Order> createNewOrder(OriginalOrder originalOrder, Map<Integer, List<Storage>> productStorageCache) {

        //从原始订单中分析生成真实订单
        if(log.isDebugEnabled()) {
            log.debug("开始从原始订单中分析生成智库城订单,原始订单信息[id={}, itemSize={}]", originalOrder.getId(), originalOrder.getOriginalOrderItemList().size());
        }

        Order order = extractOrder(originalOrder);
        if(order == null) {
            log.info("原始订单中的订单项只有服务或邮费补差产品,所以不必生成订单,原始订单信息[id={}, itemSize={}]", originalOrder.getId(), originalOrder.getOriginalOrderItemList().size());
            return new ArrayList<Order>();
        }

        if(log.isDebugEnabled()) {
            log.debug("生成的智库城订单信息(未拆单)[itemSize={}]", order.getOrderItemList().size());
        }

        //拆单之前为订单根据优惠活动加赠品
        addGiftByActivity(order);

        List<Order> orders = orderGenerateService.createOrderWithSplit(false, order, productStorageCache);

        if(log.isDebugEnabled()) {
            log.debug("原始订单[id={}, itemSize={}]处理结束, 是否发生拆单:{}, 生成的订单数量:{}, 生成订单的概要信息:", new Object[]{originalOrder.getId(),
                    originalOrder.getOriginalOrderItemList().size(), orders.size()>1?"是":"否", orders.size()});
            for(Order generateOrder : orders) {
                log.debug("生成的订单信息[id={}, itemSize={}]", generateOrder.getId(), generateOrder.getOrderItemList().size());
            }
        }

        return orders;

    }

    /**
     * 拆单之前为订单根据优惠活动加赠品
     * @param order
     */
    private void addGiftByActivity(Order order) {
        Map<Activity, List<OrderItem>> activityToOrderItemMap = activityService.findInUseActivityByOrder(order);
        if(activityToOrderItemMap.isEmpty()) return;
        Map<Integer, Integer> giftAmountMap = new HashMap<Integer, Integer>();
        for(Map.Entry<Activity, List<OrderItem>> entry : activityToOrderItemMap.entrySet()) {
            Activity activity = entry.getKey();
            List<OrderItem> orderItemList = entry.getValue();
            int buyCount = 0;
            for(OrderItem orderItem : orderItemList) {
                buyCount += orderItem.getBuyCount();
            }
            for(ActivityItem activityItem : activity.getActivityItems()) {
                Integer productId = activityItem.getProductId();
                Integer productAmount = giftAmountMap.get(productId);
                if(productAmount == null) productAmount = 0;
                giftAmountMap.put(productId, productAmount + buyCount * activityItem.getAmount());
            }
        }

        for(Map.Entry<Integer, Integer> entry : giftAmountMap.entrySet()) {
            Integer productId = entry.getKey();
            Integer amount = entry.getValue();
            if(amount <= 0) continue;

            OrderItem orderItem = orderGenerateService.createOrderItemByGift(order, productId, amount);
            order.getOrderItemList().add(orderItem);
        }
    }


    /**
     * 从原始订单中分析生成真实订单
     *
     * @param originalOrder
     * @return
     *
     */
    private Order extractOrder(OriginalOrder originalOrder) {

        PlatformType platformType = originalOrder.getPlatformType();
        OriginalOrderStatus originalOrderStatus = OriginalOrderStatus.valueOf(originalOrder.getStatus());
        List<OriginalOrderItem> originalOrderItems = originalOrder.getOriginalOrderItemList();

        Order order = new Order();

        switch (originalOrderStatus) {
            case WAIT_SELLER_SEND_GOODS:
                order.setStatus(OrderStatus.WAIT_PROCESS);
                break;
            case WAIT_BUYER_CONFIRM_GOODS:
                order.setStatus(OrderStatus.INVOICED);
                break;
            case TRADE_FINISHED:
            case FINISHED_L:
                order.setStatus(OrderStatus.SIGNED);
                break;
        }

        order.setType(OrderType.NORMAL);
        order.setGenerateType(OrderGenerateType.AUTO_CREATE);
        order.setPlatformType(platformType);
        order.setPlatformOrderNo(originalOrder.getPlatformOrderNo());
        order.setOriginalOrderId(originalOrder.getId());
        order.setShopId(originalOrder.getShopId());

        order.setBuyerId(originalOrder.getBuyerId());
        order.setBuyerAlipayNo(originalOrder.getBuyerAlipayNo());
        order.setBuyerMessage(originalOrder.getBuyerMessage());
        order.setRemark(originalOrder.getRemark());
        order.setBuyTime(originalOrder.getBuyTime());
        order.setPayTime(originalOrder.getPayTime());

        Invoice invoice = new Invoice();
        Receiver receiver = originalOrder.getReceiver().clone();
        if(PlatformType.TAO_BAO.equals(originalOrder.getPlatformType()) || PlatformType.TAO_BAO_2.equals(originalOrder.getPlatformType())) {
            //淘宝的地址没有带省市县信息,需要添加上去
            receiver.copyAreaToAddress();
        }
        invoice.setReceiver(receiver);
        order.setInvoice(invoice);
        order.setNeedReceipt(originalOrder.getNeedReceipt());
        order.setReceiptTitle(originalOrder.getReceiptTitle());
        order.setReceiptContent(originalOrder.getReceiptContent());

        if(originalOrder.getPostFee().greaterThan(Money.valueOf(0))) {
            //有订单邮费,生成一条订单邮费预收款
            paymentService.createByOriginalOrder(originalOrder);
        }

        List<OrderItem> orderItems = extractOrderItems(originalOrder, originalOrderItems);
        if(orderItems.isEmpty()) {
            //原始订单项只有服务或邮费补差产品,所以不必生成订单
            return null;
        }

        order.setOrderItemList(orderItems);

        return order;
    }

    /**
     * 从原始订单项中分析生成订单项
     *
     * @param originalOrder
     * @param originalOrderItems
     * @return
     *
     */
    private List<OrderItem> extractOrderItems(OriginalOrder originalOrder, List<OriginalOrderItem> originalOrderItems) {

        List<OrderItem> orderItems = new ArrayList<OrderItem>();

        //邮费补差产品
        String postageProductSku = confService.getConfValue(SystemConfConstant.POSTAGE_PRODUCT_SKU);
        //服务补差产品
        String serviceProductSku = confService.getConfValue(SystemConfConstant.SERVICE_PRODUCT_SKU);

        for (OriginalOrderItem ooItem : originalOrderItems) {
            //以防万一,后面模块查询会用到
            ooItem.setOriginalOrder(originalOrder);

            String sku = ooItem.getSku();
            if (StringUtils.isBlank(sku)) {
                throw new StewardBusinessException(String.format("原始订单项的sku为空, originalOrderItemId[%d]", ooItem.getId()));
            }

            if(sku.equals(postageProductSku)) {
                //该订单项是邮费补差产品,需要生成一条邮费补差预收款
                paymentService.createByOriginalOrderItem(PaymentType.POST_COVER, ooItem);
                //停止原始订单项的解析
                continue;
            } else if(sku.equals(serviceProductSku)) {
                //该订单项是邮费补差产品,需要生成一条服务补差预收款
                paymentService.createByOriginalOrderItem(PaymentType.SERVICE_COVER, ooItem);
                //停止原始订单项的解析
                continue;
            }

            boolean findSku = false;

            Mealset mealset = mealSetService.findBySku(sku);
            if(mealset != null) {
                //从套餐查询
                //被删除的套餐也会生效
                List<MealsetItem> mealsetItems = mealset.getMealsetItemList();
                for(MealsetItem mealsetItem : mealsetItems) {
                    Product product = mealsetItem.getProduct();
                    OrderItem orderItem = orderGenerateService.createOrderItemByOriginalOrderItem(OrderItemType.MEALSET, ooItem, product, mealsetItem);
                    orderItems.add(orderItem);
                    findSku = true;
                }
            } else {
                //从产品表查找
                //被删除的产品也会生效
                Product product = productService.findProductBySKU(sku);
                if(product != null) {
                    OrderItem orderItem = orderGenerateService.createOrderItemByOriginalOrderItem(OrderItemType.PRODUCT, ooItem, product, null);
                    orderItems.add(orderItem);
                    findSku = true;
                }
            }

            if(findSku) {
                continue;
            }

            throw new StewardBusinessException(String.format("根据sku查询不到产品或套餐, sku[%s]", sku));

        }

        return orderItems;
    }



}


