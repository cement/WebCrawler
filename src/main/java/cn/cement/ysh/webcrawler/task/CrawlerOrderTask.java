package cn.cement.ysh.webcrawler.task;

import cn.cement.ysh.webcrawler.config.ApplicationConfig;
import cn.cement.ysh.webcrawler.crawler.KeywordsCrawler;
import cn.cement.ysh.webcrawler.entry.crawler.CrawOrder;
import cn.cement.ysh.webcrawler.entry.crawler.DicCrawWord;
import cn.cement.ysh.webcrawler.threadpool.CrawlerThreadExecutor;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static cn.cement.ysh.webcrawler.threadpool.CrawlerThreadExecutor.executor;

@Slf4j
public class CrawlerOrderTask {

    public static void runTask() {
        if (executor.getQueue().size() > 0) {
            log.warn("定时爬取任务正在运行,本次任务退出执行");
            return;
        }
        Query query = Query.query(Criteria.where("iused").is(true));
        List<CrawOrder> crawOrders = ApplicationConfig.mongoTemplate.find(query, CrawOrder.class);

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
                        KeywordsCrawler keywordsCrawler = new KeywordsCrawler(webUrl,deep,orderId, combinedMap);
//                        ApplicationConfig.executorService.execute(keywordsCrawler);
                        keywordsCrawler.start();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            });

        }
    }
}
