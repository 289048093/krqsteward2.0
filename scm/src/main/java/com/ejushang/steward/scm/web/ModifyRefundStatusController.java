package com.ejushang.steward.scm.web;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.openapicenter.tb.api.TbRefundApi;
import com.ejushang.steward.openapicenter.tb.api.TbTradeApi;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.exception.TaoBaoApiException;
import com.ejushang.steward.ordercenter.constant.*;
import com.ejushang.steward.ordercenter.domain.OriginalRefund;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.ShopAuth;
import com.ejushang.steward.ordercenter.service.OriginalRefundService;
import com.ejushang.steward.ordercenter.service.RefundService;
import com.ejushang.steward.ordercenter.service.ShopService;
import com.taobao.api.domain.RefundBill;
import com.taobao.api.response.TmallEaiOrderRefundGetResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Baron.Zhang
 * Date: 2014/7/3
 * Time: 17:22
 */
@RequestMapping("/np")
@Controller
public class ModifyRefundStatusController {

    private static final Logger log = LoggerFactory.getLogger(ModifyRefundStatusController.class);
    @Autowired
    private OriginalRefundService originalRefundService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private RefundService refundService;

    @RequestMapping("/mrs")
    public String modifyRefundStatus(HttpServletResponse response) throws Exception {
        // 保存天猫、淘宝平台原始退款单的集合
        List<OriginalRefund> allOriginalRefundList = new ArrayList<OriginalRefund>();

        // 查询原始退款单的条件Bean
        OriginalRefund originalRefundQuery = new OriginalRefund();
        originalRefundQuery.setPlatformType(PlatformType.TAO_BAO);
        // 查询所有原始订单
        List<OriginalRefund> originalRefundList = originalRefundService.findOriginalRefund(originalRefundQuery);
        if(CollectionUtils.isNotEmpty(originalRefundList)){
            allOriginalRefundList.addAll(originalRefundList);
        }

        originalRefundQuery.setPlatformType(PlatformType.TAO_BAO_2);
        // 查询所有原始订单
        List<OriginalRefund> originalRefundList2 = originalRefundService.findOriginalRefund(originalRefundQuery);
        if(CollectionUtils.isNotEmpty(originalRefundList2)){
            allOriginalRefundList.addAll(originalRefundList2);
        }

        List<OriginalRefund> statusErrOriginalRefundList = new ArrayList<OriginalRefund>();

        // 根据退款单号查询天猫平台
        for(OriginalRefund originalRefund : originalRefundList){
            if(originalRefund.getShopId() == null){
                continue;
            }

            // 测试代码
//            if(!StringUtils.equalsIgnoreCase("8001406070001199",originalRefund.getRefundId())){
//                continue;
//            }

            try {
                Shop shop = shopService.getById(originalRefund.getShopId());
                if (shop == null) {
                    throw new StewardBusinessException("【" + originalRefund.getShopId() + "】不存在对应的店铺");
                }

                ShopAuth shopAuth = shopService.getShopAuthById(shop.getShopAuthId());

                if (shopAuth == null) {
                    throw new StewardBusinessException("【" + originalRefund.getShopId() + "】不存在对应的店铺授权记录");
                }

                TbRefundApi tbRefundApi = new TbRefundApi(shopAuth.getSessionKey());

                Map<String, Object> refundArgsMap = new HashMap<String, Object>();
                refundArgsMap.put("refund_id", Long.valueOf(originalRefund.getRefundId()));
                refundArgsMap.put("refund_phase", convertOriginalRefundPhase2TaoBaoRefundPhase(originalRefund.getRefundPhase()));

                TmallEaiOrderRefundGetResponse tmallEaiOrderRefundGetResponse = tbRefundApi.tmallEaiOrderRefundGet(refundArgsMap);

                if (tmallEaiOrderRefundGetResponse == null) {
                    throw new TaoBaoApiException("访问淘宝接口出现问题，估计是网络问题");
                }

                if (StringUtils.isNotBlank(tmallEaiOrderRefundGetResponse.getErrorCode())) {
                    throw new TaoBaoApiException(tmallEaiOrderRefundGetResponse.getBody());
                }

                RefundBill refundBill = tmallEaiOrderRefundGetResponse.getRefundBill();

                if (refundBill != null) {
                    // 对比状态
                    if (!StringUtils.equalsIgnoreCase(refundBill.getStatus(), originalRefund.getStatus().getName())) {
                        // 更新状态
                        originalRefund.setStatus(getOriginalRefundStatus(refundBill.getStatus()));
                        statusErrOriginalRefundList.add(originalRefund);

                        // 更新原始退款单状态
                        originalRefundService.saveOriginalRefund(originalRefund);
                        // 更新退款单
                        refundService.rollBackRefund(originalRefund);
                    }
                }

            }catch (Exception e){
                if( log.isErrorEnabled()){
                    log.error("当前退款单已过期：",e);
                }
                continue;
            }
        }


        StringBuffer stringBuffer = new StringBuffer("退款单状态修正成功！\r\n 有问题的退款单列表为：\r\n");
        for(OriginalRefund originalRefund : statusErrOriginalRefundList){
            stringBuffer.append(originalRefund.getRefundId()+"<br/>\r\n");
        }
        response.getWriter().println(stringBuffer.toString());
        response.getWriter().flush();
        return null;
    }

    /**
     * 将淘宝平台的退款单状态转为原始退款单的状态
     * @param tbRefundStatus
     * @return
     */
    private OriginalRefundStatus getOriginalRefundStatus(String tbRefundStatus){
        if(StringUtils.equalsIgnoreCase(tbRefundStatus, TbRefundStatus.WAIT_SELLER_AGREE.toString())){
            return OriginalRefundStatus.WAIT_SELLER_AGREE;
        }
        else if(StringUtils.equalsIgnoreCase(tbRefundStatus, TbRefundStatus.SELLER_REFUSE.toString())){
            return OriginalRefundStatus.SELLER_REFUSE;
        }
        else if(StringUtils.equalsIgnoreCase(tbRefundStatus, TbRefundStatus.GOODS_RETURNING.toString())){
            return OriginalRefundStatus.GOODS_RETURNING;
        }
        else if(StringUtils.equalsIgnoreCase(tbRefundStatus, TbRefundStatus.CLOSED.toString())){
            return OriginalRefundStatus.CLOSED;
        }
        else {
            throw  new StewardBusinessException("【" + tbRefundStatus + "】无法转换成智库城状态");
        }
    }



    /**
     * 将原始退款款单状态转换为淘宝状态
     * @return
     */
    private String convertOriginalRefundPhase2TaoBaoRefundPhase(RefundPhase refundPhase){
        if(StringUtils.equalsIgnoreCase(RefundPhase.AFTER_SALE.getName(),refundPhase.getName())){
            return TbRefundPhase.AFTERSALE.tbValue;
        }
        else if(StringUtils.equalsIgnoreCase(RefundPhase.ON_SALE.getName(),refundPhase.getName())){
            return TbRefundPhase.ONSALE.tbValue;
        }
        else {
            return null;
        }
    }
}
