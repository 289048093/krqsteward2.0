package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.util.TaoBaoLogUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.User;
import com.taobao.api.request.UserBuyerGetRequest;
import com.taobao.api.request.UserSellerGetRequest;
import com.taobao.api.response.UserBuyerGetResponse;
import com.taobao.api.response.UserSellerGetResponse;

import java.util.Map;

/**
 * 淘宝用户API<br/>
 * 提供了用户基本信息查询功能<br/>
 * User: Baron.Zhang
 * Date: 13-12-13
 * Time: 下午2:23
 */
public class TbUserApi {
    private TaobaoClient client;
    private String sessionKey;
    public TbUserApi(String sessionKey){
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    /**
     * taobao.user.buyer.get 查询买家信息API<br/>
     * @param fields 只返回nick, avatar参数
     * @return
     */
    public User getBuyer(String fields) throws ApiException {
        UserBuyerGetRequest req=new UserBuyerGetRequest();
        req.setFields(fields);
        UserBuyerGetResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return response.getUser();
    }

    /**
     * taobao.user.seller.get 查询卖家用户信息<br/>
     * @return
     */
    public UserSellerGetResponse userSellerGet(Map<String,Object> argsMap) throws Exception {
        UserSellerGetRequest req= new UserSellerGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        UserSellerGetResponse response = client.execute(req , sessionKey);
		return response;
    }
}
