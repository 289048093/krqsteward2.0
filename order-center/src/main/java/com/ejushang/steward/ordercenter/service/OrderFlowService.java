package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.AppConfig;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.ordercenter.bean.LogisticsBean;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.api.impl.jd.JingDongLogisticsApiService;
import com.ejushang.steward.ordercenter.service.api.impl.tb.TaoBaoLogisticsApiService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 订单状态改变Service
 * User: liubin
 * Date: 14-1-17
 */
@Service
public class OrderFlowService {

    private static final Logger log = LoggerFactory.getLogger(OrderFlowService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private OrderApproveService orderApproveService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private JingDongLogisticsApiService jingDongLogisticsApiService;

    @Autowired
    private TaoBaoLogisticsApiService taoBaoLogisticsApiService;


    @Transactional
    public void changeStatus(Order order, OrderStatus from, OrderStatus to, boolean strict) {
        if (from == to) return;
        if (from == null || to == null) {
            throw new StewardBusinessException(String.format("订单状态变更失败,订单状态为null.原始状态:%s, 目标状态:%s", from, to));
        }
        if (!from.toString().equals(order.getStatus().toString())) {
            throw new StewardBusinessException(String.format("订单状态变更失败,原始订单状态与期待的不一致.原始订单状态:%s, 期待状态:%s", order.getStatus(), from));
        }
        if (from.canBackTo(to, strict)) {
            List<OrderStatus> orderStatusList = to.getAllNodesOnPath(from);
            if (orderStatusList.isEmpty()) {
                log.error(String.format("getAllNodesOnPath查询出来为空集合,程序有bug? 原始状态:%s, 目标状态:%s", from, to));
                return;
            }
            //倒序遍历,最后一个节点不执行方法
            for (int i = orderStatusList.size() - 1; i > 0; i--) {
                OrderStatus orderStatus = orderStatusList.get(i);
                OrderStatus nextOrderStatus = orderStatusList.get(i - 1);
                OrderFlow orderFlow = OrderFlow.getOrderFlow(orderStatus, nextOrderStatus);
                if (orderFlow != null) {
                    callMethod(orderFlow.getCancelMethodName(), order);
                }
            }
        } else if (from.canNextTo(to, strict)) {
            List<OrderStatus> orderStatusList = from.getAllNodesOnPath(to);
            if (orderStatusList.isEmpty()) {
                log.error(String.format("getAllNodesOnPath查询出来为空集合,程序有bug? 原始状态:%s, 目标状态:%s", from, to));
                return;
            }
            //最后一个节点不执行方法
            for (int i = 0; i < orderStatusList.size() - 1; i++) {
                OrderStatus orderStatus = orderStatusList.get(i);
                OrderStatus nextOrderStatus = orderStatusList.get(i + 1);
                OrderFlow orderFlow = OrderFlow.getOrderFlow(orderStatus, nextOrderStatus);
                if (orderFlow != null) {
                    callMethod(orderFlow.getDoMethodName(), order);
                }
            }
        } else {
            throw new StewardBusinessException(String.format("订单状态变更失败,不允许变更到指定状态.原始状态:%s, 目标状态:%s", from, to));
        }

        generalDAO.saveOrUpdate(order);
        Employee employee = SessionUtils.getEmployee();
        //保存订单审核记录
        saveOrUpdateOrderApprove(order, to, employee);
        //保存订单操作记录
        saveOrUpdateOrderHandleLog(order, from, to, employee);
    }

    private void saveOrUpdateOrderHandleLog(Order order, OrderStatus from, OrderStatus to, Employee employee) {
        OrderHandleLog orderHandleLog = new OrderHandleLog();
        if(employee != null) {
            orderHandleLog.setOperatorId(employee.getId());
        }
        orderHandleLog.setOrderId(order.getId());
        orderHandleLog.setFromStatus(from);
        orderHandleLog.setToStatus(to);
        generalDAO.saveOrUpdate(orderHandleLog);
    }

    private void saveOrUpdateOrderApprove(Order order, OrderStatus to, Employee employee) {
        Search search = new Search(OrderApprove.class).addFilterEqual("orderId", order.getId()).addFilterEqual("orderStatus", to);
        OrderApprove orderApprove = (OrderApprove) generalDAO.searchUnique(search);
        if (orderApprove == null) {
            orderApprove = new OrderApprove();
            orderApprove.setOrderStatus(to);
            orderApprove.setOrderId(order.getId());
        }
        if(employee != null) {
            orderApprove.setOperatorId(employee.getId());
        }
        generalDAO.saveOrUpdate(orderApprove);
    }

    private void callMethod(String methodName, Order order) {
        try {
            Method method = OrderFlowService.class.getMethod(methodName, Order.class);
            method.invoke(this, order);
        } catch (StewardBusinessException e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = "订单状态变更过程中调用方法失败,methodName:" + methodName;
            log.error(errorMsg, e);
            throw new StewardBusinessException("订单操作失败, 请联系管理员");
        }
    }

    @Transactional
    public void doApprove(Order order) {
        order.setStatus(OrderStatus.WAIT_PROCESS);
    }

    @Transactional
    public void cancelApprove(Order order) {
        order.setStatus(OrderStatus.WAIT_APPROVE);
    }

    @Transactional
    public void doConfirm(Order order) {
        // OrderUtil.checkSplitStatus(order, "导入进销存");
        order.setStatus(OrderStatus.CONFIRMED);
        for(OrderItem orderItem:order.getOrderItemList())
        {
            if(orderItem.getProductId()==null){
                throw new StewardBusinessException("订单项里的productId为空值,不可执行此操作");
            }
           storageService.storageReduce(orderItem.getProductId(),orderItem.getBuyCount(),InOutStockType.OUT_STOCK_TYPE_SELL,order.getId(),"导入进销存确认订单减少库存数量",false);
        }


        /*Search search = new Search(OrderItem.class);
        search.addFilterEqual("orderId",order.getId());
        List<OrderItem> orderItemList = generalDAO.search(search);
        if (orderItemList != null) {
            for (OrderItem orderItem : orderItemList) {
                storageService.manipulateStorage(orderItem.getProdId(), -Math.abs(orderItem.getProdCount()));
                prodSalesService.updateProdSalesSaleCount(orderItem.getProdId(), Math.abs(orderItem.getProdCount()));
            }
        }*/
    }

    @Transactional
    public void cancelConfirm(Order order) {
        String desc=null;
        if(order.getStatus().equals(OrderStatus.INVALID)){
           desc="订单作废订单返回库存数量";
        }
         else{
            desc="撤销订单返回库存数量";
        }
        order.setStatus(OrderStatus.WAIT_PROCESS);
        for(OrderItem orderItem:order.getOrderItemList())
        {
            if(orderItem.getProductId()==null){
                throw new StewardBusinessException("订单项里的productId为空值,不可执行此操作");
            }
            storageService.storageIncrement(orderItem.getProductId(),order.getRepoId(),orderItem.getBuyCount(),InOutStockType.OUT_STOCK_TYPE_SELL,desc,false);
        }
       /* order.setConfirmUserId(null);
        order.setConfirmUser(null);
        order.setConfirmTime(null);
        order.setShippingNo(null);
        List<OrderItem> orderItemList = orderItemService.findOrderItemByOrderId(order.getId());
        if (orderItemList != null) {
            for (OrderItem orderItem : orderItemList) {
                storageService.manipulateStorage(orderItem.getProdId(), Math.abs(orderItem.getProdCount()));
                prodSalesService.updateProdSalesSaleCount(orderItem.getProdId(), -Math.abs(orderItem.getProdCount()));
            }
        }*/
    }

    @Transactional
    public void doPrint(Order order) {
        order.setStatus(OrderStatus.PRINTED);
        System.out.println("运行到这里" + order.getStatus());

    }

    @Transactional
    public void cancelPrint(Order order) {
        order.setStatus(OrderStatus.CONFIRMED);
    }

    @Transactional
    public void doExamine(Order order) throws Exception {
        order.setStatus(OrderStatus.EXAMINED);

        try {
            LogisticsBean logisticsBean = structLogisticsBean(order);
            // 判断订单来源是否为平台新建订单
            if(!StringUtils.isBlank(logisticsBean.getOutOrderNo())){
                // 不为平台新建订单，才进行同步操作
                // 判断来自哪个平台
                // 线上才正式发货
                String online = AppConfig.getInstance().getProperty("online");
                if(StringUtils.equalsIgnoreCase("true",online)) {
                    if (StringUtils.equals(PlatformType.TAO_BAO.toString(), logisticsBean.getOutPlatform())) {
                        // 来自淘宝平台
                        taoBaoLogisticsApiService.sendLogisticsOnline(logisticsBean);
                    } else if (StringUtils.equals(PlatformType.JING_DONG.toString(), logisticsBean.getOutPlatform())) {
                        // 来自京东平台
                        jingDongLogisticsApiService.sendLogisticsOnline(logisticsBean);
                    }
                }
            }
        } catch (Exception e) {
            String errorMsg = String.format("订单[id=%d, orderNo=%s]在请求外部平台确认发货的时候发生错误", order.getId(), order.getOrderNo());
            log.error(errorMsg, e);
            //记录到数据库里,不抛出错误
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.setTitle(errorMsg);
            errorInfo.setDetail(e.getMessage());
            errorInfo.setExtraInfoOne(String.valueOf(order.getId()));
            errorInfo.setExtraInfoTwo(order.getOrderNo());
            generalDAO.saveOrUpdate(errorInfo);
        }
    }

    /**
     * 构造logisticsBean信息
     * @param order
     * @return
     */
    public LogisticsBean structLogisticsBean(Order order){
        LogisticsBean logisticsBean = new LogisticsBean();
        // 设置订单号
        logisticsBean.setOrderNo(order.getOrderNo());
        // 设置原始订单号
        logisticsBean.setOutOrderNo(order.getPlatformOrderNo());
        // 设置外部平台
        logisticsBean.setOutPlatform(order.getPlatformType().toString());
        // 设置店铺id
        logisticsBean.setShopId(Long.valueOf(order.getShopId()));

        Invoice invoice = invoiceService.getInvoiceById(order.getInvoiceId());
        if(invoice == null){
            throw new StewardBusinessException("业务异常：订单id【"+order.getId()+"】找不到发货信息，invoiceId【"+order.getInvoiceId()+"】");
        }
        // 设置物流公司代号
        logisticsBean.setExpressCompany(invoice.getShippingComp());
        // 设置物流单号
        logisticsBean.setExpressNo(invoice.getShippingNo());
        // 设置收货人地址
        logisticsBean.setReceiveAddress(StringUtils.trimToEmpty(invoice.getReceiver().getReceiverState())
                +StringUtils.trimToEmpty(invoice.getReceiver().getReceiverCity())
                +StringUtils.trimToEmpty(invoice.getReceiver().getReceiverDistrict())
                +StringUtils.trimToEmpty(invoice.getReceiver().getReceiverAddress()));

        Shop shop = shopService.getById(order.getShopId());
        if(shop == null){
            throw new StewardBusinessException("业务异常：订单id【"+order.getId()+"】找不到店铺信息，shopId【"+order.getShopId()+"】");
        }
        logisticsBean.setSellerNick(shop.getNick());
        if(!NumberUtil.isNullOrZero(shop.getShopAuthId())) {
            ShopAuth shopAuth = shopService.getShopAuthById(shop.getShopAuthId());
            if (shopAuth == null) {
                throw new StewardBusinessException("业务异常：订单id【" + order.getId() + "】找不到店铺授权信息，shopAuthId【" + shop.getShopAuthId() + "】");
            }
            // 设置sessionKey
            logisticsBean.setSessionKey(shopAuth.getSessionKey());
        }
        return logisticsBean;
    }

    @Transactional
    public void cancelExamine(Order order) {
        order.setStatus(OrderStatus.PRINTED);
    }

    @Transactional
    public void doInvoice(Order order) throws Exception {
        order.setStatus(OrderStatus.INVOICED);
    }

    @Transactional
    public void cancelInvoice(Order order) {
        throw new UnsupportedOperationException("不能取消发货");
    }

    @Transactional
    public void doSign(Order order) throws Exception {
        List<OrderItem> orderItemList = order.getOrderItemList();
        for(OrderItem orderItem : orderItemList) {
            if(OrderItemStatus.NOT_SIGNED.equals(orderItem.getStatus())) {
                orderItem.setStatus(OrderItemStatus.SIGNED);
                generalDAO.saveOrUpdate(orderItem);
            }
        }
        order.setStatus(OrderStatus.SIGNED);
    }

    @Transactional
    public void cancelSign(Order order) {
        throw new UnsupportedOperationException("不能取消签收");
    }

    @Transactional
    public void doInvalid(Order order) {
        order.setStatus(OrderStatus.INVALID);
    }

    @Transactional
    public void cancelInvalid(Order order) {
        order.setStatus(OrderStatus.WAIT_APPROVE);
    }

}
