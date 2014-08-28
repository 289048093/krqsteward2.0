package com.ejushang.steward.scm.task;

import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 猜测原始订单品牌任务
 */
@Component
public class GuessAllOriginalOrderBrandTask {

    private static final Logger log = LoggerFactory.getLogger(GuessAllOriginalOrderBrandTask.class);

    @Autowired
    private OriginalOrderService originalOrderService;

//    @Scheduled(cron = "0 10 2 11 7 ?")
    @Transactional
    public void guessAllOriginalOrderBrand(){
        log.info("猜测原始订单品牌任务开始执行");
        StopWatch stopWatch = null;
        try {
            stopWatch = new StopWatch();
            stopWatch.start();
            originalOrderService.guessAllOriginalOrderBrand();
        } catch (Exception e) {
            log.error("猜测原始订单品牌的时候发生错误", e);
        } finally {
            if(stopWatch != null) {
                stopWatch.stop();
                log.info("猜测原始订单品牌任务执行完成, 耗时:{}", stopWatch.toString());
            }
        }
    }
}
