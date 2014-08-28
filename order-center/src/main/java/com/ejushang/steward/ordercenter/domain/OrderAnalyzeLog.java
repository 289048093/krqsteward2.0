package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import com.ejushang.steward.ordercenter.constant.OrderStatus;
import com.ejushang.steward.ordercenter.util.OrderUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 原始订单解析记录
 * User: liubin
 * Date: 14-7-7
 */
@Table(name = "t_order_analyze_log")
@Entity
public class OrderAnalyzeLog implements EntityClass<Integer>{

    private Integer id;

    private String message;

    private Date createTime;

    private Integer originalOrderId;

    @JsonIgnore
    private OriginalOrder originalOrder;

    private boolean processed;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="original_order_id", insertable = false, updatable = false)
    public OriginalOrder getOriginalOrder() {
        return originalOrder;
    }

    public void setOriginalOrder(OriginalOrder originalOrder) {
        this.originalOrder = originalOrder;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "original_order_id")
    @Basic
    public Integer getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Integer originalOrderId) {
        this.originalOrderId = originalOrderId;
    }


    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }

    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "processed")
    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
