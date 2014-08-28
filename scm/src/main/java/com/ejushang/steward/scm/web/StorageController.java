package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.constant.InOutStockType;
import com.ejushang.steward.ordercenter.domain.Storage;
import com.ejushang.steward.ordercenter.service.StorageService;
import com.ejushang.steward.ordercenter.vo.StorageVO;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * User: Sed.Li(李朝)
 * Date: 14-4-8
 * Time: 下午3:38
 */
@RequestMapping("/storage")
@Controller
public class StorageController extends BaseController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(StorageController.class);

    /**
     * storageService
     */
    @Autowired
    private StorageService storageService;

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResult listByStorage(Storage storage, HttpServletRequest request) {
        Page page = PageFactory.getPage(request);
        storageService.pageListByStorage(storage, page);
        return new JsonResult(true).addObject(page);
    }


    /**
     * 出库
     *
     * @param productId
     * @param repositoryId
     * @param num
     * @param outStockType
     * @param orderId
     * @param desc
     * @return
     */
    @RequestMapping("/storage_reduce")
    @ResponseBody
    @OperationLog("出库")
    public JsonResult storageReduce(Integer productId, Integer repositoryId, Integer num, InOutStockType outStockType, Integer orderId, String desc) {
        if (num != null && num <= 0) {
            return new JsonResult(false, "出库数量必须大于零");
        }
        if (StringUtils.isNotBlank(desc) && desc.length() > 255) {
            return new JsonResult(false, "备注信息长度不能超过255");
        }
        Storage storage=storageService.storageReduce(productId, repositoryId, num, outStockType, orderId, desc,true);

        BusinessLogUtil.bindBusinessLog("出库详情[仓库:%s,商品ID:%d,商品名称:%d,出库数量:%d]",
                    storage.getRepository()==null? repositoryId:storage.getRepository().getName(),
                    productId,
                    storage.getProduct()==null? "":storage.getProduct().getName(),
                    num
                );

        return new JsonResult(true, "操作成功");
    }

    /**
     * 入库
     *
     * @param productId
     * @param repositoryId
     * @return
     */
    @RequestMapping("/storage_increment")
    @ResponseBody
    @OperationLog("入库")
    public JsonResult storageIncrement(Integer productId, Integer repositoryId, Integer num, InOutStockType inStockType, String desc) {
        if (num != null && num <= 0) {
            return new JsonResult(false, "入库数量必须大于零");
        }
        if (StringUtils.isNotBlank(desc) && desc.length() > 255) {
            return new JsonResult(false, "备注信息长度不能超过255");
        }
        Storage storage=storageService.storageIncrementAllot2Platform(productId, repositoryId, num, inStockType, desc,true);

        BusinessLogUtil.bindBusinessLog("入库详情[仓库:%s,商品ID:%s,商品名称:%s,入库数量:%s]",
                storage.getRepository()==null? repositoryId:storage.getRepository().getName(),
                productId,
                storage.getProduct()==null? "":storage.getProduct().getName(),
                num
        );

        return new JsonResult(true, "操作成功");
    }

    /**
     * 批量更新
     * 先判断是否文件存在错误
     * 如果存在错误则直接返回，不进行更新
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/batch_update", method = RequestMethod.POST)
    @ResponseBody
    @OperationLog("批量更新库存")
    public JsonResult batchUpdate(@RequestParam("uploadFile") CommonsMultipartFile file) {
        String excelRegex = "^.*\\.xls$";
        String fileName = file.getOriginalFilename();
        if (!fileName.matches(excelRegex)) {
            return new JsonResult(false, "文件类型错误，请下载模版文件填写内容");
        }
        try {
            Map.Entry<Boolean, List<StorageVO>> es = storageService.previewBatchUpdate(file.getInputStream());
            if (es.getKey()) {
                return new JsonResult(false, "该excel文件内容存在错误，请先预览数据");
            }
            String res = storageService.batchUpdate(file.getInputStream());
            return new JsonResult(true, res);
        } catch (IOException e) {
            log.error("批量修改库存失败", e);
            return new JsonResult(false, "操作失败：" + e.getMessage());
        }
    }

    /**
     * 强制批量更新
     * 如果存在错误则忽略错误继续更新
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/force_batch_update", method = RequestMethod.POST)
    @ResponseBody
    @OperationLog("强制批量更新库存，忽略错误内容")
    public JsonResult forceBatchUpdate(@RequestParam("uploadFile") CommonsMultipartFile file) {
        String excelRegex = "^.*\\.xls$";
        String fileName = file.getOriginalFilename();
        if (!fileName.matches(excelRegex)) {
            return new JsonResult(false, "文件类型错误，请下载模版文件填写内容");
        }
        try {
            String res = storageService.batchUpdate(file.getInputStream());
            return new JsonResult(true, res);
        } catch (IOException e) {
            log.error("批量修改库存失败", e);
            return new JsonResult(false, "操作失败：" + e.getMessage());
        }
    }

    /**
     * 预览批量修改的excel
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/preview_batch_update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult previewBathUpdate(@RequestParam("uploadFile") CommonsMultipartFile file) {
        String excelRegex = "^.*\\.xls$";
        String fileName = file.getOriginalFilename();
        if (!fileName.matches(excelRegex)) {
            return new JsonResult(false, "文件类型错误，请下载模版文件填写内容");
        }
        try {
            Map.Entry<Boolean, List<StorageVO>> es = storageService.previewBatchUpdate(file.getInputStream());
            return new JsonResult(true, "操作成功").addList(es.getValue());
        } catch (IOException e) {
            log.error("批量修改库存失败", e);
            return new JsonResult(false, "操作失败：" + e.getMessage());
        }
    }

    /**
     * 批量修改库存excel模版下载
     *
     * @param response
     * @throws IOException response写入IO异常
     */
    @RequestMapping("download_template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        OutputStream os = response.getOutputStream();
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=storage.xls");
            response.setContentType("application/octet-stream; charset=utf-8");
            storageService.downloadTemplate().write(os);
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

}
