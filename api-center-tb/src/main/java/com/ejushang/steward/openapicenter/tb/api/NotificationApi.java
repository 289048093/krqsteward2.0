package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.util.TaoBaoLogUtil;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.*;
import com.taobao.api.request.*;
import com.taobao.api.response.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主动通知业务API<br/>
 * 配合主动通知业务，提供商品，交易，退款和评价等数据或状态变更的查询功能，
 * 使用：http://open.taobao.com/doc/category_list.htm?spm=0.0.0.0.maU6vt&id=87<br/>
 * User: Baron.Zhang
 * Date: 13-12-20
 * Time: 上午10:01
 */
public class NotificationApi {
    private TaobaoClient client;
    private String sessionKey;
    public NotificationApi(String sessionKey){
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    /**
     * taobao.comet.discardinfo.get 获取哪些用户丢弃了消息<br/>
     * 获取一个appkey的哪些用户丢失了消息<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public List<DiscardInfo> getCometDiscardinfo(Map<String,Object> argsMap) throws Exception {
        CometDiscardinfoGetRequest req=new CometDiscardinfoGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        CometDiscardinfoGetResponse response = client.execute(req,sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getDiscardInfoList();
    }

    /**
     * taobao.increment.authorize.message.get 通用的获取用户授权的消息数据api<br/>
     * 通用的用于获取用户授权的消息数据<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> getIncrementAuthorizeMessage(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> incrementAuthorizeMessageMap = new HashMap<String, Object>();
        IncrementAuthorizeMessageGetRequest req=new IncrementAuthorizeMessageGetRequest();
        ReflectUtils.executeMethods(req,argsMap);

        // 消息数据列表
        List<String> messagesTotal = new ArrayList<String>();
        // 当前第几页
        Long pageNo = req.getPageNo() == null ? 1L : req.getPageNo();
        // 是否有下一页
        Boolean hasNext = false;
        // 当前页记录数
        Long pageSize = req.getPageSize() == null ? 40L : req.getPageSize();
        IncrementAuthorizeMessageGetResponse response = client.execute(req,sessionKey);
        hasNext = response.getHasNext();
        messagesTotal.addAll(response.getMessages());
        while(hasNext){
            req.setPageNo(++pageNo);
            response = client.execute(req, sessionKey);
            messagesTotal.addAll(response.getMessages());
            hasNext = response.getHasNext();
        }

        incrementAuthorizeMessageMap.put(ConstantTaoBao.MESSAGES,messagesTotal);
        incrementAuthorizeMessageMap.put(ConstantTaoBao.HAS_NEXT,hasNext);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return incrementAuthorizeMessageMap;
    }

    /**
     * taobao.increment.customer.permit 开通增量消息服务<br/>
     * 提供app为自己的用户开通增量消息服务功能<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public AppCustomer permitIncrementCustomer(Map<String,Object> argsMap) throws Exception {
        IncrementCustomerPermitRequest req=new IncrementCustomerPermitRequest();
        ReflectUtils.executeMethods(req, argsMap);
        IncrementCustomerPermitResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getAppCustomer();
    }

    /**
     * taobao.increment.customer.stop 关闭用户的增量消息服务<br/>
     * 供应用关闭其用户的增量消息服务功能，这样可以节省ISV的流量。<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Boolean stopIncrementCustomer(Map<String,Object> argsMap) throws Exception {
        IncrementCustomerStopRequest req=new IncrementCustomerStopRequest();
        ReflectUtils.executeMethods(req,argsMap);
        IncrementCustomerStopResponse response = client.execute(req,sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getIsSuccess();
    }

    /**
     * taobao.increment.customers.get 查询应用为用户开通的增量消息服务<br/>
     * 提供查询应用为自身用户所开通的增量消息服务信息。这个接口有性能问题,服务端有流量限制。<br/>
     * 在丢消息后，不要每次都调用这个api获取用户列表，应用可自己保存一份主动通知用户名单。<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> getIncrementCustomers(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> incrementCustomersMap = new HashMap<String, Object>();
        IncrementCustomersGetRequest req=new IncrementCustomersGetRequest();
        ReflectUtils.executeMethods(req,argsMap);

        // 搜索到的交易信息列表，返回的Trade和Order中包含的具体信息为入参fields请求的字段信息
        List<AppCustomer> appCustomerTotal = new ArrayList<AppCustomer>();
        // 搜索到的交易信息总数
        Long totalResults = 0L;
        // 当前第几页
        Long pageNo = req.getPageNo() == null ? 1L : req.getPageNo();
        // 当前页记录数
        Long pageSize = req.getPageSize() == null ? 40L : req.getPageSize();
        IncrementCustomersGetResponse response = client.execute(req,sessionKey);
        totalResults = response.getTotalResults();
        pageNo = totalResults % pageSize == 0 ?totalResults / pageSize : totalResults / pageSize + 1;
        while(pageNo > 0){
            req.setPageNo(pageNo--);
            response = client.execute(req, sessionKey);
            appCustomerTotal.addAll(response.getAppCustomers());
        }

        incrementCustomersMap.put(ConstantTaoBao.TOTAL_RESULTS,totalResults);
        incrementCustomersMap.put(ConstantTaoBao.APP_CUSTOMERS,appCustomerTotal);

        TaoBaoLogUtil.logTaoBaoApi(response);

        return incrementCustomersMap;
    }

    /**
     * taobao.increment.items.get 获取商品变更通知信息<br/>
     * 开通主动通知业务的APP可以通过该接口获取商品变更通知信息<br/>
     * 建议获取增量消息的时间间隔是：半个小时<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> getIncrementItems(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> incrementItemsMap = new HashMap<String, Object>();
        IncrementItemsGetRequest req=new IncrementItemsGetRequest();
        ReflectUtils.executeMethods(req,argsMap);

        // 搜索到的交易信息列表，返回的Trade和Order中包含的具体信息为入参fields请求的字段信息
        List<NotifyItem> notifyItemTotal = new ArrayList<NotifyItem>();
        // 搜索到的交易信息总数
        Long totalResults = 0L;
        // 当前第几页
        Long pageNo = req.getPageNo() == null ? 1L : req.getPageNo();
        // 当前页记录数
        Long pageSize = req.getPageSize() == null ? 40L : req.getPageSize();

        IncrementItemsGetResponse response = client.execute(req,sessionKey);
        totalResults = response.getTotalResults();
        pageNo = totalResults % pageSize == 0 ?totalResults / pageSize : totalResults / pageSize + 1;
        while(pageNo > 0){
            req.setPageNo(pageNo--);
            response = client.execute(req, sessionKey);
            notifyItemTotal.addAll(response.getNotifyItems());
        }

        incrementItemsMap.put(ConstantTaoBao.TOTAL_RESULTS,totalResults);
        incrementItemsMap.put(ConstantTaoBao.NOTIFY_ITEMS,notifyItemTotal);

        TaoBaoLogUtil.logTaoBaoApi(response);
        return incrementItemsMap;
    }

    /**
     * taobao.increment.refunds.get 获取退款变更通知信息<br/>
     * 开通主动通知业务的APP可以通过该接口获取用户的退款变更通知信息<br/>
     * 建议在获取增量消息的时间间隔是：半个小时<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> getIncrementRefunds(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> incrementRefundsMap = new HashMap<String, Object>();
        IncrementRefundsGetRequest req=new IncrementRefundsGetRequest();
        ReflectUtils.executeMethods(req,argsMap);

        // 搜索到的交易信息列表，返回的Trade和Order中包含的具体信息为入参fields请求的字段信息
        List<NotifyRefund> notifyRefundTotal = new ArrayList<NotifyRefund>();
        // 搜索到的交易信息总数
        Long totalResults = 0L;
        // 当前第几页
        Long pageNo = req.getPageNo() == null ? 1L : req.getPageNo();
        // 当前页记录数
        Long pageSize = req.getPageSize() == null ? 40L : req.getPageSize();
        IncrementRefundsGetResponse response = client.execute(req,sessionKey);
        totalResults = response.getTotalResults();
        pageNo = totalResults % pageSize == 0 ?totalResults / pageSize : totalResults / pageSize + 1;
        while(pageNo > 0){
            req.setPageNo(pageNo--);
            response = client.execute(req, sessionKey);
            notifyRefundTotal.addAll(response.getNotifyRefunds());
        }

        incrementRefundsMap.put(ConstantTaoBao.TOTAL_RESULTS,totalResults);
        incrementRefundsMap.put(ConstantTaoBao.NOTIFY_REFUNDS,notifyRefundTotal);

        TaoBaoLogUtil.logTaoBaoApi(response);

        return incrementRefundsMap;
    }

    /**
     * taobao.increment.trades.get 获取交易和评价变更通知信息<br/>
     * 开通主动通知业务的APP可以通过该接口获取用户的交易和评价变更通知信息<br/>
     * 建议在获取增量消息的时间间隔是：半个小时<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> getIncrementTrades(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> incrementTradesMap = new HashMap<String, Object>();
        IncrementTradesGetRequest req=new IncrementTradesGetRequest();
        ReflectUtils.executeMethods(req,argsMap);

        // 搜索到的交易信息列表，返回的Trade和Order中包含的具体信息为入参fields请求的字段信息
        List<NotifyTrade> notifyTradeTotal = new ArrayList<NotifyTrade>();
        // 搜索到的交易信息总数
        Long totalResults = 0L;
        // 当前第几页
        Long pageNo = req.getPageNo() == null ? 1L : req.getPageNo();
        // 当前页记录数
        Long pageSize = req.getPageSize() == null ? 40L : req.getPageSize();
        IncrementTradesGetResponse response = client.execute(req,sessionKey);
        totalResults = response.getTotalResults();
        pageNo = totalResults % pageSize == 0 ?totalResults / pageSize : totalResults / pageSize + 1;
        while(pageNo > 0){
            req.setPageNo(pageNo--);
            response = client.execute(req, sessionKey);
            notifyTradeTotal.addAll(response.getNotifyTrades());
        }

        incrementTradesMap.put(ConstantTaoBao.TOTAL_RESULTS,totalResults);
        incrementTradesMap.put(ConstantTaoBao.NOTIFY_TRADES,notifyTradeTotal);

        TaoBaoLogUtil.logTaoBaoApi(response);
        return incrementTradesMap;
    }

}
