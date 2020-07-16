package cn.cement.ysh.webcrawler.crawler;

import cn.cement.ysh.webcrawler.config.ApplicationConfig;
import cn.cement.ysh.webcrawler.config.CrawlerConfig;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class WorkerCrawler<T> implements ICrawler<T>{


//    public static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(100);
    public static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(CrawlerConfig.crawlerThreadCount,new CrawlerThreadFactory()) ;
//    public static final ExecutorService executor = Executors.newWorkStealingPool();

    public static final int START_ASYNC = 0;
    public static final int START_SYNC = 1;



    public static int START_TYPE = START_ASYNC;

    public static void startDelay(Runnable runnable,int delay){
        executor.schedule(runnable, delay, TimeUnit.SECONDS);
    }

    public static void startRate(Runnable runnable,int initdelay,int period){
        executor.scheduleAtFixedRate(runnable, initdelay, period,TimeUnit.SECONDS);
    }

    public static boolean isIdle(){
        return executor.getQueue().isEmpty();
    }

    public void start() {
        start(START_ASYNC);
    }
    public void start(int startType) {
        START_TYPE = startType;
        switch (startType) {
            case START_ASYNC:
                executor.execute(this);
                break;
            case START_SYNC:
                run();
                break;
            default:
                break;
        }
    }


}
