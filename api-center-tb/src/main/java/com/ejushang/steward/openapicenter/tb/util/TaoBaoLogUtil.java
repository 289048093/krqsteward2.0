package com.ejushang.steward.openapicenter.tb.util;

import com.taobao.api.TaobaoResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Baron.Zhang
 * Date: 14-1-16
 * Time: 下午4:19
 */
public final class TaoBaoLogUtil {
    /** 根日志(最常用的方式 LogFactory.getLog(XXX.class) 就是用的 root) */
    public static final Log TAOBAOAPILOG = LogFactory.getLog("taobaoApiLog");

    /**
     * 打印淘宝APIresponse日志
     * @param response
     */
    public static void logTaoBaoApi(TaobaoResponse response){
        if(TAOBAOAPILOG.isInfoEnabled()){
            TAOBAOAPILOG.info("errorCode:"+response.getErrorCode()+","+
                                "msg:"+response.getMsg()+","+
                                "subCode:"+response.getSubCode()+","+
                                "subMsg:"+response.getSubMsg()+","+
                                "body:"+response.getBody()+","+
                                "params:"+response.getParams());
        }
    }
}
