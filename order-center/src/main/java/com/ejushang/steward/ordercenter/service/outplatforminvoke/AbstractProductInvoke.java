package com.ejushang.steward.ordercenter.service.outplatforminvoke;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.ShopAuth;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-6-3
 * Time: 上午10:57
 */
public abstract class AbstractProductInvoke implements ProductInvoke {

    @Autowired
    protected GeneralDAO generalDAO;

    protected ThreadLocal<Integer> shopThreadLocal = new ThreadLocal<Integer>();

    @Override
    public void setShopId(Integer shopId) {
        shopThreadLocal.set(shopId);
    }

    @Override
    public Integer getShopId() {
        return shopThreadLocal.get();
    }

    /**
     * 获取淘宝授权
     *
     * @return
     */
    protected ShopAuth getShopAuth() {
        if(getShopId()==null){
            throw new StewardBusinessException("产品同步请先设置店铺id");
        }
        Search search = new Search(Shop.class)
                .addFilterEqual("shopAuth.platformType", getType())
                .addFilterEqual("id",getShopId());
        Shop shop = (Shop) generalDAO.searchUnique(search);
        if (shop == null) {
            throw new StewardBusinessException(String.format("没有 %s 平台的授权店铺", getType().getValue()));
        }
        ShopAuth shopAuth = shop.getShopAuth();
        if (shopAuth == null) {
            throw new StewardBusinessException(String.format("没有 %s 店铺授权", getType().getValue()));
        }
        return shopAuth;
    }

}
