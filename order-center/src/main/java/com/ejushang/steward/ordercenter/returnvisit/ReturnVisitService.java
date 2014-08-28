package com.ejushang.steward.ordercenter.returnvisit;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.ReturnVisitAfterSales;
import com.ejushang.steward.ordercenter.domain.ReturnVisitAfterSalesLog;
import com.ejushang.steward.ordercenter.domain.ReturnVisitLog;
import com.ejushang.steward.ordercenter.domain.ReturnVisitTask;
import com.ejushang.steward.ordercenter.util.AuthorityUtil;
import com.ejushang.steward.ordercenter.vo.ApportionPersonVo;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by: codec.yang
 * Date: 2014/8/6 16:36
 */
@Service
@Transactional(readOnly = true)
public class ReturnVisitService {

    private static final Logger log= LoggerFactory.getLogger(ReturnVisitService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Transactional(readOnly = false)
    public void saveOrUpdateReturnVisitTask(ReturnVisitTask task) {
        generalDAO.saveOrUpdate(task);
    }

    public ReturnVisitTask getReturnVisitTaskById(Integer taskId){
        return this.generalDAO.get(ReturnVisitTask.class,taskId);
    }

    public List<ReturnVisitLog> findReturnVisitLogByTaskId(Integer taskId) {
        return this.generalDAO.search(new Search(ReturnVisitLog.class).addFilterEqual("taskId",taskId));
    }

    private boolean checkCurrentUserAuthorityForTask(String visitorName){
        Employee employee= AuthorityUtil.getCurrentEmployee();
        if(AuthorityUtil.hasRole(AuthorityUtil.RETURN_VISIT_SUPERVISOR_ROLE_NAME)
                ||employee.getName().equalsIgnoreCase(visitorName)){
            return true;
        }

        return false;
    }

    @Transactional(readOnly = false)
    public void saveReturnVisitLog(ReturnVisitLog returnVisitLog) {
        ReturnVisitTask task= this.getReturnVisitTaskById(returnVisitLog.getTaskId());

        if(task==null){
            throw new StewardBusinessException("回访任务["+returnVisitLog.getTaskId()+"]已不存在");
        }

        if(!checkCurrentUserAuthorityForTask(task.getVisitorName())){
            throw new StewardBusinessException("当前用户没有所选回访任务的权限");
        }

        switch(returnVisitLog.getDialStatus()){
            case CONNECTED:
                if(returnVisitLog.getAppointmentTime()==null){
                    returnVisitLog.setStatus(ReturnVisitStatus.SUCCEED);
                    task.setStatus(ReturnVisitStatus.SUCCEED);
                }else{
                    returnVisitLog.setStatus(ReturnVisitStatus.APPOINTMENT);
                    task.setStatus(ReturnVisitStatus.APPOINTMENT);
                }
                break;
            case REJECTED:
                returnVisitLog.setStatus(ReturnVisitStatus.REJECTED);
                task.setStatus(ReturnVisitStatus.REJECTED);
                break;
            default:
                returnVisitLog.setStatus(ReturnVisitStatus.FAILED);
                if(this.countVisitLogExceptConnectedAndRejectedStatus(returnVisitLog.getTaskId())>=2){
                    task.setStatus(ReturnVisitStatus.FAILED);
                }
        }

        returnVisitLog.setVisitorRealname(AuthorityUtil.getCurrentEmployee().getName());
        returnVisitLog.setVisitorName(AuthorityUtil.getCurrentEmployee().getUsername());


        if(isRedirectToAfterSale(returnVisitLog)){
            task.setRedirectAfterSale(true);
            if(task.getAfterSaleStatus()==null){
                task.setAfterSaleStatus(Progress.WAITING);
            }
        }else{
            returnVisitLog.setRedirectAfterSale(false);
        }

        Date now=new Date();
        task.setUpdateTime(now);
        task.setLastVisitTime(now);
        returnVisitLog.setCreateTime(now);
        task.setUsed(returnVisitLog.getUsed());
        this.generalDAO.saveOrUpdate(returnVisitLog);
    }

    private boolean isRedirectToAfterSale(ReturnVisitLog returnVisitLog) {

        return Boolean.TRUE.equals(returnVisitLog.getRedirectAfterSale())
                &&returnVisitLog.getDialStatus()==DialStatus.CONNECTED
                &&returnVisitLog.getAppointmentTime()==null;
    }


    private int countVisitLogExceptConnectedAndRejectedStatus(Integer taskId) {
        Preconditions.checkNotNull(taskId);
        Search search=new Search(ReturnVisitLog.class)
                .addFilterEqual("taskId", taskId)
                .addFilterNotIn("dialStatus", DialStatus.CONNECTED, DialStatus.REJECTED);
        return this.generalDAO.count(search);

    }

    public int countReturnVisitTimesByMobile(String mobile, int days, ReturnVisitType type, ReturnVisitStatus status) {
        Preconditions.checkArgument(StringUtils.isNotBlank(mobile),"Phone is not allowed to be empty.");

        Search search=new Search(ReturnVisitTask.class);
        search.addFilterEqual("mobile", mobile);

        if(days!=0){
            long milliseconds=System.currentTimeMillis()-24L*60L*60L*1000L*days;
            search.addFilterGreaterOrEqual("createTime",new Date(milliseconds));
        }

        if(type!=null){
            search.addFilterEqual("type",type);
        }

        if(status!=null){
            search.addFilterEqual("status",status);
        }

        return generalDAO.count(search);

    }



    public List<ReturnVisitTask> findAllReturnVisitTask(ReturnVisitSearchCondition condition, Page page) {
        Search search=new Search(ReturnVisitTask.class);

        Employee employee= AuthorityUtil.getCurrentEmployee();
        if(!AuthorityUtil.hasRole(AuthorityUtil.RETURN_VISIT_SUPERVISOR_ROLE_NAME)){
            search.addFilterEqual("visitorName",employee.getUsername());
        }

        search.addFilterLessOrEqual("displayTime",new Date());
        if(condition!=null){
            if(condition.getStatus()!=null){
                search.addFilterEqual("status",condition.getStatus());
            }

            if(StringUtils.isNotBlank(condition.getVisitorRealname())){
                search.addFilterEqual("visitorRealname",condition.getVisitorRealname());
            }

            if(condition.getLastVisitBeginTime()!=null){
                search.addFilterGreaterOrEqual("lastVisitTime",condition.getLastVisitBeginTime());
            }
            if(condition.getLastVisitEndTime()!=null){
                search.addFilterLessOrEqual("lastVisitTime",condition.getLastVisitBeginTime());
            }

            if(condition.getUsed()!=null){
                search.addFilterEqual("used",condition.getUsed());
            }

            if(condition.getPlatformType()!=null){
                search.addFilterEqual("order.platformType",condition.getPlatformType());
            }
            if(condition.getShopId()!=null&&condition.getShopId()>0){
                search.addFilterEqual("order.shopId",condition.getShopId());
            }
            if(condition.getBlandId()!=null&&condition.getBlandId()>0){
                search.addFilterEqual("order.orderItemList.product.brand.id",condition.getBlandId());
            }

            if(StringUtils.isNotBlank(condition.getReceiverMobile())){
                search.addFilterEqual("order.invoice.receiver.receiverMobile",condition.getReceiverMobile());
            }

            if(StringUtils.isNotBlank(condition.getReceiverPhone())){
                search.addFilterEqual("order.invoice.receiver.receiverPhone",condition.getReceiverPhone());
            }

            if(StringUtils.isNotBlank(condition.getReceiverName())){
                search.addFilterEqual("order.invoice.receiver.receiverName",condition.getReceiverName());
            }

            if(StringUtils.isNotBlank(condition.getBuyerId())){
                search.addFilterEqual("order.buyerId",condition.getBuyerId());
            }

            if(condition.getRedirectAfterSale()!=null){
                search.addFilterEqual("redirectAfterSale",condition.getRedirectAfterSale());
            }
        }

        search.addSortDesc("createTime");
        search.addPagination(page);
        return generalDAO.search(search);

    }

    @Transactional(readOnly = false)
    public void autoCloseSignedReturnVisitIfExist(Integer orderId,String remark) {
        Search search=new Search(ReturnVisitTask.class);
        search.addFilterEqual("type",ReturnVisitType.SIGNED);
        search.addFilterIn("status", ReturnVisitStatus.UNASSIGNED,ReturnVisitStatus.ASSIGNED);
        search.addFilterEqual("orderId",orderId);
        List<ReturnVisitTask> tasks=this.generalDAO.search(search);

        Date nowTime= new Date();
        for(ReturnVisitTask task:tasks){
            task.setStatus(ReturnVisitStatus.COMPLETED);
            task.setUpdateTime(nowTime);
            //insert a syslog message into return-visit-log
            ReturnVisitLog returnVisitLog=new ReturnVisitLog();
            returnVisitLog.setStatus(ReturnVisitStatus.COMPLETED);
            returnVisitLog.setVisitorName("syslog");
            returnVisitLog.setVisitorRealname("系统日志");
            returnVisitLog.setTaskId(task.getId());
            returnVisitLog.setOrderId(orderId);
            returnVisitLog.setRemark(remark);
            returnVisitLog.setCreateTime(nowTime);
            this.generalDAO.saveOrUpdate(returnVisitLog);
        }
    }

    public List<ReturnVisitTask> findRedirectAfterSaleReturnVisitTask(ReturnVisitSearchCondition condition, Page page) {
        Search search=new Search(ReturnVisitTask.class);

        Employee employee= AuthorityUtil.getCurrentEmployee();

        if(!AuthorityUtil.hasRole(AuthorityUtil.RETURN_VISIT_SUPERVISOR_ROLE_NAME)
                ||!AuthorityUtil.hasRole(AuthorityUtil.AFTER_SALE_ROLE_NAME)){
            search.addFilterEqual("visitorName",employee.getUsername());
        }

        search.addFilterEqual("redirectAfterSale",true);
        if(condition.getAfterSaleStatus()!=null){
            search.addFilterEqual("afterSaleStatus",condition.getAfterSaleStatus());
        }

        if(StringUtils.isNotBlank(condition.getReturnVisitNo())){
            search.addFilterEqual("returnVisitNo",condition.getReturnVisitNo());
        }

        search.addPagination(page);
        return this.generalDAO.search(search);
    }

    @Transactional(readOnly = false)
    public void saveReturnVisitAfterSalesLog(ReturnVisitAfterSalesLog returnVisitAfterSalesLog) {

        ReturnVisitTask task=this.getReturnVisitTaskById(returnVisitAfterSalesLog.getReturnVisitId());
        if(task==null){
            log.error("ReturnVisitTask not found from database by id[{}]",returnVisitAfterSalesLog.getReturnVisitId());
        }

        if(returnVisitAfterSalesLog.getSolutionType()==SolutionType.SOLVED_BY_PHONE){
            task.setAfterSaleStatus(Progress.COMPLETED);
        }else if(task.getAfterSaleStatus()!=Progress.COMPLETED){
            task.setAfterSaleStatus(Progress.PROCESSING);
        }

        returnVisitAfterSalesLog.setOperator(AuthorityUtil.getCurrentEmployee().getName());

        this.generalDAO.saveOrUpdate(returnVisitAfterSalesLog);
    }

    public List<ReturnVisitAfterSalesLog> findReturnVisitAfterSalesLog(Integer returnVisitId) {
        return this.generalDAO.search(new Search(ReturnVisitAfterSalesLog.class).addFilterEqual("returnVisitId",returnVisitId));
    }

    /**
     *   分配        建立一个vo
     * @return
     */
    @Transactional(readOnly = false)
    public void apportionReturnVisitTask(Integer[] taskIds,List<ApportionPersonVo> apportionPersonVos){
        Integer count=0;
        for(ApportionPersonVo apportionPersonVo:apportionPersonVos){
            for(int i=0;i<apportionPersonVo.getApportionCount();i++){
                Integer taskId=taskIds[count];
                updateReturnVisitTask(taskId,apportionPersonVo.getUsername(),apportionPersonVo.getEmployeeName());
                count++;
            }
        }
    }

    /**
     * 更新回访的回访人
     * @param taskId
     * @param employeeName
     */
    @Transactional(readOnly = false)
    public void updateReturnVisitTask(Integer taskId,String userName,String employeeName){
//        StringBuilder hql=new StringBuilder();
//        List<Object> objects=new ArrayList<Object>();
//        hql.append("update ReturnVisitTask  set visitorName=?, visitorRealname=? where id=? ");
//        objects.add(userName);
//        objects.add(employeeName);
//        objects.add(taskId);
//        generalDAO.query(hql.toString(),null,objects.toArray());
          ReturnVisitTask returnVisitTask=getReturnVisitTaskById(taskId);
          returnVisitTask.setVisitorName(userName);
          returnVisitTask.setVisitorRealname(employeeName);
          returnVisitTask.setStatus(ReturnVisitStatus.ASSIGNED);
          saveOrUpdateReturnVisitTask(returnVisitTask);

    }

    public ReturnVisitAfterSales getReturnVisitAfterSalesByAfterSalesNo(String afterSalesNo) {
        Preconditions.checkNotNull(afterSalesNo);
        Search search=new Search(ReturnVisitAfterSales.class);
        search.addFilterEqual("afterSalesNo",afterSalesNo);

        return (ReturnVisitAfterSales) this.generalDAO.searchUnique(search);
    }

    @Transactional(readOnly = false)
    public void updateAfterSaleStatusForReturnVisit(Integer returnVisitId,Progress afterSaleStatus) {
        if(returnVisitId!=null&&returnVisitId>0){
            ReturnVisitTask task=this.generalDAO.get(ReturnVisitTask.class,returnVisitId);
            if(task!=null){
                task.setAfterSaleStatus(afterSaleStatus);
            }
        }
    }

}
