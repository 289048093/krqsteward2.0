package com.ejushang.steward.ordercenter.repositoryService;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.domain.Repository;
import com.ejushang.steward.ordercenter.service.RepositoryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

/**
 * User: tin
 * Date: 14-4-8
 * Time: 下午5:03
 */
@Transactional
public class RepositoryServiceTest extends BaseTest {
  @Autowired
  private RepositoryService repositoryService;
    @Autowired
    private GeneralDAO generalDAO;


    @Test
    @Transactional
    @Rollback(false)
    public void delete(){
       String id="1,2";

         repositoryService.deleteById(id);

    }
    @Test
    @Transactional
    @Rollback(false)
    public void save(){
       Repository repository=generalDAO.get(Repository.class, 1);
        repository.setName("测试仓库11号");
        repository.setCode("01");
        repository.setAddress("广东深圳");
        repository.setShippingComp("公司");
        repository.setChargeMobile("100000");
        repository.setChargePhone("121111");
        repository.setProvinceId("340000");

//repositoryService.save(repository);

    }
    @Test
    @Transactional
    @Rollback(false)
    public void list(){
      Page page=new Page(2,3);

        Repository repository=new Repository();
                                         String name="1";

      List<Repository> repositoryList=  repositoryService.findRepository(name,page);
        for(Repository repositor:repositoryList){
            System.out.println(repositor.getArea().getName());
        }
    }

  @Test
  @Transactional
    public void findAll(){
     List<Repository> repositoryList=repositoryService.findRepositoryAll();
     for(Repository repository:repositoryList){
         System.out.println(repository.getProvince().getName());
     }

  }

    @Test
    @Transactional
    @Rollback(false)
    public void getRepo(){
        List<Employee> employees=repositoryService.getRepoEmployee(18);
        for(Employee employee:employees){
            System.out.println(employee.getName()+"////"+employee.getId());

        }
            System.out.println(employees.size());
    }
}
