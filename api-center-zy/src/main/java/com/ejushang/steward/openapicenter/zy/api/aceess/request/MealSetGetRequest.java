package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.response.MealSetGetResponseAbstract;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-25
 * Time: 下午6:23
 */
public class MealSetGetRequest extends AbstractOperatedPageGetRequest<MealSetGetResponseAbstract> {


    @Override
    public String getApiMethodName() {
        return "operated.mealset.get";
    }

    @Override
    protected void checkParams() {

    }
}
