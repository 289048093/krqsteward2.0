package com.ejushang.steward.ordercenter.service.api.impl.jd;

import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.bean.LogisticsBean;
import com.ejushang.steward.ordercenter.constant.DeliveryType;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.service.OrderFlowService;
import com.ejushang.steward.ordercenter.service.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

/**
 * User: Baron.Zhang
 * Date: 2014/6/25
 * Time: 10:55
 */
public class JingDongLogisticsApiServiceTest extends BaseTest {

    private static final String sessionKey="278c4dc3-dd12-4e77-b2d6-768cf4051a69";

    @Autowired
    private JingDongLogisticsApiService jingDongLogisticsApiService;

    @Autowired
    private OrderFlowService orderFlowService;

    @Autowired
    private OrderService orderService;

    @Test
    @Rollback(false)
    public void testFetchLogisticsTrace() throws Exception {
        Integer orderId = 0;
        Order order = orderService.findOrderById(orderId);
        LogisticsBean logisticsBean = null;
        if(order == null){
            logisticsBean = new LogisticsBean();
            logisticsBean.setSessionKey(sessionKey);
            logisticsBean.setExpressNo("768939312404");
        }
        else {
            logisticsBean = orderFlowService.structLogisticsBean(order);
        }

        if(logisticsBean != null) {
            jingDongLogisticsApiService.fetchLogisticsTrace(logisticsBean);
        }

        System.out.println();
    }

    @Test
    @Rollback(false)
    public void testSendLogisticsOnline() throws Exception {
        Integer orderId = 21252;
        Order order = orderService.findOrderById(orderId);
        LogisticsBean logisticsBean = null;
        if(order == null){
            logisticsBean = new LogisticsBean();
            logisticsBean.setSessionKey(sessionKey);
            logisticsBean.setOutOrderNo("1660348047");
            logisticsBean.setExpressNo("768939918372");
            logisticsBean.setExpressCompany(DeliveryType.shentong.toString());
        }
        else {
           logisticsBean = orderFlowService.structLogisticsBean(order);
        }
        if(logisticsBean != null) {
            jingDongLogisticsApiService.sendLogisticsOnline(logisticsBean);
        }

        System.out.println("");
    }


}
