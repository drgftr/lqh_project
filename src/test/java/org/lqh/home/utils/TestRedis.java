package org.lqh.home.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lqh.home.service.impl.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/6
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    @Test
    public void setRedis(){
        redisTemplate.opsForValue().set("first","siwei");
        redisTemplate.opsForValue().set("second","siweiWu",30, TimeUnit.SECONDS);
        System.out.println("存入缓存成功");
    }

    @Test
    public void test(){
        redisService.cacheSet("1","2",60);
        System.out.println("---");
        System.out.println(redisService.getSet("1"));
    }
}
