package com.ejushang.steward.openapicenter.zy.api.aceess.util;

import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 15:17
 */
public class MapUtils {
    public static Object findSpec(Map<String,Object> responseMap ,String parentKey,String findKey){
        if(responseMap == null){
            return null;
        }
        Map<String,Object> parentMap = null;
        if(parentKey == null){
            parentMap = responseMap;
        }
        else{
            parentMap = (Map<String, Object>) responseMap.get(parentKey);
        }

        for(Map.Entry<String,Object> entry : parentMap.entrySet()){
            if(findKey != null && findKey.equalsIgnoreCase(entry.getKey())){
                return entry.getValue();
            }

            if(entry.getValue() instanceof Map){
                return findSpec((Map<String,Object>)entry.getValue(),null,findKey);
            }
        }

        return null;
    }

    public static Object findSpec(Map<String,Object> responseMap,String findKey){
        return findSpec(responseMap,null,findKey);
    }
}
