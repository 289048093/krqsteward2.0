package com.ejushang.steward.scm.util;

import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * User:moon
 * Date: 14-4-10
 * Time: 下午1:53
 */
public class ReflectUtil {

    public static Method findUniqueMethod(Class<?> clazz, String name) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (Method method : methods) {
                if (name.equals(method.getName())) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }
}
