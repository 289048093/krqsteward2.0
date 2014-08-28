package com.ejushang.steward.ordercenter.service.outplatforminvoke.jd;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-6-5
 * Time: 下午4:34
 */
public enum JdProductStatus {
    /**
     * :从未上架,
     */
    NEVER_UP,
    /**
     * :自主下架
     */
    CUSTORMER_DOWN,
    /**
     * :系统下架
     */
    SYSTEM_DOWN,
    /**
     * :在售
     */
    ON_SALE,
    /**
     * : 待审核
     */
    AUDIT_AWAIT,
    /**
     * : 审核不通过
     */
    AUDIT_FAIL

}
