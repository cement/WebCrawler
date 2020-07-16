package cn.cement.ysh.webcrawler.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultCrawler extends BaseCrawler {

    public DefaultCrawler(String orderId, String seed, int depth, String linkRegex, Map<String, String> keywords) {
        super(orderId,seed,depth,linkRegex,keywords);
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

    public boolean isCanled(String orderId) {
        return false;
    }

    @Override
    public List<String> linkFilter(List<String> allLinks) {
        if (StringUtils.isEmpty(linkRegex)) {
            return allLinks;
        }
        List<String> filtedLinks = allLinks.stream().filter(link -> link.matches(linkRegex)).collect(Collectors.toList());
        return filtedLinks;
    }


    @Override
    public BaseCrawler newInstance(String orderId, String seed, int depth, String linkRegex, Map<String, String> keywords) {
        return new DefaultCrawler(orderId, seed, depth, linkRegex, keywords);
    }
}
