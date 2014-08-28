package com.ejushang.steward.grabber;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.message.MessageHandler;
import com.ejushang.steward.message.MessageHolder;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.ejushang.steward.ordercenter.bean.FetchConditionBean;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.keygenerator.SystemConfConstant;
import com.ejushang.steward.ordercenter.service.ConfService;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import com.ejushang.steward.ordercenter.service.OriginalRefundService;
import com.ejushang.steward.ordercenter.service.api.*;
import com.ejushang.steward.ordercenter.service.api.impl.tb.TaoBaoOrderApiService;
import com.ejushang.steward.ordercenter.service.transportation.PlatformService;
import com.ejushang.steward.ordercenter.vo.OrderHandVo;
import com.ejushang.steward.rmi.impl.OriginalOrderRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 源订单抓取主线程
 * User: liubin
 * Date: 13-12-27
 */
@Component
public class MessageGrabber extends MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageGrabber.class);

    @Autowired
    private MessageHolder messageHolder;

    @Autowired
    private ConfService confService;

    @Autowired
    private ShopBeanService shopBeanService;

    @Autowired
    private FetchOrderService fetchOrderService;

    @Autowired
    private FetchRefundService fetchRefundService;

    @Autowired
    private OriginalOrderRemoteService originalOrderRemoteService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private OriginalOrderService originalOrderService;

    @Autowired
    private OriginalRefundService originalRefundService;

    @Override
    public void doHandle(boolean firstTime){
        // 获取所有店铺信息
        List<ShopBean> shopBeanList = shopBeanService.findOnlineShopBean();
        // 获取所有抓取数据类型
        List<FetchDataType> fetchDataTypeList = Arrays.asList(FetchDataType.values());
        // 操作类型设置为auto
        FetchOptType fetchOptType = FetchOptType.AUTO;
        for (ShopBean shopBean : shopBeanList) {
            setterShopBean(fetchDataTypeList, fetchOptType, shopBean,null,null);
        }
        // 获取数据，并添加至分析队列
        assignDatas(shopBeanList);

        // 获取队列中的抓取数据的参数
        List<Map<String,String>> fetchDataParamMapList = originalOrderRemoteService.getQueueFetchDataParamMaps();
        if(CollectionUtils.isNotEmpty(fetchDataParamMapList)){
            for(Map<String,String> paramMap : fetchDataParamMapList){
                FetchConditionBean fetchConditionBean = getFetchConditionBean(paramMap);
                // 手动抓单
                fetchByManual(fetchConditionBean);
            }
        }
        /*// 抓取指定日期内的订单
        fetchBySpecTime(shopBeanList, fetchDataTypeList);*/
    }

    /**
     * 手动抓单
     * @param fetchConditionBean
     */
    private void fetchByManual(FetchConditionBean fetchConditionBean) {

        List<OriginalOrder> allOriginalOrderList = new ArrayList<OriginalOrder>();

        // 判断是根据时间段查询，还是根据订单号查询。
        if(StringUtils.equalsIgnoreCase(fetchConditionBean.getFetchByTypeName(), FetchByType.FETCH_BY_DATE.getName())){
            if(log.isInfoEnabled()){
                log.info("根据时间段查询数据：开始查询");
            }
            // 抓取日期获取并校验
            Date startDate = fetchConditionBean.getFetchStartDate();
            Date endDate = fetchConditionBean.getFetchEndDate();
            if(endDate == null ||
                    EJSDateUtils.isNew(endDate, EJSDateUtils.getCurrentDate())){
                endDate = EJSDateUtils.getCurrentDate();
            }
            // 设置每一个店铺的抓单关键参数
            List<ShopBean> shopBeanList = getShopBeans(fetchConditionBean, startDate, endDate);

            // 抓取订单
            List<OriginalOrder> originalOrderList = fetchOrderService.fetchOrdersByShopBeans(shopBeanList);
            if(CollectionUtils.isNotEmpty(originalOrderList)){
                allOriginalOrderList.addAll(originalOrderList);
            }
            // 抓取退款单
            //List<OriginalRefund> originalRefundList = fetchRefundService.fetchRefundsByShopBeans(shopBeanList);
            // 抓取退货单
            //List<OriginalRefund> originalReturnList = fetchRefundService.fetchReturnsByShopBeans(shopBeanList);

            // 保存原始订单至数据库
            originalOrderService.saveOriginalOrders(originalOrderList);
            // 保存原始退款单至数据库
            //originalRefundService.saveOriginalRefunds(originalRefundList);
            // 保存原始退货单至数据库
            //originalRefundService.saveOriginalRefunds(originalReturnList);
        }
        else{

            if(log.isInfoEnabled()){
                log.info("根据单号查询：开始查询");
            }
            // 根据单号查询 目前只查询订单
            List<ShopBean> shopBeanList = findShopBeans(fetchConditionBean);
            // 获取需要查询店铺
            ShopBean shopBeanOri = CollectionUtils.isNotEmpty(shopBeanList) ? shopBeanList.get(0) : null;

            String platformNos = StringUtils.trim(fetchConditionBean.getPlatformNos());
            String[] platformNoArr = platformNos.split(",");
            List<String> platformNoList = new ArrayList<String>();
            for(String platformNo : platformNoArr){
                platformNoList.add(StringUtils.trim(platformNo));
            }

            // 判断数据类型是否为抓取订单
            List<OriginalOrder> originalOrderList = new ArrayList<OriginalOrder>();
            if(StringUtils.equalsIgnoreCase(fetchConditionBean.getFetchDataTypeName(),FetchDataType.FETCH_ORDER.getName())){
                originalOrderList = fetchOrderService.fetchOrdersByIds(shopBeanOri, platformNoList);
                if(CollectionUtils.isNotEmpty(originalOrderList)){
                    allOriginalOrderList.addAll(originalOrderList);
                }
            }

            // 保存原始订单至数据库
            originalOrderService.saveOriginalOrders(originalOrderList);
        }

        // 将抓取到的原始订单添加至队列
        if(CollectionUtils.isNotEmpty(allOriginalOrderList)){
            messageHolder.addOrders(allOriginalOrderList);
        }
    }

    /**
     * 设置店铺的关键抓单参数
     * @param fetchConditionBean
     * @param startDate
     * @param endDate
     */
    private List<ShopBean> getShopBeans(FetchConditionBean fetchConditionBean, Date startDate, Date endDate) {
        // 根据条件查询所有需要抓单的店铺信息
        List<ShopBean> shopBeanList = findShopBeans(fetchConditionBean);
        // 构造每个店铺的抓取条件
        for(ShopBean shopBean : shopBeanList){
            // 获取并设置抓取的数据类型
            List<FetchDataType> fetchDataTypeList = getFetchDataTypeList(fetchConditionBean);
            FetchOptType fetchOptType = FetchOptType.HAND;
            // 这里设置抓取操作类型为 HAND
            shopBeanService.assignShopBean(startDate, endDate,fetchOptType,fetchDataTypeList, shopBean);
        }
        return shopBeanList;
    }

    /**
     * 获取抓单数据类型
     * @param fetchConditionBean
     * @return
     */
    private List<FetchDataType> getFetchDataTypeList(FetchConditionBean fetchConditionBean) {
        // 抓取数据类型设置
        List<FetchDataType> fetchDataTypeList = new ArrayList<FetchDataType>();
        if(StringUtils.isNotBlank(fetchConditionBean.getFetchDataTypeName())){
            fetchDataTypeList.add(FetchDataType.valueOf(fetchConditionBean.getFetchDataTypeName()));
        }
        else{
            fetchDataTypeList.addAll(Arrays.asList(FetchDataType.values()));
        }
        return fetchDataTypeList;
    }

    /**
     * 获取抓单店铺
     * @param fetchConditionBean
     * @return
     */
    private List<ShopBean> findShopBeans(FetchConditionBean fetchConditionBean) {
        // 构造查询店铺条件
        ShopBean shopBeanQuery = new ShopBean();
        if(fetchConditionBean.getPlatformId() != null){
            Platform platform = platformService.getById(fetchConditionBean.getPlatformId());
            if(platform != null) {
                shopBeanQuery.setPlatformType(PlatformType.valueOf(platform.getType()));
            }
        }
        shopBeanQuery.setShopId(fetchConditionBean.getShopId());
        // 查询要抓取记录的店铺及其授权信息
        return shopBeanService.findShopBean(shopBeanQuery);
    }

    /**
     * 将map参数转换为参数对象
     * @param paramMap
     * @return
     */
    private FetchConditionBean getFetchConditionBean(Map<String, String> paramMap) {
        FetchConditionBean fetchConditionBean = new FetchConditionBean();
        fetchConditionBean.setPlatformId(StringUtils.isBlank(paramMap.get("platformId")) ? null : Integer.valueOf(paramMap.get("platformId")));
        fetchConditionBean.setShopId(StringUtils.isBlank(paramMap.get("shopId")) ? null : Integer.valueOf(paramMap.get("shopId")));
        fetchConditionBean.setFetchStartDate(EJSDateUtils.parseDateForNull(paramMap.get("fetchStartDate"), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        fetchConditionBean.setFetchEndDate(EJSDateUtils.parseDateForNull(paramMap.get("fetchEndDate"), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        fetchConditionBean.setFetchByTypeName(paramMap.get("fetchByTypeName"));
        fetchConditionBean.setFetchDataTypeName(paramMap.get("fetchDataTypeName"));
        fetchConditionBean.setFetchDateTypeName(paramMap.get("fetchDateTypeName"));
        fetchConditionBean.setPlatformNos(paramMap.get("platformNos"));
        return fetchConditionBean;
    }

    /**
     * 获取数据，并添加至分析队列
     * @param shopBeanList
     */
    private void assignDatas(List<ShopBean> shopBeanList) {
        // 获取所有店铺的原始订单
        List<OriginalOrder> originalOrderList = fetchOrderService.fetchOrdersByShopBeans(shopBeanList);
        // 获取所有店铺的原始退款单
        List<OriginalRefund> originalRefundList = fetchRefundService.fetchRefundsByShopBeans(shopBeanList);
        // 获取所有店铺的原始退货单
        List<OriginalRefund> originalReturnList = fetchRefundService.fetchReturnsByShopBeans(shopBeanList);



        // 添加原始订单至分析队列
        if(CollectionUtils.isNotEmpty(originalOrderList)){
            messageHolder.addOrders(originalOrderList);
        }

        // 添加原始退款单至分析队列
        if(CollectionUtils.isNotEmpty(originalRefundList)){
            messageHolder.addRefunds(originalRefundList);
        }

        // 添加原始退货单至分析队列
        if(CollectionUtils.isNotEmpty(originalReturnList)){
            messageHolder.addRefunds(originalReturnList);
        }
    }

    /**
     * 抓取规定时间内的数据
     * @param shopBeanList
     * @param fetchDataTypeList
     */
    private void fetchBySpecTime(List<ShopBean> shopBeanList, List<FetchDataType> fetchDataTypeList) {
        // 抓取指定时间内的订单
        Date startDate = EJSDateUtils.parseDate("2014-07-04 20:20:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
        Date endDate = EJSDateUtils.parseDate("2014-07-05 15:00:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR);

        FetchOptType fetchOptTypeSpec = FetchOptType.MANUAL;
        for (ShopBean shopBean : shopBeanList) {
            setterShopBean(fetchDataTypeList, fetchOptTypeSpec, shopBean,startDate,endDate);
        }

        // 获取所有店铺的原始订单 并添加至分析队列
        assignDatas(shopBeanList);
    }

    /**
     * 设置店铺抓取参数
     * @param fetchDataTypeList
     * @param fetchOptType
     * @param shopBean
     * @param start
     * @param end
     */
    private void setterShopBean(List<FetchDataType> fetchDataTypeList, FetchOptType fetchOptType, ShopBean shopBean,Date start,Date end) {
        // 设置抓单操作类型
        shopBean.setFetchOptType(fetchOptType);
        // 设置抓单数据类型
        shopBean.setFetchDataTypeList(fetchDataTypeList);

        // 获取该店铺抓单的最后抓取时间
        Date lastFetchOrderDate = shopBeanService.getLastFetchOrderTime(shopBean);
        Date lastFetchRefundDate = shopBeanService.getLastFetchRefundTime(shopBean);
        Date lastFetchReturnDate = shopBeanService.getLastFetchReturnTime(shopBean);

        // 获取店铺的抓取订单开始及其结束时间
        Date fetchOrderStartDate = shopBeanService.getStartDate(start,lastFetchOrderDate);
        Date fetchOrderEndDate = shopBeanService.getEndDate(end, fetchOrderStartDate);
        shopBean.setFetchOrderStartDate(fetchOrderStartDate);
        shopBean.setFetchOrderEndDate(fetchOrderEndDate);

        // 获取店铺的抓取退款单单开始及其结束时间
        Date fetchRefundStartDate = shopBeanService.getStartDate(start,lastFetchRefundDate);
        Date fetchRefundEndDate = shopBeanService.getEndDate(end,fetchRefundStartDate);
        shopBean.setFetchRefundStartDate(fetchRefundStartDate);
        shopBean.setFetchRefundEndDate(fetchRefundEndDate);

        // 获取店铺的抓取退货单单开始及其结束时间
        Date fetchReturnStartDate = shopBeanService.getStartDate(start,lastFetchReturnDate);
        Date fetchReturnEndDate = shopBeanService.getEndDate(end,fetchReturnStartDate);
        shopBean.setFetchReturnStartDate(fetchReturnStartDate);
        shopBean.setFetchReturnEndDate(fetchReturnEndDate);

        // 设置不同平台抓取的订单状态，退款退货抓取所有状态不进行设置
        if(StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(), PlatformType.TAO_BAO.getName())
                || StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(),PlatformType.TAO_BAO_2.getName())){
            shopBean.setOrderStatus(shopBeanService.getTaoBaoOrderStatus());
        }
        else if(StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(),PlatformType.JING_DONG.getName())){
            shopBean.setOrderStatus(shopBeanService.getJingDongOrderStatus());
        }
    }



    @Override
    protected String getName() {
        return "消息抓取任务";
    }

    @Override
    protected int getExecuteIntervalInSeconds() {
        Integer value = confService.getConfIntegerValue(SystemConfConstant.MESSAGE_GRAB_INTERVAL);
        if(value == null) {
            log.warn("系统配置项[{}]取不到整数值", SystemConfConstant.MESSAGE_GRAB_INTERVAL);
            return super.getExecuteIntervalInSeconds();
        } else {
            return value;
        }
    }


}
