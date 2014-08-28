package com.ejushang.steward.ordercenter.service.api;

import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.domain.OriginalRefund;

import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 14-4-29
 * Time: 下午3:49
 */
public interface IRefundApiService {
    /**
     * 根据Api从外部平台抓取退款单
     * @param shopBean
     * @return
     */
    List<OriginalRefund> fetchRefundByApi(ShopBean shopBean) throws Exception;

    /**
     * 从部署服务器抓取外部平台推送的退款单
     * @param shopBean
     * @return
     */
    List<OriginalRefund> fetchRefundByDeploy(ShopBean shopBean) throws Exception;

    /**
     * 根据Api从外部平台抓取退款单
     * @param shopBean
     * @return
     */
    List<OriginalRefund> fetchReturnByApi(ShopBean shopBean) throws Exception;

    /**
     * 从部署服务器抓取外部平台推送的退款单
     * @param shopBean
     * @return
     */
    List<OriginalRefund> fetchReturnByDeploy(ShopBean shopBean) throws Exception;

}
