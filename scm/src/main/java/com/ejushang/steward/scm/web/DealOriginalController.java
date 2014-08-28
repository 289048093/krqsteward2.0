package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.ordercenter.service.DealOriginalOrderService;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: JBoss.WU
 * Date: 14-7-8
 * Time: 下午3:25
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/dealOriginalOrder")
public class DealOriginalController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);
    @Autowired
    private DealOriginalOrderService dealOriginalOrderService;

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult findOriginalOrder(HttpServletRequest request) throws ParseException {
        @SuppressWarnings("unchecked")
        Map<String, Object[]> map = request.getParameterMap();
        Page page = PageFactory.getPage(request);
        return new JsonResult(true).addObject(dealOriginalOrderService.findOriginalOrderDetails(map, page));
    }

    @RequestMapping("/findItem")
    @ResponseBody
    public JsonResult findOriginalOrderItem(Integer orderIds) throws ParseException {
        if (orderIds == null) {
            log.error(String.format("参数Integer类型id为空[%s]", orderIds));
            return new JsonResult(false, "请选择订单");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数Integer的id[%s]", orderIds));
        }
        return new JsonResult(true).addList(dealOriginalOrderService.findDealOriginalItemVoById(orderIds));
    }

    @RequestMapping("/findLog")
    @ResponseBody
    public JsonResult findLog(Integer orderIds) throws ParseException {
        if (orderIds == null) {
            log.error(String.format("参数Integer类型id为空[%s]", orderIds));
            return new JsonResult(false, "请选择订单");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数Integer的id[%s]", orderIds));
        }
        return new JsonResult(true).addList(dealOriginalOrderService.findOrderAnalyzeLogByOriginalOrderId(orderIds));
    }

    @OperationLog("更新异常订单项sku")
    @RequestMapping("/updateItem")
    @ResponseBody
    public JsonResult updateOriginalOrderItem(String data) {
        if(data==null){
            log.error(String.format("参数data为空[%s]",data));
            return new JsonResult(false,"请选择订单");
        }
        if (StringUtils.isBlank(data)) {
            return new JsonResult(false, "请选择订单");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数data:[%s]", data));
        }
        HashMap map = JsonUtil.json2Object(data, HashMap.class);
        dealOriginalOrderService.updateOriginalOrderItem(map);
        BusinessLogUtil.bindBusinessLog("异常订单ID:%s,新SKU:%s",map.get("id"),map.get("sku"));
        return new JsonResult(true, "更新成功");
    }

    @OperationLog("异常订单作废")
    @RequestMapping("/cancellation")
    @ResponseBody
    public JsonResult cancellationOriginalOrder(String id) {
        if (id == null) {
            log.error(String.format("参数id数组为空[%s]", id));
            return new JsonResult(false, "请勾选订单");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数id数组[%s]", id));
        }
        String[] stringIds = id.split(",");
        int[] ids = new int[stringIds.length];
        for (int i = 0; i < stringIds.length; i++) {
            ids[i] = Integer.parseInt(stringIds[i]);
        }
        dealOriginalOrderService.cancellationOriginalOrder(ids);
        BusinessLogUtil.bindBusinessLog("作废异常订单ID:%s",id);
        return new JsonResult(true, "作废成功");
    }

    @OperationLog("异常订单恢复")
    @RequestMapping("/recover")
    @ResponseBody
    public JsonResult recoverOriginalOrder(String id) {
        if (id == null) {
            log.error(String.format("参数id数组为空[%s]", id));
            return new JsonResult(false, "请勾选订单");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数id数组[%s]", id));
        }
        String[] stringIds = id.split(",");
        int[] ids = new int[stringIds.length];
        for (int i = 0; i < stringIds.length; i++) {
            ids[i] = Integer.parseInt(stringIds[i]);
        }
        dealOriginalOrderService.recoverOriginalOrder(ids);
        BusinessLogUtil.bindBusinessLog("恢复异常订单ID:%s",id);
        return new JsonResult(true, "恢复成功");
    }

    @OperationLog("异常订单解析")
    @RequestMapping("/analyze")
    @ResponseBody
    public JsonResult analyzeOriginalOrder(String id) {
        if (id == null) {
            log.error(String.format("参数id数组为空[%s]", id));
            return new JsonResult(false, "请勾选订单");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数id数组[%s]", id));
        }
        String[] ids = id.split(",");
        dealOriginalOrderService.analyzeOriginalOrder(ids);
        BusinessLogUtil.bindBusinessLog("解析异常订单ID:%s",id);
        return new JsonResult(true, "解析成功");
    }
}
