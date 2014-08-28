package com.ejushang.steward.common.springmvc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SpringMVC登录拦截器
 * 登录的逻辑都通过shiro组件实现,该拦截器的用途是为了阻止登录表单提交的post请求到达后端controller.
 * 因为登录页面是一个html,get请求会直接到达该html,post请求则由shiro拦截器拦截之后进行登录操作,将结果的json字符串写回response.
 * 如果继续执行则回请求到后端的Controller,因为没有对应的Controller映射该登录请求,所以会抛出异常.则需要在这里停止springmvc组件的执行.
 * 也不能通过写一个Controller映射html请求来实现.只能用拦截器.
 *
 * User: liubin
 * Date: 14-1-13
 */
public class LoginInteceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInteceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler)
        throws Exception {

        if(!request.getMethod().equalsIgnoreCase("POST")) {
            //请求登录页面
            return true;

        } else {
            //登录ajax请求,不往后执行controller
            return false;
        }
    }

    @Override
    public void postHandle(
        HttpServletRequest request, HttpServletResponse response, 
        Object handler, ModelAndView modelAndView) throws Exception {

        System.out.println(123123);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}