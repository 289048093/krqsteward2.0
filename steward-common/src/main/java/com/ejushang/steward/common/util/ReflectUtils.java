package com.ejushang.steward.common.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 13-12-16
 * Time: 上午9:16
 */
public class ReflectUtils {

    /**
     * 利用反射执行方法
     * @param obj 执行对象
     * @param methodName 方法名
     * @param paramValue 参数值
     * @return
     */
    public static void executeMethod2(Object obj,String methodName,Object paramValue) throws Exception {
        Class clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        Method method = null;
        for(int i = 0; i < methods.length; i++){
            if(StringUtils.equals(methodName,methods[i].getName())
                    && methods[i].getParameterTypes().length == 1){
                method = methods[i];
            }
        }
        if(method != null){
            method.invoke(obj,paramValue);
        }
        else{
            throw new Exception(obj.getClass().getName()+"不存在名为"+methodName+"("+paramValue.getClass().getName()+")的方法");
        }
    }

    public void setInt(int x){
        System.out.println(x);
    }

    /**
     * 利用反射执行方法
     * @param obj 执行对象
     * @param methodName 方法名
     * @param paramValue 参数值
     * @return
     */
    public static void executeMethod(Object obj,String methodName,Object paramValue) throws Exception {
        Class clazz = obj.getClass();
        Method method = clazz.getMethod(methodName,paramValue.getClass());
        method.invoke(obj,paramValue);
    }

    /**
     * 批量利用反射执行set方法
     * @param obj 执行对象
     * @param argsMap 保存多组方法名及其参数值的map
     * @throws Exception
     */
    public static void executeMethods(Object obj,Map<String,Object> argsMap) throws Exception {
        for(Map.Entry<String,Object> entry : argsMap.entrySet()){
            String methodName = entry.getKey();
            methodName = strucTaoSetter(methodName);
            Object paramValue = entry.getValue();
            if(paramValue != null){
                ReflectUtils.executeMethod2(obj, methodName, paramValue);
            }
        }
    }

    /**
     * 特殊处理：淘宝中，类似于s_prov_name这种字段，淘宝setter方式为：setsProvName()
     */
    public static String strucTaoSetter(String methodName){
        if (StringUtils.isBlank(methodName)) return StringUtils.EMPTY;

        if (methodName.contains("_") && methodName.split("_")[0].length() == 1) {
            return StringUtilsEjs.camelCase4Do(StringUtilsEjs.camelCase("set" + methodName));
        }

        if (methodName.contains(".") && methodName.split("\\.")[0].length() == 1) {
            return StringUtilsEjs.camelCase4Do(StringUtilsEjs.camelCase("set" + methodName));
        }

        return StringUtilsEjs.camelCase4Do(StringUtilsEjs.camelCase("set" + StringUtilsEjs.capitalize(methodName)));
    }
}

