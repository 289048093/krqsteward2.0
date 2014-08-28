package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.logisticscenter.domain.LogisticsPrintInfo;
import com.ejushang.steward.logisticscenter.service.LogisticsPrintInfoService;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import com.ejushang.steward.scm.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * User: Blomer
 * Date: 14-4-8
 * Time: 下午2:16
 */
@Controller
@RequestMapping("/logisticsprint")
public class LogisticsPrintInfoController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(LogisticsPrintInfoController.class);

    @Autowired
    private LogisticsPrintInfoService logisticsPrintInfoService;

    /**
     * 查询所有的物流公司信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request) {
        Page page = PageFactory.getPage(request);
        logisticsPrintInfoService.list(page);
        return new JsonResult(true).addObject(page);
    }

    /**
     * 根据Id查询细节
     *
     * @param id
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public JsonResult getById(Integer id) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogisticsPrintInfoController中的getById方法,参数id[%d]", id));
        }
        return new JsonResult(true).addObject(logisticsPrintInfoService.getById(id));
    }

    /**
     * 根据name查询细节
     *
     * @param name
     * @return
     */
    @RequestMapping("/detail_by_name")
    @ResponseBody
    public JsonResult getByName(String name) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogisticsPrintInfoController中的getByName方法,参数name[%s]", name));
        }
        return new JsonResult(true).addObject(logisticsPrintInfoService.getByName(name));
    }

    @RequestMapping("/get_print_html")
    @ResponseBody
    public JsonResult getPrintHtmlByName(String name, Integer repositoryId) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogisticsPrintInfoController中的getPrintHtmlByName方法,参数name[%s]", name));
        }
        if (StringUtils.isBlank(name)) {
            throw new StewardBusinessException("物流名称不能为空");
        }
        boolean hasRepoRole = SessionUtils.getEmployee().isRepositoryEmployee();
        if (!hasRepoRole) {
            repositoryId = null;
        }
        LogisticsPrintInfo logisticsPrintInfo = logisticsPrintInfoService.getByNameAndRepositoryId(name, repositoryId);
        if (logisticsPrintInfo == null) {
            if(!hasRepoRole){
                throw new StewardBusinessException("您是非仓库人员，该物流公司还没有通用设计，请先到物流管理建立该物流信息");
            }
            throw new StewardBusinessException("该仓库的物流公司还未设计，请先到物流管理建立该物流信息");
        }
        return new JsonResult(true).addObject(logisticsPrintInfo);
    }

    /**
     * 增加或更新物流
     *
     * @param logisticsPrintInfo
     * @param id
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    @OperationLog("增加或更新物流")
    public JsonResult save(HttpServletRequest request, MultipartFile file, @ModelAttribute("id") LogisticsPrintInfo logisticsPrintInfo, Integer id) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogisticPrintInfoController中的save方法,参数logisticsPrintInfo[%s],id[%d]", logisticsPrintInfo.toString(), id));
        }
        assertEntityExist("没有对应的物流设计", id, logisticsPrintInfo);
        deleteOldLogiticsPic(logisticsPrintInfo);
        logisticsPrintInfo = this.upload(file, logisticsPrintInfo);
        logisticsPrintInfoService.save(logisticsPrintInfo);
        BusinessLogUtil.bindBusinessLog("物流名称[%s],图片路径[%s],递增[%d]",
                logisticsPrintInfo.getName(),logisticsPrintInfo.getLogisticsPicturePath(),logisticsPrintInfo.getLaw());
        return new JsonResult(true);
    }

    /**
     * 删除原图片
     *
     * @param logisticsPrintInfo
     */
    private void deleteOldLogiticsPic(LogisticsPrintInfo logisticsPrintInfo) {
        if (logisticsPrintInfo == null) {
            return;
        }
        String pic = logisticsPrintInfo.getLogisticsPicturePath();
        if (StringUtils.isBlank(pic)) {
            return;
        }
        String dir = Application.getInstance().getConfigValue(Application.PropertiesKey.UPLOAD_DIR.value).replaceFirst("^/", "").replaceFirst("/$", "");
        File oldPic = new File(WebUtil.getWebAppPath() + File.separator
                + dir + File.separator
                + pic);
        if (!oldPic.delete()) {
            oldPic.deleteOnExit();
        }
    }

    /**
     * 设计物流信息
     *
     * @param logisticsPrintInfo
     * @return
     */
    @RequestMapping("/updateDesign")
    @ResponseBody
    @OperationLog("设计物流信息")
    public JsonResult updateDesign(LogisticsPrintInfo logisticsPrintInfo) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogisticsPtintInfoController中的updateDesign方法,参数logisticsPrintInfo[%s]", logisticsPrintInfo));
        }
        LogisticsPrintInfo oldLpi = logisticsPrintInfoService.getById(logisticsPrintInfo.getId());
        log.info(String.format("LogisticsPrintInfoService中的updateDesign方法,查询出来的LogisticsPrintInfo[%s]", oldLpi.toString()));
        oldLpi.setPrintHtml(logisticsPrintInfo.getPrintHtml());
        oldLpi.setPageHeight(logisticsPrintInfo.getPageHeight());
        oldLpi.setPageWidth(logisticsPrintInfo.getPageWidth());
        logisticsPrintInfoService.save(oldLpi);
        BusinessLogUtil.bindBusinessLog("设计物流信息：物流名称[%s],页面宽度[%d],页面高度[%d]",
                    oldLpi.getName(),oldLpi.getPageWidth(),oldLpi.getPageHeight());
        return new JsonResult(true);
    }

    /**
     * 处理上传图片
     *
     * @param file
     * @param logisticsPrintInfo
     * @return
     */
    private LogisticsPrintInfo upload(MultipartFile file, LogisticsPrintInfo logisticsPrintInfo) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogiticsPrintInfoService中的upload方法,参数file[%s],logiticsPrintInfo[%s]",
                    file.getOriginalFilename(), logisticsPrintInfo.toString()));
        }
        //获得配置文件的 paths
        String paths = Application.getInstance().getConfigValue(Application.PropertiesKey.UPLOAD_DIR.value);
        //随机生成图片的名字
        String name = UUID.randomUUID() + ".jpg";
        //获得项目的绝对路径
        String path = WebUtil.getWebAppPath();
        if (log.isInfoEnabled()) {
            log.info(String.format("项目路径[%s]", path));
            log.info(String.format("upload:[%s]", paths));
        }
        paths = paths.replaceFirst("^/", "").replaceFirst("/$", "");
        File uploadDir = new File(path + File.separator + paths);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        File targetFile = new File(uploadDir, name);
        if (log.isInfoEnabled()) {
            log.info(String.format("图片保存的绝对路径是[%s]", targetFile.getAbsolutePath()));
        }
        targetFile.deleteOnExit();
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            log.error("文件保存失败");
        }
        String printHtml = "LODOP.PRINT_INITA(0,0,1000,600,\"初始化打印控件\");\n" +
                "LODOP.ADD_PRINT_SETUP_BKIMG(\"<img border='0' src='" + paths + "/" + name + "'>\");";
        logisticsPrintInfo.setPrintHtml(printHtml);
        logisticsPrintInfo.setLogisticsPicturePath(name);
        return logisticsPrintInfo;
    }

}
