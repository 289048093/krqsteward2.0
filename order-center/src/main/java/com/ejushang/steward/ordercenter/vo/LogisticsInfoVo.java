package com.ejushang.steward.ordercenter.vo;

import java.util.Date;

/**
 * User: Shiro
 * Date: 14-8-1
 * Time: 下午3:04
 */
public class LogisticsInfoVo {
    private Integer id;

    /** 订单号(一个订单在业务上只允许有一个快递单号) */
    private String orderNo;

    /** 物流单号(每一笔物流单号的数据都会向 kuaidi100 发送请求. 请求一次就需要付费! 所以, 请务必保证只在线上可以请求及只发送一次) */
    private String expressNo;

    /** 物流公司名(shunfeng, yunda, ems, tiantian等) */
    private String expressCompany;

    /** 物流公司中文名(shunfeng, yunda, ems, tiantian等) */
    private String expressCompanyName;

    /** 收货地址(到市一级即可. 如广东省深圳市, 北京市) */
    private String sendTo;

    /** 物流信息(由 快递100 提供, 存储 json 字符串) */
    private String expressInfo;

    /** 物流状态(1 表示配送完成, 0 表示未完成), 默认是 0 */
    private Boolean expressStatus;

    /** 物流状态名称 */
    private String expressStatusName;

    /** 第一条状态的时间 */
    private Date firstTime;

    /** 物流状态最新的时间 */
    private Date latestTime;

    /** 是否已请求第三方物流(1已请求, 0未请求), 默认是 0 */
    private Boolean wasRequest;

    /** 是否已请求第三方物流名称(1已请求, 0未请求), 默认是 0 */
    private String wasRequestName;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressCompanyName() {
        return expressCompanyName;
    }

    public void setExpressCompanyName(String expressCompanyName) {
        this.expressCompanyName = expressCompanyName;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getExpressInfo() {
        return expressInfo;
    }

    public void setExpressInfo(String expressInfo) {
        this.expressInfo = expressInfo;
    }

    public Boolean getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(Boolean expressStatus) {
        this.expressStatus = expressStatus;
    }

    public String getExpressStatusName() {
        return expressStatusName;
    }

    public void setExpressStatusName(String expressStatusName) {
        this.expressStatusName = expressStatusName;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Date getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(Date latestTime) {
        this.latestTime = latestTime;
    }

    public Boolean getWasRequest() {
        return wasRequest;
    }

    public void setWasRequest(Boolean wasRequest) {
        this.wasRequest = wasRequest;
    }

    public String getWasRequestName() {
        return wasRequestName;
    }

    public void setWasRequestName(String wasRequestName) {
        this.wasRequestName = wasRequestName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
