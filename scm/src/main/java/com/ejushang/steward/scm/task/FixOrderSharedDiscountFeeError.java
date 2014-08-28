//package com.ejushang.steward.scm.task;
//
//import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
//import com.ejushang.steward.ordercenter.service.MealSetService;
//import com.ejushang.steward.ordercenter.service.OrderService;
//import com.ejushang.steward.ordercenter.service.OriginalOrderService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//
///**
//*
//*/
//@Component
//public class FixOrderSharedDiscountFeeError {
//    private static final Logger log = LoggerFactory.getLogger(FixOrderSharedDiscountFeeError.class);
//
//    @Autowired
//    private OriginalOrderService originalOrderService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private MealSetService mealSetService;
//
//    @Autowired
//    private GeneralDAO generalDAO;
//
////    @Scheduled(cron = "0 10 2 29 7 ?")
////    @PostConstruct
//    @Transactional
//    public void fixOrderSharedDiscountFee() throws Exception {
//
//        orderService.fixOrderFee();
//    }
//}
