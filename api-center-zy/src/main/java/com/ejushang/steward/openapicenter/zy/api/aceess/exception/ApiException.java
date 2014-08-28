package com.ejushang.steward.openapicenter.zy.api.aceess.exception;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 11:32
 */
public class ApiException extends Exception {
    private static final long serialVersionUID = -5460343391472891863L;

    private String errCode;
    private String errMsg;

    public ApiException() {
        super();
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }
}
