package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.domain.Refund;
import com.ejushang.steward.ordercenter.service.RefundService;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Map;
import java.util.Date;

/**
 * User: Shiro
 * Date: 14-5-10
 * Time: 上午10:27
 */
@Controller
@RequestMapping("/refund")
public class RefundController extends BaseController {

    private static final Logger logger = Logger.getLogger(RefundController.class);

    @Autowired
    private RefundService refundService;

    @RequestMapping("/list")
    @ResponseBody
    JsonResult findOrderDetails(HttpServletRequest request) throws ParseException {
        @SuppressWarnings("unchecked")
        Map<String, Object[]> map = request.getParameterMap();
        Page page = PageFactory.getPage(request);
        refundService.findRefund(map, page);
        return new JsonResult(true).addObject(page);
    }

    @OperationLog("添加退款申请")
    @RequestMapping("/add")
    @ResponseBody
    public JsonResult saveRefund(Integer oldItemId, @ModelAttribute("id") Refund refund) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数是oldItemId[{}]和Refund[{}]", oldItemId, refund));
        }
        refundService.saveRefund(oldItemId, refund);
        BusinessLogUtil.bindBusinessLog("退款订单项ID[%s],退款单号[%s],退款状态[%s],退款金额[%s],退款原因[%s],备注[%s]",
                oldItemId,refund.getPlatformRefundNo(),refund.getStatus(),refund.getActualRefundFee(),refund.getReason(),refund.getRemark());
        return new JsonResult(true, "保存成功");
    }

    @OperationLog("修改退款申请")
    @RequestMapping("/update")
    @ResponseBody
    public JsonResult updateRefund(Integer id, Refund refund) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数是id[{}]和refund[{}]", id, refund));
        }
        refundService.updateRefund(id, refund);
        BusinessLogUtil.bindBusinessLog("退款申请ID[%s],退款单号[%s],退款状态[%s],退款金额[%s],退款原因[%s],备注[%s]",
                id,refund.getPlatformRefundNo(),refund.getStatus(),refund.getActualRefundFee(),refund.getReason(),refund.getRemark());
        return new JsonResult(true, "修改成功");
    }

    @OperationLog("删除退款申请")
    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult deleteRefund(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数id[{}]", id));
        }
        refundService.deleteRefund(id);
        BusinessLogUtil.bindBusinessLog("退款申请ID：%s",id);
        return new JsonResult(true, "删除成功");
    }

    @OperationLog("作废退款申请")
    @RequestMapping("/cancellation")
    @ResponseBody
    public JsonResult cancellationRefund(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数id[{}]", id));
        }
        try {
            refundService.cancellationRefund(id);
            return new JsonResult(true, "作废成功");
        } catch (StewardBusinessException se) {
            return new JsonResult(false,se.getMessage());
        }
    }

    @RequestMapping("/getRefund")
    @ResponseBody
    public JsonResult findById(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数id[{}]", id));
        }
        return new JsonResult(true).addObject(refundService.getById(id));
    }

    @RequestMapping("/extract2excel")
    @ResponseBody
    public JsonResult extract2Excel(HttpServletResponse response,HttpServletRequest request) throws IOException{
        OutputStream os = response.getOutputStream();
        @SuppressWarnings("unchecked")
        Map<String, Object[]> map = request.getParameterMap();
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=Refund_"+ EJSDateUtils.formatDate(new Date(), EJSDateUtils.DateFormatType.DATE_USA_STYLE)+".xls");
            response.setContentType("application/octet-stream; charset=utf-8");
            Workbook workbook = refundService.reportRefund(map);
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
