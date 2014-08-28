package com.ejushang.steward.openapicenter.zy.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.zy.api.aceess.DefaultZiYouClient;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.RefundGetRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.RefundsQueryRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.RefundGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.RefundsQueryResponse;
import com.ejushang.steward.openapicenter.zy.constant.ConstantZiYou;

import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-12
 * Time: 上午11:41
 */
public class ZyRefundApi {
    private DefaultZiYouClient client;
    public ZyRefundApi(){
        client = new DefaultZiYouClient(ConstantZiYou.ZY_API_URL,ConstantZiYou.ZY_APP_KEY,ConstantZiYou.ZY_APP_SECRET,ConstantZiYou.YYL_ACCESS_TOKEN);
    }

    /**
     *批量退款退货单查询
     * @param argsMap
     * @return
     * @throws Exception
     */
   public RefundsQueryResponse refundsQuery(Map<String,Object> argsMap) throws Exception{
       RefundsQueryRequest request=new RefundsQueryRequest();
       ReflectUtils.executeMethods(request,argsMap);
       return client.excute(request,ConstantZiYou.YYL_ACCESS_TOKEN);
   }

    /**
     * 单笔退款退货单查询
     * @param argsMap
     * @return
     * @throws Exception
     */
   public RefundGetResponse  refundGet(Map<String,Object> argsMap) throws Exception{
       RefundGetRequest request=new RefundGetRequest();
       ReflectUtils.executeMethods(request,argsMap);
       return client.excute(request,ConstantZiYou.YYL_ACCESS_TOKEN);
   }

}
