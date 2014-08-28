package com.ejushang.steward.common.grabber;


import java.util.Date;
import java.util.List;

/**
 * User: liubin
 * Date: 14-5-10
 */
public final class Message {

    private final List<? extends Object> messages;

    private final MessageType messageType;

    private final Date addTime;

    public Message(List<? extends Object> messages, MessageType messageType) {
        this.messages = messages;
        this.messageType = messageType;
        this.addTime = new Date();
    }

    public List<? extends Object> getMessages() {
        return messages;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Date getAddTime() {
        return addTime;
    }
}
