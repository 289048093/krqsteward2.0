package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TmallEaiOrderRefundGetRequest;
import com.taobao.api.request.TmallEaiOrderRefundGoodReturnGetRequest;
import com.taobao.api.request.TmallEaiOrderRefundGoodReturnMgetRequest;
import com.taobao.api.request.TmallEaiOrderRefundMgetRequest;
import com.taobao.api.response.TmallEaiOrderRefundGetResponse;
import com.taobao.api.response.TmallEaiOrderRefundGoodReturnGetResponse;
import com.taobao.api.response.TmallEaiOrderRefundGoodReturnMgetResponse;
import com.taobao.api.response.TmallEaiOrderRefundMgetResponse;

import java.util.Map;

/**
 * 淘宝退款API<br/>
 * 提供了卖家接收退款，退款留言等功能<br/>
 * User: Baron.Zhang
 * Date: 13-12-13
 * Time: 下午2:23
 */
public class TbRefundApi {

    private TaobaoClient client;
    private String sessionKey;

    public TbRefundApi(String sessionKey){
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    /**
     * tmall.eai.order.refund.mget 批量退款单查询
     * @param argsMap
     * @return
     * @throws Exception
     */
    public TmallEaiOrderRefundMgetResponse tmallEaiOrderRefundMget(Map<String,Object> argsMap) throws Exception {
        TmallEaiOrderRefundMgetRequest request = new TmallEaiOrderRefundMgetRequest();
        ReflectUtils.executeMethods(request, argsMap);
        TmallEaiOrderRefundMgetResponse response = client.execute(request,sessionKey);
        return  response;
    }

    /**
     * tmall.eai.order.refund.get 单笔退款单查询
     * @param argsMap
     * @return
     * @throws Exception
     */
    public TmallEaiOrderRefundGetResponse tmallEaiOrderRefundGet(Map<String,Object> argsMap) throws Exception {
        TmallEaiOrderRefundGetRequest request = new TmallEaiOrderRefundGetRequest();
        ReflectUtils.executeMethods(request,argsMap);
        TmallEaiOrderRefundGetResponse response = client.execute(request,sessionKey);
        return response;
    }

    /**
     * tmall.eai.order.refund.good.return.mget 批量退货单查询
     * @param argsMap
     * @return
     * @throws Exception
     */
    public TmallEaiOrderRefundGoodReturnMgetResponse tmallEaiOrderRefundGoodReturnMget(Map<String,Object> argsMap) throws Exception {
        TmallEaiOrderRefundGoodReturnMgetRequest request = new TmallEaiOrderRefundGoodReturnMgetRequest();
        ReflectUtils.executeMethods(request,argsMap);
        TmallEaiOrderRefundGoodReturnMgetResponse response = client.execute(request,sessionKey);
        return response;
    }

    /**
     * tmall.eai.order.refund.good.return.get 单笔退货单查询
     * @param argsMap
     * @return
     * @throws Exception
     */
    public TmallEaiOrderRefundGoodReturnGetResponse tmallEaiOrderRefundGoodReturnGet(Map<String,Object> argsMap) throws Exception {
        TmallEaiOrderRefundGoodReturnGetRequest request = new TmallEaiOrderRefundGoodReturnGetRequest();
        ReflectUtils.executeMethods(request,argsMap);
        TmallEaiOrderRefundGoodReturnGetResponse response = client.execute(request,sessionKey);
        return response;
    }


}
