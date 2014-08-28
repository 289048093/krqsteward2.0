package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.PoiUtil;
import com.ejushang.steward.ordercenter.constant.DeliveryType;
import com.ejushang.steward.ordercenter.constant.OrderItemStatus;
import com.ejushang.steward.ordercenter.constant.OrderItemType;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.*;
import com.ejushang.steward.ordercenter.service.ShopService;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import com.ejushang.steward.ordercenter.vo.CollectInvoiceOrderVo;
import com.ejushang.steward.ordercenter.vo.OrderItemVo;
import com.ejushang.steward.ordercenter.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: tin
 * Date: 14-6-20
 * Time: 下午3:07
 */
public class OrderExcelUtil {

    private OrderExcelUtil() {
    }

    /**
     * 创建订单管理订单导出的标题
     *
     * @param sheet Sheet对象
     * @return 返回标题单元格的下标
     */
    public static int createExcelOrderTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        PoiUtil.createCell(titleRow, 0, "订单信息");
        int cellIndex = 0;
        Row row = sheet.createRow(1);
        PoiUtil.createCell(row, cellIndex++, "生成类型");
        PoiUtil.createCell(row, cellIndex++, "订单类型");
        PoiUtil.createCell(row, cellIndex++, "订单状态");
        PoiUtil.createCell(row, cellIndex++, "退款状态");
        PoiUtil.createCell(row, cellIndex++, "退货状态");
        PoiUtil.createCell(row, cellIndex++, "智库城订单编号");
        PoiUtil.createCell(row, cellIndex++, "平台类型");
        PoiUtil.createCell(row, cellIndex++, "店铺名称");
        PoiUtil.createCell(row, cellIndex++, "外部平台订单编号");
        PoiUtil.createCell(row, cellIndex++, "外部平台订单金额");
        PoiUtil.createCell(row, cellIndex++, "整单优惠金额");
        PoiUtil.createCell(row, cellIndex++, "邮费");
        PoiUtil.createCell(row, cellIndex++, "货款");
        PoiUtil.createCell(row, cellIndex++, "总条数");
        PoiUtil.createCell(row, cellIndex++, "总件数");
        PoiUtil.createCell(row, cellIndex++, "买家留言");
        PoiUtil.createCell(row, cellIndex++, "备注说明");
        PoiUtil.createCell(row, cellIndex++, "线下备注说明");
        PoiUtil.createCell(row, cellIndex++, "买家ID");
        PoiUtil.createCell(row, cellIndex++, "收货省");
        PoiUtil.createCell(row, cellIndex++, "收货市");
        PoiUtil.createCell(row, cellIndex++, "收货区(县)");
        PoiUtil.createCell(row, cellIndex++, "收货地址");
        PoiUtil.createCell(row, cellIndex++, "收货人");
        PoiUtil.createCell(row, cellIndex++, "邮政编码");
        PoiUtil.createCell(row, cellIndex++, "收货电话");
        PoiUtil.createCell(row, cellIndex++, "收货手机");
        PoiUtil.createCell(row, cellIndex++, "快递单号");
        PoiUtil.createCell(row, cellIndex++, "快递公司");
        PoiUtil.createCell(row, cellIndex++, "发票抬头");
        PoiUtil.createCell(row, cellIndex++, "发票内容");
        PoiUtil.createCell(row, cellIndex++, "库房");
        PoiUtil.createCell(row, cellIndex++, "下单时间");
        PoiUtil.createCell(row, cellIndex++, "付款时间");
        int orderCellIndex = cellIndex - 1;
        PoiUtil.createCell(titleRow, cellIndex, "订单项信息");
        //商品条目
        PoiUtil.createCell(row, cellIndex++, "商品编号");
        PoiUtil.createCell(row, cellIndex++, "sku");
        PoiUtil.createCell(row, cellIndex++, "商品规格");
        PoiUtil.createCell(row, cellIndex++, "订货数量");
        PoiUtil.createCell(row, cellIndex++, "商品名称");
        PoiUtil.createCell(row, cellIndex++, "订单项类型");
        PoiUtil.createCell(row, cellIndex++, "智库城订单项编号");
        PoiUtil.createCell(row, cellIndex++, "外部平台订单项编号");
        PoiUtil.createCell(row, cellIndex++, "订单项状态");
        PoiUtil.createCell(row, cellIndex++, "线上退货状态");
        PoiUtil.createCell(row, cellIndex++, "线下退货状态");
        PoiUtil.createCell(row, cellIndex++, "是否已换货");
        PoiUtil.createCell(row, cellIndex++, "换货状态");
        PoiUtil.createCell(row, cellIndex++, "品牌");
        PoiUtil.createCell(row, cellIndex++, "类别");
        PoiUtil.createCell(row, cellIndex++, "原价(一口价)");
        PoiUtil.createCell(row, cellIndex++, "促销价");
        PoiUtil.createCell(row, cellIndex++, "库存");
        PoiUtil.createCell(row, cellIndex++, "订单项优惠金额");
        PoiUtil.createCell(row, cellIndex++, "分摊优惠金额");
        PoiUtil.createCell(row, cellIndex++, "分摊邮费");
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
        PoiUtil.createCell(row, cellIndex++, "价格描述");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, orderCellIndex));  //合并订单标题的单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, orderCellIndex + 1, cellIndex - 1));  //合并商品条目标题的单元格
        return orderCellIndex;
    }

    /**
     * 发货管理的Excel汇总导出
     *
     * @param sheet Sheet对象
     * @return 返回标题单元格的下标
     */
    public static void collectInvoiceOrderExcelTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        PoiUtil.createCell(titleRow, 0, "订单汇总");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        Row row = sheet.createRow(1);
        PoiUtil.createCell(row, 0, "产品编号");
        PoiUtil.createCell(row, 1, "订货数量");
        PoiUtil.createCell(row, 2, "产品名称");
        PoiUtil.createCell(row, 3, "货款");
        PoiUtil.createCell(row, 4, "物流单号");

    }

    /**
     * 发货管理订单导出的标题
     *
     * @param sheet SHEET对象
     * @return 返回头标题的下标
     */
    public static int createExcelInvoiceOrderTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        PoiUtil.createCell(titleRow, 0, "订单信息");
        int cellIndex = 0;
        Row row = sheet.createRow(1);
        PoiUtil.createCell(row, cellIndex++, "智库城订单编号");
        PoiUtil.createCell(row, cellIndex++, "订单类型");
        PoiUtil.createCell(row, cellIndex++, "平台类型");
        PoiUtil.createCell(row, cellIndex++, "外部平台订单编号");
        PoiUtil.createCell(row, cellIndex++, "订单状态");
        PoiUtil.createCell(row, cellIndex++, "快递公司");
        PoiUtil.createCell(row, cellIndex++, "快递单号");
        PoiUtil.createCell(row, cellIndex++, "收货人");
        PoiUtil.createCell(row, cellIndex++, "收货省");
        PoiUtil.createCell(row, cellIndex++, "收货市");
        PoiUtil.createCell(row, cellIndex++, "收货区(县)");
        PoiUtil.createCell(row, cellIndex++, "收货地址");
        PoiUtil.createCell(row, cellIndex++, "商品名称");
        PoiUtil.createCell(row, cellIndex++, "商品编号");
        PoiUtil.createCell(row, cellIndex++, "条形码");
        PoiUtil.createCell(row, cellIndex++, "类别");
        PoiUtil.createCell(row, cellIndex++, "单价");
        PoiUtil.createCell(row, cellIndex++, "数量");
        PoiUtil.createCell(row,cellIndex++,"货款");
        PoiUtil.createCell(row,cellIndex++,"运费");
        PoiUtil.createCell(row, cellIndex++, "品牌");
        PoiUtil.createCell(row, cellIndex++, "买家留言");
        PoiUtil.createCell(row, cellIndex++, "备注说明");
        PoiUtil.createCell(row, cellIndex++, "线下备注说明");
        PoiUtil.createCell(row, cellIndex++, "买家ID");
        PoiUtil.createCell(row, cellIndex++, "邮政编码");
        PoiUtil.createCell(row, cellIndex++, "收货电话");
        PoiUtil.createCell(row, cellIndex++, "收货手机");
        PoiUtil.createCell(row, cellIndex++, "库房");
        PoiUtil.createCell(row, cellIndex++, "下单时间");
        PoiUtil.createCell(row, cellIndex++, "付款时间");
        PoiUtil.createCell(row,cellIndex++,"审单时间");
        PoiUtil.createCell(row, cellIndex++, "打印时间");
        PoiUtil.createCell(row,cellIndex++,"店铺名称");
        PoiUtil.createCell(row, cellIndex++, "发票抬头");
        PoiUtil.createCell(row, cellIndex++, "发票内容");
//        int orderCellIndex = cellIndex - 1;
//        PoiUtil.createCell(titleRow, cellIndex, "订单项信息");
//        //商品条目
//        PoiUtil.createCell(row, cellIndex++, "商品编号");
//        PoiUtil.createCell(row, cellIndex++, "商品名称");
//        PoiUtil.createCell(row, cellIndex++, "条形码");
//        PoiUtil.createCell(row, cellIndex++, "类别");
//        PoiUtil.createCell(row, cellIndex++, "单价");
//        PoiUtil.createCell(row, cellIndex++, "数量");
//        PoiUtil.createCell(row, cellIndex++, "库存");
//        PoiUtil.createCell(row, cellIndex++, "品牌");
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, orderCellIndex));  //合并订单标题的单元格
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, orderCellIndex + 1, cellIndex - 1));  //合并商品条目标题的单元格
        return cellIndex;
    }

    /**
     * 拼订单管理的订单导出的订单数据
     *
     * @param row            Row对象
     * @param startCellIndex Cell的下标
     * @param order          OrderVo对象
     * @return 返回单元格下标
     */
    public static int renderOrderVoExcel(Row row, int startCellIndex, OrderVo order) {
        PoiUtil.createCell(row, startCellIndex++, order.getGenerateType() != null ? order.getGenerateType().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOrderType() != null ? order.getOrderType().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOrderStatus() != null ? order.getOrderStatus().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getRefunding() != null ? order.getRefunding() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOrderReturnStatus() != null ? order.getOrderReturnStatus().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOrderNo() != null ? order.getOrderNo() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOutPlatformType() != null ? order.getOutPlatformType().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getShopName() != null ? order.getShopName() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getPlatformOrderNo() != null ? order.getPlatformOrderNo() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOutActualFee() != null ? order.getOutActualFee() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getSharedDiscountFee() != null ? order.getSharedDiscountFee() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getPostFee() != null ? order.getPostFee() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getGoodsFee() != null ? order.getGoodsFee() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getItemCount());
        PoiUtil.createCell(row, startCellIndex++, order.getItemNumCount());
        PoiUtil.createCell(row, startCellIndex++, order.getBuyerMessage() != null ? order.getBuyerMessage() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getRemark() != null ? order.getRemark() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOfflineRemark() != null ? order.getOfflineRemark() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getBuyerId() != null ? order.getBuyerId() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverState() != null ? order.getReceiverState() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverCity() != null ? order.getReceiverCity() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverDistrict() != null ? order.getReceiverDistrict() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverAddress() != null ? order.getReceiverAddress() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverName() != null ? order.getReceiverName() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverZip() != null ? order.getReceiverZip() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverPhone() != null ? order.getReceiverPhone() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverMobile() != null ? order.getReceiverMobile() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getShippingNo() != null ? order.getShippingNo() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getShippingComp() != null ? DeliveryType.valueOf(order.getShippingComp()).getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiptTitle() != null ? order.getReceiptTitle() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiptContent() != null ? order.getReceiptContent() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getRepoName() != null ? order.getRepoName() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getBuyTime() != null ? order.getBuyTime() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getPayTime() != null ? order.getPayTime() : "");
        return startCellIndex;
    }

    /**
     * 拼发货管理的订单导出的订单数据
     *
     * @param row            Row对象
     * @param startCellIndex Cell的下标
     * @param order          OrderVo对象
     * @return 返回单元格下标
     */
    public static int renderInvoiceOrderVoExcel(Row row, int startCellIndex, OrderVo order,OrderItemVo orderItem) {
        PoiUtil.createCell(row, startCellIndex++, order.getOrderNo() != null ? order.getOrderNo() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOrderType() != null ? order.getOrderType().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOutPlatformType() != null ? order.getOutPlatformType().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getPlatformOrderNo() != null ? order.getPlatformOrderNo() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOrderStatus() != null ? order.getOrderStatus().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getShippingComp() != null ? DeliveryType.valueOf(order.getShippingComp()).getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getShippingNo() != null ? order.getShippingNo() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverName() != null ? order.getReceiverName() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverState() != null ? order.getReceiverState() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverCity() != null ? order.getReceiverCity() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverDistrict() != null ? order.getReceiverDistrict() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverAddress() != null ? order.getReceiverAddress() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getProductName() != null ? orderItem.getProductName() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getProductCode() != null ? orderItem.getProductCode() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getProductSku() != null ? orderItem.getProductSku() + "" : "");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getCateName() != null ? orderItem.getCateName() + "" : "");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getPrice() != null ? orderItem.getPrice().toString() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getBuyCount() != null ? orderItem.getBuyCount().toString() : "0");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getGoodsFee() != null ? orderItem.getGoodsFee().toString() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getPostFee() != null ? orderItem.getPostFee().toString() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getBrandName() != null ? (orderItem.getBrandName()!= null ? orderItem.getBrandName() : "") : "");
        PoiUtil.createCell(row, startCellIndex++, order.getBuyerMessage() != null ? order.getBuyerMessage() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getRemark() != null ? order.getRemark() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getOfflineRemark() != null ? order.getOfflineRemark() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getBuyerId() != null ? order.getBuyerId() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverZip() != null ? order.getReceiverZip() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverPhone() != null ? order.getReceiverPhone() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiverMobile() != null ? order.getReceiverMobile() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getRepoName() != null ? order.getRepoName() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getBuyTime() != null ? order.getBuyTime() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getPayTime() != null ? order.getPayTime() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getConfirmedTime() != null ? order.getConfirmedTime() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getPrintedTime() != null ? order.getPrintedTime() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getShopName() != null ? order.getShopName() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiptTitle() != null ? order.getReceiptTitle() : "");
        PoiUtil.createCell(row, startCellIndex++, order.getReceiptContent() != null ? order.getReceiptContent() : "");
        return startCellIndex;
    }

    /**
     * 拼订单管理的订单导出的订单项数据
     *
     * @param row            Row对象
     * @param startCellIndex Cell的下标
     * @param orderItemVo    OrderItemVo对象
     * @return 返回单元格下标
     */
    public static int renderOrderItemVoExcel(Row row, int startCellIndex, OrderItemVo orderItemVo) {
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getProductCode() != null ? orderItemVo.getProductCode() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getProductSku() != null ? orderItemVo.getProductSku() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getSpecInfo() != null ? orderItemVo.getSpecInfo() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getBuyCount() != null ? orderItemVo.getBuyCount() : "0");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getProductName() != null ? orderItemVo.getProductName() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getType() != null ? orderItemVo.getType() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getId());
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getPlatformSubOrderNo() != null ? orderItemVo.getPlatformSubOrderNo() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getStatus() != null ? OrderItemStatus.valueOf(orderItemVo.getStatus()).getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getReturnStatus() != null ? orderItemVo.getReturnStatus().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getOfflineReturnStatus() != null ? orderItemVo.getOfflineReturnStatus().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getExchanged() == true ? "换货" : "未换货");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getExchangedGoods() != null ? orderItemVo.getExchangedGoods() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getBrandName() != null ? orderItemVo.getBrandName() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getCateName() != null ? orderItemVo.getCateName() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getPrice() != null ? orderItemVo.getPrice() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getDiscountPrice() != null ? orderItemVo.getDiscountPrice() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getRepoNum() != null ? orderItemVo.getRepoNum() : "0");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getDiscountFee() != null ? orderItemVo.getDiscountFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getSharedDiscountFee() != null ? orderItemVo.getSharedDiscountFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getSharedPostFee() != null ? orderItemVo.getSharedPostFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getPostCoverFee() != null ? orderItemVo.getPostCoverFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getPostCoverRefundFee() != null ? orderItemVo.getPostCoverRefundFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getServiceCoverFee() != null ? orderItemVo.getServiceCoverFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getServiceCoverRefundFee() != null ? orderItemVo.getServiceCoverRefundFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getRefundFee() != null ? orderItemVo.getRefundFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getOfflineRefundFee() != null ? orderItemVo.getOfflineRefundFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getReturnPostFee() != null ? orderItemVo.getReturnPostFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getReturnPostPayer() != null ? orderItemVo.getReturnPostPayer().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getOfflineReturnPostFee() != null ? orderItemVo.getOfflineReturnPostFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getOfflineReturnPostPayer() != null ? orderItemVo.getOfflineReturnPostPayer().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getExchangePostFee() != null ? orderItemVo.getExchangePostFee() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getExchangePostPayer() != null ? orderItemVo.getExchangePostPayer().getValue() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getGoodsFee() != null ? orderItemVo.getGoodsFee().toString() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItemVo.getPriceDescription() != null ? orderItemVo.getPriceDescription() : "");

        return startCellIndex;
    }

    /**
     * 拼发货管理的订单导出的订单项数据
     *
     * @param row            Row对象
     * @param startCellIndex Cell的下标
     * @param orderItem      OrderItem对象
     * @return 返回单元格下标
     */
    public static int renderOrderItemExcel(Row row, int startCellIndex, OrderItemVo orderItem) {
        PoiUtil.createCell(row, startCellIndex++, orderItem.getProductCode() != null ? orderItem.getProductCode() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getProductName() != null ? orderItem.getProductName() : "");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getProductSku() != null ? orderItem.getProductSku() + "" : "");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getCateName() != null ? orderItem.getCateName() + "" : "");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getPrice() != null ? orderItem.getPrice().toString() : "0.00");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getBuyCount() != null ? orderItem.getBuyCount().toString() : "0");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getRepoNum() != null ? orderItem.getRepoNum().toString() : "0");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getBrandName() != null ? (orderItem.getBrandName()!= null ? orderItem.getBrandName() : "") : "");
        PoiUtil.createCell(row, startCellIndex++, orderItem.getGoodsFee() != null ? orderItem.getGoodsFee().toString() : "0.00");
        return startCellIndex;
    }

    public static int collectInvoiceOrderExcel(Row row, int startCellIndex,CollectInvoiceOrderVo collectInvoiceOrderVo) {
        PoiUtil.createCell(row, startCellIndex++, collectInvoiceOrderVo.getProductNo());
        PoiUtil.createCell(row, startCellIndex++,collectInvoiceOrderVo.getBuyCount());
        PoiUtil.createCell(row, startCellIndex++,collectInvoiceOrderVo.getProductName());
        PoiUtil.createCell(row, startCellIndex++,collectInvoiceOrderVo.getGoodsFee().toString());
        PoiUtil.createCell(row, startCellIndex++, collectInvoiceOrderVo.getShippingNos());
        return startCellIndex;
    }

   public static void collectNum(int rowIndex,Workbook workbook,Sheet sheet,int size,int productNum){
       CellStyle myStyle = workbook.createCellStyle();
       Row row2 = sheet.createRow(++rowIndex);
       PoiUtil.createCell(row2, 0, "订单导出汇总");
       Row row = sheet.createRow(++rowIndex);
       PoiUtil.createCell(row, 0, "导出订单总条数");
       PoiUtil.createCell(row, 1,size + "条",myStyle);
       Row row1 = sheet.createRow(++rowIndex);
       PoiUtil.createCell(row1, 0, "导出订货数量");
       PoiUtil.createCell(row1, 1, productNum + "件",myStyle);
   }
    /**
     * 导入订单的判断数据方法
     *
     * @param shopService
     * @param productService
     * @param row
     * @param rowNum
     * @param orders
     */
    public static void judgeLeadInOrder(ShopService shopService, ProductService productService, Row row, int rowNum, List<Order> orders) {
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        OrderItem orderItem = new OrderItem();
        Invoice invoice = new Invoice();
        Receiver receiver = new Receiver();
        order.setPlatformOrderNo(StringUtils.isNotBlank(PoiUtil.getStringCellValue(row, 0)) ? PoiUtil.getStringCellValue(row, 0) : null);
        String platform = PoiUtil.getStringCellValue(row, 1);
        leadInExceptionMess(platform, String.format("导入失败,检测到第%s行的订单的平台类型为空", rowNum));
        if (platform.equals("天猫") || platform.equals("京东")) {
            throw new StewardBusinessException(String.format("导入失败，检测到第%s行订单的平台类型是天猫或者京东", rowNum));
        }
        order.setPlatformType(PlatformType.enumValueOf(platform));
        leadInExceptionMess(order.getPlatformType(), "导入失败，检测到第%s行订单的平台类型匹配不上系统的平台类型，请查看Excel平台类型是否正确");
        String shopName = PoiUtil.getStringCellValue(row, 2);
        if (StringUtils.isNotBlank(shopName)) {
            Shop shop = shopService.findShopByNick(shopName);
            order.setShopId(shop != null ? shop.getId() : null);
        } else {
            Shop shopCondition = new Shop();
            shopCondition.setPlatformType(order.getPlatformType());
            List<Shop> shops = shopService.getShop(shopCondition);
            order.setShopId(shops.size() > 0 ? shops.get(0).getId() : null);
        }
        String sku = PoiUtil.getStringCellValue(row, 3);
        orderItem.setProductSku(leadInExceptionMess(sku, String.format("导入失败，检测到第%s行订单没有sku", rowNum)));
        Product product = productService.findProductBySKU(orderItem.getProductSku());
        leadInExceptionMess(product, String.format("导入失败，检测到第%s行Excel的产品SKU对应不上系统产品SKU", rowNum));
        orderItem.setPrice(Money.valueOf(leadInExceptionMess(PoiUtil.getStringCellValue(row, 4), String.format("导入失败，检测到第%s行订单没有价格", rowNum))));
        orderItem.setBuyCount(leadInExceptionMess(PoiUtil.getIntegerCellValue(row, 5), String.format("导入失败，检测到第%s行订单没有订货数量", rowNum)));
        String sharePostFee = PoiUtil.getStringCellValue(row, 6);
        order.setSharedPostFee(Money.valueOf(leadInExceptionMess(sharePostFee, String.format("导入失败，检测到第%s行订单没有分摊邮费", rowNum))));
        orderItem.setSharedPostFee(Money.valueOf(sharePostFee));
        order.setBuyerMessage(StringUtils.isNotBlank(PoiUtil.getStringCellValue(row, 7)) ? PoiUtil.getStringCellValue(row, 7) : null);
        order.setRemark(StringUtils.isNotBlank(PoiUtil.getStringCellValue(row, 8)) ? PoiUtil.getStringCellValue(row, 8) : null);
        order.setBuyerId(leadInExceptionMess(PoiUtil.getStringCellValue(row, 9), String.format("导入失败，检测到第%s行订单没有买家ID", rowNum)));
        receiver.setReceiverState(leadInExceptionMess(PoiUtil.getStringCellValue(row, 10), String.format("导入失败，检测到第%s行订单没有省份", rowNum)));
        receiver.setReceiverCity(leadInExceptionMess(PoiUtil.getStringCellValue(row, 11), String.format("导入失败，检测到第%s行订单没有城市", rowNum)));
        receiver.setReceiverDistrict(PoiUtil.getStringCellValue(row,12));
        receiver.setReceiverAddress(leadInExceptionMess(PoiUtil.getStringCellValue(row, 13), String.format("导入失败，检测到第%s行订单没有收货详细地址", rowNum)));
        receiver.setReceiverName(leadInExceptionMess(PoiUtil.getStringCellValue(row, 14), String.format("导入失败，检测到第%s行订单没有收货人", rowNum)));
        String receiverZip = PoiUtil.getStringCellValue(row, 15);
        if (StringUtils.isNotBlank(receiverZip)) {
            receiver.setReceiverZip(receiverZip);
        }
        if (StringUtils.isNotBlank(PoiUtil.getStringCellValue(row, 16))) {
            receiver.setReceiverPhone(PoiUtil.getStringCellValue(row, 16));
        }
//        receiver.setReceiverMobile(leadInExceptionMess(PoiUtil.getStringCellValue(row, 17), String.format("导入失败，检测到第%s行订单没有收货手机", rowNum)));
        receiver.setReceiverMobile(PoiUtil.getStringCellValue(row, 17));
        if(StringUtils.isBlank(receiver.getReceiverMobile())&&StringUtils.isBlank(receiver.getReceiverPhone())){
            throw new StewardBusinessException(String.format("导入失败，检测到第%s行订单的收货手机和电话都为空，必须保留一个联系电话", rowNum));
        }
        invoice.setShippingComp(DeliveryType.enumValueOf(leadInExceptionMess(PoiUtil.getStringCellValue(row, 18), String.format("导入失败，检测到第%s行订单没有快递", rowNum))).toString());
        leadInExceptionMess(invoice.getShippingComp(), String.format("导入失败，检测到第%s行订单的快递公司没被收录在系统里", rowNum));
        String receiptTitle = PoiUtil.getStringCellValue(row, 19);
        if (StringUtils.isNotBlank(receiptTitle)) {
            order.setReceiptTitle(receiptTitle);
        }
        String receiptContent = PoiUtil.getStringCellValue(row, 20);
        if (!StringUtils.isNotBlank(receiptContent)) {
            order.setReceiptContent(receiptContent);
        }
        Date buyDate = null;
        try {
            buyDate = PoiUtil.getDateCellValue(row, 21, EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
        } catch (Exception e) {
            throw new StewardBusinessException(String.format("导入失败,检测到第%s行订单的下单时间没有按照模板格式无法被系统识别", rowNum));
        }
        leadInExceptionMess(buyDate, String.format("导入失败，检测到第%s行订单没有下单时间", rowNum));
        order.setBuyTime(buyDate);
        Date payTime = null;
        try {
            payTime = PoiUtil.getDateCellValue(row, 22, EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
        } catch (Exception e) {
            throw new StewardBusinessException(String.format("导入失败，检测到第%s行订单的付款时间没有按照模板格式无法被系统识别", rowNum));
        }
        order.setPayTime(payTime != null ? payTime : order.getBuyTime());
        invoice.setReceiver(receiver);
        orderItems.add(orderItem);
        order.setOrderItemList(orderItems);
        order.setInvoice(invoice);
        orders.add(order);
    }

    public static String leadInExceptionMess(String value, String mess) {
        if (StringUtils.isBlank(value)) {
            throw new StewardBusinessException(mess);

        } else {
            return value;
        }

    }

    public static Integer leadInExceptionMess(Integer value, String mess) {
        if (value == null) {
            throw new StewardBusinessException(mess);

        } else {
            return value;
        }

    }

    public static Object leadInExceptionMess(Object value, String mess) {
        if (value == null) {
            throw new StewardBusinessException(mess);

        } else {
            return value;
        }

    }
}
