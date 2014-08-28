package com.ejushang.steward.ordercenter.shopService;

import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.domain.OrderFetch;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.ShopAuth;
import com.ejushang.steward.ordercenter.service.ShopService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: Shiro
 * Date: 14-4-18
 * Time: 下午4:33
 */
public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testGetShop() {
        Shop shop = new Shop();
        List<Shop> list = shopService.getShop(shop);
        System.out.println(new JsonResult(true).addList(list));
    }

    @Test
    public void testSaveGetShop() {
        Shop shop = new Shop();
        shop.setTitle("易居尚官方旗舰店");
        shop.setNick("易居尚");
        shop.setOutShopId("123");
        shopService.saveShop(shop);
    }

    @Test
    public void testDeleteShop() {
        shopService.deleteShop(2);
    }

    @Test
    public void testSaveShopAuth() {
        ShopAuth shopAuth = new ShopAuth();
        shopAuth.setId(1);
        shopAuth.setSessionKey("wqfasdf");
        shopAuth.setRefreshToken("sadfasdf");
        shopAuth.setUserId("12");
        shopService.saveShopAuth(shopAuth);
    }

}
