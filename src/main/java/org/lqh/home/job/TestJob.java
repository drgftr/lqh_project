package org.lqh.home.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/21
 **/
@Slf4j
public class TestJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(TestJob.class);
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String i = context.getJobDetail().getJobDataMap().getString("i");
        // get parameters
//        context.getJobDetail().getJobDataMap().forEach(
//                (k, v) -> logger.info("param, key:{}, value:{}", k, v)
//        );
        // your logics
        logger.info("我是第{}个job，执行时间{}: ",i,new Date());
    }

}
