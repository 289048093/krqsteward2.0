package com.ejushang.steward.analyzer;

import com.ejushang.steward.main.GrabberMain;
import com.ejushang.steward.main.GrabberMonitor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 */
public class StartAnalyzerTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(GrabberMain.SPRING_CFG_FILES);
        GrabberMonitor grabberMonitor = context.getBean(GrabberMonitor.class);
        grabberMonitor.startAnalyzer();
    }

}
