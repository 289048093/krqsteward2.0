package com.ejushang.steward.openapicenter.zy.exception;

/**
 * User: Shiro
 * Date: 14-8-13
 * Time: 上午10:09
 */
public class ZiYouApiException extends Exception {
    public ZiYouApiException(String msg, Exception e){
        super(msg,e);
    }

    public ZiYouApiException(String msg){
        super(msg);
    }

    public ZiYouApiException(Exception e){
        super(e);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
