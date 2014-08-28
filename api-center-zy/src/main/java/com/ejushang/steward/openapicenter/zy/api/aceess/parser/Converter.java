package com.ejushang.steward.openapicenter.zy.api.aceess.parser;

import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;

/**
 * 动态格式转换器
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 14:05
 */
public interface Converter {

    /**
     * 将字符串转换为响应对象
     * @param rsp 响应字符串
     * @param clazz 领域类型
     * @param <T> 领域泛型
     * @return
     * @throws ApiException
     */
    public <T extends ZiYouResponse> T toResponse(String rsp, Class<T> clazz) throws ApiException;
}
