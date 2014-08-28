package com.ejushang.steward.main;

/**
 * 工作程序状态
 * User: liubin
 * Date: 13-12-30
 */
public enum WorkerStatus {

    STOPPED("stopped"),

    RUNNING("running"),

    STOP_WAITING("stop_waiting");

    private String value;

    WorkerStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
