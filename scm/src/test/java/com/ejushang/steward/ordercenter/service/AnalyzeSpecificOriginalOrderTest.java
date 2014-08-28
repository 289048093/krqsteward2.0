package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


/**
 * User: liubin
 * Date: 14-3-10
 */

public class AnalyzeSpecificOriginalOrderTest extends BaseAnalyzeTest {

    @Autowired
    private MessageAnalyzeService messageAnalyzeService;

    @Autowired
    private GeneralDAO generalDAO;


    @Before
    public void init() {
        super.init();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testAnalyzeSpecificOriginalOrder() {
        String[] platformOrderNoArray = new String[]{"1618927401"};
        for (int i = 0; i < platformOrderNoArray.length; i++) {
            analyzeOriginalOrder(platformOrderNoArray[i]);
        }
    }

    private void analyzeOriginalOrder(String platformOrderNo) {
        Search search = new Search(OriginalOrder.class);
        List<OriginalOrder> originalOrders = generalDAO.search(search.addFilterEqual("platformOrderNo", platformOrderNo));
        assertThat(originalOrders.size(), is(1));
        messageAnalyzeService.analyzeOriginalOrders(originalOrders);
    }


}
