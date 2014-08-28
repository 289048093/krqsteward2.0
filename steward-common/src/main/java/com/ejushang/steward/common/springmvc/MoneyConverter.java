package com.ejushang.steward.common.springmvc;

import com.ejushang.steward.common.util.Money;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * User: liubin
 * Date: 14-4-2
 */
public class MoneyConverter implements Converter<String, Money> {

    private static final Logger log = LoggerFactory.getLogger(MoneyConverter.class);

    @Override
    public Money convert(String source) {
        if(StringUtils.isBlank(source)) return null;
        try {
            return Money.valueOf(source);
        } catch (NumberFormatException e) {
            log.warn("金额在转成货币对象的时候发生错误:" + source);
            throw e;
        }

    }
}
