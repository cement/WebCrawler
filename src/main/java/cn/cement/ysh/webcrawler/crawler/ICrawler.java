package cn.cement.ysh.webcrawler.crawler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICrawler<T> extends Runnable {


     T  getContent() throws IOException;


     boolean  resultCallback(T document);


     List<String> linkFilter(List<String> allLinks);


//     BaseCrawler newInstance(String orderId,String seed, int depth, String linkRegex, Map<String,String> regexs);


     List<String> findAllLinks(T content);

     boolean isCancled();

    ICrawler newInstance(String seed, int depth);

    void start(int startType);
}
