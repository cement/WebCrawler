package cn.cement.ysh.webcrawler.crawler;

import cn.cement.ysh.webcrawler.utils.utils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WordsCrawler extends BaseCrawler {

    private static  MongoTemplate mongoTemplate;

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        WordsCrawler.mongoTemplate = mongoTemplate;
    }


    public WordsCrawler() {
    }

    public WordsCrawler(String seed, int depth) {
        super(seed, depth);
    }


    public WordsCrawler(String orderId, String seed, int depth, String linkRegex, Map<String, String> regexs) {
        super(orderId,seed, depth,linkRegex, regexs);
    }


    @Override
    public BaseCrawler newInstance(String orderId,String seed, int depth, String linkRegex, Map<String,String> regexs){
        return new WordsCrawler(orderId,seed, depth,linkRegex, regexs);
    }

    @Override
    public void resultCallback(Document document) {
        log.info("原文:{}",document.text());
        LinkedHashMap<String,Object> resultMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(keywords)) {
            for (Map.Entry<String,String> entry:keywords.entrySet()){
                List<String> text= utils.findContentByRegex(document.text(), entry.getValue());
                List<String> part = text.stream().map(e -> utils.findPartByRegex(e,entry.getValue())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(text)){
                    resultMap.put("orderId", orderId);
                    resultMap.put("crawWord", entry.getKey());
                    resultMap.put("webUrl", seed);
                    resultMap.put("result", part);
                    resultMap.put("sourceText", text);
                    resultMap.put("crawTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            }
        }
        log.info("结果:{}",resultMap);
        if (!CollectionUtils.isEmpty(resultMap)){
            executor.execute(()-> mongoTemplate.save(resultMap, "craw_result_test"));
        }
    }
}


//    private ObjectId _id;
//    private String orderId; //爬寻命令id
//    private String webUrl; // 网址
//    private String crawWord; // 爬寻的关键词
//    private String result; // 爬寻结果 多个结果以分号分隔
//    private String sourceText; // 原文
//    private String crawTime; //爬寻时间
//    private boolean transToInner=false; // 是否发送到内网