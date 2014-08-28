package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.datastructure.ZiYouHashMap;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiRuleException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.TradeGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.RequestCheckUtils;

import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/8/7
 * Time: 11:17
 */
public class TradeGetRequest implements ZiYouRequest<TradeGetResponse> {

    private Map<String,String> headerMap = new ZiYouHashMap();
    private Map<String,String> udfParams; // add user-defined text params;
    private Long timestamp;

    /** 交易id */
    private String tid;

    private String apiMethodName = "ziyou.trade.get";

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    @Override
    public String getApiMethodName() {
        return this.apiMethodName;
    }

    @Override
    public Map<String, String> getTextParams() {
        ZiYouHashMap textParams = new ZiYouHashMap();
        textParams.put("tid",this.tid);
        if(this.udfParams != null){
            textParams.putAll(udfParams);
        }
        return textParams;
    }

    @Override
    public Long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Class<TradeGetResponse> getResponseClass() {
        return TradeGetResponse.class;
    }

    @Override
    public void check() throws ApiRuleException {
        RequestCheckUtils.checkNotEmpty(this.tid,"tid");
    }

    @Override
    public Map<String, String> getHeaderMap() {
        return this.headerMap;
    }

    @Override
    public void putOtherTextParam(String key, String value) {
        if(this.udfParams == null){
            this.udfParams = new ZiYouHashMap();
        }
        this.udfParams.put(key,value);
    }
}
