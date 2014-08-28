package com.ejushang.steward.logisticscenter.service;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.logisticscenter.domain.LogisticsInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * User: Blomer
 * Date: 14-4-8
 * Time: 下午2:08
 */
@Service
public class LogisticsInfoService {

    private static final Logger log = LoggerFactory.getLogger(LogisticsInfoService.class);

    @Autowired
    private GeneralDAO generalDAO;

    public List<LogisticsInfo> list() {
        return null;
    }

    @Transactional
    public void save(LogisticsInfo logisticsInfo) {
        if (log.isInfoEnabled()) {
            log.info("LogisticsInfoService中的save方法,参数{}",logisticsInfo);
        }
        generalDAO.saveOrUpdate(logisticsInfo);
    }
}
