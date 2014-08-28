package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.constant.AfterSalesStatus;
import com.ejushang.steward.ordercenter.domain.AfterSales;
import com.ejushang.steward.ordercenter.service.AfterSalesService;
import com.ejushang.steward.ordercenter.vo.AfterSalesVo;
import com.ejushang.steward.ordercenter.vo.ReceiveGoodsVo;
import com.ejushang.steward.ordercenter.vo.ReturnGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

/**
 * 售后模块
 *
 * @Author Channel
 * @Date 2014/8/12
 * @Version: 1.0
 */
@Controller
@RequestMapping("/afterSales")
public class AfterSalesController {

    @Autowired
    private AfterSalesService afterSalesService;

    @RequestMapping("/save")
    @ResponseBody
    public JsonResult save(AfterSalesVo afterSalesVo) {
        Integer id = afterSalesService.saveAfterSales(afterSalesVo);
        return new JsonResult(true, "添加成功").addObject(id);
    }

    @RequestMapping("/after_sales_detail")
    @ResponseBody
    public JsonResult findDetails(HttpServletRequest request) throws ParseException {
        @SuppressWarnings("unchecked")
        Map<String, Object[]> map = request.getParameterMap();
        Page page = PageFactory.getPage(request);
        return new JsonResult(true).addObject(afterSalesService.findDetails(map, page));
    }

    @RequestMapping("/on_pay_after_sales_refund")
    @ResponseBody
    public JsonResult onPayAfterSalesRefund(Integer id) {
        afterSalesService.onPayAfterSalesRefund(id);
        return new JsonResult(true, "确认支付成功");
    }


    @RequestMapping("/get")
    @ResponseBody
    public JsonResult get(Integer id) {
        AfterSales afterSales = afterSalesService.getAfterSales(id);
        if (afterSales != null) {
            return new JsonResult(true, "获取成功").addObject(afterSales);
        } else {
            return new JsonResult(false, "售后工单不存在");
        }
    }

    @RequestMapping("/flow")
    @ResponseBody
    public JsonResult flow(Integer afterSalesId, AfterSalesStatus status, String remarks) {
        afterSalesService.flow(afterSalesId, status, remarks);
        return new JsonResult(true, "成功修改状态");
    }

    @RequestMapping("/confirmGoods")
    @ResponseBody
    public JsonResult confirmGoods(ReturnGoodsVo returnGoodsVo, Boolean confirm) {
        afterSalesService.confirmGoods(returnGoodsVo, confirm != null ? confirm : false);
        return new JsonResult(true, "操作成功");
    }

    @RequestMapping("/receiveGoods")
    @ResponseBody
    public JsonResult receiveGoods(ReceiveGoodsVo receiveGoodsVo, Boolean confirm) {
        afterSalesService.receiveGoods(receiveGoodsVo, confirm != null ? confirm : false);
        return new JsonResult(true, "操作成功");
    }

    @RequestMapping("/buildOrder")
    @ResponseBody
    public JsonResult buildOrder(Integer afterSalesId) {
        afterSalesService.buildOrder(afterSalesId);
        return new JsonResult(true, "成功生成发货订单");
    }

    @RequestMapping("/cancel")
    @ResponseBody
    public JsonResult cancel(Integer afterSalesId, String remarks) {
        afterSalesService.flow(afterSalesId, AfterSalesStatus.CANCEL, remarks);
        return new JsonResult(true, "成功作废售后单");
    }
}
