package com.ejushang.steward.common.shiro;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.service.EmployeeService;
import com.ejushang.uams.api.dto.ErrorCode;
import com.ejushang.uams.exception.UamsClientException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by liubin on 13-12-16.
 */
public class ShiroDatabaseRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(ShiroDatabaseRealm.class);

    @Autowired
    private EmployeeService employeeService;

	/**
	 * 用户登录的认证方法
	 *
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordCaptchaToken usernamePasswordToken = (UsernamePasswordCaptchaToken) token;

        // 判断验证码
        String captcha = usernamePasswordToken.getCaptcha();
        Session session = SecurityUtils.getSubject().getSession();
        String captchaInSession = (String)session.getAttribute("captcha");
        session.setAttribute("captcha", null);
        if (null == captcha || !captcha.equalsIgnoreCase(captchaInSession)) {
            throw new AuthenticationException("验证码错误");
        }

		String username = usernamePasswordToken.getUsername();

		if (username == null) {
			throw new AccountException("用户名不能为空");
		}

        Employee employee;
        try {

            employee = employeeService.login(username, new String(usernamePasswordToken.getPassword()));

        } catch (UamsClientException e) {
            ErrorCode errorCode = ErrorCode.valueOf(e.getErrorInfo().getErrorCode());
            if(errorCode == null) {
                log.error("登录失败", e);
                throw new AuthenticationException("登录失败");
            }
            switch (errorCode) {
                case EMPLOYEE_NOT_EXIST: {
                    throw new UnknownAccountException("用户不存在");
                }
                case USERNAME_PASSWORD_MISMATCH: {
                    throw new IncorrectCredentialsException("密码错误");
                }
                case EMPLOYEE_FROZEN: {
                    throw new DisabledAccountException("该用户已被禁止登录");
                }
                default: {
                    log.error("登录失败", e);
                    throw new AuthenticationException("登录失败");
                }
            }
        } catch (Exception e) {
            log.error("登录发生错误", e);
            throw new AuthenticationException("登录失败");
        }

        return new SimpleAuthenticationInfo(employee, null, getName());
    }

    /**
     *
     * 当用户进行访问链接时的授权方法
     *
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("Principal对象不能为空");
        }

        Employee employee = (Employee)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        info.addObjectPermissions((Collection<org.apache.shiro.authz.Permission>)employee.getPermissionsCache().values());

        return info;
    }

    private boolean checkIfRootUser(PrincipalCollection principals) {
        Employee employee = (Employee)principals.getPrimaryPrincipal();
        return (employee != null && employee.isRoot());
    }

    //以下方法都是为root用户服务,判断为root直接跳过验证

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        if(checkIfRootUser(principals)) {
            return true;
        }
        return super.isPermitted(principals, permission);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, org.apache.shiro.authz.Permission permission) {
        if(checkIfRootUser(principals)) {
            return true;
        }
        return super.isPermitted(principals, permission);
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection subjectIdentifier, String... permissions) {
        if(permissions == null || permissions.length == 0) {
            return null;
        }
        if(checkIfRootUser(subjectIdentifier)) {
            boolean[] results = new boolean[permissions.length];
            Arrays.fill(results, true);
            return results;
        }
        return super.isPermitted(subjectIdentifier, permissions);
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection principals, List<org.apache.shiro.authz.Permission> permissions) {
        if(permissions == null || permissions.isEmpty()) {
            return null;
        }
        if(checkIfRootUser(principals)) {
            boolean[] results = new boolean[permissions.size()];
            Arrays.fill(results, true);
            return results;
        }
        return super.isPermitted(principals, permissions);
    }

    @Override
    public boolean isPermittedAll(PrincipalCollection subjectIdentifier, String... permissions) {
        if(checkIfRootUser(subjectIdentifier)) {
            return true;
        }
        return super.isPermittedAll(subjectIdentifier, permissions);
    }

    @Override
    public boolean isPermittedAll(PrincipalCollection principal, Collection<org.apache.shiro.authz.Permission> permissions) {
        if(checkIfRootUser(principal)) {
            return true;
        }
        return super.isPermittedAll(principal, permissions);
    }

    @Override
    public void checkPermission(PrincipalCollection subjectIdentifier, String permission) throws AuthorizationException {
        if(checkIfRootUser(subjectIdentifier)) {
            return;
        }
        super.checkPermission(subjectIdentifier, permission);
    }

    @Override
    public void checkPermission(PrincipalCollection principal, org.apache.shiro.authz.Permission permission) throws AuthorizationException {
        if(checkIfRootUser(principal)) {
            return;
        }
        super.checkPermission(principal, permission);
    }

    @Override
    public void checkPermissions(PrincipalCollection subjectIdentifier, String... permissions) throws AuthorizationException {
        if(checkIfRootUser(subjectIdentifier)) {
            return;
        }
        super.checkPermissions(subjectIdentifier, permissions);
    }

    @Override
    public void checkPermissions(PrincipalCollection principal, Collection<org.apache.shiro.authz.Permission> permissions) throws AuthorizationException {
        if(checkIfRootUser(principal)) {
            return;
        }
        super.checkPermissions(principal, permissions);
    }

}
