package com.ejushang.steward.scm;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.service.ResourceService;
import com.ejushang.uams.exception.UamsClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: liubin
 * Date: 14-4-9
 */
public class ScmApplication extends Application {


    private static final Logger log = LoggerFactory.getLogger(ScmApplication.class);


    @Override
    protected void doInit() throws Exception {
        applicationContext.getBean(ResourceService.class).load();
    }
}
