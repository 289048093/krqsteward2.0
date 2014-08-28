package com.ejushang.steward.ordercenter.service.api.impl.jd;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.openapicenter.jd.api.JdRefundApi;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.ejushang.steward.openapicenter.jd.exception.JingDongApiException;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.FetchDataType;
import com.ejushang.steward.ordercenter.constant.JdRefundStatus;
import com.ejushang.steward.ordercenter.constant.OriginalRefundStatus;
import com.ejushang.steward.ordercenter.constant.RefundPhase;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.OriginalRefundService;
import com.ejushang.steward.ordercenter.service.api.IRefundApiService;
import com.jd.open.api.sdk.domain.service.RefundapplySaf.QueryMap;
import com.jd.open.api.sdk.domain.service.RefundapplySaf.RefundapplyResponse;
import com.jd.open.api.sdk.response.service.PopAfsRefundapplyQuerylistResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/5/10
 * Time: 11:36
 */
@Service
@Transactional
public class JingDongRefundApiService implements IRefundApiService {

    private static final Logger log = LoggerFactory.getLogger(JingDongRefundApiService.class);

    @Autowired
    private OriginalRefundService originalRefundService;

    @Autowired
    private OrderFetchService orderFetchService;

    @Override
    public List<OriginalRefund> fetchRefundByApi(ShopBean shopBean) throws Exception {

        if(log.isInfoEnabled()){
            log.info("京东平台抓取退款单：抓取参数{}",shopBean);
            log.info("京东平台抓取退款单：【{}】开始抓取退款单",shopBean.getSellerNick());
            log.info("京东平台抓取退款单：抓取开始时间为：{}", shopBean.getFetchRefundStartDate());
            log.info("京东平台抓取退款单：抓取结束时间为：{}", shopBean.getFetchRefundEndDate());
        }

        // 根据API获取京东退款单
        List<QueryMap> queryMapList = getQueryMapsByApi(shopBean);
        if(log.isInfoEnabled()){
            log.info("京东平台抓取退款单：通过API共抓取到{}条退款单",queryMapList.size());
        }

        // 将京东退款单转换为平台原始退款单
        List<OriginalRefund> originalRefundList = convertQueryMapList2OriginalRefundList(queryMapList,shopBean);

        if(log.isInfoEnabled()){
            log.info("京东平台抓取退款单：保存原始退款单至数据库：");
        }
        // 保存原始退款单
        saveOriginalRefund(originalRefundList);

        if(log.isInfoEnabled()){
            log.info("京东平台抓取退款单：保存抓单记录至数据库：");
        }
        // 保存抓取记录信息
        saveOrderFetchForRefund(shopBean);

        return originalRefundList;
    }

    /**
     * 根据API获取京东退款单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<QueryMap> getQueryMapsByApi(ShopBean shopBean) throws Exception {
        // 保存京东退款单的集合
        List<QueryMap> queryMapList = new ArrayList<QueryMap>();

        // 通过API抓取京东退款单 //根据创建时间
        List<QueryMap> queryMapList1 = searchRefundByCd(shopBean);
        if(log.isInfoEnabled()){
            log.info("京东平台抓取退款单：根据创建时间抓取到{}条退款单",queryMapList1.size());
        }

        // 通过API抓取京东退款单 //根据更新时间
        List<QueryMap> queryMapList2 = searchRefundByUd(shopBean);
        if(log.isInfoEnabled()){
            log.info("京东平台抓取退款单：根据更新时间抓取到{}条退款单",queryMapList2.size());
        }

        // 1.先添加根据更新时间抓取的退款单 防止旧数据
        addQueryMapDistinct(queryMapList, queryMapList2);
        // 2,添加根据创建时间抓取的退款单
        addQueryMapDistinct(queryMapList, queryMapList1);

        return queryMapList;
    }

    /**
     * 去重复添加京东退款单
     * @param queryMapList
     * @param queryMapList1
     */
    private void addQueryMapDistinct(List<QueryMap> queryMapList, List<QueryMap> queryMapList1) {
        if(CollectionUtils.isNotEmpty(queryMapList1)){
            for(QueryMap queryMap : queryMapList1){
                boolean isExist = false;
                for(QueryMap queryMapOri : queryMapList){
                    if(StringUtils.equalsIgnoreCase(queryMap.getId(), queryMapOri.getId())){
                        isExist = true;
                    }
                }
                if(!isExist){
                    queryMapList.add(queryMap);
                }
            }
        }
    }

    /**
     * 保存原始退款单
     * @param originalRefundList
     */
    private void saveOriginalRefund(List<OriginalRefund> originalRefundList) {
        // 保存原始退款单
        for(OriginalRefund originalRefund : originalRefundList){
            Integer oldId = originalRefund.getId();
            originalRefundService.saveOriginalRefund(originalRefund);
            if(NumberUtil.isNullOrZero(oldId)) {
                for (OriginalRefundItem originalRefundItem : originalRefund.getOriginalRefundItemList()) {
                    originalRefundItem.setOriginalRefundId(originalRefund.getId());
                    originalRefundService.saveOriginalRefundItem(originalRefundItem);
                }

                for (OriginalRefundTag originalRefundTag : originalRefund.getOriginalRefundTagList()) {
                    originalRefundTag.setOriginalRefundId(originalRefund.getId());
                    originalRefundService.saveOriginalRefundTag(originalRefundTag);
                }
            }
        }
    }

    /**
     * 保存抓取退款单记录
     * @param shopBean
     */
    private void saveOrderFetchForRefund(ShopBean shopBean) {
        OrderFetch orderFetchNew = getOrderFetchForRefund(shopBean);
        orderFetchService.save(orderFetchNew);
    }

    /**
     * 获取退款单抓取信息
     * @param shopBean
     * @return
     */
    private OrderFetch getOrderFetchForRefund(ShopBean shopBean) {
        OrderFetch orderFetchNew = new OrderFetch();
        orderFetchNew.setFetchStartTime(shopBean.getFetchRefundStartDate());
        // 设置抓取时间，为end
        orderFetchNew.setFetchTime(shopBean.getFetchRefundEndDate());
        orderFetchNew.setPlatformType(shopBean.getPlatformType());
        orderFetchNew.setShopId(shopBean.getShopId());
        orderFetchNew.setCreateTime(EJSDateUtils.getCurrentDate());
        orderFetchNew.setFetchDataType(FetchDataType.FETCH_REFUND);
        orderFetchNew.setFetchOptType(shopBean.getFetchOptType());
        return orderFetchNew;
    }

    @Override
    public List<OriginalRefund> fetchRefundByDeploy(ShopBean shopBean) throws Exception {
        return new ArrayList<OriginalRefund>();
    }

    @Override
    public List<OriginalRefund> fetchReturnByApi(ShopBean shopBean) throws Exception {
        return new ArrayList<OriginalRefund>();
    }

    @Override
    public List<OriginalRefund> fetchReturnByDeploy(ShopBean shopBean) throws Exception {
        return new ArrayList<OriginalRefund>();
    }

    /**
     * 批量将京东退款单转换为平台订单
     * @param queryMapList
     * @return
     */
    private List<OriginalRefund> convertQueryMapList2OriginalRefundList(List<QueryMap> queryMapList,ShopBean shopBean){
        List<OriginalRefund> originalRefundList = new ArrayList<OriginalRefund>();
        if(CollectionUtils.isNotEmpty(queryMapList)){
            OriginalRefund originalRefund = null;
            for(QueryMap queryMap : queryMapList){
                originalRefund = convertQueryMap2OriginalRefund(queryMap,shopBean);
                if(originalRefund != null){
                    originalRefundList.add(originalRefund);
                }
            }
        }

        return originalRefundList;
    }

    /**
     * 将京东退款单转换为平台订单
     * @param queryMap
     * @return
     */
    private OriginalRefund convertQueryMap2OriginalRefund(QueryMap queryMap,ShopBean shopBean){
        OriginalRefund originalRefund = null;
        if(queryMap == null){
            return originalRefund;
        }

        OriginalRefund originalRefundQuery = new OriginalRefund();
        originalRefundQuery.setRefundId(queryMap.getId());
        originalRefund = originalRefundService.getOriginalRefund(originalRefundQuery);

        // 判断抓取到的退款单是否为最新
        if(originalRefund != null &&
                EJSDateUtils.isNew(originalRefund.getModified(),
                        EJSDateUtils.parseDateForNull(queryMap.getCheckTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR))){
            return originalRefund;
        }

        if(originalRefund == null) {
            originalRefund = new OriginalRefund();
        }
        originalRefund.setRefundId(queryMap.getId());
        originalRefund.setBuyerId(queryMap.getBuyerId());
        originalRefund.setBuyerNick(queryMap.getBuyerName());
        originalRefund.setCheckTime(EJSDateUtils.parseDateForNull(queryMap.getCheckTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        originalRefund.setModified(EJSDateUtils.parseDateForNull(queryMap.getCheckTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        originalRefund.setCreated(EJSDateUtils.parseDateForNull(queryMap.getApplyTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        originalRefund.setRefundFee(Money.valueOfCent(StringUtils.isNotBlank(queryMap.getApplyRefundSum()) ? new BigDecimal(queryMap.getApplyRefundSum()).longValue() : 0L));
        originalRefund.setActualRefundFee(Money.valueOfCent(StringUtils.isNotBlank(queryMap.getApplyRefundSum()) ? new BigDecimal(queryMap.getApplyRefundSum()).longValue() : 0L));
        originalRefund.setStatus(getOriginalRefundStatus(queryMap.getStatus()));
        originalRefund.setCheckUsername(queryMap.getCheckUsername());
        originalRefund.setTid(queryMap.getOrderId());
        originalRefund.setPlatformType(shopBean.getPlatformType());
        originalRefund.setRefundPhase(RefundPhase.ON_SALE);
        originalRefund.setShopId(shopBean.getShopId());

        if(originalRefund.getModified() == null){
            originalRefund.setModified(originalRefund.getCreated());
        }

        return originalRefund;
    }

    /**
     * 将京东退款单状态转换为原始订单状态
     * @param jdRefundStatus
     * @return
     */
    private OriginalRefundStatus getOriginalRefundStatus(String jdRefundStatus){
        if(StringUtils.equalsIgnoreCase(jdRefundStatus, JdRefundStatus.FINANCE_APPROVAL_NO_PASS.getValue())
                || StringUtils.equalsIgnoreCase(jdRefundStatus, JdRefundStatus.MERCHANT_APPROVAL_NO_PASS.getValue())){
            return OriginalRefundStatus.SELLER_REFUSE;
        }
        else if(StringUtils.equalsIgnoreCase(jdRefundStatus, JdRefundStatus.FINANCE_APPROVAL_PASS.getValue())){
            return OriginalRefundStatus.SUCCESS;
        }
        else if(StringUtils.equalsIgnoreCase(jdRefundStatus, JdRefundStatus.PENDING_APPROVAL.getValue())
                || StringUtils.equalsIgnoreCase(jdRefundStatus, JdRefundStatus.MERCHANT_APPROVAL_PASS.getValue())
                || StringUtils.equalsIgnoreCase(jdRefundStatus, JdRefundStatus.MANUAL_APPROVAL_PASS.getValue())
                ){
            return OriginalRefundStatus.WAIT_SELLER_AGREE;
        }
        else{
            return null;
            //throw new StewardBusinessException("【" + jdRefundStatus + "】无法识别.");
        }
    }

    /**
     * 根据更新时间通过API抓取京东退款单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<QueryMap> searchRefundByUd(ShopBean shopBean) throws Exception {
        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantJingDong.STATUS,shopBean.getRefundStatus());
        argsMap.put(ConstantJingDong.CHECK_TIME_START, EJSDateUtils.formatDate(shopBean.getFetchRefundStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        argsMap.put(ConstantJingDong.CHECK_TIME_END,EJSDateUtils.formatDate(shopBean.getFetchRefundEndDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        argsMap.put(ConstantJingDong.PAGE_INDEX,1);
        argsMap.put(ConstantJingDong.PAGE_SIZE,ConstantJingDong.JD_FETCH_REFUND_PAGE_SIZE);

        // 通过api抓取京东售前退款
        JdRefundApi refundApi = new JdRefundApi(shopBean.getSessionKey());
        PopAfsRefundapplyQuerylistResponse response = refundApi.popAfsRefundapplyQuerylist(argsMap);

        if(!StringUtils.equalsIgnoreCase(response.getCode(),"0")){
            throw new JingDongApiException(response.getMsg());
        }

        RefundapplyResponse refundapplyResponse = response.getRefundApplyResponse();
        // 查询失败
        if(!refundapplyResponse.getResultState()){
            throw new JingDongApiException(refundapplyResponse.getResultInfo());
        }

        // 总条数
        Long count = refundapplyResponse.getCount();
        Integer pageSize = ConstantJingDong.JD_FETCH_REFUND_PAGE_SIZE;
        long pageNo = count % pageSize == 0 ? count/pageSize : count/pageSize + 1;

        List<QueryMap> queryMapList = new ArrayList<QueryMap>();
        for(int i = 1; i <= pageNo; i++){
            argsMap.put(ConstantJingDong.PAGE_INDEX,i);
            response = refundApi.popAfsRefundapplyQuerylist(argsMap);
            refundapplyResponse = response.getRefundApplyResponse();
            // 查询失败
            if(!refundapplyResponse.getResultState()){
                throw new JingDongApiException(refundapplyResponse.getResultInfo());
            }
            if(CollectionUtils.isNotEmpty(refundapplyResponse.getResults())){
                queryMapList.addAll(refundapplyResponse.getResults());
            }
        }
        return queryMapList;
    }

    /**
     * 根据创建时间通过API抓取京东退款单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<QueryMap> searchRefundByCd(ShopBean shopBean) throws Exception {
        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantJingDong.STATUS,shopBean.getRefundStatus());
        argsMap.put(ConstantJingDong.APPLY_TIME_START, EJSDateUtils.formatDate(shopBean.getFetchRefundStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        argsMap.put(ConstantJingDong.APPLY_TIME_END,EJSDateUtils.formatDate(shopBean.getFetchRefundEndDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        argsMap.put(ConstantJingDong.PAGE_INDEX,1);
        argsMap.put(ConstantJingDong.PAGE_SIZE,ConstantJingDong.JD_FETCH_REFUND_PAGE_SIZE);

        // 通过api抓取京东售前退款
        JdRefundApi refundApi = new JdRefundApi(shopBean.getSessionKey());
        PopAfsRefundapplyQuerylistResponse response = refundApi.popAfsRefundapplyQuerylist(argsMap);

        if(!StringUtils.equalsIgnoreCase(response.getCode(),"0")){
            throw new JingDongApiException(response.getMsg());
        }

        RefundapplyResponse refundapplyResponse = response.getRefundApplyResponse();
        // 查询失败
        if(!refundapplyResponse.getResultState()){
            throw new JingDongApiException(refundapplyResponse.getResultInfo());
        }

        // 总条数
        Long count = refundapplyResponse.getCount();
        Integer pageSize = ConstantJingDong.JD_FETCH_REFUND_PAGE_SIZE;
        long pageNo = count % pageSize == 0 ? count/pageSize : count/pageSize + 1;

        List<QueryMap> queryMapList = new ArrayList<QueryMap>();
        for(int i = 1; i <= pageNo; i++){
            argsMap.put(ConstantJingDong.PAGE_INDEX,i);
            response = refundApi.popAfsRefundapplyQuerylist(argsMap);
            refundapplyResponse = response.getRefundApplyResponse();
            // 查询失败
            if(!refundapplyResponse.getResultState()){
                throw new JingDongApiException(refundapplyResponse.getResultInfo());
            }
            if(CollectionUtils.isNotEmpty(refundapplyResponse.getResults())){
                queryMapList.addAll(refundapplyResponse.getResults());
            }
        }
        return queryMapList;
    }

}
