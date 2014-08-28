package com.ejushang.steward.common.exception;

/**
 * ERP项目业务异常
 * User: liubin
 * Date: 14-1-10
 */
public class StewardBusinessException extends RuntimeException {

    public StewardBusinessException() {
        super();
    }

    public StewardBusinessException(String message) {
        super(message);
    }

    public StewardBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public StewardBusinessException(Throwable cause) {
        super(cause);
    }

}
