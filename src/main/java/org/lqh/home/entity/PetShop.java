package org.lqh.home.entity;

import lombok.Data;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/15
 **/
@Data
public class PetShop {
    private long id;
    //主人id 还未找到主人的这个是0 找到了就改成主人的
    private long user_id;
    //宠物信息
    private long usermsg_id;
    //管理员id
    private long admin_id;
    //店铺id
    private long shop_id;
    //宠物名
    private String name;
    //开始销售时间
    private long sellTime;
    //卖出去的时间
    private long endTime;
    //成本价
    private double costPrice;
    //销售价
    private double sellPrice;
    //状态 0未卖出去 1已出售
    private int state;
    //宠物类别
    private Pet pet;

    private UserMsg userMsg;
}
