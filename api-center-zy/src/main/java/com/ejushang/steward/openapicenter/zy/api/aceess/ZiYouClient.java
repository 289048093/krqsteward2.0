package com.ejushang.steward.openapicenter.zy.api.aceess;

import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.ZiYouRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;

/**
 * 自有客户端
 * User: Baron.Zhang
 * Date: 2014/8/7
 * Time: 11:40
 */
public interface ZiYouClient {

    String APP_KEY = "app_key";
    String URL = "url";
    String METHOD_NAME = "method_name";
    String ACCESS_TOKEN = "access_token";
    String SIGN = "sign";
    String TIMESTAMP = "timestamp";
    String RANDOM_VAL = "random_val";
    String PARAM_JSON = "param_json";

    /**
     * 执行自有平台API公开请求
     * @param request 具体的请求
     * @param <T> 领域泛型
     * @return
     * @throws ApiException
     */
    public <T extends ZiYouResponse> T excute(ZiYouRequest<T> request) throws ApiException;

    /**
     * 执行自有平台API隐私请求
     * @param request 具体的请求
     * @param session 访问令牌
     * @param <T> 领域泛型
     * @return
     * @throws ApiException
     */
    public <T extends ZiYouResponse> T excute(ZiYouRequest<T> request,String session) throws ApiException;

}
