package org.lqh.home.entity;

import lombok.Data;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/13
 **/
@Data
public class Pet {
    private long id;
    private String type;
    private String description;

    public Pet(String type, String description){
        this.type = type;
        this.description = description;
    }
}

