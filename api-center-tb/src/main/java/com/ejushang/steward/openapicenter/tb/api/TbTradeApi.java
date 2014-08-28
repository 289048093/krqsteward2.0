package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.util.TaoBaoLogUtil;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.taobao.api.domain.TradeAmount;
import com.taobao.api.domain.TradeConfirmFee;
import com.taobao.api.request.*;
import com.taobao.api.response.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 淘宝交易API<br/>
 * 提供了订单下载，修改收货地址、修改交易备注等功能
 * User: Baron.Zhang
 * Date: 13-12-16
 * Time: 上午10:49
 */
public class TbTradeApi {
    private TaobaoClient client;
    private String sessionKey;
    public TbTradeApi(String sessionKey){
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    /**
     * taobao.trade.amount.get 交易订单帐务查询 <br/>
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     */
    public TradeAmount getTradeAmount(Map<String,Object> argsMap) throws Exception {
        TradeAmountGetRequest req = new TradeAmountGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TradeAmountGetResponse response = null;
        response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return response.getTradeAmount();
    }

    /**
     * taobao.trade.close 卖家关闭一笔交易<br/>
     * 关闭一笔订单，可以是主订单或子订单。当订单从创建到关闭时间小于10s的时候，会报“CLOSE_TRADE_TOO_FAST”错误。<br/>
     * @param argsMap
     * @return
     */
    public Trade closeTrade(Map<String,Object> argsMap) throws Exception {
        TradeCloseRequest req=new TradeCloseRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TradeCloseResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return response.getTrade();
    }

    /**
     * taobao.trade.confirmfee.get 获取交易确认收货费用<br/>
     * 获取交易确认收货费用 可以获取主订单或子订单的确认收货费用<br/>
     * @param argsMap
     * @return
     */
    public TradeConfirmFee getTradeConfirmFee(Map<String,Object> argsMap) throws Exception {
        TradeConfirmfeeGetRequest req=new TradeConfirmfeeGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        TradeConfirmfeeGetResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return response.getTradeConfirmFee();
    }

    /**
     * taobao.trade.fullinfo.get 获取单笔交易的详细信息<br/>
     * 获取单笔交易的详细信息<br/>
     * <ul>
     *     <li>1. 只有在交易成功的状态下才能取到交易佣金，其它状态下取到的都是零或空值 </li>
     *     <li>2. 只有单笔订单的情况下Trade数据结构中才包含商品相关的信息</li>
     *     <li>3. 获取到的Order中的payment字段在单笔子订单时包含物流费用，多笔子订单时不包含物流费用</li>
     *     <li>4. 请按需获取字段，减少TOP系统的压力</li>
     * </ul>
     * @param argsMap
     * @return
     */
    public TradeFullinfoGetResponse tradeFullinfoGet(Map<String,Object> argsMap) throws Exception {
        TradeFullinfoGetRequest req=new TradeFullinfoGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        TradeFullinfoGetResponse response = client.execute(req , sessionKey);
		return response;
    }

    /**
     * taobao.trade.get 获取单笔交易的部分信息(性能高) <br/>
     * 获取单笔交易的部分信息<br/>
     * @param argsMap
     * @return
     */
    public TradeGetResponse tradeGet(Map<String,Object> argsMap) throws Exception {
        TradeGetRequest req=new TradeGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TradeGetResponse response = client.execute(req , sessionKey);
		return response;
    }

    /**
     * taobao.trade.memo.add 对一笔交易添加备注<br/>
     * 根据登录用户的身份（买家或卖家），自动添加相应的交易备注,不能重复调用些接口添加备注，需要更新备注请用taobao.trade.memo.update<br/>
     * @param argsMap
     * @return
     */
    public Trade addTradeMemo(Map<String,Object> argsMap) throws Exception {
        TradeMemoAddRequest req=new TradeMemoAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TradeMemoAddResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return response.getTrade();
    }

    /**
     * taobao.trade.memo.add 对一笔交易添加备注<br/>
     * 根据登录用户的身份（买家或卖家），自动添加相应的交易备注,不能重复调用些接口添加备注，需要更新备注请用taobao.trade.memo.update<br/>
     * @param argsMap
     * @return
     */
    public Trade updateTradeMemo(Map<String,Object> argsMap) throws Exception {
        TradeMemoUpdateRequest req=new TradeMemoUpdateRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TradeMemoUpdateResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return response.getTrade();
    }

    /**
     * taobao.trade.ordersku.update 更新交易订单的销售属性<br/>
     * <ul>
     *     <li>只能更新发货前子订单的销售属性 只能更新价格相同的销售属性。</li>
     *     <li>对于拍下减库存的交易会同步更新销售属性的库存量。</li>
     *     <li>对于旺店的交易，要使用商品扩展信息中的SKU价格来比较。</li>
     *     <li> 必须使用sku_id或sku_props中的一个参数来更新，如果两个都传的话，sku_id优先</li>
     * </ul>
     * @param argsMap
     * @return
     */
    public Order updateTradeOrdersku(Map<String,Object> argsMap) throws Exception {
        TradeOrderskuUpdateRequest req=new TradeOrderskuUpdateRequest();
        ReflectUtils.executeMethods(req,argsMap);
        TradeOrderskuUpdateResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return  response.getOrder();
    }

    /**
     * taobao.trade.postage.update 修改订单邮费价格<br/>
     * 修改订单邮费接口，通过传入订单编号和邮费价格，修改订单的邮费，返回修改时间modified,邮费post_fee,总费用total_fee。<br/>
     * 只有为等待买家付款的状态下，才可以修改邮费<br/>
     * @param argsMap
     * @return
     */
    public Trade updateTradePostage(Map<String,Object> argsMap) throws Exception {
        TradePostageUpdateRequest req=new TradePostageUpdateRequest();
        ReflectUtils.executeMethods(req,argsMap);
        TradePostageUpdateResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return response.getTrade();
    }

    /**
     * taobao.trade.receivetime.delay 延长交易收货时间<br/>
     * @param argsMap
     * @return
     */
    public Trade delayTradeReceivetime(Map<String,Object> argsMap) throws Exception {
        TradeReceivetimeDelayRequest req=new TradeReceivetimeDelayRequest();
        ReflectUtils.executeMethods(req,argsMap);
        TradeReceivetimeDelayResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return response.getTrade();
    }

    /**
     * taobao.trade.shippingaddress.update 更改交易的收货地址<br/>
     * 只能更新一笔交易里面的买家收货地址 只能更新发货前（即买家已付款，等待卖家发货状态）的交易的买家收货地址<br/>
     * 更新后的发货地址可以通过taobao.trade.fullinfo.get查到 参数中所说的字节为GBK编码的（英文和数字占1字节，中文占2字节）
     * @param argsMap
     * @return
     */
    public Trade updateTradeShippingaddress(Map<String,Object> argsMap) throws Exception {
        TradeShippingaddressUpdateRequest req=new TradeShippingaddressUpdateRequest();
        ReflectUtils.executeMethods(req,argsMap);
        TradeShippingaddressUpdateResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return response.getTrade();
    }

    /**
     * taobao.trade.snapshot.get 交易快照查询<br/>
     * 目前只支持类型为“旺店标准版(600)”或“旺店入门版(610)”的交易<br/>
     * 对于“旺店标准版”类型的交易，返回的snapshot字段为交易快照编号<br/>
     * 对于“旺店入门版”类型的交易，返回的snapshot字段为JSON结构的数据(其中的shopPromotion包含了优惠，积分等信息)<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Trade getTradeSnapshot(Map<String,Object> argsMap) throws Exception {
        TradeSnapshotGetRequest req=new TradeSnapshotGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        TradeSnapshotGetResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
		return response.getTrade();
    }

    /**
     * taobao.trades.sold.get 查询卖家已卖出的交易数据（根据创建时间）<br/>
     * 搜索当前会话用户作为卖家已卖出的交易数据（只能获取到三个月以内的交易信息）<br/>
     * 1. 返回的数据结果是以订单的创建时间倒序排列的。<br/>
     * 2. 返回的数据结果只包含了订单的部分数据，可通过taobao.trade.fullinfo.get获取订单详情。<br/>
     * @param argsMap
     * @TaoBaoLogUtil.logTaoBaoApi(response);
		return 返回一个map类型，包含搜索到的交易信息总数、是否存在下一页、搜索到的交易信息列表。
     * @throws Exception
     */
    public TradesSoldGetResponse tradesSoldGet(Map<String,Object> argsMap) throws Exception {
        TradesSoldGetRequest  req = new TradesSoldGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TradesSoldGetResponse response = client.execute(req, sessionKey);
		return response;
    }

    /**
     * taobao.trades.sold.increment.get 查询卖家已卖出的增量交易数据（根据修改时间）<br/>
     * 搜索当前会话用户作为卖家已卖出的增量交易数据（只能获取到三个月以内的交易信息）<br/>
     * 1. 一次请求只能查询时间跨度为一天的增量交易记录，即end_modified - start_modified <= 1天。<br/>
     * 2. 返回的数据结果是以订单的修改时间倒序排列的，通过从后往前翻页的方式可以避免漏单问题。<br/>
     * 3. 返回的数据结果只包含了订单的部分数据，可通过taobao.trade.fullinfo.get获取订单详情。<br/>
     * 4. 使用主动通知监听订单变更事件，可以实时获取订单更新数据。<br/>
     * @param argsMap
     * @return
     */
    public TradesSoldIncrementGetResponse tradesSoldIncrementGet(Map<String,Object> argsMap) throws Exception {
        TradesSoldIncrementGetRequest req=new TradesSoldIncrementGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        TradesSoldIncrementGetResponse response = client.execute(req , sessionKey);
		return response;
    }

    /**
     * taobao.trades.sold.incrementv.get 查询卖家已卖出的增量交易数据（根据入库时间）<br/>
     * 搜索当前会话用户作为卖家已卖出的增量交易数据（只能获取到三个月以内的交易信息）<br/>
     * 1. 一次请求只能查询时间跨度为一天的增量交易记录，即end_create - start_create <= 1天。<br/>
     * 2. 返回的数据结果是以订单入库时间的倒序排列的(该时间和订单修改时间不同)，通过从后往前翻页的方式可以避免漏单问题。<br/>
     * 3. 返回的数据结果只包含了订单的部分数据，可通过taobao.trade.fullinfo.get获取订单详情。<br/>
     * 4. 使用主动通知监听订单变更事件，可以实时获取订单更新数据。<br/>
     * @param argsMap
     * @return
     */
    public Map<String,Object> getTradesSoldIncrementv(Map<String,Object> argsMap) throws Exception {
        TradesSoldIncrementvGetRequest req=new TradesSoldIncrementvGetRequest();
        ReflectUtils.executeMethods(req,argsMap);

        // 创建保存卖家交易数据的map
        Map<String,Object> tradesSoldIncrementvMap = new HashMap<String, Object>();
        // 搜索到的交易信息列表，返回的Trade和Order中包含的具体信息为入参fields请求的字段信息
        List<Trade> tradesTotal = new ArrayList<Trade>();
        // 搜索到的交易信息总数
        Long totalResults = 0L;
        // 是否存在下一页
        Boolean hasNext = false;
        // 当前第几页
        Long pageNo = req.getPageNo() == null ? 1L : req.getPageNo();
        // 当前页记录数
        Long pageSize = req.getPageSize() == null ? 40L : req.getPageSize();

        TradesSoldIncrementvGetResponse response = null;
        response = client.execute(req , sessionKey);
        totalResults = response.getTotalResults();
        hasNext = response.getHasNext();
        pageNo = totalResults % pageSize == 0 ?totalResults / pageSize : totalResults / pageSize + 1;
        while(pageNo > 0){
            req.setPageNo(pageNo--);
            response = client.execute(req, sessionKey);
            tradesTotal.addAll(response.getTrades());
        }

        tradesSoldIncrementvMap.put(ConstantTaoBao.TOTAL_RESULTS,totalResults);
        tradesSoldIncrementvMap.put(ConstantTaoBao.TRADES,tradesTotal);
        tradesSoldIncrementvMap.put(ConstantTaoBao.HAS_NEXT,hasNext);

        TaoBaoLogUtil.logTaoBaoApi(response);
		return tradesSoldIncrementvMap;
    }


}
