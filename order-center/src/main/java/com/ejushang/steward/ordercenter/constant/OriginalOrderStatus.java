package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 原始订单状态枚举
 * User: Baron.Zhang
 * Date: 14-1-6
 * Time: 下午2:19
 */
@JsonSerialize(using = EnumSerializer.class)
public enum OriginalOrderStatus implements ViewEnum {

    // 没有创建支付宝交易
    TRADE_NO_CREATE_PAY("没有创建支付宝交易"),
    // 等待买家付款
    WAIT_BUYER_PAY("等待买家付款"),
    // 淘宝：等待卖家发货,即:买家已付款 / 京东：等待出库
    WAIT_SELLER_SEND_GOODS("等待卖家发货,即:买家已付款"),
    // 淘宝：等待买家确认收货,即:卖家已发货 / 京东：等待确认收货
    WAIT_BUYER_CONFIRM_GOODS("等待买家确认收货,即:卖家已发货"),
    // 买家已签收,货到付款专用
    TRADE_BUYER_SIGNED("买家已签收,货到付款专用"),
    // 交易成功
    TRADE_FINISHED("交易成功"),
    // 付款以后用户退款成功，交易自动关闭
    TRADE_CLOSED("付款以后用户退款成功，交易自动关闭"),
    // 付款以前，卖家或买家主动关闭交易
    TRADE_CLOSED_BY_TAOBAO("付款以前，卖家或买家主动关闭交易"),
    // 京东：完成
    FINISHED_L("完成"),
    // 京东：取消（取消的订单不返回收货人基本信息）
    TRADE_CANCELED("取消（取消的订单不返回收货人基本信息）"),
    // 京东：已锁定（锁定的订单不返回收货人基本信息）
    LOCKED("已锁定（锁定的订单不返回收货人基本信息）");

    public String value;

    public String name;

    OriginalOrderStatus(String value) {
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
