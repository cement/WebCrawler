package cn.cement.ysh.webcrawler.crawler;

import cn.cement.ysh.webcrawler.config.CrawlerConfig;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.cement.ysh.webcrawler.threadpool.CrawlerForkJoinPoolExecutor.forkJoinPool;


@Slf4j
@Data
@NoArgsConstructor
public abstract class BasicCrawler<T> extends WorkerCrawler<T> implements Cloneable {
    public String seed;
    public int depth;
    public AtomicInteger atomic = new AtomicInteger(depth);

    public BasicCrawler(String seed, int depth) {
        this.seed = seed;
        this.depth = depth;
    }

    @Override
    public void run() {
        int queueSize = forkJoinPool.getQueuedSubmissionCount();
        int poolSize = forkJoinPool.getPoolSize();
        try {
            if (CrawlerConfig.isCancancleRuning && isCancled()) {
                log.info(">>>>>> 当前页面爬取取消! >>>>>>  :[{}/{}]:{}-{} ", poolSize, queueSize, depth, this.seed);
                return;
            }
            log.debug(">>>>>> 当前页面开始爬取! >>>>>>  :[{}/{}]:{}-{} ", poolSize, queueSize, depth, this.seed);
            T content = getContent();
            boolean isHalt = resultCallback(getContent());
            if (--depth > 0 && !isHalt) {
                List<String> allLinks = findAllLinks(content);
                List<String> filtedLinks = linkFilter(allLinks);
                for (String link : filtedLinks) {
                    ICrawler crawler = newInstance(link, depth);
                    forkJoinPool.execute(crawler);
//                    crawler.start(START_TYPE);
                }
            }
            log.debug("<<<<<< 当前页面爬取完成! <<<<<<  :[{}/{}]:{}-{} ", poolSize, queueSize, depth, this.seed);
            if (queueSize<=0) {
                log.info("====== 当前任务即将爬取完成! ====== :[{}/{}]:{}-{} ", poolSize, queueSize, depth, this.seed);
            }

        } catch (IOException e) {
            log.warn("****** 当前页面爬取IO错误! ******  :[{}/{}]:{}-{},{} ", poolSize, queueSize, depth, this.seed, e.getMessage());
        } catch (Exception e) {
            log.error("****** 当前页面爬取其他错误! ******  :[{}/{}]:{}-{},{} ", poolSize, queueSize, depth, this.seed, e.getMessage());
        }
    }

    @Override
    protected ICrawler clone() throws CloneNotSupportedException {
        return (ICrawler) super.clone();
    }
}
