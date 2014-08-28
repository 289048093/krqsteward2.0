package com.ejushang.steward.common.util;

import com.ejushang.steward.common.domain.BusinessLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * User:moon
 * Date: 14-7-16
 * Time: 上午10:32
 */
public class BusinessLogUtil {

    private static final Logger log= LoggerFactory.getLogger(BusinessLogUtil.class);

    private static BusinessLogHolder businessLogHolder = BusinessLogHolder.getInstance();

    /**
     * 绑定业务操作日志到ThreadLocal变量
     * @param executionResult 操作结果 true[成功]，false[失败]
     * @param description 操作的具体内容描述
     */
    public static void bindBusinessLog(Boolean executionResult, String description){
        BusinessLog businessLog = peekBusinessLog();
        if(businessLog == null) {
            log.error("设置description时发现BusinessLog为空,程序有bug?");
            return;
        }
        businessLog.setOperationResult(executionResult);
        businessLog.setDescription(description);
    }

    /**
     * 绑定业务操作日志到ThreadLocal变量
     * @param executionResult 操作结果 true[成功]，false[失败]
     * @param format 操作的具体内容格式化描述字符串，包含参数占位符
     * @param args 格式化字符串参数
     */
    public static void bindBusinessLog(Boolean executionResult, String format, Object ... args){
        String str=null;
        try {
            str = String.format(format, args);
        }catch (Exception e){
            log.error("Formatting string exception:【format=[{}],args={}】", format, Arrays.toString(args));
            str=format;
        }

        bindBusinessLog(executionResult,str);
    }

    /**
     * 绑定业务操作日志到ThreadLocal变量，操作结果设置为true
     * @param format 操作的具体内容格式化描述字符串，包含参数占位符
     * @param args 格式化字符串参数
     */
    public static void bindBusinessLog(String format, Object ... args){
        bindBusinessLog(true,format,args);
    }

    /**
     * 绑定异常信息到ThreadLocal变量，操作结果设置为false
     * @param operationException 异常信息描述
     */
    public static void bindExceptionMessage(String operationException){
        BusinessLog businessLog = peekBusinessLog();
        if(businessLog == null) {
            log.error("设置operationException时发现BusinessLog为空,程序有bug?");
            return;
        }
        businessLog.setOperationException(operationException);
        businessLog.setOperationResult(false);
    }

    /**
     * 删除并返回绑定到ThreadLocal变量的BusinessLog对象
     * @return
     */
    public static BusinessLog pollBusinessLog(){
        BusinessLog businessLog=businessLogHolder.pollBusinessLog();
        return businessLog;
    }

    /**
     * 获取绑定到ThreadLocal变量的BusinessLog对象
     * @return
     */
    public static BusinessLog peekBusinessLog(){
        BusinessLog businessLog=businessLogHolder.getCurrentBusinessLog();
        return businessLog;
    }

    /**
     * 把BusinessLog对象添加到ThreadLocal变量中
     * @param businessLog
     */
    public static void addBusinessLog(BusinessLog businessLog){
         businessLogHolder.addBusinessLog(businessLog);
    }


}
