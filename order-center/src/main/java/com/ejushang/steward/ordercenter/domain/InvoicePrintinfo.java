package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_invoice_printinfo")
@Entity
public class InvoicePrintinfo implements EntityClass<Integer>, OperableData {

    private Integer id;

    private String printHtml;

    private String logisticsPicturePath;

    private Date createTime;

    private Date updateTime;



    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "print_html")
    @Basic
    public String getPrintHtml() {
        return printHtml;
    }

    public void setPrintHtml(String printHtml) {
        this.printHtml = printHtml;
    }


    @javax.persistence.Column(name = "logistics_picture_path")
    @Basic
    public String getLogisticsPicturePath() {
        return logisticsPicturePath;
    }

    public void setLogisticsPicturePath(String logisticsPicturePath) {
        this.logisticsPicturePath = logisticsPicturePath;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }


    @javax.persistence.Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setOperatorId(Integer operatorId) {
    }

    @Override
    @Transient
    public Integer getOperatorId() {
        return null;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);
    }

}
