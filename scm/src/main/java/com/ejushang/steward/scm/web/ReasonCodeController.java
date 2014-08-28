package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.domain.ReasonCode;
import com.ejushang.steward.ordercenter.service.ReasonCodeService;
import com.ejushang.steward.ordercenter.domain.ReasonCodeCategory;
import com.ejushang.steward.scm.common.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by: codec.yang
 * Date: 2014/8/26 17:04
 */

@Controller
@RequestMapping("/reasoncode")
public class ReasonCodeController extends BaseController {

    @Autowired
    private ReasonCodeService reasonCodeService;

    @RequestMapping("/saveOrUpdateFirstLevelCategory")
    @ResponseBody
    public JsonResult saveOrUpdateFirstLevelCategory(@ModelAttribute("id") ReasonCodeCategory category){
        category.setLevel(ReasonCodeCategory.LEVEL_1);
        return this.saveOrUpdateCategory(category);
    }

    @RequestMapping("/saveOrUpdateSecondLevelCategory")
    @ResponseBody
    public JsonResult saveOrUpdateSecondLevelCategory(@ModelAttribute("id") ReasonCodeCategory category){
        if(NumberUtil.isNullOrLessThanOne(category.getParentId())){
            return new JsonResult(false).addObject("父目录不允许为空");
        }
        category.setLevel(ReasonCodeCategory.LEVEL_2);
        return this.saveOrUpdateCategory(category);
    }

    private JsonResult saveOrUpdateCategory(ReasonCodeCategory category){
        if(StringUtils.isBlank(category.getName())){
            return new JsonResult(false).addObject("目录名称不允许为空");
        }

        this.reasonCodeService.saveOrUpdateReasonCodeCategory(category);
        return new JsonResult(true);
    }

    @RequestMapping("/saveOrUpdateReasonCode")
    @ResponseBody
    public JsonResult saveOrUpdateReasonCode(@ModelAttribute("id") ReasonCode reasonCode){
        if(StringUtils.isBlank(reasonCode.getCode())){
            return new JsonResult(false).addObject("原因码不允许为空");
        }
        if(StringUtils.isBlank(reasonCode.getName())){
            return new JsonResult(false).addObject("原因不允许为空");
        }

        if(NumberUtil.isNullOrLessThanOne(reasonCode.getFirstLevelCategoryId())){
            return new JsonResult(false).addObject("所属一级类目不允许为空");
        }

        if(NumberUtil.isNullOrLessThanOne(reasonCode.getSecondLevelCategoryId())){
            return new JsonResult(false).addObject("所属二级类目不允许为空");
        }

        this.reasonCodeService.saveOrUpdateReasonCode(reasonCode);

        return new JsonResult(true).addObject("保存成功");

    }

    @RequestMapping("/categoryList")
    @ResponseBody
    public JsonResult categoryList(@RequestParam("level") int level){
        List<ReasonCodeCategory> categoryList=reasonCodeService.findAllCategoryByLevel(level);
        return new JsonResult(true).addList(categoryList);
    }

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult reasonCodeList(@RequestParam(value = "firstLevelCategoryId",required = false) Integer firstLevelCategoryId,
                                     @RequestParam(value = "secondLevelCategoryId",required = false) Integer secondLevelCategoryId){
        List<ReasonCode> reasonCodes=this.reasonCodeService.findAllReasonCode(firstLevelCategoryId,secondLevelCategoryId);
        List dataList=new ArrayList(reasonCodes.size());
        for(ReasonCode rc:reasonCodes){
            Map<String,String> map=new HashMap<String,String>();
            map.put("id",rc.getId().toString());
            map.put("firstLevelCategoryName",rc.getFirstLevelCategory().getName());
            map.put("secondLevelCategoryName",rc.getSecondLevelCategory().getName());
            map.put("code",rc.getCode());
            map.put("name",rc.getName());
            map.put("remark",rc.getRemark());
            dataList.add(map);

        }
        return new JsonResult(true).addList(dataList);
    }

    @RequestMapping("/tree")
    @ResponseBody
    public JsonResult reasonCodeTree(){
        List<ReasonCode> reasonCodes=this.reasonCodeService.findAllReasonCode(null,null);
        List<ReasonCodeCategory> categories1=this.reasonCodeService.findAllCategoryByLevel(ReasonCodeCategory.LEVEL_1);
        List<ReasonCodeCategory> categories2=this.reasonCodeService.findAllCategoryByLevel(ReasonCodeCategory.LEVEL_2);

        Map<Integer,ReasonCodeCategory> idToCategoryMap1=this.toMap(categories1);
        Map<Integer,ReasonCodeCategory> idToCategoryMap2=this.toMap(categories2);

        for(ReasonCodeCategory rc:categories2){
            ReasonCodeCategory parent=idToCategoryMap1.get(rc.getParentId());
            if(parent!=null){
                parent.getChildren().add(rc);
            }
        }

        for(ReasonCode rc:reasonCodes){
            ReasonCodeCategory parent=idToCategoryMap2.get(rc.getSecondLevelCategoryId());
            if(parent!=null){
                parent.getReasonCodes().add(rc);
            }

        }
        return new JsonResult(true).addList(categories1);
    }


    private Map<Integer,ReasonCodeCategory> toMap(List<ReasonCodeCategory> categories){
        Map<Integer,ReasonCodeCategory> idToCategoryMap=new HashMap<Integer, ReasonCodeCategory>();
        for(ReasonCodeCategory rc:categories){
            idToCategoryMap.put(rc.getId(),rc);
        }

        return idToCategoryMap;
    }


    @RequestMapping("/deleteCategory")
    @ResponseBody
    public JsonResult deleteCategory(Integer[] ids,int level){
        if(ids!=null&&ids.length>0) {
            this.reasonCodeService.deleteReasonCategory(Arrays.asList(ids),level);
        }
        return new JsonResult(true);
    }


    @RequestMapping("/deleteReasonCode")
    @ResponseBody
    public JsonResult deleteReasonCode(Integer[] ids){
        this.reasonCodeService.deleteReasonCodeById(ids);
        return new JsonResult(true);
    }

}
