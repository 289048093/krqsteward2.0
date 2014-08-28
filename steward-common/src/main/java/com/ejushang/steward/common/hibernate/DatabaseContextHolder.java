package com.ejushang.steward.common.hibernate;

/**
 * User: Baron.Zhang
 * Date: 2014/5/22
 * Time: 14:54
 */
public class DatabaseContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setCustomerType(String customerType){
        contextHolder.set(customerType);
    }

    public static String getCustomerType(){
        return contextHolder.get();
    }

    public static void clearCustomerType(){
        contextHolder.remove();
    }
}
