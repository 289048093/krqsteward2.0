package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.common.util.PoiUtil;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.ordercenter.domain.ProductCategory;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * User: Joyce.qu
 * Date: 14-4-12
 * Time: 下午3:21
 */
@Service
@Transactional
public class ProductCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private ProductService productService;

    /**
     * 新增一个商品分类
     *
     * @param productCategory 一个分类的信息
     * @param parentId        父分类ID
     */
    public ProductCategory save(ProductCategory productCategory, Integer parentId) {
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryService类中的save方法,参数为ProductCategory类型:[{}]", productCategory);
        }
        //检验新增条件
        verificationForSave(productCategory);
        if (!NumberUtil.isNullOrZero(parentId)) {
            productCategory.setParentCategoryId(parentId);
        }
        generalDAO.saveOrUpdate(productCategory);
        return productCategory;
    }

    /**
     * 检查执行新增的条件
     *
     * @param productCategory 一个分类的信息
     */
    private void verificationForSave(ProductCategory productCategory) {
        if (StringUtils.isBlank(productCategory.getName())) {
            logger.info("ProductCategoryService类中的save方法抛异常,原因是：分类名不能为空");
            throw new StewardBusinessException("分类名称不能为空");
        }
        if (isExistByName(productCategory.getName())) {
            logger.info("ProductCategoryService类中的save方法抛异常,原因是：分类已存在");
            throw new StewardBusinessException("分类已存在");
        }
    }


    /**
     * 修改一个商品分类
     *
     * @param productCategory 一个分类的信息
     */
    public void update(ProductCategory productCategory) {
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryService类中的update方法,参数为ProductCategory类型:[{}]", productCategory);
        }
        if (productCategory.getId() == null) {
            throw new StewardBusinessException("参数错误，id不能为空");
        }
        if (StringUtils.isBlank(productCategory.getName())) {
            throw new StewardBusinessException("参数错误，name不能为空");
        }
        ProductCategory old = generalDAO.get(ProductCategory.class, productCategory.getId());
        old.setName(productCategory.getName());
        generalDAO.saveOrUpdate(old);
    }


    /**
     * 检查执行修改的条件
     *
     * @param productCategory 一个分类的信息
     */
    private void verificationForUpdate(ProductCategory productCategory) {
        if (StringUtils.isBlank(productCategory.getName())) {
            logger.info("ProductCategoryService类中的update方法抛异常,原因是：分类名不能为空");
            throw new StewardBusinessException("分类名称不能为空");
        }
        Search search = new Search(ProductCategory.class);
        search.addFilterEqual("name", productCategory.getName());
        if (isExistByName(productCategory.getName())) {
            logger.info("ProductCategoryService类中的update方法抛异常,原因是：已有该名称的分类，分类名不得重复");
            throw new StewardBusinessException("已有该名称的分类，分类名不得重复");
        }

    }

    /**
     * 根据id查找分类
     *
     * @param id 商品分类ID
     */
    @Transactional(readOnly = true)
    public ProductCategory get(Integer id) {
        if (NumberUtil.isNullOrZero(id)) {
            logger.info("ProductCategoryService类中的get方法抛异常,原因是：分类ID不合法");
            throw new StewardBusinessException("分类ID不合法");
        }
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryService类中的get方法,参数为Integer类型{}", id);
        }
        return generalDAO.get(ProductCategory.class, id);
    }

    /**
     * 根据名字查找分类
     *
     * @param name 商品分类的name
     */
    @Transactional(readOnly = true)
    public ProductCategory get(String name) {
        if (StringUtils.isBlank(name)) {
            logger.info("ProductCategoryService类中的get方法抛异常,原因是：分类名称不合法");
            throw new StewardBusinessException("分类名称不合法");
        }
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryService类中的get方法,参数为String类型{}", name);
        }
        Search search = new Search(ProductCategory.class);
        search.addFilterEqual("name", name);
        //noinspection unchecked
        List<ProductCategory> productCategories = generalDAO.search(search);
        if (productCategories == null || productCategories.isEmpty()) {

            throw new StewardBusinessException("没有名称为:{" + name + "}的分类");
        }
        if (productCategories.isEmpty()) {
            ProductCategory category = new ProductCategory();
            category.setName(name);
            category.setOperatorId(SessionUtils.getEmployee().getId());
            generalDAO.saveOrUpdate(category);
            productCategories.add(category);
        }
        return productCategories.get(0);
    }


    /**
     * 删除指定分类
     *
     * @param id 商品分类ID
     */
    public ProductCategory delete(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryService类中的delete方法,参数为Integer类型:" + id);
        }
        ProductCategory productCategory = get(id);
        verificationForDelete(id);
        generalDAO.remove(productCategory);

        return productCategory;

    }

    private void verificationForDelete(Integer id) {
        if (isExistChildren(id)) {
            logger.info("ProductCategoryService类中的delete方法抛异常,原因是：此分类下包含子类，不能删除");
            throw new StewardBusinessException("此分类下包含子类，不能删除");
        }
        if (productService.getProductByCategoryId(id) != null && productService.getProductByCategoryId(id).size() > 0) {
            logger.info("ProductCategoryService类中的delete方法抛异常,原因是：有商品属于该分类，不能删除");
            throw new StewardBusinessException("有商品属于该分类，不能删除");
        }
    }

    /**
     * 根据id查询所包含子类(包括子类的子类)
     *
     * @param id 商品catid
     */
    private List<ProductCategory> findChildren(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryService类中的findChildren方法,参数为Integer类型:[{}]", id);
        }
        Search search = new Search(ProductCategory.class);
        search.addFilterEqual("parentCategoryId", id);
        //noinspection unchecked
        return generalDAO.search(search);
    }

    /**
     * 根据id查询是否包含子类
     *
     * @param id 商品catid
     */
    private boolean isExistChildren(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryService类中的isExistChildren方法,参数为Integer类型:[{}]", id);
        }
        List<ProductCategory> productCategories = findChildren(id);
        return !(productCategories == null || productCategories.size() == 0);
    }

    /**
     * 根据指定条件查询分类
     *
     * @param name 商品分类名称
     */
    @Transactional(readOnly = true)
    public List<ProductCategory> listByName(String name) {
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryService类中的findByKey方法,参数为String类型[{}]", name);
        }
        //noinspection unchecked
        List<ProductCategory> productCategories = generalDAO.findAll(ProductCategory.class); // 只做一次查询，不递归调用
        Map<Integer, ProductCategory> catMap = new HashMap<Integer, ProductCategory>();
        Set<ProductCategory> returnCats = new HashSet<ProductCategory>();
        for (ProductCategory tmp : productCategories) {     // 遍历所有节点，建立父子关系树
            ProductCategory sub = catMap.get(tmp.getId());
            if (sub == null) {
                catMap.put(tmp.getId(), tmp);
            }
            Integer parentId = tmp.getParentCategoryId();
            if (parentId != null) {
                ProductCategory parent = catMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(tmp);
                }
            }
            // 当查询的name不为空，则返回含有那么关键字对象，否则返回父类为空的根节点
            if ((StringUtils.isNotBlank(name) && tmp.getName().contains(name)) ||
                    (StringUtils.isBlank(name) && parentId == null)) {
                returnCats.add(tmp);
            }
            tmp.setParentCategory(null);//防止递归调用查询
        }
        return new ArrayList<ProductCategory>(returnCats);
    }

    /**
     * 查询一个分类的后代类
     *
     * @param productCategory 该分类
     */
    private List<ProductCategory> findDirectChildren(ProductCategory productCategory) {
        productCategory.setParentCategory(null);//防止递归调用
        Search search = new Search(ProductCategory.class);
        search.addFilterEqual("parentCategoryId", productCategory.getId());
        //noinspection unchecked
        List<ProductCategory> children = generalDAO.search(search);
        productCategory.setChildren(children);
        //递归注入children
        if (children != null && !children.isEmpty()) {
            for (ProductCategory child : children) {
                findDirectChildren(child);
            }
        }
        return children;
    }

    /**
     * 根据名字查询商品分类是否存在
     *
     * @param name 一个分类的名称
     */
    public boolean isExistByName(String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        Search search = new Search(ProductCategory.class);
        search.addFilterEqual("name", name);
        //noinspection unchecked
        List<ProductCategory> productCategories = generalDAO.search(search);
        return !(productCategories == null || productCategories.size() == 0);
    }

    @Transactional
    public void uploadExcel(CommonsMultipartFile file) throws IOException {
        Workbook workbook = file.getName().matches("^.*\\.xlsx$")
                ? new XSSFWorkbook(file.getInputStream())
                : new HSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(workbook.getFirstVisibleTab());
        int rowNum = sheet.getLastRowNum();
        int firstRow = 1;
        for (int ri = firstRow; ri <= rowNum; ri++) {
            Row row = sheet.getRow(ri);
            int cellNum = row.getLastCellNum();
            ProductCategory parentCategory = null;
            for (int ci = 0; ci <= cellNum; ci++) {
                String name = PoiUtil.getStringCellValue(row, ci);
                if (StringUtils.isBlank(name)) {
                    break;
                }
                Search search = new Search(ProductCategory.class).addFilterEqual("name", name);
                ProductCategory category = (ProductCategory) generalDAO.searchUnique(search);
                if (category == null) {
                    category = new ProductCategory();
                    category.setName(name);
                    save(category, parentCategory == null ? null : parentCategory.getId());
                }
                parentCategory = category;
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ProductCategory> findAll() {
        return generalDAO.findAll(ProductCategory.class);
    }
}