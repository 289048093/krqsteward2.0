package com.ejushang.steward.openapicenter.jd.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.delivery.DeliveryLogisticsGetRequest;
import com.jd.open.api.sdk.request.delivery.EtmsTraceGetRequest;
import com.jd.open.api.sdk.response.delivery.DeliveryLogisticsGetResponse;
import com.jd.open.api.sdk.response.delivery.EtmsTraceGetResponse;

import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/5/27
 * Time: 14:59
 */
public class JdLogisticsApi {
    private JdClient client;

    public JdLogisticsApi(String sessionKey){
        client = new DefaultJdClient(ConstantJingDong.JD_API_URL,sessionKey,ConstantJingDong.JD_APP_KEY,
                ConstantJingDong.JD_APP_SECRET);
    }

    /**
     * 360buy.delivery.logistics.get<br/>
     * 检索商家物流公司信息（只可获取商家后台已设置的物流公司信息）<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public DeliveryLogisticsGetResponse deliveryLogisticsGet(Map<String,Object> argsMap) throws Exception {
        DeliveryLogisticsGetRequest request = new DeliveryLogisticsGetRequest();
        ReflectUtils.executeMethods(request,argsMap);
        DeliveryLogisticsGetResponse response = client.execute(request);
        return response;
    }

    /**
     * jingdong.etms.trace.get<br/>
     * 京东物流全程跟踪查询接口   相关文档下载：ERP对接方案【11月4日更新】   京东快递可配送区域<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public EtmsTraceGetResponse etmsTraceGet(Map<String,Object> argsMap) throws Exception {
        EtmsTraceGetRequest request = new EtmsTraceGetRequest();
        ReflectUtils.executeMethods(request,argsMap);
        EtmsTraceGetResponse response = client.execute(request);
        return response;
    }

}
