package org.lqh.home.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;


import org.lqh.home.entity.Order;
import org.lqh.home.service.ICancelOrderService;
import org.lqh.home.utils.ReturnT;
import org.lqh.home.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.DelayQueue;


/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/20
 **/
@Service
public class CancelOrderService implements ICancelOrderService {

    @Value("${order.isOpen}")
    private int isOpen;

    //提早队列，用来存订单对象
    DelayQueue<Order> queue = new DelayQueue<>();

    //@Autowired(required=true)
    @Autowired
    private ThreadPoolTaskExecutor executorService ;

//    @Autowired
//    public CancelOrderService (ThreadPoolExecutor executorService){
//        this.executorService = executorService;
//    }


    @Override
    public void cancelOrder() {
        executorService.submit(() -> {
            try {
                //创建一个线程，用来模仿定时取消过期订单job
                System.out.println("******开启主动取消订单******，时间:"+ DateUtil.date());
                while (isOpen == 1){
                    try {
                        //检索并删除此队列的头部
                        Order order = queue.take();
                        order.setStatus(2);
                        //queue.remove();
                        System.out.println("订单：" + order.getOrderNo() + "付款超时，主动勾销，当前时间：" + DateUtil.date());
                        System.out.println("当前订单延迟队列数量:"+queue.size());
                        System.out.println(queue.toString());
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @Override
    public ReturnT payOrder(String orderNo) {
        if (StringUtils.isEmpty(orderNo)){
            return ReturnT.getFail("订单号为空");
        }
        if (queue == null || queue.isEmpty()){
            return ReturnT.getFail("延迟队列为空");
        }
        System.out.println(orderNo + "付款中");
        Boolean flag = true;
        System.out.println("当前 延迟列队数量:"+queue.size());
        for (Order q : queue){
            if (q.getOrderNo().equals(orderNo)){
                queue.remove(q);
                System.out.println(orderNo+"付款完成!延迟列队数量:"+queue.size());
                System.out.println(queue.toString());
                flag=false;
            }
        }
        if (flag){
            return new ReturnT(403,"未找到订单");
        }
        return ReturnT.getSuccess();
    }

    @Override
    public DelayQueue<Order> getAllOrder() {
        return queue;
    }

    public void addOrder(Order order){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                queue.add(order);
            }
        });
    }

    @Override
    public DelayQueue<Order> getOrder(long user_id) {
        executorService.submit(() -> {
            try {
                Date date = DateUtil.date();
                System.out.println("开始创建订单.....,时间:"+DateUtil.date());
                queue.add(new Order("SO001", user_id, 0, date, DateUtil.offset(date, DateField.SECOND, 3)));
                queue.add(new Order("SO002", 2, 0, DateUtil.offset(date, DateField.SECOND, 3), DateUtil.offset(date, DateField.MINUTE, 1)));
                queue.add(new Order("SO003",3 , 0, DateUtil.offset(date, DateField.SECOND, 6), DateUtil.offset(date, DateField.MINUTE, 2)));
                System.out.println("当前订单延迟队列数量:"+queue.size());
                System.out.println(queue.toString());
                System.out.println("创建订单完毕......,时间:"+DateUtil.date());
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        return queue;
    }
}
