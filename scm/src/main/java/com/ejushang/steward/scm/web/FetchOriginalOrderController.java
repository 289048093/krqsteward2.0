package com.ejushang.steward.scm.web;

import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Baron.Zhang
 * Date: 2014/7/4
 * Time: 20:44
 */
@Controller
@RequestMapping("/np")
public class FetchOriginalOrderController {



    @RequestMapping("/foo")
    public String fetchOriginalOrder(HttpServletResponse response){
        List<OriginalOrder> originalOrderList = new ArrayList<OriginalOrder>();
        //originalOrderList.add();

        return null;
    }

    /**
     * 构造
     * @param id
     * @param platformOrderNo
     * @return
     */
    private OriginalOrder getOriginalOrder(Integer id, String platformOrderNo){
        OriginalOrder originalOrder = new OriginalOrder();
        originalOrder.setId(id);
        originalOrder.setPlatformOrderNo(platformOrderNo);
        return originalOrder;
    }

}
