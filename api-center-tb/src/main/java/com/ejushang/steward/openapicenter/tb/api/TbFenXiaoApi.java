package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.FenxiaoOrdersGetRequest;
import com.taobao.api.response.FenxiaoOrdersGetResponse;

import java.util.Map;

/**
 * 淘宝分销api
 * User: Baron.Zhang
 * Date: 14-4-24
 * Time: 上午11:21
 */
public class TbFenXiaoApi {
    private TaobaoClient client;
    private String sessionKey;
    public TbFenXiaoApi(String sessionKey){
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    /**
     * taobao.fenxiao.orders.get 查询采购单信息 <br/>
     * 分销商或供应商均可用此接口查询采购单信息（代销）； (发货处理请调用物流API中的发货接口)。<br/>
     * @param argsMap
     * @return
     * @throws ApiException
     */
    public FenxiaoOrdersGetResponse fenxiaoOrdersGet(Map<String,Object> argsMap) throws Exception {
        FenxiaoOrdersGetRequest req=new FenxiaoOrdersGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        FenxiaoOrdersGetResponse response = client.execute(req , sessionKey);
        return response;
    }
}
