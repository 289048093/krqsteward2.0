package com.ejushang.steward.openapicenter.zy.api.aceess.parser;

import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;

/**
 * User: Baron.Zhang
 * Date: 2014/8/7
 * Time: 14:40
 */
public class ObjectXmlParser<T extends ZiYouResponse> implements ZiYouParser<T> {

    private Class<T> clazz;

    public ObjectXmlParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T parse(String rsp) throws ApiException {
        Converter converter = new XmlConverter();
        return converter.toResponse(rsp, clazz);
    }

    @Override
    public Class<T> getResponseClass() throws ApiException {
        return clazz;
    }
}
