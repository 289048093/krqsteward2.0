package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.datastructure.ZiYouHashMap;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiRuleException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.RefundGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.RequestCheckUtils;

import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-11
 * Time: 下午4:51
 */
public class RefundGetRequest implements ZiYouRequest<RefundGetResponse> {
    private Map<String,String> headerMap = new ZiYouHashMap();
    private Map<String,String> udfParams; // add user-defined text params;
    private Long timestamp;

    private String refundId;

    private String refundPhase;

    private String apiMethodName = "ziyou.order.refund.get";

    public String getRefundPhase() {
        return refundPhase;
    }

    public void setRefundPhase(String refundPhase) {
        this.refundPhase = refundPhase;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    @Override
    public String getApiMethodName() {
        return this.apiMethodName;
    }

    @Override
    public Map<String, String> getTextParams() {
        ZiYouHashMap textParams = new ZiYouHashMap();
        textParams.put("refundId",this.refundId);
        textParams.put("refundPhase",this.refundPhase);
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
        this.timestamp=timestamp;
    }

    @Override
    public Class<RefundGetResponse> getResponseClass() {
       return RefundGetResponse.class;
    }

    @Override
    public void check() throws ApiRuleException {
        RequestCheckUtils.checkNotEmpty(this.refundId,"refundId");
        RequestCheckUtils.checkNotEmpty(this.refundPhase,"refundPhase");
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
