package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.taobao.api.domain.LogisticsCompany;
import com.taobao.api.response.LogisticsCompaniesGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/5/27
 * Time: 15:15
 */
public class TbLogisticsApiTest {

    private String sessionKey = "62001037ac9ZZ7bf33b3985499e7c66beafed56534739c21675300784";

    private TbLogisticsApi tbLogisticsApi = new TbLogisticsApi(sessionKey);

    @Test
    public void testLogisticsCompaniesGet() throws Exception {
        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantTaoBao.FIELDS,"id,code,name,reg_mail_no");
        LogisticsCompaniesGetResponse response = tbLogisticsApi.logisticsCompaniesGet(argsMap);

        if(StringUtils.isBlank(response.getErrorCode())){
            List<LogisticsCompany> logisticsCompanyList = response.getLogisticsCompanies();
            for(LogisticsCompany logisticsCompany : logisticsCompanyList){
                System.out.println("物流公司编号：" + logisticsCompany.getId()
                    + "，物流公司代码：" + logisticsCompany.getCode()
                    + "，物流公司简称：" + logisticsCompany.getName()
                    + "，运单号验证正则表达式：" + logisticsCompany.getRegMailNo()
                );
            }
        }
    }
}
