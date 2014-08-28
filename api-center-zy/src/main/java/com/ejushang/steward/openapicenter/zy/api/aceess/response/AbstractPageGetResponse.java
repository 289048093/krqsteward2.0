package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.ZiYouObject;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

import java.util.List;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-26
 * Time: 下午6:27
 */
public abstract class AbstractPageGetResponse extends ZiYouResponse {


    @ApiField("total_results")
    private Long totalResults;

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }
}
