package org.lqh.home.controller;

import org.lqh.home.job.TestJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/21
 **/
@RestController
public class TestController {


    private Scheduler schedule;

    @Autowired
    public TestController(Scheduler schedule) {
        this.schedule = schedule;
    }

    @GetMapping("/order")
    public void order(@RequestParam int id){
        JobDetail job = newJob(TestJob.class)
                .usingJobData("id",id+"")
                .withIdentity("Job_"+id, "group1")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("Job_"+id+"_trigger", "group1")
                .startAt(new Date(System.currentTimeMillis() + 10*1000))
                .build();
        try {
            schedule.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/test")
    public void test(){

        for (int i=0;i<5;i++){
            JobDetail job = newJob(TestJob.class)
                    .usingJobData("i",i+"")
                    .withIdentity("Job_"+i, "group1")
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity("Job_"+i+"_trigger", "group1")
                    .startAt(new Date(System.currentTimeMillis() + (i+1)*2*1000))
                    .build();
            try {
                schedule.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }
}
