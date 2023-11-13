package org.lqh.home.entity;

import lombok.Data;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/3
 **/
@Data
public class Shop {
    private long id;
    private String name;
    private String tel;
    private Long registerTime;
    private int state;
    private String address;
    private String logo;
    private Employee admin;
    private long admin_id;
}
