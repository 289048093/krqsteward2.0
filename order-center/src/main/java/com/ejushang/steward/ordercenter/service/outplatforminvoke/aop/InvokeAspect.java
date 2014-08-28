package com.ejushang.steward.ordercenter.service.outplatforminvoke.aop;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ApiInvokeException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 产品同步调用API切面
 * User:  Sed.Lee(李朝)
 * Date: 14-7-18
 * Time: 下午6:07
 */
@Component
@Aspect
public class InvokeAspect {

    private static final Logger log = LoggerFactory.getLogger(InvokeAspect.class);


    /**
     * @param jp
     * @return
     * @throws ApiInvokeException
     */
    @Around(value = "InvokePointCut.apiInvoke()")
    public Object InvokeAround(ProceedingJoinPoint jp) throws Throwable {
        if (isOnline()) {  //判断是否是在线
            return jp.proceed();
        }
        return null;
    }

    private boolean isOnline() {
        return Application.getInstance().getBooleanConfigValue("online");
    }
}


