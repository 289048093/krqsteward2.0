package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Shiro
 * Date: 14-4-8
 * Time: 下午4:42
 */
@Controller
public class OrderFetchController {
    private static final Logger log = LoggerFactory.getLogger(OrderFetchController.class);

    @Autowired
    private OrderFetchService orderFetchService;

    /**
     *获取所有抓单记录
     * @param request
     * @return
     */
    @RequestMapping("/orderFetch/list")
    @ResponseBody
    public JsonResult findAll(HttpServletRequest request) {
        if (log.isInfoEnabled()) {
            log.info("OrderFetchController里的findAll没设置参数");
        }
        Page page = PageFactory.getPage(request);
        orderFetchService.findAll(page);
        return new JsonResult(true).addObject(page);
    }
}
