package org.lqh.home.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/26
 **/
@RestController
public class Hello {

    @RequestMapping("/")
    public String hello(){
        return "hello";
    }
}
