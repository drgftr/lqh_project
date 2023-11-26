//package org.lqh.home.utils;
//
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//
///**
// * @description: TODO 类描述
// * @author: 丁真
// * @date: 2023/11/20
// **/
//@Configuration
//@EnableAsync
//public class ThreadPoolConfig {
//
//    /**
//     * 核心线程数
//     */
//    @Value("${async.executor.thread.core_pool_size}")
//    private int corePoolSize;
//
//    /**
//     * 最大线程数
//     */
//    @Value("${async.executor.thread.max_pool_size}")
//    private int maxPoolSize;
//
//    /**
//     * 队列最大长度
//     */
//    @Value("${async.executor.thread.queue_capacity}")
//    private int queueCapacity;
//
//    /**
//     * 线程池维护线程所允许的空闲时间
//     */
//    @Value("${async.executor.thread.keep_alive_seconds}")
//    private int keepAliveSeconds;
//
//    /**
//     * 线程池对拒绝任务(无线程可用)的处理策略
//     */
//    private CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
//
//    private String threadNamePrefix = "AsyncExecutorThread-";
//
//    @Bean(name = "taskExecutor")
//    public ThreadPoolTaskExecutor asyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(corePoolSize);
//        executor.setMaxPoolSize(maxPoolSize);
//        executor.setQueueCapacity(queueCapacity);
//        executor.setKeepAliveSeconds(keepAliveSeconds);
//        executor.setRejectedExecutionHandler(callerRunsPolicy);
//        executor.setThreadNamePrefix(threadNamePrefix);
//        executor.setRejectedExecutionHandler(callerRunsPolicy);
//        executor.initialize();
//        return executor;
//    }
//
//}
