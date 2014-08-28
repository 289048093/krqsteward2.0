package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.EmployeeUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-28
 */
@Table(name = "t_error_info")
@Entity
public class ErrorInfo implements EntityClass<Integer>, OperableData {

    private Integer id;

    private String title;

    private String detail;

    private String extraInfoOne;

    private String extraInfoTwo;

    private String extraInfoThree;

    private Date createTime;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Column(name = "extra_info_one")
    public String getExtraInfoOne() {
        return extraInfoOne;
    }

    public void setExtraInfoOne(String extraInfoOne) {
        this.extraInfoOne = extraInfoOne;
    }

    @Column(name = "extra_info_two")
    public String getExtraInfoTwo() {
        return extraInfoTwo;
    }

    public void setExtraInfoTwo(String extraInfoTwo) {
        this.extraInfoTwo = extraInfoTwo;
    }

    @Column(name = "extra_info_three")
    public String getExtraInfoThree() {
        return extraInfoThree;
    }

    public void setExtraInfoThree(String extraInfoThree) {
        this.extraInfoThree = extraInfoThree;
    }

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
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
    }

    @Override
    @Transient
    public Integer getOperatorId() {
        return null;
    }


}
