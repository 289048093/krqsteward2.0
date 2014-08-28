package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateType;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ProductGetResponse;

import java.util.Map;

/**
 * 获取产品数据
 * <p/>
 * User:  Sed.Lee(李朝)
 * Date: 14-8-22
 * Time: 下午1:54
 */
public class ProductGetRequest extends AbstractOperatedPageGetRequest<ProductGetResponse> {


    @Override
    public String getApiMethodName() {
        return "operated.product.get";
    }



    @Override
    protected void checkParams() {

    }


}
