package com.ejushang.steward.ordercenter.service.outplatforminvoke;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-6-3
 * Time: 上午11:06
 */
public class ApiInvokeException extends Exception {
    public ApiInvokeException() {
        super();
    }

    public ApiInvokeException(String message) {
        super(message);
    }

    public ApiInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiInvokeException(Throwable cause) {
        super(cause);
    }
}
