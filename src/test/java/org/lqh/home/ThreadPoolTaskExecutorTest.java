package org.lqh.home;

import org.junit.Test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池运行
 * 1）当池子大小小于corePoolSize就新建线程，并处理请求
 * 2）当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去从workQueue中取任务并处理
 * 3）当workQueue放不下新入的任务时，新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize就用RejectedExecutionHandler来做拒绝处理
 * 4）另外，当池子的线程数大于corePoolSize的时候，多余的线程会等待keepAliveTime长的时间，如果无请求可处理就自行销毁
 * @date: 2023/11/20
 **/
public class ThreadPoolTaskExecutorTest {
    private Logger log = LoggerFactory.getLogger(ThreadPoolTaskExecutorTest.class);
    /**
     * ThreadPoolTaskExecutor用法：
     * corePoolSize：线程池维护线程最小的数量，默认为1
     * maxPoolSize：线程池维护线程最大数量，默认为Integer.MAX_VALUE
     * keepAliveSeconds：(maxPoolSize-corePoolSize)部分线程空闲最大存活时间，默认存活时间是60s
     * queueCapacity：阻塞任务队列的大小，默认为Integer.MAX_VALUE，默认使用LinkedBlockingQueue
     * allowCoreThreadTimeOut：设置为true的话，keepAliveSeconds参数设置的有效时间对corePoolSize线程也有效，默认是flase
     * threadFactory：：用于设置创建线程的工厂，可以通过线程工厂给每个创建出来的线程设置更有意义的名字。使用开源框架guava提供的ThreadFactoryBuilder可以快速给线程池里的线程设置有意义的名字
     * rejectedExecutionHandler：拒绝策略，当队列workQueue和线程池maxPoolSize都满了，说明线程池处于饱和状态，那么必须采取一种策略处理提交的新任务。这个策略默认情况下是AbortPolicy，表示无法处理新任务时抛出异常，有以下四种策略，当然也可以根据实际业务需求类实现RejectedExecutionHandler接口实现自己的处理策略
     * 1.AbortPolicy：丢弃任务，并且抛出RejectedExecutionException异常
     * 2.DiscardPolicy：丢弃任务，不处理，不抛出异常
     * 3.CallerRunsPolicy：直接在execute方法的调用线程中运行被拒绝的任务
     * 4.DiscardOldestPolicy：丢弃队列中最前面的任务，然后重新尝试执行任务
     * 处理流程
     * 1.查看核心线程池是否已满，不满就创建一条线程执行任务，否则执行第二步。
     * 2.查看任务队列是否已满，不满就将任务存储在任务队列中，否则执行第三步。
     * 3.查看线程池是否已满，即就是是否达到最大线程池数，不满就创建一条线程执行任务，否则就按照策略处理无法执行的任务
     */
    @Test
    public void  test() throws InterruptedException {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); //核心池大小
        executor.setMaxPoolSize(10); //最大线程数
        executor.setQueueCapacity(10); //队列程度
        executor.setKeepAliveSeconds(60); //线程空闲时间
        executor.setThreadNamePrefix("子线程-");//线程前缀名称
        //丢弃任务，并且抛出RejectedExecutionException异常
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); //配置拒绝策略
        executor.initialize(); //初始化

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " 工作开始！");
                    Thread.sleep((long) (Math.random() * 2000));
//                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + " 工作结束！");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        for (int i = 0; i < 5; i++) {
            executor.execute(task);
        }
        //返回可用处理器的Java虚拟机的数量。
        int n = Runtime.getRuntime().availableProcessors();//获取到服务器的cpu内核
        log.info("服务器的cpu内核:{}", n);

        Thread.sleep(5000);
        System.out.println("主线程工作结束！");
        //关闭服务器
        executor.shutdown();
    }

}
