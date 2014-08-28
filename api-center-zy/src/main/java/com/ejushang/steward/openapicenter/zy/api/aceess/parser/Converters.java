package com.ejushang.steward.openapicenter.zy.api.aceess.parser;

import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Product;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiListField;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.Constants;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 转换工具类
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 14:44
 */
public class Converters {
    /**
     * 是否对JSON返回的数据类型进行校验，默认不校验。给内部测试JSON返回时用的开关。
     * 规则：返回的"基本"类型只有String,Long,Boolean,Date,采取严格校验方式，如果类型不匹配，报错
     */
    public static boolean isCheckJsonType = false;

    private static final Set<String> baseFields = new HashSet<String>();

    static {
        baseFields.add("errorCode");
        baseFields.add("msg");
        baseFields.add("subCode");
        baseFields.add("subMsg");
        baseFields.add("body");
        baseFields.add("params");
        baseFields.add("success");
        baseFields.add("topForbiddenFields");
    }

    private Converters() {
    }

    /**
     * 使用指定 的读取器去转换字符串为对象。
     *
     * @param <T>    领域泛型
     * @param clazz  领域类型
     * @param reader 读取器
     * @return 领域对象
     * @throws com.taobao.api.ApiException
     */
    public static <T> T convert(Class<T> clazz, Reader reader) throws ApiException {
        T rsp = null;

        try {
            rsp = clazz.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor pd : pds) {
                Method method = pd.getWriteMethod();
                if (method == null) { // ignore read-only fields
                    continue;
                }

                String itemName = pd.getName();
                String listName = null;

                Field field;
                if (baseFields.contains(itemName) && ZiYouResponse.class.isAssignableFrom(clazz)) {
                    field = ZiYouResponse.class.getDeclaredField(itemName);
                } else {
                        field = findField(clazz,itemName);
                }
                if(field==null){
                    continue;
                }

                ApiField jsonField = field.getAnnotation(ApiField.class);
                if (jsonField != null) {
                    itemName = jsonField.value();
                }
                ApiListField jsonListField = field.getAnnotation(ApiListField.class);
                if (jsonListField != null) {
                    listName = jsonListField.value();
                }

                if (!reader.hasReturnField(itemName)) {
                    if (listName == null || !reader.hasReturnField(listName)) {
                        continue; // ignore non-return field
                    }
                }

                Class<?> typeClass = field.getType();
                // 目前
                if (String.class.isAssignableFrom(typeClass)) {
                    Object value = reader.getPrimitiveObject(itemName);
                    if (value instanceof String) {
                        method.invoke(rsp, value.toString());
                    } else {
                        if (isCheckJsonType && value != null) {
                            throw new ApiException(itemName + " is not a String");
                        }
                        if (value != null) {
                            method.invoke(rsp, value.toString());
                        } else {
                            method.invoke(rsp, "");
                        }
                    }
                } else if (Long.class.isAssignableFrom(typeClass)) {
                    Object value = reader.getPrimitiveObject(itemName);
                    if (value instanceof Long) {
                        method.invoke(rsp, (Long) value);
                    } else {
                        if (isCheckJsonType && value != null) {
                            throw new ApiException(itemName + " is not a Number(Long)");
                        }
                        if (StringUtils.isNumeric(value)) {
                            method.invoke(rsp, Long.valueOf(value.toString()));
                        }
                    }
                } else if (Integer.class.isAssignableFrom(typeClass)) {
                    Object value = reader.getPrimitiveObject(itemName);
                    if (value instanceof Integer) {
                        method.invoke(rsp, (Integer) value);
                    } else {
                        if (isCheckJsonType && value != null) {
                            throw new ApiException(itemName + " is not a Number(Integer)");
                        }
                        if (StringUtils.isNumeric(value)) {
                            method.invoke(rsp, Integer.valueOf(value.toString()));
                        }
                    }
                } else if (Boolean.class.isAssignableFrom(typeClass)) {
                    Object value = reader.getPrimitiveObject(itemName);
                    if (value instanceof Boolean) {
                        method.invoke(rsp, (Boolean) value);
                    } else {
                        if (isCheckJsonType && value != null) {
                            throw new ApiException(itemName + " is not a Boolean");
                        }
                        if (value != null) {
                            method.invoke(rsp, Boolean.valueOf(value.toString()));
                        }
                    }
                } else if (Double.class.isAssignableFrom(typeClass)) {
                    Object value = reader.getPrimitiveObject(itemName);
                    if (value instanceof Double) {
                        method.invoke(rsp, (Double) value);
                    } else {
                        if (isCheckJsonType && value != null) {
                            throw new ApiException(itemName + " is not a Double");
                        }
                    }
                } else if (Number.class.isAssignableFrom(typeClass)) {
                    Object value = reader.getPrimitiveObject(itemName);
                    if (value instanceof Number) {
                        method.invoke(rsp, (Number) value);
                    } else {
                        if (isCheckJsonType && value != null) {
                            throw new ApiException(itemName + " is not a Number");
                        }
                    }
                } else if (Date.class.isAssignableFrom(typeClass)) {
                    DateFormat format = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
                    format.setTimeZone(TimeZone.getTimeZone(Constants.DATE_TIMEZONE));
                    Object value = reader.getPrimitiveObject(itemName);
                    if (value instanceof String) {
                        method.invoke(rsp, format.parse(value.toString()));
                    }
                } else if (List.class.isAssignableFrom(typeClass)) {
                    Type fieldType = field.getGenericType();
                    if (fieldType instanceof ParameterizedType) {
                        ParameterizedType paramType = (ParameterizedType) fieldType;
                        Type[] genericTypes = paramType.getActualTypeArguments();
                        if (genericTypes != null && genericTypes.length > 0) {
                            if (genericTypes[0] instanceof Class<?>) {
                                Class<?> subType = (Class<?>) genericTypes[0];
                                List<?> listObjs = reader.getListObjects(listName, itemName, subType);
                                if (listObjs != null) {
                                    method.invoke(rsp, listObjs);
                                }
                            }
                        }
                    }
                } else if (Enum.class.isAssignableFrom(typeClass)) {
                    Object value = reader.getPrimitiveObject(itemName);
                    if (value instanceof String) {
                        Method meth = typeClass.getMethod("valueOf", String.class);
                        Object enumValue = meth.invoke(typeClass, value);
                        method.invoke(rsp, enumValue);
                    } else {
                        if (isCheckJsonType && value != null) {
                            throw new ApiException(itemName + " is not a Enum");
                        }
                    }
                } else if (Money.class.isAssignableFrom(typeClass)) {
                    Object value = reader.getPrimitiveObject(itemName);
                    if (value instanceof String) {
                        Method meth = typeClass.getMethod("valueOf", value.getClass());
                        Object enumValue = meth.invoke(typeClass, value);
                        method.invoke(rsp, enumValue);
                    } else if (value instanceof Double) {
                        Method meth = typeClass.getMethod("valueOf", double.class);
                        Object enumValue = meth.invoke(typeClass, value);
                        method.invoke(rsp, enumValue);
                    } else if (value instanceof Long) {
                        Method meth = typeClass.getMethod("valueOf", long.class);
                        Object enumValue = meth.invoke(typeClass, value);
                        method.invoke(rsp, enumValue);
                    } else {
                        if (isCheckJsonType && value != null) {
                            throw new ApiException(itemName + " is not a Money");
                        }
                    }
                } else {
                    Object obj = reader.getObject(itemName, typeClass);
                    if (obj != null) {
                        method.invoke(rsp, obj);
                    }
                }
            }
        } catch (Exception e) {
            throw new ApiException(e);
        }

        return rsp;
    }

    private static Field findField(Class<?> clazz,String fieldName){
        if(clazz.getName().equals("java.lang.Object")){
            return null;
        }
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
           return findField(clazz.getSuperclass(), fieldName);
        }
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Field field = Product.class.getSuperclass().getDeclaredField("test");
        System.out.println(field.getName());
    }
}
