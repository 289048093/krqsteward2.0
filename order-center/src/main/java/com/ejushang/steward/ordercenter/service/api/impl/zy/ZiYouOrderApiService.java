package com.ejushang.steward.ordercenter.service.api.impl.zy;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;

import com.ejushang.steward.openapicenter.zy.api.ZyTradeApi;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Order;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Promotion;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Trade;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.TradeGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.TradesQueryResponse;
import com.ejushang.steward.openapicenter.zy.constant.ConstantZiYou;
import com.ejushang.steward.openapicenter.zy.exception.ZiYouApiException;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.FetchDataType;
import com.ejushang.steward.ordercenter.constant.ShopType;
import com.ejushang.steward.ordercenter.constant.ZyDateType;
import com.ejushang.steward.ordercenter.domain.OrderFetch;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.domain.OriginalOrderItem;
import com.ejushang.steward.ordercenter.domain.PromotionInfo;

import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import com.ejushang.steward.ordercenter.service.api.IOrderApiService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * User: Shiro
 * Date: 14-8-7
 * Time: 上午10:26
 */
public class ZiYouOrderApiService implements IOrderApiService {
    private static final Logger log = LoggerFactory.getLogger(ZiYouOrderApiService.class);

    @Autowired
    private OrderFetchService orderFetchService;

    @Autowired
    private OriginalOrderService originalOrderService;

    @Override
    public List<OriginalOrder> fetchOrderByApi(ShopBean shopBean) throws Exception {

        if (log.isInfoEnabled()) {
            log.info("自有API抓取订单:抓取参数【{}】", shopBean);
            log.info("自有API抓取订单:【{}】开始抓取订单：：：", shopBean.getSellerNick());
            log.info("自有API抓取订单：订单抓取开始时间：{}", shopBean.getFetchOrderStartDate());
            log.info("自有API抓取订单：订单抓取结束时间：{}", shopBean.getFetchOrderEndDate());
        }

        // 根据自有平台API抓取平台交易记录
        List<Trade> tradeList = getTradesByApi(shopBean);
        if (log.isInfoEnabled()) {
            log.info("自有API抓取订单：共抓取到自有平台交易记录：{}条", tradeList.size());
        }

        // 将外部平台订单转换为系统原始订单
        List<OriginalOrder> originalOrderList = getOriginalOrders(tradeList, shopBean);
        if (log.isInfoEnabled()) {
            log.info("自有API抓取订单：转换后的原始订单数为：{}条", originalOrderList.size());
            log.info("自有API抓取订单：保存原始订单及其订单项：");
        }

        // 保存所有原始订单至数据库
        originalOrderService.saveOriginalOrders(originalOrderList);

        if (log.isInfoEnabled()) {
            log.info("自有API抓取订单：保存订单抓取记录：");
        }
        // 所有操作成功完成，添加订单抓取记录
        saveOrderFetch(shopBean);

        if (log.isInfoEnabled()) {
            log.info("自有API抓取订单：【{}】抓单结束", shopBean.getSellerNick());
        }

        return originalOrderList;
    }

    /**
     * 通过自有平台API抓取订单
     *
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<Trade> getTradesByApi(ShopBean shopBean) throws Exception {
        // 保存所有外部交易记录的集合
        List<Trade> tradeList = new ArrayList<Trade>(0);

        // 判断账号类型是否为店铺
        if (StringUtils.equals(shopBean.getShopType().getValue(), ShopType.SHOP.getValue())) {
            if (log.isInfoEnabled()) {
                log.info("自有API抓取订单：当前账号【{}】为【店铺】类型，开始抓单", shopBean.getSellerNick());
            }
            // 根据自有平台API抓取自有平台交易信息
            tradeList = searchTradesByApi(shopBean);
        } else {
            if (log.isInfoEnabled()) {
                log.info("自有API抓取订单：当前账号【{}】为【供应商】类型，开始抓单", shopBean.getSellerNick());
            }

            // todo:待处理部分
        }

        return tradeList;
    }

    /**
     * 根据自有平台API抓取自有平台交易信息
     *
     * @param shopBean
     * @throws Exception
     */
    private List<Trade> searchTradesByApi(ShopBean shopBean) throws Exception {
        List<Trade> tradeList = new ArrayList<Trade>(0);

        // 通过自有平台api抓取订单 // 根据创建时间
        List<Trade> tradeList1 = searchTradeByApiAndCd(shopBean);
        if (log.isInfoEnabled()) {
            log.info("自有API抓取订单：根据创建时间抓单：{}条", tradeList1.size());
        }

        // 通过自有平台api抓取订单 // 根据修改时间
        List<Trade> tradeList2 = searchTradeByApiByUd(shopBean);
        if (log.isInfoEnabled()) {
            log.info("自有API抓取订单：根据更新时间抓单：{}条", tradeList2.size());
        }

        // 1.先添加更新时间的数据，防止被根据创建时间获取的旧数据替换
        addOriginalOrderDistinct(tradeList, tradeList2);
        // 2.再添加创建时间的数据
        addOriginalOrderDistinct(tradeList, tradeList1);

        return tradeList;
    }


    /**
     * 根据创建时间通过自有平台API检索订单
     *
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<Trade> searchTradeByApiAndCd(ShopBean shopBean) throws Exception {
        // 创建保存订单查询的map
        Map<String, Object> tradeArgsMap = new HashMap<String, Object>();
        // 设置每次查询100条 减少调用API的次数
        tradeArgsMap.put(ConstantZiYou.PAGE_SIZE, ConstantZiYou.ZY_FETCH_ORDER_PAGE_SIZE);
        //设置根据创建时间检索订单
        tradeArgsMap.put(ConstantZiYou.DATE_TYPE, ZyDateType.CREATED.zyValue);
        //设置订单创建时间的开始
        tradeArgsMap.put(ConstantZiYou.START_CREATED, shopBean.getFetchOrderEndDate());
        //设置订单创建时间的结束
        tradeArgsMap.put(ConstantZiYou.END_CREATED, shopBean.getFetchOrderEndDate());
        // 设置抓取订单状态
        tradeArgsMap.put(ConstantZiYou.STATUS, shopBean.getOrderStatus());
        //设置买家昵称
        tradeArgsMap.put(ConstantZiYou.BUYER_NICK, shopBean.getSellerNick());

        if (log.isInfoEnabled()) {
            log.info("淘宝API抓取订单：开始从淘宝平台查询订单信息……，参数argsMap = " + tradeArgsMap);
        }

        // 淘宝交易API
        ZyTradeApi tradeApi = new ZyTradeApi();
        if (log.isInfoEnabled()) {
            log.info("京东API抓取订单：TradeApi初始化：" + tradeApi);
        }

        TradesQueryResponse response = tradeApi.tradesQuery(tradeArgsMap);
        String errorCode = response.getErrorCode();
        if (StringUtils.equals(errorCode, "isv.invalid-parameter:buyer_nick")) {
            throw new ZiYouApiException("ErrorCode：" + errorCode + "，用户不存在【***】");
        } else if (StringUtils.equals(errorCode, "isp.remote-service-timeout")) {
            throw new ZiYouApiException("ErrorCode：" + errorCode + "，参数：API调用远程服务超时");
        }

        // 查询订单总条数
        Long totalCount = response.getTotalResults();

        Long pageSize = ConstantZiYou.ZY_FETCH_ORDER_PAGE_SIZE;
        Long pageNo = totalCount % pageSize == 0 ? totalCount / pageSize
                : totalCount / pageSize + 1;

        List<Trade> allTradeList = new ArrayList<Trade>();
        for (long i = 1L; i <= pageNo; i++) {
            tradeArgsMap.put(ConstantZiYou.PAGE_NO, i);
            response = tradeApi.tradesQuery(tradeArgsMap);
            errorCode = response.getErrorCode();
            // 处理淘宝API异常
            processZiYouApiException(shopBean, errorCode);
            if (CollectionUtils.isNotEmpty(response.getTrades())) {
                allTradeList.addAll(response.getTrades());
            }
        }

        Collections.sort(allTradeList, new Comparator<Trade>() {
            @Override
            public int compare(Trade o1, Trade o2) {
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            }
        });

        return allTradeList;
    }

    /**
     * 处理淘宝抓单的异常
     *
     * @param shopBean
     * @param errorCode
     * @throws ZiYouApiException
     */
    private void processZiYouApiException(ShopBean shopBean, String errorCode) throws ZiYouApiException {
        if (StringUtils.equals(errorCode, "isv.invalid-parameter:buyer_nick")) {
            throw new ZiYouApiException("ErrorCode：" + errorCode + "，用户不存在【" + shopBean.getSellerNick() + "】");
        } else if (StringUtils.equals(errorCode, "isp.remote-service-timeout")) {
            throw new ZiYouApiException("ErrorCode：" + errorCode + "，参数：API调用远程服务超时");
        }
    }

    /**
     * 根据更新时间通过自有平台API检索订单
     *
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<Trade> searchTradeByApiByUd(ShopBean shopBean) throws Exception {
        // 创建保存订单查询的map
        Map<String, Object> tradeArgsMap = new HashMap<String, Object>();
        // 设置每次查询100条 减少调用API的次数
        tradeArgsMap.put(ConstantZiYou.PAGE_SIZE, ConstantZiYou.ZY_FETCH_ORDER_PAGE_SIZE);
        //设置根据创建时间检索订单
        tradeArgsMap.put(ConstantZiYou.DATE_TYPE, ZyDateType.MODIFIED.zyValue);
        //设置订单创建时间的开始
        tradeArgsMap.put(ConstantZiYou.START_CREATED, shopBean.getFetchOrderEndDate());
        //设置订单创建时间的结束
        tradeArgsMap.put(ConstantZiYou.END_CREATED, shopBean.getFetchOrderEndDate());
        // 设置抓取订单状态
        tradeArgsMap.put(ConstantZiYou.STATUS, shopBean.getOrderStatus());
        //设置买家昵称
        tradeArgsMap.put(ConstantZiYou.BUYER_NICK, shopBean.getSellerNick());

        if (log.isInfoEnabled()) {
            log.info("淘宝API抓取订单：开始从淘宝平台查询订单信息……，参数argsMap = " + tradeArgsMap);
        }

        // 淘宝交易API
        ZyTradeApi tradeApi = new ZyTradeApi();
        if (log.isInfoEnabled()) {
            log.info("京东API抓取订单：TradeApi初始化：" + tradeApi);
        }

        TradesQueryResponse response = tradeApi.tradesQuery(tradeArgsMap);
        String errorCode = response.getErrorCode();
        if (StringUtils.equals(errorCode, "isv.invalid-parameter:buyer_nick")) {
            throw new ZiYouApiException("ErrorCode：" + errorCode + "，用户不存在【***】");
        } else if (StringUtils.equals(errorCode, "isp.remote-service-timeout")) {
            throw new ZiYouApiException("ErrorCode：" + errorCode + "，参数：API调用远程服务超时");
        }

        // 查询订单总条数
        Long totalCount = response.getTotalResults();

        Long pageSize = ConstantZiYou.ZY_FETCH_ORDER_PAGE_SIZE;
        Long pageNo = totalCount % pageSize == 0 ? totalCount / pageSize
                : totalCount / pageSize + 1;

        List<Trade> allTradeList = new ArrayList<Trade>();
        for (long i = 1L; i <= pageNo; i++) {
            tradeArgsMap.put(ConstantZiYou.PAGE_NO, i);
            response = tradeApi.tradesQuery(tradeArgsMap);
            errorCode = response.getErrorCode();
            // 处理淘宝API异常
            processZiYouApiException(shopBean, errorCode);
            if (CollectionUtils.isNotEmpty(response.getTrades())) {
                allTradeList.addAll(response.getTrades());
            }
        }

        Collections.sort(allTradeList, new Comparator<Trade>() {
            @Override
            public int compare(Trade o1, Trade o2) {
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            }
        });

        return allTradeList;
    }

    /**
     * 去除重复后添加自有平台至集合
     *
     * @param tradeList
     * @param tradeList2
     */
    private void addOriginalOrderDistinct(List<Trade> tradeList, List<Trade> tradeList2) {
        if (CollectionUtils.isNotEmpty(tradeList2)) {
            for (Trade trade : tradeList2) {
                boolean isExist = false;
                for (Trade tradeOri : tradeList) {
                    if (trade.getTid().equals(tradeOri.getTid())) {
                        isExist = true;
                    }
                }

                if (!isExist) {
                    tradeList.add(trade);
                }
            }
        }
    }

    @Override
    public List<OriginalOrder> fetchOrderByDeploy(ShopBean shopBean) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OriginalOrder fetchOrderById(ShopBean shopBean, String platformOrderNo) throws Exception {
        // 保存淘宝交易信息的集合
        List<Trade> tradeList = new ArrayList<Trade>();
        // 通过API获取
        ZyTradeApi tradeApi = new ZyTradeApi();
        Map<String, Object> tradeArgsMap = new HashMap<String, Object>();
        tradeArgsMap.put(ConstantZiYou.TID, Long.valueOf(platformOrderNo));
        TradeGetResponse response = tradeApi.tradeGet(tradeArgsMap);
        tradeList.add(response.getTrade());

        List<OriginalOrder> originalOrderList = getOriginalOrders(tradeList, shopBean);

        OriginalOrder originalOrder = CollectionUtils.isNotEmpty(originalOrderList) ? originalOrderList.get(0) : null;

        if (originalOrder != null) {
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
        if (CollectionUtils.isNotEmpty(platformOrderNoList)) {
            for (String platformOrderNo : platformOrderNoList) {
                OriginalOrder originalOrder = fetchOrderById(shopBean, platformOrderNo);
                if (originalOrder != null) {
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
        if (CollectionUtils.isNotEmpty(shopBeanMapList)) {
            for (Map<ShopBean, List<String>> shopBeanMap : shopBeanMapList) {
                for (Map.Entry<ShopBean, List<String>> entry : shopBeanMap.entrySet()) {
                    partOriginalOrderList = fetchOrdersByIds(entry.getKey(), entry.getValue());
                    if (CollectionUtils.isNotEmpty(partOriginalOrderList)) {
                        originalOrderList.addAll(partOriginalOrderList);
                    }
                }
            }
        }
        return originalOrderList;
    }

    /**
     * 获取原始订单集合
     *
     * @param trades
     * @param shopBean
     * @return
     */
    private List<OriginalOrder> getOriginalOrders(List<Trade> trades, ShopBean shopBean) throws Exception {
        // 原始订单集合
        List<OriginalOrder> originalOrders = new ArrayList<OriginalOrder>();
        if (trades == null) {
            return originalOrders;
        }
        for (Trade trade : trades) {
            // 将外部订单转化为系统原始订单
            OriginalOrder originalOrder = convertTrade2OriginalOrder(trade, shopBean);
            // 添加原始订单至集合
            originalOrders.add(originalOrder);
        }
        return originalOrders;
    }

    /**
     * 将外部交易对象Trade转换为内部原始订单OriginalOrder对象
     *
     * @param trade
     * @return
     */
    private OriginalOrder convertTrade2OriginalOrder(Trade trade, ShopBean shopBean) throws Exception {
        OriginalOrder originalOrder = null;
        if (trade == null) {
            return originalOrder;
        }
        OriginalOrder originalOrderQuery = new OriginalOrder();
        originalOrderQuery.setPlatformOrderNo(String.valueOf(trade.getTid()));
        originalOrder = originalOrderService.getOriginalOrderByCondition(originalOrderQuery);
        // 判断抓取下来的订单是否是最新的订单
        if (originalOrder != null && EJSDateUtils.isNew(originalOrder.getModifiedTime(), trade.getModifiedTime())) {
            return originalOrder;
        }

        if (originalOrder == null) {
            originalOrder = new OriginalOrder();
        }
        // 原始订单号
        originalOrder.setPlatformOrderNo(String.valueOf(trade.getTid()));
        // 交易状态
        originalOrder.setStatus(trade.getStatus());
        // 商品金额（商品价格乘以数量的总金额）
        originalOrder.setTotalFee(Money.valueOf(trade.getTotalFee()));
        // 订单实付金额(卖家应收金额）
        originalOrder.setActualFee(Money.valueOf(trade.getActualFee()));
        // 建议使用trade.promotion_details查询系统优惠 系统优惠金额（如打折，VIP，满就送等）
        originalOrder.setDiscountFee(Money.valueOf(trade.getDiscountFee()));
        // 设置订单所有订单级优惠
        originalOrder.setAllDiscountFee(Money.valueOf(trade.getDiscountFee()));
        // 是否包含邮费。与available_confirm_fee同时使用
        originalOrder.setHasPostFee(trade.getHasPostFee());
        // 邮费
        originalOrder.setPostFee(Money.valueOf(trade.getPostFee()));
        // 交易中剩余的确认收货金额（这个金额会随着子订单确认收货而不断减少，交易成功后会变为零）
        originalOrder.setAvailableConfirmFee(Money.valueOf(trade.getAvailableConfirmFee()));
        // 卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额）
        originalOrder.setReceivedPayment(Money.valueOf(trade.getReceivedPayment()));
        // 买家使用积分,下单时生成，且一直不变
        originalOrder.setPointFee(trade.getPointFee());
        // 买家实际使用积分（扣除部分退款使用的积分），交易完成后生成（交易成功或关闭），交易未完成时该字段值为0
        originalOrder.setRealPointFee(trade.getRealPointFee());
        //送货（日期）类型（1-只工作日送货(双休日、假日不用送);2-只双休日、假日送货(工作日不用送);3-工作日、双休日与假日均可送货;其他值-返回"任意时间"）'
        originalOrder.setDeliveryType(trade.getDeliveryType());
        // 买家留言
        originalOrder.setBuyerMessage(trade.getBuyerMessage());
        // 客服备注
        originalOrder.setRemark(trade.getRemark());
        // 买家id
        originalOrder.setBuyerId(trade.getBuyerId());
        // 买家支付宝账号
        originalOrder.setBuyerAlipayNo(trade.getBuyerAlipayNo());
        // 下单时间（交易创建时间）
        originalOrder.setBuyTime(trade.getBuyTime());
        // 支付时间
        originalOrder.setPayTime(trade.getPayTime());
        //结单时间
        originalOrder.setEndTime(trade.getEndTime());
        // 订单创建时间
        originalOrder.setCreateTime(trade.getCreateTime());
        // 订单修改时间
        originalOrder.setModifiedTime(trade.getModifiedTime());
        // 外部平台类型
        originalOrder.setPlatformType(shopBean.getPlatformType());
        // 店铺id
        originalOrder.setShopId(shopBean.getShopId());
        // 外部平台店铺id
        originalOrder.setOutShopId(shopBean.getOutShopId());
        // 是否需要发票
        Boolean needInvoice = StringUtils.isBlank(trade.getNeedReceipt()) ? false : true;
        originalOrder.setNeedReceipt(needInvoice);
        // 发票抬头
        originalOrder.setReceiptTitle(trade.getReceiptTitle());
        // 发票内容 : 暂时获取不到
        // 设置是否处理标识 默认为false
        originalOrder.setProcessed(false);

        // 设置收货人信息
        Receiver receiver = new Receiver();
        originalOrder.setReceiver(receiver);
        // 收货人姓名
        //originalOrder.setReceiverName(StringUtils.isBlank(trade.getReceiverName()) ? "sandbox_ejs_test1" : trade.getReceiverName());
        receiver.setReceiverName(trade.getReceiverName());
        // 收货人电话
        receiver.setReceiverPhone(trade.getReceiverPhone());
        // 收货人手机号
        receiver.setReceiverMobile(trade.getReceiverMobile());
        // 收货人邮编
        receiver.setReceiverZip(trade.getReceiverZip());
        // 收货人省份
        //originalOrder.setReceiverState(StringUtils.isBlank(trade.getReceiverState()) ? "广东省" : trade.getReceiverState());
        receiver.setReceiverState(trade.getReceiverState());
        // 收货人城市
        //originalOrder.setReceiverCity(StringUtils.isBlank(trade.getReceiverCity()) ? "深圳市" : trade.getReceiverCity());
        receiver.setReceiverCity(trade.getReceiverCity());
        // 收货人地区
        receiver.setReceiverDistrict(trade.getReceiverDistrict());
        // 收货人详细地址
        //originalOrder.setReceiverAddress(StringUtils.isBlank(trade.getReceiverAddress()) ? "宝安区大宝路41号" : trade.getReceiverAddress());
        receiver.setReceiverAddress(trade.getReceiverAddress());

        // 创建保存原始订单项的集合
        List<OriginalOrderItem> originalOrderItemList = new ArrayList<OriginalOrderItem>();
        // 设置订单项信息
        if (trade.getOrders() != null) {
            for (Order order : trade.getOrders()) {
                OriginalOrderItem originalOrderItem = convertOrder2OriginalOrderItem(order, originalOrder);
                if (originalOrderItem != null) {
                    originalOrderItemList.add(originalOrderItem);
                }
            }
        }

        // 交易记录的status都在Order中
        if (StringUtils.isBlank(originalOrder.getStatus())) {
            originalOrder.setStatus(trade.getStatus());
        }
        originalOrder.setOriginalOrderItemList(originalOrderItemList);

        // 只有新增订单时才设置优惠信息
        if (NumberUtil.isNullOrZero(originalOrder.getId())) {
            // 设置优惠详情信息
            List<Promotion> promotionList = trade.getPromotions();
            List<PromotionInfo> promotionInfoList = new ArrayList<PromotionInfo>(0);
            if (promotionList != null) {
                for (Promotion promotion : promotionList) {
                    PromotionInfo promotionInfo = convertPromotionDetail2PromotionInfo(promotion, originalOrder, shopBean);
                    if (promotionInfo != null) {
                        //添加进优惠详情集合
                        promotionInfoList.add(promotionInfo);
                    }
                }
            }
            // 设置订单优惠详情
            originalOrder.setPromotionInfoList(promotionInfoList);
        }

        if (originalOrder.getId() == null) {
            // 如果没有抓取到自有平台交易信息的创建时间，就将其设置为当前时间
            originalOrder.setCreateTime(originalOrder.getCreateTime() == null ? EJSDateUtils.getCurrentDate() : originalOrder.getCreateTime());
        }

        return originalOrder;
    }

    /**
     * 将外部订单对象Order转换为内部原始订单项OriginalOrderItem对象
     *
     * @param order
     * @return
     */
    private OriginalOrderItem convertOrder2OriginalOrderItem(Order order, OriginalOrder originalOrder) throws Exception {
        OriginalOrderItem originalOrderItem = null;
        if (order == null) {
            return originalOrderItem;
        }

        if (!NumberUtil.isNullOrZero(originalOrder.getId())) {
            OriginalOrderItem originalOrderItemQuery = new OriginalOrderItem();
            originalOrderItemQuery.setPlatformSubOrderNo(String.valueOf(order.getOid()));
            originalOrderItem = originalOrderService.getOriginalOrderItemByCondition(originalOrderItemQuery);
        }

        if (originalOrderItem == null) {
            originalOrderItem = new OriginalOrderItem();
        }

        // 子订单号
        originalOrderItem.setPlatformSubOrderNo(String.valueOf(order.getOid()));
        // 设置商品条形码
        originalOrderItem.setSku(order.getSku());
        // 设置商品名称
        originalOrderItem.setTitle(order.getTitle());
        // 商品价格
        originalOrderItem.setPrice(Money.valueOf(order.getPrice()));
        // 商品购买数量
        originalOrderItem.setBuyCount(order.getBuyCount());
        // 商品金额（商品价格*数量）
        originalOrderItem.setTotalFee(getItemTotalFee(order));
        // 应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）
        originalOrderItem.setPayableFee(Money.valueOf(order.getTotalFee()));
        // 子订单实付金额。精确到2位小数，单位:元。如:200.07，表示:200元7分。
        // 对于多子订单的交易，计算公式如下：payment = price * num + adjust_fee - discount_fee ；
        // 单子订单交易，payment与主订单的payment一致，对于退款成功的子订单，
        // 由于主订单的优惠分摊金额，会造成该字段可能不为0.00元。
        // 建议使用退款前的实付金额减去退款单中的实际退款金额计算。
        originalOrderItem.setActualFee(Money.valueOf(order.getActualFee()));
        // 子订单级订单优惠金额
        originalOrderItem.setDiscountFee(Money.valueOf(order.getDiscountFee()));
        // 手工调整金额
        originalOrderItem.setAdjustFee(Money.valueOf(order.getAdjustFee()));
        // 分摊之后的实付金额
        originalOrderItem.setDivideOrderFee(Money.valueOf(order.getDivideOrderFee()));
        // 优惠分摊
        originalOrderItem.setPartMjzDiscount(Money.valueOf(order.getPartMjzDiscount()));
        // 订单所有优惠的优惠分摊
        originalOrderItem.setAllPartMjzDiscount(Money.valueOf(order.getAllPartMjzDiscount()));


        return originalOrderItem;
    }

    /**
     * 计算订单项商品金额
     *
     * @return
     */
    private Money getItemTotalFee(Order order) {
        Money totalFee = Money.valueOf(0);

        if (order == null) {
            return totalFee;
        }
        // 计算商品金额
        totalFee = Money.valueOfCent(
                Money.valueOf(order.getPrice()).getCent() * order.getBuyCount());

        return totalFee;
    }

    /**
     * 将自有平台优惠信息转换为系统优惠信息
     *
     * @param promotion
     * @return
     */
    private PromotionInfo convertPromotionDetail2PromotionInfo(Promotion promotion, OriginalOrder originalOrder, ShopBean shopBean) {
        PromotionInfo promotionInfo = new PromotionInfo();
        // 外部订单或订单项编号
        promotionInfo.setPlatformOrderNo(String.valueOf(promotion.getOid()));
        // 优惠信息的名称
        promotionInfo.setPromotionName(promotion.getPromotionName());
        // 优惠金额
        promotionInfo.setDiscountFee(Money.valueOf(promotion.getDiscountFee()));
        // 优惠id
        promotionInfo.setPromotionId(promotion.getPromotionId());
        // 平台信息
        promotionInfo.setPlatformType(shopBean.getPlatformType());

        return promotionInfo;
    }

    /**
     * 保存订单抓取记录
     *
     * @param shopBean
     */
    private void saveOrderFetch(ShopBean shopBean) {
        OrderFetch orderFetchNew = getOrderFetch(shopBean);
        orderFetchService.save(orderFetchNew);
    }

    /**
     * 获取订单抓取信息
     *
     * @param shopBean
     * @return
     */
    private OrderFetch getOrderFetch(ShopBean shopBean) {
        OrderFetch orderFetchNew = new OrderFetch();
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
}
