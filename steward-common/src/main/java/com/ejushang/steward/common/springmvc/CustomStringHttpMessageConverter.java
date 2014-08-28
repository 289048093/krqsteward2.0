package com.ejushang.steward.common.springmvc;

import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;

/**
 * 解决ResponseBody乱码问题
 */
public class CustomStringHttpMessageConverter extends StringHttpMessageConverter {

    public CustomStringHttpMessageConverter() {
        super(Charset.forName("UTF-8"));
    }
}