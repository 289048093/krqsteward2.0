package com.ejushang.steward.ordercenter.bean;

import com.ejushang.steward.ordercenter.constant.FetchDataType;
import com.ejushang.steward.ordercenter.constant.FetchOptType;

import java.util.Date;

/**
 * User: Baron.Zhang
 * Date: 2014/7/15
 * Time: 10:08
 */
public class ShopFetchBean {

    /** 抓取操作类型 */
    private FetchOptType fetchOptType;

    /** 抓取数据类型 */
    private FetchDataType fetchDataType;

    /** 抓取状态 */
    private String status;

    /** 抓取开始时间 */
    private Date fetchStartDate;

    /** 抓取结束时间 */
    private Date fetchEndDate;

    public FetchOptType getFetchOptType() {
        return fetchOptType;
    }

    public void setFetchOptType(FetchOptType fetchOptType) {
        this.fetchOptType = fetchOptType;
    }

    public FetchDataType getFetchDataType() {
        return fetchDataType;
    }

    public void setFetchDataType(FetchDataType fetchDataType) {
        this.fetchDataType = fetchDataType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
