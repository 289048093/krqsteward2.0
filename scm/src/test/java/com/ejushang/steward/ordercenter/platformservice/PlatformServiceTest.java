package com.ejushang.steward.ordercenter.platformservice;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.BaseTest;
import com.ejushang.steward.ordercenter.domain.Platform;
import com.ejushang.steward.ordercenter.service.transportation.PlatformService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

/**
 * User: Shiro
 * Date: 14-4-12
 * Time: 下午2:08
 */
public class PlatformServiceTest extends BaseTest {
    @Autowired
    private PlatformService platformService;

    @Test
    @Transactional
    @Rollback(false)
    public void testSaveOrUpdate() {
        Platform platform = new Platform();
        platform.setName("易居尚");
        platformService.savePlatform(platform);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testDelete() {
        platformService.deleteById(3);
    }

    @Test
    public void testGetByKey() {
        Page page = new Page(1, 10);
        System.out.println(new JsonResult(true).addList(platformService.getByKey(null, page)));
    }
}
