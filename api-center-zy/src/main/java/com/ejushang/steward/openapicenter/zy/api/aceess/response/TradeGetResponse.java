package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Trade;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

/**
 * User: Baron.Zhang
 * Date: 2014/8/7
 * Time: 11:17
 */
public class TradeGetResponse extends ZiYouResponse{
    private static final long serialVersionUID = 6409306746834752236L;

    /**
     *  搜索到的交易信息列表
     */
    @ApiField("trade")
    private Trade trade;

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }
}
