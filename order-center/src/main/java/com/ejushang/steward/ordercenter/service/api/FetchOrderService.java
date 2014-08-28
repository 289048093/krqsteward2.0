package com.ejushang.steward.ordercenter.service.api;

import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.FetchDataType;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.service.api.impl.jd.JingDongOrderApiService;
import com.ejushang.steward.ordercenter.service.api.impl.tb.TaoBaoOrderApiService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 2014/7/14
 * Time: 14:58
 */
@Service
public class FetchOrderService {

    private static final Logger log = LoggerFactory.getLogger(FetchOrderService.class);
    @Autowired
    private TaoBaoOrderApiService taoBaoOrderApiService;

    @Autowired
    private JingDongOrderApiService jingDongOrderApiService;

    /**
     * 抓取店铺的订单数据
     * @param shopBean
     * @return
     */
    public List<OriginalOrder> fetchOrdersByShopBean(ShopBean shopBean){
        List<OriginalOrder> originalOrderList = new ArrayList<OriginalOrder>();
        if(shopBean == null){
            return originalOrderList;
        }

        if(!shopBean.getFetchDataTypeList().contains(FetchDataType.FETCH_ORDER)){
            return originalOrderList;
        }

        if(StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(), PlatformType.TAO_BAO.getName())
                || StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(),PlatformType.TAO_BAO_2.getName())){
            boolean isExistException = false;
            try {
                originalOrderList = taoBaoOrderApiService.fetchOrderByDeploy(shopBean);
            } catch (Exception e) {
                isExistException = true;
                if(log.isErrorEnabled()) {
                    log.error("聚石塔抓取订单异常：【"+shopBean.getSellerNick()+"】抓取订单出现异常", e);
                }
            }
            if(isExistException){
                try {
                    originalOrderList = taoBaoOrderApiService.fetchOrderByApi(shopBean);
                } catch (Exception e) {
                    if(log.isErrorEnabled()) {
                        log.error("淘宝API抓取订单异常：【"+shopBean.getSellerNick()+"】抓取订单出现异常", e);
                    }
                }
            }
        }
        else if(StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(),PlatformType.JING_DONG.getName())){
            try {
                originalOrderList = jingDongOrderApiService.fetchOrderByApi(shopBean);
            } catch (Exception e) {
                if(log.isErrorEnabled()) {
                    log.error("京东API抓取订单异常：【"+shopBean.getSellerNick()+"】抓取订单出现异常", e);
                }
            }
        }

        return originalOrderList;
    }

    /**
     * 批量抓取店铺的订单数据
     * @param shopBeanList
     * @return
     */
    public List<OriginalOrder> fetchOrdersByShopBeans(List<ShopBean> shopBeanList){
        List<OriginalOrder> allOriginalOrderList = new ArrayList<OriginalOrder>();
        List<OriginalOrder> originalOrderList = null;
        if(CollectionUtils.isNotEmpty(shopBeanList)){
            for(ShopBean shopBean : shopBeanList){
                originalOrderList = fetchOrdersByShopBean(shopBean);
                if(CollectionUtils.isNotEmpty(originalOrderList)){
                    allOriginalOrderList.addAll(originalOrderList);
                }
            }
        }
        return allOriginalOrderList;
    }

    /**
     * 根据id抓取订单
     * @param shopBeanOri
     * @param platformNoList
     * @return
     * @throws Exception
     */
    public List<OriginalOrder> fetchOrdersByIds(ShopBean shopBeanOri, List<String> platformNoList){
        List<OriginalOrder> originalOrderList = new ArrayList<OriginalOrder>();
        if(StringUtils.equalsIgnoreCase(shopBeanOri.getPlatformType().getName(), PlatformType.TAO_BAO.getName())
                || StringUtils.equalsIgnoreCase(shopBeanOri.getPlatformType().getName(),PlatformType.TAO_BAO_2.getName())){
            try {
                originalOrderList = taoBaoOrderApiService.fetchOrdersByIds(shopBeanOri,platformNoList);
            } catch (Exception e) {
                if(log.isErrorEnabled()) {
                    log.error("淘宝API抓取订单异常：【"+shopBeanOri.getSellerNick()+"】抓取订单出现异常", e);
                }
            }
        }
        else if(StringUtils.equalsIgnoreCase(shopBeanOri.getPlatformType().getName(),PlatformType.JING_DONG.getName())){
            try {
                originalOrderList = jingDongOrderApiService.fetchOrdersByIds(shopBeanOri,platformNoList);
            } catch (Exception e) {
                if(log.isErrorEnabled()) {
                    log.error("京东API抓取订单异常：【"+shopBeanOri.getSellerNick()+"】抓取订单出现异常", e);
                }
            }
        }

        return originalOrderList;
    }

}
