package com.ejushang.steward.openapicenter.zy.api.aceess.parser;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 14:56
 */
public class StdoutStreamErrorListener extends BufferErrorListener {
    public void end() {
        System.out.print(buffer.toString());
    }
}
