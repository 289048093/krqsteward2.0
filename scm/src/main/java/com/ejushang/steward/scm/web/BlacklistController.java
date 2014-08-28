package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.domain.Blacklist;
import com.ejushang.steward.ordercenter.service.BlacklistService;
import com.ejushang.steward.scm.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * User:moon
 * Date: 14-8-7
 * Time: 上午10:20
 */
@Controller
@RequestMapping("/blacklist")
public class BlacklistController extends BaseController {

    @Autowired
    private BlacklistService blacklistService;

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(Blacklist blacklist,HttpServletRequest request){
        Page page= PageFactory.getPage(request);
        blacklistService.findAllBlacklist(blacklist,page);
        return new JsonResult(true,"查询成功！").addObject(page);
    }

    @RequestMapping("/save")
    @ResponseBody
    public JsonResult save(@ModelAttribute("id") Blacklist blacklist){
        blacklistService.saveOrUpdateBlacklistIfNotExist(blacklist);
        return new JsonResult(true,"操作成功！");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult delete(Integer[] ids){
        blacklistService.delBlacklist(ids);
        return new JsonResult(true,"操作成功！");
    }
}
