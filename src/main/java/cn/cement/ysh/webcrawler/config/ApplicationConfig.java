package cn.cement.ysh.webcrawler.config;


import cn.cement.ysh.webcrawler.threadpool.CrawlerThreadHandler;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

@EnableKnife4j
@EnableSwagger2
@Configuration
public class ApplicationConfig {

//    public static ForkJoinPool forkJoinPool =  new ForkJoinPool(Runtime.getRuntime().availableProcessors(),ForkJoinPool.defaultForkJoinWorkerThreadFactory,new CrawlerThreadHandler(), true);
    public static ExecutorService executorService = Executors.newWorkStealingPool();

    public static MongoTemplate mongoTemplate;
    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        ApplicationConfig.mongoTemplate = mongoTemplate;
    }


}
