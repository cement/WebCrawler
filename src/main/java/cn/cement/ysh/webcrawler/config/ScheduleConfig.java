package cn.cement.ysh.webcrawler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @YSH 调度任务多线程配置
 */
@EnableScheduling
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {



    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(3));
    }
/* 不使用注解是可以启动定时任务*/
//    @Bean("taskScheduler")
    public ThreadPoolTaskScheduler getCaseThreadPoolTaskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
        taskScheduler.setThreadNamePrefix("task-thread-");
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setPoolSize(6);
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        return  taskScheduler;
    }



}

