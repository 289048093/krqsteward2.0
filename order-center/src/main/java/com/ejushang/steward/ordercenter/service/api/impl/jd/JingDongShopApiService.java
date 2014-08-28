package com.ejushang.steward.ordercenter.service.api.impl.jd;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.openapicenter.jd.api.JdShopApi;
import com.ejushang.steward.ordercenter.bean.ShopAndAuthBean;
import com.ejushang.steward.ordercenter.constant.ShopType;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.ShopAuth;
import com.ejushang.steward.ordercenter.service.ShopService;
import com.ejushang.steward.ordercenter.service.api.IShopApiService;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.seller.ShopSafService.ShopJosResult;
import com.jd.open.api.sdk.response.seller.VenderShopQueryResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 14-4-11
 * Time: 下午1:31
 */
@Service
@Transactional
public class JingDongShopApiService implements IShopApiService {

    @Autowired
    private ShopService shopService;

    @Override
    public void addShopAndAuth(ShopAndAuthBean shopAndAuthBean) throws JdException {
        // 初始化京东shopApi
        JdShopApi jdShopApi = new JdShopApi(shopAndAuthBean.getAccessToken());
        VenderShopQueryResponse response = jdShopApi.venderShopQuery();
        ShopJosResult shopJosResult = response.getShopJosResult();

        Shop shopQuery = new Shop();
        shopQuery.setUid(shopAndAuthBean.getUserId());
        shopQuery.setPlatformType(shopAndAuthBean.getPlatformType());
        Shop shop = shopService.getShopByConditionIncludeDelete(shopQuery);
        if(shop != null){
            // 将is_delete状态改为不启用
            shop.setIsDelete(false);
        }

        shop = shop == null ? new Shop() : shop;

        ShopAuth shopAuth = null;
        if(!NumberUtil.isNullOrZero(shop.getShopAuthId())){
            shopAuth = shopService.getShopAuthById(shop.getShopAuthId());
        }

        if(shopJosResult != null){
            // 店铺编号
            shop.setOutShopId(String.valueOf(shopJosResult.getShopId()));
            // 用户id
            shop.setUid(shopAndAuthBean.getUserId());
            // 买家昵称
            shop.setNick(shopAndAuthBean.getUserNick());
            // 店铺名称
            shop.setTitle(shopJosResult.getShopName());
            // 开店时间
            shop.setCreateTime(shopJosResult.getOpenTime());
            // logo地址
            shop.setPicPath(shopJosResult.getLogoUrl());
            // 店铺简介
            shop.setDescription(shopJosResult.getBrief());
            // 主营类目编号
            shop.setCatId(String.valueOf(shopJosResult.getCategoryMain()));
            // 最后修改时间
            shop.setUpdateTime(EJSDateUtils.getCurrentDate());
            // 平台
            shop.setPlatformType(shopAndAuthBean.getPlatformType());
            // 账号类型
            shop.setShopType(ShopType.SHOP);
        }
        else{
            // 当前账号为供应商时
            // 卖家昵称
            shop.setNick(shopAndAuthBean.getUserNick());
            // 卖家用户id
            shop.setUid(shopAndAuthBean.getUserId());
            // 平台
            shop.setPlatformType(shopAndAuthBean.getPlatformType());
            // 账号类型
            shop.setShopType(ShopType.VENDOR);
        }

        shopAuth = shopAuth == null ? new ShopAuth() : shopAuth;
        // 店铺对应的session key(即Access Token)
        shopAuth.setSessionKey(shopAndAuthBean.getAccessToken());
        // session key 有效时长
        shopAuth.setExpiresIn(shopAndAuthBean.getExpiresIn());
        // refresh token
        shopAuth.setRefreshToken(shopAndAuthBean.getRefreshToken());
        // refresh token 有效时长
        shopAuth.setReExpiresIn(shopAndAuthBean.getReExpiresIn());
        // token_type
        shopAuth.setTokenType(shopAndAuthBean.getTokenType());
        // 外部平台账号昵称
        shopAuth.setUserNick(shopAndAuthBean.getUserNick());
        // 外部平台帐号对应id
        shopAuth.setUserId(shopAndAuthBean.getUserId());
        // 当前授权用户来自哪个平台（如天猫，京东）
        shopAuth.setPlatformType(shopAndAuthBean.getPlatformType());
        // Session key第一次授权时间
        shopAuth.setCreateTime(EJSDateUtils.getCurrentDate());
        // Session key最后修改时间
        shopAuth.setUpdateTime(EJSDateUtils.getCurrentDate());

        shopService.saveShopAuth(shopAuth);
        shop.setShopAuthId(shopAuth.getId());
        shopService.saveShop(shop);
    }

    @Override
    public Boolean updateShop(Shop shop, String sessionKey) throws Exception {
        return null;
    }
}
