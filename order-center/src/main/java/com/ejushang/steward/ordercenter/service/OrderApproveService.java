package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.ordercenter.constant.OrderStatus;
import com.ejushang.steward.ordercenter.domain.OrderApprove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: Blomer
 * Date: 14-4-17
 * Time: 上午9:14
 */
@Service
public class OrderApproveService {
    @Autowired
    private GeneralDAO generalDAO;

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    /**
     * 查询所有的订单审核记录
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<OrderApprove> list(Page page) {
        Search search = new Search(OrderApprove.class);
        search.addSortAsc("id").addPagination(page);
        return generalDAO.search(search);
    }

    /**
     * 增加或更新订单审核
     *
     * @return
     */
    @Transactional
    public void saveOrUpdate(OrderApprove orderApprove) {
        if (log.isInfoEnabled()) {
            log.info(String.format("OrderApproveService中的saveOrUpdate方法,参数是orderApprove[%s]", orderApprove.toString()));
        }
        Search search = new Search(OrderApprove.class);
        search.addFilterEqual("orderId", orderApprove.getOrderId()).addFilterEqual("orderStatus", orderApprove.getOrderStatus());
        OrderApprove orderApprove1 = (OrderApprove) generalDAO.searchUnique(search);
        if (null == orderApprove1) {
            generalDAO.saveOrUpdate(orderApprove);
            return;
        }
        orderApprove1.setOperatorId(orderApprove.getOperatorId());
        generalDAO.saveOrUpdate(orderApprove1);
    }

    /**
     * 查询订单的历史状态
     * <p/>
     * 当订单没有该历史状态时返回null
     *
     * @param status  要查询的状态
     * @param orderId 订单id
     * @return OrderApprove
     */
    @Transactional(readOnly = true)
    public OrderApprove findByOrderStatusWithOrderId(OrderStatus status, Integer orderId) {
        if (log.isInfoEnabled()) {
            log.info(String.format("OrderApproveService中的findByOrderStatusWithOrderId方法,参数orderStatus[%s],orderId[%d]", status, orderId));
        }
        Search search = new Search(OrderApprove.class);
        search.addFilterEqual("orderStatus", status).addFilterEqual("orderId", orderId);
        return (OrderApprove) generalDAO.searchUnique(search);
    }
    @Transactional(readOnly = true)
    public List<OrderApprove> findByOrderStatus(OrderStatus status) {
        Search search = new Search(OrderApprove.class);
        search.addFilterEqual("orderStatus", status);
        return generalDAO.search(search);
    }
}
