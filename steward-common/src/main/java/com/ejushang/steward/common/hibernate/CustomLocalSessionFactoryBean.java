package com.ejushang.steward.common.hibernate;

import com.ejushang.steward.common.util.Money;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 用来注册Hibernate的UserType
 * User: liubin
 * Date: 14-4-2
 */
public class CustomLocalSessionFactoryBean extends LocalSessionFactoryBean {

    private LocalSessionFactoryBuilder sfb;

    @Override
    public void afterPropertiesSet() throws IOException {
        super.afterPropertiesSet();

        registerUserType();

        //此时才真正生成sessionFactory对象
        Field sessionFactoryField = FieldUtils.getDeclaredField(LocalSessionFactoryBean.class, "sessionFactory", true);
        try {
            sessionFactoryField.set(this, sfb.buildSessionFactory());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("注册自定义类之后,通过反射对LocalSessionFactoryBean对象的sessionFactory属性赋值的时候发生错误");
        }


    }

    private void registerUserType() {
        sfb.registerTypeOverride(new MoneyType(), new String[]{Money.class.getName()});
    }

    @Override
    protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder sfb) {
        //prevent build session factory
        this.sfb = sfb;
        return null;
    }
}
