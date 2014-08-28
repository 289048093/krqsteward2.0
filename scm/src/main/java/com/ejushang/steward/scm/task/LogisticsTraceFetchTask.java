package com.ejushang.steward.scm.task;

import com.ejushang.steward.ordercenter.bean.LogisticsBean;
import com.ejushang.steward.ordercenter.constant.OrderStatus;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.service.OrderFlowService;
import com.ejushang.steward.ordercenter.service.OrderService;
import com.ejushang.steward.ordercenter.service.api.impl.jd.JingDongLogisticsApiService;
import com.ejushang.steward.ordercenter.service.api.impl.tb.TaoBaoLogisticsApiService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 2014/5/29
 * Time: 14:21
 */
@Component
public class LogisticsTraceFetchTask {

    private static final Logger log = LoggerFactory.getLogger(LogisticsTraceFetchTask.class);
    @Autowired
    private OrderService orderService;

    @Autowired
    private JingDongLogisticsApiService jingDongLogisticsApiService;

    @Autowired
    private TaoBaoLogisticsApiService taoBaoLogisticsApiService;

    @Autowired
    private OrderFlowService orderFlowService;

    //@Scheduled(cron = "0 0/3 * * * ?")
    public void fetchLogisticsTrace(){
        if(log.isInfoEnabled()){
            log.info("查询物流流转信息：查询开始");
        }
        // 查询所有已验货状态的订单
        List<Order> orderList = orderService.findOrderbyOrderStatus(OrderStatus.EXAMINED);

        if(log.isInfoEnabled()){
            log.info("查询物流流转信息：查询所有【已验货】状态的订单，共有{}条",orderList.size());
        }

        if(CollectionUtils.isNotEmpty(orderList)){
            for(Order order : orderList){
                LogisticsBean logisticsBean = orderFlowService.structLogisticsBean(order);
                Boolean hasLogisticsTrace = false;
                if(StringUtils.equalsIgnoreCase(logisticsBean.getOutPlatform(), PlatformType.TAO_BAO.getName())
                        || StringUtils.equalsIgnoreCase(logisticsBean.getOutPlatform(), PlatformType.TAO_BAO_2.getName())){
                    try {
                        hasLogisticsTrace = taoBaoLogisticsApiService.fetchLogisticsTrace(logisticsBean);
                    } catch (Exception e) {
                        if(log.isErrorEnabled()){
                            log.error("查询淘宝物流流转信息：订单号【{}】查询失败，{}",order.getPlatformOrderNo(), e.getMessage());
                        }
                    }
                }
                else if(StringUtils.equalsIgnoreCase(logisticsBean.getOutPlatform(), PlatformType.JING_DONG.getName())){
                    try {
                        hasLogisticsTrace = jingDongLogisticsApiService.fetchLogisticsTrace(logisticsBean);
                    } catch (Exception e) {
                        if(log.isErrorEnabled()){
                            log.error("查询京东物流流转信息：订单号【{}】查询失败，{}",order.getPlatformOrderNo(), e.getMessage());
                        }
                    }
                }
                // 存在物流信息，更新订单状态为已验货
                if(hasLogisticsTrace){
                    order.setStatus(OrderStatus.INVOICED);
                    orderService.saveAddOrder(order);
                }
            }
        }
    }
}
