package com.ejushang.steward.ordercenter.service.api.impl.jd;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.openapicenter.jd.api.JdOrderApi;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.ejushang.steward.openapicenter.jd.exception.JingDongApiException;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.OrderFetch;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.domain.OriginalOrderItem;
import com.ejushang.steward.ordercenter.domain.PromotionInfo;
import com.ejushang.steward.ordercenter.keygenerator.SequenceGenerator;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import com.ejushang.steward.ordercenter.service.ShopService;
import com.ejushang.steward.ordercenter.service.api.IOrderApiService;
import com.jd.open.api.sdk.domain.order.*;
import com.jd.open.api.sdk.response.order.OrderGetResponse;
import com.jd.open.api.sdk.response.order.OrderSearchResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 京东订单服务
 * User: Baron.Zhang
 * Date: 14-4-11
 * Time: 下午1:40
 */
@Service
@Transactional
public class JingDongOrderApiService implements IOrderApiService {

    private static final Logger log = LoggerFactory.getLogger(JingDongOrderApiService.class);

    @Autowired
    private OrderFetchService orderFetchService;
    @Autowired
    private OriginalOrderService originalOrderService;

    @Override
    public List<OriginalOrder> fetchOrderByApi(ShopBean shopBean) throws Exception {
        if(log.isInfoEnabled()){
            log.info("京东API抓取订单:抓取参数：{}",shopBean);
            log.info("京东API抓取订单:【{}】开始抓取订单：：：",shopBean.getSellerNick());
            log.info("京东API抓取订单: 订单抓取开始时间：{}",shopBean.getFetchOrderStartDate());
            log.info("京东API抓取订单: 订单抓取结束时间：{}",shopBean.getFetchOrderEndDate());
        }

        // 根据API抓取京东订单
        List<OrderSearchInfo> orderSearchInfoList = getOrderSearchInfosByApi(shopBean);
        if(log.isInfoEnabled()){
            log.info("京东API抓取订单：共抓取{}条订单", orderSearchInfoList.size());
        }

        // 将平台订单转换为原始订单
        List<OriginalOrder> originalOrderList = convertPlatformOrder2OriginalOrder(orderSearchInfoList,shopBean);
        if(log.isInfoEnabled()){
            log.info("京东API抓取订单：共转换{}条订单", originalOrderList.size());
        }

        if(log.isInfoEnabled()){
            log.info("京东API抓取订单：保存原始订单至数据库：");
        }
        // 保存原始订单至数据库
        originalOrderService.saveOriginalOrders(originalOrderList);

        if(log.isInfoEnabled()){
            log.info("京东API抓取订单：保存抓单记录至数据库：");
        }
        // 所有操作成功完成，添加订单抓取记录
        saveOrderFetch(shopBean);

        return originalOrderList;
    }

    /**
     * 保存抓单记录
     * @param shopBean
     */
    private void saveOrderFetch(ShopBean shopBean) {
        OrderFetch orderFetchNew = getOrderFetch(shopBean);
        orderFetchService.save(orderFetchNew);
    }

    /**
     * 根据API抓取京东订单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<OrderSearchInfo> getOrderSearchInfosByApi(ShopBean shopBean) throws Exception {

        List<OrderSearchInfo> orderSearchInfoList = new ArrayList<OrderSearchInfo>();

        // 通过api抓取订单 根据创建日期
        List<OrderSearchInfo> orderSearchInfoList1 = searchOrderByApiAndCd(shopBean);
        if(log.isInfoEnabled()){
            log.info("京东API抓取订单：根据创建时间抓取{}条", orderSearchInfoList1.size());
        }

        // 通过api抓取订单 根据更新日期
        List<OrderSearchInfo> orderSearchInfoList2 = searchOrderByApiAndUd(shopBean);
        if(log.isInfoEnabled()){
            log.info("京东API抓取订单：根据更新时间抓取{}条", orderSearchInfoList2.size());
        }

        // 1.先添加根据更新时间抓取的订单 防止旧数据
        addOrderSearchInfoDistinct(orderSearchInfoList, orderSearchInfoList2);
        // 2.添加根据创建时间抓取的订单
        addOrderSearchInfoDistinct(orderSearchInfoList, orderSearchInfoList1);

        return orderSearchInfoList;
    }

    /**
     * 去重复添加京东订单
     * @param orderSearchInfoList
     * @param orderSearchInfoList1
     */
    private void addOrderSearchInfoDistinct(List<OrderSearchInfo> orderSearchInfoList, List<OrderSearchInfo> orderSearchInfoList1) {
        if(CollectionUtils.isNotEmpty(orderSearchInfoList1)){
            for(OrderSearchInfo orderSearchInfo : orderSearchInfoList1){
                boolean isExist = false;
                for (OrderSearchInfo orderSearchInfoOri : orderSearchInfoList){
                    if(StringUtils.equalsIgnoreCase(orderSearchInfo.getOrderId(), orderSearchInfoOri.getOrderId())){
                        isExist = true;
                    }
                }
                if(!isExist){
                    orderSearchInfoList.add(orderSearchInfo);
                }
            }
        }
    }

    /**
     * 获取订单抓取信息
     * @param shopBean
     * @return
     */
    private OrderFetch getOrderFetch(ShopBean shopBean) {
        OrderFetch orderFetchNew = new OrderFetch();
        // 设置抓取开始时间
        orderFetchNew.setFetchStartTime(shopBean.getFetchOrderStartDate());
        // 设置抓取时间，为end
        orderFetchNew.setFetchTime(shopBean.getFetchOrderEndDate());
        orderFetchNew.setPlatformType(shopBean.getPlatformType());
        orderFetchNew.setShopId(shopBean.getShopId());
        orderFetchNew.setCreateTime(EJSDateUtils.getCurrentDate());
        orderFetchNew.setFetchDataType(FetchDataType.FETCH_ORDER);
        orderFetchNew.setFetchOptType(shopBean.getFetchOptType());
        return orderFetchNew;
    }

    /**
     * 订单批量转换
     * @param orderSearchInfoList
     * @return
     */
    private List<OriginalOrder> convertPlatformOrder2OriginalOrder(List<OrderSearchInfo> orderSearchInfoList,ShopBean shopBean) {
        // 创建保存原始订单的集合
        List<OriginalOrder> originalOrderList = new ArrayList<OriginalOrder>();
        OriginalOrder originalOrder = null;
        for(OrderSearchInfo orderSearchInfo : orderSearchInfoList){

            // 将外部平台订单转换为原始订单
            originalOrder = convertOrderSearchInfo2OriginalOrder(orderSearchInfo, shopBean);
            // 添加原始订单
            originalOrderList.add(originalOrder);
        }

        return originalOrderList;
    }

    /**
     * 将外部平台订单转换为原始订单
     * @param orderSearchInfo
     * @return
     */
    private OriginalOrder convertOrderSearchInfo2OriginalOrder(OrderSearchInfo orderSearchInfo, ShopBean shopBean) {
        OriginalOrder originalOrder = null;
        if(orderSearchInfo == null){
            return originalOrder;
        }

        OriginalOrder originalOrderQuery = new OriginalOrder();
        originalOrderQuery.setPlatformOrderNo(orderSearchInfo.getOrderId());
        originalOrder = originalOrderService.getOriginalOrderByCondition(originalOrderQuery);

        // 判断是否为最新的订单
        if(originalOrder != null &&
                EJSDateUtils.isNew(originalOrder.getModifiedTime(),
                        EJSDateUtils.parseDateForNull(orderSearchInfo.getModified(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR))){
            return originalOrder;
        }

        if(originalOrder == null){
            originalOrder = new OriginalOrder();
        }

        // 订单id
        originalOrder.setPlatformOrderNo(orderSearchInfo.getOrderId());
        // 商家id
        originalOrder.setOutShopId(orderSearchInfo.getVenderId());
        // 支付方式
        // 订单总金额/商品金额
        originalOrder.setTotalFee(Money.valueOf(orderSearchInfo.getOrderTotalPrice()));
        // 用户应付金额
        originalOrder.setPayableFee(Money.valueOf(orderSearchInfo.getOrderPayment()));
        // 用户实付金额
        originalOrder.setActualFee(Money.valueOf(orderSearchInfo.getOrderSellerPrice()));
        // 订单货款金额（订单总金额-商家优惠金额）
        originalOrder.setSellerFee(Money.valueOf(orderSearchInfo.getOrderSellerPrice()));
        // 商品的运费/邮费
        originalOrder.setPostFee(Money.valueOf(orderSearchInfo.getFreightPrice()));
        // 商家的优惠金额
        originalOrder.setSellerDiscountFee(Money.valueOf(orderSearchInfo.getSellerDiscount()));
        // 整单优惠金额（通过计算得出）
        originalOrder.setDiscountFee(getDiscountFee(orderSearchInfo.getCouponDetailList()));
        // 所有订单级优惠
        originalOrder.setAllDiscountFee(getAllDiscountFee(orderSearchInfo.getCouponDetailList()));
        // 订单状态（英文）
        originalOrder.setStatus(getOriginalOrderStatus(orderSearchInfo.getOrderState()));
        // 送货（日期）类型
        originalOrder.setDeliveryType(orderSearchInfo.getDeliveryType());
        // 发票信息
        originalOrder.setReceiptContent(orderSearchInfo.getInvoiceInfo());
        // 设置是否需要发票
        originalOrder.setNeedReceipt(StringUtils.isBlank(orderSearchInfo.getInvoiceInfo()) ? false : true);
        // 买家下单时订单备注
        originalOrder.setBuyerMessage(orderSearchInfo.getOrderRemark());
        // 商家备注
        originalOrder.setRemark(orderSearchInfo.getVenderRemark());
        // 下单时间
        originalOrder.setBuyTime(EJSDateUtils.parseDateForNull(orderSearchInfo.getOrderStartTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 确认付款时间
        originalOrder.setPayTime(EJSDateUtils.parseDateForNull(orderSearchInfo.getPaymentConfirmTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 结单时间
        originalOrder.setEndTime(EJSDateUtils.parseDateForNull(orderSearchInfo.getOrderEndTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 订单更新时间
        originalOrder.setModifiedTime(EJSDateUtils.parseDateForNull(orderSearchInfo.getModified(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 买家id
        originalOrder.setBuyerId(orderSearchInfo.getPin());
        // 设置平台信息
        originalOrder.setPlatformType(shopBean.getPlatformType());
        // 设置店铺id
        originalOrder.setShopId(shopBean.getShopId());
        // 设置余额支付金额
        originalOrder.setBalancedUsed(Money.valueOf(orderSearchInfo.getBalanceUsed()));
        // 设置操作处理标识
        originalOrder.setProcessed(false);

        // 设置收货人信息
        UserInfo userInfo = orderSearchInfo.getConsigneeInfo();
        Receiver receiver = new Receiver();
        originalOrder.setReceiver(receiver);
        if(userInfo != null){
            // 姓名
            receiver.setReceiverName(userInfo.getFullname());
            // 地址
            receiver.setReceiverAddress(userInfo.getFullAddress());
            // 固定电话
            receiver.setReceiverPhone(userInfo.getTelephone());
            // 手机
            receiver.setReceiverMobile(userInfo.getMobile());
            // 省
            receiver.setReceiverState(userInfo.getProvince());
            // 市
            receiver.setReceiverCity(userInfo.getCity());
            // 县/区
            receiver.setReceiverDistrict(userInfo.getCounty());
        }

        // 设置订单项信息
        List<ItemInfo> itemInfoList = orderSearchInfo.getItemInfoList();
        List<CouponDetail> couponDetailList = orderSearchInfo.getCouponDetailList();
        if(CollectionUtils.isNotEmpty(originalOrder.getOriginalOrderItemList())){
            originalOrder.setOriginalOrderItemList(new ArrayList<OriginalOrderItem>());
        }
        if(itemInfoList != null){
            for(ItemInfo itemInfo : itemInfoList){
                OriginalOrderItem originalOrderItem = convertItemInfo2OriginalOrderItem(orderSearchInfo, couponDetailList, itemInfo,originalOrder);
                // 添加原始订单项
                originalOrder.getOriginalOrderItemList().add(originalOrderItem);
            }
        }

        // 只有新增订单时才新增优惠信息
        if(NumberUtil.isNullOrZero(originalOrder.getId())){
            // 设置优惠详情信息
            PromotionInfo promotionInfo = null;
            if(couponDetailList != null){
                for(CouponDetail couponDetail : couponDetailList){
                    promotionInfo = new PromotionInfo();
                    // 订单号
                    promotionInfo.setPlatformOrderNo(orderSearchInfo.getOrderId());
                    // sku编号
                    promotionInfo.setSkuId(couponDetail.getSkuId());
                    // 优惠金额
                    promotionInfo.setDiscountFee(Money.valueOf(couponDetail.getCouponPrice()));
                    // 优惠类型
                    promotionInfo.setCouponType(couponDetail.getCouponType());

                    // 添加优惠详情
                    originalOrder.getPromotionInfoList().add(promotionInfo);
                }
            }
        }


        if(originalOrder.getId() == null) {
            originalOrder.setCreateTime(originalOrder.getBuyTime() == null ? EJSDateUtils.getCurrentDate() : originalOrder.getBuyTime());
        }

        return originalOrder;
    }

    /**
     * 将外部平台订单转换为原始订单
     * @param orderInfo
     * @return
     */
    private OriginalOrder convertOrderInfo2OriginalOrder(OrderInfo orderInfo, ShopBean shopBean) {
        OriginalOrder originalOrder = null;
        if(orderInfo == null){
            return originalOrder;
        }

        // 查看数据库是否已经存在该记录
        OriginalOrder originalOrderQuery = new OriginalOrder();
        originalOrderQuery.setPlatformOrderNo(orderInfo.getOrderId());
        originalOrder = originalOrderService.getOriginalOrderByCondition(originalOrderQuery);

        // 判断是否为最新订单
        if(originalOrder != null &&
                EJSDateUtils.isNew(originalOrder.getModifiedTime(),
                        EJSDateUtils.parseDateForNull(orderInfo.getModified(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR))){
            return originalOrder;
        }

        if(originalOrder == null){
            originalOrder = new OriginalOrder();
        }

        // 订单id
        originalOrder.setPlatformOrderNo(orderInfo.getOrderId());
        // 商家id
        originalOrder.setOutShopId(orderInfo.getVenderId());
        // 支付方式
        // 订单总金额/商品金额
        originalOrder.setTotalFee(Money.valueOf(orderInfo.getOrderTotalPrice()));
        // 用户应付金额
        originalOrder.setPayableFee(Money.valueOf(orderInfo.getOrderPayment()));
        // 用户实付金额
        originalOrder.setActualFee(Money.valueOf(orderInfo.getOrderSellerPrice()));
        // 订单货款金额（订单总金额-商家优惠金额）
        originalOrder.setSellerFee(Money.valueOf(orderInfo.getOrderSellerPrice()));
        // 商品的运费/邮费
        originalOrder.setPostFee(Money.valueOf(orderInfo.getFreightPrice()));
        // 商家的优惠金额
        originalOrder.setSellerDiscountFee(Money.valueOf(orderInfo.getSellerDiscount()));
        // 整单优惠金额（通过计算得出）
        originalOrder.setDiscountFee(getDiscountFee(orderInfo.getCouponDetailList()));
        // 获取所有订单级优惠
        originalOrder.setAllDiscountFee(getAllDiscountFee(orderInfo.getCouponDetailList()));
        // 订单状态（英文）
        originalOrder.setStatus(getOriginalOrderStatus(orderInfo.getOrderState()));
        // 送货（日期）类型
        originalOrder.setDeliveryType(orderInfo.getDeliveryType());
        // 发票信息
        originalOrder.setReceiptContent(orderInfo.getInvoiceInfo());
        // 设置是否需要发票
        originalOrder.setNeedReceipt(StringUtils.isBlank(orderInfo.getInvoiceInfo()) ? false : true);
        // 买家下单时订单备注
        originalOrder.setBuyerMessage(orderInfo.getOrderRemark());
        // 商家备注
        originalOrder.setRemark(orderInfo.getVenderRemark());
        // 下单时间
        originalOrder.setBuyTime(EJSDateUtils.parseDateForNull(orderInfo.getOrderStartTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 确认付款时间
        originalOrder.setPayTime(EJSDateUtils.parseDateForNull(orderInfo.getPaymentConfirmTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 结单时间
        originalOrder.setEndTime(EJSDateUtils.parseDateForNull(orderInfo.getOrderEndTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 订单更新时间
        originalOrder.setModifiedTime(EJSDateUtils.parseDateForNull(orderInfo.getModified(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 买家id
        originalOrder.setBuyerId(orderInfo.getPin());
        // 设置平台信息
        originalOrder.setPlatformType(shopBean.getPlatformType());
        // 设置店铺id
        originalOrder.setShopId(shopBean.getShopId());
        // 设置余额支付金额
        originalOrder.setBalancedUsed(Money.valueOf(orderInfo.getBalanceUsed()));
        // 设置操作处理标识
        originalOrder.setProcessed(false);

        // 设置收货人信息
        UserInfo userInfo = orderInfo.getConsigneeInfo();
        Receiver receiver = new Receiver();
        originalOrder.setReceiver(receiver);
        if(userInfo != null){
            // 姓名
            receiver.setReceiverName(userInfo.getFullname());
            // 地址
            receiver.setReceiverAddress(userInfo.getFullAddress());
            // 固定电话
            receiver.setReceiverPhone(userInfo.getTelephone());
            // 手机
            receiver.setReceiverMobile(userInfo.getMobile());
            // 省
            receiver.setReceiverState(userInfo.getProvince());
            // 市
            receiver.setReceiverCity(userInfo.getCity());
            // 县/区
            receiver.setReceiverDistrict(userInfo.getCounty());
        }

        // 设置订单项信息
        List<ItemInfo> itemInfoList = orderInfo.getItemInfoList();
        List<CouponDetail> couponDetailList = orderInfo.getCouponDetailList();
        if(CollectionUtils.isNotEmpty(originalOrder.getOriginalOrderItemList())){
            originalOrder.setOriginalOrderItemList(new ArrayList<OriginalOrderItem>());
        }
        if(itemInfoList != null){
            for(ItemInfo itemInfo : itemInfoList){
                OriginalOrderItem originalOrderItem = convertItemInfo2OriginalOrderItem(orderInfo, couponDetailList, itemInfo,originalOrder);
                // 添加原始订单项
                originalOrder.getOriginalOrderItemList().add(originalOrderItem);
            }
        }

        // 只有新增订单时才新增优惠信息
        if(NumberUtil.isNullOrZero(originalOrder.getId())) {
            // 设置优惠详情信息
            PromotionInfo promotionInfo = null;
            if (couponDetailList != null) {
                for (CouponDetail couponDetail : couponDetailList) {
                    promotionInfo = new PromotionInfo();
                    // 订单号
                    promotionInfo.setPlatformOrderNo(orderInfo.getOrderId());
                    // sku编号
                    promotionInfo.setSkuId(couponDetail.getSkuId());
                    // 优惠金额
                    promotionInfo.setDiscountFee(Money.valueOf(couponDetail.getCouponPrice()));
                    // 优惠类型
                    promotionInfo.setCouponType(couponDetail.getCouponType());
                    // 添加平台类型
                    promotionInfo.setPlatformType(shopBean.getPlatformType());
                    // 添加优惠详情
                    originalOrder.getPromotionInfoList().add(promotionInfo);
                }
            }
        }

        if(originalOrder.getId() == null) {
            originalOrder.setCreateTime(originalOrder.getBuyTime() == null ? EJSDateUtils.getCurrentDate() : originalOrder.getBuyTime());
        }

        return originalOrder;
    }

    /**
     * 将京东订单项转换为平台原始订单项
     * @param orderSearchInfo
     * @param couponDetailList
     * @param itemInfo
     * @return
     */
    private OriginalOrderItem convertItemInfo2OriginalOrderItem(OrderSearchInfo orderSearchInfo, List<CouponDetail> couponDetailList, ItemInfo itemInfo,OriginalOrder originalOrder) {
        OriginalOrderItem originalOrderItem = null;
        if(itemInfo == null){
            return originalOrderItem;
        }

        if(!NumberUtil.isNullOrZero(originalOrder.getId())) {
            OriginalOrderItem originalOrderItemQuery = new OriginalOrderItem();
            originalOrderItemQuery.setSku(itemInfo.getOuterSkuId());
            originalOrderItemQuery.setOriginalOrderId(originalOrder.getId());
            originalOrderItem = originalOrderService.getOriginalOrderItemByCondition(originalOrderItemQuery);
        }

        if(originalOrderItem == null) {
            originalOrderItem = new OriginalOrderItem();
        }
        // 商品条形码
        originalOrderItem.setSku(itemInfo.getOuterSkuId());
        // 外部平台商品条形码设置
        originalOrderItem.setOuterSku(itemInfo.getSkuId());
        // 商品名称
        originalOrderItem.setTitle(itemInfo.getSkuName());
        // 商品单价
        originalOrderItem.setPrice(Money.valueOf(itemInfo.getJdPrice()));
        // 购买数量
        originalOrderItem.setBuyCount(NumberUtils.toLong(itemInfo.getItemTotal()));
        // 商品金额
        originalOrderItem.setTotalFee(getItemTotalFee(itemInfo));
        // 应付金额
        originalOrderItem.setPayableFee(getItemPayableFee(itemInfo,couponDetailList));
        // 实付金额
        originalOrderItem.setActualFee(getItemPayableFee(itemInfo,couponDetailList));
        // 子订单级优惠金额
        originalOrderItem.setDiscountFee(getItemDiscountFee(itemInfo,couponDetailList));
        // 分摊后的实付金额
        originalOrderItem.setDivideOrderFee(getItemDivideOrderFee(itemInfo,orderSearchInfo));
        // 优惠分摊金额
        originalOrderItem.setPartMjzDiscount(getItemMjzDiscountFee(itemInfo,orderSearchInfo));
        // 所有优惠金额的分摊优惠
        originalOrderItem.setAllPartMjzDiscount(getAllItemMjzDiscountFee(itemInfo,orderSearchInfo));
        // 设置外部子订单号
        if(StringUtils.isBlank(originalOrderItem.getPlatformSubOrderNo())) {
            // 生成外部平台子订单号
            originalOrderItem.setPlatformSubOrderNo(SequenceGenerator.getInstance().getNextOrderItemNo());
        }

        return originalOrderItem;
    }

    /**
     * 将京东订单项转换为平台原始订单项
     * @param orderInfo
     * @param couponDetailList
     * @param itemInfo
     * @return
     */
    private OriginalOrderItem convertItemInfo2OriginalOrderItem(OrderInfo orderInfo, List<CouponDetail> couponDetailList, ItemInfo itemInfo,OriginalOrder originalOrder) {
        OriginalOrderItem originalOrderItem = null;

        if(itemInfo == null){
            return originalOrderItem;
        }

        if(!NumberUtil.isNullOrZero(originalOrder.getId())) {
            OriginalOrderItem originalOrderItemQuery = new OriginalOrderItem();
            originalOrderItemQuery.setSku(itemInfo.getOuterSkuId());
            originalOrderItemQuery.setOriginalOrderId(originalOrder.getId());
            originalOrderItem = originalOrderService.getOriginalOrderItemByCondition(originalOrderItemQuery);
        }

        if(originalOrderItem == null) {
            originalOrderItem = new OriginalOrderItem();
        }
        // 商品条形码
        originalOrderItem.setSku(itemInfo.getOuterSkuId());
        // 外部平台商品条形码设置
        originalOrderItem.setOuterSku(itemInfo.getSkuId());
        // 商品名称
        originalOrderItem.setTitle(itemInfo.getSkuName());
        // 商品单价
        originalOrderItem.setPrice(Money.valueOf(itemInfo.getJdPrice()));
        // 购买数量
        originalOrderItem.setBuyCount(NumberUtils.toLong(itemInfo.getItemTotal()));
        // 商品金额
        originalOrderItem.setTotalFee(getItemTotalFee(itemInfo));
        // 应付金额
        originalOrderItem.setPayableFee(getItemPayableFee(itemInfo,couponDetailList));
        // 实付金额
        originalOrderItem.setActualFee(getItemPayableFee(itemInfo,couponDetailList));
        // 子订单级优惠金额
        originalOrderItem.setDiscountFee(getItemDiscountFee(itemInfo,couponDetailList));
        // 分摊后的实付金额
        originalOrderItem.setDivideOrderFee(getItemDivideOrderFee(itemInfo,orderInfo));
        // 优惠分摊金额
        originalOrderItem.setPartMjzDiscount(getItemMjzDiscountFee(itemInfo,orderInfo));
        // 所有优惠分摊金额
        originalOrderItem.setAllPartMjzDiscount(getAllItemMjzDiscountFee(itemInfo,orderInfo));
        // 设置外部子订单号
        if(StringUtils.isBlank(originalOrderItem.getPlatformSubOrderNo())) {
            // 生成外部平台子订单号
            originalOrderItem.setPlatformSubOrderNo(SequenceGenerator.getInstance().getNextOrderItemNo());
        }

        return originalOrderItem;
    }

    /**
     * 计算获取分摊后的实付金额
     * @param itemInfo
     * @return
     */
    private Money getItemDivideOrderFee(ItemInfo itemInfo,OrderSearchInfo orderSearchInfo){
        // 获得订单项的应付金额
        Money payableFee = getItemPayableFee(itemInfo,orderSearchInfo.getCouponDetailList());
        // 获得订单项的分摊优惠金额
        Money itemMjzDiscountFee = getItemMjzDiscountFee(itemInfo,orderSearchInfo);

        return payableFee.subtract(itemMjzDiscountFee);
    }

    /**
     * 计算获取分摊后的实付金额
     * @param itemInfo
     * @return
     */
    private Money getItemDivideOrderFee(ItemInfo itemInfo,OrderInfo orderSearchInfo){
        // 获得订单项的应付金额
        Money payableFee = getItemPayableFee(itemInfo, orderSearchInfo.getCouponDetailList());
        // 获得订单项的分摊优惠金额
        Money itemMjzDiscountFee = getItemMjzDiscountFee(itemInfo,orderSearchInfo);

        return payableFee.subtract(itemMjzDiscountFee);
    }

    /**
     * 计算获取分摊优惠金额
     * 计算公式：实付金额所占百分比 * 店铺优惠金额
     * @param itemInfo
     * @return
     */
    private Money getItemMjzDiscountFee(ItemInfo itemInfo,OrderSearchInfo orderSearchInfo){
        Money partMjzDiscountFee = Money.valueOf(0);
        if(itemInfo == null){
            return partMjzDiscountFee;
        }

        // 获得当前实付金额所占百分比
        Money paymentPercent = getPaymentPercent(itemInfo,orderSearchInfo);
        // 获取当前店铺优惠金额
        Money discountFee = getDiscountFee(orderSearchInfo.getCouponDetailList());

        partMjzDiscountFee = paymentPercent.multiply(discountFee.getAmount());

        return partMjzDiscountFee;
    }

    /**
     * 计算获取所有优惠金额的分摊优惠
     * 计算公式：实付金额所占百分比 * 所有优惠金额
     * @param itemInfo
     * @return
     */
    private Money getAllItemMjzDiscountFee(ItemInfo itemInfo,OrderSearchInfo orderSearchInfo){
        Money partMjzDiscountFee = Money.valueOf(0);
        if(itemInfo == null){
            return partMjzDiscountFee;
        }

        // 获得当前实付金额所占百分比
        Money paymentPercent = getPaymentPercent(itemInfo,orderSearchInfo);
        // 获取所有
        Money discountFee = getAllDiscountFee(orderSearchInfo.getCouponDetailList());

        partMjzDiscountFee = paymentPercent.multiply(discountFee.getAmount());

        return partMjzDiscountFee;
    }

    /**
     * 计算获取所有优惠金额的分摊优惠
     * 计算公式：实付金额所占百分比 * 所有优惠金额
     * @param itemInfo
     * @return
     */
    private Money getAllItemMjzDiscountFee(ItemInfo itemInfo,OrderInfo orderInfo){
        Money partMjzDiscountFee = Money.valueOf(0);
        if(itemInfo == null){
            return partMjzDiscountFee;
        }

        // 获得当前实付金额所占百分比
        BigDecimal paymentPercentBig = getPaymentPercent(itemInfo,orderInfo);
        // 获取所有优惠金额
        Money discountFee = getAllDiscountFee(orderInfo.getCouponDetailList());
        // 计算所有优惠金额的分摊金额
        BigDecimal partMjzDiscountFeeBig = paymentPercentBig.multiply(new BigDecimal(discountFee.getAmount()));
        // 转为Money对象
        partMjzDiscountFee = Money.valueOf(partMjzDiscountFeeBig.doubleValue());

        return partMjzDiscountFee;
    }

    /**
     * 计算获取分摊优惠金额
     * 计算公式：实付金额所占百分比 * 店铺优惠金额
     * @param itemInfo
     * @return
     */
    private Money getItemMjzDiscountFee(ItemInfo itemInfo,OrderInfo orderInfo){
        Money partMjzDiscountFee = Money.valueOf(0);
        if(itemInfo == null){
            return partMjzDiscountFee;
        }
        // 获得当前实付金额所占百分比
        BigDecimal paymentPercentBig = getPaymentPercent(itemInfo,orderInfo);
        // 获取当前店铺优惠金额
        Money discountFee = getDiscountFee(orderInfo.getCouponDetailList());
        // 计算店铺优惠金额的分摊金额
        BigDecimal partMjzDiscountFeeBig = paymentPercentBig.multiply(new BigDecimal(discountFee.getAmount()));
        // 转为Money对象
        partMjzDiscountFee = Money.valueOf(partMjzDiscountFeeBig.doubleValue());

        return partMjzDiscountFee;
    }

    /**
     * 计算实付金额所占百分比
     * @param itemInfo
     * @param orderSearchInfo
     * @return
     */
    private Money getPaymentPercent(ItemInfo itemInfo,OrderSearchInfo orderSearchInfo){
        // 当前订单项实付金额
        Money itemPaymentFee = getItemPayableFee(itemInfo,orderSearchInfo.getCouponDetailList());
        // 当前订单实付金额
        Money orderPaymentFee =getTotalItemPayableFee(orderSearchInfo.getItemInfoList(),orderSearchInfo.getCouponDetailList());
        if(orderPaymentFee.getCent() == 0L){
            return Money.valueOf(0);
        }
        Money paymentPercent = itemPaymentFee.divide(orderPaymentFee);

        return paymentPercent;
    }

    /**
     * 计算实付金额所占百分比
     * @param itemInfo
     * @param orderInfo
     * @return
     */
    private BigDecimal getPaymentPercent(ItemInfo itemInfo,OrderInfo orderInfo){
        // 当前订单项实付金额
        Money itemPaymentFee = getItemPayableFee(itemInfo, orderInfo.getCouponDetailList());
        // 当前订单实付金额
        Money orderPaymentFee =getTotalItemPayableFee(orderInfo.getItemInfoList(),orderInfo.getCouponDetailList());
        if(itemPaymentFee == null || orderPaymentFee == null || orderPaymentFee.getCent() == 0L){
            return BigDecimal.ZERO;
        }

        BigDecimal paymentPercentBig = new BigDecimal(itemPaymentFee.getCent()).divide(new BigDecimal(orderPaymentFee.getCent()),4,BigDecimal.ROUND_HALF_UP);

        return paymentPercentBig;
    }


    /**
     * 计算获取订单项优惠金额
     * @param itemInfo
     * @param couponDetailList
     * @return
     */
    private Money getItemDiscountFee(ItemInfo itemInfo,List<CouponDetail> couponDetailList){
        Money discountFee = Money.valueOf(0);
        if(itemInfo == null){
            return discountFee;
        }
        if(couponDetailList != null){
            for(CouponDetail couponDetail : couponDetailList){
                if(StringUtils.equals(itemInfo.getSkuId(),couponDetail.getSkuId())){
                    discountFee = discountFee.add(Money.valueOf(couponDetail.getCouponPrice()));
                }
            }
        }
        return  discountFee;
    }

    /**
     * 计算获取京东订单项应付金额
     * @param itemInfo
     * @param couponDetailList
     * @return
     */
    private Money getItemPayableFee(ItemInfo itemInfo,List<CouponDetail> couponDetailList){
        // 订单项商品金额
        Money itemTotalFee = getItemTotalFee(itemInfo);
        // 订单项优惠金额
        Money itemDiscountFee = getItemDiscountFee(itemInfo,couponDetailList);

        Money payableFee = itemTotalFee.subtract(itemDiscountFee);

        return payableFee;
    }

    /**
     * 计算订单项商品应付金额之和（未分摊订单级金额前）
     */
    private Money getTotalItemPayableFee(List<ItemInfo> itemInfoList, List<CouponDetail> couponDetailList){
        Money totalItemPayableFee = Money.valueOf(0);
        if(itemInfoList != null){
            for(ItemInfo itemInfo : itemInfoList){
                totalItemPayableFee = totalItemPayableFee.add(getItemPayableFee(itemInfo,couponDetailList));
            }
        }
        return totalItemPayableFee;
    }

    /**
     * 计算订单项商品金额
     * @param itemInfo
     * @return
     */
    private Money getItemTotalFee(ItemInfo itemInfo){
        Money totalFee = Money.valueOf(0);

        if(itemInfo == null){
            return totalFee;
        }
        // 计算商品金额
        totalFee = Money.valueOfCent(
                Money.valueOf(itemInfo.getJdPrice()).getCent() * NumberUtils.toLong(itemInfo.getItemTotal()));

        return totalFee;
    }

    /**
     * 获取原始订单状态，与淘宝兼容
     * @param jdOrderState
     * @return
     */
    private String getOriginalOrderStatus(String jdOrderState){
        if(StringUtils.equalsIgnoreCase(jdOrderState, JdOrderStatus.WAIT_SELLER_STOCK_OUT.toString())){
            return OriginalOrderStatus.WAIT_SELLER_SEND_GOODS.toString();
        }
        else if(StringUtils.equalsIgnoreCase(jdOrderState, JdOrderStatus.WAIT_GOODS_RECEIVE_CONFIRM.toString())){
            return OriginalOrderStatus.WAIT_BUYER_CONFIRM_GOODS.toString();
        }
        else if(StringUtils.equalsIgnoreCase(jdOrderState, JdOrderStatus.FINISHED_L.toString())){
            return OriginalOrderStatus.TRADE_FINISHED.toString();
        }
        else {
            return jdOrderState;
            //throw new StewardBusinessException("【" + jdOrderState + "】无法识别!");
        }
    }

    /**
     * 获取整单优惠(需要分摊减去的优惠：100-店铺优惠，35-满返满送(返现))
     * @param couponDetailList 订单优惠详情
     * @return
     */
    private Money getDiscountFee(List<CouponDetail> couponDetailList) {
        Money discountFee = Money.valueOf(0);
        if(couponDetailList == null){
            return discountFee;
        }
        for(CouponDetail couponDetail : couponDetailList){
            if(StringUtils.isBlank(couponDetail.getSkuId())
                    && (StringUtils.equals(couponDetail.getCouponType(), PromotionType.JD_DIANPU.getDesc())
                   || StringUtils.equals(couponDetail.getCouponType(), PromotionType.JD_MANFANMANSONG.getDesc()))){
                discountFee = discountFee.add(Money.valueOf(couponDetail.getCouponPrice()));
            }
        }
        return discountFee;
    }

    /**
     * 获取所有订单级优惠
     * @param couponDetailList 订单优惠详情
     * @return
     */
    private Money getAllDiscountFee(List<CouponDetail> couponDetailList) {
        Money discountFee = Money.valueOf(0);
        if(couponDetailList == null){
            return discountFee;
        }
        for(CouponDetail couponDetail : couponDetailList){
            if(StringUtils.isBlank(couponDetail.getSkuId())){
                discountFee = discountFee.add(Money.valueOf(couponDetail.getCouponPrice()));
            }
        }
        return discountFee;
    }

    /**
     * 通过API根据更新时间查询订单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<OrderSearchInfo> searchOrderByApiAndUd(ShopBean shopBean) throws Exception {

        // 从京东平台获取订单信息,设置参数
        Map<String,Object> argsMap = new HashMap<String, Object>();
        // 订单抓取开始时间
        argsMap.put(ConstantJingDong.START_DATE,EJSDateUtils.formatDate(shopBean.getFetchOrderStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 订单抓取结束时间
        argsMap.put(ConstantJingDong.END_DATE,EJSDateUtils.formatDate(shopBean.getFetchOrderEndDate(),EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 查询时间类型
        argsMap.put(ConstantJingDong.DATETYPE,ConstantJingDong.JD_DATE_TYPE);
        // 查询订单类型
        argsMap.put(ConstantJingDong.ORDER_STATE, shopBean.getOrderStatus());
        // 查询第几页
        argsMap.put(ConstantJingDong.PAGE,"1");
        // 查询的每页最大条数
        argsMap.put(ConstantJingDong.PAGE_SIZE,ConstantJingDong.JD_FETCH_ORDER_PAGE_SIZE);
        // 查询排序类型：降序
         argsMap.put(ConstantJingDong.SORTTYPE,"1");
        // 设置返回结果字段
        argsMap.put(ConstantJingDong.OPTIONAL_FIELDS,getOrderOptionsFields());

        if(log.isInfoEnabled()){
            log.info("京东API抓取订单：设置参数argsMap="+argsMap);
        }

        // 初始化京东订单API
        JdOrderApi orderApi = new JdOrderApi(shopBean.getSessionKey());
        if(log.isInfoEnabled()){
            log.info("京东API抓取订单:JdOrderApi初始化："+orderApi);
        }
        OrderSearchResponse response = orderApi.orderSearch(argsMap);

        OrderResult orderResult = response.getOrderInfoResult();
        String code = response.getCode();

        if(log.isInfoEnabled()){
            log.info("京东API抓取订单:执行api调用进行订单抓取，返回code："+code+" （0表示访问正常，其他数字表示有异常）");
        }

        // 创建保存OrderSearchInfo的集合
        List<OrderSearchInfo> allOrderSearchInfoList = new ArrayList<OrderSearchInfo>();
        if(StringUtils.equals(code, "0") && orderResult != null){
            int totalCount = orderResult.getOrderTotal();
            int pageSize = Integer.valueOf(ConstantJingDong.JD_FETCH_ORDER_PAGE_SIZE);
            int totalPage = totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;

            if(log.isInfoEnabled()){
                log.info("京东API抓取订单:订单抓取返回结果，总条数："+totalCount+"，总页数："+totalPage+"，每页抓取条数："
                +pageSize);
            }

            for(int i = 1;i <= totalPage; i++){
                argsMap.put(ConstantJingDong.PAGE,String.valueOf(i));
                response = orderApi.orderSearch(argsMap);
                orderResult = response.getOrderInfoResult();
                code = response.getCode();
                if(StringUtils.equals(code,"0") && orderResult != null){
                    if(CollectionUtils.isNotEmpty(orderResult.getOrderInfoList())){
                        allOrderSearchInfoList.addAll(orderResult.getOrderInfoList());
                    }
                }
            }
        }

        Collections.sort(allOrderSearchInfoList,new Comparator<OrderSearchInfo>() {
            @Override
            public int compare(OrderSearchInfo o1, OrderSearchInfo o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });

        if(log.isInfoEnabled()){
            log.info("京东API抓取订单:订单抓取完成，总条数："+allOrderSearchInfoList.size());
        }
        return allOrderSearchInfoList;
    }

    /**
     * 根据api查询订单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<OrderSearchInfo> searchOrderByApiAndCd(ShopBean shopBean) throws Exception {

        // 从京东平台获取订单信息,设置参数
        Map<String,Object> argsMap = new HashMap<String, Object>();
        // 订单抓取开始时间
        argsMap.put(ConstantJingDong.START_DATE,EJSDateUtils.formatDate(shopBean.getFetchOrderStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 订单抓取结束时间
        argsMap.put(ConstantJingDong.END_DATE,EJSDateUtils.formatDate(shopBean.getFetchOrderEndDate(),EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        // 查询时间类型
        argsMap.put(ConstantJingDong.DATETYPE,"1");
        // 查询订单类型
        argsMap.put(ConstantJingDong.ORDER_STATE, shopBean.getOrderStatus());
        // 查询第几页
        argsMap.put(ConstantJingDong.PAGE,"1");
        // 查询的每页最大条数
        argsMap.put(ConstantJingDong.PAGE_SIZE,ConstantJingDong.JD_FETCH_ORDER_PAGE_SIZE);
        // 查询排序类型：降序
        argsMap.put(ConstantJingDong.SORTTYPE,"1");
        // 设置返回结果字段
        argsMap.put(ConstantJingDong.OPTIONAL_FIELDS,getOrderOptionsFields());

        if(log.isInfoEnabled()){
            log.info("京东API抓取订单：设置参数argsMap="+argsMap);
        }

        // 初始化京东订单API
        JdOrderApi orderApi = new JdOrderApi(shopBean.getSessionKey());
        if(log.isInfoEnabled()){
            log.info("京东API抓取订单:JdOrderApi初始化："+orderApi);
        }
        OrderSearchResponse response = orderApi.orderSearch(argsMap);

        OrderResult orderResult = response.getOrderInfoResult();
        String code = response.getCode();

        if(log.isInfoEnabled()){
            log.info("京东API抓取订单:执行api调用进行订单抓取，返回code："+code+" （0表示访问正常，其他数字表示有异常）");
        }

        // 创建保存OrderSearchInfo的集合
        List<OrderSearchInfo> allOrderSearchInfoList = new ArrayList<OrderSearchInfo>();
        if(StringUtils.equals(code, "0") && orderResult != null){
            int totalCount = orderResult.getOrderTotal();
            int pageSize = Integer.valueOf(ConstantJingDong.JD_FETCH_ORDER_PAGE_SIZE);
            int totalPage = totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;

            if(log.isInfoEnabled()){
                log.info("京东API抓取订单:订单抓取返回结果，总条数："+totalCount+"，总页数："+totalPage+"，每页抓取条数："
                        +pageSize);
            }

            for(int i = 1;i <= totalPage; i++){
                argsMap.put(ConstantJingDong.PAGE,String.valueOf(i));
                response = orderApi.orderSearch(argsMap);
                orderResult = response.getOrderInfoResult();
                code = response.getCode();
                if(StringUtils.equals(code,"0") && orderResult != null){
                    if(CollectionUtils.isNotEmpty(orderResult.getOrderInfoList())){
                        allOrderSearchInfoList.addAll(orderResult.getOrderInfoList());
                    }
                }
            }
        }

        Collections.sort(allOrderSearchInfoList,new Comparator<OrderSearchInfo>() {
            @Override
            public int compare(OrderSearchInfo o1, OrderSearchInfo o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });

        if(log.isInfoEnabled()){
            log.info("京东API抓取订单:订单抓取完成，总条数："+allOrderSearchInfoList.size());
        }
        return allOrderSearchInfoList;
    }

    /**
     * 获取订单检索返回字段
     * @return
     */
    private String getOrderOptionsFields(){
        List<String> fieldList = new ArrayList<String>(0);
        fieldList.add(ConstantJingDong.ORDER_ID);
        fieldList.add(ConstantJingDong.VENDER_ID);
        fieldList.add(ConstantJingDong.PAY_TYPE);
        fieldList.add(ConstantJingDong.ORDER_TOTAL_PRICE);
        fieldList.add(ConstantJingDong.ORDER_PAYMENT);
        fieldList.add(ConstantJingDong.ORDER_SELLER_PRICE);
        fieldList.add(ConstantJingDong.FREIGHT_PRICE);
        fieldList.add(ConstantJingDong.SELLER_DISCOUNT);
        fieldList.add(ConstantJingDong.ORDER_STATE);
        fieldList.add(ConstantJingDong.ORDER_STATE_REMARK);
        fieldList.add(ConstantJingDong.DELIVERY_TYPE);
        fieldList.add(ConstantJingDong.INVOICE_INFO);
        fieldList.add(ConstantJingDong.ORDER_REMARK);
        fieldList.add(ConstantJingDong.ORDER_START_TIME);
        fieldList.add(ConstantJingDong.ORDER_END_TIME);
        fieldList.add(ConstantJingDong.CONSIGNEE_INFO);
        fieldList.add(ConstantJingDong.ITEM_INFO_LIST);
        fieldList.add(ConstantJingDong.COUPON_DETAIL_LIST);
        fieldList.add(ConstantJingDong.RETURN_ORDER);
        fieldList.add(ConstantJingDong.VENDER_REMARK);
        fieldList.add(ConstantJingDong.PIN);
        fieldList.add(ConstantJingDong.BALANCE_USED);
        fieldList.add(ConstantJingDong.MODIFIED);
        fieldList.add(ConstantJingDong.PAYMENT_CONFIRM_TIME);
        fieldList.add(ConstantJingDong.LOGISTICS_ID);
        fieldList.add(ConstantJingDong.WAYBILL);
        fieldList.add(ConstantJingDong.VAT_INVOICE_INFO);
        fieldList.add(ConstantJingDong.PARENT_ORDER_ID);
        fieldList.add(ConstantJingDong.ORDER_TYPE);

        StringBuffer sb = new StringBuffer("");
        String sep = ",";
        for(int i = 0; i < fieldList.size(); i++){
            if(i == 0){
                sb.append(fieldList.get(i));
            }
            else{
                sb.append(",").append(fieldList.get(i));
            }
        }
        return sb.toString();
    }

    @Override
    public List<OriginalOrder> fetchOrderByDeploy(ShopBean shopBean) throws Exception {
        return null;
    }

    @Override
    public OriginalOrder fetchOrderById(ShopBean shopBean,String platformOrderNo) throws Exception {
        JdOrderApi jdOrderApi = new JdOrderApi(shopBean.getSessionKey());
        Map<String,Object> orderArgsMap = new HashMap<String, Object>();
        orderArgsMap.put(ConstantJingDong.OPTIONAL_FIELDS,getOrderOptionsFields());
        orderArgsMap.put(ConstantJingDong.ORDER_ID,platformOrderNo);

        OrderGetResponse orderGetResponse = jdOrderApi.orderGet(orderArgsMap);

        if(!StringUtils.equalsIgnoreCase("0",orderGetResponse.getCode())){
            throw new JingDongApiException(orderGetResponse.getMsg());
        }

        OrderDetailInfo orderDetailInfo = orderGetResponse.getOrderDetailInfo();

        OriginalOrder originalOrder = null;
        if(orderDetailInfo != null) {
            OrderInfo orderInfo = orderDetailInfo.getOrderInfo();
            originalOrder = convertOrderInfo2OriginalOrder(orderInfo,shopBean);
        }

        if(originalOrder != null){
            originalOrderService.saveOriginalOrder(originalOrder);
            for (OriginalOrderItem originalOrderItem : originalOrder.getOriginalOrderItemList()) {
                originalOrderItem.setOriginalOrderId(originalOrder.getId());
                originalOrderService.saveOriginalOrderItem(originalOrderItem);
            }
            for (PromotionInfo promotionInfo : originalOrder.getPromotionInfoList()) {
                promotionInfo.setOriginalOrderId(originalOrder.getId());
                originalOrderService.savePromotionInfo(promotionInfo);
            }
        }

        return originalOrder;
    }

    @Override
    public List<OriginalOrder> fetchOrdersByIds(ShopBean shopBean, List<String> platformOrderNoList) throws Exception {
        List<OriginalOrder> originalOrderList = new ArrayList<OriginalOrder>();
        if(CollectionUtils.isNotEmpty(platformOrderNoList)){
            for(String platformOrderNo : platformOrderNoList) {
                OriginalOrder originalOrder = fetchOrderById(shopBean, platformOrderNo);
                if(originalOrder != null){
                    originalOrderList.add(originalOrder);
                }
            }
        }
        return originalOrderList;
    }

    @Override
    public List<OriginalOrder> fetchOrdersByIds(List<Map<ShopBean, List<String>>> shopBeanMapList) throws Exception {
        List<OriginalOrder> originalOrderList = new ArrayList<OriginalOrder>();
        List<OriginalOrder> partOriginalOrderList = null;
        if(CollectionUtils.isNotEmpty(shopBeanMapList)){
            for(Map<ShopBean,List<String>> shopBeanMap : shopBeanMapList){
                for(Map.Entry<ShopBean,List<String>> entry : shopBeanMap.entrySet()){
                    partOriginalOrderList = fetchOrdersByIds(entry.getKey(),entry.getValue());
                    if(CollectionUtils.isNotEmpty(partOriginalOrderList)){
                        originalOrderList.addAll(partOriginalOrderList);
                    }
                }
            }
        }
        return originalOrderList;
    }

}
