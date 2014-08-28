package com.ejushang.steward.ordercenter.service.api.impl.zy;


import com.ejushang.steward.openapicenter.zy.api.ZyLogisticsApi;
import com.ejushang.steward.openapicenter.zy.api.ZyTradeApi;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Trade;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.LogisticsSendResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.TradeGetResponse;
import com.ejushang.steward.openapicenter.zy.constant.ConstantZiYou;
import com.ejushang.steward.openapicenter.zy.exception.ZiYouApiException;
import com.ejushang.steward.ordercenter.bean.LogisticsBean;

import com.ejushang.steward.ordercenter.constant.OriginalOrderStatus;
import com.ejushang.steward.ordercenter.service.api.ILogisticsApiService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-7
 * Time: 上午10:33
 */
public class ZiYouLogisticsApiService implements ILogisticsApiService {
    private static final Logger log = LoggerFactory.getLogger(ZiYouLogisticsApiService.class);

    @Override
    public Boolean sendLogisticsOnline(LogisticsBean logisticsBean) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("订单发货处理：对自有平台订单进行发货处理。参数：" + logisticsBean);
        }

        // 自有API初始化
        // 物流API
        ZyLogisticsApi logisticsApi = new ZyLogisticsApi();
        // 交易API
        ZyTradeApi tradeApi = new ZyTradeApi();

        // 创建保存交易参数的map
        Map<String, Object> tradeArgsMap = new HashMap<String, Object>();
        // 交易id
        tradeArgsMap.put(ConstantZiYou.TID, Long.valueOf(logisticsBean.getOutOrderNo()));
        // 查询交易状态
        TradeGetResponse response = tradeApi.tradeGet(tradeArgsMap);

        Trade trade = response.getTrade();
        if (trade == null) {
            throw new ZiYouApiException(response.getBody());
        }
        // 如果订单状态不为买家已付款，跳过发货操作
        if (!StringUtils.equals(OriginalOrderStatus.WAIT_SELLER_SEND_GOODS.toString(), trade.getStatus())) {
            return true;
        }

        // 创建保存物流参数的map
        Map<String, Object> logisticsArgsMap = new HashMap<String, Object>();
        logisticsArgsMap.put(ConstantZiYou.TID, Long.valueOf(logisticsBean.getOutOrderNo()));

        logisticsArgsMap.put(ConstantZiYou.SID, logisticsBean.getExpressNo());

        logisticsArgsMap.put(ConstantZiYou.COMPANY_CODE, logisticsBean.getExpressCompany());

        LogisticsSendResponse logisticsSendResponse = logisticsApi.logisticsSend(logisticsArgsMap);
        // 操作成功
        if (logisticsSendResponse.getSuccess()) {
            if (log.isInfoEnabled()) {
                log.info("订单发货处理成功！");
            }
            return true;
        }

        if (log.isInfoEnabled()) {
            log.info("订单发货处理失败！");
        }

        return false;
    }

    @Override
    public Boolean fetchLogisticsTrace(LogisticsBean logisticsBean) throws Exception {
        return null;
    }

}
