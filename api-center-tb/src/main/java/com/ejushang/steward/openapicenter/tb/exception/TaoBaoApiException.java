package com.ejushang.steward.openapicenter.tb.exception;

/**
 * User: Baron.Zhang
 * Date: 14-1-8
 * Time: 下午2:57
 */
public class TaoBaoApiException extends Exception {

    public TaoBaoApiException(String msg, Exception e){
        super(msg,e);
    }

    public TaoBaoApiException(String msg){
        super(msg);
    }

    public TaoBaoApiException(Exception e){
        super(e);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
