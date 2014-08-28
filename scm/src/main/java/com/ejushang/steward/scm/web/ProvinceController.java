package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.ordercenter.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: tin
 * Date: 14-4-10
 * Time: 下午2:38
 */
@Controller
@RequestMapping("/province")
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;

    @RequestMapping("/findAll")
    @ResponseBody
    JsonResult findProvinceAll(){
     return new JsonResult(true).addList(provinceService.findProvinceAll());

    }

}
