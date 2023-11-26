package org.lqh.home.entity;

import lombok.Data;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/20
 **/
@Data
public class Order implements Delayed{

    //订单号
    public String orderNo;
    //用户id
    public long user_id;
    //订单状态（0待领取，1已领取，2已勾销）
    public int status;
    //订单创立时间
    public Date createTime;
    //订单失效时间
    public Date cancelTime;

    public Order(String orderNo, long user_id, Integer status, Date createTime, Date cancelTime) {
        this.orderNo = orderNo;
        this.user_id = user_id;
        this.status = status;
        this.createTime = createTime;
        this.cancelTime = cancelTime;
    }

    public Order (){
    }

    /**
     * 取得延迟时间，用失效时间-当前时间，时间单位须要对立
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        //上面用到unit.convert()办法，其实在这个小场景不须要用到，只是学习一下如何应用罢了
        return unit.convert(cancelTime.getTime() - System.currentTimeMillis(), TimeUnit.MINUTES);
    }

    /**
     * 用于提早队列外部比拟排序，以后工夫的延迟时间 - 比拟对象的延迟时间
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        //这里依据勾销工夫来比拟，如果勾销所剩时间小的，就会优先被队列提取进去
        //注意延迟时间 的绑定就是这绑定的属性
        return this.getCancelTime().compareTo(((Order)o).getCancelTime());
    }
}














