/* Copyright 2013 David Wolverton
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ejushang.steward.common.genericdao.dao.hibernate;


import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.genericdao.search.ExampleOptions;
import com.ejushang.steward.common.genericdao.search.Filter;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.genericdao.search.SearchResult;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.common.util.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface for general Data Access Object that can be used for any type domain
 * object. A single instance implementing this interface can be used for
 * multiple types of domain objects.
 *
 * 源码地址: http://hibernate-generic-dao.googlecode.com/svn/tags/1.2.0
 *
 * @author dwolverton
 */
@SuppressWarnings("unchecked")
@Repository
public class GeneralDAO extends HibernateBaseDAO {

    // 删除order by字句使用的正则表达式
    private static Pattern removeOrderByPattern = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);

    /**
     * Returns the total number of results that would be returned using the
     * given <code>Search</code> if there were no paging or maxResults limits.
     */
	public int count(Search search) {
		return _count(search);
	}

//    /**
//     * <p>
//     * Get the entity with the specified type and id from the datastore.
//     *
//     * <p>
//     * If none is found, return null.
//     */
//	public <T> T find(Class<T> type, Serializable id) {
//		return (T) _get(type, id);
//	}
//
//    /**
//     * Get all entities of the specified type from the datastore that have one
//     * of these ids. An array of entities is returned that matches the same
//     * order of the ids listed in the call. For each entity that is not found in
//     * the datastore, a null will be inserted in its place in the return array.
//     */
//	public <T> T[] find(Class<T> type, Serializable... ids) {
//		return _get(type, ids);
//	}

    /**
     * Get a list of all the objects of the specified type.
     */
	public <T> List<T> findAll(Class<T> type) {
		return _all(type);
	}

    /**
     * Flushes changes in the Hibernate session to the datastore.
     */
	public void flush() {
		_flush();
	}

//    /**
//     * <p>
//     * Get a reference to the entity with the specified type and id from the
//     * datastore.
//     *
//     * <p>
//     * This does not require a call to the datastore and does not populate any
//     * of the entity's values. Values may be fetched lazily at a later time.
//     * This increases performance if a another entity is being saved that should
//     * reference this entity but the values of this entity are not needed.
//     *
//     * @throws a
//     *             HibernateException if no matching entity is found
//     */
//	public <T> T getReference(Class<T> type, Serializable id) {
//		return _load(type, id);
//	}
//
//    /**
//     * <p>
//     * Get a reference to the entities of the specified type with the given ids
//     * from the datastore. An array of entities is returned that matches the
//     * same order of the ids listed in the call.
//     *
//     * <p>
//     * This does not require a call to the datastore and does not populate any
//     * of the entities' values. Values may be fetched lazily at a later time.
//     * This increases performance if a another entity is being saved that should
//     * reference these entities but the values of these entities are not needed.
//     *
//     * @throws a
//     *             HibernateException if any of the matching entities are not
//     *             found.
//     */
//	public <T> T[] getReferences(Class<T> type, Serializable... ids) {
//		return _load(type, ids);
//	}

    /**
     * Returns <code>true</code> if the object is connected to the current
     * Hibernate session.
     */
	public boolean isAttached(Object entity) {
		return _sessionContains(entity);
	}

    /**
     * Refresh the content of the given entity from the current datastore state.
     */
	public void refresh(Object... entities) {
		_refresh(entities);
	}

    /**
     * Remove the specified entity from the datastore.
     *
     * @return <code>true</code> if the entity is found in the datastore and
     *         removed, <code>false</code> if it is not found.
     */
	public boolean remove(Object entity) {
		return _deleteEntity(entity);
	}

    /**
     * Remove all of the specified entities from the datastore.
     */
	public void remove(Object... entities) {
		_deleteEntities(entities);
	}

    /**
     * Remove the entity with the specified type and id from the datastore.
     *
     * @return <code>true</code> if the entity is found in the datastore and
     *         removed, <code>false</code> if it is not found.
     */
	public boolean removeById(Class<?> type, Serializable id) {
		return _deleteById(type, id);
	}

    /**
     * Remove all the entities of the given type from the datastore that have
     * one of these ids.
     */
	public void removeByIds(Class<?> type, Serializable... ids) {
		_deleteById(type, ids);
	}

//    /**
//     * <p>
//     * If the id of the entity is null or zero, add it to the datastore and
//     * assign it an id; otherwise, update the corresponding entity in the
//     * datastore with the properties of this entity. In either case the entity
//     * passed to this method will be attached to the session.
//     *
//     * <p>
//     * If an entity to update is already attached to the session, this method
//     * will have no effect. If an entity to update has the same id as another
//     * instance already attached to the session, an error will be thrown.
//     *
//     * @return <code>true</code> if create; <code>false</code> if update.
//     */
//	public boolean save(Object entity) {
//		return _saveOrUpdateIsNew(entity);
//	}
//
//    /**
//     * <p>
//     * For each entity, if the id of the entity is null or zero, add it to the
//     * datastore and assign it an id; otherwise, update the corresponding entity
//     * in the datastore with the properties of this entity. In either case the
//     * entity passed to this method will be attached to the session.
//     *
//     * <p>
//     * If an entity to update is already attached to the session, this method
//     * will have no effect. If an entity to update has the same id as another
//     * instance already attached to the session, an error will be thrown.
//     */
//	public boolean[] save(Object... entities) {
//		return _saveOrUpdateIsNew(entities);
//	}

    /**
     * Search for objects given the search parameters in the specified
     * <code>Search</code> object.
     */
	public List search(Search search) {
        if(search.getPagination() == null) {
            return _search(search);
        } else {
            return searchAndCount(search).getResult();
        }
	}

    /**
     * Returns a <code>SearchResult</code> object that includes both the list of
     * results like <code>search()</code> and the total length like
     * <code>count()</code>.
     */
	private SearchResult searchAndCount(Search search) {
        if (search == null)
            throw new NullPointerException("Search is null.");
        if (search.getSearchClass() == null)
            throw new NullPointerException("Search class is null.");

        Page page = search.getPagination();
        search.setPage(page.getPageNo() - 1);
        search.setMaxResults(page.getLimit());

        SearchResult searchResult = _searchAndCount(search);

        page.setTotalCount(searchResult.getTotalCount());
        page.setResult(searchResult.getResult());

		return searchResult;
	}

    /**
     * Search for a single result using the given parameters.
     */
	public Object searchUnique(Search search) {
		return _searchUnique(search);
	}

    /**
     * Generates a search filter from the given example using default options.
     */
	public Filter getFilterFromExample(Object example) {
		return _getFilterFromExample(example);
	}

    /**
     * Generates a search filter from the given example using the specified options.
     */
	public Filter getFilterFromExample(Object example, ExampleOptions options) {
		return _getFilterFromExample(example, options);
	}

    //===================== 以下为新增方法 =====================//



    /**
     * 直接使用hibernate的saveOrUpdate方法
     * @param entity
     */
    public void saveOrUpdate(Object entity) {
        if(entity instanceof OperableData) {
            //设置时间
            OperableData operableData = (OperableData) entity;
            Serializable id = getMetadataUtil().getId(entity);
            Date now = EJSDateUtils.getCurrentDate();
            if(NumberUtil.isNullOrZero(id)) {
                operableData.setCreateTime(now);
            }
            operableData.setUpdateTime(now);
            //拿到当前登录用户,设置operatorId
            Employee employee = SessionUtils.getEmployee();
            if(employee != null) {
                operableData.setOperatorId(employee.getId());
            }
        }
		_saveOrUpdate(entity);
	}

    /**
     * 直接使用hibernate的saveOrUpdate方法
     * @param entities
     */
    public void saveOrUpdate(Object... entities) {
        for(Object entity : entities) {
            saveOrUpdate(entity);
        }
    }

    /**
     * 直接使用hibernate的saveOrUpdate方法
     * @param entities
     */
    public <T> void saveOrUpdate(List<T> entities) {
        for(Object entity : entities) {
            saveOrUpdate(entity);
        }
    }

    /**
     * 直接调用session.get
     * @param type
     * @param id
     * @param <T>
     * @return
     */
	public <T> T get(Class<T> type, Serializable id) {
		return _get(type, id);
	}

    /**
     * 批量加载
     * @param type
     * @param ids
     * @param <T>
     * @return
     */
	public <T> T[] get(Class<T> type, Serializable... ids) {
		return _get(type, ids);
	}

    /**
     * 执行查询hql
     * @param ql
     * @param page 可以为null
     * @param args
     * @param <RT>
     * @return
     */
    public <RT> List<RT> query(String ql, Page page, Object... args) {
        return queryWithPrepare(ql, page, null, args);
    }

    /**
     * 执行查询hql
     * @param ql
     * @param page 可以为null
     * @param queryPrepare 可以对被创建的query执行一些操作,可以为null
     * @param args
     * @param <RT>
     * @return
     */
    public <RT> List<RT> queryWithPrepare(String ql, Page page, QueryPrepare queryPrepare, Object... args) {
        Query query = getSession().createQuery(ql);
        for (int i = 0; i < args.length; i++) {
            query.setParameter(i, args[i]);
        }
        if(queryPrepare != null) {
            queryPrepare.prepare(query);
        }
        if(page == null) {
            return query.list();
        }

        if(this.hasGroupBy(ql)) {
            //count
            StringBuilder countQL = new StringBuilder("select count(1) ");
            countQL.append(ql.substring(ql.indexOf("from"), ql.lastIndexOf("order")));
            countQL = new StringBuilder(removeFetchInCountQl(countQL.toString()));
            Query countQuery = getSession().createQuery(countQL.toString());
            for (int i = 0; i < args.length; i++) {
                countQuery.setParameter(i, args[i]);
            }
            List count = countQuery.list();
            page.setTotalCount(count.size());

        } else {
            //count
            page.setTotalCount(this.count(query.getQueryString(), args));
        }

        query.setFirstResult(page.getStart());
        query.setMaxResults(page.getLimit());
        page.setResult(query.list());
        return page.getResult();
    }

    /**
     * hql语句如果用了fetch,删除查询总条数的fetch语句,否则会报错
     * @param countQL
     * @return
     */
    private String removeFetchInCountQl(String countQL) {
        if(StringUtils.contains(countQL, " fetch ")) {
            countQL = (countQL.toString().replaceAll("fetch", ""));
        }
        return countQL;
    }

    /**
     * 执行insert或update语句
     * @param ql
     * @param args
     * @return
     */
    public int update(String ql, Object... args) {
        Query query = super.getSession().createQuery(ql);
        for(int i = 0; i < args.length; i++){
            query.setParameter(i, args[i]);
        }
        return query.executeUpdate();
    }

    /**
     * 查询总条数
     * @param ql
     * @param args
     * @return
     */
    public int count(String ql, Object... args) {
        Query query = getSession().createQuery(ql);
        for (int i = 0; i < args.length; i++) {
            query.setParameter(i, args[i]);
        }
        String countQueryString = " select count(1) " + this.removeSelect(this.removeOrderBy(ql));
        countQueryString = removeFetchInCountQl(countQueryString);

        Query countQuery = getSession().createQuery(countQueryString);
        for (int i = 0; i < args.length; i++) {
            countQuery.setParameter(i, args[i]);
        }
        List countList = countQuery.list();
        long totalCount = 0l;
        if(countList != null && countList.size() >= 1){
            if(this.hasGroupBy(ql)){
                totalCount = (long)countList.size();
            }else{
                totalCount = (Long)countList.get(0);
            }
        }

        return Long.valueOf(totalCount).intValue();
    }

    private boolean hasGroupBy(String ql) {
        if(ql != null && !"".equals(ql)){
            if(ql.indexOf("group by") > -1){
                return true;
            }
        }
        return false;
    }

    /**
     * 去除ql语句中的select子句
     * @param hql 查询语句
     * @return 删除后的语句
     */
    private String removeSelect(String hql) {
        Assert.hasText(hql);
        int beginPos = hql.toLowerCase().indexOf("from");
        Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
        return hql.substring(beginPos);
    }

    /**
     * 删除ql语句中的order by字句
     * @param ql 查询语句
     * @return 删除后的查询语句
     */
    private String removeOrderBy(String ql){
        if(ql != null && !"".equals(ql)){
            Matcher m = removeOrderByPattern.matcher(ql);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, "");
            }
            m.appendTail(sb);
            return sb.toString();
        }
        return "";
    }
}
