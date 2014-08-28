package com.ejushang.steward.scm.common.web;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.springmvc.DateTypeEditor;
import com.ejushang.steward.common.util.NumberUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Date;

public class BaseController {

    protected void assertEntityExist(String message, Integer id, EntityClass<Integer> entity) {
        if(id == null) return;
        assertEntityExist(message, entity);
    }

    protected void assertEntityExist(String message, EntityClass<Integer> entity) {
        if(NumberUtil.isNullOrZero(entity.getId())) {
            throw new StewardBusinessException(message);
        }
    }

    @InitBinder
    private void dateBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateTypeEditor());
    }

}