
package com.ejushang.steward.common.util;

import com.ejushang.steward.common.domain.BusinessLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 业务日志持有对象
 * 内部使用ThreadLocal维护元素为BusinessLog的一个栈
 * 使用栈的原因是一次线程请求过程中,可能被拦截多次(例如内部forward),所以可能会生成多个BusinessLog
 *
 * User: liubin
 * Date: 14-1-13
 */
public class BusinessLogHolder {

    private static final Logger log = LoggerFactory.getLogger(BusinessLogHolder.class);

    //used for single instance
    private static final BusinessLogHolder instance = new BusinessLogHolder();
    private BusinessLogHolder() {}
    public static final BusinessLogHolder getInstance() {
        return instance;
    }

    //treated deque as stack
    private ThreadLocal<Deque<BusinessLog>> businessLogHolder = new ThreadLocal<Deque<BusinessLog>>();

    /**
     * 添加业务日志
     * @param businessLog
     */
    public void addBusinessLog(BusinessLog businessLog) {
        Deque<BusinessLog> businessLogs = businessLogHolder.get();
        if(businessLogs == null) {
            businessLogs = new ArrayDeque<BusinessLog>();
            businessLogHolder.set(businessLogs);
        }
        businessLogs.push(businessLog);
    }

    /**
     * 得到当前的业务日志
     * @return
     */
    public BusinessLog getCurrentBusinessLog() {
        Deque<BusinessLog> businessLogs = businessLogHolder.get();
        if(businessLogs == null || businessLogs.isEmpty()) {
            return null;
        }
        return businessLogs.peek();
    }

    /**
     * 从栈中弹出业务日志,如果栈为空进行清理操作
     * @return
     */
    public BusinessLog pollBusinessLog() {
        Deque<BusinessLog> businessLogs = businessLogHolder.get();
        BusinessLog businessLog = null;
        if(businessLogs != null && !businessLogs.isEmpty()) {
            businessLog = businessLogs.poll();
        }
        if(businessLogs == null || businessLogs.isEmpty()) {
            businessLogHolder.remove();
        }
        return businessLog;
    }

}
