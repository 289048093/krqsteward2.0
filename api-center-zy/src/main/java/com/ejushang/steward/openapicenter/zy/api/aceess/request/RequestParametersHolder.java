package com.ejushang.steward.openapicenter.zy.api.aceess.request;


import com.ejushang.steward.openapicenter.zy.api.aceess.datastructure.ZiYouHashMap;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/8/7
 * Time: 15:54
 */
public class RequestParametersHolder {
    private ZiYouHashMap protocalMustParams;
    private ZiYouHashMap protocalOptParams;
    private ZiYouHashMap applicationParams;

    public ZiYouHashMap getProtocalMustParams() {
        return protocalMustParams;
    }

    public void setProtocalMustParams(ZiYouHashMap protocalMustParams) {
        this.protocalMustParams = protocalMustParams;
    }

    public ZiYouHashMap getProtocalOptParams() {
        return protocalOptParams;
    }

    public void setProtocalOptParams(ZiYouHashMap protocalOptParams) {
        this.protocalOptParams = protocalOptParams;
    }

    public ZiYouHashMap getApplicationParams() {
        return applicationParams;
    }

    public void setApplicationParams(ZiYouHashMap applicationParams) {
        this.applicationParams = applicationParams;
    }

    public Map<String, String> getAllParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (protocalMustParams != null && !protocalMustParams.isEmpty()) {
            params.putAll(protocalMustParams);
        }
        if (protocalOptParams != null && !protocalOptParams.isEmpty()) {
            params.putAll(protocalOptParams);
        }
        if (applicationParams != null && !applicationParams.isEmpty()) {
            params.putAll(applicationParams);
        }
        return params;
    }
}
