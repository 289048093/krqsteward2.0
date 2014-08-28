package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.MealSet;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateTypeBean;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiListField;

import java.util.List;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-25
 * Time: 下午6:19
 */
public class MealSetGetResponseAbstract extends AbstractOperatePageGetResponse {

    @ApiListField("data")
    private List<MealSet> data;

    @Override
    public List<MealSet> getData() {
        return data;
    }

    @Override
    public void setData(List<? extends OperateTypeBean> data) {
        this.data = (List<MealSet>) data;
    }
}
