package com.ejushang.steward.ordercenter.RefundService;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.PostPayer;
import com.ejushang.steward.ordercenter.domain.OrderFetch;
import com.ejushang.steward.ordercenter.domain.OrderItem;
import com.ejushang.steward.ordercenter.domain.Refund;
import com.ejushang.steward.ordercenter.service.RefundService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Shiro
 * Date: 14-5-8
 * Time: 上午11:29
 */
public class RefundServiceTest extends BaseTest {
    @Autowired
    private RefundService refundService;

    @Autowired
    private GeneralDAO generalDAO;

    @Test
    @Transactional
    @Rollback(false)
    public void findDetail() throws ParseException {
       Page page = new Page(1, 2);
        Map<String, Object[]> map = new HashMap<String, Object[]>();
//        Object[] obj1 = new Object[1];
//        Object[] obj2 = new Object[1];
//        Object[] obj3 = new Object[1];
//        Object[] obj4 = new Object[1];
//        Object[] obj5 = new Object[1];
//        obj1[0]  = "name";
//        obj2[0]  ="has";
//        obj3[0]  = "Z";
//        obj4[0]  = "refundTime";
//        obj5[0]  = "0000-00-00 00:00:00";
//        map.put("conditionQuery",obj1);
//        map.put("conditionType", obj2);
//        map.put("conditionValue",obj3);
//        map.put("dateType",obj4 );
//        map.put("startDate",obj5);
        refundService.findRefund(map, page);
       System.out.println( new JsonResult(true).addObject(refundService.findRefund(map, page)));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testSaveRefund() {
        Refund refund = new Refund();
        refund.setRefundFee(Money.valueOf(13.00));
       // refund.setRefundTime(new Date());
        refund.setReason("asdfsadgqsg");
        refund.setAlsoReturn(true);
        refund.setActualRefundFee(Money.valueOf(10.00));
        refund.setPostFee(Money.valueOf(12));
        refund.setPostPayer(PostPayer.BUYER);
        OrderItem orderItem = generalDAO.get(OrderItem.class, 280);
        refundService.saveRefund(280, refund);
    }

    @Test
    @Transactional
    public void testUpdateRefund() {
        Refund refund = new Refund();
        refund.setRefundFee(Money.valueOf(100000));
        refund.setAlsoReturn(true);
        refund.setActualRefundFee(Money.valueOf(435));
        refund.setPostFee(Money.valueOf(12));
        refund.setPostPayer(PostPayer.BUYER);
      refundService.updateRefund(5,refund);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testDeleteRefund() {
        refundService.deleteRefund(5);
    }
}