package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.ordercenter.domain.Order;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 集合或数组的Join方法，类似JS的 Array.join()
 * <p/>
 * User:  Sed.Lee(李朝)
 * Date: 14-6-25
 * Time: 上午9:54
 */
public class Joiner {

    public static String join(Iterable it, Object separator) {
        return com.google.common.base.Joiner.on(separator.toString()).join(it);
    }

    public static <T> String join(T[] t, Object separator) {
        return com.google.common.base.Joiner.on(separator.toString()).join(t);
    }

    /**
     * 对集合中的元素的属性进行join
     *
     * @param itb
     * @param separator
     * @param operator
     * @param <T>
     * @return
     */
    public static <T> String join(Iterable<T> itb, Object separator, Operator<T> operator) {
        StringBuilder s = new StringBuilder();
        for (Iterator<T> it = itb.iterator(); it.hasNext(); ) {
            T t = it.next();
            s.append(operator.convert(t));
            if (it.hasNext()) {
                s.append(separator);
            }
        }
        return s.toString();
    }

    /**
     * 对数组中的元素的数据进行join
     *
     * @param ts
     * @param separator
     * @param operator
     * @param <T>
     * @return
     */
    public static <T> String join(T[] ts, Object separator, Operator<T> operator) {
        return join(Arrays.asList(ts), separator, operator);
    }

    /**
     * 对元素进行加工处理，返回字符串
     *
     * @param <T>
     */
    public static interface Operator<T> {
        /**
         * @param t
         * @return 返回对参数处理后的字符串
         */
        String convert(T t);
    }


    public static void main(String[] args) {
        Order order = new Order();
        order.setOrderNo("aaaaa");

        Order order2 = new Order();
        order2.setOrderNo("bbbbb");

        Order[] os = {order, order2};

        System.out.println(join(os, ",", new Operator<Order>() {
            @Override
            public String convert(Order order) {
                return order.getOrderNo();
            }
        }));
    }
}
