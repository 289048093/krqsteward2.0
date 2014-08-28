package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.OperationLog;
import com.ejushang.steward.ordercenter.domain.ShopProduct;
import com.ejushang.steward.ordercenter.service.ShopProductService;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ApiInvokeException;
import com.ejushang.steward.ordercenter.util.ShopProInputPar;
import com.ejushang.steward.scm.common.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-8-8
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class ShopProductController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ShopProductController.class);

    @Autowired
    private ShopProductService shopProductService;

    @OperationLog("商品同步模块：根据条件查询店铺")
    @RequestMapping("/shopProduct/list")
    @ResponseBody
    public JsonResult list(ShopProInputPar shopProInputPar, HttpServletRequest request) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数shopProInputPar:[%s]" , shopProInputPar));
        }
        Page page = PageFactory.getPage(request);
        page.setResult(shopProductService.list(shopProInputPar));

        return new JsonResult(true).addObject(page);
    }

    @OperationLog("商品同步模块：商品上架")
    @RequestMapping("/shopProduct/listing")
    @ResponseBody
    public JsonResult listing(Integer[] shopProductIds) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数shopProductIds:[%s]" , shopProductIds));
        }
        try {
            shopProductService.listing(shopProductIds);
            return new JsonResult(true);
        } catch (ApiInvokeException e) {
            return new JsonResult(false, e.getMessage());
        }
    }

    @OperationLog("商品同步模块：商品下架")
    @RequestMapping("/shopProduct/delisting")
    @ResponseBody
    public JsonResult delisting(Integer[] shopProductIds) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数shopProductIds:[%s]" , shopProductIds));
        }
        try {
            shopProductService.delisting(shopProductIds);
            return new JsonResult(true);
        } catch (ApiInvokeException e) {
            return new JsonResult(false, e.getMessage());
        }
    }


    @OperationLog("商品同步模块：删除商品同步")
    @RequestMapping("/shopProduct/delete")
    @ResponseBody
    public JsonResult delete(Integer[] shopProductIds) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数shopProductIds:[%s]" , shopProductIds));
        }
        shopProductService.delete(shopProductIds);
        return new JsonResult(true);
    }

    @OperationLog("商品同步模块：添加商品")
    @RequestMapping("/shopProduct/add")
    @ResponseBody
    public JsonResult add(Integer[] productIds,Integer shopId,Integer storagePercent,Boolean autoPutaway,Boolean synStatus,Integer platformId) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数productIds:[%s],店铺id:[%s],库存占比:[%s],是否自动上架:[%s],是否自动同步:[%s],平台id:[%s]," ,
                                        productIds,shopId,storagePercent,autoPutaway,synStatus,platformId));
        }
        try {
            shopProductService.add(productIds,shopId,storagePercent,autoPutaway,synStatus,platformId);
            return new JsonResult(true);
        } catch (StewardBusinessException e) {
            return new JsonResult(false, e.getMessage());
        }
    }

    @OperationLog("商品同步模块：更新商品")
    @RequestMapping("/shopProduct/update")
    @ResponseBody
    public JsonResult update(Integer id, Integer storageNum, Integer storagePercent, Boolean synStatus, Boolean autoPutaway) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数id:[%s],店铺库存数量:[%s],店铺库存占比:[%s],是否自动同步:[%s],是否自动上架:[%s]" ,
                                        id,storageNum,storagePercent,synStatus,autoPutaway));
        }
        try {
            shopProductService.update(id, storageNum, storagePercent, synStatus, autoPutaway);
            return new JsonResult(true);
        } catch (StewardBusinessException se) {
            return new JsonResult(false, se.getMessage());
        }
    }

    @OperationLog("商品同步模块：查看商品详情")
    @RequestMapping("/shopProduct/searchDetail")
    @ResponseBody
    public JsonResult searchDetail(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数id:[%s]" , id));
        }
        try {
            return new JsonResult(true).addObject(shopProductService.searchDetail(id));
        } catch (StewardBusinessException se) {
            return new JsonResult(false, se.getMessage());
        }
    }

    @OperationLog("商品同步模块：手动同步")
    @RequestMapping("/shopProduct/updateShopStorage")
    @ResponseBody
    public JsonResult updateShopStorage(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数id:[%s]" , id));
        }
        try {
            shopProductService.updateShopStorage(id);
            return new JsonResult(true);
        } catch (ApiInvokeException e) {
            return new JsonResult(false, e.getMessage());
        }
    }

}
