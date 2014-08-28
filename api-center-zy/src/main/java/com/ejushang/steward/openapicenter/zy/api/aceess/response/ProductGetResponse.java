package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateTypeBean;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.Product;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.ZiYouObject;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiListField;

import java.util.ArrayList;
import java.util.List;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-22
 * Time: 下午1:55
 */
public class ProductGetResponse extends AbstractOperatePageGetResponse {

    @ApiListField("data")
    private List<Product> data;

    @Override
    public List<Product> getData() {
        return data;
    }

    @Override
    public void setData(List<? extends OperateTypeBean> data) {
        this.data = (List<Product>) data;
    }
}
