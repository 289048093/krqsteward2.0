package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.ProductPlatformService;
import com.ejushang.steward.ordercenter.service.ShopService;
import com.ejushang.steward.ordercenter.service.StorageService;
import com.ejushang.steward.ordercenter.service.transportation.PlatformService;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * User: liubin
 * Date: 14-3-11
 */
@Controller
public class ProductController extends BaseController {
    static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private PlatformService platformService;

    @Autowired
    private ShopService shopService;
    @Autowired
    private StorageService storageService;

    @Autowired
    private ProductPlatformService productPlatformService;

    @OperationLog("保存产品")
    @RequestMapping("/product/save")
    @ResponseBody
    public JsonResult save(@ModelAttribute("id") Product product, Integer repositoryId, Integer repositoryNum, Integer id, Integer[] platformIdArray, String[] marketPriceArray,
                           String[] importPriceArray, Boolean[] isPutawayArray, Integer[] storageNumArray, String[] platformUrlArray, Integer[] storagePercentRealArray, Boolean[] synStatusArray) {
        assertEntityExist("产品不存在", id, product);
        List<ShopProduct> shopProductList = new ArrayList<ShopProduct>();
        for (int i = 0; i < platformIdArray.length; i++) {
            /**
             * 处理数组，设值进去
             */
            ShopProduct shopProduct = generateShopProduct(platformIdArray[i], marketPriceArray[i], importPriceArray[i], storageNumArray[i], platformUrlArray[i], storagePercentRealArray[i]);
            if (logger.isInfoEnabled()) {
                logger.info("需要保存的productPlatform：" + shopProduct);
            }
            /**
             * 布尔字段的校验
             */
            shopProduct.setPutaway(isPutawayArray[i]);
            shopProduct.setSynStatus(synStatusArray[i]);
            shopProductList.add(shopProduct);
        }
        productService.save(product, shopProductList, repositoryId, repositoryNum);
        BusinessLogUtil.bindBusinessLog("产品详情:名称[%s],编号[%s],品牌ID[%s],条形码[%s],分类[%s],市场价[%s],最低价[%s],产地[%s],仓库ID[%s],总库存[%s]",
                product.getName(), product.getProductNo(), product.getBrandId(), product.getSku(),
                product.getCategoryId(), product.getMarketPrice(), product.getMinimumPrice(), product.getLocation(), repositoryId, repositoryNum);
        return new JsonResult(true, "添加成功!");
    }

    private ShopProduct generateShopProduct(Integer shopId, String amount, String amount1, Integer storageNum, String platformUrl, Integer storagePercentReal) {
        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setShopId(shopId);
        shopProduct.setPrice(Money.valueOf(amount));
        shopProduct.setDiscountPrice(Money.valueOf(amount1));
        shopProduct.setStorageNum(storageNum);
        shopProduct.setPlatformUrl(platformUrl);
        shopProduct.setStoragePercent(storagePercentReal);
        return shopProduct;
    }

    @OperationLog("修改产品")
    @RequestMapping("/product/update")
    @ResponseBody
    public JsonResult update(@ModelAttribute("id") Product product, Integer repositoryId, Integer repositoryNum,
                             Integer id, Integer[] platformIdArray, String[] marketPriceArray,
                             String[] importPriceArray, Boolean[] isPutawayArray, Integer[] storageNumArray,
                             String[] platformUrlArray, Integer[] productPlatformIdArray,
                             Integer[] storagePercentRealArray, Boolean[] synStatusArray) {
        assertEntityExist("产品不存在", id, product);
        List<ShopProduct> shopProductList = new ArrayList<ShopProduct>();
        for (int i = 0; i < platformIdArray.length; i++) {
            ShopProduct shopProduct = generateShopProduct(platformIdArray[i], marketPriceArray[i], importPriceArray[i], storageNumArray[i], platformUrlArray[i], storagePercentRealArray[i]);
            shopProduct.setId(productPlatformIdArray[i]);
            shopProduct.setPutaway(isPutawayArray[i]);
            shopProduct.setSynStatus(synStatusArray[i]);
            shopProductList.add(shopProduct);
        }

        productService.save(product, shopProductList, repositoryId, repositoryNum);

        BusinessLogUtil.bindBusinessLog("产品详情:名称[%s],编号[%s],品牌ID[%s],条形码[%s],分类[%s],市场价[%s],最低价[%s],产地[%s],仓库ID[%s],总库存[%s]",
                product.getName(), product.getProductNo(), product.getBrandId(), product.getSku(),
                product.getCategoryId(), product.getMarketPrice(), product.getMinimumPrice(), product.getLocation(), repositoryId, repositoryNum);

        return new JsonResult(true, "修改成功!");
    }

    private boolean isTrue(String str) {
        return "true".equals(str);
    }


    @RequestMapping("/product/list/addOrder")
    @ResponseBody
    public JsonResult findProducAddOrder(HttpServletRequest request, String searchType, String searchValue, String platform) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();//数据
        Page page = PageFactory.getPage(request);
        productService.findProductAddOrder(searchType, searchValue, page, platform);
        List<Product> productList = page.getResult();
        for (Product product : productList) {
            //数据的录入
            Map<String, Object> productVo = productService.extractData(product);
            productVo.putAll(extractPlatformData(product.getId()));
            dataList.add(productVo);
        }
        page.setResult(dataList);
        return new JsonResult(true).addObject(page);
    }

    @RequestMapping("/product/list")
    @ResponseBody
    public JsonResult findProductAll(HttpServletRequest request, String searchType, String searchValue, Integer brandId) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();//数据
        Page page = PageFactory.getPage(request);
        productService.findProductByAll(searchType, searchValue, brandId, page);
        List<Product> productList = page.getResult();
        for (Product product : productList) {
            //数据的录入
            Map<String, Object> productVo = productService.extractData(product);
            productVo.putAll(extractPlatformData(product.getId()));
            dataList.add(productVo);
        }
        page.setResult(dataList);
        return new JsonResult(true).addObject(page);
    }

    /**
     * 提取指定产品id的产品平台信息
     *
     * @param productId
     * @return
     */
    private Map<String, Object> extractPlatformData(Integer productId) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<ShopProduct> shopProducts = productPlatformService.listByProductId(productId);
        for (ShopProduct shopProduct : shopProducts) {
            if (logger.isInfoEnabled()) {
                logger.info("商品对应的商品-店铺信息：ShopProduct" + shopProduct);
            }
            if (shopProduct == null) {
                continue;
            }
            productService.copyPlatformProperties(data, shopProduct);
        }
        return data;
    }
    /**
     * 删除商品信息 通过ID
     *
     * @param idArray 商品id
     * @return 删除的条数
     */
    @RequestMapping("/product/delete")
    @ResponseBody
    public JsonResult delete(int[] idArray) throws IOException {
        productService.delete(idArray);
        return new JsonResult(true, "删除成功");
    }

    private Map<String, Object> putMap(String header, String dataIndex) {
        Map<String, Object> headMap = new HashMap<String, Object>();
        headMap.put("header", header);
        headMap.put("dataIndex", dataIndex);
        return headMap;
    }

    @RequestMapping("/product/getHead")
    @ResponseBody
    public JsonResult getHead() {
        List<Map<String, Object>> head = new ArrayList<Map<String, Object>>();//表头
        /**
         * 设置map的值
         */
        setHeadValue(head);
//        List<Platform> platformList = platformService.findPlatform();
//        for (int i = 0; i < platformList.size(); i++) {
//            Platform platform = platformList.get(i);
//            setHeadPlatform(head, platform);
//        }
        return new JsonResult(true).addList(head);
    }

//    private void setHeadPlatform(List<Map<String, Object>> head, Platform temp) {
//        Map<String, Object> headMap;
//        headMap = new HashMap<String, Object>();//动态表头
//        headMap.put("text", temp.getName());
//        List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();//内表格
//        table.add(this.putMap("一口价", "60", temp.getType() + "MarketPrice"));
//        table.add(this.putMap("促销价", "60", temp.getType() + "ImportPrice"));
//        /**
//         * 做成单选框
//         */
//        Map<String, Object> tempMap = this.putMap("是否上架", "100", temp.getType() + "IsPutaway");
//        tempMap.put("xtype", "checkcolumn");
//        table.add(tempMap);
//        tempMap = this.putMap("是否同步", "100", temp.getType() + "SynStatus");
//        tempMap.put("xtype", "checkcolumn");
//        table.add(tempMap);
//        table.add(this.putMap("库存", "60", temp.getType() + "StorageNum"));
//        table.add(this.putMap("库存占比", "60", temp.getType() + "StoragePercentReal"));
//        table.add(this.putMap("链接", "100", temp.getType() + "PlatformUrl"));
//        headMap.put("columns", table);
//        head.add(headMap);
//    }

    private void setHeadValue(List<Map<String, Object>> head) {
        head.add(this.putMap("产品ID","id"));
        head.add(this.putMap("产品品牌","brandName"));
        head.add(this.putMap("产品名称",  "name"));
        head.add(this.putMap("产品编号",  "productNo"));
        head.add(this.putMap("sku","sku"));
        head.add(this.putMap("外部平台产品编码",  "outerProductNo"));
        head.add(this.putMap("产品分类","prodCategoryName"));
        head.add(this.putMap("产品描述",  "description"));
        head.add(this.putMap("销售价",  "importPrice"));
        head.add(this.putMap("市场价",  "marketPrice"));
        head.add(this.putMap("最低价", "minimumPrice"));
        head.add(this.putMap("颜色", "color"));
        head.add(this.putMap("重量",  "weight"));
        head.add(this.putMap("尺寸",  "boxSize"));
        head.add(this.putMap("规格",  "speci"));
        head.add(this.putMap("产地",  "orgin"));
        head.add(this.putMap("产品类型",  "style"));
        head.add(this.putMap("库位",  "location"));
        head.add(this.putMap("总库存", "repositoryNum"));
        head.add(this.putMap("仓库名",  "repositoryName"));
    }

    /**
     * 导单其实也是添加商品
     *
     * @throws IOException
     */
    @OperationLog("导入产品")
    @RequestMapping("/product/leadingIn")
    @ResponseBody
    public JsonResult uploadTemplet(MultipartFile multipartFile) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("验证模版中");
        }
        /**
         * 判断是否是xls文件
         */
        String fileName = multipartFile.getOriginalFilename();
        String excelRegex = "^.*\\.(xls|xlsx)$";
        if (!fileName.matches(excelRegex)) {
            if (logger.isInfoEnabled()) {
                logger.info("导入的文件格式不正确，必须是xls或者xlsx文件");
            }
            return new JsonResult(false, "导入的文件格式不正确！");
        }
        Workbook wb;
        InputStream is = null;
        List<Row> rows = new ArrayList<Row>();
        try {
            is = multipartFile.getInputStream();
            wb = fileName.matches("^.*\\.xls$")?new HSSFWorkbook(is):new XSSFWorkbook(is);
            Sheet sheet = wb.getSheetAt(0);
            if (sheet.getLastRowNum() < 2) {
                throw new StewardBusinessException("没有数据");
            }
            sheet.removeRow(sheet.getRow(0)); //remove 前两行标题
            sheet.removeRow(sheet.getRow(1));
            for (Row row : sheet) {
                rows.add(row);
            }
        } catch (OfficeXmlFileException e) {
            throw new StewardBusinessException("文件读取失败，可能是文件的格式不对，请下载模版进行修改上传", e);
        } finally {
            if (is != null) is.close();
        }
        String warnMsg = productService.leadInProduct(rows);
        if (StringUtils.isNotBlank(warnMsg)) {
            return new JsonResult(false, "产品导入完成" + warnMsg);
        }
        return new JsonResult(true, "导入成功!");
    }
    /*************************商品-平台*************************/
    /**
     * 根据平台和商品ID查询
     *
     * @param shopProduct
     * @return
     */
    @OperationLog("保存商品-平台 信息")
    @RequestMapping("/productPlatform/save")
    @ResponseBody
    public JsonResult saveProductPlatform(ShopProduct shopProduct) {
        if (logger.isInfoEnabled()) {
            logger.info("查找产品-店铺  ShopProduct：" + shopProduct);
        }
        return new JsonResult(true, "添加成功!");
    }

    /**
     * 修改产品-平台数据
     *
     * @param shopProduct
     * @return
     */
    @OperationLog("修改商品-平台 信息")
    @RequestMapping("/productPlatform/update")
    @ResponseBody
    public JsonResult updateProductPlatform(ShopProduct shopProduct) {
        if (logger.isInfoEnabled()) {
            logger.info("修改产品-店铺  ShopProduct：" + shopProduct);
        }
        productService.saveProductPlatform(null);
        return new JsonResult(true, "修改成功!");
    }

    /**
     * 查询产品-平台数据
     *
     * @param shopProduct
     * @return
     */
    @RequestMapping("/productPlatform/list")
    @ResponseBody
    public JsonResult findProductPlatform(ShopProduct shopProduct) {
        if (logger.isInfoEnabled()) {
            logger.info("修改产品-店铺  ShopProduct：" + shopProduct);
        }
        List<ShopProduct> shopProductList = productService.findShopProduct(shopProduct);
        return new JsonResult(true).addList(shopProductList);
    }

    /**
     * 删除产品-平台数据
     *
     * @param shopProduct
     * @return
     */
    @OperationLog("删除商品-平台 信息")
    @RequestMapping("/productPlatform/delete")
    @ResponseBody
    public JsonResult deleteProductPlatform(ShopProduct shopProduct) {
        if (logger.isInfoEnabled()) {
            logger.info("删除产品-店铺  ShopProduct：" + shopProduct);
        }
        return new JsonResult(true, "删除成功!");
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping("/product/detail")
    @ResponseBody
    public JsonResult detail(Integer id) {
        if (id == null) {
            return new JsonResult(false, "产品id不能为空");
        }
        return new JsonResult(true, "操作成功").addObject(productService.findDetail(id));
    }
}
