package com.ejushang.steward.logisticscenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.ordercenter.domain.Repository;

import javax.persistence.*;
import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_logistics_printinfo")
@Entity
public class LogisticsPrintInfo implements EntityClass<Integer>, OperableData {

    private Integer id;

    private String name;

    private Integer law;

    private String printHtml;

    private String logisticsPicturePath;

    private Date createTime;

    private Date updateTime;


    /**
     * 打印页面高度
     */
    private Integer pageHeight;

    /**
     * 打印页面宽度
     */
    private Integer pageWidth;

    /**
     * 仓库
     */
    private Integer repositoryId;

    private Repository repository;

    @Column(name = "repository_id")
    @Basic
    public Integer getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", insertable = false, updatable = false)
    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @Column(name = "page_height")
    public Integer getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(Integer pageHeight) {
        this.pageHeight = pageHeight;
    }

    @Column(name = "page_width")
    public Integer getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(Integer pageWidth) {
        this.pageWidth = pageWidth;
    }


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @javax.persistence.Column(name = "law")
    @Basic
    public Integer getLaw() {
        return law;
    }

    public void setLaw(Integer law) {
        this.law = law;
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

    @Override
    public String toString() {
        return "LogisticsPrintInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", law=" + law +
                ", printHtml='" + printHtml + '\'' +
                ", logisticsPicturePath='" + logisticsPicturePath + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
