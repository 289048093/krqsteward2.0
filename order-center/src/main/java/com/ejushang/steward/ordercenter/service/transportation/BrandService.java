package com.ejushang.steward.ordercenter.service.transportation;


import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;

import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.common.util.PoiUtil;
import com.ejushang.steward.ordercenter.domain.Brand;
import com.ejushang.steward.ordercenter.domain.Platform;
import com.ejushang.steward.ordercenter.domain.Supplier;
import com.ejushang.steward.ordercenter.keygenerator.SequenceGenerator;
import com.ejushang.steward.ordercenter.keygenerator.SystemConfConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * User: 龙清华
 * Date: 13-12-24
 * Time: 下午2:38
 */
@Transactional
@Service
public class BrandService {
    static final Logger logger = LoggerFactory.getLogger(BrandService.class);

    @Autowired
    private GeneralDAO generalDAO;
    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ProductService productService;


    /**
     * 获取品牌id和name
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listBrandIdAndName() {
        List<Brand> brands = findAll();
        List<Map<String, Object>> brandNames = new ArrayList<Map<String, Object>>();
        for (Brand brand : brands) {
            Map<String, Object> vo = new HashMap<String, Object>();
            vo.put("id", brand.getId());
            vo.put("name", brand.getName());
            brandNames.add(vo);
        }
        return brandNames;
    }

    /**
     * 通过ID获得品牌信息
     *
     * @param brandId
     * @return
     */
    @Transactional(readOnly = true)
    public Brand get(Integer brandId) {
        if (logger.isInfoEnabled()) {
            logger.info("获得brandId{" + brandId + "}");
        }
        return generalDAO.get(Brand.class, brandId);
    }

    /**
     * 通过品牌名过滤查询所有品牌
     *
     * @param brand
     * @param page  分页使用
     * @return
     */
    @Transactional(readOnly = true)
    public List<Brand> findBrandAll(Brand brand, Page page) {
        if (logger.isInfoEnabled()) {
            logger.info("品牌：" + brand);
        }
        Search search = new Search(Brand.class);
        /**
         * 如果brand不为null就模糊查询
         */
        if (brand != null) {
            if (!NumberUtil.isNullOrZero(brand.getSupplierId())) {
                search.addFilterEqual("supplierId", brand.getSupplierId());
            }

            if (StringUtils.isNotBlank(brand.getName())) {
                search.addFilterLike("name", "%" + brand.getName().trim() + "%");
            }
        }
        search.addFilterEqual("deleted", false)
                .addPagination(page);
        return generalDAO.search(search);
    }

    /**
     * @return品牌集合
     */
    public List<Brand> findBrand(Brand brand) {
        if (logger.isInfoEnabled()) {
            logger.info("模糊查询品牌：brand" + brand);
        }
        Search search = new Search(Brand.class).setCacheable(true);
        return generalDAO.search(search);
    }

    /**
     * 保存品牌数据
     *
     * @param brand
     */
    @Transactional
    public void save(Brand brand) {
        if (logger.isInfoEnabled()) {
            logger.info("保存的品牌对象 brand：" + brand);
        }
        if (brand == null) {
            throw new StewardBusinessException("未传入任何数据!");
        }
        /**
         * 如果code已经存在，不能修改
         * */
        if (isBrandExistByCode(brand.getCode()) && NumberUtil.isNullOrZero(brand.getId())) {
            throw new StewardBusinessException("品牌编号已存在!");
        }
        Brand oldBrand = findByName(brand.getName());
        if (oldBrand != null) {
            throw new StewardBusinessException(String.format("品牌名[%s]已经存在", brand.getName()));
        }
        Integer supplierId = brand.getSupplierId();
        if (supplierId == null) {
            throw new StewardBusinessException("供应商Id不能为空");
        }
        Supplier supplier = supplierService.findSupplierById(supplierId);
        if (supplier == null) {
            throw new StewardBusinessException("供应商不存在，请刷新后重试");
        }
//        supplierService.brand.getSupplierId()
        generalDAO.saveOrUpdate(brand);
    }

    /**
     * 品牌修改
     *
     * @param brand
     */
    @Transactional
    public void update(Brand brand) {
        if (logger.isInfoEnabled()) {
            logger.info("修改的品牌对象 brand：" + brand);
        }
        Brand oldBrand = generalDAO.get(Brand.class, brand.getId());
        if (oldBrand == null) {
            throw new StewardBusinessException("品牌不存在，请刷新后重试");
        }
        if (!brand.getName().equals(oldBrand.getName())) {//如果修改了名字
            Brand nameBrand = findByName(brand.getName());
            if (nameBrand != null && !nameBrand.getId().equals(oldBrand.getId())) {
                throw new StewardBusinessException(String.format("品牌名[%s]已经存在", brand.getName()));
            }
            oldBrand.setName(brand.getName());
        }
        oldBrand.setCode(brand.getCode());
        oldBrand.setSupplierId(brand.getSupplierId());
        oldBrand.setPaymentRule(brand.getPaymentRule());
        oldBrand.setPaymentType(brand.getPaymentType());
        generalDAO.saveOrUpdate(oldBrand);
    }

    @Transactional(readOnly = true)
    private Brand findByName(String name) {
        Search search = new Search(Brand.class).setCacheable(true).addFilterEqual("name", name).addFilterEqual("deleted", false);
        return (Brand) generalDAO.searchUnique(search);
    }

    /**
     * /**
     * 删除品牌数据
     *
     * @param array
     */
    @Transactional
    public void delete(int[] array) {
        for (int anArray : array) {
            Brand brand = this.get(anArray);
            if (brand == null) {        //品牌id不存在 忽略
                continue;
            }
            if (productService.countByBrandId(anArray) > 0) {
                throw new StewardBusinessException(String.format("品牌[%s]下存在产品，不能删除", brand.getName()));
            }
            brand.setDeleted(true);
            generalDAO.saveOrUpdate(brand);
        }
    }

    /**
     * 通过品牌ID判断品牌是否存在
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isBrandExist(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("品牌id是否存在 id：" + id);
        }
        Search search = new Search(Brand.class).addFilterEqual("id", id).addFilterEqual("deleted", false);
        int count = generalDAO.count(search);
        return count > 0;
    }

    /**
     * 通过品牌ID判断品牌是否存在
     *
     * @param code
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isBrandExistByCode(String code) {
        if (logger.isInfoEnabled()) {
            logger.info("品牌code是否存在 code：" + code);
        }
        Search search = new Search(Brand.class).addFilterEqual("code", code).addFilterEqual("deleted", false);
        int count = generalDAO.count(search);
        return count > 0;
    }


    /**
     * 通过品牌名判断品牌是否存在
     *
     * @param brandName
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isBrandExistByName(String brandName, Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("品牌name是否存在 id：" + brandName);
        }
        Search search = new Search(Brand.class).addFilterEqual("name", brandName).addFilterNotEqual("id", id).addFilterEqual("deleted", false);
        int count = generalDAO.count(search);
        return count > 0;
    }

    @Transactional
    public void leadInBrand(Sheet sheet) {
        if (sheet.getLastRowNum() < 2) {
            throw new StewardBusinessException("没有数据!");
        }
        for (int i = sheet.getFirstRowNum() + 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            /** 将单元格设置成string型，不然纯数字就不能转换*/
            for (int k = 0; k < 5; k++) {
                if (row.getCell(k) != null) {
                    row.getCell(k).setCellType(Cell.CELL_TYPE_STRING);
                }
            }
            this.validateExcel(row, i);
        }
    }

    /**
     * 导单前验证excel表单是否合法
     *
     * @param row
     * @return
     */
    public void validateExcel(Row row, int index) {
        String name = row.getCell(0).getStringCellValue();         //品牌名
        Cell brandCodeCell = row.getCell(1);
        String code = null;
        if (brandCodeCell != null) {
            code = row.getCell(1).getStringCellValue();         //品牌编号
        }
        if (StringUtils.isBlank(code)) {
            code = UUID.randomUUID().toString();
        }
        String supper = PoiUtil.getStringCellValue(row, 2);           //所属供应商
        if (StringUtils.isBlank(supper)) {
            throw new StewardBusinessException("供应商必须填,行号：" + (row.getRowNum() + 1));
        }
        String paymentType = PoiUtil.getStringCellValue(row, 3);   //结算方式
        String paymentRule = PoiUtil.getStringCellValue(row, 4);   //结算规则

        Brand brand = new Brand();
        Supplier supplier = supplierService.getByName(supper);

        if (supplier == null) {      //判断品牌是否存在
            supplier = new Supplier();
            supplier.setCode(SequenceGenerator.getInstance().getNextWithoutCache(SystemConfConstant.NEXT_SUPPLIER_NO));
            supplier.setName(supper);
            supplierService.saveSupplier(supplier);
//            throw new StewardBusinessException("供应商:" + supper + " 不存在" + "  行数：" + index);
        }
        Brand oldBrand = findByName(name);
        if (oldBrand != null) {
            throw new StewardBusinessException(String.format("品牌名[%s]已经存在,行号:%d", name, row.getRowNum()));
        }
        brand.setName(name);
        if (isBrandExistByCode(code)) {
            throw new StewardBusinessException(String.format("品牌编码[%s]已经存在,行号:%d", code, row.getRowNum() + 1));
        }
        brand.setCode(code);
        brand.setSupplierId(supplier.getId());
        brand.setPaymentType(paymentType);
        brand.setPaymentRule(paymentRule);

        if (logger.isInfoEnabled()) {
            logger.info("导入的实体brand:" + brand);
        }
        generalDAO.saveOrUpdate(brand);
    }

    @Transactional(readOnly = true)
    public int countBySupplierId(Integer idArray) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("参数 supplierId[%s]：", idArray));
        }
        Search search = new Search(Brand.class).addFilterEqual("supplierId", idArray).addFilterEqual("deleted", false);
        return generalDAO.count(search);
    }


    @Transactional(readOnly = true)
    public List<Brand> findAll() {
        Search search = new Search(Brand.class).setCacheable(true);
        search.addFilterEqual("deleted", false);
        //noinspection unchecked
        return generalDAO.search(search);
    }
}

