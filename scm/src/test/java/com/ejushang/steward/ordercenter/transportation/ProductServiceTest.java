package com.ejushang.steward.ordercenter.transportation;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.ProductLocation;
import com.ejushang.steward.ordercenter.constant.ProductStyle;
import com.ejushang.steward.ordercenter.domain.Product;
import com.ejushang.steward.ordercenter.domain.ShopProduct;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

/**
 * User: tin
 * Date: 14-4-8
 * Time: 下午5:03
 */
public class ProductServiceTest extends BaseTest {
    @Autowired
    private ProductService productService;

    @Rollback(false)
    @Test
    public void testDelete() {
        int[] id = {4, 1};
//      productService.delete(id);
    }

    @Test
    public void testSave() {
        Product product = new Product();
        product.setBrandId(19);
        product.setBrandId(1);
        product.setName("测试商品001");
        product.setProductNo("123123");
        product.setSku("123213as1d");
        product.setCategoryId(1);
        product.setDescription("测试使用");
        product.setPicUrl("");//暂时没有
        product.setMarketPrice(Money.valueOf(11));
//        product.setImportPrice(Money.valueOf(11));
        product.setMinimumPrice(Money.valueOf(11));
        product.setColor("红色");
        product.setWeight("123");
        product.setBoxSize("123");
        product.setSpeci("123");
        product.setOrgin("123");
        product.setStyle(ProductStyle.enumValueOf("A"));
        product.setLocation(ProductLocation.enumValueOf("正常商品"));
//        try {
//            productService.save(product,null,null,null);
//        } catch (ApiInvokeException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }
    @Test
    public void testSaveProductPlatform() {
        ShopProduct shopProduct =new ShopProduct();
        productService.saveProductPlatform(null);
    }
    @Rollback(false)
    @Test
    public void testFindProductByAll() {
        Page page = new Page(1, 2);
        System.out.println(productService.findProductByAll(null, null,null, page));
    }

    @Rollback(false)
    @Test
    public void testFindProductByPlatform() {
    }
    @Rollback(false)
    @Test
    public void testFindProductBySKU() {
    }
    @Rollback(false)
    @Test
    public void testFindProductPlatform() {
    }
    @Rollback(false)
    @Test
    public void testGet() {
    }
    @Rollback(false)
    @Test
    public void isProductExist() {
    }
    @Rollback(false)
    @Test
    public void isProductExistBySKU() {
    }



}
