package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.datastructure.ZiYouHashMap;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateType;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiRuleException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.RequestCheckUtils;

import java.util.Date;
import java.util.Map;

/**
 * 分页请求抽象类
 * User:  Sed.Lee(李朝)
 * Date: 14-8-25
 * Time: 上午11:41
 */
public abstract class AbstractPageGetRequest<T extends ZiYouResponse> extends AbstractRequest<T> {

    private Date startTime;

    private Date endTime;

    private Integer pageNo;

    private Integer pageSize;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Map<String, String> getTextParams() {
        ZiYouHashMap textParams = new ZiYouHashMap();
        textParams.put("start_time",startTime);
        textParams.put("end_time",endTime);
        textParams.put("page_no",pageNo);
        textParams.put("page_size",pageSize);
        if(this.udfParams != null){
            textParams.putAll(udfParams);
        }
        return  textParams;
    }

    @Override
    public final void check() throws ApiRuleException {
        RequestCheckUtils.checkNotEmpty(startTime, "startTime");
        RequestCheckUtils.checkNotEmpty(endTime, "endTime");
        checkParams();
    }

    /**
     * 检查用户参数合法性，除分页参数 ,此方法供check()方法调用
     */
    protected abstract void checkParams();

}
