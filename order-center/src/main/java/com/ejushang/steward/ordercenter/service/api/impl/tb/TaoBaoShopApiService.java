package com.ejushang.steward.ordercenter.service.api.impl.tb;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.openapicenter.tb.api.TbShopApi;
import com.ejushang.steward.openapicenter.tb.api.TbTmcApi;
import com.ejushang.steward.openapicenter.tb.api.TbUserApi;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.exception.TaoBaoApiException;
import com.ejushang.steward.ordercenter.bean.ShopAndAuthBean;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.constant.ShopType;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.ShopAuth;
import com.ejushang.steward.ordercenter.service.ShopService;
import com.ejushang.steward.ordercenter.service.api.IShopApiService;
import com.taobao.api.domain.User;
import com.taobao.api.response.ShopGetResponse;
import com.taobao.api.response.ShopUpdateResponse;
import com.taobao.api.response.TmcUserPermitResponse;
import com.taobao.api.response.UserSellerGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 14-4-11
 * Time: 下午1:47
 */
@Service
@Transactional
public class TaoBaoShopApiService implements IShopApiService {

    private static final Logger log = LoggerFactory.getLogger(TaoBaoShopApiService.class);

    @Autowired
    private ShopService shopService;

    @Override
    public void addShopAndAuth(ShopAndAuthBean shopAndAuthBean) throws Exception {

        if(log.isInfoEnabled()){
            log.info("淘宝店铺新增：参数：" + shopAndAuthBean);
        }

        // 判断用户是天猫商家还是淘宝集市卖家
        TbUserApi tbUserApi = new TbUserApi(shopAndAuthBean.getAccessToken());
        Map<String,Object> userArgsMap = new HashMap<String, Object>();
        userArgsMap.put(ConstantTaoBao.FIELDS,"user_id,nick,type");
        UserSellerGetResponse userSellerGetResponse = tbUserApi.userSellerGet(userArgsMap);
        if(userSellerGetResponse == null || StringUtils.isNotBlank(userSellerGetResponse.getErrorCode())){
            throw new TaoBaoApiException(userSellerGetResponse == null ? "userSellerGetResponse为null" : userSellerGetResponse.getBody());
        }
        User user = userSellerGetResponse.getUser();
        if(user == null){
            throw new TaoBaoApiException("【"+shopAndAuthBean.getUserNick()+"】无对应的用户");
        }

        if(StringUtils.equalsIgnoreCase(user.getType(),"B")){ // 天猫商家
            shopAndAuthBean.setPlatformType(PlatformType.TAO_BAO);
        }
        else if(StringUtils.equalsIgnoreCase(user.getType(),"C")){ //集市卖家
            shopAndAuthBean.setPlatformType(PlatformType.TAO_BAO_2);
        }
        else{ // 其他
            shopAndAuthBean.setPlatformType(PlatformType.TAO_BAO);
        }


        // 获得淘宝平台当前用户的店铺信息
        com.taobao.api.domain.Shop tbShop = getOuterShop(shopAndAuthBean);

        // 系统是否存在该店铺信息
        Shop shopQuery = new Shop();
        shopQuery.setUid(shopAndAuthBean.getUserId());
        shopQuery.setPlatformType(shopAndAuthBean.getPlatformType());
        Shop shop = shopService.getShopByConditionIncludeDelete(shopQuery);
        if(shop != null){
            // 将is_delete状态改为不启用
            shop.setIsDelete(false);
        }

        if(shop == null) {
            if(log.isInfoEnabled()){
                log.info("淘宝店铺新增：系统中不存在该用户【"+shopAndAuthBean.getUserNick()+"】的店铺信息");
            }
            shop = new Shop();
        }



        ShopAuth shopAuth = null;
        if(!NumberUtil.isNullOrZero(shop.getId())){
            ShopAuth shopAuthQuery = new ShopAuth();
            shopAuthQuery.setId(shop.getShopAuthId());
            shopAuth = shopService.getShopAuthByCondition(shopAuthQuery);
        }

        if(tbShop != null){
            // 店铺编号
            shop.setOutShopId(String.valueOf(tbShop.getSid()));
            // 卖家昵称
            shop.setNick(shopAndAuthBean.getUserNick());
            // 卖家用户id
            shop.setUid(shopAndAuthBean.getUserId());
            // 店铺名称
            shop.setTitle(tbShop.getTitle());
            // 开店时间
            shop.setCreateTime(tbShop.getCreated());
            // logo地址
            shop.setPicPath(tbShop.getPicPath());
            // 店铺简介
            shop.setDescription(tbShop.getDesc());
            // 店铺公告
            shop.setBulletin(tbShop.getBulletin());
            // 主营类目编号
            shop.setCatId(String.valueOf(tbShop.getCid()));
            // 最后修改时间
            shop.setUpdateTime(tbShop.getModified());
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

        shopAuth = shopAuth == null ? new ShopAuth() :shopAuth;
        // 店铺对应的session key(即Access Token)
        shopAuth.setSessionKey(shopAndAuthBean.getAccessToken());
        // session key 有效时长
        shopAuth.setExpiresIn(shopAndAuthBean.getExpiresIn());
        // refresh token
        shopAuth.setRefreshToken(shopAndAuthBean.getRefreshToken());
        // refresh token 有效时长
        shopAuth.setReExpiresIn(shopAndAuthBean.getReExpiresIn());
        // r1级别API或字段的访问过期时间
        shopAuth.setR1ExpiresIn(shopAndAuthBean.getR1ExpiresIn());
        // r2级别API或字段的访问过期时间
        shopAuth.setR2ExpiresIn(shopAndAuthBean.getR2ExpiresIn());
        // w1级别API或字段的访问过期时间
        shopAuth.setW1ExpiresIn(shopAndAuthBean.getW1ExpiresIn());
        // w2级别API或字段的访问过期时间
        shopAuth.setW2ExpiresIn(shopAndAuthBean.getW2ExpiresIn());
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

        // 为该店铺进行消息服务授权
        TbTmcApi tmcApi = new TbTmcApi(shopAndAuthBean.getAccessToken());
        TmcUserPermitResponse response = tmcApi.tmcUserPermit(null);
        if(response.getIsSuccess()){
            if(log.isInfoEnabled()){
                log.info("淘宝店铺新增："+shopAndAuthBean.getUserNick()+"消息服务授权成功");
            }
        }
        else{
            if(log.isInfoEnabled()){
                log.info("淘宝店铺新增："+shopAndAuthBean.getUserNick()+"消息服务授权失败，ErrorCode："+response.getErrorCode());
            }
            throw new TaoBaoApiException("淘宝店铺新增："+shopAndAuthBean.getUserNick()+"消息服务授权失败，ErrorCode："+response.getErrorCode());
        }
        // 新增店铺
        shopService.saveShopAuth(shopAuth);
        shop.setShopAuthId(shopAuth.getId());
        shopService.saveShop(shop);
    }

    /**
     * 获取外部平台的店铺信息
     * @param shopAndAuthBean
     * @return
     * @throws Exception
     */
    private com.taobao.api.domain.Shop getOuterShop(ShopAndAuthBean shopAndAuthBean) throws Exception {
        // 初始化淘宝shopApi
        TbShopApi shopApi = new TbShopApi(shopAndAuthBean.getAccessToken());
        if(log.isInfoEnabled()){
            log.info("淘宝店铺新增：初始化淘宝shopApi："+shopApi);
        }

        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantTaoBao.FIELDS,getShopFields());
        argsMap.put(ConstantTaoBao.NICK,shopAndAuthBean.getUserNick());
        if(log.isInfoEnabled()){
            log.info("淘宝店铺新增：访问淘宝店铺api参数：" + argsMap);
        }
        // 获取淘宝店铺信息
        ShopGetResponse shopGetResponse = shopApi.shopGet(argsMap);

        com.taobao.api.domain.Shop tbShop = null;
        // 当前账户是供应商或不存在店铺
        if(StringUtils.equals(shopGetResponse.getErrorCode(), "isv.shop-service-error:SHOP_IS_NOT_EXIST")
                || StringUtils.equals(shopGetResponse.getErrorCode(),"isv.invalid-parameter:user-without-shop")){
            if(log.isInfoEnabled()){
                log.info("淘宝店铺新增：当前昵称" + shopAndAuthBean.getUserNick() + "对应的是供应商或不是店铺。errorCode:"
                        + shopGetResponse.getErrorCode());
            }
        }
        else if(StringUtils.equals(shopGetResponse.getErrorCode(),"isv.user-not-exist:invalid-nick")
                || StringUtils.equals(shopGetResponse.getErrorCode(),"40")){
            if(log.isErrorEnabled()){
                log.error("淘宝店铺新增：当前昵称" + shopAndAuthBean.getUserNick()+"对应的账户不存在或获取信息失败"
                        + shopGetResponse.getErrorCode());
            }
            // 抛出异常
            throw new TaoBaoApiException("当前昵称" + shopAndAuthBean.getUserNick()+"对应的账户不存在或获取信息失败"
                    + shopGetResponse.getErrorCode());
        }
        else{
            tbShop = shopGetResponse.getShop();
            if(log.isInfoEnabled()){
                log.info("淘宝店铺新增：获取淘宝店铺信息，" + tbShop);
            }
        }
        return tbShop;
    }

    /**
     * 获取查询店铺返回字段
     * @return
     */
    private String getShopFields(){
        List<String> fieldList = new ArrayList<String>();
        fieldList.add(ConstantTaoBao.SID);
        fieldList.add(ConstantTaoBao.CID);
        fieldList.add(ConstantTaoBao.NICK);
        fieldList.add(ConstantTaoBao.TITLE);
        fieldList.add(ConstantTaoBao.DESC);
        fieldList.add(ConstantTaoBao.BULLETIN);
        fieldList.add(ConstantTaoBao.PIC_PATH);
        fieldList.add(ConstantTaoBao.CREATED);
        fieldList.add(ConstantTaoBao.MODIFIED);
        fieldList.add(ConstantTaoBao.SHOP_SCORE);

        StringBuffer sb = new StringBuffer("");
        for(int i = 0; i < fieldList.size(); i++){
            if(i == 0){
                sb.append(fieldList.get(i));
            }
            else{
                sb.append(",").append(fieldList.get(i));
            }
        }
        return sb.toString();
    }

    /**
     * 同步店铺信息至淘宝平台
     * @param shop
     * @return
     */
    public Boolean updateShop(Shop shop,String sessionKey) throws Exception {
        if(log.isInfoEnabled()){
            log.info("将信息同步到淘宝平台："+shop);
        }
        // 来自淘宝平台
        Map<String,Object> argsMap = new HashMap<String, Object>();
        // 设置店铺标题
        argsMap.put(ConstantTaoBao.TITLE,shop.getTitle());
        // 设置店铺公告
        argsMap.put(ConstantTaoBao.BULLETIN,shop.getBulletin());
        // 设置店铺描述
        argsMap.put(ConstantTaoBao.DESC,shop.getDescription());

        if(log.isInfoEnabled()){
            log.info("店铺信息同步至淘宝，参数argsMap = "+ argsMap);
        }
        // 创建淘宝shopApi
        TbShopApi shopApi = new TbShopApi(sessionKey);
        // 将店铺信息更新至淘宝平台
        ShopUpdateResponse response = shopApi.shopUpdate(argsMap);
        if(StringUtils.isNotBlank(response.getErrorCode())){
            throw new TaoBaoApiException("调用淘宝shopApi.shopUpdate("+argsMap+")出现异常:" + response.getBody());
        }
        if(log.isInfoEnabled()){
            log.info("成功将店铺信息同步至淘宝平台："+shop);
        }

        return true;
    }
}
