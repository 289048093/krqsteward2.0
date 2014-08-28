package com.ejushang.steward.ordercenter.returnvisit;

import com.ejushang.steward.ordercenter.domain.OrderSignedLog;
import com.ejushang.steward.ordercenter.service.OrderSignedLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by: codec.yang
 * Date: 2014/8/25 11:41
 */
public class ProcessOrderSignedLogThread extends Thread {

    private static Logger log= LoggerFactory.getLogger(ProcessOrderSignedLogThread.class);

    private volatile Object obj=new Object();

    private volatile boolean stopped=false;

    private static final long interval=12*60*60*1000;//Wait 12 hours

    private OrderSignedLogService orderSignedLogService;

    public ProcessOrderSignedLogThread(OrderSignedLogService orderSignedLogService, String threadName){
        this.orderSignedLogService=orderSignedLogService;
        this.setName(threadName);
    }

    @Override
    public void run() {
        log.info("ProcessOrderSignedLogThread start...");
        while(!stopped){
            try {
                log.info("Begin to process signed order log...");

//                List<OrderSignedLog> orderSignedLogList= this.orderSignedLogService.findAllUnProcessedOrderSignedLog();
//                ReturnVisitManager.fromOrderSignedLog(orderSignedLogList);

//                log.info("Process signed order log End!!!!!, logs number:{}", orderSignedLogList.size());
            }catch (Throwable t){
                log.error("Exception occurred while processing order signed log",t);
            }

            synchronized (obj){
                try {
                    obj.wait(interval);
                } catch (Exception e) {
                    log.error("Thread is interrupted, just ignore it.",e);
                }
            }
        }

        log.info("ProcessOrderSignedLogThread end!!!!!!");
    }

    public void exit(){
        log.info("ProcessOrderSignedLogThread exiting...");
        this.stopped=true;
        this.notifyMe();
    }

    public void notifyMe(){
        synchronized (this.obj){
            this.obj.notify();
        }
    }

}
