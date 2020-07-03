package cn.cement.ysh.webcrawler.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableKnife4j
@EnableSwagger2
@Configuration
public class ApplicationConfig {
}
