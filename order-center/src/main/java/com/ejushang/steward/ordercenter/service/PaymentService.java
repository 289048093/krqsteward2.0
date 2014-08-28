package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Filter;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.common.util.PoiUtil;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.domain.OrderItem;
import com.ejushang.steward.ordercenter.domain.Payment;
import com.ejushang.steward.ordercenter.domain.PaymentAllocation;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.util.OrderUtil;
import com.ejushang.steward.ordercenter.util.PaymentSearchCondition;
import com.ejushang.steward.ordercenter.vo.OrderItemVo;
import com.ejushang.steward.ordercenter.vo.PaymentOrderItemVo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-5-8
 * Time: 上午11:12
 * To change this template use File | Settings | File Templates.
 */

@Service
@Transactional
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private GeneralDAO generalDAO;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderFeeService orderFeeService;

    @Autowired
    private MemberInfoLogService memberInfoLogService;

    public final static String DISTRIBUTION_SUCCESS = "分配成功";

    /**
     * 根据条件查询一个预收款
     */
    @Transactional(readOnly = true)
    public List<Payment> findByKey(PaymentSearchCondition paymentSearchCondition, Page page) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的findByKey方法");
        }
        Search search = new Search(Payment.class).addPagination(page);

        //增加查询条件
        addSearchCondition(paymentSearchCondition, search);

        return generalDAO.search(search);
    }

    private void addSearchCondition(PaymentSearchCondition paymentSearchCondition, Search search) {
        if (!StringUtils.isBlank(paymentSearchCondition.getType()) && !paymentSearchCondition.getType().equals("null")) {
            search.addFilterEqual("type", PaymentType.valueOf(paymentSearchCondition.getType()));
        }
        if (!StringUtils.isBlank(paymentSearchCondition.getAllocateStatus()) && !paymentSearchCondition.getAllocateStatus().equals("null")) {
            search.addFilterEqual("allocateStatus", PaymentAllocateStatus.valueOf(paymentSearchCondition.getAllocateStatus()));
        }
        if (!StringUtils.isBlank(paymentSearchCondition.getPlatformType()) && !paymentSearchCondition.getPlatformType().equals("null")) {
            search.addFilterEqual("platformType", PlatformType.valueOf(paymentSearchCondition.getPlatformType()));
        }
        if (paymentSearchCondition.getStartDate() != null) {
            if (paymentSearchCondition.getDateType().equals("payDate")) {
                search.addFilterGreaterOrEqual("payTime", paymentSearchCondition.getStartDate());
            }
            if (paymentSearchCondition.getDateType().equals("orderDate")) {
                search.addFilterGreaterOrEqual("buyTime", paymentSearchCondition.getStartDate());
            }
            if (paymentSearchCondition.getDateType().equals("createDate")) {
                search.addFilterGreaterOrEqual("createTime", paymentSearchCondition.getStartDate());
            }
        }
        if (paymentSearchCondition.getEndDate() != null) {
            if (paymentSearchCondition.getDateType().equals("payDate")) {
                search.addFilterLessOrEqual("payTime", paymentSearchCondition.getEndDate());
            }
            if (paymentSearchCondition.getDateType().equals("orderDate")) {
                search.addFilterLessOrEqual("buyTime", paymentSearchCondition.getEndDate());
            }
            if (paymentSearchCondition.getDateType().equals("createDate")) {
                search.addFilterLessOrEqual("createTime", paymentSearchCondition.getEndDate());
            }
        }
        if (!NumberUtil.isNullOrZero(paymentSearchCondition.getShopId())) {
            search.addFilterEqual("shopId", paymentSearchCondition.getShopId());
        }
        if (!StringUtils.isBlank(paymentSearchCondition.getConditionValue())) {
            if (!StringUtils.isBlank(paymentSearchCondition.getConditionQuery())) {
                if (paymentSearchCondition.getConditionType().equals("has")) {
                    search.addFilterLike(paymentSearchCondition.getConditionQuery(), "%" + paymentSearchCondition.getConditionValue() + "%");
                }
                if (paymentSearchCondition.getConditionType().equals("!")) {
                    search.addFilterNot(Filter.like(paymentSearchCondition.getConditionQuery(), "%" + paymentSearchCondition.getConditionValue() + "%"));
                }
                if (paymentSearchCondition.getConditionType().equals("=")) {
                    search.addFilterEqual(paymentSearchCondition.getConditionQuery(), paymentSearchCondition.getConditionValue());
                }
                if (paymentSearchCondition.getConditionType().equals("!=")) {
                    search.addFilterNotEqual(paymentSearchCondition.getConditionQuery(), paymentSearchCondition.getConditionValue());
                }
                if (paymentSearchCondition.getConditionType().equals(">=")) {
                    search.addFilterGreaterOrEqual(paymentSearchCondition.getConditionQuery(), paymentSearchCondition.getConditionValue());
                }
                if (paymentSearchCondition.getConditionType().equals("<=")) {
                    search.addFilterLessOrEqual(paymentSearchCondition.getConditionQuery(), paymentSearchCondition.getConditionValue());
                }
            }
        }
        search.addSortDesc("createTime");
    }

    /**
     * 根据预收款ID查询对应的分配明细
     *
     * @param id 预收款id
     */
    @Transactional(readOnly = true)
    private List<PaymentAllocation> findPAByPaymentId(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的findPAByPaymentId方法,参数预收款id:[{}]", id);
        }
        Search search = new Search(PaymentAllocation.class);
        if (!NumberUtil.isNullOrZero(id)) {
            search.addFilterEqual("paymentId", id);
        }
        return generalDAO.search(search);
    }

    /**
     * 分配预收款
     *
     * @param id         预收款id
     * @param orderItems 所要分配的订单项
     * @param fees       预收款分配金额
     * @param refundFees 预收款退款分配金额
     */
    public void distributionPayment(Integer id, List<OrderItem> orderItems, List<Money> fees, List<Money> refundFees) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的distributionPayment方法,分配的预收款id:[{}]", id);
        }
        Payment payment = generalDAO.get(Payment.class, id);
        List<PaymentAllocation> PAsByDelete = findDeletePAs(id, orderItems);
        updateDeleteOrderItems(payment, PAsByDelete);
        deletePAs(PAsByDelete);
        if (orderItems != null) {
            isFeeLegal(payment, fees, refundFees);
            updateOrderItems(payment, orderItems, fees, refundFees);
            List<PaymentAllocation> paymentAllocations = new ArrayList<PaymentAllocation>();
            updatePA(payment, orderItems, fees, refundFees, paymentAllocations);
            updatePaymentStatus(payment);
        } else {
            payment.setAllocateStatus(PaymentAllocateStatus.WAIT_ALLOCATE);
            generalDAO.saveOrUpdate(payment);
        }


    }

    /**
     * 更新要被删除分配的订单项金额
     *
     * @param payment            预收款
     * @param paymentAllocations 预收款分配明细
     */
    private void updateDeleteOrderItems(Payment payment, List<PaymentAllocation> paymentAllocations) {
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        for (int i = 0; i < paymentAllocations.size(); i++) {
            PaymentAllocation paymentAllocation = paymentAllocations.get(i);
            OrderItem orderItem = orderService.findOrderItemById(paymentAllocation.getOrderItemId());
            if (payment.getType().equals(PaymentType.POST_COVER)) {
                if (paymentAllocation.getPaymentFee() != null) {
                    orderItem.setPostCoverFee(orderItem.getPostCoverFee().subtract(paymentAllocation.getPaymentFee()));
                }
                if (paymentAllocation.getRefundFee() != null) {
                    orderItem.setPostCoverRefundFee(orderItem.getPostCoverRefundFee().subtract(paymentAllocation.getRefundFee()));
                }
            }
            if (payment.getType().equals(PaymentType.SERVICE_COVER)) {
                if (paymentAllocation.getPaymentFee() != null) {
                    orderItem.setServiceCoverFee(orderItem.getServiceCoverFee().subtract(paymentAllocation.getPaymentFee()));
                }
                if (paymentAllocation.getRefundFee() != null) {
                    orderItem.setServiceCoverRefundFee(orderItem.getServiceCoverRefundFee().subtract(paymentAllocation.getRefundFee()));
                }
            }
            if (payment.getType().equals(PaymentType.ORDER_POST_FEE)) {
                if (paymentAllocation.getPaymentFee() != null) {
                    orderItem.setSharedPostFee(orderItem.getSharedPostFee().subtract(paymentAllocation.getPaymentFee()));
                }
            }
            orderFeeService.calculateOrderItemFee(orderItem);
            updateOrderFee(orderItem);
            orderItemList.add(orderItem);
        }
        generalDAO.saveOrUpdate(orderItemList);
    }

    /**
     * 删除指定订单项明细
     *
     * @param paymentAllocations 预收款分配明细
     */
    public void deletePAs(List<PaymentAllocation> paymentAllocations) {
        for (int i = 0; i < paymentAllocations.size(); i++) {
            generalDAO.remove(paymentAllocations.get(i));
        }
    }

    /**
     * 查找所要删除的预收款明细
     *
     * @param id         预收款id
     * @param orderItems 同一预收款下的其他订单项
     */
    public List<PaymentAllocation> findDeletePAs(Integer id, List<OrderItem> orderItems) {
        List<PaymentAllocation> paymentAllocations = findPAByPaymentId(id);
        if (orderItems != null) {
            for (int i = 0; i < orderItems.size(); i++) {
                PaymentAllocation paymentAllocation = getUniquePA(id, orderItems.get(i).getId());
                if (paymentAllocations.contains(paymentAllocation)) {
                    paymentAllocations.remove(paymentAllocation);
                }
            }
        }
        return paymentAllocations;
    }

    /**
     * 修改预收款状态为已分配
     *
     * @param payment 预收款信息
     */
    private void updatePaymentStatus(Payment payment) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的updatePaymentStatus方法,参数为预收款[id={}]", payment.getId());
        }
        payment.setAllocateStatus(PaymentAllocateStatus.ALLOCATED);
        generalDAO.saveOrUpdate(payment);
    }

    /**
     * 更新预收款明细
     *
     * @param payment    预收款信息
     * @param orderItems 所要分配的订单项
     * @param fees       预收款分配金额
     * @param refundFees 预收款退款分配金额
     */
    private void updatePA(Payment payment, List<OrderItem> orderItems, List<Money> fees, List<Money> refundFees, List<PaymentAllocation> paymentAllocations) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的updatePA方法,所更新的预收款[id={}]", payment.getId());
        }
        for (int i = 0; i < orderItems.size(); i++) {
            PaymentAllocation paymentAllocation = getUniquePA(payment.getId(), orderItems.get(i).getId());
            if (paymentAllocation == null) {
                paymentAllocation = new PaymentAllocation();
                paymentAllocation.setPaymentId(payment.getId());
                paymentAllocation.setOrderItemId(orderItems.get(i).getId());
                paymentAllocation.setPaymentFee(fees.get(i));
                paymentAllocation.setRefundFee(refundFees.get(i));
                paymentAllocations.add(paymentAllocation);
            } else {
                paymentAllocationFeeInit(paymentAllocation);
                paymentAllocation.setPaymentFee(fees.get(i));
                paymentAllocation.setRefundFee(refundFees.get(i));
                paymentAllocations.add(paymentAllocation);
            }
        }
        generalDAO.saveOrUpdate(paymentAllocations);
    }

    /**
     * 初始化预收款金额
     *
     * @param paymentAllocation 预收款明细信息
     */
    private void paymentAllocationFeeInit(PaymentAllocation paymentAllocation) {
        if (paymentAllocation.getPaymentFee() == null) {
            paymentAllocation.setPaymentFee(Money.valueOf(0));
        }
        if (paymentAllocation.getRefundFee() == null) {
            paymentAllocation.setRefundFee(Money.valueOf(0));
        }
    }

    /**
     * 更新订单项金额
     *
     * @param payment    预收款信息
     * @param orderItems 所要分配的订单项
     * @param fees       预收款分配金额
     * @param refundFees 预收款退款分配金额
     */
    private void updateOrderItems(Payment payment, List<OrderItem> orderItems, List<Money> fees, List<Money> refundFees) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的updateOrderItems方法,参数：预收款[id={}]", payment.getId());
        }
        if (payment.getType().equals(PaymentType.POST_COVER)) {
            for (int i = 0; i < orderItems.size(); i++) {
                OrderItem orderItem = orderItems.get(i);
                PaymentAllocation paymentAllocation = getUniquePA(payment.getId(), orderItems.get(i).getId());
                if (paymentAllocation != null) {
                    Money postConverFee = orderItems.get(i).getPostCoverFee().subtract(paymentAllocation.getPaymentFee()).add(fees.get(i));
                    orderItems.get(i).setPostCoverFee(orderItems.get(i).getPostCoverFee().subtract(paymentAllocation.getPaymentFee()).add(fees.get(i)));
                    orderItems.get(i).setPostCoverRefundFee(orderItems.get(i).getPostCoverRefundFee().subtract(paymentAllocation.getRefundFee()).add(refundFees.get(i)));
                } else {
                    orderItems.get(i).setPostCoverFee(orderItems.get(i).getPostCoverFee().add(fees.get(i)));
                    orderItems.get(i).setPostCoverRefundFee(orderItems.get(i).getPostCoverRefundFee().add(refundFees.get(i)));
                }
                orderFeeService.calculateOrderItemFee(orderItems.get(i));
                updateOrderFee(orderItems.get(i));

            }
        }
        if (payment.getType().equals(PaymentType.SERVICE_COVER)) {
            for (int i = 0; i < orderItems.size(); i++) {
                PaymentAllocation paymentAllocation = getUniquePA(payment.getId(), orderItems.get(i).getId());
                if (paymentAllocation != null) {
                    orderItems.get(i).setServiceCoverFee(orderItems.get(i).getServiceCoverFee().subtract(paymentAllocation.getPaymentFee()).add(fees.get(i)));
                    orderItems.get(i).setServiceCoverRefundFee(orderItems.get(i).getServiceCoverRefundFee().subtract(paymentAllocation.getRefundFee()).add(refundFees.get(i)));
                } else {
                    orderItems.get(i).setServiceCoverFee(orderItems.get(i).getServiceCoverFee().add(fees.get(i)));
                    orderItems.get(i).setServiceCoverRefundFee(orderItems.get(i).getServiceCoverRefundFee().add(refundFees.get(i)));

                }
                orderFeeService.calculateOrderItemFee(orderItems.get(i));
                updateOrderFee(orderItems.get(i));
            }
        }
        if (payment.getType().equals(PaymentType.ORDER_POST_FEE)) {
            for (int i = 0; i < orderItems.size(); i++) {
                PaymentAllocation paymentAllocation = getUniquePA(payment.getId(), orderItems.get(i).getId());
                if (paymentAllocation != null) {
                    orderItems.get(i).setSharedPostFee(orderItems.get(i).getSharedPostFee().subtract(paymentAllocation.getPaymentFee()).add(fees.get(i)));

                    //   orderItems.get(i).setServiceCoverRefundFee(orderItems.get(i).getServiceCoverRefundFee().add(refundFees.get(i)));
                } else {
                    orderItems.get(i).setSharedPostFee(orderItems.get(i).getSharedPostFee().add(fees.get(i)));
                    //   orderItems.get(i).setSharedDiscountFee(orderItems.get(i).getSharedPostFee().subtract(paymentAllocation.getRefundFee()).add(refundFees.get(i)));
                }
                orderFeeService.calculateOrderItemFee(orderItems.get(i));
                updateOrderFee(orderItems.get(i));
            }
        }
        orderService.saveOrderItems(orderItems);
    }

    /**
     * 判断所分配的预收款总额是否合法
     *
     * @param payment    预收款信息
     * @param fees       预收款分配金额
     * @param refundFees 预收款退款分配金额
     */
    private boolean isFeeLegal(Payment payment, List<Money> fees, List<Money> refundFees) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的isFeeLegal方法");
        }
        Money feeCount = Money.valueOf(0);
        Money refundFeeCount = Money.valueOf(0);
//        List<PaymentAllocation> paymentAllocations = findPAByPaymentId(payment.getId());
//        for (int k = 0; k < paymentAllocations.size(); k++) {
//            if (paymentAllocations.get(k).getPaymentFee() != null) {
//                feeCount = feeCount.add(paymentAllocations.get(k).getPaymentFee());
//            }
//            if (paymentAllocations.get(k).getRefundFee() != null) {
//                refundFeeCount = refundFeeCount.add(paymentAllocations.get(k).getRefundFee());
//            }
//        }

        for (int i = 0; i < fees.size(); i++) {
            feeCount = feeCount.add(fees.get(i));
        }
        for (int j = 0; j < refundFees.size(); j++) {
            refundFeeCount = refundFeeCount.add(refundFees.get(j));
        }
        if (feeCount.equals(payment.getPaymentFee()) && refundFeeCount.equals(payment.getRefundFee())) {
            return true;
        }
        throw new StewardBusinessException("分配金额总和与预收款金额总和不符，不能保存");
    }

    /**
     * 根据预收款id查询对应订单项
     *
     * @param id 预收款id
     */
    @Transactional(readOnly = true)
    public List<OrderItemVo> findOrderItemByPaymentId(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的findOrderItemByPaymentId方法,参数为预收款id:[{}]", id);
        }
        Payment payment = generalDAO.get(Payment.class, id);
        List<PaymentAllocation> paymentAllocations = findPAByPaymentId(id);
        List<OrderItem> orderItems = setFeeByPayment(payment, paymentAllocations);
        List<OrderItemVo> orderItemVos = new ArrayList<OrderItemVo>();
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItemVo orderItemVo = new OrderItemVo();
            OrderUtil.getOrderItemVos(orderItemVo, orderItemVos, orderItems.get(i), getUniquePA(payment.getId(), orderItems.get(i).getId()),null,null,false);
        }
        return orderItemVos;
    }

    /**
     * 得到每个订单项对应预收款的分配金额
     *
     * @param payment            预收款
     * @param paymentAllocations 预收款明细
     */
    private List<OrderItem> setFeeByPayment(Payment payment, List<PaymentAllocation> paymentAllocations) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的setFeeByPayment方法,参数为预收款[id={}]", payment.getId());
        }
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        if (paymentAllocations != null && paymentAllocations.size() != 0) {
            for (int i = 0; i < paymentAllocations.size(); i++) {
                PaymentAllocation paymentAllocation = paymentAllocations.get(i);
                OrderItem orderItem = orderService.findOrderItemById(paymentAllocations.get(i).getOrderItemId());
                OrderItemFeeInit(orderItem);
                if (payment.getType().equals(PaymentType.POST_COVER)) {
                    if (paymentAllocation.getPaymentFee() != null) {
                        orderItem.setPostCoverFee(paymentAllocation.getPaymentFee());
                    }
                    if (paymentAllocation.getRefundFee() != null) {
                        orderItem.setPostCoverRefundFee(paymentAllocation.getRefundFee());
                    }
                }
                if (payment.getType().equals(PaymentType.SERVICE_COVER)) {
                    if (paymentAllocation.getPaymentFee() != null) {
                        orderItem.setServiceCoverFee(paymentAllocation.getPaymentFee());
                    }
                    if (paymentAllocation.getRefundFee() != null) {
                        orderItem.setServiceCoverRefundFee(paymentAllocation.getRefundFee());
                    }
                }
                if (payment.getType().equals(PaymentType.ORDER_POST_FEE)) {
                    if (paymentAllocation.getPaymentFee() != null) {
                        orderItem.setSharedPostFee(paymentAllocation.getPaymentFee());
                    }
                }
                orderItems.add(orderItem);
            }
        }
        return orderItems;
    }

    private void OrderItemFeeInit(OrderItem orderItem) {
        orderItem.setPostCoverFee(Money.valueOf(0));
        orderItem.setPostCoverRefundFee(Money.valueOf(0));
        orderItem.setServiceCoverFee(Money.valueOf(0));
        orderItem.setServiceCoverRefundFee(Money.valueOf(0));
        orderItem.setSharedPostFee(Money.valueOf(0));
    }

//     /*
//     * 删除预收款记录，并更新对应订单项金额
//     *
//     * @param paymentId   预收款信息id
//     * @param orderItemId 订单项id
//     */
//    public void deletePAandUpdateprice(Integer paymentId, Integer orderItemId) {
//        if (logger.isInfoEnabled()) {
//            logger.info("PaymentService类中的findOrderItemByPaymentId方法,参数为预收款id:[{}],订单项id:[{}]",paymentId,orderItemId);
//        }
//        Payment payment = generalDAO.get(Payment.class, paymentId);
//        OrderItem orderItem = orderService.findOrderItemById(orderItemId);
//        PaymentAllocation paymentAllocation = getUniquePA(paymentId, orderItemId);
//        if (paymentAllocation == null) {
//            throw new StewardBusinessException("未找到对应分配明细");
//        }
//        updateOrderItemPrice(payment, orderItem, paymentAllocation);
//        deletePA(paymentAllocation);
//    }

    /**
     * 删除预收款明细
     *
     * @param paymentAllocation 预收款明细信息
     */
    private void deletePA(PaymentAllocation paymentAllocation) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的deletePA方法,参数为{}", paymentAllocation.toString());
        }
        generalDAO.remove(paymentAllocation);
    }

    /**
     * 根据预收款和订单项，得到唯一的一条预收款明细
     *
     * @param paymentId   预收款信息id
     * @param orderItemId 订单项id
     */
    private PaymentAllocation getUniquePA(Integer paymentId, Integer orderItemId) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的getUniquePA方法,参数为预收款id:[{}],订单项id:[{}]", paymentId, orderItemId);
        }
        Search search = new Search(PaymentAllocation.class);
        search.addFilterEqual("paymentId", paymentId);
        search.addFilterEqual("orderItemId", orderItemId);
        search.addSortDesc("id");
        List<PaymentAllocation> paymentAllocations = generalDAO.search(search);
        return CollectionUtils.isEmpty(paymentAllocations) ? null : paymentAllocations.get(0);
    }

    /**
     * 更新订单项金额
     *
     * @param payment           预收款信息
     * @param orderItem         预收款订单项
     * @param paymentAllocation 预收款明细
     */
    private void updateOrderItemPrice(Payment payment, OrderItem orderItem, PaymentAllocation paymentAllocation) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的updateOrderItemPrice方法,参数为预收款[id={}],订单项[id={}],预收款分配明细[id={}]",
                    new Object[]{payment.getId(), orderItem.getId(), paymentAllocation.getId()});
        }
        if (payment.getType().equals(PaymentType.POST_COVER)) {
            if (paymentAllocation.getPaymentFee() != null) {
                orderItem.setPostCoverFee(orderItem.getPostCoverFee().subtract(paymentAllocation.getPaymentFee()));
            }
            if (paymentAllocation.getRefundFee() != null) {
                orderItem.setPostCoverRefundFee(orderItem.getPostCoverRefundFee().subtract(paymentAllocation.getRefundFee()));
            }
        }
        if (payment.getType().equals(PaymentType.SERVICE_COVER)) {
            if (paymentAllocation.getPaymentFee() != null) {
                orderItem.setServiceCoverFee(orderItem.getServiceCoverFee().subtract(paymentAllocation.getPaymentFee()));
            }
            if (paymentAllocation.getRefundFee() != null) {
                orderItem.setServiceCoverRefundFee(orderItem.getServiceCoverRefundFee().subtract(paymentAllocation.getRefundFee()));
            }
        }
        if (payment.getType().equals(PaymentType.ORDER_POST_FEE)) {
            if (paymentAllocation.getPaymentFee() != null) {
                orderItem.setSharedPostFee(orderItem.getSharedPostFee().subtract(paymentAllocation.getPaymentFee()));
            }
        }
        orderFeeService.calculateOrderItemFee(orderItem);
        updateOrderFee(orderItem);
        generalDAO.saveOrUpdate(orderItem);
    }

    /**
     * 更新订单项对应订单的金额
     *
     * @param orderItem 预收款分配的订单项
     */
    private void updateOrderFee(OrderItem orderItem) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的updateOrderFee方法,参数为订单项[id={}]", orderItem.getId());
        }
        Order order = orderService.findOrderById(orderItem.getOrderId());
        List<OrderItem> orderItemList = orderService.findOrderItemByOrderId(order.getId());
        orderFeeService.calculateOrderFee(order, orderItemList);
        generalDAO.saveOrUpdate(order);
    }


    /**
     * 各种预收款类型，判断订单项是否满足分配条件
     *
     * @param paymentId   预收款信息id
     * @param orderItemId 预收款订单项voId
     */
    public OrderItemVo isOrderItemLegal(Integer paymentId, Integer orderItemId) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的isOrderItemLegal方法,参数为预收款id[{}],订单项id[{}]", paymentId, orderItemId);
        }
        Payment payment = generalDAO.get(Payment.class, paymentId);
        OrderItem orderItem = orderService.findOrderItemById(orderItemId);
        OrderItemVo orderItemVo = new OrderItemVo();
        List<OrderItemVo> orderItemVos = new ArrayList<OrderItemVo>();
        OrderUtil.getOrderItemVos(orderItemVo, orderItemVos, orderItem, null,null,null,false);
        orderItemVo = orderItemVos.get(0);
        if (!payment.getPlatformType().equals(orderItem.getPlatformType())) {
            throw new StewardBusinessException("预收款和订单项必须来自同一平台");
        }
        if (!payment.getShop().equals(orderItem.getOrder().getShop())) {
            throw new StewardBusinessException("预收款和订单项必须来自同一店铺");
        }
//        if (payment.getType().equals(PaymentType.SERVICE_COVER) && !isServiceCoverLegal(orderItem)) {
//            throw new StewardBusinessException("该订单项不满足执行服务补差的条件");
//        }
        if (payment.getType().equals(PaymentType.ORDER_POST_FEE)) {
            isPostCoverLegal(payment, orderItem);
        }
        PaymentAllocation paymentAllocation = getUniquePA(paymentId, orderItem.getId());
        if (paymentAllocation == null) {
            initOrderItemVoFee(orderItemVo);
            return orderItemVo;
        }
        updateFeeByPayment(orderItemVo, payment, paymentAllocation);
        return orderItemVo;
    }

    /**
     * 更新订单项vo的金额，是其成为指定预收款所分配的金额（如一个订单项的邮费补差是50，其中8号预收款配分的邮费补差是30，则改为30）
     *
     * @param orderItemVo       订单项vo
     * @param payment           预收款
     * @param paymentAllocation 预收款分配明细
     */
    private void updateFeeByPayment(OrderItemVo orderItemVo, Payment payment, PaymentAllocation paymentAllocation) {
        if (payment.getType().equals(PaymentType.POST_COVER)) {
            if (paymentAllocation.getPaymentFee() != null) {
                orderItemVo.setPostCoverFee(paymentAllocation.getPaymentFee() + "");
            }
            if (paymentAllocation.getRefundFee() != null) {
                orderItemVo.setPostCoverRefundFee(paymentAllocation.getRefundFee() + "");
            }
        }
        if (payment.getType().equals(PaymentType.SERVICE_COVER)) {
            if (paymentAllocation.getPaymentFee() != null) {
                orderItemVo.setServiceCoverFee(paymentAllocation.getPaymentFee() + "");
            }
            if (paymentAllocation.getRefundFee() != null) {
                orderItemVo.setServiceCoverRefundFee(paymentAllocation.getRefundFee() + "");
            }
        }
        if (payment.getType().equals(PaymentType.ORDER_POST_FEE)) {
            if (paymentAllocation.getPaymentFee() != null) {
                orderItemVo.setSharedPostFee(paymentAllocation.getPaymentFee() + "");
            }
        }
    }

    /**
     * 为订单项vo初始化金额
     *
     * @param orderItemVo 订单项vo
     */
    private void initOrderItemVoFee(OrderItemVo orderItemVo) {
        orderItemVo.setPostCoverFee("0");
        orderItemVo.setPostCoverRefundFee("0");
        orderItemVo.setServiceCoverFee("0");
        orderItemVo.setServiceCoverRefundFee("0");
        orderItemVo.setSharedPostFee("0");
    }

    /**
     * 判断订单项是否满足“订单邮费”的分配条件
     *
     * @param payment   预收款信息
     * @param orderItem 预收款订单项
     */
    private boolean isPostCoverLegal(Payment payment, OrderItem orderItem) {
        if (!orderItem.getType().equals(OrderItemType.PRODUCT) && !orderItem.getType().equals(OrderItemType.MEALSET)) {
            throw new StewardBusinessException("分摊订单邮费的订单项必须是正常商品或是套餐商品");
        }
        if (!payment.getPlatformOrderNo().equals(orderItem.getPlatformOrderNo())) {
            throw new StewardBusinessException("邮费和订单项不是来源与同一订单");
        }
        return true;
    }

    /**
     * 判断订单项是否满足“服务补差”的分配条件
     *
     * @param orderItem 预收款订单项
     */
    private boolean isServiceCoverLegal(OrderItem orderItem) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的isServiceCoverLegal方法,参数为订单项[id={}]", orderItem.getId());
        }
        String[] types = {OrderItemType.REPLENISHMENT.getValue(), OrderItemType.EXCHANGE_AFTERSALE.getValue()};
        for (int i = 0; i < types.length; i++) {
            if (orderItem.getType().getValue().equals(types[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据原始订单项生成预收款,邮费补差和服务补差生成预收款会使用到
     *
     * @param originalOrderItem
     */
    public Payment createByOriginalOrderItem(PaymentType paymentType, OriginalOrderItem originalOrderItem) {

        Payment payment = new Payment();
        payment.setType(paymentType);

        payment.setOriginalOrderItemId(originalOrderItem.getId());
        payment.setPlatformSubOrderNo(originalOrderItem.getPlatformSubOrderNo());

        OriginalOrder originalOrder = originalOrderItem.getOriginalOrder();
        createPayment(payment, originalOrder, Money.valueOf(originalOrderItem.getBuyCount()));

        if (logger.isDebugEnabled()) {
            logger.debug("原始订单项[id={}]是{}产品, 生成一笔{}预收款[id={}].", new Object[]{originalOrderItem.getId(), paymentType.value, paymentType.value, payment.getId()});
        }


        return payment;

    }

    /**
     * 根据原始订单生成预收款,订单邮费预收款会使用到
     *
     * @param originalOrder
     */
    public Payment createByOriginalOrder(OriginalOrder originalOrder) {
        Payment payment = new Payment();
        payment.setType(PaymentType.ORDER_POST_FEE);
        createPayment(payment, originalOrder, originalOrder.getPostFee());

        if (logger.isDebugEnabled()) {
            logger.debug("原始订单[id={}]包含邮费[{}], 生成的订单邮费预收款[id={}]", new Object[]{originalOrder.getId(), originalOrder.getPostFee(), payment.getId()});
        }

        return payment;
    }

    /**
     * 将vo对象转换成普通订单项
     *
     * @param paymentOrderItemVos
     */
    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItem(PaymentOrderItemVo[] paymentOrderItemVos) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的getOrderItem方法");
        }
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        if (paymentOrderItemVos.length == 0) {
            throw new StewardBusinessException("没有传入分配好的订单项");
        }
        for (int i = 0; i < paymentOrderItemVos.length; i++) {
            OrderItem orderItem = orderService.findOrderItemById(Integer.valueOf(paymentOrderItemVos[i].getId()));
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private void createPayment(Payment payment, OriginalOrder originalOrder, Money paymentFee) {

        payment.setOriginalOrderId(originalOrder.getId());
        payment.setAllocateStatus(PaymentAllocateStatus.WAIT_ALLOCATE);

        payment.setPlatformOrderNo(originalOrder.getPlatformOrderNo());
        payment.setPlatformType(originalOrder.getPlatformType());
        payment.setShopId(originalOrder.getShopId());

        payment.setBuyerMessage(originalOrder.getBuyerMessage());
        payment.setBuyerId(originalOrder.getBuyerId());
        payment.setBuyTime(originalOrder.getBuyTime());
        payment.setPayTime(originalOrder.getPayTime());
        payment.setRemark(originalOrder.getRemark());

        //退款金额就等于订购数量
        payment.setPaymentFee(paymentFee);

        generalDAO.saveOrUpdate(payment);

//        memberInfoLogService.saveOnPayment(payment);
        memberInfoLogService.save(null,null,payment,null,OrderLogType.PAYMENT,false);

    }

    /**
     * 根据平台类型和订单号查询预收款
     *
     * @param platformType
     * @param platformOrderNo
     * @return
     */
    @Transactional(readOnly = true)
    public List<Payment> findByPlatformOrderNo(PlatformType platformType, String platformOrderNo) {
        Search search = new Search(Payment.class);
        if (platformType != null) {
            search.addFilterEqual("platformType", platformType);
        }
        search.addFilterEqual("platformOrderNo", platformOrderNo);
        return generalDAO.search(search);
    }

    /**
     * 根据平台订单号查询<b>非已分配</b>的订单邮费预收款
     *
     * @param platformOrderNo
     * @return
     */
    @Transactional(readOnly = true)
    public List<Payment> findPostFeeAndNotAllocatedPaymentByPlatformOrderNo(String platformOrderNo) {
        if (platformOrderNo == null) {
            return null;
        }
        Search search = new Search(Payment.class)
                .addFilterEqual("platformOrderNo", platformOrderNo)
                .addFilterEqual("type", PaymentType.ORDER_POST_FEE)
                .addFilterNotEqual("allocateStatus", PaymentAllocateStatus.ALLOCATED);
        //noinspection unchecked
        return generalDAO.search(search);
    }

    /**
     * 根据原始订单信息更新预收款,现在只更新备注
     *
     * @param originalOrder
     * @param payments
     */
    public void updateByOriginalOrder(OriginalOrder originalOrder, List<Payment> payments) {
        logger.info("根据原始订单[id={}]更新预收款[size={}]信息", originalOrder.getId(), payments.size());

        for (Payment payment : payments) {
            payment.setRemark(originalOrder.getRemark());
            generalDAO.saveOrUpdate(payment);
        }
    }

    /**
     * 根据订单项ID，查询预收款分配明细
     *
     * @param id 订单项id
     */
    public List<PaymentAllocation> findPAsByOrderItem(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的findPAsByOrderItem方法,订单项ID为：[{}]");
        }
        Search search = new Search(PaymentAllocation.class);
        if (NumberUtil.isNullOrZero(id)) {
            throw new StewardBusinessException("订单项ID不合法");
        }
        search.addFilterEqual("orderItemId", id);
        return generalDAO.search(search);
    }

    public List<PaymentAllocation> findAllPaymentAllocation() {
        if (logger.isInfoEnabled()) {
            logger.info("PaymentService类中的findPAsByOrderItem方法,订单项ID为：[{}]");
        }
        Search search = new Search(PaymentAllocation.class);

        return generalDAO.search(search);
    }

    /**
     * 根据外部平台子订单号查询订单项
     *
     * @param platformType
     * @param platformSubOrderNo
     * @return
     */
    public List<Payment> findByPlatformSubOrderNo(PlatformType platformType, String platformSubOrderNo) {
        Preconditions.checkNotNull(platformType);
        Preconditions.checkNotNull(platformSubOrderNo);
        Search search = new Search(Payment.class);
        search.addFilterEqual("platformType", platformType);
        search.addFilterEqual("platformSubOrderNo", platformSubOrderNo);
        return generalDAO.search(search);

    }

    /**
     * 预收款退款
     *
     * @param paymentId
     * @param refundFee
     */
    public void paymentReturn(Integer paymentId, Money refundFee) {
        logger.debug("预收款[id={}]退款,退款金额[{}]", paymentId, refundFee);
        Payment payment = generalDAO.get(Payment.class, paymentId);
        payment.setRefundFee(payment.getRefundFee().add(refundFee));
        payment.setAllocateStatus(PaymentAllocateStatus.WAIT_ALLOCATE);
        generalDAO.saveOrUpdate(payment);
    }

    @Transactional(readOnly = true)
    public Payment get(Integer id) {
        return generalDAO.get(Payment.class, id);
    }


    @Transactional(readOnly = true)
    public List<PaymentAllocation> getByPaymentId(Integer paymentId) {
        return generalDAO.search(new Search(PaymentAllocation.class).addFilterEqual("paymentId", paymentId));
    }

    @Transactional(readOnly = true)
    public List<Payment> getByOrderItemId(Integer orderItemId) {
        List<PaymentAllocation> paymentAllocations = generalDAO.search(new Search(PaymentAllocation.class).addFilterEqual("orderItemId", orderItemId));
        List<Payment> payments = new ArrayList<Payment>();
        Payment payment = null;
        for (PaymentAllocation paymentAllocation : paymentAllocations) {
            payment = (Payment) generalDAO.search(new Search(Payment.class).addFilterEqual("id", paymentAllocation.getPaymentId())).get(0);
            payments.add(payment);
        }
        return payments;
    }

    @Transactional(readOnly = true)
    public List<PaymentAllocation> getById(Integer paymentId, Integer orderItemId) {
        Search search = new Search(PaymentAllocation.class);
        search.addFilterEqual("paymentId", paymentId);
        search.addFilterEqual("orderItemId", orderItemId);
        return generalDAO.search(search);
    }

   /*public List<Payment> findPaymentByPayTime() {

       List<Payment> paymentList = findByKey(paymentSearchCondition, null);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        createExcelTitle(sheet);

        int rowIndex = 2;//从第三行开始，一二行放title
        for (Payment payment : paymentList) {
            if(payment.getAllocateStatus().equals(PaymentAllocateStatus.ALLOCATED)){
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            renderPayment2Excel(row, cellIndex, payment);
            }
        }
        return workbook;
    }*/

    public Workbook reportPayment(PaymentSearchCondition paymentSearchCondition) {
        List<Payment> paymentList = findByKey(paymentSearchCondition, null);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        createExcelTitle(sheet);

        int rowIndex = 2;//从第三行开始，一二行放title
        for (Payment payment : paymentList) {
            if (payment.getAllocateStatus().equals(PaymentAllocateStatus.ALLOCATED)) {
                List<PaymentAllocation> paymentAllocations = findPAByPaymentId(payment.getId());
                List<OrderItem> orderItems = new ArrayList<OrderItem>();
                for (PaymentAllocation paymentAllocation : paymentAllocations) {
                    OrderItem orderItem = orderService.findOrderItemById(paymentAllocation.getOrderItemId());
//                orderItems.add(orderItem);
//                for (int i = 0; orderItems != null && i < orderItems.size(); i++) {
//                    OrderItem item = orderItems.get(i);
                    Row row = sheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    renderPayment2Excel(row, cellIndex, orderItem, payment, paymentAllocation);
                }
//
//            }
//
            }
        }
        return workbook;
    }

    private void renderPayment2Excel(Row itemRow, int startCellIndex, OrderItem orderItem, Payment payment, PaymentAllocation paymentAllocation) {

        PoiUtil.createCell(itemRow, startCellIndex++, payment.getType().getValue());
        PoiUtil.createCell(itemRow, startCellIndex++, payment.getAllocateStatus().getValue());
        PoiUtil.createCell(itemRow, startCellIndex++, payment.getPlatformType().getValue());
        if (payment.getShop() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, payment.getShop().getNick());
        } else {
            startCellIndex++;
        }
        if (payment.getPlatformOrderNo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, payment.getPlatformOrderNo());
        } else {
            startCellIndex++;
        }
        if (orderItem.getPlatformOrderNo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getPlatformSubOrderNo());
        } else {
            startCellIndex++;
        }
        if (orderService.findOrderById(orderItem.getOrderId()) != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderService.findOrderById(orderItem.getOrderId()).getOrderNo());
        } else {
            startCellIndex++;
        }
        PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getId());
        if (payment.getBuyTime() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, payment.getBuyTime());
        } else {
            startCellIndex++;
        }
        if (payment.getPayTime() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, payment.getPayTime());
        } else {
            startCellIndex++;
        }
        if (orderItem.getType().getValue() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getType().getValue());  //订单项类型
        } else {
            startCellIndex++;
        }
        if (orderItem.getStatus().getValue() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getStatus().getValue());//订单项状态
        } else {
            startCellIndex++;
        }
        if (orderItem.getReturnStatus().getValue() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getReturnStatus().getValue());  //
        } else {
            startCellIndex++;
        }
        if (orderItem.getOfflineReturnStatus().getValue() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getOfflineReturnStatus().getValue());//
        } else {
            startCellIndex++;
        }
        if (orderItem.getExchanged()) {
            PoiUtil.createCell(itemRow, startCellIndex++, "是");
        } else {
            PoiUtil.createCell(itemRow, startCellIndex++, "否");
        }
        if (orderItem.getProductCode() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getProductCode());
        } else {
            startCellIndex++;
        }
        if (orderItem.getProductName() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getProductName());
        } else {
            startCellIndex++;
        }
        if (orderItem.getSpecInfo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getSpecInfo());
        } else {
            startCellIndex++;
        }

        PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getProductSku());
        if (orderItem.getProduct().getBrand() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getProduct().getBrand().getName());
        } else {
            startCellIndex++;
        }
        if (orderItem.getProduct().getCategory() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getProduct().getCategory().getName());
        } else {
            startCellIndex++;
        }
        if (orderItem.getPrice().toString() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getPrice().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getDiscountPrice() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getDiscountPrice().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getBuyCount() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getBuyCount());
        } else {
            startCellIndex++;
        }
        if (orderItem.getRepoNum() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getRepoNum());
        } else {
            startCellIndex++;
        }
        if (orderItem.getDiscountFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getDiscountFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getSharedDiscountFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getSharedDiscountFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getSharedPostFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getSharedPostFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getActualFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getActualFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getPostCoverFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getPostCoverFee().toString());
        } else {
            startCellIndex++;
        }

        if (orderItem.getPostCoverRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getPostCoverRefundFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getServiceCoverFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getServiceCoverFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getServiceCoverRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getServiceCoverRefundFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getRefundFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getOfflineRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getOfflineRefundFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getOfflineRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getReturnPostFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getReturnPostPayer() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getReturnPostPayer().getValue());
        } else {
            startCellIndex++;
        }
        if (orderItem.getOfflineReturnPostFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getOfflineReturnPostFee().toString());
        } else {
            startCellIndex++;
        }
        if (orderItem.getOfflineReturnPostPayer() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getOfflineReturnPostPayer().getValue());
        } else {
            startCellIndex++;
        }
        if (orderItem.getExchangePostFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getExchangePostFee().toString());
        } else {
            startCellIndex++;
        }

        if (orderItem.getExchangePostPayer() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getExchangePostPayer().getValue());
        } else {
            startCellIndex++;
        }
        if (orderItem.getGoodsFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getGoodsFee().toString());
        } else {
            startCellIndex++;
        }
        if (payment.getPaymentFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, payment.getPaymentFee().toString());
        } else {
            startCellIndex++;
        }
        if (paymentAllocation.getPaymentFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, paymentAllocation.getPaymentFee().toString());
        } else {
            startCellIndex++;
        }
        if (payment.getRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, payment.getRefundFee().toString());
        } else {
            startCellIndex++;
        }
        if (paymentAllocation.getRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, paymentAllocation.getRefundFee().toString());
        } else {
            startCellIndex++;
        }
        if (payment.getBuyerId() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, payment.getBuyerId());
        } else {
            startCellIndex++;
        }
        if (payment.getBuyerMessage() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, payment.getBuyerMessage());
        } else {
            startCellIndex++;
        }
        if (payment.getRemark() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, payment.getRemark());
        } else {
            startCellIndex++;
        }
        if (orderItem.getPriceDescription() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, orderItem.getPriceDescription());
        } else {
            startCellIndex++;
        }

    }

    private void createExcelTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        PoiUtil.createCell(titleRow, 0, "导出预收款信息");
        int cellIndex = 0;
        Row row = sheet.createRow(1);
        PoiUtil.createCell(row, cellIndex++, "预收款类型");
        PoiUtil.createCell(row, cellIndex++, "分配状态");
        PoiUtil.createCell(row, cellIndex++, "平台类型");
        PoiUtil.createCell(row, cellIndex++, "店铺名称");
        PoiUtil.createCell(row, cellIndex++, "外部平台订单编号");
        PoiUtil.createCell(row, cellIndex++, "外部平台订单项编号");
        PoiUtil.createCell(row, cellIndex++, "智库城订单编号");
        PoiUtil.createCell(row, cellIndex++, "智库城订单项编号");
        PoiUtil.createCell(row, cellIndex++, "下单时间");
        PoiUtil.createCell(row, cellIndex++, "付款时间");


        PoiUtil.createCell(row, cellIndex++, "订单项类型");
        PoiUtil.createCell(row, cellIndex++, "订单项状态");
        PoiUtil.createCell(row, cellIndex++, "线上退货状态");
        PoiUtil.createCell(row, cellIndex++, "线下退货状态");
        PoiUtil.createCell(row, cellIndex++, "换货状态");
        PoiUtil.createCell(row, cellIndex++, "商品编号");
        PoiUtil.createCell(row, cellIndex++, "商品名称");
        PoiUtil.createCell(row, cellIndex++, "商品规格");
        PoiUtil.createCell(row, cellIndex++, "sku");
        PoiUtil.createCell(row, cellIndex++, "品牌");

        PoiUtil.createCell(row, cellIndex++, "类别");
        PoiUtil.createCell(row, cellIndex++, "原价（一口价）");
        PoiUtil.createCell(row, cellIndex++, "促销价");
        PoiUtil.createCell(row, cellIndex++, "订货数量");
        PoiUtil.createCell(row, cellIndex++, "库存");
        PoiUtil.createCell(row, cellIndex++, "订单项优惠金额");
        PoiUtil.createCell(row, cellIndex++, "分摊优惠金额");
        PoiUtil.createCell(row, cellIndex++, "分摊邮费");
        PoiUtil.createCell(row, cellIndex++, "成交金额");
        PoiUtil.createCell(row, cellIndex++, "邮费补差金额");

        PoiUtil.createCell(row, cellIndex++, "邮费补差退款金额");
        PoiUtil.createCell(row, cellIndex++, "服务补差金额");
        PoiUtil.createCell(row, cellIndex++, "服务补差退款金额");
        PoiUtil.createCell(row, cellIndex++, "线上退款金额");
        PoiUtil.createCell(row, cellIndex++, "线下退款金额");
        PoiUtil.createCell(row, cellIndex++, "线上退货邮费");
        PoiUtil.createCell(row, cellIndex++, "线上退货邮费承担方");
        PoiUtil.createCell(row, cellIndex++, "线下退货邮费");
        PoiUtil.createCell(row, cellIndex++, "线下退货邮费承担方");
        PoiUtil.createCell(row, cellIndex++, "线下换货邮费");

        PoiUtil.createCell(row, cellIndex++, "线下换货邮费承担方");
        PoiUtil.createCell(row, cellIndex++, "货款");
        PoiUtil.createCell(row, cellIndex++, "预收款金额");
        PoiUtil.createCell(row, cellIndex++, "预收款分配金额");
        PoiUtil.createCell(row, cellIndex++, "预收款退款金额");
        PoiUtil.createCell(row, cellIndex++, "预收款退款分配金额");
        PoiUtil.createCell(row, cellIndex++, "买家ID");
        PoiUtil.createCell(row, cellIndex++, "买家留言");
        PoiUtil.createCell(row, cellIndex++, "客服备注");
        PoiUtil.createCell(row, cellIndex++, "价格描述");

        int titleCell = cellIndex - 1;
        sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, titleCell));//合并订单标题的单元格
    }
}
