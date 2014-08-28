package com.ejushang.steward.main;

/**
 * User: liubin
 * Date: 13-12-27
 */
public interface GrabberMBean {

    void startAll();

    void stopAll();

    void startAnalyzer();

    void startGrabber();

    void stopGrabber();

    void stopAnalyzer();

    String getStatus();

}
