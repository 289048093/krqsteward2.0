package com.ejushang.steward.ordercenter.service.api.impl.zy;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;

import com.ejushang.steward.openapicenter.zy.api.ZyRefundApi;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.RefundBill;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.RefundItem;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Tag;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Trade;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.RefundsQueryResponse;
import com.ejushang.steward.openapicenter.zy.constant.ConstantZiYou;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.OriginalRefundService;
import com.ejushang.steward.ordercenter.service.api.IRefundApiService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


/**
 * User: Shiro
 * Date: 14-8-7
 * Time: 上午10:32
 */
public class ZiYouRefundApiService implements IRefundApiService {

    private static final Logger log = LoggerFactory.getLogger(ZiYouRefundApiService.class);

    @Autowired
    private OriginalRefundService originalRefundService;

    @Autowired
    private OrderFetchService orderFetchService;

    @Override
    public List<OriginalRefund> fetchRefundByApi(ShopBean shopBean) throws Exception {
        if(log.isInfoEnabled()){
            log.info("自有API抓取退款单：抓取参数{}",shopBean);
            log.info("自有API抓取退款单：【{}】开始抓单：：：",shopBean.getSellerNick());
            log.info("自有API抓取退款单：抓取开始时间为：{}",shopBean.getFetchRefundStartDate());
            log.info("自有API抓取退款单：抓取结束时间为：{}",shopBean.getFetchRefundEndDate());
        }
        // 使用自有API抓取自有平台退款单
        List<RefundBill> refundBillList =searchRefundsByApi(shopBean);
        if(log.isInfoEnabled()){
            log.info("自有API退款单抓取：抓取原始自有平台退款单共：{}条",refundBillList.size());
        }
        // 将自有平台退款单转换成平台原始退款单
        List<OriginalRefund> originalRefundList = convertRefundBillList2OriginalRefundList(refundBillList,shopBean);
        if(log.isInfoEnabled()){
            log.info("自有API退款单抓取：转换后的原始退款单共：{}条",originalRefundList.size());
        }

        if(log.isInfoEnabled()){
            log.info("自有API抓取退款单：保存原始退款单：");
        }
        // 保存原始退款单
        originalRefundService.saveOriginalRefunds(originalRefundList);

        if(log.isInfoEnabled()){
            log.info("自有API抓取退款单：保存抓取记录：");
        }
        // 更新抓取时间(只要过程没有异常，就要更新抓取时间，否则会有重复抓取
        // 所有操作成功完成，添加退款单抓取记录
        saveOrderFetchForRefund(shopBean);

        return originalRefundList;
    }

    @Override
    public List<OriginalRefund> fetchRefundByDeploy(ShopBean shopBean) throws Exception {
        return null;
    }

    @Override
    public List<OriginalRefund> fetchReturnByApi(ShopBean shopBean) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<OriginalRefund> fetchReturnByDeploy(ShopBean shopBean) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 批量将自有平台退款单转换为平台原始退款单
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
     * 批量将自有平台退款商品详情转换为智库城退款单商品详情
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
     * 将自有平台退款商品详情转换为智库城退款单商品详情
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
        originalRefundItem.setNum(refundItem.getNum());
        originalRefundItem.setPrice(Money.valueOfCent(refundItem.getPrice()));
        originalRefundItem.setSku(refundItem.getSku());

        return originalRefundItem;
    }
    /**
     * 将自有平台退款单转换为智库城原始退款单
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
                EJSDateUtils.parseDateForNull(refundBill.getModified().toString(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR))
                ){
            return originalRefund;
        }

        if(originalRefund == null) {
            originalRefund = new OriginalRefund();
        }

        originalRefund.setRefundId(String.valueOf(refundBill.getRefundId()));
        originalRefund.setRefundType(refundBill.getRefundType());
        originalRefund.setTradeStatus(StringUtils.isNotBlank(refundBill.getTradeStatus()) ? refundBill.getTradeStatus().toUpperCase() : refundBill.getTradeStatus());
        originalRefund.setRefundFee(Money.valueOfCent(refundBill.getRefundFee()));
        originalRefund.setReason(refundBill.getReason());
        originalRefund.setActualRefundFee(Money.valueOfCent(refundBill.getActualRefundFee()));
        originalRefund.setCreated(EJSDateUtils.parseDateForNull(refundBill.getCreated().toString(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        originalRefund.setBuyerNick(refundBill.getBuyerNick());
        originalRefund.setSellerNick(refundBill.getSellerNick());
        originalRefund.setTid(String.valueOf(refundBill.getTid()));
        originalRefund.setOid(String.valueOf(refundBill.getOid()));
        originalRefund.setStatus(getOriginalRefundStatus(refundBill.getStatus()));
        originalRefund.setRefundPhase(getRefundPhase(refundBill.getRefundPhase()));
        originalRefund.setModified(EJSDateUtils.parseDateForNull(refundBill.getModified().toString(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        originalRefund.setBillType(refundBill.getBillType());
        originalRefund.setSid(refundBill.getSid());
        originalRefund.setOperationLog(refundBill.getOperationLog());
        originalRefund.setDescription(refundBill.getDescription());
        originalRefund.setBuyerId(refundBill.getBuyerId());
        originalRefund.setCompanyName(DeliveryType.valueOf(refundBill.getCompanyCode()));
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
     * 将自有平台的退款单状态转为原始退款单的状态
     * @param zyRefundStatus
     * @return
     */
    private OriginalRefundStatus getOriginalRefundStatus(String zyRefundStatus){
        if(StringUtils.equalsIgnoreCase(zyRefundStatus, ZyRefundStatus.WAIT_SELLER_AGREE.getName())){
            return OriginalRefundStatus.WAIT_SELLER_AGREE;
        }
        else if(StringUtils.equalsIgnoreCase(zyRefundStatus,ZyRefundStatus.SELLER_REFUSE.getName())){
            return OriginalRefundStatus.SELLER_REFUSE;
        }
        else if(StringUtils.equalsIgnoreCase(zyRefundStatus,ZyRefundStatus.GOODS_RETURNING.getName())){
            return OriginalRefundStatus.GOODS_RETURNING;
        }
        else if(StringUtils.equalsIgnoreCase(zyRefundStatus,ZyRefundStatus.CLOSED.getName())){
            return OriginalRefundStatus.CLOSED;
        }
        else if(StringUtils.equalsIgnoreCase(zyRefundStatus, ZyRefundStatus.SUCCESS.getName())){
            return OriginalRefundStatus.SUCCESS;
        }
        else {
            throw  new StewardBusinessException("【" + zyRefundStatus + "】无法转换成智库城状态");
        }
    }
    /**
     * 根据自有平台API抓取自有平台交易信息
     *
     * @param shopBean
     * @throws Exception
     */
    private List<RefundBill> searchRefundsByApi(ShopBean shopBean) throws Exception {
        List<RefundBill> refundBillList = new ArrayList<RefundBill>(0);

        // 通过自有平台api抓取订单 // 根据创建时间
        List<RefundBill> refundBillList1 = searchRefundsByApiAndCd(shopBean);
        if (log.isInfoEnabled()) {
            log.info("自有API抓取退款单：根据创建时间抓单：{}条", refundBillList1.size());
        }

        // 通过自有平台api抓取订单 // 根据修改时间
        List<RefundBill> refundBillList2 = searchRefundsByApiAndUd(shopBean);
        if (log.isInfoEnabled()) {
            log.info("自有API抓取退款单：根据更新时间抓单：{}条", refundBillList2.size());
        }

        // 1.先添加更新时间的数据，防止被根据创建时间获取的旧数据替换
        addOriginalRefundDistinct(refundBillList, refundBillList2);
        // 2.再添加创建时间的数据
        addOriginalRefundDistinct(refundBillList, refundBillList1);

        return refundBillList;
    }

    /**
     * 去除重复后添加自有平台至集合
     *
     * @param refundBillList
     * @param refundBillList2
     */
    private void addOriginalRefundDistinct(List<RefundBill> refundBillList, List<RefundBill> refundBillList2) {
        if (CollectionUtils.isNotEmpty(refundBillList2)) {
            for (RefundBill refundBill : refundBillList2) {
                boolean isExist = false;
                for (RefundBill refundBill2 : refundBillList) {
                    if (refundBill.getRefundId().equals(refundBill2.getRefundId())) {
                        isExist = true;
                    }
                }

                if (!isExist) {
                    refundBillList.add(refundBill);
                }
            }
        }
    }

    /**
     * 使用自有API抓取退款单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<RefundBill> searchRefundsByApiAndUd(ShopBean shopBean) throws Exception {
        // 构造保存退款单的集合
        List<RefundBill> refundBillList = new ArrayList<RefundBill>();

        ZyRefundApi refundApi = new ZyRefundApi();
        if(log.isInfoEnabled()){
            log.info("自有API退款单抓取：初始化ZyRefundApi");
        }
        // 构造参数
        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantZiYou.DATE_TYPE,ZyDateType.MODIFIED.zyValue);
        argsMap.put(ConstantZiYou.START_CREATED, shopBean.getFetchRefundStartDate());
        argsMap.put(ConstantZiYou.END_CREATED, shopBean.getFetchRefundEndDate());
        argsMap.put(ConstantZiYou.PAGE_NO,1L);
        argsMap.put(ConstantZiYou.PAGE_SIZE,ConstantZiYou.ZY_FETCH_REFUND_PAGE_SIZE);
        argsMap.put(ConstantZiYou.STATUS,shopBean.getRefundStatus());

        if(log.isInfoEnabled()){
            log.info("自有API退款单抓取：初始化ZyRefundApi调用参数argsMap：" + argsMap );
        }

        // 执行查询
        RefundsQueryResponse response = refundApi.refundsQuery(argsMap);

        // 符合条件的数据总条数
        Long totalCount = response.getTotalResults();
        Long pageSize = ConstantZiYou.ZY_FETCH_ORDER_PAGE_SIZE;
        Long pageNo = totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;


        for(long i = 1; i <= pageNo; i++){
            argsMap.put(ConstantZiYou.PAGE_NO,i);
            response = refundApi.refundsQuery(argsMap);
            if(CollectionUtils.isNotEmpty(response.getRefundBillList())) {
                refundBillList.addAll(response.getRefundBillList());
            }
        }

        Collections.sort(refundBillList, new Comparator<RefundBill>() {
            @Override
            public int compare(RefundBill o1,RefundBill o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });
        return refundBillList;
    }
    /**
     * 使用自有API抓取退款单
     * @param shopBean
     * @return
     * @throws Exception
     */
    private List<RefundBill> searchRefundsByApiAndCd(ShopBean shopBean) throws Exception {

        // 构造保存退款单的集合
        List<RefundBill> refundBillList = new ArrayList<RefundBill>();

       ZyRefundApi refundApi = new ZyRefundApi();
        if(log.isInfoEnabled()){
            log.info("自有API退款单抓取：初始化ZyRefundApi");
        }
        // 构造参数
        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantZiYou.DATE_TYPE,ZyDateType.CREATED.zyValue);
        argsMap.put(ConstantZiYou.START_CREATED, shopBean.getFetchRefundStartDate());
        argsMap.put(ConstantZiYou.END_CREATED, shopBean.getFetchRefundEndDate());
        argsMap.put(ConstantZiYou.PAGE_NO,1L);
        argsMap.put(ConstantZiYou.PAGE_SIZE,ConstantZiYou.ZY_FETCH_REFUND_PAGE_SIZE);
        argsMap.put(ConstantZiYou.STATUS,shopBean.getRefundStatus());

        if(log.isInfoEnabled()){
            log.info("自有API退款单抓取：初始化ZyRefundApi调用参数argsMap：" + argsMap );
        }

        // 执行查询
        RefundsQueryResponse response = refundApi.refundsQuery(argsMap);

        // 符合条件的数据总条数
        Long totalCount = response.getTotalResults();
        Long pageSize = ConstantZiYou.ZY_FETCH_REFUND_PAGE_SIZE;
        Long pageNo = totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;


        for(long i = 1; i <= pageNo; i++){
            argsMap.put(ConstantZiYou.PAGE_NO,i);
            response = refundApi.refundsQuery(argsMap); 
            if(CollectionUtils.isNotEmpty(response.getRefundBillList())) {
                refundBillList.addAll(response.getRefundBillList());
            }
        }

        Collections.sort(refundBillList, new Comparator<RefundBill>() {
            @Override
            public int compare(RefundBill o1,RefundBill o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });
        return refundBillList;
    }
    /**
     * 将自有平台退款类型转换为原始平台退款类型
     * @param zyRefundPhase
     * @return
     */
    private RefundPhase getRefundPhase(String zyRefundPhase){
        if(StringUtils.equalsIgnoreCase(zyRefundPhase,ZyRefundPhase.ONSALE.toString())){
            return RefundPhase.ON_SALE;
        }
        else if(StringUtils.equalsIgnoreCase(zyRefundPhase,ZyRefundPhase.AFTERSALE.toString())){
            return RefundPhase.AFTER_SALE;
        }
        else{
            return null;
            //throw new StewardBusinessException("【" +tbRefundPhase + "】无法识别");
        }
    }
    /**
     * 批量将自有退款标签信息转换为智库城原始标签信息
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
     * 将自有退款标签信息转换为智库城原始标签信息
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
     * 保存抓取退货单记录
     * @param shopBean
     */
    private void saveOrderFetchForReturn(ShopBean shopBean) {
        OrderFetch orderFetchNew = getOrderFetchForReturn(shopBean);
        orderFetchService.save(orderFetchNew);
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

}
