package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.LogisticsInfo;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

/**
 * User: Shiro
 * Date: 14-8-12
 * Time: 上午10:33
 */
public class TraceSearchResponse extends ZiYouResponse {
    private static final long serialVersionUID = -1296051302429466600L;
    @ApiField("logistics_info")
    private LogisticsInfo logisticsInfo;

    public LogisticsInfo getLogisticsInfo() {
        return logisticsInfo;
    }

    public void setLogisticsInfo(LogisticsInfo logisticsInfo) {
        this.logisticsInfo = logisticsInfo;
    }
}
