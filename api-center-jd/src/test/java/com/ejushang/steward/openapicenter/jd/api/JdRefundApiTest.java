package com.ejushang.steward.openapicenter.jd.api;

import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.ejushang.steward.openapicenter.jd.exception.JingDongApiException;
import com.jd.open.api.sdk.domain.service.RefundapplySaf.QueryMap;
import com.jd.open.api.sdk.domain.service.RefundapplySaf.RefundapplyResponse;
import com.jd.open.api.sdk.response.service.PopAfsRefundapplyQuerylistResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/5/13
 * Time: 16:37
 */
public class JdRefundApiTest {

    private static final Logger log = LoggerFactory.getLogger(JdRefundApiTest.class);

    private String sessionKey = "4d3562cc-0300-4039-9cec-edb933c12251";

    @Test
    public void testPopAfsRefundapplyQuerylist() throws Exception {
        Map<String,Object> argsMap = new HashMap<String, Object>();
        //argsMap.put(ConstantJingDong.STATUS,"");
        argsMap.put(ConstantJingDong.APPLY_TIME_START, "2014-05-01 00:00:00");
        argsMap.put(ConstantJingDong.APPLY_TIME_END,"2014-05-14 00:00:00");
        argsMap.put(ConstantJingDong.PAGE_INDEX,1);
        argsMap.put(ConstantJingDong.PAGE_SIZE, ConstantJingDong.JD_FETCH_REFUND_PAGE_SIZE);

        // 通过api抓取京东售前退款
        JdRefundApi refundApi = new JdRefundApi(sessionKey);
        PopAfsRefundapplyQuerylistResponse response = refundApi.popAfsRefundapplyQuerylist(argsMap);

        response.getMsg();

        RefundapplyResponse refundapplyResponse = response.getRefundApplyResponse();
        // 查询失败
        if(!refundapplyResponse.getResultState()){
            throw new JingDongApiException(refundapplyResponse.getResultInfo());
        }

        List<QueryMap> queryMapList = refundapplyResponse.getResults();
    }
}
