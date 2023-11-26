package org.lqh.home.service;

import org.lqh.home.entity.Order;
import org.lqh.home.utils.ReturnT;

import java.util.concurrent.DelayQueue;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/20
 **/
public interface ICancelOrderService {
    //勾销订单
    void  cancelOrder();
    //支付
    ReturnT payOrder(String orderNo);
    //获取所有订单
    DelayQueue<Order> getAllOrder();
    //生成订单
    DelayQueue<Order> getOrder(long user_id);
}
