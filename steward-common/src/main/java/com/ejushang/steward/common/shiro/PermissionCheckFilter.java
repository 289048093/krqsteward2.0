package com.ejushang.steward.common.shiro;

import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.PermissionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 校验url访问权限
 * User: liubin
 * Date: 13-12-16
 */
public class PermissionCheckFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        Subject subject = getSubject(request, response);
        String requestURI = getPathWithinApplication(request);
        return subject.isPermitted(new UrlPermission(trimUrlSuffix(requestURI)));
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        Subject subject = getSubject(request, response);
        // If the subject isn't identified, redirect to login URL
        if (subject.getPrincipal() == null) {
            saveRequestAndRedirectToLogin(request, response);
        } else {
            HttpServletResponse resp = (HttpServletResponse) response;
            //判断没有权限,则返回授权失败的json信息
            String requestURI = getPathWithinApplication(request);
            String errorMsg = "您没有操作权限,请求的URI:" + requestURI;
            resp.setStatus(998); //没有权限的错误码
            new JsonResult(false, errorMsg).writeToResponse(resp);
        }
        return false;
    }

    /**
     * 去除url后缀,调用前:doc.xxx.html,调用后:doc.xxx
     * @param url
     * @return
     */
    private String trimUrlSuffix(String url) {
        if(StringUtils.isBlank(url)) return url;
        int index = url.lastIndexOf(".");
        if(index == -1) return url;
        return url.substring(0, index);
    }
}
