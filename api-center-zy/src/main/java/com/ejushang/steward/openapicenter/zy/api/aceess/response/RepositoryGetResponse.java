package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateTypeBean;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Repository;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.ZiYouObject;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiListField;

import java.util.List;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-25
 * Time: 上午10:58
 */
public class RepositoryGetResponse extends AbstractOperatePageGetResponse {

    @ApiListField("data")
    private List<Repository> data;


    @Override
    public List<Repository> getData() {
        return data;
    }

    @Override
    public void setData(List<? extends OperateTypeBean> data) {
        this.data = (List<Repository>) data;
    }
}
