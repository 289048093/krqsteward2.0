package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.domain.Contract;
import com.ejushang.steward.ordercenter.domain.Supplier;
import com.ejushang.steward.ordercenter.keygenerator.SequenceGenerator;
import com.ejushang.steward.ordercenter.keygenerator.SystemConfConstant;
import com.ejushang.steward.ordercenter.service.transportation.SupplierService;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * User: Shiro
 * Date: 14-4-12
 * Time: 下午3:32
 */
@Controller
public class SupplierController extends BaseController {

    private static final Logger logger = Logger.getLogger(SupplierController.class);

    @Autowired
    private SupplierService supplierService;

    @RequestMapping("/supplier/list")
    @ResponseBody
    public JsonResult getByKey(HttpServletRequest request,String name) {
        Page page = PageFactory.getPage(request);
        supplierService.listPageByName(name, page);
        return new JsonResult(true).addObject(page);

    }

    @OperationLog("删除供应商信息")
    @RequestMapping("/supplier/delete")
    @ResponseBody
    public JsonResult delete(Integer idArray) {
        Supplier supplier=supplierService.deleteById(idArray);

        //Just record business log
        if(supplier!=null){
            BusinessLogUtil.bindBusinessLog("供应商的名称:%s,编号:%s",supplier.getName(),supplier.getCode());
        }

        return new JsonResult(true, "删除成功");
    }

    @OperationLog("保存供应商信息")
    @RequestMapping("/supplier/save")
    @ResponseBody
    public JsonResult save(@ModelAttribute("id") Supplier supplier) {

        //生成供应商No
        supplier.setCode(SequenceGenerator.getInstance().getNextWithoutCache(SystemConfConstant.NEXT_SUPPLIER_NO));
        supplierService.saveSupplier(supplier);

        BusinessLogUtil.bindBusinessLog("供应商的名称:%s,编号:%s",supplier.getName(),supplier.getCode());

        return new JsonResult(true, "添加成功");
    }

    @OperationLog("修改供应商信息")
    @RequestMapping("/supplier/update")
    @ResponseBody
    public JsonResult update(@ModelAttribute("id") Supplier supplier) {
        supplierService.saveSupplier(supplier);

        BusinessLogUtil.bindBusinessLog("更新后供应商的名称:%s,编号:%s",supplier.getName(),supplier.getCode());

        return new JsonResult(true, "更新成功");
    }

    /**
     * **********合同信息******************
     */
    @OperationLog("保存合同信息")
    @RequestMapping("/contract/save")
    @ResponseBody
    public JsonResult saveContract(@ModelAttribute("id") Contract contract, Integer id) {
        supplierService.saveContract(contract);
        BusinessLogUtil.bindBusinessLog("合同详情：[合同编号=%s,品牌商ID=%s]",contract.getCode(),contract.getSupplierId());
        return new JsonResult(true, "添加成功!");
    }

    @OperationLog("修改合同信息")
    @RequestMapping("/contract/update")
    @ResponseBody
    public JsonResult updateContract(Contract contract) {
        supplierService.saveContract(contract);
        BusinessLogUtil.bindBusinessLog("合同详情：[合同编号=%s,品牌商ID=%s]",contract.getCode(),contract.getSupplierId());
        return new JsonResult(true, "修改成功!");
    }

    @RequestMapping("/contract/list")
    @ResponseBody
    public JsonResult findContractAll(HttpServletRequest request, Integer supplierId, String code) {
        Page page = PageFactory.getPage(request);
        supplierService.findContractByAll(supplierId, code, page);
        return new JsonResult(true).addObject(page);
    }


    /**
     * 删除合同 通过ID
     *
     * @param idArray 合同id
     * @return
     */
    @OperationLog("删除合同信息")
    @RequestMapping("/contract/delete")
    @ResponseBody
    public JsonResult deleteContract(int[] idArray) throws IOException {
        List<Contract> contractList=supplierService.deleteContract(idArray);

        //Record business log
        if(!contractList.isEmpty()){
            StringBuilder sb=new StringBuilder();
            sb.append("被删除合同详情：");
            for(Contract contract:contractList){
                sb.append("{合同编号：").append(contract.getCode()).append("}");
            }
            BusinessLogUtil.bindBusinessLog(true,sb.toString());
        }

        return new JsonResult(true, "删除成功");
    }

    /**
     * 导单其实也是添加供应商信息
     *
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @OperationLog("导入合同信息")
    @RequestMapping("/contract/leadingIn")
    @ResponseBody
    public JsonResult uploadTemplet(MultipartFile multipartFile,
                                    HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        if (logger.isInfoEnabled()) {
            logger.info("验证excel中");
        }
        /**
         * 判断是否是xls文件
         */
        String fileName = multipartFile.getOriginalFilename();

        if (!fileName.matches("^.*\\.(xls|xlsx)$")) {
            if (logger.isInfoEnabled()) {
                logger.info("导入的文件格式不正确，请确认是否是xls或者xlsx文件");
            }
            return new JsonResult(false, "导入的文件格式不正确！");
        }
        Workbook wb;
        wb = multipartFile.getName().matches("^.*\\.xlsx$")
                ? new XSSFWorkbook(multipartFile.getInputStream())
                : new HSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        logger.info(" sheet.getLastRowNum()" + sheet.getLastRowNum());
        supplierService.leadInContract(sheet);
        return new JsonResult(true, "导入成功！");
    }
}
