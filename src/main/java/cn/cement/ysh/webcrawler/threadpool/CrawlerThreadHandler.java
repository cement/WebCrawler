package cn.cement.ysh.webcrawler.threadpool;

import cn.cement.ysh.webcrawler.config.ApplicationConfig;
import cn.cement.ysh.webcrawler.config.CrawlerConfig;
import cn.cement.ysh.webcrawler.task.CrawlerOrderTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.cement.ysh.webcrawler.config.CrawlerConfig.maximumPoolSize;
import static cn.cement.ysh.webcrawler.config.CrawlerConfig.queueLength;

@Slf4j
public class CrawlerThreadHandler implements ThreadFactory, Thread.UncaughtExceptionHandler,RejectedExecutionHandler {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private static String groupName = "CrawLerThreadGroup";

    public CrawlerThreadHandler() {
//        SecurityManager s = System.getSecurityManager();
        group = new ThreadGroup(groupName);
//        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = "crawler-" + poolNumber.getAndIncrement() + "-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        t.setUncaughtExceptionHandler(this);
        if (!t.isDaemon()) {
            t.setDaemon(true);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }



    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        int queueSize = executor.getQueue().size();

        if (queueSize >= queueLength && queueSize<queueLength*2){
//              r.run();
            ApplicationConfig.executorService.execute(r);
        }
        else{
            log.warn("线程队列太长,拒绝执行本次任务!");
        }
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("线程组 {}-{} 捕获异常:{}",groupName,t.getName(),e.getMessage(),e);
    }

}