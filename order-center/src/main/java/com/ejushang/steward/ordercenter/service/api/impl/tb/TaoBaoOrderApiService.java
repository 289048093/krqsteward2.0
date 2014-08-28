package com.ejushang.steward.ordercenter.service.api.impl.tb;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.openapicenter.tb.api.TbTradeApi;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.exception.TaoBaoApiException;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.FetchDataType;
import com.ejushang.steward.ordercenter.constant.OriginalOrderStatus;
import com.ejushang.steward.ordercenter.constant.ShopType;
import com.ejushang.steward.ordercenter.constant.TbOrderStatus;
import com.ejushang.steward.ordercenter.domain.OrderFetch;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.domain.OriginalOrderItem;
import com.ejushang.steward.ordercenter.domain.PromotionInfo;
import com.ejushang.steward.ordercenter.domain.taobao.JdpTbTrade;
import com.ejushang.steward.ordercenter.domain.taobao.JdpTbTradeQuery;
import com.ejushang.steward.ordercenter.service.*;
import com.ejushang.steward.ordercenter.service.api.IOrderApiService;
import com.ejushang.steward.ordercenter.service.taobao.JdpTbTradeService;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.PromotionDetail;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.response.TradeFullinfoGetResponse;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: Baron.Zhang
 * Date: 14-4-11
 * Time: 下午1:45
 */
@Service
@Transactional
public class TaoBaoOrderApiService implements IOrderApiService {

    private static final Logger log = LoggerFactory.getLogger(TaoBaoOrderApiService.class);

    @Autowired
    private OrderFetchService orderFetchService;

    @Autowired
    private JdpTbTradeService jdpTbTradeService;

    @Autowired
    private OriginalOrderService originalOrderService;

    @Override
    public List<OriginalOrder> fetchOrderByApi(ShopBean shopBean) throws Exception {

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取订单:抓取参数【{}】",shopBean);
            log.info("淘宝API抓取订单:【{}】开始抓取订单：：：",shopBean.getSellerNick());
            log.info("淘宝API抓取订单：订单抓取开始时间：{}" , shopBean.getFetchOrderStartDate());
            log.info("淘宝API抓取订单：订单抓取结束时间：{}" , shopBean.getFetchOrderEndDate());
        }

        // 根据淘宝API抓取平台交易记录
        List<Trade> tradeList = getTradesByApi(shopBean);
        if(log.isInfoEnabled()){
            log.info("淘宝API抓取订单：共抓取到淘宝交易记录：{}条" , tradeList.size());
        }

        // 将外部平台订单转换为系统原始订单
        List<OriginalOrder> originalOrderList = getOriginalOrders(tradeList, shopBean);
        if(log.isInfoEnabled()){
            log.info("淘宝API抓取订单：转换后的原始订单数为：{}条" , originalOrderList.size());
            log.info("淘宝API抓取订单：保存原始订单及其订单项：");
        }

        // 保存所有原始订单至数据库
        originalOrderService.saveOriginalOrders(originalOrderList);

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取订单：保存订单抓取记录：");
        }
        // 所有操作成功完成，添加订单抓取记录
        saveOrderFetch(shopBean);

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取订单：【{}】抓单结束",shopBean.getSellerNick());
        }

        return originalOrderList;
    }

    /**
     * 保存订单抓取记录
     * @param shopBean
     */
    private void saveOrderFetch(ShopBean shopBean) {
        OrderFetch orderFetchNew = getOrderFetch(shopBean);
        orderFetchService.save(orderFetchNew);
    }

    /**
     * 通过淘宝API抓取订单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<Trade> getTradesByApi(ShopBean shopBean) throws Exception {
        // 保存所有外部交易记录的集合
        List<Trade> tradeList = new ArrayList<Trade>(0);

        // 判断账号类型是否为店铺
        if(StringUtils.equals(shopBean.getShopType().getValue(), ShopType.SHOP.getValue())){
            if(log.isInfoEnabled()){
                log.info("淘宝API抓取订单：当前账号【{}】为【店铺】类型，开始抓单",shopBean.getSellerNick());
            }
            // 根据淘宝API抓取淘宝交易信息
            tradeList = searchTradesByApi(shopBean);
        }
        else{
            if(log.isInfoEnabled()){
                log.info("淘宝API抓取订单：当前账号【{}】为【供应商】类型，开始抓单",shopBean.getSellerNick());
            }

            // todo:待处理部分
        }

        return tradeList;
    }

    /**
     * 根据淘宝API抓取淘宝交易信息
     * @param shopBean
     * @throws Exception
     */
    private List<Trade> searchTradesByApi(ShopBean shopBean) throws Exception {
        List<Trade> tradeList = new ArrayList<Trade>(0);

        // 通过淘宝api抓取订单 // 根据创建时间
        List<Trade> tradeList1 = searchTradeByApiAndCd(shopBean);
        if(log.isInfoEnabled()){
            log.info("淘宝API抓取订单：根据创建时间抓单：{}条",tradeList1.size());
        }

        // 通过淘宝api抓取订单 // 根据修改时间
        List<Trade> tradeList2 = searchTradeByApiByUd(shopBean);
        if(log.isInfoEnabled()){
            log.info("淘宝API抓取订单：根据更新时间抓单：{}条",tradeList2.size());
        }

        // 1.先添加更新时间的数据，防止被根据创建时间获取的旧数据替换
        addOriginalOrderDistinct(tradeList, tradeList2);
        // 2.再添加创建时间的数据
        addOriginalOrderDistinct(tradeList, tradeList1);

        return tradeList;
    }

    /**
     * 去除重复后添加淘宝平台至集合
     * @param tradeList
     * @param tradeList2
     */
    private void addOriginalOrderDistinct(List<Trade> tradeList, List<Trade> tradeList2) {
        if(CollectionUtils.isNotEmpty(tradeList2)){
            for(Trade trade : tradeList2){
                boolean isExist = false;
                for(Trade tradeOri : tradeList){
                    if(trade.getTid().longValue() == tradeOri.getTid().longValue()){
                        isExist = true;
                    }
                }

                if(!isExist){
                    tradeList.add(trade);
                }
            }
        }
    }

    /**
     * 根据更新时间通过淘宝API检索订单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<Trade> searchTradeByApiByUd(ShopBean shopBean) throws Exception {
        // 创建保存订单查询的map
        Map<String,Object> tradeArgsMap = new HashMap<String, Object>();
        // 获取并设置查询订单的field
        tradeArgsMap.put(ConstantTaoBao.FIELDS,getTradeFields());
        // 设置每次查询100条 减少调用API的次数
        tradeArgsMap.put(ConstantTaoBao.PAGE_SIZE,ConstantTaoBao.TB_FETCH_ORDER_PAGE_SIZE);
        // 设置抓取订单状态
        tradeArgsMap.put(ConstantTaoBao.STATUS,shopBean.getOrderStatus());
        // 设置订单创建时间的起始
        tradeArgsMap.put(ConstantTaoBao.START_MODIFIED,shopBean.getFetchOrderStartDate());
        // 设置订单创建时间的结束
        tradeArgsMap.put(ConstantTaoBao.END_MODIFIED,shopBean.getFetchOrderEndDate());

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取订单：开始从淘宝平台查询订单信息……，参数argsMap = " + tradeArgsMap);
        }

        // 淘宝交易API
        TbTradeApi tradeApi = new TbTradeApi(shopBean.getSessionKey());
        if(log.isInfoEnabled()){
            log.info("京东API抓取订单：TradeApi初始化："+tradeApi);
        }

        TradesSoldIncrementGetResponse response = tradeApi.tradesSoldIncrementGet(tradeArgsMap);
        String errorCode = response.getErrorCode();
        if(StringUtils.equals(errorCode,"isv.invalid-parameter:buyer_nick")){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，用户不存在【***】");
        }
        else if(StringUtils.equals(errorCode,"isv.invalid-parameter:seller_nick")){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，参数：seller_nick无效，格式不对、非法值、越界等");
        }
        else if(StringUtils.equals(errorCode,"isp.remote-service-timeout")){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，参数：API调用远程服务超时");
        }
        else if(StringUtils.isNotBlank(errorCode)){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，"+response.getBody());
        }

        // 查询订单总条数
        Long totalCount = response.getTotalResults();

        Long pageSize = ConstantTaoBao.TB_FETCH_ORDER_PAGE_SIZE;
        Long pageNo = totalCount % pageSize == 0 ? totalCount/pageSize
                : totalCount/pageSize + 1;

        List<Trade> allTradeList = new ArrayList<Trade>();
        for(long i = 1L; i <= pageNo; i++){
            tradeArgsMap.put(ConstantTaoBao.PAGE_NO,i);
            response = tradeApi.tradesSoldIncrementGet(tradeArgsMap);
            errorCode = response.getErrorCode();
            // 处理淘宝API异常
            processTaoBaoApiException(shopBean, errorCode);
            if(CollectionUtils.isNotEmpty(response.getTrades())){
                allTradeList.addAll(response.getTrades());
            }
        }

        Collections.sort(allTradeList,new Comparator<Trade>() {
            @Override
            public int compare(Trade o1, Trade o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });

        return allTradeList;
    }

    /**
     * 处理淘宝抓单的异常
     * @param shopBean
     * @param errorCode
     * @throws TaoBaoApiException
     */
    private void processTaoBaoApiException(ShopBean shopBean, String errorCode) throws TaoBaoApiException {
        if(StringUtils.equals(errorCode, "isv.invalid-parameter:buyer_nick")){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，用户不存在【"+shopBean.getSellerNick()+"】");
        }
        else if(StringUtils.equals(errorCode,"isv.invalid-parameter:seller_nick")){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，参数：seller_nick【"+shopBean.getSellerNick()+"】无效，格式不对、非法值、越界等");
        }
        else if(StringUtils.equals(errorCode,"isp.remote-service-timeout")){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，参数：API调用远程服务超时");
        }
    }

    /**
     * 根据创建时间通过淘宝API检索订单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<Trade> searchTradeByApiAndCd(ShopBean shopBean) throws Exception {
        // 创建保存订单查询的map
        Map<String,Object> tradeArgsMap = new HashMap<String, Object>();
        // 获取并设置查询订单的field
        tradeArgsMap.put(ConstantTaoBao.FIELDS,getTradeFields());
        // 设置每次查询100条 减少调用API的次数
        tradeArgsMap.put(ConstantTaoBao.PAGE_SIZE,ConstantTaoBao.TB_FETCH_ORDER_PAGE_SIZE);
        // 设置抓取订单状态
        tradeArgsMap.put(ConstantTaoBao.STATUS,shopBean.getOrderStatus());
        // 设置订单创建时间的起始
        tradeArgsMap.put(ConstantTaoBao.START_CREATED,shopBean.getFetchOrderStartDate());
        // 设置订单创建时间的结束
        tradeArgsMap.put(ConstantTaoBao.END_CREATED,shopBean.getFetchOrderEndDate());

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取订单：开始从淘宝平台查询订单信息……，参数argsMap = " + tradeArgsMap);
        }

        // 淘宝交易API
        TbTradeApi tradeApi = new TbTradeApi(shopBean.getSessionKey());
        if(log.isInfoEnabled()){
            log.info("京东API抓取订单：TradeApi初始化："+tradeApi);
        }

        TradesSoldGetResponse response = tradeApi.tradesSoldGet(tradeArgsMap);
        String errorCode = response.getErrorCode();
        if(StringUtils.equals(errorCode,"isv.invalid-parameter:buyer_nick")){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，用户不存在【***】");
        }
        else if(StringUtils.equals(errorCode,"isv.invalid-parameter:seller_nick")){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，参数：seller_nick无效，格式不对、非法值、越界等");
        }
        else if(StringUtils.equals(errorCode,"isp.remote-service-timeout")){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，参数：API调用远程服务超时");
        }
        else if(StringUtils.isNotBlank(errorCode)){
            throw new TaoBaoApiException("ErrorCode："+errorCode+"，"+response.getBody());
        }

        // 查询订单总条数
        Long totalCount = response.getTotalResults();

        Long pageSize = ConstantTaoBao.TB_FETCH_ORDER_PAGE_SIZE;
        Long pageNo = totalCount % pageSize == 0 ? totalCount/pageSize
                : totalCount/pageSize + 1;

        List<Trade> allTradeList = new ArrayList<Trade>();
        for(long i = 1L; i <= pageNo; i++){
            tradeArgsMap.put(ConstantTaoBao.PAGE_NO,i);
            response = tradeApi.tradesSoldGet(tradeArgsMap);
            errorCode = response.getErrorCode();
            // 处理淘宝API异常
            processTaoBaoApiException(shopBean, errorCode);
            if(CollectionUtils.isNotEmpty(response.getTrades())){
                allTradeList.addAll(response.getTrades());
            }
        }

        Collections.sort(allTradeList,new Comparator<Trade>() {
            @Override
            public int compare(Trade o1, Trade o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });

        return allTradeList;
    }

    /**
     * 获取订单抓取信息
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

    /**
     * 获取原始订单集合
     * @param trades
     * @param shopBean
     * @return
     */
    private List<OriginalOrder> getOriginalOrders(List<Trade> trades, ShopBean shopBean) throws Exception {
        // 原始订单集合
        List<OriginalOrder> originalOrders = new ArrayList<OriginalOrder>();
        if(trades == null){
            return originalOrders;
        }
        for (Trade trade : trades){
            // 将外部订单转化为系统原始订单
            OriginalOrder originalOrder = convertTrade2OriginalOrder(trade,shopBean);
            // 添加原始订单至集合
            originalOrders.add(originalOrder);
        }
        return originalOrders;
    }

    /**
     * 将外部订单对象Order转换为内部原始订单项OriginalOrderItem对象
     * @param order
     * @return
     */
    private OriginalOrderItem convertOrder2OriginalOrderItem(Order order,OriginalOrder originalOrder) throws Exception {
        OriginalOrderItem originalOrderItem = null;
        if(order == null){
            return originalOrderItem;
        }

        if(!NumberUtil.isNullOrZero(originalOrder.getId())) {
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
        originalOrderItem.setSku(StringUtils.isNotBlank(order.getOuterSkuId()) ? order.getOuterSkuId() : order.getOuterIid());
        // 设置商品名称
        originalOrderItem.setTitle(order.getTitle());
        // 商品价格
        originalOrderItem.setPrice(Money.valueOf(order.getPrice()));
        // 商品购买数量
        originalOrderItem.setBuyCount(order.getNum());
        // 商品金额（商品价格*数量）
        originalOrderItem.setTotalFee(getItemTotalFee(order));
        // 应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）
        originalOrderItem.setPayableFee(Money.valueOf(order.getTotalFee()));
        // 子订单实付金额。精确到2位小数，单位:元。如:200.07，表示:200元7分。
        // 对于多子订单的交易，计算公式如下：payment = price * num + adjust_fee - discount_fee ；
        // 单子订单交易，payment与主订单的payment一致，对于退款成功的子订单，
        // 由于主订单的优惠分摊金额，会造成该字段可能不为0.00元。
        // 建议使用退款前的实付金额减去退款单中的实际退款金额计算。
        originalOrderItem.setActualFee(Money.valueOf(order.getPayment()));
        // 子订单级订单优惠金额
        originalOrderItem.setDiscountFee(Money.valueOf(order.getDiscountFee()));
        // 手工调整金额
        originalOrderItem.setAdjustFee(Money.valueOf(order.getAdjustFee()));
        // 分摊之后的实付金额
        originalOrderItem.setDivideOrderFee(Money.valueOf(order.getDivideOrderFee()));
        // 优惠分摊
        originalOrderItem.setPartMjzDiscount(Money.valueOf(order.getPartMjzDiscount()));
        // 订单所有优惠的优惠分摊
        originalOrderItem.setAllPartMjzDiscount(Money.valueOf(order.getPartMjzDiscount()));

        return originalOrderItem;
    }

    /**
     * 订单所有优惠的优惠分摊
     * 计算公式：订单项实付/(订单实付+整单优惠)*所有订单优惠
     * 订单项实付=商品金额*数量-订单项优惠
     * @param originalOrder
     * @param originalOrderItem
     * @return
     */
    private Money getAllPartMjzDiscount(OriginalOrder originalOrder, OriginalOrderItem originalOrderItem){
        Money allPartMjzDiscount = Money.valueOf(0);
        if(originalOrder == null || originalOrderItem == null){
            return allPartMjzDiscount;
        }

        // 订单项实付
        Money itemActualFee = originalOrderItem.getPayableFee() == null ? Money.valueOf(0) : originalOrderItem.getPayableFee();
        // 订单实付
        Money orderActualFee = originalOrder.getActualFee() == null ? Money.valueOf(0) : originalOrder.getActualFee();
        // 订单整单优惠
        Money discountFee = originalOrder.getDiscountFee() == null ? Money.valueOf(0) : originalOrder.getDiscountFee();
        // 订单所有优惠
        Money allDiscountFee = originalOrder.getAllDiscountFee() == null ? Money.valueOf(0) : originalOrder.getAllDiscountFee();

        if(itemActualFee.getCent() == 0 || orderActualFee.add(discountFee).getCent() == 0){
            return allPartMjzDiscount;
        }
        // 获取优惠占比
        BigDecimal paymentPercentBig = new BigDecimal(itemActualFee.getCent()).divide(new BigDecimal(orderActualFee.add(discountFee).getCent()),4,BigDecimal.ROUND_HALF_UP);
        // 获取优惠分摊
        BigDecimal allPartMjzDiscountBig = paymentPercentBig.multiply(new BigDecimal(allDiscountFee.getAmount()));
        // 转换为Money
        allPartMjzDiscount = Money.valueOf(allPartMjzDiscountBig.doubleValue());

        return allPartMjzDiscount;
    }

    /**
     * 计算订单项商品金额
     * @return
     */
    private Money getItemTotalFee(Order order){
        Money totalFee = Money.valueOf(0);

        if(order == null){
            return totalFee;
        }
        // 计算商品金额
        totalFee = Money.valueOfCent(
                Money.valueOf(order.getPrice()).getCent() *order.getNum());

        return totalFee;
    }

    /**
     * 将外部交易对象Trade转换为内部原始订单OriginalOrder对象
     * @param trade
     * @return
     */
    private OriginalOrder convertTrade2OriginalOrder(Trade trade,ShopBean shopBean) throws Exception {
        OriginalOrder originalOrder = null;
        if(trade == null){
            return originalOrder;
        }
        OriginalOrder originalOrderQuery = new OriginalOrder();
        originalOrderQuery.setPlatformOrderNo(String.valueOf(trade.getTid()));
        originalOrder = originalOrderService.getOriginalOrderByCondition(originalOrderQuery);
        // 判断抓取下来的订单是否是最新的订单
        if(originalOrder!= null && EJSDateUtils.isNew(originalOrder.getModifiedTime(),trade.getModified())){
            return originalOrder;
        }

        if(originalOrder == null) {
            originalOrder = new OriginalOrder();
        }
        // 原始订单号
        originalOrder.setPlatformOrderNo(String.valueOf(trade.getTid()));
        // 交易状态
        originalOrder.setStatus(trade.getStatus());
        // 商品金额（商品价格乘以数量的总金额）
        originalOrder.setTotalFee(Money.valueOf(trade.getTotalFee()));
        // 订单实付金额(卖家应收金额）
        originalOrder.setActualFee(Money.valueOf(trade.getPayment()));
        // 建议使用trade.promotion_details查询系统优惠 系统优惠金额（如打折，VIP，满就送等）
        originalOrder.setDiscountFee(Money.valueOf(trade.getDiscountFee()));
        // 设置订单所有订单级优惠
        originalOrder.setAllDiscountFee(Money.valueOf(trade.getDiscountFee()));
        // 买家使用积分,下单时生成，且一直不变
        originalOrder.setPointFee(trade.getPointFee());
        // 是否包含邮费。与available_confirm_fee同时使用
        originalOrder.setHasPostFee(trade.getHasPostFee());
        // 交易中剩余的确认收货金额（这个金额会随着子订单确认收货而不断减少，交易成功后会变为零）
        originalOrder.setAvailableConfirmFee(Money.valueOf(trade.getAvailableConfirmFee()));
        // 买家实际使用积分（扣除部分退款使用的积分），交易完成后生成（交易成功或关闭），交易未完成时该字段值为0
        originalOrder.setRealPointFee(trade.getRealPointFee());
        // 邮费
        originalOrder.setPostFee(Money.valueOf(trade.getPostFee()));
        // 卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额）
        originalOrder.setReceivedPayment(Money.valueOf(trade.getReceivedPayment()));
        // 买家留言
        originalOrder.setBuyerMessage(trade.getBuyerMessage());
        // 客服备注
        originalOrder.setRemark(trade.getSellerMemo());
        // 买家id
        originalOrder.setBuyerId(trade.getBuyerNick());
        // 买家支付宝账号
        originalOrder.setBuyerAlipayNo(trade.getBuyerAlipayNo());
        // 下单时间（交易创建时间）
        originalOrder.setBuyTime(trade.getCreated());
        // 支付时间
        originalOrder.setPayTime(trade.getPayTime());
        // 订单创建时间
        originalOrder.setCreateTime(trade.getCreated());
        // 订单修改时间
        originalOrder.setModifiedTime(trade.getModified());
        // 外部平台类型
        originalOrder.setPlatformType(shopBean.getPlatformType());
        // 店铺id
        originalOrder.setShopId(shopBean.getShopId());
        // 外部平台店铺id
        originalOrder.setOutShopId(shopBean.getOutShopId());
        // 是否需要发票
        Boolean needInvoice = StringUtils.isBlank(trade.getInvoiceName()) ? false : true;
        originalOrder.setNeedReceipt(needInvoice);
        // 发票抬头
        originalOrder.setReceiptTitle(trade.getInvoiceName());
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
        if(trade.getOrders() != null){
            for (Order order : trade.getOrders()){
                OriginalOrderItem originalOrderItem = convertOrder2OriginalOrderItem(order,originalOrder);
                if(originalOrderItem != null) {
                    originalOrderItemList.add(originalOrderItem);
                }
            }
        }

        // 交易记录的status都在Order中
        if(StringUtils.isBlank(originalOrder.getStatus())){
            originalOrder.setStatus(trade.getOrders().get(0).getStatus());
        }
        originalOrder.setOriginalOrderItemList(originalOrderItemList);

        // 只有新增订单时才设置优惠信息
        if(NumberUtil.isNullOrZero(originalOrder.getId())) {
            // 设置优惠详情信息
            List<PromotionDetail> promotionDetailList = trade.getPromotionDetails();
            List<PromotionInfo> promotionInfoList = new ArrayList<PromotionInfo>(0);
            if (promotionDetailList != null) {
                for (PromotionDetail promotionDetail : promotionDetailList) {
                    PromotionInfo promotionInfo = convertPromotionDetail2PromotionInfo(promotionDetail, originalOrder, shopBean);
                    if (promotionInfo != null) {
                        //添加进优惠详情集合
                        promotionInfoList.add(promotionInfo);
                    }
                }
            }
            // 设置订单优惠详情
            originalOrder.setPromotionInfoList(promotionInfoList);
        }

        if(originalOrder.getId() == null) {
            // 如果没有抓取到淘宝交易信息的创建时间，就将其设置为当前时间
            originalOrder.setCreateTime(originalOrder.getCreateTime() == null ? EJSDateUtils.getCurrentDate() : originalOrder.getCreateTime());
        }

        return originalOrder;
    }

    /**
     * 获取订单的所有订单级优惠
     * @param trade
     * @return
     */
    private Money getAllDiscountFee(Trade trade){
        Money allDiscountFee = Money.valueOf(0);
        if(trade == null){
            return allDiscountFee;
        }
        List<PromotionDetail> promotionDetailList = trade.getPromotionDetails();
        if(CollectionUtils.isEmpty(promotionDetailList)){
            return allDiscountFee;
        }

        for(PromotionDetail promotionDetail : promotionDetailList){
            if(trade.getTid().longValue() == promotionDetail.getId().longValue()){
                if(StringUtils.isNotBlank(promotionDetail.getDiscountFee())){
                    allDiscountFee = allDiscountFee.add(Money.valueOf(promotionDetail.getDiscountFee()));
                }
            }
        }

        // 加上订单整单优惠
        allDiscountFee = allDiscountFee.add(Money.valueOf(trade.getDiscountFee()));

        return allDiscountFee;
    }

    /**
     * 将淘宝优惠信息转换为系统优惠信息
     * @param promotionDetail
     * @return
     */
    private PromotionInfo convertPromotionDetail2PromotionInfo(PromotionDetail promotionDetail,OriginalOrder originalOrder,ShopBean shopBean) {
        PromotionInfo promotionInfo = new PromotionInfo();
        // 外部订单或订单项编号
        promotionInfo.setPlatformOrderNo(String.valueOf(promotionDetail.getId()));
        // 优惠信息的名称
        promotionInfo.setPromotionName(promotionDetail.getPromotionName());
        // 优惠金额
        promotionInfo.setDiscountFee(Money.valueOf(promotionDetail.getDiscountFee()));
        // 赠品名称
        promotionInfo.setGiftItemName(promotionDetail.getGiftItemName());
        // 赠品id
        promotionInfo.setGiftItemId(promotionDetail.getGiftItemId());
        // 赠品数量
        promotionInfo.setGiftItemNum(NumberUtils.toInt(promotionDetail.getGiftItemNum()));
        // 优惠活动描述
        promotionInfo.setPromotionDesc(promotionDetail.getPromotionDesc());
        // 优惠id
        promotionInfo.setPromotionId(promotionDetail.getPromotionId());
        // 平台信息
        promotionInfo.setPlatformType(shopBean.getPlatformType());

        return promotionInfo;
    }

    /**
     * 获取订单查询字段
     * @return
     */
    private String getTradeFields() {
        List<String> fieldList = new ArrayList<String>();
        fieldList.add("tid");
        fieldList.add("num");
        fieldList.add("num_iid");
        fieldList.add("status");
        fieldList.add("title");
        fieldList.add("type");
        fieldList.add("price");
        fieldList.add("seller_cod_fee");
        fieldList.add("discount_fee");
        fieldList.add("point_fee");
        fieldList.add("has_post_fee");
        fieldList.add("total_fee");
        fieldList.add("is_lgtype");
        fieldList.add("is_brand_sale");
        fieldList.add("is_force_wlb");
        fieldList.add("lg_aging");
        fieldList.add("lg_aging_type");
        fieldList.add("created");
        fieldList.add("pay_time");
        fieldList.add("modified");
        fieldList.add("end_time");
        fieldList.add("buyer_message");
        fieldList.add("alipay_id");
        fieldList.add("alipay_no");
        fieldList.add("alipay_url");
        fieldList.add("buyer_memo");
        fieldList.add("buyer_flag");
        fieldList.add("seller_memo");
        fieldList.add("seller_flag");
        fieldList.add("invoice_name");
        fieldList.add("invoice_type");
        fieldList.add("buyer_nick");
        fieldList.add("buyer_area");
        fieldList.add("buyer_email");
        fieldList.add("has_yfx");
        fieldList.add("yfx_fee");
        fieldList.add("yfx_id");
        fieldList.add("yfx_type");
        fieldList.add("has_buyer_message");
        fieldList.add("area_id");
        fieldList.add("credit_card_fee");
        fieldList.add("nut_feature");
        fieldList.add("step_trade_status");
        fieldList.add("step_paid_fee");
        fieldList.add("mark_desc");
        fieldList.add("eticket_ext");
        fieldList.add("send_time");
        fieldList.add("shipping_type");
        fieldList.add("buyer_cod_fee");
        fieldList.add("express_agency_fee");
        fieldList.add("adjust_fee");
        fieldList.add("buyer_obtain_point_fee");
        fieldList.add("cod_fee");
        fieldList.add("trade_from");
        fieldList.add("alipay_warn_msg");
        fieldList.add("cod_status");
        fieldList.add("can_rate");
        fieldList.add("service_orders");
        fieldList.add("commission_fee");
        fieldList.add("trade_memo");
        fieldList.add("buyer_rate");
        fieldList.add("trade_source");
        fieldList.add("seller_can_rate");
        fieldList.add("is_part_consign");
        fieldList.add("is_daixiao");
        fieldList.add("real_point_fee");
        fieldList.add("receiver_city");
        fieldList.add("receiver_district");
        fieldList.add("arrive_interval");
        fieldList.add("arrive_cut_time");
        fieldList.add("consign_interval");
        fieldList.add("async_modified");
        fieldList.add("is_wt");
        fieldList.add("orders");
        fieldList.add("promotion");
        fieldList.add("promotion_details");
        fieldList.add("seller_nick");
        fieldList.add("iid");
        fieldList.add("pic_path");
        fieldList.add("payment");
        fieldList.add("snapshot_url");
        fieldList.add("snapshot");
        fieldList.add("seller_rate");
        fieldList.add("post_fee");
        fieldList.add("buyer_alipay_no");
        fieldList.add("receiver_name");
        fieldList.add("receiver_state");
        fieldList.add("receiver_address");
        fieldList.add("receiver_zip");
        fieldList.add("receiver_mobile");
        fieldList.add("receiver_phone");
        fieldList.add("consign_time");
        fieldList.add("seller_alipay_no");
        fieldList.add("seller_mobile");
        fieldList.add("seller_phone");
        fieldList.add("seller_name");
        fieldList.add("seller_email");
        fieldList.add("available_confirm_fee");
        fieldList.add("received_payment");
        fieldList.add("timeout_action_time");
        fieldList.add("is_3D");

        StringBuffer fieldBuffer = new StringBuffer();

        for(int i = 0; i < fieldList.size(); i++){
            if(i == 0){
                fieldBuffer.append(fieldList.get(i));
            }
            else{
                fieldBuffer.append(",").append(fieldList.get(i));
            }
        }

        return fieldBuffer.toString();
    }

    @Override
    public List<OriginalOrder> fetchOrderByDeploy(ShopBean shopBean) throws Exception {
        if(log.isInfoEnabled()){
            log.info("聚石塔抓取订单:抓取参数{}",shopBean);
            log.info("聚石塔抓取订单:【{}】开始抓取订单：：：",shopBean.getSellerNick());
            log.info("聚石塔抓取订单：订单抓取开始时间：" + shopBean.getFetchOrderStartDate());
            log.info("聚石塔抓取订单：订单抓取结束时间：" + shopBean.getFetchOrderEndDate());
        }

        // 从聚石塔中抓取订单
        List<Trade> trades = getTradesByJst(shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔订单抓取：抓取到淘宝订单共：{}条" , trades.size());
        }

        // 将淘宝平台交易信息转换为系统交易信息
        List<OriginalOrder> originalOrderList = getOriginalOrders(trades, shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔订单抓取：转换后的原始订单数为：" + originalOrderList.size());
            log.info("聚石塔订单抓取：保存原始订单及其订单项：");
        }
        // 保存原始订单
        originalOrderService.saveOriginalOrders(originalOrderList);

        // 更新抓取时间(只要过程没有异常，就要更新抓取时间，否则会有重复抓取
        // 所有操作成功完成，添加订单抓取记录
        if(log.isInfoEnabled()){
            log.info("聚石塔订单抓取：保存订单抓取记录：");
        }
        // 保存订单抓取记录
        saveOrderFetch(shopBean);

        if(log.isInfoEnabled()){
            log.info("聚石塔订单抓取：【{}】抓取结束。", shopBean.getSellerNick());
        }

        return originalOrderList;
    }

    /**
     * 通过聚石塔抓取淘宝交易信息
     * @param shopBean
     * @return
     * @throws com.taobao.api.ApiException
     * @throws TaoBaoApiException
     */
    private List<Trade> getTradesByJst(ShopBean shopBean) throws com.taobao.api.ApiException, TaoBaoApiException {
        List<JdpTbTrade> jdpTbTradeList = new ArrayList<JdpTbTrade>();
        // 根据创建时间抓取聚石塔订单
        List<JdpTbTrade> jdpTbTradeList1 = findJdpTbTradesByCd(shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔抓取订单：根据创建时间抓取订单:{}条",jdpTbTradeList1.size());
        }
        // 根据更新时间抓取聚石塔订单
        List<JdpTbTrade> jdpTbTradeList2 = findJdpTbTradesByUd(shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔抓取订单：根据更新时间抓取订单:{}条",jdpTbTradeList2.size());
        }

        // 1.先添加更新时间的数据 防止被创建时间的旧数据替换
        addJdpTbTradeDistinct(jdpTbTradeList, jdpTbTradeList2);
        // 2.再添加创建时间的数据
        addJdpTbTradeDistinct(jdpTbTradeList, jdpTbTradeList1);

        if(log.isInfoEnabled()){
            log.info("聚石塔订单抓取：抓取到原始淘宝交易信息：{}条",jdpTbTradeList.size());
        }

        // 将json交易信息转化为淘宝交易信息
        List<Trade> trades = convertJdpTbTradeList2TradeList(jdpTbTradeList);


        return trades;
    }


    /**
     * 批量将聚石塔交易信息转换为淘宝平台交易信息
     * @param jdpTbTradeList
     * @return
     * @throws com.taobao.api.ApiException
     * @throws TaoBaoApiException
     */
    private List<Trade> convertJdpTbTradeList2TradeList(List<JdpTbTrade> jdpTbTradeList) throws com.taobao.api.ApiException, TaoBaoApiException {
        List<Trade> trades = new ArrayList<Trade>();
        if(CollectionUtils.isNotEmpty(jdpTbTradeList)){
            for (JdpTbTrade jdpTbTrade : jdpTbTradeList){
                Trade trade = convertJdpTbTrade2Trade(jdpTbTrade);
                if(trade != null){
                    trades.add(trade);
                }
            }
        }
        return trades;
    }

    /**
     * 将聚石塔交易信息转换为淘宝平台交易信息
     * @param jdpTbTrade
     * @return
     * @throws com.taobao.api.ApiException
     * @throws TaoBaoApiException
     */
    private Trade convertJdpTbTrade2Trade(JdpTbTrade jdpTbTrade) throws com.taobao.api.ApiException, TaoBaoApiException {
        TradeFullinfoGetResponse rsp = TaobaoUtils.parseResponse(
                jdpTbTrade.getJdpResponse(), TradeFullinfoGetResponse.class);
        if(StringUtils.isNotBlank(rsp.getErrorCode())){
            throw new TaoBaoApiException(rsp.getBody());
        }
        return rsp.getTrade();
    }

    /**
     * 去除重复添加聚石塔交易信息
     * @param jdpTbTradeList
     * @param jdpTbTradeList2
     */
    private void addJdpTbTradeDistinct(List<JdpTbTrade> jdpTbTradeList, List<JdpTbTrade> jdpTbTradeList2) {
        if(CollectionUtils.isNotEmpty(jdpTbTradeList2)){
            for(JdpTbTrade jdpTbTrade : jdpTbTradeList2){
                boolean isExist = false;
                for(JdpTbTrade jdpTbTradeOri : jdpTbTradeList){
                    if(jdpTbTrade.getTid().longValue() == jdpTbTradeOri.getTid().longValue()){
                        isExist = true;
                    }
                }
                if(!isExist){
                    jdpTbTradeList.add(jdpTbTrade);
                }
            }
        }
    }

    /**
     * 根据更新时间抓取聚石塔订单
     * @param shopBean
     * @return
     */
    private List<JdpTbTrade> findJdpTbTradesByUd(ShopBean shopBean) {
        // 从聚石塔获取交易信息 根据更新时间抓取订单
        JdpTbTradeQuery jdpTbTradeQuery2 = new JdpTbTradeQuery();
        jdpTbTradeQuery2.setSellerNick(shopBean.getSellerNick());
        jdpTbTradeQuery2.setStartJdpModified(shopBean.getFetchOrderStartDate());
        jdpTbTradeQuery2.setEndJdpModified(shopBean.getFetchOrderEndDate());
        jdpTbTradeQuery2.setStatus(shopBean.getOrderStatus());

        // 获得聚石塔订单信息
        return jdpTbTradeService.findJdpTbTradeByJdpTbTradeQuery(jdpTbTradeQuery2);
    }

    /**
     * 根据创建时间抓取聚石塔订单
     * @param shopBean
     * @return
     */
    private List<JdpTbTrade> findJdpTbTradesByCd(ShopBean shopBean) {
        // 从聚石塔获取交易信息  根据创建时间抓取订单
        JdpTbTradeQuery jdpTbTradeQuery1 = new JdpTbTradeQuery();
        jdpTbTradeQuery1.setSellerNick(shopBean.getSellerNick());
        jdpTbTradeQuery1.setStartJdpCreated(shopBean.getFetchOrderStartDate());
        jdpTbTradeQuery1.setEndJdpCreated(shopBean.getFetchOrderEndDate());
        jdpTbTradeQuery1.setStatus(shopBean.getOrderStatus());

        // 获得聚石塔订单信息
        return jdpTbTradeService.findJdpTbTradeByJdpTbTradeQuery(jdpTbTradeQuery1);
    }


    @Override
    public OriginalOrder fetchOrderById(ShopBean shopBean,String platformOrderNo) throws Exception {

        // 保存淘宝交易信息的集合
        List<Trade> tradeList = new ArrayList<Trade>();

        // 首先通过聚石塔获取
        // 从聚石塔获取交易信息  根据创建时间抓取订单
        JdpTbTradeQuery jdpTbTradeQuery = new JdpTbTradeQuery();
        jdpTbTradeQuery.setTid(Long.valueOf(platformOrderNo));
        // 获得聚石塔订单信息
        List<JdpTbTrade> jdpTbTradeList = jdpTbTradeService.findJdpTbTradeByJdpTbTradeQuery(jdpTbTradeQuery);

        JdpTbTrade jdpTbTrade = CollectionUtils.isNotEmpty(jdpTbTradeList) ? jdpTbTradeList.get(0) : null;

        if(jdpTbTrade == null){
            // 通过API获取
            TbTradeApi tbTradeApi = new TbTradeApi(shopBean.getSessionKey());

            Map<String,Object> tradeArgsMap = new HashMap<String, Object>();
            tradeArgsMap.put(ConstantTaoBao.FIELDS,getTradeFields());
            tradeArgsMap.put(ConstantTaoBao.TID,Long.valueOf(platformOrderNo));

            TradeFullinfoGetResponse tradeFullinfoGetResponse = tbTradeApi.tradeFullinfoGet(tradeArgsMap);
            if(tradeFullinfoGetResponse == null){
                throw new TaoBaoApiException("tradeFullinfoGetResponse="+tradeFullinfoGetResponse+";可能由于网络问题导致!");
            }

            if(StringUtils.isNotBlank(tradeFullinfoGetResponse.getErrorCode())){
                throw new TaoBaoApiException(tradeFullinfoGetResponse.getBody());
            }
            tradeList.add(tradeFullinfoGetResponse.getTrade());
        }
        else{
            TradeFullinfoGetResponse rsp = TaobaoUtils.parseResponse(
                    jdpTbTrade.getJdpResponse(), TradeFullinfoGetResponse.class);
            if(StringUtils.isNotBlank(rsp.getErrorCode())){
                throw new TaoBaoApiException(rsp.getBody());
            }
            if(rsp.getTrade() != null){
                tradeList.add(rsp.getTrade());
            }
        }

        List<OriginalOrder> originalOrderList = getOriginalOrders(tradeList,shopBean);

        OriginalOrder originalOrder = CollectionUtils.isNotEmpty(originalOrderList) ? originalOrderList.get(0) : null;

        if(originalOrder != null) {
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
            for(String platformOrderNo : platformOrderNoList){
                OriginalOrder originalOrder = fetchOrderById(shopBean,platformOrderNo);
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
