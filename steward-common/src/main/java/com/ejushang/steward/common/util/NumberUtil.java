package com.ejushang.steward.common.util;

/**
 *
 */
public class NumberUtil {

    public static boolean isNullOrZero(Integer num) {
        return num == null || num.equals(0);
    }

    public static boolean isNullOrZero(Object o) {
        if (o == null) return true;
        if (o instanceof Number) {
            if (((Number) o).intValue() == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNullOrLessThanOne(Number num){
        return num==null||num.intValue()<1;
    }

    /**
     * 判断是否相等<p/>
     * 如果不为null则返回equals的值<br/>
     * 两个参数同时为null时返回true
     *
     * @param n1
     * @param n2
     * @return
     */
    public static boolean equals(Number n1, Number n2) {
        return (n1 != null && n1.equals(n2)) || (n1 == null && n2 == null);
    }

}