package com.ejushang.steward.ordercenter.orderService;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Shiro
 * Date: 14-4-17
 * Time: 下午2:13
 */
@Transactional
public class OriginalOrderServiceTest extends BaseTest {

    @Autowired
    private OriginalOrderService originalOrderService;

    @Test
    public void testGetOriginalOrder() {
        OriginalOrder originalOrder = new OriginalOrder();
        Date startTime=null;
        Date endTime=null;
        try {
            startTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-04-17 00:00:00");
            endTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-04-18 00:00:00");
        } catch (ParseException e){
            e.printStackTrace();
        }
     System.out.println(new JsonResult(true).addList(originalOrderService.getOriginalOrder(originalOrder,startTime,endTime)));
    }

    @Test
    @Rollback(false)
    public void testSaveOrUpdate() {
        OriginalOrder originalOrder = new OriginalOrder();
        originalOrder.setId(5);
        originalOrder.setStatus("待发货");
        originalOrder.setPayableFee(Money.valueOf(200));
        originalOrder.setBuyerId("123");
        Receiver receiver = new Receiver();
        receiver.setReceiverName("jack");
        receiver.setReceiverState("湖南");
        receiver.setReceiverCity("长沙");
        receiver.setReceiverAddress("长江一号");
        originalOrder.setReceiver(receiver);
        originalOrder.setPlatformType(PlatformType.TAO_BAO);
        originalOrder.setPlatformOrderNo("1213434324");
        originalOrder.setPayTime(new Date());
        originalOrder.setShopId(8);
        originalOrderService.saveOriginalOrder(originalOrder);
    }

    @Test
    @Rollback(false)
    public void testDelete(){
        originalOrderService.deleteOriginalOrder(3);

    }
}
