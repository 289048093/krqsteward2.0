package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.customercenter.domain.CustomerTag;
import com.ejushang.steward.customercenter.service.CustomerTagService;
import com.ejushang.steward.scm.common.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * User:moon
 * Date: 14-8-6
 * Time: 下午4:48
 */
@Controller
@RequestMapping("/customerTag")
public class CustomerTagController extends BaseController {

    private static final Logger log= LoggerFactory.getLogger(CustomerTagController.class);

    @Autowired
    private CustomerTagService customerTagService;

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request, String name) {
        if(log.isInfoEnabled()){
            log.info("findCustomerTag接收到的name为："+name);
        }
        Page page = PageFactory.getPage(request);
        customerTagService.findCustomerTag(name, page);
        return new JsonResult(true,"查询成功!").addObject(page);
    }

    @RequestMapping("/save")
    @ResponseBody
    public JsonResult save(@ModelAttribute("id") CustomerTag customerTag){
        customerTagService.saveTag(customerTag);
        return new JsonResult(true,"操作成功！");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult delete(Integer[] ids){
        customerTagService.deleteTag(ids);
        return new JsonResult(true,"操作成功！");
    }

    @RequestMapping("/listNoPage")
    @ResponseBody
    public JsonResult listNoPage() {
        List<CustomerTag> customerTagList=customerTagService.findAllCustomerTagNoPage();
        return new JsonResult(true,"查询成功!").addList(customerTagList);
    }

}
