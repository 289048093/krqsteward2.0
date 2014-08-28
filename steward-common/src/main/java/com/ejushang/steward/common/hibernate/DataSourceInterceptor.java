package com.ejushang.steward.common.hibernate;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

/**
 * User: Baron.Zhang
 * Date: 2014/5/22
 * Time: 15:02
 */

public class DataSourceInterceptor {

    public void setDataSource(JoinPoint jp){
        DatabaseContextHolder.setCustomerType("dataSource");
    }

    public void setDataSourceTb(JoinPoint jp){
        DatabaseContextHolder.setCustomerType("dataSourceTb");
    }
}
