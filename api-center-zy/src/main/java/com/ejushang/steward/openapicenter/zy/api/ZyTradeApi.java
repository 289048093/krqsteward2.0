package com.ejushang.steward.openapicenter.zy.api;

import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.zy.api.aceess.DefaultZiYouClient;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.TradeGetRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.TradesQueryRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.TradeGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.TradesQueryResponse;
import com.ejushang.steward.openapicenter.zy.constant.ConstantZiYou;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/8/4
 * Time: 14:50
 */
public class ZyTradeApi {

    private DefaultZiYouClient client;

    private static final String ORDER_GET_URL = "/order/get";

    public ZyTradeApi(){
        client = new DefaultZiYouClient(ConstantZiYou.ZY_API_URL,ConstantZiYou.ZY_APP_KEY,ConstantZiYou.ZY_APP_SECRET,ConstantZiYou.YYL_ACCESS_TOKEN);
    }

    /**
     * 获取订单信息
     * @param argsMap
     * @return
     * @throws Exception
     */
    public String orderGet2(Map<String,Object> argsMap) throws Exception {
        return client.execute(ORDER_GET_URL,JsonUtil.object2Json(argsMap));
    }

    /**
     * 获取订单信息
     * @param argsMap
     * @return
     * @throws Exception
     */
    public TradeGetResponse tradeGet(Map<String, Object> argsMap) throws Exception {
        TradeGetRequest request = new TradeGetRequest();
        ReflectUtils.executeMethods(request, argsMap);
        return client.excute(request, ConstantZiYou.YYL_ACCESS_TOKEN);
    }

    /**
     * 查询买家已卖出的交易数据
     * @param argsMap
     * @return
     * @throws Exception
     */
    public TradesQueryResponse tradesQuery(Map<String,Object> argsMap) throws Exception{
        TradesQueryRequest request=new TradesQueryRequest();
        ReflectUtils.executeMethods(request,argsMap);
        return client.excute(request,ConstantZiYou.YYL_ACCESS_TOKEN);
    }
}
