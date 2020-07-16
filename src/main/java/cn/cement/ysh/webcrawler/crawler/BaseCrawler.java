package cn.cement.ysh.webcrawler.crawler;

import cn.cement.ysh.webcrawler.config.CrawlerConfig;
import cn.cement.ysh.webcrawler.utils.utils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Data
@NoArgsConstructor
public abstract class BaseCrawler extends WorkerCrawler<Document> {

    public String orderId;
    public String seed;
    public int depth;
    public String linkRegex;
    public AtomicInteger atomic = new AtomicInteger(depth);
    //    public Map<String,String> keywords = new LinkedHashMap<>();
    public Map<String, String> keywordRegex = new LinkedHashMap<>();


    public BaseCrawler(String seed, int depth) {
        this.seed = seed;
        this.depth = depth;
    }


    public BaseCrawler(String orderId, String seed, int depth, String linkRegex, Map<String, String> keywordRegex) {
        this.orderId = orderId;
        this.seed = seed;
        this.depth = depth;
        this.keywordRegex = keywordRegex;
        this.linkRegex = linkRegex;
    }

    @Override
    public abstract Document getDocument() throws IOException;

    @Override
    public abstract void resultCallback(Document document);


    @Override
    public abstract List<String> linkFilter(List<String> allLinks);


    @Override
    public abstract BaseCrawler newInstance(String orderId, String seed, int depth, String linkRegex, Map<String, String> keywordRegex);


    public abstract boolean isCanled(String orderId);

    @Override
    public void run() {
        try {
            if (CrawlerConfig.isCancancleRuning && isCanled(orderId)){
                log.error("======当前页面爬取取消!====== :{}-{},{},{}  ",this.orderId, this.atomic.get(), this.keywordRegex.keySet(), this.seed );
                return;
            }
            log.debug(">>>>>> 当前页面开始爬取! >>>>>> :{}-{},{},{}  ", this.orderId, this.atomic.get(), this.keywordRegex.keySet(), this.seed);
            Document document = getDocument();
            resultCallback(document);
            if (atomic.incrementAndGet() < depth) {
                List<String> allLinks = utils.findAllLinks(document);
                List<String> filtedLinks = linkFilter(allLinks);
                for (String link : filtedLinks) {
                    BaseCrawler baseCrawler = newInstance(orderId, link, atomic.get(), linkRegex, keywordRegex);
                    baseCrawler.start(START_TYPE);
                }
            }
            log.debug("=== 当前页面爬取完成! === :{}-{},{},{}={} ", this.orderId, this.atomic.get(), this.keywordRegex.keySet(), document.title(), this.seed);
            if (executor.getActiveCount() <= 2 && executor.getQueue().isEmpty()) {
                log.info("<<<<<< 当前任务即将爬取完成! <<<<<<  :{}-{},{} ", this.orderId, this.atomic.get(), this.keywordRegex.keySet());
            }
        } catch (IOException e) {
            log.error("当前页面爬取错误!-->{} :{}-{},{},{}  ", e.getMessage(),this.orderId, this.atomic.get(), this.keywordRegex.keySet(), this.seed );
        }
    }
}
