package com.ejushang.steward.ordercenter;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.domain.BusinessLog;
import com.ejushang.steward.common.domain.SqlLog;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.domain.Product;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * User: liubin
 * Date: 14-3-10
 */
public class SomeDaoTest extends BaseTest {

    @Autowired
    private GeneralDAO generalDAO;

    @Test
    @Transactional
    @Rollback(false)
    public void test() throws InterruptedException {

        assertThat(Application.getInstance(), notNullValue());
        assertThat(Application.getInstance().getApplicationContext(), notNullValue());

        BusinessLog businessLog = new BusinessLog();
        businessLog.setOperationName(RandomStringUtils.randomAlphabetic(6) + "operationName");
        generalDAO.saveOrUpdate(businessLog);

        SqlLog sqlLog = new SqlLog();
        sqlLog.setContent(RandomStringUtils.randomAlphabetic(6) + "content");
        sqlLog.setBusinessLogId(businessLog.getId());

        generalDAO.saveOrUpdate(sqlLog);
        assertThat(sqlLog.getId(), notNullValue());
        assertThat(businessLog.getId(), notNullValue());


        generalDAO.flush();
        generalDAO.getSession().evict(businessLog);
        generalDAO.getSession().evict(sqlLog);
        BusinessLog businessLogReload = generalDAO.get(BusinessLog.class, businessLog.getId());
        SqlLog sqlLogReload = generalDAO.get(SqlLog.class, sqlLog.getId());
        Page page = new Page(1, 10);
        List logs = generalDAO.search(new Search(BusinessLog.class).addPagination(page));
        assertThat(!logs.isEmpty(), is(true));
        assertThat(page.getResult(), is(logs));

        assertEquals(businessLog, businessLogReload, Sets.newHashSet("sqlLogList"));
        assertEquals(sqlLog, sqlLogReload, Sets.newHashSet("businessLog"));

//        assertThat(businessLogReload.getSqlLogList().size(), is(1));

        assertThat(StringUtils.isBlank(JsonUtil.object2Json(businessLogReload)), is(false));
        System.out.println(JsonUtil.object2Json(businessLogReload));

    }


    @Test
    @Transactional
    @Rollback(false)
    public void testMoney() throws InterruptedException {

        Product product = new Product();
        product.setName(RandomStringUtils.randomAlphabetic(6) + "productName");
        product.setSku(RandomStringUtils.randomAlphabetic(6) + "Sku");
        product.setMarketPrice(Money.valueOf(Double.valueOf(RandomStringUtils.randomNumeric(3))));
        generalDAO.saveOrUpdate(product);

        generalDAO.flush();
        generalDAO.getSession().evict(product);

        Product reloadProduct = generalDAO.get(Product.class, product.getId());
        assertEquals(product, reloadProduct);

        List products = generalDAO.search(new Search(Product.class).addFilterEqual("buyPrice", product.getMarketPrice()));
        assertThat(products.size(), is(1));
        assertEquals(product, products.get(0));



//        assertThat(StringUtils.isBlank(JsonUtil.objectToJson(employeeReload)), is(false));
//        System.out.println(JsonUtil.objectToJson(employeeReload));

    }

    @Test
    @Transactional
    @Rollback(false)
    public void testEnum() throws InterruptedException {

        Order order = new Order();
        order.setOrderNo(RandomStringUtils.randomAlphabetic(6) + "orderNo");
//        order.setTotalFee(Money.valueOf(10));
        order.setBuyerId("0");
        order.setShopId(0);
        order.setRepoId(0);

        System.out.println(new JsonResult(true).addObject(order).toJson());
        String s = "{\"id\":null,\"orderNo\":\"nfDjkTorderNo\",\"type\":\"NORMAL\",\"status\":\"WAIT_PROCESS\"}";

        Order orderJson = JsonUtil.json2Object(s, Order.class);
        System.out.println(orderJson.getStatus());

//        generalDAO.saveOrUpdate(order);

    }


    private void assertEquals(Object o1, Object o2) {
        assertEquals(o1, o2, null);
    }

    private void assertEquals(Object o1, Object o2, Collection<String> excludeFields) {
        assertThat(EqualsBuilder.reflectionEquals(o1, o2, excludeFields), is(true));
    }

}
