package com.ejushang.steward.ordercenter.vo;

/**
 * User:moon
 * Date: 14-8-13
 * Time: 上午9:56
 */
public class ApportionPersonVo {

    //用户id
    private Integer employeeId;

    //用户名
    private String username;

    //真实姓名
    private String  employeeName;
    //分配数量
    private Integer apportionCount;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getApportionCount() {
        return apportionCount;
    }

    public void setApportionCount(Integer apportionCount) {
        this.apportionCount = apportionCount;
    }
}
