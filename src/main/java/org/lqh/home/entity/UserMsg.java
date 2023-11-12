package org.lqh.home.entity;

import lombok.Data;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@Data
public class UserMsg {
    //id
    private long id;
    //用户id
    private long userId;
    //管理员id
    private long adminId;
    //店铺id
    private long shopId;
    //宠物name
    private String name;
    //地址
    private String address;
    //发布时间
    private long createtime;
    //生日
    private long birth;
    //价格
    private double price;
    //是否接种 0未接种 1已接种
    private int isinoculation;
    //性别
    private String sex;
    //状态
    private int state;

}
