package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.OriginalOrderStatus;
import com.ejushang.steward.ordercenter.constant.OriginalRefundStatus;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.constant.RefundPhase;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.domain.OriginalRefund;
import com.ejushang.steward.ordercenter.domain.Repository;
import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


/**
 * User: liubin
 * Date: 14-3-10
 */
public class AnalyzeSpecificOriginalRefundTest extends BaseAnalyzeTest {


    @Autowired
    private MessageAnalyzeService messageAnalyzeService;

    @Autowired
    private RefundAnalyzeTestService refundAnalyzeTestService;

    @Autowired
    private GeneralDAO generalDAO;


    /**
     * 根据外部平台订单,模拟生成一条线上退款单
     */
    @Test
    @Transactional
    @Rollback(false)
    public void testCreateOnlineOriginalRefund() {

        String platformOrderNo = "1609366129";
        OriginalRefundStatus status = OriginalRefundStatus.SUCCESS;
        Money refundFee = Money.valueOf(10d);
        Date refundTime = new Date();
        String buyerAlipayNo = null;

        createOnlineOriginalRefund(platformOrderNo, status, refundFee, refundTime, buyerAlipayNo);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testAnalyzeSpecificOriginalRefund() {
        String refundNo = "26725637";
        Search search = new Search(OriginalRefund.class);
        List<OriginalRefund> originalRefunds = generalDAO.search(search.addFilterEqual("refundId", refundNo));
        assertThat(originalRefunds.size(), is(1));
        messageAnalyzeService.analyzeOriginalRefunds(originalRefunds);

    }


    private void createOnlineOriginalRefund(String platformOrderNo, OriginalRefundStatus status, Money refundFee, Date refundTime, String buyerAlipayNo) {
        Search search = new Search(OriginalOrder.class);
        List<OriginalOrder> originalOrders = generalDAO.search(search.addFilterEqual("platformOrderNo", platformOrderNo));
        assertThat(originalOrders.size(), is(1));

        OriginalOrder originalOrder = originalOrders.get(0);

        OriginalRefundBuilder refundBuilder = refundAnalyzeTestService.createBuilder(originalOrder, RefundPhase.ON_SALE, status);
        refundBuilder.setBuyerAlipayNo(buyerAlipayNo);
        refundBuilder.setRefundFee(refundFee);
        refundBuilder.setRefundTime(refundTime);
        OriginalRefund originalRefund = refundBuilder.build();

        messageAnalyzeService.analyzeOriginalRefunds(Lists.newArrayList(originalRefund));
    }



}
