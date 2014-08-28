package com.ejushang.steward.openapicenter.zy.api.aceess.parser;

import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 15:02
 */
public class ObjectJsonParser<T extends ZiYouResponse> implements ZiYouParser {

    private Class<T> clazz;

    public ObjectJsonParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T parse(String rsp) throws ApiException {
        Converter converter = new JsonConverter();
        return converter.toResponse(rsp, clazz);
    }

    @Override
    public Class getResponseClass() throws ApiException {
        return this.clazz;
    }
}
