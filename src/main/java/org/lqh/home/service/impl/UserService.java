package org.lqh.home.service.impl;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.lqh.home.common.Constants;
import org.lqh.home.entity.Employee;
import org.lqh.home.entity.Users;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.net.param.LoginParam;
import org.lqh.home.net.param.RegisterParam;
import org.lqh.home.net.param.Result;
import org.lqh.home.service.IEmployeeService;
import org.lqh.home.service.IUserService;
import org.lqh.home.service.IUsersService;
import org.lqh.home.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/6
 **/
@Service
public class UserService implements IUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private IEmployeeService iEmployeeService;

    private RedisService redisService;

    private RedisTemplate redisTemplate;

    private IUsersService iUsersService;

    @Autowired
    public UserService(RedisService redisService, IEmployeeService iEmployeeService, RedisTemplate redisTemplate,IUsersService iUsersService) {
        this.redisService = redisService;
        this.iEmployeeService = iEmployeeService;
        this.redisTemplate = redisTemplate;
        this.iUsersService = iUsersService;
    }

    @Override
    public NetResult sendRegisterCode(String phone) {
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if (!RegexUtil.isMobileExact(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不合法");
        }

        //已被注册
        if (iUsersService.selectPhone(phone)!=null) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号已注册");
        }
        Long lastSandTime = 0L;
        try {
            lastSandTime = Long.parseLong(this.redisService.getValue(phone));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            lastSandTime = 0l;
            redisService.cacheValue(phone, System.currentTimeMillis() + "", 60);
        }

        //在不在1分钟内
        if (System.currentTimeMillis() - lastSandTime < 1 * 60 * 1000) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "调用频率过多");
        }

        String expiredV = redisService.getValue(phone + phone);
        if (StringUtil.isNullOrNullStr(expiredV)) {
            String code = "123456" ; //+ System.currentTimeMillis()
            redisService.cacheValue(phone + phone, code, 60);
//            CodeResBeam
            return ResultGenerator.genSuccessResult(code);
        } else {
            return ResultGenerator.genSuccessResult(expiredV);
        }

    }

    @Override
    public NetResult adminLogin(LoginParam loginParam) {
        //System.out.println(loginParam);
        //判断账号密码是不是空
        if (StringUtil.isEmpty(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if (StringUtil.isEmpty(loginParam.getPassword())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, Constants.PASSWORD_IS_NULL);
        }
        //密码md5加密
        loginParam.setPassword(MD5Util.MD5Encode(loginParam.getPassword(), "utf-8"));
        //从数据库找这个人
        Employee employee = iEmployeeService.getAdmin(loginParam.getPhone(),loginParam.getPassword());
        if (employee != null) {
            //如果有 加个token
            String token = UUID.randomUUID().toString();
            employee.setToken(token);
            employee.setPassword(null);
            redisTemplate.opsForValue().set(token, employee, 30, TimeUnit.MINUTES);
           //logger.info("token__"+token);
            return ResultGenerator.genSuccessResult(employee);
        }
        return ResultGenerator.genFailResult(Constants.ADMIN_IS_NULL);

    }

    @Override
    public NetResult login(LoginParam loginParam) {
        /**
         * 检查手机号是否空
         */
        if (StringUtil.isEmpty(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,Constants.PHONE_IS_NULL);
        }
        /**
         * 检查手机号格式
         */
        if (!RegexUtil.isMobileExact(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,Constants.PHONE_ERROR);
        }
        loginParam.setPassword(MD5Util.MD5Encode(loginParam.getPassword(), "utf-8"));
        Users users = iUsersService.getUser(loginParam.getPhone(),loginParam.getPassword());
        if(users != null){
            String token = UUID.randomUUID().toString();
            users.setToken(token);
            users.setPassword(null);
            redisTemplate.opsForValue().set(token, users, 30, TimeUnit.MINUTES);
            return ResultGenerator.genSuccessResult(users);
        }
        return ResultGenerator.genErrorResult(NetCode.LOGIN_ERROR,Constants.LOGIN_ERROR);
    }

    //注册
    @Override
    public NetResult  register(RegisterParam registerParam) {
        String code = registerParam.getCode();
        String expiredV = (String) redisTemplate.opsForValue().get(registerParam.getPhone());
        //看看验证码过期没
        if (StringUtil.isEmpty(expiredV)){
            return ResultGenerator.genErrorResult(NetCode.CODE_ERROR, Constants.CODE_LAPSE);
        }
        //验证码输入是否正确
        if (!code.equals(expiredV)){
            return ResultGenerator.genErrorResult(NetCode.CODE_ERROR, Constants.CODE_ERROR);
        }

        //判断账号是不是空
        if (StringUtil.isEmpty(registerParam.getUsername())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, Constants.NAME_IS_NULL);
        }

        //没密码给个123456
        if (StringUtil.isEmpty(registerParam.getPassword())) {
            registerParam.setPassword("123456");
        }
        registerParam.setPassword(MD5Util.MD5Encode(registerParam.getPassword(), "utf-8"));

        //面向成人的产品，不让未成年注册
        if (registerParam.getAge()<18){
            return ResultGenerator.genErrorResult(NetCode.AGE_INVALID, Constants.AGE_ERROR);
        }

        Users users1 = iUsersService.selectPhone(registerParam.getPhone());
        if(users1!=null){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,Constants.PHONE_OCCUPATION);
        }

        //设置当前时间
        registerParam.setRegistertime(System.currentTimeMillis());
        try {
            iUsersService.add(registerParam);
            return ResultGenerator.genSuccessResult("注册成功");
        }catch (Exception e){
            return ResultGenerator.genFailResult("注册失败"+e.getMessage());
        }
    }
}
