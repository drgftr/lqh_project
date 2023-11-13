package org.lqh.home.net.param;

import lombok.Data;
import org.lqh.home.entity.Users;

import java.io.Serializable;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/9
 **/
@Data
public class RegisterParam implements Serializable {
    //private Users users;
    private String code;
    private String username;
    private String phone;
    private String password;
    private int state;
    private int age;
    private Long registertime;
}
