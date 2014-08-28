package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.bean.TransferInfoBean;
import com.ejushang.steward.ordercenter.constant.DeliveryType;
import com.ejushang.steward.ordercenter.exception.ErpBusinessException;
import com.ejushang.steward.ordercenter.service.LogisticsService;
import com.ejushang.steward.ordercenter.vo.LogisticsInfoVo;
import com.ejushang.uams.client.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * User: Blomer
 * Date: 14-4-8
 * Time: 下午2:17
 */
@Controller
@RequestMapping(value = "/tb/logistics")
public class LogisticsInfoController {
    private static final Logger log = LoggerFactory.getLogger(LogisticsInfoController.class);

    @Autowired
    private LogisticsService logisticsInfoService;

    /**
     * 接受第三方物流推送的数据.
     */
    @RequestMapping(value = "/back")
    public JsonResult logisticsBack(String param, HttpServletResponse response) throws IOException {
        if (log.isInfoEnabled()){
            log.info("第三方物流(kuaidi100)发送过来的物流信息: (" + param+ ")");
        }

        if (StringUtils.isBlank(param)) {
            return new JsonResult(false, "没有找到相应的物流数据");
        }
        try {
            logisticsInfoService.handleThirdLogisticsInfo(param.trim());
            return new JsonResult(true);
        } catch (Exception e) {
            if (log.isErrorEnabled())
                log.error("处理第三方物流查询平台回调数据时异常: ", e);

            return new JsonResult(false);
        }
    }

    /**
     * 向第三方物流发送查询请求
     */
    @RequestMapping(value = "/send")
    public JsonResult sendLogisticsRequest(String orderNo, String company, String expressNo,
                                           String to, HttpServletResponse response) throws IOException {
        // 只是拆过多单的需要向 快递100 发送请求, 其他的是否应该由天猫提供物流信息?
        if (StringUtils.isBlank(orderNo)) {
            return new JsonResult(false, "请务必传入订单号");

        }
        if (StringUtils.isBlank(company)) {
            return new JsonResult(false, "请务必传入物流公司(英文字母)");
        }
        DeliveryType deliveryType;
        try {
            deliveryType = DeliveryType.valueOf(company);
        } catch (Exception e) {
            if (log.isErrorEnabled())
                log.error("未定义此物流公司(" + company + ")信息");
            return new JsonResult(false, "未定义此物流公司(\" + company + \")信息, 请联系技术部!");
        }
        if (deliveryType == null) {
            if (log.isWarnEnabled())
                log.warn("没有物流公司信息, 无法发送物流(" + expressNo + ")请求.");
            return new JsonResult(false, "未定义此物流公司(\" + company + \")信息, 请联系技术部!");
        }
        if (StringUtils.isBlank(expressNo)) {
            return new JsonResult(false,"请务必传入物流编号");
        }
        if (StringUtils.isBlank(to)) {
            return new JsonResult(false,"第三方物流查询平台要求必须传入收货地(中文), 到市即可. 如: 广东省深圳市");
        }

        try {
            logisticsInfoService.sendLogisticsInfoRequest(orderNo, deliveryType, expressNo, to);
        } catch (ErpBusinessException e) {
            return new JsonResult(false,e.getMessage());
        } catch (DuplicateKeyException e) {
            // 只在并发的时候才可能出现
            return new JsonResult(false,"已向第三方物流查询平台发送过请求.");
        }

        return new JsonResult(true);
    }

    /**
     * 批量查询物流信息
     * @param expressNos
     * @param response
     * @throws IOException
     */
    @RequestMapping("/findByExpressNo")
    public JsonResult findByExpressNo(String expressNos,HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        if(StringUtils.isBlank(expressNos)){
            return new JsonResult(false,"物流单号不能为空，请输入物流单号");
        }
        String[] expressArr = expressNos.split("\\n");
        if(expressArr == null){
            throw new ErpBusinessException("参数解析错误！expressNos = " + expressNos);
        }
        List<LogisticsInfoVo> logisticsInfoList = logisticsInfoService.findLogisticsInfoByExpressNos(Arrays.asList(expressArr));
        return new JsonResult(true,"物流信息查询成功！").addList(logisticsInfoList);
    }

    /**
     * 查询中转信息
     * @param expressNo
     * @param response
     * @throws IOException
     */
    @RequestMapping("/detailByExpressNo")
    public JsonResult detailByExpressNo(String expressNo,HttpServletResponse response) throws IOException {
        List<TransferInfoBean> transferInfoBeanList = logisticsInfoService.findTransferInfoByExpressNo(expressNo);
        return new JsonResult(true,"物流信息查询成功！").addList(transferInfoBeanList);
    }

    /**
     * 导出
     * @param expressNos
     * @param response
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/exportByExpressNos")
    public JsonResult exportByExpressNos(String expressNos,HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        if(StringUtils.isBlank(expressNos)){
            return new JsonResult(false,"物流单号不能为空，请输入物流单号");
        }

        String[] expressArr = expressNos.split("\\n");
        if(expressArr == null){
            throw new ErpBusinessException("参数解析错误！expressNos = " + expressNos);
        }

        File file = logisticsInfoService.exportLogisticsFullInfoBeanByExpressNos(Arrays.asList(expressArr));

        InputStream is = new BufferedInputStream(new FileInputStream(file));
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        //清空response
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
        response.addHeader("Content-Length", "" + file.length());
        response.setContentType("application/octet-stream");
        FileCopyUtils.copy(is, toClient);
        return new JsonResult(true);
//        byte[] buffer = new byte[is.available()];
//        try{
//            is.read(buffer);
//            // 清空response
//            response.reset();
//            // 设置response的Header
//            response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
//            response.addHeader("Content-Length", "" + file.length());
//            response.setContentType("application/octet-stream");
//            toClient.write(buffer);
//            toClient.flush();
//        }
//        finally {
//            is.close();
//            toClient.close();
//        }

    }


}
