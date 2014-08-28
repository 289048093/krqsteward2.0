package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.openapicenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.FetchDataType;
import com.ejushang.steward.ordercenter.domain.OrderFetch;
import com.ejushang.steward.ordercenter.domain.Shop;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 2014/6/26
 * Time: 17:14
 */
public class OrderFetchServiceTest extends BaseTest {

    @Autowired
    private OrderFetchService orderFetchService;

    @Autowired
    private ShopService shopService;

    /**
     * 新增订单抓取记录
     */
    @Test
    @Rollback(false)
    public void testAddOrderFetch(){
        List<Shop> shopList = shopService.getShopAndAuth(null);

        Date fetchOrderDate = new Date();
        Date fetchRefundDate = new Date();
        Date fetchReturnDate = new Date();
        for(Shop shop : shopList){
            if(shop.getShopAuth() != null){
                // 说明当前店铺是已授权店铺
                OrderFetch orderFetch = new OrderFetch();
                orderFetch.setFetchTime(fetchOrderDate);
                orderFetch.setPlatformType(shop.getPlatformType());
                orderFetch.setShopId(shop.getId());
                orderFetch.setCreateTime(new Date());
                orderFetch.setFetchDataType(FetchDataType.FETCH_ORDER);

                OrderFetch refundFetch = new OrderFetch();
                refundFetch.setFetchTime(fetchRefundDate);
                refundFetch.setPlatformType(shop.getPlatformType());
                refundFetch.setShopId(shop.getId());
                refundFetch.setCreateTime(new Date());
                refundFetch.setFetchDataType(FetchDataType.FETCH_REFUND);

                OrderFetch returnFetch = new OrderFetch();
                returnFetch.setFetchTime(fetchReturnDate);
                returnFetch.setPlatformType(shop.getPlatformType());
                returnFetch.setShopId(shop.getId());
                returnFetch.setCreateTime(new Date());
                returnFetch.setFetchDataType(FetchDataType.FETCH_RETURN);

                orderFetchService.save(orderFetch);
                orderFetchService.save(refundFetch);
                orderFetchService.save(returnFetch);
            }
        }

    }

}
