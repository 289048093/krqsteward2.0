package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.datastructure.ZiYouHashMap;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiRuleException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.LogisticsSendResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.RequestCheckUtils;

import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-11
 * Time: 下午5:33
 */
public class LogisticsSendRequest implements ZiYouRequest<LogisticsSendResponse> {

    private Map<String,String> headerMap = new ZiYouHashMap();
    private Map<String,String> udfParams; // add user-defined text params;
    private Long timestamp;
    /**
     * 订单编号
     */
    private String tid;
    /**
     * 运单号
     */
    private String sid;
    /**
     *物流公司编码。可选值：shunfeng（顺丰），zhongtong（中通），yunda（韵达），zhaijisong（宅急送），
     * ems（ems），yuantong（圆通），shentong（申通），quanritongkuaidi（全日通），kuaijiesudi（快捷），
     * huitongkuaidi（汇通），guotongkuaidi（国通），lianbangkuaidi（联邦），quanfengkuaidi（全峰），
     * suer（速尔），tiantian（天天），youshuwuliu（优速），youzhengguonei（邮政国内小包），zengyi（增益速递），
     * debang（德邦物流），changjiazisong（厂家自送），xinbang（新邦物流），debangwuliu（德邦物流），unknown（未知）
     */
    private String companyCode;

    private String apiMethodName = "ziyou.logistics.send";

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

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
        textParams.put("sid",this.sid);
        textParams.put("companyCode",this.companyCode);
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
    public Class<LogisticsSendResponse> getResponseClass() {
        return LogisticsSendResponse.class;
    }

    @Override
    public void check() throws ApiRuleException {
        RequestCheckUtils.checkNotEmpty(this.tid,"tid");
        RequestCheckUtils.checkNotEmpty(this.sid,"sid");
        RequestCheckUtils.checkNotEmpty(this.companyCode,"companyCode");
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
