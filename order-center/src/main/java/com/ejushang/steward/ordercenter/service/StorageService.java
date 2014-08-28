package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.PoiUtil;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.ordercenter.constant.InOutStockType;
import com.ejushang.steward.ordercenter.constant.StorageFlowType;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ApiInvokeException;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ProductInvoke;
import com.ejushang.steward.ordercenter.service.outplatforminvoke.ProductInvokeWrapper;
import com.ejushang.steward.ordercenter.vo.StorageVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * User: Sed.Li(李朝)
 * Date: 14-4-8
 * Time: 下午3:36
 */
@Service
public class StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);


    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private StorageFlowService storageFlowService;

    @Autowired
    private ProductPlatformService productPlatformService;

    @Autowired(required = false)
    private List<ProductInvoke> productInvokes = new ArrayList<ProductInvoke>();

    /**
     * 添加产品关联到仓库
     * 不对id是否有对应实体做校验
     *
     * @param productId
     * @param repositoryId
     */
    public void saveProductWithRepository(Integer productId, Integer repositoryId) {
        if (productId == null || productId <= 0) {
            throw new StewardBusinessException("产品Id错误");
        }
        if (repositoryId == null || repositoryId <= 0) {
            throw new StewardBusinessException("仓库Id错误");
        }
        Storage storage = new Storage();
        storage.setProductId(productId);
        storage.setRepositoryId(repositoryId);
        storage.setAmount(0);
    }


    /**
     * 分页条件查询
     *
     * @param storage 查询实体条件
     * @param page    分页
     * @return 库存列表
     */
    @Transactional(readOnly = true)
    public List<Storage> pageListByStorage(Storage storage, Page page) {
        Product pro = storage.getProduct();
        Search search = new Search(Storage.class);
        search.addFilterEqual("product.deleted", false);
        if (pro != null) {
            if (pro.getBrandId() != null) {
                search.addFilterEqual("product.brand.id", pro.getBrandId());
            }
            if (pro.getCategoryId() != null) {
                search.addFilterEqual("product.category.id", pro.getCategoryId());
            }
            if (StringUtils.isNotBlank(pro.getName())) {
                search.addFilterLike("product.name", String.format("%%%s%%", pro.getName().trim()));
            }
            if (StringUtils.isNotBlank(pro.getProductNo())) {
                search.addFilterLike("product.productNo", String.format("%%%s%%", pro.getProductNo().trim()));
            }
            if (StringUtils.isNotBlank(pro.getSku())) {
                search.addFilterLike("product.sku", String.format("%%%s%%", pro.getSku().trim()));
            }
        }
        if (storage.getRepositoryId() != null) {
            search.addFilterEqual("repositoryId", storage.getRepositoryId());
        }
        Employee employee = SessionUtils.getEmployee();
        if (employee.isRepositoryEmployee()) {     //仓库人员只能查看自己仓库的数据
            //noinspection unchecked
//            List<Repository> repositories = generalDAO.search(new Search(Repository.class).addFilterEqual("chargePersonId", employee.getId()));
            List<Repository> repositories = new ArrayList<Repository>();
            List<RepositoryCharger> repositoryChargers = generalDAO.search(new Search(RepositoryCharger.class).addFilterEqual("chargerId", employee.getId()));
            for (RepositoryCharger repositoryCharger : repositoryChargers) {
                repositories.add(repositoryCharger.getRepository());
            }
            search.addFilterIn("repository", repositories);
        }
        search.addPagination(page);
        //noinspection unchecked
        return generalDAO.search(search);
    }


    /**
     * 批量更新库存（excel导入)
     *
     * @param is 导入文件的读取IO流
     * @return 错误信息，如果没有错误，返回null
     * @throws IOException
     */
    @Transactional
    public String batchUpdate(InputStream is) throws IOException {
        Workbook workbook = new HSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        StringBuilder sb = new StringBuilder();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Storage storage;
            int amountRange;
            try {
                Map.Entry<StorageVO, Integer> entry = parseRow2StorageVO(row);
                StorageVO vo = entry.getKey();
                storage = getByProductSku(vo.getProduct().getSku());
                storage.setAmount(vo.getAmount());
                amountRange = entry.getValue();
            } catch (Exception e) {
                sb.append(String.format("第%d行错误：%s； ", row.getRowNum(), e.getMessage()));
                continue;
            }
            int beforeAmount = storage.getAmount() - amountRange;
            if (amountRange == 0) {
                continue;
            } else if (amountRange > 0) {
                storageFlowService.add(storage.getId(), beforeAmount, Math.abs(amountRange), StorageFlowType.IN_STOCK, InOutStockType.IN_STOCK_TYPE_CHECK, null);
            } else if (amountRange < 0) {
                storageFlowService.add(storage.getId(), beforeAmount, Math.abs(amountRange), StorageFlowType.OUT_STOCK, InOutStockType.OUT_STOCK_TYPE_CHECK, null);
            }
            generalDAO.saveOrUpdate(storage);
            //重新分配库存
            Product product = storage.getProduct();
            try {
                allotProductStorage2Platform(product.getSku(), true);
            } catch (ApiInvokeException e) {
                log.info(e.getMessage(), e);
                throw new StewardBusinessException(e.getMessage(), e);
            }
        }
        if (sb.length() > 0) {
            return "库存导入文件解析错误，错误行:" + sb.toString();
        }
        return "操作成功";
    }

    /**
     * 预览批量更新库存（excel导入)
     *
     * @param is 导入文件的读取IO流
     * @return 返回map，key为是否存在错误的boolean值，value为预览的库存记录结果
     * @throws IOException 读取流错误
     */
    @Transactional(readOnly = true)
    public Map.Entry<Boolean, List<StorageVO>> previewBatchUpdate(InputStream is) throws IOException {
        boolean hasError = false;
        Map<Boolean, List<StorageVO>> map = new HashMap<Boolean, List<StorageVO>>(1, 1);
        Workbook workbook = new HSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        List<StorageVO> res = new ArrayList<StorageVO>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            StorageVO storageVO;
            try {
                Map.Entry<StorageVO, Integer> entry = parseRow2StorageVO(row);
                storageVO = entry.getKey();
                Integer amountRange = entry.getValue();
                //计算库存差
                storageVO.setBeforeAmount(storageVO.getAmount() - amountRange);
                storageVO.setAfterAmount(storageVO.getAmount());
                storageVO.setAmount(amountRange);
                storageVO.setStatusMsg(amountRange == 0 ? "不需要更新" : "可以更新");
            } catch (Exception e) {
                storageVO = extractRow2StorageVO(row);
                storageVO.setAfterAmount(storageVO.getAmount());
                storageVO.setAmount(null);
                storageVO.setStatusMsg("不可更新:" + e.getMessage());
                hasError = true;
            }
            res.add(storageVO);
        }
        map.put(hasError, res);
        return map.entrySet().iterator().next();
    }


    /**
     * excel模版下载
     *
     * @return excel的Workbook对象
     */
    public Workbook downloadTemplate() {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        row.setHeightInPoints(30);
        for (StorageExcelCellIndex s : StorageExcelCellIndex.values()) {
            sheet.setColumnWidth(s.index, (s.title.getBytes().length + 2) * 256);
            Cell cell = PoiUtil.createCell(row, s.index(), s.title());
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.GOLD.getIndex());
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
            cell.setCellStyle(style);
        }
        return workbook;
    }

    /**
     * 获取row的数据到storagevo
     *
     * @param row
     * @return
     */
    private StorageVO extractRow2StorageVO(Row row) {
        StorageVO storageVO = new StorageVO();
        int i = 0;
        Product product = new Product();
        storageVO.setProduct(product);
        product.setBrand(new Brand());
        Repository repository = new Repository();
        storageVO.setRepository(repository);
        product.setName(PoiUtil.getStringCellValue(row, StorageExcelCellIndex.PRODUCT_NAME.index()));
        product.getBrand().setName(PoiUtil.getStringCellValue(row, StorageExcelCellIndex.BRAND_NAME.index()));
        product.setProductNo(PoiUtil.getStringCellValue(row, StorageExcelCellIndex.PRODUCT_CODE.index()));
        product.setBoxSize(PoiUtil.getStringCellValue(row, StorageExcelCellIndex.PRODUCT_SIZE.index()));
        product.setSku(PoiUtil.getStringCellValue(row, StorageExcelCellIndex.PRODUCT_SKU.index()));
        storageVO.setAmount(PoiUtil.getIntegerCellValue(row, StorageExcelCellIndex.STORAGE_AMOUNT.index()));
        return storageVO;
    }


    /**
     * 解析excel单行为storage实体
     *
     * @param row excel row
     * @return key为storage实体，value为出入库数量的map.entry。出库value为负数，入库为正数
     * @throws StewardBusinessException row数据错误
     */
    @Transactional(readOnly = true)
    private Map.Entry<StorageVO, Integer> parseRow2StorageVO(Row row) {
        if (row == null) return null;
        Storage storage;
        //  序号是根据downloadTemplate方法中生成的excel文件的序号得到
        String sku = PoiUtil.getStringCellValue(row, StorageExcelCellIndex.PRODUCT_SKU.index());
        Integer amount = PoiUtil.getIntegerCellValue(row, StorageExcelCellIndex.STORAGE_AMOUNT.index());
        if (StringUtils.isBlank(sku)) {
            throw new StewardBusinessException("条形码不能为空");
        }
        if (amount == null) {
            throw new StewardBusinessException("库存数量不能为空");
        }
        if (amount < 0) {
            throw new StewardBusinessException("库存数量不能小于0");
        }
        //暂时为一个产品对应一个仓库，不需要提交仓库信息
//        if (StringUtils.isBlank(repositoryName)) {
//            throw new StewardBusinessException("仓库名称不能为空");
//        }
//        storage = findBySkuAndRepositoryName(sku, repositoryName);
        storage = getByProductSku(sku);
        if (storage == null) {
            //暂时为一个产品对应一个仓库，不需要对仓库信息校验
//            Product product = productService.findProductBySKU(sku);
//            if (product == null) {
//                throw new StewardBusinessException(String.format("条形码错误，没有找到编码为[%s]的相关产品", sku));
//            }
//            Repository repository = repositoryService.findByName(repositoryName);
//            if (repository == null) {
//                throw new StewardBusinessException(String.format("仓库名错误,没有找到仓库[%s]", repositoryName));
//            }
//            throw new StewardBusinessException(String.format("仓库[%1$s]下没有编码[%2$s]的产品", repositoryName, sku));
            throw new StewardBusinessException(String.format("条形码错误，没有找到编码为[%s]的产品库存记录", sku));
        }
        int range = amount - storage.getAmount();
        StorageVO storageVO = new StorageVO();
        BeanUtils.copyProperties(storage, storageVO);
        storageVO.setAmount(amount);
        Map<StorageVO, Integer> map = new HashMap<StorageVO, Integer>(1, 1);
        map.put(storageVO, range);
        return map.entrySet().iterator().next();
    }

    /**
     * 通过sku找到产品库存
     *
     * @param sku
     * @return
     */
    private Storage getByProductSku(String sku) {
        Search search = new Search(Storage.class).addFilterEqual("product.sku", sku).addFilterEqual("product.deleted", false);
        @SuppressWarnings("unchecked") List<Storage> ss = generalDAO.search(search);
        if (ss == null || ss.isEmpty()) {
            return null;
        }
        return ss.get(0);
    }


    /**
     * 通过产品sku查找库存记录
     *
     * @param sku sku
     * @return 库存对象
     */
    @Transactional(readOnly = true)
    private Storage findBySkuAndRepositoryName(String sku, String name) {
        Search search = new Search(Storage.class).addFilterEqual("product.sku", sku).addFilterEqual("repository.name", name).addFilterEqual("product.deleted", false);
        return (Storage) generalDAO.searchUnique(search);
    }

    /**
     * 通过产品id和仓库id查找库存记录
     *
     * @param productId 产品ID
     * @return 库存对象
     */
    @Transactional(readOnly = true)
    public Storage findByProductIdAndRepositoryId(Integer productId, Integer repositoryId) {
        Search search = new Search(Storage.class).addFilterEqual("productId", productId).addFilterEqual("repositoryId", repositoryId).addFilterEqual("product.deleted", false);
        return (Storage) generalDAO.searchUnique(search);
    }

    /**
     * 通过产品id查找库存记录
     *
     * @param productId 产品ID
     * @return 库存对象
     */
    @Transactional(readOnly = true)
    public Storage findByProductId(Integer productId) {
        Search search = new Search(Storage.class).addFilterEqual("productId", productId).addFilterEqual("product.deleted", false);
        @SuppressWarnings("unchecked") List<Storage> ss = generalDAO.search(search);
        if (ss == null || ss.isEmpty()) {
            return null;
        }
        return ss.get(0);  //业务上暂时定为一个产品对应一个仓库，取第一条数据
    }


    /**
     * 出库
     * 使用自身synchronized同步锁
     *
     * @param productId
     * @param repositoryId
     * @param num
     * @param outStockType
     * @param syncAmount   是否同步库存到京东/天猫等外部平台
     * @throws StewardBusinessException 当出库时，库存不足则抛出异常
     */
    @Transactional
    public Storage storageReduce(Integer productId, Integer repositoryId, Integer num, InOutStockType outStockType, Integer orderId, String desc, boolean syncAmount) {
        Storage storage;
        int beforeAmount = 0;
        synchronized (this) {
            storage = findByProductIdAndRepositoryId(productId, repositoryId);
            if (storage == null || storage.getAmount() == 0) { //库存可以为负数？！！
                Product product = generalDAO.get(Product.class, productId);
                log.warn(String.format("sku为[%s]的产品[%s]库存不足，进行了出库操作", product.getSku(), product.getName()));
//                if (storage == null) {
//                    storage = new Storage();
//                    storage.setRepositoryId(repositoryId);
//                    storage.setProductId(productId);
//                    storage.setProduct(product);
//                    storage.setAmount(0);
//                }
                throw new StewardBusinessException(String.format("sku为[%s]的产品[%s]库存不足，不能出库", product.getSku(), product.getName()));
            }
            if (storage.getAmount() != null) {
                beforeAmount = storage.getAmount();
            }
            storage.setAmount(storage.getAmount() - num);
            generalDAO.saveOrUpdate(storage);
            Product product = storage.getProduct();
            try {
                allotProductStorage2Platform(product.getSku(), syncAmount);
            } catch (ApiInvokeException e) {
                log.info(e.getMessage(), e);
                throw new StewardBusinessException(e.getMessage(), e);
            }
        }
        storageFlowService.add(storage.getId(), beforeAmount, num, StorageFlowType.OUT_STOCK, outStockType, orderId, desc);
        return storage;
    }

    /**
     * 出库
     * <p/>
     * 重写出库方法，不传仓库id，因为产品业务上只对应一个仓库
     *
     * @param productId
     * @param num
     * @param outStockType
     * @param orderId
     * @param desc
     * @param syncAmount   是否同步库存到京东/天猫等外部平台
     * @return
     */
    public Storage storageReduce(Integer productId, Integer num, InOutStockType outStockType, Integer orderId, String desc, boolean syncAmount) {
        Storage storage = findByProductId(productId);
        return storageReduce(productId, storage.getRepositoryId(), num, outStockType, orderId, desc, syncAmount);
    }

    /**
     * 入库
     * 使用自身synchronized同步锁
     *
     * @param productId
     * @param repositoryId
     * @param num
     * @param syncAmount   是否同步库存到京东/天猫等外部平台
     */
    @Transactional
    public Storage storageIncrement(Integer productId, Integer repositoryId, Integer num, InOutStockType inStockType, String desc, boolean syncAmount) {
        Storage storage;
        int beforeMount = 0;
        synchronized (this) {
            storage = findByProductIdAndRepositoryId(productId, repositoryId);
            if (storage == null) {
                storage = new Storage();
                storage.setProductId(productId);
                storage.setRepositoryId(repositoryId);
                storage.setAmount(num);
            } else {
                beforeMount = storage.getAmount();
                storage.setAmount(storage.getAmount() + num);
            }
            generalDAO.saveOrUpdate(storage);
        }
        Product product = generalDAO.get(Product.class, productId);
        try {
            allotProductStorage2Platform(product.getSku(), syncAmount);
        } catch (ApiInvokeException e) {
            log.info(e.getMessage(), e);
            throw new StewardBusinessException(e.getMessage(), e);
        }
        storageFlowService.add(storage.getId(), beforeMount, num, StorageFlowType.IN_STOCK, inStockType, desc);
        return storage;
    }

    /**
     * 产品添加时候初始入库
     *
     * @param productId
     * @param repositoryId
     * @param num
     * @param inStockType
     * @param desc
     * @param syncAmount   是否同步库存到京东/天猫等外部平台
     * @return
     * @throws ApiInvokeException
     */
    public Storage storageIncrementForProductInit(Integer productId, Integer repositoryId, Integer num, InOutStockType inStockType, String desc, boolean syncAmount) throws ApiInvokeException {
        Storage storage;
        int beforeMount = 0;
        synchronized (this) {
            storage = findByProductIdAndRepositoryId(productId, repositoryId);
            if (storage == null) {
                storage = new Storage();
                storage.setProductId(productId);
                storage.setRepositoryId(repositoryId);
                storage.setAmount(num);
            } else {
                beforeMount = storage.getAmount();
                storage.setAmount(storage.getAmount() + num);
            }
            generalDAO.saveOrUpdate(storage);
        }
        Product product = generalDAO.get(Product.class, productId);
        allotProductStorage2Platform(product.getSku(), syncAmount);
        storageFlowService.add(storage.getId(), beforeMount, num, StorageFlowType.IN_STOCK, inStockType, desc);
        return storage;
    }


    /**
     * 入库并分配库存到平台
     *
     * @param productId
     * @param repositoryId
     * @param num
     * @param inStockType
     * @param desc
     * @param syncAmount   是否同步库存到京东/天猫等外部平台
     * @return
     */
    @Transactional
    public Storage storageIncrementAllot2Platform(Integer productId, Integer repositoryId, Integer num, InOutStockType inStockType, String desc, boolean syncAmount) {
        Storage storage = storageIncrement(productId, repositoryId, num, inStockType, desc, syncAmount);
        Product product = storage.getProduct();
        try {
            allotProductStorage2Platform(product.getSku(), syncAmount);
        } catch (ApiInvokeException e) {
            log.info(e.getMessage(), e);
            throw new StewardBusinessException(e.getMessage(), e);
        }
        return storage;
    }


    /**
     * 入库时分配库存到产品所在的每个平台
     *
     * @param sku
     * @param syncAmount 是否同步库存到京东/天猫等外部平台
     */
    private void allotProductStorage2Platform(String sku, boolean syncAmount) throws ApiInvokeException {
        List<ShopProduct> shopProducts = productPlatformService.listByProductSku(sku);
        for (ShopProduct shopProduct : shopProducts) {
            if (shopProduct == null || !shopProduct.getSynStatus()) {
                continue;
            }
            if (syncAmount) {     // TODO  同步 修改 智库诚 表
                new ProductInvokeWrapper(shopProduct).updateShopStorage();
            }
        }
    }

    /**
     * 查询拥有库存的产品以及产品对应的库存
     * 库存数量小于0的也包含在内
     * <p/>
     * 如果传入的产品ID集合为空,则查询所有满足条件的产品库存
     *
     * @param productIdSet 如果为null或空集合,则查询所有满足条件的产品库存
     * @return
     */
    @Transactional(readOnly = true)
    public Map<Integer, List<Storage>> findProductsWithStorage(Set<Integer> productIdSet) {

        Map<Integer, List<Storage>> productRepoMap = new HashMap<Integer, List<Storage>>();

        Search search = new Search(Storage.class).addFetch("repository");
        if (productIdSet != null && !productIdSet.isEmpty()) {
            search.addFilterIn("productId", productIdSet);
        }
        List<Storage> storages = generalDAO.search(search);

        for (Storage storage : storages) {
            List<Storage> productStorages = productRepoMap.get(storage.getProductId());
            if (productStorages == null) {
                productStorages = new ArrayList<Storage>();
                productRepoMap.put(storage.getProductId(), productStorages);
            }
            productStorages.add(storage);
        }

        return productRepoMap;

    }

    /**
     * 查询所有库存记录，已删除的产品除外
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<Storage> findAll() {
        Search search = new Search(Storage.class).addFilterEqual("product.deleted", false);
        //noinspection unchecked
        return generalDAO.search(search);
    }


    /**
     * 批量更新的excel的cell序号
     */
    private enum StorageExcelCellIndex {
        /**
         * 产品名
         */
        PRODUCT_NAME("产品名称", 0),
        /**
         * 品牌名
         */
        BRAND_NAME("品牌名称", 1),
        /**
         * 品牌编号
         */
        PRODUCT_CODE("产品编码", 2),
        /**
         * 产品规格
         */
        PRODUCT_SIZE("产品规格", 3),
        /**
         * 条形码
         */
        PRODUCT_SKU("产品条形码(必填)", 4),
        /**
         * 库存数量
         */
        STORAGE_AMOUNT("库存数量(必填)", 5);
        /**
         * 仓库名
         */
        //
//        REPOSITORY_NAME("仓库名称",6);        // 一个产品对应一个仓库，因此可以不用指定仓库

        /**
         * 标题名
         */
        private String title;

        /**
         * cell序号
         */
        private int index;

        private StorageExcelCellIndex(String title, int value) {
            this.title = title;
            this.index = value;
        }

        public String title() {
            return title;
        }

        public int index() {
            return index;
        }
    }

    /**
     * 根据商品id,查询所有仓库总库存
     *
     * @param productId 商品id
     * @return
     */
    public Integer findAmountByProdId(Integer productId) {
        String hql = "select sum(s.amount) from Storage s where s.productId = ?";
        List<Long> amounts = generalDAO.query(hql,null,productId);
        if (amounts==null || amounts.size()==0) {
            throw new StewardBusinessException("查无商品ID为"+ productId+"的库存");
        }
        if (amounts.get(0)==null) {
            throw new StewardBusinessException("查无商品ID为"+ productId+"的库存");
        }
        long amount = amounts.get(0);
        return (int)amount;
    }

}
