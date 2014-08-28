package com.ejushang.steward.ordercenter.data;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.service.MessageAnalyzeService;
import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * User: liubin
 * Date: 14-3-10
 */

public class TryAnalyzeFailedOriginalOrderTest extends BaseTest {

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private MessageAnalyzeService messageAnalyzeService;

    @Autowired
    private OriginalOrderService originalOrderService;


    @Test
    @Transactional
    @Rollback(false)
    public void test() {
        List<OriginalOrder> originalOrders = originalOrderService.findUnprocessedOriginalOrders();
        messageAnalyzeService.analyzeOriginalOrders(originalOrders);
    }

}
