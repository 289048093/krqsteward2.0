package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

/**
 * User: Shiro
 * Date: 14-8-11
 * Time: 下午5:26
 */
public class LogisticsSendResponse extends ZiYouResponse {
    private static final long serialVersionUID = -1385617110448967247L;
    @ApiField("is_success")
    private Boolean isSuccess;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }
}
