package com.ejushang.steward.openapicenter.zy.api.aceess.datastructure;

import com.ejushang.steward.openapicenter.zy.api.aceess.util.Constants;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * User: Baron.Zhang
 * Date: 2014/8/6
 * Time: 10:22
 */
public class ZiYouHashMap extends HashMap<String,String> {
    private static final long serialVersionUID = -872952458643597346L;

    public ZiYouHashMap() {
        super();
    }

    public ZiYouHashMap(Map<? extends String, ? extends String> m) {
        super(m);
    }

    public String put(String key, Object value) {
        String strValue;

        if (value == null) {
            strValue = null;
        } else if (value instanceof String) {
            strValue = (String) value;
        } else if (value instanceof Integer) {
            strValue = ((Integer) value).toString();
        } else if (value instanceof Long) {
            strValue = ((Long) value).toString();
        } else if (value instanceof Float) {
            strValue = ((Float) value).toString();
        } else if (value instanceof Double) {
            strValue = ((Double) value).toString();
        } else if (value instanceof Boolean) {
            strValue = ((Boolean) value).toString();
        } else if (value instanceof Date) {
            DateFormat format = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
            format.setTimeZone(TimeZone.getTimeZone(Constants.DATE_TIMEZONE));
            strValue = format.format((Date) value);
        } else {
            strValue = value.toString();
        }

        return this.put(key, strValue);
    }

    public String put(String key, String value) {
        if (StringUtils.areNotEmpty(key, value)) {
            return super.put(key, value);
        } else {
            return null;
        }
    }
}
