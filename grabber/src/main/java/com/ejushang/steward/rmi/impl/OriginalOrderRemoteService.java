package com.ejushang.steward.rmi.impl;

import com.ejushang.steward.common.grabber.Message;
import com.ejushang.steward.message.MessageHolder;
import com.ejushang.steward.ordercenter.rmi.IOriginalOrderRemoteService;
import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * User: liubin
 * Date: 14-7-9
 */
public class OriginalOrderRemoteService implements IOriginalOrderRemoteService {

    private static final Logger log = LoggerFactory.getLogger(OriginalOrderRemoteService.class);

    @Autowired
    private MessageHolder messageHolder;

    @Autowired
    private OriginalOrderService originalOrderService;

    public static final int MAX_MESSAGE_COUNTS = 10000;

    private LinkedBlockingQueue<Map<String,String>> fetchDataParamQueue = new LinkedBlockingQueue<Map<String,String>>(MAX_MESSAGE_COUNTS);

    private int maxSize = 500;

    @Override
    @Transactional
    public boolean tryAnalyzeOriginalOrders(List<Integer> ids) {
        log.info("接收到解析原始订单的请求,size:" + (ids == null ? 0 : ids.size()));
        if(ids == null || ids.isEmpty()) return false;
//        if(ids.size() > maxSize) {
//            log.warn("接收到解析原始订单的请求,请求数量大于500,忽略请求,size:" + ids.size());
//            return false;
//        }
        messageHolder.addOrderIds(ids);
        return true;
    }

    @Override
    public boolean getRemoteFetchDataParamMap(Map<String, String> fetchDataParamMap) {
        if(log.isInfoEnabled()){
            log.info("接收到查询原始订单的请求，fetchDataParamMap：" + fetchDataParamMap);
        }
        if(fetchDataParamMap == null){
            return false;
        }

        try {
            fetchDataParamQueue.offer(fetchDataParamMap, 10, TimeUnit.SECONDS);
            return true;
        } catch (InterruptedException e) {
            if(log.isWarnEnabled()) {
                log.warn("往队列放消息的阻塞过程被中断,队列已满?", e);
            }
            return false;
        }
    }

    @Override
    public List<Map<String,String>> getQueueFetchDataParamMaps(){
        List<Map<String,String>> paramMaps = new ArrayList<Map<String, String>>();
        fetchDataParamQueue.drainTo(paramMaps);
        return paramMaps;
    }
}
