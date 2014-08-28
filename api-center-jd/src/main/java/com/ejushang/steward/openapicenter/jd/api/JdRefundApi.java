package com.ejushang.steward.openapicenter.jd.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.service.PopAfsRefundapplyQuerybyidRequest;
import com.jd.open.api.sdk.request.service.PopAfsRefundapplyQuerylistRequest;
import com.jd.open.api.sdk.response.service.PopAfsRefundapplyQuerybyidResponse;
import com.jd.open.api.sdk.response.service.PopAfsRefundapplyQuerylistResponse;

import java.util.Map;

/**
 * 京东售后api
 * User: Baron.Zhang
 * Date: 2014/5/7
 * Time: 11:56
 */
public class JdRefundApi {
    private JdClient client;

    public JdRefundApi(String sessionKey){
        client = new DefaultJdClient(ConstantJingDong.JD_API_URL,sessionKey,ConstantJingDong.JD_APP_KEY,
                ConstantJingDong.JD_APP_SECRET);
    }

    /**
     * 售前退款：（对应：商家后台--售后客服--退款管理 ）<br/>
     * 商家通过查询条件查询退款审核单列表<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public PopAfsRefundapplyQuerylistResponse popAfsRefundapplyQuerylist(Map<String,Object> argsMap) throws Exception {
        PopAfsRefundapplyQuerylistRequest request = new PopAfsRefundapplyQuerylistRequest();
        ReflectUtils.executeMethods(request, argsMap);
        PopAfsRefundapplyQuerylistResponse response = client.execute(request);
        return response;
    }

    /**
     * 售前退款：（对应：商家后台--售后客服--退款管理 ）<br/>
     * 根据id查看退款审核详情<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public PopAfsRefundapplyQuerybyidResponse popAfsRefundapplyQuerybyid(Map<String,Object> argsMap) throws Exception {
        PopAfsRefundapplyQuerybyidRequest request = new PopAfsRefundapplyQuerybyidRequest();
        ReflectUtils.executeMethods(request,argsMap);
        PopAfsRefundapplyQuerybyidResponse response = client.execute(request);
        return response;
    }


}
