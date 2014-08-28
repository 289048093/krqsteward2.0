package com.ejushang.steward.customercenter.service;

import Vo.CommentVo;
import Vo.ScaleVo;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.customercenter.constant.CommentResult;
import com.ejushang.steward.customercenter.domain.*;
import com.ejushang.steward.ordercenter.returnvisit.ReturnVisitManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.CustomerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User:moon
 * Date: 14-8-7
 * Time: 下午1:58
 */
@Service
@Transactional
public class CommentService {

    private Logger log= LoggerFactory.getLogger(getClass());

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerTagService customerTagService;

    public void saveComment(Comment comment){
        generalDAO.saveOrUpdate(comment);
    }

    /**
     * 评价列表
     * @param map
     * @param page
     * @return
     */
    public Page findAllComment(Map<String, Object[]> map,Page page){

        Map<String, String> commentMapConditions = new HashMap<String, String>();
        //将接受到的map参数解析并赋值给map1
        CustomerUtil.getConditionMap(map, commentMapConditions);
        //HQL条件的值
        List<Object> objects = new ArrayList<Object>();
        //拼接HQL的变量
        StringBuilder stringBuilder = new StringBuilder();
        //拼接HQL的方法
        CustomerUtil.commentCondition(commentMapConditions, stringBuilder, objects);
        //执行HQL
        List<Comment> comments = generalDAO.query(stringBuilder.toString(), page, objects.toArray());
        List<CommentVo> commentVos=new ArrayList<CommentVo>();
        for(Comment comment:comments){
            CommentVo commentVo=new CommentVo();
            commentVo.setComment(comment);
            Customer customer=customerService.getCustomerByMobile(comment.getMobile());
            if(customer==null){
                commentVo.setCustomerTags(null);
            }else{
                commentVo.setCustomerTags(customer.getTags());
            }
            commentVos.add(commentVo);
        }
        page.setResult(commentVos);
        return page;
    }

    //查询出评价结果在查询结果中的比例
    public int find(Map<String, Object[]> map,CommentResult result){
        Map<String, String> findCommentMapConditions = new HashMap<String, String>();
        //将接受到的map参数解析并赋值给map1
        CustomerUtil.getConditionMap(map, findCommentMapConditions);
        //HQL条件的值
        List<Object> objects = new ArrayList<Object>();

        StringBuilder hql=new StringBuilder();
        hql.append("select count(c.id) from Comment c where 1=1 and c.result=? ");
        objects.add(result);
        hql.append(" and c.id in ( ");
        CustomerUtil.commentCondition1(hql);
        CustomerUtil.addCommentCondition(findCommentMapConditions, hql, objects);
        hql.append(" ) ");
        return generalDAO.count(hql.toString(),objects.toArray());
    }

    public List<ScaleVo> getResultScale(Map<String, Object[]> map){
        List<ScaleVo> scaleVos=new ArrayList<ScaleVo>();
        ScaleVo scaleVo=new ScaleVo();
        scaleVo.setName(CommentResult.BAD.getValue());
        scaleVo.setData(find(map, CommentResult.BAD));
        scaleVos.add(scaleVo);

        ScaleVo scaleVo1=new ScaleVo();
        scaleVo1.setName(CommentResult.NEUTRAL.getValue());
        scaleVo1.setData(find(map,CommentResult.NEUTRAL));
        scaleVos.add(scaleVo1);

        ScaleVo scaleVo2=new ScaleVo();
        scaleVo2.setName(CommentResult.GOOD.getValue());
        scaleVo2.setData(find(map,CommentResult.GOOD));
        scaleVos.add(scaleVo2);

        return scaleVos;
    }

    //查询出分类结果在查询结果中的比例
    public int findCategory(Map<String, Object[]> map,Integer categoryId){
        Map<String, String> findCommentMapConditions = new HashMap<String, String>();
        //将接受到的map参数解析并赋值给map1
        CustomerUtil.getConditionMap(map, findCommentMapConditions);
        //HQL条件的值
        List<Object> objects = new ArrayList<Object>();

        StringBuilder hql=new StringBuilder();
        hql.append("select count(c.id) from Comment c where 1=1 and c.id in (select cc.commentId from CommentCommentCategory cc where cc.categoryId=? )  ");
        objects.add(categoryId);
        hql.append(" and c.id in ( ");
        CustomerUtil.commentCondition1(hql);
        CustomerUtil.addCommentCondition(findCommentMapConditions, hql, objects);
        hql.append(" ) ");
        return generalDAO.count(hql.toString(),objects.toArray());
    }

    public List<ScaleVo> getCategoryScale(Map<String, Object[]> map){
        List<ScaleVo> scaleVos=new ArrayList<ScaleVo>();
        List<CommentCategory> commentCategories=findAllCommentCategory();
        for(CommentCategory commentCategory:commentCategories){
            ScaleVo scaleVo=new ScaleVo();
            scaleVo.setName(commentCategory.getName());
            scaleVo.setData(findCategory(map,commentCategory.getId()));
            scaleVos.add(scaleVo);
        }
        return scaleVos;
    }
    /**
     * 根据评价结果查询评价
     * @param result
     * @return
     */
    public List<Comment> findCommentByResult(CommentResult result){
        Search search=new Search(Comment.class);
        if(result!=null){
            search.addFilterEqual("result",result);
        }
        return generalDAO.search(search);
    }

    /**
     * 根据评价分类查询评价
     * @param categoryId
     * @return
     */
    public List<Comment> findCommentByCategoryId(Integer categoryId){
        StringBuilder hql=new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        hql.append("select c from Comment c where 1=1");
        if(!NumberUtil.isNullOrZero(categoryId)){
            hql.append(" and c.id in (select cc.commentId from CommentCommentCategory cc where cc.categoryId=? ) ");
            params.add(categoryId);
        }
        return generalDAO.query(hql.toString(),null,params.toArray());
    }

    /**
     * 根据id得到comment
     * @param id
     * @return
     */
    public Comment getComment(Integer id){
        return generalDAO.get(Comment.class,id);
    }

    /**
     * 给评论归类
     * @param commentId
     * @param categoryIds
     * @param isToVisit   是否转回访
     */
    public void categoryToComment(Integer commentId,Integer[] categoryIds,Boolean isToVisit){

        Comment comment=getComment(commentId);

        if(comment==null){
            log.error("No comment found from database by commentId:"+commentId);
            return;
        }

        if(categoryIds!=null){
            generalDAO.getSession().createQuery("delete from CommentCommentCategory where commentId=" + commentId).executeUpdate();
            for(Integer categoryId:categoryIds){
                saveCommentCommentCategory(commentId,categoryId);
            }
            //给评论归类后，设置 category为true
            comment.setCategory(true);
            saveComment(comment);
        }

        if(isToVisit!=null){
            if(isToVisit){
                //转入回访
                ReturnVisitManager.fromNegativeComment(commentId,comment.getOrderId());
            }
        }

    }


    /**
     * 保存评论分类中间表
     * @param commentId
     * @param categoryId
     */
    public void saveCommentCommentCategory(Integer commentId,Integer categoryId){
        CommentCommentCategory commentCommentCategory=new CommentCommentCategory();
        commentCommentCategory.setCommentId(commentId);
        commentCommentCategory.setCategoryId(categoryId);
        generalDAO.saveOrUpdate(commentCommentCategory);
    }

    /**
     *
     * @return
     */
    public List<CommentCategory>  findAllCommentCategory(){
        Search search=new Search(CommentCategory.class);
        return generalDAO.search(search);
    }

    /**
     * 添加标签到会员            评价列表
     * @param mobile
     * @param tagIds
     */
    public void addTagInCommentToCustomer(String mobile,Integer[] tagIds){
        Customer customer=customerService.getCustomerByMobile(mobile);
        if(customer==null){
            throw new StewardBusinessException(String.format("根据手机号：[%s]查询不到会员。",mobile));
        }
        for(Integer tagId:tagIds){
            customerTagService.saveCustomerCustomerTag(customer.getId(),tagId);
        }
    }

    /**
     * 批量给会员添加标签
     * @param mobiles
     * @param tagIds
     */
    public void addTagInCommentToCustomers(String[] mobiles,Integer[] tagIds){
        for(String mobile:mobiles){
            addTagInCommentToCustomer(mobile,tagIds);
        }
    }

}
