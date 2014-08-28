package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.service.taobao.JdpTbTradeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 2014/5/13
 * Time: 17:37
 */
public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Autowired
    private JdpTbTradeService jdpTbTradeService;

    @Test
    @Rollback(false)
    public void testGetShopAndAuth(){

        List<Shop> shopList = shopService.getShopAndAuth(null);


    }

    @Test
    public void test(){
        Shop shop = new Shop();

        List<Shop> shopList = jdpTbTradeService.findShopByShopQuery(shop);

        Assert.assertNotNull(shopList);
    }

    @Test
    public void test2(){

    }

}
