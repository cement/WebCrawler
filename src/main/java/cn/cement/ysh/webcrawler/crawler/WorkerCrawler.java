package cn.cement.ysh.webcrawler.crawler;

import static cn.cement.ysh.webcrawler.threadpool.CrawlerThreadPoolExecutor.executor;

public abstract class WorkerCrawler<T> implements ICrawler<T>{


    public static final int START_ASYNC = 0;
    public static final int START_SYNC = 1;

    public static int START_TYPE = START_ASYNC;


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
