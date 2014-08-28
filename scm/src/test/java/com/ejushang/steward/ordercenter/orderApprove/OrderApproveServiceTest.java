package com.ejushang.steward.ordercenter.orderApprove;

import com.ejushang.steward.ordercenter.constant.OrderStatus;
import com.ejushang.steward.ordercenter.domain.OrderApprove;
import com.ejushang.steward.ordercenter.service.OrderApproveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * User: Blomer
 * Date: 14-4-17
 * Time: 上午9:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
@Transactional
public class OrderApproveServiceTest {
    @Autowired
    private OrderApproveService orderApproveService;

    @Test
    public void testSaveOrUpdate(){
        OrderApprove orderApprove = new OrderApprove();
        orderApproveService.saveOrUpdate(orderApprove);
    }

    @Test
    public void testDetial(){
        System.out.println("hehe" + orderApproveService.findByOrderStatusWithOrderId(OrderStatus.PRINTED, 1));
    }
}
