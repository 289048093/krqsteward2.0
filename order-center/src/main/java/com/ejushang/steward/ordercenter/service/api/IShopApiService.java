package com.ejushang.steward.ordercenter.service.api;


import com.ejushang.steward.ordercenter.bean.ShopAndAuthBean;
import com.ejushang.steward.ordercenter.domain.Shop;

/**
 * User: Baron.Zhang
 * Date: 14-4-9
 * Time: 下午3:19
 */
public interface IShopApiService {
    /**
     * 新增店铺
     * @return
     */
    void addShopAndAuth(ShopAndAuthBean shopAndAuthBean) throws Exception;

    /**
     * 更新店铺信息至外部平台
     * @param shop
     * @param sessionKey
     * @return
     * @throws Exception
     */
    Boolean updateShop(Shop shop,String sessionKey) throws Exception;
}
