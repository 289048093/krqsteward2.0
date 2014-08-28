package com.ejushang.steward.ordercenter.supplierService;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.domain.Contract;
import com.ejushang.steward.ordercenter.domain.Supplier;
import com.ejushang.steward.ordercenter.service.transportation.SupplierService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * User: Shiro
 * Date: 14-4-12
 * Time: 下午2:42
 */
public class SupplierServiceTest extends BaseTest {
    @Autowired
    private SupplierService supplierService;

    @Test
    @Transactional
    @Rollback(false)
    public void testSaveOrUpdate() {
        Supplier supplier = new Supplier();
        supplier.setId(1);
        supplier.setName("戴德a");
        // supplier.setCode(SequenceGenerator.getInstance().getNextWithoutCache(SystemConfConstant.NEXT_SUPPLIER_NO));
        supplierService.saveSupplier(supplier);
    }


    @Test
    @Transactional
    @Rollback(false)
    public void testDelete() {
        supplierService.deleteById(2);
    }

    /**
     * 根据条件查询供应商
     */
    @Test
    public void testGetByKey() {
        Page page = new Page(1, 10);
        Supplier supplier = new Supplier();
        //  supplier.setName("a");
        System.out.println(new JsonResult(true).addList(supplierService.listPageByName("", page)));
    }

    /**
     * 根据条件查询合同
     */
    @Test
    public void testFindContractByAll() {
        Page page = new Page(1, 10);
        Integer id = 1;
        String code = "123";
        System.out.println(new JsonResult(true).addList(supplierService.findContractByAll(id, code, page)));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testSaveContract() {
        Contract contract = new Contract();
        contract.setId(43);
        contract.setCode("0987654321");
        contract.setBeginTime(new Date());
        supplierService.saveContract(contract);
    }

    /**
     * 判断合同是否到期
     */
    @Test
    public void testIsContractExpire() {
        boolean flag = supplierService.isContractExpire(1);
        System.out.print(flag);
    }
}
