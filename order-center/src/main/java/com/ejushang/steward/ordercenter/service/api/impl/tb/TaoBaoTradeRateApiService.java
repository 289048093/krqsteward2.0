package com.ejushang.steward.ordercenter.service.api.impl.tb;

import com.ejushang.steward.openapicenter.tb.api.TraderateApi;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.exception.TaoBaoApiException;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.TbRateType;
import com.ejushang.steward.ordercenter.constant.TbTradeRateRole;
import com.ejushang.steward.ordercenter.domain.OriginalTraderate;
import com.ejushang.steward.ordercenter.service.api.ITradeRateApiService;
import com.taobao.api.domain.TradeRate;
import com.taobao.api.response.TraderatesGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * User: Shiro
 * Date: 14-8-22
 * Time: 上午11:39
 */
@Service
@Transactional
public class TaoBaoTradeRateApiService implements ITradeRateApiService {
    private static final Logger log = LoggerFactory.getLogger(TaoBaoTradeRateApiService.class);

    @Override
    public List<OriginalTraderate> fetchTradeRate(ShopBean shopBean) throws Exception {
        return null;

    }

    /**
     * 根据通过淘宝API检索评价
     *
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<TradeRate> getTradeRateByGetBuyer(ShopBean shopBean) throws Exception {
        // 创建保存订单查询的map
        Map<String, Object> tradeArgsMap = new HashMap<String, Object>();
        // 获取并设置查询评价的field
        tradeArgsMap.put(ConstantTaoBao.FIELDS, getTradeFields());
        // 设置每次查询100条 减少调用API的次数
        tradeArgsMap.put(ConstantTaoBao.PAGE_SIZE, ConstantTaoBao.TB_FETCH_RATE_PAGE_SIZE);
        // 设置评价创建时间的起始
        tradeArgsMap.put(ConstantTaoBao.START_CREATED, shopBean.getFetchTradeRateStartDate());
        // 设置评价创建时间的结束
        tradeArgsMap.put(ConstantTaoBao.END_CREATED, shopBean.getFetchTradeRateEndDate());

        tradeArgsMap.put(ConstantTaoBao.RATE_TYPE, TbRateType.GET.tbValue);

        tradeArgsMap.put(ConstantTaoBao.ROLE, TbTradeRateRole.BUYER.tbValue);

        if (log.isInfoEnabled()) {
            log.info("淘宝API抓取评价：开始从淘宝平台查询评价信息……，参数argsMap = " + tradeArgsMap);
        }

        // 淘宝交易API
        TraderateApi traderateApi = new TraderateApi(shopBean.getSessionKey());
        if (log.isInfoEnabled()) {
            log.info("京东API抓取评价：TradeApi初始化：" + traderateApi);
        }

        TraderatesGetResponse response = traderateApi.tradeRatesGet(tradeArgsMap);
        String errorCode = response.getErrorCode();
        if (StringUtils.equals(errorCode, "isv.user-not-exist:invalid-nick")) {
            throw new TaoBaoApiException("ErrorCode：" + errorCode + "，用户未登录或者非法的用户昵称");
        }
        else if (StringUtils.equals(errorCode, "isv.invalid-parameter:role")) {
            throw new TaoBaoApiException("ErrorCode：" + errorCode + "，评价者角色非法;买家身份不能查询评价");
        }
        else if (StringUtils.equals(errorCode, "isv.invalid-parameter:rate_type")) {
            throw new TaoBaoApiException("ErrorCode：" + errorCode + "，评价类型非法");
        }
        else if (StringUtils.equals(errorCode, "isv.invalid-parameter:result")) {
            throw new TaoBaoApiException("ErrorCode：" + errorCode + "，评价结果非法");
        }
        else if (StringUtils.equals(errorCode, "isv.rate-service-error")) {
            throw new TaoBaoApiException("ErrorCode：" + errorCode + "，参数校验错误");
        }
        else if (StringUtils.isNotBlank(errorCode)) {
            throw new TaoBaoApiException("ErrorCode：" + errorCode + "，" + response.getBody());
        }

        return response.getTradeRates();
    }

    private String getTradeFields() {
        List<String> fieldList = new ArrayList<String>();
        fieldList.add("tid");
        fieldList.add("oid");
        fieldList.add("role");
        fieldList.add("nick");
        fieldList.add("result");
        fieldList.add("created");
        fieldList.add("rated_nick");
        fieldList.add("item_title");
        fieldList.add("item_price");
        fieldList.add("content");
        fieldList.add("reply");
        fieldList.add("num_iid");
        fieldList.add("valid_score");
        StringBuffer fieldBuffer = new StringBuffer();

        for (int i = 0; i < fieldList.size(); i++) {
            if (i == 0) {
                fieldBuffer.append(fieldList.get(i));
            } else {
                fieldBuffer.append(",").append(fieldList.get(i));
            }
        }

        return fieldBuffer.toString();
    }
}
