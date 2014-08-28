package com.ejushang.steward.common.util;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.service.EmployeeService;

/**
 * User: liubin
 * Date: 14-4-16
 */
public class EmployeeUtil {

    private static EmployeeService employeeService = Application.getBean(EmployeeService.class);

    public static String getOperatorName(Integer id) {
        if(id == null) return null;
        Employee employee = employeeService.get(id);
        if(employee == null) return null;
        return employee.getName();
    }

    public static String getOperatorUsername(Integer id) {
        if(id == null) return null;
        Employee employee = employeeService.get(id);
        if(employee == null) return null;
        return employee.getUsername();
    }

    public static Employee getOperator(Integer id) {
        if(id == null) return null;
        return employeeService.get(id);
    }


}
