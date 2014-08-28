package com.ejushang.steward.openapicenter.jd.exception;

/**
 * User: Baron.Zhang
 * Date: 14-1-8
 * Time: 下午2:57
 */
public class JingDongApiException extends Exception {

    public JingDongApiException(String msg, Exception e){
        super(msg,e);
    }

    public JingDongApiException(String msg){
        super(msg);
    }

    public JingDongApiException(Exception e){
        super(e);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
