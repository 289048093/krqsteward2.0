package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.service.BusinessLogService;
import com.ejushang.steward.scm.common.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * User:moon
 * Date: 14-4-9
 * Time: 下午5:48
 */
@Controller
@RequestMapping("/businessLog")
public class BusinessLogController extends BaseController{

    private static final Logger log= LoggerFactory.getLogger(BusinessLogController.class);

    @Autowired
    private BusinessLogService businessLogService;

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request, String paramType,String param,String createTimeStart,String createTimeEnd) {
        if(log.isInfoEnabled()){
            log.info("findAllBusinessLog接收到的paramType为："+paramType+"param为："+param);
        }
        Page page = PageFactory.getPage(request);
        businessLogService.findAllBusinessLog(paramType,param,createTimeStart,createTimeEnd,page);
        return new JsonResult(true,"查询成功!").addObject(page);

    }


}
