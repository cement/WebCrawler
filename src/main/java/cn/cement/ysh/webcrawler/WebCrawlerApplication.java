package cn.cement.ysh.webcrawler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import static cn.cement.ysh.webcrawler.threadpool.CrawlerThreadPoolExecutor.executor;

@Slf4j
@SpringBootApplication
public class WebCrawlerApplication extends SpringBootServletInitializer implements ApplicationRunner, CommandLineRunner {

    public static void main(String[] args) {
        log.info(">>>>>> 程序开始 >>>>>>");
        SpringApplication.run(WebCrawlerApplication.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            executor.shutdownNow();
            log.info("<<<<<< 程序结束 <<<<<<");
        }));
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebCrawlerApplication.class);
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("====== 程序初始化[ApplicationRunner] ======");
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("====== 程序初始化[CommandLineRunner] ======");
    }
}
