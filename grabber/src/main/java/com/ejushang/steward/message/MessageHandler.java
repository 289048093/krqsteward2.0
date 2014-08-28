package com.ejushang.steward.message;

import com.ejushang.steward.main.WorkerStatus;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * User: liubin
 * Date: 14-5-12
 */
public abstract class MessageHandler implements Runnable {


    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private AtomicReference<WorkerStatus> status = new AtomicReference<WorkerStatus>(WorkerStatus.STOPPED);

    public void start() {
        if(status.compareAndSet(WorkerStatus.STOPPED, WorkerStatus.RUNNING)) {
            log.info("启动" + getName());
            new Thread(this).start();
        }
    }

    public void stop() {
        if(status.compareAndSet(WorkerStatus.RUNNING, WorkerStatus.STOP_WAITING)) {
            log.info("停止" + getName());
        }
    }

    @Override
    public void run() {
        boolean firstTime = true;
        StopWatch sw = new StopWatch();
        long minInterval = readMinInterval();

        while(WorkerStatus.RUNNING == status.get()) {
            try {
                log.info("{}开始执行", getName());
                sw.reset();
                sw.start();

                doHandle(firstTime);

                firstTime = false;

                sw.stop();
                log.info("{}执行成功, 耗时:{}", getName(), sw.toString());
                long sleepTime = minInterval - sw.getTime();
                if(sleepTime > 0) {
                    try {
                        log.info("{}休眠{}ms", getName(), sleepTime);
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                }
            } catch (Exception e) {
                log.error("执行" +  getName() + "的时候发生未知错误", e);
            }
        }
        status.compareAndSet(WorkerStatus.STOP_WAITING, WorkerStatus.STOPPED);
    }

    public long readMinInterval() {
        return getExecuteIntervalInSeconds() * 1000;
    }

    public AtomicReference<WorkerStatus> getStatus() {
        return status;
    }

    /**
     * 实际的处理方法
     * @param firstTime 方法是否是第一次运行
     * @throws Exception
     */
    protected abstract void doHandle(boolean firstTime) throws Exception;

    /**
     * 得到任务名字
     * @return
     */
    protected abstract String getName();

    /**
     * 任务运行的时间间隔,默认60s.
     * 假设间隔设置的是60s,
     * e.g. 任务第一次在0s的时候启动,执行花了10s,那么第二次运行会在60s的时候启动.
     * e.g. 任务第一次在0s的时候启动,执行花了70s,那么第二次运行会在第一次任务结束后马上启动.
     * @return
     */
    protected int getExecuteIntervalInSeconds() {
        return 60;
    }

}
