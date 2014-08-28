package Vo;

import com.ejushang.steward.ordercenter.domain.AfterSales;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.domain.ReturnVisitTask;

import java.util.List;

/**
 * User:moon
 * Date: 14-8-21
 * Time: 上午11:13
 */
public class CustomerVo {

    private List<Order> orderList;
    private List<ReturnVisitTask> returnVisitTasks;
    private List<AfterSales> afterSaleses;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<ReturnVisitTask> getReturnVisitTasks() {
        return returnVisitTasks;
    }

    public void setReturnVisitTasks(List<ReturnVisitTask> returnVisitTasks) {
        this.returnVisitTasks = returnVisitTasks;
    }

    public List<AfterSales> getAfterSaleses() {
        return afterSaleses;
    }

    public void setAfterSaleses(List<AfterSales> afterSaleses) {
        this.afterSaleses = afterSaleses;
    }
}
