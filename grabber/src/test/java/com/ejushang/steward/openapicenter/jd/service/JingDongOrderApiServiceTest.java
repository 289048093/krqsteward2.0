package com.ejushang.steward.openapicenter.jd.service;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.openapicenter.BaseTest;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.FetchDataType;
import com.ejushang.steward.ordercenter.constant.JdOrderStatus;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.OrderFetch;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.ShopAuth;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.ShopService;
import com.ejushang.steward.ordercenter.service.api.impl.jd.JingDongOrderApiService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 14-4-19
 * Time: 上午11:48
 */
public class JingDongOrderApiServiceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(JingDongOrderApiServiceTest.class);

    @Autowired
    private JingDongOrderApiService jingDongOrderApiService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private OrderFetchService orderFetchService;

    @Test
    @Rollback(false)
    public void testFetchOrderByApi() throws Exception {
        Shop shop = shopService.getById(2);
        ShopAuth shopAuth = shopService.getShopAuthById(shop.getShopAuthId());
        ShopBean shopBean = new ShopBean();
        shopBean.setShopId(shop.getId());
        shopBean.setOutShopId(shop.getOutShopId());
        shopBean.setTitle(shop.getTitle());
        shopBean.setUserId(shopAuth.getUserId());
        shopBean.setSellerNick(shop.getNick());
        shopBean.setSessionKey(shopAuth.getSessionKey());
        shopBean.setRefreshToken(shopAuth.getRefreshToken());
        shopBean.setPlatformType(shop.getPlatformType());
        shopBean.setOrderStatus(JdOrderStatus.WAIT_SELLER_STOCK_OUT.toString()+"," + JdOrderStatus.WAIT_GOODS_RECEIVE_CONFIRM.toString()+","+JdOrderStatus.FINISHED_L.toString());


        // 查询京东平台的最后抓单记录
        OrderFetch orderFetch = orderFetchService.findLastOrderFetch(PlatformType.JING_DONG,shopBean.getShopId(), FetchDataType.FETCH_ORDER);
        Date fetchTime = orderFetch == null ? null : orderFetch.getFetchTime();
        if(log.isInfoEnabled()){
            log.info("京东API抓取订单:最后抓取时间为："+fetchTime);
        }

        // 获取订单抓取开始和结束时间
        Date startDate = getStartDate(fetchTime);
        Date endDate = getEndDate(startDate);

        shopBean.setFetchOrderStartDate(startDate);
        shopBean.setFetchOrderEndDate(endDate);

        shopBean.setFetchOrderStartDate(EJSDateUtils.parseDate("2014-06-18 16:43:07", EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        shopBean.setFetchOrderEndDate(EJSDateUtils.parseDate("2014-06-18 17:08:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR));

        List<OriginalOrder> originalOrderList = jingDongOrderApiService.fetchOrderByApi(shopBean);

        for(OriginalOrder originalOrder : originalOrderList){
            if(StringUtils.equalsIgnoreCase(originalOrder.getPlatformOrderNo(),"1561491721")){
                System.out.println("");
            }
            System.out.println("订单号："+originalOrder.getPlatformOrderNo()+"; 更新时间："+EJSDateUtils.formatDate(originalOrder.getModifiedTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }

    }

    @Test
    @Rollback(false)
    public void test(){
        System.out.println(".........");
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
                        + ConstantJingDong.JD_FETCH_ORDER_TIME_INTERVAL_INCREMENT+"秒，订单抓取结束时间："+end);
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

}
