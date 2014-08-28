package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.service.FinancialService;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * User:moon
 * Date: 14-6-17
 * Time: 下午2:50
 */
@Controller
@RequestMapping("/financial")
public class FinancialController extends BaseController {

    @Autowired
    private FinancialService financialService;

    @OperationLog("财务人员导出财务数据")
    @RequestMapping("/extract2excel")
    @ResponseBody
    public JsonResult extract2Excel(HttpServletResponse response,String searchTimeType,String startPayTime, String endPayTime, String platformType, Integer shopId,Integer repoId,String status) throws IOException {
        OutputStream os = response.getOutputStream();
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR) + "Item.xls");
            response.setContentType("application/octet-stream; charset=utf-8");
            Workbook workbook = financialService.reportOrderItem(searchTimeType,startPayTime,endPayTime,platformType,shopId,repoId,status);
            workbook.write(os);
            os.flush();
            return new JsonResult(true,"导出成功！");
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
    @OperationLog("仓库人员导出财务数据")
    @RequestMapping("/extractMerger2excel")
    @ResponseBody
    public JsonResult extractMerger2Excel(HttpServletResponse response,String searchTimeType,String startPayTime, String endPayTime, String platformType, Integer shopId,Integer repoId,String status) throws IOException {
        OutputStream os = response.getOutputStream();
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR) + "MergerItem.xls");
            response.setContentType("application/octet-stream; charset=utf-8");
            Workbook workbook = financialService.reportMergerOrderItem(searchTimeType, startPayTime, endPayTime, platformType, shopId, repoId, status);
            workbook.write(os);
            os.flush();
            return new JsonResult(true,"导出成功！");
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult financialOrder(HttpServletRequest request,String searchTimeType,String startPayTime, String endPayTime, String platformType, Integer shopId,Integer repoId,String status) {
        Page page=PageFactory.getPage(request);
        financialService.financialOrder(searchTimeType,startPayTime,endPayTime,platformType,shopId,repoId,status,page);
        return new JsonResult(true, "操作成功").addObject(page);
    }

}
