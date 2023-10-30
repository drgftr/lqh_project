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
    private String username;
    private String email;
    private String phone;
    private String password;
    private int age;
    private int state;

}
