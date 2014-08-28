package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 京东平台订单状态
 * User: Baron.Zhang
 * Date: 14-4-14
 * Time: 下午7:27
 */
@JsonSerialize(using = EnumSerializer.class)
public enum JdOrderStatus implements ViewEnum {
    WAIT_SELLER_STOCK_OUT("等待出库"),
    SEND_TO_DISTRIBUTION_CENER("发往配送中心（只适用于LBP，SOPL商家）"),
    DISTRIBUTION_CENTER_RECEIVED("配送中心已收货（只适用于LBP，SOPL商家）"),
    WAIT_GOODS_RECEIVE_CONFIRM("等待确认收货"),
    RECEIPTS_CONFIRM("收款确认（服务完成）（只适用于LBP，SOPL商家）"),
    WAIT_SELLER_DELIVERY("等待发货（只适用于海外购商家，等待境内发货 标签下的订单）"),
    FINISHED_L("完成"),
    TRADE_CANCELED("取消（取消的订单不返回收货人基本信息）"),
    LOCKED("已锁定（锁定的订单不返回收货人基本信息）");


    private String value;

    private JdOrderStatus(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getValue() {
        return value;
    }


}
