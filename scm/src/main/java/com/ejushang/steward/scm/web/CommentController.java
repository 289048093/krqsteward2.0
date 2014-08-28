package com.ejushang.steward.scm.web;

import Vo.ScaleVo;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.customercenter.constant.CommentResult;
import com.ejushang.steward.customercenter.domain.CommentCategory;
import com.ejushang.steward.customercenter.service.CommentService;
import com.ejushang.steward.scm.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * User:moon
 * Date: 14-8-8
 * Time: 上午10:43
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    private CommentService commentService;

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request) {
        Map<String, Object[]> map = request.getParameterMap();
        Page page = PageFactory.getPage(request);
        commentService.findAllComment(map, page);
        return new JsonResult(true,"查询成功!").addObject(page);
    }

    @RequestMapping("/addCategoryToComment")
    @ResponseBody
    public JsonResult addCategoryToComment(Integer commentId,Integer[] categoryIds,Boolean isVisit) {
        commentService.categoryToComment(commentId,categoryIds,isVisit);
        return new JsonResult(true,"操作成功!");
    }

    @RequestMapping("/findAllCommentCategory")
    @ResponseBody
    public JsonResult findAllCommentCategory() {
        List<CommentCategory> commentCategories= commentService.findAllCommentCategory();
        return new JsonResult(true,"查询成功!").addList(commentCategories);
    }

    @RequestMapping("/addTagInCommentToCustomer")
    @ResponseBody
    public JsonResult addTagInCommentToCustomer(String mobile,Integer[] tagIds) {
        commentService.addTagInCommentToCustomer(mobile,tagIds);
        return new JsonResult(true,"操作成功!");
    }

    @RequestMapping("/addTagInCommentToCustomers")
    @ResponseBody
    public JsonResult addTagInCommentToCustomers(String[] mobiles,Integer[] tagIds) {
        commentService.addTagInCommentToCustomers(mobiles, tagIds);
        return new JsonResult(true,"操作成功!");
    }

    @RequestMapping("/getResultScale")
    @ResponseBody
    public JsonResult getResultScale(HttpServletRequest request) {
        Map<String, Object[]> map = request.getParameterMap();
        List<ScaleVo> scaleVos=commentService.getResultScale(map);
        return new JsonResult(true,"查询成功!").addList(scaleVos);
    }

    @RequestMapping("/getCategoryScale")
    @ResponseBody
    public JsonResult getCategoryScale(HttpServletRequest request) {
        Map<String, Object[]> map = request.getParameterMap();
        List<ScaleVo> scaleVos=commentService.getCategoryScale(map);
        return new JsonResult(true,"查询成功!").addList(scaleVos);
    }
}
