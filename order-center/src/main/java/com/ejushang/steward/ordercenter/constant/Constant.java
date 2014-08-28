package com.ejushang.steward.ordercenter.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 14-2-26
 * Time: 下午4:46
 */
public class Constant {

    public static final class Encoding{
        public static final String UTF8 = "UTF-8";
    }

    /** 抓取订单来源 */
    public static final class OrderFetchType{
        /** 通过api抓取订单 */
        public static final String API = "api";
        /** 通过聚石塔抓取订单 */
        public static final String JST = "jst";
    }

    public static final List<String> LOGISTICS_EXCEL_HEAD_LIST = new ArrayList<String>(){
        {
            add("");
        }
    };

}
