package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateTypeBean;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.ZiYouObject;

import java.util.List;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-27
 * Time: 下午2:57
 */
public abstract class AbstractOperatePageGetResponse extends AbstractPageGetResponse {

    public abstract List<? extends OperateTypeBean> getData();

    public abstract void setData(List<? extends OperateTypeBean> data);
}
