package com.ejushang.steward.common.page;

import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * User: liubin
 * Date: 14-3-11
 */
public class PageFactory {

    public static Page getPage(HttpServletRequest request) {
        int pageNo = ServletRequestUtils.getIntParameter(request, "page", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "limit", Page.DEFAULT_PAGE_SIZE);
        return new Page(pageNo, pageSize);
    }

}
