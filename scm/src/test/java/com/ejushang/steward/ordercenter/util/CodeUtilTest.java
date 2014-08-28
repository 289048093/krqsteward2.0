package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.ordercenter.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-5-22
 * Time: 下午3:56
 */
public class CodeUtilTest extends BaseTest{


    @Test
    @Transactional
    public void testCodeCreate(){
         String newCode = CodeUtil.createCode("");
        Assert.assertTrue(newCode.matches("^\\d{3}$"));
        newCode = CodeUtil.createCode("000");
        Assert.assertTrue(newCode.matches("^000\\d{3}$"));
    }
}
