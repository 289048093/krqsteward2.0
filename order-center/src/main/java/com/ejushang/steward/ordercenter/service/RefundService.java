package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.*;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.util.RefundUtil;
import com.google.common.base.Preconditions;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;

/**
 * User: Shiro
 * Date: 14-5-8
 * Time: 上午11:23
 */
@Service
@Transactional
public class RefundService {

    private static final Logger log = LoggerFactory.getLogger(RefundService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private OrderFeeService orderFeeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberInfoLogService memberInfoLogService;


    /**
     * 获取退款列表
     *
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<Refund> findRefund(Map<String, Object[]> map, Page page) throws ParseException {
        Map<String, String> map1 = new HashMap<String, String>();
        //将接受到的map参数解析并赋值给map1
        RefundUtil.getConditionMap(map, map1);
        //HQL条件的值
        List<Object> objects = new ArrayList<Object>();
        //拼接HQL的变量
        StringBuilder stringBuilder = new StringBuilder();
        //拼接HQL的方法
        RefundUtil.refundCondition(map1, stringBuilder, objects);
        //执行HQL
        List<Refund> refunds = generalDAO.query(stringBuilder.toString(), page, objects.toArray());
        for (Refund refund : refunds) {
            Map<String, Object[]> map2 = new HashMap<String, Object[]>();
            Object[] obj = new Object[1];
            if (refund.getType().equals(RefundType.ORDER)) {
                obj[0] = refund.getOrderItemId();
                map2.put("orderItemId", obj);
                refund.setOrderItemVo(orderService.findOrderItemVoByMap(map2).get(0));
            }
            if(refund.getOrderItem()!=null) {
                refund.setReceiverPhone(refund.getOrderItem().getOrder().getInvoice().getReceiver().getReceiverPhone());
                refund.setReceiverMobile(refund.getOrderItem().getOrder().getInvoice().getReceiver().getReceiverMobile());
            }
        }
        return refunds;
    }

    /**
     * 新建线下退款申请
     *
     * @param refund
     */
    @Transactional
    public void saveRefund(Integer oldItemId, Refund refund) {
        String flag = "add";
        OrderItem orderItem = generalDAO.get(OrderItem.class, oldItemId);
        Order order = orderItem.getOrder();
        refund.setCreateTime(new Date());
        //判断该订单项是否正在申请退款
        if (orderItem.getRefunding()) {
            throw new StewardBusinessException("该订单项正在申请退款，请检查后重新操作");
        }

        //如果是同时退货的情况
        if (refund.isAlsoReturn()) {
            if (orderItem.getOfflineReturnStatus().equals(OrderItemReturnStatus.RETURNED)) {
                throw new StewardBusinessException("该订单项已退货，请检查后重新操作");
            }
            if (orderItem.getOfflineReturnStatus() == null ||
                    orderItem.getOfflineReturnStatus().equals(OrderItemReturnStatus.NORMAL)) {
                //实际退款金额大于账面退款金额时抛出异常
                if (refund.getActualRefundFee().compareTo(refund.getRefundFee()) == 1) {
                    throw new StewardBusinessException("实际退款金额不能大于账面退款金额");
                }
            } else {
                throw new StewardBusinessException("该订单项已退货");
            }
        } else {
            //如果不是同时退货的话，实际退货金额等于账面退款金额
            refund.setActualRefundFee(refund.getRefundFee());
        }

        if (refund.getRefundTime() == null) {
            refund.setRefundTime(refund.getCreateTime());
        }
        refund.setPhase(RefundPhase.AFTER_SALE);
        refund.setPlatformType(orderItem.getPlatformType());
        refund.setType(RefundType.ORDER);
        refund.setOrderItemId(orderItem.getId());
        refund.setBuyerId(order.getBuyerId());
        refund.setShopId(order.getShopId());
        generalDAO.saveOrUpdate(refund);
        orderItem.setOrder(order);
        refund.setOrderItem(orderItem);
        //根据退款信息更新订单项和订单相关信息
        if (refund.getStatus().equals(RefundStatus.SUCCESS)) {
            activationOfflineRefund(refund, null, flag);
//            memberInfoLogService.saveByOrderRefund(refund);
            memberInfoLogService.save(null,refund,null,null,OrderLogType.REFUND,false);


        } else if (refund.getStatus().equals(RefundStatus.IN_PROCESS)) {
            refund.getOrderItem().setRefunding(true);
            generalDAO.saveOrUpdate(refund.getOrderItem());
            orderFeeService.checkOrderReturnStatus(refund.getOrderItem().getOrder());
            generalDAO.saveOrUpdate(refund.getOrderItem().getOrder());
        } else {
            refund.getOrderItem().setRefunding(false);
            generalDAO.saveOrUpdate(refund.getOrderItem());
            orderFeeService.checkOrderReturnStatus(refund.getOrderItem().getOrder());
            generalDAO.saveOrUpdate(refund.getOrderItem().getOrder());
        }
    }


    /**
     * 修改退款
     *
     * @param refund
     */
    @Transactional
    public void updateRefund(Integer id, Refund refund) {
        String flag = "update";
        Refund refund1 = generalDAO.get(Refund.class, id);
        //如果退款类型为成功的话再更改相关金额
        if (refund.getStatus().equals(RefundStatus.SUCCESS)) {
            activationOfflineRefund(refund, refund1, flag);
            //会员日志添加
            if(refund1.getStatus().equals(RefundStatus.SUCCESS)){
                memberInfoLogService.updateRefund(refund);
            }
            else{
//               memberInfoLogService.saveByOrderRefund(refund);
                memberInfoLogService.save(null,refund,null,null,OrderLogType.REFUND,false);
            }
        } else if (refund.getStatus().equals(RefundStatus.IN_PROCESS)) {
            if (refund.isAlsoReturn()) {
                if (refund1.getOrderItem().getOfflineReturnStatus().equals(OrderItemReturnStatus.RETURNED) && refund1.isAlsoReturn() == false && refund1.isOnline() == false) {
                    throw new StewardBusinessException("该订单项线下已退货，请检查后重新操作");
                }
                if (refund1.getOrderItem().getReturnStatus().equals(OrderItemReturnStatus.RETURNED) && refund1.isAlsoReturn() == false && refund1.isOnline()) {
                    throw new StewardBusinessException("该订单项线上已退货，请检查后重新操作");
                }
            }
            refund1.getOrderItem().setRefunding(true);
            generalDAO.saveOrUpdate(refund1.getOrderItem());
            orderFeeService.checkOrderReturnStatus(refund1.getOrderItem().getOrder());
            generalDAO.saveOrUpdate(refund1.getOrderItem().getOrder());
        } else {
            if (findByItemId(refund1.getOrderItemId()).size() < 1 || refund1.getStatus().equals(RefundStatus.IN_PROCESS)) {
                refund1.getOrderItem().setRefunding(false);
            }
            generalDAO.saveOrUpdate(refund1.getOrderItem());
            orderFeeService.checkOrderReturnStatus(refund1.getOrderItem().getOrder());
            generalDAO.saveOrUpdate(refund1.getOrderItem().getOrder());
        }
        if (refund1.isOnline() == false) {
            refund1.setRefundFee(refund.getRefundFee());
            refund1.setRefundTime(refund.getRefundTime());
            refund1.setStatus(refund.getStatus());
        } else {
            if (!refund1.getStatus().equals(RefundStatus.SUCCESS)) {
                throw new StewardBusinessException("要求在线上退款成功之后才可在线下进行修改");
            }
        }
        if (refund.isAlsoReturn()) {
            refund1.setAlsoReturn(true);
            refund1.setPostFee(refund.getPostFee());
            refund1.setPostPayer(refund.getPostPayer());
            refund1.setShippingNo(refund.getShippingNo());
            refund1.setShippingComp(refund.getShippingComp());
            if (refund.getActualRefundFee().compareTo(refund1.getRefundFee()) == 1) {
                throw new StewardBusinessException("实际退款金额不能大于账面退款金额");
            }
            refund1.setActualRefundFee(refund.getActualRefundFee());
        } else {
            refund1.setAlsoReturn(false);
            refund1.setActualRefundFee(refund1.getRefundFee());
            refund1.getOrderItem().setReturnPostFee(Money.valueOf(0));
            refund1.getOrderItem().setReturnPostPayer(null);
            refund1.setPostFee(Money.valueOf(0));
            refund1.setPostPayer(null);
            refund1.setShippingNo(null);
            refund1.setShippingComp(null);
        }
        //根据退款信息更新订单项和订单相关信息
        refund1.setRevisitTime(refund.getRevisitTime());
        refund1.setStatus(refund.getStatus());
        refund1.setReason(refund.getReason());
        refund1.setRemark(refund.getRemark());
        refund1.setBuyerAlipayNo(refund.getBuyerAlipayNo());
        generalDAO.saveOrUpdate(refund1);
    }

    //更改退款表中是否存在正在申请退货状态
    @Transactional(readOnly = true)
    public List<Refund> findByItemId(Integer id) {
        Search search = new Search(Refund.class);
        search.addFilterEqual("orderItemId", id);
        search.addFilterEqual("status", RefundStatus.IN_PROCESS);
        return generalDAO.search(search);
    }

    /**
     * 根据ID获得退款信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Refund getById(Integer id) {
        return generalDAO.get(Refund.class, id);
    }

    /**
     * 删除退款信息
     *
     * @param id
     */
    @Transactional
    public void deleteRefund(Integer id) {
        Refund refund = this.getById(id);
        OrderItem orderItem = refund.getOrderItem();
        if (refund.isOnline()) {
            throw new StewardBusinessException("不能删除线上退款信息");
        }
        if (refund.getStatus().equals(RefundStatus.SUCCESS)) {
            if (refund.isAlsoReturn()) {
                orderItem.setOfflineReturnStatus(OrderItemReturnStatus.NORMAL);
                orderItem.setOfflineReturnPostPayer(null);
                orderItem.setOfflineReturnPostFee(Money.valueOf(0));
            }
            orderItem.setOfflineRefundFee(orderItem.getOfflineRefundFee().subtract(refund.getActualRefundFee()));
        }
        if (refund.getStatus().equals(RefundStatus.IN_PROCESS)) {
            orderItem.setRefunding(false);
        }
        //修改订单项的相关金额
        orderFeeService.calculateOrderItemFee(refund.getOrderItem());
        generalDAO.saveOrUpdate(refund.getOrderItem());
        //设置订单的退货状态
        orderFeeService.checkOrderReturnStatus(refund.getOrderItem().getOrder());
        //修改订单金额
        orderFeeService.calculateOrderFee(refund.getOrderItem().getOrder(), refund.getOrderItem().getOrder().getOrderItemList());
        generalDAO.saveOrUpdate(refund.getOrderItem().getOrder());

        generalDAO.remove(refund);
    }

    /**
     * 根据订单项ID获得退款信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<Refund> findByOrderItemId(Integer id) {
        Search search = new Search(Refund.class);
        search.addFilterEqual("orderItemId", id);
        return generalDAO.search(search);
    }

    /**
     * 根据订单项ID和时间获得退款信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<Refund> findByOrderItemIdAndRefundTime(Integer id, String startPayTime, String endPayTime) {
        Search search = new Search(Refund.class);
        search.addFilterEqual("orderItemId", id);
        if (!StringUtils.isBlank(startPayTime)) {
            search.addFilterGreaterOrEqual("refundTime", EJSDateUtils.parseDate(startPayTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        if (!StringUtils.isBlank(endPayTime)) {
            search.addFilterLessOrEqual("refundTime", EJSDateUtils.parseDate(endPayTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        return generalDAO.search(search);
    }

    /**
     * 根据退款状态和时间获得退款信息
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<Refund> findByRefundTypeAndRefundTime(RefundType type, String startPayTime, String endPayTime, String platformType) {
        Search search = new Search(Refund.class);
        search.addFilterEqual("type", type);
        if (!StringUtils.isBlank(startPayTime)) {
            search.addFilterGreaterOrEqual("refundTime", EJSDateUtils.parseDate(startPayTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        if (!StringUtils.isBlank(endPayTime)) {
            search.addFilterLessOrEqual("refundTime", EJSDateUtils.parseDate(endPayTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        if (!StringUtils.isBlank(platformType)) {
            search.addFilterEqual("platformType", PlatformType.valueOf(platformType));
        }

        Employee employee = SessionUtils.getEmployee();
        if (employee.isRepositoryEmployee()) {     //仓库人员只能查看自己管理的仓库的数据
            List<Repository> repositories =  new ArrayList<Repository>();
            List<RepositoryCharger> repositoryChargers = generalDAO.search(new Search(RepositoryCharger.class).addFilterEqual("chargerId", employee.getId()));
            for(RepositoryCharger repositoryCharger:repositoryChargers){
                repositories.add(repositoryCharger.getRepository());
            }
            search.addFilterIn("orderItem.order.repo", repositories);
//            search.addFilterEqual("orderItem.order.repo.chargePersonId", employee.getId());
        }
        search.addFilterEqual("status", RefundStatus.SUCCESS);
        return generalDAO.search(search);
    }

    /**
     * 根据预收款ID获得退款信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<Refund> findByPaymentIdAndRefundTime(Integer id, String startPayTime, String endPayTime) {
        Search search = new Search(Refund.class);
        search.addFilterEqual("paymentId", id);
        if (!StringUtils.isBlank(startPayTime)) {
            search.addFilterGreaterOrEqual("refundTime", EJSDateUtils.parseDate(startPayTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        if (!StringUtils.isBlank(endPayTime)) {
            search.addFilterLessOrEqual("refundTime", EJSDateUtils.parseDate(endPayTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        return generalDAO.search(search);
    }

    /**
     * 激活线上退款信息
     */
    @Transactional
    public void activationOnlineRefund(Refund refund) {
        if (!refund.isOnline()) {
            throw new StewardBusinessException("当前退款不为线上退款,退款ID:" + refund.getId());
        }
        OrderItem orderItem = refund.getOrderItem();
        Order order = orderItem.getOrder();
        //将订单项标记为没有退款
        refund.getOrderItem().setRefunding(false);
        orderItem.setRefundFee(orderItem.getRefundFee().add(refund.getRefundFee()));
        orderItem.setActualRefundFee(orderItem.getActualRefundFee().add(refund.getActualRefundFee()));
        if (refund.isAlsoReturn()) {
            //同时退货

            orderItem.setReturnStatus(OrderItemReturnStatus.RETURNED);
            orderItem.setReturnPostFee(refund.getPostFee());
            orderItem.setReturnPostPayer(refund.getPostPayer());
        }
        //修改订单项的相关金额
        orderFeeService.calculateOrderItemFee(orderItem);
        generalDAO.saveOrUpdate(orderItem);
        //设置订单的退货状态
        orderFeeService.checkOrderReturnStatus(order);
        //修改订单金额
        orderFeeService.calculateOrderFee(order, order.getOrderItemList());
        generalDAO.saveOrUpdate(order);
    }

    /**
     * 激活线下退款信息
     */
    @Transactional
    public void activationOfflineRefund(Refund refund, Refund oldRefund, String flag) {
        //如果没有同时退货，实际退款金额等于账面退款金额
        if (refund.isAlsoReturn() == false) {
            refund.setActualRefundFee(refund.getRefundFee());
        }
        if (refund.isOnline()) {
            throw new StewardBusinessException("当前退款不为线下退款,退款ID:" + refund.getId());
        }
        OrderItem orderItem = null;
        Order order = null;
        //进行新建操作
        if (flag.equals("add")) {
            orderItem = refund.getOrderItem();
            order = orderItem.getOrder();
            //订单项线下退款金额加上实际退款金额
            orderItem.setOfflineRefundFee(orderItem.getOfflineRefundFee().add(refund.getActualRefundFee()));
        } else {  //修改操作
            orderItem = oldRefund.getOrderItem();
            order = orderItem.getOrder();
            //当同一条订单项没有正在申请退款的记录或当前要修改的退款记录为正在申请退款，更改状态是需要更改订单项的相关状态，否则不需要改变
            if (findByItemId(orderItem.getId()).size() < 1 || oldRefund.getStatus().equals(RefundStatus.IN_PROCESS)) {
                oldRefund.getOrderItem().setRefunding(false);
            }
            if (oldRefund.isOnline()) {
                if (refund.isAlsoReturn() == false) {
                    refund.setActualRefundFee(oldRefund.getRefundFee());
                }
                if (oldRefund.getStatus().equals(RefundStatus.SUCCESS)) {
                    //订单项线上实际退款金额等于原订单项实际退款金额减去原退款列表中的实际退款金额加上新设置的实际退款金额
                    orderItem.setActualRefundFee(orderItem.getActualRefundFee().subtract(oldRefund.getActualRefundFee()).add(refund.getActualRefundFee()));
                } else {
                    orderItem.setActualRefundFee(orderItem.getActualRefundFee().add(refund.getActualRefundFee()));
                }
            } else {
                if (oldRefund.getStatus().equals(RefundStatus.SUCCESS)) {
                    orderItem.setOfflineRefundFee(orderItem.getOfflineRefundFee().subtract(oldRefund.getActualRefundFee()).add(refund.getActualRefundFee()));
                } else {
                    orderItem.setOfflineRefundFee(orderItem.getOfflineRefundFee().add(refund.getActualRefundFee()));
                }
            }
        }

        if (refund.isAlsoReturn()) {
            if (flag.equals("add")) {
                if (orderItem.getOfflineReturnStatus().equals(OrderItemReturnStatus.RETURNED)) {
                    throw new StewardBusinessException("该订单项已退货，请检查后重新操作");
                }
                orderItem.setOfflineReturnPostFee(refund.getPostFee());
                orderItem.setOfflineReturnPostPayer(refund.getPostPayer());
                orderItem.setOfflineReturnStatus(OrderItemReturnStatus.RETURNED);
            } else {
                if (orderItem.getOfflineReturnStatus().equals(OrderItemReturnStatus.RETURNED) && oldRefund.isAlsoReturn() == false && oldRefund.isOnline() == false) {
                    throw new StewardBusinessException("该订单项线下已退货，请检查后重新操作");
                }
                if (orderItem.getReturnStatus().equals(OrderItemReturnStatus.RETURNED) && oldRefund.isAlsoReturn() == false && oldRefund.isOnline()) {
                    throw new StewardBusinessException("该订单项线上已退货，请检查后重新操作");
                }
                if (oldRefund.isOnline()) {
                    orderItem.setReturnPostFee(refund.getPostFee());
                    orderItem.setReturnPostPayer(refund.getPostPayer());
                    orderItem.setReturnStatus(OrderItemReturnStatus.RETURNED);
                } else {

                    orderItem.setOfflineReturnPostFee(refund.getPostFee());
                    orderItem.setOfflineReturnPostPayer(refund.getPostPayer());
                    orderItem.setOfflineReturnStatus(OrderItemReturnStatus.RETURNED);
                }
            }
        } else if (oldRefund != null && oldRefund.isAlsoReturn() && refund.isAlsoReturn() == false) {
            if (oldRefund.isOnline()) {
                orderItem.setReturnPostFee(Money.valueOf(0));
                orderItem.setReturnPostPayer(null);
                orderItem.setReturnStatus(OrderItemReturnStatus.NORMAL);
            } else {
                orderItem.setOfflineReturnPostFee(Money.valueOf(0));
                orderItem.setOfflineReturnPostPayer(null);
                orderItem.setOfflineReturnStatus(OrderItemReturnStatus.NORMAL);
            }
        }
        //修改订单项的相关金额
        OrderItem orderItem1 = orderItem;
        orderFeeService.calculateOrderItemFee(orderItem);
        generalDAO.saveOrUpdate(orderItem);
        //设置订单的退货状态
        orderFeeService.checkOrderReturnStatus(order);
        //修改订单金额
        orderFeeService.calculateOrderFee(order, order.getOrderItemList());
        generalDAO.saveOrUpdate(order);


    }


    /**
     * 根据外部平台退款单编号查询系统内的退款单
     *
     * @param platformType
     * @param platformRefundNo
     * @return
     */
    public List<Refund> findByPlatformRefundNo(PlatformType platformType, String platformRefundNo) {
        Preconditions.checkNotNull(platformType);
        Preconditions.checkNotNull(platformRefundNo);
        Search search = new Search(Refund.class);
        search.addFilterEqual("platformType", platformType);
        search.addFilterEqual("platformRefundNo", platformRefundNo);
        return generalDAO.search(search);
    }

    /**
     * 根据原始退款单和对应订单项创建退款记录
     *
     * @param originalRefund
     * @param orderItems
     * @return
     */
    public List<Refund> createByOrderItems(OriginalRefund originalRefund, List<OrderItem> orderItems) {
        if (log.isDebugEnabled()) {
            log.debug("根据订单项退款生成新的智库城退款,原始退款信息[id={}], 外部子订单号[{}]或交易号[{}]对应的订单项款数量[{}]",
                    new Object[]{originalRefund.getId(), originalRefund.getOid(), originalRefund.getTid(), orderItems.size()});
        }


        List<Refund> refunds = new ArrayList<Refund>();
        Money totalRefundFee = originalRefund.getRefundFee();
        Money totalOrderItemFee = Money.valueOf(0d);
        for (OrderItem orderItem : orderItems) {
            if (!orderItem.getType().equals(OrderItemType.PRODUCT) && !orderItem.getType().equals(OrderItemType.MEALSET)) {
                //只对正常和套餐订单项进行退款
                continue;
            }
            totalOrderItemFee = totalOrderItemFee.add(orderItem.calculateOrderItemFee());
        }

        for (OrderItem orderItem : orderItems) {
            if (!orderItem.getType().equals(OrderItemType.PRODUCT) && !orderItem.getType().equals(OrderItemType.MEALSET)) {
                //只对正常和套餐订单项进行退款
                continue;
            }
            BigDecimal percent = new BigDecimal(0d);
            if (totalOrderItemFee.getAmount() != 0d) {
                percent = orderItem.calculateOrderItemFee().getAmountWithBigDecimal().divide(totalOrderItemFee.getAmountWithBigDecimal(), 2, RoundingMode.HALF_UP);
            }
            Money refundFee = Money.valueOf(totalRefundFee.getAmountWithBigDecimal().setScale(2, RoundingMode.HALF_UP).multiply(percent).doubleValue());

            refunds.add(createByOriginalRefund(originalRefund, RefundType.ORDER, orderItem.getId(), refundFee));
        }

        return refunds;

    }

    /**
     * 根据原始退款单和对应订单项创建退款记录
     *
     * @param originalRefund
     * @param payments
     * @return
     */
    public List<Refund> createByPayments(OriginalRefund originalRefund, List<Payment> payments) {
        if (log.isDebugEnabled()) {
            log.debug("根据预收款退款生成新的智库城退款,原始退款信息[id={}], 外部子订单号[{}]对应的预收款数量[{}]",
                    new Object[]{originalRefund.getId(), originalRefund.getOid(), payments.size()});
        }

        List<Refund> refunds = new ArrayList<Refund>();


        Money totalRefundFee = originalRefund.getRefundFee();
        Money totalPaymentFee = Money.valueOf(0d);
        for (Payment payment : payments) {
            totalPaymentFee = totalPaymentFee.add(payment.getPaymentFee());
        }

        for (Payment payment : payments) {
            BigDecimal percent = payment.getPaymentFee().getAmountWithBigDecimal().divide(totalPaymentFee.getAmountWithBigDecimal(), 2, RoundingMode.HALF_UP);
            Money refundFee = Money.valueOf(totalRefundFee.getAmountWithBigDecimal().setScale(2, RoundingMode.HALF_UP).multiply(percent).doubleValue());

            refunds.add(createByOriginalRefund(originalRefund, RefundType.PAYMENT, payment.getId(), refundFee));
        }

        return refunds;

    }

    /**
     * 根据原始退款信息创建线上退款记录
     *
     * @param originalRefund
     * @param refundType
     * @param referenceId
     * @param refundFee
     * @return
     */
    private Refund createByOriginalRefund(OriginalRefund originalRefund, RefundType refundType, Integer referenceId, Money refundFee) {

        Refund refund = new Refund();

        switch (originalRefund.getStatus()) {
            case GOODS_RETURNING:
            case WAIT_SELLER_AGREE:
                refund.setStatus(RefundStatus.IN_PROCESS);
                break;

            case SUCCESS:
                refund.setStatus(RefundStatus.SUCCESS);
                break;

            case SELLER_REFUSE:
            case CLOSED:
                refund.setStatus(RefundStatus.CLOSED);
                break;
        }
        refund.setType(refundType);
        switch (refundType) {
            case ORDER:
                refund.setOrderItemId(referenceId);
                refund.setOrderItem(orderService.findOrderItemById(referenceId));
                break;
            case PAYMENT:
                refund.setPaymentId(referenceId);
                refund.setPayment(paymentService.get(referenceId));
                break;
        }

        //退款阶段是根据订单交易状态来区分的
        RefundPhase refundPhase = originalRefund.getRefundPhase();
        refund.setPhase(refundPhase);

        refund.setPlatformType(originalRefund.getPlatformType());
        refund.setShopId(originalRefund.getShopId());
        refund.setPlatformRefundNo(originalRefund.getRefundId());
        refund.setOriginalRefundId(originalRefund.getId());
        refund.setOriginalRefund(originalRefund);

        refund.setAlsoReturn(false);
        refund.setBuyerId(originalRefund.getBuyerId());
        refund.setBuyerName(originalRefund.getBuyerNick());
        refund.setDescription(originalRefund.getDescription());
        refund.setOnline(true);
        refund.setReason(originalRefund.getReason());
        refund.setRefundTime(originalRefund.getModified());
        refund.setRefundFee(refundFee);
        refund.setActualRefundFee(refundFee);
        refund.setShippingNo(originalRefund.getSid());
        if (originalRefund.getCompanyName() != null) {
            refund.setShippingComp(originalRefund.getCompanyName().getName());
        }

        generalDAO.saveOrUpdate(refund);

        if (log.isDebugEnabled()) {
            log.debug("退款记录生成成功[id={},refundFee={}]", refund.getId(), refund.getRefundFee());
        }

        takeAffect(refund, null);

        return refund;
    }

    /**
     * 校正线上退款
     *
     * @param originalRefund
     */
    public void rollBackRefund(OriginalRefund originalRefund) {
        Refund refund = getByOriginalRefundId(originalRefund.getId());
        if (refund != null) {
            OrderItem orderItem = refund.getOrderItem();
            if (refund.getStatus().equals(RefundStatus.SUCCESS) && !originalRefund.getStatus().equals(OriginalRefundStatus.SUCCESS)) {
                if (originalRefund.getStatus().equals(OriginalRefundStatus.GOODS_RETURNING)
                        || originalRefund.getStatus().equals(OriginalRefundStatus.WAIT_SELLER_AGREE)) {
                    refund.setStatus(RefundStatus.IN_PROCESS);
                    if (orderItem != null) {
                        orderItem.setRefunding(true);
                    }
                }
                if (originalRefund.getStatus().equals(OriginalRefundStatus.CLOSED) ||
                        originalRefund.getStatus().equals(OriginalRefundStatus.SELLER_REFUSE)) {
                    refund.setStatus(RefundStatus.CLOSED);
                }
                if (orderItem != null) {
                    if (refund.isAlsoReturn()) {
                        orderItem.setReturnStatus(OrderItemReturnStatus.NORMAL);
                        orderItem.setReturnPostPayer(null);
                        orderItem.setReturnPostFee(Money.valueOf(0));
                    }
                    orderItem.setRefundFee(orderItem.getRefundFee().subtract(refund.getRefundFee()));
                    orderItem.setActualRefundFee(orderItem.getActualRefundFee().subtract(refund.getActualRefundFee()));

                    //修改订单项的相关金额
                    orderFeeService.calculateOrderItemFee(refund.getOrderItem());
                    generalDAO.saveOrUpdate(refund.getOrderItem());
                    //设置订单的退货状态
                    orderFeeService.checkOrderReturnStatus(refund.getOrderItem().getOrder());
                    //修改订单金额
                    orderFeeService.calculateOrderFee(refund.getOrderItem().getOrder(), refund.getOrderItem().getOrder().getOrderItemList());
                    generalDAO.saveOrUpdate(refund.getOrderItem().getOrder());

                }
                generalDAO.saveOrUpdate(refund);
            }
        }
    }

    /**
     * 根据原始退款Id
     *
     * @param id
     * @return
     */
    public Refund getByOriginalRefundId(Integer id) {
        Search search = new Search(Refund.class);
        search.addFilterEqual("originalRefundId", id);
        List<Refund> refundList = generalDAO.search(search);
        if (refundList.size() > 0) {
            return refundList.get(0);
        } else {
            return null;
        }
    }

    /**
     *
     * @param refunds
     * @param originalRefund
     * @return
     */
    public void updateByOriginalRefund(List<Refund> refunds, OriginalRefund originalRefund) {

        //修改退款单金额
        updateRefundFee(refunds, originalRefund);

        for(Refund refund : refunds) {
            log.debug("根据原始退款[id={}]对退款记录[id={}]进行更新", originalRefund.getId(), refund.getId());
            refund.setDescription(originalRefund.getDescription());
            refund.setReason(originalRefund.getReason());
            if (StringUtils.isBlank(refund.getShippingNo()) && StringUtils.isBlank(refund.getShippingComp())) {
                refund.setShippingNo(originalRefund.getSid());
                if (originalRefund.getCompanyName() != null) {
                    refund.setShippingComp(originalRefund.getCompanyName().getName());
                }
            }


            RefundStatus formerRefundStatus = refund.getStatus();
            if(RefundStatus.SUCCESS.equals(formerRefundStatus)) {
                //已经成功的不变化状态
                generalDAO.saveOrUpdate(refund);
                return;
            }
            switch (originalRefund.getStatus()) {
                case GOODS_RETURNING:
                case WAIT_SELLER_AGREE:
                    refund.setStatus(RefundStatus.IN_PROCESS);
                    break;

                case SUCCESS:
                    refund.setStatus(RefundStatus.SUCCESS);
                    break;

                case SELLER_REFUSE:
                case CLOSED:
                    refund.setStatus(RefundStatus.CLOSED);
                    break;
            }

            generalDAO.saveOrUpdate(refund);
            takeAffect(refund, formerRefundStatus);
        }

    }

    /**
     * 根据原始退款,修改退款金额
     * @param refunds
     * @param originalRefund
     */
    private void updateRefundFee(List<Refund> refunds, OriginalRefund originalRefund) {
        if(refunds.isEmpty()) return;
        if(refunds.size() == 1) {
            //一个原始退款单对应一个退款单,直接修改金额
            Refund refund = refunds.get(0);
            if(RefundStatus.SUCCESS.equals(refund.getStatus())) {
                //如果退款单已经成功,不修改金额
                return;
            }
            refund.setRefundFee(originalRefund.getRefundFee());
            refund.setActualRefundFee(originalRefund.getRefundFee());
        } else {
            //一个原始退款单对应多个退款单,按比例修改金额
            Money oldTotalFee = Money.valueOf(0d);
            Money totalRefundFee = originalRefund.getRefundFee();
            for(Refund refund : refunds) {
                if(RefundStatus.SUCCESS.equals(refund.getStatus())) {
                    //如果退款单已经成功,不修改金额
                    return;
                }
                oldTotalFee = oldTotalFee.add(refund.getRefundFee());
            }

            for(Refund refund : refunds) {
                BigDecimal percent = refund.getRefundFee().getAmountWithBigDecimal().divide(oldTotalFee.getAmountWithBigDecimal(), 2, RoundingMode.HALF_UP);
                Money refundFee = Money.valueOf(totalRefundFee.getAmountWithBigDecimal().setScale(2, RoundingMode.HALF_UP).multiply(percent).doubleValue());
                refund.setRefundFee(refundFee);
                refund.setActualRefundFee(refundFee);
            }
        }
    }

    /**
     * 抓取线上退款记录,根据退款之前的状态判断是否需要使该退款生效
     *
     * @param refund
     * @param formerRefundStatus
     */
    private void takeAffect(Refund refund, RefundStatus formerRefundStatus) {

        if (refund.getStatus().equals(formerRefundStatus)) {
            //状态无改变
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("退款记录从[{}]状态变更到[{}]状态", formerRefundStatus, refund.getStatus());
        }

        switch (refund.getStatus()) {
            case IN_PROCESS: {
                switch (refund.getType()) {
                    case ORDER: {
                        //将订单项标记为正在退款
                        orderService.markOrderItemRefunding(refund.getOrderItemId());
                        break;
                    }
                    case PAYMENT: {
                        break;
                    }
                }

                break;
            }
            case CLOSED: {
                switch (refund.getType()) {
                    case ORDER: {
                        //将订单项标记为没有退款
                        orderService.markOrderItemNotRefunding(refund.getOrderItemId());
                        break;
                    }
                    case PAYMENT: {
                        break;
                    }
                }
                break;
            }
            case SUCCESS: {
                //转变成退款成功的状态
                switch (refund.getPhase()) {
                    case ON_SALE: {
                        //售前退款=退货
                        refund.setAlsoReturn(true);
                        break;
                    }

                    case AFTER_SALE: {
                        break;

                    }
                }

                switch (refund.getType()) {
                    case ORDER: {
                        //订单退款
                        activationOnlineRefund(refund);
                        //添加会员日志
//                        memberInfoLogService.saveByOrderRefund(refund);
                        memberInfoLogService.save(null,refund,null,null,OrderLogType.REFUND,false);
                        break;
                    }

                    case PAYMENT: {
                        //预收款退款
                        paymentService.paymentReturn(refund.getPaymentId(), refund.getRefundFee());
//                        memberInfoLogService.saveByPaymentRefund(refund);
                        memberInfoLogService.save(null,refund,null,null,OrderLogType.REFUND,true);
                        break;
                    }
                }


                break;
            }
        }

    }

//    public List<Refund> findRefundByRefundTime() {
//        Search search = new Search(Refund.class);
//        search.addFilterEqual("status", RefundStatus.SUCCESS);
//        search.addSortAsc("refundTime");
//        return generalDAO.search(search);
//    }

    public Workbook reportRefund(Map<String, Object[]> map) {

        List<Refund> refundList = null;
        try {
            refundList = findRefund(map, null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        createExcelTitle(sheet);

        int rowIndex = 2;//从第三行开始，一二行放title
        for (Refund refund : refundList) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            renderRefund2Excel(row, cellIndex, refund);
        }
        return workbook;
    }

    private void renderRefund2Excel(Row itemRow, int startCellIndex, Refund refund) {
        //退款日期
        if (refund.getRefundTime() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getRefundTime());
        } else {
            startCellIndex++;
        }
        //店铺
        if (refund.getShop() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getShop().getNick());
        } else {
            startCellIndex++;
        }
        //退款类型
        PoiUtil.createCell(itemRow, startCellIndex++, refund.getType().getValue());
        //售后类型
        if (refund.isAlsoReturn()) {
            PoiUtil.createCell(itemRow, startCellIndex++, "退款退货");
        } else {
            PoiUtil.createCell(itemRow, startCellIndex++, "退款");
        }
        //状态
        PoiUtil.createCell(itemRow, startCellIndex++, refund.getStatus().getValue());

        //是否线上退货
        if (refund.isOnline()) {
            PoiUtil.createCell(itemRow, startCellIndex++, "是");
        } else {
            PoiUtil.createCell(itemRow, startCellIndex++, "否");
        }

        //外部平台订单编号
        if (refund.getOrderItem() != null && refund.getOrderItem().getPlatformOrderNo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getPlatformOrderNo());
        } else {
            startCellIndex++;
        }
        //智库城订单编号
        if (refund.getOrderItem() != null && refund.getOrderItem().getOrder().getOrderNo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getOrder().getOrderNo());
        } else {
            startCellIndex++;
        }

        //退款单号
        if (refund.getPlatformRefundNo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getPlatformRefundNo());
        } else {
            startCellIndex++;
        }
        //买家Id
        if (refund.getBuyerId() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getBuyerId());
        } else {
            startCellIndex++;
        }

        //收货人姓名
        if (refund.getOrderItem() != null && refund.getOrderItem().getOrder().getInvoice() != null &&
                refund.getOrderItem().getOrder().getInvoice().getReceiver().getReceiverName() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getOrder().getInvoice().getReceiver().getReceiverName());
        } else {
            startCellIndex++;
        }
        //联系电话
        if (refund.getOrderItem() != null && refund.getOrderItem().getOrder().getInvoice() != null &&
                refund.getOrderItem().getOrder().getInvoice().getReceiver().getReceiverPhone() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getOrder().getInvoice().getReceiver().getReceiverPhone());
        } else {
            startCellIndex++;
        }

        if (refund.getOrderItem() != null && refund.getOrderItem().getOrder().getInvoice() != null &&
                refund.getOrderItem().getOrder().getInvoice().getReceiver().getReceiverMobile() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getOrder().getInvoice().getReceiver().getReceiverMobile());
        } else {
            startCellIndex++;
        }

        if (refund.getOrderItem() != null && refund.getOrderItem().getProductCode() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getProductCode());
        } else {
            startCellIndex++;
        }
        //商品名称
        if (refund.getOrderItem() != null && refund.getOrderItem().getProductName() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getProductName());
        } else {
            startCellIndex++;
        }
        if (refund.getOrderItem() != null && refund.getOrderItem().getSpecInfo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getSpecInfo());
        } else {
            startCellIndex++;
        }
        //SKU
        if (refund.getOrderItem() != null && refund.getOrderItem().getProduct() != null
                && refund.getOrderItem().getProduct().getSku() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getProduct().getSku());
        } else {
            startCellIndex++;
        }
        //OuterSKU
        if (refund.getOrderItem() != null && refund.getOrderItem().getOuterSku() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getOuterSku());
        } else {
            startCellIndex++;
        }

        if (refund.getOrderItem() != null && refund.getOrderItem().getBuyCount() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getBuyCount());
        } else {
            startCellIndex++;
        }

        if (refund.getOrderItem() != null && refund.getOrderItem().getActualFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getActualFee().toString());
        } else {
            startCellIndex++;
        }
        if (refund.getOrderItem() != null && refund.getOrderItem().getGoodsFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getOrderItem().getGoodsFee().toString());
        } else {
            startCellIndex++;
        }

        if (refund.getRefundFee().toString() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getRefundFee().toString());
        } else {
            startCellIndex++;
        }

        if (refund.getPostFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getPostFee().toString());
        } else {
            startCellIndex++;
        }

        if (refund.getReason() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getReason());
        } else {
            startCellIndex++;
        }
        if (refund.getRemark() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getRemark());
        } else {
            startCellIndex++;
        }
        if (refund.getPostPayer() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getPostPayer().getValue());
        } else {
            startCellIndex++;
        }

        if (refund.getShippingComp() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, DeliveryType.valueOf(refund.getShippingComp()).toDesc());
        } else {
            startCellIndex++;
        }
        if (refund.getShippingNo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getShippingNo());
        } else {
            startCellIndex++;
        }
        if (refund.getRevisitTime() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, refund.getRevisitTime());
        } else {
            startCellIndex++;
        }


    }

    private void createExcelTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        PoiUtil.createCell(titleRow, 0, "导出的退款信息");
        int cellIndex = 0;
        Row row = sheet.createRow(1);
        PoiUtil.createCell(row, cellIndex++, "退款日期");
        PoiUtil.createCell(row, cellIndex++, "店铺");
        PoiUtil.createCell(row, cellIndex++, "退款类型");
        PoiUtil.createCell(row, cellIndex++, "售后类型");
        PoiUtil.createCell(row, cellIndex++, "状态");
        PoiUtil.createCell(row, cellIndex++, "是否线上退款");
        PoiUtil.createCell(row, cellIndex++, "外部平台订单编号");
        PoiUtil.createCell(row, cellIndex++, "智库城订单编号");
        PoiUtil.createCell(row, cellIndex++, "退款单号");
        PoiUtil.createCell(row, cellIndex++, "买家ID");
        PoiUtil.createCell(row, cellIndex++, "收货人姓名");
        PoiUtil.createCell(row, cellIndex++, "联系电话");
        PoiUtil.createCell(row, cellIndex++, "手机号码");
        PoiUtil.createCell(row, cellIndex++, "商品编码");
        PoiUtil.createCell(row, cellIndex++, "商品名称");
        PoiUtil.createCell(row, cellIndex++, "商品规格");
        PoiUtil.createCell(row, cellIndex++, "商品SKU");
        PoiUtil.createCell(row, cellIndex++, "外部平台商品条形码（京东）");
        PoiUtil.createCell(row, cellIndex++, "数量");
        PoiUtil.createCell(row, cellIndex++, "成交金额");
        PoiUtil.createCell(row, cellIndex++, "货款");
        PoiUtil.createCell(row, cellIndex++, "退款金额");
        PoiUtil.createCell(row, cellIndex++, "运费");
        PoiUtil.createCell(row, cellIndex++, "退款原因");
        PoiUtil.createCell(row, cellIndex++, "备注");
        PoiUtil.createCell(row, cellIndex++, "邮费承担方");
        PoiUtil.createCell(row, cellIndex++, "退回快递");
        PoiUtil.createCell(row, cellIndex++, "退回快递单号");
        PoiUtil.createCell(row, cellIndex++, "回访时间");
        int orderCellIndex = cellIndex - 1;
        sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, orderCellIndex));  //合并订单标题的单元格

    }

    /**
     * 退款作废
     *
     * @param refundId
     */
    public void cancellationRefund(Integer refundId) {
        Refund refund = generalDAO.get(Refund.class,refundId);
        RefundStatus oldStatus = refund.getStatus();
        if (oldStatus!=RefundStatus.IN_PROCESS) {
            throw new StewardBusinessException("只有状态为：｛正在申请｝的退款才能作废。");
        }
        refund.setStatus(RefundStatus.CLOSED);
        generalDAO.saveOrUpdate(refund);
        takeAffect(refund,oldStatus);
    }

}
