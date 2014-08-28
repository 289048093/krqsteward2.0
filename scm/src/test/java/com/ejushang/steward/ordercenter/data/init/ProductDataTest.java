package com.ejushang.steward.ordercenter.data.init;

import com.ejushang.steward.common.domain.Province;
import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.transportation.BrandService;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: tin
 * Date: 14-5-9
 * Time: 上午11:52
 */
@Transactional
public class ProductDataTest extends BaseTest {

    @Autowired
    GeneralDAO generalDAO;
    //日期格式
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //生成数据的条数
    private int time = 10;
    //平台
    private PlatformType platformType = PlatformType.TAO_BAO;
    //订单类型
    private OrderType orderType=OrderType.NORMAL;
    //订单状态
    private OrderStatus orderStatus=OrderStatus.WAIT_PROCESS;
    //订单退货状态
    private OrderReturnStatus orderReturnStatus= OrderReturnStatus.NORMAL;
    //订单生成类型
    private OrderGenerateType orderGenerateType= OrderGenerateType.MANUAL_CREATE;
    //系统优惠金额
    private Money sharedDiscountFee=Money.valueOf(0);
    //分摊邮费金额
    private Money sharedPostFee=Money.valueOf(0);
    //实付金额
    private Money actualFee=Money.valueOf(0);
    //订单货款
    private Money goodsFee=Money.valueOf(0);



    @Test
    @Transactional
    @Rollback(false)
    public void supplier() {
        Search search = new Search(Supplier.class);
        List<Supplier> suppliers = generalDAO.search(search);
        String[] suName = new String[suppliers.size()];
        String[] suCode = new String[suppliers.size()];
        for (int i = 0; i < suppliers.size(); i++) {
            Supplier supplier = suppliers.get(i);
            suName[i] = supplier.getName();
            suCode[i] = supplier.getCode();
        }
        String[] name = new String[time];
        String[] code = new String[time];
        Supplier supplier = new Supplier();
        name = ProductDataTest.stringRandom(time, suName);
        code = ProductDataTest.stringRandom(time, suCode);
        for (int i = 0; i < time; i++) {
            Supplier supplier1 = new Supplier();
            supplier1.setCode(code[i]);
            supplier1.setName(name[i]);
            generalDAO.saveOrUpdate(supplier1);
        }

    }

    @Test
    @Transactional
    @Rollback(false)
    public void cate() {

        List<ProductCategory> productCategories = generalDAO.search(new Search(ProductCategory.class));
        String[] cateNames = new String[productCategories.size()];
        String[] cateCodes = new String[productCategories.size()];
        for (int i = 0; i < productCategories.size(); i++) {
            cateNames[i] = productCategories.get(i).getName();
//            cateCodes[i] = productCategories.get(i).getCode();
        }
        String[] name = new String[time];
        String[] code = new String[time];
        name = ProductDataTest.stringRandom(time, cateNames);
        code = ProductDataTest.stringRandom(time, cateCodes);
        for (int i = 0; i < time; i++) {
            ProductCategory productCategory = new ProductCategory();
//            productCategory.setCode(code[i]);
            productCategory.setName(name[i]);
            generalDAO.saveOrUpdate(productCategory);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void brandInit() {

        Search search1 = new Search(Brand.class);
        List<Brand> brands = generalDAO.search(search1);
        String[] brandName = new String[brands.size()];
        String[] brandCode = new String[brands.size()];
        for (int i = 0; i < brands.size(); i++) {
            Brand brand = brands.get(i);
            brandName[i] = brand.getName();
            brandCode[i] = brand.getCode();
        }
        String[] name = new String[time];
        name = stringRandom(time, brandName);
        String[] code = new String[time];
        code = stringRandom(time, brandCode);
        List<Supplier> suppliers = generalDAO.search(new Search(Supplier.class).addSortDesc("id"));
        for (int i = 0; i < time; i++) {
            Brand brand = new Brand();
            brand.setName(name[i]);
            brand.setCode(code[i]);
            brand.setSupplierId(suppliers.get(i).getId());
            brand.setPaymentType("POST_COVER");
            generalDAO.saveOrUpdate(brand);

        }


    }

    @Test
    @Transactional
    @Rollback(false)
    public void productInit() {

        List<Product> products = generalDAO.search(new Search(Product.class));
        List<Brand> brands = generalDAO.search(new Search(Brand.class).addSortDesc("id"));
        List<ProductCategory> productCategories = generalDAO.search(new Search(ProductCategory.class).addSortDesc("id"));
        String[] prodName = new String[products.size()];
        String[] skus = new String[products.size()];
        Integer[] cateIds = new Integer[productCategories.size()];
        String[] productNos = new String[products.size()];
        for (int i = 0; i < productCategories.size(); i++) {
            cateIds[i] = productCategories.get(i).getId();
        }
        for (int i = 0; i < products.size(); i++) {
            prodName[i] = products.get(i).getName();
            skus[i] = products.get(i).getSku();
            productNos[i] = products.get(i).getProductNo();

        }
        String[] prodName1 = new String[time];
        String[] sku = new String[time];
        String[] productNo = new String[time];
        prodName1 = ProductDataTest.stringRandom(time, prodName);
        sku = ProductDataTest.stringRandom(time, skus);
        productNo = ProductDataTest.stringRandom(time, productNos);
        for (int i = 0; i < time; i++) {
            Product product = new Product();
            product.setBrandId(brands.get(i).getId());
            product.setName(prodName1[i]);
            product.setProductNo(productNo[i]);
            product.setSku(sku[i]);
            product.setDescription(RandomStringUtils.randomAlphabetic(12));
            product.setPicUrl(RandomStringUtils.randomAlphabetic(6));
            product.setMarketPrice(Money.valueOf(intRandom(4)));
//            product.setImportPrice(Money.valueOf(intRandom(4)));
            product.setMinimumPrice(Money.valueOf(intRandom(4)));
            product.setLocation(ProductLocation.valueOf("NORMAL"));
            product.setStyle(ProductStyle.valueOf("A"));
            product.setCategoryId(cateIds[i]);
            generalDAO.saveOrUpdate(product);
        }

    }

    @Test
    @Transactional
    @Rollback(false)
    public void repository() {

        List<Repository> repositories = generalDAO.search(new Search(Repository.class));
        List<Province> provices = generalDAO.search(new Search(Province.class).addSortDesc("id"));
        String[] repoNames = new String[repositories.size()];
        String[] repoCodes = new String[repositories.size()];
        for (int i = 0; i < repositories.size(); i++) {
            repoNames[i] = repositories.get(i).getName();
            repoCodes[i] = repositories.get(i).getCode();
        }
        String[] repoName = new String[time];
        repoName = stringRandom(time, repoNames);
        String[] repoCode = new String[time];
        repoCode = stringRandom(time, repoCodes);

        for (int i = 0; i < time; i++) {
            Repository repository1 = new Repository();
            repository1.setName(repoName[i]);
            repository1.setCode(repoCode[i]);
            repository1.setAddress(RandomStringUtils.randomAlphanumeric(11));
            repository1.setShippingComp("shunfeng");
            repository1.setChargeMobile(intRandom(7) + "");
            repository1.setChargePhone(intRandom(7) + "");
            repository1.setProvinceId(provices.get(i).getId());
            generalDAO.saveOrUpdate(repository1);

        }

    }

    @Test
    @Transactional
    @Rollback(false)
    public void storageInit() {

        List<Repository> repositories = generalDAO.search(new Search(Repository.class).addSortDesc("id"));
        System.out.println(repositories.size());
        List<Product> products = generalDAO.search(new Search(Product.class).addSortDesc("id"));
        System.out.println(products.size());
        List<Storage> storages = generalDAO.search(new Search(Storage.class));

        Integer[] repoIds = new Integer[storages.size()];
        Integer[] prodIds = new Integer[storages.size()];

        for (int i = 0; i < storages.size(); i++) {
            repoIds[i] = storages.get(i).getRepositoryId();
            prodIds[i] = storages.get(i).getProductId();
        }


        for (int i = 0; i < time; i++) {
            Storage storage = new Storage();
            storage.setProductId(products.get(i).getId());
            storage.setRepositoryId(repositories.get(i).getId());
            storage.setAmount(intRandom(3));

            generalDAO.saveOrUpdate(storage);

        }
    }





    public static String[] stringRandom(int time, String[] name1) {

        String[] name = new String[time];
        for (int i = 0; i < time; i++) {
            name[i] = RandomStringUtils.randomAlphabetic(7);
            for (int j = 0; j < name1.length; j++) {
                if (name[i].equals(name1[j])) {
                    i = (i == 0 ? 0 : i - 1);
                    break;
                }
            }

        }
        return name;
    }



    public static Integer intRandom(int time) {
        String price = "";

        Boolean result = true;
        while (result) {
            price = RandomStringUtils.randomNumeric(time);

            if (price.charAt(0) != '0') {
                result = false;
            }
        }

        return Integer.parseInt(price);
    }

    public static String stringRandom(int time) {
        return RandomStringUtils.randomAlphanumeric(time);
    }


}
