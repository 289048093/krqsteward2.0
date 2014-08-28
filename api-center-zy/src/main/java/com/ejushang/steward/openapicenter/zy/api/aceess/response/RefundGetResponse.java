package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.RefundBill;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;

/**
 * User: Shiro
 * Date: 14-8-11
 * Time: 下午4:42
 */
public class RefundGetResponse extends ZiYouResponse {
    private static final long serialVersionUID = -7934584311313110214L;
    @ApiField("refund_bill")
    private RefundBill refundBill;

    public RefundBill getRefundBill() {
        return refundBill;
    }

    public void setRefundBill(RefundBill refundBill) {
        this.refundBill = refundBill;
    }
}
