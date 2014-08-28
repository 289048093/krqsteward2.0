package com.ejushang.steward.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * User: Baron.Zhang
 * Date: 13-12-13
 * Time: 下午2:58
 */
public class StringUtilsEjs {

    /** 首字母大写 */
    public static String capitalize(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /** 下划线命名改为驼峰法命名 */
    public static String camelCase(String str) {
        return camelCase4Delimiter(str, "_");
    }

    /** 使用分隔符拆分后改为驼峰法命名 */
    public static String camelCase4Delimiter(String str, String delimiter) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (!str.contains(delimiter)) {
            return str;
        }
        String[] splitStr = str.split("\\" + delimiter);
        StringBuilder sbd = new StringBuilder();
        for (int i = 0; i < splitStr.length; i++) {
            // 首词不需要大写
            sbd.append(i == 0 ? splitStr[i] : capitalize(splitStr[i]));
        }
        return sbd.toString();
    }

    /** . 分隔命名改为驼峰法命名 */
    public static String camelCase4Do(String str) {
        return camelCase4Delimiter(str, ".");
    }

}
