package org.lqh.home.net.param;

import lombok.Data;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/8
 **/
@Data
public class LoginParam {
    private String phone;
    private String password;
    private String username;
    //type 1管理员 type 0用户
    private int type;
}
