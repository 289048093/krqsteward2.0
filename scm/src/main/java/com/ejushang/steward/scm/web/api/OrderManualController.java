package com.ejushang.steward.scm.web.api;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.page.PageFactory;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.bean.FetchConditionBean;
import com.ejushang.steward.ordercenter.bean.ShopBean;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.domain.Platform;
import com.ejushang.steward.ordercenter.rmi.IOriginalOrderRemoteService;
import com.ejushang.steward.ordercenter.service.OrderFetchService;
import com.ejushang.steward.ordercenter.service.api.ShopBeanService;
import com.ejushang.steward.ordercenter.service.transportation.PlatformService;
import com.ejushang.steward.ordercenter.vo.OrderHandVo;
import com.ejushang.steward.common.util.OperationLog;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * User: Baron.Zhang
 * Date: 2014/7/7
 * Time: 11:19
 */
@Controller
public class OrderManualController {

    private static final Logger log = LoggerFactory.getLogger(OrderManualController.class);

    @Autowired
    private ShopBeanService shopBeanService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private IOriginalOrderRemoteService originalOrderRemoteService;

    @Autowired
    private OrderFetchService orderFetchService;

    @OperationLog("手动抓取订单")
    @ResponseBody
    @RequestMapping("/omc/fetchOrderByManual")
    public JsonResult fetchOrderByManual(FetchConditionBean fetchConditionBean){
        // 判断是根据时间段查询，还是根据订单号查询。
        if(StringUtils.equalsIgnoreCase(fetchConditionBean.getFetchByTypeName(),FetchByType.FETCH_BY_DATE.getName())){
            if(log.isInfoEnabled()){
                log.info("根据时间段查询数据：开始查询");
            }

            // 抓取日期获取并校验
            Date startDate = fetchConditionBean.getFetchStartDate();
            if(startDate == null){
                return new JsonResult(false,"【开始时间】不能为空！");
            }
            else if(EJSDateUtils.isNew(startDate,EJSDateUtils.getCurrentDate())){
                return new JsonResult(false,"【开始时间】不能大于【当前时间】！");
            }
            Date endDate = fetchConditionBean.getFetchEndDate();
            if(endDate == null ||
                    EJSDateUtils.isNew(endDate,EJSDateUtils.getCurrentDate())){
                endDate = EJSDateUtils.getCurrentDate();
            }
            if(EJSDateUtils.isNew(startDate,endDate)){
                return new JsonResult(false,"【开始时间】不能大于【结束时间】！");
            }

            if(!StringUtils.equalsIgnoreCase(fetchConditionBean.getFetchDataTypeName(),FetchDataType.FETCH_ORDER.getName())){
                return new JsonResult(false,"目前只支持抓取数据类型为：抓取订单！");
            }
        }
        else{
            // 根据单号查询 目前只查询订单
            // 平台和店铺必须不能为空
            if(fetchConditionBean.getPlatformId() == null){
                return new JsonResult(false,"根据单号查询：平台必须选择！");
            }

            if(fetchConditionBean.getShopId() == null){
                return new JsonResult(false,"根据单号查询：店铺必须选择！");
            }

            if(StringUtils.isBlank(fetchConditionBean.getFetchDataTypeName())){
                return new JsonResult(false,"根据单号查询：抓取订单数据类型必须选择！");
            }

            if(!StringUtils.equalsIgnoreCase(fetchConditionBean.getFetchDataTypeName(),FetchDataType.FETCH_ORDER.getName())){
                return new JsonResult(false,"目前只支持抓取数据类型为：抓取订单！");
            }

            if(StringUtils.isBlank(fetchConditionBean.getPlatformNos())){
                return new JsonResult(false,"根据单号查询：订单号必须输入，且要以英文“,”分隔！");
            }
        }

        Map<String,String> fetchDataParamMap = getFetchDataParamMap(fetchConditionBean);
        try {
            originalOrderRemoteService.getRemoteFetchDataParamMap(fetchDataParamMap);
        } catch (Exception e) {
            if(log.isErrorEnabled()){
                log.error("远程获取抓取数据参数map，出现异常：",e);
            }
            return new JsonResult(false,"发送抓单请求失败！异常："+e.getMessage());
        }

        return new JsonResult(true,"已成功发送抓单请求，系统会自动执行抓单");
    }

    /**
     * 将参数对象转换为map
     * @param fetchConditionBean
     * @return
     */
    private Map<String,String> getFetchDataParamMap(FetchConditionBean fetchConditionBean) {
        Map<String,String> fetchDataParamMap = new HashMap<String,String>();
        fetchDataParamMap.put("platformId",String.valueOf(fetchConditionBean.getPlatformId()));
        fetchDataParamMap.put("shopId",String.valueOf(fetchConditionBean.getShopId()));
        fetchDataParamMap.put("fetchStartDate", EJSDateUtils.formatDate(fetchConditionBean.getFetchStartDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        fetchDataParamMap.put("fetchEndDate",EJSDateUtils.formatDate(fetchConditionBean.getFetchEndDate(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        fetchDataParamMap.put("fetchByTypeName",fetchConditionBean.getFetchByTypeName());
        fetchDataParamMap.put("fetchDataTypeName",fetchConditionBean.getFetchDataTypeName());
        fetchDataParamMap.put("fetchDateTypeName",fetchConditionBean.getFetchDateTypeName());
        fetchDataParamMap.put("platformNos",fetchConditionBean.getPlatformNos());
        return fetchDataParamMap;
    }

    /**
     * 获取返回前端的结果对象集合
     * @param originalOrderList
     * @return
     */
    private List<OrderHandVo> getOrderHandVoList(List<OriginalOrder> originalOrderList){
        // 保存返回给前端的信息
        List<OrderHandVo> orderHandVoList = new ArrayList<OrderHandVo>();
        if(CollectionUtils.isNotEmpty(originalOrderList)){
            for(OriginalOrder originalOrder : originalOrderList){
                OrderHandVo orderHandVo = new OrderHandVo();
                orderHandVo.setOriginalOrderId(originalOrder.getId());
                orderHandVo.setPlatformOrderNo(originalOrder.getPlatformOrderNo());
                orderHandVo.setStatus("成功");
                orderHandVo.setDescription(originalOrder.getPlatformType().getValue());
                orderHandVoList.add(orderHandVo);
            }
        }
        return orderHandVoList;
    }

    /**
     * 获取所有订单的订单号
     * @param originalOrderList
     * @return
     */
    private List<Integer> getOriginalOrderIdList(List<OriginalOrder> originalOrderList){
        List<Integer> originalOrderIdList = new ArrayList<Integer>();
        if(CollectionUtils.isNotEmpty(originalOrderList)){
            for(OriginalOrder originalOrder : originalOrderList){
                originalOrderIdList.add(originalOrder.getId());
            }
        }
        return originalOrderIdList;
    }

    /**
     * 设置店铺的关键抓单参数
     * @param fetchConditionBean
     * @param startDate
     * @param endDate
     */
    private List<ShopBean> getShopBeans(FetchConditionBean fetchConditionBean, Date startDate, Date endDate) {
        // 根据条件查询所有需要抓单的店铺信息
        List<ShopBean> shopBeanList = findShopBeans(fetchConditionBean);
        // 构造每个店铺的抓取条件
        for(ShopBean shopBean : shopBeanList){
            // 获取并设置抓取的数据类型
            List<FetchDataType> fetchDataTypeList = getFetchDataTypeList(fetchConditionBean);
            FetchOptType fetchOptType = FetchOptType.HAND;
            // 这里设置抓取操作类型为 HAND
            shopBeanService.assignShopBean(startDate, endDate,fetchOptType,fetchDataTypeList, shopBean);
        }
        return shopBeanList;
    }


    /**
     * 获取抓单数据类型
     * @param fetchConditionBean
     * @return
     */
    private List<FetchDataType> getFetchDataTypeList(FetchConditionBean fetchConditionBean) {
        // 抓取数据类型设置
        List<FetchDataType> fetchDataTypeList = new ArrayList<FetchDataType>();
        if(StringUtils.isNotBlank(fetchConditionBean.getFetchDataTypeName())){
            fetchDataTypeList.add(FetchDataType.valueOf(fetchConditionBean.getFetchDataTypeName()));
        }
        else{
            fetchDataTypeList.addAll(Arrays.asList(FetchDataType.values()));
        }
        return fetchDataTypeList;
    }

    /**
     * 获取抓单店铺
     * @param fetchConditionBean
     * @return
     */
    private List<ShopBean> findShopBeans(FetchConditionBean fetchConditionBean) {
        // 构造查询店铺条件
        ShopBean shopBeanQuery = new ShopBean();
        if(fetchConditionBean.getPlatformId() != null){
            Platform platform = platformService.getById(fetchConditionBean.getPlatformId());
            if(platform != null) {
                shopBeanQuery.setPlatformType(PlatformType.valueOf(platform.getType()));
            }
        }
        shopBeanQuery.setShopId(fetchConditionBean.getShopId());
        // 查询要抓取记录的店铺及其授权信息
        return shopBeanService.findShopBean(shopBeanQuery);
    }


    @OperationLog("获取抓取数据类型列表")
    @ResponseBody
    @RequestMapping("/omc/findFetchDataType")
    public JsonResult findFetchDataType(){
        return new JsonResult(true).addList(Arrays.asList(FetchDataType.values()));
    }

    @OperationLog("获取抓取日期类型列表")
    @ResponseBody
    @RequestMapping("/omc/findFetchDateType")
    public JsonResult findFetchDateType(){
        return new JsonResult(true).addList(Arrays.asList(FetchDateType.values()));
    }

    @OperationLog("获取抓取条件类型列表")
    @ResponseBody
    @RequestMapping("/omc/findFetchByType")
    public JsonResult findFetchByType(){
        return new JsonResult(true).addList(Arrays.asList(FetchByType.values()));
    }

    @OperationLog("获取手动抓单记录日志")
    @ResponseBody
    @RequestMapping("/omc/findOrderFetchByHand")
    public JsonResult findOrderFetchByHand(HttpServletRequest request){
        Page page = PageFactory.getPage(request);
        orderFetchService.findOrderFetchByCondition(null,null,null,FetchOptType.HAND,page);
        return new JsonResult(true).addObject(page);
    }

}
