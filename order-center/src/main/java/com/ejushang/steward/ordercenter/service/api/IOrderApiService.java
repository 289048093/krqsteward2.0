package com.ejushang.steward.ordercenter.service.api;

import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;

import java.util.List;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 14-4-9
 * Time: 下午3:18
 */
public interface IOrderApiService {

    /**
     * 根据Api从外部平台抓取订单
     * @param shopBean
     * @return
     * @throws Exception
     */
    List<OriginalOrder> fetchOrderByApi(ShopBean shopBean) throws Exception;

    /**
     * 从部署服务器抓取外部平台推送的订单
     * @param shopBean
     * @return
     * @throws Exception
     */
    List<OriginalOrder> fetchOrderByDeploy(ShopBean shopBean) throws Exception;

    /**
     * 根据外部平台订单号查询
     * @param shopBean
     * @return
     */
    OriginalOrder fetchOrderById(ShopBean shopBean,String platformOrderNo) throws Exception;

    /**
     * 根据外部平台订单号批量查询
     * @param shopBean
     * @param platformOrderNoList
     * @return
     */
    List<OriginalOrder> fetchOrdersByIds(ShopBean shopBean,List<String> platformOrderNoList) throws Exception;

    /**
     * 根据外部平台订单号批量查询
     * @param shopBeanMapList
     * @return
     */
    List<OriginalOrder> fetchOrdersByIds(List<Map<ShopBean,List<String>>> shopBeanMapList) throws Exception;

}
