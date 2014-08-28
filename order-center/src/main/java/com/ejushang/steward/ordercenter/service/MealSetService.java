package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.domain.Mealset;
import com.ejushang.steward.ordercenter.domain.MealsetItem;
import com.ejushang.steward.ordercenter.keygenerator.SequenceGenerator;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-4-9
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */

@Service
@Transactional
public class MealSetService {

    private static final Logger logger = LoggerFactory.getLogger(MealSetService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private ProductService productService;

    /**
     * 插入或者修改一个套餐
     *
     * @param mealset      一个套餐的信息
     * @param productIds   对应套餐项的商品列表
     * @param doublePrices 对应套餐项的套餐价列表
     * @param mealCounts   对应套餐项的数量价列表
     */
    public void save(Mealset mealset, Integer[] productIds, Double[] doublePrices, Integer[] mealCounts) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetService类中的save方法,参数为Mealset类型,名称为[{}]", mealset.getName());
        }
        verificationForSave(mealset, productIds, doublePrices, mealCounts);
        if (StringUtils.isBlank(mealset.getSku())) {
            mealset.setSku(SequenceGenerator.getInstance().getNextMealSetNo());
        }else{
            isSKURepeat(mealset);
        }
        generalDAO.saveOrUpdate(mealset);
        saveMealsetItem(mealset, productIds, doublePrices, mealCounts);
    }

    private void isSKURepeat(Mealset mealset) {
        Search search = new Search(Mealset.class).addFilterEqual("sku", mealset.getSku()).addFilterNotEqual("id", mealset.getId());
        int count = generalDAO.count(search);
        if(count>0) {
            throw new StewardBusinessException("SKU已经存在");
        }
    }

    /**
     * 插入或者修改一个套餐的套餐项
     *
     * @param mealset   一个套餐的信息
     * @param prodId    对应套餐项的商品列表
     * @param mealPrice 对应套餐项的套餐价列表
     * @param mealCount 对应套餐项的数量价列表
     */
    private void saveMealsetItem(Mealset mealset, Integer[] prodId, Double[] mealPrice, Integer[] mealCount) {
        List<Money> mealPrices = doubleToMoney(mealPrice);
//        deleteByMealsetId(mealset.getId());
        List<MealsetItem> mealsetItems = new ArrayList<MealsetItem>();
        for (int i = 0; i < prodId.length; i++) {
            MealsetItem mealsetItem = new MealsetItem();
            mealsetItem.setMealsetId(mealset.getId());
            mealsetItem.setProductId(prodId[i]);
            mealsetItem.setAmount(mealCount[i]);
            mealsetItem.setPrice(mealPrices.get(i));
            mealsetItems.add(mealsetItem);
        }
        saveItems(mealsetItems);
    }

    /**
     * 金额类型转换
     *
     * @param doublePrices 一个些double类型的金额
     */
    private List<Money> doubleToMoney(Double[] doublePrices) {
        List<Money> mealPrices = new ArrayList<Money>();
        for (int i = 0; i < doublePrices.length; i++) {
            Money money = Money.valueOf(doublePrices[i]);
            mealPrices.add(money);
        }
        return mealPrices;
    }

    /**
     * 检查执行保存或修改的条件
     *
     * @param mealset      一个套餐的信息
     * @param productIds   对应套餐项的商品列表
     * @param doublePrices 对应套餐项的套餐价列表
     * @param mealCounts   对应套餐项的数量价列表
     */
    private void verificationForSave(Mealset mealset, Integer[] productIds, Double[] doublePrices, Integer[] mealCounts) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetService类中的verificationForSave方法,参数为Mealset类型,名称为[{}]", mealset.getName());
        }
        if (!NumberUtil.isNullOrZero(mealset.getId())) {
            logger.info("MealSetService类中的save方法抛异常,原因是：此套餐已经存在，且保存后的套餐不得修改");
            throw new StewardBusinessException("此套餐已经存在，且保存后的套餐不得修改");
        }
        if (StringUtils.isBlank(mealset.getName())) {
            logger.info("MealSetService类中的save方法抛异常,原因是：套餐名不能为空");
            throw new StewardBusinessException("套餐名不能为空");
        }
        if (StringUtils.isBlank(mealset.getSellDescription())) {
            logger.info("MealSetService类中的save方法抛异常,原因是：套餐描述不能为空");
            throw new StewardBusinessException("套餐描述不能为空");
        }
        if (productIds == null || productIds.length == 0) {
            logger.info("MealSetService类中的save方法抛异常,原因是：套餐必须添加商品");
            throw new StewardBusinessException("套餐必须添加商品");
        }
        for (int i = 0; i < productIds.length; i++) {
            if (NumberUtil.isNullOrZero(productIds[i])) {
                logger.info("MealSetService类中的save方法抛异常,原因是：某套餐项对应的商品Id不合法");
                throw new StewardBusinessException("某套餐项对应的商品Id不合法");
            }
        }
        for (int i = 0; i < doublePrices.length; i++) {
            if (doublePrices[i] != null && doublePrices[i] < 0) {
                logger.info("MealSetService类中的save方法抛异常,原因是：某套餐项的金额不合法");
                throw new StewardBusinessException("某套餐项的金额不合法");
            }
        }
        for (int i = 0; i < mealCounts.length; i++) {
            if (NumberUtil.isNullOrZero(mealCounts[i])) {
                logger.info("MealSetService类中的save方法抛异常,原因是：某套餐项的数量不合法");
                throw new StewardBusinessException("某套餐项的数量不合法");
            }
        }
    }

    /**
     * 通过ID获得一个套餐
     *
     * @param id 套餐ID
     */
    @Transactional(readOnly = true)
    public Mealset get(Integer id) {
        if (NumberUtil.isNullOrZero(id)) {
            throw new StewardBusinessException("套餐ID不合法");
        }
        if (logger.isInfoEnabled()) {
            logger.info("MealSetService类中的get方法,参数为要查询的套餐的ID，类型为Integer[{}]", id);
        }
        return generalDAO.get(Mealset.class, id);
    }

    /**
     * 通过SKU获得一个套餐
     *
     * @param sku 套餐sku
     */
    @Transactional(readOnly = true)
    public Mealset findBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            throw new StewardBusinessException("套餐sku不合法");
        }
        if (logger.isInfoEnabled()) {
            logger.info("MealSetService类中的findBySku方法,参数为要查询的套餐的sku，类型为String[{}]", sku);
        }
        Search search = new Search(Mealset.class).setCacheable(true);
        search.addFilterEqual("sku", sku);
        return (Mealset) generalDAO.searchUnique(search);
    }

    /**
     * 根据条件获得套餐
     *
     * @param
     */
    @Transactional(readOnly = true)
    public List<Mealset> findByKey(String searchType,String searchValue, Page page) {
        Search search = new Search(Mealset.class).setCacheable(true).addPagination(page).addFilterEqual("deleted", false);
        if (logger.isInfoEnabled()) {
            logger.info("MealSetService类中的findByKey方法,参数为要查询的套餐的name，类型为String[{}]", searchType);
        }
        if (!StringUtils.isBlank(searchType)&&!StringUtils.isBlank(searchValue)) {
            search.addFilterLike(searchType, "%" + searchValue + "%");
        }
        return generalDAO.search(search);
    }

    /**
     * 删除一个套餐
     *
     * @param id 套餐ID
     */
    public void delete(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetService类中的delete方法,参数为要逻辑删除的套餐的id，类型为Integer[{}]", id);
        }
        Mealset mealset = get(id);
        mealset.setDeleted(true);
        generalDAO.saveOrUpdate(mealset);
    }

    /**
     * 批量删除
     *
     * @param idStrsStr
     */
    public void delete(String idStrsStr) {
        String[] idStrs = idStrsStr.split(",");
        for (String idStr : idStrs) {
            delete(Integer.parseInt(idStr));
        }
    }


    /**
     * 批量插入或者修改套餐项
     *
     * @param mealsetItems 一个套餐项的信息
     */
    private void saveItems(List<MealsetItem> mealsetItems) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetItemService类中的saveItems方法,参数为List<MealsetItem>");
        }
        for (int i = 0; i < mealsetItems.size(); i++) {
            verificationForSaveItem(mealsetItems.get(i));
        }
        generalDAO.saveOrUpdate(mealsetItems);
    }

    /**
     * 检查插入套餐项的条件
     *
     * @param mealsetItem 一个套餐项的信息
     */
    private void verificationForSaveItem(MealsetItem mealsetItem) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetItemService类中的verificationForSaveItem方法");
        }
        if (NumberUtil.isNullOrZero(mealsetItem.getProductId()) || productService.get(mealsetItem.getProductId()) == null) {
            logger.info("MealSetItemService类中的save方法抛异常,原因是：该套餐项无对应商品");
            throw new StewardBusinessException("该套餐项无对应商品");
        }
        if (NumberUtil.isNullOrZero(mealsetItem.getAmount())) {
            logger.info("MealSetItemService类中的save方法抛异常,原因是：该套餐项的商品数量不合法");
            throw new StewardBusinessException("该套餐项的商品数量不合法");
        }
    }

    /**
     * 获得套餐项
     *
     * @param mealsetId 套餐Id
     */
    @Transactional(readOnly = true)
    public List<MealsetItem> findItemByKey(Integer mealsetId) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetItemService类中的findByKey方法,参数为要查询的套餐项对应套餐的id，类型为Integer[{}],若id为空则查询全部", mealsetId);
        }
        Search search = new Search(MealsetItem.class).setCacheable(true);
        if (!NumberUtil.isNullOrZero(mealsetId)) {
            search.addFilterEqual("mealsetId", mealsetId);
        }
        return generalDAO.search(search);
    }

    /**
     * 删除一个套餐项
     *
     * @param id 套餐项的ID
     */
    public void deleteItem(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetItemService类中的delete方法,参数为要删除的套餐项的id，类型为Integer[{}]", id);
        }
        MealsetItem mealsetItem = generalDAO.get(MealsetItem.class, id);
        generalDAO.remove(mealsetItem);
    }

    /**
     * 删除一个套餐下的所有套餐项
     *
     * @param mealsetId 套餐的ID
     */
    private void deleteByMealsetId(Integer mealsetId) {
        if (NumberUtil.isNullOrZero(mealsetId)) {
            throw new StewardBusinessException("对应的套餐的ID不合法");
        }
        if (logger.isInfoEnabled()) {
            logger.info("MealSetItemService类中的deleteByMealsetId方法,参数为要删除的套餐项对应套餐的id，类型为Integer[{}]", mealsetId);
        }
        List<MealsetItem> mealsetItems = findItemByKey(mealsetId);
        if (mealsetItems != null && mealsetItems.size() != 0) {
            for (int i = 0; i < mealsetItems.size(); i++) {
                generalDAO.remove(mealsetItems.get(i));
            }
        }
    }

    public void update(Mealset mealSet) {
        Mealset oldMealSet = generalDAO.get(Mealset.class, mealSet.getId());
        if (oldMealSet == null) {
            throw new StewardBusinessException("套餐不存在，请刷新后重试");
        }
        isSKURepeat(mealSet);
        oldMealSet.setName(mealSet.getName());
        oldMealSet.setSku(StringUtils.isBlank(mealSet.getSku())
                ? SequenceGenerator.getInstance().getNextMealSetNo()
                : mealSet.getSku());
        oldMealSet.setSellDescription(mealSet.getSellDescription());
        generalDAO.saveOrUpdate(oldMealSet);
    }
}
