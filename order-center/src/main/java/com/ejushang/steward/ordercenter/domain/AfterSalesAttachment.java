package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.Money;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.Date;
import java.util.List;

import com.ejushang.steward.ordercenter.constant.AttachmentType;

/**
 * 售后附件表
 * @Author Channel
 * @Date 2014-08-22
 */
@Table(name = "t_after_sales_attachment")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSalesAttachment implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 所属售后工单ID
     */
    private Integer afterSalesId;
    /**
     * 附件名
     */
    private String name;
    /**
     * 附件路径
     */
    private String path;
    /**
     * AttachmentType:附件类型;IMAGE:图片,DOC:word,EXL:excel,OTH:other;
     */
    private AttachmentType type;
    /**
     * 操作人员ID
     */
    private Integer operatorId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
    * 获取"id"
    */
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    /**
     * 设置"id"
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
    * 获取"所属售后工单ID"
    */
    @javax.persistence.Column(name = "after_sales_id")
    @Basic
    public Integer getAfterSalesId() {
        return afterSalesId;
    }

    /**
     * 设置"所属售后工单ID"
     */
    public void setAfterSalesId(Integer afterSalesId) {
        this.afterSalesId = afterSalesId;
    }
    /**
    * 获取"附件名"
    */
    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    /**
     * 设置"附件名"
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
    * 获取"附件路径"
    */
    @javax.persistence.Column(name = "path")
    @Basic
    public String getPath() {
        return path;
    }

    /**
     * 设置"附件路径"
     */
    public void setPath(String path) {
        this.path = path;
    }
    /**
    * 获取"AttachmentType:附件类型;IMAGE:图片,DOC:word,EXL:excel,OTH:other;"
    */
    @javax.persistence.Column(name = "type")
    @Enumerated(EnumType.STRING)
    public AttachmentType getType() {
        return type;
    }

    /**
     * 设置"AttachmentType:附件类型;IMAGE:图片,DOC:word,EXL:excel,OTH:other;"
     */
    public void setType(AttachmentType type) {
        this.type = type;
    }
    /**
    * 获取"操作人员ID"
    */
    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    /**
     * 设置"操作人员ID"
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
    /**
    * 获取"创建时间"
    */
    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置"创建时间"
     */
    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }
    /**
    * 获取"更新时间"
    */
    @javax.persistence.Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置"更新时间"
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);
    }
    /**
     * 售后单实体
     */
    @JsonIgnore
    private AfterSales afterSales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_sales_id", insertable = false, updatable = false)
    public AfterSales getAfterSales() {
        return afterSales;
    }

    public void setAfterSales(AfterSales afterSales) {
        this.afterSales = afterSales;
    }
    @Override
    public String toString() {
        StringBuilder strBuf = new StringBuilder("AfterSalesAttachment{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("afterSalesId=").append(this.afterSalesId);
        strBuf.append(", ");
        strBuf.append("name=").append(this.name);
        strBuf.append(", ");
        strBuf.append("path=").append(this.path);
        strBuf.append(", ");
        strBuf.append("type=").append(this.type);
        strBuf.append(", ");
        strBuf.append("operatorId=").append(this.operatorId);
        strBuf.append(", ");
        strBuf.append("createTime=").append(this.createTime);
        strBuf.append(", ");
        strBuf.append("updateTime=").append(this.updateTime);
        strBuf.append("}");
        return strBuf.toString();
    }

}