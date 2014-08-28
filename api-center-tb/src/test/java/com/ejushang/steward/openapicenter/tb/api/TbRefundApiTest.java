package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.exception.TaoBaoApiException;
import com.taobao.api.domain.RefundBill;
import com.taobao.api.domain.ReturnBill;
import com.taobao.api.response.TmallEaiOrderRefundGoodReturnMgetResponse;
import com.taobao.api.response.TmallEaiOrderRefundMgetResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.*;

/**
 * User: Baron.Zhang
 * Date: 2014/5/8
 * Time: 14:35
 */
public class TbRefundApiTest {

    private String sessionKey = "6201e1489617d8d2681cadfhf881e8c72b10ba3fa4c9a541675300784";

    @Test
    public void testTmallEaiOrderRefundMget() throws Exception {
        TbRefundApi refundApi = new TbRefundApi(sessionKey);

        // 构造参数
        Map<String,Object> argsMap = new HashMap<String, Object>();
        /*argsMap.put(ConstantTaoBao.STATUS, TbRefundStatus.WAIT_SELLER_AGREE.getValue()
                +","+TbRefundStatus.SELLER_REFUSE.getValue()
                +","+TbRefundStatus.GOODS_RETURNING.getValue()
                +","+TbRefundStatus.CLOSED.getValue()
                +","+TbRefundStatus.SUCCESS.getValue()
        );*/
        argsMap.put(ConstantTaoBao.START_TIME, EJSDateUtils.parseDate("2014-05-01 00:00:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        argsMap.put(ConstantTaoBao.END_TIME, EJSDateUtils.parseDate("2014-05-07 23:59:59", EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        argsMap.put(ConstantTaoBao.PAGE_NO,1L);
        argsMap.put(ConstantTaoBao.PAGE_SIZE,ConstantTaoBao.TB_FETCH_REFUND_PAGE_SIZE);
        //argsMap.put(ConstantTaoBao.USE_HAS_NEXT,Boolean.valueOf(true));

        // 执行查询
        TmallEaiOrderRefundMgetResponse response = refundApi.tmallEaiOrderRefundMget(argsMap);

        if(StringUtils.isNotBlank(response.getErrorCode())){
            throw new TaoBaoApiException("ErrorCode："+response.getErrorCode()+"，body：" + response.getBody());
        }
        // 符合条件的数据总条数
        Long totalCount = response.getTotalResults();
        Long pageSize = ConstantTaoBao.TB_FETCH_REFUND_PAGE_SIZE;
        Long pageNo = totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;

        List<RefundBill> refundBillList = new ArrayList<RefundBill>();
        for(long i = 1; i <= pageNo; i++){
            argsMap.put(ConstantTaoBao.PAGE_NO,i);
            response = refundApi.tmallEaiOrderRefundMget(argsMap);
            if(StringUtils.isNotBlank(response.getErrorCode())){
                throw new TaoBaoApiException("ErrorCode："+response.getErrorCode()+"，body：" + response.getBody());
            }
            if(CollectionUtils.isNotEmpty(response.getRefundBillList())) {
                refundBillList.addAll(response.getRefundBillList());
            }
        }

        Collections.sort(refundBillList,new Comparator<RefundBill>() {
            @Override
            public int compare(RefundBill o1, RefundBill o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });

        for(RefundBill refundBill : refundBillList){
            if(StringUtils.equalsIgnoreCase(refundBill.getRefundPhase(), "AFTERSALE")){
                System.out.println("退款单号：" + refundBill.getRefundId()+"，创建时间："+refundBill.getCreated()+"，更新时间："+refundBill.getModified());
            }
        }

        System.out.println("总条数："+totalCount+"=="+refundBillList.size());
    }

    @Test
    public void testTmallEaiOrderRefundGoodReturnMget() throws Exception {
        TbRefundApi refundApi = new TbRefundApi(sessionKey);

        Map<String,Object> argsMap = new HashMap<String, Object>();
        argsMap.put(ConstantTaoBao.START_TIME, EJSDateUtils.parseDate("2014-05-01 00:00:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        argsMap.put(ConstantTaoBao.END_TIME, EJSDateUtils.parseDate("2014-05-07 23:59:59", EJSDateUtils.DateFormatType.DATE_FORMAT_STR));
        argsMap.put(ConstantTaoBao.PAGE_NO,1L);
        argsMap.put(ConstantTaoBao.PAGE_SIZE,ConstantTaoBao.TB_FETCH_REFUND_PAGE_SIZE);

        TmallEaiOrderRefundGoodReturnMgetResponse response = refundApi.tmallEaiOrderRefundGoodReturnMget(argsMap);

        if(StringUtils.isNotBlank(response.getErrorCode())){
            throw new TaoBaoApiException("ErrorCode："+response.getErrorCode()+"，body：" + response.getBody());
        }

        // 符合条件的数据总条数
        Long totalCount = response.getTotalResults();
        Long pageSize = ConstantTaoBao.TB_FETCH_REFUND_PAGE_SIZE;
        Long pageNo = totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;

        List<ReturnBill> returnBillList = new ArrayList<ReturnBill>();
        for(long i = 1; i <= pageNo; i++){
            argsMap.put(ConstantTaoBao.PAGE_NO,i);
            response = refundApi.tmallEaiOrderRefundGoodReturnMget(argsMap);
            if(StringUtils.isNotBlank(response.getErrorCode())){
                throw new TaoBaoApiException("ErrorCode："+response.getErrorCode()+"，body：" + response.getBody());
            }
            if(CollectionUtils.isNotEmpty(response.getReturnBillList())) {
                returnBillList.addAll(response.getReturnBillList());
            }
        }

        Collections.sort(returnBillList,new Comparator<ReturnBill>() {
            @Override
            public int compare(ReturnBill o1, ReturnBill o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });

        for(ReturnBill returnBill : returnBillList){
            System.out.println("退款单号：" + returnBill.getRefundId()+"，创建时间："+returnBill.getCreated()+"，更新时间："+returnBill.getModified());
        }

        System.out.println("总条数："+totalCount+"=="+returnBillList.size());
    }



}
