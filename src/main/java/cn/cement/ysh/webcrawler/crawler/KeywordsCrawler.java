package cn.cement.ysh.webcrawler.crawler;

import cn.cement.ysh.webcrawler.config.ApplicationConfig;
import cn.cement.ysh.webcrawler.entry.crawler.CrawOrder;
import cn.cement.ysh.webcrawler.utils.utils;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.cement.ysh.webcrawler.config.MongodbConfig.crawlerResultName;
import static cn.cement.ysh.webcrawler.threadpool.CrawlerThreadExecutor.executor;

@Slf4j
public class KeywordsCrawler extends JsoupCrawler {


    public String orderId;
//    public Map<String, String> keywords = new LinkedHashMap<>();
    public Map<String, String> keywordRegex = new LinkedHashMap<>();


    public KeywordsCrawler(String seed, int depth, String orderId, Map<String, String> keywordRegex) {
        super(seed, depth);
        this.orderId = orderId;
        this.keywordRegex = keywordRegex;
    }

    @Override
    public boolean resultCallback(Document document) {
        if (Objects.isNull(document)){
            return super.resultCallback(document);
        }
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
        log.debug("[{}]{}-{} {}-{}:\r\n\t结果:{}", executor.getQueue().size(),orderId,keywordRegex.keySet(),depth,seed, resultMap);
        if (!CollectionUtils.isEmpty(resultMap)) {
            ApplicationConfig.mongoTemplate.save(resultMap, crawlerResultName);
            log.info("[{}]{}-{} {}-{}:\r\n\t保存结果:{}",executor.getQueue().size(),orderId,keywordRegex.keySet(),depth,seed, resultMap);
//            ApplicationConfig.executorService.execute(() -> {
//                log.info("***保存结果1:{}", resultMap);
//                try {
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                log.info("***保存结果2:{}", resultMap);
//            });
        }
        return super.resultCallback(document);
    }

    @Override
    public boolean isCancled() {
        CrawOrder crawOrder = ApplicationConfig.mongoTemplate.findOne(Query.query(Criteria.where("_id").is(orderId)), CrawOrder.class, "craw_order");
        boolean iused = crawOrder.isIused();
        return iused;
    }


    @Override
    public ICrawler newInstance(String seed, int depth) {
        return new KeywordsCrawler(seed,depth,orderId,keywordRegex);
    }




}
