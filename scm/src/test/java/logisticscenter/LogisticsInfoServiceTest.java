package logisticscenter;

import com.ejushang.steward.common.util.JsonResult;
import com.ejushang.steward.logisticscenter.domain.LogisticsInfo;
import com.ejushang.steward.logisticscenter.service.LogisticsInfoService;
import com.ejushang.steward.ordercenter.bean.TransferInfoBean;
import com.ejushang.steward.ordercenter.constant.DeliveryType;
import com.ejushang.steward.ordercenter.constant.TbRefundPhase;
import com.ejushang.steward.ordercenter.constant.ZyDateType;
import com.ejushang.steward.ordercenter.constant.ZyRefundStatus;
import com.ejushang.steward.ordercenter.service.LogisticsService;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Blomer
 * Date: 14-4-9
 * Time: 上午9:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
@Transactional
public class LogisticsInfoServiceTest {

    @Autowired
    private LogisticsService logisticsInfoService;

    @Test
    @Rollback(false)
    public void TestUpdate() {
        String param1="{\"message\":\"\",\"status\":\"shutdown\",\"lastResult\":{\"message\":\"ok\",\"state\":\"3\",\"data\":[{\"context\":\"广东深圳公司罗湖区东门印象分部:快件已被 图片 签收\",\"time\":\"2014-08-05 14:35:35\",\"ftime\":\"2014-08-05 14:35:35\"},{\"context\":\"广东深圳公司罗湖区东门印象分部:进行派件扫描；派送业务员：小郭；联系电话：18028763080\",\"time\":\"2014-08-05 10:11:49\",\"ftime\":\"2014-08-05 10:11:49\"},{\"context\":\"广东深圳公司罗湖区东门印象分部:到达目的地网点，快件将很快进行派送\",\"time\":\"2014-08-05 09:22:03\",\"ftime\":\"2014-08-05 09:22:03\"},{\"context\":\"广东深圳公司罗湖区东门分部:进行快件扫描，将发往：广东深圳公司罗湖区东门印象分部\",\"time\":\"2014-08-05 07:39:45\",\"ftime\":\"2014-08-05 07:39:45\"},{\"context\":\"广东深圳公司:进行快件扫描，将发往：广东深圳公司罗湖区东门分部\",\"time\":\"2014-08-05 05:07:22\",\"ftime\":\"2014-08-05 05:07:22\"},{\"context\":\"广东深圳公司:在分拨中心进行卸车扫描\",\"time\":\"2014-08-05 05:01:47\",\"ftime\":\"2014-08-05 05:01:47\"},{\"context\":\"广东东莞分拨中心:从站点发出，本次转运目的地：广东深圳公司\",\"time\":\"2014-08-05 01:43:18\",\"ftime\":\"2014-08-05 01:43:18\"},{\"context\":\"广东东莞分拨中心:快件进入分拨中心进行分拨\",\"time\":\"2014-08-05 01:41:32\",\"ftime\":\"2014-08-05 01:41:32\"},{\"context\":\"广东江门公司:进行揽件扫描\",\"time\":\"2014-08-04 20:11:19\",\"ftime\":\"2014-08-04 20:11:19\"}],\"status\":\"200\",\"com\":\"yunda\",\"nu\":\"1900784662898\",\"ischeck\":\"1\",\"condition\":\"F00\"},\"billstatus\":\"check\"}";
        logisticsInfoService.handleThirdLogisticsInfo(param1.trim());
    }
    @Test
    @Rollback(false)
    public void logisticsInfo() {
        String orderNo = "20140725142869657";
        String expressNo = "V197743552";
        DeliveryType deliveryType = DeliveryType.yuantong;
        String sendTo = "四川省乐山市";

        // 添加
        logisticsInfoService.sendLogisticsInfoRequest(orderNo, deliveryType, expressNo, sendTo);
//
//        // 查询
//        Logistics info = logistic
// sInfoService.findLogisticsInfoByExpressNo(expressNo);
//        Assert.assertNotNull(info);
//
//        info.setExpressInfo("{}");
//        // 更新
//        logisticsInfoService.updateLogisticsInfo(info);
//
//        info = logisticsInfoService.findLogisticsInfoByExpressNo(expressNo);
//        Assert.assertEquals("{}", info.getExpressInfo());
//        Assert.assertEquals(false, info.getExpressStatus().booleanValue());
//
//        info.setExpressInfo("{success}");
//        info.setExpressStatus(true);
//        logisticsInfoService.updateLogisticsInfo(info);
//
//        info = logisticsInfoService.findLogisticsInfoByExpressNo(expressNo);
//        Assert.assertEquals("{success}", info.getExpressInfo());
//        Assert.assertEquals(true, info.getExpressStatus().booleanValue());

        //logisticsInfoService.saveLogisticsInfo(new Logistics(orderNo, expressNo, deliveryType, sendTo));

//        logisticsInfoService.deleteLogisticsInfo(info.getId());
//        info = logisticsInfoService.findLogisticsInfoByExpressNo(expressNo);
//        Assert.assertNull(info);
    }

    @Test
    public void testExport() throws IllegalAccessException, IOException, InvocationTargetException {
        List<String> expressNos = new ArrayList<String>();
        expressNos.add("11111");
        expressNos.add("22222");
        expressNos.add("33333");
        expressNos.add("44444");
        expressNos.add("55555");
        expressNos.add("123123123123");
        expressNos.add("1900784662898");
        File file = logisticsInfoService.exportLogisticsFullInfoBeanByExpressNos(expressNos);

        System.out.println(file.getAbsolutePath());
    }
   @Test
    public void testFindTransferInfoByExpressNo(){
       String expressNo="1900784662898";
       List<TransferInfoBean> transferInfoBeans=logisticsInfoService.findTransferInfoByExpressNo(expressNo);

       System.out.println(new JsonResult(true).addList(transferInfoBeans));

   }
}
