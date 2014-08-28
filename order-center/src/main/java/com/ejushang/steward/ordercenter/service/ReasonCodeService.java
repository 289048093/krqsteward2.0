package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.ordercenter.domain.ReasonCode;
import com.ejushang.steward.ordercenter.domain.ReasonCodeCategory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by: codec.yang
 * Date: 2014/8/26 17:03
 */

@Service
@Transactional
public class ReasonCodeService {

    @Autowired
    private GeneralDAO generalDAO;


    @Transactional(readOnly = true)
    public ReasonCodeCategory getReasonCodeCategoryById(Integer categoryId){
        return generalDAO.get(ReasonCodeCategory.class,categoryId);
    }

    @Transactional(readOnly = true)
    public ReasonCode getReasonCodeById(Integer id){
        return generalDAO.get(ReasonCode.class,id);
    }


    @Transactional(readOnly = true)
    private boolean existCategory(ReasonCodeCategory category){
        Search search=new Search(ReasonCodeCategory.class);
        search.addFilterEqual("name",category.getName());
        search.addFilterEqual("level",category.getLevel());
        if(!NumberUtil.isNullOrLessThanOne(category.getId())){
            search.addFilterNotEqual("id",category.getId());
        }
        return generalDAO.count(search)>0;
    }

    public void saveOrUpdateReasonCodeCategory(ReasonCodeCategory category) {

        if(this.existCategory(category)){
            throw new StewardBusinessException("目录名称已存在");
        }

        if(category.getLevel()==ReasonCodeCategory.LEVEL_2){
            if(category.getParentId()==null||this.getReasonCodeCategoryById(category.getParentId())==null){
                throw new StewardBusinessException("父目录不允许为空");
            }
        }

        generalDAO.saveOrUpdate(category);

    }

    @Transactional(readOnly = true)
    public ReasonCode getReasonCodeByCode(String code){
        Search search=new Search(ReasonCode.class);
        search.addFilterEqual("code",code);
        search.addFilterNotEqual("deleted", true);
        return (ReasonCode) generalDAO.searchUnique(search);
    }

    public void saveOrUpdateReasonCode(ReasonCode reasonCode) {
        ReasonCode rc=this.getReasonCodeByCode(reasonCode.getCode());
        if(rc!=null&&(reasonCode.getId()==null||!rc.getId().equals(reasonCode.getId()))){
            throw new StewardBusinessException("原因码已存在");
        }

        if(this.getReasonCodeCategoryById(reasonCode.getFirstLevelCategoryId())==null){
            throw new StewardBusinessException("一级类目不存在");
        }

        if(this.getReasonCodeCategoryById(reasonCode.getSecondLevelCategoryId())==null){
            throw new StewardBusinessException("二级类目不存在");
        }


        generalDAO.saveOrUpdate(reasonCode);
    }

    @Transactional(readOnly = true)
    public List<ReasonCodeCategory> findAllCategoryByLevel(int level) {
        Search search=new Search(ReasonCodeCategory.class);
        search.addFilterEqual("level", level);
        return generalDAO.search(search);
    }

    public void deleteReasonCodeById(Integer[] ids) {
        if(ids==null||ids.length==0){
            return;
        }
        this.generalDAO.update("update ReasonCode set deleted=1 where id in("+ StringUtils.join(ids,",")+")");
    }

    @Transactional(readOnly = true)
    public List<ReasonCode> findAllReasonCode(Integer firstLevelCategoryId,Integer secondLevelCategoryId) {
        Search search=new Search(ReasonCode.class);
        search.addFilterNotEqual("deleted",true);
        if(!NumberUtil.isNullOrLessThanOne(firstLevelCategoryId)){
            search.addFilterEqual("firstLevelCategoryId",firstLevelCategoryId);
        }

        if(!NumberUtil.isNullOrLessThanOne(secondLevelCategoryId)){
            search.addFilterEqual("secondLevelCategoryId",secondLevelCategoryId);
        }

        return generalDAO.search(search);
    }



    public void deleteReasonCategory(List<Integer> ids, int level) {
        if(level==ReasonCodeCategory.LEVEL_1&&this.hasChildren(ids)){
            throw new StewardBusinessException("所选类目中存在子类目，不允许删除");
        }else if(level==ReasonCodeCategory.LEVEL_2&&this.hasReasonCodeInCategory(ids)){
            throw new StewardBusinessException("所选类目中存在原因码，不允许删除");
        }

        for(Integer id:ids) {
            generalDAO.removeByIds(ReasonCodeCategory.class, id);
        }

    }

    private boolean hasChildren(List<Integer> parentIdList){
        Search search=new Search(ReasonCodeCategory.class);
        search.addFilterIn("parentId", parentIdList);
        return generalDAO.count(search)>0;
    }

    private boolean hasReasonCodeInCategory(List<Integer> ids) {
        Search search=new Search(ReasonCode.class);
        search.addFilterNotEqual("deleted",true);
        search.addFilterIn("secondLevelCategoryId", ids);
        return generalDAO.count(search)>0;
    }

    @Transactional(readOnly = true)
    public List<ReasonCode> findReasonCodeByCodeIn(Collection<String> codes) {
        Search search=new Search(ReasonCode.class);
        search.addFilterNotEqual("deleted",true);
        search.addFilterIn("code", codes);
        return generalDAO.search(search);
    }
}
