package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.domain.Activity;
import com.ejushang.steward.ordercenter.service.ActivityService;
import com.ejushang.steward.common.util.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * User: Shiro
 * Date: 14-6-27
 * Time: 下午5:44
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;


    @RequestMapping("/list")
    @ResponseBody
    public JsonResult getByKey(HttpServletRequest request, @ModelAttribute("id") Activity activity, String searchType, String searchValue) {
        Page page = PageFactory.getPage(request);

        return new JsonResult(true).addObject(activityService.getByKey(activity, page, searchType, searchValue));
    }

    @RequestMapping("/delete")
    @ResponseBody
    @OperationLog("删除优惠活动")
    public JsonResult delete(Integer[] id) {
        List<Activity> activities=activityService.deleteActivity(id);

        //Just record business log
        if(!activities.isEmpty()){
            StringBuilder sb=new StringBuilder();
            sb.append("删除优惠活动：");
            for(Activity activity:activities){
                sb.append(activity.getRemark()).append(",");
            }
            BusinessLogUtil.bindBusinessLog(true,sb.toString());
        }
        return new JsonResult(true, "删除成功");
    }

    @RequestMapping("/save")
    @ResponseBody
    @OperationLog("增加优惠活动")
    public JsonResult save(@ModelAttribute("id") Activity activity, Integer[] proId, Integer[] amout,Integer shopIds[]) {
        activityService.saveActivity(activity, proId, amout,shopIds);

        BusinessLogUtil.bindBusinessLog("优惠活动名称：%s,是否启用：%s,产品ID:%s,商品数量:%s,商店ID：%s",
                activity.getRemark(),activity.getInUse(), Arrays.toString(proId),Arrays.toString(amout),Arrays.toString(shopIds));

        return new JsonResult(true, "添加成功");
    }

    @RequestMapping("/update")
    @ResponseBody
    @OperationLog("修改优惠活动")
    public JsonResult update(@ModelAttribute("id") Activity activity, Integer[] proId, Integer[] amout,Integer shopIds[]) {
        activityService.saveActivity(activity, proId, amout,shopIds);
        BusinessLogUtil.bindBusinessLog("修改活动ID:%d,优惠活动名称：%s,是否启用：%s,产品ID:%s,商品数量:%s,商店ID：%s",
                activity.getId(), activity.getRemark(), activity.getInUse(), Arrays.toString(proId), Arrays.toString(amout), Arrays.toString(shopIds));
        return new JsonResult(true, "修改成功");
    }
//    @RequestMapping("/detail")
//    @ResponseBody
//    public JsonResult getByActivityId(Integer id) {
//        return new JsonResult(true).addObject(activityService.getByActivityId(id));
//    }

    @RequestMapping("/detail")
    @ResponseBody
    public JsonResult getById(Integer id) {
        return new JsonResult(true).addObject(activityService.getById(id));
    }
}
