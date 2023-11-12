package org.lqh.home.entity;

import lombok.Data;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@Data
public class Pet {
    //id
    private int id;
    //类别
    private String type;
    //描述
    private String description;
}
