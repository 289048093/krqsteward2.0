package com.ejushang.steward.customercenter.service;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.customercenter.domain.Customer;
import com.ejushang.steward.customercenter.domain.CustomerCustomerTag;
import com.ejushang.steward.customercenter.domain.CustomerTag;
import org.apache.commons.lang3.StringUtils;
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
public class CustomerTagService {

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private CustomerService customerService;

    /**
     * 添加标签
     * @param
     */
    public void saveTag(CustomerTag customerTag){
        if(customerTag!=null){
            CustomerTag customerTag1=new CustomerTag();
            if(NumberUtil.isNullOrZero(customerTag.getId())){
                 customerTag1=findTagByName(customerTag.getName());
            } else {
                 customerTag1=findTag(customerTag.getId(),customerTag.getName());
            }
            if(customerTag1!=null){
                throw  new StewardBusinessException(String.format("已经存在标签名为[%s]的标签，不能重复添加！",customerTag.getName()));
            }
        }
        generalDAO.saveOrUpdate(customerTag);
    }

    /**
     * 查询标签分页
     * @param name
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<CustomerTag> findCustomerTag(String name,Page page){
        Search search=new Search(CustomerTag.class);
        if(!StringUtils.isBlank(name)){
            search.addFilterLike("name","%"+name+"%");
        }
        search.addPagination(page).addSortDesc("createTime");
        return generalDAO.search(search);
    }

    /**
     * 查询标签
     * @return
     */
    @Transactional(readOnly = true)
    public List<CustomerTag> findAllCustomerTagNoPage(){
        Search search=new Search(CustomerTag.class);
        search.addSortDesc("createTime");
        return generalDAO.search(search);
    }

    /**
     * 根据id查询标签
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public CustomerTag findTagById(Integer id){
        return generalDAO.get(CustomerTag.class,id);
    }

    /**
     * 根据name查询标签
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public CustomerTag findTagByName(String name){
        Search search=new Search(CustomerTag.class);
        search.addFilterEqual("name",name);
        return (CustomerTag)generalDAO.searchUnique(search);
    }

    /**
     * 根据name查询标签
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public CustomerTag findTag(Integer id,String name){
        Search search=new Search(CustomerTag.class);
        search.addFilterEqual("name",name);
        search.addFilterNotEqual("id",id);
        return (CustomerTag)generalDAO.searchUnique(search);
    }

    /**
     * 批量删除标签，并且会员中包含该标签的也会被删除
     * @param ids
     */
    public void deleteTag(Integer[] ids){
        for(Integer id :ids){
            List<CustomerCustomerTag> customerCustomerTags= findCustomerCustomerTag(null, id);
            delCustomerCustomerTag(customerCustomerTags);
            generalDAO.removeById(CustomerTag.class,id);
        }
    }

    /**
     * 根据会员id和标签id查询中间表
     * @param tagId
     * @return
     */
    @Transactional(readOnly = true)
    public List<CustomerCustomerTag> findCustomerCustomerTag(Integer customerId, Integer tagId){
        Search search=new Search(CustomerCustomerTag.class);
        if(!NumberUtil.isNullOrZero(customerId)){
            search.addFilterEqual("customerId",customerId);
        }
        if(!NumberUtil.isNullOrZero(tagId)){
            search.addFilterEqual("tagId",tagId);
        }
        return generalDAO.search(search);
    }

    /**
     * 删除中间表的数据
     * @param customerCustomerTags
     */
    public void delCustomerCustomerTag(List<CustomerCustomerTag> customerCustomerTags){
        for(CustomerCustomerTag customerCustomerTag:customerCustomerTags){
            generalDAO.remove(customerCustomerTag);
        }
    }

    /**
     * 保存中间表的数据
     * @param customerId
     * @param tagId
     */
    public void saveCustomerCustomerTag(Integer customerId,Integer tagId){
        if(isExist(customerId,tagId)){
            Customer customer=customerService.getCustomer(customerId);
            CustomerTag customerTag=findTagById(tagId);
            throw new StewardBusinessException(String.format("会员[%s]已经拥有[%s]标签，不能重复添加！",customer.getRealName(),customerTag.getName()));
        }
        CustomerCustomerTag customerCustomerTag=new CustomerCustomerTag();
        customerCustomerTag.setCustomerId(customerId);
        customerCustomerTag.setTagId(tagId);
        generalDAO.saveOrUpdate(customerCustomerTag);
    }

    @Transactional(readOnly = true)
    public Boolean isExist(Integer customerId,Integer tagId){

        Search search=new Search(CustomerCustomerTag.class);
        search.addFilterEqual("customerId",customerId);
        search.addFilterEqual("tagId",tagId);
        return generalDAO.count(search)>0;
    }

}
