package cn.cement.ysh.webcrawler.task;

import cn.cement.ysh.webcrawler.crawler.KeywordsCrawler;
import cn.cement.ysh.webcrawler.service.CrawlerOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    public void keywordCrawlerTask() {
        try {
            KeywordsCrawler.runTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
