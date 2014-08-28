package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.openapicenter.tb.api.TbShopApi;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.exception.TaoBaoApiException;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Platform;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.ShopAuth;
import com.ejushang.steward.ordercenter.service.api.impl.jd.JingDongShopApiService;
import com.ejushang.steward.ordercenter.service.api.impl.tb.TaoBaoShopApiService;
import com.ejushang.steward.ordercenter.vo.ShopVo;
import com.taobao.api.response.ShopGetResponse;
import org.apache.commons.collections.CollectionUtils;
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
 * User: Shiro
 * Date: 14-4-18
 * Time: 下午3:48
 */
@Service
public class ShopService {

    private static final Logger logger = LoggerFactory.getLogger(ShopService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private TaoBaoShopApiService taoBaoShopApiService;

    @Autowired
    private JingDongShopApiService jingDongShopApiService;

    /**
     * 根据条件查询店铺，结果分页
     *
     * @param page
     * @param shop
     * @return
     */
    @Transactional(readOnly = false)
    public List<Shop> findShop(Page page, Shop shop) {
        Search search = new Search(Shop.class).setCacheable(true);

        if (shop != null) {
            if (StringUtils.isNotBlank(shop.getNick())) {
                search.addFilterLike("nick", "%" + shop.getNick() + "%");
            }
            if (StringUtils.isNotBlank(shop.getTitle())) {
                search.addFilterLike("title", "%" + shop.getTitle() + "%");
            }
        }
        search.addFilterEqual("isDelete", false);
        search.addFetch("shopAuth").addSortAsc("createTime").addPagination(page);

        return generalDAO.search(search);
    }

    /**
     * 查询店铺
     *
     * @param shop
     * @return
     */
    @Transactional(readOnly = true)
    public List<Shop> getShop(Shop shop) {
        if (logger.isInfoEnabled()) {
            logger.info("getShop方法参数为shop[{}]", shop);
        }
        Search search = new Search(Shop.class).setCacheable(true);
        if (shop != null) {
            if (!NumberUtil.isNullOrZero(shop.getId())) {
                search.addFilterEqual("id", shop.getId());
            }
            if (StringUtils.isNotBlank(shop.getNick())) {
                search.addFilterLike("nick", "%" + shop.getNick().trim() + "%");
            }
            if (StringUtils.isNotBlank(shop.getUid())) {
                search.addFilterLike("uid", "%" + shop.getUid().trim() + "%");
            }
            if (StringUtils.isNotBlank(shop.getTitle())) {
                search.addFilterLike("title", "%" + shop.getTitle().trim() + "%");
            }
            if (shop.getPlatformType() != null) {
                search.addFilterEqual("platformType", shop.getPlatformType());
            }
            if (!NumberUtil.isNullOrZero(shop.getOutShopId())) {
                search.addFilterEqual("outShopId", shop.getOutShopId());
            }
        }
        search.addFilterEqual("isDelete", false);
        return generalDAO.search(search);
    }

    /**
     * 查询在线店铺
     * @return
     */
    @Transactional(readOnly = true)
    public List<Shop> findOnlineShop(){
        Search search = new Search(Shop.class).setCacheable(true);
        search.addFilterNotEmpty("shopAuthId")
                .addFilterEqual("isDelete", false);
        return generalDAO.search(search);
    }

    /**
     * 根据条件查询唯一的返回值
     *
     * @param shop
     * @return
     */
    public Shop getShopByCondition(Shop shop) {
        if (logger.isInfoEnabled()) {
            logger.info("getShop方法参数为shop[{}]", shop);
        }
        Search search = new Search(Shop.class).setCacheable(true);
        if (shop != null) {
            if (!NumberUtil.isNullOrZero(shop.getUid())) {
                search.addFilterEqual("uid", shop.getUid());
            }
            if (shop.getPlatformType() != null) {
                search.addFilterEqual("platformType", shop.getPlatformType());
            }
        }
        search.addFilterEqual("isDelete", false);
        List<Shop> shopList = generalDAO.search(search);
        return CollectionUtils.isNotEmpty(shopList) ? shopList.get(0) : null;
    }

    /**
     * 根据条件查询唯一的返回值
     *
     * @param shop
     * @return
     */
    public Shop getShopByConditionIncludeDelete(Shop shop) {
        if (logger.isInfoEnabled()) {
            logger.info("getShop方法参数为shop[{}]", shop);
        }
        Search search = new Search(Shop.class).setCacheable(true);
        if (shop != null) {
            if (!NumberUtil.isNullOrZero(shop.getUid())) {
                search.addFilterEqual("uid", shop.getUid());
            }
            if (shop.getPlatformType() != null) {
                search.addFilterEqual("platformType", shop.getPlatformType());
            }
        }
        //search.addFilterEqual("isDelete", false);
        List<Shop> shopList = generalDAO.search(search);
        return CollectionUtils.isNotEmpty(shopList) ? shopList.get(0) : null;
    }

    /**
     * 查询店铺
     *
     * @param shop
     * @return
     */
    @Transactional(readOnly = true)
    public List<Shop> getShopAndAuth(Shop shop) {
        if (logger.isInfoEnabled()) {
            logger.info("getShop方法参数为shop[{}]", shop);
        }
        Search search = new Search(Shop.class)
                .addFetch("shopAuth");
        if (shop != null) {
            // 根据id精确查询
            if (!NumberUtil.isNullOrZero(shop.getId())) {
                search.addFilterEqual("id", shop.getId());
            }
            // 根据nick模糊查询
            if (StringUtils.isNotBlank(shop.getNick())) {
                search.addFilterLike("nick", "%" + shop.getNick().trim() + "%");
            }
            // 根据店铺名称模糊查询
            if (StringUtils.isNotBlank(shop.getTitle())) {
                search.addFilterLike("title", "%" + shop.getTitle().trim() + "%");
            }
            // 根据外部平台店铺id精确查询
            if (!NumberUtil.isNullOrZero(shop.getOutShopId())) {
                search.addFilterEqual("outShopId", shop.getOutShopId());
            }
            // 根据平台模糊查询
            if(shop.getPlatformType()!=null){
                search.addFilterEqual("platformType",shop.getPlatformType());
            }
        }
        search.addFilterEqual("isDelete", false);
        return generalDAO.search(search);
    }

    /**
     * 查询店铺
     *
     * @param shop
     * @return
     */
    @Transactional(readOnly = true)
    public List<Shop> getOnlineShopAndAuth(Shop shop) {
        if (logger.isInfoEnabled()) {
            logger.info("getShop方法参数为shop[{}]", shop);
        }
        Search search = new Search(Shop.class)
                .addFetch("shopAuth").setCacheable(true);
        if (shop != null) {
            // 根据id精确查询
            if (!NumberUtil.isNullOrZero(shop.getId())) {
                search.addFilterEqual("id", shop.getId());
            }
            // 根据nick模糊查询
            if (StringUtils.isNotBlank(shop.getNick())) {
                search.addFilterLike("nick", "%" + shop.getNick().trim() + "%");
            }
            // 根据店铺名称模糊查询
            if (StringUtils.isNotBlank(shop.getTitle())) {
                search.addFilterLike("title", "%" + shop.getTitle().trim() + "%");
            }
            // 根据外部平台店铺id精确查询
            if (!NumberUtil.isNullOrZero(shop.getOutShopId())) {
                search.addFilterEqual("outShopId", shop.getOutShopId());
            }
            // 根据平台模糊查询
            if(shop.getPlatformType()!=null){
                search.addFilterEqual("platformType",shop.getPlatformType());
            }
        }
        search.addFilterNotEmpty("shopAuthId");
        search.addFilterEqual("isDelete", false);
        return generalDAO.search(search);
    }

    /**
     * 获取店铺及其授权信息
     *
     * @param id
     * @return
     */
    public ShopVo getShopVo(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("getShopVo方法参数为id={}", id);
        }
        Search search = new Search(Shop.class)
                .addFetch("shopAuth").setCacheable(true);
        if (!NumberUtil.isNullOrZero(id)) {
            search.addFilterEqual("id", id);
        }
        search.addFilterEqual("isDelete", false);

        List<Shop> shopList = generalDAO.search(search);
        Shop shop = CollectionUtils.isNotEmpty(shopList) ? shopList.get(0) : null;
        ShopVo shopVo = convertShopAndAuth2ShopVo(shop);
        return shopVo;
    }

    /**
     * 将包含授权信息的Shop转换为ShopVo
     *
     * @param shop
     * @return
     */
    private ShopVo convertShopAndAuth2ShopVo(Shop shop) {
        ShopVo shopVo = null;
        if (shop == null) {
            return shopVo;
        }

        shopVo = new ShopVo();
        shopVo.setId(shop.getId());
        shopVo.setShopId(shop.getOutShopId());
        shopVo.setCatId(shop.getCatId());
        shopVo.setNick(shop.getNick());
        shopVo.setTitle(shop.getTitle());
        shopVo.setDescription(shop.getDescription());
        shopVo.setBulletin(shop.getBulletin());
        shopVo.setPicPath(shop.getPicPath());
        shopVo.setItemScore(shop.getItemScore());
        shopVo.setServiceScore(shop.getServiceScore());
        shopVo.setDeliveryScore(shop.getDeliveryScore());
        shopVo.setDeExpress(shop.getDeExpress());
        shopVo.setEnableMsg(shop.getEnableMsg());
        shopVo.setMsgTemp(shop.getMsgTemp());
        shopVo.setMsgSign(shop.getMsgSign());
        shopVo.setOutPlatformType(shop.getPlatformType().getName());
        shopVo.setOutPlatformTypeValue(PlatformType.valueOf(shop.getPlatformType().getName()).getValue());
        shopVo.setCreateTime(shop.getCreateTime());
        shopVo.setUpdateTime(shop.getUpdateTime());
        if (shop.getShopAuth() != null) {
            shopVo.setSessionKey(shop.getShopAuth().getSessionKey());
            shopVo.setRefreshToken(shop.getShopAuth().getRefreshToken());
        }

        return shopVo;
    }

    /**
     * 查询店铺授权
     *
     * @param shopAuth
     * @return
     */
    @Transactional(readOnly = true)
    public List<ShopAuth> getShopAuth(ShopAuth shopAuth) {
        if (logger.isInfoEnabled()) {
            logger.info("getShop方法参数为shop[{}]", shopAuth);
        }
        Search search = new Search(ShopAuth.class);
        if (shopAuth != null) {
            if (!NumberUtil.isNullOrZero(shopAuth.getId())) {
                search.addFilterEqual("id", shopAuth.getId());
            }
        }
        search.addFilterEqual("isDelete", false);
        return generalDAO.search(search);
    }

    /**
     * 根据条件查询唯一的授权信息
     *
     * @param shopAuth
     * @return
     */
    public ShopAuth getShopAuthByCondition(ShopAuth shopAuth) {
        if (logger.isInfoEnabled()) {
            logger.info("getShop方法参数为shop[{}]", shopAuth);
        }
        Search search = new Search(ShopAuth.class);
        if (shopAuth != null) {
            if (!NumberUtil.isNullOrZero(shopAuth.getId())) {
                search.addFilterEqual("id", shopAuth.getId());
            }
        }
        search.addFilterEqual("isDelete", false);

        List<ShopAuth> shopAuthList = generalDAO.search(search);
        return CollectionUtils.isNotEmpty(shopAuthList) ? shopAuthList.get(0) : null;
    }

    /**
     * 保存或更新店铺
     *
     * @param shop
     */
    @Transactional
    public void saveShop(Shop shop) {
        if (logger.isInfoEnabled()) {
            logger.info("saveShop方法参数为shop[{}]", shop);
        }
        generalDAO.saveOrUpdate(shop);
    }

    /**
     * 根据ID删除店铺
     *
     * @param id
     */
    @Transactional
    public void deleteShop(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("deleteShop方法参数为id[{}]", id);
        }
        generalDAO.removeById(Shop.class, id);
    }

    /**
     * 根据id逻辑删除店铺
     *
     * @param id
     */
    public void deleteShopForLogic(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("deleteShopForLogic方法参数为id[{}]", id);
        }
        Shop shop = getById(id);
        shop.setIsDelete(true);
        generalDAO.saveOrUpdate(shop);
    }

    /**
     * 根据id获得店铺对象
     *
     * @param id
     * @return
     */
    @Transactional
    public Shop getById(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("getById方法参数为id[{}]", id);
        }
        return generalDAO.get(Shop.class, id);
    }

    /**
     * 根据id获得店铺对象
     *
     * @param id
     * @return
     */
    @Transactional
    public ShopAuth getShopAuthById(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("getShopAuthById方法参数为id[{}]", id);
        }
        return generalDAO.get(ShopAuth.class, id);
    }

    /**
     * 保存或修改shopAuth
     *
     * @param shopAuth
     * @return
     */
    @Transactional
    public void saveShopAuth(ShopAuth shopAuth) {
        if (logger.isInfoEnabled()) {
            logger.info("saveShopAuth方法参数为shopAuth[{}]", shopAuth);
        }
        generalDAO.saveOrUpdate(shopAuth);
    }

    @Transactional(readOnly = true)
    public List<Shop> findByPlatformType(PlatformType platformType) {
        Search search = new Search(Shop.class).setCacheable(true);
        search.addFilterEqual("platformType", platformType);
        search.addFilterEqual("isDelete", false);
        List<Shop> shops = generalDAO.search(search);
        return shops;
    }
       @Transactional(readOnly = true)
    public Shop findShopByNick(String nick) {
        List<Shop> shops = generalDAO.search(new Search(Shop.class).setCacheable(true).addFilterEqual("nick", nick));
        if (shops.size() > 0) {
            return shops.get(0);
        }
        List<Shop> shopList = generalDAO.search(new Search(Shop.class).setCacheable(true).addFilterEqual("title", nick));
        if (shopList.size() > 0) {
            return shopList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 店铺更新
     *
     * @param shop
     */
    public void updateShop(Shop shop, String sessionKey) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("店铺更新：" + shop);
        }

        if (!(StringUtils.isBlank(shop.getBulletin()) && StringUtils.isBlank(shop.getDescription()))) {
            // 判断来自哪个平台
            if (StringUtils.equals(shop.getPlatformType().getName(), PlatformType.TAO_BAO.getName())
                    || StringUtils.equals(shop.getPlatformType().getName(), PlatformType.TAO_BAO_2.getName())) {
                // 将店铺信息更新至淘宝平台
                taoBaoShopApiService.updateShop(shop, sessionKey);
            } else if (StringUtils.equals(shop.getPlatformType().getName(), PlatformType.JING_DONG.getName())) {
                // 将店铺信息更新至京东平台
                jingDongShopApiService.updateShop(shop, sessionKey);
            }
        }
        // 所有操作成功，店铺更新
        generalDAO.saveOrUpdate(shop);
        if (logger.isInfoEnabled()) {
            logger.info("店铺更新成功!");
        }
    }

    /**
     * 动态获取评分
     *
     * @param id
     * @return
     */
    public ShopVo dynamicGetScore(Integer id) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("动态获取评分：id = " + id);
        }
        // 获取店铺信息与店铺授权信息
        ShopVo shopVo = getShopVo(id);

        if (logger.isInfoEnabled()) {
            logger.info("从外部平台动态获取评分信息：id = " + id);
        }

        // 判断来自哪个平台
        if (StringUtils.equals(shopVo.getOutPlatformType(), PlatformType.TAO_BAO.getName())
                || StringUtils.equals(shopVo.getOutPlatformType(), PlatformType.TAO_BAO_2.getName())) {
            // 来自淘宝平台
            Map<String, Object> argsMap = new HashMap<String, Object>();
            // 设置返回字段（必须）
            argsMap.put(ConstantTaoBao.FIELDS, "sid,cid,nick,title,desc,bulletin,pic_path,created,modified,shop_score");
            // 设置卖家昵称（必须）
            argsMap.put(ConstantTaoBao.NICK, shopVo.getNick());

            if (logger.isInfoEnabled()) {
                logger.info("查询淘宝店铺信息，参数argsMap = " + argsMap);
            }

            // 创建淘宝shopApi
            TbShopApi shopApi = new TbShopApi(shopVo.getSessionKey());
            // 获取淘宝店铺信息
            ShopGetResponse response = shopApi.shopGet(argsMap);
            if (StringUtils.isNotBlank(response.getErrorCode())) {
                throw new TaoBaoApiException("调用淘宝 shopApi.getShop(" + argsMap + ")出现异常，" + response.getBody());
            }
            com.taobao.api.domain.Shop shop = response.getShop();
            // 设置商品描述评分
            shopVo.setItemScore(shop.getShopScore().getItemScore());
            // 设置服务态度评分
            shopVo.setServiceScore(shop.getShopScore().getServiceScore());
            // 设置发货速度评分
            shopVo.setDeliveryScore(shop.getShopScore().getDeliveryScore());

        } else if (StringUtils.equals(shopVo.getOutPlatformType(), PlatformType.JING_DONG.getName())) {
            // todo:来自京东平台
        }

        // 获取评分信息的同时将评分信息更新到本地
        Shop shop = getById(id);

        if (shop == null) {
            shop = new Shop();
        }

        shop.setBulletin(shopVo.getBulletin());
        shop.setDescription(shopVo.getDescription());
        // 设置商品描述评分
        shop.setItemScore(shopVo.getItemScore());
        // 设置服务态度评分
        shop.setServiceScore(shopVo.getServiceScore());
        // 设置发货速度评分
        shop.setDeliveryScore(shopVo.getDeliveryScore());

        if (logger.isInfoEnabled()) {
            logger.info("店铺：更新评分信息至本地，" + shop);
        }
        // 将评分信息更新到本地
        generalDAO.saveOrUpdate(shop);
        return shopVo;
    }
}
