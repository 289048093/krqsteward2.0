package com.ejushang.steward.ordercenter.outplatforminvoke;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ApiInvokeException;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ProductInvokeWrapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-7
 * Time: 下午1:31
 */
public class ProductInviokeWrapperTest extends BaseTest {

    private Product product;

    private Shop shop;

    private Storage storage;

    private ShopProduct shopProduct;

    @Autowired
    private GeneralDAO generalDAO;

    @Before
    public void before() {

        product = (Product) generalDAO.searchUnique(new Search(Product.class).addFilterEqual("sku", "4897002404686"));
        if (product == null) {
            product = new Product();
            product.setName("product_name");
            product.setProductNo("bbb");
            product.setSku("4897002404686");
            generalDAO.saveOrUpdate(product);
        }
        storage = (Storage) generalDAO.searchUnique(new Search(Storage.class).addFilterEqual("productId", product.getId()));
        if (storage == null) {
            storage = new Storage();
            storage.setProductId(product.getId());
            Repository repository = new Repository();
            repository.setName("testRepo");
            repository.setCode("000000");
            repository.setProvinceId("00000");
            generalDAO.saveOrUpdate(repository);
            storage.setRepositoryId(repository.getId());
        }
        storage.setAmount(300);
        generalDAO.saveOrUpdate(storage);
        shop = (Shop) generalDAO.search(new Search(Shop.class).addFilterEqual("platformType", PlatformType.TAO_BAO)).get(0);
        shopProduct = new ShopProduct();
        shopProduct.setShopId(shop.getId());
        shopProduct.setShop(shop);
        shopProduct.setPutaway(true);
        shopProduct.setSynStatus(true);
        shopProduct.setProduct(product);
        shopProduct.setProdId(product.getId());
        shopProduct.setStoragePercent(50);
        generalDAO.saveOrUpdate(shopProduct);

        //初始化web环境
        HttpServletRequest request = new SedServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    @Transactional
    @Rollback
    public void testSyscInfo() throws ApiInvokeException {
            new ProductInvokeWrapper(product.getId(), shop.getId()).syncProductInfo();
            System.out.println(shopProduct.getPlatformUrl());
    }

    @Test
    @Transactional
    @Rollback
    public void testInit1() throws ApiInvokeException {
        new ProductInvokeWrapper(product.getSku(), shop.getId()).syncProductInfo();
        System.out.println(shopProduct.getPlatformUrl());
    }

    @Test
    @Transactional
    @Rollback
    public void testInit2() throws ApiInvokeException {
            new ProductInvokeWrapper(shopProduct).syncProductInfo();
            System.out.println(shopProduct.getPlatformUrl());
    }

    @Test
    @Transactional
    @Rollback
    public void testDelisting() throws ApiInvokeException {
            new ProductInvokeWrapper(product.getId(), shop.getId()).delisting();
    }

    @Test
    @Transactional
    @Rollback
    public void testListing() throws ApiInvokeException {
            new ProductInvokeWrapper(product.getId(), shop.getId()).listing();
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateShopStorage() throws ApiInvokeException {
            storage.setAmount(280);
            shopProduct.setStoragePercent(100);
            new ProductInvokeWrapper(product.getId(), shop.getId()).updateShopStorage();
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateShopStorageByNum() throws ApiInvokeException {
        storage.setAmount(280);
        shopProduct.setStoragePercent(100);
        new ProductInvokeWrapper(product.getId(), shop.getId()).updateShopStorage(280);
    }

}
