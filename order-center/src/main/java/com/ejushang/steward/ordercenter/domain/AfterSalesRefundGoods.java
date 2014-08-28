package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.common.util.Money;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.Date;
import java.util.List;

import com.ejushang.steward.ordercenter.constant.RefundGoodsFace;
import com.ejushang.steward.ordercenter.constant.RefundGoodsFunc;
import com.ejushang.steward.ordercenter.constant.RefundGoodsPack;

/**
 * 换货补货关联表
 * @Author Channel
 * @Date 2014-08-22
 */
@Table(name = "t_after_sales_refund_goods")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSalesRefundGoods implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 所属售后工单ID
     */
    private Integer afterSalesId;
    /**
     * 订单项ID
     */
    private Integer afterSalesItemId;
    /**
     * 退货数量
     */
    private Integer count;
    /**
     * 物流编号
     */
    private String shippingNo;
    /**
     * 物流公司
     */
    private String shippingComp;
    /**
     * 收货数量
     */
    private Integer receivedCount;
    /**
     * 是否已退货（客户已发货）
     */
    private Boolean returned;
    /**
     * 是否已收货（仓库已收货）
     */
    private Boolean received;
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
    /**
     * 操作者ID
     */
    private Integer operatorId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 版本号
     */
    private Integer version;

    /**
    * 获取"id"
    */
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    /**
     * 设置"id"
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
    * 获取"所属售后工单ID"
    */
    @javax.persistence.Column(name = "after_sales_id")
    @Basic
    public Integer getAfterSalesId() {
        return afterSalesId;
    }

    /**
     * 设置"所属售后工单ID"
     */
    public void setAfterSalesId(Integer afterSalesId) {
        this.afterSalesId = afterSalesId;
    }
    /**
    * 获取"订单项ID"
    */
    @javax.persistence.Column(name = "after_sales_item_id")
    @Basic
    public Integer getAfterSalesItemId() {
        return afterSalesItemId;
    }

    /**
     * 设置"订单项ID"
     */
    public void setAfterSalesItemId(Integer afterSalesItemId) {
        this.afterSalesItemId = afterSalesItemId;
    }
    /**
    * 获取"退货数量"
    */
    @javax.persistence.Column(name = "count")
    @Basic
    public Integer getCount() {
        return count;
    }

    /**
     * 设置"退货数量"
     */
    public void setCount(Integer count) {
        this.count = count;
    }
    /**
    * 获取"物流编号"
    */
    @javax.persistence.Column(name = "shipping_no")
    @Basic
    public String getShippingNo() {
        return shippingNo;
    }

    /**
     * 设置"物流编号"
     */
    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }
    /**
    * 获取"物流公司"
    */
    @javax.persistence.Column(name = "shipping_comp")
    @Basic
    public String getShippingComp() {
        return shippingComp;
    }

    /**
     * 设置"物流公司"
     */
    public void setShippingComp(String shippingComp) {
        this.shippingComp = shippingComp;
    }
    /**
    * 获取"收货数量"
    */
    @javax.persistence.Column(name = "received_count")
    @Basic
    public Integer getReceivedCount() {
        return receivedCount;
    }

    /**
     * 设置"收货数量"
     */
    public void setReceivedCount(Integer receivedCount) {
        this.receivedCount = receivedCount;
    }
    /**
    * 获取"是否已退货（客户已发货）"
    */
    @javax.persistence.Column(name = "returned")
    @Basic
    public Boolean isReturned() {
        return returned;
    }

    /**
     * 设置"是否已退货（客户已发货）"
     */
    public void setReturned(Boolean returned) {
        this.returned = returned;
    }
    /**
    * 获取"是否已收货（仓库已收货）"
    */
    @javax.persistence.Column(name = "received")
    @Basic
    public Boolean isReceived() {
        return received;
    }

    /**
     * 设置"是否已收货（仓库已收货）"
     */
    public void setReceived(Boolean received) {
        this.received = received;
    }
    /**
    * 获取"RefundGoodsPack:退货产品包装;NEW:新,OLD:有/非新,NVL:无;"
    */
    @javax.persistence.Column(name = "pack")
    @Enumerated(EnumType.STRING)
    public RefundGoodsPack getPack() {
        return pack;
    }

    /**
     * 设置"RefundGoodsPack:退货产品包装;NEW:新,OLD:有/非新,NVL:无;"
     */
    public void setPack(RefundGoodsPack pack) {
        this.pack = pack;
    }
    /**
    * 获取"RefundGoodsFunc:退货产品功能;GOOD:好,BAD:坏,CHECK:待检测;"
    */
    @javax.persistence.Column(name = "func")
    @Enumerated(EnumType.STRING)
    public RefundGoodsFunc getFunc() {
        return func;
    }

    /**
     * 设置"RefundGoodsFunc:退货产品功能;GOOD:好,BAD:坏,CHECK:待检测;"
     */
    public void setFunc(RefundGoodsFunc func) {
        this.func = func;
    }
    /**
    * 获取"RefundGoodsFace:退货产品外观;NEW:新,SMALL_DAMAGE:轻微损:DAMAGE:严重损;"
    */
    @javax.persistence.Column(name = "face")
    @Enumerated(EnumType.STRING)
    public RefundGoodsFace getFace() {
        return face;
    }

    /**
     * 设置"RefundGoodsFace:退货产品外观;NEW:新,SMALL_DAMAGE:轻微损:DAMAGE:严重损;"
     */
    public void setFace(RefundGoodsFace face) {
        this.face = face;
    }
    /**
    * 获取"退货产品收货备注"
    */
    @javax.persistence.Column(name = "remark")
    @Basic
    public String getRemark() {
        return remark;
    }

    /**
     * 设置"退货产品收货备注"
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
    * 获取"操作者ID"
    */
    @javax.persistence.Column(name = "operator_id")
    @Basic
    public Integer getOperatorId() {
        return operatorId;
    }

    /**
     * 设置"操作者ID"
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
    /**
    * 获取"创建时间"
    */
    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置"创建时间"
     */
    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }
    /**
    * 获取"更新时间"
    */
    @javax.persistence.Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置"更新时间"
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);
    }
    /**
    * 获取"版本号"
    */
    @Version
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置"版本号"
     */
    void setVersion(Integer version) {
        this.version = version;
    }
    /**
     * 售后单项目实体
     */
    @JsonIgnore
    private AfterSalesItem afterSalesItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_sales_item_id", insertable = false, updatable = false)
    public AfterSalesItem getAfterSalesItem() {
        return afterSalesItem;
    }

    public void setAfterSalesItem(AfterSalesItem afterSalesItem) {
        this.afterSalesItem = afterSalesItem;
    }

    /**
     * 售后单实体
     */
    @JsonIgnore
    private AfterSales afterSales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_sales_id", insertable = false, updatable = false)
    public AfterSales getAfterSales() {
        return afterSales;
    }

    public void setAfterSales(AfterSales afterSales) {
        this.afterSales = afterSales;
    }



    @Override
    public String toString() {
        StringBuilder strBuf = new StringBuilder("AfterSalesRefundGoods{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("afterSalesId=").append(this.afterSalesId);
        strBuf.append(", ");
        strBuf.append("afterSalesItemId=").append(this.afterSalesItemId);
        strBuf.append(", ");
        strBuf.append("count=").append(this.count);
        strBuf.append(", ");
        strBuf.append("shippingNo=").append(this.shippingNo);
        strBuf.append(", ");
        strBuf.append("shippingComp=").append(this.shippingComp);
        strBuf.append(", ");
        strBuf.append("receivedCount=").append(this.receivedCount);
        strBuf.append(", ");
        strBuf.append("returned=").append(this.returned);
        strBuf.append(", ");
        strBuf.append("received=").append(this.received);
        strBuf.append(", ");
        strBuf.append("pack=").append(this.pack);
        strBuf.append(", ");
        strBuf.append("func=").append(this.func);
        strBuf.append(", ");
        strBuf.append("face=").append(this.face);
        strBuf.append(", ");
        strBuf.append("remark=").append(this.remark);
        strBuf.append(", ");
        strBuf.append("operatorId=").append(this.operatorId);
        strBuf.append(", ");
        strBuf.append("createTime=").append(this.createTime);
        strBuf.append(", ");
        strBuf.append("updateTime=").append(this.updateTime);
        strBuf.append(", ");
        strBuf.append("version=").append(this.version);
        strBuf.append("}");
        return strBuf.toString();
    }

}