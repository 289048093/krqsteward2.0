package com.ejushang.steward.openapicenter.jd.constant;

import com.ejushang.steward.openapicenter.jd.util.JdAppConfig;
import org.apache.commons.lang3.StringUtils;

/**
 * User: Baron.Zhang
 * Date: 14-4-11
 * Time: 下午2:45
 */
public class ConstantJingDong {
    private static final JdAppConfig APP_CONFIG = JdAppConfig.getInstance();

    /**
     * 使用OAuth2.0协议授权地址
     */
    public static final String JD_AUTHORIZE_URL = APP_CONFIG.getProperty("JD_AUTHORIZE_URL");

    /**
     * 使用OAuth2.0协议获取Access Token（即Session Key）地址
     */
    public static final String JD_TOKEN_URL = APP_CONFIG.getProperty("JD_TOKEN_URL");
    /**
     * 京东API访问地址
     */
    public static final String JD_API_URL = APP_CONFIG.getProperty("JD_API_URL");
    /**
     * 京东消息服务API访问地址
     */
    public static final String JD_MC_API_URL = APP_CONFIG.getProperty("JD_MC_API_URL");

    /**
     * 京东APP回调地址
     */
    public static final String JD_REDIRECT_URL = APP_CONFIG.getProperty("JD_REDIRECT_URL");
    /**
     * 京东APP_KEY
     */
    public static final String JD_APP_KEY = APP_CONFIG.getProperty("JD_APP_KEY");
    /**
     * 京东APP_SECRET
     */
    public static final String JD_APP_SECRET = APP_CONFIG.getProperty("JD_APP_SECRET");

    /** 订单查询间隔时间，单位：秒 */
    public static final Integer JD_FETCH_ORDER_TIME_INTERVAL = APP_CONFIG.getProperty("JD_FETCH_ORDER_TIME_INTERVAL") == null
            ? 300 : Integer.valueOf(APP_CONFIG.getProperty("JD_FETCH_ORDER_TIME_INTERVAL"));
    /** 订单查询间隔时间增量，单位：秒 */
    public static final Integer JD_FETCH_ORDER_TIME_INTERVAL_INCREMENT = APP_CONFIG.getProperty("JD_FETCH_ORDER_TIME_INTERVAL_INCREMENT") == null
            ? 1800 : Integer.valueOf(APP_CONFIG.getProperty("JD_FETCH_ORDER_TIME_INTERVAL_INCREMENT"));
    /** 订单当前最后抓取时间往后推的时间，单位：秒 */
    public static final Integer JD_FETCH_ORDER_TIME_DELAY = APP_CONFIG.getProperty("JD_FETCH_ORDER_TIME_DELAY") == null
            ? -30 : Integer.valueOf(APP_CONFIG.getProperty("JD_FETCH_ORDER_TIME_DELAY"));
    /** 订单查询每页记录数 */
    public static final String JD_FETCH_ORDER_PAGE_SIZE = APP_CONFIG.getProperty("JD_FETCH_ORDER_PAGE_SIZE") == null
            ? "100" : APP_CONFIG.getProperty("JD_FETCH_ORDER_PAGE_SIZE");

    /** 退款单查询每页记录数 */
    public static final Integer JD_FETCH_REFUND_PAGE_SIZE = StringUtils.isBlank(APP_CONFIG.getProperty("JD_FETCH_REFUND_PAGE_SIZE"))
            ? 100 : Integer.valueOf(APP_CONFIG.getProperty("JD_FETCH_REFUND_PAGE_SIZE"));

    /** 京东：查询时间类型，1：按订单创建时间抓取，其他数字：按订单状态变更（如订单状态，运单号变更）时间抓取 */
    public static final String JD_DATE_TYPE = "0";

    /**
     * 店铺api参数与返回值
     */
    public static final String CREATE_TIME = "create_time";
    public static final String CAT_NAME = "cat_name";
    public static final String HOME_SHOW = "home_show";
    public static final String FIELDS = "fields";
    public static final String SHOPCATS = "shopCats";
    public static final String CID = "cid";
    public static final String PARENT_ID = "parent_id";
    public static final String NAME = "name";
    public static final String IS_PARENT = "is_parent";
    public static final String IS_OPEN = "is_open";
    public static final String IS_HOME_SHOW = "is_home_show";
    public static final String INDEX_ID = "index_id";
    public static final String VENDER_INFO_RESULT = "vender_info_result";
    public static final String COL_TYPE = "col_type";
    public static final String CATE_MAIN = "cate_main";
    public static final String SHOP_JOS_RESULT = "shop_jos_result";
    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_NAME = "shop_name";
    public static final String OPEN_TIME = "open_time";
    public static final String LOGO_URL = "logo_url";
    public static final String BRIEF = "brief";
    public static final String CATEGORY_MAIN = "category_main";
    public static final String CATEGORY_MAIN_NAME = "category_main_name";

    /**
     * 订单api参数与返回值
     */
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String DATETYPE = "dateType";
    public static final String PAGE = "page";
    public static final String SORTTYPE = "sortType";
    public static final String ORDER_SEARCH = "order_search";
    public static final String ORDER_INFO_LIST = "order_info_list";
    public static final String ORDER_TOTAL = "order_total";
    public static final String PAY_TYPE = "pay_type";
    public static final String ORDER_TOTAL_PRICE = "order_total_price";
    public static final String ORDER_PAYMENT = "order_payment";
    public static final String ORDER_SELLER_PRICE = "order_seller_price";
    public static final String FREIGHT_PRICE = "freight_price";
    public static final String SELLER_DISCOUNT = "seller_discount";
    public static final String ORDER_STATE_REMARK = "order_state_remark";
    public static final String DELIVERY_TYPE = "delivery_type";
    public static final String INVOICE_INFO = "invoice_info";
    public static final String ORDER_REMARK = "order_remark";
    public static final String ORDER_START_TIME = "order_start_time";
    public static final String ORDER_END_TIME = "order_end_time";
    public static final String CONSIGNEE_INFO = "consignee_info";
    public static final String ITEM_INFO_LIST = "item_info_list";
    public static final String COUPON_DETAIL_LIST = "coupon_detail_list";
    public static final String RETURN_ORDER = "return_order";
    public static final String PIN = "pin";
    public static final String BALANCE_USED = "balance_used";
    public static final String PAYMENT_CONFIRM_TIME = "payment_confirm_time";
    public static final String VAT_INVOICE_INFO = "vat_invoice_info";
    public static final String PARENT_ORDER_ID = "parent_order_id";
    public static final String ORDER_TYPE = "order_type";
    public static final String OUTER_SKU_ID = "outer_sku_id";
    public static final String SKU_NAME = "sku_name";
    public static final String GIFT_POINT = "gift_point";
    public static final String ITEM_TOTAL = "item_total";
    public static final String FULLNAME = "fullname";
    public static final String FULL_ADDRESS = "full_address";
    public static final String TELEPHONE = "telephone";
    public static final String MOBILE = "mobile";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String COUNTY = "county";
    public static final String COUPON_PRICE = "coupon_price";
    public static final String COUPON_TYPE = "coupon_type";
    public static final String SKU_ID = "sku_id";
    public static final String TAXPAYER_IDENT = "taxpayer_ident";
    public static final String REGISTERED_ADDRESS = "registered_address";
    public static final String REGISTERED_PHONE = "registered_phone";
    public static final String DEPOSIT_BANK = "deposit_bank";
    public static final String BANK_ACCOUNT = "bank_account";
    public static final String ORDER_STATE = "order_state";
    public static final String ORDER = "order";
    public static final String ORDERINFO = "OrderInfo";
    public static final String OPTIONAL_FIELDS = "optional_fields";
    public static final String PRINT_RESULT = "print_result";
    public static final String HTML_CONTENT = "html_content";
    public static final String IMAGE_DATA = "image_data";
    public static final String CKY2_NAME = "cky2_name";
    public static final String PICKUPSIGN_TYPE = "pickUpSign_type";
    public static final String ORDERLEVEL_TYPE = "orderLevel_Type";
    public static final String ORDER_PRINTDATA = "order_printdata";
    public static final String OUT_BOUND_DATE = "out_bound_date";
    public static final String BF_DELI_GOOD_GLAG = "bf_deli_good_glag";
    public static final String COD_TIME_NAME = "cod_time_name";
    public static final String SORTING_CODE = "sorting_code";
    public static final String CREATE_DATE = "create_date";
    public static final String SHOULD_PAY = "should_pay";
    public static final String PAYMENT_TYPESTR = "payment_typeStr";
    public static final String PARTNER = "partner";
    public static final String GENERADE = "generade";
    public static final String INVOICE_TITLE = "invoice_title";
    public static final String INVOICE_TYPE = "invoice_type";
    public static final String INVOICE_CONTENT = "invoice_content";
    public static final String ITEMS_COUNT = "items_count";
    public static final String ORDER_ITEM = "order_item";
    public static final String CONSIGNEE = "consignee";
    public static final String FREIGHT = "freight";
    public static final String CONS_NAME = "cons_name";
    public static final String CONS_PHONE = "cons_phone";
    public static final String CONS_ADDRESS = "cons_address";
    public static final String CONS_HANDSET = "cons_handset";
    public static final String WARE_ID = "ware_id";
    public static final String WARE_NAME = "ware_name";
    public static final String NUM = "num";
    public static final String JD_PRICE = "jd_price";
    public static final String PRICE = "price";
    public static final String PRODUCE_NO = "produce_no";
    public static final String LOGISTICS_ID = "logistics_id";
    public static final String WAYBILL = "waybill";
    public static final String WAYBILLCODE = "waybillCode";
    public static final String VENDERREMARKQUERYRESULT = "venderRemarkQueryResult";
    public static final String API_JOS_RESULT= "api_jos_result";
    public static final String VENDER_REMARK = "vender_remark";
    public static final String CREATED = "created ";
    public static final String REMARK = "remark";
    public static final String TRADE_NO = "trade_no";
    public static final String FLAG = "flag";
    public static final String VENDER_ID = "vender_id";
    public static final String MODIFIED = "modified";
    public static final String CODE = "code";
    public static final String DEL_APPLY_TYPE = "del_apply_type";
    public static final String DEL_APPLY_REASON = "del_apply_reason";
    public static final String RESULT = "result";
    public static final String SUCCESS = "success";
    public static final String RESULT_CODE = "result_code";
    public static final String RESULT_DESCRIBE = "result_describe";

    public static final String STATUS = "status";
    public static final String APPLY_TIME_START = "apply_time_start";
    public static final String APPLY_TIME_END = "apply_time_end";
    public static final String CHECK_TIME_START = "check_time_start";
    public static final String CHECK_TIME_END = "check_time_end";
    public static final String PAGE_INDEX = "page_index";
    public static final String PAGE_SIZE = "page_size";
    public static final String ID = "id";
    public static final String ORDER_ID = "order_id";
    public static final String BUYER_ID = "buyer_id";
    public static final String BUYER_NAME = "buyer_name";




}
