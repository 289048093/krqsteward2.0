package com.ejushang.steward.scm.web;

import Vo.CustomerVo;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.customercenter.domain.Customer;
import com.ejushang.steward.customercenter.domain.UpdateCustomer;
import com.ejushang.steward.customercenter.service.CustomerService;
import com.ejushang.steward.customercenter.service.CustomerTagService;
import com.ejushang.steward.ordercenter.constant.BlacklistType;
import com.ejushang.steward.scm.common.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * User:moon
 * Date: 14-8-6
 * Time: 下午5:49
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    private static final Logger log= LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request) {
        Map<String, Object[]> map = request.getParameterMap();
        Page page = PageFactory.getPage(request);
        customerService.findAllCustomer(map, page);
        return new JsonResult(true,"查询成功!").addObject(page);
    }

    @RequestMapping("/addTagToCustomer")
    @ResponseBody
    public JsonResult addTagToCustomer(Integer customerId,Integer[] tagIds) {
        customerService.addTagToCustomer(customerId,tagIds);
        return new JsonResult(true,"操作成功!");
    }

    @RequestMapping("/addTagToCustomers")
    @ResponseBody
    public JsonResult addTagToCustomers(Integer[] customerIds,Integer[] tagIds) {
        customerService.addTagToCustomers(customerIds, tagIds);
        return new JsonResult(true,"操作成功!");
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult update(UpdateCustomer updateCustomer,Integer[] tagIds,String mobile) {
        customerService.updateCustomer(updateCustomer, tagIds, mobile);
        return new JsonResult(true,"操作成功!");
    }

    @RequestMapping("/addCustomerToBlacklist")
    @ResponseBody
    public JsonResult addCustomerToBlacklist(String mobile,BlacklistType type) {
        customerService.addCustomerToBlacklist(mobile,type);
        return new JsonResult(true,"操作成功!");
    }

    @RequestMapping("/getCustomerByMobile")
    @ResponseBody
    public JsonResult getCustomerByMobile(String mobile) {
        if(StringUtils.isBlank(mobile)){
            return new JsonResult(true,"手机号不能为空!");
        }
        Customer customer=this.customerService.getCustomerByMobile(mobile);
        return new JsonResult(true).addObject(customer);
    }

    @RequestMapping("/getCustomerDescription")
    @ResponseBody
    public JsonResult getCustomerDescription(String mobile) {
        if(StringUtils.isBlank(mobile)){
            return new JsonResult(true,"手机号不能为空!");
        }
        CustomerVo customerVo= customerService.getCustomerDescription(mobile);
        return new JsonResult(true).addObject(customerVo);
    }
}
