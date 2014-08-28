package com.ejushang.steward.openapicenter.jd.service;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.openapicenter.BaseTest;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.OrderFetch;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.domain.OriginalRefund;
import com.ejushang.steward.ordercenter.domain.OriginalRefundFetch;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.OriginalRefundService;
import com.ejushang.steward.ordercenter.service.api.IOrderApiService;
import com.ejushang.steward.ordercenter.service.api.IRefundApiService;
import com.ejushang.steward.ordercenter.service.api.ShopBeanService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

/**
 * User: Baron.Zhang
 * Date: 2014/5/14
 * Time: 19:28
 */
public class JingDongRefundApiServiceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(JingDongRefundApiServiceTest.class);

    private static final String taoBaoRefundApiService = "taoBaoRefundApiService";
    private static final String taoBaoOrderApiService = "taoBaoOrderApiService";
    private static final String jingDongRefundApiService = "jingDongRefundApiService";
    private static final String jingDongOrderApiService = "jingDongOrderApiService";

    @Autowired
    private Map<String,IRefundApiService> refundApiServiceMap;

    @Autowired
    private Map<String,IOrderApiService> orderApiServiceMap;

    @Autowired
    private ShopBeanService shopBeanService;

    @Autowired
    private OrderFetchService orderFetchService;

    @Autowired
    private OriginalRefundService originalRefundService;



    @Test
    @Rollback(false)
    public void testFetchRefundByApi(){
        // 保存原始退款单的集合
        List<OriginalRefund> allOriginalRefundList = new ArrayList<OriginalRefund>();
        // 保存原始订单的集合
        List<OriginalOrder> allOriginalOrderList = new ArrayList<OriginalOrder>();
        // 获取所有店铺信息
        List<ShopBean> shopBeanList = shopBeanService.findShopBean();
        for (ShopBean shopBean : shopBeanList) {
            // 设置抓取条件信息
            assignShopBean(shopBean);

            // 测试条件测试代码
            if (!StringUtils.equalsIgnoreCase(shopBean.getPlatformType().toString(), PlatformType.JING_DONG.toString())){
                continue;
            }

            if (StringUtils.equalsIgnoreCase(shopBean.getPlatformType().toString(), PlatformType.JING_DONG.toString())) {
                if(log.isInfoEnabled()){
                    log.info("京东抓取退款单开始：店铺：【{}】，抓取开始时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchRefundStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
                List<OriginalRefund> originalRefundList = null;
                try {
                    originalRefundList = refundApiServiceMap.get(jingDongRefundApiService).fetchRefundByApi(shopBean);
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("京东抓取退款单出现异常，{}",e.getMessage());
                    }
                }
                if (CollectionUtils.isNotEmpty(originalRefundList)) {
                    allOriginalRefundList.addAll(originalRefundList);
                }
                if(log.isInfoEnabled()){
                    log.info("京东抓取退款单结束：店铺：【{}】，抓取结束时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchRefundEndDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }

                if(log.isInfoEnabled()){
                    log.info("京东抓取退货单开始：店铺：【{}】，抓取开始时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchReturnStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
                List<OriginalRefund> originalReturnList = null;
                try {
                    originalReturnList = refundApiServiceMap.get(jingDongRefundApiService).fetchReturnByApi(shopBean);
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("京东抓取退货单出现异常，{}",e.getMessage());
                    }
                }
                if (CollectionUtils.isNotEmpty(originalReturnList)) {
                    allOriginalRefundList.addAll(originalReturnList);
                }
                if(log.isInfoEnabled()){
                    log.info("京东抓取退货单结束：店铺：【{}】，抓取结束时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchReturnEndDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }

                if(log.isInfoEnabled()){
                    log.info("京东抓取订单开始：店铺：【{}】，抓取开始时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchOrderStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
                List<OriginalOrder> originalOrderList = null;
                try {
                    originalOrderList = orderApiServiceMap.get(jingDongOrderApiService).fetchOrderByApi(shopBean);
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("京东抓取订单出现异常，{}",e.getMessage());
                    }
                }
                if (CollectionUtils.isNotEmpty(originalOrderList)) {
                    allOriginalOrderList.addAll(originalOrderList);
                }
                if(log.isInfoEnabled()){
                    log.info("京东抓取订单结束：店铺：【{}】，抓取结束时间：{}",shopBean.getTitle()
                            ,EJSDateUtils.formatDate(shopBean.getFetchOrderEndDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
            }else if (StringUtils.equalsIgnoreCase(shopBean.getPlatformType().toString(), PlatformType.TAO_BAO.toString())
                    || StringUtils.equalsIgnoreCase(shopBean.getPlatformType().toString(), PlatformType.TAO_BAO_2.toString())) {

                if(log.isInfoEnabled()){
                    log.info("淘宝抓取退款单开始：店铺：【{}】，抓取开始时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchRefundStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
                List<OriginalRefund> originalRefundList = null;
                try {
                    originalRefundList = refundApiServiceMap.get(taoBaoRefundApiService).fetchRefundByDeploy(shopBean);
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("淘宝抓取退款单出现异常，{}",e.getMessage());
                    }
                }
                if (CollectionUtils.isNotEmpty(originalRefundList)) {
                    allOriginalRefundList.addAll(originalRefundList);
                }
                if(log.isInfoEnabled()){
                    log.info("淘宝抓取退款单结束：店铺：【{}】，抓取结束时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchRefundEndDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }

                if(log.isInfoEnabled()){
                    log.info("淘宝抓取退货单开始：店铺：【{}】，抓取开始时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchReturnStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
                List<OriginalRefund> originalReturnList = null;
                try {
                    originalReturnList = refundApiServiceMap.get(taoBaoRefundApiService).fetchReturnByDeploy(shopBean);
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("淘宝抓取退货单出现异常，{}",e.getMessage());
                    }
                }
                if (CollectionUtils.isNotEmpty(originalReturnList)) {
                    allOriginalRefundList.addAll(originalReturnList);
                }
                if(log.isInfoEnabled()){
                    log.info("淘宝抓取退货单结束：店铺：【{}】，抓取结束时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchReturnEndDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }


                if(log.isInfoEnabled()){
                    log.info("淘宝抓取订单开始：店铺：【{}】，抓取开始时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchOrderStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
                List<OriginalOrder> originalOrderList = null;
                try {
                    originalOrderList = orderApiServiceMap.get(taoBaoOrderApiService).fetchOrderByDeploy(shopBean);
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("淘宝抓取订单出现异常，{}",e.getMessage());
                    }
                }
                if (CollectionUtils.isNotEmpty(originalOrderList)) {
                    allOriginalOrderList.addAll(originalOrderList);
                }
                if(log.isInfoEnabled()){
                    log.info("淘宝抓取订单结束：店铺：【{}】，抓取结束时间：{}",shopBean.getTitle()
                            , EJSDateUtils.formatDate(shopBean.getFetchOrderEndDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
            }
        }

        int count = 0;
        Map<String,Long> countMap = new HashMap<String, Long>();
        for(OriginalOrder originalOrder : allOriginalOrderList){
            if(!(StringUtils.equalsIgnoreCase(OriginalOrderStatus.WAIT_SELLER_SEND_GOODS.toString(),originalOrder.getStatus())
                    || StringUtils.equalsIgnoreCase(OriginalOrderStatus.WAIT_BUYER_CONFIRM_GOODS.toString(),originalOrder.getStatus())
                    || StringUtils.equalsIgnoreCase(OriginalOrderStatus.TRADE_FINISHED.toString(),originalOrder.getStatus()))){
                System.out.println(originalOrder.getPlatformOrderNo()+":" + originalOrder.getPlatformType().toString()+"," + originalOrder.getStatus());
                count++;
            }

            if(countMap.containsKey(originalOrder.getPlatformOrderNo())){
                countMap.put(originalOrder.getPlatformOrderNo(),(countMap.get(originalOrder.getPlatformOrderNo())+1));
            }
            else{
                countMap.put(originalOrder.getPlatformOrderNo(),1L);
            }
        }

        for(Map.Entry<String,Long> entry : countMap.entrySet())
        {
            if(entry.getValue() > 1){
                System.out.println("===================="+entry.getKey()+":"+entry.getValue());
            }
            else{
                System.out.println(entry.getKey()+":"+entry.getValue());
            }
        }
        System.out.println(count);
        System.out.println(allOriginalRefundList.size());
    }

    /**
     * 设置抓取条件信息，如抓取时间、抓取状态等
     * @param shopBean
     */
    private void assignShopBean(ShopBean shopBean) {
        // 店铺最新订单抓取日志
        OrderFetch orderFetch = null;
        // 店铺最新退款单抓取日志
        OriginalRefundFetch originalRefundFetch = null;
        // 店铺最后订单抓取日期
        Date lastFetchOrderTime = null;
        // 店铺最后退款单抓取日期
        Date lastFetchRefundTime = null;
        // 店铺最后退货单抓取日期
        Date lastFetchReturnTime = null;
        // 订单状态
        String orderStatus = null;
        // 退款单状态
        String refundStatus = null;
        // 退款单状态
        String returnStatus = null;
        if (StringUtils.equalsIgnoreCase(shopBean.getPlatformType().toString(), PlatformType.JING_DONG.toString())) {
            // 获取订单最后抓单时间
            // 查询京东平台的最后抓取订单记录
            orderFetch = orderFetchService.findLastOrderFetch(PlatformType.JING_DONG, shopBean.getShopId(), FetchDataType.FETCH_ORDER);
            lastFetchOrderTime = orderFetch == null ? null : orderFetch.getFetchTime();
            if (log.isInfoEnabled()) {
                log.info("京东API抓取订单:最后抓取时间为：{}", lastFetchOrderTime);
            }
            orderStatus = JdOrderStatus.WAIT_SELLER_STOCK_OUT.toString()
                    +","+ JdOrderStatus.WAIT_GOODS_RECEIVE_CONFIRM.toString()
                    +","+ JdOrderStatus.FINISHED_L.toString()
            ;
            // 查询京东平台的最后抓取退款单记录
            orderFetch = orderFetchService.findLastOrderFetch(PlatformType.JING_DONG, shopBean.getShopId(),FetchDataType.FETCH_REFUND);
            lastFetchRefundTime = orderFetch == null ? null : orderFetch.getFetchTime();
            if (log.isInfoEnabled()) {
                log.info("京东API抓取退款单:最后抓取时间为：{}", lastFetchRefundTime);
            }
            //refundStatus = "";

            // 查询京东平台的最后抓取退货单记录
            orderFetch = orderFetchService.findLastOrderFetch(PlatformType.JING_DONG, shopBean.getShopId(),FetchDataType.FETCH_RETURN);
            lastFetchReturnTime = orderFetch == null ? null : orderFetch.getFetchTime();
            if (log.isInfoEnabled()) {
                log.info("京东API抓取退货单:最后抓取时间为：{}" , lastFetchReturnTime);
            }
        } else if (StringUtils.equalsIgnoreCase(shopBean.getPlatformType().toString(), PlatformType.TAO_BAO.toString())) {
            // 获取订单最后抓单时间
            // 查询淘宝平台的最后抓取订单记录
            orderFetch = orderFetchService.findLastOrderFetch(PlatformType.TAO_BAO, shopBean.getShopId(), FetchDataType.FETCH_ORDER);
            lastFetchOrderTime = orderFetch == null ? null : orderFetch.getFetchTime();
            if (log.isInfoEnabled()) {
                log.info("淘宝API抓取订单:最后抓取时间为：" + lastFetchOrderTime);
            }
            orderStatus = TbOrderStatus.WAIT_SELLER_SEND_GOODS.toString()
                    +","+ TbOrderStatus.WAIT_BUYER_CONFIRM_GOODS.toString()
                    +","+ TbOrderStatus.TRADE_FINISHED.toString();
            // 查询淘宝平台的最后抓取订单记录
            orderFetch = orderFetchService.findLastOrderFetch(PlatformType.TAO_BAO, shopBean.getShopId(), FetchDataType.FETCH_REFUND);
            lastFetchRefundTime = orderFetch == null ? null : orderFetch.getFetchTime();
            if (log.isInfoEnabled()) {
                log.info("淘宝API抓取退款单:最后抓取时间为：" + lastFetchRefundTime);
            }
            //refundStatus = "";

            // 查询淘宝平台的最后抓取订单记录
            orderFetch = orderFetchService.findLastOrderFetch(PlatformType.TAO_BAO, shopBean.getShopId(), FetchDataType.FETCH_RETURN);
            lastFetchReturnTime = orderFetch == null ? null : orderFetch.getFetchTime();
            if (log.isInfoEnabled()) {
                log.info("淘宝API抓取退货单:最后抓取时间为：" + lastFetchReturnTime);
            }

        } else if (StringUtils.equalsIgnoreCase(shopBean.getPlatformType().toString(), PlatformType.TAO_BAO_2.toString())) {
            // 获取订单最后抓单时间
            // 查询淘宝平台的最后抓取订单记录
            orderFetch = orderFetchService.findLastOrderFetch(PlatformType.TAO_BAO_2, shopBean.getShopId(), FetchDataType.FETCH_ORDER);
            lastFetchOrderTime = orderFetch == null ? null : orderFetch.getFetchTime();
            if (log.isInfoEnabled()) {
                log.info("淘宝API抓取订单:最后抓取时间为：" + lastFetchOrderTime);
            }
            orderStatus = TbOrderStatus.WAIT_SELLER_SEND_GOODS.toString()
                    +","+ TbOrderStatus.WAIT_BUYER_CONFIRM_GOODS.toString()
                    +","+ TbOrderStatus.TRADE_FINISHED.toString();
            // 查询淘宝平台的最后抓取订单记录
            orderFetch = orderFetchService.findLastOrderFetch(PlatformType.TAO_BAO_2, shopBean.getShopId(), FetchDataType.FETCH_REFUND);
            lastFetchRefundTime = orderFetch == null ? null : orderFetch.getFetchTime();
            if (log.isInfoEnabled()) {
                log.info("淘宝API抓取退款单:最后抓取时间为：" + lastFetchRefundTime);
            }
            //refundStatus = "";

            // 查询淘宝平台的最后抓取订单记录
            orderFetch = orderFetchService.findLastOrderFetch(PlatformType.TAO_BAO_2, shopBean.getShopId(), FetchDataType.FETCH_RETURN);
            lastFetchReturnTime = orderFetch == null ? null : orderFetch.getFetchTime();
            if (log.isInfoEnabled()) {
                log.info("淘宝API抓取退货单:最后抓取时间为：" + lastFetchReturnTime);
            }

        }


        // 获取订单抓取开始和结束时间
        Date startFetchOrderDate = getStartDate(lastFetchOrderTime);
        Date endFetchOrderDate = getEndDate(startFetchOrderDate);

        // 测试时间设置
        startFetchOrderDate = EJSDateUtils.parseDate("2014-06-16 00:00:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
        endFetchOrderDate = EJSDateUtils.parseDate("2014-06-21 23:59:59", EJSDateUtils.DateFormatType.DATE_FORMAT_STR);

        // 设置订单抓取开始日期和结束日期
        shopBean.setFetchOrderStartDate(startFetchOrderDate);
        shopBean.setFetchOrderEndDate(endFetchOrderDate);

        // 获取退款单抓取开始和结束时间
        Date startFetchRefundDate = getStartDate(lastFetchRefundTime);
        Date endFetchRefundDate = getEndDate(startFetchRefundDate);
        // 测试时间设置
        //startFetchRefundDate = EJSDateUtils.parseDate("2014-06-16 00:00:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
        //endFetchRefundDate = EJSDateUtils.parseDate("2014-06-22 23:59:59", EJSDateUtils.DateFormatType.DATE_FORMAT_STR);

        // 设置退款单抓取开始日期和结束日期
        shopBean.setFetchRefundStartDate(startFetchRefundDate);
        shopBean.setFetchRefundEndDate(endFetchRefundDate);

        // 获取退货单抓取开始和结束日期
        Date startFetchReturnDate = getStartDate(lastFetchReturnTime);
        Date endFetchReturnDate = getEndDate(startFetchReturnDate);
        // 测试时间设置
        //startFetchReturnDate = EJSDateUtils.parseDate("2014-06-05 16:24:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
        //endFetchReturnDate = EJSDateUtils.parseDate("2014-06-05 16:24:59", EJSDateUtils.DateFormatType.DATE_FORMAT_STR);

        // 设置退货单抓取开始日期和结束日期
        shopBean.setFetchReturnStartDate(startFetchReturnDate);
        shopBean.setFetchReturnEndDate(endFetchReturnDate);



        // 设置查询订单状态
        shopBean.setOrderStatus(orderStatus);
        // 设置查询退款单状态
        shopBean.setRefundStatus(refundStatus);
        // 设置查询退货单状态
        shopBean.setReturnStatus(returnStatus);
    }

    /**
     * 获取订单抓取结束时间
     * @param start
     * @return
     */
    private Date getEndDate(Date start) {
        Date end;// 订单抓取开始时间相对于当前时间少于30Min
        if(DateUtils.addSeconds(start, ConstantJingDong.JD_FETCH_ORDER_TIME_INTERVAL_INCREMENT).compareTo(EJSDateUtils.getCurrentDate()) > 0){
            end = EJSDateUtils.getCurrentDate();
            if(log.isInfoEnabled()){
                log.info("京东API抓取订单:抓取开始时间为："+start+ "，当前时间为："+EJSDateUtils.getCurrentDate()+"，二者相差少于"
                        +ConstantJingDong.JD_FETCH_ORDER_TIME_INTERVAL_INCREMENT+"秒，订单抓取结束时间："+end);
            }
        }
        else{
            // 开始时间相对于当前时间超过30Min
            end = DateUtils.addSeconds(start, ConstantJingDong.JD_FETCH_ORDER_TIME_INTERVAL_INCREMENT);
            log.info("京东API抓取订单:抓取开始时间为："+start+ "，当前时间为："+EJSDateUtils.getCurrentDate()+"，二者相差超过"
                    +ConstantJingDong.JD_FETCH_ORDER_TIME_INTERVAL_INCREMENT+"秒，订单抓取结束时间："+end);
        }
        return end;
    }

    /**
     * 获取订单抓取开始时间
     * @param fetchTime
     * @return
     */
    private Date getStartDate(Date fetchTime) {
        Date start;
        if(fetchTime == null){ // 没有抓取记录
            // 当前时间往前推N秒
            start = DateUtils.addSeconds(EJSDateUtils.getCurrentDate(), -ConstantJingDong.JD_FETCH_ORDER_TIME_INTERVAL);
            if(log.isInfoEnabled()){
                log.info("京东API抓取订单:没有抓取到历史记录，从当前"+start+"开始抓取：：：");
            }
        }
        else{
            // 将抓取订单时间往前推N秒
            start = DateUtils.addSeconds(fetchTime, ConstantJingDong.JD_FETCH_ORDER_TIME_DELAY);
            if(log.isInfoEnabled()){
                log.info("京东API抓取订单:最后抓取时间为"+fetchTime
                        +"，往后推"+ConstantJingDong.JD_FETCH_ORDER_TIME_DELAY+"秒，从"+start+"开始抓取：：：");
            }
        }
        return start;
    }

    @Test
    public void test(){
        Long l = new BigDecimal("3980").longValue();
        System.out.println(l);
    }
}
