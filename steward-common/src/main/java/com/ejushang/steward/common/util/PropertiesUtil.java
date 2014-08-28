/**
 * Copyright  2013-6-17 第七大道-技术支持部-网站开发组
 * 自主运营平台WEB 下午2:44:04
 * 版本号： v1.0
 */

package com.ejushang.steward.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * <li>类描述：</li> <li>创建者： amos.zhou</li> <li>项目名称： platform-manage</li> <li>
 * 创建时间： 2013-6-17 下午2:44:04</li> <li>版本号： v1.0</li>
 */
public class PropertiesUtil {

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static Logger logger = Logger.getLogger(PropertiesUtil.class);

	private static PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	private static ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();

	/**
	 * 载入多个properties文件, 相同的属性在最后载入的文件中的值将会覆盖之前的载入. 文件路径使用Spring Resource格式,
	 * 文件编码使用UTF-8.
	 * 
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
	 */
	public static Properties loadProperties(String... resourcesPaths)
			throws IOException {
		Properties props = new Properties();

		for (String location : resourcesPaths) {
			logger.debug("Loading properties file from:" + location);
			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				propertiesPersister.load(props, new InputStreamReader(is,
						DEFAULT_ENCODING));
			} catch (IOException ex) {
				logger.info("Could not load properties from classpath:"
						+ location + ": " + ex.getMessage());
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}
		return props;
	}
}