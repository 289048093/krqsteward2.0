package com.ejushang.steward.scm.web;

import com.ejushang.steward.ordercenter.service.OrderService;
import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import com.ejushang.steward.ordercenter.service.UpdateDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 不需要权限即可访问的链接,用来执行代码
 */
@Controller
public class NoPermissionController {
    static final Logger log = LoggerFactory.getLogger(NoPermissionController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private UpdateDataService updateDataService;

//    @RequestMapping("/np/original_order_brand/init")
//    @ResponseBody
//    public String initOriginalOrderBrand() {
//        log.info("猜测原始订单品牌任务开始执行");
//        StopWatch stopWatch = null;
//        try {
//            stopWatch = new StopWatch();
//            stopWatch.start();
//
//            originalOrderService.guessAllOriginalOrderBrand();
//            return "success";
//        } catch (Exception e) {
//            log.error("猜测原始订单品牌的时候发生错误", e);
//            return "fail";
//        } finally {
//            if(stopWatch != null) {
//                stopWatch.stop();
//                log.info("猜测原始订单品牌任务执行完成, 耗时:{}", stopWatch.toString());
//            }
//        }
//    }

//    @RequestMapping("/np/sku_not_found/zp")
//    @ResponseBody
//    public String updateNotFoundSku() {
//        updateDataService.updateOriginalOrderItemSkuZpLvXingDai();
//        return "success";
//    }
//
    @RequestMapping("/np/fix/order")
    @ResponseBody
    public String fixOrder() throws Exception {
        orderService.fixOrderFee();
        return "success";
    }


}
