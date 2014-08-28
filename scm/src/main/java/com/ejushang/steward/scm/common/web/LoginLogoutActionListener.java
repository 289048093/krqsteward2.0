package com.ejushang.steward.scm.common.web;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.service.EmployeeService;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户登录登出Listener
 * User: liubin
 * Date: 14-1-16
 */
public class LoginLogoutActionListener implements AuthenticationListener {

    private static final Logger log = LoggerFactory.getLogger(LoginLogoutActionListener.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
        log.info("用户[{}]登录成功", ((UsernamePasswordToken) token).getUsername());
        Employee employee = (Employee)info.getPrincipals().getPrimaryPrincipal();
//        employeeService.doAfterLoginSuccess(employee);

    }

    @Override
    public void onFailure(AuthenticationToken token, AuthenticationException ae) {
        log.info("用户[{}]登录失败", ((UsernamePasswordToken) token).getUsername());
    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        Employee employee = (Employee)principals.getPrimaryPrincipal();
        if(employee == null) {
            log.info("用户已注销");
        } else {
            log.info("用户[{}]已注销", employee.getUsername());
        }
    }

}
