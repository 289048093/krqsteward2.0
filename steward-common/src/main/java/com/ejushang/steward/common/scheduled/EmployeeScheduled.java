package com.ejushang.steward.common.scheduled;

import com.ejushang.steward.common.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-5-16
 * Time: 下午5:46
 */
@Component
public class EmployeeScheduled {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeService employeeService;

    /**
     * 每天任务
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void reloadEmployee(){
        //每天重新加载缓存
        log.info("重新加载用户缓存");
        employeeService.reloadCache();
    }
}
