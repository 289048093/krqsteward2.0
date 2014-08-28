package com.ejushang.steward.ordercenter;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.domain.Mealset;
import com.ejushang.steward.ordercenter.domain.Product;
import com.ejushang.steward.ordercenter.service.MealSetService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-4-9
 * Time: 下午5:38
 * To change this template use File | Settings | File Templates.
 */

public class MealSetTest extends BaseTest {

    @Autowired
    private MealSetService mealSetService;

//    @Test
//    @Transactional
//    @Rollback(true)
//    public void testSave() {
//        Mealset mealSet = new Mealset();
//        mealSet.setName("套餐1");
//        mealSet.setSku("123123123");
//        mealSet.setSellDescription("这是第一个套餐");
//        List<Integer> ids = new ArrayList<Integer>();
//        ids.add(1);
//        ids.add(2);
//        List<Double> moneys = new ArrayList<Double>();
//        moneys.add(15.6);
//        moneys.add(18.0);
//        List<Integer> counts = new ArrayList<Integer>();
//        counts.add(10);
//        counts.add(20);
//        mealSetService.save(mealSet, ids, moneys, counts);
//
//    }
//
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void testUpdate() {
//        Mealset mealset = mealSetService.get(2);
//        mealset.setName("修改后的套餐1111");
//        List<Integer> ids = new ArrayList<Integer>();
//        ids.add(1);
//        ids.add(2);
//        List<Double> moneys = new ArrayList<Double>();
//        moneys.add(15.6);
//        moneys.add(18.0);
//        List<Integer> counts = new ArrayList<Integer>();
//        counts.add(40);
//        counts.add(50);
//        mealSetService.save(mealset, ids, moneys, counts);
//    }

    @Test
    @Transactional
    @Rollback(false)
    public void testGet() {
        Mealset mealset = mealSetService.get(3);
        if (mealset != null) {
            System.out.println(mealset.getMealsetItemList().get(0).getPrice() + "=============================");
        } else {
            System.out.println("查无此套餐");
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testFind() {
//        Page page = new Page(1, 15);
//        List<Mealset> mealsets = mealSetService.findByKey(null, page);
//
//        List<Mealset> mealsets1 = mealSetService.findByKey("套餐1", page);
//
//        Mealset mealset = mealSetService.findBySku("1");
//        System.out.println(mealset.getSku());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testDelete() {
        mealSetService.delete(2);
    }


}
