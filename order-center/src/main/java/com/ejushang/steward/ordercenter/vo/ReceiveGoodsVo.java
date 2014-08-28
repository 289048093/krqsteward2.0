package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.ordercenter.constant.RefundGoodsFace;
import com.ejushang.steward.ordercenter.constant.RefundGoodsFunc;
import com.ejushang.steward.ordercenter.constant.RefundGoodsPack;

/**
 * 确认收货实体
 *
 * @Author Channel
 * @Date 2014/8/11
 * @Version: 1.0
 */
public class ReceiveGoodsVo {

    /**
     * 售后工单项ID
     */
    private Integer afterSalesItemId;
    /**
     * 收货数量
     */
    private Integer receivedCount;
    /**
     * RefundGoodsPack:退货产品包装;NEW:新,OLD:有/非新,NVL:无;
     */
    private RefundGoodsPack pack;
    /**
     * RefundGoodsFunc:退货产品功能;GOOD:好,BAD:坏,CHECK:待检测;
     */
    private RefundGoodsFunc func;
    /**
     * RefundGoodsFace:退货产品外观;NEW:新,SMALL_DAMAGE:轻微损:DAMAGE:严重损;
     */
    private RefundGoodsFace face;
    /**
     * 退货产品收货备注
     */
    private String remark;

    public Integer getAfterSalesItemId() {
        return afterSalesItemId;
    }

    public void setAfterSalesItemId(Integer afterSalesItemId) {
        this.afterSalesItemId = afterSalesItemId;
    }

    public Integer getReceivedCount() {
        return receivedCount;
    }

    public void setReceivedCount(Integer receivedCount) {
        this.receivedCount = receivedCount;
    }

    public RefundGoodsPack getPack() {
        return pack;
    }

    public void setPack(RefundGoodsPack pack) {
        this.pack = pack;
    }

    public RefundGoodsFunc getFunc() {
        return func;
    }

    public void setFunc(RefundGoodsFunc func) {
        this.func = func;
    }

    public RefundGoodsFace getFace() {
        return face;
    }

    public void setFace(RefundGoodsFace face) {
        this.face = face;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ReceiveGoodsVo{" +
                "afterSalesItemId=" + afterSalesItemId +
                ", receivedCount=" + receivedCount +
                ", pack=" + pack +
                ", func=" + func +
                ", face=" + face +
                ", remark='" + remark + '\'' +
                '}';
    }
}
