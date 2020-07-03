package cn.cement.ysh.webcrawler.crawler;

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
public class BaseCrawler extends WorkerCrawler<Document> {

    public String orderId;
    public String seed;
    public int depth;
    public String linkRegex;
    public AtomicInteger atomic = new AtomicInteger(depth);
//    public Map<String,String> keywords = new LinkedHashMap<>();
    public Map<String,String> keywords = new LinkedHashMap<>();


    public BaseCrawler(String seed, int depth) {
        this.seed = seed;
        this.depth = depth;
    }


    public BaseCrawler(String orderId,String seed, int depth, String linkRegex, Map<String,String> regexs) {
        this.orderId=orderId;
        this.seed = seed;
        this.depth = depth;
        this.keywords = regexs;
        this.linkRegex = linkRegex;
    }

    @Override
    public Document getDocument() throws IOException {
        Document document = Jsoup.connect(seed).execute().parse();
        return document;
    }

    @Override
    public void resultCallback(Document document) {
        //TODO do nothing,subclass process this;
    }

    @Override
    public List<String> linkFilter(List<String> allLinks){
      if (StringUtils.isEmpty(linkRegex)){
          return allLinks;
      }
      List<String> filtedLinks = allLinks.stream().filter(link -> link.matches(linkRegex)).collect(Collectors.toList());
      return filtedLinks;
    }



    @Override
    public BaseCrawler newInstance(String orderId, String seed, int depth, String linkRegex, Map<String, String> regexs) {
        return new BaseCrawler(orderId,seed,depth,linkRegex,regexs);
    }


    @Override
    public void run() {
        try {
                log.info("=== depth:{},url:{}  当前页面爬取中 ===!", this.atomic.get(), this.seed);
                Document document = getDocument();
                resultCallback(document);
                if (atomic.incrementAndGet()<depth){
                    List<String> allLinks = utils.findAllLinks(document);
                    List<String> filtedLinks = linkFilter(allLinks);
                    for (String link : filtedLinks) {
                        BaseCrawler baseCrawler = newInstance(orderId,link, atomic.get(),linkRegex,keywords);
                        baseCrawler.start(START_TYPE);
                    }
                    log.info("=== depth:{},title:{},url:{}  当前页面爬取完成! ===", this.orderId,this.atomic.get(),document.title(),this.seed);
                }else{
                    log.info("=== depth:{},title:{},url:{}  当前页面爬取完成! ===", this.orderId,this.atomic.get(), document.title(),this.seed);
                }
            if (executor.getActiveCount()<=1 && executor.getQueue().isEmpty()){
                    log.info("****** orderId:{},当前任务爬取完成! ******", this.orderId,this.seed);
                }
        } catch (IOException e) {
            log.error("depth:{},url:{}  当前页面爬取错误!-->{}",this.atomic.get(), this.seed,e.getMessage());
        }
    }
}
