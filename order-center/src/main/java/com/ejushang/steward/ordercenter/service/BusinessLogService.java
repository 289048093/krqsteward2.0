package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.BusinessLog;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.EJSDateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * User:moon
 * Date: 14-4-9
 * Time: 下午5:40
 */
@Service
@Transactional
public class BusinessLogService {

    private static final Logger log= LoggerFactory.getLogger(BusinessLogService.class);

    @Autowired
    private GeneralDAO generalDAO;

    public void saveBusinessLog(BusinessLog businessLog){

        generalDAO.saveOrUpdate(businessLog);

    }

    @Transactional(readOnly = true)
    public BusinessLog getBusinessLog(Integer id){

        return generalDAO.get(BusinessLog.class,id);
    }

    @Transactional(readOnly = true)
    public List<BusinessLog> findAllBusinessLog(String paramType,String param,String startDate,String endDate,Page page){
        if(log.isInfoEnabled()){
            log.info("findAllBusinessLog接收到的paramType为：[{}],param为：[{}]",paramType,param);
        }
        Search search=new Search(BusinessLog.class);
        if(!StringUtils.isBlank(paramType)){
            //paramType为operatorName 根据操作用户的名字查
            if(paramType.equals("operatorName") && !StringUtils.isBlank(param)){
                search.addFilterLike("operatorName","%"+param+"%");
            }
            //paramType为operationName 根据操作的名字查
            else if(paramType.equals("operationName") && !StringUtils.isBlank(param)){
                search.addFilterLike("operationName","%"+param+"%");
            }
        }
        if(!StringUtils.isBlank(startDate)){
                search.addFilterGreaterOrEqual("createTime", EJSDateUtils.parseDate(startDate, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        if(!StringUtils.isBlank(endDate)){
            search.addFilterLessOrEqual("createTime",EJSDateUtils.parseDate(endDate, EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        }
        search.addSortDesc("createTime").addPagination(page);
        return generalDAO.search(search);
    }

}
