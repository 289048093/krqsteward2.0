package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.EmployeeUtil;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.vo.DealOriginalOrderItemVo;
import com.ejushang.steward.ordercenter.vo.DealOriginalOrderVo;
import com.ejushang.steward.ordercenter.vo.OrderItemVo;
import com.ejushang.steward.ordercenter.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Transient;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: JBoss
 * Date: 14-7-8
 * Time: 上午9:43
 */
public class DealOriginalOrderUtil {
    /**
     * 拼接订单HQL
     *
     * @param map           controller接收的map参数
     * @param stringBuilder 拼接HQL变量
     * @param objects       添加的变量参数List集合
     * @throws java.text.ParseException
     */
    public static void orderCondition(Map<String, String> map, StringBuilder stringBuilder, List<Object> objects) throws ParseException {
        //获取拼接的HQL语句
        sortOrderCondition(map, stringBuilder, objects);
        //去除重复列
        stringBuilder.append(" group by o.id order by ");
        //按照前端参数排序
        if (map.get("sortParam") != null) {
            String[] orderByType = map.get("sortParam").split("#");
            stringBuilder.append(" o.").append(orderByType[0]);
            stringBuilder.append(" ").append(orderByType[1].equalsIgnoreCase("asc") ? "desc" : "asc");
        } else {
            stringBuilder.append(" o.buyTime ");
            stringBuilder.append(" desc ");
        }
    }

    /**
     * 选择性的左外连接订单项
     *
     * @param orderConditions Hql的where部分
     * @param targetHql       完整的HQL
     */
    private static void choiceLeftJoin(StringBuilder orderConditions, StringBuilder targetHql) {
        targetHql.append("select o from OriginalOrder o ");
        if (orderConditions.toString().contains("i.")) {
            targetHql.append(" left outer  join  o.originalOrderItemList i ");
        }
        if (orderConditions.toString().contains("b.")) {
            targetHql.append(" left  outer join o.originalOrderBrands b ");
        }
        targetHql.append(" where 1=1 ").append(orderConditions.toString());
    }


    private static void sortOrderCondition(Map<String, String> map, StringBuilder targetHql, List<Object> objects) throws ParseException {
        StringBuilder orderConditions = new StringBuilder();
        //右边条件1判断
        if (!StringUtils.isBlank(map.get("conditionValue")) && !map.get("conditionQuery").equalsIgnoreCase("null")) {
            orderConditions.append(rightCondition(map.get("conditionType"), map.get("conditionQuery")));
            if (map.get("conditionType").equals("has") || map.get("conditionType").equals("!")) {
                objects.add("%" + StringUtils.trimToNull(map.get("conditionValue")) + "%");
            } else {
                //类型的校验和转化方法
                checkDataType(objects, map.get("conditionQuery"), StringUtils.trimToNull(map.get("conditionValue")));
            }
        }


        //解析状态
        if (!StringUtils.isBlank(map.get("processed")) && !map.get("processed").equals("null")) {
            orderConditions.append(" and ").append(" o.processed=").append(" ? ");
            objects.add(Boolean.valueOf(map.get("processed")));
        }
        //是否有效
        if (!StringUtils.isBlank(map.get("discard")) && !map.get("discard").equals("null")) {
            orderConditions.append(" and ").append(" o.discard=").append(" ? ");
            objects.add(Boolean.valueOf(map.get("discard")));
        }
        //订单日期类型及时间
        if (!StringUtils.isBlank(map.get("dateType")) && !map.get("dateType").equals("all")) {

            if (!StringUtils.isBlank(map.get("startDate"))) {
                objects.add(EJSDateUtils.parseDateForNull(StringUtils.trimToNull(map.get("startDate")), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            }
            if (!StringUtils.isBlank(map.get("endDate"))) {
                objects.add(EJSDateUtils.parseDateForNull(StringUtils.trimToNull(map.get("endDate")), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            }
            orderConditions.append(dateConditionUtil(map.get("dateType"), map.get("startDate"), map.get("endDate")));
        }
        //店铺
        if (!StringUtils.isBlank(map.get("shopId")) && !map.get("shopId").equals("null")) {
            orderConditions.append(" and ").append(" o.shopId=").append(" ? ");
            objects.add(Integer.parseInt(StringUtils.trimToNull(map.get("shopId"))));
        }
        //品牌
        if (!StringUtils.isBlank(map.get("brandId")) && !map.get("brandId").equals("null")) {
            orderConditions.append(" and ").append(" b.brandId=").append(" ? ");
            objects.add(Integer.parseInt(StringUtils.trimToNull(map.get("brandId"))));
        }
        //平台
        if (!StringUtils.isBlank(map.get("outPlatformType")) && !map.get("outPlatformType").equals("null")) {
            orderConditions.append(" and ").append(" o.platformType=").append(" ? ");
            objects.add(PlatformType.valueOf(StringUtils.trimToNull(map.get("outPlatformType"))));
        }
        //选择性判断是否左外连接订单项
        choiceLeftJoin(orderConditions, targetHql);
    }

    /**
     * 判断是否为浮点型，因为java少了对浮点型的校验
     *
     * @param number 字符串参数
     * @return 返回布尔类型
     */
    private static boolean isFloatPointNumber(String number) {
        number = number.trim();
        String pointPrefix = "(\\-|\\+){0,1}\\d*\\.\\d+";//浮点数的正则表达式-小数点在中间与前面
        String pointSuffix = "(\\-|\\+){0,1}\\d+\\.";//浮点数的正则表达式-小数点在后面
        if (number.matches(pointPrefix) || number.matches(pointSuffix))
            return true;
        else
            return false;
    }


    /**
     * 拼接右边条件字段的where
     *
     * @param conQuery 字段参数
     * @return 返回拼接字段
     */
    private static String conditionQuery(String conQuery) {
        String result = null;
        if (conQuery.equalsIgnoreCase("platformOrderNo")) {
            result = "o.platformOrderNo";
        }

        if (StringUtils.trimToNull(conQuery).equalsIgnoreCase("sku")) {
            result = "i.sku";
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
    private static String rightCondition(String conType, String query) {
        StringBuilder stringBuilder = new StringBuilder();
        String newQuery = conditionQuery(query);
        if (conType.equalsIgnoreCase("=")) {
            stringBuilder.append(" and ").append(newQuery).append(" = ").append(" ? ");
        }
        if (conType.equalsIgnoreCase("!=")) {
            stringBuilder.append(" and ").append(newQuery).append(" <> ").append(" ? ");
        }
        if (conType.equalsIgnoreCase("<=")) {
            stringBuilder.append(" and ").append(newQuery).append(" <= ").append(" ? ");
        }
        if (conType.equalsIgnoreCase(">")) {
            stringBuilder.append(" and ").append(newQuery).append(" > ").append(" ? ");
        }
        if (conType.equalsIgnoreCase(">=")) {
            stringBuilder.append(" and ").append(newQuery).append(" >= ").append(" ? ");
        }
        if (conType.equalsIgnoreCase("<")) {
            stringBuilder.append(" and ").append(newQuery).append(" < ").append(" ? ");
        }
        if (conType.equalsIgnoreCase("!")) {
            stringBuilder.append(" and ").append(newQuery).append(" not like ").append(" ? ");
        }
        if (conType.equalsIgnoreCase("has")) {
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
    private static String dateConditionUtil(String dateType, String startDate, String endDate) {
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
    private static String dateConditionQueryUtil(String dateType) {
        String column = null;
        if (dateType.trim().equalsIgnoreCase("payDate")) {
            column = "o.payTime";
        }
        if (dateType.trim().equalsIgnoreCase("orderDate")) {
            column = "o.buyTime";
        }
        return column;
    }

//======================================================================================================================================

    /**
     * 获取操作人
     *
     * @param operatorId 操作人Id
     * @return 返回实体
     */
    @Transient
    public static String getOperatorName(Integer operatorId) {
        return EmployeeUtil.getOperatorName(operatorId);
    }

    /**
     * 获取Order的参数的Map对象
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
            } else {
                String data = val.toString();
                String sort = data.substring(1, data.length() - 1);
                HashMap hashMap = (HashMap) JsonUtil.json2Object(sort, Map.class);
                map1.put("orderByType", hashMap.get("property").toString());
                map1.put("sort", hashMap.get("direction").toString());
            }
        }
    }

    /**
     * 校验map参数
     * @param objects ？的值
     */
    private static void checkDataType(List<Object> objects, String Query, String value) {
        if (Query.equalsIgnoreCase("actualFee")) {
            if (StringUtils.isNumeric(StringUtils.trimToNull(value)) || isFloatPointNumber(StringUtils.trimToNull(value))) {
                objects.add(Money.valueOf(value));
            } else {
                throw new StewardBusinessException("成交金额只能输入数字");
            }
        } else {
            objects.add(value);
        }
    }

    /**
     * 拼接OrderItemVo
     * @param dealOriginalOrderItemVo 订单项VO实体
     * @param originalOrderItem   订单项VO实体
     */

    public static DealOriginalOrderItemVo getOrderItemVo(DealOriginalOrderItemVo dealOriginalOrderItemVo, OriginalOrderItem originalOrderItem) {
        dealOriginalOrderItemVo.setActualFee(originalOrderItem.getActualFee().toString());
        dealOriginalOrderItemVo.setBuyCount(Integer.valueOf(originalOrderItem.getBuyCount().toString()));
        dealOriginalOrderItemVo.setDiscountFee(originalOrderItem.getDiscountFee().toString());
        dealOriginalOrderItemVo.setId(originalOrderItem.getId());
        dealOriginalOrderItemVo.setOutSku(originalOrderItem.getOuterSku());
        dealOriginalOrderItemVo.setSku(originalOrderItem.getSku());
        dealOriginalOrderItemVo.setPlatformSubOrderNo(originalOrderItem.getPlatformSubOrderNo());
        dealOriginalOrderItemVo.setPrice(originalOrderItem.getPrice().toString());
        dealOriginalOrderItemVo.setTitle(originalOrderItem.getTitle());
        return dealOriginalOrderItemVo;
    }


    /**
     * .原始订单Vo
     * @param originalOrder
     * @param dealOriginalOrderVos
     * @param dealOriginalOrderVo
     * @return
     */
    public static DealOriginalOrderVo getDealOriginalOrderVo(OriginalOrder originalOrder, List<DealOriginalOrderVo> dealOriginalOrderVos,DealOriginalOrderVo dealOriginalOrderVo) {
          dealOriginalOrderVo.setId(originalOrder.getId());
        if(originalOrder.getReceiver()!=null){
            Receiver receiver=originalOrder.getReceiver();
            dealOriginalOrderVo.setAddress(receiver.getReceiverAddress());
            dealOriginalOrderVo.setProvince(receiver.getReceiverState());
            dealOriginalOrderVo.setCity(receiver.getReceiverCity());
            dealOriginalOrderVo.setReceiverDistrict(receiver.getReceiverDistrict());
            dealOriginalOrderVo.setReceiverName(receiver.getReceiverName());
            dealOriginalOrderVo.setReceiverPhone(receiver.getReceiverPhone());
            dealOriginalOrderVo.setReceiverMobile(receiver.getReceiverMobile());
            dealOriginalOrderVo.setReceiverZip(receiver.getReceiverZip());
        }
        dealOriginalOrderVo.setBuyerId(originalOrder.getBuyerId());
        dealOriginalOrderVo.setBuyTime(originalOrder.getBuyTime());
        dealOriginalOrderVo.setPayTime(originalOrder.getPayTime());
        dealOriginalOrderVo.setCreateTime(originalOrder.getCreateTime());
        dealOriginalOrderVo.setModifiedTime(originalOrder.getModifiedTime());
        dealOriginalOrderVo.setOutActualFee(originalOrder.getActualFee().toString());
        dealOriginalOrderVo.setOutOrderNo(originalOrder.getPlatformOrderNo());
        dealOriginalOrderVo.setPlatformType(originalOrder.getPlatformType());
        dealOriginalOrderVo.setPostFee(originalOrder.getPostFee().toString());
        dealOriginalOrderVo.setDiscard(originalOrder.getDiscard()?"失效":"正常");
        dealOriginalOrderVo.setProcessed(originalOrder.getProcessed()?"已被解析":"未被解析");

         dealOriginalOrderVos.add(dealOriginalOrderVo);
        return dealOriginalOrderVo;
    }
}
