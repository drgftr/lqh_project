package org.lqh.home.service.impl;


import org.lqh.home.common.Constants;
import org.lqh.home.entity.Employee;
import org.lqh.home.entity.User;
import org.lqh.home.mapper.UserMapper;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.net.param.LoginParam;
import org.lqh.home.net.param.RegisterParam;
import org.lqh.home.service.IEmployeeService;
import org.lqh.home.service.ILoginService;
import org.lqh.home.service.IUserService;
import org.lqh.home.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/6
 **/
@Service
public class LoginService implements ILoginService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private IEmployeeService iEmployeeService;

    private RedisService redisService;

    private RedisTemplate redisTemplate;

    private IUserService iUserService;

    @Autowired
    public LoginService(RedisService redisService, IEmployeeService iEmployeeService, RedisTemplate redisTemplate
            , IUserService iUserService) {
        this.redisService = redisService;
        this.iEmployeeService = iEmployeeService;
        this.redisTemplate = redisTemplate;
        this.iUserService = iUserService;
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
        if (iUserService.findByPhone(phone)!=null) {
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
    public NetResult login(LoginParam loginParam){
        loginParam.setPassword(MD5Util.MD5Encode(loginParam.getPassword(), "utf-8"));
        //随机生成一个token
        String token = UUID.randomUUID().toString();
        //如果type=0则为普通用户，type=1则为管理员
        if (loginParam.getType() == 0){
            //找这个用户
            User user = iUserService.getUser(loginParam.getPhone(),loginParam.getPassword());
            //如果用户存在
            if(user != null){
                //设置token
                user.setToken(token);
                //把密码设置成null 保密
                user.setPassword(null);
                //把用户数据存入redis中 要用token取
                redisTemplate.opsForValue().set(token, user, 30, TimeUnit.MINUTES);
                return ResultGenerator.genSuccessResult(user);
            }
            //不存在返回用户账号密码错误
            return ResultGenerator.genErrorResult(NetCode.LOGIN_ERROR,Constants.LOGIN_ERROR);
        }else if(loginParam.getType()==1){
            //找到这个管理员
            Employee employee = iEmployeeService.select(loginParam.getPhone(),loginParam.getPassword());
            //如果存在
            if (employee != null) {
                //设置token
                employee.setToken(token);
                //把密码设置成null 保密
                employee.setPassword(null);
                //把管理员数据存入redis中 要用token取
                redisTemplate.opsForValue().set(token, employee, 30, TimeUnit.MINUTES);
                return ResultGenerator.genSuccessResult(employee);
            }
            //不存在返回管理员账号密码错误
            return ResultGenerator.genErrorResult(NetCode.LOGIN_ERROR,Constants.ADMIN_IS_NULL);
        }else{
            //不是1或0 返回非法请求
            return ResultGenerator.genErrorResult(NetCode.TYPE_ERROR, Constants.INVALID_REQUEST);
        }
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

        User users1 = iUserService.findByPhone(registerParam.getPhone());
        if(users1!=null){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,Constants.PHONE_OCCUPATION);
        }

        //设置当前时间
        registerParam.setRegistertime(System.currentTimeMillis());
        try {
            iUserService.add(registerParam);
            return ResultGenerator.genSuccessResult("注册成功");
        }catch (Exception e){
            return ResultGenerator.genFailResult("注册失败"+e.getMessage());
        }
    }


}
