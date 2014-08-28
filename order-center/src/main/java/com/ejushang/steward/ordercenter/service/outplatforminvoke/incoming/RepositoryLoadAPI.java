package com.ejushang.steward.ordercenter.service.outplatforminvoke.incoming;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateType;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.RepositoryGetRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.AbstractOperatePageGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.AbstractPageGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.RepositoryGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-26
 * Time: 下午4:23
 */
public class RepositoryLoadAPI extends AbstractLoadAPI {

    private final static Logger log = LoggerFactory.getLogger(RepositoryLoadAPI.class);


    @Override
    protected AbstractOperatePageGetResponse remoteGet(Date startTime, Date endTime, OperateType type, Integer pageNo, Integer pageSize) throws ApiException {
        RepositoryGetRequest req = new RepositoryGetRequest();
        req.setStartTime(startTime);
        req.setEndTime(endTime);
        req.setType(type);
        req.setPageNo(pageNo);
        req.setPageSize(pageSize);
        RepositoryGetResponse res = client.excute(req);
        if (StringUtils.isNotBlank(res.getErrorCode())) {
            log.error("商品中心获取仓库数据错误，错误码[{}],错误信息[{}]", res.getErrorCode(), res.getMsg());
            throw new ApiException(res.getMsg());
        }
        return res;
    }
}
