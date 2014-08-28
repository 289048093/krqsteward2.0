package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.datastructure.ZiYouHashMap;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiRuleException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.TradesQueryResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.RequestCheckUtils;

import java.util.Date;
import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-11
 * Time: 下午3:04
 */
public class TradesQueryRequest implements ZiYouRequest<TradesQueryResponse> {
    private Map<String, String> headerMap = new ZiYouHashMap();
    private Map<String, String> udfParams; // add user-defined text params;
    private Long timestamp;

    /**
     * 查询交易时间类型。可选值：created（根据创建时间），modified（根据更新时间）
     */
    private String dateType;

    /**
     * 查询交易创建时间开始。格式:yyyy-MM-dd HH:mm:ss
     */
    private Date startCreated;

    /**
     * 查询交易创建时间结束。格式:yyyy-MM-dd HH:mm:ss
     */
    private Date endCreated;

    /**
     * 交易状态，默认查询所有交易状态的数据，除了默认值外每次只能查询一种状态。
     * 可选值：WAIT_BUYER_PAY（等待买家付款），WAIT_SELLER_SEND_GOODS（等待卖家发货,即:买家已付款），
     * WAIT_BUYER_CONFIRM_GOODS（等待买家确认收货,即:卖家已发货）， TRADE_FINISHED（交易成功）
     */
    private String status;

    /**
     * 买家昵称
     */
    private String buyerNick;

    /**
     * 页码。取值范围:大于零的整数; 默认值:1
     */
    private Long pageNo;

    /**
     * 每页条数。取值范围:大于零的整数; 默认值:40;最大值:100
     */
    private Long pageSize;


    private String apiMethodName = "ziyou.trades.query";


    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public Date getStartCreated() {
        return startCreated;
    }

    public void setStartCreated(Date startCreated) {
        this.startCreated = startCreated;
    }

    public Date getEndCreated() {
        return endCreated;
    }

    public void setEndCreated(Date endCreated) {
        this.endCreated = endCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public String getApiMethodName() {
        return this.apiMethodName;
    }

    @Override
    public Map<String, String> getTextParams() {
        ZiYouHashMap textParams = new ZiYouHashMap();
        textParams.put("dateType", this.dateType);
        textParams.put("startCreated", this.startCreated);
        textParams.put("endCreated", this.endCreated);
        textParams.put("status", this.status);
        textParams.put("buyerNick", this.buyerNick);
        textParams.put("pageNo", this.pageNo);
        textParams.put("pageSize", this.pageSize);
        if (this.udfParams != null) {
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
    public Class<TradesQueryResponse> getResponseClass() {
        return TradesQueryResponse.class;
    }

    @Override
    public void check() throws ApiRuleException {
        RequestCheckUtils.checkMinValue(this.pageNo, 1, "pageNo");
        RequestCheckUtils.checkMaxValue(this.pageSize,100,"pageSize");
    }

    @Override
    public Map<String, String> getHeaderMap() {
        return this.headerMap;
    }

    @Override
    public void putOtherTextParam(String key, String value) {
        if (this.udfParams == null) {
            this.udfParams = new ZiYouHashMap();
        }
        this.udfParams.put(key, value);
    }
}
