package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.JsonResult;
import com.taobao.api.response.TmallTraderateFeedsGetResponse;
import com.taobao.api.response.TraderatesGetResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-22
 * Time: 下午2:48
 */
public class TbTradeRateApiTest {
    private String sessionKey = "6201e1489617d8d2681cadfhf881e8c72b10ba3fa4c9a541675300784";
    @Test
    public void testTradeRateGet() throws Exception {
        TraderateApi traderateApi = new TraderateApi(sessionKey);
        Map<String,Object> argsMap=new HashMap<String, Object>();
        argsMap.put("fields",getTradeFields());
        argsMap.put("rate_type","get");
        argsMap.put("role","buyer");
        TraderatesGetResponse response=traderateApi.tradeRatesGet(argsMap);
        System.out.println(new JsonResult(true).addList(response.getTradeRates()));
    }
    private String getTradeFields() {
        List<String> fieldList = new ArrayList<String>();
        fieldList.add("tid");
        fieldList.add("oid");
        fieldList.add("role");
        fieldList.add("nick");
        fieldList.add("result");
        fieldList.add("created");
        fieldList.add("rated_nick");
        fieldList.add("item_title");
        fieldList.add("item_price");
        fieldList.add("content");
        fieldList.add("reply");
        fieldList.add("num_iid");
        fieldList.add("valid_score");
        StringBuffer fieldBuffer = new StringBuffer();

        for(int i = 0; i < fieldList.size(); i++){
            if(i == 0){
                fieldBuffer.append(fieldList.get(i));
            }
            else{
                fieldBuffer.append(",").append(fieldList.get(i));
            }
        }

        return fieldBuffer.toString();
    }
    @Test
    public void testFeedsGet() throws Exception {
        TraderateApi traderateApi = new TraderateApi(sessionKey);
        Map<String,Object> argsMap=new HashMap<String, Object>();
        argsMap.put("child_trade_id",775339550764481l);
        TmallTraderateFeedsGetResponse response=traderateApi.feedsGet(argsMap);
        System.out.println(new JsonResult(true).addObject(response.getTmallRateInfo()));
    }
}
