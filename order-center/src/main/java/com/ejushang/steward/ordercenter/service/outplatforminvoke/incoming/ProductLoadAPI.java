package com.ejushang.steward.ordercenter.service.outplatforminvoke.incoming;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateType;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateTypeBean;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Product;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.request.ProductGetRequest;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.AbstractOperatePageGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.AbstractPageGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ProductGetResponse;
import com.ejushang.steward.ordercenter.service.transportation.ProductLoadService;
import com.ejushang.steward.ordercenter.service.transportation.ProductService;
import com.ejushang.steward.ordercenter.util.CollectionUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-25
 * Time: 下午3:17
 */
@Component
public class ProductLoadAPI extends AbstractLoadAPI {

    @Autowired
    private ProductLoadService productLoadService;

    static {
        log = LoggerFactory.getLogger(ProductLoadAPI.class);
    }

    @PostConstruct
    public void init() {
        operateService = new OperateService() {
            @Override
            public void add(OperateTypeBean bean) {
                productLoadService.save((Product) bean);
            }

            @Override
            public void update(OperateTypeBean bean) {
                productLoadService.update((Product) bean);
            }

            @Override
            public void delete(OperateTypeBean bean) {
                Product product = (Product) bean;
                productLoadService.delete(product.getSku());
            }
        };
    }

    /**
     * 从商品中心获取Product     //TODO 网络超时重试
     *
     * @param startTime 必填
     * @param endTime   必填
     * @param type
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ApiException
     */
    @Override
    protected AbstractOperatePageGetResponse remoteGet(Date startTime, Date endTime, OperateType type, Integer pageNo, Integer pageSize) throws ApiException {
        ProductGetRequest req = new ProductGetRequest();
        req.setStartTime(startTime);
        req.setEndTime(endTime);
        req.setType(type);
        req.setPageNo(pageNo);
        req.setPageSize(pageSize);
        ProductGetResponse res = client.excute(req);
        if (StringUtils.isNotBlank(res.getErrorCode())) {
            log.error("商品中心获取数据错误，错误码[{}],错误信息[{}]", res.getErrorCode(), res.getMsg());
            throw new ApiException(res.getMsg());
        }
        return res;
    }
}
