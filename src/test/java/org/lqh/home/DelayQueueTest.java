package org.lqh.home;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.SneakyThrows;
import org.junit.Test;
import org.lqh.home.entity.Order;
import org.lqh.home.service.MyDelayedTask;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * DelayQueue是一个无界的BlockingQueue，用于放置实现了Delayed接口的对象，
 * 其中的对象只能在其到期时才能从队列中取走。这种队列是有序的，即队头对象的延迟到期时间最长。
 * 注意：不能将null元素放置到这种队列中。
 *
 * DelayQueue是一个没有边界BlockingQueue实现，加入其中的元素必需实现Delayed接口。当生产者线程调用put之类的方法加入元素时，
 * 会触发Delayed接口中的compareTo方法进行排序，也就是说队列中元素的顺序是按到期时间排序的，而非它们进入队列的顺序。
 * 排在队列头部的元素是最早到期的，越往后到期时间越晚
 * @author: 丁真
 * @date: 2023/11/20
 **/
public class DelayQueueTest {
    private static DelayQueue delayQueue = new DelayQueue();
    @Test
    public void testDelayQueue() throws InterruptedException {
        new Thread(new Runnable() {
        @SneakyThrows
        @Override
        public void run() {
            //将指定的元素插入到此延迟队列中。
            /**
             * public boolean offer(E e)将指定的元素插入到此延迟队列中。
             * Specified by:
             * offer在接口 BlockingQueue<E extends Delayed>
             * Specified by:
             * offer在接口 Queue<E extends Delayed>
             * 参数
             * e - 要添加的元素
             * 结果
             * true
             * 异常
             * NullPointerException - 如果指定的元素为空
             *
             * name可以重复
             */
            Date date = DateUtil.date();
            String k = null;
            delayQueue.offer(new MyDelayedTask(k,1000));
            delayQueue.offer(new MyDelayedTask("task1",390));
            delayQueue.offer(new MyDelayedTask("task3",190));
            delayQueue.offer(new MyDelayedTask("task4",590));
            delayQueue.offer(new MyDelayedTask("task5",690));
            delayQueue.offer(new MyDelayedTask("task6",790));
            delayQueue.offer(new MyDelayedTask("task7",490));
            System.out.println(Arrays.toString(delayQueue.toArray()));
            delayQueue.remove("task1");
            System.out.println(Arrays.toString(delayQueue.toArray()));
            delayQueue.put(new MyDelayedTask("task8",4900));
            System.out.println(Arrays.toString(delayQueue.toArray()));
            //检索并删除此队列的头部，如果需要，等待有一个延迟到期的元素在此队列上可用。
            delayQueue.take();
            System.out.println(Arrays.toString(delayQueue.toArray()));
            //检索并删除此队列的头，或者如果此队列没有已过期延迟的元素，则返回 null 。
            delayQueue.poll();
            System.out.println(Arrays.toString(delayQueue.toArray()));
            //检索但不删除此队列的头，如果此队列为空，则返回null 。 与poll不同，如果队列中没有过期的元素可用，则此方法返回将在以后过期的元素（如果存在）。
            System.out.println(delayQueue.peek());

            /**
             * MyDelayedTask{name='task3', start=1700485217199, time=1900}
             * MyDelayedTask{name='task1', start=1700485217199, time=3900}
             * MyDelayedTask{name='task7', start=1700485217199, time=4900}
             * MyDelayedTask{name='task4', start=1700485217199, time=5900}
             * MyDelayedTask{name='task5', start=1700485217199, time=6900}
             * MyDelayedTask{name='task6', start=1700485217199, time=7900}
             * MyDelayedTask{name='null', start=1700485217199, time=10000}
             */
        }
    }).start();

        while (true) {
            Delayed take = delayQueue.take();
            System.out.println(take);
        }
    }

    @Test
    public void testPut() throws InterruptedException {
        /**
         * public void put(E e)将指定的元素插入到此延迟队列中。 队列是无限制的，这个方法永远不会被阻塞。
         * Specified by:
         * put中的 BlockingQueue<E extends Delayed>
         * 参数
         * e - 要添加的元素
         * 异常
         * NullPointerException - 如果指定的元素为空
         */
        BlockingQueue<Task> delayqueue = new DelayQueue<>();
        long now = System.currentTimeMillis();
        delayqueue.put(new Task(now+3000));
        delayqueue.put(new Task(now+4000));
        delayqueue.put(new Task(now+6000));
        delayqueue.put(new Task(now+1000));
        System.out.println(delayqueue);
        /**
         * [1700485587838, 1700485589838, 1700485592838, 1700485590838]
         * 1700485587838
         * 1700485589838
         * 1700485590838
         * 1700485592838
         */

        for(int i=0; i<4; i++) {
            System.out.println(delayqueue.take());
        }
    }

    static class Task implements Delayed{
        long time = System.currentTimeMillis();
        public Task(long time) {
            this.time = time;
        }
        @Override
        public int compareTo(Delayed o) {
            if(this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS))
                return -1;
            else if(this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS))
                return 1;
            else
                return 0;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }
        @Override
        public String toString() {
            return "" + time;
        }
    }
}
