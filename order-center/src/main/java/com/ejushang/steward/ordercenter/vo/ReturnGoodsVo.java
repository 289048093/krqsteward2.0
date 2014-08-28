package com.ejushang.steward.ordercenter.vo;

/**
 * 确认退货实体
 *
 * @Author Channel
 * @Date 2014/8/11
 * @Version: 1.0
 */
public class ReturnGoodsVo {

    /**
     * 售后工单项ID
     */
    private Integer afterSalesItemId;

    /**
     * 物流编号
     */
    private String shippingNo;
    /**
     * 物流公司
     */
    private String shippingComp;

    public Integer getAfterSalesItemId() {
        return afterSalesItemId;
    }

    public void setAfterSalesItemId(Integer afterSalesItemId) {
        this.afterSalesItemId = afterSalesItemId;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public String getShippingComp() {
        return shippingComp;
    }

    public void setShippingComp(String shippingComp) {
        this.shippingComp = shippingComp;
    }

    @Override
    public String toString() {
        return "ConfirmReturnVo{" +
                "afterSalesItemId=" + afterSalesItemId +
                ", shippingNo='" + shippingNo + '\'' +
                ", shippingComp='" + shippingComp + '\'' +
                '}';
    }


}
