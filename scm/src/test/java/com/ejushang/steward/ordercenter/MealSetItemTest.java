package com.ejushang.steward.ordercenter;

import com.ejushang.steward.ordercenter.domain.MealsetItem;
import com.ejushang.steward.ordercenter.service.MealSetService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-4-10
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
public class MealSetItemTest extends BaseTest {

    @Autowired
    MealSetService mealSetService;

    @Test
    @Transactional
    @Rollback(true)
    public void testFind() {
        List<MealsetItem> mealSetItems = mealSetService.findItemByKey(null);

        List<MealsetItem> mealsetItemList = mealSetService.findItemByKey(2);

        System.out.println(mealSetItems.get(0).getPrice() + "========" + mealsetItemList.get(0).getPrice());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testDelete() {
        mealSetService.deleteItem(null);
    }
}
