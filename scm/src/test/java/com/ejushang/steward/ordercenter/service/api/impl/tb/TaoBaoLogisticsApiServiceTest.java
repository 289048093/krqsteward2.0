package com.ejushang.steward.ordercenter.service.api.impl.tb;

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
 * Date: 2014/6/24
 * Time: 11:24
 */
public class TaoBaoLogisticsApiServiceTest extends BaseTest{

    private static final String sessionKey = "6200925dbd073e0617f956aadebbd4f28ZZ38a4b807ecb81675300784";

    @Autowired
    private TaoBaoLogisticsApiService taoBaoLogisticsApiService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderFlowService orderFlowService;



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
            taoBaoLogisticsApiService.fetchLogisticsTrace(logisticsBean);
        }

        System.out.println();
    }

    @Test
    @Rollback(false)
    public void testSendLogisticsOnline() throws Exception {
        Integer orderId = 5500;
        Order order = orderService.findOrderById(orderId);
        LogisticsBean logisticsBean = null;
        if(order == null){
            logisticsBean = new LogisticsBean();
            logisticsBean.setSessionKey(sessionKey);
            logisticsBean.setOutOrderNo("719269235034980");
            logisticsBean.setExpressNo("test111111");
            //logisticsBean.setExpressCompany(DeliveryType.shentong.toString());
        }
        else {
            logisticsBean = orderFlowService.structLogisticsBean(order);
        }
        if(logisticsBean != null) {
            taoBaoLogisticsApiService.sendLogisticsOnline(logisticsBean);
        }

        System.out.println("");
    }
//
//    @Test
//    @Rollback(false)
//    public void testSendLogisticsOnline() throws Exception {
//        LogisticsBean logisticsBean = new LogisticsBean();
//        logisticsBean.setSessionKey(sessionKey);
//        logisticsBean.setOutOrderNo("703264409946629");
//        logisticsBean.setExpressNo("V187177970");
//        logisticsBean.setExpressCompany("yuantong");
//
//        taoBaoLogisticsApiService.sendLogisticsOnline(logisticsBean);
//    }
//
//    @Test
//    @Rollback(false)
//    public void testFetchLogisticsTrace() throws Exception {
//        LogisticsBean logisticsBean = new LogisticsBean();
//        logisticsBean.setSessionKey(sessionKey);
//        logisticsBean.setOutOrderNo("703264409946629");
//        logisticsBean.setExpressNo("V187177970");
//        logisticsBean.setExpressCompany("yuantong");
//        logisticsBean.setSellerNick("易居尚官方旗舰店");
//
//        taoBaoLogisticsApiService.fetchLogisticsTrace(logisticsBean);
//
//        System.out.println();
//
//    }

}
