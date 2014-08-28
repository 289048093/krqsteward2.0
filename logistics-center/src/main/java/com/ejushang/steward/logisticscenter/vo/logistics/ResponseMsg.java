package com.ejushang.steward.logisticscenter.vo.logistics;

/**
 * 第三方物流响应的数据
 * User: Shiro
 * Date: 14-8-1
 * Time: 上午11:34
 */
public class ResponseMsg {
    private boolean result;
    private int returnCode;
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "[result:" + result + ", returnCode:" + returnCode + ", message:" + message + "]";
    }

}