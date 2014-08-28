package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Product;
import com.ejushang.steward.ordercenter.constant.ProductLocation;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-5-14
 * Time: 下午3:18
 */
public class BeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 对象属性拷贝
     * <p/>
     * 可以在Money与String类型之间进行属性拷贝，对象必须符合JavaBean规范
     *
     * @param src  原始对象
     * @param dest 目标对象
     */
    public static void copyProperties(Object src, Object dest) {
        Method[] destMethods = dest.getClass().getDeclaredMethods();
        Class<?> srcClazz = src.getClass();
        for (Method m : destMethods) {
            m.setAccessible(true);
            String methodName = m.getName();
            if (methodName.contains("set")) {
                String methodDesc = methodName.substring(3);
                Method readMethod = null;
                try {
                    try {
                        readMethod = srcClazz.getMethod("get" + methodDesc);
                    } catch (NoSuchMethodException e) {
                        try {
                            readMethod = srcClazz.getMethod("is" + methodDesc);
                        } catch (NoSuchMethodException e1) {
                            continue;
                        }
                    }
                    Object returnValue = readMethod.invoke(src);
                    Class<?> parameterType = m.getParameterTypes()[0];

                    if (!(returnValue instanceof String) && String.class.isAssignableFrom(parameterType)) {//如果类src类型不为String，而dest为String，则直接读ToString
                        returnValue = returnValue == null ? null : returnValue.toString();
                    }
                    if (Money.class.equals(parameterType) && !Money.class.isInstance(returnValue)) {
                        returnValue = Money.valueOf(returnValue.toString());
                    }
                    if (returnValue instanceof String && Enum.class.isAssignableFrom(parameterType)) {
                        returnValue = parameterType.getMethod("valueOf", String.class).invoke(parameterType, returnValue);
                    }
                    m.setAccessible(true);
                    m.invoke(dest, returnValue);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }

    public static void main(String[] args) {
        Order order = new Order();
        order.setBuyerMessage("hello");
        order.setActualFee(Money.valueOf(188));
        order.setGoodsFee(Money.valueOf(99));
        OrderVo vo = new OrderVo();
        copyProperties(order, vo);
        System.out.println(vo.getActualFee());
        System.out.println(vo.getBuyerMessage());
        Order o2 = new Order();
        copyProperties(vo, o2);
        System.out.println(o2.getActualFee());
        System.out.println(o2.getBuyerMessage());

        Product product = new Product();
        product.setLocation("NORMAL");
        com.ejushang.steward.ordercenter.domain.Product product1 = new com.ejushang.steward.ordercenter.domain.Product();
        copyProperties(product, product1);
        System.out.println(product1.getLocation());
    }
}
