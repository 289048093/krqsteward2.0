package com.ejushang.steward.ordercenter.service.api.impl.jd;


import com.ejushang.steward.common.util.EjsMapUtils;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.openapicenter.jd.api.JdLogisticsApi;
import com.ejushang.steward.openapicenter.jd.api.JdOrderApi;
import com.ejushang.steward.openapicenter.jd.api.JdRefundApi;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.ejushang.steward.openapicenter.jd.exception.JingDongApiException;
import com.ejushang.steward.openapicenter.tb.api.TbTradeApi;
import com.ejushang.steward.ordercenter.bean.LogisticsBean;
import com.ejushang.steward.ordercenter.constant.DeliveryType;
import com.ejushang.steward.ordercenter.constant.JdOrderStatus;
import com.ejushang.steward.ordercenter.service.api.ILogisticsApiService;
import com.jd.open.api.sdk.domain.order.OrderDetailInfo;
import com.jd.open.api.sdk.response.delivery.EtmsTraceGetResponse;
import com.jd.open.api.sdk.response.order.OrderGetResponse;
import com.jd.open.api.sdk.response.order.OrderSopOutstorageResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 14-4-11
 * Time: 下午1:38
 */
@Service
@Transactional
public class JingDongLogisticsApiService implements ILogisticsApiService {

    private static final Logger log = LoggerFactory.getLogger(JingDongLogisticsApiService.class);

    @Override
    public Boolean sendLogisticsOnline(LogisticsBean logisticsBean) throws Exception {
        if(log.isInfoEnabled()){
            log.info("京东在线订单发货处理：对京东平台订单进行在线发货处理。参数："+logisticsBean+",sessionKey" + logisticsBean.getSessionKey());
        }

        // 淘宝API初始化
        // 物流API
        JdOrderApi orderApi = new JdOrderApi(logisticsBean.getSessionKey());

        // 创建保存交易参数的map
        Map<String,Object> orderArgsMap = new HashMap<String, Object>();
        // 交易字段
        orderArgsMap.put(ConstantJingDong.OPTIONAL_FIELDS,getOrderFields());
        // 交易id
        orderArgsMap.put(ConstantJingDong.ORDER_ID,logisticsBean.getOutOrderNo());

        String orderStatus = "";
        OrderGetResponse response = null;
        try {
            response = orderApi.orderGet(orderArgsMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(response != null) {
            OrderDetailInfo orderDetailInfo = response.getOrderDetailInfo();
            if (!StringUtils.equalsIgnoreCase("0", response.getCode())) {
                throw new JingDongApiException(response.getMsg());
            }
            orderStatus =  orderDetailInfo.getOrderInfo().getOrderState();
        }
        else{
            String responseJson = orderApi.orderGetBySelf(orderArgsMap,logisticsBean.getSessionKey());
            Map<String,Object> responseMap = JsonUtil.json2Object(responseJson,HashMap.class);
            String code = String.valueOf(EjsMapUtils.findSpec(responseMap, "code"));
            if (!StringUtils.equalsIgnoreCase("0",code)) {
                throw new JingDongApiException(responseMap.toString());
            }
            orderStatus = String.valueOf(EjsMapUtils.findSpec(responseMap, "order_state"));
        }

        if(log.isInfoEnabled()){
            log.info("当前订单【{}】的订单状态为：【{}】",logisticsBean.getOutOrderNo(),orderStatus);
        }

        // 如果订单状态不为买家已付款，跳过发货操作
        if (!StringUtils.equals(JdOrderStatus.WAIT_SELLER_STOCK_OUT.toString(), orderStatus)) {
            if(log.isInfoEnabled()){
                log.info("订单【{}】,外部订单【{}】状态不为买家已付款，跳过发货操作",logisticsBean.getOrderNo(),logisticsBean.getOutOrderNo());
            }
            return true;
        }

        // 创建保存物流参数的map
        Map<String,Object> logisticsArgsMap = new HashMap<String, Object>();
        // 淘宝交易ID
        logisticsArgsMap.put(ConstantJingDong.ORDER_ID, logisticsBean.getOutOrderNo());
        // 运单号.具体一个物流公司的真实运单号码。淘宝官方物流会校验，请谨慎传入；
        logisticsArgsMap.put(ConstantJingDong.WAYBILL,logisticsBean.getExpressNo());
        // 物流公司代码.如"POST"就代表中国邮政,"ZJS"就代表宅急送.调用 taobao.logistics.companies.get 获取。
        logisticsArgsMap.put(ConstantJingDong.LOGISTICS_ID, DeliveryType.valueOf(logisticsBean.getExpressCompany()).getJdId());

        OrderSopOutstorageResponse orderSopOutstorageResponse = orderApi.orderSopOutstorage(logisticsArgsMap);

        // 非正常和订单已发货的情况不抛出异常
        if(!StringUtils.equalsIgnoreCase("0",orderSopOutstorageResponse.getCode())
                && !StringUtils.equalsIgnoreCase("10400001",orderSopOutstorageResponse.getCode())){
            throw new JingDongApiException(
                    "京东订单号："+logisticsBean.getOutOrderNo()+","+
                    "京东运单号："+logisticsBean.getExpressNo()+","+
                    "京东物流公司："+logisticsBean.getExpressCompany()+","+
                    orderSopOutstorageResponse.getMsg());
        }

        if(log.isInfoEnabled()){
            log.info("京东在线订单发货处理成功！");
        }

        return true;
    }

    @Override
    public Boolean fetchLogisticsTrace(LogisticsBean logisticsBean) throws Exception {
        if(log.isInfoEnabled()){
            log.info("京东获取物流流转信息：获取开始，参数{}",logisticsBean);
        }

        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantJingDong.WAYBILLCODE,logisticsBean.getExpressNo());

        JdLogisticsApi logisticsApi = new JdLogisticsApi(logisticsBean.getSessionKey());

        EtmsTraceGetResponse response = logisticsApi.etmsTraceGet(argsMap);

        if(!StringUtils.equalsIgnoreCase("0",response.getCode())){
            throw new JingDongApiException(response.getMsg());
        }

        if(log.isInfoEnabled()){
            log.info("京东获取物流流转信息：获取到的物流流转信息条数为:{}",response.getTraceApiDtos().size());
        }

        return CollectionUtils.isNotEmpty(response.getTraceApiDtos());
    }

    /**
     * 获取交易字段
     * @return
     */
    private String getOrderFields(){
        StringBuffer stringBuffer = new StringBuffer();
        // 交易id
        stringBuffer.append("order_id,");
        // 交易状态
        stringBuffer.append("order_state");

        return stringBuffer.toString();
    }
}
