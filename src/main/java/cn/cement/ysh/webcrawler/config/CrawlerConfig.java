package cn.cement.ysh.webcrawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class CrawlerConfig {

    public static boolean isCancancleRuning = false;
    @Value("${crawler.cancancle.runing:false}")
    public void setCancancleRuning(boolean cancleRuning) {
        CrawlerConfig.isCancancleRuning = cancleRuning;
    }


    public static int crawlerConnectionTimeout;
    @Value("${crawler.connection.timeout:20000}")
    public static void setCrawlerConnectionTimeout(int crawlerConnectionTimeout) {
        CrawlerConfig.crawlerConnectionTimeout = crawlerConnectionTimeout;
    }


    public static int queueLength;
    @Value("${crawler.queue.length:20000}")
    public  void setQueueLength(int queueLength) {
        CrawlerConfig.queueLength = queueLength;
    }

    public static int corePoolSize;
    @Value("${crawler.corepool.size:10}")
    public  void setCorePoolSize(int corePoolSize) {
        CrawlerConfig.corePoolSize = corePoolSize;
    }

    public static int maximumPoolSize;
    @Value("${crawler.maxpool.size:100}")
    public  void setMaximumPoolSize(int maximumPoolSize) {
        CrawlerConfig.maximumPoolSize = maximumPoolSize;
    }

    public static long keepAliveTime;
    @Value("${crawler.keepalive.time:1800}")
    public  void setKeepAliveTime(long keepAliveTime) {
        CrawlerConfig.keepAliveTime = keepAliveTime;
    }



}
