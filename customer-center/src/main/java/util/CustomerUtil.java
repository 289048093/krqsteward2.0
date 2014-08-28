package util;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.customercenter.constant.CommentResult;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User:moon
 * Date: 14-8-6
 * Time: 上午9:40
 */
public class CustomerUtil {

    private CustomerUtil(){

    }

    /**
     * 获取customer的参数的Map对象
     *
     * @param map  传过来的参数
     * @param map1 解析后的参数
     */
    public static void getConditionMap(Map<String, Object[]> map, Map<String, String> map1) {
        for (Map.Entry<String, Object[]> entry : map.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue()[0];
            map1.put(key, val + "");
        }
    }

    /**
     * 拼接会员HQL
     *
     * @param map           controller接收的map参数
     * @param stringBuilder 拼接HQL变量
     * @param objects       添加的变量参数List集合
     * @throws java.text.ParseException
     */
    public static void customerCondition(Map<String, String> map, StringBuilder stringBuilder, List<Object> objects)  {
        stringBuilder.append("select c from Customer c left outer join fetch c.mobiles mobiles where 1=1  ");

        //平台类型的判断
        if (!StringUtils.isBlank(map.get("platformType")) && !map.get("platformType").equals("null")) {
            stringBuilder.append(" and c.id in (select p.customerId from PlatformUser p where p.platformType=?) ");
            objects.add(PlatformType.valueOf(map.get("platformType")));
        }
        //会员名的判断   模糊查询
        if (!StringUtils.isBlank(map.get("buyerId")) && !map.get("buyerId").equals("null")) {
            stringBuilder.append(" and c.id in (select p.customerId from PlatformUser p where p.buyerId like ?) ");
            objects.add("%"+map.get("buyerId")+"%");
        }
        //根据地址      模糊查询
        if (!StringUtils.isBlank(map.get("address")) && !map.get("address").equals("null")) {
            stringBuilder.append(" and c.address like ? ");
            objects.add("%" + map.get("address") + "%");
        }
        //标签的判断
        if (!StringUtils.isBlank(map.get("tags")) && !map.get("tags").equals("null")) {
            stringBuilder.append(" and c.id in (select cct.customerId from CustomerCustomerTag cct where cct.tagId=?) ");
            objects.add(Integer.parseInt(map.get("tags")));
        }
        //店铺的判断
        if (!StringUtils.isBlank(map.get("shopId")) && !map.get("shopId").equals("null")) {
            stringBuilder.append(" and c.id in (select cs.customerId from CustomerShop cs where cs.shopId=?) ");
            objects.add(Integer.valueOf(map.get("shopId")));
        }
        //根据商品编号
        if (!StringUtils.isBlank(map.get("prodNo")) && !map.get("prodNo").equals("null")) {
            stringBuilder.append(" and mobiles.mobile in " +
                    "(select oi.order.invoice.receiver.receiverMobile from OrderItem oi where oi.productCode=?) ");
            objects.add(map.get("prodNo"));
        }
        //根据sku
        if (!StringUtils.isBlank(map.get("sku")) && !map.get("sku").equals("null")) {
            stringBuilder.append(" and mobiles.mobile  in " +
                    "(select oi.order.invoice.receiver.receiverMobile from OrderItem oi where oi.productSku=?) ");
            objects.add(map.get("sku"));
        }
        //等级
        if (!StringUtils.isBlank(map.get("grade")) && !map.get("grade").equals("null")) {
            stringBuilder.append(" and c.grade= ? ");
            objects.add(Integer.parseInt(map.get("grade")));
        }
        //交易次数
        if (!StringUtils.isBlank(map.get("tradeCount")) && !map.get("tradeCount").equals("null")) {
            stringBuilder.append(" and c.tradeCount= ? ");
            objects.add(Integer.parseInt(map.get("tradeCount")));
        }
        //最近交易时间
        if (!StringUtils.isBlank(map.get("startDate")) && !map.get("startDate").equals("null")) {
            stringBuilder.append(" and c.lastTradeTime >= ? ");
            objects.add(EJSDateUtils.parseDate(map.get("startDate"),EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        if (!StringUtils.isBlank(map.get("endDate")) && !map.get("endDate").equals("null")) {
            stringBuilder.append(" and c.lastTradeTime <= ? ");
            objects.add(EJSDateUtils.parseDate(map.get("endDate"),EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        //生日                                                  SIMPLE_DATE_FORMAT_STR
        if (!StringUtils.isBlank(map.get("birthdayStartDate")) && !map.get("birthdayStartDate").equals("null")) {
            stringBuilder.append(" and c.birthday >= ? ");
            objects.add(EJSDateUtils.parseDate(map.get("birthdayStartDate"),EJSDateUtils.DateFormatType.SIMPLE_DATE_FORMAT_STR));
        }
        if (!StringUtils.isBlank(map.get("birthdayEndDate")) && !map.get("birthdayEndDate").equals("null")) {
            stringBuilder.append(" and c.birthday <= ? ");
            objects.add(EJSDateUtils.parseDate(map.get("birthdayEndDate"),EJSDateUtils.DateFormatType.SIMPLE_DATE_FORMAT_STR));
        }
        //累计购买金额
        if (!StringUtils.isBlank(map.get("totalTradeFeeStart")) && !map.get("totalTradeFeeStart").equals("null")) {
            stringBuilder.append(" and c.totalTradeFee >= ? ");
            objects.add(Money.valueOf(map.get("totalTradeFeeStart")));
        }
        if (!StringUtils.isBlank(map.get("totalTradeFeeEnd")) && !map.get("totalTradeFeeEnd").equals("null")) {
            stringBuilder.append(" and c.totalTradeFee <= ? ");
            objects.add(Money.valueOf(map.get("totalTradeFeeEnd")));
        }
        stringBuilder.append(" order by ");
        stringBuilder.append(" c.createTime desc");

    }

    /**
     * 拼接评论HQL
     * @param stringBuilder 拼接HQL变量
     */
    public static void commentCondition(Map<String, String> map, StringBuilder stringBuilder, List<Object> objects)  {
        stringBuilder.append("select c from Comment c where 1=1  ");
        addCommentCondition(map,stringBuilder,objects);
        stringBuilder.append(" order by ");
        stringBuilder.append(" c.createTime desc");
    }

    /**
     * 拼接评论HQL
     * @param stringBuilder 拼接HQL变量
     */
    public static void commentCondition1(StringBuilder stringBuilder)  {
        stringBuilder.append(" select c.id from Comment c where 1=1  ");
    }
    /**
     * 拼接评论HQL
     *
     * @param map           controller接收的map参数
     * @param stringBuilder 拼接HQL变量
     * @param objects       添加的变量参数List集合
     * @throws java.text.ParseException
     */
    public static void addCommentCondition(Map<String, String> map, StringBuilder stringBuilder, List<Object> objects)  {
//        stringBuilder.append("select c from Comment c where 1=1  ");

        //会员名的判断
        if (!StringUtils.isBlank(map.get("buyerId")) && !map.get("buyerId").equals("null")) {
            stringBuilder.append(" and c.buyerId=?) ");
            objects.add(map.get("buyerId"));
        }

        //平台类型的判断
        if (!StringUtils.isBlank(map.get("platformType")) && !map.get("platformType").equals("null")) {
            stringBuilder.append(" and c.platformType=?) ");
            objects.add(PlatformType.valueOf(map.get("platformType")));
        }

        //店铺的判断
        if (!StringUtils.isBlank(map.get("shopId")) && !map.get("shopId").equals("null")) {
            stringBuilder.append(" and c.shopId= ? ");
            objects.add(Integer.valueOf(map.get("shopId")));
        }

        //根据sku
        if (!StringUtils.isBlank(map.get("sku")) && !map.get("sku").equals("null")) {
            stringBuilder.append(" and c.productSku=?) ");
            objects.add(map.get("sku"));
        }
        //评价类型  and c.id in (select p.customerId from PlatformUser p where platformType=?)
        if (!StringUtils.isBlank(map.get("categoryId")) && !map.get("categoryId").equals("null")) {
            stringBuilder.append(" and c.id in (select cc.commentId from CommentCommentCategory cc where cc.categoryId=? ) ");
            objects.add(Integer.parseInt(map.get("categoryId")));
        }
        //评价结果
        if (!StringUtils.isBlank(map.get("result")) && !map.get("result").equals("null")) {
            stringBuilder.append(" and c.result= ? ");
            objects.add(CommentResult.valueOf(map.get("result")));
        }
        //是否已归类
        if (!StringUtils.isBlank(map.get("isCategory")) && !map.get("isCategory").equals("null")) {
            stringBuilder.append(" and c.category = ? ");
            objects.add(Boolean.valueOf(map.get("isCategory")));
        }
        //评价时间
        if (!StringUtils.isBlank(map.get("commentTimeStart")) && !map.get("commentTimeStart").equals("null")) {
            stringBuilder.append(" and c.id in (select cc.commentId from CommentContent cc where cc.commentTime >= ? ) ");
            objects.add(EJSDateUtils.parseDate(map.get("commentTimeStart"),EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        if (!StringUtils.isBlank(map.get("commentTimeEnd")) && !map.get("commentTimeEnd").equals("null")) {
            stringBuilder.append(" and c.id in (select cc.commentId from CommentContent cc where cc.commentTime <= ? ) ");
            objects.add(EJSDateUtils.parseDate(map.get("commentTimeEnd"),EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        //评价内容
        if (!StringUtils.isBlank(map.get("content")) && !map.get("content").equals("null")) {
            stringBuilder.append(" and c.id in (select cc.commentId from CommentContent cc where cc.content like ? ) ");
            objects.add("%"+map.get("content")+"%");
        }

//        stringBuilder.append(" order by ");
//        stringBuilder.append(" c.createTime desc");
    }


}
