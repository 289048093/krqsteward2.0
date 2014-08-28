package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.EmployeeUtil;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.vo.AfterSalesVo;
import com.ejushang.steward.ordercenter.vo.FinancialOrderItemVo;
import com.ejushang.steward.ordercenter.vo.OrderItemVo;
import com.ejushang.steward.ordercenter.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Transient;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: JBoss.WU
 * Date: 14-8-12
 * Time: 下午5:07
 * To change this template use File | Settings | File Templates.
 */
public class AfterSaleUtil {
    /**
     * 拼接订单HQL
     *
     * @param map           controller接收的map参数
     * @param stringBuilder 拼接HQL变量
     * @param objects       添加的变量参数List集合
     * @throws java.text.ParseException
     */
    public static void orderCondition(Map<String, String> map, StringBuilder stringBuilder, List<Object> objects) throws ParseException {
        //获取拼接的HQL0000000000000000000000000000000000000000000000000000000000000000000000000语句
        sortOrderCondition(map, stringBuilder, objects);
        //去除重复列
        stringBuilder.append(" group by s.id order by ");
        stringBuilder.append(" s.createTime ");
        stringBuilder.append(" desc ");

    }

    /**
     * 选择性的左外连接订单项
     *
     * @param orderConditions Hql的where部分
     * @param targetHql       完整的HQL
     */
    private static void choiceLeftJoin(StringBuilder orderConditions, StringBuilder targetHql) {
        targetHql.append("select s from AfterSales s  ");
        if (orderConditions.toString().contains("i.")) {
            targetHql.append(" left outer  join s.afterSalesItemList i ");
        }
        targetHql.append(" where 1=1 ").append(orderConditions.toString());

    }


    private static void sortOrderCondition(Map<String, String> map, StringBuilder targetHql, List<Object> objects) throws ParseException {
        StringBuilder orderConditions = new StringBuilder();
//        //右边条件1判断
//        if (!StringUtils.isBlank(map.get("conditionValue")) && !map.get("conditionQuery").equalsIgnoreCase("null")) {
//            orderConditions.append(rightCondition(map.get("conditionType"), map.get("conditionQuery")));
//            if (map.get("conditionType").equals("has") || map.get("conditionType").equals("!")) {
//                objects.add("%" + StringUtils.trimToNull(map.get("conditionValue")) + "%");
//            } else {
//                //类型的校验和转化方法
//                checkDataType(objects, map.get("conditionQuery"), map.get("conditionValue").trim());
//            }
//        }

        //售后类型
        if (!StringUtils.isBlank(map.get("afterSalesType")) && !map.get("afterSalesType").equals("null")) {
            String type = map.get("afterSalesType");
            if (AfterSalesType.valueOf(type).equals(AfterSalesType.REFUND)) {
                orderConditions.append(" and ").append(" i.type =").append(" ? ").append(" or i.type= ").append(" ? ");
                objects.add(AfterSalesType.REFUND);
                objects.add(AfterSalesType.REFUND_GOODS);
            } else {
                orderConditions.append(" and ").append(" i.type =").append(" ? ");
                objects.add(AfterSalesType.valueOf(type));
            }
        }
        //售后状态
        if (!StringUtils.isBlank(map.get("afterSalesStatus")) && !map.get("afterSalesStatus").equals("null")) {
            orderConditions.append(" and ").append(" s.status =").append(" ? ");
            objects.add(AfterSalesStatus.valueOf(map.get("afterSalesStatus")));

        }
        //退款状态
        if (!StringUtils.isBlank(map.get("payment")) && !map.get("payment").equals("null")) {
            orderConditions.append(" and ").append(" i.afterSalesRefund.payment =").append(" ? ");
            objects.add(Boolean.valueOf(map.get("payment")));
        }
        //到货状态
        if (!StringUtils.isBlank(map.get("returned")) && !map.get("returned").equals("null")) {
            orderConditions.append(" and ").append(" i.afterSalesRefundGoods.returned =").append(" ? ");
            objects.add(Boolean.valueOf(map.get("returned")));
        }
        //退货状态
        if (!StringUtils.isBlank(map.get("send")) && !map.get("send").equals("null")) {
            orderConditions.append(" and ").append(" i.afterSalesRefundGoods.send =").append(" ? ");
            objects.add(Boolean.valueOf(map.get("send")));
        }
        //到货状态
        if (!StringUtils.isBlank(map.get("returned")) && !map.get("returned").equals("null")) {
            orderConditions.append(" and ").append(" i.afterSalesRefundGoods.returned =").append(" ? ");
            objects.add(Boolean.valueOf(map.get("returned")));
        }
        //买家ID
        if (!StringUtils.isBlank(map.get("buyerId")) && !map.get("buyerId").equals("null")) {
            orderConditions.append(" and ").append(" s.order.buyerId =").append(" ? ");
            objects.add(map.get("buyerId").trim());
        }
        //外部平台编号
        if (!StringUtils.isBlank(map.get("platformOrderNo")) && !map.get("platformOrderNo").equals("null")) {
            orderConditions.append(" and ").append(" s.platformOrderNo =").append(" ? ");
            objects.add(map.get("platformOrderNo").trim());
        }
        //店铺
        if (!StringUtils.isBlank(map.get("shopId")) && !map.get("shopId").equals("null")) {
            orderConditions.append(" and ").append(" s.order.shopId =").append(" ? ");
            objects.add(Integer.parseInt(map.get("shopId")));
        }
        //品牌
        if (!StringUtils.isBlank(map.get("brandId")) && !map.get("brandId").equals("null")) {
            orderConditions.append(" and ").append(" s.brandId=").append(" ? ");
            objects.add(Integer.parseInt(map.get("brandId")));
        }
        //订单日期类型及时间
        if (!StringUtils.isBlank(map.get("dateType")) && !map.get("dateType").equals("null")) {

            if (!StringUtils.isBlank(map.get("startDate"))) {
                objects.add(EJSDateUtils.parseDateForNull(StringUtils.trimToNull(map.get("startDate")), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            }
            if (!StringUtils.isBlank(map.get("endDate"))) {
                objects.add(EJSDateUtils.parseDateForNull(StringUtils.trimToNull(map.get("endDate")), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            }
            orderConditions.append(dateConditionUtil(map.get("dateType"), map.get("startDate"), map.get("endDate")));
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
//    private static String conditionQuery(String conQuery) {
//        String result = null;
//
//        if (conQuery.equalsIgnoreCase("orderNo")) {
//            result = "o.orderNo";
//        }
//        if (conQuery.equalsIgnoreCase("platformOrderNo")) {
//            result = "o.platformOrderNo";
//        }
//        if (conQuery.equalsIgnoreCase("prodName")) {
//            result = "i.productName";
//        }
//        if (conQuery.equalsIgnoreCase("buyerId")) {
//            result = "o.buyerId";
//        }
//        if (conQuery.equalsIgnoreCase("receiverAddress")) {
//            result = "o.invoice.receiver.receiverAddress";
//        }
//        if (conQuery.equalsIgnoreCase("offlineRemark")) {
//            result = "o.offlineRemark";
//        }
//        if (conQuery.equalsIgnoreCase("receiverName")) {
//            result = "o.invoice.receiver.receiverName";
//        }
//        if (conQuery.equalsIgnoreCase("shippingNo")) {
//            result = "o.invoice.shippingNo";
//        }
//        if (conQuery.equalsIgnoreCase("buyerAlipayNo")) {
//            result = "o.buyerAlipayNo";
//        }
//        ///////////////////////////////////订单模块没使用的条件开始
//        if (conQuery.equals("buyerMessage")) {
//            result = "o.buyerMessage";
//        }
//        if (conQuery.equals("remark")) {
//            result = "o.remark";
//        }
//        if (conQuery.equals("actualFee")) {
//            result = "o.actualFee";
//        }
//        if (conQuery.equals("receiverMobile")) {
//            result = "o.invoice.receiver.receiverMobile";
//        }
//        ////////////////////////////////////////结束
//        if (conQuery.equals("prodCode")) {
//            result = "i.productCode";
//        }
//        if (conQuery.equals("prodSku")) {
//            result = "i.productSku";
//        }
//        return result;
//    }

//    /**
//     * 拼接右边条件
//     *
//     * @param conType 符号参数
//     * @param query   条件字段参数
//     * @return 返回拼接条件
//     */
//    private static String rightCondition(String conType, String query) {
//        StringBuilder stringBuilder = new StringBuilder();
//        String newQuery = conditionQuery(query);
//        if (conType.equalsIgnoreCase("=")) {
//            stringBuilder.append(" and ").append(newQuery).append(" = ").append(" ? ");
//        }
//        if (conType.equalsIgnoreCase("!=")) {
//            stringBuilder.append(" and ").append(newQuery).append(" <> ").append(" ? ");
//        }
//        if (conType.equalsIgnoreCase("<=")) {
//            stringBuilder.append(" and ").append(newQuery).append(" <= ").append(" ? ");
//        }
//        if (conType.equalsIgnoreCase(">")) {
//            stringBuilder.append(" and ").append(newQuery).append(" > ").append(" ? ");
//        }
//        if (conType.equalsIgnoreCase(">=")) {
//            stringBuilder.append(" and ").append(newQuery).append(" >= ").append(" ? ");
//        }
//        if (conType.equalsIgnoreCase("<")) {
//            stringBuilder.append(" and ").append(newQuery).append(" < ").append(" ? ");
//        }
//        if (conType.equalsIgnoreCase("!")) {
//            stringBuilder.append(" and ").append(newQuery).append(" not like ").append(" ? ");
//        }
//        if (conType.equalsIgnoreCase("has")) {
//            stringBuilder.append(" and ").append(newQuery).append(" like ").append(" ? ");
//        }
//        return stringBuilder.toString();
//    }

    /**
     * 拼接日期
     *
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
        if (dateType.trim().equalsIgnoreCase("createDate")) {
            column = "s.createTime";
        }
        if (dateType.trim().equalsIgnoreCase("refundDate")) {
            column = "i.afterSalesRefund.createTime";
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
     * @param map    传过来的参数
     * @param result 解析后的参数
     */
    public static void getConditionMap(Map<String, Object[]> map, Map<String, String> result) {
        for (Map.Entry<String, Object[]> entry : map.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue()[0];
            result.put(key, val.toString());
        }
    }

    /**
     * 校验map参数
     *
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

    public static void getAfterSaleVo(AfterSales afterSales, AfterSalesVo afterSalesVo) {
        afterSalesVo.setOrderId(afterSales.getOrderId());
        afterSalesVo.setId(afterSales.getId());
        afterSalesVo.setReason(afterSales.getReason());
        afterSalesVo.setReasonCode(afterSales.getReasonCode());
        afterSalesVo.setRemark(afterSales.getRemark());
        afterSalesVo.setBrandName(afterSales.getBrandName());
        afterSalesVo.setPlatformType(afterSales.getPlatformType());
        afterSalesVo.setPlatformOrderNo(afterSales.getPlatformOrderNo());
        afterSalesVo.setRevisitId(afterSales.getRevisitId());
        Order order = afterSales.getOrder();
        if (order != null) {
            afterSalesVo.setBuyerId(order.getBuyerId());
            afterSalesVo.setShopName(order.getShop().getNick());
            afterSalesVo.setPlatformType(order.getPlatformType());
        }
        List<AfterSalesItem> afterSalesItems = afterSales.getAfterSalesItemList();
        int afterSalesRefundNum = 0;
        int afterSalesRefundGoodsNum = 0;
        int onlineRefundCount = 0;
        int refundGoodsCount = 0;
        int receiveRefundGoodsCount = 0;
        int afterSalesRefundOnlineNum=0;
        int afterSalesRefundOfflineNum=0;
        for (AfterSalesItem afterSalesItem : afterSalesItems) {
            AfterSalesRefund afterSalesRefund = afterSalesItem.getAfterSalesRefund();
            if (afterSalesRefund != null) {
                afterSalesRefundNum++;
                if (afterSalesRefund.getOnlineFee().getAmount() > 0) {
                    onlineRefundCount++;
                }
                //线上
                if(afterSalesRefund.getOnlineFee().getAmount()>0&&afterSalesRefund.getOfflineFee().getAmount()==0){
                    afterSalesRefundOnlineNum++;
                }
                //线下
                else if(afterSalesRefund.getOnlineFee().getAmount()==0&&afterSalesRefund.getOfflineFee().getAmount()>0){
                    afterSalesRefundOfflineNum++;
                }
            }
            AfterSalesRefundGoods afterSalesRefundGoods = afterSalesItem.getAfterSalesRefundGoods();
            if (afterSalesRefundGoods != null) {
                afterSalesRefundGoodsNum++;
                if (afterSalesRefundGoods.isReturned()) {
                    refundGoodsCount++;
                }
                if (afterSalesRefundGoods.isReceived()) {
                    receiveRefundGoodsCount++;
                }
            }

        }
        String refundStatus = null;
        if (onlineRefundCount < afterSalesRefundNum && onlineRefundCount > 0) {
            refundStatus = "部分支付";
        } else if (onlineRefundCount == afterSalesRefundNum) {
            refundStatus = "已支付";
        } else {
            refundStatus = "未支付";
        }
        afterSalesVo.setRefundStatus(refundStatus);
        String refundGoods = null;
        if (refundGoodsCount < afterSalesRefundGoodsNum && refundGoodsCount > 0) {
            refundGoods = "部分退货";
        } else if (refundGoodsCount == afterSalesRefundGoodsNum) {
            refundGoods = "已退货";
        } else if(afterSalesRefundGoodsNum==0) {
            refundGoods="";
        } else{
            refundGoods = "未退货";
        }
        afterSalesVo.setRefundGoodsStatus(refundGoods);
        String refundGoodsStatus = null;
        if (receiveRefundGoodsCount < afterSalesRefundGoodsNum && receiveRefundGoodsCount > 0) {
            refundGoodsStatus = "部分到货";
        } else if (receiveRefundGoodsCount == afterSalesRefundGoodsNum) {
            refundGoodsStatus = "已到货";
        } else if(afterSalesRefundGoodsNum==0) {
            refundGoodsStatus = "";
        }  else{
            refundGoodsStatus = "未到货";
        }
        afterSalesVo.setReceiverGoodsStatus(refundGoodsStatus);
        String refundWay=null;
        if(afterSalesRefundOnlineNum==afterSalesRefundNum&&afterSalesRefundOfflineNum==0){
           refundWay="线上退款";
        }
        else if(afterSalesRefundOfflineNum==afterSalesRefundNum&&afterSalesRefundOnlineNum==0){
            refundWay="线下退款";
        }
        else if(afterSalesRefundNum==0){
            refundWay="";
        }       else{
            refundWay="线下，线上退款";
        }
        afterSalesVo.setRefundWay(refundWay);
    }

}
