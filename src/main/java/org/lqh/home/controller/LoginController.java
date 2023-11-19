package org.lqh.home.controller;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.lqh.home.common.Constants;
import org.lqh.home.interceptor.TokenInterceptor;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.net.param.LoginParam;
import org.lqh.home.net.param.RegisterParam;
import org.lqh.home.net.param.Result;
import org.lqh.home.service.IEmployeeService;
import org.lqh.home.service.ILoginService;
import org.lqh.home.service.IUserService;
import org.lqh.home.service.impl.RedisService;
import org.lqh.home.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/6
 **/
@RestController
public class LoginController {

    private RedisTemplate redisTemplate;
    private ILoginService iLoginService;
    private Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    @Autowired
    public LoginController(ILoginService iLoginService, RedisTemplate redisTemplate) {
        this.iLoginService = iLoginService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/login")
    public NetResult login(@RequestBody LoginParam loginParam) {

        //判断手机号是不是空
        if (StringUtil.isEmpty(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }

        //判断手机号合法
        if (!RegexUtil.isMobileExact(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号格式不正确");
        }

        String expiredV = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getSMSRedisKey(loginParam.getPhone()));
        String code = loginParam.getCode();
        //验证码过期没
        if (StringUtil.isEmpty(expiredV)) {
            return ResultGenerator.genErrorResult(NetCode.CODE_ERROR, Constants.CODE_LAPSE);
        }
        //验证码是否正确
        if (!code.equals(expiredV)) {
            return ResultGenerator.genErrorResult(NetCode.CODE_ERROR, Constants.CODE_ERROR);
        }
        //登录
        NetResult netResult = iLoginService.login(loginParam);
        return netResult;
    }

    //验证
    @GetMapping("/verifycode")
    public NetResult verifyCode(@RequestParam String phone, @RequestParam String code) {
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if (!RegexUtil.isMobileExact(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不合法");
        }
        String expiredV = (String) redisTemplate.opsForValue().get(phone);

        if (StringUtil.isNullOrNullStr(expiredV)) {
            return ResultGenerator.genErrorResult(NetCode.CODE_ERROR, Constants.CODE_LAPSE);
        } else {
            if (expiredV.equals(code)) {
                return ResultGenerator.genSuccessResult("验证码正常");
            } else {
                return ResultGenerator.genErrorResult(NetCode.CODE_ERROR, Constants.CODE_ERROR);
            }
        }
    }

    //注册
    @PostMapping("/register")
    public NetResult register(@RequestBody RegisterParam registerParam) {
        try {
            NetResult netResult = iLoginService.register(registerParam);
            return netResult;
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知异常" + e.getMessage());
        }
    }

    /**
     * 短信发送验证码
     *
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("/getverifycode")
    public NetResult sendCode(@RequestParam String phone) throws Exception {
        //System.out.println(phone);
        // 检查手机号是否空
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        //检查手机号格式
        if (!RegexUtil.isMobileExact(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号格式不正确");
        }
        String code = RandomCode.getCode();
        String ssmResult =AliSendSMSUtil.sendSms(code,phone);
        if (ssmResult == null){
            return ResultGenerator.genFailResult("发送验证码失败");
        }
        // 将新的验证码存入缓存，设置过期时间为60秒
        redisTemplate.opsForValue().set(RedisKeyUtil.getSMSRedisKey(phone), code, 300, TimeUnit.SECONDS);
        return ResultGenerator.genSuccessResult(Result.StringToJson(ssmResult));
    }
}
