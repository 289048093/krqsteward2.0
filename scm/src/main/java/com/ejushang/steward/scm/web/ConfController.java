package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.domain.Conf;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.service.ConfService;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
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

/**
 * User:moon
 * Date: 14-4-14
 * Time: 下午8:27
 */
@Controller
@RequestMapping("/conf")
public class ConfController extends BaseController {

    @Autowired
    private ConfService confService;

    /**
     * 查询配置
     * @param request
     * @param conf
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResult findAllConf(HttpServletRequest request,Conf conf){
        Page page= PageFactory.getPage(request);
        confService.findAllConf(page,conf);
        return new JsonResult(true).addObject(page);
    }

    /**
     * 添加配置
     * @param conf
     * @return
     */
    @RequestMapping("/addOrUpdate")
    @ResponseBody
    @OperationLog("修改配置")
    public JsonResult saveConf(@ModelAttribute("id")Conf conf){
        confService.save(conf);

        BusinessLogUtil.bindBusinessLog("配置参数详情：[%s=%s]",conf.getName(),conf.getValue());

        return new JsonResult(true,"操作成功！");
    }

    /**
     * 查询配置详细
     */
    @RequestMapping("/findConfById")
    @ResponseBody
    public JsonResult findConfById(Integer id){
        Conf conf=confService.findConf(id);
        return new JsonResult(true).addObject(conf);
    }

    @RequestMapping("/extract2excel")
    @ResponseBody
    public JsonResult extract2Excel(HttpServletResponse response) throws IOException {
        OutputStream os = response.getOutputStream();
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=Order&Item.xls");
            response.setContentType("application/octet-stream; charset=utf-8");
            Workbook workbook = confService.testPoi();
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
