package com.ejushang.steward.customercenter.job;

import com.ejushang.steward.common.domain.Receiver;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.customercenter.domain.Customer;
import com.ejushang.steward.customercenter.domain.PlatformUser;
import com.ejushang.steward.customercenter.service.CustomerService;
import com.ejushang.steward.ordercenter.domain.MemberInfoLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by: codec.yang
 * Date: 2014/8/8 15:01
 */
@Service
@Transactional
public class GenerateCustomerService {

    private static final Logger log= LoggerFactory.getLogger(GenerateCustomerService.class);

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private CustomerService customerService;


    @Transactional(readOnly = true)
    public List<MemberInfoLog> findUnProcessedMemberInfoLog(int limit){
        Search search=new Search(MemberInfoLog.class);
        search.addFilterEqual("processed",false);
        search.addSort("id",false);
        if(limit>0){
           search.setMaxResults(limit);
        }

        return this.generalDAO.search(search);
    }


    @Transactional(readOnly = false)
    public void generateCustomer(List<MemberInfoLog> memberInfoLogs) {
        for(MemberInfoLog memberInfoLog:memberInfoLogs){
            try {
                this.analyze(memberInfoLog);
            }catch (Exception e){
                log.error("Exception occurred when generating customer",e);
            }
        }
    }

    @Transactional(readOnly = false)
    private void analyze(MemberInfoLog memberInfoLog){
        Receiver receiver=memberInfoLog.getReceiver();
        if(receiver==null|| StringUtils.isBlank(receiver.getReceiverMobile())){
            log.error("Receiver or mobile is null, MemberInfoLog id:"+memberInfoLog.getId());
            return;
        }

        Customer customer= customerService.getCustomerByMobile(receiver.getReceiverMobile());

        if(customer==null){
            customer=this.createCustomer(memberInfoLog);
        }

        switch (memberInfoLog.getOrderLogType()){
            case ORIGINAL:
                customer.increaseTradeCount();
                customer.setLastTradeTime(this.getNewerDateBetween(customer.getLastTradeTime(),memberInfoLog.getCreateTime()));
                break;
            case CONFIRMED:
                customer.increaseTotalTradeFee(memberInfoLog.getActualFee());
                if(memberInfoLog.getOffline()){
                    customer.increaseTradeCount();
                    customer.setLastTradeTime(this.getNewerDateBetween(customer.getLastTradeTime(),memberInfoLog.getCreateTime()));
                }
                break;
            case PAYMENT:
                customer.increaseTotalTradeFee(memberInfoLog.getPaymentFee());
                break;
            case REFUND:
                customer.decreaseTotalTradeFee(memberInfoLog.getRefundOrderFee());
                customer.decreaseTotalTradeFee(memberInfoLog.getRefundPaymentFee());
                break;
            default:
                log.error("Unknown order log type[{}], log id[{}]",memberInfoLog.getOrderLogType(),memberInfoLog.getId());
                return;
        }

        customer.addPlatformUserIfNotExist(memberInfoLog.getPlatformType(),memberInfoLog.getBuyerId());
        customer.addShopIfNotExist(memberInfoLog.getShopId());

        if(customer.getTotalTradeFee()==null){
            customer.setTotalTradeFee(Money.valueOf(0));
        }

        this.generalDAO.saveOrUpdate(customer);
        savePlatformUserForCustomer(customer);

        memberInfoLog.setProcessed(true);
        memberInfoLog.setUpdateTime(new Date());
        this.generalDAO.saveOrUpdate(memberInfoLog);
    }

    private void savePlatformUserForCustomer(Customer customer){
        for(PlatformUser user:customer.getPlatformUsers()){
            user.setCustomerId(customer.getId());
            this.generalDAO.saveOrUpdate(user);
        }

    }

    private Date getNewerDateBetween(Date date1,Date date2){
        if(date1==null){
            return date2;
        }

        if(date2==null){
            return date1;
        }

        return (date1.before(date2)? date2:date1);

    }

    private Customer createCustomer(MemberInfoLog memberInfoLog){
        Customer customer=new Customer();

        customer.setAddress(memberInfoLog.getReceiver().getReceiverAddress());
        customer.setState(memberInfoLog.getReceiver().getReceiverState());
        customer.setCity(memberInfoLog.getReceiver().getReceiverCity());
        customer.setDistrict(memberInfoLog.getReceiver().getReceiverDistrict());
        customer.setRealName(memberInfoLog.getReceiver().getReceiverName());
        customer.setPhone(memberInfoLog.getReceiver().getReceiverPhone());
        customer.addMobileIfNotExist(memberInfoLog.getReceiver().getReceiverMobile());

        return customer;
    }
}
