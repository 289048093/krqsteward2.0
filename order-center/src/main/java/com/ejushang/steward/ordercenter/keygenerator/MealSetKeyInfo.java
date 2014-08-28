package com.ejushang.steward.ordercenter.keygenerator;

import com.ejushang.steward.common.util.EJSDateUtils;

/**
 * User:Amos.zhou
 * Date: 13-12-27
 * Time: 上午10:58
 */
public class MealSetKeyInfo extends KeyInfo {

    public MealSetKeyInfo(String keyName, int poolSize) {
        super(keyName, poolSize);
    }

    @Override
    public String getPrefix() {
       return "TU" + EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.SIMPLE_DATE_FORMAT);
    }

    @Override
    public void updateNexValue(Integer currValue) {
        maxCacheValue = currValue + poolSize;
        minCacheValue = currValue + 1;
        nextValue = minCacheValue;
    }
}
