package com.ejushang.steward.ordercenter.constant;

import com.ejushang.steward.common.domain.util.ViewEnum;
import com.ejushang.steward.common.jackson.EnumSerializer;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * User: Sed.Li(李朝)
 * Date: 14-4-16
 * Time: 上午9:48
 */
@JsonSerialize(using = EnumSerializer.class)
public enum InOutStockType implements ViewEnum{

    IN_STOCK_TYPE_INIT("初始入库"),
    IN_STOCK_TYPE_PROCURE("采购入库"),
    IN_STOCK_TYPE_ALLOT("调拨入库"),
    IN_STOCK_TYPE_CHECK("盘点入库"),
    IN_STOCK_TYPE_RETURNS("退货入库"),
    IN_STOCK_TYPE_MAINTAIN("维修入库"),
    IN_STOCK_TYPE_OTHER("其他"),

    OUT_STOCK_TYPE_SCRAP("报废出库"),
    OUT_STOCK_TYPE_DIFFERENCE("差错出库"),
    OUT_STOCK_TYPE_ALLOT("调拨出库"),
    OUT_STOCK_TYPE_RECEIVE("领用出库"),
    OUT_STOCK_TYPE_CHECK("盘点出库"),
    OUT_STOCK_TYPE_RETURNS("退货出库"),
    OUT_STOCK_TYPE_LOGISTICS_BREAK("物流损坏"),
    OUT_STOCK_TYPE_SELL("销售出库"),
    OUT_STOCK_TYPE_GIFT("赠品出库"),
    OUT_STOCK_TYPE_OTHER("其他");


    private String value;

    private InOutStockType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
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
