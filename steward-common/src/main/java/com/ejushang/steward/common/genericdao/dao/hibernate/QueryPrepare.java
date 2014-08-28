package com.ejushang.steward.common.genericdao.dao.hibernate;

import org.hibernate.Query;

/**
 * User: liubin
 * Date: 14-7-17
 */
public interface QueryPrepare {

    void prepare(Query query);

}
