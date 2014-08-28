package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.constant.StorageFlowType;
import com.ejushang.steward.ordercenter.service.StorageFlowService;
import com.ejushang.steward.scm.common.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * User: Sed.Li(李朝)
 * Date: 14-4-11
 * Time: 下午2:06
 */
@RequestMapping("/storageflow")
@Controller
public class StorageFlowController extends BaseController {

    @Autowired
    private StorageFlowService storageFlowService;

    /**
     * 查询指定时间范围内的指定仓库的出入库产品
     * @param type
     * @param minDate
     * @param maxDate
     * @param repositoryId
     * @param request
     * @return
     */
    @RequestMapping("/list_product")
    @ResponseBody
    public JsonResult listProduct(StorageFlowType type, Date minDate, Date maxDate, Integer repositoryId, HttpServletRequest request) {
        Page page = PageFactory.getPage(request);
        storageFlowService.pageListGroupProduct(type, minDate, maxDate, repositoryId, page);
        return new JsonResult(true).addObject(page);
    }


    /**
     * 查询产品的出入库记录
     *
     * @param sku
     * @param minDate
     * @param maxDate
     * @param repositoryId
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResult listBySkuAndDate(String sku,StorageFlowType type, Date minDate, Date maxDate, Integer repositoryId, HttpServletRequest request) {
        if (StringUtils.isBlank(sku)) {
            return new JsonResult(false,"产品条码不能为空");
        }
        Page page = PageFactory.getPage(request);
        storageFlowService.pageList(sku, type, minDate, maxDate, repositoryId, page);
        return new JsonResult(true).addObject(page);
    }

}
