package com.ejushang.steward.scm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.ServletContextResource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * User: Blomer
 * Date: 14-4-10
 * Time: 上午10:28
 */
public class WebUtil {

    private static final Logger log = LoggerFactory.getLogger(WebUtil.class);

    /**
     * 取得当前的WebContext的路径.例如:d:/tomcat 5.0/webapps/caakee
     *
     * @return
     */
    public static String getWebAppPath() {
        String rs = "";
//        /** 得到解析类SystemConfig的URL绝对路径 **/
//        URL configUrl = clazz.getClassLoader().getResource("");
//        /** 转换为URI，这是关键，URI处理了空格，中文等问题 **/
//        URI uri = configUrl.toURI();
//        /** 以下就简单了，用文件方法得到绝对String路径 **/
//        String absolutePath = new File(uri).getAbsolutePath();
        try {
//            if (clazz.getResource("/")==null){
//            	return "/";
//            }

//            String absolutePath = URLDecoder.decode(clazz.getResource("/").getPath(), "UTF-8");
//            if (absolutePath!=null){
//                if (absolutePath.indexOf("WEB-INF")>-1){
//                    rs = absolutePath.substring(0,absolutePath.indexOf("WEB-INF")-1);
//                }
//            }
            rs = new ServletContextResource(ContextLoader.getCurrentWebApplicationContext().getServletContext(), "/").getFile().getAbsolutePath();

        } catch (Exception e) {
            log.error("获取webRoot路径错误", e);
        }
        return rs;
    }

}
