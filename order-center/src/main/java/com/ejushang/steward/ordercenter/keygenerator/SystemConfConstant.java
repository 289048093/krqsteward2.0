package com.ejushang.steward.ordercenter.keygenerator;

/**
 * User:Amos.zhou
 * Date: 13-12-25
 * Time: 上午11:01
 */
public final class SystemConfConstant {

    /**随机数串种子*/
    public static final String  ALL_NUMBER  =   "0123456789";

    /**每轮最小的订单号*/
    public static final Integer MIN_ORDER_NO    = 111111;

    /**每轮最大的订单号，当达到最大值以后就回到MIN_ORDER_NO*/
    public static final Integer MAX_ORDER_NO = 999999;

    /**系统配置表中存下一个订单号key*/
    public static final String  NEXT_ORDER_NO   =   "next_order_no";

    public static final String  NEXT_ORDER_ITEM_NO   =   "next_order_item_no";

    /**系统配置表中存下一个套餐编号key*/
    public static final String  NEXT_MEAL_SET_NO = "next_meal_set_no";

    /**系统配置表中存下一个商家编号key*/
    public static final String  NEXT_SUPPLIER_NO = "next_supplier_no";

    /**系统配置表中存下一个回访编号key*/
    public static final String NEXT_RETURN_VISIT_NO="next_return_visit_no";


    /** 邮费补差商品的SKU **/
    public static final String  POSTAGE_PRODUCT_SKU = "postage_product_sku";
    /** 服务补差商品的SKU **/
    public static final String  SERVICE_PRODUCT_SKU = "service_product_sku";

    /** 订单抓取和分析的最短时间间隔 **/
    public static final String  MESSAGE_GRAB_INTERVAL = "message_grab_interval";
    public static final String  MESSAGE_ANALYZE_INTERVAL = "message_analyze_interval";
    

    // ======================= 预警相关的参数设置 key =======================

    /** 订单未处理发货的触发警告时间 key, 若不设置 value 则默认值是 16:00(下午 4 点) */
    public static final String NO_SEND_GOODS_TRIGGER_WARN_TIME = "no_send_goods_trigger_warn_time";

    /** 最新的物流状态距离现在的时间间隔 key, 若不设置 value 则默认是 24(小时) */
    public static final String LATEST_TIME_DISTANCE_HOUR = "latest_time_distance_hour";

    /** 物流地址中给最近区域预警签收的 key, 若不设置 value 则默认是 广东(多个以 / 隔开) */
    public static final String GD_ADDRESS = "gd_address";
    /** 物流地址中给最近区域预警签收的 时间 预报 key, 若不设置 value 则默认是 48(小时) */
    public static final String GD_DISTANCE_HOUR = "gd_distance_hour";

    /** 物流地址中给江浙沪预警签收的 key, 若不设置 value 则默认是 江苏/浙江/上海(以 / 隔开) */
    public static final String JZH_ADDRESS = "jzh_address";
    /** 物流地址中给江浙沪预警签收的 时间 预报 key, 若不设置 value 则默认是 72(小时) */
    public static final String JZH_DISTANCE_HOUR = "jzh_distance_hour";

    /** 物流地址中给稍慢区域预警签收的 key, 若不设置 value 则默认是 广西/福建/海南/江西/安徽/湖北/湖南/河南/北京/天津/河北/山东/山西/重庆(多个以 / 隔开) */
    public static final String SLOW_ADDRESS = "slow_address";
    /** 物流地址中给稍慢区域预警签收的 时间 预报 key, 若不设置 value 则默认是 96(小时) */
    public static final String SLOW_DISTANCE_HOUR = "slow_distance_hour";

    /** 物流地址中给偏远区域预警签收的 key, 若不设置 value 则默认是 内蒙古/辽宁/吉林/黑龙江/云南/贵州/四川/陕西/甘肃/青海/宁夏/西藏(多个以 / 隔开) */
    public static final String PY_ADDRESS = "py_address";
    /** 物流地址中给偏远区域预警签收的 时间 预报 key, 若不设置 value 则默认是 144(小时) */
    public static final String PY_DISTANCE_HOUR = "py_distance_hour";

}
