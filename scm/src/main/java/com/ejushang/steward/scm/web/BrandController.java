package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.domain.Brand;
import com.ejushang.steward.ordercenter.service.transportation.BrandService;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * User: 龙清华
 * Date: 14-4-9
 * Time: 下午2:46
 */
@Controller
public class BrandController {
    static final Logger logger = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    BrandService brandService;

    /**
     * 查询所有品牌 通过品牌名过滤
     *
     * @param request
     * @param brand
     * @return
     */
    @RequestMapping("/brand/list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request, Brand brand) {
        Page page = PageFactory.getPage(request);
        brandService.findBrandAll(brand, page);
        return new JsonResult(true).addObject(page);
    }

    /**
     * 获取产品id和name
     *
     * @return
     */
    @RequestMapping("/brand/list_brand_id_name")
    @ResponseBody
    public JsonResult listBrandIdName() {
        return new JsonResult(true, "操作成功").addList(brandService.listBrandIdAndName());
    }

    /**
     * 查询下拉列列表的值
     *
     * @param brand
     * @return
     */
    @RequestMapping("/brand/forList")
    @ResponseBody
    public JsonResult forList(Brand brand) {
        return new JsonResult(true).addList(brandService.findBrand(brand));
    }

    /**
     * 保存品牌信息
     *
     * @param brand
     * @return
     */
    @OperationLog("保存品牌信息")
    @RequestMapping("/brand/save")
    @ResponseBody
    public JsonResult save(Brand brand) {
        if (logger.isInfoEnabled()) {
            logger.info("保存品牌的brand:" + brand);
        }
        brandService.save(brand);
        BusinessLogUtil.bindBusinessLog(true,"品牌名称:%s,品牌编号：%s,供应商ID:%d",brand.getName(),brand.getCode(),brand.getSupplierId());
        return new JsonResult(true, "添加成功!");
    }

    /**
     * 修改品牌信息
     *
     * @param brand
     * @return
     */
    @OperationLog("修改品牌信息")
    @RequestMapping("/brand/update")
    @ResponseBody
    public JsonResult update(Brand brand) {
        if (logger.isInfoEnabled()) {
            logger.info("修改品牌的brand:" + brand);
        }
        brandService.update(brand);
        BusinessLogUtil.bindBusinessLog(true, "品牌名称:%s,品牌编号：%s,供应商ID:%d", brand.getName(), brand.getCode(), brand.getSupplierId());
        return new JsonResult(true, "修改成功!");
    }

    /**
     * 删除品牌信息
     *
     * @param idArray
     * @return
     */
    @OperationLog("删除品牌信息")
    @RequestMapping("/brand/delete")
    @ResponseBody
    public JsonResult delete(int[] idArray) {
        if (logger.isInfoEnabled()) {
            logger.info("删除品牌的brand:" + Arrays.toString(idArray));
        }
        brandService.delete(idArray);
        BusinessLogUtil.bindBusinessLog(true,"删除品牌的ID："+Arrays.toString(idArray));
        return new JsonResult(true, "删除成功!");
    }


    /**
     * 导入品牌信息
     *
     * @throws java.io.IOException
     */
    @OperationLog("导入产品")
    @RequestMapping("/brand/leadingIn")
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
        wb =  multipartFile.getName().matches("^.*\\.xlsx$")
                ? new XSSFWorkbook(multipartFile.getInputStream())
                : new HSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        brandService.leadInBrand(sheet);
        return new JsonResult(true, "导入成功!");
    }

}
