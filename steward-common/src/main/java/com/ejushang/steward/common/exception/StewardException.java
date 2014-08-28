package com.ejushang.steward.common.exception;

/**
 * User: liubin
 * Date: 14-2-7
 */
public class StewardException extends Exception {
    public StewardException() {
        super();
    }

    public StewardException(String message) {
        super(message);
    }

    public StewardException(String message, Throwable cause) {
        super(message, cause);
    }

    public StewardException(Throwable cause) {
        super(cause);
    }

}
