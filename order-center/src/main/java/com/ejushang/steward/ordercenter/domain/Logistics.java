package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.EJSDateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * User: liubin
 * Date: 14-3-28
 */
@Table(name = "t_logistics_info")
@Entity
public class Logistics implements EntityClass<Integer>, OperableData {

    private Integer id;

    private String orderNo;

    private String expressNo;

    private String expressCompany;

    private String sendTo;

    private String expressInfo;

    private Boolean expressStatus;

    private Date firstTime;

    private Date latestTime;

    private Boolean wasRequest;

    private Date createTime;

    private Date updateTime;

    public Logistics() {}
    /**
     * 用来添加
     *
     * @param orderNo 订单号
     * @param expressNo 物流单号
     * @param expressCompany 物流公司
     * @param sendTo 收货地址(到省一级即可. 如广东省, 北京市)
     */
    public Logistics(String orderNo, String expressNo, String expressCompany, String sendTo) {
        this.orderNo = orderNo;
        this.expressNo = expressNo;
        this.expressCompany = expressCompany;
        this.sendTo = sendTo;
    }

    /**
     * 记录向第三方平台发送了成功请求
     *
     * @param expressNo 物流单号
     * @param wasRequest 是否已请求第三方物流(1 已请求, 0未请求), 默认是 0 >> false
     */
    public Logistics(String expressNo, Boolean wasRequest) {
        this.expressNo = expressNo;
        this.wasRequest = wasRequest;
    }

    /**
     * 用来更新物流信息
     *
     * @param expressNo 物流单号
     * @param firstTime 第一条物流信息时间
     * @param latestTime 最新的物流信息时间
     * @param expressStatus 物流状态, 1 表示成功, 0 表示未成功
     * @param expressInfo 物流信息
     */
    public Logistics(String expressNo, Date firstTime, Date latestTime, Integer expressStatus, String expressInfo) {
        this.expressNo = expressNo;
        this.firstTime = firstTime;
        this.latestTime = latestTime;
        this.expressStatus = (expressStatus == 1);
        this.expressInfo = expressInfo;
    }


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Column(name = "order_no")
    @Basic
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


    @Column(name = "express_no")
    @Basic
    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }


    @Column(name = "express_company")
    @Basic
    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }


    @Column(name = "send_to")
    @Basic
    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }


    @Column(name = "express_info")
    @Basic
    public String getExpressInfo() {
        return expressInfo;
    }

    public void setExpressInfo(String expressInfo) {
        this.expressInfo = expressInfo;
    }


    @Column(name = "express_status")
    @Basic
    public Boolean getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(Boolean expressStatus) {
        this.expressStatus = expressStatus;
    }


    @Column(name = "first_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = UglyTimestampUtil.convertTimestampToDate(firstTime);
    }


    @Column(name = "latest_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(Date latestTime) {
        this.latestTime = UglyTimestampUtil.convertTimestampToDate(latestTime);
    }


    @Column(name = "was_request")
    @Basic
    public Boolean getWasRequest() {
        return wasRequest;
    }

    public void setWasRequest(Boolean wasRequest) {
        this.wasRequest = wasRequest;
    }


    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }


    @Column(name = "update_time")
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
        StringBuilder sbd = new StringBuilder();
        sbd.append("id:").append(id);
        sbd.append(", 订单号:").append(orderNo);
        sbd.append(", 快递单号:").append(expressNo);
        sbd.append(", 寄送到:").append(sendTo);
        if (StringUtils.isNotBlank(expressCompany))
            sbd.append(", 物流公司:").append(expressCompany);
        else
            sbd.append(", 未记录物流公司");

        // ignore info
        // sbd.append(expressInfo);
        if (firstTime != null)
            sbd.append(", 第一条物流信息时间:").append(EJSDateUtils.formatDate(firstTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        if (latestTime != null)
            sbd.append(", 最新的物流信息时间:").append(EJSDateUtils.formatDate(latestTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));

        if (createTime != null)
            sbd.append(", 创建时间:").append(EJSDateUtils.formatDate(createTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        if (updateTime != null)
            sbd.append(", 更新时间:").append(EJSDateUtils.formatDate(updateTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));

        // sbd.append(", 配送状态:").append(expressStatus ? "已完成" : "未完成");

        return sbd.toString();
    }
}
