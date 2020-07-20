package cn.cement.ysh.webcrawler.crawler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICrawler<T> extends Runnable {


     T  getContent() throws IOException;


     void  resultCallback(T document);


     List<String> linkFilter(List<String> allLinks);


     BaseCrawler newInstance(String orderId,String seed, int depth, String linkRegex, Map<String,String> regexs);


     boolean isCanled(String orderId);
}
