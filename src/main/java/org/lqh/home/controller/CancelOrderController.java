package org.lqh.home.controller;

import org.lqh.home.entity.Order;
import org.lqh.home.net.NetResult;
import org.lqh.home.service.ICancelOrderService;
import org.lqh.home.service.impl.CancelOrderService;
import org.lqh.home.utils.ResultGenerator;
import org.lqh.home.utils.ReturnT;
import org.lqh.home.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/20
 **/
@RestController
@RequestMapping("/pay")
public class CancelOrderController {

//    @Resource(name="cacheUpdateTaskExecutor")
//    private ThreadPoolTaskExecutor applicationTaskExecutor;

    @Resource
    private CancelOrderService cancelOrderService;


//    @Autowired
//    public CancelOrderController(CancelOrderService cancelOrderService){
//        this.cancelOrderService = cancelOrderService;
//    }

    //生成订单
    @PostMapping("/generateOrders")
    public NetResult generateOrders(long userId){
        //String orderNo = StringUtil.getUUID();
        cancelOrderService.getOrder(userId);
        return ResultGenerator.genSuccessResult("订单生成成功");
    }

    //取消订单
    @PostMapping("/cancelOrder")
    public NetResult cancelOrder(){
        cancelOrderService.cancelOrder();
        return ResultGenerator.genSuccessResult("订单取消成功");
    }

    @GetMapping("/payOrder")
    public NetResult payOrder(String orderNo){
        //String orderNo = StringUtil.getUUID();
        ReturnT returnT = cancelOrderService.payOrder(orderNo);
        return ResultGenerator.genSuccessResult(returnT);
    }

    @GetMapping("/getAllOrder")
    public NetResult getAllOrder(){

        DelayQueue<Order> allOrder = cancelOrderService.getAllOrder();
        return ResultGenerator.genSuccessResult(allOrder);
    }


}
