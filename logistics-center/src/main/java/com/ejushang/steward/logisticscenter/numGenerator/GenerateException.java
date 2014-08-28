package com.ejushang.steward.logisticscenter.numGenerator;

import com.ejushang.steward.common.exception.StewardBusinessException;

/**
 * User: JBOSS.wu
 * Date: 14-4-23
 * Time: 下午3:02
 */
public class GenerateException extends StewardBusinessException {



    public GenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
