package com.ejushang.steward.message;

import com.ejushang.steward.common.grabber.Message;
import com.ejushang.steward.common.grabber.MessageType;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.domain.OriginalRefund;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 源消息队列持有者,管理抓取与分析线程的交互
 *
 * User: liubin
 * Date: 13-12-27
 */
@Component
public class MessageHolder {

    private final Logger log = LoggerFactory.getLogger(MessageHolder.class);

    public static final int MAX_MESSAGE_COUNTS = 10000;

    private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<Message>(MAX_MESSAGE_COUNTS);


    /**
     * 往队列放入原始订单信息
     * @param originalOrders
     * @return
     */
    public boolean addOrders(List<OriginalOrder> originalOrders) {
        return add(new Message(originalOrders, MessageType.ORDER));
    }

    /**
     * 往队列放入原始订单主键
     * @param originalOrderIds
     * @return
     */
    public boolean addOrderIds(List<Integer> originalOrderIds) {
        return add(new Message(originalOrderIds, MessageType.ORDER_ID));
    }

    /**
     * 往队列放入原始退款信息
     * @param originalRefunds
     * @return
     */
    public boolean addRefunds(List<OriginalRefund> originalRefunds) {
        return add(new Message(originalRefunds, MessageType.REFUND));
    }


    /**
     * 往队列放入元素
     * @param grabberMessage
     * @return
     */
    private boolean add(Message grabberMessage) {

        try {
            queue.offer(grabberMessage, 10, TimeUnit.SECONDS);
            return true;
        } catch (InterruptedException e) {
            log.warn("往队列放消息的阻塞过程被中断,队列已满?", e);
        }
        return false;
    }

    /**
     * 取出队列中的所有元素
     * @return
     */
    public List<Message> fetchAll() {
        List<Message> allMessages = new ArrayList<Message>();
        queue.drainTo(allMessages);
        return allMessages;
    }

}
