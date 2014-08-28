package com.ejushang.steward.common.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_business_log")
@Entity
public class BusinessLog implements EntityClass<Integer>, OperableData {

    private Integer id;

    private String operatorName; //操作人的name

    private String operationName;

    private String params;

    private String requestUrl;

    private Integer operatorId; //操作人的id

    private Date createTime;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 请求执行时间,单位毫秒
     */
    private Long executionTime;
    /**
     * 模块名称
     */
    private String resourceName;

    private Boolean operationResult;

    private String operationException;

    private String description;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "operator_name")
    @Basic
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    @javax.persistence.Column(name = "operation_name")
    @Basic
    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @javax.persistence.Column(name = "params")
    @Basic
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }


    @javax.persistence.Column(name = "request_url")
    @Basic
    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }


    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {}

    @Override
    @Transient
    public Date getUpdateTime() {return null;}

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }


    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    @javax.persistence.Column(name = "ip")
    @Basic
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @javax.persistence.Column(name = "execution_time")
    @Basic
    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    @javax.persistence.Column(name = "resource_name")
    @Basic
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @javax.persistence.Column(name = "operation_result")
    @Basic
    public Boolean getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Boolean operationResult) {
        this.operationResult = operationResult;
    }
    @javax.persistence.Column(name = "operation_exception")
    @Basic
    public String getOperationException() {
        return operationException;
    }

    public void setOperationException(String operationException) {
        this.operationException = operationException;
    }
    @javax.persistence.Column(name = "description")
    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
