package com.ejushang.steward.ordercenter.bean;

import com.ejushang.steward.ordercenter.constant.FetchByType;
import com.ejushang.steward.ordercenter.constant.FetchDataType;
import com.ejushang.steward.ordercenter.constant.FetchDateType;
import com.ejushang.steward.ordercenter.constant.FetchOptType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * User: Baron.Zhang
 * Date: 2014/7/9
 * Time: 15:18
 */
public class FetchConditionBean {

    /** 平台id */
    private Integer platformId;
    /** 店铺id */
    private Integer shopId;
    /** 抓取开始时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fetchStartDate;
    /** 抓取结束时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fetchEndDate;
    private String fetchByTypeName;
    private String fetchDataTypeName;
    private String fetchDateTypeName;
    /** 以","分割的编号列表 */
    private String platformNos;

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Date getFetchStartDate() {
        return fetchStartDate;
    }

    public void setFetchStartDate(Date fetchStartDate) {
        this.fetchStartDate = fetchStartDate;
    }

    public Date getFetchEndDate() {
        return fetchEndDate;
    }

    public void setFetchEndDate(Date fetchEndDate) {
        this.fetchEndDate = fetchEndDate;
    }

    public String getFetchByTypeName() {
        return fetchByTypeName;
    }

    public void setFetchByTypeName(String fetchByTypeName) {
        this.fetchByTypeName = fetchByTypeName;
    }

    public String getFetchDataTypeName() {
        return fetchDataTypeName;
    }

    public void setFetchDataTypeName(String fetchDataTypeName) {
        this.fetchDataTypeName = fetchDataTypeName;
    }

    public String getFetchDateTypeName() {
        return fetchDateTypeName;
    }

    public void setFetchDateTypeName(String fetchDateTypeName) {
        this.fetchDateTypeName = fetchDateTypeName;
    }

    public String getPlatformNos() {
        return platformNos;
    }

    public void setPlatformNos(String platformNos) {
        this.platformNos = platformNos;
    }

    public String toString() {
        return "ShopBean={" +
                "platformId = " + platformId + "," +
                "shopId = " + shopId + "," +
                "fetchStartDate = " + fetchStartDate + "," +
                "fetchEndDate = " + fetchEndDate + "," +
                "fetchDataTypeName = " + fetchDataTypeName + "," +
                "fetchDateTypeName = " + fetchDateTypeName + "," +
                "fetchByTypeName = " + fetchByTypeName + "," +
                "platformNos = " + platformNos + "," +
                "}";
    }
}
