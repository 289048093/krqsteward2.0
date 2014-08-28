package com.ejushang.steward.ordercenter.data;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import com.ejushang.steward.ordercenter.service.UpdateDataService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * User: liubin
 * Date: 14-3-10
 */

public class UpdateNotFoundSkuTest extends BaseTest {

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private UpdateDataService updateDataService;


    @Test
    @Transactional
    @Rollback(false)
    public void test() {
        updateDataService.updateOriginalOrderItemSkuZpLvXingDai();
    }

}
