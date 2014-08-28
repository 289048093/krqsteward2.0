package com.ejushang.steward.openapicenter.zy.constant;

import com.ejushang.steward.openapicenter.zy.util.ZyAppConfig;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * User: Baron.Zhang
 * Date: 2014/8/4
 * Time: 14:50
 */
public class ConstantZiYou {

    /** 自有开放平台属性文件 */
    public static final ZyAppConfig APP_CONFIG = ZyAppConfig.getInstance();

    /** 自有开放平台APP_KEY */
    public static final String ZY_APP_KEY = APP_CONFIG.getProperty("ZY_APP_KEY");
    /** 自有开放平台ZY_APP_SECRET */
    public static final String ZY_APP_SECRET = APP_CONFIG.getProperty("ZY_APP_SECRET");
    /** 自有开放平台ZY_ACCESS_TOKEN */
    public static final String YYL_ACCESS_TOKEN = APP_CONFIG.getProperty("YYL_ACCESS_TOKEN");

    /** 自有平台API访问地址 */
    public static final String ZY_API_URL = APP_CONFIG.getProperty("ZY_API_URL");

    /** 订单查询间隔时间，单位：秒 */
    public static final Integer ZY_FETCH_ORDER_TIME_INTERVAL = APP_CONFIG.getProperty("ZY_FETCH_ORDER_TIME_INTERVAL") == null
            ? 300 : Integer.valueOf(APP_CONFIG.getProperty("ZY_FETCH_ORDER_TIME_INTERVAL"));
    /** 订单查询间隔时间增量，单位：秒 */
    public static final Integer ZY_FETCH_ORDER_TIME_INTERVAL_INCREMENT = APP_CONFIG.getProperty("ZY_FETCH_ORDER_TIME_INTERVAL_INCREMENT") == null
            ? 1800 : Integer.valueOf(APP_CONFIG.getProperty("ZY_FETCH_ORDER_TIME_INTERVAL_INCREMENT"));
    /** 订单当前最后抓取时间往后推的时间，单位：秒 */
    public static final Integer ZY_FETCH_ORDER_TIME_DELAY = APP_CONFIG.getProperty("ZY_FETCH_ORDER_TIME_DELAY") == null
            ? -30 : Integer.valueOf(APP_CONFIG.getProperty("ZY_FETCH_ORDER_TIME_DELAY"));
    /** 订单查询每页记录数 */
    public static final Long ZY_FETCH_ORDER_PAGE_SIZE = APP_CONFIG.getProperty("ZY_FETCH_ORDER_PAGE_SIZE") == null
            ? 100L : NumberUtils.toLong(APP_CONFIG.getProperty("ZY_FETCH_ORDER_PAGE_SIZE"));

    /** 使用api抓取退款单时，一页多少条抓取 */
    public static final Long ZY_FETCH_REFUND_PAGE_SIZE = APP_CONFIG.getProperty("ZY_FETCH_REFUND_PAGE_SIZE") == null
            ? 40L : NumberUtils.toLong(APP_CONFIG.getProperty("ZY_FETCH_REFUND_PAGE_SIZE"));


    /**
     * 订单API
     */
    public static final String DATE_TYPE="date_type";
    public static final String START_CREATED="start_created";
    public static final String END_CREATED="end_created";
    public static final String STATUS="status";
    public static final String BUYER_NICK="buyer_nick";
    public static final String PAGE_NO="page_no";
    public static final String PAGE_SIZE="page_size";
    public static final String TID="tid";


    /**
     * 退款退货API
     */

    public static final String REFUND_ID="refund_id";
    public static final String REFUND_PHASE="refund_phase";
    public static final String SID="sid";
    public static final String COMPANY_CODE="company_code";

}
