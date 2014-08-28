package logisticscenter;

import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.logisticscenter.domain.LogisticsPrintInfo;
import com.ejushang.steward.logisticscenter.service.LogisticsPrintInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

/**
 * User: Blomer
 * Date: 14-4-8
 * Time: 下午3:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
@Transactional
public class LogisticsPrintInfoServiceTest {

    @Autowired
    private LogisticsPrintInfoService logisticsPrintInfoService;

    @Test
    public void TestList() {
        int pageNo = 1;
        int pageSize = 15;
        Page page = new Page(pageNo, pageSize);
        List<LogisticsPrintInfo> list = logisticsPrintInfoService.list(page);
        System.out.println("11314308zheli:" + list.get(0).getId()+" "+list.get(1).getId()+" "+list.get(2).getId());
    }

    @Test
    public void TestGetByName() {

       LogisticsPrintInfo logisticsPrintInfo = logisticsPrintInfoService.getByName("zhongtong");
        System.out.println("11314308zheli:" + logisticsPrintInfo.toString());
    }


    @Test
    public void TestSave() {
        LogisticsPrintInfo logisticsPrintInfo = new LogisticsPrintInfo();
       // logisticsPrintInfo.setId(39);
        logisticsPrintInfo.setName("shentong6");
        logisticsPrintInfo.setLaw(11314308);
        logisticsPrintInfo.setLogisticsPicturePath("asdfasd");
        logisticsPrintInfo.setOperatorId(123123);
        logisticsPrintInfo.setPrintHtml("asdfsda");
        logisticsPrintInfoService.save(logisticsPrintInfo);
        //assertThat(count, is(equalTo(1)));
    }

}
