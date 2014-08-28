package com.ejushang.steward.ordercenter.service.outplatforminvoke.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-7-21
 * Time: 下午1:21
 */

public class InvokePointCut {

    /**
     * 匹配ProductInvoke的子类所有带参数的公有方法
     */
    @Pointcut("execution(public * com.ejushang.steward.ordercenter.service.outplatforminvoke.ProductInvoke+.*(Object+,..))")
    void apiInvoke() {
    }
}