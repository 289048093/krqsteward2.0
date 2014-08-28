package com.ejushang.steward.ordercenter.storage;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.service.EmployeeService;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.Product;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.uams.api.dto.EmployeeDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-4-28
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public class StorageServiceTest extends BaseTest {

    @Autowired
    private GeneralDAO  generalDAO;

    @Autowired
    private EmployeeService employeeService;

    @Test
    @Transactional
    @Rollback(true)
    public void myTest(){
        Product product = new Product();
        product.setName("adsf");
        Shop shop  = new Shop();
        shop.setNick("hhhhhh");
        System.out.println(new JsonResult(false, "没有权限").addData("product",product).addData("shop",shop).toJson());
    }
}
