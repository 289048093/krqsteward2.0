package com.ejushang.steward.ordercenter.data.init;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.OriginalOrderService;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-5-9
 * Time: 上午10:59
 * To change this template use File | Settings | File Templates.
 */
public class InitTest extends BaseTest{

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private ProductService productService;

    @Autowired
    private OriginalOrderService originalOrderService;

    @Test
    @Transactional
    @Rollback(false)           //插入10个店铺
    public void ShopInit(){
        List<Shop> shops = new ArrayList<Shop>();
        for (int i=1;i<=10;i++) {
            Shop shop = new Shop();
            shop.setCatId(RandomStringUtils.randomNumeric(6));  //所属类目ID
            shop.setNick("卖家名称" + RandomStringUtils.randomAlphabetic(3));
            shop.setTitle("标题" + RandomStringUtils.randomAlphabetic(3));
            shop.setDescription("描述" + RandomStringUtils.randomAlphabetic(3));
            shop.setBulletin("公告" + RandomStringUtils.randomAlphabetic(3));
            shop.setPicPath("地址" + RandomStringUtils.randomAlphabetic(3));
            shop.setItemScore(RandomStringUtils.randomNumeric(2));
            shop.setServiceScore(RandomStringUtils.randomNumeric(2));
            shop.setDeliveryScore(RandomStringUtils.randomNumeric(2));
            shop.setDeExpress("中通");
            shop.setEnableMsg(false);
            shop.setShopType(ShopType.VENDOR);
            shop.setMsgTemp("短信模版" + RandomStringUtils.randomAlphabetic(3));
            shop.setMsgSign("短信签名" + RandomStringUtils.randomAlphabetic(3));
            shop.setPlatformType(PlatformType.JING_DONG);
            shop.setShopAuth(null);
            shop.setUid(i+"");
            shops.add(shop);
        }
        generalDAO.saveOrUpdate(shops);
    }

    @Test
    @Transactional
    @Rollback(false)            //插入原始订单及对应订单项
    public void OriginalOrderAndItemInit(){
        List<Product> products = productService.findProductByAll(null,null,null,null);
        for (int i =1;i<=10;i++) {

            List<OriginalOrderItem> originalOrderItems = new ArrayList<OriginalOrderItem>();
            Money totalFee = Money.valueOf(0);
            totalFee = createItem(products, i, originalOrderItems, totalFee);
            OriginalOrder originalOrder = createOrder(totalFee);

            generalDAO.saveOrUpdate(originalOrder);
            for(int n=0;n<originalOrderItems.size();n++) {
                originalOrderItems.get(n).setOriginalOrderId(originalOrder.getId());
            }
            generalDAO.saveOrUpdate(originalOrderItems);
        }
    }

    @Test
    @Transactional
    @Rollback(false)             //插入智库城内部订单
    public  void OrderInit(){
        List<Order> orders = new ArrayList<Order>();
        Search search = new Search(OriginalOrder.class);
        List<OriginalOrder> originalOrders = generalDAO.search(search);
        for (int i=0;i<originalOrders.size();i++) {
            OriginalOrder originalOrder = originalOrders.get(i);
            Order order = new Order();
            order.setOrderNo(RandomStringUtils.randomNumeric(6));
            order.setType(OrderType.NORMAL);
            order.setStatus(OrderStatus.WAIT_APPROVE);
            order.setGenerateType(OrderGenerateType.AUTO_CREATE);
            order.setSharedDiscountFee(Money.valueOf(0));
            order.setSharedPostFee(Money.valueOf(0));
            order.setActualFee(originalOrder.getActualFee());
            order.setGoodsFee(originalOrder.getActualFee());
            order.setBuyerId(originalOrder.getBuyerId());
            order.setBuyerMessage(originalOrder.getBuyerMessage());
            order.setRemark(originalOrder.getRemark());
            order.setRepoId(1);
            order.setBuyTime(originalOrder.getBuyTime());
            order.setPayTime(originalOrder.getPayTime());
            order.setShopId(originalOrder.getShopId());
            order.setNeedReceipt(originalOrder.getNeedReceipt());
            order.setReceiptTitle(originalOrder.getReceiptTitle());
            order.setReceiptContent(originalOrder.getReceiptContent());
            order.setPlatformType(PlatformType.JING_DONG);
            order.setPlatformOrderNo(originalOrder.getPlatformOrderNo());
            order.setOriginalOrderId(originalOrder.getId());
            order.setValid(true);
            order.setOrderReturnStatus(OrderReturnStatus.NORMAL);
            orders.add(order);
        }
        generalDAO.saveOrUpdate(orders);
    }

    @Test
    @Transactional
    @Rollback(false)        //插入智库城内部订单项
    public void OrderItemInit(){
        int j=0;
        int k=0;
        Search search = new Search(OriginalOrderItem.class);
        List<OriginalOrderItem> originalOrderItems = generalDAO.search(search);
        Search searchOrder = new Search(Order.class);
        List<Order> orders = generalDAO.search(searchOrder);
        for(int i=0;i<originalOrderItems.size();i++) {
            OriginalOrderItem originalOrderItem = originalOrderItems.get(i);
            OriginalOrder originalOrder = generalDAO.get(OriginalOrder.class,originalOrderItem.getOriginalOrderId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOriginalOrderItemId(originalOrderItem.getId());
            orderItem.setPlatformSubOrderNo(originalOrderItem.getPlatformSubOrderNo());
            orderItem.setPlatformType(PlatformType.JING_DONG);
            orderItem.setPlatformOrderNo(originalOrder.getPlatformOrderNo());
            Product product = productService.findProductBySKU(originalOrderItem.getSku());
            orderItem.setProductId(product.getId());
            orderItem.setProductCode(product.getProductNo());
            orderItem.setProductSku(product.getSku());
            orderItem.setProductName(product.getName());
            orderItem.setPriceDescription("订单价格描述"+ RandomStringUtils.randomAlphabetic(3));
            orderItem.setOrderId(orders.get(j).getId());
            k++;
            if (k==3) {
                j++;
                k=0;
            }
            orderItem.setStatus(OrderItemStatus.NOT_SIGNED);
            orderItem.setType(OrderItemType.PRODUCT);
            orderItem.setReturnStatus(OrderItemReturnStatus.NORMAL);
            orderItem.setOfflineReturnStatus(OrderItemReturnStatus.NORMAL);
            orderItem.setExchanged(false);
            orderItem.setRefunding(false);
            orderItem.setPrice(originalOrderItem.getPrice());
            orderItem.setDiscountPrice(originalOrderItem.getPrice());
            orderItem.setBuyCount(originalOrderItem.getBuyCount().intValue());
            orderItem.setDiscountFee(originalOrderItem.getDiscountFee());
            orderItem.setSharedDiscountFee(Money.valueOf(0));
            orderItem.setSharedPostFee(Money.valueOf(0));
            orderItem.setActualFee(originalOrderItem.getPrice());
            orderItem.setRefundFee(Money.valueOf(0));
            orderItem.setOfflineRefundFee(Money.valueOf(0));
            orderItem.setActualRefundFee(Money.valueOf(0));
            orderItem.setServiceCoverFee(Money.valueOf(0));
            orderItem.setServiceCoverRefundFee(Money.valueOf(0));
            orderItem.setPostCoverFee(Money.valueOf(0));
            orderItem.setPostCoverRefundFee(Money.valueOf(0));
            orderItem.setOfflineReturnPostFee(Money.valueOf(0));
            orderItem.setReturnPostFee(Money.valueOf(0));
            orderItem.setExchangePostFee(Money.valueOf(0));
            orderItem.setReturnPostPayer(PostPayer.BUYER);
            orderItem.setOfflineReturnPostPayer(PostPayer.BUYER);
            orderItem.setExchangePostPayer(PostPayer.BUYER);
            orderItem.setValid(true);

            generalDAO.saveOrUpdate(orderItem);
        }
    }

    @Test
    @Transactional
    @Rollback(false)        //插入预收款
    public void paymentInit(){
        List<Payment> payments = new ArrayList<Payment>();
        Search searchOrder = new Search(Order.class);
        List<Order> orders = generalDAO.search(searchOrder);
        List<OriginalOrderItem> originalOrderItems = generalDAO.search(new Search(OriginalOrderItem.class));
        for(int i =0;i<10;i++) {
            Order order = orders.get(i);
            OriginalOrder originalOrder = generalDAO.get(OriginalOrder.class,order.getOriginalOrderId());
            Payment payment = new Payment();
            payment.setPlatformOrderNo(originalOrder.getPlatformOrderNo());
            payment.setOriginalOrderItemId(originalOrderItems.get(i).getId());
            payment.setPlatformType(PlatformType.JING_DONG);
            payment.setPlatformOrderNo(originalOrder.getPlatformOrderNo());
            payment.setOriginalOrderId(originalOrder.getId());
            payment.setBuyTime(originalOrder.getBuyTime());
            payment.setPayTime(originalOrder.getPayTime());
            payment.setBuyerId(originalOrder.getBuyerId());
            payment.setBuyerMessage(originalOrder.getBuyerMessage());
            payment.setRemark(originalOrder.getRemark());
            payment.setAllocateStatus(PaymentAllocateStatus.WAIT_ALLOCATE);   //可修改
            if (i%3==0) {
                payment.setType(PaymentType.POST_COVER);
            }
            if (i%3==1){
                payment.setType(PaymentType.SERVICE_COVER);
            }
            if (i%3==2){
                payment.setType(PaymentType.ORDER_POST_FEE);
            }
            payment.setShopId(originalOrder.getShopId());
            payment.setPaymentFee(Money.valueOf(RandomStringUtils.randomNumeric(2)));
            payment.setRefundFee(Money.valueOf(0));
            payments.add(payment);
        }
        generalDAO.saveOrUpdate(payments);
    }


    private OriginalOrder createOrder(Money totalFee) {
        OriginalOrder originalOrder = new OriginalOrder();
        Shop shop = getShop();
        originalOrder.setStatus(OriginalOrderStatus.WAIT_SELLER_SEND_GOODS.toString());
        originalOrder.setTotalFee(totalFee);
        originalOrder.setDiscountFee(Money.valueOf(0));
        originalOrder.setActualFee(totalFee);
        originalOrder.setHasPostFee(false);
        originalOrder.setPostFee(Money.valueOf(15));
        originalOrder.setAvailableConfirmFee(totalFee);
        originalOrder.setReceivedPayment(Money.valueOf(0));
        originalOrder.setAdjustFee(Money.valueOf(0));
        originalOrder.setPostFee(Money.valueOf(0));
        originalOrder.setRealPointFee((long) 0);
        originalOrder.setSellerDiscountFee(Money.valueOf(0));
        originalOrder.setSellerFee(totalFee);
        originalOrder.setBalancedUsed(Money.valueOf(0));
        originalOrder.setPayableFee(totalFee);
        originalOrder.setDeliveryType(DeliveryType.shunfeng.toString());
        originalOrder.setBuyerMessage("买家留言" + RandomStringUtils.randomAlphabetic(3));
        originalOrder.setRemark("客服备注" + RandomStringUtils.randomAlphabetic(3));
        originalOrder.setBuyerId("买家id" + RandomStringUtils.randomNumeric(3));
        originalOrder.setBuyTime(EJSDateUtils.getCurrentDate());
        originalOrder.setPayTime(EJSDateUtils.getCurrentDate());
        Receiver receiver = createReceiver();
        originalOrder.setReceiver(receiver);
        originalOrder.setPlatformType(PlatformType.JING_DONG);
        originalOrder.setPlatformOrderNo(RandomStringUtils.randomNumeric(8));
        originalOrder.setShopId(shop.getId());
        originalOrder.setOutShopId("店铺ID"+RandomStringUtils.randomNumeric(3));
        originalOrder.setNeedReceipt(false);
        originalOrder.setReceiptTitle("发票抬头"+RandomStringUtils.randomAlphabetic(3));
        originalOrder.setReceiptContent("发票内容"+RandomStringUtils.randomNumeric(3));
        originalOrder.setProcessed(false);
        return originalOrder;
    }

    private Shop getShop() {
        Search search = new Search(Shop.class);
        List<Shop> shops = generalDAO.search(search);
        Shop shop=shops.get(0);
        return shop;
    }

    private Receiver createReceiver() {
        Receiver receiver = new Receiver();
        receiver.setReceiverName("姓名" + RandomStringUtils.randomAlphabetic(3));
        receiver.setReceiverPhone("0759-" + RandomStringUtils.randomNumeric(8));
        receiver.setReceiverMobile("159"+ RandomStringUtils.randomNumeric(8));
        receiver.setReceiverZip(RandomStringUtils.randomNumeric(6));
        receiver.setReceiverState("省份"+ RandomStringUtils.randomAlphabetic(3));
        receiver.setReceiverCity("城市"+ RandomStringUtils.randomAlphabetic(3));
        receiver.setReceiverDistrict("地区"+ RandomStringUtils.randomAlphabetic(3));
        receiver.setReceiverAddress("详细地址"+ RandomStringUtils.randomAlphabetic(3));
        return receiver;
    }

    private Money createItem(List<Product> products, int i, List<OriginalOrderItem> originalOrderItems, Money totalFee) {
        for (int j = 1;j<4;j++) {
            OriginalOrderItem originalOrderItem = new OriginalOrderItem();
            if(products.size()>i) {
                originalOrderItem.setSku(products.get(i).getSku());
                Money price =  products.get(i).getMinimumPrice();
                originalOrderItem.setPrice(price);
                String count = RandomStringUtils.randomNumeric(1);
                Money itemTotalFee = price.multiply(Long.valueOf(count));
                totalFee = totalFee.add(itemTotalFee);
                originalOrderItem.setBuyCount((long)Integer.valueOf(count));
                originalOrderItem.setTotalFee(itemTotalFee);
                originalOrderItem.setPayableFee(itemTotalFee);
                originalOrderItem.setActualFee(itemTotalFee);
                originalOrderItem.setPlatformSubOrderNo(RandomStringUtils.randomNumeric(6));
                originalOrderItem.setDiscountFee(Money.valueOf(0));
                originalOrderItem.setAdjustFee(Money.valueOf(0));
                originalOrderItem.setDivideOrderFee(itemTotalFee);
                originalOrderItem.setPartMjzDiscount(Money.valueOf(0));
                originalOrderItems.add(originalOrderItem);
            }  else {
                break;
            }
        }
        return totalFee;
    }


    /**
     * 重置订单项的产品信息，防止同一订单的订单项出现相同的产品
     */
    @Test
    @Transactional
    @Rollback(false)
    public void resetOrderItem(){
        List<Order> os = generalDAO.findAll(Order.class);
        List<Repository> rs = generalDAO.findAll(Repository.class);
        List<Storage> ss = generalDAO.findAll(Storage.class);

        Random random = new Random();

        if(rs.isEmpty())return;

        Iterator<Repository> it = rs.iterator();

        for(Order o:os){
            List<OrderItem> is = o.getOrderItemList();
             Repository r = null;
            if(!it.hasNext()){
                it = rs.iterator();
            }
                r = it.next();
                o.setRepoId(r.getId());

            Search search = new Search(Storage.class);
            search.addFilterEqual("repositoryId",r.getId());
            List<Storage> ps = generalDAO.search(search);

            Iterator<Storage> pIt = ps.iterator();

            if(ps.isEmpty())continue;

            int size = ps.size();

            for(OrderItem i:is){
                Product p = null;
                if(!pIt.hasNext()){
                    pIt = ps.iterator();
                }

                p = pIt.next().getProduct();

                i.setProductId(p.getId());
                i.setProductName(p.getName());
                i.setProductCode(p.getProductNo());
                i.setProductSku(p.getSku());
                generalDAO.saveOrUpdate(i);
            }
            generalDAO.saveOrUpdate(o);

        }
    }

}
