package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.ordercenter.constant.*;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Shiro
 * Date: 14-5-10
 * Time: 上午11:19
 */
public class RefundUtil {
    /**
     * 拼接订单HQL
     *
     * @param map           controller接收的map参数
     * @param stringBuilder 拼接HQL变量
     * @param objects       添加的变量参数List集合
     * @throws java.text.ParseException
     */
    public static void refundCondition(Map<String, String> map, StringBuilder stringBuilder, List<Object> objects) throws ParseException {
        stringBuilder.append("select r from Refund r left outer join r.shop s left outer join r.originalRefund o where 1=1  ");
        //右边条件1判断
        if (!StringUtils.isBlank(map.get("conditionValue1"))) {
            stringBuilder.append(RefundUtil.rightCondition(map.get("conditionType1"), map.get("conditionQuery1")));
            if (map.get("conditionType1").equals("has") || map.get("conditionType1").equals("!")) {
                objects.add("%" + map.get("conditionValue1") + "%");
            } else {
                objects.add(map.get("conditionValue1"));
            }
        }
        //右边条件2判断
        if (!StringUtils.isBlank(map.get("conditionValue2"))) {
            stringBuilder.append(RefundUtil.rightCondition(map.get("conditionType2"), map.get("conditionQuery2")));
            if (map.get("conditionType2").equals("has") || map.get("conditionType2").equals("!")) {
                objects.add("%" + map.get("conditionValue2") + "%");
            } else {
                objects.add(map.get("conditionValue2"));
            }
        }
        //退款状态判断
        if (!StringUtils.isBlank(map.get("status")) && !map.get("status").equals("null")) {
            stringBuilder.append(" and ").append(" r.status= ").append(" ? ");
            objects.add(RefundStatus.valueOf(map.get("status")));
        }
        if (!StringUtils.isBlank(map.get("shippingNo")) && !map.get("shippingNo").equals("null")) {
            stringBuilder.append(" and ").append(" r.shippingNo= ").append(" ? ");
            objects.add(RefundStatus.valueOf(map.get("shippingNo")));
        }
        //是否同时退货
        if (!StringUtils.isBlank(map.get("alsoReturn")) && !map.get("alsoReturn").equals("null")) {
            stringBuilder.append(" and ").append(" r.alsoReturn= ").append(" ? ");
            objects.add(Boolean.valueOf(map.get("alsoReturn")));
        }
        //日期类型及时间
        if (!StringUtils.isBlank(map.get("dateType")) && !map.get("dateType").equals("all")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (!StringUtils.isBlank(map.get("startDate"))) {
                objects.add(simpleDateFormat.parse(map.get("startDate")));
            }
            if (!StringUtils.isBlank(map.get("endDate"))) {
                objects.add(simpleDateFormat.parse(map.get("endDate")));
            }
            stringBuilder.append(RefundUtil.dateConditionUtil(map.get("dateType"), map.get("startDate"), map.get("endDate")));
        }
        //店铺
        if (!StringUtils.isBlank(map.get("shopId")) && !map.get("shopId").equals("null")) {
            stringBuilder.append(" and ").append(" s.id=").append(" ? ");
            objects.add(Integer.valueOf(map.get("shopId")));
        }
        //退款类型
        if (!StringUtils.isBlank(map.get("type")) && !map.get("type").equals("null")) {
            stringBuilder.append(" and ").append(" r.type=").append(" ? ");
            objects.add(RefundType.valueOf(map.get("type")));
        }
        //平台类型
        if (!StringUtils.isBlank(map.get("platformType")) && !map.get("platformType").equals("null")) {
            stringBuilder.append(" and ").append(" r.platformType= ").append(" ? ");
            objects.add(PlatformType.valueOf(map.get("platformType")));
        }
        //是否线上退款
        if (!StringUtils.isBlank(map.get("online")) && !map.get("online").equals("null")) {
            stringBuilder.append(" and ").append(" r.online= ").append(" ? ");
            objects.add(Boolean.valueOf(map.get("online")));
        }
        //退款阶段
        if (!StringUtils.isBlank(map.get("phase")) && !map.get("phase").equals("null")) {
            stringBuilder.append(" and ").append(" r.phase= ").append(" ? ");
            objects.add(RefundPhase.valueOf(map.get("phase")));
        }
        //退货邮费承担方
        if (!StringUtils.isBlank(map.get("postPayer")) && !map.get("postPayer").equals("null")) {
            stringBuilder.append(" and ").append(" r.postPayer= ").append(" ? ");
            objects.add(PostPayer.valueOf(map.get("postPayer")));
        }
        //品牌
        if (!StringUtils.isBlank(map.get("brandId")) && !map.get("brandId").equals("null")) {
            stringBuilder.append(" and ").append(" r.orderItem.product.brand.id= ").append(" ? ");
            objects.add(Integer.valueOf(map.get("brandId")));
        }
        stringBuilder.append(" order by ");
        stringBuilder.append(" r.createTime desc");

    }

    /**
     * 拼接右边条件字段的where
     *
     * @param conQuery 字段参数
     * @return 返回拼接字段
     */
    public static String conditionQuery(String conQuery) {
        //支付宝账号
        String result = null;
        if (conQuery.equals("platformOrderNo")) {
            result = "r.orderItem.platformOrderNo";
        }
        if (conQuery.equals("orderNo")) {
            result = "r.orderItem.order.orderNo";
        }
        if (conQuery.equals("shippingNo")) {
            result = "r.shippingNo";
        }
        if (conQuery.equals("platformRefundNo")) {
            result = "r.platformRefundNo";
        }
        if (conQuery.equals("buyerAlipayNo")) {
            result = "r.buyerAlipayNo";
        }
        if (conQuery.equals("productNo")) {
            result = "r.orderItem.product.productNo";
        }
        if (conQuery.equals("productName")) {
            result = "r.orderItem.product.name";
        }
        if (conQuery.equals("sku")) {
            result = "r.orderItem.product.sku";
        }
        if (conQuery.equals("buyerId")) {
            result = "r.buyerId";
        }
        return result;
    }

    /**
     * 拼接右边条件
     *
     * @param conType 符号参数
     * @param query   条件字段参数
     * @return 返回拼接条件
     */
    public static String rightCondition(String conType, String query) {
        StringBuilder stringBuilder = new StringBuilder();
        String newQuery = conditionQuery(query);
        if (conType.equals("=")) {
            stringBuilder.append(" and ").append(newQuery).append(" = ").append(" ? ");
        }
        if (conType.equals("!=")) {
            stringBuilder.append(" and ").append(newQuery).append(" <> ").append(" ? ");
        }
        if (conType.equals("<=")) {
            stringBuilder.append(" and ").append(newQuery).append(" <= ").append(" ? ");
        }
        if (conType.equals(">")) {
            stringBuilder.append(" and ").append(newQuery).append(" > ").append(" ? ");
        }
        if (conType.equals(">=")) {
            stringBuilder.append(" and ").append(newQuery).append(" >= ").append(" ? ");
        }
        if (conType.equals("<")) {
            stringBuilder.append(" and ").append(newQuery).append(" < ").append(" ? ");
        }
        if (conType.equals("!")) {
            stringBuilder.append(" and ").append(newQuery).append(" not like ").append(" ? ");
        }
        if (conType.equals("has")) {
            stringBuilder.append(" and ").append(newQuery).append(" like ").append(" ? ");
        }
        return stringBuilder.toString();
    }

    /**
     * 拼接日期
     *
     * @param dateType  日期类型
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 返回拼接日期条件
     */
    public static String dateConditionUtil(String dateType, String startDate, String endDate) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!StringUtils.isBlank(startDate)) {
            stringBuilder.append(" and ").append(dateConditionQueryUtil(dateType)).append(" >= ").append(" ? ").append(" ");
        }
        if (!StringUtils.isBlank(endDate)) {
            stringBuilder.append(" and ").append(dateConditionQueryUtil(dateType)).append("  <= ").append(" ? ").append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * 拼接日期类型的字段
     *
     * @param dateType 日期类型
     * @return 返回日期类型字段
     */
    public static String dateConditionQueryUtil(String dateType) {
        String column = null;
        if (dateType.trim().equalsIgnoreCase("refundDate")) {
            column = "r.refundTime";
        }
        if (dateType.trim().equals("createDate")) {
            column = "r.createTime";
        }
        return column;
    }

    /**
     * 获取Refund的参数的Map对象
     *
     * @param map  传过来的参数
     * @param map1 解析后的参数
     */
    public static void getConditionMap(Map<String, Object[]> map, Map<String, String> map1) {
        for (Map.Entry<String, Object[]> entry : map.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue()[0];
            if (!key.equals("sort")) {
                map1.put(key, val + "");
//            } else {
//                String data = val.toString();
//                String sort = data.substring(1, data.length() - 1);
//                HashMap hashMap = (HashMap) JsonUtil.json2Object(sort, Map.class);
//                map1.put("type", hashMap.get("property") + "");
//                map1.put("sort", hashMap.get("direction") + "");
//            }
        }
    }
    }
}
