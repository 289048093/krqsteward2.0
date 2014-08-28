package com.ejushang.steward.common.springmvc;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.exception.StewardException;
import com.ejushang.steward.common.util.BusinessLogUtil;
import com.ejushang.steward.common.util.OperationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * User: liubin
 * Date: 14-2-7
 *
 * springmvc异常信息记录类
 *
 */
public class CustomMappingExceptionResolver extends SimpleMappingExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 记录错误日志
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
                                              Object handler, Exception ex){

        logger.debug("进入统一的错误处理:" + ex.getMessage());

        response.setStatus(997); //系统错误的错误吗
        HandlerMethod maControl = (HandlerMethod) handler;
        Method method = maControl.getMethod();
        if(method.isAnnotationPresent(OperationLog.class)){
            BusinessLogUtil.bindExceptionMessage(ex.getMessage());
        }
        String msg = "系统发生错误,请联系管理员";
        if(ex instanceof StewardBusinessException) {
            msg = ex.getMessage();
        } else if(ex instanceof StewardException) {
            msg = ex.getMessage();
        } else {
            //未知错误
            logger.error(ex.getMessage(), ex);
        }

        ModelAndView mav = new ModelAndView();
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("success", false);
        attributes.put("msg", msg);
        view.setAttributesMap(attributes);
        mav.setView(view);
        return mav;

    }


}