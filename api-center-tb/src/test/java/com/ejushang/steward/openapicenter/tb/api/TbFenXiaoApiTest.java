package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.taobao.api.domain.PurchaseOrder;
import com.taobao.api.response.FenxiaoOrdersGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.*;

/**
 * User: Baron.Zhang
 * Date: 14-4-24
 * Time: 上午11:42
 */
public class TbFenXiaoApiTest {

    private String sessionKey = "6200900bdf26d90edf44fc3efdd69588f821b16c2674f7c1914910482";

    @Test
    public void testFenxiaoOrdersGet() throws Exception {
        Map<String,Object> argsMap = new HashMap<String, Object>();

        Date startDate = EJSDateUtils.parseDate("2014-03-01 00:00:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
        Date endDate = EJSDateUtils.parseDate("2014-03-07 23:59:59", EJSDateUtils.DateFormatType.DATE_FORMAT_STR);

        argsMap.put(ConstantTaoBao.START_CREATED, startDate);
        argsMap.put(ConstantTaoBao.END_CREATED, endDate);
        argsMap.put(ConstantTaoBao.PAGE_NO,1L);
        argsMap.put(ConstantTaoBao.PAGE_SIZE,50L);

        TbFenXiaoApi tbFenXiaoApi = new TbFenXiaoApi(sessionKey);

        FenxiaoOrdersGetResponse response = tbFenXiaoApi.fenxiaoOrdersGet(argsMap);

        System.out.println("ErrorCode：" + response.getErrorCode());

        if(StringUtils.equals(response.getErrorCode(),"isv.invalid-parameter:user_id_num")){
            throw new Exception("ErrorCode：" + response.getErrorCode()+"，用户数字ID不合法，或者不是分销平台用户");
        }
        else if(StringUtils.equals(response.getErrorCode(),"isv.invalid-parameter:purchaseorder_intervaldate")){
            throw new Exception("ErrorCode：" + response.getErrorCode()+"，采购单查询的起始时间与结束时间跨度不能超过7天");
        }
        else if(StringUtils.equals(response.getErrorCode(),"isv.invalid-parameter:purchaseorder_empty")){
            throw new Exception("ErrorCode：" + response.getErrorCode()+"，此错误是 查询订单的起始时间，结束时间为空或者间隔超过7天。");
        }

        Long totalCount = response.getTotalResults();
        List<PurchaseOrder> purchaseOrderList = response.getPurchaseOrders();

        System.out.println("总条数："+totalCount);
        System.out.println("当前条数：" + purchaseOrderList.size());

        for (PurchaseOrder purchaseOrder : purchaseOrderList){

        }

    }

    public String getFenXiaoOrderFields(){
        List<String> fieldList = new ArrayList<String>();
        fieldList.add("supplier_memo");
        fieldList.add("fenxiao_id");
        fieldList.add("pay_type");
        fieldList.add("trade_type");
        fieldList.add("distributor_from");
        fieldList.add("id");
        fieldList.add("status");
        fieldList.add("buyer_nick");
        fieldList.add("memo");
        fieldList.add("tc_order_id");
        fieldList.add("receiver");
        fieldList.add("shipping");
        fieldList.add("logistics_company_name");
        fieldList.add("logistics_id");
        fieldList.add("isv_custom_key");
        fieldList.add("isv_custom_value");
        fieldList.add("end_time");
        fieldList.add("supplier_flag");
        fieldList.add("buyer_payment");
        fieldList.add("order_messages");
        fieldList.add("sub_purchase_orders");
        fieldList.add("buyer_taobao_id");
        fieldList.add("features");
        fieldList.add("supplier_from");
        fieldList.add("supplier_username");
        fieldList.add("distributor_username");
        fieldList.add("created");
        fieldList.add("alipay_no");
        fieldList.add("total_fee");
        fieldList.add("post_fee");
        fieldList.add("distributor_payment");
        fieldList.add("snapshot_url");
        fieldList.add("pay_time");
        fieldList.add("consign_time");
        fieldList.add("modified");

        StringBuffer sb = new StringBuffer("");
        for(int i = 0; i < fieldList.size(); i++){
            if(i == 0){
                sb.append(fieldList.get(i));
            }
            else{
                sb.append(",").append(fieldList.get(i));
            }
        }

        return sb.toString();
    }
}
