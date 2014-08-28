package com.ejushang.steward.openapicenter.jd.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.jd.constant.ConstantJingDong;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.ware.*;
import com.jd.open.api.sdk.response.ware.*;

import java.util.Map;

/**
 * 京东商品api
 * User: Baron.Zhang
 * Date: 2014/5/26
 * Time: 18:28
 */
public class JdSkuApi {
    private JdClient client;

    public JdSkuApi(String sessionKey) {
        client = new DefaultJdClient(ConstantJingDong.JD_API_URL, sessionKey, ConstantJingDong.JD_APP_KEY,
                ConstantJingDong.JD_APP_SECRET);
    }

    /**
     * 360buy.sku.custom.get<br/>
     * 根据商家设定的sku的外部id获取所对应的sku数据，一个sku的外部id对应一个sku数据。<br/>
     *
     * @param argsMap
     * @return
     * @throws Exception
     */
    public SkuCustomGetResponse skuCustomGet(Map<String, Object> argsMap) throws Exception {
        SkuCustomGetRequest request = new SkuCustomGetRequest();
        ReflectUtils.executeMethods(request, argsMap);
        SkuCustomGetResponse response = client.execute(request);
        return response;
    }

    /**
     * 360buy.ware.update.delisting <br/>
     * 产品下架
     *
     * @param wareId  必填
     * @param tradeNo
     * @return
     * @throws JdException
     */
    public WareUpdateDelistingResponse wareUpdateDelisting(String wareId, String tradeNo) throws JdException {
        WareUpdateDelistingRequest req = new WareUpdateDelistingRequest();
        req.setWareId(wareId);
        req.setTradeNo(tradeNo);
        return client.execute(req);
    }

    /**
     * 360buy.ware.update.listing <br/>
     * 产品上架
     *
     * @param wareId  必填
     * @param tradeNo
     * @return
     * @throws JdException
     */
    public WareUpdateListingResponse wareUpdateListing(String wareId, String tradeNo) throws JdException {
        WareUpdateListingRequest req = new WareUpdateListingRequest();
        req.setWareId(wareId);
        req.setTradeNo(tradeNo);
        return client.execute(req);
    }

    /**
     * 360buy.sku.stock.update <br/>
     * 通过产品sku修改京东库存
     *
     * @param sku      智库存条形码
     * @param quantity
     * @return
     */
    public WareSkuStockUpdateResponse wareSkuStockUpdateByOuterId(String sku, Long quantity) throws JdException {
        WareSkuStockUpdateRequest req = new WareSkuStockUpdateRequest();
        req.setOuterId(sku);
        req.setQuantity(quantity.toString());
        return client.execute(req);
    }

    /**
     * 360buy.sku.stock.update <br/>
     * 通过产品sku修改京东库存
     *
     * @param skuId      jd skuId
     * @param quantity
     * @return
     */
    public WareSkuStockUpdateResponse wareSkuStockUpdateBySkuId(String skuId, Long quantity) throws JdException {
        WareSkuStockUpdateRequest req = new WareSkuStockUpdateRequest();
        req.setSkuId(skuId);
        req.setQuantity(quantity.toString());
        return client.execute(req);
    }

    /**
     * 360buy.ware.get<br/>
     * 通过京东wareId查询商品信息
     *
     * @param wareId
     * @param fields
     * @return
     * @throws JdException
     */
    public WareGetResponse wareGet(String wareId, String fields) throws JdException {
        WareGetRequest req = new WareGetRequest();
        req.setWareId(wareId);
        req.setFields(fields);
        return client.execute(req);
    }
}
