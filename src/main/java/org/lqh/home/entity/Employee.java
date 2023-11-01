package org.lqh.home.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/26
 **/
@Getter
@Setter
@Data
public class Employee {
    public Long id;
    //员工部门id
    private Long did;
    //员工名称
    private String username;
    //邮箱
    private String email;
    //手机号
    private String phone;
    //密码
    private String password;
    //年龄
    private int age;
    //状态0正常 1离职
    private int state;
    //方便传数据的
    private Department department;

}
