package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.util.TaoBaoLogUtil;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.TradeRate;
import com.taobao.api.request.TmallTraderateFeedsGetRequest;
import com.taobao.api.request.TraderatesGetRequest;
import com.taobao.api.response.TmallTraderateFeedsGetResponse;
import com.taobao.api.response.TraderatesGetResponse;

import java.util.List;
import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-22
 * Time: 上午10:55
 */
public class TraderateApi {
    private TaobaoClient client;
    private String sessionKey;
    public TraderateApi(String sessionKey){
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    public TraderatesGetResponse tradeRatesGet(Map<String,Object> argsMap) throws Exception{
        TraderatesGetRequest request=new TraderatesGetRequest();
        ReflectUtils.executeMethods(request,argsMap);
        TraderatesGetResponse response=client.execute(request , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response;
    }

    public TmallTraderateFeedsGetResponse feedsGet(Map<String,Object> argsMap) throws Exception{
        TmallTraderateFeedsGetRequest request=new TmallTraderateFeedsGetRequest();
        ReflectUtils.executeMethods(request,argsMap);
        TmallTraderateFeedsGetResponse response = client.execute(request , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response;
    }
}
