package com.ejushang.steward.ordercenter.orderfetchservice;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.FetchDataType;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.OrderFetch;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.ShopService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * User: Shiro
 * Date: 14-4-9
 * Time: 上午10:28
 */
public class OrderFetchServiceTest extends BaseTest {
    @Autowired
    private OrderFetchService orderFetchService;

    @Autowired
    private ShopService shopService;

    @Test
    public void testFindAll() {
        Page page = new Page(1, 10);
        List<OrderFetch> list = orderFetchService.findAll(page);
        System.out.println(new JsonResult(true).addList(list));
    }

    @Test
    @Transactional
    public void testSave() {
        OrderFetch orderFetch = new OrderFetch();
        orderFetch.setCreateTime(new Date());
        orderFetch.setFetchTime(new Date());
        orderFetch.setPlatformType(PlatformType.JING_DONG);
        Shop shop = shopService.getById(1);
        orderFetch.setShop(shop);
        orderFetchService.save(orderFetch);
    }

    @Test
    public void testGetLast() {
        System.out.print(orderFetchService.findLastOrderFetch(PlatformType.JING_DONG, 7, FetchDataType.FETCH_ORDER).getPlatformType());
    }

    @Test
    public void testFindLastOrderFetch(){
        System.out.print(orderFetchService.findLastOrderFetch(PlatformType.TAO_BAO,1,FetchDataType.FETCH_ORDER));
    }
}

