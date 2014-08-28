package com.ejushang.steward.openapicenter.zy.api.aceess.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Baron.Zhang
 * Date: 2014/8/5
 * Time: 14:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface ApiListField {

    /** JSON列表属性映射名称 **/
    public String value() default "";

}
