package org.lqh.home.service.impl;


import org.lqh.home.common.Constants;
import org.lqh.home.entity.Employee;
import org.lqh.home.entity.User;
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
        if (iUserService.findByPhone(phone) != null) {
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
            String code = "123456"; //+ System.currentTimeMillis()
            redisService.cacheValue(phone + phone, code, 60);
//            CodeResBeam
            return ResultGenerator.genSuccessResult(code);
        } else {
            return ResultGenerator.genSuccessResult(expiredV);
        }

    }

    @Override
    public NetResult login(LoginParam loginParam) {
        String phone = loginParam.getPhone();
        loginParam.setPassword(MD5Util.MD5Encode(loginParam.getPassword(), "utf-8"));
        //随机生成一个token
        String token = UUID.randomUUID().toString();
        //如果type=0则为普通用户，type=1则为管理员
        if (loginParam.getType() == 0) {
            //找这个用户
            User user = iUserService.getUser(phone, loginParam.getPassword());
            //如果用户存在
            if (user != null) {
                //设置token
                user.setToken(token);
                //把密码设置成null 保密
                user.setPassword(null);
                //把用户数据存入redis中 要用token取
                redisTemplate.opsForValue().set(RedisKeyUtil.getTokenRedisKey(token), user, 30, TimeUnit.MINUTES);
                return ResultGenerator.genSuccessResult(user);
            }
            //不存在返回用户账号密码错误
            return ResultGenerator.genErrorResult(NetCode.LOGIN_ERROR, Constants.LOGIN_ERROR);
        } else if (loginParam.getType() == 1) {
            //找到这个管理员
            Employee employee = iEmployeeService.getAdmin(phone, loginParam.getPassword());
            //如果存在
            if (employee != null) {
                //设置token
                employee.setToken(token);
                //把密码设置成null 保密
                employee.setPassword(null);
                //把管理员数据存入redis中 要用token取
                redisTemplate.opsForValue().set(RedisKeyUtil.getTokenRedisKey(token), employee, 30, TimeUnit.MINUTES);
                return ResultGenerator.genSuccessResult(employee);
            }
            //不存在返回管理员账号密码错误
            return ResultGenerator.genErrorResult(NetCode.LOGIN_ERROR, Constants.ADMIN_IS_NULL);
        } else {
            //不是1或0 返回非法请求
            return ResultGenerator.genErrorResult(NetCode.TYPE_ERROR, Constants.INVALID_REQUEST);
        }
    }

    //注册
    @Override
    public NetResult register(RegisterParam registerParam) {

        //判断账号是不是空
        if (StringUtil.isEmpty(registerParam.getUsername())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, Constants.NAME_IS_NULL);
        }
        //没密码给个123456
        if (StringUtil.isEmpty(registerParam.getPassword())) {
            registerParam.setPassword("123456");
        }
        //md5加密
        registerParam.setPassword(MD5Util.MD5Encode(registerParam.getPassword(), "utf-8"));

        //面向成人的产品，不让未成年注册
        if (registerParam.getAge() < 18) {
            return ResultGenerator.genErrorResult(NetCode.AGE_INVALID, Constants.AGE_ERROR);
        }

        //在数据库通过手机号找人
        User users1 = iUserService.findByPhone(registerParam.getPhone());
        //找到了说明已经注册
        if (users1 != null) {
            //一个手机号只能注册一次
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_OCCUPATION);
        }

        String code = registerParam.getCode();
        //从redis中获取验证码
        String expiredV = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getSMSRedisKey(registerParam.getPhone()));
        //验证码输入是否正确
        if (!code.equals(expiredV)) {
            return ResultGenerator.genErrorResult(NetCode.CODE_ERROR, Constants.CODE_ERROR);
        }

        //设置当前时间
        registerParam.setRegistertime(System.currentTimeMillis());
        //判断数据加到数据库中没
        int result = iUserService.add(registerParam);
        //没加进去
        if (result == 0){
            //返回注册失败
            return ResultGenerator.genFailResult("注册失败");
        }
        //返回注册成功
        return ResultGenerator.genSuccessResult("注册成功");
    }


}
