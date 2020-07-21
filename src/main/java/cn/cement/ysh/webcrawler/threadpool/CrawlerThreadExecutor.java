package cn.cement.ysh.webcrawler.threadpool;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static cn.cement.ysh.webcrawler.config.CrawlerConfig.*;
import static cn.cement.ysh.webcrawler.config.CrawlerConfig.keepAliveTime;

public class CrawlerThreadExecutor extends ThreadPoolExecutor {
    private boolean isPaused;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();

    public CrawlerThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static final CrawlerThreadHandler crawlerThreadHandler = new CrawlerThreadHandler();
    public static final LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(queueLength);
    public static final CrawlerThreadExecutor executor = new CrawlerThreadExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.MILLISECONDS,linkedBlockingQueue, crawlerThreadHandler, crawlerThreadHandler) ;

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused) unpaused.await();
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }

    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }
}
