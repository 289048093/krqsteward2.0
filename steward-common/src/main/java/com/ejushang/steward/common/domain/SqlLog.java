package com.ejushang.steward.common.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_sql_log")
@Entity
public class SqlLog implements EntityClass<Integer> {

    private Integer id;

    private String content;

    private String operationType;

    private Integer executionTime;

    private Integer businessLogId;

    @JsonIgnore
    private BusinessLog businessLog;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="business_log_id", insertable = false, updatable = false)
    public BusinessLog getBusinessLog() {
        return businessLog;
    }

    public void setBusinessLog(BusinessLog businessLog) {
        this.businessLog = businessLog;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "business_log_id")
    @Basic
    public Integer getBusinessLogId() {
        return businessLogId;
    }

    public void setBusinessLogId(Integer businessLogId) {
        this.businessLogId = businessLogId;
    }


    @javax.persistence.Column(name = "content")
    @Basic
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @javax.persistence.Column(name = "operation_type")
    @Basic
    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }


    @javax.persistence.Column(name = "execution_time")
    @Basic
    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

}
