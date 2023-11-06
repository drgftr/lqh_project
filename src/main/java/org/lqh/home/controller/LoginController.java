package org.lqh.home.controller;

import io.netty.util.internal.StringUtil;
import org.lqh.home.common.Constants;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.service.impl.RedisService;
import org.lqh.home.service.impl.UserService;
import org.lqh.home.utils.RegexUtil;
import org.lqh.home.utils.ResultGenerator;
import org.lqh.home.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/6
 **/
@RestController
public class LoginController {

    private RedisService redisService;
    private UserService userService;

    @Autowired
    public  LoginController(RedisService redisService,UserService userService){
        this.redisService = redisService;
        this.userService = userService;
    }

    //登录
    @GetMapping("/getverifycode")
    public NetResult sendVerifyCode(@RequestParam String phone){
        return userService.sendRegisterCode(phone);

    }

    //验证
    @GetMapping("/verifycode")
    public NetResult verifyCode(@RequestParam String phone,@RequestParam String code){
        if (StringUtils.isEmpty(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if(!RegexUtil.isMobileExact(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号不合法");
        }
        String expiredV = redisService.getValue(phone+phone);
        System.out.println(expiredV);
        if(StringUtil.isNullOrEmpty(expiredV)){
            return  ResultGenerator.genFailResult("验证码过期");
        }else {
            if (expiredV.equals(code)){
                return  ResultGenerator.genSuccessResult("验证码正常");
            }else {
                return ResultGenerator.genFailResult("验证码不存在");
            }
        }
    }

    //注册
    public void register(){

    }
}
