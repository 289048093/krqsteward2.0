package com.ejushang.steward.openapicenter.zy.api;

import com.ejushang.steward.openapicenter.zy.api.aceess.request.LogisticsSendRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.LogisticsSendResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-19
 * Time: 上午10:49
 */
public class ZyLogisticsApiTest {
    @Test
    public void testLogisticsSend() throws  Exception{
        ZyLogisticsApi logisticsApi=new ZyLogisticsApi();
        Map<String,Object> argsMap=new HashMap<String, Object>();
        argsMap.put("tid","40079550049089");
        argsMap.put("sid","123456789");
        argsMap.put("company_code","shunfeng");
        LogisticsSendResponse response=logisticsApi.logisticsSend(argsMap);
        System.out.println(response.getBody());
    }

}
