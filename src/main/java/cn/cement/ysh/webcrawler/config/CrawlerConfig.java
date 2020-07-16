package cn.cement.ysh.webcrawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class CrawlerConfig {

//    public static ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    public static boolean isCancancleRuning = false;
    @Value("${crawler.cancancle.runing:false}")
    public void setCancancleRuning(boolean cancleRuning) {
        CrawlerConfig.isCancancleRuning = cancleRuning;
    }


    public static int crawlerThreadCount;
    @Value("${crawler.thread.count:10}")
    public void setCrawlerThreadCount(int crawlerThreadCount) {
        CrawlerConfig.crawlerThreadCount = crawlerThreadCount;
    }


    public static String crawlerResultName;
    @Value("${crawler.result.name:craw_result}")
    public  void setCrawlerResultName(String crawlerResultName) {
        CrawlerConfig.crawlerResultName = crawlerResultName;
    }

    public static int crawlerConnectionTimeout;
    @Value("${crawler.connection.timeout:20000}")
    public static void setCrawlerConnectionTimeout(int crawlerConnectionTimeout) {
        CrawlerConfig.crawlerConnectionTimeout = crawlerConnectionTimeout;
    }



}
