package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Trade;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiListField;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Shiro
 * Date: 14-8-11
 * Time: 下午2:43
 */
public class TradesQueryResponse extends ZiYouResponse {

    private static final long serialVersionUID = 2563855529592583863L;

    @ApiField("total_results")
    private Long totalResults;

    @ApiListField("trades")
    private List<Trade> trades=new ArrayList<Trade>(0);

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
}
