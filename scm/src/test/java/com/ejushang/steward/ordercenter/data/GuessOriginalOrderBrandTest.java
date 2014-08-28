package com.ejushang.steward.ordercenter.data;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.ActivityType;
import com.ejushang.steward.ordercenter.constant.OriginalOrderStatus;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * User: liubin
 * Date: 14-3-10
 */

public class GuessOriginalOrderBrandTest extends BaseTest {

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private OriginalOrderService originalOrderService;


    @Test
    @Transactional
    @Rollback(false)
    public void test() {
        List<OriginalOrder> originalOrders = generalDAO.findAll(OriginalOrder.class);
        //数量太多,先解析100个试试
//        originalOrders = originalOrders.subList(0,100);
//        List<OriginalOrder> originalOrders = originalOrderService.findUnprocessedOriginalOrders();
        for(OriginalOrder originalOrder : originalOrders) {
            originalOrderService.guessOriginalOrderBrand(originalOrder, true);
        }
    }

}
