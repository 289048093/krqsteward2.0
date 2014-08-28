package com.ejushang.steward.scm.web.returnvisit;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.service.EmployeeService;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.constant.BlacklistType;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.returnvisit.ReturnVisitSearchCondition;
import com.ejushang.steward.ordercenter.returnvisit.ReturnVisitService;
import com.ejushang.steward.ordercenter.service.BlacklistService;
import com.ejushang.steward.ordercenter.service.OrderService;
import com.ejushang.steward.ordercenter.service.ReasonCodeService;
import com.ejushang.steward.ordercenter.util.OrderUtil;
import com.ejushang.steward.ordercenter.vo.ApportionPersonVo;
import com.ejushang.steward.ordercenter.vo.OrderVo;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.uams.exception.UamsClientException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by: codec.yang
 * Date: 2014/8/7 10:59
 */
@Controller
@RequestMapping("/returnvisit")
public class ReturnVisitController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ReturnVisitController.class);

    @Autowired
    private ReturnVisitService returnVisitService;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ReasonCodeService reasonCodeService;

    @Autowired
    private OrderService orderService;


    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(@ModelAttribute("returnVisitSearchCondition") ReturnVisitSearchCondition condition, HttpServletRequest request) {
        Page page = PageFactory.getPage(request);
        this.returnVisitService.findAllReturnVisitTask(condition,page);
        return new JsonResult(true).addObject(page);
    }


    @RequestMapping("/findReturnVisitLog")
    @ResponseBody
    public JsonResult findReturnVisitLog(Integer taskId){
        List<ReturnVisitLog> list=this.returnVisitService.findReturnVisitLogByTaskId(taskId);
        Collections.sort(list,new Comparator<ReturnVisitLog>() {
            @Override
            public int compare(ReturnVisitLog o1, ReturnVisitLog o2) {
                if(o1.getCreateTime()==null)
                    return -1;
                if(o2.getCreateTime()==null){
                    return 1;
                }
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            }
        });
        return new JsonResult(true).addList(list);
    }

    @RequestMapping("/getOrderById")
    @ResponseBody
    public JsonResult getOrderById(Integer orderId){
        Order order=this.orderService.findOrderById(orderId);
        OrderVo vo=OrderUtil.getOrderVo(order);
        return new JsonResult(true).addObject(vo);
    }


    @RequestMapping("/saveReturnVisitLog")
    @ResponseBody
    public JsonResult saveReturnVisitLog(@ModelAttribute("returnVisitLog") ReturnVisitLog returnVisitLog,
                                         @RequestParam("reasonCodeStr") String reasonCodeStr){
        if(returnVisitLog==null|| NumberUtil.isNullOrLessThanOne(returnVisitLog.getTaskId())||returnVisitLog.getDialStatus()==null){
            return new JsonResult(false,"缺少必填数据");
        }

        List<ReasonCode> reasonCodes=this.getReasonCodes(reasonCodeStr);
        returnVisitLog.setReasonCodes(new HashSet<ReasonCode>(reasonCodes));

        this.returnVisitService.saveReturnVisitLog(returnVisitLog);
        if(Boolean.TRUE.equals(returnVisitLog.getAddToBlacklist())){
            ReturnVisitTask task=this.returnVisitService.getReturnVisitTaskById(returnVisitLog.getTaskId());
            if(task!=null&& StringUtils.isNotBlank(task.getPhone())){
                this.blacklistService.saveOrUpdateBlacklistIfNotExist(new Blacklist(BlacklistType.PHONE, task.getPhone()));
            }
        }
        return new JsonResult(true);
    }

    private List<ReasonCode> getReasonCodes(String reasonCodeStr){
        if(StringUtils.isNotBlank(reasonCodeStr)){
            String[] reasonCodes= reasonCodeStr.split("[,， ]");
            Set<String> reasonCodeSet= new HashSet<String>(reasonCodes.length);
            for(String code:reasonCodes){
                if(StringUtils.isNotBlank(code)){
                    reasonCodeSet.add(code.trim());
                }
            }

            List<ReasonCode> reasonCodeList=this.reasonCodeService.findReasonCodeByCodeIn(reasonCodeSet);
            if(reasonCodeList.size()!=reasonCodeSet.size()){
                throw new StewardBusinessException("存在未定义的原因码");
            }

            return reasonCodeList;

        }

        return Collections.emptyList();
    }

    @RequestMapping("/redirectAfterSaleList")
    @ResponseBody
    public JsonResult listRedirectAfterSaleReturnVisit(@ModelAttribute("returnVisitSearchCondition") ReturnVisitSearchCondition condition, HttpServletRequest request) {
        Page page = PageFactory.getPage(request);
        this.returnVisitService.findRedirectAfterSaleReturnVisitTask(condition, page);
        return new JsonResult(true).addObject(page);
    }


    @RequestMapping("/saveAfterSaleLog")
    @ResponseBody
    public JsonResult saveAfterSaleLog(@ModelAttribute ReturnVisitAfterSalesLog returnVisitAfterSalesLog) {
        this.returnVisitService.saveReturnVisitAfterSalesLog(returnVisitAfterSalesLog);
        return new JsonResult(true);
    }

    @RequestMapping("/findAfterSaleLog")
    @ResponseBody
    public JsonResult findAfterSaleLog(Integer returnVisitId) {
        List<ReturnVisitAfterSalesLog> logList=this.returnVisitService.findReturnVisitAfterSalesLog(returnVisitId);
        Collections.sort(logList,new Comparator<ReturnVisitAfterSalesLog>() {
            @Override
            public int compare(ReturnVisitAfterSalesLog o1, ReturnVisitAfterSalesLog o2) {
                if(o1.getCreateTime()==null)
                    return -1;
                if(o2.getCreateTime()==null){
                    return 1;
                }
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            }
        });
        return new JsonResult(true).addList(logList);
    }

    //分配任务
    @RequestMapping("/apportionReturnVisitTask")
    @ResponseBody
    public JsonResult apportionReturnVisitTask(Integer[] taskIds,String apportionPersonVos) throws IOException {

        if (!apportionPersonVos.startsWith("[")) {
            apportionPersonVos = "[" + apportionPersonVos + "]";
        }
        List<ApportionPersonVo> apportionPersonVos1 = JsonUtil.jsonToList(apportionPersonVos, ApportionPersonVo.class);
        returnVisitService.apportionReturnVisitTask(taskIds,apportionPersonVos1);
        return new JsonResult(true,"分配成功!");
    }

    @RequestMapping("/findVisiter")
    @ResponseBody
    public JsonResult findVisiter(String roleName) throws UamsClientException {
        List<Employee> employees=employeeService.findEmployeeByRole(roleName); //回访专员
        return new JsonResult(true).addList(employees);
    }

    @RequestMapping("/findEmp")
    @ResponseBody
    public JsonResult findEmp(String searchType,String searchValue) throws UamsClientException {
        List<Employee> employees=employeeService.findEmp(searchType,searchValue);
        return new JsonResult(true).addList(employees);
    }


}
