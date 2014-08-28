package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.ordercenter.constant.InOutStockType;
import com.ejushang.steward.ordercenter.constant.StorageFlowType;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.util.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * User: Sed.Li(李朝)
 * Date: 14-4-10
 * Time: 下午8:49
 */
@Service
public class StorageFlowService {

    private static final Logger log = LoggerFactory.getLogger(StorageFlowService.class);

    @Autowired
    private GeneralDAO generalDAO;

    /**
     * 添加库存日志记录
     *
     * @param storageId
     * @param num
     * @param type
     * @param inOutStockType
     * @return
     * @throws AuthenticationException 未登录
     */
    @Transactional
    public StorageFlow add(Integer storageId, Integer beforeAmount, Integer num, StorageFlowType type, InOutStockType inOutStockType, String desc) {
        return add(storageId, beforeAmount, num, type, inOutStockType, null, desc);
    }

    /**
     * 添加库存日志记录
     *
     * @param orderId
     * @param storageId
     * @param num
     * @param type
     * @param inOutStockType
     * @return
     */
    @Transactional
    public StorageFlow add(Integer storageId, Integer beforeAmount, Integer num, StorageFlowType type, InOutStockType inOutStockType, Integer orderId, String desc) {
        if (storageId == null) {
            throw new StewardBusinessException("storage的ID不能为空");
        }

        StorageFlow sf = new StorageFlow();
        Date now = new Date();
        sf.setBeforeAmount(beforeAmount);
        sf.setAmount(num);
        sf.setCreateTime(now);
        Employee employee = SessionUtils.getEmployee();
        if (employee == null) {
            throw new StewardBusinessException("您还未登录，请先登录再操作");
        }
        sf.setOperatorId(employee.getId());
        sf.setStorageId(storageId);
        sf.setType(type);
        sf.setInOutStockType(inOutStockType);
        sf.setOrderId(orderId);
        sf.setDesc(desc);
        generalDAO.saveOrUpdate(sf);
        return sf;
    }

    /**
     * 入/出库记录查询，通过sku
     *
     * @param sku
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<StorageFlow> pageList(String sku, StorageFlowType type, Date minDate, Date maxDate, Integer repositoryId, Page page) {
        Search search = new Search(StorageFlow.class);
        if (type != null) {
            search.addFilterEqual("type", type);
        }
        if (StringUtils.isNotBlank(sku)) {
            search.addFilterEqual("storage.product.sku", sku);
        }
        if (minDate != null) {
            search.addFilterGreaterOrEqual("createTime", minDate);
        }
        if (maxDate != null) {
            search.addFilterLessOrEqual("createTime", maxDate);
        }
        if (repositoryId != null) {
            search.addFilterEqual("storage.repositoryId", repositoryId);
        }
        search.addPagination(page);
        //noinspection unchecked
        return generalDAO.search(search);
    }


    /**
     * 按最小/最大出入库时间查询出入库日志记录
     *
     * @param minDate      时间下限，可以为null
     * @param maxDate      时间上限,可以为null
     * @param repositoryId 仓库Id,可以为null
     * @param page         分页，如果为null则不分页，即查询全部
     * @return
     */
    @Transactional(readOnly = true)
    public List<Storage> pageListGroupProduct(StorageFlowType type, Date minDate, Date maxDate, Integer repositoryId, Page page) {
        StringBuilder hql = new StringBuilder("select s_ from StorageFlow sf right join sf.storage s_ where 1=1 ");
//        StringBuilder hql = new StringBuilder("select s_ from StorageFlow sf right join sf.storage s_ where s_.product.deleted=0 ");
        StringBuilder countHql = new StringBuilder("select count(distinct sf.storage.id) from StorageFlow sf where 1=1 "); // hql优化
//        StringBuilder countHql = new StringBuilder("select count(distinct sf.storage.id) from StorageFlow sf where sf.storage.product.deleted=0 ");
        List<String> keys = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        if (type != null) {
            hql.append(" and sf.type=:type ");
            countHql.append(" and sf.type=:type ");
            keys.add("type");
            values.add(type);
        }
        if (minDate != null) {
            hql.append(" and sf.createTime >:min");
            countHql.append(" and sf.createTime >:min ");
            keys.add("min");
            values.add(minDate);
        }
        if (maxDate != null) {
            hql.append(" and sf.createTime<:max ");
            countHql.append(" and sf.createTime<:max ");
            keys.add("max");
            values.add(maxDate);
        }
        if (repositoryId != null) {
            hql.append(" and sf.storage.repositoryId =:repoId");
            countHql.append(" and sf.storage.repositoryId =:repoId");
            keys.add("repoId");
            values.add(repositoryId);
        }
        Employee employee = SessionUtils.getEmployee();
        if (employee.isRepositoryEmployee()) {     //仓库人员只能查看自己管理的仓库的日志
            //noinspection unchecked
//            List<Repository> repositories =  generalDAO.search(new Search(Repository.class).addFilterEqual("chargePersonId", employee.getId()));
            List<Repository> repositories =  new ArrayList<Repository>();
            List<RepositoryCharger> repositoryChargers = generalDAO.search(new Search(RepositoryCharger.class).addFilterEqual("chargerId", employee.getId()));
            for(RepositoryCharger repositoryCharger:repositoryChargers){
                repositories.add(repositoryCharger.getRepository());
            }
            if (repositories.isEmpty()) {
                return new ArrayList<Storage>();
            }
            String repoIds = Joiner.join(repositories, ",", new Joiner.Operator<Repository>() {
                @Override
                public String convert(Repository repository) {
                    return repository.getId().toString();
                }
            });
            hql.append(" and sf.storage.repositoryId in (").append(repoIds).append(")");
            countHql.append(" and sf.storage.repositoryId in (").append(repoIds).append(")");
        }
        hql.append(" group by s_ ");
//        hql.append(" group by sf.storage.product,sf.storage.repository");
        Query query = generalDAO.getSession().createQuery(hql.toString());
        Query countQuery = generalDAO.getSession().createQuery(countHql.toString());

        for (int i = 0; i < keys.size(); i++) {
            query.setParameter(keys.get(i), values.get(i));
            countQuery.setParameter(keys.get(i), values.get(i));
        }
        if (page != null) { //分页
            query.setFirstResult(page.getStart());
            query.setMaxResults(page.getPageSize());
            int total = ((Number) countQuery.uniqueResult()).intValue();
            page.setTotalCount(total);
        }
        //noinspection unchecked
        List<Storage> storages = query.list();
        if (page != null) {
            page.setResult(storages);
        }
        return storages;
    }

    /**
     * 通过产品id和仓库id查找库存日志记录
     * 如果参数都为null，则查询出所有
     *
     * @param productId    产品id为null，则查询出仓库下的所有记录
     * @param repositoryId 仓库id为null，则查询出该产品的所有记录
     * @return
     */
    @Transactional(readOnly = true)
    public List<StorageFlow> findByProductIdAndRepositoryId(Integer productId, Integer repositoryId) {
        Search search = new Search(StorageFlow.class);
        if (productId != null) {
            search.addFilterEqual("productId", productId);
        }
        if (repositoryId != null) {
            search.addFilterEqual("repositoryId", repositoryId);
        }
        //noinspection unchecked
        return generalDAO.search(search);
    }
}
