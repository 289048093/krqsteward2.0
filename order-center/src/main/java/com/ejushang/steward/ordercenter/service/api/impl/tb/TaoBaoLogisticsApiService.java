package com.ejushang.steward.ordercenter.service.api.impl.tb;


import com.ejushang.steward.openapicenter.tb.api.TbLogisticsApi;
import com.ejushang.steward.openapicenter.tb.api.TbTradeApi;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.exception.TaoBaoApiException;
import com.ejushang.steward.ordercenter.bean.LogisticsBean;
import com.ejushang.steward.ordercenter.constant.DeliveryType;
import com.ejushang.steward.ordercenter.constant.OriginalOrderStatus;
import com.ejushang.steward.ordercenter.service.api.ILogisticsApiService;
import com.taobao.api.domain.Shipping;
import com.taobao.api.domain.Trade;
import com.taobao.api.domain.TransitStepInfo;
import com.taobao.api.response.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 14-4-11
 * Time: 下午1:43
 */
@Service
@Transactional
public class TaoBaoLogisticsApiService implements ILogisticsApiService {

    private static final Logger log = LoggerFactory.getLogger(TaoBaoLogisticsApiService.class);

    @Override
    public Boolean sendLogisticsOnline(LogisticsBean logisticsBean) throws Exception {
        if(log.isInfoEnabled()){
            log.info("在线订单发货处理：对淘宝平台订单进行在线发货处理。参数："+logisticsBean+",sessionKey" + logisticsBean.getSessionKey());
        }

        // 淘宝API初始化
        // 物流API
        TbLogisticsApi logisticsApi = new TbLogisticsApi(logisticsBean.getSessionKey());
        // 交易API
        TbTradeApi tradeApi = new TbTradeApi(logisticsBean.getSessionKey());

        // 创建保存交易参数的map
        Map<String,Object> tradeArgsMap = new HashMap<String, Object>();
        // 交易字段
        tradeArgsMap.put(ConstantTaoBao.FIELDS,getTradeFields());
        // 交易id
        tradeArgsMap.put(ConstantTaoBao.TID,Long.valueOf(logisticsBean.getOutOrderNo()));

        // 查询交易状态
        TradeFullinfoGetResponse response = tradeApi.tradeFullinfoGet(tradeArgsMap);

        Trade trade = response.getTrade();
        if(trade == null){
            throw new TaoBaoApiException(response.getBody());
        }
        // 如果订单状态不为买家已付款，跳过发货操作
        if(!StringUtils.equals(OriginalOrderStatus.WAIT_SELLER_SEND_GOODS.toString(), trade.getStatus())){
            return true;
        }

        // 创建保存物流参数的map
        Map<String,Object> logisticsArgsMap = new HashMap<String, Object>();
        // 淘宝交易ID
        logisticsArgsMap.put(ConstantTaoBao.TID, Long.valueOf(logisticsBean.getOutOrderNo()));
        // 运单号.具体一个物流公司的真实运单号码。淘宝官方物流会校验，请谨慎传入；
        logisticsArgsMap.put(ConstantTaoBao.OUT_SID,logisticsBean.getExpressNo());
        // 物流公司代码.如"POST"就代表中国邮政,"ZJS"就代表宅急送.调用 taobao.logistics.companies.get 获取。
        // 如果是货到付款订单，选择的物流公司必须支持货到付款发货方式
        logisticsArgsMap.put(ConstantTaoBao.COMPANY_CODE, DeliveryType.valueOf(logisticsBean.getExpressCompany()).getTmallCode());

        // 淘宝在线发货处理
        LogisticsOnlineSendResponse logisticsOnlineSendResponse = logisticsApi.logisticsOnlineSend(logisticsArgsMap);
        //LogisticsOnlineConfirmResponse logisticsOnlineConfirmResponse = logisticsApi.logisticsOnlineConfirm(logisticsArgsMap);

        Shipping shipping = logisticsOnlineSendResponse.getShipping();
        if(shipping == null){
            if(log.isInfoEnabled()){
                log.info("淘宝线上发货失败！{}",logisticsOnlineSendResponse.getBody());
                log.info("开始使用淘宝线下发货...");
            }
            LogisticsOfflineSendResponse logisticsOfflineSendResponse = logisticsApi.logisticsOfflineSend(logisticsArgsMap);
            shipping = logisticsOfflineSendResponse.getShipping();

            if(shipping == null) {
                throw new TaoBaoApiException("淘宝订单号：" + logisticsBean.getOutOrderNo() + ","
                        + "淘宝运单号：" + logisticsBean.getExpressNo() + ","
                        + "淘宝物流公司：" + logisticsBean.getExpressCompany() + "," + logisticsOnlineSendResponse.getBody()
                        +","+logisticsOfflineSendResponse.getBody());
            }
        }



        // 操作成功
        if(shipping.getIsSuccess()){
            if(log.isInfoEnabled()){
                log.info("在线订单发货处理成功！");
            }
            return true;
        }

        if(log.isInfoEnabled()){
            log.info("在线订单发货处理失败！");
        }

        return false;
    }

    @Override
    public Boolean fetchLogisticsTrace(LogisticsBean logisticsBean) throws Exception {
        if(log.isInfoEnabled()){
            log.info("淘宝获取物流流转信息：开始获取，参数{}",logisticsBean);
        }

        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantTaoBao.TID,StringUtils.isNotBlank(logisticsBean.getOutOrderNo()) ? Long.valueOf(logisticsBean.getOutOrderNo()) : null);
        argsMap.put(ConstantTaoBao.SELLER_NICK,logisticsBean.getSellerNick());

        if(log.isInfoEnabled()){
            log.info("淘宝获取物流流转信息：参数TID={}",StringUtils.isNotBlank(logisticsBean.getOutOrderNo()) ? Long.valueOf(logisticsBean.getOutOrderNo()) : null);
            log.info("淘宝获取物流流转信息：SELLER_NICK={}",logisticsBean.getSellerNick());
        }

        TbLogisticsApi logisticsApi = new TbLogisticsApi(logisticsBean.getSessionKey());

        LogisticsTraceSearchResponse response = logisticsApi.logisticsTraceSearch(argsMap);

        if(log.isInfoEnabled()){
            log.info("淘宝获取物流流转信息：LogisticsTraceSearchResponse={}",response);
            log.info("淘宝获取物流流转信息：getCompanyName={}",response.getCompanyName());
            log.info("淘宝获取物流流转信息：getOutSid={}",response.getOutSid());
            log.info("淘宝获取物流流转信息：getStatus={}",response.getStatus());
            log.info("淘宝获取物流流转信息：getTid={}",response.getTid());
            log.info("淘宝获取物流流转信息：getTraceList={}",response.getTraceList());
            log.info("淘宝获取物流流转信息：body={}",response.getBody());
        }

        if(StringUtils.isNotBlank(response.getErrorCode())){
            throw new TaoBaoApiException(response.getBody());
        }

        List<TransitStepInfo> transitStepInfoList = response.getTraceList();

        if(log.isInfoEnabled()){
            log.info("淘宝获取物流流转信息：获取到的物流流转信息条数为:{}",transitStepInfoList.size());
        }

        return CollectionUtils.isNotEmpty(transitStepInfoList);
    }

    /**
     * 获取交易字段
     * @return
     */
    private String getTradeFields(){
        StringBuffer stringBuffer = new StringBuffer();
        // 交易id
        stringBuffer.append("tid,");
        // 交易状态
        stringBuffer.append("status");

        return stringBuffer.toString();
    }
}
