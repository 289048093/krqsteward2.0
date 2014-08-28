package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_invoice")
@Entity
public class Invoice implements EntityClass<Integer>{

    private Integer id;

    private Receiver receiver;

    private String shippingNo;

    private String shippingComp;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "shipping_no")
    @Basic
    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }


    @javax.persistence.Column(name = "shipping_comp")
    @Basic
    public String getShippingComp() {
        return shippingComp;
    }

    public void setShippingComp(String shippingComp) {
        this.shippingComp = shippingComp;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }


    /**
     * 拆单的时候复制发货信息
     * @return
     */
    public Invoice copyForSplit() {
        Invoice copiedInvoice = new Invoice();
        copiedInvoice.setReceiver(getReceiver().clone());
        copiedInvoice.setShippingComp(getShippingComp());
        copiedInvoice.setShippingNo(getShippingNo());
        return copiedInvoice;

    }
}
