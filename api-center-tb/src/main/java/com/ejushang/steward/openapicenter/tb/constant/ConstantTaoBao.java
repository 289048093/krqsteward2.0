package com.ejushang.steward.openapicenter.tb.constant;

import com.ejushang.steward.openapicenter.tb.util.TbAppConfig;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * 常量类
 * User: Baron.Zhang
 * Date: 13-12-13
 * Time: 下午2:48
 */
public class ConstantTaoBao {
    private static final TbAppConfig APP_CONFIG = TbAppConfig.getInstance();

    /**
     * 使用OAuth2.0协议授权地址
     */
    public static final String TB_AUTHORIZE_URL = APP_CONFIG.getProperty("TB_AUTHORIZE_URL");

    /**
     * 使用OAuth2.0协议获取Access Token（即Session Key）地址
     */
    public static final String TB_TOKEN_URL = APP_CONFIG.getProperty("TB_TOKEN_URL");
    /**
     * 淘宝API访问地址
     */
    public static final String TB_API_URL = APP_CONFIG.getProperty("TB_API_URL");
    /**
     * 淘宝消息服务API访问地址
     */
    public static final String TB_MC_API_URL = APP_CONFIG.getProperty("TB_MC_API_URL");
    /**
     * 淘宝APP回调地址
     */
    public static final String TB_REDIRECT_URL = APP_CONFIG.getProperty("TB_REDIRECT_URL");
    /**
     * 淘宝APP_KEY
     */
    public static final String TB_APP_KEY = APP_CONFIG.getProperty("TB_APP_KEY");
    /**
     * 淘宝APP_SECRET
     */
    public static final String TB_APP_SECRET = APP_CONFIG.getProperty("TB_APP_SECRET");

    /** 订单查询间隔时间，单位：秒 */
    public static final Integer TB_FETCH_ORDER_TIME_INTERVAL = APP_CONFIG.getProperty("TB_FETCH_ORDER_TIME_INTERVAL") == null
            ? 300 : Integer.valueOf(APP_CONFIG.getProperty("TB_FETCH_ORDER_TIME_INTERVAL"));
    /** 订单查询间隔时间增量，单位：秒 */
    public static final Integer TB_FETCH_ORDER_TIME_INTERVAL_INCREMENT = APP_CONFIG.getProperty("TB_FETCH_ORDER_TIME_INTERVAL_INCREMENT") == null
            ? 1800 : Integer.valueOf(APP_CONFIG.getProperty("TB_FETCH_ORDER_TIME_INTERVAL_INCREMENT"));
    /** 订单当前最后抓取时间往后推的时间，单位：秒 */
    public static final Integer TB_FETCH_ORDER_TIME_DELAY = APP_CONFIG.getProperty("TB_FETCH_ORDER_TIME_DELAY") == null
            ? -30 : Integer.valueOf(APP_CONFIG.getProperty("TB_FETCH_ORDER_TIME_DELAY"));
    /** 订单查询每页记录数 */
    public static final Long TB_FETCH_ORDER_PAGE_SIZE = APP_CONFIG.getProperty("TB_FETCH_ORDER_PAGE_SIZE") == null
            ? 100L : NumberUtils.toLong(APP_CONFIG.getProperty("TB_FETCH_ORDER_PAGE_SIZE"));

    /** 订单查询每页记录数 */
    public static final Long TB_FETCH_RATE_PAGE_SIZE = APP_CONFIG.getProperty("TB_FETCH_RATE_PAGE_SIZE") == null
            ? 100L : NumberUtils.toLong(APP_CONFIG.getProperty("TB_FETCH_RATE_PAGE_SIZE"));

    /** 使用api抓取退款单时，一页多少条抓取 */
    public static final Long TB_FETCH_REFUND_PAGE_SIZE = APP_CONFIG.getProperty("TB_FETCH_REFUND_PAGE_SIZE") == null
            ? 40L : NumberUtils.toLong(APP_CONFIG.getProperty("TB_FETCH_REFUND_PAGE_SIZE"));

    /** 淘宝：查询时间类型，1：按订单创建时间抓取，其他数字：按订单状态变更（如订单状态，运单号变更）时间抓取 */
    public static final String TB_DATE_TYPE = "0";

    /**
     * 淘宝授权参数名
     */
    public static final String RESPONSE_TYPE = "response_type";
    public static final String ERROR = "error";
    public static final String ERROR_DESCRIPTION = "error_description";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String GRANT_TYPE = "grant_type";
    public static final String CODE = "code";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String STATE = "state";
    public static final String VIEW = "view";
    public static final String AUTHORIZATION_CODE = "authorization_code";


    public static final String ACCESS_TOKEN = "access_token";
    public static final String TOKEN_TYPE = "token_type";
    public static final String EXPIRES_IN = "expires_in";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String RE_EXPIRES_IN = "re_expires_in";
    public static final String R1_EXPIRES_IN = "r1_expires_in";
    public static final String R2_EXPIRES_IN = "r2_expires_in";
    public static final String W1_EXPIRES_IN = "w1_expires_in";
    public static final String W2_EXPIRES_IN = "w2_expires_in";
    public static final String TAOBAO_USER_NICK = "taobao_user_nick";
    public static final String TAOBAO_USER_ID = "taobao_user_id";
    public static final String SUB_TAOBAO_USER_ID = "sub_taobao_user_id";
    public static final String SUB_TAOBAO_USER_NICK = "sub_taobao_user_nick";


    /**
     * 淘宝API参数名
     */
    /**
     * 类目API参数
     */
    public static final String CIDS = "cids";
    public static final String PID = "pid";
    public static final String PARENT_PID = "parent_pid";
    public static final String IS_KEY_PROP = "is_key_prop";
    public static final String IS_SALE_PROP = "is_sale_prop";
    public static final String IS_COLOR_PROP = "is_color_prop";
    public static final String IS_ENUM_PROP = "is_enum_prop";
    public static final String IS_INPUT_PROP = "is_input_prop";
    public static final String IS_ITEM_PROP = "is_item_prop";
    public static final String CHILD_PATH = "child_path";
    public static final String ATTR_KEYS = "attr_keys";
    public static final String PVS = "pvs";
    public static final String LAST_MODIFIED = "last_modified";
    public static final String ITEM_CATS = "item_cats";
    public static final String ITEM_PROPS = "item_props";
    public static final String PROP_VALUES = "prop_values";


    /**
     * 交易API参数
     */
    public static final String RATE_STATUS = "rate_status";
    public static final String TRADE_AMOUNT = "trade_amount";
    public static final String CLOSE_REASON = "close_reason";
    public static final String TRADE = "trade";
    public static final String IS_DETAIL = "is_detail";
    public static final String TRADE_CONFIRM_FEE = "trade_confirm_fee";
    public static final String FLAG = "flag";
    public static final String RESET = "reset";
    public static final String OID = "oid";
    public static final String SKU_PROPS = "sku_props";
    public static final String ORDER = "order";
    public static final String DAYS = "days";
    public static final String RECEIVER_PHONE = "receiver_phone";
    public static final String RECEIVER_MOBILE = "receiver_mobile";
    public static final String RECEIVER_STATE = "receiver_state";
    public static final String RECEIVER_CITY = "receiver_city";
    public static final String RECEIVER_DISTRICT = "receiver_district";
    public static final String RECEIVER_ADDRESS = "receiver_address";
    public static final String RECEIVER_ZIP = "receiver_zip";
    public static final String IS_ACOOKIE = "is_acookie";
    public static final String START_CREATE = "start_create";
    public static final String END_CREATE = "end_create";
    public static final String EXT_TYPE = "ext_type";
    public static final String TAG = "tag";
    public static final String USE_HAS_NEXT = "use_has_next";
    public static final String TRADES = "trades";

    /**
     * 物流API参数
     */
    public static final String VALUATION = "valuation";
    public static final String CONSIGN_AREA_ID = "consign_area_id";
    public static final String DELIVERY_TEMPLATE = "delivery_template";
    public static final String AREAS = "areas";
    public static final String TEMPLATE_IDS = "template_ids";
    public static final String USER_NICK = "user_nick";
    public static final String TEMPLATE_ID = "template_id";
    public static final String ASSUMER = "assumer";
    public static final String TEMPLATE_TYPES = "template_types";
    public static final String TEMPLATE_DESTS = "template_dests";
    public static final String TEMPLATE_START_STANDARDS = "template_start_standards";
    public static final String TEMPLATE_START_FEES = "template_start_fees";
    public static final String TEMPLATE_ADD_STANDARDS = "template_add_standards";
    public static final String TEMPLATE_ADD_FEES = "template_add_fees";
    public static final String COMPLETE = "complete";
    public static final String DELIVERY_TEMPLATES = "delivery_templates";
    public static final String CONTACT_NAME = "contact_name";
    public static final String PROVINCE = "province";
    public static final String COUNTRY = "country";
    public static final String ADDR = "addr";
    public static final String ZIP_CODE = "zip_code";
    public static final String PHONE = "phone";
    public static final String MOBILE_PHONE = "mobile_phone";
    public static final String SELLER_COMPANY = "seller_company";
    public static final String MEMO = "memo";
    public static final String GET_DEF = "get_def";
    public static final String CANCEL_DEF = "cancel_def";
    public static final String CONTACT_ID = "contact_id";
    public static final String ADDRESS_RESULT = "address_result";
    public static final String RDEF = "rdef";
    public static final String ADDRESSES = "addresses";
    public static final String IS_RECOMMENDED = "is_recommended";
    public static final String ORDER_MODE = "order_mode";
    public static final String LOGISTICS_COMPANIES = "logistics_companies";
    public static final String ORDER_SOURCE = "order_source";
    public static final String ORDER_TYPE = "order_type";
    public static final String LOGIS_TYPE = "logis_type";
    public static final String COMPANY_ID = "company_id";
    public static final String S_NAME = "s_name";
    public static final String S_AREA_ID = "s_area_id";
    public static final String S_ADDRESS = "s_address";
    public static final String S_ZIP_CODE = "s_zip_code";
    public static final String S_MOBILE_PHONE = "s_mobile_phone";
    public static final String S_TELEPHONE = "s_telephone";
    public static final String R_NAME = "r_name";
    public static final String R_AREA_ID = "r_area_id";
    public static final String R_ADDRESS = "r_address";
    public static final String R_ZIP_CODE = "r_zip_code";
    public static final String R_MOBILE_PHONE = "r_mobile_phone";
    public static final String R_TELEPHONE = "r_telephone";
    public static final String ITEM_JSON_STRING = "item_json_string";
    public static final String R_PROV_NAME = "r_prov_name";
    public static final String R_CITY_NAME = "r_city_name";
    public static final String R_DIST_NAME = "r_dist_name";
    public static final String S_PROV_NAME = "s_prov_name";
    public static final String S_CITY_NAME = "s_city_name";
    public static final String S_DIST_NAME = "s_dist_name";
    public static final String RESULT_DESC = "result_desc";
    public static final String ORDER_ID = "order_id";
    public static final String SHIPPING = "shipping";
    public static final String MODIFY_TIME = "modify_time";
    public static final String RECREATED_ORDER_ID = "recreated_order_id";
    public static final String COMPANY_CODE = "company_code";
    public static final String SENDER_ID = "sender_id";
    public static final String CANCEL_ID = "cancel_id";
    public static final String FEATURE = "feature";
    public static final String SELLER_IP = "seller_ip";
    public static final String SHIPPINGS = "shippings";
    public static final String BUYER_NICK = "buyer_nick";
    public static final String SELLER_CONFIRM = "seller_confirm";
    public static final String RECEIVER_NAME = "receiver_name";
    public static final String START_CREATED = "start_created";
    public static final String END_CREATED = "end_created";
    public static final String TRADE_ID = "trade_id";
    public static final String MAIL_NO = "mail_no";
    public static final String OCCURE_TIME = "occure_time";
    public static final String OPERATE_DETAIL = "operate_detail";
    public static final String OPERATOR_NAME = "operator_name";
    public static final String OPERATOR_CONTACT = "operator_contact";
    public static final String CURRENT_CITY = "current_city";
    public static final String NEXT_CITY = "next_city";
    public static final String FACILITY_NAME = "facility_name";
    public static final String NODE_DESCRIPTION = "node_description";
    public static final String SERVICE_TYPE = "service_type";
    public static final String SOURCE_ID = "source_id";
    public static final String TARGET_ID = "target_id";
    public static final String GOODS_VALUE = "goods_value";
    public static final String IS_NEED_CARRIAGE = "is_need_carriage";
    public static final String LOGISTICS_PARTNERS = "logistics_partners";
    public static final String SELLER_NICK = "seller_nick";
    public static final String IS_SPLIT = "is_split";
    public static final String SUB_TID = "sub_tid";
    public static final String OUT_SID = "out_sid";
    public static final String COMPANY_NAME = "company_name";
    public static final String TID = "tid";
    public static final String TRACE_LIST = "trace_list";

    /**
     * 店铺API
     */
    public static final String PARENT_CID = "parent_cid";
    public static final String SELLER_CAT = "seller_cat";
    public static final String SELLER_CATS = "seller_cats";
    public static final String PICT_URL = "pict_url";
    public static final String SORT_ORDER = "sort_order";
    public static final String SHOP = "shop";
    public static final String BULLETIN = "bulletin";

    /**
     * 主动通知业务API
     */
    public static final String TYPES = "types";
    public static final String USER_ID = "user_id";
    public static final String START = "start";
    public static final String END = "end";
    public static final String DISCARD_INFO_LIST = "discard_info_list";
    public static final String HAS_NEXT = "has_next";
    public static final String TOPICS = "topics";
    public static final String APP_CUSTOMER = "app_customer";
    public static final String IS_SUCCESS = "is_success";
    public static final String FIELDS = "fields";
    public static final String APP_CUSTOMERS = "app_customers";
    public static final String NOTIFY_ITEMS = "notify_items";
    public static final String NOTIFY_REFUNDS = "notify_refunds";
    public static final String STATUS = "status";
    public static final String PAGE_NO = "page_no";
    public static final String PAGE_SIZE = "page_size";
    public static final String NOTIFY_TRADES = "notify_trades";
    public static final String TOTAL_RESULTS = "total_results";

    /**
     * 商品API参数    85个
     */
    public static final String NUM = "num";
    public static final String PRODUCTS = "products";
    public static final String PRICE = "price";
    public static final String TYPE = "type";
    public static final String STUFF_STATUS = "stuff_status";
    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String LOCATION_CITY = "location.city";
    public static final String APPROVE_STATUS = "approve_status";
    public static final String CID = "cid";
    public static final String PROPS = "props";
    public static final String FREIGHT_PAYER = "freight_payer";
    public static final String VALID_THRU = "valid_thru";
    public static final String HAS_INVOICE = "has_invoice";
    public static final String HAS_WARRANTY = "has_warranty";
    public static final String IS_TAOBAO = "is_taobao";
    public static final String IS_EX = "is_ex";
    public static final String IS_3D = "is_3D";
    public static final String WEIGHT = "weight";
    public static final String IS_XINPIN = "is_xinpin";
    public static final String SUB_STOCK = "sub_stock";
    public static final String SCENIC_TICKET_PAY_WAY = "scenic_ticket_pay_way";
    public static final String SCENIC_TICKET_BOOK_COST = "scenic_ticket_book_cost";
    public static final String ITEM_SIZE = "item_size";
    public static final String ITEM_WEIGHT = "item_weight";
    public static final String CHANGE_PROP = "change_prop";
    public static final String SELL_POINT = "sell_point";
    public static final String DESC_MODULES = "desc_modules";
    public static final String OCALITY_LIFE_NETWORK_ID = "ocality_life.network_id";
    public static final String GLOBAL_STOCK_TYPE = "global_stock_type";
    public static final String GLOBAL_STOCK_COUNTRY = "global_stock_country";
    public static final String CAT_ID = "cat_id";
    public static final String ANCHOR_MODULES = "anchor_modules";
    public static final String TRACK_IID = "track_iid";
    public static final String LOCALITY_LIFE_ONSALE_AUTO_REFUND_RATI = "locality_life.onsale_auto_refund_rati";
    public static final String ITEM_SPEC_PROP = "item_spec_prop";
    public static final String SUB_PIC_URLS = "sub_pic_urls";
    public static final String SKU_SPEC_IDS = "sku_spec_ids";
    public static final String HAS_SHOWCASE = "has_showcase";
    public static final String SELLER_CIDS = "seller_cids";
    public static final String HAS_DISCOUNT = "has_discount";
    public static final String CATEGORY_ID = "category_id";
    public static final String PIC_URL = "pic_url";
    public static final String SUB_PIC_PATHS = "sub_pic_paths";
    public static final String COVER = "cover";
    public static final String AUTHOR = "author";
    public static final String COPYRIGHT_FILES = "copyright_files";
    public static final String COPYRIGHT_END = "copyright_end";
    public static final String SELL_WAY = "sell_way";
    public static final String RELATION_LINK = "relation_link";
    public static final String ID = "id";
    public static final String SKU_ID = "sku_id";
    public static final String ITEM_NUM = "item_num";
    public static final String ITEM_PRICE = "item_price";
    public static final String NUM_IIDS = "num_iids";
    public static final String ITEM_TEMPLATE_LIST = "item_template_list";
    public static final String LOCATION_STATE = "location.state";
    public static final String POST_FEE = "post_fee";
    public static final String EXPRESS_FEE = "express_fee";
    public static final String EMS_FEE = "ems_fee";
    public static final String INCREMENT = "increment";
    public static final String INPUT_PIDS = "input_pids";
    public static final String SKU_QUANTITIES = "sku_quantities";
    public static final String SKU_PRICES = "sku_prices";
    public static final String SKU_PROPERTIES = "sku_properties";
    public static final String PIC_PATH = "pic_path";
    public static final String AUTO_FILL = "auto_fill";
    public static final String SKU_OUTER_IDS = "sku_outer_ids";
    public static final String IS_REPLACE_SKU = "is_replace_sku";
    public static final String INPUT_STR = "input_str";
    public static final String LANG = "lang";
    public static final String AFTER_SALE_ID = "after_sale_id";
    public static final String SELL_PROMISE = "sell_promise";
    public static final String COD_POSTAGE_ID = "cod_postage_id";
    public static final String IS_LIGHTNING_CONSIGNMENT = "is_lightning_consignment";
    public static final String EMPTY_FIELDS = "empty_fields";
    public static final String FOOD_SECURITY_PRD_LICENSE_NO = "food_security.prd_license_no";
    public static final String BANNER = "banner";
    public static final String REGULAR_SHELVED = "regular_shelved";
    public static final String NEVER_ON_SHELF = "never_on_shelf";
    public static final String OFF_SHELF = "off_shelf";
    public static final String FOR_SHELVED = "for_shelved";
    public static final String SOLD_OUT = "sold_out";
    public static final String VIOLATION_OFF_SHELF = "violation_off_shelf";
    public static final String ITEMS = "items";
    public static final String TRACK_IIDS = "track_iids";
    public static final String ORDER_BY = "order_by";
    public static final String START_MODIFIED = "start_modified";
    public static final String END_MODIFIED = "end_modified";
    public static final String PROPERTY_ALIAS = "property_alias";
    public static final String POSITION = "position";
    public static final String IS_MAJOR = "is_major";
    public static final String OUTER_ID = "outer_id";
    public static final String BINDS = "binds";
    public static final String SALE_PROPS = "sale_props";
    public static final String NAME = "name";
    public static final String MAJOR = "major";
    public static final String NATIVE_UNKEYPROPS = "native_unkeyprops";
    public static final String PACKING_LIST = "packing_list";
    public static final String EXTRA_INFO = "extra_info";
    public static final String SELL_PT = "sell_pt";
    public static final String NICK = "nick";
    public static final String Q = "q";
    public static final String VERTICAL_MARKET = "vertical_market";
    public static final String CUSTOMER_PROPS = "customer_props";
    public static final String MARKET_ID = "market_id";
    public static final String NUM_IID = "num_iid";
    public static final String SKUID_QUANTITIES = "skuid_quantities";
    public static final String OUTERID_QUANTITIES = "outerid_quantities";
    public static final String FOOD_SECURITY_DESIGN_CODE = "food_security.design_code";
    public static final String FOOD_SECURITY_FACTORY = "food_security.factory";
    public static final String FOOD_SECURITY_FACTORY_SITE = "food_security.factory_site";
    public static final String FOOD_SECURITY_CONTACT = "food_security.contact";
    public static final String FOOD_SECURITY_MIX = "food_security.mix";
    public static final String FOOD_SECURITY_PLAN_STORAGE = "food_security.plan_storage";
    public static final String FOOD_SECURITY_PERIOD = "food_security.period";
    public static final String FOOD_SECURITY_FOOD_ADDITIVE = "food_security.food_additive";
    public static final String FOOD_SECURITY_SUPPLIER = "food_security.supplier";
    public static final String FOOD_SECURITY_PRODUCT_DATE_START = "food_security.product_date_start";
    public static final String FOOD_SECURITY_PRODUCT_DATE_END = "food_security.product_date_end";
    public static final String FOOD_SECURITY_STOCK_DATE_START = "food_security.stock_date_start";
    public static final String FOOD_SECURITY_STOCK_DATE_END = "food_security.stock_date_end";
    public static final String FOOD_SECURITY_HEALTH_PRODUCT_NO = "food_security.health_product_no";
    public static final String LOCALITY_LIFE_EXPIRYDATE = "locality_life.expirydate";
    public static final String LOCALITY_LIFE_NETWORK_ID = "locality_life.network_id";
    public static final String LOCALITY_LIFE_MERCHANT = "locality_life.merchant";
    public static final String LOCALITY_LIFE_VERIFICATION = "locality_life.verification";
    public static final String LOCALITY_LIFE_REFUND_RATIO = "locality_life.refund_ratio";
    public static final String LOCALITY_LIFE_CHOOSE_LOGIS = "locality_life.choose_logis";
    public static final String LOCALITY_LIFE_ONSALE_AUTO_REFUND_RATIO = "locality_life.onsale_auto_refund_ratio";
    public static final String LOCALITY_LIFE_REFUNDMAFEE = "locality_life.refundmafee";
    public static final String PAIMAI_INFO_MODE = "paimai_info.mode";
    public static final String PAIMAI_INFO_DEPOSIT = "paimai_info.deposit";
    public static final String PAIMAI_INFO_INTERVAL = "paimai_info.interval";
    public static final String PAIMAI_INFO_RESERVE = "paimai_info.reserve";
    public static final String PAIMAI_INFO_VALID_HOUR = "paimai_info.valid_hour";
    public static final String PAIMAI_INFO_VALID_MINUTE = "paimai_info.valid_minute";
    public static final String ETC_NETWORK_ID = "etc.network_id";
    public static final String ETC_MERCHANT_ID = "etc.merchant_id";
    public static final String ETC_MERCHANT_NICK = "etc.merchant_nick";
    public static final String ETC_VERIFICATION_PAY = "etc.verification_pay";
    public static final String ETC_OVERDUE_PAY = "etc.overdue_pay";
    public static final String ETC_AUTO_REFUND = "etc.auto_refund";
    public static final String ETC_ASSOCIATION_STATUS = "etc.association_status";
    public static final String ITEM_IDS = "item_ids";
    public static final String PROMOTIONS = "promotions";
    public static final String BRAND_CAT_CONTROL_INFO = "brand_cat_control_info";
    public static final String USR_ID = "usr_id";
    public static final String ITEM_ID = "item_id";
    public static final String DESCRIPTION = "description";
    public static final String PROV = "prov";
    public static final String CITY = "city";
    public static final String AUCTION_STATUS = "auction_status";
    public static final String IMAGE_1 = "image_1";
    public static final String IMAGE_2 = "image_2";
    public static final String IMAGE_3 = "image_3";
    public static final String IMAGE_4 = "image_4";
    public static final String IMAGE_5 = "image_5";
    public static final String SUB_STOCK_AT_BUY = "sub_stock_at_buy";
    public static final String SHOP_CATS = "shop_cats";
    public static final String VIDEO_ID = "video_id";
    public static final String AUCTION_POINT = "auction_point";
    public static final String LIST_TIME = "list_time";
    public static final String VIP_PROMOTED = "vip_promoted";
    public static final String PROMOTED_STATUS = "promoted_status";
    public static final String SKUS = "skus";
    public static final String POSTAGE_ID = "postage_id";
    public static final String REMOVE_FIELDS = "remove_fields";
    public static final String HAVE_INVOICE = "have_invoice";
    public static final String TICKET_ITEM_PROCESS_RESULT = "ticket_item_process_result";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String BRAND_ID = "brand_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String CERTIFIED_PIC_STR = "certified_pic_str";
    public static final String LABEL_PRICE = "label_price";
    public static final String CERTIFIED_TXT_STR = "certified_txt_str";
    public static final String SPEC_PROPS = "spec_props";
    public static final String SPEC_PROPS_ALIAS = "spec_props_alias";
    public static final String CUSTOMER_SPEC_PROPS = "customer_spec_props";
    public static final String IMAGE = "image";
    public static final String BARCODE = "barcode";
    public static final String PRODUCT_CODE = "product_code";
    public static final String MARKET_TIME = "market_time";
    public static final String PRODUCT_SPEC = "product_spec";
    public static final String SPEC_ID = "spec_id";
    public static final String CERTIFY_TYPE = "certify_type";
    public static final String CERTIFY_PIC = "certify_pic";
    public static final String PROPERTIES = "properties";
    public static final String PRODUCT_SPECS = "product_specs";
    public static final String SPEC_IDS = "spec_ids";
    public static final String TICKETS = "tickets";
    public static final String SECONDKILL = "SecondKill";

    /**
     * 消息服务api参数
     */
    public static final String NICKS = "nicks";
    public static final String CREATED = "created";
    public static final String TOPIC = "topic";
    public static final String CONTENT = "content";
    public static final String S_MESSAGE_IDS = "s_message_ids";
    public static final String F_MESSAGE_IDS = "f_message_ids";
    public static final String GROUP_NAME = "group_name";
    public static final String QUANTITY = "quantity";
    public static final String MESSAGES = "messages";
    public static final String TMC_USER = "tmc_user";


    public static final String SID = "sid";
    public static final String MODIFIED = "modified";
    public static final String SHOP_SCORE = "shop_score";
    public static final String DISCOUNT_FEE = "discount_fee";
    public static final String POINT_FEE = "point_fee";
    public static final String TOTAL_FEE = "total_fee";
    public static final String PAY_TIME = "pay_time";
    public static final String ALIPAY_ID = "alipay_id";
    public static final String ALIPAY_NO = "alipay_no";
    public static final String BUYER_AREA = "buyer_area";
    public static final String TRADE_FROM = "trade_from";
    public static final String PAYMENT = "payment";
    public static final String RECEIVED_PAYMENT = "received_payment";
    public static final String SELLER_FLAG = "seller_flag";
    public static final String HAS_BUYER_MESSAGE = "has_buyer_message";
    public static final String CREDIT_CARD_FEE = "credit_card_fee";
    public static final String MARK_DESC = "mark_desc";
    public static final String SHIPPING_TYPE = "shipping_type";
    public static final String ADJUST_FEE = "adjust_fee";
    public static final String BUYER_OBTAIN_POINT_FEE = "buyer_obtain_point_fee";
    public static final String COMMISSION_FEE = "commission_fee";
    public static final String BUYER_RATE = "buyer_rate";
    public static final String IS_PART_CONSIGN = "is_part_consign";
    public static final String IS_DAIXIAO = "is_daixiao";
    public static final String REAL_POINT_FEE = "real_point_fee";
    public static final String ORDERS = "orders";
    public static final String SELLER_RATE = "seller_rate";
    public static final String CONSIGN_TIME = "consign_time";
    public static final String HAS_POST_FEE = "has_post_fee";


    public static final String ORDERS_OID = "orders.oid";
    public static final String ORDERS_STATUS = "orders.status";
    public static final String ORDERS_CID = "orders.cid";
    public static final String ORDERS_IID = "orders.iid";
    public static final String ORDERS_TITLE = "orders.title";
    public static final String ORDERS_PRICE = "orders.price";
    public static final String ORDERS_NUM_IID = "orders.num_iid";
    public static final String ORDERS_ITEM_MEAL_ID = "orders.item_meal_id";
    public static final String ORDERS_SKU_ID = "orders.sku_id";
    public static final String ORDERS_NUM = "orders.num";
    public static final String ORDERS_OUTER_SKU_ID = "orders.outer_sku_id";
    public static final String ORDERS_ORDER_FROM = "orders.order_from";
    public static final String ORDERS_TOTAL_FEE= "orders.total_fee";
    public static final String ORDERS_PAYMENT = "orders.payment";
    public static final String ORDERS_DISCOUNT_FEE = "orders.discount_fee";
    public static final String ORDERS_ADJUST_FEE = "orders.adjust_fee";
    public static final String ORDERS_MODIFIED = "orders.modified";
    public static final String ORDERS_SKU_PROPERTIES_NAME = "orders.sku_properties_name";
    public static final String ORDERS_REFUND_ID = "orders.refund_id";
    public static final String ORDERS_IS_OVERSOLD = "orders.is_oversold";
    public static final String ORDERS_IS_SERVICE_ORDER = "orders.is_service_order";
    public static final String ORDERS_END_TIME = "orders.end_time";
    public static final String ORDERS_CONSIGN_TIME = "orders.consign_time";
    public static final String ORDERS_SHIPPING_TYPE = "orders.shipping_type";
    public static final String ORDERS_BIND_OID = "orders.bind_oid";
    public static final String ORDERS_LOGISTICS_COMPANY = "orders.logistics_company";
    public static final String ORDERS_INVOICE_NO = "orders.invoice_no";
    public static final String ORDERS_IS_DAIXIAO = "orders.is_daixiao";
    public static final String ORDERS_DIVIDE_ORDER_FEE = "orders.divide_order_fee";
    public static final String ORDERS_PART_MJZ_DISCOUNT = "orders.part_mjz_discount";
    public static final String ORDERS_ITEM_MEAL_NAME = "orders.item_meal_name";
    public static final String ORDERS_PIC_PATH = "orders.pic_path";
    public static final String ORDERS_SELLER_NICK = "orders.seller_nick";
    public static final String ORDERS_BUYER_NICK = "orders.buyer_nick";
    public static final String ORDERS_REFUND_STATUS= "orders.refund_status";
    public static final String ORDERS_OUTER_IID= "orders.outer_iid";
    public static final String ORDERS_SNAPSHOT_URL = "orders.snapshot_url";
    public static final String ORDERS_SNAPSHOT = "orders.snapshot";
    public static final String ORDERS_TIMEOUT_ACTION_TIME = "orders.timeout_action_time";
    public static final String ORDERS_BUYER_RATE = "orders.buyer_rate";
    public static final String ORDERS_SELLER_RATE = "orders.seller_rate";
    public static final String ORDERS_SELLER_TYPE = "orders.seller_type";

    /**
     * 评价Api参数
     */
    public static final String RATE_TYPE="rate_type";
    public static final String ROLE="role";
    public static final String RESULT="result";
    public static final String START_DATE="start_date";
    public static final String END_DATE="end_date";
    public static final String CHILDTRADEID="child_trade_id";


}



