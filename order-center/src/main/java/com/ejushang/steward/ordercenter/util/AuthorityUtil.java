package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.uams.api.dto.RoleDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


/**
 * Created by: codec.yang
 * Date: 2014/8/8 11:34
 */
public class AuthorityUtil {

    public static final String RETURN_VISIT_SUPERVISOR_ROLE_NAME="回访主管";
    public static final String AFTER_SALE_ROLE_NAME="售后专员";

    public static Employee getCurrentEmployee(){
        Subject subject= SecurityUtils.getSubject();
        return (Employee) subject.getPrincipal();
    }

    public static boolean hasRole(String roleName){
        Employee employee=getCurrentEmployee();
        if(employee.isRoot()){
            return true;
        }

        for(RoleDto role:employee.getRoles()){
            if(roleName.equalsIgnoreCase(role.getName())){
                return true;
            }
        }

        return false;
    }

}
