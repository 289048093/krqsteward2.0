package com.ejushang.steward.common.util;

import com.ejushang.steward.common.domain.Employee;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: liubin
 * Date: 14-1-13
 */
public class SessionUtils {

    private static final Logger log = LoggerFactory.getLogger(SessionUtils.class);

    /**
     * 得到当前登录用户
     * @return
     */
    public static Employee getEmployee() {
        try {
            return (Employee)SecurityUtils.getSubject().getPrincipal();
        } catch (UnavailableSecurityManagerException ignore) {
            //此时一般是web.xml的shiro filter被注释了.可以忽略
        } catch (Exception e) {
            log.error("获取user的时候出错", e);
        }
        return null;
    }

}
