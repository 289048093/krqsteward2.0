package com.ejushang.steward.ordercenter.returnvisit;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.service.EmployeeService;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.keygenerator.SequenceGenerator;
import com.ejushang.steward.ordercenter.service.AfterSalesService;
import com.ejushang.steward.ordercenter.service.BlacklistService;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.OrderService;
import com.ejushang.steward.ordercenter.service.OrderSignedLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: codec.yang
 * Date: 2014/8/6 12:06
 */

@Component
public class ReturnVisitProcessor {

    private static final Logger log= LoggerFactory.getLogger(ReturnVisitProcessor.class);


    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private ReturnVisitService returnVisitService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AfterSalesService afterSalesService;

    @Autowired
    private OrderSignedLogService orderSignedLogService;


    private ProcessOrderSignedLogThread processSignedOrderThread;

    @PostConstruct
    public void init(){
        log.info("ReturnVisitProcessor init...");
        this.processSignedOrderThread=new ProcessOrderSignedLogThread(this.orderSignedLogService,"CreateReturnVisitForSignedOrderThread");
        this.processSignedOrderThread.start();
    }

    @PreDestroy
    public void destroy(){
        log.info("ReturnVisitProcessor destroying....");
        this.processSignedOrderThread.exit();
    }

    public void notifyProcessSignedOrderThread(){
        this.processSignedOrderThread.notifyMe();
    }


    /**
     * 处理回访事件主流程：
     * 1.判断是否被列入电话黑名单或者是否为内部员工，是则终止流程
     * 2.根据回访类型分别进行处理：
     * 2.1 签收回访：
     *     1）判断是否为售后创建的订单，是则不创建回访单（防止跟售后回访重复）
     *     2）判断是否包含锅具，是则生成回访单（锅具订单必回访）
     *     3）判断该手机号是否在30天内接受过回访，不是则生成回访单（30天内只接受一次回访）
     * 2.2 售后回访：
     *     1）首先判断该售后单是否是“回访转售后”产生的售后单，如果是则需要更新关联的回访单的售后状态
     *     2）判断是否还存在其它未完成的售后单与它的平台订单号相同，是则不生成回访单 （同一平台订单号产生的多个售后单只回访一次）
     *     3）自动结束同一订单产生的签收回访单 （售后回访覆盖签收回访）
     *     4）判断当前该手机号在3天内是否接受过售后回访，是则将新生成的回访单显示在界面的时间推迟3天
     * 2.3 差评回访
     *     1）自动结束同一订单产生的签收回访单 （差评回访覆盖签收回访）
     *     2）生成差评回访单
     * @param event
     */
    @Transactional
    public void process(ReturnVisitEvent event){
        if(log.isDebugEnabled()) {
            log.debug(event.toString());
        }

        Order order= this.orderService.findOrderById(event.getOrderId());
        if(order==null){
            log.error("No order found from database by id:"+event.getOrderId());
            return;
        }
        event.init(order);

        if(this.isInPhoneBlacklist(event.getMobile())
                ||this.isInternalEmployee(event.getMobile())){
            return;
        }

        switch (event.getType()){
            case SIGNED:
                if(isOrderCreatedForAfterSale(event.getOrderId())){
                    log.info("Order[{}] is created as after sales",event.getOrderId());
                    return;
                }
                
                if(containsPot(event.getOrderItemList())||!isVisitedIn30Days(event.getMobile())){
                    createAndSaveReturnVisitTask(event);
                }
                break;
            case AFTER_SALE:

                updateAfterSaleStatusForReturnVisit(event.getAfterSaleNo());

                if(this.existUncompletedAftersaleFromSameOriginalOrder(event.getPlatformType(),event.getPlatformOrderNo(),event.getAfterSaleNo())){
                    log.info("Still exists other uncompleted after-sales caused by other orders with same platformOrderNo[{}].",event.getPlatformOrderNo());
                    return;
                }

                this.returnVisitService.autoCloseSignedReturnVisitIfExist(event.getOrderId(),"同一订单生成售后回访单，签收回访单自动结束");

                ReturnVisitTask task=createReturnVisitTask(event);

                if(hasAftersaleReturnVisitIn3Days(event.getMobile())){
                    task.setDisplayTime(new Date(task.getDisplayTime().getTime()+3L*24L*60L*60L*1000L));
                }
                saveReturnVisitTask(task);
                break;
            case NEGATIVE_COMMENT:
                this.returnVisitService.autoCloseSignedReturnVisitIfExist(event.getOrderId(),"同一订单生成差评回访单，签收回访单自动结束");
                createAndSaveReturnVisitTask(event);
                break;
            default:
                new StewardBusinessException("Unknown ReturnVisit Type:"+event.getType());

        }
    }

    /**
     * 判断该订单是否为售后创建的订单，比如售后换货等
     * @param orderId
     * @return
     */
    private boolean isOrderCreatedForAfterSale(Integer orderId) {
        return this.afterSalesService.findAfterSalesByOrderId(orderId).size()>0;
    }

    /**
     * 更新回访单的售后处理状态（针对回访转售后的场景）
     * @param afterSaleNo
     */
    private void updateAfterSaleStatusForReturnVisit(String afterSaleNo) {
        AfterSales afterSales=this.afterSalesService.getAfterSalesByCode(afterSaleNo);
        if(afterSales==null|| NumberUtil.isNullOrLessThanOne(afterSales.getRevisitId())){
            return;
        }

        log.debug("AfterSales[no={}] is redirected from return-visit",afterSaleNo);

        List<AfterSales> afterSalesList= this.afterSalesService.findAfterSalesByReturnVisitId(afterSales.getRevisitId());

        for(AfterSales as: afterSalesList){
            if(!afterSaleNo.equalsIgnoreCase(as.getCode())){
                if(isAfterSalesOnProcessing(as)){
                    log.debug("Not all after-sales created for same return-visit are finished");
                    return;
                }
            }
        }
        if(log.isDebugEnabled()) {
            log.debug("All after-sales created for same return-visit are finished, change return-visit after-sales status to completed, return-visit-id:"
                    + afterSales.getRevisitId());
        }
        this.returnVisitService.updateAfterSaleStatusForReturnVisit(afterSales.getRevisitId(), Progress.COMPLETED);

    }

    private boolean isAfterSalesOnProcessing(AfterSales afterSales){
        if(afterSales==null){
            log.error("Argument afterSales is null");
            return false;
        }
        return afterSales.getStatus()==AfterSalesStatus.SAVE
                ||afterSales.getStatus()==AfterSalesStatus.ACCEPT
                    ||afterSales.getStatus()==AfterSalesStatus.CHECK;
    }

    /**
     * 判断给定的平台订单号下是否存在尚未结束的售后单
     * @param platformOrderNo
     * @return
     */
    private boolean existUncompletedAftersaleFromSameOriginalOrder(PlatformType platformType, String platformOrderNo,String afterSalesNo) {
        if(platformType==null|| StringUtils.isBlank(platformOrderNo)){
            log.warn("platformType[{}] or/and platformOrder[{}] is null or empty, afterSalesNo[{}]", new Object[]{platformType, platformOrderNo, afterSalesNo});
            return false;
        }

        List<AfterSales> dataList=this.afterSalesService.findAfterSalesByPlatformTypeAndOrderNo(platformType, platformOrderNo);
        for(AfterSales afterSales:dataList){
            if((this.isAfterSalesOnProcessing(afterSales))
                    &&!afterSalesNo.equals(afterSales.getCode())){
                return true;
            }
        }
        return false;
    }

    /**
     * 创建并保存回访单
     * @param event
     */
    private void createAndSaveReturnVisitTask(ReturnVisitEvent event){
        saveReturnVisitTask(createReturnVisitTask(event));
    }

    /**
     * 保存回访单
     * @param task
     */
    private void saveReturnVisitTask(ReturnVisitTask task){
        this.returnVisitService.saveOrUpdateReturnVisitTask(task);
    }

    /**
     * 创建回访单
     * @param event
     * @return
     */
    private ReturnVisitTask createReturnVisitTask(ReturnVisitEvent event){
        ReturnVisitTask task=new ReturnVisitTask();
        task.setCreateTime(new Date());
        task.setUpdateTime(task.getCreateTime());
        task.setDisplayTime(task.getCreateTime());
        task.setType(event.getType());
        task.setOrderId(event.getOrderId());
        task.setStatus(ReturnVisitStatus.UNASSIGNED);
        task.setPhone(event.getPhone());
        task.setMobile(event.getMobile());
        task.setPlatformOrderNo(event.getPlatformOrderNo());
        task.setReturnVisitNo(SequenceGenerator.getInstance().getNextReturnVisitNo());

        return task;
    }


    /**
     * 判断该手机号在30天内是否接受过回访
     * @param mobile
     * @return
     */
    private boolean isVisitedIn30Days(String mobile){
        int count=this.returnVisitService.countReturnVisitTimesByMobile(mobile, 30, null, null);
        log.debug("Phone[{}] got {} times of return visit in 30 days",mobile,count);
        return count>0;
    }

    /**
     * 判断该手机号在3天内是否接受过售后回访
     * @param mobile
     * @return
     */
    private boolean hasAftersaleReturnVisitIn3Days(String mobile){
        int count=this.returnVisitService.countReturnVisitTimesByMobile(mobile, 30, ReturnVisitType.AFTER_SALE, ReturnVisitStatus.SUCCEED);
        log.debug("Phone[{}] got {} times of after-sale return visit in 3 days",mobile,count);
        return count>0;
    }

    /**
     * 判断订单项里面是否包含锅具
     * @param orderItemList
     * @return
     */
    private boolean containsPot(List<OrderItem> orderItemList){
        for(OrderItem orderItem:orderItemList){
            Product product=orderItem.getProduct();
            if(product==null||product.getCategory()==null){
                log.warn("No product or category for order item:"+orderItem.getId());
                continue;
            }
            ProductCategory category=product.getCategory();
            while(category!=null){
                if("锅具".equals(category.getName())){
                    return true;
                }
                category=category.getParentCategory();
            }
        }

        return false;
    }

    /**
     * 判断电话黑名单中是否包含指定的手机号
     * @param phone
     * @return
     */
    private boolean isInPhoneBlacklist(String phone){
        boolean result= this.blacklistService.getPhoneBlacklistByPhone(phone)!=null;
        log.debug("Is phone[{}] in blacklist: {}",phone,result);
        return result;
    }

    /**
     * 根据手机号判断是否为内部员工
     * @param phone
     * @return
     */
    private boolean isInternalEmployee(String phone){
        boolean isEmployee= this.employeeService.findEmployeeByTel(phone)!=null;
        log.debug("Is phone[{}] is internal employee:{}",phone,isEmployee);
        return isEmployee;
    }


    @Transactional(readOnly = false)
    public void createReturnVisitForOrderSignedLog(List<OrderSignedLog> orderSignedLogList) {

        List<Integer> idList=new ArrayList<Integer>(orderSignedLogList.size());

        for(OrderSignedLog osl:orderSignedLogList){
            ReturnVisitManager.fromSignedOrder(osl.getOrderId());
            idList.add(osl.getId());
        }
        this.orderSignedLogService.updateProcessedStatus(idList);

    }
}
