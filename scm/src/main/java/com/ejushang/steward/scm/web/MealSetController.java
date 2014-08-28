package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.domain.Mealset;
import com.ejushang.steward.ordercenter.domain.MealsetItem;
import com.ejushang.steward.ordercenter.service.MealSetService;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-4-9
 * Time: 下午5:35
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class MealSetController extends BaseController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MealSetController.class);

    @Autowired
    private MealSetService mealSetService;

    @RequestMapping(value = "/mealset/list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request, String searchType,String searchValue) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetController里的list方法,参数searchType{}",searchType );
        }
        Page page = PageFactory.getPage(request);
        mealSetService.findByKey(searchType,searchValue, page);

        return new JsonResult(true).addObject(page);
    }

    @RequestMapping(value = "/mealset/save")
    @ResponseBody
    @OperationLog("新增或修改套餐")
    public JsonResult save(Mealset mealSet, Integer[] prodId, Double[] mealPrice, Integer[] mealCount) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetController里的save方法,参数类型为：Mealset{" + mealSet.toString() + "}");
        }
        mealSetService.save(mealSet, prodId, mealPrice, mealCount);

        BusinessLogUtil.bindBusinessLog("套餐详情[套餐名称:%s,SKU:%s,描述:%s,商品ID:%s,价格:%s,商品数量:%s]",
                mealSet.getName(),mealSet.getSku(),mealSet.getSellDescription(),
                    Arrays.toString(prodId),Arrays.toString(mealPrice),Arrays.toString(mealCount)
        );

        return new JsonResult(true);
    }

    @RequestMapping(value = "/mealset/delete")
    @ResponseBody
    @OperationLog("逻辑删除套餐")
    public JsonResult delete(String id) {
        if (!id.matches("^\\d+(\\d*,\\d+)*$")) {     // id格式： 123 或者 12,13,14
            throw new StewardBusinessException("ID不合法");
        }
        if (logger.isInfoEnabled()) {
            logger.info("MealSetController里的delete方法,参数类型为：{" + id + "}");
        }
        mealSetService.delete(id);
        BusinessLogUtil.bindBusinessLog(true,"被删除套餐ID:"+id);

        return new JsonResult(true);
    }

    @RequestMapping(value = "/mealSetItem/list")
    @ResponseBody
    public JsonResult list(Integer mealSetId) {
        if (NumberUtil.isNullOrZero(mealSetId)) {
            throw new StewardBusinessException("套餐ID不存在");
        }
        if (logger.isInfoEnabled()) {
            logger.info("MealSetItemController里的list方法,参数类型为：Integer{" + mealSetId + "}");
        }
        List<MealsetItem> mealsetItems = mealSetService.findItemByKey(mealSetId);

        return new JsonResult(true).addObject(mealsetItems);
    }

    /**
     * 套餐修改
     *
     * @param mealSet
     * @return
     */
    @RequestMapping(value = "/meal_set/update")
    @ResponseBody
    public JsonResult update(Mealset mealSet) {
        valid4update(mealSet);
        mealSetService.update(mealSet);
        return new JsonResult(true);
    }

    private void valid4update(Mealset mealset) {
        if (NumberUtil.isNullOrZero(mealset.getId())) {
            logger.info("MealSetService类中的update方法抛异常,原因是：id为空");
            throw new StewardBusinessException("此套餐已经存在，且保存后的套餐不得修改");
        }
        if (StringUtils.isBlank(mealset.getName())) {
            logger.info("MealSetService类中的update方法抛异常,原因是：套餐名不能为空");
            throw new StewardBusinessException("套餐名不能为空");
        }
        if (StringUtils.isBlank(mealset.getSellDescription())) {
            logger.info("MealSetService类中的update方法抛异常,原因是：套餐描述不能为空");
            throw new StewardBusinessException("套餐描述不能为空");
        }
    }

}
