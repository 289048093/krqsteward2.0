package com.ejushang.steward.openapicenter.zy.api.aceess.parser;

import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 14:02
 */
public interface ZiYouParser<T extends ZiYouResponse>  {

    /**
     * 把响应字符串解释成相应的领域对象。
     *
     * @param rsp 响应字符串
     * @return 领域对象
     */
    public T parse(String rsp) throws ApiException;

    /**
     * 获取响应类类型。
     */
    public Class<T> getResponseClass() throws ApiException;
}
