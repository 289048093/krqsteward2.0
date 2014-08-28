package com.ejushang.steward.common.domain;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Id;

/**
 * User: liubin
 * Date: 14-3-28
 */
@Embeddable
public class Receiver implements Cloneable {

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverZip;

    private String receiverState;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;



    @javax.persistence.Column(name = "receiver_name")
    @Basic
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


    @javax.persistence.Column(name = "receiver_phone")
    @Basic
    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }


    @javax.persistence.Column(name = "receiver_mobile")
    @Basic
    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }


    @javax.persistence.Column(name = "receiver_zip")
    @Basic
    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }


    @javax.persistence.Column(name = "receiver_state")
    @Basic
    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }


    @javax.persistence.Column(name = "receiver_city")
    @Basic
    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }


    @javax.persistence.Column(name = "receiver_district")
    @Basic
    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }


    @javax.persistence.Column(name = "receiver_address")
    @Basic
    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    @Override
    public Receiver clone() {
        Receiver receiver = new Receiver();
        receiver.setReceiverName(this.getReceiverName());
        receiver.setReceiverAddress(this.getReceiverAddress());
        receiver.setReceiverCity(this.getReceiverCity());
        receiver.setReceiverDistrict(this.getReceiverDistrict());
        receiver.setReceiverMobile(this.getReceiverMobile());
        receiver.setReceiverPhone(this.getReceiverPhone());
        receiver.setReceiverState(this.getReceiverState());
        receiver.setReceiverZip(this.getReceiverZip());

        return receiver;
    }

    /**
     * 将省市县信息添加到地址的最前面
     */
    public void copyAreaToAddress() {
        StringBuilder sb = new StringBuilder();
        boolean hasCity = receiverCity != null && receiverAddress.contains(receiverCity);
        if(receiverState != null && (!receiverAddress.startsWith(receiverState) || !hasCity)) {
            sb.append(receiverState);
            if(receiverCity != null && !receiverAddress.startsWith(receiverCity)) {
                sb.append(receiverCity);
                if(receiverDistrict != null && !receiverAddress.startsWith(receiverDistrict)) {
                    sb.append(receiverDistrict);
                }
            }
        }
//        boolean hasState = receiverState != null && receiverAddress.contains(receiverState);
//        boolean hasCity = receiverCity != null && receiverAddress.contains(receiverCity);
//        boolean hasDistrict = receiverDistrict != null && receiverAddress.contains(receiverDistrict);
//        if(receiverState != null && !hasState) {
//            sb.append(receiverState);
//            if(receiverCity != null && !hasCity) {
//                sb.append(receiverCity);
//                if(receiverDistrict != null && !hasDistrict) {
//                    sb.append(receiverDistrict);
//                }
//            }
//        }
        sb.append(receiverAddress);
        this.receiverAddress = sb.toString();
    }
}
