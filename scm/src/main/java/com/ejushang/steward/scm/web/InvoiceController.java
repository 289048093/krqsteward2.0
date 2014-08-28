package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.logisticscenter.service.LogisticsPrintInfoService;
import com.ejushang.steward.ordercenter.service.InvoiceService;
import com.ejushang.steward.ordercenter.service.OrderService;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.commons.lang3.StringUtils;
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
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * User: Blomer
 * Date: 14-2-8
 * Time: 上午10:34
 * 发货管理Controller
 */

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private LogisticsPrintInfoService logisticsPrintInfoService;


    @Autowired
    private OrderService orderService;

    /**
     * 从 已验货||已打印||已确认 返回去 待处理
     *
     * @param orderIds
     * @throws java.io.IOException
     */
    @RequestMapping("/back_to_wait_process")
    @ResponseBody
    public JsonResult backToWaitProcess(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有传进参数");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("InvoiceController中的backToWaitProcess方法,参数orderIds[%s]", Arrays.toString(orderIds)));
        }
        invoiceService.orderBackToWaitProcess(orderIds);
        return new JsonResult(true);
    }

    /**
     * 根据orderIds 进行物流单或发货单的打印
     *
     * @param orderIds 订单id
     * @throws IOException
     */
    @RequestMapping("/delivery_print")
    @ResponseBody
    public JsonResult deliveryPrint(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有传进参数");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("InvoiceController中的deliveryPrint方法,参数orderIds[%s]", Arrays.toString(orderIds)));
        }
        List<Map<String, Object>> mapList = invoiceService.deliveryPrint(orderIds);
        return new JsonResult(true).addList(mapList);
    }

    /**
     * 根据orderIds 进行订单打印
     *
     * @param orderIds 订单id
     * @throws IOException
     */
    @RequestMapping("/order_print")
    @ResponseBody
    public JsonResult orderPrint(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有传进参数");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("InvoiceController中的orderPrint方法,参数orderIds[%s]", Arrays.toString(orderIds)));
        }
        List<Map<String, Object>> mapList = invoiceService.orderPrint(orderIds);
        return new JsonResult(true).addList(mapList);
    }

    /**
     * 确认打印
     *
     * @param orderIds
     * @throws IOException
     */
    @RequestMapping("/affirm_print")
    @ResponseBody
    public JsonResult orderAffirmPrint(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有传进参数");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("InvoiceController中的orderAffirmPrint方法,参数orderIds[%s]", Arrays.toString(orderIds)));
        }
        invoiceService.orderAffirmPrint(orderIds);
        return new JsonResult(true, "确认打印成功");
    }

    /**
     * 返回已导入 即 返回已确认
     *
     * @param orderIds
     * @throws IOException
     */
    @RequestMapping("/back_to_confirm")
    @ResponseBody
    public JsonResult orderBackToConfirm(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有传进参数");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("InvoiceController中的orderBackToConfirm方法,参数orderIds[%s]", Arrays.toString(orderIds)));
        }
        invoiceService.orderBackToConfirm(orderIds);
        return new JsonResult(true);
    }

    /**
     * 批量验货
     *
     * @param orderIds
     * @throws IOException
     */
    @RequestMapping("/batch_examine")
    @ResponseBody
    public JsonResult orderBatchExamine(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有传进参数");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("InvoiceController中的orderBatchExamine方法,参数orderIds[%s]", Arrays.toString(orderIds)));
        }
        String warnMsg = invoiceService.orderBatchExamine(orderIds);
        if (StringUtils.isNotBlank(warnMsg)) {
            return new JsonResult(false, warnMsg);
        }
        return new JsonResult(true);
    }

    /**
     * 根据 shippingNos 物流编号 进行验货
     *
     * @param shippingNos
     * @throws IOException
     */
    @RequestMapping("/inspection")
    @ResponseBody
    public JsonResult orderInspection(String[] shippingNos) {
        if (null == shippingNos || shippingNos.length == 0) {
            return new JsonResult(false, "没有传进参数");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("InvoiceController中的orderInspection方法,参数orderIds[%s]", shippingNos));
        }
        /*List<Order> orderList = invoiceService.orderInspection(shippingNos);
        return new JsonResult(true).addList(orderList);*/
        List<Map<String, Object>> orderMapList = invoiceService.orderInspection(shippingNos);
        return new JsonResult(true).addList(orderMapList);
    }

    /**
     * 返回已打印
     *
     * @param orderIds
     * @throws IOException
     */
    @RequestMapping("/back_to_print")
    @ResponseBody
    public JsonResult orderBackToPrint(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有传进参数");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("InvoiceController中的orderBackToPrint方法,参数orderIds[%s]", Arrays.toString(orderIds)));
        }
        invoiceService.orderBackToPrint(orderIds);
        return new JsonResult(true);
    }

    /**
     * 确认发货
     *
     * @param orderIds
     * @throws IOException
     */
    @RequestMapping("/invoice")
    @ResponseBody
    public JsonResult orderInvoice(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有传进参数");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("InvoiceController中的orderInvoice方法,参数orderIds[%s]", Arrays.toString(orderIds)));
        }
        invoiceService.orderInvoice(orderIds);
        return new JsonResult(true);
    }

    /**
     * 联想物流编号
     *
     * @param orderIds
     * @throws IOException
     */
    @RequestMapping("/OrderShipping/update")
    @ResponseBody
    @OperationLog("联想物流编号")
    public JsonResult updateOrderShipping(Integer orderIds[], String shippingComp, String intNo, String isCover) {
        logisticsPrintInfoService.updateOrderShipping(orderIds, shippingComp, intNo, isCover);
        BusinessLogUtil.bindBusinessLog("联想物流编号，订单ID：%s,物流公司:%s,是否覆盖:%s,基数:%s",
                Arrays.toString(orderIds),shippingComp,isCover,intNo
        );
        return new JsonResult(true, "操作成功");
    }


    /**
     * @param request
     * @return
     */
    @RequestMapping("/list_invoice_orders")
    @ResponseBody
    public JsonResult listInvoiceOrders(HttpServletRequest request) throws ParseException {
        Map<String, Object[]> map = request.getParameterMap();
        Page page = PageFactory.getPage(request);
        invoiceService.listInvoiceOrder(map, page);
        return new JsonResult(true, "操作成功").addObject(page);
    }

    @RequestMapping("/list_invoice_report_orders")
    @ResponseBody
    public void listInvoiceReportOrders(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Map<String, Object[]> map = request.getParameterMap();
      Workbook workbook= invoiceService.reportInvoiceOrders(map);
        OutputStream os = response.getOutputStream();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR) + "ReportOrders.xls");
        response.setContentType("application/octet-stream; charset=utf-8");
        workbook.write(os);
        os.flush();
        os.close();
    }

    /**
     * @param orderId
     * @return
     */
    @RequestMapping("/list_invoice_order_item")
    @ResponseBody
    public JsonResult listInvoiceOrderItem(Integer orderId) {
        return new JsonResult(true, "操作成功").addList(invoiceService.listInvoiceOrderItemByOrderId(orderId));
    }

    /**
     * 签收
     *
     * @param orderIds
     * @return
     */
    @RequestMapping("/signed")
    @ResponseBody
    public JsonResult signed(Integer[] orderIds) {
        if (orderIds == null || orderIds.length == 0) {
            throw new StewardBusinessException("没有传入有效参数");
        }
        invoiceService.signed(orderIds);
        return new JsonResult(true);
    }

    @RequestMapping("/collect_invoice_order_excel")
    @ResponseBody
    public void collectInvoiceOrderExcel(String orderIds, HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
        OutputStream os = response.getOutputStream();
        Map<String, Object[]> map = request.getParameterMap();
        Workbook workbook = invoiceService.collectInvoiceOrderExcel(map);
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR) + "collectInvoiceOrderExcel.xls");
            response.setContentType("application/octet-stream; charset=utf-8");
            workbook.write(os);
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }

    }
}
