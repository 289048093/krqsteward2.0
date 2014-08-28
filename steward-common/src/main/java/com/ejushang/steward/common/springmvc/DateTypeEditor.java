package com.ejushang.steward.common.springmvc;

import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换器
 * <p/>
 * 根据日期字符串长度判断是长日期还是短日期。
 *
 * @author liubin
 */
public class DateTypeEditor extends PropertyEditorSupport {


    public static final String LONG_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int LONG_FORMAT_LENGTH = LONG_FORMAT.length();
    public final DateFormat DF_LONG = new SimpleDateFormat(LONG_FORMAT);

    public static final String SHORT_FORMAT = "yyyy-MM-dd";
    public static final int SHORT_FORMAT_LENGTH = SHORT_FORMAT.length();
    public final DateFormat DF_SHORT = new SimpleDateFormat(SHORT_FORMAT);

    public static final String HOUR_FORMAT = "yyyy-MM-dd HH";
    public static final int HOUR_FORMAT_LENGTH = HOUR_FORMAT.length();
    public final DateFormat DF_HOUR = new SimpleDateFormat(HOUR_FORMAT);

    public static final String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
    public static final int MINUTE_FORMAT_LENGTH = MINUTE_FORMAT.length();
    public final DateFormat DF_MINUTE = new SimpleDateFormat(MINUTE_FORMAT);

    public static final String ONLY_TIME_FORMAT = "HH:mm";
    public static final int ONLY_TIME_FORMAT_LENGTH = ONLY_TIME_FORMAT.length();
    public final DateFormat DF_ONLY_HOUR = new SimpleDateFormat(ONLY_TIME_FORMAT);



    public void setAsText(String text) throws IllegalArgumentException {
        text = text.trim();
        if (StringUtils.isBlank(text)) {
            setValue(null);
            return;
        }
        try {
            if (text.length() <= ONLY_TIME_FORMAT_LENGTH) {
                setValue(new java.sql.Date(DF_ONLY_HOUR.parse(text).getTime()));
            } else if (text.length() <= MINUTE_FORMAT_LENGTH) {
                setValue(new java.sql.Date(DF_SHORT.parse(text).getTime()));
            } else if (text.length() <= HOUR_FORMAT_LENGTH) {
                setValue(new java.sql.Timestamp(DF_HOUR.parse(text).getTime()));
            } else if (text.length() <= SHORT_FORMAT_LENGTH) {
                setValue(new java.sql.Timestamp(DF_MINUTE.parse(text).getTime()));
            } else {
                setValue(new java.sql.Timestamp(DF_LONG.parse(text).getTime()));
            }
        } catch (ParseException ex) {
            IllegalArgumentException iae = new IllegalArgumentException(
                    "Could not parse date: " + ex.getMessage());
            iae.initCause(ex);
            throw iae;
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? DF_LONG.format(value) : null);
    }
}
