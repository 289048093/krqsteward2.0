package com.ejushang.steward.openapicenter.zy.api.aceess.response;


import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

import java.io.Serializable;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 11:18
 */
public abstract class ZiYouResponse implements Serializable{

    private static final long serialVersionUID = -3706703937927466199L;

    @ApiField("code")
    private String errorCode;

    @ApiField("msg")
    private String msg;

    @ApiField("sub_code")
    private String subCode;

    @ApiField("sub_msg")
    private String subMsg;

    private String body;
    private Map<String, String> params;

    @ApiField("top_forbidden_fields")
    private String topForbiddenFields;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return this.subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return this.subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isSuccess() {
        return this.errorCode == null && this.subCode == null;
    }

    public String getTopForbiddenFields() {
        return topForbiddenFields;
    }

    public void setTopForbiddenFields(String topForbiddenFields) {
        this.topForbiddenFields = topForbiddenFields;
    }
}
