package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.util.TaoBaoLogUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.*;
import com.taobao.api.request.*;
import com.taobao.api.response.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h3>淘宝物流API</h3>
 * 提供了发货，物流单详情，区域地址和物流公司信息查询功能<br/>
 * User: Baron.Zhang
 * Date: 13-12-13
 * Time: 下午2:23
 */
public class TbLogisticsApi {
    private TaobaoClient client;
    private String sessionKey;
    public TbLogisticsApi(String sessionKey){
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    /**
     * taobao.areas.get 查询地址区域<br/>
     * 查询标准地址区域代码信息 参考：http://www.stats.gov.cn/tjbz/xzqhdm/t20100623_402652267.htm<br/>
     * @param fields
     * @return
     */
    public List<Area> getAreas(String fields) throws ApiException {
        AreasGetRequest req = new AreasGetRequest();
        req.setFields(fields);
        AreasGetResponse response = null;
        response = client.execute(req,sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getAreas();
    }

    /**
     * taobao.delivery.template.add 新增运费模板<br/>
     * @param argsMap
     * @return
     */
    public DeliveryTemplate addDeliveryTemplate(Map<String,Object> argsMap) throws Exception {
        DeliveryTemplateAddRequest req=new DeliveryTemplateAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        DeliveryTemplateAddResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getDeliveryTemplate();
    }

    /**
     * taobao.delivery.template.delete 删除运费模板<br/>
     * 根据用户指定的模板ID删除指定的模板<br/>
     * @param templateId
     * @return
     */
    public Boolean deleteDeliveryTemplate(Long templateId) throws ApiException {
        DeliveryTemplateDeleteRequest req=new DeliveryTemplateDeleteRequest();
        req.setTemplateId(templateId);
        DeliveryTemplateDeleteResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getComplete();
    }

    /**
     * taobao.delivery.template.get 获取用户指定运费模板信息<br/>
     * 获取用户指定运费模板信息<br/>
     * @param argsMap
     * @return
     */
    public Map<String,Object> getDeliveryTemplate(Map<String,Object> argsMap) throws Exception {
        DeliveryTemplateGetRequest req=new DeliveryTemplateGetRequest();
        ReflectUtils.executeMethods(req,argsMap);

        Map<String,Object> deliveryTemplateMap = new HashMap<String, Object>();
        DeliveryTemplateGetResponse response = null;
        response = client.execute(req , sessionKey);
        deliveryTemplateMap.put(ConstantTaoBao.TOTAL_RESULTS,response.getTotalResults());
        deliveryTemplateMap.put(ConstantTaoBao.DELIVERY_TEMPLATES, response.getDeliveryTemplates());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return deliveryTemplateMap;
    }

    /**
     * taobao.delivery.template.update 修改运费模板<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Boolean updateDeliveryTemplate(Map<String,Object> argsMap) throws Exception {
        DeliveryTemplateUpdateRequest req=new DeliveryTemplateUpdateRequest();
        ReflectUtils.executeMethods(req,argsMap);
        DeliveryTemplateUpdateResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getComplete();
    }

    /**
     * taobao.delivery.templates.get 获取用户下所有模板<br/>
     * 根据用户ID获取用户下所有模板<br/>
     * @param fields
     * @return
     */
    public Map<String,Object> getDeliveryTemplates(String fields) throws ApiException {
        Map<String,Object> deliveryTemplatesMap = new HashMap<String, Object>();
        DeliveryTemplatesGetRequest req=new DeliveryTemplatesGetRequest();
        req.setFields(fields);
        DeliveryTemplatesGetResponse response = null;
        response = client.execute(req , sessionKey);
        deliveryTemplatesMap.put(ConstantTaoBao.TOTAL_RESULTS,response.getTotalResults());
        deliveryTemplatesMap.put(ConstantTaoBao.DELIVERY_TEMPLATES, response.getDeliveryTemplates());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return deliveryTemplatesMap;
    }

    /**
     * taobao.logistics.address.add 卖家地址库新增接口<br/>
     * 通过此接口新增卖家地址库,卖家最多可添加5条地址库,新增第一条卖家地址，将会自动设为默认地址库<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public AddressResult addLogisticsAddress(Map<String,Object> argsMap) throws Exception {
        LogisticsAddressAddRequest req=new LogisticsAddressAddRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsAddressAddResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getAddressResult();
    }

    /**
     * taobao.logistics.address.modify 卖家地址库修改<br/>
     * @param argsMap
     * @return
     */
    public AddressResult modifyLogisticsAddress(Map<String,Object> argsMap) throws Exception {
        LogisticsAddressModifyRequest req=new LogisticsAddressModifyRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsAddressModifyResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getAddressResult();
    }

    /**
     * taobao.logistics.address.remove 删除卖家地址库
     * @param contactId
     * @return
     */
    public AddressResult removeLogisticsAddress(Long contactId) throws ApiException {
        LogisticsAddressRemoveRequest req=new LogisticsAddressRemoveRequest();
        req.setContactId(contactId);
        LogisticsAddressRemoveResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getAddressResult();
    }

    /**
     * taobao.logistics.address.search 查询卖家地址库<br/>
     * @param rdef
     * @return
     */
    public List<AddressResult> searchLogisticsAddress(String rdef) throws ApiException {
        LogisticsAddressSearchRequest req=new LogisticsAddressSearchRequest();
        req.setRdef(rdef);
        LogisticsAddressSearchResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getAddresses();
    }

    /**
     * taobao.logistics.companies.get 查询物流公司信息<br/>
     * 查询淘宝网合作的物流公司信息，用于发货接口。<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public LogisticsCompaniesGetResponse logisticsCompaniesGet(Map<String,Object> argsMap) throws Exception {
        LogisticsCompaniesGetRequest req=new LogisticsCompaniesGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsCompaniesGetResponse response = client.execute(req,sessionKey);
        return response;
    }

    /**
     * taobao.logistics.consign.order.createandsend 创建订单并发货<br/>
     * 创建物流订单，并发货。<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> createandsendLogisticsConsignOrder(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> logisticsConsignOrderMap = new HashMap<String, Object>();
        LogisticsConsignOrderCreateandsendRequest req=new LogisticsConsignOrderCreateandsendRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsConsignOrderCreateandsendResponse response = client.execute(req,sessionKey);
        logisticsConsignOrderMap.put(ConstantTaoBao.RESULT_DESC,response.getResultDesc());
        logisticsConsignOrderMap.put(ConstantTaoBao.ORDER_ID,response.getOrderId());
        logisticsConsignOrderMap.put(ConstantTaoBao.IS_SUCCESS, response.getIsSuccess());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return logisticsConsignOrderMap;
    }

    /**
     * taobao.logistics.consign.resend 修改物流公司和运单号<br/>
     * 支持卖家发货后修改物流公司和运单号。<br/>
     * 支持订单类型支持在线下单和自己联系。<br/>
     * 自己联系只能切换为自己联系的公司，在线下单也只能切换为在线下单的物流公司。<br/>
     * 调用时订单状态是卖家已发货，自己联系在发货后24小时内在线下单未揽收成功才可使用<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Shipping resendLogisticsConsign(Map<String,Object> argsMap) throws Exception {
        LogisticsConsignResendRequest req=new LogisticsConsignResendRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsConsignResendResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getShipping();
    }

    /**
     * taobao.logistics.dummy.send 无需物流（虚拟）发货处理<br/>
     * 用户调用该接口可实现无需物流（虚拟）发货,使用该接口发货，交易订单状态会直接变成卖家已发货<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Shipping sendLogisticsDummy(Map<String,Object> argsMap) throws Exception {
        LogisticsDummySendRequest req=new LogisticsDummySendRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsDummySendResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getShipping();
    }

    /**
     * taobao.logistics.offline.send 自己联系物流（线下物流）发货<br/>
     * 用户调用该接口可实现自己联系发货（线下物流），使用该接口发货，交易订单状态会直接变成卖家已发货。<br/>
     * 不支持货到付款、在线下单类型的订单。<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public LogisticsOfflineSendResponse logisticsOfflineSend(Map<String,Object> argsMap) throws Exception {
        LogisticsOfflineSendRequest req=new LogisticsOfflineSendRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsOfflineSendResponse response = client.execute(req , sessionKey);
        return response;
    }

    /**
     * taobao.logistics.online.cancel 取消物流订单接口<br/>
     * 调此接口取消发货的订单，重新选择物流公司发货。前提是物流公司未揽收货物。<br/>
     * 对未发货和已经被物流公司揽收的物流订单，是不能取消的。<br/>
     * @param tid
     * @return
     */
    public Map<String,Object> cancelLogisticsOnline(Long tid) throws ApiException {
        Map<String,Object> logisticsOnlineMap = new HashMap<String, Object>();
        LogisticsOnlineCancelRequest req=new LogisticsOnlineCancelRequest();
        req.setTid(tid);
        LogisticsOnlineCancelResponse response = client.execute(req , sessionKey);
        logisticsOnlineMap.put(ConstantTaoBao.IS_SUCCESS,response.getIsSuccess());
        logisticsOnlineMap.put(ConstantTaoBao.MODIFY_TIME,response.getModifyTime());
        logisticsOnlineMap.put(ConstantTaoBao.RECREATED_ORDER_ID, response.getRecreatedOrderId());

        TaoBaoLogUtil.logTaoBaoApi(response);

        return logisticsOnlineMap;
    }

    /**
     * taobao.logistics.online.confirm 确认发货通知接口<br/>
     * 确认发货的目的是让交易流程继承走下去，确认发货后交易状态会由【买家已付款】变为【卖家已发货】，<br/>
     *   然后买家才可以确认收货，货款打入卖家账号。货到付款的订单除外<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public LogisticsOnlineConfirmResponse logisticsOnlineConfirm(Map<String,Object> argsMap) throws Exception {
        LogisticsOnlineConfirmRequest req=new LogisticsOnlineConfirmRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsOnlineConfirmResponse response = client.execute(req , sessionKey);
        return response;
    }

    /**
     * taobao.logistics.online.send 在线订单发货处理（支持货到付款）<br/>
     * 用户调用该接口可实现在线订单发货（支持货到付款） 调用该接口实现在线下单发货，有两种情况：<br/>
     * 如果不输入运单号的情况：交易状态不会改变，需要调用taobao.logistics.online.confirm确认发货后交易状态才会变成卖家已发货。<br/>
     * 如果输入运单号的情况发货：交易订单状态会直接变成卖家已发货 。<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public LogisticsOnlineSendResponse logisticsOnlineSend(Map<String,Object> argsMap) throws Exception {
        LogisticsOnlineSendRequest req=new LogisticsOnlineSendRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsOnlineSendResponse response = client.execute(req , sessionKey);
        return response;
    }

    /**
     * taobao.logistics.orders.detail.get 批量查询物流订单,返回详细信息<br/>
     * 查询物流订单的详细信息，涉及用户隐私字段。<br/>
     * （注：该API主要是提供给卖家查询物流订单使用，买家查询物流订单，建议使用taobao.logistics.trace.search）<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> getLogisticsOrdersDetail(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> logisticsOrdersDetailMap = new HashMap<String, Object>();
        LogisticsOrdersDetailGetRequest req=new LogisticsOrdersDetailGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsOrdersDetailGetResponse response = client.execute(req , sessionKey);
        logisticsOrdersDetailMap.put(ConstantTaoBao.SHIPPINGS,response.getShippings());
        logisticsOrdersDetailMap.put(ConstantTaoBao.TOTAL_RESULTS,response.getTotalResults());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return logisticsOrdersDetailMap;
    }

    /**
     * taobao.logistics.orders.get 批量查询物流订单<br/>
     * 批量查询物流订单。<br/>
     * （注：该API主要是提供给卖家查询物流订单使用，买家查询物流订单，建议使用taobao.logistics.trace.search）<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> getLogisticsOrders(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> logisticsOrdersMap = new HashMap<String, Object>();
        LogisticsOrdersGetRequest req=new LogisticsOrdersGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsOrdersGetResponse response = client.execute(req , sessionKey);
        logisticsOrdersMap.put(ConstantTaoBao.SHIPPINGS,response.getShippings());
        logisticsOrdersMap.put(ConstantTaoBao.TOTAL_RESULTS,response.getTotalResults());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return logisticsOrdersMap;
    }

    /**
     * taobao.logistics.orderstore.push 物流订单仓内信息推送接口<br/>
     * 卖家使用自己的物流公司发货，可以通过本接口将订单的仓内信息推送到淘宝，淘宝保存这些仓内信息，并可在页面展示这些仓内信息。<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Shipping pushLogisticsOrderstore(Map<String,Object> argsMap) throws Exception {
        LogisticsOrderstorePushRequest req=new LogisticsOrderstorePushRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsOrderstorePushResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getShipping();
    }

    /**
     * taobao.logistics.ordertrace.push 物流订单流转信息推送接口<br/>
     * 卖家使用自己的物流公司发货，可以通过本接口将订单的流转信息推送到淘宝，淘宝保存这些流转信息，并可在页面展示这些流转信息。<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Shipping pushLogisticsOrdertrace(Map<String,Object> argsMap) throws Exception {
        LogisticsOrdertracePushRequest req=new LogisticsOrdertracePushRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsOrdertracePushResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getShipping();
    }

    /**
     * taobao.logistics.partners.get 查询支持起始地到目的地范围的物流公司<br/>
     * 查询物流公司信息（可以查询目的地可不可达情况）<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public List<LogisticsPartner> getLogisticsPartners(Map<String,Object> argsMap) throws Exception {
        LogisticsPartnersGetRequest req=new LogisticsPartnersGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsPartnersGetResponse response = client.execute(req,sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getLogisticsPartners();
    }

    /**
     * taobao.logistics.trace.search 物流流转信息查询<br/>
     * 用户根据淘宝交易号查询物流流转信息，如2010-8-10 15：23：00到达杭州集散地。<br/>
     *  此接口的返回信息都由物流公司提供。<br/>
     *  （备注：使用线下发货（offline.send）的运单，不支持运单状态的实时跟踪，只要一发货，状态就会变为对方已签收，
     *  该字段仅对线上发货（online.send）的运单有效。）<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public LogisticsTraceSearchResponse logisticsTraceSearch(Map<String,Object> argsMap) throws Exception {
        LogisticsTraceSearchRequest req=new LogisticsTraceSearchRequest();
        ReflectUtils.executeMethods(req,argsMap);
        LogisticsTraceSearchResponse response = client.execute(req,sessionKey);
        return response;
    }

}
