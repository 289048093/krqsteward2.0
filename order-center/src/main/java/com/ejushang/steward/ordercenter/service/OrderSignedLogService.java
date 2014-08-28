package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.ordercenter.domain.OrderSignedLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: Shiro
 * Date: 14-8-25
 * Time: 上午11:04
 */
@Service
@Transactional
public class OrderSignedLogService {
    private static final Logger logger = LoggerFactory.getLogger(OrderSignedLogService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Transactional
    public void saveOrderSignedLog(OrderSignedLog orderSignedLog){
       generalDAO.saveOrUpdate(orderSignedLog);
    }

    @Transactional(readOnly = true)
    public List<OrderSignedLog> findAllUnProcessedOrderSignedLog() {
        Search search=new Search(OrderSignedLog.class);
        search.addFilterEqual("processed",true);
        return generalDAO.search(search);
    }

    @Transactional(readOnly = false)
    public void updateProcessedStatus(List<Integer> idList) {
        if(idList==null||idList.isEmpty()){
            return;
        }
        generalDAO.update("update OrderSignedLog set processed=1,updateTime=now() where id in("+ StringUtils.join(idList,",")+")");
    }
}
