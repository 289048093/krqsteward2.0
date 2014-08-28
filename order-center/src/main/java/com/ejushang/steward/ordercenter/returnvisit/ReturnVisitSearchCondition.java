package com.ejushang.steward.ordercenter.returnvisit;

import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.constant.Progress;
import com.ejushang.steward.ordercenter.constant.ReturnVisitStatus;
import com.ejushang.steward.ordercenter.constant.ReturnVisitType;

import java.util.Date;

/**
 * Created by: codec.yang
 * Date: 2014/8/12 11:13
 */
public class ReturnVisitSearchCondition {
    private ReturnVisitStatus status;
    private String visitorRealname;
    private Date lastVisitBeginTime;
    private Date lastVisitEndTime;
    private Boolean used;
    private PlatformType platformType;
    private Integer shopId;
    private Integer blandId;
    private ReturnVisitType type;
    private String buyerId;
    private String receiverName;
    private String receiverMobile;
    private String receiverPhone;
    private Progress afterSaleStatus;
    private String returnVisitNo;
    private Boolean redirectAfterSale;

    public Progress getAfterSaleStatus() {
        return afterSaleStatus;
    }

    public void setAfterSaleStatus(Progress afterSaleStatus) {
        this.afterSaleStatus = afterSaleStatus;
    }

    public String getReturnVisitNo() {
        return returnVisitNo;
    }

    public void setReturnVisitNo(String returnVisitNo) {
        this.returnVisitNo = returnVisitNo;
    }

    public ReturnVisitStatus getStatus() {
        return status;
    }

    public void setStatus(ReturnVisitStatus status) {
        this.status = status;
    }

    public String getVisitorRealname() {
        return visitorRealname;
    }

    public void setVisitorRealname(String visitorRealname) {
        this.visitorRealname = visitorRealname;
    }

    public Date getLastVisitBeginTime() {
        return lastVisitBeginTime;
    }

    public void setLastVisitBeginTime(Date lastVisitBeginTime) {
        this.lastVisitBeginTime = lastVisitBeginTime;
    }

    public Date getLastVisitEndTime() {
        return lastVisitEndTime;
    }

    public void setLastVisitEndTime(Date lastVisitEndTime) {
        this.lastVisitEndTime = lastVisitEndTime;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getBlandId() {
        return blandId;
    }

    public void setBlandId(Integer blandId) {
        this.blandId = blandId;
    }

    public ReturnVisitType getType() {
        return type;
    }

    public void setType(ReturnVisitType type) {
        this.type = type;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Boolean getRedirectAfterSale() {
        return redirectAfterSale;
    }

    public void setRedirectAfterSale(Boolean redirectAfterSale) {
        this.redirectAfterSale = redirectAfterSale;
    }
}
