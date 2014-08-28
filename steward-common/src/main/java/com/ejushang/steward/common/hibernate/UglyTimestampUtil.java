package com.ejushang.steward.common.hibernate;

import java.util.Date;

/**
 * User: liubin
 * Date: 14-4-1
 */
public class UglyTimestampUtil {

    /**
     * 把java.sql.Timestamp转换成java.util.Date
     * 解决JDK恶心的他们之间互相equals不相等的问题
     * @param date
     * @return
     */
    public static final Date convertTimestampToDate(Date date) {
        if(date == null) return null;
        return new Date(date.getTime());
    }


}
