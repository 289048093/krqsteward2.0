package com.ejushang.steward.openapicenter.tb.util;

import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;

/**
 * User: Baron.Zhang
 * Date: 14-4-25
 * Time: 上午10:42
 */
public class TbMessageHandler implements MessageHandler {
    @Override
    public void onMessage(Message message, MessageStatus status) throws Exception {
        message.getTopic();
        message.getContent();
    }
}
