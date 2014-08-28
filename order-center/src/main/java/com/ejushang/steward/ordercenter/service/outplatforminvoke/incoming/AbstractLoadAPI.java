package com.ejushang.steward.ordercenter.service.outplatforminvoke.incoming;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.openapicenter.zy.api.aceess.DefaultZiYouClient;
import com.ejushang.steward.openapicenter.zy.api.aceess.ZiYouClient;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateType;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateTypeBean;
import com.ejushang.steward.openapicenter.zy.api.aceess.exception.ApiException;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.AbstractOperatePageGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.AbstractPageGetResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ProductGetResponse;
import com.ejushang.steward.openapicenter.zy.constant.ConstantZiYou;
import com.ejushang.steward.ordercenter.util.CollectionUtil;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-25
 * Time: 下午3:18
 */
public abstract class AbstractLoadAPI {

    protected static final ZiYouClient client = new DefaultZiYouClient(ConstantZiYou.ZY_API_URL, ConstantZiYou.ZY_APP_KEY, ConstantZiYou.ZY_APP_SECRET, ConstantZiYou.YYL_ACCESS_TOKEN);

    protected final static Integer DEFAULT_PAGE_SIZE = 20;

    protected OperateService operateService;

    protected  static Logger log = null;

    protected abstract AbstractOperatePageGetResponse remoteGet(Date startTime, Date endTime, OperateType type, Integer pageNo, Integer pageSize) throws ApiException;


    @Transactional
    public void loadData(Date startTime, Date endTime) throws ApiException {
        Integer pageNo = 1;
        AbstractPageGetResponse res = loadData(startTime, endTime, pageNo);
        Long total = res.getTotalResults();
        int pageCount = (int) Math.ceil((total * 1.0) / DEFAULT_PAGE_SIZE);
        if (pageCount > 1) {
            for (int i = 2; i <= pageCount; i++) {
                loadData(startTime, endTime, pageNo);
            }
        }
    }

    private AbstractPageGetResponse loadData(Date startTime, Date endTime, int pageNo) throws ApiException {
        return loadData(startTime, endTime, null, pageNo, DEFAULT_PAGE_SIZE);
    }

    private AbstractPageGetResponse loadData(Date startTime, Date endTime, OperateType type, Integer pageNo, Integer pageSize) throws ApiException {

        log.debug("请求商品中心数据：startTime:{},endTime:{},type:{},pageNo:{},pageSize:{}",
                EJSDateUtils.formatDate(startTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR),
                EJSDateUtils.formatDate(endTime, EJSDateUtils.DateFormatType.DATE_FORMAT_STR),
                type,
                pageNo,
                pageSize);
        AbstractOperatePageGetResponse res = remoteGet(startTime, endTime, type, pageNo, pageSize);
        List<? extends OperateTypeBean> productList = res.getData();
        Map<OperateType, ArrayList<OperateTypeBean>> map = ImmutableMap.of(OperateType.ADD, new ArrayList<OperateTypeBean>(),
                OperateType.UPDATE, new ArrayList<OperateTypeBean>(),
                OperateType.DELETE, new ArrayList<OperateTypeBean>());
        if (CollectionUtil.isNotEmpty(productList)) {
            for (OperateTypeBean prod : productList) {
                map.get(prod.getType()).add(prod);
            }
            for (OperateTypeBean prod : map.get(OperateType.ADD)) {
                try {
                    operateService.add(prod);
                } catch (Exception e) {
                    log.error("操作商品中心的数据时错误，数据:{}", prod,e);
                }
            }
            for (OperateTypeBean prod : map.get(OperateType.UPDATE)) {
                try {
                    operateService.update(prod);
                } catch (Exception e) {
                    log.error("操作商品中心的数据时错误，数据:{}", prod);
                }
            }
            for (OperateTypeBean prod : map.get(OperateType.DELETE)) {
                try {
                    operateService.delete(prod);
                } catch (Exception e) {
                    log.error("操作商品中心的数据时错误，数据:{}", prod);
                }
            }
        }
        return res;
    }

}
