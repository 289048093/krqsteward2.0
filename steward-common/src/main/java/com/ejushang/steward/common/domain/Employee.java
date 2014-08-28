package com.ejushang.steward.common.domain;

import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.uams.api.dto.ResourceDto;
import com.ejushang.uams.api.dto.RoleDto;
import com.ejushang.uams.enumer.EmployeeStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 员工信息
 * User: liubin
 * Date: 14-4-8
 */
public class Employee implements Serializable {

    private Integer id;

    /**
     * 登录名 *
     */
    private String username;

    /**
     * 员工状态 *
     */
    private EmployeeStatus status;

    /**
     * 真实姓名 *
     */
    private String name;

    /**
     * 是否是超级管理员 *
     */
    private boolean root;

    private List<RoleDto> roles;


    @JsonIgnore
    /** 用户拥有的权限缓存,key为url,value为UrlPermission对象 **/
    private Map<String, ? extends Object> permissionsCache;

    @JsonIgnore
    /** 用户拥有的资源和权限缓存 **/
    private List<ResourceDto> resourceList = new ArrayList<ResourceDto>(0);

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    public List<ResourceDto> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<ResourceDto> resourceList) {
        this.resourceList = resourceList;
    }

    public void setPermissionsCache(Map<String, ? extends Object> permissionsCache) {
        this.permissionsCache = permissionsCache;
    }

    public Map<String, ? extends Object> getPermissionsCache() {
        return permissionsCache;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    /**
     * 是否是仓库人员
     *
     * @return
     */
    @Transient
    public boolean isRepositoryEmployee() {
        if (getRoles() != null) {
            for (RoleDto role : getRoles()) {
                if (role.getName().contains("仓库")) {
                    return true;
                }
            }
        }
        return false;
    }
}
