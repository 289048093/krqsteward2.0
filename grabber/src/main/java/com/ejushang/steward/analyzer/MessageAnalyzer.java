package com.ejushang.steward.analyzer;

import com.ejushang.steward.common.grabber.Message;
import com.ejushang.steward.message.MessageHandler;
import com.ejushang.steward.message.MessageHolder;
import com.ejushang.steward.ordercenter.keygenerator.SystemConfConstant;
import com.ejushang.steward.ordercenter.service.ConfService;
import com.ejushang.steward.ordercenter.service.MessageAnalyzeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息分析处理主线程
 * User: liubin
 * Date: 13-12-27
 */
@Component
public class MessageAnalyzer extends MessageHandler {

    private final Logger log = LoggerFactory.getLogger(MessageAnalyzer.class);

    @Autowired
    private MessageHolder messageHolder;


    @Autowired
    private ConfService confService;

    @Autowired
    private MessageAnalyzeService messageAnalyzeService;



    @Override
    public void doHandle(boolean firstTime) throws Exception {
        List<Message> messageList = messageHolder.fetchAll();

        messageAnalyzeService.handleMessage(firstTime, messageList);

    }



    @Override
    protected String getName() {
        return "消息分析任务";
    }

    @Override
    protected int getExecuteIntervalInSeconds() {
        Integer value = confService.getConfIntegerValue(SystemConfConstant.MESSAGE_ANALYZE_INTERVAL);
        if(value == null) {
            log.warn("系统配置项[{}]取不到整数值", SystemConfConstant.MESSAGE_ANALYZE_INTERVAL);
            return super.getExecuteIntervalInSeconds();
        } else {
            return value;
        }
    }



}
