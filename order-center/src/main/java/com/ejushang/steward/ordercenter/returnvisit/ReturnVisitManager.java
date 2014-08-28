package com.ejushang.steward.ordercenter.returnvisit;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.constant.ReturnVisitType;
import com.ejushang.steward.ordercenter.domain.OrderSignedLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by: codec.yang
 * Date: 2014/8/6 11:53
 */
public class ReturnVisitManager {

    private static Logger log= LoggerFactory.getLogger(ReturnVisitManager.class);
    private static ReturnVisitProcessor processor;

    /**
     * 售后转回访
     * @param orderId
     * @param afterSalesNo
     */
    public static void fromAfterSales(Integer orderId,String afterSalesNo){
        log.info("orderId[{}],afterSalesNo[{}]",orderId,afterSalesNo);

        if(NumberUtil.isNullOrLessThanOne(orderId)){
            log.warn("Order id is null or less than 1");
            return;
        }
        if(StringUtils.isBlank(afterSalesNo)){
            log.warn("afterSalesNo is null or empty");
            return;
        }

        fireEvent(new ReturnVisitEvent(ReturnVisitType.AFTER_SALE,orderId,afterSalesNo));

    }


    /**
     * 签收回访
     * @param orderId
     */
    public static void fromSignedOrder(Integer orderId){
        log.info("orderId[{}]",orderId);

        if(NumberUtil.isNullOrLessThanOne(orderId)){
            log.warn("Order id is null or less than 1");
            return;
        }

        fireEvent(new ReturnVisitEvent(ReturnVisitType.SIGNED,orderId));
    }

    /**
     * 差评转回访
     * @param commentId
     * @param orderId
     */
    public static void fromNegativeComment(Integer commentId,Integer orderId){
        log.info("commentId[{}],orderId[{}]", commentId, orderId);

        if(NumberUtil.isNullOrLessThanOne(orderId)){
            log.warn("Order id is null or less than 1");
            return;
        }

        fireEvent(new ReturnVisitEvent(ReturnVisitType.NEGATIVE_COMMENT,orderId));

    }

    /**
     * 从订单签收日志生成回访
     * @param orderSignedLogList
     */
    public static void fromOrderSignedLog(List<OrderSignedLog> orderSignedLogList) {
        if(orderSignedLogList==null||orderSignedLogList.isEmpty()){
            return;
        }

        getProcessor().createReturnVisitForOrderSignedLog(orderSignedLogList);

    }


    /**
     * 收到签收订单时唤醒处理线程后台去处理
     */
    public static void onReceiveSignedOrder(){
        getProcessor().notifyProcessSignedOrderThread();
    }



    private static void fireEvent(ReturnVisitEvent event){
        getProcessor().process(event);
    }

    private static ReturnVisitProcessor getProcessor(){
        if(processor==null){
            processor=Application.getBean(ReturnVisitProcessor.class);
        }
        return processor;
    }

}
