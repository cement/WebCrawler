package cn.cement.ysh.webcrawler.task;

import cn.cement.ysh.webcrawler.config.ApplicationConfig;
import cn.cement.ysh.webcrawler.crawler.KeywordsCrawler;
import cn.cement.ysh.webcrawler.crawler.WorkerCrawler;
import cn.cement.ysh.webcrawler.entry.crawler.CrawOrder;
import cn.cement.ysh.webcrawler.entry.crawler.DicCrawWord;
import cn.cement.ysh.webcrawler.service.CrawlerOrderService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CrawlerTask {

    @Autowired
    private CrawlerOrderService orderService;
    @Autowired
    private MongoTemplate mongoTemplate;

//    @Resource(name = "ThreadExecutor")
//    private ExecutorService executorService;

    @Scheduled(fixedRateString = "${crawler.task.fixedRate:3600000}")
    public void run() {
        if (WorkerCrawler.executor.getQueue().size() > 0) {
            log.warn("定时爬取任务正在运行,本次任务退出执行");
            return;
        }
        Query query = Query.query(Criteria.where("iused").is(true));
        List<CrawOrder> crawOrders = mongoTemplate.find(query, CrawOrder.class);

        for (CrawOrder crawOrder : crawOrders) {
            ApplicationConfig.executorService.execute(() -> {
                try {
                    int deep = Optional.ofNullable(crawOrder.getDeep()).orElse(1);
                    String orderId = crawOrder.getOrderId();
                    List<String> webUrls = crawOrder.getWebUrls();

                    LinkedHashMap<String, String> combinedMap = new LinkedHashMap<>();

                    String[] keywordArray = Optional.ofNullable(crawOrder.getKeyWord()).orElse("").split("[,，]");
                    for (String keyword : keywordArray) {
                        combinedMap.put(keyword, keyword);
                    }
                    List<DicCrawWord> dicCrawWords = Optional.ofNullable(crawOrder.getCrawWords()).orElse(Lists.newArrayList(new DicCrawWord()));
                    for (DicCrawWord dicCrawWord : dicCrawWords) {
                        String word = dicCrawWord.getWord();
                        String regx = dicCrawWord.getRegx();
                        if (word != null && regx != null) {
                            combinedMap.put(word, regx);
                        }
                    }

                    for (String webUrl : webUrls) {
                        KeywordsCrawler keywordsCrawler = new KeywordsCrawler(orderId, webUrl, deep, null, combinedMap);
//                        ApplicationConfig.executorService.execute(keywordsCrawler);
                        keywordsCrawler.start();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
    }
}
