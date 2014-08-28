package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.domain.Conf;
import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.DeliveryType;
import com.ejushang.steward.ordercenter.constant.OriginalOrderStatus;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.keygenerator.SystemConfConstant;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: liubin
 * Date: 14-5-26
 */
public class OriginalOrderBuilder {

    private Shop tbShop;

    private Shop jdShop;

    private Money postFee = Money.valueOf(0);

    private OriginalOrderStatus status = OriginalOrderStatus.WAIT_SELLER_SEND_GOODS;

    private PlatformType platformType = PlatformType.TAO_BAO;

    private List<OriginalOrderItem> originalOrderItemList = new ArrayList<OriginalOrderItem>();

    private OrderAnalyzeTestService orderAnalyzeTestService = Application.getBean(OrderAnalyzeTestService.class);

    private ConfService confService = Application.getBean(ConfService.class);

    private ShopService shopService = Application.getBean(ShopService.class);

    private OriginalOrderService originalOrderService = Application.getBean(OriginalOrderService.class);

    public OriginalOrderBuilder() {
        List<Shop> shops = shopService.findByPlatformType(PlatformType.TAO_BAO);
        if(shops.isEmpty()) {
            tbShop = orderAnalyzeTestService.initShop(PlatformType.TAO_BAO);
        } else {
            tbShop = shops.get(0);
        }
        shops = shopService.findByPlatformType(PlatformType.JING_DONG);
        if(shops.isEmpty()) {
            jdShop = orderAnalyzeTestService.initShop(PlatformType.JING_DONG);
        } else {
            jdShop = shops.get(0);
        }

    }

    public Shop getShop(PlatformType platformType) {
        switch (platformType) {
            case TAO_BAO:
                return tbShop;
            case JING_DONG:
                return jdShop;
            default:
                return null;
        }

    }

    public void setPostFee(Money postFee) {
        this.postFee = postFee;
    }

    public void setOriginalOrderStatus(OriginalOrderStatus status) {
        this.status = status;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    public OriginalOrderItem addPostCoverItem(int buyCount) {
        //邮费补差产品
        Conf postageProductSkuConf = confService.getConfByName(SystemConfConstant.POSTAGE_PRODUCT_SKU);
        if(StringUtils.isBlank(postageProductSkuConf.getValue())) {
            Product product = orderAnalyzeTestService.initProduct(null, null);
            postageProductSkuConf.setValue(product.getSku());
            confService.save(postageProductSkuConf);
        }

        OriginalOrderItem originalOrderItem = initOriginalOrderItem(Money.valueOfCent(100L), (long)buyCount, Money.valueOf(0d), postageProductSkuConf.getValue());
        originalOrderItemList.add(originalOrderItem);
        return originalOrderItem;
    }

    public OriginalOrderItem addServiceCoverItem(int buyCount) {
        //服务补差产品
        Conf serviceProductSkuConf = confService.getConfByName(SystemConfConstant.SERVICE_PRODUCT_SKU);
        if(StringUtils.isBlank(serviceProductSkuConf.getValue())) {
            Product product = orderAnalyzeTestService.initProduct(null, null);
            serviceProductSkuConf.setValue(product.getSku());
            confService.save(serviceProductSkuConf);
        }

        OriginalOrderItem originalOrderItem = initOriginalOrderItem(Money.valueOfCent(100L), (long)buyCount, Money.valueOf(0d), serviceProductSkuConf.getValue());
        originalOrderItemList.add(originalOrderItem);
        return originalOrderItem;
    }

    public OriginalOrderItem addProductItem(Money price, int buyCount, Repository repository) {
        Product product = orderAnalyzeTestService.initProduct(null, repository);
        if(repository != null) {
            //多初始化5个产品以供页面测试
            for (int i = 0; i < 5; i++) {
                orderAnalyzeTestService.initProduct(null, repository);
            }
        }
        OriginalOrderItem originalOrderItem = initOriginalOrderItem(price, (long)buyCount, Money.valueOf(0d), product.getSku());
        originalOrderItemList.add(originalOrderItem);
        return originalOrderItem;
    }

    public OriginalOrderItem addMealsetItem(int buyCount) {
        Object[] results = orderAnalyzeTestService.initMealset();
        Mealset mealset = (Mealset)results[0];
        Money mealsetPrice = (Money)results[1];
        OriginalOrderItem originalOrderItem = initOriginalOrderItem(mealsetPrice, (long)buyCount, Money.valueOf(0d), mealset.getSku());
        originalOrderItemList.add(originalOrderItem);
        return originalOrderItem;
    }


    private OriginalOrderItem initOriginalOrderItem(Money price, long buyCount, Money sharedDicountFee, String sku) {

        OriginalOrderItem originalOrderItem = new OriginalOrderItem();
        originalOrderItem.setBuyCount(buyCount);
        originalOrderItem.setSku(sku);
        originalOrderItem.setPrice(price);

        originalOrderItem.setAllPartMjzDiscount(sharedDicountFee);
        originalOrderItem.setPlatformSubOrderNo(orderAnalyzeTestService.randomString("suborderno", 8));

        return originalOrderItem;
    }



    public OriginalOrder build() {

        OriginalOrder originalOrder = new OriginalOrder();
        originalOrder.setStatus(status.toString());
        Money discountFee = Money.valueOf(0);
        for(OriginalOrderItem originalOrderItem : originalOrderItemList) {
            discountFee = discountFee.add(originalOrderItem.getAllPartMjzDiscount());
        }
        originalOrder.setDiscountFee(discountFee);
        originalOrder.setHasPostFee(postFee.getCent() != 0L);
        originalOrder.setPostFee(postFee);
//            originalOrder.setAvailableConfirmFee(totalFee);
//            originalOrder.setReceivedPayment(Money.valueOf(0));
//            originalOrder.setAdjustFee(Money.valueOf(0));
//            originalOrder.setRealPointFee((long) 0);
//            originalOrder.setSellerDiscountFee(Money.valueOf(0));
//            originalOrder.setSellerFee(totalFee);
//            originalOrder.setBalancedUsed(Money.valueOf(0));
//            originalOrder.setPayableFee(totalFee);
        originalOrder.setDeliveryType(DeliveryType.shunfeng.toString());
        originalOrder.setBuyerMessage("买家留言" + RandomStringUtils.randomAlphabetic(3));
        originalOrder.setRemark("客服备注" + RandomStringUtils.randomAlphabetic(3));
        originalOrder.setBuyerId("买家id" + RandomStringUtils.randomNumeric(3));
        originalOrder.setBuyerAlipayNo(String.format("%s@%s.com", RandomStringUtils.randomAlphabetic(6), RandomStringUtils.randomAlphabetic(3)));
        originalOrder.setBuyTime(EJSDateUtils.getCurrentDate());
        originalOrder.setPayTime(EJSDateUtils.getCurrentDate());
        originalOrder.setModifiedTime(EJSDateUtils.getCurrentDate());
        Receiver receiver = createReceiver();
        originalOrder.setReceiver(receiver);
        originalOrder.setPlatformType(platformType);
        originalOrder.setPlatformOrderNo(orderAnalyzeTestService.randomString("tmall", 10));
        originalOrder.setOutShopId("店铺ID" + RandomStringUtils.randomNumeric(3));
        originalOrder.setNeedReceipt(false);
        originalOrder.setReceiptTitle("发票抬头" + RandomStringUtils.randomAlphabetic(3));
        originalOrder.setReceiptContent("发票内容" + RandomStringUtils.randomNumeric(3));
        originalOrder.setProcessed(false);

        switch (platformType) {
            case TAO_BAO:
                originalOrder.setShopId(tbShop.getId());
                break;
            case JING_DONG:
                originalOrder.setShopId(jdShop.getId());
                break;
        }

        originalOrderService.saveOriginalOrder(originalOrder);

        for(OriginalOrderItem originalOrderItem : originalOrderItemList) {
            originalOrderItem.setOriginalOrderId(originalOrder.getId());
            originalOrderService.saveOriginalOrderItem(originalOrderItem);
        }
        originalOrder.setOriginalOrderItemList(originalOrderItemList);


        return originalOrder;
    }

    private Receiver createReceiver() {
        Receiver receiver = new Receiver();
        receiver.setReceiverName("姓名" + RandomStringUtils.randomAlphabetic(3));
        receiver.setReceiverPhone("0759-" + RandomStringUtils.randomNumeric(8));
        receiver.setReceiverMobile("159"+ RandomStringUtils.randomNumeric(8));
        receiver.setReceiverZip(RandomStringUtils.randomNumeric(6));
        receiver.setReceiverState("广东省");
        receiver.setReceiverCity("深圳市");
        receiver.setReceiverDistrict("宝安区");
        receiver.setReceiverAddress("详细地址"+ RandomStringUtils.randomAlphabetic(3));
        return receiver;
    }

}
