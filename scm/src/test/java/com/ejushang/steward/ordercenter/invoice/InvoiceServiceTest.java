package com.ejushang.steward.ordercenter.invoice;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.service.InvoiceService;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Blomer
 * Date: 14-4-14
 * Time: 下午1:10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
@Transactional
public class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;

    @Test
    @Rollback(false)
    public void testOrderBackToWaitProcess() {
        Integer[] orderIds = {1};
        invoiceService.orderBackToWaitProcess(orderIds);
    }

    @Test
    @Rollback(false)
    public void testDeliveryPrint() {
        Integer[] orderIds = {1};
       /* List<Order> orderList = invoiceService.deliveryPrint(orderIds);
        for (Order order : orderList) {
            System.out.println(order.toString());
            System.out.println(order.getOrderItemList());
        }*/
    }

    @Test
    @Transactional
    public void testOrderPrint() {
        Integer[] orderIds = {1};
        /*List<Order> orderList = invoiceService.deliveryPrint(orderIds);
        for (Order order : orderList) {
            System.out.println(order.toString());
            System.out.println(order.getOrderItemList());
            System.out.println(order.getRepo());
        }*/
    }

    @Test
    @Rollback(false)
    public void testOrderAffirmPrint() {
        Integer[] orderIds = {1};
        invoiceService.orderAffirmPrint(orderIds);
        /*int orderId = 1;
        OrderStatus status;
        OrderApprove orderApprove = dao.searchUnique();
        if(orderApprove == null) {
            orderApprove = new OrderApprove();
            orderApprove.setOrderId(orderId);
            orderApprove.setOrderStatus(status);
        }
        dao.save(orderApprove);*/
    }

    @Test
    @Rollback(false)
    public void testOrderBackToConfirm() {
        Integer[] orderIds = {1};
        invoiceService.orderBackToConfirm(orderIds);
    }
    @Test
    @Rollback(false)
    @Transactional
    public void collect() throws IOException, ParseException {
//        Map<String,Object[]> map=new HashMap<String,Object[]>();
//        FileOutputStream fos = new FileOutputStream("d:\\test.xls");
//        String orderIds="7406,7390,7384,7369,6967,6965";
//        Workbook workbook = invoiceService.collectInvoiceOrderExcel(orderIds);
//        workbook.write(fos);
//        fos.flush();
//        fos.close();
    }
    @Test
    @Rollback(false)
    public void testOrderBatchExamine() {
        Integer[] orderIds = {1};
        invoiceService.orderBatchExamine(orderIds);
    }

    @Test
    @Rollback(false)
    public void testOrderInspection() {
        String[] shippingNo = {"11314308"};
        List<Map<String, Object>> mapList = invoiceService.orderInspection(shippingNo);

        Map map = mapList.get(0);

        System.out.println(map.get("confirmUser"));

    }

    @Test
    @Rollback(false)
    public void testOrderBackToPrint() {
        Integer[] orderIds = {1};
        invoiceService.orderBackToPrint(orderIds);
    }

    @Test
    @Rollback(false)
    public void testOrderInvoice() {
        Integer[] orderIds = {1};
        invoiceService.orderInvoice(orderIds);
    }


}
