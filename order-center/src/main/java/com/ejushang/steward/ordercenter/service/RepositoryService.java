package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.service.EmployeeService;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.ordercenter.domain.Repository;
import com.ejushang.steward.ordercenter.domain.RepositoryCharger;
import com.ejushang.steward.ordercenter.domain.Storage;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tin
 * Date: 14-4-9
 * Time: 下午4:55
 */
@Service
@Transactional
public class RepositoryService {
    private static final Logger logger = Logger.getLogger(RepositoryService.class);
    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 查询仓库名称
     *
     * @param name 仓库名参数
     * @param page 分页参数
     * @return 返回仓库List
     */
    @Transactional(readOnly = true)
    public List<Repository> findRepository(String name, Page page) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数仓库名name:[%s],Page:[%s]", name, page.toString()));
        }

        StringBuilder hql = new StringBuilder("select r from Repository r where 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (StringUtils.isNotBlank(name)) {
            hql.append(" and r.name like ? ");
            params.add("%" + name + "%");
        }
        Employee employee = SessionUtils.getEmployee();
        if (employee.isRepositoryEmployee()) {     //仓库人员只能查看自己仓库的数据
            hql.append(" and r.id in ( select rc.repositoryId from RepositoryCharger rc where rc.chargerId=? )");
            params.add(employee.getId());
        }

        //noinspection unchecked
        return generalDAO.query(hql.toString(), page, params.toArray());

    }

    /**
     * 通过name和code查询仓库
     *
     * @param repository 仓库实体
     * @return 返回仓库集合
     */
    @Transactional(readOnly = true)
    public List<Repository> findRepositoryByRepository(Repository repository) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数仓库名Repository[%s]", repository.toString()));
        }
        Search search = new Search(Repository.class).setCacheable(true);
        if (!StringUtils.isBlank(repository.getName())) {
            search.addFilterEqual("name", repository.getName());
        }
        if (!StringUtils.isBlank(repository.getCode())) {
            search.addFilterEqual("code", repository.getCode());
        }
        //noinspection unchecked
        return generalDAO.search(search);

    }

    /**
     * 查询所有的仓库
     *
     * @return 返回仓库List
     */
    @Transactional(readOnly = true)
    public List<Repository> findRepositoryAll() {
        Search search = new Search(Repository.class).setCacheable(true);
        //noinspection unchecked
        return generalDAO.search(search);
    }


    /**
     * 批量删除仓库（当库存存在时，仓库不可删除）
     *
     * @param ids 仓库ID
     */
    public void deleteById(String ids) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数Integer类型的id数组[%s]", ids));
        }
        if (NumberUtil.isNullOrZero(ids)) {
            throw new StewardBusinessException("删除仓库操作的id数组为空");
        }
        String[] stringIds = ids.split(",");
        Integer[] id = new Integer[stringIds.length];
        for (int i = 0; i < id.length; i++) {
            id[i] = Integer.parseInt(stringIds[i]);
        }
        for (int i : id) {
            Search search = new Search(Storage.class);
            search.addFilterEqual("repositoryId", i);
            @SuppressWarnings("unchecked") List<Storage> storageList = generalDAO.search(search);//当库存有记录时，不能删除仓库
            if (storageList.size() > 0) {
                throw new StewardBusinessException("仓库存在库存，不可删除操作");
            } else {
                generalDAO.removeById(Repository.class, i);
            }
        }

    }

    /**
     * 添加仓库（名称和编码有重复的不可以添加）
     *
     * @param repository 仓库实体       Integer[] chargerIds
     */
    public void add(Repository repository, Integer[] chargerIds) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数Repository:[%s]", repository.toString()));
        }
        Search search = new Search(Repository.class);
        search.addFilterEqual("name", repository.getName());

        @SuppressWarnings("unchecked") List<Repository> repositories = generalDAO.search(search);
        if (repositories.size() > 0) {
            throw new StewardBusinessException("已经存在此仓库");
        }
        Repository repository1 = findByCode(repository.getCode());
        if (repository1 != null) {
            throw new StewardBusinessException("已存在此编码的仓库");
        }
        Employee employee = SessionUtils.getEmployee();
        repository.setOperatorId(employee != null ? employee.getId() : null);
        generalDAO.saveOrUpdate(repository);

        saveRepositoryCharger(chargerIds, repository.getId());
    }


    /**
     * 更新仓库
     *
     * @param repository 仓库实体
     */
    public void save(Repository repository, Integer[] chargerIds) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("方法参数Repository[%s]", repository.toString()));
        }
        if (!StringUtils.isBlank(repository.getName())) {
            Repository repository1 = new Repository();
            repository1.setName(repository.getName());
            List<Repository> repositories = findRepositoryByRepository(repository1);
            for (Repository r : repositories) {
                if (!r.getId().equals(repository.getId())) {
                    throw new StewardBusinessException("已经有该仓库名称");
                }
            }
        }
        if (!StringUtils.isBlank(repository.getCode())) {
            Repository repository1 = new Repository();
            repository1.setCode(repository.getCode());
            List<Repository> repositories = findRepositoryByRepository(repository1);
            for (Repository r : repositories) {
                if (!r.getId().equals(repository.getId())) {
                    throw new StewardBusinessException("已经有该仓库编码");
                }
            }
        }
        generalDAO.saveOrUpdate(repository);

        generalDAO.getSession().createQuery("delete from RepositoryCharger where repositoryId=" + repository.getId()).executeUpdate();
        if (chargerIds != null && chargerIds.length > 0) {
            saveRepositoryCharger(chargerIds, repository.getId());
        }
    }

    /**
     * 查询指定 仓库编码 的仓库
     *
     * @param code 仓库编码
     * @return 返回仓库实体
     */
    public Repository findByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return (Repository) generalDAO.searchUnique(new Search(Repository.class).setCacheable(true).addFilterEqual("code", code));
    }

    /**
     * 查询指定 仓库编码 的仓库
     *
     * @param name 仓库名
     * @return 仓库实体
     */
    public Repository findByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return (Repository) generalDAO.searchUnique(new Search(Repository.class).addFilterEqual("name", name));
    }


    @Transactional(readOnly = true)
    public Repository get(Integer id) {
        return generalDAO.get(Repository.class, id);
    }

    private void saveRepositoryCharger(Integer[] chargerIds, Integer repoId) {
        for (Integer chargerId : chargerIds) {
            RepositoryCharger repositorieChargers = findRepositoryChargerByChargerId(chargerId);
            Employee employee = employeeService.get(chargerId);
            if (!employee.isRepositoryEmployee()) {
                throw new StewardBusinessException(String.format("用户[%s]不是仓库人员！", employee.getUsername()));
            }
            if (repositorieChargers != null) {
                throw new StewardBusinessException(String.format("用户[%s]已经在管理仓库了，不能重复分配！", employee.getUsername()));
            }
            RepositoryCharger repositoryCharger = new RepositoryCharger();
            repositoryCharger.setChargerId(chargerId);
            repositoryCharger.setRepositoryId(repoId);
            generalDAO.saveOrUpdate(repositoryCharger);
        }
    }

    private RepositoryCharger findRepositoryChargerByChargerId(Integer chargerId) {
        Search search = new Search(RepositoryCharger.class);
        search.addFilterEqual("chargerId", chargerId);
        return (RepositoryCharger) generalDAO.searchUnique(search);
    }

    public List<RepositoryCharger> getRepositoryByEmployeeId(Integer employeeId, Integer repoId) {
        Search search = new Search(RepositoryCharger.class);
        search.addFilterEqual("chargerId", employeeId);
        search.addFilterNotEqual("repositoryId", repoId);
        return generalDAO.search(search);
    }

    @Transactional(readOnly = true)
    public List<Employee> getRepoEmployee(Integer repoId) {
        StringBuilder hql = new StringBuilder("select rc.chargerId from RepositoryCharger rc where rc.repositoryId=?");
        List<Object> params = new ArrayList<Object>();
        params.add(repoId);
        List<Integer> ids = generalDAO.query(hql.toString(), null, params.toArray());
        List<Employee> employees = new ArrayList<Employee>();
        for (Integer id : ids) {
            if (id != null) {
                Employee employee = employeeService.get(id);
                employees.add(employee);
            }
        }
        return employees;
    }
}
