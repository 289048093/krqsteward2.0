package com.ejushang.steward.main;

import com.ejushang.steward.analyzer.MessageAnalyzer;
import com.ejushang.steward.grabber.MessageGrabber;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 订单抓取分析入口
 * User: liubin
 * Date: 13-12-26
 */
public class GrabberMonitor implements GrabberMBean {

    @Autowired
    private MessageAnalyzer messageAnalyzer;
    @Autowired
    private MessageGrabber messageGrabber;

    @Override
    public void startAll() {
        startAnalyzer();
        startGrabber();
    }

    @Override
    public void stopAll() {
        stopAnalyzer();
        stopGrabber();
    }

    @Override
    public void startAnalyzer() {
        messageAnalyzer.start();
    }

    @Override
    public void stopAnalyzer() {
        messageAnalyzer.stop();
    }

    @Override
    public void startGrabber() {
        messageGrabber.start();
    }

    @Override
    public void stopGrabber() {
        messageGrabber.stop();
    }


    @Override
    public String getStatus() {
        String grabberStatus = messageGrabber.getStatus().get().toString();
        String analyzerStatus = messageAnalyzer.getStatus().get().toString();
        String result = String.format("grabber status:%s, analyzer status:%s", grabberStatus, analyzerStatus);
        return result;
    }
}
