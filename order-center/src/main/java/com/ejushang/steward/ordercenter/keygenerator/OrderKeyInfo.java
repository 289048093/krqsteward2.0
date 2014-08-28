package com.ejushang.steward.ordercenter.keygenerator;

import com.ejushang.steward.common.util.EJSDateUtils;

/**
 * User:Amos.zhou
 * Date: 13-12-26
 * Time: 上午10:51
 */
public class OrderKeyInfo extends KeyInfo {


    public OrderKeyInfo(String keyName, int poolSize) {
        super(keyName, poolSize);
    }

    @Override
    public String getPrefix() {
        //得到当前的时间，格式是yymmdd
        return EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.SIMPLE_DATE_FORMAT);
    }

    @Override
    public void updateNexValue(Integer currentValue) {
        //每轮从11111--99999,达到99999以后又从11111开始 ，构成一个环
        //增长了poolSize后的理论值
        maxCacheValue = currentValue + poolSize;
        //如果增长步长后，已经超过最大值，则从头开始（不必考虑浪费情况）
        if (maxCacheValue > SystemConfConstant.MAX_ORDER_NO) {
            minCacheValue = SystemConfConstant.MIN_ORDER_NO;
            maxCacheValue = minCacheValue + poolSize;
        } else {
            minCacheValue = currentValue + 1;
        }
        //初始化nextValue
        nextValue = minCacheValue;
    }

}
