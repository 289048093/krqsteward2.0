package com.ejushang.steward.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/7/21
 * Time: 16:23
 */
public class EjsMapUtils {

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
            if(StringUtils.equalsIgnoreCase(findKey,entry.getKey())){
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
