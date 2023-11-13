package org.lqh.home.utils;

import org.lqh.home.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/8
 **/
@Configuration
public class TokenConfig implements WebMvcConfigurer {


    private RedisTemplate redisTemplate;

    @Autowired
    public TokenConfig(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new TokenInterceptor(redisTemplate));
        registration.addPathPatterns("/**");  //所以路径都被拦截
        registration.excludePathPatterns(   //添加不拦截路径
                "/user/publish",
                "/login",                   //登录
                "/**/*.html",               //html静态资源
                "/**/*.js",                  //js静态资源
                "/**/*.css",                 //css静态资源
                "/getverifycode",
                "/register",
                "/adminlogin",
                "/userlogin"

        );
    }
}
