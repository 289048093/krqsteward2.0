package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.ordercenter.constant.OrderStatus;
import com.ejushang.steward.ordercenter.domain.OrderItem;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.service.OrderService;
import com.ejushang.steward.ordercenter.service.ShopService;
import com.ejushang.steward.ordercenter.vo.AddOrderVo;
import com.ejushang.steward.ordercenter.vo.QueryProdVo;
import com.ejushang.steward.scm.common.web.BaseController;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.*;

/**
 * User: tin
 * Date: 14-4-14
 * Time: 下午1:52
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private ShopService shopService;


    @RequestMapping("/list")
    @ResponseBody
    JsonResult findOrderDetails(HttpServletRequest request) throws ParseException {
        @SuppressWarnings("unchecked")
        Map<String, Object[]> map = request.getParameterMap();
        Page page = PageFactory.getPage(request);
        return new JsonResult(true).addObject(orderService.findOrderDetails(map, page));
    }

    @OperationLog("删除订单项")
    @RequestMapping("/deleteItemList")
    @ResponseBody
    JsonResult deleteByOrderItemById(Integer id) {
        OrderItem orderItem=orderService.deleteOrderItemById(id);

        //Record business log
        if(orderItem!=null){
            BusinessLogUtil.bindBusinessLog("订单号:%s,删除商品:%s",
                        orderItem.getOrder()==null? "":orderItem.getOrder().getOrderNo(),
                        orderItem.getProduct()==null? orderItem.getProductId():orderItem.getProduct().getName()
                    );
        }

        return new JsonResult(true, "删除成功");
    }

    @OperationLog("保存订单")
    @RequestMapping("/updateByOrder")
    @ResponseBody
    JsonResult save(String data) {
        if(StringUtils.isBlank(data)){
         return new JsonResult(false,"没有要更新的订单项");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数data:[%s]", data));
        }
        HashMap map = JsonUtil.json2Object(data, HashMap.class);
        orderService.saveOrder(map);

        BusinessLogUtil.bindBusinessLog(true,"更新订单,订单ID:"+map.get("id"));

        return new JsonResult(true, "更新成功").addObject("更新成功");
    }

    @RequestMapping("/ItemList")
    @ResponseBody
    JsonResult findDetailItemList(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, Object[]> map = request.getParameterMap();
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数orderIds:[%s]，conditionQuery:[%s],orderItemId:[%s]", request.getParameter("orderIds"), request.getParameter("conditionQuery"), request.getParameter("orderItemId")));
        }
        return new JsonResult(true).addList(orderService.findOrderItemVoByMap(map));
    }


    @OperationLog("批量更改物流")
    @RequestMapping({"/updateStautsByOrder", "/invoiceUpdateStautsByOrder"})
    @ResponseBody
    JsonResult updateStatusByOrder(Integer[] orderIds, String shippingComp) {
        if (null == orderIds) {
            return new JsonResult(false, "没有勾选订单");
        }
        if (StringUtils.isBlank(shippingComp)) {
            return new JsonResult(false, "物流公司不能为空");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数orderIds:[%s],shippingComp:[%s]", Arrays.toString(orderIds), shippingComp));
        }
        orderService.updateStatusByOrder(orderIds, shippingComp);

        BusinessLogUtil.bindBusinessLog("批量更新订单物流，订单ID:%s，物流变更为：%s",Arrays.toString(orderIds), shippingComp);

        return new JsonResult(true, "更新成功");
    }

    /**
     * 导入进销存 即 订单 确认
     *
     * @param orderIds 订单ID数组
     */
    @OperationLog("导入进销存")
    @RequestMapping("/confirm")
    @ResponseBody
    public JsonResult orderConfirm(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有勾选订单");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("OrderController中的orderConfirm方法参数是orderIds[%s]", Arrays.toString(orderIds)));
        }
        try {
            orderService.orderConfirm(orderIds);
            BusinessLogUtil.bindBusinessLog("导入进销存订单ID:%s",Arrays.toString(orderIds));
            return new JsonResult(true);
        } catch (StewardBusinessException e) {
            log.error(e.getMessage());
            BusinessLogUtil.bindBusinessLog(false,"导入进销存%s失败，失败原因:%s",Arrays.toString(orderIds),e.getMessage());
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 订单作废
     *
     * @param orderIds 订单ID数组
     */
    @OperationLog("订单作废")
    @RequestMapping("/cancellation")
    @ResponseBody
    public JsonResult orderCancellation(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有勾选订单");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数是orderIds[%s]", Arrays.toString(orderIds)));
        }
        try {
            orderService.orderCancellation(orderIds);
            BusinessLogUtil.bindBusinessLog("作废订单ID:%s",Arrays.toString(orderIds));
            return new JsonResult(true, "操作成功");
        } catch (StewardBusinessException e) {
            log.error(e.getMessage());
            BusinessLogUtil.bindBusinessLog(false,"作废订单%s失败，失败原因:%s",Arrays.toString(orderIds),e.getMessage());
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 批量审核订单
     *
     * @param orderIds 订单ID数组
     */
    @OperationLog("批量审核订单")
    @RequestMapping("/check")
    @ResponseBody
    public JsonResult orderCheck(Integer[] orderIds) {
        if (null == orderIds) {
            return new JsonResult(false, "没有勾选订单");
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数是orderIds[%s]", Arrays.toString(orderIds)));
        }
        try {
            orderService.orderCheck(orderIds);
            BusinessLogUtil.bindBusinessLog("批量审核订单ID:%s",Arrays.toString(orderIds));
            return new JsonResult(true, "审核成功");
        } catch (StewardBusinessException e) {
            log.error(e.getMessage());
            BusinessLogUtil.bindBusinessLog(false,"审核订单%s失败，失败原因:%s",Arrays.toString(orderIds),e.getMessage());
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 订单恢复 作废-->待审核
     *
     * @param orderIds 订单ID数组
     */
    @OperationLog("批量订单恢复")
    @RequestMapping("/recover")
    @ResponseBody
    public JsonResult orderRecover(Integer[] orderIds) {
        try {
            orderService.orderRecover(orderIds);
            BusinessLogUtil.bindBusinessLog("批量恢复订单ID:%s",Arrays.toString(orderIds));
            return new JsonResult(true, "恢复成功");
        } catch (StewardBusinessException e) {
            log.error(e.getMessage());
            BusinessLogUtil.bindBusinessLog(false,"批量恢复订单%s失败，失败原因:%s",Arrays.toString(orderIds),e.getMessage());
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * @param data
     * @return
     */
    @RequestMapping("/updateShippingNo")
    @ResponseBody
    @OperationLog("更新物流单号")
    public JsonResult updateShippingNo(String data, OrderStatus orderStatus) {
        Map dataMap = JsonUtil.json2Object(data, Map.class);
        String shippingNo = (String) dataMap.get("shippingNo");
        Integer orderId = (Integer) dataMap.get("id");
        orderService.updateShippingNo(shippingNo, orderStatus, orderId);
        BusinessLogUtil.bindBusinessLog("更新订单[id=%d]物流号为:%s",orderId,shippingNo);
        return new JsonResult(true, "操作成功");
    }

    /**
     * 加赠品或补货商品
     *
     * @param queryProdVos
     * @param orderIds
     * @return
     * @throws Exception
     */
    @OperationLog("加赠品或补货商品")
    @RequestMapping("/addGift")
    @ResponseBody
    public JsonResult addGiftOrderByHand(String queryProdVos, Integer[] orderIds) throws Exception {
        if (log.isInfoEnabled()) {
            log.info(" 传值queryProdVos:[%s],orderIds:[%s]", queryProdVos, orderIds);
        }
        if (!queryProdVos.startsWith("[")) {
            queryProdVos = "[" + queryProdVos + "]";
        }
        QueryProdVo[] queryProdVos1 = JsonUtil.jsonToArray(queryProdVos, QueryProdVo[].class);

        orderService.addGiftByHand(queryProdVos1, orderIds);

        //Record business log
        StringBuilder sb=new StringBuilder();
        for(QueryProdVo qpv:queryProdVos1){
            sb.append("{sku:").append(qpv.getSku()).append(",数量:").append(qpv.getNum()).append("},");
        }
        BusinessLogUtil.bindBusinessLog("给订单%s添加商品:%s",Arrays.toString(orderIds),sb.toString());

        return new JsonResult(true, "添加成功");
    }

    /**
     * 手动加正常订单货补货订单
     *
     * @param queryProdVos
     * @param addOrderVo
     * @param orderType
     * @return
     * @throws IOException
     */
    @OperationLog("手动加正常订单或补货订单")
    @RequestMapping("/addOrder")
    @ResponseBody
    public JsonResult addOrderByHand(String queryProdVos, AddOrderVo addOrderVo, String orderType, String platformType) throws IOException, NoSuchMethodException, IllegalAccessException {
        if (log.isInfoEnabled()) {
            log.info(String.format("传值queryProdVos:[%s],addOrderVo:[%s]", queryProdVos, addOrderVo));
        }

        if (!queryProdVos.startsWith("[")) {
            queryProdVos = "[" + queryProdVos + "]";
        }

        QueryProdVo[] queryProdVos1 = JsonUtil.jsonToArray(queryProdVos, QueryProdVo[].class);

        if (log.isInfoEnabled()) {
            log.info(String.format("传值queryProdVos1[%s],addOrderVo:[%s]", queryProdVos1.toString(), addOrderVo.toString()));
        }
        List<String> orders = orderService.addOrderByHand(addOrderVo, queryProdVos1, orderType, platformType);
        return new JsonResult(true, "添加订单成功！").addList(orders);
    }

    /**
     * 手动加换货订单
     *
     * @param queryProdVos
     * @param addOrderVo
     * @return
     * @throws IOException
     */
    @OperationLog("手动加换货订单")
    @RequestMapping("/addExchangeOrder")
    @ResponseBody
    public JsonResult addExchangeOrderByHand(String queryProdVos, AddOrderVo addOrderVo, Integer orderItemId, String platformType) throws IOException, NoSuchMethodException, IllegalAccessException {
        if (log.isInfoEnabled()) {
            log.info(String.format("传值queryProdVos:[%s],addOrderVo:[%s]", queryProdVos, addOrderVo));
        }

        if (!queryProdVos.startsWith("[")) {
            queryProdVos = "[" + queryProdVos + "]";
        }

        QueryProdVo[] queryProdVos1 = JsonUtil.jsonToArray(queryProdVos, QueryProdVo[].class);

        if (log.isInfoEnabled()) {
            log.info(String.format("传值queryProdVos1:[%s],addOrderVo:[%s]", queryProdVos1, addOrderVo));
        }

        List<String> s = orderService.addExchangeOrderByHand(addOrderVo, queryProdVos1, orderItemId, platformType);
        return new JsonResult(true, "添加换货订单成功！").addList(s);
    }

    @RequestMapping("/shopList")
    @ResponseBody
    public JsonResult findAllShop(Shop shop) {
        List<Shop> shopList = shopService.getShop(shop);
        return new JsonResult(true).addList(shopList);
    }

    /**
     * 刷单按钮
     * @param orderIds
     * @return
     */
    @OperationLog("刷单操作")
    @RequestMapping("/cheatOrder")
    @ResponseBody
    public JsonResult cheatOrder(Integer[] orderIds){
        orderService.cheatOrder(orderIds);
        BusinessLogUtil.bindBusinessLog("进行刷单操作的订单编号为[%s]",Arrays.toString(orderIds));
        return new JsonResult(true,"刷单操作成功!");
    }

    /**
     * 换货前订单校验
     *
     * @param request
     * @return
     */
    @RequestMapping("/checkOrder")
    @ResponseBody
    public JsonResult checkOrder(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, Object[]> map = request.getParameterMap();
        orderService.checkOrder(Integer.parseInt(map.get("orderIds")[0].toString()));
        return new JsonResult(true).addList(orderService.findOrderItemVoByMap(map));
    }

    /**
     * 售前换货
     *
     * @param oldOrderItemId
     * @param productId
     * @param count
     * @return
     */
    @OperationLog("售前换货")
    @RequestMapping("/exchangeGood")
    @ResponseBody
    public JsonResult exchangeGoods(Integer oldOrderItemId, Integer productId, Integer count) {
        if (log.isInfoEnabled()) {
            log.info(String.format("方法参数是oldOrderItemId[{}],productId[{}],count[{}]", oldOrderItemId, productId, count));
        }
        orderService.exchangeGoods(oldOrderItemId, productId, count);

        BusinessLogUtil.bindBusinessLog("换货前订单项id[%d],新商品id[%d],数量[%d]",oldOrderItemId,productId,count);

        return new JsonResult(true, "操作成功");
    }

    /**
     * @param orderIds
     * @return
     */
    @RequestMapping("/approveLogs")
    @ResponseBody
    public JsonResult approveLogsByOrderId(Integer orderIds) {
        return new JsonResult(true).addObject(orderService.findApproveLogs(orderIds));
    }

    /**
     * 手动拆分订单
     *
     * @param orderId
     * @param orderItemIds
     * @return
     */
    @OperationLog("手动拆分订单")
    @RequestMapping("/splitOrder")
    @ResponseBody
    public JsonResult splitOrderByHand(Integer orderId, Integer[] orderItemIds) {
        List<String> stringList = orderService.spiltOrderByHand(orderId, orderItemIds);
        BusinessLogUtil.bindBusinessLog("手动拆单，订单ID[%d],订单项ID%s",orderId,Arrays.toString(orderItemIds));
        return new JsonResult(true, "操作成功").addList(stringList);
    }

    @OperationLog("Excel导出换货订单")
    @RequestMapping("/extractExchangeOrder2Excel")
    @ResponseBody
    public JsonResult extractExchangeOrder2Excel(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        OutputStream os = response.getOutputStream();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object[]> map = request.getParameterMap();
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR) + "ExchangeOrder.xls");
            response.setContentType("application/octet-stream; charset=utf-8");
            Workbook workbook = orderService.reportExchangeOrderItem(map);
            workbook.write(os);
            os.flush();
            return new JsonResult(true, "导出成功！");
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    @OperationLog("Excel导出查询订单")
    @RequestMapping("/reportOrders")
    @ResponseBody
    public void reportOrders(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Map<String, Object[]> map = request.getParameterMap();
        Workbook workbook= orderService.reportOrderAndOrderItems(map);
        OutputStream os = response.getOutputStream();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR) + "ReportOrders.xls");
        response.setContentType("application/octet-stream; charset=utf-8");
        workbook.write(os);
        os.flush();
        os.close();
//        @SuppressWarnings("unchecked")
//        Map<String, Object[]> map = request.getParameterMap();
//        orderService.reportOrderAndOrderItems(map,response);

    }

    @OperationLog("订单管理导入订单")
    @RequestMapping("/leadInOrders")
    @ResponseBody
    public JsonResult leadInOrders(MultipartFile multipartFile) throws IOException, ParseException {
        String fileName = multipartFile.getOriginalFilename();
        if (!fileName.matches("^.+\\.xlsx$")) {
            if (log.isInfoEnabled()) {
                log.info("导入的文件格式不正确，不是xlsx文件");
            }
            return new JsonResult(false, "导入的文件格式不正确！");
        }
        XSSFWorkbook wb;
        InputStream is = null;
        Map<String, Integer> num;
        List<Row> rows = new ArrayList<Row>();
        try {
            is = multipartFile.getInputStream();
            wb = new XSSFWorkbook(is);
            Sheet sheet = wb.getSheetAt(0);
            if (sheet.getLastRowNum() < 1) {
                throw new StewardBusinessException("Excel导入订单没有数据");
            }
            sheet.removeRow(sheet.getRow(0)); //remove 前一行标题
            for (Row row : sheet) {
                rows.add(row);
            }
            num = orderService.OrderLeadIn(rows);
        } finally {
            if (is != null) is.close();
        }
        return new JsonResult(true, String.format("导入成功!导入了%s条数据,生成了%s条订单", num.get("recordNum"), num.get("orderNum")));
    }
}
