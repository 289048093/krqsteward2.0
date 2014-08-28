package com.ejushang.steward.ordercenter.transportation;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.domain.Brand;
import com.ejushang.steward.ordercenter.service.transportation.BrandService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

/**
 * User: 龙清华
 * Date: 14-4-8
 * Time: 下午5:03
 */
@Transactional
public class BrandServiceTest extends BaseTest {
    @Autowired
    private BrandService brandService;

    @Test
    @Rollback(false)
    public void TestDelete() {
    }

    @Test
    @Rollback(false)
    public void testSave() {
        Brand brand = new Brand();
        brand.setName("for xinxin de ceshi");
        brandService.save(brand);
        System.out.println("插入成功，id为" + brand.getId());
    }

    @Test
    @Rollback(false)
    public void testFindBrandALl() {
        Page page = new Page(1, 2);
        System.out.println(brandService.findBrandAll(null, page));
    }
    @Test
     @Rollback(false)
     public void testGet() {
        System.out.println(brandService.get(1));
//        System.out.println(brandService.fin("测试品牌"));
    }
    @Test
    @Rollback(false)
    public void testIsExist() {
        System.out.println(brandService.isBrandExistByName("测试品牌",-1));
        System.out.println(brandService.isBrandExist(1));
    }
}
