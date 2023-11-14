package org.lqh.home.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@Data
public class UserMsg implements Serializable {
    //id
    private long id;
    //用户id
    private long user_id;
    //管理员id
    private long admin_id;
    //店铺id
    private long shop_id;

    private long pet_id;
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
    //状态 0是上架了 1是没上架
    private int state;
    //宠物种类
    private Pet pet;
    private Shop shop;
    private Employee admin;
    private Users users;
    private String shopName;
    private String masterName;
}
