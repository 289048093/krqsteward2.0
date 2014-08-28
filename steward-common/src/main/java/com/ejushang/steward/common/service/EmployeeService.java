package com.ejushang.steward.common.service;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.shiro.UrlPermission;
import com.ejushang.uams.api.dto.*;
import com.ejushang.uams.client.UamsClient;
import com.ejushang.uams.client.UamsClientContext;
import com.ejushang.uams.exception.UamsClientException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: liubin
 * Date: 14-4-8
 */
@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private UamsClient uamsClient = UamsClientContext.createUamsClient();

    private EmployeeCache employeeCache = null;

    @Autowired
    private ResourceService resourceService;

    /**
     * 获取用户缓存
     * <p/>
     * 防止employeeCache比uams先初始化好
     *
     * @return
     */
    private EmployeeCache getEmployeeCache() {
        if (employeeCache == null) {
            synchronized (this) {
                if (employeeCache == null) {
                    employeeCache = new EmployeeCache();
                }
            }
        }
        return employeeCache;
    }


    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     * @throws UamsClientException
     */
    public Employee login(String username, String password) throws UamsClientException {

        EmployeeDto employeeDto = uamsClient.login(username, password);
        Employee employee = new Employee();
        employee.setId(employeeDto.getId());
        employee.setStatus(employeeDto.getStatus());
        employee.setUsername(employeeDto.getUsername());
        employee.setRoot(employeeDto.getRootUser());
        employee.setName(employeeDto.getName());


        if (!employee.isRoot()) {

            //缓存用户权限
            List<ResourceDto> employeeResources = uamsClient.findEmployeeResources(employee.getId());
            Map<String, UrlPermission> permissionsCache = new HashMap<String, UrlPermission>();
            for (ResourceDto resourceDto : employeeResources) {
                for (OperationDto operationDto : resourceDto.getOperationList()) {
                    String url = operationDto.getUrl();
                    permissionsCache.put(url, new UrlPermission(url));
                }
            }

            employee.setResourceList(employeeResources);
            employee.setPermissionsCache(permissionsCache);

            //缓存用户角色
            List<RoleDto> roles = uamsClient.findEmployeeRoles(employee.getId());
            employee.setRoles(roles);

        }

        return employee;

    }

    /**
     * 根据ID查询用户
     *
     * @param id
     * @return
     */
    public Employee get(Integer id) {
        EmployeeDto employeeInfo = getEmployeeCache().getById(id);
        if (employeeInfo == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        if(!employeeInfo.getRootUser()){
            try {
                employee.setRoles(uamsClient.findEmployeeRoles(id));
            } catch (UamsClientException e) {
                throw new StewardBusinessException(e.getMessage(),e);
            }
        }
        employee.setStatus(employeeInfo.getStatus());
        employee.setUsername(employeeInfo.getUsername());
        employee.setRoot(employeeInfo.getRootUser());
        employee.setName(employeeInfo.getName());
        return employee;
    }

    /**
     * 通过用户名和姓名查找用户
     * <p/>
     * 如果username为null,则通过name查找；如果name为null，则通过username查找
     *
     * @param username
     * @param name
     * @return
     */
    public List<EmployeeDto> findEmployeeByName(String username, String name) {
        username = StringUtils.trimToEmpty(username);
        name = StringUtils.trimToEmpty(name);
        Collection<EmployeeDto> allEmployee = getEmployeeCache().getAllEmployee();
        List<EmployeeDto> res = new ArrayList<EmployeeDto>();
        for (EmployeeDto e : allEmployee) {
            if (e.getUsername().contains(username) && e.getName().contains(name)) {
                res.add(e);
            }
        }
        return res;
    }

    /**
     * 通过电话查找用户
     * <p/>
     *
     * @param tel

     * @return
     */
    public Employee findEmployeeByTel(String tel) {
        List<EmployeeDto> employeeDtos = findEmployeeByName(null,null);
        Employee employee = null;
        for (EmployeeDto employeeDto:employeeDtos) {
            if (!StringUtils.isBlank(employeeDto.getTel()) && employeeDto.getTel().equals(tel)) {
                employee = new Employee();
                employee.setId(employeeDto.getId());
                employee.setStatus(employeeDto.getStatus());
                employee.setUsername(employeeDto.getUsername());
                employee.setRoot(employeeDto.getRootUser());
                employee.setName(employeeDto.getName());
                break;
            }
        }
        if (employee!=null) {
            return employee;
        }
        log.error("查询电话为"+tel+"的用户出错");
        return null;
    }

    /**
     * 通过角色名称查找用户
     * <p/>
     *
     * @param roleName

     * @return
     */
    public List<Employee> findEmployeeByRole(String roleName) throws UamsClientException {
        List<Employee> employees = new ArrayList<Employee>();
        List<EmployeeDto> employeeDtos = uamsClient.findEmployeeByRole(roleName);
        for (EmployeeDto employeeDto:employeeDtos) {
            Employee employee = new Employee();
            employee.setId(employeeDto.getId());
            employee.setStatus(employeeDto.getStatus());
            employee.setUsername(employeeDto.getUsername());
            employee.setRoot(employeeDto.getRootUser());
            employee.setName(employeeDto.getName());
            employees.add(employee);
        }
        return employees;
    }

    public List<Employee> findEmp(String searchType,String searchValue) throws UamsClientException {
        List<Employee> employees = new ArrayList<Employee>();
        List<EmployeeDto> employeeDtos =findEmpByName(searchType,searchValue);
        for (EmployeeDto employeeDto:employeeDtos) {
            Employee employee = new Employee();
            employee.setId(employeeDto.getId());
            employee.setStatus(employeeDto.getStatus());
            employee.setUsername(employeeDto.getUsername());
            employee.setRoot(employeeDto.getRootUser());
            employee.setName(employeeDto.getName());
            employees.add(employee);
        }
        return employees;
    }

    /**
     * 重新加载缓存
     */
    public void reloadCache() {
        getEmployeeCache().loadData();
    }

    /**
     * 用户缓存
     * User: Sed.Li(李朝)
     */
    private class EmployeeCache {
        private Map<Integer, EmployeeDto> idMap = null;
        private Map<String, EmployeeDto> userNameMap = null;

        private ReadWriteLock lock = new ReentrantReadWriteLock();

        private EmployeeCache() {
            loadData();
        }

        /**
         * 从server获取数据
         */
        private void loadData() {
            Map<Integer, EmployeeDto> idMapTmp = new HashMap<Integer, EmployeeDto>();
            Map<String, EmployeeDto> userNameMapTmp = new HashMap<String, EmployeeDto>();
            List<EmployeeDto> allEmployeeTmp = null;
            try {
                allEmployeeTmp = uamsClient.findEmployeeByName(null, null);
            } catch (UamsClientException e) {
                log.error("查询用户失败", e);
            }
            if (allEmployeeTmp == null) {
                allEmployeeTmp = new ArrayList<EmployeeDto>();
            }
            for (EmployeeDto e : allEmployeeTmp) {
                idMapTmp.put(e.getId(), e);
                userNameMapTmp.put(e.getUsername(), e);
            }
            lock.writeLock().lock();
            try {
                idMap = idMapTmp;
                userNameMap = userNameMapTmp;
//                allEmployee = allEmployeeTmp;
            } finally {
                lock.writeLock().unlock();
            }
        }

        private void put(EmployeeDto ed) {
            lock.writeLock().lock();
            try {
                if (idMap.get(ed.getId()) != null) {
                    return;
                }
                idMap.put(ed.getId(), ed);
                userNameMap.put(ed.getUsername(), ed);
//                allEmployee.add(ed);
            } finally {
                lock.writeLock().unlock();
            }
        }

        public EmployeeDto getById(Integer id) {
            EmployeeDto ed = null;
            lock.readLock().lock();
            try {
                ed = idMap.get(id);
                if (ed != null) {
                    return ed;
                }
            } finally {
                lock.readLock().unlock();
            }
            return loadFromServerById(id);
        }

        /**
         * 通过id从uams服务器获取用户，并加入到缓存
         *
         * @param id
         * @return
         */
        private EmployeeDto loadFromServerById(Integer id) {
            EmployeeDto ed = null;
            try {
                EmployeeInfoDto employeeInfo = uamsClient.getEmployeeInfo(id);
                if (employeeInfo == null) {
                    return null;
                }
                ed = new EmployeeDto();
                BeanUtils.copyProperties(employeeInfo, ed);
                put(ed);
            } catch (UamsClientException e) {
                log.warn(String.format("通过Id[%d]获取用户出错", id), e);
            }
            return ed;
        }

        public EmployeeDto getByUsername(String username) {
            lock.readLock().lock();
            try {
                EmployeeDto ed = userNameMap.get(username);
                if (ed != null) {
                    return ed;
                }
            } finally {
                lock.readLock().unlock();
            }
            return loadFromServerByUsername(username);
        }

        /**
         * 通过用户名从uams服务器获取用户,并加入到缓存
         *
         * @param username
         * @return
         */
        private EmployeeDto loadFromServerByUsername(String username) {
            try {
                List<EmployeeDto> eds = uamsClient.findEmployeeByName(username, null);
                if (eds != null && !eds.isEmpty()) {
                    for (EmployeeDto e : eds) {
                        if (StringUtils.equals(e.getUsername(), username)) {
                            put(e);
                            return e;
                        }
                    }
                }
            } catch (UamsClientException e) {
                log.warn(String.format("通过用户名[%s]获取用户出错", username), e);
            }
            return null;
        }


        public Collection<EmployeeDto> getAllEmployee() {
            lock.readLock().lock();
            try {
                return idMap.values();
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    public List<EmployeeDto> findEmpByName(String searchType, String searchValue) throws UamsClientException {
        String userName = null;
        String realName = null;
        if (!StringUtils.isBlank(searchType) && searchType.equals("userName") && !StringUtils.isBlank(searchValue)) {
            userName = searchValue;
        }
        if (!StringUtils.isBlank(searchType) && searchType.equals("realName") && !StringUtils.isBlank(searchValue)) {
            realName = searchValue;
        }

        return findEmployeeByName(userName, realName);
    }

    public List<ResourceDto> findResourceByEmployeeId(Integer employeeId) throws UamsClientException {

        if (employeeId != null) {
            Employee employee = get(employeeId);
            List<ResourceDto> resourceDtos;
            if (employee.isRoot()) {
                resourceDtos = resourceService.findAllResource();
            } else {
                resourceDtos = uamsClient.findEmployeeResources(employeeId);
                for(Iterator<ResourceDto> iterator = resourceDtos.iterator(); iterator.hasNext();) {
                    ResourceDto resourceDto = iterator.next();
                    if(resourceDto.getHasEntryOperation() != null && !resourceDto.getHasEntryOperation()) {
                        iterator.remove();
                    }
                }
            }
            Collections.sort(resourceDtos, new Comparator<ResourceDto>() {
                @Override
                public int compare(ResourceDto o1, ResourceDto o2) {
                    if (o1.getId().equals(o2.getId())) {
                        return 0;
                    } else if (o1.getId()>o2.getId()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            return resourceDtos;
        }
        return resourceService.findAllResource();
    }

    //修改用户密码
    public Boolean updatePassword(Integer id, String oldPassword, String newPassword) throws UamsClientException {
        Boolean b = uamsClient.updatePassword(id, oldPassword, newPassword);
        return b;
    }

}
