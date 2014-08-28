package com.ejushang.steward.ordercenter.service.api;

import com.ejushang.steward.ordercenter.bean.LogisticsBean;

/**
 * User: Baron.Zhang
 * Date: 14-4-9
 * Time: 下午3:20
 */
public interface ILogisticsApiService {

    /**
     * 将对应的原始订单在平台上的状态变更为已发货
     * @param logisticsBean
     */
    Boolean sendLogisticsOnline(LogisticsBean logisticsBean) throws Exception;

    /**
     * 查询物流流转信息<br/>
     * 淘宝：根据交易id（tid）、卖家昵称（sellerNick）查询<br/>
     * 京东：根据运单号（waybillCode）查询<br/>
     * @param logisticsBean
     */
    Boolean fetchLogisticsTrace(LogisticsBean logisticsBean) throws Exception;
}
