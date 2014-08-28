package com.ejushang.steward.openapicenter.zy.api.aceess.parser;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 14:53
 */
public interface JsonErrorListener {
    void start(String text);
    void error(String message, int column);
    void end();
}
