package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.ordercenter.domain.OrderApprove;
import com.ejushang.steward.ordercenter.domain.OrderHandleLog;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-5-26
 * Time: 上午10:57
 * To change this template use File | Settings | File Templates.
 */
public class ApproveLogsVo {
    private List<OrderApprove> orderApproves;

    private List<OrderHandleLog> orderHandleLogs;

    public List<OrderApprove> getOrderApproves() {
        return orderApproves;
    }

    public void setOrderApproves(List<OrderApprove> orderApproves) {
        this.orderApproves = orderApproves;
    }

    public List<OrderHandleLog> getOrderHandleLogs() {
        return orderHandleLogs;
    }

    public void setOrderHandleLogs(List<OrderHandleLog> orderHandleLogs) {
        this.orderHandleLogs = orderHandleLogs;
    }
}
