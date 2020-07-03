package cn.cement.ysh.webcrawler.task;

import cn.cement.ysh.webcrawler.crawler.WordsCrawler;
import cn.cement.ysh.webcrawler.crawler.WorkerCrawler;
import cn.cement.ysh.webcrawler.entry.crawler.CrawOrder;
import cn.cement.ysh.webcrawler.service.CrawlerOrderService;
import lombok.extern.slf4j.Slf4j;
import ognl.OgnlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class CrawlerTask{

    @Autowired
    private CrawlerOrderService orderService;
    @Scheduled(fixedRate=60000)
    public void run() {
        if (WorkerCrawler.executor.getQueue().size()>0){
            log.warn("定时爬取任务正在运行,本次任务退出执行");
            return;
        }
        CrawOrder crawOrder = orderService.selectCrawlerorderById("13247");
        int deep = crawOrder.getDeep();
        String orderId = crawOrder.getOrderId();
        List<String> webUrls = crawOrder.getWebUrls();

        /*处理关键词与正则表达式*/
        Map<String, String> keywords = Arrays.stream(crawOrder.getKeyWord().split(","))
                .filter(keyword-> !StringUtils.isEmpty(keyword)).collect(Collectors.toMap(keyword->keyword, keyword->keyword));
        Map<String, String> typewords= crawOrder.getCrawWords().stream()
                .filter(keyword-> !StringUtils.isEmpty(keyword)).collect(Collectors.toMap(crawword -> crawword.getWord(), craworder -> craworder.getRegx()));
//        LinkedHashMap<Object, Object> combinedMap = new LinkedHashMap<>();
//        combinedMap.putAll(keywords);
//        combinedMap.putAll(typewords);
        /*合并关键词与正则表达式*/
        Map<String, String> combinedMap = Stream.concat(keywords.entrySet().stream(), typewords.entrySet().stream()).collect(
                Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        webUrls.parallelStream().forEach(url->{
            WordsCrawler defaultCrawler = new WordsCrawler(orderId,url, deep,null, combinedMap);
            defaultCrawler.start();
        });
    }
}
