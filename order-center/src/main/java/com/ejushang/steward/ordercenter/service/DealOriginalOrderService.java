package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.rmi.IOriginalOrderRemoteService;
import com.ejushang.steward.ordercenter.util.DealOriginalOrderUtil;
import com.ejushang.steward.ordercenter.util.OrderUtil;
import com.ejushang.steward.ordercenter.vo.DealOriginalOrderItemVo;
import com.ejushang.steward.ordercenter.vo.DealOriginalOrderVo;
import com.ejushang.steward.ordercenter.vo.OrderAnalyzeLogVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * User: JBoss
 * Date: 14-7-8
 * Time: 上午9:43
 */
@Service
@Transactional
public class DealOriginalOrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private GeneralDAO generalDAO;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OriginalOrderService originalOrderService;
    @Autowired
    private MessageAnalyzeService messageAnalyzeService;
    @Autowired
    private IOriginalOrderRemoteService originalOrderRemoteService;

    /**
     * 查询订单
     *
     * @param map  controller接收过来的参数
     * @param page 分页参数
     * @return 返回List集合
     */

    @Transactional(readOnly = true)
    public Page findOriginalOrderDetails(Map<String, Object[]> map, Page page) throws ParseException {
        Map<String, String> map1 = new HashMap<String, String>();

        //将接受到的map参数解析并赋值给map1
        DealOriginalOrderUtil.getConditionMap(map, map1);
        //显示层的Vo的List
        List<DealOriginalOrderVo> orderVos = new ArrayList<DealOriginalOrderVo>();
        //HQL条件的值
        List<Object> objects = new ArrayList<Object>();
        //拼接HQL的变量
        StringBuilder stringBuilder = new StringBuilder();
        //拼接HQL的方法
        DealOriginalOrderUtil.orderCondition(map1, stringBuilder, objects);
        //执行HQL
        List<OriginalOrder> orders = generalDAO.query(stringBuilder.toString(), page, objects.toArray());
        //拼接Vo的循环
        for (OriginalOrder order : orders) {
            DealOriginalOrderVo dealOriginalOrderVo = new DealOriginalOrderVo();
            @SuppressWarnings("unchecked")
            List<OriginalOrderBrand> originalOrderBrands = generalDAO.search(new Search(OriginalOrderBrand.class).addFilterEqual("originalOrderId", order.getId()));
            List<String> brands = new ArrayList<String>();
            for (OriginalOrderBrand originalOrderBrand : originalOrderBrands) {
                if (!brands.contains(originalOrderBrand.getBrand().getName())) {
                    brands.add(originalOrderBrand.getBrand().getName());
                }
            }
            for (String brandName : brands) {
                dealOriginalOrderVo.setBrandName((dealOriginalOrderVo.getBrandName() != null ? dealOriginalOrderVo.getBrandName() + "," :"")+brandName);
            }
            if (order.getShopId() != null) {
                Shop shop = new Shop();
                shop.setId(order.getShopId());
                List<Shop> shops = shopService.getShop(shop);
                if (shops.size() > 0) {
                    dealOriginalOrderVo.setShopName(shops.get(0).getNick());
                }
            }
            DealOriginalOrderUtil.getDealOriginalOrderVo(order, orderVos, dealOriginalOrderVo);
        }
        if (page == null) {
            page = new Page(1, 1);
        }
        page.setResult(orderVos);
        return page;
    }

    /**
     * 通过原始订单id查询原始订单项
     *
     * @param originalOrderId 原始订单id
     * @return 返回原始订单项Vo
     */
    @Transactional(readOnly = true)
    public List<DealOriginalOrderItemVo> findDealOriginalItemVoById(Integer originalOrderId) {
        if (log.isInfoEnabled()) {
            log.info(String.format("originalOrderId：[%s]", originalOrderId));
        }
        List<DealOriginalOrderItemVo> dealOriginalOrderItemVos = new ArrayList<DealOriginalOrderItemVo>();
        DealOriginalOrderItemVo dealOriginalOrderItemVo;
        Search search = new Search(OriginalOrderItem.class);
        search.addFilterEqual("originalOrderId", originalOrderId);
        @SuppressWarnings("unchecked")
        List<OriginalOrderItem> originalOrderItems = generalDAO.search(search);
        for (OriginalOrderItem originalOrderItem : originalOrderItems) {
            dealOriginalOrderItemVo = new DealOriginalOrderItemVo();
            @SuppressWarnings("unchecked")
            List<OriginalOrderBrand> originalOrderBrands = generalDAO.search(new Search(OriginalOrderBrand.class).addFilterEqual("originalOrderItemId", originalOrderItem.getId()));
            List<String> brands = new ArrayList<String>();
            for (OriginalOrderBrand originalOrderBrand : originalOrderBrands) {
                Brand brand=originalOrderBrand.getBrand();
                if (brand == null) {
                    continue;
                }
                if (!brands.contains(brand.getName())) {
                    brands.add(brand.getName());
                }
            }
            StringBuilder brandNames=new StringBuilder();
            if(dealOriginalOrderItemVo.getBrandName()!=null){
                brandNames.append(dealOriginalOrderItemVo.getBrandName()).append(",");
            }
            for (String brandName : brands) {
               brandNames.append(brandName).append(",");
//                dealOriginalOrderItemVo.setBrandName((dealOriginalOrderItemVo.getBrandName() != null ? dealOriginalOrderItemVo.getBrandName() + "," : "") + brandName);
                dealOriginalOrderItemVo.setBrandName(brandNames.toString());
            }
            dealOriginalOrderItemVos.add(DealOriginalOrderUtil.getOrderItemVo(dealOriginalOrderItemVo, originalOrderItem));

        }
        //noinspection unchecked
        return dealOriginalOrderItemVos;

    }

    /**
     * 通过原始订单ID查询解析日志
     *
     * @param originalOrderId 原始订单ID
     * @return 返回日志Vo
     */
    @Transactional(readOnly = true)
    public List<OrderAnalyzeLogVo> findOrderAnalyzeLogByOriginalOrderId(Integer originalOrderId) {
        if (originalOrderId == null) {
            log.error("参数originalOrderId为空");
            throw new StewardBusinessException("原始订单ID为空");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("参数Integer类型的originalOrderId为[%s]", originalOrderId));
        }

        List<OrderAnalyzeLogVo> orderAnalyzeLogVos = new ArrayList<OrderAnalyzeLogVo>();
        @SuppressWarnings("unchecked")
        List<OrderAnalyzeLog> orderAnalyzeLogs = generalDAO.search(new Search(OrderAnalyzeLog.class).addFilterEqual("originalOrderId", originalOrderId));
        for (OrderAnalyzeLog orderAnalyzeLog : orderAnalyzeLogs) {
            OrderAnalyzeLogVo orderAnalyzeLogVo = new OrderAnalyzeLogVo();
            orderAnalyzeLogVo.setProcessed(orderAnalyzeLog.isProcessed() ? "解析成功" : "解析失败");
            orderAnalyzeLogVo.setCreateTime(orderAnalyzeLog.getCreateTime());
            orderAnalyzeLogVo.setMessage(orderAnalyzeLog.getMessage());
            orderAnalyzeLogVos.add(orderAnalyzeLogVo);
        }
        return orderAnalyzeLogVos;
    }

    /**
     * 修改原始订单项sku
     *
     * @param map 接收参数的MAP
     */
    @Transactional
    public void updateOriginalOrderItem(Map map) {
        String sku = map.get("sku").toString();
        Integer id = Integer.parseInt(map.get("id").toString());
        OriginalOrderItem originalOrderItem = generalDAO.get(OriginalOrderItem.class, id);
        originalOrderItem.setSku(sku);
        OriginalOrder originalOrder = originalOrderItem.getOriginalOrder();
        if (originalOrder.getDiscard()) {
            throw new StewardBusinessException("失效的原始订单不能执行更新操作");
        }
        if (originalOrder.getProcessed()) {
            throw new StewardBusinessException("已解析的原始订单不能执行更新操作");
        }
        originalOrderService.guessOriginalOrderBrand(originalOrder, true);
        try {
            generalDAO.saveOrUpdate(originalOrderItem);
        } catch (Exception e) {
            throw new StewardBusinessException("更新失败");
        }
    }

    /**
     * .原始订单作废
     *
     * @param id id数组
     */
    @Transactional
    public void cancellationOriginalOrder(int[] id) {
        if(id==null){
            log.error(String.format("参数id数组为空[%s]", Arrays.toString(id)));
            throw new StewardBusinessException("id参数为空");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数id:[%s]",id));
        }
        for (int orderId : id) {
            OriginalOrder originalOrder = generalDAO.get(OriginalOrder.class, orderId);
            if (originalOrder == null) {
                throw new StewardBusinessException("通过Id没有找到原始订单");
            }
            if (originalOrder.getDiscard()) {
                throw new StewardBusinessException("失效的原始订单不能执行作废操作");
            }
            if (originalOrder.getProcessed()) {
                throw new StewardBusinessException("已解析的原始订单不能执行作废操作");
            }

        }
        messageAnalyzeService.updateOriginalOrderDiscard(id, true);
    }

    /**
     * 原始订单恢复
     *
     * @param  id id数组
     */
    @Transactional
    public void recoverOriginalOrder(int[] id) {
        if(id==null){
            log.error(String.format("参数id数组为空[%s]",Arrays.toString(id)));
            throw new StewardBusinessException("id参数为空");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数id:[%s]",Arrays.toString(id)));
        }
        for (int orderId : id) {
            OriginalOrder originalOrder = generalDAO.get(OriginalOrder.class, orderId);
            if (!originalOrder.getDiscard()) {
                throw new StewardBusinessException("有效的原始订单不能执行恢复操作");
            }
        }
        messageAnalyzeService.updateOriginalOrderDiscard(id, false);
    }

    /**
     * 重新解析原始订单
     *
     * @param ids id数组
     */
    @Transactional
    public void analyzeOriginalOrder(String[] ids) {
        if(ids==null){
            log.error(String.format("参数ids数组为空[%s]",Arrays.toString(ids)));
            throw new StewardBusinessException("ids参数为空");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数id:[%s]",Arrays.toString(ids)));
        }
        List<Integer> idList = new ArrayList<Integer>();
        for (int i = 0; i < ids.length; i++) {
            idList.add(Integer.parseInt(ids[i]));
        }
        for (int orderId : idList) {
            OriginalOrder originalOrder = generalDAO.get(OriginalOrder.class, orderId);
            if (originalOrder.getDiscard()) {
                throw new StewardBusinessException("无效的原始订单不能执行解析操作");
            }
            if (originalOrder.getProcessed()) {
                throw new StewardBusinessException("已解析的原始订单不能执行解析操作");
            }
        }
        try {
            Boolean bool = originalOrderRemoteService.tryAnalyzeOriginalOrders(idList);
            if (!bool) {
                log.error("解析结果失败");
                throw new StewardBusinessException("解析结果失败");
            }
        } catch (Exception e) {
            log.error("解析原始订单抛异常，解析结果失败");
            throw new StewardBusinessException("解析原始订单抛异常，解析结果失败");
        }


    }

}
