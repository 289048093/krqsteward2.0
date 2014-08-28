package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.keygenerator.SequenceGenerator;
import com.ejushang.steward.ordercenter.returnvisit.ReturnVisitService;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import com.ejushang.steward.ordercenter.util.AfterSaleUtil;
import com.ejushang.steward.ordercenter.util.OrderUtil;
import com.ejushang.steward.ordercenter.vo.AfterSalesVo;
import com.ejushang.steward.ordercenter.vo.ReceiveGoodsVo;
import com.ejushang.steward.ordercenter.vo.ReturnGoodsVo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * 售后工单服务
 *
 * @Author Channel
 * @Date 2014/8/5
 * @Version: 1.0
 */
@Service
@Transactional
public class AfterSalesService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private RefundService refundService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderFeeService orderFeeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReturnVisitService returnVisitService;

    /**
     * 生成唯一的Code
     * TODO: 生成规则没定，暂用UUID代替
     */
    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    /**
     * 查询afterSales
     */
    @Transactional(readOnly = true)
    public Page findDetails(Map<String, Object[]> map, Page page) throws ParseException {
        Map<String, String> orderMapConditions = new HashMap<String, String>();

        //将接受到的map参数解析并赋值给map1
        OrderUtil.getConditionMap(map, orderMapConditions);
        //显示层的Vo的List
        List<AfterSalesVo> afterSalesVos = new ArrayList<AfterSalesVo>();
        //HQL条件的值
        List<Object> objects = new ArrayList<Object>();
        //拼接HQL的变量
        StringBuilder stringBuilder = new StringBuilder();
        //拼接HQL的方法
        AfterSaleUtil.orderCondition(orderMapConditions, stringBuilder, objects);
        //执行HQL
        List<AfterSales> afterSalesList = generalDAO.query(stringBuilder.toString(), page, objects.toArray());
        System.out.print(stringBuilder.toString());
        //拼接Vo的循环
        for (AfterSales afterSales : afterSalesList) {
            AfterSalesVo afterSalesVo = new AfterSalesVo();
            AfterSaleUtil.getAfterSaleVo(afterSales, afterSalesVo);
            afterSalesVos.add(afterSalesVo);
        }
        page.setResult(afterSalesVos);
        return page;
    }

    /**
     * 保存售后工单
     *
     * @param afterSalesVo
     * @return 售后单ID
     */
    public Integer saveAfterSales(AfterSalesVo afterSalesVo) {
        Integer orderId = afterSalesVo.getOrderId();
        if (orderId == null) {
            throw new StewardBusinessException("必须指定售后订单。");
        }
        AfterSales afterSales = null;
        Integer afterSalesId = afterSalesVo.getId();
        if (afterSalesId != null) {
            afterSales = generalDAO.get(AfterSales.class, afterSalesId);
            Integer orderId_ = afterSales.getOrderId();
            if (!orderId_.equals(orderId)) {
                throw new StewardBusinessException("不能修改售后单绑定的订单。");
            }
            AfterSalesStatus status = afterSales.getStatus();
            switch (status) {
                case SAVE:  // 处理中
                case CHECK:  // 客服确认，进入仓库审批中
                case REJECT:  // 仓库审批驳回
                case CANCEL:  // 作废售后工单
                    break;
                case ACCEPT:  //仓库审批通过
                    throw new StewardBusinessException("仓库审批通过不能再编辑售后工单。");
                case FINISH:  // 订单完成状态
                    throw new StewardBusinessException("售后工单已结束，无法再进行编辑。");
                default:
                    throw new StewardBusinessException("售后工单状态异常。");
            }
        } else {
            afterSales = new AfterSales();
            afterSales.setOrderId(orderId);
            afterSales.setCode(generateCode());
            Employee employee = SessionUtils.getEmployee();
            if (employee != null) {
                afterSales.setServiceUserId(employee.getId());
                afterSales.setServiceUserName(employee.getName());
            }
            // TODO: 这个原因要得咨询一下看是不是从库里面直接取的
            afterSales.setReason(afterSalesVo.getReason());
//            afterSales.setStatus(AfterSalesStatus.SAVE); // 新增后为处理中状态
        }
        afterSales.setSend(afterSalesVo.getSend());
        // 售后工单基础信息
        AfterSalesSource source = afterSalesVo.getSource();
        if (source == null) {
            throw new StewardBusinessException("请指定售后单来源。");
        }
        afterSales.setSource(source);
        // 如果来源是回访单则必须填写回访单号
        if (AfterSalesSource.VISIT.equals(source)) {
            Integer revisitId = afterSalesVo.getRevisitId();
            if (revisitId == null) {
                throw new StewardBusinessException("回访单转售后必须指定回访单。");
            }
            ReturnVisitTask returnVisit = returnVisitService.getReturnVisitTaskById(revisitId);
            if (returnVisit == null) {
                throw new StewardBusinessException("回访单不存在。");
            }
            afterSales.setRevisitId(revisitId);
        }
        // 验证所属订单
        Order order = orderService.findOrderById(orderId);
        if (order == null) {
            throw new StewardBusinessException("关联的订单信息不存在。");
        }
        String reasonCode = afterSalesVo.getReasonCode();
        if (StringUtils.isBlank(reasonCode)) {
            throw new StewardBusinessException("必须指定售后原因码。");
        }
        afterSales.setReasonCode(reasonCode);
        afterSales.setRemark(afterSalesVo.getRemark());
        // 售后工单所属品牌
        List<OrderItem> orderItemList = order.getOrderItemList();
        if (orderItemList == null || orderItemList.isEmpty()) {
            throw new StewardBusinessException("订单异常，所属订单不包含订单项。");
        }
        // 同一个订单品牌肯定一致，故只取一次
        OrderItem orderItem = orderItemList.get(0);
        Product product = orderItem.getProduct();
        if (product == null) {
            throw new StewardBusinessException("订单异常，无法获取产品信息。");
        }
//        afterSales.setBrandId(product.getBrandId());
//        afterSales.setBrandName(product.getBrandName());
        Brand brand = product.getBrand();
        if (brand == null) {
            throw new StewardBusinessException("订单异常，无法获取品牌信息。");
        }
        afterSales.setBrandId(brand.getId());
        afterSales.setBrandName(brand.getName());
        afterSales.setPlatformType(order.getPlatformType());
        afterSales.setPlatformOrderNo(order.getPlatformOrderNo());
        generalDAO.saveOrUpdate(afterSales);
        // 保存原因码
        saveAfterSalesReason(reasonCode, afterSales);
        // 保存附件
        List<String> attachmentList = afterSalesVo.getAttachmentList();
        if (attachmentList != null && !attachmentList.isEmpty()) {
            saveAfterSalesAttachment(attachmentList, afterSales);
        }
        // 保存发货信息
        AfterSalesSend afterSalesSend = afterSales.getAfterSalesSend();
        Boolean send = afterSales.isSend();
        if (send != null && send) {
            AfterSalesVo.Send sendInfo = afterSalesVo.getSendInfo();
            if (sendInfo == null) {
                throw new StewardBusinessException("未配置发货信息。");
            }
            saveAfterSalesSend(sendInfo, afterSalesSend, afterSales);
        } else {
            // 不发货则删除发货信息
            if (afterSalesSend != null) {
                generalDAO.remove(afterSalesSend);
            }
        }
        // 处理售后工单项
        List<AfterSalesVo.Item> itemList = afterSalesVo.getItemList();
        if (itemList == null || itemList.isEmpty()) {
            throw new StewardBusinessException("至少配置一个售后项。");
        }
        List<AfterSalesItem> afterSalesItemList = afterSales.getAfterSalesItemList();
        Map<Object, AfterSalesItem> noUseAfterSalesItemMap = new HashMap<Object, AfterSalesItem>();
        if (afterSalesItemList != null) {
            for (AfterSalesItem afterSalesItem : afterSalesItemList) {
                noUseAfterSalesItemMap.put(afterSalesItem.getId(), afterSalesItem);
            }
        }
        for (AfterSalesVo.Item itemVo : itemList) {
            Integer itemId = itemVo.getId();
            AfterSalesItem afterSalesItem = null;
            if (itemId != null) {
                afterSalesItem = noUseAfterSalesItemMap.remove(itemId);
                if (afterSalesItem == null) {
                    // 说明该订单项已经被移除，则采用新增订单项方式
                    itemVo.setId(null);
                }
            }
            saveAfterSalesItem(itemVo, afterSalesItem, afterSales);
        }
        // 移除掉无效的售后工单项
        if (!noUseAfterSalesItemMap.isEmpty()) {
            Collection<AfterSalesItem> removeAfterSalesItemList = noUseAfterSalesItemMap.values();
            for (AfterSalesItem removeItem : removeAfterSalesItemList) {
                AfterSalesRefund afterSalesRefund = removeItem.getAfterSalesRefund();
                Boolean payment = false;
                // 已确认支付，不能删除工单项
                if (afterSalesRefund != null && (payment = afterSalesRefund.isPayment()) != null && payment) {
                    throw new StewardBusinessException("不能删除已确认支付的售后工单项。");
                }
            }
            removeAfterSalesItemList(removeAfterSalesItemList);
        }
        AfterSalesStatus status = afterSales.getStatus();
        if (status == null) {
            approve(afterSales, AfterSalesStatus.SAVE, "创建订单");
        } else {
            switch (status) {
                case REJECT: // 仓库审批驳回
                    approve(afterSales, AfterSalesStatus.CHECK, "驳回修改后转仓库审核");
                    break;
                case CANCEL:// 作废售后工单
                    approve(afterSales, AfterSalesStatus.CHECK, "作废工单重新编辑");
                    break;
            }
        }
        return afterSales.getId();
    }

    /**
     * 保存附件信息
     * TODO: 这块要进一步确认实现细节
     *
     * @param attachmentList
     * @param afterSales
     */
    private void saveAfterSalesAttachment(List<String> attachmentList, AfterSales afterSales) {
        // 先清除历史原因码
        List<AfterSalesAttachment> afterSalesAttachmentList = afterSales.getAfterSalesAttachmentList();
        if (afterSalesAttachmentList == null) {
            generalDAO.remove(afterSalesAttachmentList.toArray());
        }
        for (String attachment : attachmentList) {
            if (StringUtils.isBlank(attachment)) {
                continue;
            }
            AfterSalesAttachment attachmentObj = new AfterSalesAttachment();
            attachmentObj.setAfterSalesId(afterSales.getId());
            attachmentObj.setPath(attachment);
            generalDAO.saveOrUpdate(attachmentObj);
        }
    }

    /**
     * 验证空串
     *
     * @param str
     */
    private String validEmpty(String str, String error) {
        if (StringUtils.isBlank(str)) {
            throw new StewardBusinessException(error);
        }
        return str;
    }

    /**
     * 保存发货信息
     *
     * @param sendVo
     * @param afterSalesSend
     * @param afterSale
     */
    private void saveAfterSalesSend(AfterSalesVo.Send sendVo, AfterSalesSend afterSalesSend, AfterSales afterSale) {
        if (afterSalesSend == null) {
            afterSalesSend = new AfterSalesSend();
            afterSalesSend.setAfterSalesId(afterSale.getId());
        }
        String receiverMobile = sendVo.getReceiverMobile();
        String receiverPhone = sendVo.getReceiverPhone();
        if (StringUtils.isBlank(receiverPhone) && StringUtils.isBlank(receiverMobile)) {
            throw new StewardBusinessException("发货信息异常，收货电话和手机至少填写一个。");
        }
        afterSalesSend.setReceiverPhone(receiverPhone);
        afterSalesSend.setReceiverMobile(receiverMobile);
//        afterSalesSend.setShippingNo(sendVo.getShippingNo());
        afterSalesSend.setReceiverName(validEmpty(sendVo.getReceiverName(), "发货信息异常，未填写收件人"));
        afterSalesSend.setShippingComp(validEmpty(sendVo.getShippingComp(), "发货信息异常，未填写快递公司"));
        afterSalesSend.setReceiverState(validEmpty(sendVo.getReceiverState(), "发货信息异常，未填写收货省份"));
        afterSalesSend.setReceiverCity(validEmpty(sendVo.getReceiverCity(), "发货信息异常，未填写收货城市"));
        afterSalesSend.setReceiverDistrict(validEmpty(sendVo.getReceiverDistrict(), "发货信息异常，未填写收货地区"));
        afterSalesSend.setReceiverAddress(validEmpty(sendVo.getReceiverAddress(), "发货信息异常，未填写收货详细地址"));
        afterSalesSend.setReceiverZip(validEmpty(sendVo.getReceiverZip(), "发货信息异常，未填写收货邮政编码"));
        afterSalesSend.setReceiverRemark(sendVo.getReceiverRemark());
        generalDAO.saveOrUpdate(afterSalesSend);
    }

    /**
     * 保存售后原因码
     *
     * @param reasonCode
     * @param afterSales
     */
    private void saveAfterSalesReason(String reasonCode, AfterSales afterSales) {
        String[] reasonCodeList = reasonCode.split(",");
        // 先清除历史原因码
        List<AfterSalesReason> afterSalesReasonList = afterSales.getAfterSalesReasonList();
        if (afterSalesReasonList == null) {
            generalDAO.remove(afterSalesReasonList.toArray());
        }
        for (String code : reasonCodeList) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            // TODO: 需要对接原因码模块
            AfterSalesReason reason = new AfterSalesReason();
            reason.setAfterSalesId(afterSales.getId());
            reason.setReasonCode(code);
            generalDAO.saveOrUpdate(reason);
        }
    }

    /**
     * 移除售后工单项列表
     *
     * @param items
     */
    private void removeAfterSalesItemList(Collection<AfterSalesItem> items) {
        if (items != null && !items.isEmpty()) {
            Iterator<AfterSalesItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                AfterSalesItem next = iterator.next();
                removeAfterSalesItem(next);
            }
        }
    }

    /**
     * 移除售后工单项
     *
     * @param item
     */
    private void removeAfterSalesItem(AfterSalesItem item) {
        removeAfterSalesRefund(item.getAfterSalesRefund());
        removeAfterSalesRefundGoods(item.getAfterSalesRefundGoods());
        removeAfterSalesPatchList(item.getAfterSalesPatchList());
        generalDAO.remove(item);
    }

    /**
     * 移除退款信息
     *
     * @param afterSalesRefund
     */
    private void removeAfterSalesRefund(AfterSalesRefund afterSalesRefund) {
        if (afterSalesRefund != null) {
            List<AfterSalesRefundAlloc> afterSalesRefundAllocList = afterSalesRefund.getAfterSalesRefundAllocList();
            if (afterSalesRefundAllocList != null && !afterSalesRefundAllocList.isEmpty()) {
                generalDAO.remove(afterSalesRefundAllocList.toArray());
            }
            generalDAO.remove(afterSalesRefund);
        }
    }

    /**
     * 移除退货信息
     *
     * @param afterSalesRefundGoods
     */
    private void removeAfterSalesRefundGoods(AfterSalesRefundGoods afterSalesRefundGoods) {
        if (afterSalesRefundGoods != null) {
            generalDAO.remove(afterSalesRefundGoods);
        }
    }

    /**
     * 移除补货列表信息
     *
     * @param afterSalesPatchList
     */
    private void removeAfterSalesPatchList(Collection<AfterSalesPatch> afterSalesPatchList) {
        if (afterSalesPatchList != null && !afterSalesPatchList.isEmpty()) {
            Iterator<AfterSalesPatch> iterator = afterSalesPatchList.iterator();
            while (iterator.hasNext()) {
                AfterSalesPatch next = iterator.next();
                removeAfterSalesPatch(next);
            }
        }
    }

    /**
     * 移除补货信息
     *
     * @param afterSalesPatch
     */
    private void removeAfterSalesPatch(AfterSalesPatch afterSalesPatch) {
        if (afterSalesPatch != null) {
            generalDAO.remove(afterSalesPatch);
        }
    }

    /**
     * 根据售后操作组合确定售后类型
     *
     * @param refund
     * @param refundGoods
     * @param patch
     */
    private AfterSalesType getAfterSalesType(boolean refund, boolean refundGoods, boolean patch) {
        AfterSalesType type = null;
        if (refund) {
            type = AfterSalesType.REFUND;
        }
        if (refundGoods) {
            type = AfterSalesType.REFUND_GOODS;
            if (!refund && !patch) {
                throw new StewardBusinessException("退货必须同时进行退款或者换货。");
            }
        }
        if (patch) {
            type = AfterSalesType.PATCH;
            if (refundGoods) { // 补货和退货同时存在则为换货
                type = AfterSalesType.SWAP;
            }
        }
        return type;
    }

    /**
     * 保存售后工单项目
     *
     * @param afterSalesItemVo
     * @param afterSalesItem
     * @param afterSales
     */
    private void saveAfterSalesItem(AfterSalesVo.Item afterSalesItemVo, AfterSalesItem afterSalesItem, AfterSales afterSales) {
        Integer orderItemId = afterSalesItemVo.getOrderItemId();
        if (orderItemId == null) {
            throw new StewardBusinessException("必须指定订单项。");
        }
        OrderItem orderItem = generalDAO.get(OrderItem.class, orderItemId);
        if (orderItem == null) {
            throw new StewardBusinessException("订单项不存在。");
        }
        Integer orderId = orderItem.getOrderId();
        if (orderId == null || !orderId.equals(afterSales.getOrderId())) {
            throw new StewardBusinessException("只能选择售后订单下的订单项。");
        }
        if (afterSalesItem == null) {
            afterSalesItem = new AfterSalesItem();
            afterSalesItem.setAfterSalesId(afterSales.getId());
            afterSalesItem.setOrderItemId(orderItemId);
        } else {
            Integer orderItemId_ = afterSalesItem.getOrderItemId();
            if (!orderItemId_.equals(orderItemId)) {
                throw new StewardBusinessException("不能修改售后项绑定的订单项。");
            }
        }
        AfterSalesVo.Refund afterSalesRefundVo = afterSalesItemVo.getRefund();
        AfterSalesVo.RefundGoods afterSalesRefundGoodsVo = afterSalesItemVo.getRefundGoods();
        List<AfterSalesVo.Patch> afterSalesPatchVoList = afterSalesItemVo.getPatchList();
        // 根据售后内容确认售后项类型
        boolean refund = afterSalesRefundVo != null;
        boolean refundGoods = afterSalesRefundGoodsVo != null;
        boolean patch = afterSalesPatchVoList != null && !afterSalesPatchVoList.isEmpty();
        AfterSalesType type = getAfterSalesType(refund, refundGoods,
                patch);
        if (type == null) {
            throw new StewardBusinessException("未配置售后信息。");
        }
        afterSalesItem.setRefund(refund);
        afterSalesItem.setRefundGoods(refundGoods);
        afterSalesItem.setPatch(patch);
        afterSalesItem.setType(type);
        generalDAO.saveOrUpdate(afterSalesItem);
        // 退款处理
        AfterSalesRefund afterSalesRefund = afterSalesItem.getAfterSalesRefund();
        if (refund) {
            saveAfterSalesItemRefund(afterSalesRefundVo, afterSalesRefund, afterSales, afterSalesItem);
        } else {
            // 已确认支付，不能删除退款操作
            Boolean payment = false;
            if (afterSalesRefund != null && (payment = afterSalesRefund.isPayment()) != null && payment) {
                throw new StewardBusinessException("已确认支付，不能删除退款操作。");
            }
            // 取消退款操作
            removeAfterSalesRefund(afterSalesRefund);
        }
        // 退货处理
        AfterSalesRefundGoods afterSalesRefundGoods = afterSalesItem.getAfterSalesRefundGoods();
        if (refundGoods) {
            saveAfterSalesItemRefundGoods(afterSalesRefundGoodsVo, afterSalesRefundGoods, afterSales, afterSalesItem, orderItem);
        } else {
            // 已取消退货操作
            removeAfterSalesRefundGoods(afterSalesRefundGoods);
        }
        // 补货处理
        List<AfterSalesPatch> afterSalesPatchList = afterSalesItem.getAfterSalesPatchList();
        Map<Object, AfterSalesPatch> noUseAfterSalesPatchMap = new HashMap<Object, AfterSalesPatch>();
        if (afterSalesPatchList != null) {
            for (AfterSalesPatch afterSalesPatch : afterSalesPatchList) {
                noUseAfterSalesPatchMap.put(afterSalesPatch.getId(), afterSalesPatch);
            }
        }
        if (patch) {
            for (AfterSalesVo.Patch patchVo : afterSalesPatchVoList) {
                Integer patchId = patchVo.getId();
                AfterSalesPatch patchObj = null;
                if (patchId != null) {
                    patchObj = noUseAfterSalesPatchMap.remove(patchId);
                    if (patchObj == null) {
                        patchVo.setId(null);
                    }
                }
                saveAfterSalesItemPatch(patchVo, patchObj, afterSales, afterSalesItem);
            }
        }
        // 移除无效的补货信息
        if (noUseAfterSalesPatchMap != null && !noUseAfterSalesPatchMap.isEmpty()) {
            removeAfterSalesPatchList(noUseAfterSalesPatchMap.values());
        }
    }

    /**
     * 保存售后工单补货信息
     *
     * @param afterSalesPatchVo
     * @param afterSalesPatch
     * @param afterSales
     * @param afterSalesItem
     */
    private void saveAfterSalesItemPatch(AfterSalesVo.Patch afterSalesPatchVo, AfterSalesPatch afterSalesPatch, AfterSales afterSales,
                                         AfterSalesItem afterSalesItem) {
        if (afterSalesPatch == null) {
            afterSalesPatch = new AfterSalesPatch();
            afterSalesPatch.setAfterSalesId(afterSales.getId());
            afterSalesPatch.setAfterSalesItemId(afterSalesItem.getId());
        }
        Integer productId = afterSalesPatchVo.getProductId();
        if (productId == null) {
            throw new StewardBusinessException("产品ID不能为空。");
        }
        Product product = productService.get(productId);
        if (product == null || product.isDeleted()) {
            throw new StewardBusinessException("选择的产品不存在或已被删除。");
        }
        Integer brandId = product.getBrandId();
        if (brandId == null || !brandId.equals(afterSales.getBrandId())) {
            throw new StewardBusinessException("选择的品牌必须是“" + afterSales.getBrandName() + "”。");
        }
        afterSalesPatch.setProductId(productId);
        Integer patchCount = afterSalesPatchVo.getPatchCount();
        if (patchCount == null || patchCount < 1) {
            throw new StewardBusinessException("补货的产品数量必须大于等于1。");
        }
        afterSalesPatch.setCount(patchCount);
        // 包含退货操作则认为该订单为售后换货订单
        Boolean refundGoods = afterSalesItem.isRefundGoods();
        if (refundGoods != null && refundGoods) {
            afterSalesPatch.setOrderItemType(OrderItemType.EXCHANGE_AFTERSALE);
        } else {
            afterSalesPatch.setOrderItemType(OrderItemType.REPLENISHMENT);
        }
        generalDAO.saveOrUpdate(afterSalesPatch);
    }

    /**
     * 保存售后工单退货信息
     *
     * @param afterSalesRefundGoodsVo
     * @param afterSalesRefundGoods
     * @param afterSales
     * @param afterSalesItem
     * @param orderItem
     */
    private void saveAfterSalesItemRefundGoods(AfterSalesVo.RefundGoods afterSalesRefundGoodsVo, AfterSalesRefundGoods afterSalesRefundGoods, AfterSales afterSales, AfterSalesItem afterSalesItem, OrderItem orderItem) {
        if (afterSalesRefundGoods == null) {
            afterSalesRefundGoods = new AfterSalesRefundGoods();
            afterSalesRefundGoods.setAfterSalesId(afterSales.getId());
            afterSalesRefundGoods.setAfterSalesItemId(afterSalesItem.getId());
        }
        Integer count = afterSalesRefundGoodsVo.getCount();
        if (count == null) {
            throw new StewardBusinessException("未填写退款数量。");
        }
        Integer buyCount = orderItem.getBuyCount();
        // 上次确认多次退货不与判断，只看当前有无超出即可
        if (buyCount == null || buyCount < count) {
            throw new StewardBusinessException("退货数量不能大于购买数量。");
        }
        afterSalesRefundGoods.setCount(count);
        generalDAO.saveOrUpdate(afterSalesRefundGoods);
    }

    /**
     * 保存售后工单退款信息
     *
     * @param afterSalesRefundVo
     * @param afterSalesRefund
     * @param afterSales
     * @param afterSalesItem
     */
    private void saveAfterSalesItemRefund(AfterSalesVo.Refund afterSalesRefundVo, AfterSalesRefund afterSalesRefund, AfterSales afterSales,
                                          AfterSalesItem afterSalesItem) {
        // 退款处理
        if (afterSalesRefund == null) {
            afterSalesRefund = new AfterSalesRefund();
            afterSalesRefund.setAfterSalesId(afterSales.getId());
            afterSalesRefund.setAfterSalesItemId(afterSalesItem.getId());
        }
        RefundMethod refundMethod = afterSalesRefundVo.getRefundMethod();
        if (refundMethod != null) {
            afterSalesRefund.setRefundMethod(refundMethod);
            switch (refundMethod) {
                case ALIPAY:
                    String alipayNo = afterSalesRefundVo.getAlipayNo();
                    if (StringUtils.isBlank(alipayNo)) {
                        throw new StewardBusinessException("未填写支付宝账户。");
                    }
                    afterSalesRefund.setAlipayNo(alipayNo);
                    break;
                case BANK:
                    afterSalesRefund.setRefundMethod(refundMethod);
                    String bank = afterSalesRefundVo.getBank();
                    if (StringUtils.isBlank(bank)) {
                        throw new StewardBusinessException("未指定银行。");
                    }
                    afterSalesRefund.setAlipayNo(bank);
                    String bankAccout = afterSalesRefundVo.getBankAccout();
                    if (StringUtils.isBlank(bankAccout)) {
                        throw new StewardBusinessException("未填写银行卡号。");
                    }
                    afterSalesRefund.setBankAccout(bankAccout);
                    String bankUser = afterSalesRefundVo.getBankUser();
                    if (StringUtils.isBlank(bankUser)) {
                        throw new StewardBusinessException("未填写银行开户人。");
                    }
                    afterSalesRefund.setBankUser(bankUser);
                    break;
                default:
                    throw new StewardBusinessException("无法设别的退款方式。");
            }
        }
        generalDAO.saveOrUpdate(afterSalesRefund);
        // 退款分配处理
        List<AfterSalesVo.RefundAlloc> afterSalesRefundAllocList = afterSalesRefundVo.getRefundAllocList();
        if (afterSalesRefundAllocList == null) {
            throw new StewardBusinessException("退款金额未进行分配操作。");
        }
        Map<Object, AfterSalesRefundAlloc> noUseAfterSalesRefundAllocMap = new HashMap<Object, AfterSalesRefundAlloc>();
        List<AfterSalesRefundAlloc> refundAllocList = afterSalesRefund.getAfterSalesRefundAllocList();
        if (refundAllocList != null) {
            for (AfterSalesRefundAlloc refundAlloc : refundAllocList) {
                noUseAfterSalesRefundAllocMap.put(refundAlloc.getId(), refundAlloc);
            }
        }
        Money feeSum = Money.valueOf(0);
        Money onlineFee = Money.valueOf(0);
        Money offlineFee = Money.valueOf(0);
        for (AfterSalesVo.RefundAlloc afterSalesRefundAllocVo : afterSalesRefundAllocList) {
            Integer refundAllocId = afterSalesRefundAllocVo.getId();
            AfterSalesRefundAlloc refundAlloc = null;
            if (refundAllocId != null) {
                refundAlloc = noUseAfterSalesRefundAllocMap.remove(refundAllocId);
                if (refundAlloc == null) {
                    refundAlloc.setId(null);
                } else {
                    refundAlloc = new AfterSalesRefundAlloc();
                }
            } else {
                refundAlloc = new AfterSalesRefundAlloc();
            }
            Money platformFee = afterSalesRefundAllocVo.getPlatformFee();
            Money supplierFee = afterSalesRefundAllocVo.getSupplierFee();
            if (platformFee == null || platformFee.compareTo(Money.valueOf(0)) < 0 ||
                    supplierFee == null || supplierFee.compareTo(Money.valueOf(0)) < 0) {
                throw new StewardBusinessException("退款分配金额必须大于等于0。");
            }
            // 确认支付后不能调整退款金额
            Boolean payment = afterSalesRefund.isPayment();
            if (payment != null && payment) {
                // 不能新增退款金额
                if (refundAlloc.getId() == null) {
                    throw new StewardBusinessException("确认支付后不能新增退款金额。");
                }
                if (!platformFee.equals(refundAlloc.getPlatformFee()) || !supplierFee.equals(refundAlloc.getSupplierFee())) {
                    throw new StewardBusinessException("确认支付后不能调整退款金额。");
                }
            } else {
                refundAlloc.setPlatformFee(platformFee);
                refundAlloc.setSupplierFee(supplierFee);
                refundAlloc.setFee(platformFee.add(supplierFee));
            }
            Boolean online = afterSalesRefundAllocVo.getOnline();
            if (online != null) {
                Money sumFee = platformFee.add(supplierFee);
                if (online) {
                    onlineFee = onlineFee.add(sumFee);
                } else {
                    offlineFee = offlineFee.add(sumFee);
                }
            }
            refundAlloc.setAfterSalesId(afterSales.getId());
            refundAlloc.setAfterSalesItemId(afterSalesItem.getId());
            refundAlloc.setAfterSalesRefundId(afterSalesRefund.getId());
            refundAlloc.setType(afterSalesRefundAllocVo.getType());
            refundAlloc.setOnline(online);
            generalDAO.saveOrUpdate(refundAlloc);
        }
        feeSum = onlineFee.add(offlineFee);
        if (feeSum.compareTo(Money.valueOf(0)) < 1) {
            throw new StewardBusinessException("退款总金额必须大于0。");
        }
        // 移除无效的退款分配信息
        if (!noUseAfterSalesRefundAllocMap.isEmpty()) {
            Object[] objects = noUseAfterSalesRefundAllocMap.values().toArray();
            generalDAO.remove(objects);
        }
    }

    @Transactional(readOnly = true)
    public AfterSales getAfterSales(Integer id) {
        if (id == null) {
            throw new StewardBusinessException("售后ID不能为空。");
        }
        AfterSales afterSales = generalDAO.get(AfterSales.class, id);
        Hibernate.initialize(afterSales.getAfterSalesFlowLogList());
        Hibernate.initialize(afterSales.getAfterSalesAttachmentList());
        List<AfterSalesItem> afterSalesItemList = afterSales.getAfterSalesItemList();
        Hibernate.initialize(afterSalesItemList);
        for (AfterSalesItem item : afterSalesItemList) {
            AfterSalesRefund afterSalesRefund = item.getAfterSalesRefund();
            if (afterSalesRefund != null) {
                Hibernate.initialize(afterSalesRefund.getAfterSalesRefundAllocList());
            }
            List<AfterSalesPatch> afterSalesPatchList = item.getAfterSalesPatchList();
            if (afterSalesPatchList != null) {
                Hibernate.initialize(afterSalesPatchList);
            }
        }
        Hibernate.initialize(afterSales.getAfterSalesReasonList());
        Hibernate.initialize(afterSales.getAfterSalesSend());
        return afterSales;
    }

    /**
     * 流程操作
     *
     * @param afterSalesId 售后单ID
     * @param status       状态
     * @param remarks      拒绝原因
     */
    public void flow(Integer afterSalesId, AfterSalesStatus status, String remarks) {
        if (afterSalesId == null) {
            throw new StewardBusinessException("必须传递售后单ID。");
        }
        AfterSales afterSales = generalDAO.get(AfterSales.class, afterSalesId);
        if (afterSales == null) {
            throw new StewardBusinessException("售后单不存在或已被删除。");
        }
        approve(afterSales, status, remarks);
    }

    /**
     * <pre>
     * 测试售后工单是否完成
     * 如果完成则将售后工单修改为完成状态，否则不处理
     * </pre>
     *
     * @param afterSales 当前售后单
     */
    private boolean finish(AfterSales afterSales) {
        try {
            approve(afterSales, AfterSalesStatus.FINISH, null);
            return true;
        } catch (StewardBusinessException e) {
            return false;
        }
    }

    /**
     * 审批操作
     *
     * @param afterSales 当前售后单
     * @param status     审批状态
     * @param remarks    审批备注
     */
    private void approve(AfterSales afterSales, AfterSalesStatus status, String remarks) {
        AfterSalesStatus curStatus = afterSales.getStatus();
        if (status.equals(curStatus)) {
            throw new StewardBusinessException("不能进行重复操作。");
        }
        switch (status) {
            case SAVE: { // 处理中
                if (curStatus != null && !ArrayUtils.contains(new AfterSalesStatus[]{AfterSalesStatus.CANCEL}, curStatus)) {
                    throw new StewardBusinessException("当前订单状态为“" + curStatus.getValue() + "”，无法调整为处理中状态。");
                }
                break;
            }
            case CHECK: { // 客服确认，进入仓库审批中
                if (!ArrayUtils.contains(new AfterSalesStatus[]{AfterSalesStatus.SAVE, AfterSalesStatus.CANCEL, AfterSalesStatus.REJECT}, curStatus)) {
                    throw new StewardBusinessException("当前订单状态为“" + curStatus.getValue() + "”，无法进行客服审核操作。");
                }
                break;
            }
            case ACCEPT: { //仓库审批通过
                if (!ArrayUtils.contains(new AfterSalesStatus[]{AfterSalesStatus.CHECK, AfterSalesStatus.REJECT}, curStatus)) {
                    throw new StewardBusinessException("当前订单状态为“" + curStatus.getValue() + "”，无法进行仓库审核通过操作。");
                }
                break;
            }
            case REJECT: { // 仓库审批驳回
                if (StringUtils.isBlank(remarks)) {
                    throw new StewardBusinessException("必须填写驳回原因。");
                }
                if (!ArrayUtils.contains(new AfterSalesStatus[]{AfterSalesStatus.CHECK}, curStatus)) {
                    throw new StewardBusinessException("当前订单状态为“" + curStatus.getValue() + "”，无法进行仓库审核驳回操作。");
                }
                break;
            }
            case CANCEL: { // 作废售后工单
                if (ArrayUtils.contains(new AfterSalesStatus[]{AfterSalesStatus.FINISH}, curStatus)) {
                    throw new StewardBusinessException("当前订单状态为“" + curStatus.getValue() + "”，无法作废售后工单。");
                }
                // 进行作废操作
                cancelAfterSales(afterSales);
                break;
            }
            case FINISH: { // 订单完成状态
                // 必须仓库同意
                if (AfterSalesStatus.ACCEPT.equals(curStatus)) {
                    throw new StewardBusinessException("当前订单状态为“" + curStatus.getValue() + "”，无法转入完成状态。");
                }
                // 有退款的必须已确认支付
                List<AfterSalesRefund> afterSalesRefundList = afterSales.getAfterSalesRefundList();
                if (afterSalesRefundList != null && !afterSalesRefundList.isEmpty()) {
                    for (AfterSalesRefund refund : afterSalesRefundList) {
                        Boolean payment = refund.isPayment();
                        if (payment == null || !payment) {
                            throw new StewardBusinessException("有未确认的退款项，无法转入完成状态。");
                        }
                    }
                }
                // 有退货的必须已确认收货
                List<AfterSalesRefundGoods> afterSalesRefundGoodsList = afterSales.getAfterSalesRefundGoodsList();
                if (afterSalesRefundGoodsList != null && !afterSalesRefundGoodsList.isEmpty()) {
                    for (AfterSalesRefundGoods goods : afterSalesRefundGoodsList) {
                        Boolean received = goods.isReceived();
                        if (received == null || !received) {
                            throw new StewardBusinessException("有未确认收货的退货项，无法转入完成状态。");
                        }
                    }
                }
                // 有发货订单的必须已签收订单
                Boolean send = afterSales.isSend();
                if (send != null && send) {
                    AfterSalesSend afterSalesSend = afterSales.getAfterSalesSend();
                    if (afterSalesSend == null) {
                        throw new StewardBusinessException("数据异常~未找到发货信息。");
                    }
                    Integer orderId = afterSalesSend.getOrderId();
                    Order order = generalDAO.get(Order.class, orderId);
                    if (order == null) {
                        throw new StewardBusinessException("数据异常~未找到发货订单。");
                    }
                    OrderStatus orderStatus = order.getStatus();
                    if (!OrderStatus.SIGNED.equals(orderStatus)) {
                        throw new StewardBusinessException("发货订单还未签收，无法转入完成状态。");
                    }
                }
                break;
            }
            default:
                throw new StewardBusinessException("审批失败 ~ 不合法的审批状态。");
        }
        // 更改订单状态
        afterSales.setStatusBefore(curStatus);
        afterSales.setStatus(status);
        if (remarks != null) {
            afterSales.setStatusRemark(remarks);
        }
        generalDAO.saveOrUpdate(afterSales);
        // 记录流程日志
        addFlowLog(afterSales);
    }


    /**
     * 添加流程日志
     *
     * @param afterSales
     */
    private void addFlowLog(AfterSales afterSales) {
        AfterSalesFlowLog flowLog = new AfterSalesFlowLog();
        Employee employee = SessionUtils.getEmployee();
        if (employee != null) {
            flowLog.setOperatorId(employee.getId());
            flowLog.setOperatorName(employee.getName());
        }
        flowLog.setAfterSalesId(afterSales.getId());
        flowLog.setRemark(afterSales.getStatusRemark());
        flowLog.setStatusBefore(afterSales.getStatusBefore());
        flowLog.setStatus(afterSales.getStatus());
        Date date = afterSales.getUpdateTime();
        flowLog.setCreateTime(date);
        flowLog.setUpdateTime(date);
        generalDAO.saveOrUpdate(flowLog);
    }

    /**
     * 确认退货
     *
     * @param returnGoodsVo 确认退货实体
     */
    public void confirmGoods(ReturnGoodsVo returnGoodsVo, boolean confirm) {
        Integer afterSalesItemId = returnGoodsVo.getAfterSalesItemId();
        String shippingComp = returnGoodsVo.getShippingComp();
        validEmpty(shippingComp, "必须指定退货快递公司。");
        String shippingNo = returnGoodsVo.getShippingNo();
        validEmpty(shippingNo, "必须填写退货快递单号。");
        AfterSalesItem afterSalesItem = generalDAO.get(AfterSalesItem.class, afterSalesItemId);
        if (afterSalesItem == null) {
            throw new StewardBusinessException("售后项不存在或已被删除。");
        }
        AfterSalesRefundGoods afterSalesRefundGoods = afterSalesItem.getAfterSalesRefundGoods();
        if (afterSalesRefundGoods == null) {
            throw new StewardBusinessException("该售后项未进行退货操作。");
        }
        afterSalesRefundGoods.setShippingNo(shippingNo);
        afterSalesRefundGoods.setShippingComp(shippingComp);
        if (confirm) {
            // 确认退货
            Boolean returned = afterSalesRefundGoods.isReturned();
            if (returned != null && returned) {
                throw new StewardBusinessException("该售后项已确认退货。");
            }
            afterSalesRefundGoods.setReturned(true);
        }
        generalDAO.saveOrUpdate(afterSalesRefundGoods);
    }

    /**
     * 确认收货
     *
     * @param receiveGoodsVo 确认收货实体
     */
    public void receiveGoods(ReceiveGoodsVo receiveGoodsVo, boolean confirm) {
        Integer afterSalesItemId = receiveGoodsVo.getAfterSalesItemId();
        Integer receivedCount = receiveGoodsVo.getReceivedCount();
        AfterSalesItem afterSalesItem = generalDAO.get(AfterSalesItem.class, afterSalesItemId);
        if (afterSalesItem == null) {
            throw new StewardBusinessException("售后项不存在或已被删除。");
        }
        AfterSalesRefundGoods afterSalesRefundGoods = afterSalesItem.getAfterSalesRefundGoods();
        if (afterSalesRefundGoods == null) {
            throw new StewardBusinessException("该售后项未进行退货操作。");
        }
        afterSalesRefundGoods.setReceivedCount(receivedCount);
        afterSalesRefundGoods.setPack(receiveGoodsVo.getPack());
        afterSalesRefundGoods.setFunc(receiveGoodsVo.getFunc());
        afterSalesRefundGoods.setFace(receiveGoodsVo.getFace());
        afterSalesRefundGoods.setRemark(receiveGoodsVo.getRemark());
        if (confirm) {
            // 确认收货
            if (receivedCount == null) {
                throw new StewardBusinessException("请填写收到的退货数量。");
            }
            Boolean received = afterSalesRefundGoods.isReceived();
            if (received != null && received) {
                throw new StewardBusinessException("该售后项已确认收货。");
            }
            afterSalesRefundGoods.setReceived(true);
        }
        generalDAO.saveOrUpdate(afterSalesRefundGoods);
    }

    /**
     * 删除退款分配信息
     *
     * @param refundId
     */
    private void deleteRefundAlloc(Integer refundId) {
        Search search = new Search(RefundAlloc.class);
        search.addFilterEqual("refundId", refundId);
        List refundAllocList = generalDAO.search(search);
        if (refundAllocList != null && !refundAllocList.isEmpty()) {
            generalDAO.remove(refundAllocList.toArray());
        }
    }


    /**
     * 作废售后工单
     *
     * @param afterSales
     */
    private void cancelAfterSales(AfterSales afterSales) {
        AfterSalesStatus status = afterSales.getStatus();
        if (AfterSalesStatus.FINISH.equals(status)) {
            throw new StewardBusinessException("售后单已结束无法再进行作废操作。");
        }
        // 回滚发货单记录
        Boolean buildOrder = afterSales.isBuildOrder();
        if (buildOrder != null && buildOrder) {
            AfterSalesSend afterSalesSend = afterSales.getAfterSalesSend();
            if (afterSalesSend != null) {
                // 作废掉订单
                orderService.orderCancellation(new Integer[]{afterSalesSend.getOrderId()});
                afterSalesSend.setOrderId(null);
                afterSales.setBuildOrder(false);
            }
        }
        // 回滚退款记录
        List<AfterSalesRefund> afterSalesRefundList = afterSales.getAfterSalesRefundList();
        if (afterSalesRefundList != null && !afterSalesRefundList.isEmpty()) {
            for (AfterSalesRefund refund : afterSalesRefundList) {
                // 回滚线上退款记录
                returnPayAfterSalesRefund(refund.getId());
            }
        }
    }

    /**
     * 生成发货订单
     *
     * @param id
     */
    public void buildOrder(Integer id) {
        if (id == null) {
            throw new StewardBusinessException("必须指定售后工单ID。");
        }
        AfterSales afterSales = generalDAO.get(AfterSales.class, id);
        if (afterSales == null) {
            throw new StewardBusinessException("售后工单不存在或已被删除。");
        }
        Boolean send = afterSales.isSend();
        if (send == null || !send) {
            throw new StewardBusinessException("该售后单已配置为不生成发货订单。");
        }
        AfterSalesStatus status = afterSales.getStatus();
        if (status == null || !AfterSalesStatus.ACCEPT.equals(status)) {
            throw new StewardBusinessException("售后状态为“" + (status != null ? status.getValue() : "N/A") + "”，不能生成发货订单操作。");
        }
        List<AfterSalesPatch> afterSalesPatchList = afterSales.getAfterSalesPatchList();
        if (afterSalesPatchList == null || afterSalesPatchList.isEmpty()) {
            throw new StewardBusinessException("当前售后单无需发货。");
        }
        // 验证发货信息
        AfterSalesSend afterSalesSend = afterSales.getAfterSalesSend();
        if (afterSalesSend == null) {
            throw new StewardBusinessException("当前售后单没有配置发货信息。");
        }
        String receiverPhone = afterSalesSend.getReceiverPhone();
        String receiverMobile = afterSalesSend.getReceiverMobile();
        if (StringUtils.isBlank(receiverPhone) && StringUtils.isBlank(receiverMobile)) {
            throw new StewardBusinessException("发货信息异常，收货电话和手机至少填写一个。");
        }
        Order afterSalesOrder = afterSales.getOrder();
        if (afterSalesOrder == null) {
            throw new StewardBusinessException("数据异常，售后工单对应的订单不存在或已被删除。");
        }
        // 创建发货信息表
        Invoice invoice = new Invoice();
        Receiver receiver = new Receiver();
        invoice.setReceiver(receiver);
        invoice.setShippingComp(validEmpty(afterSalesSend.getShippingComp(), "发货信息异常，未填写快递公司"));
        receiver.setReceiverState(validEmpty(afterSalesSend.getReceiverState(), "发货信息异常，未填写收货省份"));
        receiver.setReceiverCity(validEmpty(afterSalesSend.getReceiverCity(), "发货信息异常，未填写收货城市"));
        receiver.setReceiverDistrict(validEmpty(afterSalesSend.getReceiverDistrict(), "发货信息异常，未填写收货地区"));
        receiver.setReceiverAddress(validEmpty(afterSalesSend.getReceiverAddress(), "发货信息异常，未填写收货详细地址"));
        receiver.setReceiverZip(validEmpty(afterSalesSend.getReceiverZip(), "发货信息异常，未填写收货邮政编码"));
        receiver.setReceiverName(validEmpty(afterSalesSend.getReceiverName(), "发货信息异常，未填写收件人"));
        generalDAO.saveOrUpdate(invoice);
        // 生成发货订单
        List<OrderItem> orderItemList = new LinkedList<OrderItem>();
        OrderType orderType = OrderType.REPLENISHMENT;
        PlatformType platformType = afterSalesOrder.getPlatformType();
        for (AfterSalesPatch patch : afterSalesPatchList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPlatformType(platformType);
            orderItem.setProductId(patch.getProductId());
            Product product = patch.getProduct();
            orderItem.setProductCode(product.getProductNo());
            orderItem.setProductSku(product.getSku());
            orderItem.setProductName(product.getName());
            orderItem.setPriceDescription(null);
            orderItem.setSourceItemId(null);
            orderItem.setStatus(OrderItemStatus.NOT_SIGNED);
            orderItem.setBuyCount(patch.getCount());
            orderItem.setSpecInfo(OrderUtil.getSpecInfo(product));
            orderItem.setValid(true);
            OrderItemType orderItemType = patch.getOrderItemType();
            if (OrderItemType.EXCHANGE_AFTERSALE.equals(orderItemType)) {
                orderType = OrderType.EXCHANGE; // 有一个是换货订单则整个订单就是换货类型
            }
            orderItem.setType(orderItemType);
            orderItem.setReturnStatus(OrderItemReturnStatus.NORMAL);
            orderItem.setOfflineReturnStatus(OrderItemReturnStatus.NORMAL);
            orderItemList.add(orderItem);
        }
        Order order = new Order();
        order.setOrderNo(SequenceGenerator.getInstance().getNextOrderNo());
        order.setStatus(OrderStatus.WAIT_APPROVE);
        order.setOrderReturnStatus(OrderReturnStatus.NORMAL);
        order.setGenerateType(OrderGenerateType.MANUAL_CREATE);
        orderFeeService.calculateOrderFee(order, orderItemList); // 计算订单金额
        order.setBuyerId(afterSalesOrder.getBuyerId());
        order.setBuyerAlipayNo(afterSalesOrder.getBuyerAlipayNo());
        order.setBuyerMessage(afterSalesOrder.getBuyerMessage());
        String receiverRemark = afterSalesSend.getReceiverRemark();
        if (StringUtils.isNotBlank(receiverRemark)) {
            order.setRemark("该订单由售后工单生成，售后备注：" + receiverRemark);
        }
        order.setRepoId(afterSalesOrder.getRepoId()); // 库房与原订单保持一致
        Date curTime = new Date();
        order.setBuyTime(curTime);
        order.setPayTime(curTime);
        order.setShopId(afterSalesOrder.getShopId());
        order.setNeedReceipt(false); // 暂时不考虑是否要发票
        order.setPlatformType(platformType);
        order.setInvoiceId(invoice.getId());
        order.setValid(true);
        order.setType(orderType);
        generalDAO.saveOrUpdate(order);
        for (OrderItem item : orderItemList) {
            item.setOrderId(order.getId());
            generalDAO.saveOrUpdate(item);
        }
        // 更新售后工单生成发货订单状态
        afterSalesSend.setOrderId(order.getId());
        generalDAO.saveOrUpdate(afterSalesSend);
        afterSales.setBuildOrder(true);
        generalDAO.saveOrUpdate(afterSales);
    }

    /**
     * 根据订单ID查询售后单
     *
     * @param orderId 订单id
     * @return
     * @Author codec.yang
     */
    @Transactional(readOnly = true)
    public List<AfterSales> findAfterSalesByOrderId(Integer orderId) {
        if (NumberUtil.isNullOrLessThanOne(orderId)) {
            return Collections.EMPTY_LIST;
        }
        Search search = new Search(AfterSales.class);
        search.addFilterEqual("orderId", orderId);

        return this.generalDAO.search(search);
    }

    /**
     * 根据平台类型和平台订单编号查询售后单
     *
     * @param platformType
     * @param platformOrderNo
     * @return
     * @Author codec.yang
     */
    public List<AfterSales> findAfterSalesByPlatformTypeAndOrderNo(PlatformType platformType, String platformOrderNo) {
        Search search = new Search(AfterSales.class);
        search.addFilterEqual("platformType", platformType);
        search.addFilterEqual("platformOrderNo", platformOrderNo);

        return this.generalDAO.search(search);
    }

    /**
     * 根据售后单编号查询售后单
     *
     * @param code
     * @return
     * @Author codec.yang
     */
    public AfterSales getAfterSalesByCode(String code) {
        Search search = new Search(AfterSales.class);
        search.addFilterEqual("code", code);
        return (AfterSales) this.generalDAO.searchUnique(search);

    }

    @Transactional
    public void saveAfterSalesRefund(AfterSalesRefund afterSalesRefund) {
        generalDAO.saveOrUpdate(afterSalesRefund);
    }

    @Transactional
    public void saveAfterSalesRefundAlloc(AfterSalesRefundAlloc afterSalesRefundAlloc) {
        generalDAO.saveOrUpdate(afterSalesRefundAlloc);
    }

    @Transactional
    public AfterSalesRefundAlloc findAfterSalesRefundAllocById(Integer id) {
        return generalDAO.get(AfterSalesRefundAlloc.class, id);
    }

    @Transactional
    public AfterSalesRefund findAfterSalesRefundById(Integer id) {
        return generalDAO.get(AfterSalesRefund.class, id);
    }

    /**
     * 确认支付功能
     */
    @Transactional
    public void onPayAfterSalesRefund(Map<String, Object[]> map) {
        Map<String, String> targetMap = new HashMap<String, String>();
        AfterSaleUtil.getConditionMap(map, targetMap);
        AfterSalesRefund afterSalesRefund = findAfterSalesRefundById(Integer.parseInt(targetMap.get("afterSalesRefundId")));
        List<AfterSalesRefundAlloc> afterSalesRefundAllocs = afterSalesRefund.getAfterSalesRefundAllocList();
        if (afterSalesRefund == null) {
            throw new StewardBusinessException("找不到对应的退款记录");
        }
        if (afterSalesRefund.isPayment()!=null&&afterSalesRefund.isPayment()) {
            throw new StewardBusinessException("已支付的退款不能再次退款");
        }
        RefundMethod refundMethod = RefundMethod.valueOf(targetMap.get("refundMethod").trim());
        afterSalesRefund.setRefundMethod(refundMethod);
        if (refundMethod.equals(RefundMethod.ALIPAY)) {
            afterSalesRefund.setAlipayNo(targetMap.get("alipayNo").trim());
        } else {
            afterSalesRefund.setBank(targetMap.get("bank").trim());
            afterSalesRefund.setBankAccout(targetMap.get("bankAccout").trim());
            afterSalesRefund.setBankUser(targetMap.get("bankUser").trim());
        }
        afterSalesRefund.setPayment(true);

        if (afterSalesRefund.getOnlineFee().getAmount() > 0) {
            Refund refund = refundService.getById(afterSalesRefund.getOnlineRefundId());
            if (refund == null) {
                throw new StewardBusinessException("没有找到对应的线上退款记录");
            }
            for (AfterSalesRefundAlloc afterSalesRefundAlloc : afterSalesRefundAllocs) {
                RefundAlloc refundAlloc = getRefundAlloc(afterSalesRefundAlloc, refund, afterSalesRefund);
                refundService.saveRefundAlloc(refundAlloc);
            }
        } else {
            List<RefundAlloc> refundAllocs = new ArrayList<RefundAlloc>();
            Order order = afterSalesRefund.getAfterSales().getOrder();
            Refund refund = new Refund();
            for (AfterSalesRefundAlloc afterSalesRefundAlloc : afterSalesRefundAllocs) {
                refundAllocs.add(getRefundAlloc(afterSalesRefundAlloc, null, afterSalesRefund));
            }
            //todo:退款插入数据需要重新考虑
            refund.setRefundTime(new Date());
            Employee employee = SessionUtils.getEmployee();
            refund.setOperatorId(employee.getId());
            refund.setBuyerAlipayNo(afterSalesRefund.getAlipayNo());
            refund.setOnline(false);
            refund.setBuyerId(order.getBuyerId());
            refund.setPlatformType(order.getPlatformType());
            refund.setRefundFee(afterSalesRefund.getFee());
            refund.setPostFee(Money.valueOf(0));
            refund.setPhase(RefundPhase.AFTER_SALE);
            refund.setType(RefundType.ORDER);
            refund.setOrderItemId(afterSalesRefund.getAfterSalesItem().getOrderItemId());
            //todo:暂时不要修改订单数据金额
//            refundService.saveRefund(afterSalesRefund.getAfterSalesItem().getOrderItemId(),refund);
            generalDAO.saveOrUpdate(refund);
            refundService.saveRefundAlloc(refundAllocs);
        }
        saveAfterSalesRefund(afterSalesRefund);
    }

    private RefundAlloc getRefundAlloc(AfterSalesRefundAlloc afterSalesRefundAlloc, Refund refund, AfterSalesRefund afterSalesRefund) {
        RefundAlloc refundAlloc = new RefundAlloc();
        refundAlloc.setSupplierFee(afterSalesRefundAlloc.getSupplierFee());
        Employee employee = SessionUtils.getEmployee();
        refundAlloc.setOperatorId(employee.getId());
        refundAlloc.setPlatformFee(afterSalesRefundAlloc.getPlatformFee());
        refundAlloc.setFee(afterSalesRefundAlloc.getFee());
        refundAlloc.setOnline(afterSalesRefundAlloc.isOnline());
        refundAlloc.setRefundId(refund != null ? refund.getId() : afterSalesRefund.getOnlineRefundId());
        refundAlloc.setRefundTime(refund != null ? refund.getRefundTime() : new Date());
        refundAlloc.setType(afterSalesRefundAlloc.getType());
        return refundAlloc;
    }
//    /**
//     * 确认支付功能
//     *
//     *
//     */
//    @Transactional
//    public void onPayAfterSalesRefund(Map<String,Object[]> map) {


//        Map<String, String> targetMap = new HashMap<String, String>();
//        AfterSaleUtil.getConditionMap(map, targetMap);
//        //退款的线上总金额
//        Money onlineFee = Money.valueOf(0);
//        //退款的线下总金额
//        Money offlineFee = Money.valueOf(0);
//        AfterSalesRefund afterSalesRefund = findAfterSalesRefundById(id);
//        if (afterSalesRefund == null) {
//            throw new StewardBusinessException("找不到对应的退款记录");
//        }
//        if (afterSalesRefund.isPayment()) {
//            throw new StewardBusinessException("已支付的退款不能再次退款");
//        }
//        afterSalesRefund.setPayment(true);
//        saveAfterSalesRefund(afterSalesRefund);

//        //更新退款分配
//        //更新货款
//        AfterSalesRefundAlloc goodsTypeAfterSalesRefundAlloc = findAfterSalesRefundAllocById(Integer.parseInt(targetMap.get("goodsTypeId")));
//        if (goodsTypeAfterSalesRefundAlloc == null) {
//            throw new StewardBusinessException("没找到对应的退款分配记录");
//        }
//        getAfterSalesRefundAlloc(goodsTypeAfterSalesRefundAlloc, Boolean.valueOf(targetMap.get("goodsTypeOnline")), Money.valueOf(targetMap.get("goodsTypeFee")), Money.valueOf(targetMap.get("goodsTypePlatformFee")), Money.valueOf(targetMap.get("goodsTypeSupplierFee")));
//        calculateFee(onlineFee, offlineFee, Boolean.valueOf(targetMap.get("goodsTypeOnline")), Money.valueOf(targetMap.get("goodsTypeFee")), Money.valueOf(targetMap.get("goodsTypePlatformFee")), Money.valueOf(targetMap.get("goodsTypeSupplierFee")));
//        saveAfterSalesRefundAlloc(goodsTypeAfterSalesRefundAlloc);
//        //更新运费
//        AfterSalesRefundAlloc postTypeAfterSalesRefundAlloc = findAfterSalesRefundAllocById(Integer.parseInt(targetMap.get("postTypeId")));
//        if (postTypeAfterSalesRefundAlloc == null) {
//            throw new StewardBusinessException("没找到对应的退款分配记录");
//        }
//        getAfterSalesRefundAlloc(postTypeAfterSalesRefundAlloc, Boolean.valueOf(targetMap.get("postTypeOnline")), Money.valueOf(targetMap.get("postTypeFee")), Money.valueOf(targetMap.get("postTypePlatformFee")), Money.valueOf(targetMap.get("postTypeSupplierFee")));
//        calculateFee(onlineFee, offlineFee, Boolean.valueOf(targetMap.get("postTypeOnline")), Money.valueOf(targetMap.get("postTypeFee")), Money.valueOf(targetMap.get("postTypePlatformFee")), Money.valueOf(targetMap.get("postTypeSupplierFee")));
//        saveAfterSalesRefundAlloc(postTypeAfterSalesRefundAlloc);
//        // 更新退款运费
//        AfterSalesRefundAlloc refundPostTypeAfterSalesRefundAlloc = findAfterSalesRefundAllocById(Integer.parseInt(targetMap.get("refundPostTypeId")));
//        if (refundPostTypeAfterSalesRefundAlloc == null) {
//            throw new StewardBusinessException("没找到对应的退款分配记录");
//        }
//        getAfterSalesRefundAlloc(refundPostTypeAfterSalesRefundAlloc, Boolean.valueOf(targetMap.get("refundPostTypeOnline")), Money.valueOf(targetMap.get("refundPostTypeFee")), Money.valueOf(targetMap.get("refundPostTypePlatformFee")), Money.valueOf(targetMap.get("refundPostTypeSupplierFee")));
//        calculateFee(onlineFee, offlineFee, Boolean.valueOf(targetMap.get("refundPostTypeOnline")), Money.valueOf(targetMap.get("refundPostTypeFee")), Money.valueOf(targetMap.get("refundPostTypePlatformFee")), Money.valueOf(targetMap.get("refundPostTypeSupplierFee")));
//        saveAfterSalesRefundAlloc(refundPostTypeAfterSalesRefundAlloc);
//        //更新退款
////        AfterSalesRefund afterSalesRefund = findAfterSalesRefundById(Integer.parseInt(targetMap.get("afterSalesRefundId")));
//        RefundMethod refundMethod = RefundMethod.valueOf(targetMap.get("refundMethod").trim());
//        afterSalesRefund.setRefundMethod(refundMethod);
//        if (refundMethod.equals(RefundMethod.ALIPAY)) {
//            afterSalesRefund.setAlipayNo(targetMap.get("alipayNo").trim());
//        } else {
//            afterSalesRefund.setBank(targetMap.get("bank").trim());
//            afterSalesRefund.setBankAccout(targetMap.get("bankAccout").trim());
//            afterSalesRefund.setBankUser(targetMap.get("bankUser").trim());
//        }
//        afterSalesRefund.setOnlineFee(onlineFee);
//        afterSalesRefund.setOfflineFee(offlineFee);
//        afterSalesRefund.setPayment(true);
//        saveAfterSalesRefund(afterSalesRefund);


//    }

    /**
     * 取消支付
     */
    @Transactional
    public void returnPayAfterSalesRefund(Integer id) {
        if (log.isInfoEnabled()) {
            log.info(String.format("参数id[%s]", id));
        }
        AfterSalesRefund afterSalesRefund = getAfterSalesRefund(id);
        Refund refund = null;
        if (afterSalesRefund.getOnlineRefundId() == null) {
            refund = refundService.getById(afterSalesRefund.getOnlineRefundId());
            for (RefundAlloc refundAlloc : refund.getRefundAllocList()) {
                generalDAO.remove(refundAlloc);
            }
            generalDAO.remove(refund);
        } else {
            refund = refundService.getById(afterSalesRefund.getOfflineRefundId());
            for (RefundAlloc refundAlloc : refund.getRefundAllocList()) {
                generalDAO.remove(refundAlloc);
            }
        }
        afterSalesRefund.setPayment(false);
        generalDAO.saveOrUpdate(afterSalesRefund);
        //todo:对订单进行金额操作

    }
//
//    //获取前端的退款分配数据
//    private AfterSalesRefundAlloc getAfterSalesRefundAlloc(AfterSalesRefundAlloc afterSalesRefundAlloc, Boolean online, Money fee, Money platformFee, Money supplierFee) {
//        afterSalesRefundAlloc.setOnline(online);
//        afterSalesRefundAlloc.setFee(fee);
//        //获取当前登录用户，即操作人
//        Employee employee = SessionUtils.getEmployee();
//        afterSalesRefundAlloc.setOperatorId(employee.getId());
//        afterSalesRefundAlloc.setPlatformFee(platformFee);
//        afterSalesRefundAlloc.setSupplierFee(supplierFee);
//        return afterSalesRefundAlloc;
//    }
//
//    //计算退款的线上和线下总金额
//    private void calculateFee(Money onlineFee, Money offlineFee, Boolean online, Money fee, Money platformFee, Money supplierFee) {
//        if (online) {
//            onlineFee = onlineFee.add(fee).add(platformFee).add(supplierFee);
//        } else {
//            offlineFee = offlineFee.add(fee).add(platformFee).add(supplierFee);
//        }
//    }


    /**
     * 根据回访单id查询售后单
     *
     * @param returnVisitId
     * @return
     * @Author codec.yang
     */
    public List<AfterSales> findAfterSalesByReturnVisitId(Integer returnVisitId) {
        if (NumberUtil.isNullOrLessThanOne(returnVisitId)) {
            return Collections.emptyList();
        }
        Search search = new Search(AfterSales.class);
        search.addFilterEqual("revisitId", returnVisitId);

        return this.generalDAO.search(search);
    }

    public AfterSalesRefund getAfterSalesRefund(Integer id) {
        return generalDAO.get(AfterSalesRefund.class, id);

    }
}
