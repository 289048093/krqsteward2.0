package com.ejushang.steward.ordercenter.orderService;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.domain.OrderItem;
import com.ejushang.steward.ordercenter.domain.Product;
import com.ejushang.steward.ordercenter.service.OrderService;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import com.ejushang.steward.ordercenter.vo.OrderVo;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tin
 * Date: 14-4-14
 * Time: 上午10:45
 */
@Transactional
public class orderServiceTest extends BaseTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private GeneralDAO generalDAO;
   //改变订单字段Online的值
    @Test
    @Transactional
    @Rollback(false)
    public void changeOrderOnline() throws ParseException, IOException {
        List<Order> orders = generalDAO.search(new Search(Order.class));
        for (Order order : orders) {
            if (order.getPlatformType().equals(PlatformType.JING_DONG) || order.getPlatformType().equals(PlatformType.TAO_BAO)) {
                order.setOnline(true);
            } else {
                order.setOnline(false);
            }
           generalDAO.saveOrUpdate(order);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void update() {
        Integer[] id = {1};

        orderService.updateStatusByOrder(id, "shunfen");

    }

    @Test
    @Transactional
    @Rollback(false)
    public void leadInOrders() throws IOException, ParseException {
        XSSFWorkbook wb;
        InputStream is = null;
        List<Row> rows = new ArrayList<Row>();
        try {
            is = new FileInputStream("d:\\leadInOrderExcelModel20040707.xlsx");
            wb = new XSSFWorkbook(is);
            Sheet sheet = wb.getSheetAt(0);
//            if (sheet.getLastRowNum() < 1) {
//                throw new StewardBusinessException("没有数据");
//            }
            sheet.removeRow(sheet.getRow(0)); //remove 前两行标题
            for (Row row : sheet) {
                rows.add(row);
            }
            orderService.OrderLeadIn(rows);
        } finally {
            if (is != null) is.close();
        }


    }


    @Test
    @Rollback(false)
    public void testOrderConfirm() {
        Integer[] orderIds = {1};
        orderService.orderConfirm(orderIds);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testExchange() {
//        OrderItem orderItem = orderService.findOrderItemById(4);
//        Product product = productService.findProductBySKU("mIyQoay");
//        orderService.exchangeGoods(1, 1, 3);
    }
}
