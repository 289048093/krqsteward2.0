package com.ejushang.steward.ordercenter.service.api;

import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.FetchDataType;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.OriginalRefund;
import com.ejushang.steward.ordercenter.service.api.impl.jd.JingDongRefundApiService;
import com.ejushang.steward.ordercenter.service.api.impl.tb.TaoBaoRefundApiService;
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
 * Time: 15:02
 */
@Service
public class FetchRefundService {

    private static final Logger log = LoggerFactory.getLogger(FetchRefundService.class);
    @Autowired
    private TaoBaoRefundApiService taoBaoRefundApiService;

    @Autowired
    private JingDongRefundApiService jingDongRefundApiService;


    /**
     * 抓取店铺的退款单数据
     * @param shopBean
     * @return
     */
    public List<OriginalRefund> fetchRefundsByShopBean(ShopBean shopBean){
        List<OriginalRefund> originalRefundList = new ArrayList<OriginalRefund>();

        if(shopBean == null){
            return originalRefundList;
        }

        if(!shopBean.getFetchDataTypeList().contains(FetchDataType.FETCH_REFUND)){
            return originalRefundList;
        }

        // 淘宝
        if(StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(), PlatformType.TAO_BAO.getName())
                || StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(),PlatformType.TAO_BAO_2.getName())){
            boolean isExistException = false;
            try {
                originalRefundList = taoBaoRefundApiService.fetchRefundByDeploy(shopBean);
            } catch (Exception e) {
                if(log.isErrorEnabled()) {
                    log.error("聚石塔抓取退款单异常：【"+shopBean.getSellerNick()+"】抓取退款单出现异常", e);
                }
                isExistException = true;
            }
            if(isExistException){
                try {
                    originalRefundList = taoBaoRefundApiService.fetchRefundByApi(shopBean);
                } catch (Exception e) {
                    if(log.isErrorEnabled()) {
                        log.error("淘宝API抓取退款单异常：【"+shopBean.getSellerNick()+"】抓取退款单出现异常", e);
                    }
                }
            }
        }// 京东
        else if(StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(),PlatformType.JING_DONG.getName())){
            try {
                originalRefundList = jingDongRefundApiService.fetchRefundByApi(shopBean);
            } catch (Exception e) {
                if(log.isErrorEnabled()) {
                    log.error("淘宝API抓取退货单异常：【"+shopBean.getSellerNick()+"】抓取退货单出现异常", e);
                }
            }
        }

        return originalRefundList;
    }

    /**
     * 批量抓取店铺的退款单数据
     * @param shopBeanList
     * @return
     */
    public List<OriginalRefund> fetchRefundsByShopBeans(List<ShopBean> shopBeanList){
        List<OriginalRefund> allOriginalRefundList = new ArrayList<OriginalRefund>();
        List<OriginalRefund> originalRefundList = null;
        if(CollectionUtils.isNotEmpty(shopBeanList)){
            for(ShopBean shopBean : shopBeanList){
                originalRefundList = fetchRefundsByShopBean(shopBean);
                if(CollectionUtils.isNotEmpty(originalRefundList)){
                    allOriginalRefundList.addAll(originalRefundList);
                }
            }
        }
        return allOriginalRefundList;
    }

    /**
     * 抓取店铺的退货单数据
     * @param shopBean
     * @return
     */
    public List<OriginalRefund> fetchReturnsByShopBean(ShopBean shopBean){
        List<OriginalRefund> originalRefundList = new ArrayList<OriginalRefund>();

        if(shopBean == null){
            return originalRefundList;
        }

        if(!shopBean.getFetchDataTypeList().contains(FetchDataType.FETCH_RETURN)){
            return originalRefundList;
        }

        // 淘宝
        if(StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(), PlatformType.TAO_BAO.getName())
                || StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(),PlatformType.TAO_BAO_2.getName())){
            boolean isExistException = false;
            try {
                originalRefundList = taoBaoRefundApiService.fetchReturnByDeploy(shopBean);
            } catch (Exception e) {
                if(log.isErrorEnabled()) {
                    log.error("聚石塔抓取退货单异常：【"+shopBean.getSellerNick()+"】抓取退货单出现异常", e);
                }
                isExistException = true;
            }
            if(isExistException){
                try {
                    originalRefundList = taoBaoRefundApiService.fetchReturnByApi(shopBean);
                } catch (Exception e) {
                    if(log.isErrorEnabled()) {
                        log.error("淘宝API抓取退货单异常：【"+shopBean.getSellerNick()+"】抓取退货单出现异常", e);
                    }
                }
            }
        }// 京东
        else if(StringUtils.equalsIgnoreCase(shopBean.getPlatformType().getName(),PlatformType.JING_DONG.getName())){
            try {
                originalRefundList = jingDongRefundApiService.fetchReturnByApi(shopBean);
            } catch (Exception e) {
                if(log.isErrorEnabled()) {
                    log.error("京东API抓取退货单异常：【"+shopBean.getSellerNick()+"】抓取退货单出现异常", e);
                }
            }
        }

        return originalRefundList;
    }

    /**
     * 批量抓取店铺的退货单数据
     * @param shopBeanList
     * @return
     */
    public List<OriginalRefund> fetchReturnsByShopBeans(List<ShopBean> shopBeanList){
        List<OriginalRefund> allOriginalReturnList = new ArrayList<OriginalRefund>();
        List<OriginalRefund> originalReturnList = null;
        if(CollectionUtils.isNotEmpty(shopBeanList)){
            for(ShopBean shopBean : shopBeanList) {
                originalReturnList = fetchReturnsByShopBean(shopBean);
                if(CollectionUtils.isNotEmpty(originalReturnList)){
                    allOriginalReturnList.addAll(originalReturnList);
                }
            }
        }
        return allOriginalReturnList;
    }

}
