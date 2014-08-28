package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.domain.OrderItem;
import com.ejushang.steward.ordercenter.service.PaymentService;
import com.ejushang.steward.ordercenter.util.PaymentSearchCondition;
import com.ejushang.steward.ordercenter.vo.OrderItemVo;
import com.ejushang.steward.ordercenter.vo.PaymentOrderItemVo;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-5-8
 * Time: 下午4:42
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class PaymentController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(value = "/payment/list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request, PaymentSearchCondition paymentSearchCondition) {
        Page page = PageFactory.getPage(request);
        paymentService.findByKey(paymentSearchCondition, page);
        return new JsonResult(true).addObject(page);
    }

    @RequestMapping(value = "/payment/orderItem")
    @ResponseBody
    public JsonResult orderItemList(Integer id) {
        List<OrderItemVo> orderItemVos = paymentService.findOrderItemByPaymentId(id);
        return new JsonResult(true).addObject(orderItemVos);
    }

    @RequestMapping(value = "/payment/isOrderItemLegal")
    @ResponseBody
    public JsonResult isOrderItemLegal(Integer paymentId, Integer orderItemId) {
        try {
            OrderItemVo orderItemVo1 = paymentService.isOrderItemLegal(paymentId, orderItemId);
            return new JsonResult(true).addObject(orderItemVo1);
        } catch (StewardBusinessException e) {
            return new JsonResult(false, e.getMessage());
        }

    }

//
//    @RequestMapping(value = "/payment/delete")
//    @ResponseBody
//    @OperationLog("删除一项分配记录")
//    public JsonResult deletePAandUpdateprice(Integer paymentId, Integer orderItemId) {
//        paymentService.deletePAandUpdateprice(paymentId, orderItemId);
//        return new JsonResult(true, "删除成功");
//    }


    @RequestMapping(value = "/payment/save")
    @ResponseBody
    @OperationLog("保存分配结果")
    public JsonResult distributionPayment(Integer id, String orderItems) throws IOException {
        if (!orderItems.startsWith("[")) {
            orderItems = "[" + orderItems + "]";
        }
        if (orderItems.equals("[null]")) {
            paymentService.distributionPayment(id, null, null, null);
            return new JsonResult(true, "预收款记录删除成功");
        } else {
            PaymentOrderItemVo[] paymentOrderItemVos = JsonUtil.jsonToArray(orderItems, PaymentOrderItemVo[].class);
            List<OrderItem> orderItemList = new ArrayList<OrderItem>();
            List<Money> fees = new ArrayList<Money>();
            List<Money> refundFees = new ArrayList<Money>();
            orderItemList = paymentService.getOrderItem(paymentOrderItemVos);
            for (int i = 0; i < paymentOrderItemVos.length; i++) {
                Money fee = Money.valueOf(paymentOrderItemVos[i].getFeesString());
                Money refundFee = Money.valueOf(paymentOrderItemVos[i].getRefundFeesString());
                fees.add(fee);
                refundFees.add(refundFee);
            }
            try {
                paymentService.distributionPayment(id, orderItemList, fees, refundFees);
                return new JsonResult(true, PaymentService.DISTRIBUTION_SUCCESS);
            } catch (StewardBusinessException e) {
                log.error("分配失败", e);
                return new JsonResult(false, e.getMessage());
            }
        }
    }

    @RequestMapping("/payment/extract2excel")
    @ResponseBody
    public JsonResult extract2Excel(HttpServletResponse response,PaymentSearchCondition paymentSearchCondition) throws IOException {
        OutputStream os = response.getOutputStream();
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=Payment_"+ EJSDateUtils.formatDate(new Date(), EJSDateUtils.DateFormatType.DATE_USA_STYLE)+".xls");
            response.setContentType("application/octet-stream; charset=utf-8");
            Workbook workbook = paymentService.reportPayment(paymentSearchCondition);
            workbook.write(os);
            os.flush();
            return new JsonResult(true);
        } finally {
            if (os != null) {
                os.close();
            }
        }

    }

}
