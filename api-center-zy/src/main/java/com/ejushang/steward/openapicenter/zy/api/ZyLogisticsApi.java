package com.ejushang.steward.openapicenter.zy.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.zy.api.aceess.DefaultZiYouClient;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.LogisticsSendRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.LogisticsSendResponse;
import com.ejushang.steward.openapicenter.zy.constant.ConstantZiYou;

import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-12
 * Time: 上午11:55
 */
public class ZyLogisticsApi {
    private DefaultZiYouClient client;
    public ZyLogisticsApi(){
        client = new DefaultZiYouClient(ConstantZiYou.ZY_API_URL,ConstantZiYou.ZY_APP_KEY,ConstantZiYou.ZY_APP_SECRET,ConstantZiYou.YYL_ACCESS_TOKEN);
    }

    public LogisticsSendResponse logisticsSend(Map<String,Object> argsMap) throws Exception{
        LogisticsSendRequest request=new LogisticsSendRequest();
        ReflectUtils.executeMethods(request,argsMap);
        return client.excute(request,ConstantZiYou.YYL_ACCESS_TOKEN);
    }
}
