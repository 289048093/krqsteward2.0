package com.ejushang.steward.common.shiro;

import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.WebUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 该Filter用来解决已登录用户在登录界面不能重新登录的问题
 * User: liubin
 * Date: 13-12-17
 */
public class ShiroReloginFilter extends FormAuthenticationFilter {

    private Logger log = LoggerFactory.getLogger(ShiroReloginFilter.class);


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(isLoginRequest(request, response)) return false;
        Subject subject = getSubject(request, response);
        return subject.isRemembered() || subject.isAuthenticated() || isPermissive(mappedValue);
    }

    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        //TODO 判断是自定义异常则忽略
        log.error("登录发生错误", ae);
        request.setAttribute("errorMsg", ae.getMessage());
    }

    protected String getCaptcha(ServletRequest request) {

        return org.apache.shiro.web.util.WebUtils.getCleanParam(request, "captcha");
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {

        String username = getUsername(request);

        String password = getPassword(request);

        String captcha = getCaptcha(request);

        boolean rememberMe = isRememberMe(request);

        String host = getHost(request);

        return new UsernamePasswordCaptchaToken(username,
                password.toCharArray(), rememberMe, host, captcha);

    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (WebUtils.isAjaxRequest(httpServletRequest)) {
            new JsonResult(true).writeToResponse(httpServletResponse);
            return false;
        } else {
            return super.onLoginSuccess(token, subject, request, response);
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e, ServletRequest request,
                                     ServletResponse response) {
        String errorMsg = e.getMessage();
        if(e instanceof IncorrectCredentialsException) {
            errorMsg = "密码错误";
        }
        if (WebUtils.isAjaxRequest(((HttpServletRequest) request))) {
            try {
                new JsonResult(false, errorMsg).writeToResponse((HttpServletResponse) response);
            } catch (IOException e1) {
                log.error("", e1);
            }
            return true;
        } else {
            return super.onLoginFailure(token, e, request, response);
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {

        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }
            if (WebUtils.isAjaxRequest(((HttpServletRequest) request))) {
                ((HttpServletResponse)response).setStatus(999); //session超时的错误码
//                new JsonResult(JsonResult.FAILURE).setMsg("请重新登录").toJson((HttpServletResponse)response);
                return false;
            } else {
                saveRequestAndRedirectToLogin(request, response);
                return false;
            }
        }
    }


}
