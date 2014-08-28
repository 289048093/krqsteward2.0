package com.ejushang.steward.openapicenter.tb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: Baron.Zhang
 * Date: 13-12-24
 * Time: 下午1:20
 */
public class TbAppConfig extends Properties{
    private static final Logger log = LoggerFactory.getLogger(TbAppConfig.class);
    private static final String propFileName = "tb_openapi.properties";

    private static TbAppConfig conf= null;
    private static String configFile = null;
    /**
     * 私有构造函数
     *
     */
    private TbAppConfig(){

    }

    /**
     * 单例模式
     * @return
     */
    public static TbAppConfig getInstance(String configFileName){
        if (conf==null){
            conf  = new TbAppConfig();
            try{
                InputStream is =  Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);
                conf.load(is);
                is.close();
            } catch (IOException e) {
                log.error(configFileName+"文件读取失败！");
            }
        }
        return conf;
    }

    public static TbAppConfig getInstance(){
        return getInstance(propFileName);
    }


    /**
     * 存储到props文件的格式中
     */
    public void storeToProps(){
        FileOutputStream fos=null;
        try {
            fos = new FileOutputStream(configFile);
        } catch (FileNotFoundException e1) {
            log.error("文件未找到："+configFile+e1.toString());
        }

        try {
            conf.store(fos,"");
        }catch (FileNotFoundException e) {
            log.error("配置文件"+configFile+"找不到！！\n"+e.toString());
        }catch (Exception e) {
            log.error("读取配置文件"+configFile+"错误！！\n"+e.toString());
        }

    }


}
