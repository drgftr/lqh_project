package org.lqh.home;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * (exclude = { DataSourceAutoConfiguration.class })
 *
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/26
 **/
@SpringBootApplication
@MapperScan("org.lqh.home.mapper")
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
