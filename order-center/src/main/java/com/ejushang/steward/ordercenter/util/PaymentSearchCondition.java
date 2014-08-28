package com.ejushang.steward.ordercenter.util;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-5-22
 * Time: 下午2:47
 * To change this template use File | Settings | File Templates.
 */


public class PaymentSearchCondition {

    //时间类型
    private String dateType;

    //起始时间
    private Date startDate;

    //结束时间
    private Date endDate;

    //平台类型
    private String platformType;

    //对应店铺id
    private Integer shopId;

    //预收款类型
    private String type;

    //预收款状态
    private String allocateStatus;

    //包含，不包含，等于，不等于，大于等于，小于等于
    private String conditionQuery;

    //条件类型
    private String conditionType;

    //条件值
    private String conditionValue;

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAllocateStatus() {
        return allocateStatus;
    }

    public void setAllocateStatus(String allocateStatus) {
        this.allocateStatus = allocateStatus;
    }

    public String getConditionQuery() {
        return conditionQuery;
    }

    public void setConditionQuery(String conditionQuery) {
        this.conditionQuery = conditionQuery;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }
}
