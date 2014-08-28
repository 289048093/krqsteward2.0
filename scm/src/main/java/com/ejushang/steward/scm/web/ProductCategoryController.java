package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.domain.ProductCategory;
import com.ejushang.steward.ordercenter.service.ProductCategoryService;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-4-12
 * Time: 下午6:21
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ProductCategoryController extends BaseController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProductCategoryController.class);

    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/productCategory/list")
    @ResponseBody
    public JsonResult list(String name) {
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryController类中的list方法,参数为String类型{" + name + "}");
        }
        List<ProductCategory> productCategorys = productCategoryService.listByName(name);
        return new JsonResult(true).addObject(productCategorys);
    }

    @RequestMapping(value = "/productCategory/save")
    @ResponseBody
    @OperationLog("新增商品分类")
    public JsonResult save(ProductCategory productCategory, Integer parentId) {
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryController里的save方法,参数ProductCategory类型{" + productCategory.toString() + "}");
        }
        ProductCategory newProductCategory = productCategoryService.save(productCategory, parentId);
        BusinessLogUtil.bindBusinessLog(true,"新增商品类别："+newProductCategory.getName());
        return new JsonResult(true).addObject(newProductCategory);
    }

    @RequestMapping(value = "/productCategory/update")
    @ResponseBody
    @OperationLog("修改商品分类")
    public JsonResult update(ProductCategory productCategory) {
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryController里的save方法,参数ProductCategory类型{" + productCategory.toString() + "}");
        }
        productCategoryService.update(productCategory);
        BusinessLogUtil.bindBusinessLog(true,"更新后商品类别为："+productCategory.getName());
        return new JsonResult(true);
    }

    @RequestMapping(value = "/productCategory/delete")
    @ResponseBody
    @OperationLog("删除商品分类")
    public JsonResult delete(Integer id) {
        if (NumberUtil.isNullOrZero(id)) {
            throw new StewardBusinessException("ID不合法");
        }
        if (logger.isInfoEnabled()) {
            logger.info("ProductCategoryController里的delete方法,参数Integer类型{" + id + "}");
        }
        ProductCategory deletedCategory=productCategoryService.delete(id);

        if(deletedCategory!=null){
            BusinessLogUtil.bindBusinessLog(true,"删除商品分类，类别名称为:"+deletedCategory.getName());
        }

        return new JsonResult(true);
    }

    @RequestMapping("/product_category/upload_excel")
    @ResponseBody
    @OperationLog("上传商品分类")
    public JsonResult uploadExcel(@RequestParam("uploadFile") CommonsMultipartFile uploadFile) {
        String excelRegex = "^.*\\.(xls|xlsx)$";
        String fileName = uploadFile.getOriginalFilename();
        if (!fileName.matches(excelRegex)) {
            return new JsonResult(false, "文件类型错误，请下载模版文件填写内容");
        }
        try {
            productCategoryService.uploadExcel(uploadFile);
        } catch (IOException e) {
            throw new StewardBusinessException("文件读取错误");
        }
        return new JsonResult(true);
    }
}
