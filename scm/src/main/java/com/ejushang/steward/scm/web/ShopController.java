package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.service.ShopService;
import com.ejushang.steward.ordercenter.vo.ShopVo;
import com.ejushang.steward.common.util.OperationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺管理控制类
 * User: Baron.Zhang
 * Date: 14-1-7
 * Time: 下午1:30
 */
@Controller
public class ShopController {

    private static final Logger log = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ShopService shopService;

    /**
     * 店铺一览
     * @param shop
     */
    @OperationLog("根据条件查询店铺")
    @RequestMapping("/shop/list")
    @ResponseBody
    public JsonResult listShop(Shop shop,HttpServletRequest request) throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(log.isInfoEnabled()){
            log.info("店铺：查询店铺一览，" + shop);
        }
        Page page = PageFactory.getPage(request);
        // 查询店铺一览
        shopService.findShop(page,shop);

        Shop dbShop = null;
        ShopVo shopVo = null;
        List<ShopVo> shopVoList = new ArrayList<ShopVo>();
        for(Object object : page.getResult()){
            dbShop = (Shop) object;
            shopVo = convertShopAndAuth2ShopVo(dbShop);
            if(shopVo != null){
                shopVoList.add(shopVo);
            }
        }

        page.setResult(shopVoList);

        return new JsonResult(true).addObject(page);
    }

    @OperationLog("获取在线店铺")
    @RequestMapping("/np/findOnlineShop")
    @ResponseBody
    public JsonResult findOnlineShop(){
        List<Shop> shopList = shopService.findOnlineShop();
        return new JsonResult(true).addList(shopList);
    }

    /**
     * 将包含授权信息的Shop转换为ShopVo
     * @param shop
     * @return
     */
    private ShopVo convertShopAndAuth2ShopVo(Shop shop){
        ShopVo shopVo = null;
        if(shop == null){
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
        if(shop.getShopAuth() != null) {
            shopVo.setSessionKey(shop.getShopAuth().getSessionKey());
            shopVo.setRefreshToken(shop.getShopAuth().getRefreshToken());
        }

        return shopVo;
    }


    /**
     * 店铺明细
     * @param id
     */
    @OperationLog("根据店铺id查询店铺明细")
    @RequestMapping("/shop/detail")
    @ResponseBody
    public JsonResult detailShop(Integer id,HttpServletRequest request) throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(log.isInfoEnabled()){
            log.info("店铺：查询店铺明细，id = " + id);
        }
        // 查询店铺明细
        ShopVo shopVo = shopService.getShopVo(id);
        return new JsonResult(true).addObject(shopVo);
    }

    /**
     * 店铺更新
     * @param shop
     * @param sessionKey
     */
    @OperationLog("店铺更新")
    @RequestMapping("/shop/update")
    @ResponseBody
    public JsonResult updateShop(@ModelAttribute("id")Shop shop,String sessionKey) throws Exception {
        if(log.isInfoEnabled()){
            log.info("店铺：更新店铺，" + shop);
        }
        // 更新店铺
        shopService.updateShop(shop,sessionKey);
        BusinessLogUtil.bindBusinessLog("店铺详情:名称[%s],描述[%s],卖家昵称[%s],session Key[%s]",
                shop.getTitle(),shop.getDescription(),shop.getNick(),sessionKey);
        return new JsonResult(true,"更新成功！");
    }

    /**
     * 店铺删除
     * @param id
     * @param response
     */
    @OperationLog("店铺逻辑删除")
    @RequestMapping("/shop/delete")
    @ResponseBody
    public JsonResult deleteShop(Integer id,HttpServletResponse response) throws IOException {
        if(log.isInfoEnabled()){
            log.info("店铺：删除店铺，id = " + id);
        }
        // 删除店铺
        shopService.deleteShopForLogic(id);
        BusinessLogUtil.bindBusinessLog("店铺ID:%d",id);
        return new JsonResult(true,"删除成功！");
    }

    /**
     * 动态获取评分
     * @param id
     */
    @OperationLog("动态获取评分")
    @RequestMapping("/shop/dynamicGetScore")
    @ResponseBody
    public JsonResult dynamicGetScore(Integer id) throws Exception {
        if(log.isInfoEnabled()){
            log.info("店铺：动态获取评分，id = " + id);
        }
        // 动态获取评分
        ShopVo shopVo = shopService.dynamicGetScore(id);
        return new JsonResult(true).addObject(shopVo);
    }
}
