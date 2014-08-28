package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.datastructure.ZiYouHashMap;
import com.ejushang.steward.openapicenter.zy.api.aceess.domain.OperateType;
import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;

import java.util.Map;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-26
 * Time: 上午10:47
 */
public abstract class AbstractOperatedPageGetRequest<T extends ZiYouResponse> extends AbstractPageGetRequest<T> {

    private OperateType type;

    public OperateType getType() {
        return type;
    }

    public void setType(OperateType type) {
        this.type = type;
    }

    @Override
    public Map<String, String> getTextParams() {
        ZiYouHashMap map = (ZiYouHashMap) super.getTextParams();
        map.put("type",type);
        return map;
    }
}
