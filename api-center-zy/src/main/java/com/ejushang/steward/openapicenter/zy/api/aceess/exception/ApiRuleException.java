package com.ejushang.steward.openapicenter.zy.api.aceess.exception;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 11:34
 */
public class ApiRuleException extends ApiException {

    private static final long serialVersionUID = -4491346115254359010L;

    public ApiRuleException(String errCode, String errMsg) {
        super(errCode, errMsg);
    }
}
