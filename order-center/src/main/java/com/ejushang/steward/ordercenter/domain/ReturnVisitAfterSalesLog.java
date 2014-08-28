package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.ordercenter.constant.SolutionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: codec.yang
 * Date: 2014/8/15 11:05
 */
@Table(name = "t_returnvisit_aftersales_log")
@Entity
public class ReturnVisitAfterSalesLog implements EntityClass<Integer>,OperableData {

    private Integer id;
    private String operator;
    private String remark;
    private Date createTime;
    private Integer returnVisitId;
    private Integer operatorId;
    private SolutionType solutionType;
    private String afterSalesNo;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {

    }

    @Override
    @Transient
    public Date getUpdateTime() {
        return null;
    }

    @Override
    public void setOperatorId(Integer operatorId) {
        this.operatorId=operatorId;
    }

    @Column(name = "operator_id")
    @JsonIgnore
    public Integer getOperatorId() {
        return this.operatorId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name="remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "return_visit_id")
    public Integer getReturnVisitId() {
        return returnVisitId;
    }

    public void setReturnVisitId(Integer returnVisitId) {
        this.returnVisitId = returnVisitId;
    }


    @Column(name="solution_type")
    @Enumerated(EnumType.STRING)
    public SolutionType getSolutionType() {
        return solutionType;
    }

    public void setSolutionType(SolutionType solutionType) {
        this.solutionType = solutionType;
    }

    @Column(name="after_sales_no")
    public String getAfterSalesNo() {
        return afterSalesNo;
    }

    public void setAfterSalesNo(String afterSalesNo) {
        this.afterSalesNo = afterSalesNo;
    }
}
