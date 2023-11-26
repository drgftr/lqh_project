package org.lqh.home.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/9
 **/
@Data
public class User implements Serializable {
    //用户id
    private Long id;
    //用户名
    private String username;
    //手机号
    private String phone;
    //密码
    private String password;
    //状态
    private int state;
    //年龄
    private int age;
    //注册时间
    private Long registerTime;
    private double money;
    //token
    private String token;
}
