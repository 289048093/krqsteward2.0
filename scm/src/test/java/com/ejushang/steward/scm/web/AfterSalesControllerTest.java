package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.vo.AfterSalesVo;
import com.ejushang.steward.ordercenter.vo.ReceiveGoodsVo;
import com.ejushang.steward.ordercenter.vo.ReturnGoodsVo;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author Channel
 * @Date 2014/8/12
 * @Version: 1.0
 */
public class AfterSalesControllerTest extends ControllerTest {

    private AfterSalesVo.Item refund(AfterSalesVo.Item item) {
        AfterSalesVo.Refund refund = new AfterSalesVo.Refund();
        List<AfterSalesVo.RefundAlloc> refundAllocList = new LinkedList<AfterSalesVo.RefundAlloc>();
        AfterSalesVo.RefundAlloc refundAlloc = new AfterSalesVo.RefundAlloc();
        refundAlloc.setType(RefundClass.POST);
        refundAlloc.setPlatformFee(Money.valueOf(5));
        refundAlloc.setSupplierFee(Money.valueOf(5));
        refundAlloc.setOnline(true);
        refundAllocList.add(refundAlloc);
        AfterSalesVo.RefundAlloc refundAlloc1 = new AfterSalesVo.RefundAlloc();
        refundAlloc1.setType(RefundClass.GOODS);
        refundAlloc1.setPlatformFee(Money.valueOf(0));
        refundAlloc1.setSupplierFee(Money.valueOf(5));
        refundAlloc1.setOnline(false);
        refundAllocList.add(refundAlloc1);
        refund.setRefundAllocList(refundAllocList);
        refund.setRefundMethod(RefundMethod.ALIPAY);
        refund.setAlipayNo("test001");
        item.setRefund(refund);
        return item;
    }

    private AfterSalesVo.Item goods(AfterSalesVo.Item item) {
        AfterSalesVo.RefundGoods refundGoods = new AfterSalesVo.RefundGoods();
        refundGoods.setCount(1);
        item.setRefundGoods(refundGoods);
        return item;
    }

    private AfterSalesVo.Item patch(AfterSalesVo.Item item) {
        AfterSalesVo.Patch patch = new AfterSalesVo.Patch();
        patch.setPatchCount(1);
        patch.setProductId(4372);
        List<AfterSalesVo.Patch> patchList = item.getPatchList();
        if (patchList == null) {
            patchList = new LinkedList<AfterSalesVo.Patch>();
            item.setPatchList(patchList);
        }
        patchList.add(patch);
        return item;
    }

    private AfterSalesVo.Item item(Integer itemId) {
        AfterSalesVo.Item item = new AfterSalesVo.Item();
        item.setOrderItemId(itemId);
        return item;
    }

    private AfterSalesVo convertAfterSalesVo(Object obj) {
        afterSalesGoodsIdList.clear();
        afterSalesRefundIdList.clear();
        AfterSalesVo afterSalesVo = new AfterSalesVo();
        afterSalesVo.setId(jsonInt(obj, "id"));
        afterSalesVo.setSource(AfterSalesSource.valueOf(jsonString(obj, "source.name")));
        afterSalesVo.setOrderId(jsonInt(obj, "orderId"));
        afterSalesVo.setReasonCode(jsonString(obj, "reasonCode"));
        boolean isSend = jsonBoolean(obj, "send");
        afterSalesVo.setSend(isSend);
        if (isSend) {
            AfterSalesVo.Send send = new AfterSalesVo.Send();
            Object afterSalesSend = jsonValue(obj, "afterSalesSend");
            send.setReceiverAddress(jsonString(afterSalesSend, "receiverAddress"));
            send.setReceiverCity(jsonString(afterSalesSend, "receiverCity"));
            send.setReceiverDistrict(jsonString(afterSalesSend, "receiverDistrict"));
            send.setReceiverState(jsonString(afterSalesSend, "receiverState"));
            send.setReceiverName(jsonString(afterSalesSend, "receiverName"));
            send.setReceiverPhone(jsonString(afterSalesSend, "receiverPhone"));
            send.setReceiverRemark(jsonString(afterSalesSend, "receiverRemark"));
            send.setReceiverZip(jsonString(afterSalesSend, "receiverZip"));
            send.setShippingComp(jsonString(afterSalesSend, "shippingComp"));
            afterSalesVo.setSendInfo(send);
        }
        Object[] afterSalesItemLists = jsonArray(obj, "afterSalesItemList");
        if (afterSalesItemLists != null && afterSalesItemLists.length > 0) {
            afterSalesVo.setItemList(new LinkedList<AfterSalesVo.Item>());
            for (Object afterSalesItem : afterSalesItemLists) {
                AfterSalesVo.Item item = item(jsonInt(afterSalesItem, "orderItemId"));
                item.setId(jsonInt(afterSalesItem, "id"));
                if (jsonBoolean(afterSalesItem, "refund")) {
                    Object afterSalesRefund = jsonValue(afterSalesItem, "afterSalesRefund");
                    AfterSalesVo.Refund refund = new AfterSalesVo.Refund();
                    refund.setId(jsonInt(afterSalesRefund, "id"));
                    afterSalesRefundIdList.add(refund.getId());
                    refund.setRefundMethod(RefundMethod.valueOf(jsonString(afterSalesRefund, "refundMethod.name")));
                    refund.setAlipayNo(jsonString(afterSalesRefund, "alipayNo"));
                    refund.setBank(jsonString(afterSalesRefund, "bank"));
                    refund.setBankUser(jsonString(afterSalesRefund, "bankUser"));
                    refund.setBankAccout(jsonString(afterSalesRefund, "bankAccout"));
                    Object[] afterSalesRefundAllocLists = jsonArray(afterSalesRefund, "afterSalesRefundAllocList");
                    if (afterSalesRefundAllocLists != null && afterSalesRefundAllocLists.length > 0) {
                        List<AfterSalesVo.RefundAlloc> refundAllocList = new LinkedList<AfterSalesVo.RefundAlloc>();
                        refund.setRefundAllocList(refundAllocList);
                        for (Object afterSalesRefundAlloc : afterSalesRefundAllocLists) {
                            AfterSalesVo.RefundAlloc refundAlloc = new AfterSalesVo.RefundAlloc();
                            refundAlloc.setType(RefundClass.valueOf(jsonString(afterSalesRefundAlloc, "type.name")));
                            refundAlloc.setPlatformFee(Money.valueOf(jsonDouble(afterSalesRefundAlloc, "platformFee")));
                            refundAlloc.setSupplierFee(Money.valueOf(jsonDouble(afterSalesRefundAlloc, "supplierFee")));
                            refundAlloc.setOnline(jsonBoolean(afterSalesRefundAlloc, "online"));
                            refundAllocList.add(refundAlloc);
                        }
                    }
                    item.setRefund(refund);
                }
                if (jsonBoolean(afterSalesItem, "refundGoods")) {
                    afterSalesGoodsIdList.add(item.getId());
                    Object afterSalesRefundGoods = jsonValue(afterSalesItem, "afterSalesRefundGoods");
                    AfterSalesVo.RefundGoods refundGoods = new AfterSalesVo.RefundGoods();
                    refundGoods.setId(jsonInt(afterSalesRefundGoods, "id"));
                    refundGoods.setCount(jsonInt(afterSalesRefundGoods, "count"));
                    item.setRefundGoods(refundGoods);
                }
                if (jsonBoolean(afterSalesItem, "patch")) {
                    Object[] afterSalesPatchLists = jsonArray(afterSalesItem, "afterSalesPatchList");
                    if (afterSalesPatchLists != null && afterSalesPatchLists.length > 0) {
                        List<AfterSalesVo.Patch> patchList = new LinkedList<AfterSalesVo.Patch>();
                        for (Object afterSalesPatch : afterSalesPatchLists) {
                            AfterSalesVo.Patch patch = new AfterSalesVo.Patch();
                            patch.setId(jsonInt(afterSalesPatch, "id"));
                            patch.setPatchCount(jsonInt(afterSalesPatch, "count"));
                            patch.setProductId(jsonInt(afterSalesPatch, "productId"));
                            patchList.add(patch);
                        }
                        item.setPatchList(patchList);
                    }
                }
                afterSalesVo.getItemList().add(item);
            }
        }
        return afterSalesVo;
    }


    private static Integer afterSalesId;

    private static List<Integer> afterSalesGoodsIdList = new LinkedList<Integer>();

    private static List<Integer> afterSalesRefundIdList = new LinkedList<Integer>();

    private Map<String, Object> afterSales;

    @Test
    public void Start() throws Exception {
    }

    @Test
    public void 创建售后工单() throws Exception {

        // 创建售后工单
        AfterSalesVo afterSalesVo = new AfterSalesVo();
        afterSalesVo.setSource(AfterSalesSource.ORDER);
        afterSalesVo.setOrderId(2);
        afterSalesVo.setReasonCode("+01,-01");
        afterSalesVo.setSend(true);
        AfterSalesVo.Send send = new AfterSalesVo.Send();
        send.setReceiverAddress("洪浪北");
        send.setReceiverCity("深圳");
        send.setReceiverDistrict("南山区");
        send.setReceiverState("广东");
        send.setReceiverName("test");
        send.setReceiverPhone("138");
        send.setReceiverRemark("发货信息");
        send.setReceiverZip("50008");
        send.setShippingComp("测试物流");
        afterSalesVo.setSendInfo(send);
        afterSalesVo.setItemList(new LinkedList<AfterSalesVo.Item>());
        // 添加退款操作
        afterSalesVo.getItemList().add(refund(item(2)));
        // 添加退货操作
        afterSalesVo.getItemList().add(goods(refund(item(3))));
        // 添加补货操作
        afterSalesVo.getItemList().add(patch(item(4)));
        // 添加换货操作
        afterSalesVo.getItemList().add(patch(goods(refund(item(4)))));
        JsonResult mvc = mvc("/afterSales/save").params(afterSalesVo).call();
        Assert.assertTrue(mvc.isSuccess());
        afterSalesId = (Integer) mvc.getData().get("obj");
        System.out.println("创建售后工单：" + afterSalesId);
    }

    @Test
    public void 获取售后工单详情() throws Exception {
        创建售后工单();
        JsonResult mvc = mvc("/afterSales/get").param("id", afterSalesId).call();
        Assert.assertTrue(mvc.isSuccess());
        Map<String, Object> obj = (Map<String, Object>) mvc.getData().get("obj");
        afterSales = obj;
    }

    @Test
    public void 更新售后工单() throws Exception {
        // 更新售后单
        创建售后工单();
        // 获取售后工单
        获取售后工单详情();
        AfterSalesVo afterSalesVo = convertAfterSalesVo(afterSales);
        JsonResult mvc = mvc("/afterSales/save").params(afterSalesVo).call();
        Assert.assertTrue(mvc.isSuccess());
    }

    @Test
    public void 确认到货() throws Exception {
        // 退货信息和到货信息与售后流程没有联系，什么时候都可以编辑。确认退货和确认发货的操作按钮不可逆。可逆的只是相关的信息。
        创建售后工单();
        for (Integer itemId : afterSalesGoodsIdList) {
            ReturnGoodsVo returnGoodsVo = new ReturnGoodsVo();
            returnGoodsVo.setAfterSalesItemId(itemId);
            returnGoodsVo.setShippingComp("测试快递");
            returnGoodsVo.setShippingNo("TS001");
            JsonResult mvc = mvc("/afterSales/confirmGoods").params(returnGoodsVo).param("confirm", true).call();
            Assert.assertTrue(mvc.isSuccess());
            // 修改到货信息
            mvc = mvc("/afterSales/confirmGoods").params(returnGoodsVo).param("confirm", false).call();
            Assert.assertTrue(mvc.isSuccess());
            // 重复确认到货
            mvc = mvc("/afterSales/confirmGoods").params(returnGoodsVo).param("confirm", true).call();
            Assert.assertTrue(!mvc.isSuccess());
        }
    }

    @Test
    public void 确认收货() throws Exception {
        // 退货信息和到货信息与售后流程没有联系，什么时候都可以编辑。确认退货和确认发货的操作按钮不可逆。可逆的只是相关的信息。
        创建售后工单();
        for (Integer itemId : afterSalesGoodsIdList) {
            ReceiveGoodsVo receiveGoodsVo = new ReceiveGoodsVo();
            receiveGoodsVo.setAfterSalesItemId(itemId);
            receiveGoodsVo.setFace(RefundGoodsFace.NEW);
            receiveGoodsVo.setFunc(RefundGoodsFunc.GOOD);
            receiveGoodsVo.setPack(RefundGoodsPack.NEW);
            receiveGoodsVo.setReceivedCount(1);
            receiveGoodsVo.setRemark("收货测试");
            JsonResult mvc = mvc("/afterSales/receiveGoods").params(receiveGoodsVo).param("confirm", true).call();
            Assert.assertTrue(mvc.isSuccess());
            // 修改到货信息
            mvc = mvc("/afterSales/receiveGoods").params(receiveGoodsVo).param("confirm", false).call();
            Assert.assertTrue(mvc.isSuccess());
            // 重复确认到货
            mvc = mvc("/afterSales/receiveGoods").params(receiveGoodsVo).param("confirm", true).call();
            Assert.assertTrue(!mvc.isSuccess());
        }
    }

    @Test
    public void 仓库确认流程() throws Exception {
        // 仓库确认流程
        创建售后工单();
        Assert.assertTrue(mvc("/afterSales/flow").param("afterSalesId", afterSalesId)
                .param("status", AfterSalesStatus.CHECK)
                .param("remarks", "客服确认").call().isSuccess());
        Assert.assertTrue(mvc("/afterSales/flow").param("afterSalesId", afterSalesId)
                .param("status", AfterSalesStatus.ACCEPT)
                .param("remarks", "仓库确认").call().isSuccess());
    }

    @Test
    public void 仓库驳回流程() throws Exception {
        // 仓库确认流程
        创建售后工单();
        Assert.assertTrue(mvc("/afterSales/flow").param("afterSalesId", afterSalesId)
                .param("status", AfterSalesStatus.CHECK)
                .param("remarks", "客服确认").call().isSuccess());
        Assert.assertTrue(mvc("/afterSales/flow").param("afterSalesId", afterSalesId)
                .param("status", AfterSalesStatus.REJECT)
                .param("remarks", "仓库驳回").call().isSuccess());
    }

    @Test
    public void 售后单审核通过之后无法编辑() throws Exception {
        // 验证仓库确认后无法修改
        仓库确认流程();
        // 获取售后工单
        JsonResult mvc = mvc("/afterSales/get").param("id", afterSalesId).call();
        Assert.assertTrue(mvc.isSuccess());
        Map<String, Object> obj = (Map<String, Object>) mvc.getData().get("obj");
        AfterSalesVo afterSalesVo = convertAfterSalesVo(obj);
        mvc = mvc("/afterSales/save").params(afterSalesVo).call();
        Assert.assertTrue(!mvc.isSuccess());
    }

    @Test
    public void 被驳回后可以再次编辑并提交() throws Exception {
        仓库驳回流程();
        // 获取售后工单
        JsonResult mvc = mvc("/afterSales/get").param("id", afterSalesId).call();
        Assert.assertTrue(mvc.isSuccess());
        Map<String, Object> obj = (Map<String, Object>) mvc.getData().get("obj");
        AfterSalesVo afterSalesVo = convertAfterSalesVo(obj);
        mvc = mvc("/afterSales/save").params(afterSalesVo).call();
        Assert.assertTrue(mvc.isSuccess());
    }

    @Test
    public void 生成发货单() throws Exception {
        //生成发货单，必须仓库同意
        创建售后工单();
        JsonResult mvc = mvc("/afterSales/buildOrder").param("afterSalesId", afterSalesId).call();
        Assert.assertTrue(!mvc.isSuccess());
        仓库确认流程();
        mvc = mvc("/afterSales/buildOrder").param("afterSalesId", afterSalesId).call();
        Assert.assertTrue(mvc.isSuccess());
    }

    @Test
    public void 作废异常售后单() throws Exception {
        // 异常单只能作废，作废有回滚操作。已结束的售后单无法作废。如果异常单无法作废，需要新建新的售后单走流程（如退货）。
        //（1）退款：退款管理里退款记录的问题。线上的不动，线下的删除。
        //（2）发货：作废生成的发货单。如果生成的发货单已发货，则无法作废。
        生成发货单();
        JsonResult mvc = mvc("/afterSales/cancel").param("afterSalesId", afterSalesId).param("remarks", "作废售后工单").call();
        Assert.assertTrue(mvc.isSuccess());
    }

    @Test
    public void 售后单转为已结束() throws Exception {

    }

    public void 确认支付后金额信息不能修改() throws Exception {
        // 验证确认退款后修改退款
        仓库确认流程();
        // on_pay_after_sales_refund
        // 获取售后工单
        JsonResult mvc = mvc("/afterSales/get").param("id", afterSalesId).call();
        Assert.assertTrue(mvc.isSuccess());
        Map<String, Object> obj = (Map<String, Object>) mvc.getData().get("obj");

        // 增加退款分配金额
        AfterSalesVo afterSalesVo = convertAfterSalesVo(obj);
        AfterSalesVo.RefundAlloc refundAlloc = new AfterSalesVo.RefundAlloc();
        refundAlloc.setOnline(true);
        refundAlloc.setPlatformFee(Money.valueOf(0));
        refundAlloc.setSupplierFee(Money.valueOf(5));
        refundAlloc.setType(RefundClass.GOODS);
        afterSalesVo.getItemList().get(0).getRefund().getRefundAllocList().add(refundAlloc);
        mvc = mvc("/afterSales/save").params(afterSalesVo).call();
        Assert.assertTrue(!mvc.isSuccess());

        // 删除退款
        afterSalesVo = convertAfterSalesVo(obj);
        afterSalesVo.getItemList().get(0).setRefund(null);
        mvc = mvc("/afterSales/save").params(afterSalesVo).call();
        Assert.assertTrue(!mvc.isSuccess());

        // 删除售后项目
        afterSalesVo = convertAfterSalesVo(obj);
        afterSalesVo.getItemList().remove(0);
        mvc = mvc("/afterSales/save").params(afterSalesVo).call();
        Assert.assertTrue(!mvc.isSuccess());
    }

    @Test
    public void 测试售后单已结束状态() throws Exception {
        // 创建售后工单
        AfterSalesVo afterSalesVo = new AfterSalesVo();
        afterSalesVo.setSource(AfterSalesSource.ORDER);
        afterSalesVo.setOrderId(2);
        afterSalesVo.setReasonCode("+01,-01");
        afterSalesVo.setSend(true);
        AfterSalesVo.Send send = new AfterSalesVo.Send();
        send.setReceiverAddress("洪浪北");
        send.setReceiverCity("深圳");
        send.setReceiverDistrict("南山区");
        send.setReceiverState("广东");
        send.setReceiverName("test");
        send.setReceiverPhone("138");
        send.setReceiverRemark("发货信息");
        send.setReceiverZip("50008");
        send.setShippingComp("测试物流");
        afterSalesVo.setSendInfo(send);
        afterSalesVo.setItemList(new LinkedList<AfterSalesVo.Item>());
        // 添加退款操作
//        afterSalesVo.getItemList().add(refund(item(2)));
        // 添加退货操作
        afterSalesVo.getItemList().add(goods(refund(item(3))));
        // 添加补货操作
//        afterSalesVo.getItemList().add(patch(item(4)));
        // 添加换货操作
//        afterSalesVo.getItemList().add(patch(goods(refund(item(4)))));
        JsonResult mvc = mvc("/afterSales/save").params(afterSalesVo).call();
        Assert.assertTrue(mvc.isSuccess());
        afterSalesId = (Integer) mvc.getData().get("obj");
        System.out.println("创建售后工单：" + afterSalesId);

        mvc = mvc("/afterSales/get").param("id", afterSalesId).call();
        Assert.assertTrue(mvc.isSuccess());
        Map<String, Object> obj = (Map<String, Object>) mvc.getData().get("obj");
        afterSales = obj;
        afterSalesVo = convertAfterSalesVo(obj);

        // 仓库确认流程
        Assert.assertTrue(mvc("/afterSales/flow").param("afterSalesId", afterSalesId)
                .param("status", AfterSalesStatus.CHECK)
                .param("remarks", "客服确认").call().isSuccess());
        Assert.assertTrue(mvc("/afterSales/flow").param("afterSalesId", afterSalesId)
                .param("status", AfterSalesStatus.ACCEPT)
                .param("remarks", "仓库确认").call().isSuccess());
        // 确认收货
        for (Integer itemId : afterSalesGoodsIdList) {
            ReceiveGoodsVo receiveGoodsVo = new ReceiveGoodsVo();
            receiveGoodsVo.setAfterSalesItemId(itemId);
            receiveGoodsVo.setFace(RefundGoodsFace.NEW);
            receiveGoodsVo.setFunc(RefundGoodsFunc.GOOD);
            receiveGoodsVo.setPack(RefundGoodsPack.NEW);
            receiveGoodsVo.setReceivedCount(1);
            receiveGoodsVo.setRemark("收货测试");
            mvc = mvc("/afterSales/receiveGoods").params(receiveGoodsVo).param("confirm", true).call();
            Assert.assertTrue(mvc.isSuccess());
        }
        // 确认支付
        for (Integer refundId : afterSalesRefundIdList) {
            mvc = mvc("/afterSales/on_pay_after_sales_refund").param("id", refundId).call();
            Assert.assertTrue(mvc.isSuccess());
        }
    }

}
