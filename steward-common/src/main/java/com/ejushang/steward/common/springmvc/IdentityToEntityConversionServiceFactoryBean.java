package com.ejushang.steward.common.springmvc;

import com.ejushang.steward.common.domain.Conf;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ConversionService工厂类,实例化ConversionService之前注册所有ID转实体类的转换器
 * User: liubin
 * Date: 14-3-13
 */
public class IdentityToEntityConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

    private static final Logger log = LoggerFactory.getLogger(IdentityToEntityConversionServiceFactoryBean.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private SessionFactory sessionFactory;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public void setConverters(Set<?> converters) {
        if(initialized.compareAndSet(false, true)) {
            Set<Object> allConverters = new HashSet<Object>();
            allConverters.addAll(initEntityConverters());
            allConverters.addAll(converters);
            super.setConverters(allConverters);
        } else {
            super.setConverters(converters);
        }
    }

    @Override
    public void afterPropertiesSet() {
        if(initialized.compareAndSet(false, true)) {
            super.setConverters(initEntityConverters());
        }
        super.afterPropertiesSet();
    }

    private Set<Object> initEntityConverters() {
        Set<Object> converters = new HashSet<Object>();

        //参数绑定的时候金额转成货币对象
        converters.add(new MoneyConverter());

        //所有在springmvc的controller方法中加了@ModelAttribute注解的实体类,都需要在这里注册转换器
        try {
            converters.add(new IdentityToEntityConverterFactory());
        } catch (Exception e) {
            log.error("为实体类注册id转对象的转换器的时候发生错误", e);
        }


        return converters;
    }

    /**
     * ID转实体类的转换器的工厂类
     */
    private class IdentityToEntityConverterFactory implements ConverterFactory<String, EntityClass<Integer>> {

        private  Map converterMap = new HashMap();

        private IdentityToEntityConverterFactory() throws Exception {

            //所有在springmvc的controller方法中加了@ModelAttribute注解的实体类,都需要在这里注册转换器
            Map<String,ClassMetadata> allClassMetadata = sessionFactory.getAllClassMetadata();
            for(Map.Entry<String, ClassMetadata> entry : allClassMetadata.entrySet()) {
                Class<?> entityClass = Class.forName(entry.getValue().getEntityName());
                addConverter((Class<? extends EntityClass<Integer>>) entityClass);
            }

        }

        private void addConverter(Class<? extends EntityClass<Integer>> entityClass) {
            converterMap.put(entityClass, new IdentityToEntityConverter(entityClass));
        }

        @Override
        public <T extends EntityClass<Integer>> Converter<String, T> getConverter(Class<T> targetType) {
            return (Converter<String, T>) converterMap.get(targetType);
        }
    }


    /**
     * ID转实体类的转换器
     * @param <T> 实体类class
     */
    private class IdentityToEntityConverter<T extends EntityClass<Integer>> implements Converter<String, T> {

        private Class<T> entityClass;

        public IdentityToEntityConverter(Class<T> entityClass) {
            this.entityClass = entityClass;
        }

        @Override
        public T convert(String source) {
            if(!StringUtils.isNumeric(source)) {
                try {
                    return entityClass.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException("实例化实体类的时候发生错误,class:" + entityClass.getName(), e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("实例化实体类的时候发生错误,class:" + entityClass.getName(), e);
                }
            }
            T t = generalDAO.get(entityClass, Integer.parseInt(source));
            if(t == null) {
                log.warn("根据ID查询出来的对象为null, id[{}], class[{}]", source, entityClass.getName());
            }
            return t;
        }
    }


}
