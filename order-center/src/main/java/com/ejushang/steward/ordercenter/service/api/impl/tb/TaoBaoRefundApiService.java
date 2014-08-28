package com.ejushang.steward.ordercenter.service.api.impl.tb;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.openapicenter.tb.api.TbRefundApi;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.exception.TaoBaoApiException;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.domain.taobao.*;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.OriginalRefundService;
import com.ejushang.steward.ordercenter.service.api.IRefundApiService;
import com.ejushang.steward.ordercenter.service.taobao.JdpTbTradeService;
import com.taobao.api.domain.RefundBill;
import com.taobao.api.domain.RefundItem;
import com.taobao.api.domain.ReturnBill;
import com.taobao.api.domain.Tag;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.response.TmallEaiOrderRefundGetResponse;
import com.taobao.api.response.TmallEaiOrderRefundGoodReturnGetResponse;
import com.taobao.api.response.TmallEaiOrderRefundGoodReturnMgetResponse;
import com.taobao.api.response.TmallEaiOrderRefundMgetResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * User: Baron.Zhang
 * Date: 2014/5/10
 * Time: 11:37
 */
@Service
@Transactional
public class TaoBaoRefundApiService implements IRefundApiService {

    private static final Logger log = LoggerFactory.getLogger(TaoBaoRefundApiService.class);

    @Autowired
    private OriginalRefundService originalRefundService;

    @Autowired
    private JdpTbTradeService jdpTbTradeService;

    @Autowired
    private OrderFetchService orderFetchService;

    @Override
    public List<OriginalRefund> fetchRefundByApi(ShopBean shopBean) throws Exception {

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取退款单：抓取参数{}",shopBean);
            log.info("淘宝API抓取退款单：【{}】开始抓单：：：",shopBean.getSellerNick());
            log.info("淘宝API抓取退款单：抓取开始时间为：{}",shopBean.getFetchRefundStartDate());
            log.info("淘宝API抓取退款单：抓取结束时间为：{}",shopBean.getFetchRefundEndDate());
        }

        // 使用淘宝API抓取天猫退款单
        List<RefundBill> refundBillList = searchRefund(shopBean);
        if(log.isInfoEnabled()){
            log.info("淘宝API退款单抓取：抓取原始淘宝退款单共：{}条",refundBillList.size());
        }

        // 将天猫退款单转换成平台原始退款单
        List<OriginalRefund> originalRefundList = convertRefundBillList2OriginalRefundList(refundBillList,shopBean);
        if(log.isInfoEnabled()){
            log.info("淘宝API退款单抓取：转换后的原始退款单共：{}条",originalRefundList.size());
        }

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取退款单：保存原始退款单：");
        }
        // 保存原始退款单
        originalRefundService.saveOriginalRefunds(originalRefundList);

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取退款单：保存抓取记录：");
        }
        // 更新抓取时间(只要过程没有异常，就要更新抓取时间，否则会有重复抓取
        // 所有操作成功完成，添加退款单抓取记录
        saveOrderFetchForRefund(shopBean);

        return originalRefundList;
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
     * 保存抓取退货单记录
     * @param shopBean
     */
    private void saveOrderFetchForReturn(ShopBean shopBean) {
        OrderFetch orderFetchNew = getOrderFetchForReturn(shopBean);
        orderFetchService.save(orderFetchNew);
    }

    @Override
    public List<OriginalRefund> fetchRefundByDeploy(ShopBean shopBean) throws Exception {
        if(log.isInfoEnabled()){
            log.info("聚石塔抓取退款单：抓取参数{}",shopBean);
            log.info("聚石塔抓取退款单：【{}】开始抓单：：：",shopBean.getSellerNick());
            log.info("聚石塔抓取退款单：抓取开始时间为：{}",shopBean.getFetchRefundStartDate());
            log.info("聚石塔抓取退款单：抓取结束时间为：{}",shopBean.getFetchRefundEndDate());
        }

        // 使用淘宝API抓取天猫退款单
        List<RefundBill> refundBillList = searchRefundByJst(shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔抓取退款单：抓取原始淘宝退款记录共：{}条",refundBillList.size());
        }

        // 将天猫退款单转换成平台原始退款单v
        List<OriginalRefund> originalRefundList = convertRefundBillList2OriginalRefundList(refundBillList,shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔抓取退款单：转换后的原始退款单共：{}条",originalRefundList.size());
        }

        if(log.isInfoEnabled()){
            log.info("聚石塔抓取退款单：保存原始退款信息：");
        }
        // 保存原始退款单
        originalRefundService.saveOriginalRefunds(originalRefundList);

        if(log.isInfoEnabled()){
            log.info("聚石塔抓取退款单：保存抓单记录：");
        }
        // 所有操作成功完成，添加退款单抓取记录
        saveOrderFetchForRefund(shopBean);

        return originalRefundList;
    }

    @Override
    public List<OriginalRefund> fetchReturnByApi(ShopBean shopBean) throws Exception {

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取退货单：抓取参数{}",shopBean);
            log.info("淘宝API抓取退货单：【{}】开始抓单：：：",shopBean.getSellerNick());
            log.info("淘宝API抓取退货单：抓取开始时间为：{}",shopBean.getFetchReturnStartDate());
            log.info("淘宝API抓取退货单：抓取结束时间为：{}",shopBean.getFetchReturnEndDate());
        }

        // 使用淘宝api抓取天猫退货单
        List<ReturnBill> returnBillList = searchReturn(shopBean);
        if(log.isInfoEnabled()){
            log.info("淘宝API抓取退款单：抓取淘宝退货单共：{}条",returnBillList.size());
        }

        // 将淘宝退款单转换为平台原始订单
        List<OriginalRefund> originalRefundList = convertReturnBillList2OriginalRefundList(returnBillList, shopBean);
        if(log.isInfoEnabled()){
            log.info("淘宝API抓取退款单：转换后的原始退货单共：{}条",originalRefundList.size());
        }

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取退款单：保存原始退货单：");
        }
        // 保存原始退货信息
        originalRefundService.saveOriginalRefunds(originalRefundList);

        if(log.isInfoEnabled()){
            log.info("淘宝API抓取退款单：保存退货单抓取记录：");
        }
        // 所有操作成功完成，添加退款单抓取记录
        saveOrderFetchForReturn(shopBean);

        return originalRefundList;
    }


    /**
     * 批量将淘宝退货单转换为平台原始退款单
     * @param returnBillList
     * @param shopBean
     * @return
     */
    private List<OriginalRefund> convertReturnBillList2OriginalRefundList(List<ReturnBill> returnBillList,ShopBean shopBean){
        List<OriginalRefund> originalRefundList = new ArrayList<OriginalRefund>();
        if(CollectionUtils.isEmpty(returnBillList)){
            return originalRefundList;
        }

        OriginalRefund originalRefund = null;
        for(ReturnBill returnBill : returnBillList){
            originalRefund = convertReturnBill2OriginalRefund(returnBill,shopBean);
            if(originalRefund != null){
                originalRefundList.add(originalRefund);
            }
        }

        return originalRefundList;
    }

    /**
     * 将淘宝退货单转换为平台原始退款单
     * @param returnBill
     * @return
     */
    private OriginalRefund convertReturnBill2OriginalRefund(ReturnBill returnBill,ShopBean shopBean){
        OriginalRefund originalRefund = null;
        if(returnBill == null){
            return originalRefund;
        }

        OriginalRefund originalRefundQuery = new OriginalRefund();
        originalRefundQuery.setRefundId(String.valueOf(returnBill.getRefundId()));
        originalRefund = originalRefundService.getOriginalRefund(originalRefundQuery);

        // 判断抓取下来的退货单是否为最新
        if(originalRefund != null &&
                EJSDateUtils.isNew(originalRefund.getModified(),
                        EJSDateUtils.parseDate(returnBill.getModified(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR))){
            return originalRefund;
        }

        if(originalRefund == null) {
            originalRefund = new OriginalRefund();
            originalRefund.setRefundId(String.valueOf(returnBill.getRefundId()));
            originalRefund.setRefundVersion(String.valueOf(returnBill.getRefundVersion()));
            originalRefund.setReason(returnBill.getReason());
            originalRefund.setCreated(EJSDateUtils.parseDateForNull(returnBill.getCreated(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            originalRefund.setTid(String.valueOf(returnBill.getTid()));
            originalRefund.setOid(String.valueOf(returnBill.getOid()));
            originalRefund.setStatus(getOriginalRefundStatusFromReturnStatus(returnBill.getStatus()));
            originalRefund.setRefundPhase(getRefundPhase(returnBill.getRefundPhase()));
            originalRefund.setModified(EJSDateUtils.parseDateForNull(returnBill.getModified(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            originalRefund.setBillType(returnBill.getBillType());
            originalRefund.setCompanyName(DeliveryType.getDeliveryTypeByShortName(returnBill.getCompanyName()));
            originalRefund.setSid(returnBill.getSid());
            originalRefund.setOperationLog(returnBill.getOperationLog());
            originalRefund.setDescription(returnBill.getDesc());

            originalRefund.setPlatformType(shopBean.getPlatformType());
            originalRefund.setShopId(shopBean.getShopId());
            // 是否已处理
            originalRefund.setProcessed(false);

            List<RefundItem> refundItemList = returnBill.getItemList();
            List<OriginalRefundItem> originalRefundItemList = convertRefundItemList2OriginalRefundItemList(refundItemList);
            originalRefund.setOriginalRefundItemList(originalRefundItemList);

            List<Tag> tagList = returnBill.getTagList();
            List<OriginalRefundTag> originalRefundTagList = convertTagList2OriginalRefundTagList(tagList);
            originalRefund.setOriginalRefundTagList(originalRefundTagList);
        }
        else{
            originalRefund.setCompanyName(DeliveryType.getDeliveryTypeByShortName(returnBill.getCompanyName()));
            originalRefund.setSid(returnBill.getSid());
            originalRefund.setOperationLog(returnBill.getOperationLog());
            originalRefund.setDescription(returnBill.getDesc());
            // 是否已处理
            originalRefund.setProcessed(false);
        }

        return originalRefund;
    }

    /**
     * 将淘宝退款单状态转换为原始订单状态
     * @param returnStatus
     * @return
     */
    private OriginalRefundStatus getOriginalRefundStatusFromReturnStatus(String returnStatus){
        if(StringUtils.equalsIgnoreCase(returnStatus, TbReturnStatus.CONFIRM_SUCCESS.getName())){
            return OriginalRefundStatus.SUCCESS;
        }
        else if(StringUtils.equalsIgnoreCase(returnStatus, TbReturnStatus.WAIT_SELLER_CONFIRM_GOODS.getName())){
            return OriginalRefundStatus.GOODS_RETURNING;
        }
        else if(StringUtils.equalsIgnoreCase(returnStatus, TbReturnStatus.CONFIRM_FAILED.getName())){
            return OriginalRefundStatus.CLOSED;
        }
        else{
            return null;
            //throw new StewardBusinessException("【" + returnStatus + "】无法识别，转化为智库城退款状态");
        }
    }

    @Override
    public List<OriginalRefund> fetchReturnByDeploy(ShopBean shopBean) throws Exception {

        if(log.isInfoEnabled()){
            log.info("聚石塔抓取退货单：抓取参数{}",shopBean);
            log.info("聚石塔抓取退货单：【{}】开始抓单：：：",shopBean.getSellerNick());
            log.info("聚石塔抓取退货单：抓取开始时间为：{}",shopBean.getFetchRefundStartDate());
            log.info("聚石塔抓取退货单：抓取结束时间为：{}",shopBean.getFetchRefundEndDate());
        }

        // 使用淘宝api抓取天猫退货单
        List<ReturnBill> returnBillList = searchReturnByJst(shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔抓取退货单：抓取淘宝退货单共：{}条",returnBillList.size());
        }

        // 将淘宝退款单转换为平台原始订单
        List<OriginalRefund> originalRefundList = convertReturnBillList2OriginalRefundList(returnBillList, shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔抓取退货单：转换后的原始退货单共：{}条",originalRefundList.size());
        }

        if(log.isInfoEnabled()){
            log.info("聚石塔抓取退货单：保存原始退货单：");
        }
        // 保存原始退款单
        originalRefundService.saveOriginalRefunds(originalRefundList);

        if(log.isInfoEnabled()){
            log.info("聚石塔抓取退货单：保存退款单抓取记录：");
        }
        // 更新抓取时间(只要过程没有异常，就要更新抓取时间，否则会有重复抓取
        // 所有操作成功完成，添加退货单抓取记录
        saveOrderFetchForReturn(shopBean);

        return originalRefundList;
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

    /**
     * 获取退货单抓取信息
     * @param shopBean
     * @return
     */
    private OrderFetch getOrderFetchForReturn(ShopBean shopBean) {
        OrderFetch orderFetchNew = new OrderFetch();
        orderFetchNew.setFetchStartTime(shopBean.getFetchReturnStartDate());
        // 设置抓取时间，为end
        orderFetchNew.setFetchTime(shopBean.getFetchRefundEndDate());
        orderFetchNew.setPlatformType(shopBean.getPlatformType());
        orderFetchNew.setShopId(shopBean.getShopId());
        orderFetchNew.setCreateTime(EJSDateUtils.getCurrentDate());
        orderFetchNew.setFetchDataType(FetchDataType.FETCH_RETURN);
        orderFetchNew.setFetchOptType(shopBean.getFetchOptType());
        return orderFetchNew;
    }

    /**
     * 批量将天猫退款单转换为平台原始退款单
     * @param refundBillList
     * @return
     */
    public List<OriginalRefund> convertRefundBillList2OriginalRefundList(List<RefundBill> refundBillList,ShopBean shopBean){
        List<OriginalRefund> originalRefundList = new ArrayList<OriginalRefund>();
        if(CollectionUtils.isEmpty(refundBillList)){
            return originalRefundList;
        }

        OriginalRefund originalRefund = null;
        for(RefundBill refundBill : refundBillList){
            originalRefund = convertRefundBill2OriginalRefund(refundBill,shopBean);
            if(originalRefund != null){
                originalRefundList.add(originalRefund);
            }
        }

        return originalRefundList;
    }

    /**
     * 将天猫退款单转换为平台原始退款单
     * @param refundBill
     * @return
     */
    private OriginalRefund convertRefundBill2OriginalRefund(RefundBill refundBill,ShopBean shopBean){
        OriginalRefund originalRefund = null;
        if(refundBill == null){
            return originalRefund;
        }

        OriginalRefund originalRefundQuery = new OriginalRefund();
        originalRefundQuery.setRefundId(String.valueOf(refundBill.getRefundId()));
        originalRefund = originalRefundService.getOriginalRefund(originalRefundQuery);

        // 判断抓取的退款单是否是最新的
        if(originalRefund != null
                && EJSDateUtils.isNew(originalRefund.getModified(),
                    EJSDateUtils.parseDateForNull(refundBill.getModified(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR))
                ){
            return originalRefund;
        }

        if(originalRefund == null) {
            originalRefund = new OriginalRefund();
        }

        originalRefund.setRefundId(String.valueOf(refundBill.getRefundId()));
        originalRefund.setRefundType(refundBill.getRefundType());
        originalRefund.setTradeStatus(StringUtils.isNotBlank(refundBill.getTradeStatus()) ? refundBill.getTradeStatus().toUpperCase():refundBill.getTradeStatus());
        originalRefund.setRefundFee(Money.valueOfCent(refundBill.getRefundFee()));
        originalRefund.setReason(refundBill.getReason());
        originalRefund.setActualRefundFee(Money.valueOfCent(refundBill.getActualRefundFee()));
        originalRefund.setCreated(EJSDateUtils.parseDateForNull(refundBill.getCreated(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        originalRefund.setCurrentPhaseTimeout(EJSDateUtils.parseDateForNull(refundBill.getCurrentPhaseTimeout(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        originalRefund.setAlipayNo(refundBill.getAlipayNo());
        originalRefund.setBuyerNick(refundBill.getBuyerNick());
        originalRefund.setSellerNick(refundBill.getSellerNick());
        originalRefund.setTid(String.valueOf(refundBill.getTid()));
        originalRefund.setOid(String.valueOf(refundBill.getOid()));
        originalRefund.setCsStatus(refundBill.getCsStatus());
        originalRefund.setStatus(getOriginalRefundStatus(refundBill.getStatus()));
        originalRefund.setRefundPhase(getRefundPhase(refundBill.getRefundPhase()));
        originalRefund.setModified(EJSDateUtils.parseDateForNull(refundBill.getModified(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        originalRefund.setBillType(refundBill.getBillType());
        originalRefund.setRefundVersion(String.valueOf(refundBill.getRefundVersion()));
        originalRefund.setOperationConstraint(refundBill.getOperationConstraint());

        originalRefund.setPlatformType(shopBean.getPlatformType());
        originalRefund.setShopId(shopBean.getShopId());
        // 是否已处理
        originalRefund.setProcessed(false);

        List<RefundItem> refundItemList = refundBill.getItemList();
        List<OriginalRefundItem> originalRefundItemList = convertRefundItemList2OriginalRefundItemList(refundItemList);
        originalRefund.setOriginalRefundItemList(originalRefundItemList);

        List<Tag> tagList = refundBill.getTagList();
        List<OriginalRefundTag> originalRefundTagList = convertTagList2OriginalRefundTagList(tagList);
        originalRefund.setOriginalRefundTagList(originalRefundTagList);

        return originalRefund;
    }

    /**
     * 将淘宝平台退款类型转换为原始平台退款类型
     * @param tbRefundPhase
     * @return
     */
    private RefundPhase getRefundPhase(String tbRefundPhase){
        if(StringUtils.equalsIgnoreCase(TbRefundPhase.ONSALE.toString(),tbRefundPhase)){
            return RefundPhase.ON_SALE;
        }
        else if(StringUtils.equalsIgnoreCase(TbRefundPhase.AFTERSALE.toString(),tbRefundPhase)){
            return RefundPhase.AFTER_SALE;
        }
        else{
            return null;
            //throw new StewardBusinessException("【" +tbRefundPhase + "】无法识别");
        }
    }

    /**
     * 将淘宝平台的退款单状态转为原始退款单的状态
     * @param tbRefundStatus
     * @return
     */
    private OriginalRefundStatus getOriginalRefundStatus(String tbRefundStatus){
        if(StringUtils.equalsIgnoreCase(tbRefundStatus, TbRefundStatus.WAIT_SELLER_AGREE.toString())){
            return OriginalRefundStatus.WAIT_SELLER_AGREE;
        }
        else if(StringUtils.equalsIgnoreCase(tbRefundStatus, TbRefundStatus.SELLER_REFUSE.toString())){
            return OriginalRefundStatus.SELLER_REFUSE;
        }
        else if(StringUtils.equalsIgnoreCase(tbRefundStatus, TbRefundStatus.GOODS_RETURNING.toString())){
            return OriginalRefundStatus.GOODS_RETURNING;
        }
        else if(StringUtils.equalsIgnoreCase(tbRefundStatus, TbRefundStatus.CLOSED.toString())){
            return OriginalRefundStatus.CLOSED;
        }
        else if(StringUtils.equalsIgnoreCase(tbRefundStatus, TbRefundStatus.SUCCESS.toString())){
            return OriginalRefundStatus.SUCCESS;
        }
        else {
            throw  new StewardBusinessException("【" + tbRefundStatus + "】无法转换成智库城状态");
        }
    }

    /**
     * 批量将淘宝退款标签信息转换为智库城原始标签信息
     * @param tagList
     * @return
     */
    private List<OriginalRefundTag> convertTagList2OriginalRefundTagList(List<Tag> tagList){
        List<OriginalRefundTag> originalRefundTagList = new ArrayList<OriginalRefundTag>();
        if(CollectionUtils.isEmpty(tagList)){
            return originalRefundTagList;
        }

        OriginalRefundTag originalRefundTag = null;
        for(Tag tag : tagList){
            originalRefundTag = convertTag2OriginalRefundTag(tag);
            if(originalRefundTag != null){
                originalRefundTagList.add(originalRefundTag);
            }
        }

        return originalRefundTagList;
    }

    /**
     * 将淘宝退款标签信息转换为智库城原始标签信息
     * @param tag
     * @return
     */
    private OriginalRefundTag convertTag2OriginalRefundTag(Tag tag){
        OriginalRefundTag originalRefundTag = null;
        if(tag == null){
            return originalRefundTag;
        }

        originalRefundTag = new OriginalRefundTag();
        originalRefundTag.setTagKey(tag.getTagKey());
        originalRefundTag.setTagName(tag.getTagName());
        originalRefundTag.setTagType(tag.getTagType());

        return originalRefundTag;
    }

    /**
     * 批量将淘宝退款商品详情转换为智库城退款单商品详情
     * @param refundItemList
     * @return
     */
    private List<OriginalRefundItem>  convertRefundItemList2OriginalRefundItemList(List<RefundItem> refundItemList){
        List<OriginalRefundItem> originalRefundItemList = new ArrayList<OriginalRefundItem>();

        if(CollectionUtils.isEmpty(refundItemList)){
            return originalRefundItemList;
        }

        OriginalRefundItem originalRefundItem = null;
        for(RefundItem refundItem : refundItemList){
            originalRefundItem = convertRefundItem2OriginalRefundItem(refundItem);
            if(originalRefundItem != null){
                originalRefundItemList.add(originalRefundItem);
            }
        }

        return originalRefundItemList;
    }

    /**
     * 将淘宝退款商品详情转换为智库城退款单商品详情
     * @param refundItem
     * @return
     */
    private OriginalRefundItem convertRefundItem2OriginalRefundItem(RefundItem refundItem){
        OriginalRefundItem originalRefundItem = null;
        if(refundItem == null){
            return originalRefundItem;
        }

        originalRefundItem = new OriginalRefundItem();
        originalRefundItem.setNumIid(String.valueOf(refundItem.getNumIid()));
        originalRefundItem.setOuterId(refundItem.getOuterId());
        originalRefundItem.setNum(refundItem.getNum());
        originalRefundItem.setPrice(Money.valueOfCent(refundItem.getPrice()));
        originalRefundItem.setSku(refundItem.getSku());

        return originalRefundItem;
    }

    /**
     * 使用淘宝API抓取退款单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<RefundBill> searchRefund(ShopBean shopBean) throws Exception {

        // 构造保存淘宝退款单的集合
        List<RefundBill> refundBillList = new ArrayList<RefundBill>();

        TbRefundApi refundApi = new TbRefundApi(shopBean.getSessionKey());
        if(log.isInfoEnabled()){
            log.info("淘宝API退款单抓取：初始化TbRefundApi");
        }
        // 构造参数
        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantTaoBao.START_TIME, shopBean.getFetchRefundStartDate());
        argsMap.put(ConstantTaoBao.END_TIME, shopBean.getFetchRefundEndDate());
        argsMap.put(ConstantTaoBao.PAGE_NO,1L);
        argsMap.put(ConstantTaoBao.PAGE_SIZE,ConstantTaoBao.TB_FETCH_REFUND_PAGE_SIZE);
        argsMap.put(ConstantTaoBao.STATUS,shopBean.getRefundStatus());

        if(log.isInfoEnabled()){
            log.info("淘宝API退款单抓取：初始化TbRefundApi调用参数argsMap：" + argsMap );
        }

        // 执行查询
        TmallEaiOrderRefundMgetResponse response = refundApi.tmallEaiOrderRefundMget(argsMap);

        if(StringUtils.isNotBlank(response.getErrorCode())){
            throw new TaoBaoApiException("ErrorCode："+response.getErrorCode()+"，body：" + response.getBody());
        }
        // 符合条件的数据总条数
        Long totalCount = response.getTotalResults();
        Long pageSize = ConstantTaoBao.TB_FETCH_REFUND_PAGE_SIZE;
        Long pageNo = totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;


        for(long i = 1; i <= pageNo; i++){
            argsMap.put(ConstantTaoBao.PAGE_NO,i);
            response = refundApi.tmallEaiOrderRefundMget(argsMap);
            if(StringUtils.isNotBlank(response.getErrorCode())){
                throw new TaoBaoApiException("ErrorCode："+response.getErrorCode()+"，body：" + response.getBody());
            }
            if(CollectionUtils.isNotEmpty(response.getRefundBillList())) {
                refundBillList.addAll(response.getRefundBillList());
            }
        }

        Collections.sort(refundBillList, new Comparator<RefundBill>() {
            @Override
            public int compare(RefundBill o1, RefundBill o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });
        return refundBillList;
    }

    /**
     * 使用淘宝API抓取退款单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<ReturnBill> searchReturn(ShopBean shopBean) throws Exception {

        if(log.isInfoEnabled()){
            log.info("淘宝API退货单抓取：抓取开始，参数shopBean = " + shopBean );
        }

        // 构造保存淘宝退款单的集合
        List<ReturnBill> returnBillList = new ArrayList<ReturnBill>();

        TbRefundApi refundApi = new TbRefundApi(shopBean.getSessionKey());
        if(log.isInfoEnabled()){
            log.info("淘宝API退货单抓取：初始化TbRefundApi" + shopBean );
        }
        // 构造参数
        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantTaoBao.START_TIME, shopBean.getFetchRefundStartDate());
        argsMap.put(ConstantTaoBao.END_TIME, shopBean.getFetchRefundEndDate());
        argsMap.put(ConstantTaoBao.PAGE_NO,1L);
        argsMap.put(ConstantTaoBao.PAGE_SIZE,ConstantTaoBao.TB_FETCH_REFUND_PAGE_SIZE);
        argsMap.put(ConstantTaoBao.STATUS,shopBean.getRefundStatus());

        if(log.isInfoEnabled()){
            log.info("淘宝API退货单抓取：初始化TbRefundApi调用参数argsMap：" + argsMap );
        }

        // 执行查询
        TmallEaiOrderRefundGoodReturnMgetResponse response = refundApi.tmallEaiOrderRefundGoodReturnMget(argsMap);

        if(StringUtils.isNotBlank(response.getErrorCode())){
            throw new TaoBaoApiException("ErrorCode："+response.getErrorCode()+"，body：" + response.getBody());
        }
        // 符合条件的数据总条数
        Long totalCount = response.getTotalResults();
        Long pageSize = ConstantTaoBao.TB_FETCH_REFUND_PAGE_SIZE;
        Long pageNo = totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;


        for(long i = 1; i <= pageNo; i++){
            argsMap.put(ConstantTaoBao.PAGE_NO,i);
            response = refundApi.tmallEaiOrderRefundGoodReturnMget(argsMap);
            if(StringUtils.isNotBlank(response.getErrorCode())){
                throw new TaoBaoApiException("ErrorCode："+response.getErrorCode()+"，body：" + response.getBody());
            }
            if(CollectionUtils.isNotEmpty(response.getReturnBillList())) {
                returnBillList.addAll(response.getReturnBillList());
            }
        }

        Collections.sort(returnBillList, new Comparator<ReturnBill>() {
            @Override
            public int compare(ReturnBill o1, ReturnBill o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });

        return returnBillList;
    }

    /**
     * 使用淘宝API抓取退款单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<ReturnBill> searchReturnByJst(ShopBean shopBean) throws Exception {

        if(log.isInfoEnabled()){
            log.info("聚石塔退货单抓取：抓取开始，参数shopBean = {}", shopBean );
        }

        // 保存退货单信息
        List<JdpTmReturn> jdpTmReturnList = new ArrayList<JdpTmReturn>();

        // 根据创建时间抓取聚石塔退货单
        List<JdpTmReturn> jdpTmReturnList1 = getJdpTmReturnsByCd(shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔退货单抓取：根据创建时间抓取{}条",jdpTmReturnList1.size());
        }
        // 根据更新时间抓取聚石塔退货单
        List<JdpTmReturn> jdpTmReturnList2 = getJdpTmReturnsByUd(shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔退货单抓取：根据更新时间抓取{}条",jdpTmReturnList2.size());
        }

        // 1.先添加根据更新时间抓取的退货单 防止旧数据
        addJdpTmReturnDistinct(jdpTmReturnList, jdpTmReturnList2);
        // 2.添加根据创建时间抓取的退货单
        addJdpTmReturnDistinct(jdpTmReturnList, jdpTmReturnList1);


        // 将聚石塔退货单转为淘宝退货单
        List<ReturnBill> returnBillList = convertJdpTmReturnList2ReturnBillList(jdpTmReturnList);

        Collections.sort(returnBillList, new Comparator<ReturnBill>() {
            @Override
            public int compare(ReturnBill o1, ReturnBill o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });

        return returnBillList;
    }

    /**
     * 批量将聚石塔退款单转换为淘宝退货单信息
     * @param jdpTmReturnList
     * @return
     * @throws com.taobao.api.ApiException
     * @throws TaoBaoApiException
     */
    private List<ReturnBill> convertJdpTmReturnList2ReturnBillList(List<JdpTmReturn> jdpTmReturnList) throws com.taobao.api.ApiException, TaoBaoApiException {
        // 构造保存淘宝退款单的集合
        List<ReturnBill> returnBillList = new ArrayList<ReturnBill>();

        // 将json退款信息，转换为天猫退款单信息
        if(CollectionUtils.isNotEmpty(jdpTmReturnList)){
            for(JdpTmReturn jdpTmReturn : jdpTmReturnList){
                ReturnBill returnBill = convertJdpTmReturn2ReturnBill(jdpTmReturn);
                if(returnBill != null){
                    returnBillList.add(returnBill);
                }
            }
        }
        return returnBillList;
    }


    /**
     * 将聚石塔退款单转换为淘宝退货单信息
     * @param jdpTmReturn
     * @return
     * @throws com.taobao.api.ApiException
     * @throws TaoBaoApiException
     */
    private ReturnBill convertJdpTmReturn2ReturnBill(JdpTmReturn jdpTmReturn) throws com.taobao.api.ApiException, TaoBaoApiException {
        TmallEaiOrderRefundGoodReturnGetResponse response = TaobaoUtils.parseResponse(jdpTmReturn.getJdpResponse(), TmallEaiOrderRefundGoodReturnGetResponse.class);
        if(StringUtils.isNotBlank(response.getErrorCode())){
            throw new TaoBaoApiException(response.getBody());
        }
        return response.getReturnBill();
    }

    /**
     * 去重复添加聚石塔退款单
     * @param jdpTmReturnList
     * @param jdpTmReturnList1
     */
    private void addJdpTmReturnDistinct(List<JdpTmReturn> jdpTmReturnList, List<JdpTmReturn> jdpTmReturnList1) {
        for(JdpTmReturn jdpTmReturn : jdpTmReturnList1){
            boolean isExist = false;
            for(JdpTmReturn jdpTmReturnOri : jdpTmReturnList){
                if(jdpTmReturn.getRefundId().longValue() == jdpTmReturnOri.getRefundId().longValue()){
                    isExist = true;
                }
            }
            if(!isExist){
                jdpTmReturnList.add(jdpTmReturn);
            }
        }
    }

    /**
     * 根据更新时间抓取聚石塔退货单
     * @param shopBean
     * @return
     */
    private List<JdpTmReturn> getJdpTmReturnsByUd(ShopBean shopBean) {
        // 从聚石塔获取交易信息
        JdpTmReturnQuery jdpTmReturnQuery2 = new JdpTmReturnQuery();
        jdpTmReturnQuery2.setStartJdpModified(shopBean.getFetchRefundStartDate());
        jdpTmReturnQuery2.setEndJdpModified(shopBean.getFetchRefundEndDate());
        jdpTmReturnQuery2.setStatus(shopBean.getRefundStatus());

        // 获得聚石塔订单信息
        return jdpTbTradeService.findJdpTmReturnByJdpTmReturnQuery(jdpTmReturnQuery2);
    }

    /**
     * 根据创建时间抓取聚石塔退货单
     * @param shopBean
     * @return
     */
    private List<JdpTmReturn> getJdpTmReturnsByCd(ShopBean shopBean) {
        // 从聚石塔获取交易信息
        JdpTmReturnQuery jdpTmReturnQuery1 = new JdpTmReturnQuery();
        jdpTmReturnQuery1.setStartJdpCreated(shopBean.getFetchRefundStartDate());
        jdpTmReturnQuery1.setEndJdpCreated(shopBean.getFetchRefundEndDate());
        jdpTmReturnQuery1.setStatus(shopBean.getRefundStatus());

        // 获得聚石塔订单信息
        return jdpTbTradeService.findJdpTmReturnByJdpTmReturnQuery(jdpTmReturnQuery1);
    }


    /**
     * 查询淘宝聚石塔推送退款单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<RefundBill> searchRefundByJst(ShopBean shopBean) throws Exception {
        // 保存退款单的集合
        List<JdpTmRefund> jdpTmRefundList = new ArrayList<JdpTmRefund>();

        // 根据创建时间抓取聚石塔退款单
        List<JdpTmRefund> jdpTmRefundList1 = getJdpTmRefundsByCd(shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔退款单抓取：根据创建时间抓取{}条",jdpTmRefundList1.size());
        }
        // 根据更新时间抓取聚石塔退款单
        List<JdpTmRefund> jdpTmRefundList2 = getJdpTmRefundsByUd(shopBean);
        if(log.isInfoEnabled()){
            log.info("聚石塔退款单抓取：根据更新时间抓取{}条",jdpTmRefundList2.size());
        }

        // 1.先添加根据更新时间抓取的退款单 防止旧数据
        addJdpTmRefundDistinct(jdpTmRefundList, jdpTmRefundList2);
        // 2.去重复添加根据创建时间抓取的退款单
        addJdpTmRefundDistinct(jdpTmRefundList, jdpTmRefundList1);

        if(log.isInfoEnabled()){
            log.info("聚石塔退款单抓取：：：：抓取到原始淘宝退款信息：" + jdpTmRefundList.size() + "条");
        }

        // 将聚石塔退款单转换为淘宝退款单
        List<RefundBill> refundBillList = convertJdpTmRefundList2RefundBillList(jdpTmRefundList);

        Collections.sort(refundBillList, new Comparator<RefundBill>() {
            @Override
            public int compare(RefundBill o1, RefundBill o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });

        return refundBillList;
    }

    /**
     * 批量将聚石塔退款单转换为淘宝退款单
     * @param jdpTmRefundList
     * @return
     * @throws com.taobao.api.ApiException
     * @throws TaoBaoApiException
     */
    private List<RefundBill> convertJdpTmRefundList2RefundBillList(List<JdpTmRefund> jdpTmRefundList) throws com.taobao.api.ApiException, TaoBaoApiException {
        // 构造保存淘宝退款单的集合
        List<RefundBill> refundBillList = new ArrayList<RefundBill>();

        // 将json退款信息，转换为天猫退款单信息
        if(CollectionUtils.isNotEmpty(jdpTmRefundList)){
            for(JdpTmRefund jdpTmRefund : jdpTmRefundList){
                RefundBill refundBill = convertJdpTmRefund2RefundBill(jdpTmRefund);
                if(refundBill != null){
                    refundBillList.add(refundBill);
                }
            }
        }
        return refundBillList;
    }

    /**
     * 将聚石塔退款单转换为淘宝退款单
     * @param jdpTmRefund
     * @return
     * @throws com.taobao.api.ApiException
     * @throws TaoBaoApiException
     */
    private RefundBill convertJdpTmRefund2RefundBill(JdpTmRefund jdpTmRefund) throws com.taobao.api.ApiException, TaoBaoApiException {
        TmallEaiOrderRefundGetResponse response = TaobaoUtils.parseResponse(jdpTmRefund.getJdpResponse(), TmallEaiOrderRefundGetResponse.class);
        if(StringUtils.isNotBlank(response.getErrorCode())){
            throw new TaoBaoApiException(response.getBody());
        }
        return response.getRefundBill();
    }

    /**
     * 去重复添加退款单
     * @param jdpTmRefundList
     * @param jdpTmRefundList1
     */
    private void addJdpTmRefundDistinct(List<JdpTmRefund> jdpTmRefundList, List<JdpTmRefund> jdpTmRefundList1) {
        for(JdpTmRefund jdpTmRefund : jdpTmRefundList1){
            boolean isExist = false;
            for(JdpTmRefund jdpTmRefundOri : jdpTmRefundList){
                if(jdpTmRefund.getRefundId().longValue() == jdpTmRefundOri.getRefundId().longValue()){
                    isExist = true;
                }
            }
            if(!isExist){
                jdpTmRefundList.add(jdpTmRefund);
            }
        }
    }

    /**
     * 根据更新时间抓取聚石塔退款单
     * @param shopBean
     * @return
     */
    private List<JdpTmRefund> getJdpTmRefundsByUd(ShopBean shopBean) {
        // 从聚石塔获取交易信息
        JdpTmRefundQuery jdpTmRefundQuery2 = new JdpTmRefundQuery();
        jdpTmRefundQuery2.setSellerNick(shopBean.getSellerNick());
        jdpTmRefundQuery2.setStartJdpModified(shopBean.getFetchRefundStartDate());
        jdpTmRefundQuery2.setEndJdpModified(shopBean.getFetchRefundEndDate());
        jdpTmRefundQuery2.setStatus(shopBean.getRefundStatus());

        // 获得聚石塔订单信息
        return jdpTbTradeService.findJdpTmRefundByJdpTmRefundQuery(jdpTmRefundQuery2);
    }

    private List<JdpTmRefund> getJdpTmRefundsByCd(ShopBean shopBean) {
        // 从聚石塔获取交易信息 // 根据创建时间
        JdpTmRefundQuery jdpTmRefundQuery1 = new JdpTmRefundQuery();
        jdpTmRefundQuery1.setSellerNick(shopBean.getSellerNick());
        jdpTmRefundQuery1.setStartJdpCreated(shopBean.getFetchRefundStartDate());
        jdpTmRefundQuery1.setEndJdpCreated(shopBean.getFetchRefundEndDate());
        jdpTmRefundQuery1.setStatus(shopBean.getRefundStatus());

        // 获得聚石塔订单信息
        return jdpTbTradeService.findJdpTmRefundByJdpTmRefundQuery(jdpTmRefundQuery1);
    }
}
