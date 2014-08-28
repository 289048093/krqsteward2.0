package com.ejushang.steward.ordercenter.service;


import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.PoiUtil;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import com.ejushang.steward.ordercenter.util.OrderExcelUtil;
import com.ejushang.steward.ordercenter.util.OrderUtil;
import com.ejushang.steward.ordercenter.vo.*;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;

/**
 * User: tin
 * Date: 14-4-14
 * Time: 上午9:19
 */
@Service
@Transactional
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);


    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private ProductService productService;


    @Autowired
    private StorageService storageService;

    @Autowired
    private OrderFlowService orderFlowService;

    @Autowired
    private OrderFeeService orderFeeService;

    @Autowired
    private OrderGenerateService orderGenerateService;

    @Autowired
    private OrderApproveService orderApproveService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private MealSetService mealSetService;


    /**
     * 查询订单
     *
     * @param map  controller接收过来的参数
     * @param page 分页参数
     * @return 返回List集合
     * @throws ParseException
     */

    @Transactional(readOnly = true)
    public Page findOrderDetails(Map<String, Object[]> map, Page page) throws ParseException {
        Map<String, String> orderMapConditions = new HashMap<String, String>();

        //将接受到的map参数解析并赋值给map1
        OrderUtil.getConditionMap(map, orderMapConditions);
        //显示层的Vo的List
        List<OrderVo> orderVos = new ArrayList<OrderVo>();
        //HQL条件的值
        List<Object> objects = new ArrayList<Object>();
        //拼接HQL的变量
        StringBuilder stringBuilder = new StringBuilder();
        //拼接HQL的方法
        OrderUtil.orderCondition(orderMapConditions, stringBuilder, objects, null);
        //执行HQL
        List<Order> orders = generalDAO.query(stringBuilder.toString(), page, objects.toArray());
        //拼接Vo的循环
        for (Order order : orders) {
            OrderUtil.getOrderVo(order, orderVos, order.getOrderItemList(),false);
        }
        if (page == null) {
            page = new Page(1, 1);
        }
        page.setResult(orderVos);
        return page;
    }

    public List<Order> findOrderbyOrderStatus(OrderStatus status) {
        if (log.isInfoEnabled()) {
            log.info(String.format("订单状态参数status[%s]", status));
        }
        //noinspection unchecked
        return generalDAO.search(new Search(Order.class).addFilterEqual("valid", true).addFilterEqual("status", status));
    }

    /**
     * 保存订单项
     *
     * @param orderItem 订单项对象
     */
    public void saveOrderItem(OrderItem orderItem) {
        generalDAO.saveOrUpdate(orderItem);
    }

    /**
     * 根据订单id查询该订单下的所有订单项
     */
    @Transactional(readOnly = true)
    public List<OrderItem> findOrderItemByOrderId(Integer orderId) {
        if (orderId == null) {
            throw new StewardBusinessException("orderId为空");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数orderId是：[%s]", orderId));
        }
        Search search = new Search(OrderItem.class);
        search.addFilterEqual("orderId", orderId);
        //noinspection unchecked
        return generalDAO.search(search);
    }


    /**
     * 根据订单map查询该订单下的所有订单项
     */
    @Transactional(readOnly = true)
    public List<OrderItemVo> findOrderItemVoByMap(Map<String, Object[]> map) {

        List<OrderItemVo> orderItemVos = new ArrayList<OrderItemVo>();
        Search search = new Search(OrderItem.class);
        //拼接map参数到search里
        OrderUtil.getOrderItemCondition(map, search);
        //noinspection unchecked
        List<OrderItem> orderItems = generalDAO.search(search);
        if (orderItems.size() == 0) {
            throw new StewardBusinessException("找不到对应订单项");
        }
        for (OrderItem orderItem : orderItems) {
            OrderItemVo orderItemVo = new OrderItemVo();

            //拼接OrderItemVo
            OrderUtil.getOrderItemVos(orderItemVo,orderItemVos, orderItem, null,generalDAO,null,false);
        }

        return orderItemVos;
    }

    /**
     * 审核信息和日志信息
     *
     * @param orderIds 订单ID
     * @return 返回审核信息和日志信息的Vo
     */
    public ApproveLogsVo findApproveLogs(Integer orderIds) {
        if (null == orderIds) {
            log.error(String.format("方法参数Integer类型的OrderIds为null"));
            throw new StewardBusinessException("请求出错，请勾选订单");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数Id是：[%s]", orderIds));
        }
        ApproveLogsVo approveLogsVo = new ApproveLogsVo();
        Order order = generalDAO.get(Order.class, orderIds);
        if (order != null) {
            approveLogsVo.setOrderApproves(order.getOrderApproveList());
            approveLogsVo.setOrderHandleLogs(order.getOrderHandleLogs());
        }
        return approveLogsVo;
    }


    /**
     * 根据订单id查询订单
     */
    @Transactional(readOnly = true)
    public Order findOrderById(Integer id) {
        if (id == null) {
            throw new StewardBusinessException("订单id参数不能为空");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数Id是：[%s]", id));
        }
        return generalDAO.get(Order.class, id);
    }

//    /**
//     * 查询订单
//     */
//    @Transactional(readOnly = true)
//    public List<OrderItem> findOrderItem() {
//        Search search = new Search(OrderItem.class);
//        //noinspection unchecked
//        return generalDAO.search(search);
//    }

    /**
     * 根据订单id查询订单项
     */
    @Transactional(readOnly = true)
    public OrderItem findOrderItemById(Integer id) {
        if (log.isInfoEnabled()) {
            log.info(String.format("参数Id是：[%s]", id));
        }
        return generalDAO.get(OrderItem.class, id);
    }

    /**
     * 批量保存订单项
     */
    public void saveOrderItems(List<OrderItem> orderItems) {
        generalDAO.saveOrUpdate(orderItems);
    }


    /**
     * 保存订单
     */
    public void saveOrder(HashMap map) {
        if (log.isInfoEnabled()) {
            log.info("参数map.get('id')的值为[%s]", map.get("id"));
        }
        Order order = generalDAO.get(Order.class, Integer.parseInt(map.get("id").toString()));
        Invoice invoice = order.getInvoice();
        Receiver receiver = invoice.getReceiver();
        OrderUtil.getSaveCondition(map, receiver, order, invoice);    //获取更新的参数
        generalDAO.saveOrUpdate(invoice);
        generalDAO.saveOrUpdate(order);
    }

    /**
     * 保存订单
     */
    public void saveAddOrder(Order order) {
        generalDAO.saveOrUpdate(order);
    }


    /**
     * 批量改物流
     *
     * @param orderIds     订单ID数组
     * @param shippingComp 快递公司
     */
    public void updateStatusByOrder(Integer[] orderIds, String shippingComp) {
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数订单ID数组orderIds:[%s],物流公司shippingComp:[%s]", Arrays.toString(orderIds), shippingComp));
        }
        if (!StringUtils.isBlank(shippingComp) && !shippingComp.equals("null")) {
            for (int i : orderIds) {
                Order order = generalDAO.get(Order.class, i);
                if (order.getInvoice() != null) {
                    order.getInvoice().setShippingComp(shippingComp);
                    generalDAO.saveOrUpdate(order.getInvoice());
                }
            }
        }
    }

    /**
     * 手动添加赠品或追加商品
     */
    public void addGiftByHand(QueryProdVo[] queryProdVos1, Integer[] orderIds) {
        /**
         * 先通过orderNo查询出订单
         */
        for (Integer orderId : orderIds) {
            Order order = findOrderById(orderId);
            if (order == null) {
                if (log.isInfoEnabled()) {
                    log.info(String.format("根据订单号无法查询到订单orderId为：[%s]", orderId));
                }
                throw new IllegalArgumentException("根据订单号无法查询到订单orderId为{}" + orderId);
            }
            List<Integer> repoIds = checkRepoId(queryProdVos1);
            if (repoIds.size() > 1 || !repoIds.get(0).equals(order.getRepoId())) {
                throw new StewardBusinessException("加入的产品不在一个仓库，请检查后重新添加！");
            }

            /**
             * 添加订单项
             */
            for (QueryProdVo q : queryProdVos1) {
                Product product = productService.findProductBySKU(q.getSku());

                OrderItemType orderItemType = OrderItemType.valueOf(q.getOrderItemType());
                OrderItem orderItem = createOrderItem(q.getNum(), orderItemType, product, null);
//                orderItem.setExchangePostFee(Money.valueOf(q.getExchangePostFee()));
//                orderItem.setExchangePostPayer(PostPayer.valueOf(q.getExchangePostPayer()));
                orderItem.setOrderId(order.getId());
                orderItem.setPlatformType(order.getPlatformType());
                orderFeeService.calculateOrderItemFee(orderItem);

                saveOrderItem(orderItem);
                List<OrderItem> orderItems = findOrderItemByOrderId(order.getId());
                order.setOrderItemList(orderItems);
                orderFeeService.calculateOrderFee(order, orderItems);
            }
            saveAddOrder(order);
        }
    }

    /**
     * 手动添加订单    正常订单   补货订单
     *
     * @param addOrderVo   加订单Vo
     * @param queryProdVos vo条件
     * @param orderType    订单类型
     * @return 返回 String类型集合
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public List<String> addOrderByHand(AddOrderVo addOrderVo, QueryProdVo[] queryProdVos, String orderType, String platformType) throws IllegalAccessException, NoSuchMethodException {
        if (log.isInfoEnabled()) {
            log.info(String.format("接收到的addOrderVo为：[%s],接收到的queryProdVos为：[%s],接收到的orderType为：[%s]", addOrderVo, Arrays.toString(queryProdVos), orderType));
        }

        Order order = new Order();
        addOrderVoToOrder(addOrderVo, order);
        order.setPlatformType(PlatformType.valueOf(platformType));//平台类型
        order.setType(OrderType.valueOf(orderType));
        //设置订单的订单项
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        for (QueryProdVo queryProdVo : queryProdVos) {
            Product product = productService.findProductBySKU(queryProdVo.getSku());
            OrderItem orderItem = createOrderItem(queryProdVo.getNum(), OrderItemType.valueOf(queryProdVo.getOrderItemType()),
                    product, null);
            if (orderType.equals(OrderType.NORMAL.toString())) {
                orderItem.setPrice(Money.valueOf(queryProdVo.getMarketPrice()));
                orderItem.setDiscountFee(Money.valueOf(queryProdVo.getDiscountFee()));
                orderItem.setSharedPostFee(Money.valueOf(queryProdVo.getSharedPostFee()));
                orderItem.setSharedDiscountFee(Money.valueOf(queryProdVo.getSharedDiscountFee()));
                orderItem.setDiscountPrice(orderItem.calculateDiscountPrice());
            }
//            orderItem.setExchangePostPayer(PostPayer.valueOf(queryProdVo.getExchangePostPayer()));
//            orderItem.setExchangePostFee(Money.valueOf(queryProdVo.getExchangePostFee()));
            orderItem.setPlatformType(PlatformType.valueOf(platformType));
            orderItemList.add(orderItem);
            orderFeeService.calculateOrderItemFee(orderItem);
        }
        order.setOrderItemList(orderItemList);
        if (log.isInfoEnabled()) {
            log.info(String.format("order的详细信息是：[%s],order.getOrderItemList().size()的详细信息是：[%s]", order.toString(), order.getOrderItemList().size()));

        }
        orderFeeService.calculateOrderFee(order, orderItemList);
        //TODO 换到可以进行自动拆单的方法  并要设置订单所属的仓库
        List<Order> orders = orderGenerateService.createOrderWithSplit(true, order, null);
        List<String> stringList = new ArrayList<String>();
        for (Order order1 : orders) {
            stringList.add(order1.getOrderNo());
        }
        return stringList;
    }

    /**
     * 手动添加订单   换货订单
     *
     * @param addOrderVo   加订单Vo
     * @param queryProdVos 条件Vo
     * @return 返回String类型的List
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public List<String> addExchangeOrderByHand(AddOrderVo addOrderVo, QueryProdVo[] queryProdVos, Integer orderItemId, String platformType) throws IllegalAccessException, NoSuchMethodException {
        if (log.isInfoEnabled()) {
            log.info(String.format("接收到的addOrderVo为：[%s],接收到的queryProdVos为:[%s],接收到的orderItemId为：[%s]", addOrderVo, Arrays.toString(queryProdVos), orderItemId));
        }

        Order order = new Order();
        addOrderVoToOrder(addOrderVo, order);
        order.setPlatformType(PlatformType.valueOf(platformType));//平台类型
        order.setType(OrderType.EXCHANGE); //换货订单

        //设置订单的订单项
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        for (QueryProdVo queryProdVo : queryProdVos) {

            OrderItem orderItem = createOrderItem(queryProdVo.getNum(), OrderItemType.EXCHANGE_AFTERSALE,
                    productService.findProductBySKU(queryProdVo.getSku()), null);
            orderItem.setExchangePostPayer(PostPayer.valueOf(queryProdVo.getExchangePostPayer()));
            orderItem.setExchangePostFee(Money.valueOf(queryProdVo.getExchangePostFee()));
            orderItem.setPlatformType(PlatformType.valueOf(platformType));
            orderItem.setExchangeSourceId(orderItemId);
            orderItemList.add(orderItem);
            orderFeeService.calculateOrderItemFee(orderItem);
        }
        order.setOrderItemList(orderItemList);
        if (log.isInfoEnabled()) {
            log.info(String.format("order的详细信息是：[%s],order.getOrderItemList().size()的详细信息是：[%s]", order.toString(), order.getOrderItemList().size()));
        }
        orderFeeService.calculateOrderFee(order, orderItemList);
        List<Order> orders = orderGenerateService.createOrderWithSplit(true, order, null);
        List<String> stringList = new ArrayList<String>();
        for (Order order1 : orders) {
            stringList.add(order1.getOrderNo());
        }
        return stringList;
    }

    /**
     * 手动拆分订单
     *
     * @param orderId      订单ID
     * @param orderItemIds 订单项ID数组
     */
    public List<String> spiltOrderByHand(Integer orderId, Integer[] orderItemIds) {
        Order order = findOrderById(orderId);
        if (!order.getStatus().equals(OrderStatus.WAIT_PROCESS)) {
            throw new StewardBusinessException("手动拆分的订单状态应为待处理！");
        }
        if (!order.getOrderReturnStatus().equals(OrderReturnStatus.NORMAL)) {
            throw new StewardBusinessException("手动拆分的订单的退货状态应为正常！");
        }
        if (order.getRefunding()) {
            throw new StewardBusinessException("正在申请退款的订单不能进行拆分！");
        }
        OrderApprove orderApprove = orderApproveService.findByOrderStatusWithOrderId(OrderStatus.EXAMINED, order.getId());
        if (orderApprove != null) {
            throw new StewardBusinessException("该订单已验货，不能手动拆分了！");
        }
        for (OrderItem orderItem : order.getOrderItemList()) {
            List<PaymentAllocation> paymentAllocations = paymentService.findPAsByOrderItem(orderItem.getId());
            if (paymentAllocations.size() > 0) {
                throw new StewardBusinessException("智库城编号为" + orderItem.getId() + "的订单项已经经过预收款的分配，无法拆分！");
            }
            if (orderItem.getExchanged()) {
                throw new StewardBusinessException("智库城编号为" + orderItem.getId() + "的订单项已经被售前换货，无法拆分！");
            }
            if (orderItem.getRefunding()) {
                throw new StewardBusinessException("智库城编号为" + orderItem.getId() + "的订单项正在申请退款，无法拆分！");
            }
            if (!orderItem.getReturnStatus().equals(OrderItemReturnStatus.NORMAL)) {
                throw new StewardBusinessException("智库城编号为" + orderItem.getId() + "的订单项已经进行线上退货，无法拆分！");
            }
            if (!orderItem.getOfflineReturnStatus().equals(OrderItemReturnStatus.NORMAL)) {
                throw new StewardBusinessException("智库城编号为" + orderItem.getId() + "的订单项已经进行线下退货，无法拆分！");
            }
        }

        List<OrderItem> orderItemList = order.getOrderItemList();
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (Integer orderItemId : orderItemIds) {
            OrderItem orderItem = findOrderItemById(orderItemId);
            orderItems.add(orderItem);
            orderItemList.remove(orderItem);
        }

        List<List<OrderItem>> lists = new ArrayList<List<OrderItem>>();
        lists.add(orderItems);
        lists.add(orderItemList);

        Map<Repository, List<List<OrderItem>>> repoOrderItemListMap = new HashMap<Repository, List<List<OrderItem>>>();
        repoOrderItemListMap.put(order.getRepo(), lists);
        List<Order> orderList = orderGenerateService.splitOrder(true, order, repoOrderItemListMap);
        List<String> stringList = new ArrayList<String>();
        for (Order order1 : orderList) {
            stringList.add(order1.getOrderNo());
        }
        return stringList;

    }

    /**
     * 检验加入的产品都在一个仓库
     *
     * @param queryProdVos
     * @return
     */
    public List<Integer> checkRepoId(QueryProdVo[] queryProdVos) {

        List<Integer> repoIds = new ArrayList<Integer>();
        for (QueryProdVo queryProdVo : queryProdVos) {
            Integer repoId = (storageService.findByProductId((productService.findProductBySKU(queryProdVo.getSku())).getId())).getRepositoryId();
            if (repoIds == null || !repoIds.contains(repoId)) {
                repoIds.add(repoId);
            }
        }
        return repoIds;
    }

    /**
     * 复制订单收货信息
     *
     * @param addOrderVo 加订单Vo实体
     * @param order      订单实体
     */
    public void addOrderVoToOrder(AddOrderVo addOrderVo, Order order) {

        Receiver receiver = new Receiver();
        receiver.setReceiverName(addOrderVo.getReceiverName());
        receiver.setReceiverPhone(addOrderVo.getReceiverPhone());
        receiver.setReceiverMobile(addOrderVo.getReceiverMobile());
        receiver.setReceiverZip(addOrderVo.getReceiverZip());
        receiver.setReceiverState(addOrderVo.getReceiverState());
        receiver.setReceiverCity(addOrderVo.getReceiverCity());
        receiver.setReceiverAddress(addOrderVo.getReceiverAddress());
        receiver.setReceiverDistrict(addOrderVo.getReceiverDistrict());
        receiver.copyAreaToAddress();

        Invoice invoice = new Invoice();
        invoice.setReceiver(receiver);
        invoice.setShippingComp(addOrderVo.getShippingComp());

        order.setGenerateType(OrderGenerateType.MANUAL_CREATE);//手动创建
        order.setInvoice(invoice);
        order.setShopId(addOrderVo.getShopId());
        order.setStatus(OrderStatus.WAIT_APPROVE);//待审核
        order.setOrderReturnStatus(OrderReturnStatus.NORMAL); //订单退货状态
        order.setBuyerId(addOrderVo.getBuyerId());
        order.setBuyerMessage(addOrderVo.getBuyerMessage());
        order.setRemark(addOrderVo.getRemark());
        order.setReceiptTitle(addOrderVo.getReceiptTitle());
        order.setReceiptContent(addOrderVo.getReceiptContent());
        order.setBuyTime(new Date());
        if (addOrderVo.getBuyTime() != null) {
            order.setBuyTime(addOrderVo.getBuyTime());
        }
        order.setPayTime(new Date());
        if (addOrderVo.getPayTime() != null) {
            order.setPayTime(addOrderVo.getPayTime());
        }
        order.setPlatformOrderNo(addOrderVo.getPlatformOrderNo());

    }

    /**
     * 创建订单项
     *
     * @param buyCount      购买件数
     * @param orderItemType 订单项类型
     * @param product       产品实体
     * @param mealsetItem   套餐
     * @return 返回订单项
     */
    public OrderItem createOrderItem(int buyCount, OrderItemType orderItemType, Product product, MealsetItem mealsetItem) {

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductSku(product.getSku());
        orderItem.setProductCode(product.getProductNo());
        orderItem.setSpecInfo(OrderUtil.getSpecInfo(product));//设置规格
        orderItem.setStatus(OrderItemStatus.NOT_SIGNED);//未签收
        orderItem.setType(orderItemType);
        orderItem.setReturnStatus(OrderItemReturnStatus.NORMAL);
        orderItem.setOfflineReturnStatus(OrderItemReturnStatus.NORMAL);
        orderItem.setBuyCount(buyCount);

        return orderItem;
    }

    /**
     * 添加刷单按钮
     */
    @Transactional
    public void cheatOrder(Integer[] orderIds){
        List<Integer> jdIds=new ArrayList<Integer>();
        List<Integer> tbIds=new ArrayList<Integer>();
        for(Integer orderId:orderIds){
            Order order=findOrderById(orderId);
            if(!order.getStatus().equals(OrderStatus.WAIT_PROCESS) && !order.getType().equals(OrderType.NORMAL)){
                throw new StewardBusinessException(String.format("把订单设置成刷单状态不成功，订单号：%s",orderId));
            }
            order.setType(OrderType.CHEAT);
            if(StringUtils.isBlank(order.getOfflineRemark())){
                order.setOfflineRemark("刷单");
            }
            if(order.getPlatformType().equals(PlatformType.JING_DONG)){
                jdIds.add(orderId);
            }else{
                tbIds.add(orderId);
            }
        }
        if(tbIds.size()>0){
            orderConfirm(tbIds.toArray(new Integer[tbIds.size()]));
        }
        if(jdIds.size()>0){
            orderCancellation(jdIds.toArray(new Integer[jdIds.size()]));
        }

    }

    /**
     * 导入进销存 待处理->已确认
     *
     * @param orderIds 订单ID数组
     */
    @Transactional
    public void orderConfirm(Integer[] orderIds) {
        if (log.isInfoEnabled()) {
            log.info(String.format("OrderService中的orderConfirm方法，参数orderIds:%s", Arrays.toString(orderIds)));
        }
        for (Integer orderId : orderIds) {
            Order order = generalDAO.get(Order.class, orderId);
            if (!order.getValid()) {
                throw new StewardBusinessException(String.format("无效订单不能导入进销存，订单号：%s", order.getOrderNo()));
            }
            if (order.getRefunding()) {
                throw new StewardBusinessException(String.format("正在退货的订单无法导入进销存，订单号：%s", order.getOrderNo()));
            }
            if (order.getOrderReturnStatus().equals(OrderReturnStatus.RETURNED)) {
                throw new StewardBusinessException(String.format("已退货的订单无法导入进销存，订单号：%s", order.getOrderNo()));
            }
            List<Payment> payments = paymentService.findPostFeeAndNotAllocatedPaymentByPlatformOrderNo(order.getPlatformOrderNo());
            if (payments != null && !payments.isEmpty()) {
                throw new StewardBusinessException(String.format("订单存在未分配的订单邮费预收款，订单号：%s", order.getOrderNo()));
            }
            OrderStatus from = OrderStatus.WAIT_PROCESS;
            OrderStatus to = OrderStatus.CONFIRMED;
            orderFlowService.changeStatus(order, from, to, true);
        }
    }

    /**
     * 订单作废（除了发货订单，其他的订单都可以作废）
     *
     * @param orderIds 订单ID数组
     * @return 作废的记录条数
     */
    @Transactional
    public void orderCancellation(Integer[] orderIds) {
        for (Integer orderId : orderIds) {
            Order order = generalDAO.get(Order.class, orderId);
            if (order.getStatus().equals(OrderStatus.INVALID)) {
                throw new StewardBusinessException(String.format("作废失败,订单编号[%s]已经是作废订单",order.getOrderNo()));
            }
            if (order.getStatus().equals(OrderStatus.INVOICED)) {
                throw new StewardBusinessException(String.format("作废失败,订单编号[%s]已经是发货订单",order.getOrderNo()));
            }
            if (order.getStatus().equals(OrderStatus.CONFIRMED)&&StringUtils.isNotBlank(order.getInvoice().getShippingNo())) {
                throw new StewardBusinessException(String.format("作废失败,订单编号[%s]正在等待打印并且已经有快递单号",order.getOrderNo()));
            }
        }
        for (Integer orderId : orderIds) {
            Order order = generalDAO.get(Order.class, orderId);
            OrderStatus from = order.getStatus();
            OrderStatus from2 = OrderStatus.WAIT_APPROVE;
            OrderStatus to = OrderStatus.WAIT_APPROVE;
            OrderStatus to2 = OrderStatus.INVALID;
            //先跳到订单审核
            orderFlowService.changeStatus(order, from, to, false);
            //从待审核跳到订单作废
            orderFlowService.changeStatus(order, from2, to2, true);
        }

    }

    /**
     * 订单恢复 作废->待处理
     *
     * @param orderIds 订单ID数组
     */
    @Transactional
    public void orderRecover(Integer[] orderIds) {
        if (null == orderIds) {
            throw new StewardBusinessException("orderIds为空");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("OrderService中的orderRecover方法，参数orderIds:%s", Arrays.toString(orderIds)));
        }
        for (Integer orderId : orderIds) {
            Order order = generalDAO.get(Order.class, orderId);
            if (!order.getStatus().equals(OrderStatus.INVALID)) {
                throw new StewardBusinessException("不是作废订单不可使用订单恢复");
            }
            //订单的的初始状态
            OrderStatus from = OrderStatus.INVALID;
            //订单的更改状态
            OrderStatus to = OrderStatus.WAIT_APPROVE;
           //订单改到待处理状态
            OrderStatus to2=OrderStatus.WAIT_PROCESS;
            //使用changeStatus改变订单状态，该方法包括了订单状态操作日志的添加
            orderFlowService.changeStatus(order, from, to, true);
            orderFlowService.changeStatus(order,to,to2,true);

        }
    }

    /**
     * 订单审核 待审核->待处理
     *
     * @param orderIds 订单ID数组
     */
    @Transactional
    public void orderCheck(Integer[] orderIds) {
        if (orderIds == null) {
            throw new StewardBusinessException("orderIds为空");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数orderIds:%s", Arrays.toString(orderIds)));
        }
        for (Integer orderId : orderIds) {
            Order order = generalDAO.get(Order.class, orderId);
            if (order.getStatus() != OrderStatus.WAIT_APPROVE) {
                throw new StewardBusinessException("该订单状态不是待审核，不可执行此操作");
            }
            //订单的的初始状态
            OrderStatus from = OrderStatus.WAIT_APPROVE;
            //订单的更改状态
            OrderStatus to = OrderStatus.WAIT_PROCESS;
            //使用changeStatus改变订单状态，该方法包括了订单状态操作日志的添加
            orderFlowService.changeStatus(order, from, to, true);
        }
    }

//    @Transactional(readOnly = true)
//    public List<Order> findOrderByPayTime(String startPayTime, String endPayTime, String platformType, Integer shopId) {
//
//        Search search = new Search(Order.class);
//        if (!StringUtils.isBlank(startPayTime)) {
//            search.addFilterGreaterOrEqual("payTime", EJSDateUtils.parseDate(startPayTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
//        }
//        if (!StringUtils.isBlank(endPayTime)) {
//            search.addFilterLessOrEqual("payTime", EJSDateUtils.parseDate(endPayTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
//        }
//        if (!StringUtils.isBlank(platformType)) {
//            search.addFilterEqual("platformType", PlatformType.valueOf(platformType));
//        }
//        if (!NumberUtil.isNullOrZero(shopId)) {
//            search.addFilterLessOrEqual("shopId", shopId);
//        }
//        search.addFilterEqual("valid", true);
//        search.addFilterEqual("status", OrderStatus.SIGNED);
//        search.addSortDesc("payTime");
//
//        return generalDAO.search(search);
//    }


    /**
     * 更新物流单号
     *
     * @param shippingNo
     * @param orderId
     */
    public void updateShippingNo(String shippingNo, OrderStatus orderStatus, Integer orderId) {
        if (orderId == null) {
            throw new StewardBusinessException("orderId为空");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数shippingNo为[%s],参数orderStatus为[%s],参数orderId为[%s]", shippingNo, orderStatus.toString(), orderId.toString()));
        }
        Order order = generalDAO.get(Order.class, orderId);
        Invoice invoice = order.getInvoice();
        if (orderStatus != null && !order.getStatus().equals(orderStatus)) {
            throw new StewardBusinessException("订单状态不正确，请刷新后重试");
        }
        if (StringUtils.isNotBlank(shippingNo)) {
            List<Invoice> invoices = invoiceService.findExistShippingNoInvoice(Arrays.asList(shippingNo));
            if (!invoices.isEmpty()) {
                throw new StewardBusinessException(String.format("物流编号[%s]已经存在", shippingNo));
            }
        }
        invoice.setShippingNo(shippingNo);
        generalDAO.saveOrUpdate(invoice);
    }


    /**
     * 根据平台类型和订单号查询订单
     *
     * @param platformType
     * @param platformOrderNo
     * @return
     */
    @Transactional(readOnly = true)
    public List<Order> findByPlatformOrderNo(PlatformType platformType, String platformOrderNo) {
        Preconditions.checkNotNull(platformType);
        Preconditions.checkNotNull(platformOrderNo);
        Search search = new Search(Order.class);
        if (platformType != null) {
            search.addFilterEqual("platformType", platformType);
        }
        search.addFilterEqual("platformOrderNo", platformOrderNo).addFilterEqual("valid", true);
        //noinspection unchecked
        return generalDAO.search(search);
    }

    /**
     * 根据平台订单号查询订单
     *
     * @param platformOrderNo
     * @return
     */
    @Transactional(readOnly = true)
    public Order getOrderByPlatformOrderNo(String platformOrderNo) {
        Preconditions.checkNotNull(platformOrderNo);
        Search search = new Search(Order.class);
        if (StringUtils.isNotBlank(platformOrderNo)) {
            search.addFilterEqual("platformOrderNo", platformOrderNo);
        }
        search.addFilterEqual("valid", true);
        List<Order> orderList = generalDAO.search(search);
        return CollectionUtils.isNotEmpty(orderList) ? orderList.get(0) : null;
    }


    /**
     * 换货前对订单的校验
     *
     * @param id
     */
    public void checkOrder(Integer id) {
        Order order = findOrderById(id);
        if (!order.getPlatformType().equals(PlatformType.TAO_BAO) || !order.getPlatformType().equals(PlatformType.TAO_BAO_2)) {
            throw new StewardBusinessException("只支持天猫订单换货");
        }
        if (!order.getStatus().equals(OrderStatus.WAIT_PROCESS)) {
            throw new StewardBusinessException("只能对待处理的订单进行该操作");
        }
    }

    /**
     * 换货前对订单项的校验
     */
    public void checkOrderItem(OrderItem oldOrderItem, Integer count) {
        if (!oldOrderItem.getType().equals(OrderItemType.PRODUCT) && !oldOrderItem.getType().equals(OrderItemType.MEALSET)) {
            throw new StewardBusinessException("非商品或套餐商品类型不能进行售前换货，请检查后重新操作");
        }
        if (!oldOrderItem.getBuyCount().equals(count)) {
            throw new StewardBusinessException("售前换货的订单项需要订购数量保持一致，请检查后重新操作");
        }
        if (oldOrderItem.getRefunding()) {
            throw new StewardBusinessException("该订单项正在申请退款不能进行售前换货，请检查后重新操作");
        }
        if (oldOrderItem.getOfflineReturnStatus().equals(OrderItemReturnStatus.RETURNED)) {
            throw new StewardBusinessException("该订单项已退货，请检查后重新操作");
        }
        if (oldOrderItem.getRefunding()) {
            throw new StewardBusinessException("该订单项正在申请退款不能进行售前换货，请检查后重新操作");
        }
        if (oldOrderItem.getReturnStatus().equals(OrderItemReturnStatus.RETURNED)) {
            throw new StewardBusinessException("该订单项已退货，请检查后重新操作");
        }
    }

    /**
     * 售前换货
     *
     * @param oldOrderItemId
     * @param productId
     * @param count
     */
    public void exchangeGoods(Integer oldOrderItemId, Integer productId, Integer count) {
        OrderItem oldOrderItem = generalDAO.get(OrderItem.class, oldOrderItemId);
        Product product = generalDAO.get(Product.class, productId);
        //换货前对订单项的校验
        checkOrderItem(oldOrderItem, count);
        Storage storage = storageService.findByProductId(oldOrderItem.getProductId());
        Storage storage1 = storageService.findByProductId(product.getId());
        if (storage.getRepository() != storage1.getRepository()) {
            throw new StewardBusinessException("选择商品不在同一仓库，请检查后重新操作");
        }

        ExchangeOrderItem exchangeOrderItem = oldOrderItem.getExchangeOrderItem();
        if(exchangeOrderItem == null) {
            exchangeOrderItem = new ExchangeOrderItem();
            exchangeOrderItem.setProductId(oldOrderItem.getProductId());
            exchangeOrderItem.setProductCode(oldOrderItem.getProductCode());
            exchangeOrderItem.setProductSku(oldOrderItem.getProductSku());
            exchangeOrderItem.setProductName(oldOrderItem.getProductName());
            exchangeOrderItem.setBuyCount(oldOrderItem.getBuyCount());
            exchangeOrderItem.setSpecInfo(oldOrderItem.getSpecInfo());
            generalDAO.saveOrUpdate(exchangeOrderItem);
            oldOrderItem.setExchangeOrderItemId(exchangeOrderItem.getId());
        }

        oldOrderItem.setProductId(product.getId());
        oldOrderItem.setProductCode(product.getProductNo());
        oldOrderItem.setProductSku(product.getSku());
        oldOrderItem.setProductName(product.getName());
        oldOrderItem.setBuyCount(count);
        oldOrderItem.setSpecInfo(OrderUtil.getSpecInfo(product));
        oldOrderItem.setExchanged(true);
        generalDAO.saveOrUpdate(oldOrderItem);
    }


    /**
     * 根据外部平台子订单号查询订单项
     *
     * @param platformType
     * @param platformSubOrderNo
     * @return
     */
    public List<OrderItem> findOrderItemByPlatformSubOrderNo(PlatformType platformType, String platformSubOrderNo) {
        Search search = new Search(OrderItem.class).addFilterEqual("valid", true);
        search.addFilterEqual("platformType", platformType);
        search.addFilterEqual("platformSubOrderNo", platformSubOrderNo);
        //noinspection unchecked
        return generalDAO.search(search);

    }


    /**
     * 修改订单项和订单为正在退款
     *
     * @param orderItemId
     */
    public void markOrderItemRefunding(Integer orderItemId) {
        log.debug("将订单项[id={}]标记为正在退款", orderItemId);
        OrderItem orderItem = generalDAO.get(OrderItem.class, orderItemId);
        orderItem.setRefunding(true);
        generalDAO.saveOrUpdate(orderItem);
    }

    /**
     * 修改订单项退款状态为正常
     *
     * @param orderItemId
     */
    public void markOrderItemNotRefunding(Integer orderItemId) {
        log.debug("将订单项[id={}]标记为退款结束", orderItemId);
        OrderItem orderItem = generalDAO.get(OrderItem.class, orderItemId);
        orderItem.setRefunding(false);
        generalDAO.saveOrUpdate(orderItem);
    }

    /**
     * 删除订单项
     *
     * @param id 订单项ID
     */
    public OrderItem deleteOrderItemById(Integer id) {
        if (id == null) {
            throw new StewardBusinessException("没有勾选要删除的订单项");
        }
        if (log.isInfoEnabled()) {
            log.info("方法参数id:[{}]", id);
        }
        OrderItem orderItem = generalDAO.get(OrderItem.class, id);
        Order order = orderItem.getOrder();
        List<Payment> payments = paymentService.getByOrderItemId(id);
        if (orderItem.getType().equals(OrderItemType.PRODUCT) || orderItem.getType().equals(OrderItemType.MEALSET)) {
            throw new StewardBusinessException("订单项类型为商品或者套餐不能执行删除操作");
        }
        if (!order.getStatus().equals(OrderStatus.WAIT_PROCESS)) {
            throw new StewardBusinessException("只有待处理的订单才能进行删除订单项的操作");
        }
        if (!orderItem.getReturnStatus().equals(OrderItemReturnStatus.NORMAL) || !orderItem.getOfflineReturnStatus().equals(OrderItemReturnStatus.NORMAL)) {
            throw new StewardBusinessException("订单项不能删除,已发生退货");
        }
        //有正在申请退款或退款失败记录的订单项
        if (refundService.findByOrderItemId(id).size() > 0) {
            throw new StewardBusinessException("订单项不能删除,请先删除该订单项的退款记录");
        }
        if (orderItem == null) {
            throw new StewardBusinessException("没有找到ID对应的订单项，请检查后重新操作");
        }
        if (orderItem.getExchanged()) {
            throw new StewardBusinessException("被换货订单项无法执行删除操作，请检查后重新操作");
        }
        if (payments.size() > 0) {
            StringBuilder paymentId = new StringBuilder();
            int i = 0;
            for (Payment payment : payments) {
                i++;
                if (i == payments.size()) {
                    paymentId.append(payment.getPlatformOrderNo());
                } else {
                    paymentId.append(payment.getPlatformOrderNo() + ",");
                }
            }
            throw new StewardBusinessException("订单项不能删除,请先删除外部订单号为[" + paymentId + "]的分配记录");
        }
        //删除售后换货
        if (orderItem.getType().equals(OrderItemType.EXCHANGE_AFTERSALE)) {
            throw new StewardBusinessException("售后换货订单项不能删除,请直接作废订单");
        }
        generalDAO.remove(orderItem);
        orderFeeService.checkOrderReturnStatus(order);

        orderFeeService.calculateOrderFee(order, order.getOrderItemList());

        return orderItem;
    }

    @Transactional(readOnly = true)
    public List<OrderVo> findExchangeOrder(Map<String, Object[]> map) throws ParseException {
        Map<String, String> map1 = new HashMap<String, String>();
        //将接受到的map参数解析并赋值给map1
        OrderUtil.getConditionMap(map, map1);
        //显示层的Vo的List
        List<OrderVo> orderVos = new ArrayList<OrderVo>();
        //HQL条件的值
        List<Object> objects = new ArrayList<Object>();
        //拼接HQL的变量
        StringBuilder stringBuilder = new StringBuilder();
        //拼接HQL的方法
        OrderUtil.exchangeOrderCondition(map1, stringBuilder, objects);
        //执行HQL
        List<Order> orders = generalDAO.query(stringBuilder.toString(), null, objects.toArray());

        //拼接Vo的循环
        for (Order order : orders) {
            OrderUtil.getOrderVo(order, orderVos, order.getOrderItemList(),false);
        }

        return orderVos;
    }

    public Workbook reportExchangeOrderItem(Map<String, Object[]> map) throws ParseException, IOException {
        List<OrderVo> orderVoList = findExchangeOrder(map);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        createExchangeOrderTitle(sheet);

        int rowIndex = 2;//从第三行开始，一二行放title
        for (OrderVo or : orderVoList) {
            Integer[] ids = new Integer[1];
            ids[0] = or.getId();
            Map<String, Object[]> map1 = new HashMap<String, Object[]>();
            map1.put("orderIds", ids);
            List<OrderItemVo> orderItemVos = findOrderItemVoByMap(map1);
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;

            renderExchangeOrder2Excel(row, cellIndex, or, orderItemVos);
        }
        return workbook;
    }

    /**
     * 订单管理的订单导出
     *
     * @param map 接收查询的参数
     * @return 返回一个Workbook对象
     * @throws ParseException 异常
     * @throws IOException    异常
     */
    @Transactional(readOnly = true)
    public Workbook reportOrderAndOrderItems(Map<String, Object[]> map) throws ParseException, IOException {
        Page page = findOrderDetails(map, null);
        @SuppressWarnings("unchecked")
        List<OrderVo> orderVoList = page.getResult();
//        if(orderVoList.size()>5000){
//            throw new StewardBusinessException("导出不能超过5000条");
//        }
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        //创建一个order的excel头标题
        int orderCellIndex = OrderExcelUtil.createExcelOrderTitle(sheet);
        int rowIndex = 2;//从第三行开始，一二行放title
        int productNum = 0;
        for (OrderVo or : orderVoList) {
            Integer[] ids = new Integer[1];
            ids[0] = or.getId();
            Map<String, Object[]> map1 = new HashMap<String, Object[]>();
            map1.put("orderIds", ids);
            List<OrderItemVo> orderItemVos = findOrderItemVoByMap(map1);
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            //插入订单数据
            OrderExcelUtil.renderOrderVoExcel(row, cellIndex, or);
            //Item
            int orderItemVosSize = orderItemVos.size();
            for (int i = 0; i < orderItemVosSize; i++) {
                OrderItemVo item = orderItemVos.get(i);
                productNum = productNum + Integer.parseInt(item.getBuyCount());
                Row itemRow;
                if (i > 0) {
                    itemRow = sheet.createRow(row.getRowNum() + i);  //如果商品条目>1则新建一行
                    rowIndex++;
                } else {
                    itemRow = row;

                }
                int itemCellIndex = orderCellIndex + 1;
                //插入订单项数据
                OrderExcelUtil.renderOrderItemVoExcel(itemRow, itemCellIndex, item);
            }
            for (int i = 0; orderItemVosSize > 0 && i <= orderCellIndex; i++) {
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + orderItemVos.size() - 1, i, i));  //合并订单的单元格
            }
        }
//        CellStyle myStyle = workbook.createCellStyle();
//        Row row2 = sheet.createRow(++rowIndex);
//        PoiUtil.createCell(row2, 0, "订单导出汇总");
//        Row row = sheet.createRow(++rowIndex);
//        PoiUtil.createCell(row, 0, "导出订单总条数");
//        PoiUtil.createCell(row, 1, orderVoList.size() + "条",myStyle);
//        Row row1 = sheet.createRow(++rowIndex);
//        PoiUtil.createCell(row1, 0, "导出订货数量");
//        PoiUtil.createCell(row1, 1, productNum + "件",myStyle);
        OrderExcelUtil.collectNum(rowIndex,workbook,sheet,orderVoList.size(),productNum);
        return workbook;
    }


    private void createExchangeOrderTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        PoiUtil.createCell(titleRow, 0, "导出的换货补货订单信息");
        int cellIndex = 0;
        Row row = sheet.createRow(1);
        PoiUtil.createCell(row, cellIndex++, "订单创建日期");
        PoiUtil.createCell(row, cellIndex++, "店铺");
        PoiUtil.createCell(row, cellIndex++, "售后类型（换货/补货）");
        PoiUtil.createCell(row, cellIndex++, "外部平台订单编号");
        PoiUtil.createCell(row, cellIndex++, "智库城订单编号");
        PoiUtil.createCell(row, cellIndex++, "买家ID");
        PoiUtil.createCell(row, cellIndex++, "收货人姓名");
        PoiUtil.createCell(row, cellIndex++, "联系电话");
        PoiUtil.createCell(row, cellIndex++, "商品编码");
        PoiUtil.createCell(row, cellIndex++, "商品名称");
        PoiUtil.createCell(row, cellIndex++, "数量");
        PoiUtil.createCell(row, cellIndex++, "平台结算金额");
        PoiUtil.createCell(row, cellIndex++, "货款");
        PoiUtil.createCell(row, cellIndex++, "备注");
        PoiUtil.createCell(row, cellIndex++, "运费");
        PoiUtil.createCell(row, cellIndex++, "邮费承担方");
        PoiUtil.createCell(row, cellIndex++, "换货（补货）订单编号");
        PoiUtil.createCell(row, cellIndex++, "买家ID");
        PoiUtil.createCell(row, cellIndex++, "收货人姓名");
        PoiUtil.createCell(row, cellIndex++, "联系电话");
        PoiUtil.createCell(row, cellIndex++, "商品编码");
        PoiUtil.createCell(row, cellIndex++, "商品名称");
        PoiUtil.createCell(row, cellIndex++, "数量");
        PoiUtil.createCell(row, cellIndex++, "备注");
        PoiUtil.createCell(row, cellIndex++, "线下备注");
        PoiUtil.createCell(row, cellIndex++, "寄出快递");
        PoiUtil.createCell(row, cellIndex++, "寄出快递单号");
        PoiUtil.createCell(row, cellIndex++, "订单状态");

        int orderCellIndex = cellIndex - 1;
        sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, orderCellIndex));  //合并订单标题的单元格

    }

    private void renderExchangeOrder2Excel(Row itemRow, int startCellIndex, OrderVo order, List<OrderItemVo> orderItems) {

        PoiUtil.createCell(itemRow, startCellIndex++, order.getCreateTime());
        PoiUtil.createCell(itemRow, startCellIndex++, order.getShopName());
        PoiUtil.createCell(itemRow, startCellIndex++, order.getOrderType().getValue());
        if (order.getOrderType().equals(OrderType.EXCHANGE)) {
            OrderItem orderItem = findOrderItemById(orderItems.get(0).getExchangeSourceId());
            Order order1 = findOrderById(orderItem.getOrderId());
            if (order1.getPlatformOrderNo() != null) {
                PoiUtil.createCell(itemRow, startCellIndex++, order1.getPlatformOrderNo());
            } else {
                startCellIndex++;
            }
            PoiUtil.createCell(itemRow, startCellIndex++, order1.getOrderNo());
            PoiUtil.createCell(itemRow, startCellIndex++, order1.getBuyerId());
            PoiUtil.createCell(itemRow, startCellIndex++, order1.getInvoice().getReceiver().getReceiverName());
            PoiUtil.createCell(itemRow, startCellIndex++, order1.getInvoice().getReceiver().getReceiverMobile());
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getProductCode());
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getProductName());
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getBuyCount());
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getActualFee().toString());
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getGoodsFee().toString());
            PoiUtil.createCell(itemRow, startCellIndex++, order1.getRemark());

        }
        if (order.getOrderType().equals(OrderType.REPLENISHMENT)) {
            startCellIndex = startCellIndex + 11;
        }
        PoiUtil.createCell(itemRow, startCellIndex++, order.getInvoicePostFee().toString());
        if (orderItems.get(0).getExchangePostPayer() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItems.get(0).getExchangePostPayer().getValue()); //邮费承担方
        } else {
            startCellIndex++;
        }
        PoiUtil.createCell(itemRow, startCellIndex++, order.getOrderNo());  //订单项编号
        PoiUtil.createCell(itemRow, startCellIndex++, order.getBuyerId());
        PoiUtil.createCell(itemRow, startCellIndex++, order.getReceiverName());
        PoiUtil.createCell(itemRow, startCellIndex++, order.getReceiverMobile());
        String productCodes = null;
        String productNames = null;
        String buyCounts = null;
        for (int i = 0; i < orderItems.size(); i++) {
            if (i == 0) {
                productCodes = orderItems.get(i).getProductCode();
                productNames = orderItems.get(i).getProductName();
                buyCounts = orderItems.get(i).getBuyCount() + "";
            } else {
                productCodes = productCodes + "," + orderItems.get(i).getProductCode();
                productNames = productNames + "," + orderItems.get(i).getProductName();
                buyCounts = buyCounts + "," + (orderItems.get(i).getBuyCount() + "");
            }
        }
        PoiUtil.createCell(itemRow, startCellIndex++, productCodes);
        PoiUtil.createCell(itemRow, startCellIndex++, productNames);
        PoiUtil.createCell(itemRow, startCellIndex++, buyCounts);
        PoiUtil.createCell(itemRow, startCellIndex++, order.getRemark());
        PoiUtil.createCell(itemRow, startCellIndex++, order.getOfflineRemark());
        PoiUtil.createCell(itemRow, startCellIndex++, DeliveryType.valueOf(order.getShippingComp()).getValue());
        PoiUtil.createCell(itemRow, startCellIndex++, order.getShippingNo());
        PoiUtil.createCell(itemRow, startCellIndex++, order.getOrderStatus().getValue());
    }

    /**
     * 订单管理的导入订单
     *
     * @param rows Row集合
     * @throws ParseException 异常
     */
    @Transactional
    public Map<String, Integer> OrderLeadIn(List<Row> rows) throws ParseException {
        //记录导入的记录数和最后生成订单的订单数
        Map<String, Integer> num = new HashMap<String, Integer>();
        //记录生成的订单数
        Integer orderNum = 0;
        //记录Excel的行
        int rowNum = 0;
        //记录数据条数
        int recordNum = rows.size();
        List<Order> orders = new ArrayList<Order>();
        for (int i = 0; i < rows.size(); i++) {
            rowNum = i + 2;
            Row row = rows.get(i);
            //判断是否Excel中间有为空的订单数据，有直接跳过
//            PoiUtil.getStringCellValue(1,);
            if (StringUtils.isBlank( StringUtils.trimToNull(PoiUtil.getStringCellValue(row,1))) && StringUtils.isBlank( StringUtils.trimToNull(PoiUtil.getStringCellValue(row,3)))) {
                recordNum = recordNum - 1;
                continue;
            }
            //判断数据是否有问题和赋值给orders集合
            OrderExcelUtil.judgeLeadInOrder(shopService, productService, row, rowNum, orders);
        }
        int size = orders.size();
        //合并订单，删除多余重复订单
        if (size > 1) {
            for (int i = 1; i < size; i++) {
                Order order = orders.get(i - 1);
                Order order1 = orders.get(i);
                if (!StringUtils.isBlank(order1.getPlatformOrderNo()) && order1.getPlatformOrderNo().equals(order.getPlatformOrderNo())) {
                    List<OrderItem> orderItems = order.getOrderItemList();
                    for (OrderItem orderItem : orderItems) {
                        order1.getOrderItemList().add(orderItem);
                    }
                    orders.remove(i - 1);
                    size = size - 1;
                    i = i - 1;
                }
            }
        }
        for (Order order : orders) {
            try {
                generalDAO.saveOrUpdate(order.getInvoice());
            } catch (Exception e) {
                log.error("添加方法saveOrUpdate的报错异常为[%s]", e.getMessage());
                throw new StewardBusinessException("添加物流方法错误");
            }
            order.setInvoiceId(order.getInvoice().getId());
            order.setStatus(OrderStatus.WAIT_PROCESS);
            order.setType(OrderType.NORMAL);
            List<OrderItem> orderItems = new ArrayList<OrderItem>();
            //给orderItem赋初始值
            for (OrderItem orderItem : order.getOrderItemList()) {
                orderItem.setPlatformType(order.getPlatformType());
                orderItem.setPlatformOrderNo(order.getPlatformOrderNo());
                orderItem.setStatus(OrderItemStatus.NOT_SIGNED);
                orderItem.setType(OrderItemType.PRODUCT);
                Product product = productService.findProductBySKU(orderItem.getProductSku());
                orderItem.setProductId(product.getId());
                orderItem.setSpecInfo(OrderUtil.getSpecInfo(product));
                orderItem.setProductCode(product.getProductNo());
                orderItem.setProductName(product.getName());
                //计算订单项价格
                orderFeeService.calculateOrderItemFee(orderItem);
                orderItems.add(orderItem);
            }
            orderFeeService.calculateOrderFee(order, orderItems);
            order.setOrderItemList(orderItems);
            //拆分订单
            List<Order> orderList = orderGenerateService.createOrderWithSplit(true, order, null);
            orderNum = orderNum + orderList.size();
        }
        num.put("recordNum", recordNum);
        num.put("orderNum", orderNum);
        return num;
    }

    /**
     *
     * @param
     */
    public List<OrderItem> findOrderItemByOriginalOrderItemId(Integer originalOrderItemId) {
        Search search = new Search(OrderItem.class).addFilterEqual("valid", true);
        search.addFilterEqual("originalOrderItemId", originalOrderItemId);
        return generalDAO.search(search);

    }


    /**
     * 解决2014-07-29 10:00:00更新导致天猫订单分摊优惠计算错误的问题,重新计算
     * @throws Exception
     */
    @Transactional
    @Deprecated
    public void fixOrderSharedDiscountFee() throws Exception {

        Search search = new Search(OriginalOrder.class);
        search.addFilterEqual("platformType", PlatformType.TAO_BAO).addFilterGreaterOrEqual("createTime", DateUtils.parseDate("2014-07-29 10:00:00", "yyyy-MM-dd HH:mm:ss"));
        List<OriginalOrder> allOriginalOrderList = generalDAO.search(search);
        for(OriginalOrder originalOrder : allOriginalOrderList) {
            //重新计算订单项的分摊优惠金额
            for(OriginalOrderItem originalOrderItem : originalOrder.getOriginalOrderItemList()) {
                originalOrderItem.getAllPartMjzDiscount();
                List<OrderItem> orderItemList = findOrderItemByOriginalOrderItemId(originalOrderItem.getId());
                if(orderItemList.isEmpty()) continue;
                if(orderItemList.size() == 1) {
                    OrderItem orderItem = orderItemList.get(0);
                    Order order = orderItem.getOrder();
                    Money oldSharedDiscountFee = orderItem.getSharedDiscountFee();
                    Money sharedDiscountFee = originalOrderItem.getAllPartMjzDiscount();
                    if(!oldSharedDiscountFee.equals(sharedDiscountFee)) {
                        log.info("订单[orderNo={}]中订单项[id={}]的分摊优惠发生变化, {} => {}", new Object[]{order.getOrderNo(), orderItem.getId(), oldSharedDiscountFee, sharedDiscountFee});
                        orderItem.setSharedDiscountFee(originalOrderItem.getAllPartMjzDiscount());
                        generalDAO.saveOrUpdate(orderItem);
                    }
                } else {
                    for(OrderItem orderItem : orderItemList) {
                        Order order = orderItem.getOrder();
                        if(!orderItem.getType().equals(OrderItemType.MEALSET)) {
                            throw new StewardBusinessException(String.format("订单ID[%d]对应的订单项是多个,但是不是套餐订单项??", orderItem.getOrderId()));
                        }
                        Money oldSharedDiscountFee = orderItem.getSharedDiscountFee();

                        //根据当前套餐项的占比,计算分摊的优惠金额
                        BigDecimal mealsetItemTotalPrice = orderItem.getPrice().multiply(orderItem.getBuyCount() / originalOrderItem.getBuyCount()).getAmountWithBigDecimal();
                        BigDecimal percent = new BigDecimal(0d);
                        if(originalOrderItem.getPrice().getAmount() != 0d) {
                            percent = mealsetItemTotalPrice.divide(originalOrderItem.getPrice().getAmountWithBigDecimal(), 2, RoundingMode.HALF_UP);
                        }
                        Money sharedDiscountFee = Money.valueOf(originalOrderItem.getAllPartMjzDiscount().getAmountWithBigDecimal().setScale(2, RoundingMode.HALF_UP).multiply(percent).doubleValue());
                        if(!oldSharedDiscountFee.equals(sharedDiscountFee)) {
                            log.info("订单[orderNo={}]中订单项[id={}]的分摊优惠发生变化, {} => {}", new Object[]{order.getOrderNo(), orderItem.getId(), oldSharedDiscountFee, sharedDiscountFee});
                            orderItem.setSharedDiscountFee(sharedDiscountFee);
                            generalDAO.saveOrUpdate(orderItem);
                        }
                    }
                }
            }

            Search orderSearch = new Search(Order.class);
            orderSearch.addFilterEqual("originalOrderId", originalOrder.getId());
            List<Order> orders = generalDAO.search(orderSearch);
            for(Order order : orders) {
                Money oldSharedDiscountFee = order.getSharedDiscountFee();
                Money allSharedDiscountFee = Money.valueOf(0d);
                for(OrderItem orderItem : order.getOrderItemList()) {
                    allSharedDiscountFee = allSharedDiscountFee.add(orderItem.getSharedDiscountFee());
                }
                order.setSharedDiscountFee(allSharedDiscountFee);
                if(!oldSharedDiscountFee.equals(allSharedDiscountFee)) {
                    log.info("订单[orderNo={}]的分摊优惠发生变化, {} => {}", new Object[]{order.getOrderNo(), oldSharedDiscountFee, allSharedDiscountFee});
                    generalDAO.saveOrUpdate(order);
                }
            }
        }

    }

    /**
     * 解决天猫由于整单优惠金额不会结算给我们导致货款不对的问题,只重新计算7.20号开始的订单
     * @throws Exception
     */
    @Transactional
    @Deprecated
    public void fixOrderFee() throws Exception {

        Search search = new Search(Order.class);
        search.addFilterEqual("platformType", PlatformType.TAO_BAO).addFilterGreaterOrEqual("createTime", DateUtils.parseDate("2014-07-02 00:00:00", "yyyy-MM-dd HH:mm:ss"));;
        List<Order> orders = generalDAO.search(search);
        for(Order order : orders) {
            //重新计算订单项的分摊优惠金额
            Money oldOrderActualFee = order.getActualFee();
            Money oldOrderGoodsFee = order.getGoodsFee();
            for(OrderItem orderItem : order.getOrderItemList()) {
                Money oldOrderItemActualFee = orderItem.getActualFee();
                Money oldOrderItemGoodsFee = orderItem.getGoodsFee();
                orderFeeService.calculateOrderItemFee(orderItem);
                boolean changed = false;
                if(!oldOrderItemActualFee.equals(orderItem.getActualFee())) {
                    changed = true;
                    log.info("订单[orderNo={}]中订单项[id={}]的平台结算金额发生变化, {} => {}", new Object[]{order.getOrderNo(), orderItem.getId(), oldOrderItemActualFee, orderItem.getActualFee()});
                }
                if(!oldOrderItemGoodsFee.equals(orderItem.getGoodsFee())) {
                    changed = true;
                    log.info("订单[orderNo={}]中订单项[id={}]的货款发生变化, {} => {}", new Object[]{order.getOrderNo(), orderItem.getId(), oldOrderItemGoodsFee, orderItem.getGoodsFee()});
                }
                if(changed) {
                    generalDAO.saveOrUpdate(orderItem);
                }
            }

            orderFeeService.calculateOrderFee(order, order.getOrderItemList());

            boolean changed = false;
            if(!oldOrderActualFee.equals(order.getActualFee())) {
                changed = true;
                log.info("订单[orderNo={}]的平台结算金额发生变化, {} => {}", new Object[]{order.getOrderNo(), oldOrderActualFee, order.getActualFee()});
            }
            if(!oldOrderGoodsFee.equals(order.getGoodsFee())) {
                changed = true;
                log.info("订单[orderNo={}]的货款发生变化, {} => {}", new Object[]{order.getOrderNo(), oldOrderGoodsFee, order.getGoodsFee()});
            }
            if(changed) {
                generalDAO.saveOrUpdate(order);
            }
        }
    }

}



