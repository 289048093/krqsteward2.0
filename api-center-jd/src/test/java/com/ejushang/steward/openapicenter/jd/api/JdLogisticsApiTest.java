package com.ejushang.steward.openapicenter.jd.api;

import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.ejushang.steward.openapicenter.jd.exception.JingDongApiException;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.domain.delivery.LogisticsCompanies;
import com.jd.open.api.sdk.domain.delivery.LogisticsCompany;
import com.jd.open.api.sdk.response.delivery.DeliveryLogisticsGetResponse;
import com.jd.open.api.sdk.response.order.OrderSopOutstorageResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/5/27
 * Time: 15:03
 */
public class JdLogisticsApiTest {

    private String sessionKey = "29505106-547b-4827-8077-047fb565c454";

    private JdLogisticsApi jdLogisticsApi = new JdLogisticsApi(sessionKey);

    @Test
    public void testDeliveryLogisticsGet() throws Exception {
        DeliveryLogisticsGetResponse response = jdLogisticsApi.deliveryLogisticsGet(new HashMap<String, Object>());

        if(StringUtils.equalsIgnoreCase("0",response.getCode())){
            LogisticsCompanies logisticsCompanies = response.getLogisticsCompanies();
            System.out.println("商家id：" + logisticsCompanies.getVenderId());

            List<LogisticsCompany> logisticsCompanyList = logisticsCompanies.getLogisticsList();

            for(LogisticsCompany logisticsCompany : logisticsCompanyList){
                System.out.println("物流公司ID：" + logisticsCompany.getLogisticsId()
                    + ",物流公司名称：" + logisticsCompany.getLogisticsName()
                    + ",备注说明：" + logisticsCompany.getLogisticsRemark()
                    + ",序列：" + logisticsCompany.getSequence()
                    + ",agreeFlag：" + logisticsCompany.getAgreeFlag()
                );
            }
        }

    }

    @Test
    public void test(){

    }
}
