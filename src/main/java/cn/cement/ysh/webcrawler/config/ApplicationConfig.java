package cn.cement.ysh.webcrawler.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableKnife4j
@EnableSwagger2
@Configuration
public class ApplicationConfig {



    public static ExecutorService executorService = Executors.newWorkStealingPool();

//    @Bean("ThreadExecutor")
//    public ExecutorService taskExecutor() {
//        ExecutorService executorService = Executors.newWorkStealingPool();
//        return executorService;
//    }


   public static MongoTemplate mongoTemplate;
    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        ApplicationConfig.mongoTemplate = mongoTemplate;
    }


}
