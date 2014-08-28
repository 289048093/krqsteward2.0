package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

import java.util.Date;

/**
 * User: Shiro
 * Date: 14-8-11
 * Time: 下午6:01
 */
public class LogisticsInfo extends ZiYouObject {
    private static final long serialVersionUID = 7388462484079720470L;
    /** 物流信息单编号 */
    @ApiField("logistic_id")
    private String logisticId;

    /** 订单号(一个订单在业务上只允许有一个快递单号) */
    @ApiField("order_no")
    private String orderNo;

    /** 物流单号(每一笔物流单号的数据都会向 kuaidi100 发送请求 请求一次就需要付费! 所以, 请务必保证只在线上可以请求及只发送一次) */
    @ApiField("express_no")
    private String expressNo;

    /** 物流公司编码。可选值：shunfeng（顺丰），zhongtong（中通），yunda（韵达），zhaijisong（宅急送），ems（ems），
     * yuantong（圆通），shentong（申通），quanritongkuaidi（全日通），kuaijiesudi（快捷），huitongkuaidi（汇通），
     * guotongkuaidi（国通），lianbangkuaidi（联邦），quanfengkuaidi（全峰），suer（速尔），tiantian（天天）
     * ，youshuwuliu（优速），youzhengguonei（邮政国内小包），zengyi（增益速递），debang（德邦物流），
     * changjiazisong（厂家自送），xinbang（新邦物流），debangwuliu（德邦物流），unknown（未知） */
    @ApiField("express_company")
    private String expressCompany;

    /** 收货地址(到市一级即可 如广东省深圳市, 北京市) */
    @ApiField("send_to")
    private String sendTo;

    /** 物流信息(由快递 100 提供)，为json格式。 */
    @ApiField("express_info")
    private String expressInfo;

    /** 物流状态(1表示配送完成, 0表示未完成) */
    @ApiField("express_status")
    private String expressStatus;

    /** 第一条物流状态时间。格式:yyyy-MM-dd HH:mm:ss */
    @ApiField("first_time")
    private Date firstTime;

    /** 物流状态最新的时间。格式:yyyy-MM-dd HH:mm:ss */
    @ApiField("latest_time")
    private Date latestTime;

    /** 是否已请求第三方物流(1已请求, 0未请求), 默认是 0 */
    @ApiField("was_request")
    private String wasRequest;

    /** 物流信息创建时间。格式:yyyy-MM-dd HH:mm:ss */
    @ApiField("create_time")
    private Date createTime;

    /** 物流信息更新时间。。格式:yyyy-MM-dd HH:mm:ss */
    @ApiField("update_time")
    private Date updateTime;

    public String getLogisticId() {
        return logisticId;
    }

    public void setLogisticId(String logisticId) {
        this.logisticId = logisticId;
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

    public String getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(String expressStatus) {
        this.expressStatus = expressStatus;
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

    public String getWasRequest() {
        return wasRequest;
    }

    public void setWasRequest(String wasRequest) {
        this.wasRequest = wasRequest;
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
