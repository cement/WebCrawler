package cn.cement.ysh.webcrawler.threadpool;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static cn.cement.ysh.webcrawler.config.CrawlerConfig.*;

public class CrawlerForkJoinPoolExecutor{
    /*forkjoin 框架执行器*/
    public static ForkJoinPool forkJoinPool =  new ForkJoinPool(Runtime.getRuntime().availableProcessors(),ForkJoinPool.defaultForkJoinWorkerThreadFactory,new CrawlerThreadHandler(), true);
}
