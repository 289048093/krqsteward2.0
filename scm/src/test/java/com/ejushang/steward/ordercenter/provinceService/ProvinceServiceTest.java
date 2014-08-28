package com.ejushang.steward.ordercenter.provinceService;

import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.service.ProvinceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: tin
 * Date: 14-4-10
 * Time: 下午2:31
 */

@Transactional
public class ProvinceServiceTest extends BaseTest {
    @Autowired
    private ProvinceService provinceService;

@Test
    @Transactional
@Rollback(false)
    public void findAll(){
           provinceService.findProvinceAll();


}

}
