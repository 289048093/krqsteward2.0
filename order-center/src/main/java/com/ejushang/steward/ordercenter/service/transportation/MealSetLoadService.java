package com.ejushang.steward.ordercenter.service.transportation;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.MealSet;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.MealSetItem;
import com.ejushang.steward.ordercenter.domain.Mealset;
import com.ejushang.steward.ordercenter.domain.MealsetItem;
import com.ejushang.steward.ordercenter.keygenerator.SequenceGenerator;
import com.ejushang.steward.ordercenter.util.BeanUtils;
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
 * Date: 14-8-26
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */

@Service
public class MealSetLoadService {
    private static final Logger logger = LoggerFactory.getLogger(ProductLoadService.class);

    @Autowired
    private ProductLoadService productLoadService;

    @Autowired
    private GeneralDAO generalDAO;

    public void save(MealSet mealSet) {
        Mealset newMealset = new Mealset();
        //将Mealset转成智库城格式的Mealset
        BeanUtils.copyProperties(mealSet,newMealset);
        List<MealSetItem> mealSetItems = mealSet.getMealsetItemList();
        List<MealsetItem> mealsetItems = getMealsetItems(mealSetItems);
        save(newMealset,mealsetItems);
    }

    private List<MealsetItem> getMealsetItems(List<MealSetItem> mealSetItems) {
        List<MealsetItem> mealsetItems = new ArrayList<MealsetItem>();
        for (MealSetItem mealSetItem : mealSetItems) {
            MealsetItem mealsetItem = new MealsetItem();
            mealsetItem.setPrice(mealSetItem.getPrice());
            mealsetItem.setAmount(mealSetItem.getAmount());
            mealsetItem.setProductId(productLoadService.findProductBySKU(mealSetItem.getProductSku()).getId());
            mealsetItem.setMealsetId(findBySku(mealSetItem.getMealsetSku()).getId());
            mealsetItems.add(mealsetItem);
        }
        return mealsetItems;
    }

    public void update(MealSet mealSet) {
        Mealset oldMealset = findBySku(mealSet.getSku());
        List<MealSetItem> mealSetItems = mealSet.getMealsetItemList();
        Integer oldId = oldMealset.getId();
        BeanUtils.copyProperties(mealSet,oldMealset);
        oldMealset.setId(oldId);
        List<MealsetItem> mealsetItems = getMealsetItems(mealSetItems);
        save(oldMealset,mealsetItems);
    }

    /**
     * 逻辑批量删除套餐
     *
     * @param skus  多个套餐的SKU集合
     */
    public void delete(String[] skus) {
        String hqlStart = "update Mealset m set m.deleted = ? where m.sku in (";
        String parameter = buildParameter(skus);
        String hqlEnd = ")";
        String hql = new StringBuilder("").append(hqlStart).append(parameter).append(hqlEnd).toString();
        generalDAO.update(hql,true);
    }

    /**
     * 逻辑删除一个套餐
     *
     * @param sku 套餐SKU
     */
    public void delete(String sku) {
        Mealset mealset = findBySku(sku);
        mealset.setDeleted(true);
        generalDAO.saveOrUpdate(mealset);
    }

    private String buildParameter(String[] skus) {
        StringBuilder parameter = new StringBuilder("");
        for (int i=0;i<skus.length;i++) {
            if (i!=skus.length-1) {
                parameter.append("'"+skus[i]+"',");
            }else {
                parameter.append("'"+skus[i]+"'");
            }
        }
        return parameter.toString();
    }

    public static void main(String[] args) {
        MealSetLoadService mealSetLoadService = new MealSetLoadService();
        String[] skus =new  String[]{"TU13101100071","TU13110200130"};
        mealSetLoadService.delete(skus);
    }

    private void save(Mealset mealset,List<MealsetItem> mealsetItems) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetLoadService类中的save方法,参数为Mealset类型,名称为[{}]", mealset.getName());
        }
        verificationForSave(mealset);
        if (StringUtils.isBlank(mealset.getSku())) {
            //如果发来的sku为空，则系统自动生成一个SKU
            mealset.setSku(SequenceGenerator.getInstance().getNextMealSetNo());
        }else{
            //如果不为空，检查是否重复
            isSKURepeat(mealset);
        }
        generalDAO.saveOrUpdate(mealset);
        saveMealsetItem(mealset.getId(),mealsetItems);
    }


    /**
     * 检查执行保存或修改的条件
     *
     * @param mealset      一个套餐的信息
     */
    private void verificationForSave(Mealset mealset) {
        List<MealsetItem> mealsetItems = mealset.getMealsetItemList();
        if (logger.isInfoEnabled()) {
            logger.info("MealSetLoadService类中的verificationForSave方法,参数为Mealset类型,名称为[{}]", mealset.getName());
        }
        if (StringUtils.isBlank(mealset.getName())) {
            logger.info("MealSetLoadService类中的save方法抛异常,原因是：套餐名不能为空");
            throw new StewardBusinessException("套餐名不能为空");
        }
        if (StringUtils.isBlank(mealset.getSellDescription())) {
            logger.info("MealSetLoadService类中的save方法抛异常,原因是：套餐描述不能为空");
            throw new StewardBusinessException("套餐描述不能为空");
        }
        if (mealsetItems == null || mealsetItems.size() == 0) {
            logger.info("MealSetLoadService类中的save方法抛异常,原因是：套餐必须有套餐项");
            throw new StewardBusinessException("套餐必须添加商品");
        }
        for (MealsetItem mealsetItem : mealsetItems) {
            if (NumberUtil.isNullOrZero(mealsetItem.getProductId())) {
                logger.info("MealSetLoadService类中的save方法抛异常,原因是：某套餐项对应的商品Id不合法");
                throw new StewardBusinessException("某套餐项对应的商品Id不合法");
            }
        }
        for (MealsetItem mealsetItem : mealsetItems) {
            if (mealsetItem.getPrice() == null) {
                logger.info("MealSetLoadService类中的save方法抛异常,原因是：某套餐项的金额不合法");
                throw new StewardBusinessException("某套餐项的金额不合法");
            }
        }
        for (MealsetItem mealsetItem : mealsetItems) {
            if (NumberUtil.isNullOrZero(mealsetItem.getAmount())) {
                logger.info("MealSetLoadService类中的save方法抛异常,原因是：某套餐项的数量不合法");
                throw new StewardBusinessException("某套餐项的数量不合法");
            }
        }
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
     * @param mealsetItems   套餐项
     */
    private void saveMealsetItem(Integer mealsetId,List<MealsetItem> mealsetItems) {
        deleteByMealsetId(mealsetId);
        saveItems(mealsetItems);
    }


    /**
     * 批量插入或者修改套餐项
     *
     * @param mealsetItems 一个套餐项的信息
     */
    private void saveItems(List<MealsetItem> mealsetItems) {
        if (logger.isInfoEnabled()) {
            logger.info("MealSetLoadService类中的saveItems方法");
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
            logger.info("MealSetLoadService类中的verificationForSaveItem方法");
        }
        if (NumberUtil.isNullOrZero(mealsetItem.getProductId()) || productLoadService.get(mealsetItem.getProductId()) == null) {
            logger.info("MealSetLoadService类中的save方法抛异常,原因是：该套餐项无对应商品");
            throw new StewardBusinessException("该套餐项无对应商品");
        }
        if (NumberUtil.isNullOrZero(mealsetItem.getAmount())) {
            logger.info("MealSetLoadService类中的save方法抛异常,原因是：该套餐项的商品数量不合法");
            throw new StewardBusinessException("该套餐项的商品数量不合法");
        }
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
            logger.info("MealSetLoadService类中的deleteByMealsetId方法,参数为要删除的套餐项对应套餐的id，类型为Integer[{}]", mealsetId);
        }
        List<MealsetItem> mealsetItems = findItemByKey(mealsetId);
        if (mealsetItems != null && mealsetItems.size() != 0) {
            for (int i = 0; i < mealsetItems.size(); i++) {
                generalDAO.remove(mealsetItems.get(i));
            }
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
            logger.info("MealSetLoadService类中的findByKey方法,参数为要查询的套餐项对应套餐的id，类型为Integer[{}],若id为空则查询全部", mealsetId);
        }
        Search search = new Search(MealsetItem.class).setCacheable(true);
        if (!NumberUtil.isNullOrZero(mealsetId)) {
            search.addFilterEqual("mealsetId", mealsetId);
        }
        return generalDAO.search(search);
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
            logger.info("MealSetLoadService类中的findBySku方法,参数为要查询的套餐的sku，类型为String[{}]", sku);
        }
        Search search = new Search(Mealset.class).setCacheable(true);
        search.addFilterEqual("sku", sku);
        return (Mealset) generalDAO.searchUnique(search);
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
            logger.info("MealSetLoadService类中的get方法,参数为要查询的套餐的ID，类型为Integer[{}]", id);
        }
        return generalDAO.get(Mealset.class, id);
    }

}
