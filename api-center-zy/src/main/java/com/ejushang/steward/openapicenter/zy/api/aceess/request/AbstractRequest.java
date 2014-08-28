package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.datastructure.ZiYouHashMap;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-22
 * Time: 下午2:24
 */
public abstract class AbstractRequest<T extends ZiYouResponse> implements ZiYouRequest<T> {
    protected Class<T> clazz;

    protected Long timestamp;

    protected Map<String, String> headerMap;

    protected Map<String, String> udfParams;

    @Override
    public Class<T> getResponseClass() {
        if(clazz!=null)return clazz;
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        if (parameterizedType == null) {
            throw new IllegalArgumentException("Request 没有指定泛型");
        }
        return  clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Map<String, String> getHeaderMap() {
        return this.headerMap;
    }

    @Override
    public void putOtherTextParam(String key, String value) {
        if (udfParams == null) {
            udfParams = new ZiYouHashMap();
        }
        udfParams.put(key, value);
    }
}
