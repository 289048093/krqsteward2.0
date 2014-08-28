package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.service.EmployeeService;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import com.ejushang.uams.api.dto.ResourceDto;
import com.ejushang.uams.exception.UamsClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 * User: liubin
 * Date: 14-3-11
 */
@Controller
public class EmployeeController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/employee/resource/list")
    @ResponseBody
    public JsonResult listResource() {
        Employee employee = SessionUtils.getEmployee();
        List<ResourceDto> resourceList = null;
        if(employee != null) {
            resourceList = employee.getResourceList();
        }
        return new JsonResult(true, "查询成功!").addList(resourceList == null ? new ArrayList<ResourceDto>() : resourceList);
    }
    @RequestMapping("/employee/list")
    @ResponseBody
    public JsonResult findEmpByName(String searchType,String searchValue) throws UamsClientException {

     return   new JsonResult(true).addList(employeeService.findEmpByName(searchType,searchValue));

    }

    @RequestMapping("/employee/resource")
    @ResponseBody
    public JsonResult findResourceListByEmployeeId(Integer employeeId) throws UamsClientException {

        return   new JsonResult(true).addList(employeeService.findResourceByEmployeeId(employeeId));

    }

    @OperationLog("修改密码")
    @RequestMapping("/employee/updatePassword")
    @ResponseBody
    public JsonResult updatePassword(Integer employeeId,String oldPassword,String newPassword) throws UamsClientException {
        try{
            Boolean b=employeeService.updatePassword(employeeId,oldPassword,newPassword);
            return new JsonResult(true,"更改成功！").addObject(b);

        }catch (Exception e){
            log.error(String.format("修改密码是发生异常，异常为[%s]",e.getMessage()));
            return new JsonResult(false,"原密码错误");
        }
    }

}
