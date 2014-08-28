package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.ordercenter.constant.Progress;
import com.ejushang.steward.ordercenter.constant.ReturnVisitStatus;
import com.ejushang.steward.ordercenter.constant.ReturnVisitType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 15:53
 */

@Table(name = "t_return_visit_task")
@Entity
public class ReturnVisitTask implements EntityClass<Integer> {

    private Integer id;
    private String returnVisitNo;
    private ReturnVisitType type;
    private Integer orderId;
    private ReturnVisitStatus status;
    private String visitorName;
    private String visitorRealname;
    private Date lastVisitTime;
    private Date appointmentTime;
    private Date createTime;
    private Date updateTime;
    private String phone;
    private String mobile;
    private String platformOrderNo;
    private Date displayTime;
    private Order order;
    private Boolean used;
    private Boolean redirectAfterSale;
    private Progress afterSaleStatus;

    @Column(name="after_sale_status")
    @Enumerated(EnumType.STRING)
    public Progress getAfterSaleStatus() {
        return afterSaleStatus;
    }

    public void setAfterSaleStatus(Progress afterSaleStatus) {
        this.afterSaleStatus = afterSaleStatus;
    }

    @Column(name="redirect_after_sale")
    public Boolean getRedirectAfterSale() {
        return redirectAfterSale;
    }

    public void setRedirectAfterSale(Boolean redirectAfterSale) {
        this.redirectAfterSale = redirectAfterSale;
    }

    @Column(name="return_visit_no")
    public String getReturnVisitNo() {
        return returnVisitNo;
    }

    public void setReturnVisitNo(String returnVisitNo) {
        this.returnVisitNo = returnVisitNo;
    }

    @Column(name="used")
    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public ReturnVisitType getType() {
        return type;
    }

    public void setType(ReturnVisitType type) {
        this.type = type;
    }

    @Column(name = "order_id")
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public ReturnVisitStatus getStatus() {
        return status;
    }

    public void setStatus(ReturnVisitStatus status) {
        this.status = status;
    }

    @Column(name = "last_visit_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    @Column(name = "appointment_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    @Column(name = "create_time")
    @JsonIgnore
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time")
    @JsonIgnore
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "visitor_name")
    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    @Column(name = "visitor_realname")
    public String getVisitorRealname() {
        return visitorRealname;
    }

    public void setVisitorRealname(String visitorRealname) {
        this.visitorRealname = visitorRealname;
    }

    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "platform_order_no")
    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    @JsonIgnore
    @Column(name = "display_time")
    public Date getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(Date displayTime) {
        this.displayTime = displayTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id",insertable = false, updatable = false)
    @JsonIgnore
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
