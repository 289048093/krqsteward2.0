package com.ejushang.steward.ordercenter.rmi;

import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


/**
 * User: liubin
 * Date: 14-3-10
 */

public class OriginalOrderRemoteServiceTest extends BaseTest {

    @Autowired
    private IOriginalOrderRemoteService originalOrderRemoteService;

    @Autowired
    private OriginalOrderService originalOrderService;

    @Test
    @Transactional
    public void test() {
        List<OriginalOrder> originalOrders = originalOrderService.findUnprocessedOriginalOrders();
        if(originalOrders.size() > 10) {
            originalOrders = originalOrders.subList(0, 10);
        }
        List<Integer> ids = new ArrayList<Integer>();
        for(OriginalOrder originalOrder : originalOrders) {
            ids.add(originalOrder.getId());
        }

        assertThat(originalOrderRemoteService.tryAnalyzeOriginalOrders(ids), is(true));
    }

}
