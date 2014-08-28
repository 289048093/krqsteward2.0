package com.ejushang.steward.openapicenter.zy.api.aceess.parser;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 14:59
 */
public class ExceptionErrorListener extends BufferErrorListener {
    public void error(String type, int col) {
        super.error(type, col);
        throw new IllegalArgumentException(buffer.toString());
    }
}
