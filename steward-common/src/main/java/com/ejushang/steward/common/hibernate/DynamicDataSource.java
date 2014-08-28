package com.ejushang.steward.common.hibernate;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * User: Baron.Zhang
 * Date: 2014/5/22
 * Time: 14:53
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DatabaseContextHolder.getCustomerType();
    }
}
