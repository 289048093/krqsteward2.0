package com.ejushang.steward.common;

import com.ejushang.uams.exception.UamsClientException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * User: liubin
 * Date: 14-4-9
 */
public abstract class Application implements InitializingBean {
    public static enum PropertiesKey {
        /**
         * 京东页面链接
         */
        JD_ITEM_URI_PATTERN("jd_item_uri_pattern"),
        /**
         * 淘宝页面链接
         */
        TAOBAO_ITEM_URI_PATTERN("taobao_item_uri_pattern"),
        /**
         * 文件上传目录
         */
        UPLOAD_DIR("paths");
        public String value;

        PropertiesKey(String value) {
            this.value = value;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    protected ApplicationContext applicationContext;

    private static Application INSTANCE;

    public static Application getInstance() {
        return INSTANCE;
    }


    //config.properties的键值对
    private final Map<String, String> configProperties = new HashMap<String, String>();


    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    public static Object getBean(String name) {
        return getInstance().getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> classz) {
        return getInstance().getApplicationContext().getBean(classz);
    }


    public synchronized void init() {

        InputStream is = null;
        try {
            //初始化config.properties
            configProperties.clear();
            Properties props = new Properties();
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
            if (is != null) {
                props.load(is);
                for (Iterator<Map.Entry<Object, Object>> iter = props.entrySet().iterator(); iter.hasNext(); ) {
                    Map.Entry<Object, Object> entry = iter.next();
                    configProperties.put((String) entry.getKey(), (String) entry.getValue());
                }
            }

            doInit();

            INSTANCE = this;

            log.info("系统初始化成功");

        } catch (Exception e) {
            log.error("系统初始化的时候出错", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("系统初始化的时候出错", e);
                }
                ;
            }
        }

    }

    protected abstract void doInit() throws Exception;

    public Map<String, String> getConfigProperties() {
        return configProperties;
    }

    public String getConfigValue(String key) {
        return configProperties.get(key);
    }

    public boolean getBooleanConfigValue(String key) {
        return Boolean.parseBoolean(getConfigValue(key));
    }


    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
