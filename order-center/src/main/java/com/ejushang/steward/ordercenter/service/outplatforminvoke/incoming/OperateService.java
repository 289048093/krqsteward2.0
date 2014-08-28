package com.ejushang.steward.ordercenter.service.outplatforminvoke.incoming;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateTypeBean;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-27
 * Time: 上午11:38
 */
public interface OperateService {

    void add(OperateTypeBean bean);

    void update(OperateTypeBean bean);

    void delete(OperateTypeBean bean);
}
