package com.ejushang.steward.openapicenter.zy.api.aceess.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据结构属性注解。
 *
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface ApiField {

    /** JSON属性映射名称 **/
    public String value() default "";

}