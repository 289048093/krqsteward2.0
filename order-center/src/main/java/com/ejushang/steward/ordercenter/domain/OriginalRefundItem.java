package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.util.Money;

import javax.persistence.*;

/**
 * 原始退款单对应的对款商品项
 * User: Baron.Zhang
 * Date: 2014/5/9
 * Time: 14:23
 */
@Entity
@Table(name = "t_original_refund_item")
public class OriginalRefundItem implements EntityClass<Integer> {

    private Integer id;
    private Integer originalRefundId;
    private String numIid;
    private Money price = Money.valueOf(0);
    private Long num;
    private String outerId;
    private String sku;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "original_refund_id")
    public Integer getOriginalRefundId() {
        return originalRefundId;
    }

    public void setOriginalRefundId(Integer originalRefundId) {
        this.originalRefundId = originalRefundId;
    }

    @Column(name = "num_iid")
    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    @Column(name = "price")
    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    @Column(name = "num")
    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    @Column(name = "outer_id")
    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    @Column(name = "sku")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public String toString() {
        return "OriginalRefundItem{" +
                "id=" + id +
                ", originalRefundId=" + originalRefundId +
                ", numIid='" + numIid + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", outerId='" + outerId + '\'' +
                ", sku='" + sku + '\'' +
                '}';
    }
}
