package com.ejushang.steward.ordercenter.service;


import com.ejushang.steward.common.domain.Area;
import com.ejushang.steward.common.domain.City;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


/**
 * User: liubin
 * Date: 14-4-14
 * Time: 上午9:19
 */
@Service
@Transactional
public class RefundAnalyzeTestService {

    private static final Logger log = LoggerFactory.getLogger(RefundAnalyzeTestService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private JdbcTemplate jdbcTemplate;




    @Transactional(propagation = Propagation.REQUIRED)
    public OriginalRefundBuilder createBuilder(OriginalOrder originalOrder, RefundPhase refundPhase, OriginalRefundStatus status) {
        return new OriginalRefundBuilder(originalOrder, refundPhase, status);
    }



    /**
     * 校验分析结果是否正确
     * @param originalOrder
     * @param originalRefund
     * @param exceptedResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void checkAnalyzeResult(OriginalOrder originalOrder, OriginalRefund originalRefund, RefundAnalyzeServiceTest.OriginalRefundAnalyzeExceptedResult exceptedResult) {

        List<Order> orders = orderService.findByPlatformOrderNo(originalOrder.getPlatformType(), originalOrder.getPlatformOrderNo());
        assertThat("生成的订单数量不为" + exceptedResult.getOrderCount(), orders.size(), is(exceptedResult.getOrderCount()));
        int orderItemCount = 0;
        for(Order o : orders) {
            orderItemCount += o.getOrderItemList().size();
        }
        assertThat("生成的订单项数量不为" + exceptedResult.getOrderItemCount(), orderItemCount, is(exceptedResult.getOrderItemCount()));

//        Order order = orders.get(0);
//        List<OrderItem> orderItemList = order.getOrderItemList();
//        OrderItem orderItem = orderItemList.get(0);
//
//        PlatformType platformType = originalOrder.getPlatformType();
//        OriginalRefundBuilder refundBuilder = createBuilder(platformType, RefundPhase.ON_SALE, OriginalRefundStatus.WAIT_SELLER_AGREE);
//        refundBuilder.addOrderItem(orderItem);
//        OriginalRefund originalRefund = refundBuilder.build();

        List<Refund> refunds = refundService.findByPlatformRefundNo(originalRefund.getPlatformType(), originalRefund.getRefundId());
        assertThat("生成的退款单数量不为" + exceptedResult.getRefundCount(), refunds.size(), is(exceptedResult.getRefundCount()));

        for(Refund refund : refunds) {
            switch (originalRefund.getStatus()) {
                case GOODS_RETURNING:
                case WAIT_SELLER_AGREE:
                    assertThat(refund.getStatus(), is(RefundStatus.IN_PROCESS));
                    switch (refund.getType()) {
                        case ORDER:
                            assertThat(refund.getOrderItem().getRefunding(), is(true));
                            break;
                        case PAYMENT:
                            break;
                    }
                    break;

                case SUCCESS:
                    assertThat(refund.getStatus(), is(RefundStatus.SUCCESS));
                    switch (refund.getType()) {
                        case ORDER:
                            assertThat(refund.getOrderItem().getRefunding(), is(false));
                            switch (refund.getPhase()) {
                                case ON_SALE:
                                    assertThat(refund.getOrderItem().getReturnStatus(), is(OrderItemReturnStatus.RETURNED));
                                    break;
                                case AFTER_SALE:
                                    if(refund.isAlsoReturn()) {
                                        if (refund.isOnline()) {
                                            assertThat(refund.getOrderItem().getReturnStatus(), is(OrderItemReturnStatus.RETURNED));
                                        } else {
                                            assertThat(refund.getOrderItem().getOfflineReturnStatus(), is(OrderItemReturnStatus.RETURNED));
                                        }
                                    }
                                    break;
                            }

                            break;
                        case PAYMENT:
                            assertThat(refund.getPayment().getAllocateStatus(), is(PaymentAllocateStatus.WAIT_ALLOCATE));
                            break;
                    }
                    break;

                case SELLER_REFUSE:
                case CLOSED:
                    assertThat(refund.getStatus(), is(RefundStatus.CLOSED));
                    switch (refund.getType()) {
                        case ORDER:
                            assertThat(refund.getOrderItem().getRefunding(), is(false));
                            break;
                        case PAYMENT:
                            break;
                    }

                    break;
            }

        }

        //校验原始退款是否已经被处理
        Boolean processed = jdbcTemplate.queryForObject("select processed from t_original_refund where id = ?", new Object[]{originalRefund.getId()}, Boolean.class);
        assertThat(String.format("原始退款被处理过了,但是processed还没变为true, originalRefundId[%d]", originalRefund.getId()), processed, is(true));
    }



}


