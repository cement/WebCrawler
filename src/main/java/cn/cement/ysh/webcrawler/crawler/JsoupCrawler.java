package cn.cement.ysh.webcrawler.crawler;

import cn.cement.ysh.webcrawler.utils.utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class JsoupCrawler extends BaseCrawler<Document> {


    public JsoupCrawler(String seed, int depth) {
        super(seed, depth);
    }

    @Override
    public Document getContent() throws IOException {
        Document document = Jsoup.connect(seed).execute().parse();
        return document;
    }

    @Override
    public boolean resultCallback(Document document) {
        return false;
    }

    @Override
    public List<String> linkFilter(List<String> allLinks) {
        return allLinks;
    }

    @Override
    public List<String> findAllLinks(Document content) {
        return utils.findAllLinks(content);
    }

    @Override
    public boolean isCancled() {
        return false;
    }

    @Override
    public ICrawler newInstance(String seed, int depth) {
        return new JsoupCrawler(seed,depth);
    }


}
