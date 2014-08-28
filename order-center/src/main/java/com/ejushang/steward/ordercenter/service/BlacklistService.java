package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.constant.BlacklistType;
import com.ejushang.steward.ordercenter.domain.Blacklist;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User:moon
 * Date: 14-8-6
 * Time: 上午11:35
 */
@Service
@Transactional
public class BlacklistService {

    private static final Logger log= LoggerFactory.getLogger(BlacklistService.class);

    @Autowired
    private GeneralDAO generalDAO;

    /**
     *       根据电话黑名单类型查询黑名单数据
     * @param phone
     * @return
     */
    @Transactional(readOnly = true)
    public Blacklist getPhoneBlacklistByPhone(String phone){
        return (Blacklist) this.generalDAO.searchUnique(
                new Search(Blacklist.class)
                        .addFilterEqual("type", BlacklistType.PHONE)
                        .addFilterEqual("value", phone));
    }

    /**
     * 查询黑名单分页
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<Blacklist> findAllBlacklist(Blacklist blacklist,Page page){
        Search search=new Search(Blacklist.class);
        if(blacklist!=null){
            if(blacklist.getType()!=null){
                search.addFilterEqual("type",blacklist.getType());
            }
            if(!StringUtils.isBlank(blacklist.getValue())){
                search.addFilterLike("value","%"+blacklist.getValue()+"%");
            }
        }
        search.addPagination(page).addSortDesc("createTime");
        return generalDAO.search(search);
    }

    /**
     * 删除黑名单
     * @param ids
     */
    public void delBlacklist(Integer[] ids){
        generalDAO.removeByIds(Blacklist.class,ids);
    }

    /**
     * 判断黑名单是否存在
     * @param blacklist
     * @return
     */
    @Transactional(readOnly = true)
    public  boolean existBlacklist(Blacklist blacklist){
        Search search=new Search(Blacklist.class);
        search.addFilterEqual("type",blacklist.getType()).addFilterEqual("value",blacklist.getValue());
        return generalDAO.count(search)>0;
    }

    public List<Blacklist> findBlacklistByTypeAndValue(BlacklistType type, String value){
        Search search=new Search(Blacklist.class);
        search.addFilterEqual("type",type).addFilterEqual("value",value);
        return generalDAO.search(search);
    }

    /**
     * 添加黑名单
     * @param blacklist
     */
    public void saveOrUpdateBlacklistIfNotExist(Blacklist blacklist) {
        if(blacklist==null||blacklist.getType()==null||StringUtils.isBlank(blacklist.getValue())){
            log.error("Some required params is null or empty to create blacklist");
            throw new StewardBusinessException("缺少必要参数");
        }

        List<Blacklist> list=this.findBlacklistByTypeAndValue(blacklist.getType(),blacklist.getValue());
        if(list.isEmpty()){
            blacklist.setId(null);
        }else if(NumberUtil.isNullOrLessThanOne(blacklist.getId())||list.size()>1||!list.get(0).getId().equals(blacklist.getId())){
            log.warn("blacklist existed[ type={},value={}]",blacklist.getType(),blacklist.getValue());
            return;
        }

        generalDAO.saveOrUpdate(blacklist);
    }
}
