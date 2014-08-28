//package com.ejushang.steward.scm.task;
//
//import com.ejushang.steward.ordercenter.constant.PlatformType;
//import com.ejushang.steward.ordercenter.domain.OriginalRefund;
//import com.ejushang.steward.ordercenter.service.OriginalRefundService;
//import org.apache.commons.collections.CollectionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 遍历所有淘宝退款单，将之与线上的退款单进行对比，将原始退款单的状态进行修正
// * User: Baron.Zhang
// * Date: 2014/7/3
// * Time: 16:39
// */
//@Component
//public class ModifyOriginalRefundStatusTask {
//    private static final Logger log = LoggerFactory.getLogger(ModifyOriginalRefundStatusTask.class);
//
//    @Autowired
//    private OriginalRefundService originalRefundService;
//
//    @PostConstruct
//    public void modifyOriginalRefundStatus(){
//
//        List<OriginalRefund> allOriginalRefundList = new ArrayList<OriginalRefund>();
//
//        OriginalRefund originalRefundQuery = new OriginalRefund();
//        originalRefundQuery.setPlatformType(PlatformType.TAO_BAO);
//        // 查询所有原始订单
//        List<OriginalRefund> originalRefundList = originalRefundService.findOriginalRefund(originalRefundQuery);
//        if(CollectionUtils.isNotEmpty(originalRefundList)){
//            allOriginalRefundList.addAll(originalRefundList);
//        }
//
//        originalRefundQuery.setPlatformType(PlatformType.TAO_BAO_2);
//        // 查询所有原始订单
//        List<OriginalRefund> originalRefundList2 = originalRefundService.findOriginalRefund(originalRefundQuery);
//        if(CollectionUtils.isNotEmpty(originalRefundList2)){
//            allOriginalRefundList.addAll(originalRefundList2);
//        }
//
//        // 根据退款单号查询天猫平台
//    }
//}
