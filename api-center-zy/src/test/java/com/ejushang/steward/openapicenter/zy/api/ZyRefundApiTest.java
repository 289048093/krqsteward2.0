package com.ejushang.steward.openapicenter.zy.api;

import com.ejushang.steward.openapicenter.zy.api.aceess.request.RefundGetRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.RefundGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.RefundsQueryResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.TradeGetResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-18
 * Time: 下午5:02
 */
public class ZyRefundApiTest {
    @Test
    public void testRefundGet() throws Exception{
        ZyRefundApi refundApi = new ZyRefundApi();
        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put("refund_id","123123123");
        argsMap.put("refund_phase","onsale");
        RefundGetResponse refundGetResponse= refundApi.refundGet(argsMap);
        System.out.println(refundGetResponse.getBody());

    }

    @Test
    public void testRefundQuery() throws Exception{
         ZyRefundApi refundApi=new ZyRefundApi();
         Map<String,Object> argsMap=new HashMap<String, Object>();
        RefundsQueryResponse refundsQueryResponse=refundApi.refundsQuery(argsMap);
        System.out.print(refundsQueryResponse.getBody());
    }
}
