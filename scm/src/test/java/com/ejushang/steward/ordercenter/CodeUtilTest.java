package com.ejushang.steward.ordercenter;

import com.ejushang.steward.ordercenter.domain.ProductCategory;
import com.ejushang.steward.ordercenter.service.ProductCategoryService;
import com.ejushang.steward.ordercenter.util.CodeUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-4-12
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
public class CodeUtilTest extends BaseTest{
    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    public void test(){
//        String a1 = CodeUtil.createCode(null,1);
//        String a2 = CodeUtil.createCode(null,2);
//        String a3 = CodeUtil.createCode("AA",3);
//        String a4 = CodeUtil.createCode("B",4);
//        System.out.println(a1);
//        System.out.println(a2);
//        System.out.println(a3);
//        System.out.println(a4);

        String a1 = CodeUtil.updateCode("AAAAAC");
        System.out.println(a1);


    }

    @Test
    @Transactional
    @Rollback(true)
    public void testSave() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("第一类子类子类2");
        productCategoryService.save(productCategory,6);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testUpdate(){
        ProductCategory productCategory = productCategoryService.get(11);
        productCategory.setName("哈哈哈");
//        productCategoryService.update(productCategory,5);


    }

}
