package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.scm.util.WebUtil;
import org.junit.Test;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-5-26
 * Time: 上午10:04
 */
public class WebUtilTest extends BaseTest{

    @Test
    public void testGetWebAppPath(){
        System.out.println(WebUtil.getWebAppPath());
    }

}
