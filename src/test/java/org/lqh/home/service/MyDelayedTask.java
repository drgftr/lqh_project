package org.lqh.home.service;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @description: compareTo 方法必须提供与 getDelay 方法一致的排序
 * DelayQueue只能添加(offer/put/add)实现了Delayed接口的对象
 * @author: 丁真
 * @date: 2023/11/20
 **/
public class MyDelayedTask implements Delayed {

    private String name ;
    private long start = System.currentTimeMillis();
    private long time ;

    public MyDelayedTask(String name,long time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert((start+time) - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        MyDelayedTask o1 = (MyDelayedTask) o;
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "MyDelayedTask{" +
                "name='" + name + '\'' +
                ", start=" + start +
                ", time=" + time +
                '}';
    }
}
