package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.JsonUtil;
import com.ejushang.steward.ordercenter.bean.LogisticsFullInfoBean;
import com.ejushang.steward.ordercenter.bean.TransferInfoBean;
import com.ejushang.steward.ordercenter.constant.DeliveryType;
import com.ejushang.steward.ordercenter.constant.OrderStatus;
import com.ejushang.steward.ordercenter.domain.Logistics;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.domain.OrderSignedLog;
import com.ejushang.steward.ordercenter.exception.ErpBusinessException;
import com.ejushang.steward.ordercenter.util.WebUtil;
import com.ejushang.steward.ordercenter.vo.LogisticsInfoVo;
import com.ejushang.steward.ordercenter.vo.logistics.BackMsg;
import com.ejushang.steward.ordercenter.vo.logistics.BackResult;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * User: Blomer
 * Date: 14-4-8
 * Time: 下午2:08
 */
@Service
public class LogisticsService {

    private static final Logger log = LoggerFactory.getLogger(LogisticsService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private RequestLogisticsInfo requestLogisticsInfo;

    @Autowired
    private OrderFlowService orderFlowService;

    @Autowired
    private OrderSignedLogService orderSignedLogService;

    public List<Logistics> list() {
        return null;
    }

    @Transactional
    public void save(Logistics logistics) {
        if (log.isInfoEnabled()) {
            log.info("LogisticsInfoService中的save方法,参数{}", logistics);
        }
        generalDAO.saveOrUpdate(logistics);
    }


    /**
     * 向 kuaidi100 发送物流配送信息请求
     *
     * @param orderNo 订单号
     * @param company 物流公司
     * @param expressNo 物流单号
     * @param to 收货地址(kuaidi100 要求必须提供. 到市一级即可. 如广东省深圳市, 北京市)
     *
     * @throws ErpBusinessException 已经向第三方请求过物流查询
     * @throws org.springframework.dao.DuplicateKeyException 发生并发(两个人同时确认发送请求或网速太慢点击太快)时抛出的异常
     */
    @Transactional
    public void sendLogisticsInfoRequest(String orderNo, DeliveryType company,
                                         String expressNo, String to) throws ErpBusinessException, DuplicateKeyException {
        Logistics logistics = findLogisticsInfoByExpressNo(expressNo);
        if (logistics != null && logistics.getWasRequest()) {
            throw new ErpBusinessException(String.format("快递单(%s/%s)已经向第三方发送过请求, 不需要再发送.", company.getName(), expressNo));
        }

        // 保存, 物流单号的唯一约束, 若并发请求时, 会抛出 DuplicateKeyException 数据库异常
        save(new Logistics(orderNo, expressNo, company.getName(), to));

        // 向第三方物流请求成功则写入数据库
        if (requestLogisticsInfo.requestThirdLogistics(company.toString(), expressNo, to)) {
            if (log.isInfoEnabled())
                log.info(String.format("快递单(%s/%s)向第三方物流请求成功.", company.toDesc(), expressNo));

            updateLogisticsInfo(new Logistics(expressNo, true));
        }  else{
            if (log.isInfoEnabled())
                log.info(String.format("快递单(%s/%s)向第三方物流请求失败.", company.toDesc(), expressNo));
        }
    }

    /**
     * 根据订单号和物流单号检索物流信息
     *
     * @param logistics
     * @return
     */
    @Transactional
    public List<Logistics> findLogisticsByNoes(Logistics logistics) {
        Search search = new Search(Logistics.class);
        if (logistics != null) {
            if (StringUtils.isNotBlank(logistics.getOrderNo())) {
                search.addFilterEqual("orderNo", logistics.getOrderNo());
            }
            if (StringUtils.isNotBlank(logistics.getExpressNo())) {
                search.addFilterEqual("expressNo", logistics.getExpressNo());
            }
        }
        return generalDAO.search(search);
    }

    /**
     * 处理 kuaidi100 推送过来的物流数据
     *
     * @param logisticsMsg kuaidi100 推送的物流数据
     */
    @Transactional
    public void handleThirdLogisticsInfo(String logisticsMsg) {
        if (StringUtils.isBlank(logisticsMsg)) return;

        // 将返回的数据转换为 对象
        BackMsg backMsg = null;
        try {
            backMsg = JsonUtil.json2Object(logisticsMsg, BackMsg.class);
        } catch (Exception e) {
            if (log.isWarnEnabled())
                log.warn(String.format("物流信息(%s)解析错误:", logisticsMsg), e);
        }
        if (backMsg == null) return;

        BackResult backResult = backMsg.getLastResult();
        if (backResult == null) return;


        List<LinkedHashMap<String, String>> detailList = backResult.getData();
        if (detailList == null || detailList.size() == 0) return;

        // 最新的记录时间(索引在第一个)
        Date latestTime = getOperationDate(detailList.get(0));
        // 第一条记录时间(索引在最后一个)
        Date firstTime = getOperationDate(detailList.get(detailList.size() - 1));

        updateLogisticsInfo(new Logistics(backResult.getNu(), firstTime, latestTime, backResult.getIscheck(), logisticsMsg));

    }

    private Date getOperationDate(Map<String, String> logisticsInfo) {
        Date date = null;
        try {
            date = EJSDateUtils.parseDate(logisticsInfo.get("time"), EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
        } catch (Exception e) {
            if (log.isWarnEnabled())
                log.warn(String.format("物流记录(%s)时间转换出错:", logisticsInfo), e);
            // ignore time
        }
        return date;
    }

    @Transactional(readOnly = true)
    public List<Logistics> findLogisticsInfoByOrderNo(String orderNo) {
        return findLogisticsInfo(orderNo, StringUtils.EMPTY);
    }

    @Transactional(readOnly = true)
    public Logistics findLogisticsInfoByExpressNo(String expressNo) {
        return findLogisticsInfoByNoAndCompany(StringUtils.EMPTY, expressNo);
    }

    private List<Logistics> findLogisticsInfo(String orderNo, String expressNo) {
        Logistics logistics = new Logistics();
        if (StringUtils.isNotBlank(orderNo))
            logistics.setOrderNo(orderNo);
        if (StringUtils.isNotBlank(expressNo))
            logistics.setExpressNo(expressNo);
        return findLogisticsByNoes(logistics);
    }


    public Logistics findLogisticsInfoByNoAndCompany(String orderNo, String expressNo) {
        List<Logistics> logisticsList = findLogisticsInfo(orderNo, expressNo);
        if (logisticsList != null && logisticsList.size() == 1)
            return logisticsList.get(0);
        return null;
    }

    /**
     *
     * @param hourCount
     * @return
     */
    @Transactional(readOnly = true)
    public List<Logistics> findNotSuccessLogisticsByLaTestTime(int hourCount) {
        Search search = new Search(Logistics.class);
        search.addFilterNotNull("expressInfo");
        search.addFilterNotEmpty("expressInfo");
        search.addFilterEqual("expressStatus",0);
        search.addFilterLessOrEqual("latestTime",DateUtils.addHours(EJSDateUtils.getCurrentDate(),-(hourCount)));
        search.addSortAsc("latestTime");
        return generalDAO.search(search);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findNotSuccessLogisticsByFirstTime(int hourCount) {
        Search search = new Search(Logistics.class);
        search.addFilterNotNull("expressInfo");
        search.addFilterNotEmpty("expressInfo");
        search.addFilterEqual("expressStatus",0);
        search.addFilterLessOrEqual("firstTime", DateUtils.addHours(EJSDateUtils.getCurrentDate(),-(hourCount)));
        search.addSortAsc("firstTime");
        return generalDAO.search(search);
    }





    /**
     *
     * 更新物流信息
     * @param logistics
     */
    @Transactional
    public void updateLogisticsInfo(Logistics logistics) {
        Logistics logistics1 =getByExpressNo(logistics.getExpressNo());
        if(logistics.getWasRequest()!=null){
            logistics1.setWasRequest(logistics.getWasRequest());
        }
        if(logistics.getFirstTime()!=null){
            logistics1.setFirstTime(logistics.getFirstTime());
        }
        if(logistics.getLatestTime()!=null){
            logistics1.setLatestTime(logistics.getLatestTime());
        }
        if(StringUtils.isNotBlank(logistics.getExpressInfo())){
            logistics1.setExpressInfo(logistics.getExpressInfo());
        }
        if(logistics.getExpressStatus()!=null){
            logistics1.setExpressStatus(logistics.getExpressStatus());
        }
        generalDAO.saveOrUpdate(logistics1);
        if(logistics1.getExpressStatus()!=null && logistics1.getExpressStatus().equals(Boolean.TRUE)){
            Order order =(Order)generalDAO.search(new Search(Order.class).addFilterEqual("orderNo",logistics1.getOrderNo())).get(0);
            OrderStatus from = OrderStatus.INVOICED;
            OrderStatus to = OrderStatus.SIGNED;
            orderFlowService.changeStatus(order, from, to, true);
            OrderSignedLog orderSignedLog=new OrderSignedLog();
            orderSignedLog.setOrderId(order.getId());
            orderSignedLog.setCreateTime(EJSDateUtils.getCurrentDate());
            orderSignedLogService.saveOrderSignedLog(orderSignedLog);
        }
    }

    public Logistics getByExpressNo(String expressNo){
        Logistics logistics =null;
        Search search=new Search(Logistics.class);
        search.addFilterEqual("expressNo",expressNo);
        List<Logistics> logisticses =generalDAO.search(search);
        if(logisticses.size()>0){
            logistics = logisticses.get(0);
        }
        return logistics;
    }

    @Transactional
    public void deleteLogisticsInfo(int id) {
        generalDAO.removeById(Logistics.class,id);
    }

    @Transactional(readOnly = true)
    public List<TransferInfoBean> findTransferInfoByExpressNo(String expressNo) {

        if (log.isInfoEnabled()) {
            log.info("物流中转信息查询：：：：：：");
        }

        // 查询物流信息
        Logistics logistics = findLogisticsInfoByExpressNo(expressNo);
        if (logistics == null) {
            throw new ErpBusinessException("中转信息查询：查不到对应的物流信息！物流单号：" + expressNo);
        }
        // 创建保存中转信息的集合
        List<TransferInfoBean> transferInfoBeanList = new ArrayList<TransferInfoBean>();
        TransferInfoBean transferInfoBean = null;
        // 解析物流中转信息
        BackMsg backMsg = JsonUtil.json2Object(logistics.getExpressInfo(), BackMsg.class);
        // 获得中转信息集合
        List<LinkedHashMap<String, String>> dataList = backMsg.getLastResult().getData();
        for (LinkedHashMap<String, String> linkedHashMap : dataList) {
            transferInfoBean = new TransferInfoBean();
            transferInfoBean.setExpressNo(logistics.getExpressNo());
            transferInfoBean.setContext(linkedHashMap.get("context"));
            transferInfoBean.setTransferTime(EJSDateUtils.parseDate(linkedHashMap.get("time"), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            transferInfoBeanList.add(transferInfoBean);
        }
        return transferInfoBeanList;
    }

    /**
     * 根据物流编号查询
     * @param expressNos
     * @return
     */
    @Transactional(readOnly = true)
    public List<Logistics> findLogisticsByExpressNos(List<String> expressNos){
        Search search=new Search(Logistics.class);
        search.addFilterIn("expressNo",expressNos);
        return generalDAO.search(search);
    }

    @Transactional(readOnly = true)
    public List<LogisticsInfoVo> findLogisticsInfoByExpressNos(List<String> expressNos) throws InvocationTargetException, IllegalAccessException {

        List<LogisticsInfoVo> logisticsInfoVoList = new ArrayList<LogisticsInfoVo>();
        List<Logistics> logisticsList = findLogisticsByExpressNos(expressNos);

        LogisticsInfoVo logisticsInfoVo;
        for (Logistics logistics : logisticsList) {
            logisticsInfoVo = new LogisticsInfoVo();
            BeanUtils.copyProperties(logisticsInfoVo, logistics);
            logisticsInfoVo.setExpressCompanyName(DeliveryType.valueOf(logistics.getExpressCompany()).toDesc());
            logisticsInfoVo.setWasRequestName(logistics.getWasRequest() ? "已请求" : "未请求");
            logisticsInfoVo.setExpressStatusName(logistics.getExpressStatus() ? "配送完成" : "配送中");
            logisticsInfoVoList.add(logisticsInfoVo);
        }
        return logisticsInfoVoList;
    }


    /**
     * 导出物流信息
     * @param expressNos
     * @return
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     * @throws java.io.IOException
     */
    public File exportLogisticsFullInfoBeanByExpressNos(List<String> expressNos) throws InvocationTargetException, IllegalAccessException, IOException {
        if (log.isInfoEnabled()) {
            log.info("导出物流中转信息：：：：：：：");
        }

        // 获得物流信息
        List<Logistics> logisticsList = findLogisticsByExpressNos(expressNos);
//        if(CollectionUtils.isEmpty(logisticsList)){
//            return null;
//        }

        List<LogisticsFullInfoBean> logisticsFullInfoBeanList = new ArrayList<LogisticsFullInfoBean>();
        LogisticsFullInfoBean logisticsFullInfoBean;
        List<TransferInfoBean> transferInfoBeanList;
        TransferInfoBean transferInfoBean;
        BackMsg backMsg;
        List<LinkedHashMap<String, String>> dataList;
        for (Logistics logistics : logisticsList) {
            logisticsFullInfoBean = new LogisticsFullInfoBean();
            BeanUtils.copyProperties(logisticsFullInfoBean, logistics);
            // 创建保存中转信息的集合
            transferInfoBeanList = new ArrayList<TransferInfoBean>();
            // 解析物流中转信息
            backMsg = JsonUtil.json2Object(logistics.getExpressInfo(), BackMsg.class);
            // 获得中转信息集合
            dataList = backMsg.getLastResult().getData();
            for (LinkedHashMap<String, String> linkedHashMap : dataList) {
                transferInfoBean = new TransferInfoBean();
                transferInfoBean.setExpressNo(logistics.getExpressNo());
                transferInfoBean.setContext(linkedHashMap.get("context"));
                transferInfoBean.setTransferTime(EJSDateUtils.parseDate(linkedHashMap.get("time"), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                transferInfoBeanList.add(transferInfoBean);
            }
            logisticsFullInfoBean.setTransferInfoBeanList(transferInfoBeanList);
            logisticsFullInfoBeanList.add(logisticsFullInfoBean);
        }
        System.out.println("...");

        // 将物流中转信息保存至Excel文件中
        return convertLogistics2Excel(logisticsFullInfoBeanList);  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 将物流中转信息转化为Excel
     *
     * @param logisticsFullInfoBeanList
     * @return
     */
    private File convertLogistics2Excel(List<LogisticsFullInfoBean> logisticsFullInfoBeanList) throws IOException {

        // 创建Excel文档
        HSSFWorkbook book = new HSSFWorkbook();
        // 创建Excel工作sheet
        HSSFSheet sheet = book.createSheet("物流查询中转记录" + EJSDateUtils.formatDate(new Date(),
                EJSDateUtils.DateFormatType.SIMPLE_DATE_TIME_FORMAT_STR));

        // 创建列头字体样式
        HSSFFont fontForHead = book.createFont();
        fontForHead.setFontHeight((short) 210);
        fontForHead.setBoldweight((short) 210);

        HSSFFont fontForContent1 = book.createFont();
        fontForContent1.setFontHeight((short) 220);
        fontForContent1.setBoldweight((short) 220);

        // 设置单元格样式
        // 设置日期单元格内容格式
        HSSFCellStyle cellStyleForDateContent = book.createCellStyle();
        cellStyleForDateContent.setDataFormat(HSSFDataFormat.getBuiltinFormat(EJSDateUtils.DateFormatType.DATE_FORMAT_STR.getValue()));

        // 设置列头单元格样式
        HSSFCellStyle cellStyleForHead = book.createCellStyle();
        cellStyleForHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleForHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleForHead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyleForHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyleForHead.setFont(fontForHead);
        cellStyleForHead.setBottomBorderColor(HSSFColor.RED.index);
        cellStyleForHead.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleForHead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleForHead.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleForHead.setBorderRight(HSSFCellStyle.BORDER_THIN);

        // 设置内容单元格样式1
        HSSFCellStyle cellStyleForContent1 = book.createCellStyle();
        cellStyleForContent1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleForContent1.setFont(fontForContent1);
//        cellStyleForContent1.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
//        cellStyleForContent1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        cellStyleForContent1.setBottomBorderColor(HSSFColor.BLUE.index);
//        cellStyleForContent1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        cellStyleForContent1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        cellStyleForContent1.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        cellStyleForContent1.setBorderRight(HSSFCellStyle.BORDER_THIN);

        // 设置内容单元格样式2
        HSSFCellStyle cellStyleForContent2 = book.createCellStyle();
        cellStyleForContent2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//        cellStyleForContent2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//        cellStyleForContent2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        cellStyleForContent2.setBottomBorderColor(HSSFColor.BLUE.index);
//        cellStyleForContent2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        cellStyleForContent2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        cellStyleForContent2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        cellStyleForContent2.setBorderRight(HSSFCellStyle.BORDER_THIN);

        // 冻结窗格
        sheet.createFreezePane(1, 1);


        // 设置Excel每列宽度
        sheet.setColumnWidth(0, 4500); // 物流单号
        sheet.setColumnWidth(1, 4500); // 订单号
        sheet.setColumnWidth(2, 3500); // 物流公司
        sheet.setColumnWidth(3, 4500); // 收货地址
        sheet.setColumnWidth(4, 2500); // 物流状态
        sheet.setColumnWidth(5, 5500); // 第一条物流记录时间
        sheet.setColumnWidth(6, 5500); // 最后一条物流记录时间
        sheet.setColumnWidth(7, 5500); // 是否已请求第三方物流
        sheet.setColumnWidth(8, 5500); // 中转日期
        sheet.setColumnWidth(9, 18000); // 中转信息

        // 创建列头行
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 400);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("物流单号");
        cell.setCellStyle(cellStyleForHead);
        cell = row.createCell(1);
        cell.setCellValue("订单编号");
        cell.setCellStyle(cellStyleForHead);
        cell = row.createCell(2);
        cell.setCellValue("物流公司");
        cell.setCellStyle(cellStyleForHead);
        cell = row.createCell(3);
        cell.setCellValue("收货地址");
        cell.setCellStyle(cellStyleForHead);
        cell = row.createCell(4);
        cell.setCellValue("物流状态");
        cell.setCellStyle(cellStyleForHead);
        cell = row.createCell(5);
        cell.setCellValue("第一条物流记录时间");
        cell.setCellStyle(cellStyleForHead);
        cell = row.createCell(6);
        cell.setCellValue("最后一条物流记录时间");
        cell.setCellStyle(cellStyleForHead);
        cell = row.createCell(7);
        cell.setCellValue("是否已请求第三方物流");
        cell.setCellStyle(cellStyleForHead);
        cell = row.createCell(8);
        cell.setCellValue("中转日期");
        cell.setCellStyle(cellStyleForHead);
        cell = row.createCell(9);
        cell.setCellValue("中转信息");
        cell.setCellStyle(cellStyleForHead);


        LogisticsFullInfoBean logisticsFullInfoBean;
        // 一条物流信息有多少条物流中转信息
        int transferInfoCount = 0;
        TransferInfoBean transferInfoBean;
        int start = 1;
        for (int i = 0; i < logisticsFullInfoBeanList.size(); i++) {
            logisticsFullInfoBean = logisticsFullInfoBeanList.get(i);
            transferInfoCount = logisticsFullInfoBean.getTransferInfoBeanList().size();
            row = sheet.createRow(i + start);
            cell = row.createCell(0); // 物流单号
            if (StringUtils.isNotEmpty(logisticsFullInfoBean.getExpressNo())) {
                cell.setCellValue(logisticsFullInfoBean.getExpressNo());
            }
            cell.setCellStyle(cellStyleForContent1);
            cell = row.createCell(1); // 订单编号
            if (StringUtils.isNotEmpty(logisticsFullInfoBean.getOrderNo())) {
                cell.setCellValue(logisticsFullInfoBean.getOrderNo());
            }
            cell.setCellStyle(cellStyleForContent1);
            cell = row.createCell(2); // 物流公司
            if (StringUtils.isNotEmpty(logisticsFullInfoBean.getExpressCompany())) {
                cell.setCellValue(DeliveryType.valueOf(logisticsFullInfoBean.getExpressCompany()).toDesc());
            }
            cell.setCellStyle(cellStyleForContent1);
            cell = row.createCell(3); // 收货地址
            if (StringUtils.isNotEmpty(logisticsFullInfoBean.getSendTo())) {
                cell.setCellValue(logisticsFullInfoBean.getSendTo());
            }
            cell.setCellStyle(cellStyleForContent1);
            cell = row.createCell(4); // 物流状态
            if (logisticsFullInfoBean.getExpressStatus() != null) {
                cell.setCellValue(logisticsFullInfoBean.getExpressStatus() ? "配送完成" : "配送中");
            }
            cell.setCellStyle(cellStyleForContent1);
            cell = row.createCell(5); // 第一条物流记录时间
            if (logisticsFullInfoBean.getFirstTime() != null) {
                cell.setCellValue(EJSDateUtils.formatDate(logisticsFullInfoBean.getFirstTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            }
            cell.setCellStyle(cellStyleForContent1);
            cell = row.createCell(6); // 最后一条物流记录时间
            if (logisticsFullInfoBean.getLatestTime() != null) {
                cell.setCellValue(EJSDateUtils.formatDate(logisticsFullInfoBean.getLatestTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
            }
            cell.setCellStyle(cellStyleForContent1);
            cell = row.createCell(7); // 是否已请求第三方物流
            if (logisticsFullInfoBean.getWasRequest() != null) {
                cell.setCellValue(logisticsFullInfoBean.getWasRequest() ? "已请求" : "未请求");
            }
            cell.setCellStyle(cellStyleForContent1);

            // 单元格跨行
            sheet.addMergedRegion(new CellRangeAddress(i + start, i + start + transferInfoCount, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(i + start, i + start + transferInfoCount, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(i + start, i + start + transferInfoCount, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(i + start, i + start + transferInfoCount, 3, 3));
            sheet.addMergedRegion(new CellRangeAddress(i + start, i + start + transferInfoCount, 4, 4));
            sheet.addMergedRegion(new CellRangeAddress(i + start, i + start + transferInfoCount, 5, 5));
            sheet.addMergedRegion(new CellRangeAddress(i + start, i + start + transferInfoCount, 6, 6));
            sheet.addMergedRegion(new CellRangeAddress(i + start, i + start + transferInfoCount, 7, 7));

            for (int j = 0; j < transferInfoCount; j++) {
                transferInfoBean = logisticsFullInfoBean.getTransferInfoBeanList().get(j);
                if (j != 0) {
                    row = sheet.createRow(i + start + j);
                }
                cell = row.createCell(8); // 中转日期
                if (transferInfoBean.getTransferTime() != null) {
                    cell.setCellValue(EJSDateUtils.formatDate(transferInfoBean.getTransferTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
                }
                cell.setCellStyle(cellStyleForContent2);
                cell = row.createCell(9); // 中转信息
                if (StringUtils.isNotEmpty(transferInfoBean.getContext())) {
                    cell.setCellValue(transferInfoBean.getContext());
                }
                cell.setCellStyle(cellStyleForContent2);
            }
            start = start + transferInfoCount;
        }

        //String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String path = WebUtil.getWebAppPath();
        String parent = path + "excels";
        File dir = new File(parent);
        dir.mkdirs();
        File file = new File(parent, "logistics_" + EJSDateUtils.formatDate(new Date(), EJSDateUtils.DateFormatType.SIMPLE_DATE_TIME_FORMAT_STR) + ".xls");
        FileOutputStream fos = new FileOutputStream(file);
        try {
            book.write(fos);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return file;
    }

}
