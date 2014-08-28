package com.ejushang.steward.openapicenter.zy.api.aceess.parser;

import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Json格式转换器
 * <p/>
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 14:48
 */
public class JsonConverter implements Converter {
    @Override
    public <T extends ZiYouResponse> T toResponse(String rsp, Class<T> clazz) throws ApiException {
        JsonReader reader = new JsonValidatingReader(new ExceptionErrorListener());
        Object rootObj = reader.read(rsp);
        if (rootObj instanceof Map<?, ?>) {
            Map<?, ?> rootJson = (Map<?, ?>) rootObj;
            Collection<?> values = rootJson.values();
            for (Object rspObj : values) {
                if (rspObj instanceof Map<?, ?>) {
                    Map<?, ?> rspJson = (Map<?, ?>) rspObj;
                    return fromJson(rspJson, clazz);
                }
            }
        }
        return null;
    }

    /**
     * 把JSON格式的数据转换为对象。
     *
     * @param <T>   泛型领域对象
     * @param json  JSON格式的数据
     * @param clazz 泛型领域类型
     * @return 领域对象
     */
    public <T> T fromJson(final Map<?, ?> json, Class<T> clazz) throws ApiException {
        return Converters.convert(clazz, new Reader() {
            public boolean hasReturnField(Object name) {
                return json.containsKey(name);
            }

            public Object getPrimitiveObject(Object name) {
                return json.get(name);
            }

            public Object getObject(Object name, Class<?> type) throws ApiException {
                Object tmp = json.get(name);
                if (tmp instanceof Map<?, ?>) {
                    Map<?, ?> map = (Map<?, ?>) tmp;
                    return fromJson(map, type);
                } else {
                    return null;
                }
            }

            public List<?> getListObjects(Object listName, Object itemName, Class<?> subType) throws ApiException {
                List<Object> listObjs = null;

                Object listTmp = json.get(listName);
                if (listTmp instanceof Map<?, ?>) {
                    Map<?, ?> jsonMap = (Map<?, ?>) listTmp;
                    Object itemTmp = jsonMap.get(itemName);
                    if (itemTmp == null && listName != null) {
                        String listNameStr = listName.toString();
                        itemTmp = jsonMap.get(listNameStr.substring(0, listNameStr.length() - 1));
                    }
                    if (itemTmp instanceof List<?>) {
                        List<?> tmpList = (List<?>) itemTmp;
                        listObjs = getList(tmpList, subType);
                    }
                }
                if (listTmp instanceof List) {
                    List<?> tmpList = (List<?>) listTmp;
                    listObjs = getList(tmpList, subType);
                }
                return listObjs;
            }

            private List<Object> getList(List<?> tmpList, Class<?> subType) throws ApiException {
                List<Object> listObjs = new ArrayList<Object>();
                for (Object subTmp : tmpList) {
                    if (subTmp instanceof Map<?, ?>) {// object
                        Map<?, ?> subMap = (Map<?, ?>) subTmp;
                        Object subObj = fromJson(subMap, subType);
                        if (subObj != null) {
                            listObjs.add(subObj);
                        }
                    } else if (subTmp instanceof List<?>) {// array
                        // TODO not support yet
                    } else {// boolean, long, double, string, null
                        listObjs.add(subTmp);
                    }
                }
                return listObjs;
            }
        });
    }
}
