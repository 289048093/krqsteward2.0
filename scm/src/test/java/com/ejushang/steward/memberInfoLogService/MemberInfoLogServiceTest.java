package com.ejushang.steward.memberInfoLogService;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.OrderLogType;
import com.ejushang.steward.ordercenter.constant.OrderType;
import com.ejushang.steward.ordercenter.constant.RefundType;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.domain.Payment;
import com.ejushang.steward.ordercenter.domain.Refund;
import com.ejushang.steward.ordercenter.service.MemberInfoLogService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JBoss.WU
 * Date: 14-8-22
 * Time: 上午11:30
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class MemberInfoLogServiceTest extends BaseTest {
    @Autowired
    private GeneralDAO generalDAO;
    @Autowired
    private MemberInfoLogService memberInfoLogService;

    @Test
    @Rollback(false)
    public void saveMemberLogInfo() {
        //插入原始单会员记录
        saveOriginalInMemberLogInfo();
        //插入订单会员记录
        saveOrderInMemberLogInfo();
        //插入退款会员记录
        saveRefundInMemberLogInfo();
        //插入预收款会员记录
        savePaymentInMemberLogInfo();
    }

    @Transactional
    public void saveOriginalInMemberLogInfo() {
        List<OriginalOrder> originalOrderList = generalDAO.search(new Search(OriginalOrder.class));
        for (OriginalOrder originalOrder : originalOrderList) {
            memberInfoLogService.save(null, null, null, originalOrder, OrderLogType.ORIGINAL, false);
        }
    }

    @Transactional
    public void saveOrderInMemberLogInfo() {
        String orderHql = "from Order where valid=1 and status in('EXAMINED','INVOICED','SIGNED','BUYER_RECEIVE') and type <>'CHAT'";
        List<Order> orders = generalDAO.query(orderHql, null, new Object[]{});
        for (Order order : orders) {
            memberInfoLogService.save(order, null, null, null, OrderLogType.CONFIRMED, false);
        }
    }

    @Transactional
    public void saveRefundInMemberLogInfo() {
        String hql = "from Refund where status='SUCCESS'";
        List<Refund> refunds = generalDAO.query(hql, null, new Object[]{});
        for (Refund refund : refunds) {
            if (refund.getType().equals(RefundType.ORDER)) {
                memberInfoLogService.save(null, refund, null, null, OrderLogType.REFUND, false);
            } else {
                memberInfoLogService.save(null, refund, null, null, OrderLogType.REFUND, true);
            }
        }
    }

    @Transactional
    public void savePaymentInMemberLogInfo() {
        String hql = "from Payment ";
        List<Payment> payments = generalDAO.query(hql, null, new Object[]{});
        for (Payment payment : payments) {
            memberInfoLogService.save(null, null, payment, null, OrderLogType.PAYMENT, false);
        }
    }

}
