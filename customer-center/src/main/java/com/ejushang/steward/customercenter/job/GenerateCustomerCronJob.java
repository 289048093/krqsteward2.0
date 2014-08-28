package com.ejushang.steward.customercenter.job;

import com.ejushang.steward.customercenter.domain.Customer;
import com.ejushang.steward.ordercenter.domain.MemberInfoLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: codec.yang
 * Date: 2014/8/5 17:57
 */
@Component
public class GenerateCustomerCronJob {

    private static final Logger log= LoggerFactory.getLogger(GenerateCustomerCronJob.class);
    private int fetchSizePerQuery=10000;

    @Autowired
    private GenerateCustomerService generateCustomerService;



    public void run(){

        if(log.isInfoEnabled()){
            log.info("Generate customer cron job begin...");
        }

        while(true) {
            List<MemberInfoLog> list = generateCustomerService.findUnProcessedMemberInfoLog(fetchSizePerQuery);

            if(log.isInfoEnabled()) {
                log.info("Size of MemberInfoLog list:"+list.size());
            }

            process(list);

            //No data to fetch, exit.
            if(list.size()<fetchSizePerQuery){
                break;
            }
        }

       log.info("Generate customer cron job end!!!!!!!!!");
    }

    private void process(List<MemberInfoLog> memberInfoLogList){
        int maxSizePerTime=1000;
        int offset=0;
        int to=maxSizePerTime;
        for(;;){
            if(offset>memberInfoLogList.size()-1){
                break;
            }
            if(to>memberInfoLogList.size()){
                to=memberInfoLogList.size();
            }
            this.generateCustomerService.generateCustomer(memberInfoLogList.subList(offset,to));
            offset=to;
            to=offset+maxSizePerTime;

        }

    }

}
