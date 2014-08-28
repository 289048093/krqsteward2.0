package com.ejushang.steward.openapicenter.zy.api;

import com.ejushang.steward.common.util.EjsMapUtils;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.TradeGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.TradesQueryResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/8/4
 * Time: 16:48
 */
public class ZyOrderApiTest {

    @Test
    public void testOrderGet2() throws Exception {

        ZyTradeApi orderApi = new ZyTradeApi();

        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put("tid","123456");

        String con = orderApi.orderGet2(argsMap);

        System.out.println(con);

        Map<String,Object> responseMap = JsonUtil.json2Object(con,HashMap.class);

        System.out.println(responseMap);

        System.out.println(EjsMapUtils.findSpec(JsonUtil.json2Object(String.valueOf(EjsMapUtils.findSpec(responseMap,"param_json")),HashMap.class),"tid"));
    }

    @Test
    public void testOrderGet() throws Exception {

        ZyTradeApi orderApi = new ZyTradeApi();

        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put("tid","12345678");

        TradeGetResponse response = orderApi.tradeGet(argsMap);

        System.out.println(response.getBody());
    }

    @Test
    public void testOrderQuery() throws Exception{
          ZyTradeApi orderApi=new ZyTradeApi();
        Map<String,Object> argsMap = new HashMap<String, Object>();
        TradesQueryResponse response=orderApi.tradesQuery(argsMap);
        System.out.println(response.getBody());
    }

}
