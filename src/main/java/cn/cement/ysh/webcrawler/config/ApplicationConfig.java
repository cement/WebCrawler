package cn.cement.ysh.webcrawler.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Configuration;
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


}
