package com.ejushang.steward.openapicenter.jd.api;

import com.ejushang.steward.common.util.EjsMapUtils;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.jd.open.api.sdk.domain.order.OrderResult;
import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.jd.open.api.sdk.response.order.OrderGetResponse;
import com.jd.open.api.sdk.response.order.OrderSearchResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 14-4-14
 * Time: 下午8:08
 */
public class JdOrderApiTest {

    private String sessionKey = "278c4dc3-dd12-4e77-b2d6-768cf4051a69";

    @Test
    public void testOrderGet() throws Exception {
        JdOrderApi orderApi = new JdOrderApi(sessionKey);

        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put("order_id","1656766345");
        OrderGetResponse orderGetResponse = orderApi.orderGet(argsMap);
        //1656766345

        System.out.println(orderGetResponse.getMsg());
    }

    @Test
    public void testOrderGetBySelf() throws Exception {
        JdOrderApi orderApi = new JdOrderApi(sessionKey);
        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put("order_id","1660348047");
        String responseJson = orderApi.orderGetBySelf(argsMap,sessionKey);

        Map<String,Object> map = JsonUtil.json2Object(responseJson,HashMap.class);

        System.out.println(map);
        System.out.println(EjsMapUtils.findSpec(map,"code"));
        System.out.println(EjsMapUtils.findSpec(map,"order_state"));
        //System.out.println(((Map)((Map)(((Map)map.get("order_get_response")).get("order"))).get("orderInfo")).get("order_state"));
    }

    @Test
    public void testOrderSearch() throws Exception {


        //System.out.println(PlatformType.JING_DONG.toString());
        // 从京东平台获取订单信息
        Map<String,Object> argsMap = new HashMap<String, Object>();
        // 订单抓取开始时间
        argsMap.put(ConstantJingDong.START_DATE,"2014-08-06 00:00:00");
        // 订单抓取结束时间
        argsMap.put(ConstantJingDong.END_DATE,"2014-08-06 16:00:00");
        // 查询时间类型
        argsMap.put(ConstantJingDong.DATETYPE,ConstantJingDong.JD_DATE_TYPE);
        // 查询订单类型
        argsMap.put(ConstantJingDong.ORDER_STATE, "WAIT_SELLER_STOCK_OUT,WAIT_GOODS_RECEIVE_CONFIRM,FINISHED_L");
        // 查询第几页
        argsMap.put(ConstantJingDong.PAGE,"1");
        // 查询的每页最大条数
        argsMap.put(ConstantJingDong.PAGE_SIZE,ConstantJingDong.JD_FETCH_ORDER_PAGE_SIZE);

        JdOrderApi orderApi = new JdOrderApi("ed3466ac-c76d-4e7f-843d-7a854f289bbe");
        OrderSearchResponse response = orderApi.orderSearch(argsMap);

        OrderResult orderResult = response.getOrderInfoResult();
        String code = response.getCode();

        // 创建保存OrderSearchInfo的集合
        List<OrderSearchInfo> allOrderSearchInfoList = new ArrayList<OrderSearchInfo>();
        if(StringUtils.equals(code,"0") && orderResult != null){
            int totalCount = orderResult.getOrderTotal();
            int pageSize = Integer.valueOf(ConstantJingDong.JD_FETCH_ORDER_PAGE_SIZE);
            int totalPage = totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;
            for(;totalPage > 0; totalPage--){
                argsMap.put(ConstantJingDong.PAGE,String.valueOf(totalPage));
                response = orderApi.orderSearch(argsMap);
                orderResult = response.getOrderInfoResult();
                code = response.getCode();
                if(StringUtils.equals(code,"0") && orderResult != null){
                    if(CollectionUtils.isNotEmpty(orderResult.getOrderInfoList())){
                        allOrderSearchInfoList.addAll(orderResult.getOrderInfoList());
                    }
                }
            }
        }

        System.out.println("");



    }

    @Test
    public void test(){
        int a = Integer.valueOf("-90");
        System.out.println(a);
    }

    @Test
    public void test2(){
        StringBuilder stringBuilder = new StringBuilder(null);
        System.out.println(stringBuilder);
    }
}
