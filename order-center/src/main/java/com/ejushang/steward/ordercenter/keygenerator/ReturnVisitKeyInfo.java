package com.ejushang.steward.ordercenter.keygenerator;


import com.ejushang.steward.common.util.EJSDateUtils;

/**
 * Created by: codec.yang
 * Date: 2014/8/14 17:48
 */
public class ReturnVisitKeyInfo  extends OrderKeyInfo {

    public ReturnVisitKeyInfo(String keyName, int poolSize) {
        super(keyName, poolSize);
    }

    @Override
    public String getPrefix() {
        return "R"+EJSDateUtils.formatDate(EJSDateUtils.getCurrentDate(), EJSDateUtils.DateFormatType.SIMPLE_DATE_FORMAT);
    }

}
