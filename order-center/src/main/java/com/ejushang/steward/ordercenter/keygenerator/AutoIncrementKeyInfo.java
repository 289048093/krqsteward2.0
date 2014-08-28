package com.ejushang.steward.ordercenter.keygenerator;

/**
 * User:Amos.zhou
 * Date: 14-4-12
 * Time: 下午2:10
 */
public class AutoIncrementKeyInfo extends KeyInfo {


    public AutoIncrementKeyInfo(String keyName) {
        super(keyName, 0);
    }

    @Override
    protected String getPrefix() {
        return "";
    }

    @Override
    protected void updateNexValue(Integer currValue) {
        maxCacheValue = currValue + 1;
        minCacheValue = currValue + 1;
        nextValue = minCacheValue;
    }
}
