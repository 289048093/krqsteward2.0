package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.domain.Platform;
import com.ejushang.steward.ordercenter.service.transportation.PlatformService;
import com.ejushang.steward.common.util.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Shiro
 * Date: 14-4-12
 * Time: 下午3:51
 */

@Controller
public class PlatformController {
    @Autowired
    private PlatformService platformService;


    @RequestMapping("/platform/list")
    @ResponseBody
    public JsonResult getByKey(HttpServletRequest request, Platform platform) {
        Page page = PageFactory.getPage(request);
        platformService.getByKey(platform, page);
        return new JsonResult(true).addObject(page);
    }
    @RequestMapping("/np/platform/list")
    @ResponseBody
    public JsonResult getByKey2(HttpServletRequest request, Platform platform) {
        Page page = PageFactory.getPage(request);
        platformService.getByKey(platform, page);
        return new JsonResult(true).addObject(page);
    }

    @RequestMapping("/platform/delete")
    @ResponseBody
    @OperationLog("删除平台")
    public JsonResult delete(Integer id) {
        try {
            platformService.deleteById(id);
        } catch (Exception e) {
            return new JsonResult(false, e.getMessage());
        }
        return new JsonResult(true, "删除成功");
    }

    @RequestMapping("/platform/save")
    @ResponseBody
    @OperationLog("添加平台")
    public JsonResult save(@ModelAttribute("id") Platform platform, Integer id) {
        try {
            platformService.savePlatform(platform);
            BusinessLogUtil.bindBusinessLog(true,"添加平台："+platform.getName());

        } catch (Exception e) {
            BusinessLogUtil.bindExceptionMessage(e.getMessage());
            return new JsonResult(false, e.getMessage());
        }
        return new JsonResult(true, "添加成功");
    }

    @RequestMapping("/platform/update")
    @ResponseBody
    @OperationLog("修改平台")
    public JsonResult update(@ModelAttribute("id") Platform platform, Integer id) {
        try {
            platformService.savePlatform(platform);
            BusinessLogUtil.bindBusinessLog("平台ID：%d,更新后平台名称：%s",platform.getId(),platform.getName());
        } catch (Exception e) {
            BusinessLogUtil.bindExceptionMessage(e.getMessage());
            return new JsonResult(false, e.getMessage());
        }
        return new JsonResult(true, "更新成功");
    }
}
