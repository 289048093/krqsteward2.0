package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.datastructure.ZiYouHashMap;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiRuleException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.RefundsQueryResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.RequestCheckUtils;

import java.util.Date;
import java.util.Map;

/**
 * User: Shiro
 * Date: 14-8-11
 * Time: 下午3:43
 */
public class RefundsQueryRequest implements ZiYouRequest<RefundsQueryResponse> {
    private Map<String,String> headerMap = new ZiYouHashMap();
    private Map<String,String> udfParams; // add user-defined text params;
    private Long timestamp;

    /** 查询退款退货单时间类型。可选值：created（根据创建时间），modified（根据更新时间） */
    private String dateType;

    /** 查询退款退货单创建时间开始。格式:yyyy-MM-dd HH:mm:ss */
    private Date startCreated;

    /** 查询退款退货单创建时间结束。格式:yyyy-MM-dd HH:mm:ss */
    private Date endCreated;

    /** 退款退货状态，默认查询所有退款退货状态的数据，除了默认值外每次只能查询一种状态。
     * 可选值：WAIT_SELLER_AGREE（买家申请，等待卖家同意），SELLER_REFUSE（卖家拒绝），
     * GOODS_RETURNING（退货中），CLOSED（退款失败），SUCCESS（退款成功）。 */
    private String status;

    /**页码。取值范围:大于零的整数; 默认值:1  */
    private Long pageNo;

    /**每页条数。取值范围:大于零的整数。  */
    private Long pageSize;



    private String apiMethodName = "ziyou.order.refunds.query";

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

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String getApiMethodName() {
        return this.apiMethodName;
    }

    @Override
    public Map<String, String> getTextParams() {
        ZiYouHashMap textParams = new ZiYouHashMap();
        textParams.put("dateType",this.dateType);
        textParams.put("startCreated",this.startCreated);
        textParams.put("endCreated",this.endCreated);
        textParams.put("status",this.status);
        textParams.put("pageNo",this.pageNo);
        textParams.put("pageSize",this.pageSize);
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
    public Class<RefundsQueryResponse> getResponseClass() {
      return RefundsQueryResponse.class;
    }

    @Override
    public void check() throws ApiRuleException {
        RequestCheckUtils.checkMinValue(this.pageSize,1,"pageNo");
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
