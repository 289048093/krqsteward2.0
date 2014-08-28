package com.ejushang.steward.ordercenter.keygenerator;

import com.ejushang.steward.common.util.EJSDateUtils;

/**
 * User:Amos.zhou
 * Date: 14-5-26
 * Time: 下午7:31
 */
public class OrderItemKeyInfo extends OrderKeyInfo {

    public OrderItemKeyInfo(String keyName, int poolSize) {
        super(keyName, poolSize);
    }

    @Override
    public String getPrefix() {
        return "JD"+ EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.SIMPLE_DATE_FORMAT);
    }
}
