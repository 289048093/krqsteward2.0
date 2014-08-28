package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.scm.common.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * User: liubin
 * Date: 14-6-9
 * Time: 下午2:46
 */
@Controller
public class IndexController extends BaseController {

    static final Logger logger = LoggerFactory.getLogger(IndexController.class);


    /**
     * @param request
     * @return
     */
    @RequestMapping("/notexist")
    public String list(HttpServletRequest request) {
        return "redirect:/index.html";
    }


    @RequestMapping("/static/ctx_info")
    public String getCtxInfo(HttpSession session) {
        Employee employee = SessionUtils.getEmployee();
        if(employee != null) {
            session.setAttribute("employee", employee);
        }
        return "/static/ctx_info";
    }
}
