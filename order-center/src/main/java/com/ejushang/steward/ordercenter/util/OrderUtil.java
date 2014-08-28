package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.*;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.vo.FinancialOrderItemVo;
import com.ejushang.steward.ordercenter.vo.OrderItemVo;
import com.ejushang.steward.ordercenter.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import javax.persistence.Transient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tin
 * Date: 14-4-14
 * Time: 下午4:31
 */
public class OrderUtil {

    private OrderUtil() {
    }


    /**
     * 拼接订单HQL
     *
     * @param map           controller接收的map参数
     * @param stringBuilder 拼接HQL变量
     * @param objects       添加的变量参数List集合
     * @throws ParseException
     */
    public static void orderCondition(Map<String, String> map, StringBuilder stringBuilder, List<Object> objects, List<Integer> repoIdList) throws ParseException {
        //获取拼接的HQL0000000000000000000000000000000000000000000000000000000000000000000000000语句
        sortOrderCondition(map, stringBuilder, objects, repoIdList);
        //去除重复列
        stringBuilder.append(" group by o.id order by ");
        //按照前端参数排序
        if (StringUtils.isNotBlank(map.get("sortParam"))) {
            String[] orderByType = map.get("sortParam").split("#");
            if (map.get("sortParam").contains("o.invoice.shippingComp")) {
                stringBuilder.append(orderByType[0]).append(",o.invoice.shippingNo");
            } else if (map.get("sortParam").contains("o.")) {
                stringBuilder.append(" o.").append(orderByType[0]);
            } else if (map.get("sortParam").contains("i.")) {
                stringBuilder.append(" i.").append(orderByType[0]);
            } else if (map.get("sortParam").contains("a.")) {
                stringBuilder.append(" a.").append(orderByType[0]);
            }
            else{
                stringBuilder.append(" o.").append(orderByType[0]);
            }
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
        targetHql.append("select o from Order o left outer join fetch o.invoice invoice ");
        if (orderConditions.toString().contains("i.")) {
            targetHql.append(" left outer  join  o.orderItemList i ");
        }
        if (orderConditions.toString().contains("a.")) {
            targetHql.append(" left  outer join o.orderApproveList a ");
        }
        targetHql.append(" where 1=1 ").append(orderConditions.toString());
    }


    private static void sortOrderCondition(Map<String, String> map, StringBuilder targetHql, List<Object> objects, List<Integer> repoIdList) throws ParseException {
        StringBuilder orderConditions = new StringBuilder();
        //右边条件1判断
        if (!StringUtils.isBlank(map.get("conditionValue")) && !map.get("conditionQuery").equalsIgnoreCase("null")) {
            orderConditions.append(rightCondition(map.get("conditionType"), map.get("conditionQuery")));
            if (map.get("conditionType").equals("has") || map.get("conditionType").equals("!")) {
                objects.add("%" + StringUtils.trimToNull(map.get("conditionValue")) + "%");
            } else {
                //类型的校验和转化方法
                checkDataType(objects, map.get("conditionQuery"), map.get("conditionValue").trim());
            }
        }

        //右边条件2判断
        if (!StringUtils.isBlank(map.get("conditionValue2")) && !map.get("conditionQuery2").equalsIgnoreCase("null")) {
            orderConditions.append(rightCondition(map.get("conditionType2"), map.get("conditionQuery2")));
            if (map.get("conditionType2").equals("has") || map.get("conditionType2").equals("!")) {
                objects.add("%" + StringUtils.trimToNull(map.get("conditionValue2")) + "%");
            } else {
                checkDataType(objects, map.get("conditionQuery2"), map.get("conditionValue2").trim());
            }
        }
        //特殊日期判断
        if (!StringUtils.isBlank(map.get("appOrderStatus")) && !map.get("appOrderStatus").equals("all")) {
            orderConditions.append(" and ").append(" a.orderStatus=").append(" ? ");
            objects.add(OrderStatus.valueOf(map.get("appOrderStatus")));
//           if(repoIdList==null){
//            map.remove("orderStatus");}
        }
        //订单状态判断
        if (!StringUtils.isBlank(map.get("orderStatus")) && !map.get("orderStatus").equals("null")) {
            //发货管理的全部功能
            if (map.get("orderStatus").trim().equalsIgnoreCase("all")) {
                orderConditions.append(" and ").append(" o.status in('");
                orderConditions.append(OrderStatus.CONFIRMED).append("','");
                orderConditions.append(OrderStatus.PRINTED).append("','");
                orderConditions.append(OrderStatus.EXAMINED).append("','");
                orderConditions.append(OrderStatus.INVOICED).append("','");
                orderConditions.append(OrderStatus.SIGNED);
                orderConditions.append("')");
            } else {
                orderConditions.append(" and ").append(" o.status= ").append(" ? ");
                objects.add(OrderStatus.valueOf(map.get("orderStatus")));
            }
        }
        //订单类型判断
        if (!StringUtils.isBlank(map.get("orderType")) && !map.get("orderType").equals("null")) {
            orderConditions.append(" and ").append(" o.type= ").append(" ? ");
            objects.add(OrderType.valueOf(map.get("orderType")));
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
            objects.add(Integer.parseInt(map.get("shopId")));
        }

        //仓库
        if (!StringUtils.isBlank(map.get("repoId")) && !map.get("repoId").equals("null")) {
            orderConditions.append(" and ").append(" o.repoId =").append(" ? ");
            objects.add(Integer.parseInt(map.get("repoId")));
        }

        //物流公司
        if (!StringUtils.isBlank(map.get("shippingComp")) && !map.get("shippingComp").equals("null")) {
            orderConditions.append(" and ").append(" invoice.shippingComp=").append(" ? ");
            objects.add(map.get("shippingComp"));
        }

        //订单退货状态
        if (!StringUtils.isBlank(map.get("orderReturnStatus")) && !map.get("orderReturnStatus").equals("null")) {
            orderConditions.append(" and ").append(" o.orderReturnStatus=").append(" ? ");
            objects.add(OrderReturnStatus.valueOf(map.get("orderReturnStatus")));
        }

        //订单生成类型
        if (!StringUtils.isBlank(map.get("generateType")) && !map.get("generateType").equals("null")) {
            orderConditions.append(" and ").append(" o.generateType=").append(" ? ");
            objects.add(OrderGenerateType.valueOf(map.get("generateType")));
        }

        //订单项类型
        if (!StringUtils.isBlank(map.get("orderItemType")) && !map.get("orderItemType").equals("null")) {
            orderConditions.append(" and ").append(" i.type=").append(" ? ");
            objects.add(OrderItemType.valueOf(map.get("orderItemType")));
        }

        //订单项状态
        if (!StringUtils.isBlank(map.get("orderItemStatus")) && !map.get("orderItemStatus").equals("null")) {
            orderConditions.append(" and ").append(" i.status=").append(" ? ");
            objects.add(OrderItemStatus.valueOf(map.get("orderItemStatus")));
        }

        //订单项线下退货状态
        if (!StringUtils.isBlank(map.get("offlineOrderItemReturnStatus")) && !map.get("offlineOrderItemReturnStatus").equals("null")) {
            orderConditions.append(" and ").append(" i.offlineReturnStatus=").append(" ? ");
            objects.add(OrderItemReturnStatus.valueOf(map.get("offlineOrderItemReturnStatus").trim()));
        }
        //退款状态
        if (!StringUtils.isBlank(map.get("refunding")) && !map.get("refunding").equals("null")) {
            orderConditions.append(" and ").append(" i.refunding=").append(" ? ");
            objects.add(Boolean.valueOf(map.get("refunding").trim()));
        }
        //订单项线上退货状态
        if (!StringUtils.isBlank(map.get("orderItemReturnStatus")) && !map.get("orderItemReturnStatus").equals("null")) {
            orderConditions.append(" and ").append(" i.returnStatus=").append(" ? ");
            objects.add(OrderItemReturnStatus.valueOf(map.get("orderItemReturnStatus").trim()));
        }
        //订单项线上退货承担方
        if (!StringUtils.isBlank(map.get("returnPostPayer")) && !map.get("returnPostPayer").equals("null")) {
            orderConditions.append(" and ").append(" i.returnPostPayer=").append(" ? ");
            objects.add(PostPayer.valueOf(map.get("returnPostPayer").trim()));
        }
        //订单项线下退货承担方
        if (!StringUtils.isBlank(map.get("offlineReturnPostPayer")) && !map.get("offlineReturnPostPayer").equals("null")) {
            orderConditions.append(" and ").append(" i.offlineReturnPostPayer=").append(" ? ");
            objects.add(PostPayer.valueOf(map.get("offlineReturnPostPayer").trim()));
        }
        //订单项线下换货承担方
        if (!StringUtils.isBlank(map.get("exchangePostPayer")) && !map.get("exchangePostPayer").equals("null")) {
            orderConditions.append(" and ").append(" i.exchangePostPayer=").append(" ? ");
            objects.add(PostPayer.valueOf(map.get("exchangePostPayer").trim()));
        }
        //品牌
        if (!StringUtils.isBlank(map.get("brandId")) && !map.get("brandId").equals("null")) {
            orderConditions.append(" and ").append(" i.product.brandId=").append(" ? ");
            objects.add(Integer.parseInt(map.get("brandId").trim()));
        }
        //平台
        if (!StringUtils.isBlank(map.get("outPlatformType")) && !map.get("outPlatformType").equals("null")) {
            orderConditions.append(" and ").append(" o.platformType=").append(" ? ");
            objects.add(PlatformType.valueOf(map.get("outPlatformType").trim()));
        }
        //发货管理假如是仓库管理员，只能查询自己的仓库权限
        if (repoIdList != null && repoIdList.size() > 0) {
            orderConditions.append(" and o.repoId in( ")
                    .append(Joiner.join(repoIdList,","))
                    .append(" ) ");
        }
        //添加只显示不失效订单
        orderConditions.append(" and o.valid=").append(true);
        //发货管理的订单不能是退货单
        if (repoIdList != null) {
            orderConditions.append(" and o.orderReturnStatus <> '").append(OrderReturnStatus.RETURNED).append("' ");
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

    public static void exchangeOrderCondition(Map<String, String> map, StringBuilder stringBuilder, List<Object> objects) throws ParseException {
        sortOrderCondition(map, stringBuilder, objects, null);

        stringBuilder.append(" and (o.type='REPLENISHMENT' or o.type='EXCHANGE') ");
//        stringBuilder.append(" and (o.type in ('").append(OrderType.REPLENISHMENT).append("','").append(OrderType.EXCHANGE).append("')");
    }

    /**
     * 拼接右边条件字段的where
     *
     * @param conQuery 字段参数
     * @return 返回拼接字段
     */
    private static String conditionQuery(String conQuery) {
        String result = null;

        if (conQuery.equalsIgnoreCase("orderNo")) {
            result = "o.orderNo";
        }
        if (conQuery.equalsIgnoreCase("platformOrderNo")) {
            result = "o.platformOrderNo";
        }
        if (conQuery.equalsIgnoreCase("prodName")) {
            result = "i.productName";
        }
        if (conQuery.equalsIgnoreCase("buyerId")) {
            result = "o.buyerId";
        }
        if (conQuery.equalsIgnoreCase("receiverAddress")) {
            result = "o.invoice.receiver.receiverAddress";
        }
        if (conQuery.equalsIgnoreCase("offlineRemark")) {
            result = "o.offlineRemark";
        }
        if (conQuery.equalsIgnoreCase("receiverName")) {
            result = "o.invoice.receiver.receiverName";
        }
        if (conQuery.equalsIgnoreCase("shippingNo")) {
            result = "o.invoice.shippingNo";
        }
        if (conQuery.equalsIgnoreCase("buyerAlipayNo")) {
            result = "o.buyerAlipayNo";
        }
        ///////////////////////////////////订单模块没使用的条件开始
        if (conQuery.equals("buyerMessage")) {
            result = "o.buyerMessage";
        }
        if (conQuery.equals("remark")) {
            result = "o.remark";
        }
        if (conQuery.equals("actualFee")) {
            result = "o.actualFee";
        }
        if (conQuery.equals("receiverMobile")) {
            result = "o.invoice.receiver.receiverMobile";
        }
        ////////////////////////////////////////结束
        if (conQuery.equals("prodCode")) {
            result = "i.productCode";
        }
        if (conQuery.equals("prodSku")) {
            result = "i.productSku";
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
        if (dateType.trim().equalsIgnoreCase("printDate")) {
            column = "a.updateTime";
        }
        if (dateType.trim().equalsIgnoreCase("confirmedDate")) {
            column = "a.updateTime";
        }
        if (dateType.trim().equalsIgnoreCase("examinedDate")) {
            column = "a.updateTime";
        }
        if (dateType.trim().equalsIgnoreCase("deliveryDate")) {
            column = "a.updateTime";
        }
        if (dateType.trim().equalsIgnoreCase("orderDate")) {
            column = "o.buyTime";
        }
        if (dateType.trim().equalsIgnoreCase("receiptDate")) {
            column = "a.updateTime";
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
                map1.put("orderByType", hashMap.get("property") + "");
                map1.put("sort", hashMap.get("direction") + "");
            }
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

    //更新订单
    public static void getSaveCondition(HashMap map, Receiver receiver, Order order, Invoice invoice) {

        if (map.get("buyerMessage") != null) {
            order.setBuyerMessage(map.get("buyerMessage").toString());
        }
        if (map.get("offlineRemark") != null) {
            order.setOfflineRemark(map.get("offlineRemark").toString());
        }
        if (map.get("remark") != null) {
            order.setRemark(map.get("remark").toString());
        }
        if (map.get("shippingComp") != null) {
            invoice.setShippingComp(map.get("shippingComp").toString());
        }
        if (map.get("shippingNo") != null) {
            invoice.setShippingNo(map.get("shippingNo").toString());
        }
        if (map.get("receiverState") != null) {
            receiver.setReceiverState(map.get("receiverState").toString());
        }
        if (map.get("receiverCity") != null) {
            receiver.setReceiverCity(map.get("receiverCity").toString());
        }
        if (map.get("receiverDistrict") != null) {
            receiver.setReceiverDistrict(map.get("receiverDistrict").toString());
        }
        if (map.get("receiverName") != null) {
            receiver.setReceiverName(map.get("receiverName").toString());
        }
        if (map.get("receiverAddress") != null) {
            receiver.setReceiverAddress(map.get("receiverAddress").toString());
        }
        if (map.get("receiverZip") != null) {
            receiver.setReceiverZip(map.get("receiverZip").toString());
        }
        if (map.get("receiverPhone") != null) {
            receiver.setReceiverPhone(map.get("receiverPhone").toString());
        }
        if (map.get("receiverMobile") != null) {
            receiver.setReceiverMobile(map.get("receiverMobile").toString());
        }
        if (receiver != null) {
            invoice.setReceiver(receiver);
        }
    }

    /**
     * 拼接OrderItemVo
     *
     * @param orderItemVo 订单项VO实体
     * @param orderItem   订单项VO实体
     */

    public static OrderItemVo getOrderItemVo(OrderItemVo orderItemVo, OrderItem orderItem, OrderType orderType, GeneralDAO generalDAO, Boolean isAble) {
        if (isAble) {
            if (orderType != null && orderType.equals(OrderType.CHEAT)) {
                orderItemVo.setProductName("嘻唰唰专用");
                orderItemVo.setProductCode("嘻唰唰专用");
                orderItemVo.setType(OrderItemType.PRODUCT.getValue());
                orderItemVo.setColor("");
                orderItemVo.setActualFee("0.00");
                orderItemVo.setUserActualPrice("0.00");
                orderItemVo.setGoodsFee(Money.valueOf("0.00"));
                return orderItemVo;
            }
        }
        if (generalDAO != null) {
            if (orderItem.getExchanged()) {
                String exchangeOrderItemProductName = null;
                if (orderItem.getExchangeOrderItemId() != null) {
                    ExchangeOrderItem exchangeOrderItem = generalDAO.get(ExchangeOrderItem.class, orderItem.getExchangeOrderItemId());
                    if (exchangeOrderItem != null) {
                        exchangeOrderItemProductName = exchangeOrderItem.getProductName();
                    }
                }
                if (exchangeOrderItemProductName != null) {
                    orderItemVo.setExchangedGoods(String.format("被售前换货,原产品名称(%s)", exchangeOrderItemProductName));
                }
            }
        }
        orderItemVo.setPostFee(orderItem.getInvoicePostFee());
        orderItemVo.setOuterSku(orderItem.getOuterSku());
        orderItemVo.setActualFee(orderItem.getActualFee().toString());
        orderItemVo.setSpecInfo(orderItem.getSpecInfo());
        orderItemVo.setId(orderItem.getId());
        orderItemVo.setPlatformSubOrderNo(orderItem.getPlatformSubOrderNo());
        orderItemVo.setType(orderItem.getType().getValue());
        orderItemVo.setStatus(orderItem.getStatus().toString());
        orderItemVo.setReturnStatus(orderItem.getReturnStatus());
        orderItemVo.setOfflineReturnStatus(orderItem.getOfflineReturnStatus());
        orderItemVo.setProductCode(orderItem.getProductCode());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductSku(orderItem.getProductSku());
        orderItemVo.setExchanged(orderItem.getExchanged());
        Product product = orderItem.getProduct();
        if (product != null) {
            Brand brand = product.getBrand();
            orderItemVo.setColor(orderItem.getProduct().getColor());
            ProductCategory category = product.getCategory();
            orderItemVo.setBrandName(brand != null ? brand.getName() : null);
            orderItemVo.setCateName(category != null ? category.getName() : null);
        }
        orderItemVo.setRepoNum(orderItem.getRepoNum().toString());
        orderItemVo.setGoodsFee(orderItem.getGoodsFee());
        orderItemVo.setPrice(orderItem.getPrice().toString());
        orderItemVo.setBuyCount(orderItem.getBuyCount().toString());
        orderItemVo.setDiscountPrice(orderItem.getDiscountPrice().toString());
        orderItemVo.setDiscountFee(orderItem.getDiscountFee().toString());
        orderItemVo.setSharedDiscountFee(orderItem.getSharedDiscountFee().toString());
        orderItemVo.setSharedPostFee(orderItem.getSharedPostFee().toString());
        orderItemVo.setUserActualPrice(orderItem.getUserActualPrice().toString());
        orderItemVo.setPostCoverFee(orderItem.getPostCoverFee().toString());
        orderItemVo.setPostCoverRefundFee(orderItem.getPostCoverRefundFee().toString());
        orderItemVo.setServiceCoverFee(orderItem.getServiceCoverFee().toString());
        orderItemVo.setServiceCoverRefundFee(orderItem.getServiceCoverRefundFee().toString());
        orderItemVo.setRefundFee(orderItem.getRefundFee().toString());
        orderItemVo.setOfflineRefundFee(orderItem.getOfflineRefundFee().toString());
        orderItemVo.setReturnPostFee(orderItem.getReturnPostFee().toString());
        orderItemVo.setReturnPostPayer(orderItem.getReturnPostPayer());
        orderItemVo.setOfflineReturnPostFee(orderItem.getOfflineReturnPostFee().toString());
        orderItemVo.setOfflineReturnPostPayer(orderItem.getOfflineReturnPostPayer());
        orderItemVo.setExchangePostFee(orderItem.getExchangePostFee().toString());
        orderItemVo.setExchangePostPayer(orderItem.getExchangePostPayer());
        orderItemVo.setPriceDescription(orderItem.getPriceDescription());
        orderItemVo.setBuyerAlipayNo(orderItem.getOrder().getBuyerAlipayNo());
        if (orderItem.getExchangeSourceId() != null) {
            orderItemVo.setExchangeSourceId(orderItem.getExchangeSourceId());
        }
        return orderItemVo;
    }

    public static void getOrderItemVos(OrderItemVo orderItemVo, List<OrderItemVo> orderItemVos, OrderItem orderItem, PaymentAllocation paymentAllocation, GeneralDAO generalDAO, OrderType orderType, Boolean isAble) {
        getOrderItemVo(orderItemVo, orderItem, orderType, generalDAO, isAble);
        if (paymentAllocation != null) {
            orderItemVo.setFeesString(paymentAllocation.getPaymentFee().toString());
            orderItemVo.setRefundFeesString(paymentAllocation.getRefundFee().toString());
        }
        orderItemVos.add(orderItemVo);
    }

    public static void getInvoiceOrderItemVos(OrderItem orderItem, OrderType orderType, List<OrderItemVo> orderItemVos) {
        OrderItemVo orderItemVo = new OrderItemVo();
//        if(orderType.equals(OrderType.CHEAT)){
//            orderItemVo.setProductName("嘻唰唰专用");
//            orderItemVo.setType("商品");
//            orderItemVos.add(orderItemVo);
//        }
//        else{
        getOrderItemVos(orderItemVo, orderItemVos, orderItem, null, null, orderType, true);
//        }                                                                                            f


    }


    /**
     * 拼接OrderVo
     *
     * @param order    订单实体
     * @param orderVos OrderVoListst
     */
    public static OrderVo getOrderVo(Order order, List<OrderVo> orderVos, List<OrderItem> orderItems, Boolean type) {
        OrderVo orderVo = new OrderVo();
        orderVo.setCreateTime(order.getCreateTime());
        orderVo.setId(order.getId());
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setOrderStatus(order.getStatus());
        orderVo.setBuyerMessage(order.getBuyerMessage());
        orderVo.setRemark(order.getRemark());
        orderVo.setBuyerId(order.getBuyerId());
        orderVo.setOrderType(order.getType());
        orderVo.setOfflineRemark(order.getOfflineRemark());
        orderVo.setNeedReceipt(order.getNeedReceipt());
        orderVo.setReceiptTitle(order.getReceiptTitle());
        orderVo.setActualFee(order.getActualFee().toString());
        orderVo.setOrderReturnStatus(order.getOrderReturnStatus());
//        List<OrderApprove> orderApproves=order.getOrderApproveList();
//        //获取导入进销存时间（审单）
//         for(OrderApprove orderApprove:orderApproves) {
//             if(orderApprove.getOrderStatus().equals(OrderStatus.CONFIRMED)){
//                 orderVo.setConfirmedTime(orderApprove.getUpdateTime());
//             }
//         }
        List<OrderApprove> orderApproves = order.getOrderApproveList();
        //获取导入进销存时间（审单）
        for (OrderApprove orderApprove : orderApproves) {
            if (orderApprove.getOrderStatus().equals(OrderStatus.PRINTED)) {
                orderVo.setPrintedTime(EJSDateUtils.formatDate(orderApprove.getUpdateTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            }
            if (orderApprove.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
                orderVo.setConfirmedTime(EJSDateUtils.formatDate(orderApprove.getUpdateTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            }
        }
//        orderVo.setBuyerAlipayNo(order.getBuyerAlipayNo());    支付宝账号（暂时不显示）
        //订单项状态（拆分，合并··）
        orderVo.setGenerateType(order.getGenerateType() != null ? order.getGenerateType() : null);
        orderVo.setReceiptContent(order.getReceiptContent());
        //发货信息
        Invoice invoice = order.getInvoice();
        if (invoice != null) {
            orderVo.setShippingComp(invoice.getShippingComp());
            orderVo.setShippingNo(invoice.getShippingNo());
            //发货地址和联系方式
            if (invoice.getReceiver() != null) {
                orderVo.setReceiverZip(invoice.getReceiver().getReceiverZip());
                orderVo.setReceiverCity(invoice.getReceiver().getReceiverCity());
                orderVo.setReceiverDistrict(invoice.getReceiver().getReceiverDistrict());
                orderVo.setReceiverName(invoice.getReceiver().getReceiverName());
                orderVo.setReceiverAddress(invoice.getReceiver().getReceiverAddress());
                orderVo.setReceiverState(invoice.getReceiver().getReceiverState());
                orderVo.setReceiverMobile(invoice.getReceiver().getReceiverMobile());
                orderVo.setReceiverPhone(invoice.getReceiver().getReceiverPhone());
            }
        }
        //外部订单号
        OriginalOrder originalOrder = order.getOriginalOrder();
        orderVo.setOutActualFee(originalOrder != null ? originalOrder.getActualFee().toString() : "");
        orderVo.setSharedDiscountFee(order.getSharedDiscountFee().toString());
        orderVo.setOutPlatformType(order.getPlatformType());
        orderVo.setPlatformOrderNo(order.getPlatformOrderNo());
        orderVo.setPostFee(order.getSharedPostFee().toString());
        orderVo.setGoodsFee(order.getGoodsFee().toString());
        //仓库
        Repository repository = order.getRepo();
        orderVo.setRepoName(repository != null ? repository.getName() : "");
        orderVo.setBuyTime(EJSDateUtils.formatDate(order.getBuyTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        orderVo.setPayTime(EJSDateUtils.formatDate(order.getPayTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        //店铺
        Shop shop = order.getShop();
        if (shop != null) {
            orderVo.setShopId(shop.getId());
            orderVo.setShopName(shop.getTitle() != null ? (shop.getTitle() + "(" + order.getPlatformType().getValue() + ")") : "");
        }
        orderVo.setRepoId(order.getRepoId());
        orderVo.setOrderItem(order.getOrderItemList());
        //显示订单项第一条数据名字
        int orderItemsSize = orderItems.size();
        if (orderItemsSize > 0) {
            Boolean boolRefund = false;
            Double maxMoney = 0d;
            int index = 0;

            for (int i = 0; i < orderItemsSize; i++) {
                OrderItem orderItem = orderItems.get(i);
                if (orderItem.getPrice().getAmount() > 0) {
                    if (orderItem.getPrice().getAmount() > maxMoney) {
                        maxMoney = orderItem.getPrice().getAmount();
                        index = i;
                    }
                }
                if (orderItem.getRefunding()) {
                    boolRefund = true;
                }

                orderVo.setItemName(orderItems.get(index).getProductName());
                orderVo.setRefunding(boolRefund ? "正在申请" : "正常");
            }
        }
        orderVo.queryOthers(type);
        orderVos.add(orderVo);
        return orderVo;
    }

    public static OrderVo getOrderVo(Order order) {
        OrderVo orderVo = new OrderVo();
        orderVo.setCreateTime(order.getCreateTime());
        orderVo.setId(order.getId());
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setOrderStatus(order.getStatus());
        orderVo.setBuyerMessage(order.getBuyerMessage());
        orderVo.setRemark(order.getRemark());
        orderVo.setBuyerId(order.getBuyerId());
        orderVo.setOrderType(order.getType());
        orderVo.setOfflineRemark(order.getOfflineRemark());
        orderVo.setNeedReceipt(order.getNeedReceipt());
        orderVo.setReceiptTitle(order.getReceiptTitle());
        orderVo.setActualFee(order.getActualFee() + "");
        orderVo.setOrderReturnStatus(order.getOrderReturnStatus());
//        orderVo.setBuyerAlipayNo(order.getBuyerAlipayNo());    支付宝账号（暂时不显示）
        //订单项状态（拆分，合并··）
        orderVo.setGenerateType(order.getGenerateType() != null ? order.getGenerateType() : null);
        orderVo.setReceiptContent(order.getReceiptContent());
        //发货信息
        Invoice invoice = order.getInvoice();
        if (invoice != null) {
            orderVo.setShippingComp(invoice.getShippingComp());
            orderVo.setShippingNo(invoice.getShippingNo());
            //发货地址和联系方式
            if (invoice.getReceiver() != null) {
                orderVo.setReceiverZip(invoice.getReceiver().getReceiverZip());
                orderVo.setReceiverCity(invoice.getReceiver().getReceiverCity());
                orderVo.setReceiverDistrict(invoice.getReceiver().getReceiverDistrict());
                orderVo.setReceiverName(invoice.getReceiver().getReceiverName());
                orderVo.setReceiverAddress(invoice.getReceiver().getReceiverAddress());
                orderVo.setReceiverState(invoice.getReceiver().getReceiverState());
                orderVo.setReceiverMobile(invoice.getReceiver().getReceiverMobile());
                orderVo.setReceiverPhone(invoice.getReceiver().getReceiverPhone());
            }
        }
        //外部订单号
        OriginalOrder originalOrder = order.getOriginalOrder();
        orderVo.setOutActualFee(originalOrder != null ? originalOrder.getActualFee().toString() : "");
        orderVo.setSharedDiscountFee(order.getSharedDiscountFee().toString());
        orderVo.setOutPlatformType(order.getPlatformType());
        orderVo.setPlatformOrderNo(order.getPlatformOrderNo());
        orderVo.setPostFee(order.getSharedPostFee().toString());
        orderVo.setGoodsFee(order.getGoodsFee().toString());
        //仓库
        Repository repository = order.getRepo();
        orderVo.setRepoName(repository != null ? repository.getName() : "");
        orderVo.setBuyTime(EJSDateUtils.formatDate(order.getBuyTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        orderVo.setPayTime(EJSDateUtils.formatDate(order.getPayTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        //店铺
        Shop shop = order.getShop();
        if (shop != null) {
            orderVo.setShopId(shop.getId());
            orderVo.setShopName(shop.getTitle() != null ? (shop.getTitle() + "(" + order.getPlatformType().getValue() + ")") : "");
        }
        orderVo.setRepoId(order.getRepoId());
        orderVo.setOrderItem(order.getOrderItemList());
        //显示订单项第一条数据名字
        int orderItemsSize = orderVo.getOrderItem().size();
        if (orderItemsSize > 0) {
            Boolean boolRefund = false;
            Double maxMoney = 0d;
            int index = 0;

            for (int i = 0; i < orderItemsSize; i++) {
                OrderItem orderItem = orderVo.getOrderItem().get(i);
                if (orderItem.getPrice().getAmount() > 0) {
                    if (orderItem.getPrice().getAmount() > maxMoney) {
                        maxMoney = orderItem.getPrice().getAmount();
                        index = i;
                    }
                }
                if (orderItem.getRefunding()) {
                    boolRefund = true;
                }

                orderVo.setItemName(orderVo.getOrderItem().get(index).getProductName());
                orderVo.setRefunding(boolRefund ? "正在申请" : "正常");
            }
        }
        orderVo.queryOthers(true);
        return orderVo;
    }


    public static void getOrderItemCondition(Map<String, Object[]> map, Search search) {
        if (map.get("orderIds") != null) {
            search.addFilterEqual("orderId", Integer.parseInt(map.get("orderIds")[0].toString()));
        }
        if (map.get("conditionQuery") != null) {
            search.addFilterEqual(map.get("conditionQuery")[0].toString().equals("orderNo") ? "order.orderNo" : "order.platformOrderNo", map.get("conditionValue")[0].toString());
        }
        if (map.get("orderItemId") != null) {
            search.addFilterEqual("id", Integer.parseInt(map.get("orderItemId")[0].toString()));
        }
        search.addFilterEqual("valid", true).addSortDesc("actualFee");
    }

//         public static Order getOrderByRows(Row row){
//           Order order=new Order();
//          order.setPlatformOrderNo(row.getCell(0)+"");
//          order.setPlatformType(row.getCell(1));
//
//         }


    /**
     * 颜色;规格;尺寸;重量
     *
     * @param product
     * @return
     */
    public static String getSpecInfo(Product product) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isBlank(product.getColor())) {
            sb.append(product.getColor()).append(";");
        }
        if (!StringUtils.isBlank(product.getSpeci())) {
            sb.append(product.getSpeci()).append(";");
        }
        if (!StringUtils.isBlank(product.getBoxSize())) {
            sb.append(product.getBoxSize()).append(";");
        }
        if (!StringUtils.isBlank(product.getWeight())) {
            sb.append(product.getWeight()).append(";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


    /**
     * 拼接OrderItemVo
     *
     * @param orderItemVo 订单项VO实体
     * @param orderItem   订单项VO实体
     */

    public static FinancialOrderItemVo getFinancialOrderItemVo(FinancialOrderItemVo orderItemVo, OrderItem orderItem) {

        orderItemVo.setPlatformType(orderItem.getPlatformType());
        orderItemVo.setPostFee(orderItem.getInvoicePostFee());
        orderItemVo.setOuterSku(orderItem.getOuterSku());
        orderItemVo.setActualFee(orderItem.getActualFee());
        orderItemVo.setSpecInfo(orderItem.getSpecInfo());
        orderItemVo.setId(orderItem.getId());
        orderItemVo.setPlatformSubOrderNo(orderItem.getPlatformSubOrderNo());
        orderItemVo.setType(orderItem.getType().getValue());
        orderItemVo.setStatus(orderItem.getStatus().toString());
        orderItemVo.setReturnStatus(orderItem.getReturnStatus());
        orderItemVo.setOfflineReturnStatus(orderItem.getOfflineReturnStatus());
        orderItemVo.setProductCode(orderItem.getProductCode());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductSku(orderItem.getProductSku());
        orderItemVo.setExchanged(orderItem.getExchanged());
        Product product = orderItem.getProduct();
        if (product != null) {
            Brand brand = product.getBrand();
            orderItemVo.setColor(orderItem.getProduct().getColor());
            ProductCategory category = product.getCategory();
            orderItemVo.setBrandName(brand != null ? brand.getName() : null);
            orderItemVo.setCateName(category != null ? category.getName() : null);
        }
        orderItemVo.setRepoNum(orderItem.getRepoNum().toString());
        orderItemVo.setGoodsFee(orderItem.getGoodsFee());
        orderItemVo.setPrice(orderItem.getPrice());
        orderItemVo.setBuyCount(orderItem.getBuyCount());
        orderItemVo.setDiscountPrice(orderItem.getDiscountPrice());
        orderItemVo.setDiscountFee(orderItem.getDiscountFee());
        orderItemVo.setSharedDiscountFee(orderItem.getSharedDiscountFee());
        orderItemVo.setSharedPostFee(orderItem.getSharedPostFee());
        orderItemVo.setUserActualPrice(orderItem.getUserActualPrice());
        orderItemVo.setPostCoverFee(orderItem.getPostCoverFee());
        orderItemVo.setPostCoverRefundFee(orderItem.getPostCoverRefundFee());
        orderItemVo.setServiceCoverFee(orderItem.getServiceCoverFee());
        orderItemVo.setServiceCoverRefundFee(orderItem.getServiceCoverRefundFee());
        orderItemVo.setRefundFee(orderItem.getRefundFee());
        orderItemVo.setActualRefundFee(orderItem.getActualRefundFee());
        orderItemVo.setOfflineRefundFee(orderItem.getOfflineRefundFee());
        orderItemVo.setReturnPostFee(orderItem.getReturnPostFee());
        orderItemVo.setReturnPostPayer(orderItem.getReturnPostPayer());
        orderItemVo.setOfflineReturnPostFee(orderItem.getOfflineReturnPostFee());
        orderItemVo.setOfflineReturnPostPayer(orderItem.getOfflineReturnPostPayer());
        orderItemVo.setExchangePostFee(orderItem.getExchangePostFee());
        orderItemVo.setExchangePostPayer(orderItem.getExchangePostPayer());
        orderItemVo.setPriceDescription(orderItem.getPriceDescription());
        if (orderItem.getExchangeSourceId() != null) {
            orderItemVo.setExchangeSourceId(orderItem.getExchangeSourceId());
        }
        return orderItemVo;
    }
}
