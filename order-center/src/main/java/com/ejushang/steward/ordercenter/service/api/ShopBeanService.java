package com.ejushang.steward.ordercenter.service.api;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.ejushang.steward.ordercenter.bean.FetchConditionBean;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.OrderFetch;
import com.ejushang.steward.ordercenter.domain.OriginalRefundFetch;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.ShopService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 2014/5/13
 * Time: 17:57
 */
@Service
@Transactional
public class ShopBeanService {

    private static final Logger log = LoggerFactory.getLogger(ShopBeanService.class);
    @Autowired
    private ShopService shopService;

    @Autowired
    private OrderFetchService orderFetchService;

    /**
     * 查询店铺信息、店铺授权信息
     * @return
     */
    @Transactional(readOnly = true)
    public List<ShopBean> findShopBean(){
        // 查询所有店铺及授权信息
        List<Shop> shopList = shopService.getShopAndAuth(null);
        return convertShopList2ShopBeanList(shopList);
    }

    /**
     * 查询店铺信息、店铺授权信息
     * @return
     */
    @Transactional(readOnly = true)
    public List<ShopBean> findOnlineShopBean(){
        // 查询所有店铺及授权信息
        List<Shop> shopList = shopService.getOnlineShopAndAuth(null);
        return convertShopList2ShopBeanList(shopList);
    }

    /**
     * 根据条件查询店铺信息、店铺授权信息
     * @param shopBean
     * @return
     */
    @Transactional(readOnly = true)
    public List<ShopBean> findShopBean(ShopBean shopBean){
        Shop shopQuery = new Shop();
        shopQuery.setPlatformType(shopBean.getPlatformType());
        shopQuery.setId(shopBean.getShopId());
        // 查询所有店铺及授权信息
        List<Shop> shopList = shopService.getShopAndAuth(shopQuery);
        return convertShopList2ShopBeanList(shopList);
    }

    /**
     * 批量将店铺及授权信息转换为shopBean
     * @param shopList
     * @return
     */
    private List<ShopBean> convertShopList2ShopBeanList(List<Shop> shopList){
        List<ShopBean> shopBeanList = new ArrayList<ShopBean>();
        if(CollectionUtils.isEmpty(shopList)){
            return shopBeanList;
        }

        ShopBean shopBean = null;
        for(Shop shop : shopList){
            shopBean = convertShop2ShopBean(shop);
            if(shopBean != null){
                shopBeanList.add(shopBean);
            }
        }

        return shopBeanList;
    }

    /**
     * 将店铺及授权信息转换为shopBean
     * @param shop
     * @return
     */
    private ShopBean convertShop2ShopBean(Shop shop) {
        ShopBean shopBean = null;
        if(shop == null){
            return shopBean;
        }
        shopBean = new ShopBean();
        shopBean.setShopId(shop.getId());
        shopBean.setOutShopId(shop.getOutShopId());
        shopBean.setTitle(shop.getTitle());
        shopBean.setSellerNick(shop.getNick());
        if(shop.getShopAuth() != null) {
            shopBean.setUserId(shop.getShopAuth().getUserId());
            shopBean.setSessionKey(shop.getShopAuth().getSessionKey());
            shopBean.setRefreshToken(shop.getShopAuth().getRefreshToken());
        }
        shopBean.setPlatformType(shop.getPlatformType());
        shopBean.setShopType(shop.getShopType());
        return shopBean;
    }

    /**
     * 获取订单抓取结束时间
     * @param start
     * @return
     */
    public Date getEndDate(Date start) {
        return getStartDate(null,start);
    }

    /**
     * 获取订单抓取结束时间
     * @return
     */
    public Date getEndDate(Date endDate,Date start) {
        Date end = DateUtils.addMinutes(EJSDateUtils.getCurrentDate(),-3); // 默认结束时间为当前时间往前推3分钟
        if(endDate != null){
            end = endDate;
        }
        if(DateUtils.addSeconds(start, ConstantJingDong.JD_FETCH_ORDER_TIME_INTERVAL_INCREMENT).compareTo(end) < 0){
            // 开始时间相对于抓单结束时间超过30Min
            end = DateUtils.addSeconds(start, ConstantJingDong.JD_FETCH_ORDER_TIME_INTERVAL_INCREMENT);
        }
        return end;
    }

    /**
     * 获取订单抓取开始时间
     * @param fetchTime
     * @return
     */
    public Date getStartDate(Date fetchTime) {
        return getStartDate(null,fetchTime);
    }

    /**
     * 获取订单抓取开始时间
     * @param fetchTime
     * @return
     */
    public Date getStartDate(Date startDate,Date fetchTime) {
        if(startDate != null){
            return startDate;
        }

        Date start;
        if(fetchTime == null){ // 没有抓取记录
            // 当前时间往前推N秒
            start = DateUtils.addSeconds(EJSDateUtils.getCurrentDate(), -ConstantJingDong.JD_FETCH_ORDER_TIME_INTERVAL);
        }
        else{
            // 将抓取订单时间往前推N秒
            start = DateUtils.addSeconds(fetchTime, ConstantJingDong.JD_FETCH_ORDER_TIME_DELAY);
        }

        if(EJSDateUtils.isNew(start,DateUtils.addMinutes(EJSDateUtils.getCurrentDate(),-3))){
            start = DateUtils.addMinutes(EJSDateUtils.getCurrentDate(),-3);
        }

        return start;
    }



    /**
     * 设置店铺的关键抓取参数
     * @param startDate
     * @param endDate
     * @param shopBean
     */
    public void assignShopBean(Date startDate, Date endDate,FetchOptType fetchOptType,List<FetchDataType> fetchDataTypeList, ShopBean shopBean) {
        // 获取抓取数据类型并设置
        shopBean.setFetchDataTypeList(fetchDataTypeList);
        // 抓取操作类型设置 手动抓单为HAND 注意：这步操作必须在抓取日期设置的前面
        shopBean.setFetchOptType(fetchOptType);
        // 1.抓取日期类型设置 目前我们的api都同时会抓取创建日期以及更新时期的记录，所以这个没有必要
        // 2.根据抓取数据类型设定抓取日期
        for(FetchDataType fetchDataType : shopBean.getFetchDataTypeList()){
            if(StringUtils.equalsIgnoreCase(fetchDataType.getName(), FetchDataType.FETCH_ORDER.getName())){
                // 设置抓取订单开始时间
                shopBean.setFetchOrderStartDate(startDate);
                // 设置抓取订单结束时间
                shopBean.setFetchOrderEndDate(endDate);
            }
            else if(StringUtils.equalsIgnoreCase(fetchDataType.getName(),FetchDataType.FETCH_REFUND.getName())){
                // 设置抓取退款单单开始时间
                shopBean.setFetchRefundStartDate(startDate);
                // 设置抓取退款单结束时间
                shopBean.setFetchRefundEndDate(endDate);
            }
            else if(StringUtils.equalsIgnoreCase(fetchDataType.getName(),FetchDataType.FETCH_RETURN.getName())){
                // 设置抓取退货单开始时间
                shopBean.setFetchReturnStartDate(startDate);
                // 设置抓取退货单结束时间
                shopBean.setFetchReturnEndDate(endDate);
            }
        }

        // 根据店铺平台设置不同的抓取状态
        if(StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(), PlatformType.TAO_BAO.getName())
                || StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(),PlatformType.TAO_BAO_2.getName())){
            // 设置淘宝订单状态
            shopBean.setOrderStatus(getTaoBaoOrderStatus());
            // 退款单：抓取所有状态
            // 退货单：抓取所有状态
        }
        else if(StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(),PlatformType.JING_DONG.getName())){
            // 设置京东订单状态
            shopBean.setOrderStatus(getJingDongOrderStatus());
            // 退款单：抓取所有状态
            // 退货单：抓取所有状态
        }
    }

    /**
     * 获得淘宝查询订单状态
     * @return
     */
    public String getTaoBaoOrderStatus(){
        return  TbOrderStatus.WAIT_SELLER_SEND_GOODS.toString()
                +","+ TbOrderStatus.WAIT_BUYER_CONFIRM_GOODS.toString()
                +","+ TbOrderStatus.TRADE_FINISHED.toString();
    }

    /**
     * 获得京东查询订单状态
     * @return
     */
    public String getJingDongOrderStatus(){
        return  JdOrderStatus.WAIT_SELLER_STOCK_OUT.toString()
                +","+ JdOrderStatus.WAIT_GOODS_RECEIVE_CONFIRM.toString()
                +","+ JdOrderStatus.FINISHED_L.toString();
    }

    /**
     * 获取订单的最后抓取时间
     * @param shopBean
     * @return
     */
    public Date getLastFetchOrderTime(ShopBean shopBean){
        OrderFetch orderFetch = orderFetchService.findLastOrderFetch(shopBean.getPlatformType(),shopBean.getShopId(),FetchDataType.FETCH_ORDER,shopBean.getFetchOptType());
        return orderFetch == null ? null : orderFetch.getFetchTime();
    }

    /**
     * 获取订单的最后抓取时间
     * @param shopBean
     * @return
     */
    public Date getLastFetchRefundTime(ShopBean shopBean){
        OrderFetch orderFetch = orderFetchService.findLastOrderFetch(shopBean.getPlatformType(),shopBean.getShopId(),FetchDataType.FETCH_REFUND,shopBean.getFetchOptType());
        return orderFetch == null ? null : orderFetch.getFetchTime();
    }

    /**
     * 获取订单的最后抓取时间
     * @param shopBean
     * @return
     */
    public Date getLastFetchReturnTime(ShopBean shopBean){
        OrderFetch orderFetch = orderFetchService.findLastOrderFetch(shopBean.getPlatformType(),shopBean.getShopId(),FetchDataType.FETCH_RETURN,shopBean.getFetchOptType());
        return orderFetch == null ? null : orderFetch.getFetchTime();
    }

}
