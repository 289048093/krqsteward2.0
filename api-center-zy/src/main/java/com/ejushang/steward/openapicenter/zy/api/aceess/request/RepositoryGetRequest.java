package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.response.RepositoryGetResponse;

/**
 * 获取仓库信息
 *
 * User:  Sed.Lee(李朝)
 * Date: 14-8-25
 * Time: 上午10:58
 */
public class RepositoryGetRequest extends AbstractOperatedPageGetRequest<RepositoryGetResponse> {



    @Override
    public String getApiMethodName() {
        return "operated.repository.get";
    }


    @Override
    protected void checkParams() {

    }


}
