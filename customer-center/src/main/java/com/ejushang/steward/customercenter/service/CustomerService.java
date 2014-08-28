package com.ejushang.steward.customercenter.service;

import Vo.CustomerVo;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.customercenter.domain.Customer;
import com.ejushang.steward.customercenter.domain.CustomerMobile;
import com.ejushang.steward.customercenter.domain.UpdateCustomer;
import com.ejushang.steward.ordercenter.constant.BlacklistType;
import com.ejushang.steward.ordercenter.domain.AfterSales;
import com.ejushang.steward.ordercenter.domain.Blacklist;
import com.ejushang.steward.ordercenter.domain.Order;
import com.ejushang.steward.ordercenter.domain.ReturnVisitTask;
import com.ejushang.steward.ordercenter.service.BlacklistService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.CustomerUtil;

import java.util.*;

/**
 * User:moon
 * Date: 14-8-5
 * Time: 下午5:22
 */
@Service
@Transactional
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private CustomerTagService customerTagService;

    @Autowired
    private BlacklistService blacklistService;

    /**
     * 保存会员
     * @param customer
     */
    public void saveCustomer(Customer customer){

        generalDAO.saveOrUpdate(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(Integer id){
       return generalDAO.get(Customer.class,id);
    }

    /**
     * 修改会员信息
     */
    public void updateCustomer(UpdateCustomer updateCustomer,Integer[] tagIds,String mobile){

        Customer customer=getCustomer(updateCustomer.getId());
        if(StringUtils.isNotBlank(mobile)){
            for(CustomerMobile cm:customer.getMobiles()) {
                this.generalDAO.remove(cm);
            }
            customer.getMobiles().clear();
            customer.addMobileIfNotExist(mobile);
        }

        if(!StringUtils.isBlank(updateCustomer.getAddress())){
            customer.setAddress(updateCustomer.getAddress());
        }
        if(!StringUtils.isBlank(updateCustomer.getEmail())){
            customer.setEmail(updateCustomer.getEmail());
        }
        if (!StringUtils.isBlank(updateCustomer.getPhone())){
            customer.setPhone(updateCustomer.getPhone());
        }
        if(!StringUtils.isBlank(updateCustomer.getRealName())){
            customer.setRealName(updateCustomer.getRealName());
        }
        if(updateCustomer.getBirthday()!=null){
            customer.setBirthday(updateCustomer.getBirthday());
        }
        generalDAO.saveOrUpdate(customer);

        //删除该会员拥有的所有标签
       // generalDAO.getSession().createQuery("delete from CustomerCustomerTag where customerId=" + customer.getId()).executeUpdate();

        //给会员添加标签
        addTagToCustomer(customer.getId(),tagIds);
    }

    /**
     * 会员列表
     * @param map
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public Page findAllCustomer(Map<String, Object[]> map,Page page){
        Map<String, String> customerMapConditions = new HashMap<String, String>();
        //将接受到的map参数解析并赋值给map1
        CustomerUtil.getConditionMap(map, customerMapConditions);
        //显示层的Vo的List
        List<Customer> orderVos = new ArrayList<Customer>();
        //HQL条件的值
        List<Object> objects = new ArrayList<Object>();
        //拼接HQL的变量
        StringBuilder stringBuilder = new StringBuilder();
        //拼接HQL的方法
        CustomerUtil.customerCondition(customerMapConditions, stringBuilder, objects);
        //执行HQL
        List<Customer> customers = generalDAO.query(stringBuilder.toString(), page, objects.toArray());
        page.setResult(customers);
        return page;
    }

    /**
     * 给会员添加标签
     * @param customerId
     * @param tagIds
     */
    public void addTagToCustomer(Integer customerId,Integer[] tagIds){
        //删除该会员拥有的所有标签
        generalDAO.getSession().createQuery("delete from CustomerCustomerTag where customerId=" + customerId).executeUpdate();

        for(Integer tagId:tagIds){
            customerTagService.saveCustomerCustomerTag(customerId,tagId);
        }
    }

    /**
     * 批量给会员添加标签
     * @param customerIds
     * @param tagIds
     */
    public void addTagToCustomers(Integer[] customerIds,Integer[] tagIds){
        for(Integer customerId:customerIds){
            for(Integer tagId:tagIds){
                customerTagService.saveCustomerCustomerTag(customerId,tagId);
            }
        }
    }

    @Transactional(readOnly = true)
    public Customer getCustomerByMobile(String mobile) {
        return (Customer) this.generalDAO.searchUnique(new Search(Customer.class).addFilterEqual("mobiles.mobile",mobile));
    }

    //添加黑名单
    public void addCustomerToBlacklist(String mobile,BlacklistType type){
        Customer customer=getCustomerByMobile(mobile);
        Blacklist blacklist=new Blacklist();
        blacklist.setType(type);
        if(type.equals(BlacklistType.MAIL)){       //邮件黑名单
            blacklist.setValue(customer.getEmail());
        }else if(type.equals(BlacklistType.PHONE) || type.equals(BlacklistType.SMS)){       //电话黑名单    短信黑名单
            blacklist.setValue(mobile);
        }
        blacklistService.saveOrUpdateBlacklistIfNotExist(blacklist);
    }

    /**
     * 查看会员信息
     * @param mobile
     */
    public CustomerVo getCustomerDescription(String mobile){

        List<Order> orders=new ArrayList<Order>();
        orders=getOrderDescription(mobile);
        List<ReturnVisitTask> returnVisitTasks=new ArrayList<ReturnVisitTask>();
        returnVisitTasks=getReturnVisitTaskDescription(mobile);
        List<AfterSales> afterSaleses=new ArrayList<AfterSales>();
        afterSaleses=getAfterSalesDescription(mobile);
        CustomerVo customerVo=new CustomerVo();
        customerVo.setAfterSaleses(afterSaleses);
        customerVo.setReturnVisitTasks(returnVisitTasks);
        customerVo.setOrderList(orders);
        return customerVo;
    }

    //订单记录
    public List<Order> getOrderDescription(String mobile){
        Search search=new Search(Order.class);
        search.addFilterEqual("invoice.receiver.receiverMobile",mobile);
        return generalDAO.search(search);
    }
    //回访记录
    public List<ReturnVisitTask> getReturnVisitTaskDescription(String mobile){
        StringBuffer hql=new StringBuffer("select rvt from ReturnVisitTask rvt where 1=1 and rvt.orderId in");
        hql.append(" (select o.id from Order o where order.invoice.receiver.receiverMobile=? ) ");
        List<Object> objects=new ArrayList<Object>();
        objects.add(mobile);
        return generalDAO.query(hql.toString(), null, objects.toArray());
 //select rvt from ReturnVisitTask rvt where 1=1 and orderId in (select o.id from Order o where order.invoice.receiver.receiverMobile=?);
    }
    //售后记录
    public List<AfterSales> getAfterSalesDescription(String mobile){
        StringBuffer hql=new StringBuffer("select as from AfterSales as where 1=1 and as.orderId in");
        hql.append(" (select o.id from Order o where order.invoice.receiver.receiverMobile=? ) ");
        List<Object> objects=new ArrayList<Object>();
        objects.add(mobile);
        return generalDAO.query(hql.toString(),null,objects.toArray());
    }


}
