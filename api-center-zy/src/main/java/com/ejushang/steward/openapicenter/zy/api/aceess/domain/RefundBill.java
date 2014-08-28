package com.ejushang.steward.openapicenter.zy.api.aceess.domain;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiField;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.ApiListField;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Shiro
 * Date: 14-8-6
 * Time: 下午4:55
 */
public class RefundBill extends ZiYouObject {

    private static final long serialVersionUID = 3837301822837384160L;
    /**
     * 退款单编号
     */
    @ApiField("refund_id")
    private String refundId;

    /**
     * refund:仅退款 return:退款退货
     */
    @ApiField("refund_type")
    private String refundType;

    /**
     * 订单项编号(整单优惠时，这个字段为null）
     */
    @ApiField("oid")
    private String oid;

    /**
     * 订单状态
     */
    @ApiField("trade_status")
    private String tradeStatus;

    /**
     * 申请退款金额，单位：分
     */
    @ApiField("refund_fee")
    private Long refundFee;

    /**
     * 申请退款原因
     */
    @ApiField("reason")
    private String reason;

    /**
     * 实际退款金额
     */
    @ApiField("actual_refund_fee")
    private Long actualRefundFee;

    /**
     * 买家昵称
     */
    @ApiField("buyer_nick")
    private String buyerNick;

    /**
     * 卖家昵称
     */
    @ApiField("seller_nick")
    private String sellerNick;

    /**
     * 退款创建时间
     */
    @ApiField("created")
    private Date created;

    /**
     * 订单编号
     */
    @ApiField("tid")
    private String tid;

    /**
     * 退款单状态。可选值：WAIT_SELLER_AGREE（买家申请，等待卖家同意），SELLER_REFUSE（卖家拒绝），
     * GOODS_RETURNING（退货中），
     * CLOSED（退款失败），SUCCESS（退款成功）。
     */
    @ApiField("status")
    private String status;

    /**
     * onsale:售中 aftersale：售后
     */
    @ApiField("refund_phase")
    private String refundPhase;

    /**
     * 退款更新时间
     */
    @ApiField("modified")
    private Date modified;

    /**
     * 单据类型，refund_bill:退款单/return_bill:退货单
     */
    @ApiField("bill_type")
    private String billType;

    /**
     * 物流公司编码。
     * 可选值：shunfeng（顺丰）， zhongtong（中通），yunda（韵达），zhaijisong（宅急送），
     * ems（ems），yuantong（圆通），shentong（申通），quanritongkuaidi（全日通），
     * kuaijiesudi（快捷），huitongkuaidi（汇通），guotongkuaidi（国通），lianbangkuaidi（联邦），
     * quanfengkuaidi（全峰），suer（速尔），tiantian（天天），youshuwuliu（优速），youzhengguonei（邮政国内小包），
     * zengyi（增益速递），debang（德邦物流），changjiazisong（厂家自送），xinbang（新邦物流），debangwuliu（德邦物流），
     * unknown（未知）
     */
    @ApiField("company_code")
    private String companyCode;

    /**
     * 物流运单号
     */
    @ApiField("sid")
    private String sid;

    /**
     * 退货单操作日志
     */
    @ApiField("operation_log")
    private String operationLog;

    /**
     * 退款说明
     */
    @ApiField("description")
    private String description;

    /**
     * 买家编号
     */
    @ApiField("buyer_id")
    private String buyerId;

    /**
     * 退款单的相关标签信息
     */
    @ApiListField("tag_list")
    private List<Tag> tagList = new ArrayList<Tag>(0);

    /**
     * 退款商品信息
     */
    @ApiListField("item_list")
    private List<RefundItem> itemList = new ArrayList<RefundItem>(0);

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefundPhase() {
        return refundPhase;
    }

    public void setRefundPhase(String refundPhase) {
        this.refundPhase = refundPhase;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getOperationLog() {
        return operationLog;
    }

    public void setOperationLog(String operationLog) {
        this.operationLog = operationLog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<RefundItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<RefundItem> itemList) {
        this.itemList = itemList;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Long getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Long refundFee) {
        this.refundFee = refundFee;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getActualRefundFee() {
        return actualRefundFee;
    }

    public void setActualRefundFee(Long actualRefundFee) {
        this.actualRefundFee = actualRefundFee;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
