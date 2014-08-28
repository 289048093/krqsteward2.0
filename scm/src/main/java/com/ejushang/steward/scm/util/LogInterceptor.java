package com.ejushang.steward.scm.util;

import com.ejushang.steward.common.domain.BusinessLog;
import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.service.ResourceService;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.OperationLog;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.ordercenter.service.BusinessLogService;
import com.ejushang.uams.api.dto.OperationDto;
import com.ejushang.uams.api.dto.ResourceDto;
import com.ejushang.uams.client.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * User:moon
 * Date: 14-4-10
 * Time: 上午9:34
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private BusinessLogService businessLogService;

    @Autowired
    private ResourceService resourceService;

    private static final Logger logger=Logger.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("into controller advice........");

        HandlerMethod maControl = (HandlerMethod) handler;
        Method method = maControl.getMethod();

        if(method.isAnnotationPresent(OperationLog.class)){
            BusinessLog businessLog = new BusinessLog();
            try {
                BusinessLogUtil.addBusinessLog(businessLog);
                businessLog.setOperationResult(true);//Set default value as true
                //记录请求参数
                businessLog.setParams(JsonUtil.objectToJson(request.getParameterMap()));

                //记录用户信息
                Employee employee= SessionUtils.getEmployee();
                if(employee != null) {
                    businessLog.setOperatorId(employee.getId());
                    businessLog.setOperatorName(employee.getUsername());
                }
                businessLog.setIp(request.getRemoteAddr());
                businessLog.setRequestUrl(request.getRequestURL().toString());

                //记录请求模块信息
                String operationUrl = request.getRequestURI();
                if(!StringUtils.isBlank(operationUrl)){
                    OperationDto operationDto=resourceService.getOperationByUrl(operationUrl);
                    ResourceDto resourceDto=resourceService.getResourceByUrl(operationUrl);
                    if(operationDto!=null){
                        businessLog.setOperationName(operationDto.getName());
                    }else{
                        if(method.isAnnotationPresent(OperationLog.class)) {
                            businessLog.setOperationName(method.getAnnotation(OperationLog.class).value());
                        }
                    }
                    if(resourceDto != null) {
                        businessLog.setResourceName(resourceDto.getName());
                    }
                }
                long startTime = System.currentTimeMillis();
                //暂时把开始时间放入这个字段,等执行结束拿出来计算真正的执行时间
                businessLog.setExecutionTime(startTime);
            } catch (Exception e) {
                logger.error("记录业务操作日志的时候出错,", e);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        HandlerMethod maControl = (HandlerMethod) handler;
        Method method = maControl.getMethod();
        if(method.isAnnotationPresent(OperationLog.class)){
            try {
                BusinessLog businessLog = BusinessLogUtil.pollBusinessLog();
                if(businessLog == null) {
                    logger.error("afterCompletion中发现BusinessLog为空,程序有bug?");
                    return;
                }
                if(businessLog.getExecutionTime() != null) {
                    businessLog.setExecutionTime(System.currentTimeMillis() - businessLog.getExecutionTime());
                }
                businessLogService.saveBusinessLog(businessLog);
            } catch (Exception e) {
                logger.error("保存业务日志的时候出错", e);
            }
        }
    }

}
