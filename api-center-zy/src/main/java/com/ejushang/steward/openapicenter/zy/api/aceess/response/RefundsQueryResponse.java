package com.ejushang.steward.openapicenter.zy.api.aceess.response;

import com.ejushang.steward.openapicenter.zy.api.aceess.domain.RefundBill;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiListField;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Shiro
 * Date: 14-8-11
 * Time: 下午3:37
 */
public class RefundsQueryResponse extends ZiYouResponse {
    private static final long serialVersionUID = 4702143628822908819L;

    @ApiListField("refund_bill_list")
    private List<RefundBill> refundBillList = new ArrayList<RefundBill>(0);

    @ApiField("total_results")
    private Long totalResults;

    public List<RefundBill> getRefundBillList() {
        return refundBillList;
    }

    public void setRefundBillList(List<RefundBill> refundBillList) {
        this.refundBillList = refundBillList;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }
}
