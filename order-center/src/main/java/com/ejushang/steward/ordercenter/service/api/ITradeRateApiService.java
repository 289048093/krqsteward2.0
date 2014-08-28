package com.ejushang.steward.ordercenter.service.api;

import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.domain.OriginalTraderate;


import java.util.List;

/**
 * User: Shiro
 * Date: 14-8-22
 * Time: 上午11:40
 */
public interface ITradeRateApiService {

    /**
     * 根据Api从外部平台抓取评价
     * @param shopBean
     * @return
     * @throws Exception
     */
    List<OriginalTraderate> fetchTradeRate(ShopBean shopBean) throws Exception;


}
