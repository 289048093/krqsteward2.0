package com.ejushang.steward.ordercenter.orderService;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.ordercenter.MockBaseTest;
import com.ejushang.steward.ordercenter.constant.OrderStatus;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class OrderServiceMockTest extends MockBaseTest {

    private OrderService orderService = new OrderService();

    private TestClass test = new TestClass();

    static class MethodClass {

        public final boolean finalMethod() {
            return false;
        }

        public static boolean staticMethod() {
            return false;
        }

        public static boolean method() {
            return privateMethod();
        }

        private static boolean privateMethod() {
            return false;
        }
    }

    static class TestClass {

        public boolean newObjectTest() {
            return new File("test.txt").exists();
        }

        public boolean finalMethodTest() {
            return new MethodClass().finalMethod();
        }

        public boolean staticMethodTest() {
            return MethodClass.staticMethod();
        }

        public boolean privateMethodTest() {
            return MethodClass.method();
        }

    }

    /**
     * GeneralDAO层打桩测试示例
     *
     * @throws Exception
     */
    @Test
    public void testFindOrderbyOrderStatus() throws Exception {
        // 对GeneralDAO进行打桩
        GeneralDAO generalDAO = PowerMockito.mock(GeneralDAO.class);
        ReflectionTestUtils.setField(orderService, "generalDAO", generalDAO);
        List<Order> orderListMock = new LinkedList<Order>();
        orderListMock.add(new Order());
        PowerMockito.when(generalDAO, "search", new Search(Order.class).addFilterEqual("valid", true).addFilterEqual("status",
                OrderStatus.CONFIRMED)).thenReturn(orderListMock);
        // PowerMockito.when(generalDAO, "search", Mockito.any()).thenReturn(orderListMock);
        List<Order> orderList = orderService.findOrderbyOrderStatus(OrderStatus.CONFIRMED);
        Assert.assertTrue(orderList.size() == 1);
    }

    /**
     * Mock 内部new对象
     *
     * @throws Exception
     */
    @Test
    public void testNewObject() throws Exception {
        File file = PowerMockito.mock(File.class);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(file);
        PowerMockito.when(file.exists()).thenReturn(true);
        Assert.assertTrue(test.newObjectTest());
    }

    /**
     * Mock Final方法
     *
     * @throws Exception
     */
    @Test
    public void testFinalMethod() throws Exception {
        MethodClass method = PowerMockito.mock(MethodClass.class);
        PowerMockito.whenNew(MethodClass.class).withAnyArguments().thenReturn(method);
        PowerMockito.when(method.finalMethod()).thenReturn(true);
        Assert.assertTrue(test.finalMethodTest());
    }

    /**
     * Mock 静态方法
     *
     * @throws Exception
     */
    @Test
    public void testStaticMethod() throws Exception {
        PowerMockito.mockStatic(MethodClass.class);
        PowerMockito.when(MethodClass.staticMethod()).thenReturn(true);
        Assert.assertTrue(test.staticMethodTest());
    }

    /**
     * Mock 私有方法
     *
     * @throws Exception
     */
    @Test
    public void testPrivateMethod() throws Exception {
        PowerMockito.mockStatic(MethodClass.class);
        PowerMockito.when(MethodClass.method()).thenCallRealMethod();
        PowerMockito.when(MethodClass.class, "privateMethod").thenReturn(true);
        Assert.assertTrue(test.privateMethodTest());
    }


}