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


/**
 * 发货表
 * @Author Channel
 * @Date 2014-08-22
 */
@Table(name = "t_after_sales_send")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class AfterSalesSend implements EntityClass<Integer>, OperableData {

    /**
     * id
     */
    private Integer id;
    /**
     * 售后工单ID
     */
    private Integer afterSalesId;
    /**
     * 订单ID
     */
    private Integer orderId;
    /**
     * 物流编号
     */
    private String shippingNo;
    /**
     * 物流公司
     */
    private String shippingComp;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货人手机号(与收货人电话在业务上必须存在一个)
     */
    private String receiverPhone;
    /**
     * 收货人电话(与收货人手机号在业务上必须存在一个)
     */
    private String receiverMobile;
    /**
     * 收货人邮编
     */
    private String receiverZip;
    /**
     * 收货人省份
     */
    private String receiverState;
    /**
     * 收货人城市
     */
    private String receiverCity;
    /**
     * 收货人地区
     */
    private String receiverDistrict;
    /**
     * 不包含省市区的详细地址
     */
    private String receiverAddress;
    /**
     * 备注信息（卖家留言）
     */
    private String receiverRemark;
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
    * 获取"售后工单ID"
    */
    @javax.persistence.Column(name = "after_sales_id")
    @Basic
    public Integer getAfterSalesId() {
        return afterSalesId;
    }

    /**
     * 设置"售后工单ID"
     */
    public void setAfterSalesId(Integer afterSalesId) {
        this.afterSalesId = afterSalesId;
    }
    /**
    * 获取"订单ID"
    */
    @javax.persistence.Column(name = "order_id")
    @Basic
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置"订单ID"
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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
    * 获取"收货人姓名"
    */
    @javax.persistence.Column(name = "receiver_name")
    @Basic
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 设置"收货人姓名"
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    /**
    * 获取"收货人手机号(与收货人电话在业务上必须存在一个)"
    */
    @javax.persistence.Column(name = "receiver_phone")
    @Basic
    public String getReceiverPhone() {
        return receiverPhone;
    }

    /**
     * 设置"收货人手机号(与收货人电话在业务上必须存在一个)"
     */
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
    /**
    * 获取"收货人电话(与收货人手机号在业务上必须存在一个)"
    */
    @javax.persistence.Column(name = "receiver_mobile")
    @Basic
    public String getReceiverMobile() {
        return receiverMobile;
    }

    /**
     * 设置"收货人电话(与收货人手机号在业务上必须存在一个)"
     */
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }
    /**
    * 获取"收货人邮编"
    */
    @javax.persistence.Column(name = "receiver_zip")
    @Basic
    public String getReceiverZip() {
        return receiverZip;
    }

    /**
     * 设置"收货人邮编"
     */
    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }
    /**
    * 获取"收货人省份"
    */
    @javax.persistence.Column(name = "receiver_state")
    @Basic
    public String getReceiverState() {
        return receiverState;
    }

    /**
     * 设置"收货人省份"
     */
    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }
    /**
    * 获取"收货人城市"
    */
    @javax.persistence.Column(name = "receiver_city")
    @Basic
    public String getReceiverCity() {
        return receiverCity;
    }

    /**
     * 设置"收货人城市"
     */
    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }
    /**
    * 获取"收货人地区"
    */
    @javax.persistence.Column(name = "receiver_district")
    @Basic
    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    /**
     * 设置"收货人地区"
     */
    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }
    /**
    * 获取"不包含省市区的详细地址"
    */
    @javax.persistence.Column(name = "receiver_address")
    @Basic
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     * 设置"不包含省市区的详细地址"
     */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }
    /**
    * 获取"备注信息（卖家留言）"
    */
    @javax.persistence.Column(name = "receiver_remark")
    @Basic
    public String getReceiverRemark() {
        return receiverRemark;
    }

    /**
     * 设置"备注信息（卖家留言）"
     */
    public void setReceiverRemark(String receiverRemark) {
        this.receiverRemark = receiverRemark;
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
        StringBuilder strBuf = new StringBuilder("AfterSalesSend{");
        strBuf.append("id=").append(this.id);
        strBuf.append(", ");
        strBuf.append("afterSalesId=").append(this.afterSalesId);
        strBuf.append(", ");
        strBuf.append("orderId=").append(this.orderId);
        strBuf.append(", ");
        strBuf.append("shippingNo=").append(this.shippingNo);
        strBuf.append(", ");
        strBuf.append("shippingComp=").append(this.shippingComp);
        strBuf.append(", ");
        strBuf.append("receiverName=").append(this.receiverName);
        strBuf.append(", ");
        strBuf.append("receiverPhone=").append(this.receiverPhone);
        strBuf.append(", ");
        strBuf.append("receiverMobile=").append(this.receiverMobile);
        strBuf.append(", ");
        strBuf.append("receiverZip=").append(this.receiverZip);
        strBuf.append(", ");
        strBuf.append("receiverState=").append(this.receiverState);
        strBuf.append(", ");
        strBuf.append("receiverCity=").append(this.receiverCity);
        strBuf.append(", ");
        strBuf.append("receiverDistrict=").append(this.receiverDistrict);
        strBuf.append(", ");
        strBuf.append("receiverAddress=").append(this.receiverAddress);
        strBuf.append(", ");
        strBuf.append("receiverRemark=").append(this.receiverRemark);
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