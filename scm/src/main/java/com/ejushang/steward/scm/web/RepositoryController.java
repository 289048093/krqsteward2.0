package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.service.EmployeeService;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.domain.Repository;
import com.ejushang.steward.ordercenter.service.RepositoryService;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * User: tin
 * Date: 14-4-8
 * Time: 下午4:20
 */
@Controller
@RequestMapping("/repository")
public class RepositoryController extends BaseController {
    private static final Logger logger = Logger.getLogger(RepositoryController.class);
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private EmployeeService employeeService;


    @OperationLog("添加仓库")
    @RequestMapping("/add")
    @ResponseBody
    JsonResult add(Repository repository,Integer[] chargerIds) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数Repository:[%s]" , repository.toString()));
        }
        repository.setId(null);
        repositoryService.add(repository,chargerIds);
        BusinessLogUtil.bindBusinessLog("仓库详情[名称:%s,编码:%s,地址:%s,物流公司:%s]",
                repository.getName(),repository.getCode(),repository.getAddress(),repository.getShippingComp());

        return new JsonResult(true, "保存成功");
    }


    @OperationLog("更新仓库")
    @RequestMapping("/save")
    @ResponseBody
    JsonResult save(@ModelAttribute("id")Repository repository,Integer[] chargerIds) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数Repository[%s]" , repository.toString()));
        }
        repositoryService.save(repository, chargerIds);

        BusinessLogUtil.bindBusinessLog("更新后仓库详情[名称:%s,编码:%s,地址:%s,物流公司:%s]",
                repository.getName(),repository.getCode(),repository.getAddress(),repository.getShippingComp());

        return new JsonResult(true, "更新成功");
    }

    @OperationLog("批量删除仓库")
    @RequestMapping("/delete")
    @ResponseBody
    JsonResult deleteById(String id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数Integer类型的id[%s]" ,id));
        }
        repositoryService.deleteById(id);

        BusinessLogUtil.bindBusinessLog(true,"删除仓库，仓库ID:"+id);

        return new JsonResult(true, "删除成功");
    }
    @RequestMapping("/find_all")
    @ResponseBody
    JsonResult findRepositoryAll() {

        return new JsonResult(true).addList(repositoryService.findRepositoryAll());
    }

    @RequestMapping("/list")
    @ResponseBody
    JsonResult findRepository(String name , HttpServletRequest request) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("参数仓库名name[%s]" , name));
        }
        Page page = PageFactory.getPage(request);
        repositoryService.findRepository(name, page);
        return new JsonResult(true).addObject(page);

    }

    @RequestMapping("/findUser")
    @ResponseBody
    public JsonResult findUser(String userName,String name){
    return new JsonResult(true).addList(employeeService.findEmployeeByName(userName,name));
    }


    @RequestMapping("/getRepoEmployee")
    @ResponseBody
    public JsonResult getRepoEmployee(Integer repoId){
        List<Employee> employees= repositoryService.getRepoEmployee(repoId);
        return new JsonResult(true,"操作成功！").addList(employees);
    }

}
