package cn.cement.ysh.webcrawler.crawler;

import cn.cement.ysh.webcrawler.config.ApplicationConfig;
import cn.cement.ysh.webcrawler.config.CrawlerConfig;
import cn.cement.ysh.webcrawler.entry.crawler.CrawOrder;
import cn.cement.ysh.webcrawler.entry.crawler.DicCrawWord;
import cn.cement.ysh.webcrawler.utils.utils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class KeywordsCrawler extends DefaultCrawler {

    public KeywordsCrawler(String orderId, String seed, int depth, String linkRegex, Map<String, String> keywordRegex) {
        super(orderId, seed, depth, linkRegex, keywordRegex);
    }

    @Override
    public BaseCrawler newInstance(String orderId, String seed, int depth, String linkRegex, Map<String, String> keywordRegex) {
        return new KeywordsCrawler(orderId, seed, depth, linkRegex, keywordRegex);
    }

    @Override
    public void resultCallback(Document document) {
        log.trace("原文:{}", document.text());
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(keywordRegex)) {
            for (Map.Entry<String, String> entry : keywordRegex.entrySet()) {
                List<String> text = utils.findContentByRegex(document.text(), entry.getValue());
                List<String> part = text.stream().map(e -> utils.findPartByRegex(e, entry.getValue())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(text)) {
                    resultMap.put("orderId", orderId);
                    resultMap.put("crawWord", entry.getKey());
                    resultMap.put("title", document.title());
                    resultMap.put("webUrl", seed);
                    resultMap.put("result", part);
                    resultMap.put("sourceText", text);
                    resultMap.put("crawTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            }
        }
        log.debug("结果:{}", resultMap);
        if (!CollectionUtils.isEmpty(resultMap)) {
            ApplicationConfig.mongoTemplate.save(resultMap, CrawlerConfig.crawlerResultName);
            log.info("***保存结果2:{}", resultMap);
//            ApplicationConfig.executorService.execute(() -> {
//                log.info("***保存结果1:{}", resultMap);
//                try {
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                log.info("***保存结果2:{}", resultMap);
//            });
        }
    }

    @Override
    public boolean isCanled(String orderId) {
        CrawOrder crawOrder = ApplicationConfig.mongoTemplate.findOne(Query.query(Criteria.where("_id").is(orderId)), CrawOrder.class, "craw_order");
        boolean iused = crawOrder.isIused();
        return iused;
    }


    public static  void runTask(){
        if (WorkerCrawler.executor.getQueue().size() > 0) {
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
                        KeywordsCrawler keywordsCrawler = new KeywordsCrawler(orderId, webUrl, deep, null, combinedMap);
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
