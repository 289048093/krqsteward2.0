package com.ejushang.steward.ordercenter.service.transportation;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.*;
import com.ejushang.steward.ordercenter.constant.InOutStockType;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.domain.Product;
import com.ejushang.steward.ordercenter.domain.Repository;
import com.ejushang.steward.ordercenter.service.RepositoryService;
import com.ejushang.steward.ordercenter.service.StorageService;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ApiInvokeException;
import com.ejushang.steward.ordercenter.util.BeanUtils;
import com.ejushang.steward.ordercenter.vo.ProductVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-20
 * Time: 上午10:24
 */
@Service
@Transactional
public class ProductLoadService {
    private static final Logger logger = LoggerFactory.getLogger(ProductLoadService.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private StorageService storageService;

    public void save(com.ejushang.steward.openapicenter.zy.api.aceess.domain.Product product) {
        Product newProduct = new Product();
        //copy商品属性
        BeanUtils.copyProperties(product, newProduct);
        //根据仓库编码查询仓库
        Repository repository = repositoryService.findByCode(product.getRepositoryCode());
        save(newProduct,repository.getId());
    }

    public void update(com.ejushang.steward.openapicenter.zy.api.aceess.domain.Product product){
        Product oldProduct = findProductBySKU(product.getSku());
        //先得到原ID，防止copy属性时被覆盖
        Integer oldId = oldProduct.getId();
        //copy商品属性
        BeanUtils.copyProperties(product,oldProduct);
        oldProduct.setId(oldId);
        //根据仓库编码查询仓库
        Repository repository = repositoryService.findByCode(product.getRepositoryCode());
        updateProduct(oldProduct, repository.getId());
    }

    /**
     * 逻辑删除多个商品
     *
     * @param skuArary 商品SKU集合
     */
    public void delete(String[] skuArary) {
        for (String sku : skuArary) {
            Product product = findProductBySKU(sku);
            existNormalMealsetByProductId(product.getId());
        }
        String hqlStart = "update Product p set p.deleted = ? where p.sku in (";
        String parameter = buildParameter(skuArary);
        String hqlEnd = ")";
        String hql = new StringBuilder("").append(hqlStart).append(parameter).append(hqlEnd).toString();
        generalDAO.update(hql,true);
    }

    /**
     * 逻辑删除一个商品
     *
     * @param sku 商品sku
     */
    public void delete(String sku) {
        Product product = findProductBySKU(sku);
        existNormalMealsetByProductId(product.getId());
        product.setDeleted(true);
        generalDAO.saveOrUpdate(product);
    }

    private String buildParameter(String[] skus) {
        StringBuilder parameter = new StringBuilder("");
        for (int i=0;i<skus.length;i++) {
            if (i!=skus.length-1) {
                parameter.append("'"+skus[i]+"',");
            }else {
                parameter.append("'"+skus[i]+"'");
            }
        }
        return parameter.toString();
    }


    /**
     * 单个保存<p/>
     * 如果出错则抛出异常，事物回滚
     *
     * @param product
     */
    @Transactional
    private void save(Product product, Integer repositoryId) {
        String msg =  saveProduct(product,repositoryId);
        if (StringUtils.isNotBlank(msg)) {
            throw new StewardBusinessException(msg);
        }
    }

    /**
     * 保存
     *
     * @param product
     * @param repositoryId
     * @return errorMsg
     */
    private String saveProduct(Product product,Integer repositoryId) {
        product.setId(null);
        synchronized (this) {   // 防止添加或导入的时候，并发导致出现相同sku的问题，
            if (isProductExistBySKU(product.getSku())) {
                throw new StewardBusinessException("SKU已存在");
            }
            generalDAO.saveOrUpdate(product);
        }
        try {
            //添加该商品和仓库到库存管理，初始化库存为0；
            storageService.storageIncrementForProductInit(product.getId(), repositoryId, 0, InOutStockType.IN_STOCK_TYPE_INIT, null, false);
        } catch (ApiInvokeException e) {
            logger.info(e.getMessage(), e);
            return e.getMessage();         // 产品导入时候异常
        }
        return "";
    }

    /**
     * 通过SKU判断产品是否存在
     *
     * @param sku
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isProductExistBySKU(String sku) {
        Search search = new Search(Product.class).addFilterEqual("sku", sku);
        int count = generalDAO.count(search);
        return count > 0;
    }



    /**
     * 通过SKU获得产品
     *
     * @param SKU
     * @return
     */
    @Transactional(readOnly = true)
    public Product findProductBySKU(String SKU) {
        if (logger.isInfoEnabled()) {
            logger.info("通过SKU查找产品  SKU：" + SKU);
        }
        Search search = new Search(Product.class);
        /**
         * 如果SKU不为空，进行模糊查询
         */
        if (!StringUtils.isBlank(SKU)) {
            search.addFilterEqual("sku", SKU);
        }
        return (Product) generalDAO.searchUnique(search);
    }

    /**
     * 更新
     *
     * @param product
     * @param repositoryId
     * @return errorMsg
     */
    public void updateProduct(Product product,Integer repositoryId) {
        synchronized (this) {   // 防止修改的时候，并发导致出现相同sku的问题，
            if (product.getId() != null && countBySkuExceptById(product.getSku(), product.getId()) > 0) {
                throw new StewardBusinessException(String.format("SKU[%s]已使用", product.getSku()));
            }
            generalDAO.saveOrUpdate(product);
            Storage storage = storageService.findByProductId(product.getId());
            if (!storage.getRepositoryId().equals(repositoryId)) {
                storage.setRepositoryId(repositoryId);
                generalDAO.saveOrUpdate(storage);
            }
        }
    }

    private int countBySkuExceptById(String sku, Integer id) {
        Search search = new Search(Product.class);
        search.addFilterEqual("sku", sku).addFilterNotEqual("id", id);
        return generalDAO.count(search);
    }


    /**
     * 判断产品是否存在未删除的套餐中
     *
     * @param productId
     * @return
     */
    private void existNormalMealsetByProductId(Integer productId) {
       Search search = new Search(MealsetItem.class).addFilterEqual("mealset.deleted", false).addFilterEqual("productId", productId);
       if (generalDAO.count(search) > 0) {
           Product product = get(productId);
           throw new StewardBusinessException(String.format("产品[%s]存在于套餐中，不能删除", product.getName()));
       }
    }

    /**
     * 通过ID获得产品
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Product get(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("通过商品id获得商品对象 id：" + id);
        }
        return generalDAO.get(Product.class, id);
    }

    @Transactional(readOnly = true)
    /**
     * 通过查询条件获得产品List信息
     *
     * @param searchType
     * @param searchValue
     * @param page
     * @return
     */
    public List<Product> findProductByAll(String searchType, String searchValue, Integer brandId, Page page) {
        Search search = new Search(Product.class);
        /**
         * 如果需要查询的值为空或者searchType为null就不做条件查询
         */
        if (!StringUtils.isBlank(searchType) && !StringUtils.isBlank(searchValue)) {
            search.addFilterLike(searchType, "%" + searchValue + "%");
        }
        if (brandId != null) {
            search.addFilterEqual("brandId", brandId);
        }
        search.addSortDesc("createTime").addPagination(page).addFilterEqual("deleted", false);
        return generalDAO.search(search);
    }
}
