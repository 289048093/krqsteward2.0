package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.*;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.util.OrderUtil;
import com.ejushang.steward.ordercenter.vo.FinancialOrderItemVo;
import com.ejushang.steward.ordercenter.vo.FinancialOrderVo;
import com.ejushang.steward.ordercenter.vo.OrderItemVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * User:moon
 * Date: 14-6-17
 * Time: 下午2:47
 */
@Service
@Transactional
public class FinancialService {

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private OrderApproveService orderApproveService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderFeeService orderFeeService;

    @Autowired
    private RefundService refundService;

    //财务模块页面
    @Transactional(readOnly = true)
    public Page financialOrder(String searchTimeType,String startTime, String endTime, String platformType, Integer shopId,Integer repoId,String status,Page page) {

        Query query =  listFinancialOrderVo(searchTimeType, startTime,  endTime,  platformType, shopId, repoId, status) ;
        int count = countFinancialOrderVoTotal(searchTimeType, startTime,  endTime,  platformType,  shopId, repoId, status);
        query.setFirstResult(page.getStart());
        query.setMaxResults(page.getPageSize());
        List<FinancialOrderVo> financialOrderVos = new ArrayList<FinancialOrderVo>();
        List<Object[]> list = query.list();

        List<OrderApprove> orderApproveList = orderApproveService.findByOrderStatus(OrderStatus.PRINTED);
        Map<Integer,OrderApprove> orderApproveMap=new HashMap<Integer, OrderApprove>();
        if(orderApproveList.size()>0){
            for(OrderApprove orderApprove:orderApproveList){
                orderApproveMap.put(orderApprove.getOrderId(),orderApprove);
            }
        }
        List<OrderApprove> orderApproves = orderApproveService.findByOrderStatus(OrderStatus.INVOICED);
        Map<Integer,OrderApprove> orderApprovesMap=new HashMap<Integer, OrderApprove>();
        if(orderApproves.size()>0){
            for(OrderApprove orderApprove:orderApproves){
                orderApprovesMap.put(orderApprove.getOrderId(),orderApprove);
            }
        }

        for(Object[] objs:list){
            OrderItem orderItem=(OrderItem)objs[0]; //Item
            Order order=(Order)objs[1]; //order
            FinancialOrderVo financialOrderVo = new FinancialOrderVo();
            financialOrderVo.setFinancialType("NORMAL");
            financialOrderVo = copyFinancialOrderVo(financialOrderVo, order, orderItem,searchTimeType,orderApproveMap,orderApprovesMap,null);
            financialOrderVos.add(financialOrderVo);
        }

        if(financialOrderVos.size()>0){
//            Collections.sort(financialOrderVos,new Comparator<FinancialOrderVo>() {
//                @Override
//                public int compare(FinancialOrderVo o1, FinancialOrderVo o2) {
//                    if(o1.getHappenTime()!=null && o2.getHappenTime()!=null){
//                        if( o1.getHappenTime().before(o2.getHappenTime())){
//                            return  1;
//                        }
//                    }
//                    return 0;
//                }
//            });
            Collections.sort(financialOrderVos,new Comparator<FinancialOrderVo>() {
                @Override
                public int compare(FinancialOrderVo o1, FinancialOrderVo o2) {
                    if(o1.getHappenTime()!=null && o2.getHappenTime()!=null){
                        if( o1.getHappenTime().equals(o2.getHappenTime())){
                            return 0;
                        } else if( o1.getHappenTime().before(o2.getHappenTime())){
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                    return 1;
                }
            });

            String listEndTime=EJSDateUtils.formatDate(financialOrderVos.get(financialOrderVos.size()-1).getHappenTime(),EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
            String listStartTime=EJSDateUtils.formatDate(financialOrderVos.get(0).getHappenTime(),EJSDateUtils.DateFormatType.DATE_FORMAT_STR);

            List<FinancialOrderVo> financialOrderVos1=findAllRefundByRefundTime(listEndTime,listStartTime,platformType,shopId,repoId);//,orderMap
            for(FinancialOrderVo financialOrderVo:financialOrderVos1){
                financialOrderVos.add(financialOrderVo);
            }
            int i=0;
            i=(count % page.getPageSize() == 0) ? (count / page.getPageSize()) : (count / page.getPageSize() + 1);
                if(page.getPageNo()==i){
                    List<FinancialOrderVo> financialOrderVos2=findAllRefundByRefundTime(startTime,listEndTime,platformType,shopId,repoId);//,orderMap);
                    for(FinancialOrderVo financialOrderVo:financialOrderVos2){
                        financialOrderVos.add(financialOrderVo);
                    }
                }

            if(page.getPageNo()==1){
                List<FinancialOrderVo> financialOrderVos3=findAllRefundByRefundTime(listStartTime,endTime,platformType,shopId,repoId);//,orderMap);
                for(FinancialOrderVo financialOrderVo:financialOrderVos3){
                    financialOrderVos.add(financialOrderVo);
                }
            }

        }else{
            List<FinancialOrderVo> financialOrderVos1=findAllRefundByRefundTime(startTime,endTime,platformType,shopId,repoId);//,orderMap);
            for(FinancialOrderVo financialOrderVo:financialOrderVos1){
                financialOrderVos.add(financialOrderVo);
            }
        }
        Collections.sort(financialOrderVos,new Comparator<FinancialOrderVo>() {
            @Override
            public int compare(FinancialOrderVo o1, FinancialOrderVo o2) {
                if(o1.getHappenTime()!=null && o2.getHappenTime()!=null){
                    if( o1.getHappenTime().equals(o2.getHappenTime())){
                        return 0;
                    } else if( o1.getHappenTime().before(o2.getHappenTime())){
                        return 1;
                    } else {
                        return -1;
                    }
                }
               return 1;
           }
        });
        page.setResult(financialOrderVos);
        page.setTotalCount(count);
        return page;

    }

    @Transactional(readOnly = true)
    public List<FinancialOrderVo> findAllRefundByRefundTime(String startPayTime, String endPayTime,String platformType,
                                                            Integer shopId,Integer repoId){//, Map<Integer,OrderItem> orderItemMap
        List<FinancialOrderVo> financialOrderVos=new ArrayList<FinancialOrderVo>();
        List<Refund> refundList = refundService.findByRefundTypeAndRefundTime(RefundType.ORDER, startPayTime, endPayTime,platformType);
        for(Refund refund:refundList){
            OrderItem orderItem=orderService.findOrderItemById(refund.getOrderItemId());
            Order order=orderService.findOrderById(orderItem.getOrderId());

            Boolean b=false;
            if(NumberUtil.isNullOrZero(shopId) && NumberUtil.isNullOrZero(repoId)){
                b=true;
            }else if(NumberUtil.isNullOrZero(shopId) && !NumberUtil.isNullOrZero(repoId) && order.getRepoId().equals(repoId)){
                b=true;
            }else if(NumberUtil.isNullOrZero(repoId) && !NumberUtil.isNullOrZero(shopId) && order.getShopId().equals(shopId)){
                b=true;
            }else if(!NumberUtil.isNullOrZero(shopId) && order.getShopId().equals(shopId)
                    && !NumberUtil.isNullOrZero(repoId) && order.getRepoId().equals(repoId)){
                b=true;
            }
            if(b){
                FinancialOrderVo financialRefundVo=new FinancialOrderVo();
                financialRefundVo.setFinancialType("REFUND");
                financialRefundVo.setHappenTime(refund.getRefundTime());
                financialRefundVo=copyFinancialOrderVo(financialRefundVo, order, orderItem,null, null,null,refund);
                financialOrderVos.add(financialRefundVo);
            }
        }
        return financialOrderVos;
    }

    /**
     * 获取结果集（分页）
     *
     * @param searchTimeType
     * @param startTime
     * @param endTime
     * @param platformType
     * @param shopId
     * @param repoId
     * @param status
     * @return
     */
    @Transactional(readOnly = true)
    private  Query listFinancialOrderVo(String searchTimeType,String startTime, String endTime, String platformType, Integer shopId,Integer repoId,String status) {
        StringBuilder hql = new StringBuilder("select item_,order_ from OrderItem item_ left join item_.order order_ where order_.valid=true ");
        List<String> paramers =  new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();

        //付款时间、打印时间、发货时间
        if(!StringUtils.isBlank(searchTimeType)){
            if(searchTimeType.equals("payTime")){
                if (!StringUtils.isBlank(startTime)) {
                    hql.append(" and order_.payTime>:minTime ");
                    paramers.add("minTime");
                    values.add(EJSDateUtils.parseDate(startTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
                if (!StringUtils.isBlank(endTime)) {
                    hql.append(" and order_.payTime<:maxTime ");
                    paramers.add("maxTime");
                    values.add(EJSDateUtils.parseDate(endTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }

            }else if(searchTimeType.equals("deliveryTime") || searchTimeType.equals("printTime")) {
                appendInvoiceParams(searchTimeType,startTime, endTime, hql, paramers, values);
            }

        }
        if (!StringUtils.isBlank(status)) {
            if(status.equals(OrderStatus.INVOICED.toString())){
//                hql.append(" and (order_.status='INVOICED' or order_.status='SIGNED') ");
                hql.append(" and (order_.status= '").append(OrderStatus.INVOICED).append("' or ")
                        .append(" order_.status= '").append(OrderStatus.SIGNED).append("') ");
            }else{
                hql.append(" and order_.status=:status ");
                paramers.add("status");
                values.add(OrderStatus.valueOf(status));
            }
        }
        if (!StringUtils.isBlank(platformType)) {
            hql.append(" and order_.platformType=:platformType ");
            paramers.add("platformType");
            values.add(PlatformType.valueOf(platformType));
        }
        if (!NumberUtil.isNullOrZero(repoId)) {
            hql.append(" and order_.repoId=:repoId ");
            paramers.add("repoId");
            values.add(repoId);
        }
        if (!NumberUtil.isNullOrZero(shopId)) {
            hql.append(" and order_.shopId=:shopId ");
            paramers.add("shopId");
            values.add(shopId);
        }

        //仓库权限
        Employee employee = SessionUtils.getEmployee();
        if (employee.isRepositoryEmployee()) {     //仓库人员只能查看自己管理的仓库的日志
//            hql.append(" and order_.repoId in ( select repo_.id from Repository repo_ where repo_.chargePersonId= ").append(employee.getId()).append(")");
            hql.append(" and order_.repoId in ( select repo_.repositoryId from RepositoryCharger repo_ where repo_.chargerId= ").append(employee.getId()).append(")");
        }
//        hql.append(" and (order_.type='NORMAL' or order_.type='REPLENISHMENT' or order_.type='EXCHANGE') ");
        hql.append(" and (order_.type= '").append(OrderType.NORMAL).append("' or ")
                .append(" order_.type= '").append(OrderType.REPLENISHMENT).append("' or ")
                .append(" order_.type= '").append(OrderType.EXCHANGE).append("') ");
        hql.append(" order by order_.payTime desc ");

        Query query =  generalDAO.getSession().createQuery(hql.toString());

        for (int i = 0; i < paramers.size(); i++) {
            query.setParameter(paramers.get(i), values.get(i));
        }
        return query;
    }

    @Transactional(readOnly = true)
    private int countFinancialOrderVoTotal(String searchTimeType,String startTime, String endTime, String platformType, Integer shopId,Integer repoId,String status) {
        StringBuilder countHql = new StringBuilder("select count(item_.id) from OrderItem item_ left join item_.order order_ where order_.valid=true ");
        List<String> paramers =  new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();

        //付款时间、打印时间、发货时间
        if(!StringUtils.isBlank(searchTimeType)){
            if(searchTimeType.equals("payTime")){
                if (!StringUtils.isBlank(startTime)) {
                    countHql.append(" and order_.payTime>:minTime ");
                    paramers.add("minTime");
                    values.add(EJSDateUtils.parseDate(startTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
                if (!StringUtils.isBlank(endTime)) {
                    countHql.append(" and order_.payTime<:maxTime ");
                    paramers.add("maxTime");
                    values.add(EJSDateUtils.parseDate(endTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }

            }else if(searchTimeType.equals("deliveryTime") || searchTimeType.equals("printTime")) {
                appendInvoiceParams(searchTimeType,startTime, endTime, countHql, paramers, values);
            }

        }
        if (!StringUtils.isBlank(status)) {
            if(status.equals(OrderStatus.INVOICED.toString())){
//                countHql.append(" and (order_.status='INVOICED' or order_.status='SIGNED') ");
                countHql.append(" and (order_.status= '").append(OrderStatus.INVOICED).append("' or ")
                        .append(" order_.status= '").append(OrderStatus.SIGNED).append("') ");
            }else{
                countHql.append(" and order_.status=:status ");
                paramers.add("status");
                values.add(OrderStatus.valueOf(status));
            }
        }
        if (!StringUtils.isBlank(platformType)) {
            countHql.append(" and order_.platformType=:platformType ");
            paramers.add("platformType");
            values.add(PlatformType.valueOf(platformType));
        }
        if (!NumberUtil.isNullOrZero(repoId)) {
            countHql.append(" and order_.repoId=:repoId ");
            paramers.add("repoId");
            values.add(repoId);
        }
        if (!NumberUtil.isNullOrZero(shopId)) {
            countHql.append(" and order_.shopId=:shopId ");
            paramers.add("shopId");
            values.add(shopId);
        }
        //仓库权限
        Employee employee = SessionUtils.getEmployee();
        if (employee.isRepositoryEmployee()) {     //仓库人员只能查看自己管理的仓库的财务数据
//            countHql.append(" and order_.repoId in ( select repo_.id from Repository repo_ where repo_.chargePersonId= ").append(employee.getId()).append(")");
            countHql.append(" and order_.repoId in ( select repo_.repositoryId from RepositoryCharger repo_ where repo_.chargerId= ").append(employee.getId()).append(")");

        }
//        countHql.append(" and (order_.type='NORMAL' or order_.type='REPLENISHMENT' or order_.type='EXCHANGE') ");
        countHql.append(" and (order_.type= '").append(OrderType.NORMAL).append("' or ")
                .append(" order_.type= '").append(OrderType.REPLENISHMENT).append("' or ")
                .append(" order_.type= '").append(OrderType.EXCHANGE).append("') ");
        Query countQuery = generalDAO.getSession().createQuery(countHql.toString());
        for (int i = 0; i < paramers.size(); i++) {
            countQuery.setParameter(paramers.get(i), values.get(i));
        }
        return ((Number) countQuery.uniqueResult()).intValue();
    }

    @Transactional(readOnly = true)
    private void appendInvoiceParams(String searchTimeType,String startTime, String endTime, StringBuilder hql, List<String> paramers, List<Object> values) {
        if(StringUtils.isNotBlank(startTime) || StringUtils.isNotBlank(endTime)){
        hql.append(" and order_.id in " +
                      "(select oa_.orderId from OrderApprove oa_ " +
                            "where 1=1 ");
            if(searchTimeType.equals("deliveryTime")){
                hql.append(" and oa_.orderStatus= '").append(OrderStatus.INVOICED).append("'");
            }
            if(searchTimeType.equals("printTime")){
                hql.append(" and oa_.orderStatus='").append(OrderStatus.PRINTED).append("'");
            }
            if(StringUtils.isNotBlank(startTime)){
                hql.append(" and oa_.updateTime >:minPrintedTime");
                paramers.add("minPrintedTime");
                values.add(EJSDateUtils.parseDate(startTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            }
            if(StringUtils.isNotBlank(endTime)){
                hql.append(" and oa_.updateTime<:maxPrintedTime");
                paramers.add("maxPrintedTime");
                values.add(EJSDateUtils.parseDate(endTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            }
            hql.append(")");
        }
    }

    @Transactional(readOnly = true)
    public List<FinancialOrderVo> financialOrderNoPage(String searchTimeType,String startTime, String endTime, String platformType, Integer shopId,Integer repoId,String status) {
        Query query =  listFinancialOrderVo(searchTimeType, startTime,  endTime,  platformType,  shopId, repoId, status) ;

        List<FinancialOrderVo> financialOrderVos = new ArrayList<FinancialOrderVo>();
        List<Object[]> list = query.list();

        List<OrderApprove> orderApproveList = orderApproveService.findByOrderStatus(OrderStatus.PRINTED);
        Map<Integer,OrderApprove> orderApproveMap=new HashMap<Integer, OrderApprove>();
        if(orderApproveList.size()>0){
            for(OrderApprove orderApprove:orderApproveList){
                orderApproveMap.put(orderApprove.getOrderId(),orderApprove);
            }
        }

        for(Object[] objs:list){
            OrderItem orderItem=(OrderItem)objs[0]; //Item
            Order order=(Order)objs[1]; //order
            FinancialOrderVo financialOrderVo = new FinancialOrderVo();
            financialOrderVo.setFinancialType("NORMAL");
            financialOrderVo.setOrderId(order.getId());
            financialOrderVo = copyFinancialOrderVo(financialOrderVo, order, orderItem,searchTimeType,orderApproveMap,null,null);
            financialOrderVos.add(financialOrderVo);
        }
        List<FinancialOrderVo> financialOrderVos1=findAllRefundByRefundTime(startTime,endTime,platformType,shopId,repoId);//,orderMap
        for(FinancialOrderVo financialOrderVo:financialOrderVos1){
            financialOrderVos.add(financialOrderVo);
        }
        Collections.sort(financialOrderVos,new Comparator<FinancialOrderVo>() {
            @Override
            public int compare(FinancialOrderVo o1, FinancialOrderVo o2) {
                if(o1.getHappenTime()!=null && o2.getHappenTime()!=null){
                    if( o1.getHappenTime().equals(o2.getHappenTime())){
                        return 0;
                    } else if( o1.getHappenTime().before(o2.getHappenTime())){
                        return 1;
                    } else {
                        return -1;
                    }
                }
                return 1;
            }
        });
        return financialOrderVos;
    }

    @Transactional(readOnly = true)
    public FinancialOrderVo copyFinancialOrderVo(FinancialOrderVo financialOrderVo, Order order, OrderItem orderItem,String searchTimeType,
                                                 Map<Integer,OrderApprove> orderApproveMap,Map<Integer,OrderApprove> orderApprovesMap,Refund refund
                                                ) {
        //首先设置与订单相关的字段
        financialOrderVo.setPlatformType(order.getPlatformType());
        financialOrderVo.setShopName(order.getShop().getNick());
        financialOrderVo.setPlatformOrderNo(order.getPlatformOrderNo());
        financialOrderVo.setOrderNo(order.getOrderNo());
        financialOrderVo.setBuyTime(order.getBuyTime());
        financialOrderVo.setPayTime(order.getPayTime());
        financialOrderVo.setStatus(order.getStatus().getValue());
        financialOrderVo.setRepoName(order.getRepo().getName());
        if(orderApproveMap!=null){
            for(Map.Entry<Integer,OrderApprove> approveEntry:orderApproveMap.entrySet()){
                if(approveEntry.getKey().equals(order.getId())){
                    financialOrderVo.setPrintTime(approveEntry.getValue().getUpdateTime());  //打印时间
                }
            }
        }
        financialOrderVo.setOrderSharedDiscountFee(order.getSharedDiscountFee().toString());
        financialOrderVo.setPostFee(order.getSharedPostFee().toString());
        financialOrderVo.setBuyerId(order.getBuyerId());
        if (order.getInvoice() != null) {
            if (order.getInvoice().getReceiver().getReceiverName() != null) {
                financialOrderVo.setReceiverName(order.getInvoice().getReceiver().getReceiverName());
            }
            if (order.getInvoice().getShippingComp() != null) {
                financialOrderVo.setShippingComp(DeliveryType.valueOf(order.getInvoice().getShippingComp()).getValue());
            }
            if (order.getInvoice().getShippingNo() != null) {
                financialOrderVo.setShippingNo(order.getInvoice().getShippingNo());
            }
        }
        if(searchTimeType!=null){
            if(searchTimeType.equals("printTime")){
                financialOrderVo.setHappenTime(financialOrderVo.getPrintTime());
            }else if(searchTimeType.equals("payTime")){
                financialOrderVo.setHappenTime(financialOrderVo.getPayTime());
            }else if(searchTimeType.equals("deliveryTime")){
                OrderApprove orderApprove=orderApprovesMap.get(order.getId());
                if(orderApprove!=null){
                    financialOrderVo.setHappenTime(orderApprove.getUpdateTime());
                }
            }
        }

//        再设置与订单项有关的字段

        FinancialOrderItemVo financialOrderItemVo = new FinancialOrderItemVo();
        OrderUtil.getFinancialOrderItemVo(financialOrderItemVo, orderItem);
        financialOrderItemVo.setType(orderItem.getType().getValue());  //订单项类型
        financialOrderItemVo.setStatus(orderItem.getStatus().getValue());//订单项状态

        financialOrderVo.setOuterSku(orderItem.getOuterSku());
        if(financialOrderVo.getFinancialType()!=null){
            if(financialOrderVo.getFinancialType().equals("REFUND")){
                financialOrderVo.setFinancialType("退款");
                financialOrderVo.setOrderSharedDiscountFee(Money.valueOf(0).toString());
                financialOrderVo.setPostFee(Money.valueOf(0).toString());
                financialOrderItemVo.setPrice(Money.valueOf(0));
                financialOrderItemVo.setDiscountPrice(Money.valueOf(0));
                financialOrderItemVo.setDiscountFee(Money.valueOf(0));
                financialOrderItemVo.setSharedDiscountFee(Money.valueOf(0));
                financialOrderItemVo.setSharedPostFee(Money.valueOf(0));
            }else{
                financialOrderVo.setFinancialType("正常");
                financialOrderItemVo.setRefundFee(Money.valueOf(0));
                financialOrderItemVo.setOfflineRefundFee(Money.valueOf(0));
                financialOrderItemVo.setReturnPostFee(Money.valueOf(0));
                financialOrderItemVo.setOfflineReturnPostFee(Money.valueOf(0));
            }
        }

        if(refund!=null){
            if (refund.isOnline()) {
                financialOrderItemVo.setRefundFee(refund.getRefundFee());
                financialOrderItemVo.setActualRefundFee(refund.getActualRefundFee());
                if(refund.isAlsoReturn()){
                    financialOrderItemVo.setReturnPostPayer(refund.getPostPayer());
                    financialOrderItemVo.setReturnPostFee(refund.getPostFee());
                }
            } else {
                financialOrderItemVo.setOfflineRefundFee(refund.getActualRefundFee());
                if(refund.isAlsoReturn()){
                    financialOrderItemVo.setOfflineReturnPostPayer(refund.getPostPayer());
                    financialOrderItemVo.setOfflineReturnPostFee(refund.getPostFee());
                }
            }
        }
        orderFeeService.calculateOrderItemFee(financialOrderItemVo);

        financialOrderVo.setOrderItemVo(financialOrderItemVo);
        return financialOrderVo;
    }

    @Transactional(readOnly = true)
    public Workbook reportMergerOrderItem(String searchTimeType,String startTime, String endTime, String platformType, Integer shopId,Integer repoId,String status) {
        List<FinancialOrderVo> orderList = financialOrderNoPage(searchTimeType,startTime, endTime, platformType, shopId,repoId,status);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        int  orderCellIndex=createMergerExcelTitle(sheet);
        int rowIndex = 2;//从第三行开始，一二行放title
        List<String> stringList=new ArrayList<String>();
        for (FinancialOrderVo financialOrderVo : orderList) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            int itemCellIndex = orderCellIndex + 1;
            if(financialOrderVo.getFinancialType().equals("正常") && !stringList.contains(financialOrderVo.getOrderNo())){ //
                List<OrderItem> orderItems=orderService.findOrderItemByOrderId(financialOrderVo.getOrderId());

                renderMerger2OrderItem2Excel(row, cellIndex, financialOrderVo);
                //Item
                for (int i = 0; orderItems != null && i < orderItems.size(); i++) {
                    OrderItem item = orderItems.get(i);
                    Row itemRow;
                    if (i > 0) {
                        itemRow = sheet.createRow(row.getRowNum() + i);  //如果商品条目>1则新建一行
                        rowIndex++;
                    } else {
                        itemRow = row;
                    }
                    FinancialOrderItemVo financialOrderItemVo=new FinancialOrderItemVo();
                    financialOrderItemVo=OrderUtil.getFinancialOrderItemVo(financialOrderItemVo,item);
                    financialOrderItemVo.setType(OrderItemType.valueOf(financialOrderItemVo.getType()).getValue());
                    financialOrderItemVo.setStatus(OrderItemStatus.valueOf(financialOrderItemVo.getStatus()).getValue());
                    renderMergerOrderItem2Excel(itemRow, itemCellIndex, financialOrderItemVo);
                }
                for (int i = 0; orderItems != null && i <= orderCellIndex; i++) {
                    sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + orderItems.size() - 1, i, i));  //合并订单的单元格
                }
                stringList.add(financialOrderVo.getOrderNo());
            }else if(financialOrderVo.getFinancialType().equals("正常") && stringList.contains(financialOrderVo.getOrderNo())){
                rowIndex--;
            }else if(financialOrderVo.getFinancialType().equals("退款")){
                renderMerger2OrderItem2Excel(row, cellIndex, financialOrderVo);
                renderMergerOrderItem2Excel(row, itemCellIndex, financialOrderVo.getOrderItemVo());
            }
        }
        return workbook;
    }

    @Transactional(readOnly = true)
    public Workbook reportOrderItem(String searchTimeType,String startTime, String endTime, String platformType, Integer shopId,Integer repoId,String status) {
        List<FinancialOrderVo> orderList = financialOrderNoPage(searchTimeType,startTime, endTime, platformType, shopId,repoId,status);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        int  orderCellIndex=createMergerExcelTitle(sheet);
        int rowIndex = 2;//从第三行开始，一二行放title
        List<String> stringList=new ArrayList<String>();
        for (FinancialOrderVo financialOrderVo : orderList) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            int itemCellIndex = orderCellIndex + 1;
            renderMerger2OrderItem2Excel(row, cellIndex, financialOrderVo);
            renderMergerOrderItem2Excel(row, itemCellIndex, financialOrderVo.getOrderItemVo());
        }
        return workbook;
    }

    /**
     * 得到有关订单项的信息
     * @param itemRow
     * @param startCellIndex
     * @param financialOrderItemVo
     */
    @Transactional(readOnly = true)
    private void renderMergerOrderItem2Excel(Row itemRow, int startCellIndex, FinancialOrderItemVo financialOrderItemVo) {
        if (financialOrderItemVo.getPlatformSubOrderNo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getPlatformSubOrderNo());
        } else {
            startCellIndex++;
        }
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getId());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getType());  //订单项类型
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getStatus());//订单项状态
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getReturnStatus().getValue());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getOfflineReturnStatus().getValue());
        if (financialOrderItemVo.getExchanged()) {
            PoiUtil.createCell(itemRow, startCellIndex++, "是");
        } else {
            PoiUtil.createCell(itemRow, startCellIndex++, "否");
        }
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getProductCode());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getProductName());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getSpecInfo());  //规格
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getProductSku());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getOuterSku());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getBrandName());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getCateName());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getPrice().toString());
        if (financialOrderItemVo.getDiscountPrice() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getDiscountPrice().toString());
        } else {
            startCellIndex++;
        }
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getBuyCount());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getRepoNum());
        if (financialOrderItemVo.getDiscountFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getDiscountFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getSharedDiscountFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getSharedDiscountFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getSharedPostFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++,financialOrderItemVo.getSharedPostFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getActualFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getActualFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getGoodsFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getGoodsFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getPostCoverFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getPostCoverFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getPostCoverRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getPostCoverRefundFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getServiceCoverFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getServiceCoverFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getServiceCoverRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getServiceCoverRefundFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getRefundFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getOfflineRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getOfflineRefundFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getOfflineRefundFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getReturnPostFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getReturnPostPayer() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getReturnPostPayer().getValue());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getOfflineReturnPostFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++,financialOrderItemVo.getOfflineReturnPostFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getOfflineReturnPostPayer() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getOfflineReturnPostPayer().getValue());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getExchangePostFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getExchangePostFee().toString());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getExchangePostPayer() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getExchangePostPayer().getValue());
        } else {
            startCellIndex++;
        }
        if (financialOrderItemVo.getPriceDescription() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderItemVo.getPriceDescription());
        } else {
            startCellIndex++;
        }
    }

    /**
     * 得到有关订单的信息
     * @param itemRow
     * @param startCellIndex
     * @param financialOrderVo
     */
    @Transactional(readOnly = true)
    private void renderMerger2OrderItem2Excel(Row itemRow, int startCellIndex, FinancialOrderVo financialOrderVo) {

        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getPlatformType().getValue());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getFinancialType());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getShopName());
        if (financialOrderVo.getPlatformOrderNo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getPlatformOrderNo());
        } else {
            startCellIndex++;
        }
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderNo());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getStatus());//订单状态
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getRepoName());//仓库

        if (financialOrderVo.getOrderSharedDiscountFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderSharedDiscountFee());
        } else {
            startCellIndex++;
        }
        if (financialOrderVo.getPostFee() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getPostFee());
        } else {
            startCellIndex++;
        }
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getHappenTime());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getBuyTime());
        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getPayTime());
        if (financialOrderVo.getPrintTime() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getPrintTime());  //打印时间
        } else {
            startCellIndex++;
        }
        if (financialOrderVo.getBuyerId() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getBuyerId());
        } else {
            startCellIndex++;
        }
        if (financialOrderVo.getReceiverName() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getReceiverName());
        } else {
            startCellIndex++;
        }
        if (financialOrderVo.getShippingComp() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getShippingComp());
        } else {
            startCellIndex++;
        }
        if (financialOrderVo.getShippingNo() != null) {
            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getShippingNo());
        }else {
            startCellIndex++;
        }
    }


//    @Transactional(readOnly = true)
//    private void renderOrderItem2Excel(Row itemRow, int startCellIndex, FinancialOrderVo financialOrderVo) {
//
////        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getPlatformType().getValue());
////        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getFinancialType());
////        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getShopName());
////        if (financialOrderVo.getPlatformOrderNo() != null) {
////            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getPlatformOrderNo());
////        } else {
////            startCellIndex++;
////        }
//        if (financialOrderVo.getPlatformOrderNo() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getPlatformSubOrderNo());
//        } else {
//            startCellIndex++;
//        }
////        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderNo());
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getId());
////        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getStatus());//订单状态
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getType());  //订单项类型
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getStatus());//订单项状态
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getReturnStatus().getValue());
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getOfflineReturnStatus().getValue());
//        if (financialOrderVo.getOrderItemVo().getExchanged()) {
//            PoiUtil.createCell(itemRow, startCellIndex++, "是");
//        } else {
//            PoiUtil.createCell(itemRow, startCellIndex++, "否");
//        }
////        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getHappenTime());
////        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getBuyTime());
////        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getPayTime());
////        if (financialOrderVo.getPrintTime() != null) {
////            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getPrintTime());  //打印时间
////        } else {
////            startCellIndex++;
////        }
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getProductCode());
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getProductName());
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getSpecInfo());  //规格
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getProductSku());
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOuterSku());
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getBrandName());
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getCateName());
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getPrice());
//
//        if (financialOrderVo.getOrderItemVo().getDiscountPrice() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getDiscountPrice());
//        } else {
//            startCellIndex++;
//        }
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getBuyCount());
//        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getRepoNum());
////        PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getRepoName());//仓库
////        if (financialOrderVo.getOrderSharedDiscountFee() != null) {
////            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderSharedDiscountFee());
////        } else {
////            startCellIndex++;
////        }
//        if (financialOrderVo.getOrderItemVo().getDiscountFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getDiscountFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getSharedDiscountFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getSharedDiscountFee());
//        } else {
//            startCellIndex++;
//        }
////        if (financialOrderVo.getPostFee() != null) {
////            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getPostFee());
////        } else {
////            startCellIndex++;
////        }
//        if (financialOrderVo.getOrderItemVo().getSharedPostFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getSharedPostFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getActualFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getActualFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getGoodsFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getGoodsFee());
//        } else {
//            startCellIndex++;
//        }
//
//        if (financialOrderVo.getOrderItemVo().getPostCoverFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getPostCoverFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getPostCoverRefundFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getPostCoverRefundFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getServiceCoverFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getServiceCoverFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getServiceCoverRefundFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getServiceCoverRefundFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getRefundFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getRefundFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getOfflineRefundFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getOfflineRefundFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getOfflineRefundFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getReturnPostFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getReturnPostPayer() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getReturnPostPayer().getValue());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getOfflineReturnPostFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getOfflineReturnPostFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getOfflineReturnPostPayer() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getOfflineReturnPostPayer().getValue());
//        } else {
//            startCellIndex++;
//        }
//
//        if (financialOrderVo.getOrderItemVo().getExchangePostFee() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getExchangePostFee());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getExchangePostPayer() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getExchangePostPayer().getValue());
//        } else {
//            startCellIndex++;
//        }
//        if (financialOrderVo.getOrderItemVo().getPriceDescription() != null) {
//            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getOrderItemVo().getPriceDescription());
//        } else {
//            startCellIndex++;
//        }
////        if (financialOrderVo.getBuyerId() != null) {
////            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getBuyerId());
////        } else {
////            startCellIndex++;
////        }
////        if (financialOrderVo.getReceiverName() != null) {
////            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getReceiverName());
////        } else {
////            startCellIndex++;
////        }
////        if (financialOrderVo.getShippingComp() != null) {
////            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getShippingComp());
////        } else {
////            startCellIndex++;
////        }
////        if (financialOrderVo.getShippingNo() != null) {
////            PoiUtil.createCell(itemRow, startCellIndex++, financialOrderVo.getShippingNo());
////        }
//    }

    private int createMergerExcelTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        PoiUtil.createCell(titleRow, 0, "导出的订单项数据");
        int cellIndex = 0;
        Row row = sheet.createRow(1);
        PoiUtil.createCell(row, cellIndex++, "平台类型");
        PoiUtil.createCell(row, cellIndex++, "数据类型"); //
        PoiUtil.createCell(row, cellIndex++, "店铺名称");
        PoiUtil.createCell(row, cellIndex++, "外部平台订单编号");
        PoiUtil.createCell(row, cellIndex++, "智库城订单编号");
        PoiUtil.createCell(row, cellIndex++, "订单状态");
        PoiUtil.createCell(row, cellIndex++, "仓库");
        PoiUtil.createCell(row, cellIndex++, "整单优惠金额");
        PoiUtil.createCell(row, cellIndex++, "邮费");
        PoiUtil.createCell(row, cellIndex++, "发生时间");//

        PoiUtil.createCell(row, cellIndex++, "下单时间");
        PoiUtil.createCell(row, cellIndex++, "付款时间");
        PoiUtil.createCell(row, cellIndex++, "打印时间");
        PoiUtil.createCell(row, cellIndex++, "买家ID");
        PoiUtil.createCell(row, cellIndex++, "收货人");
        PoiUtil.createCell(row, cellIndex++, "快递公司");
        PoiUtil.createCell(row, cellIndex++, "快递单号");
        int orderCellIndex = cellIndex - 1;
        PoiUtil.createCell(row, cellIndex++, "外部平台订单项编号");
        PoiUtil.createCell(row, cellIndex++, "智库城订单项编号");
        PoiUtil.createCell(row, cellIndex++, "订单项类型");
        PoiUtil.createCell(row, cellIndex++, "订单项状态");
        PoiUtil.createCell(row, cellIndex++, "线上退货状态");
        PoiUtil.createCell(row, cellIndex++, "线下退货状态");
        PoiUtil.createCell(row, cellIndex++, "换货状态");
        PoiUtil.createCell(row, cellIndex++, "商品编号");
        PoiUtil.createCell(row, cellIndex++, "商品名称");
        PoiUtil.createCell(row, cellIndex++, "商品规格");

        PoiUtil.createCell(row, cellIndex++, "sku");
        PoiUtil.createCell(row, cellIndex++, "外部平台sku");//
        PoiUtil.createCell(row, cellIndex++, "品牌");
        PoiUtil.createCell(row, cellIndex++, "类别");
        PoiUtil.createCell(row, cellIndex++, "原价（一口价）");
        PoiUtil.createCell(row, cellIndex++, "促销价");
        PoiUtil.createCell(row, cellIndex++, "订货数量");
        PoiUtil.createCell(row, cellIndex++, "库存");
        PoiUtil.createCell(row, cellIndex++, "订单项优惠金额");
        PoiUtil.createCell(row, cellIndex++, "分摊优惠金额");

        PoiUtil.createCell(row, cellIndex++, "分摊邮费");
        PoiUtil.createCell(row, cellIndex++, "平台结算金额");
        PoiUtil.createCell(row, cellIndex++, "货款");
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
        PoiUtil.createCell(row, cellIndex++, "价格描述");

        sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, orderCellIndex));  //合并订单标题的单元格
        return  orderCellIndex;
    }

//    private void createExcelTitle(Sheet sheet) {
//        Row titleRow = sheet.createRow(0);
//        PoiUtil.createCell(titleRow, 0, "导出的订单项数据");
//        int cellIndex = 0;
//        Row row = sheet.createRow(1);
//        PoiUtil.createCell(row, cellIndex++, "平台类型");
//        PoiUtil.createCell(row, cellIndex++, "数据类型"); //
//        PoiUtil.createCell(row, cellIndex++, "店铺名称");
//        PoiUtil.createCell(row, cellIndex++, "外部平台订单编号");
//        PoiUtil.createCell(row, cellIndex++, "外部平台订单项编号");
//        PoiUtil.createCell(row, cellIndex++, "智库城订单编号");
//        PoiUtil.createCell(row, cellIndex++, "智库城订单项编号");
//        PoiUtil.createCell(row, cellIndex++, "订单状态");
//        PoiUtil.createCell(row, cellIndex++, "订单项类型");
//        PoiUtil.createCell(row, cellIndex++, "订单项状态");
//        PoiUtil.createCell(row, cellIndex++, "线上退货状态");
//        PoiUtil.createCell(row, cellIndex++, "线下退货状态");
//
//        PoiUtil.createCell(row, cellIndex++, "换货状态");
//        PoiUtil.createCell(row, cellIndex++, "发生时间");//
//        PoiUtil.createCell(row, cellIndex++, "下单时间");
//        PoiUtil.createCell(row, cellIndex++, "付款时间");
//        PoiUtil.createCell(row, cellIndex++, "打印时间");
//        PoiUtil.createCell(row, cellIndex++, "商品编号");
//        PoiUtil.createCell(row, cellIndex++, "商品名称");
//        PoiUtil.createCell(row, cellIndex++, "商品规格");
//        PoiUtil.createCell(row, cellIndex++, "sku");
//        PoiUtil.createCell(row, cellIndex++, "外部平台sku");//
//        PoiUtil.createCell(row, cellIndex++, "品牌");
//        PoiUtil.createCell(row, cellIndex++, "类别");
//        PoiUtil.createCell(row, cellIndex++, "原价（一口价）");
//
//        PoiUtil.createCell(row, cellIndex++, "促销价");
//        PoiUtil.createCell(row, cellIndex++, "订货数量");
//        PoiUtil.createCell(row, cellIndex++, "库存");
//        PoiUtil.createCell(row, cellIndex++, "仓库");
//        PoiUtil.createCell(row, cellIndex++, "整单优惠金额");
//        PoiUtil.createCell(row, cellIndex++, "订单项优惠金额");
//        PoiUtil.createCell(row, cellIndex++, "分摊优惠金额");
//        PoiUtil.createCell(row, cellIndex++, "邮费");
//        PoiUtil.createCell(row, cellIndex++, "分摊邮费");
//        PoiUtil.createCell(row, cellIndex++, "成交金额");
//        PoiUtil.createCell(row, cellIndex++, "货款");
//
//        PoiUtil.createCell(row, cellIndex++, "邮费补差金额");
//        PoiUtil.createCell(row, cellIndex++, "邮费补差退款金额");
//        PoiUtil.createCell(row, cellIndex++, "服务补差金额");
//        PoiUtil.createCell(row, cellIndex++, "服务补差退款金额");
//        PoiUtil.createCell(row, cellIndex++, "线上退款金额");
//        PoiUtil.createCell(row, cellIndex++, "线下退款金额");
//        PoiUtil.createCell(row, cellIndex++, "线上退货邮费");
//        PoiUtil.createCell(row, cellIndex++, "线上退货邮费承担方");
//        PoiUtil.createCell(row, cellIndex++, "线下退货邮费");
//        PoiUtil.createCell(row, cellIndex++, "线下退货邮费承担方");
//
//        PoiUtil.createCell(row, cellIndex++, "线下换货邮费");
//        PoiUtil.createCell(row, cellIndex++, "线下换货邮费承担方");
//        PoiUtil.createCell(row, cellIndex++, "价格描述");
//        PoiUtil.createCell(row, cellIndex++, "买家ID");
//        PoiUtil.createCell(row, cellIndex++, "收货人");
//        PoiUtil.createCell(row, cellIndex++, "快递公司");
//        PoiUtil.createCell(row, cellIndex++, "快递单号");
//
//        int orderCellIndex = cellIndex - 1;
//        sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, orderCellIndex));  //合并订单标题的单元格
//
//    }

}
