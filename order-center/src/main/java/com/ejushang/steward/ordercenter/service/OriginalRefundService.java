package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 原始退款单服务
 * User: Baron.Zhang
 * Date: 2014/5/10
 * Time: 14:04
 */
@Service
@Transactional
public class OriginalRefundService {
    private static final Logger log = LoggerFactory.getLogger(OriginalRefund.class);

    @Autowired
    private GeneralDAO generalDAO;

    /**
     * 根据条件查询最新的退款单抓取日志
     * @param platformType
     * @param shopId
     * @return
     */
    @Transactional(readOnly = true)
    public OriginalRefundFetch getOriginalRefundFetch(PlatformType platformType,Integer shopId){
        Search search = new Search(OriginalRefundFetch.class);
        search.addFilterEqual("platformType", platformType).addFilterEqual("shopId", shopId)
                .addSortDesc("fetchTime");
        List<OriginalRefundFetch> originalRefundFetchList = generalDAO.search(search);
        return CollectionUtils.isEmpty(originalRefundFetchList) ? null : originalRefundFetchList.get(0);
    }

    /**
     * 根据条件查询唯一的原始退款单
     * @param originalRefund
     * @return
     */
    @Transactional(readOnly = true)
    public OriginalRefund getOriginalRefund(OriginalRefund originalRefund){
        if (log.isInfoEnabled()) {
            log.info("getOriginalRefund方法参数为originalRefund[{}]",originalRefund.toString());
        }
        Search search = new Search(OriginalRefund.class);
        if(originalRefund != null){
            if(StringUtils.isNotBlank(originalRefund.getRefundId())){
                search.addFilterEqual("refundId",originalRefund.getRefundId());
            }
        }

        List<OriginalRefund> originalRefundList = generalDAO.search(search);

        return CollectionUtils.isEmpty(originalRefundList) ? null : originalRefundList.get(0);
    }

    /**
     * 根据条件查询唯一的原始退款单
     * @return
     */
    @Transactional(readOnly = true)
    public List<OriginalRefund> findOriginalRefund(OriginalRefund originalRefund){
        Search search = new Search(OriginalRefund.class);
        if(originalRefund != null){
            if(StringUtils.isNotBlank(originalRefund.getRefundId())){
                search.addFilterEqual("refundId",originalRefund.getRefundId());
            }

            if(originalRefund.getPlatformType() != null){
                search.addFilterEqual("platformType",originalRefund.getPlatformType());
            }

            if(originalRefund.getShopId() != null){
                search.addFilterEqual("shopId",originalRefund.getShopId());
            }

            if(StringUtils.isNotBlank(originalRefund.getSellerNick())){
                search.addFilterEqual("sellerNick",originalRefund.getSellerNick());
            }

        }
        List<OriginalRefund> originalRefundList = generalDAO.search(search);
        return originalRefundList;
    }

    /**
     * 根据条件查询唯一的原始退款单项
     * @param originalRefundItem
     * @return
     */
    @Transactional(readOnly = true)
    public OriginalRefundItem getOriginalRefundItem(OriginalRefundItem originalRefundItem){
        if (log.isInfoEnabled()) {
            log.info("getOriginalRefundItem方法参数为originalRefundItem[{}]",originalRefundItem.toString());
        }
        Search search = new Search(OriginalRefundItem.class);
        if(originalRefundItem != null){
            if(StringUtils.isNotBlank(originalRefundItem.getOuterId())){
                search.addFilterEqual("outerId",originalRefundItem.getOuterId());
            }
        }

        List<OriginalRefundItem> originalRefundItemList = generalDAO.search(search);

        return CollectionUtils.isEmpty(originalRefundItemList) ? null : originalRefundItemList.get(0);
    }

    /**
     * 保存退款单
     * @param originalRefund
     */
    public void saveOriginalRefund(OriginalRefund originalRefund){
        if (log.isInfoEnabled()) {
            log.info("saveOriginalRefund方法参数为originalRefund[{}]",originalRefund.toString());
        }
        generalDAO.saveOrUpdate(originalRefund);
    }

    /**
     * 保存退款单抓取日志
     * @param originalRefundFetch
     */
    public void saveOriginalRefundFetch(OriginalRefundFetch originalRefundFetch){
        if (log.isInfoEnabled()) {
            log.info("saveOriginalRefundFetch方法参数为originalRefundFetch[{}]",originalRefundFetch.toString());
        }
        generalDAO.saveOrUpdate(originalRefundFetch);
    }

    /**
     * 保存退款单标签信息
     * @param originalRefundTag
     */
    public void saveOriginalRefundTag(OriginalRefundTag originalRefundTag){
        if (log.isInfoEnabled()) {
            log.info("saveOriginalRefundTag方法参数为originalRefundTag[{}]",originalRefundTag.toString());
        }
        generalDAO.saveOrUpdate(originalRefundTag);
    }

    /**
     * 保存退款单商品信息
     * @param originalRefundItem
     */
    public void saveOriginalRefundItem(OriginalRefundItem originalRefundItem){
        if (log.isInfoEnabled()) {
            log.info("saveOriginalRefundItem方法参数为originalRefundItem[{}]",originalRefundItem.toString());
        }
        generalDAO.saveOrUpdate(originalRefundItem);
    }

    @Transactional(readOnly = true)
    public List<OriginalRefund> findUnprocessedOriginalRefunds() {
        Search search = new Search(OriginalRefund.class);
        return generalDAO.search(search.addFilterEqual("processed", false).addSortAsc("modified"));

    }

    /**
     * 保存原始退款单至数据库
     * @param originalRefundList
     */
    public void saveOriginalRefunds(List<OriginalRefund> originalRefundList) {
        if(CollectionUtils.isEmpty(originalRefundList)){
            return;
        }
        // 保存原始退款单
        for(OriginalRefund originalRefund : originalRefundList){
            saveOriginalRefund(originalRefund);

            for (OriginalRefundItem originalRefundItem : originalRefund.getOriginalRefundItemList()) {
                originalRefundItem.setOriginalRefundId(originalRefund.getId());
                saveOriginalRefundItem(originalRefundItem);
            }
            for (OriginalRefundTag originalRefundTag : originalRefund.getOriginalRefundTagList()) {
                originalRefundTag.setOriginalRefundId(originalRefund.getId());
                saveOriginalRefundTag(originalRefundTag);
            }
        }
    }
}
