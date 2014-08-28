package com.ejushang.steward.ordercenter.service.outplatforminvoke.incoming;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateType;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.MealSetGetRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.AbstractOperatePageGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.AbstractPageGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.MealSetGetResponseAbstract;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-26
 * Time: 下午4:33
 */
public class MealsetLoadAPI extends AbstractLoadAPI {

    private final static Logger log = LoggerFactory.getLogger(MealsetLoadAPI.class);



    @Override
    protected AbstractOperatePageGetResponse remoteGet(Date startTime, Date endTime, OperateType type, Integer pageNo, Integer pageSize) throws ApiException {
        MealSetGetRequest req = new MealSetGetRequest();
        req.setStartTime(startTime);
        req.setEndTime(endTime);
        req.setType(type);
        req.setPageNo(pageNo);
        req.setPageSize(pageSize);
        MealSetGetResponseAbstract res = client.excute(req);
        if (StringUtils.isNotBlank(res.getErrorCode())) {
            log.error("商品中心获取套餐失败，错误码[{}],错误信息[{}]", res.getErrorCode(), res.getMsg());
            throw new ApiException(res.getMsg());
        }
        return res;
    }
}
