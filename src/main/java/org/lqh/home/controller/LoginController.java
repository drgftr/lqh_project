package org.lqh.home.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.lqh.home.common.Constants;
import org.lqh.home.interceptor.TokenInterceptor;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.net.param.LoginParam;
import org.lqh.home.net.param.RegisterParam;
import org.lqh.home.net.param.Result;
import org.lqh.home.service.IEmployeeService;
import org.lqh.home.service.impl.RedisService;
import org.lqh.home.service.impl.UserService;
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

    private RedisService redisService;
    private UserService userService;
    private IEmployeeService iEmployeeService;
    private RedisTemplate redisTemplate;
    private Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);



    @Autowired
    public LoginController(RedisService redisService, UserService userService, IEmployeeService iEmployeeService, RedisTemplate redisTemplate){
        this.redisService = redisService;
        this.userService = userService;
        this.iEmployeeService =iEmployeeService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping(value = "/login" ,produces = {"application/json", "application/xml"})
    public NetResult adminLogin(@RequestBody LoginParam loginParam){
        String expiredV = redisService.getValue(loginParam.getPhone());
        if (expiredV==null){
            return ResultGenerator.genFailResult("验证码错误");
        }
        if (loginParam.getType() == 0){
            try {
                 NetResult netResult = userService.login(loginParam);
                 return netResult;
        }catch (Exception e){
            return ResultGenerator.genFailResult("未知异常"+e.getMessage());
        }
        }else {
            try {
                NetResult netResult = userService.adminLogin(loginParam);
                return netResult;
            }catch (Exception e){
                return ResultGenerator.genFailResult("未知异常"+e.getMessage());
            }
        }
    }


    //验证
    @GetMapping("/verifycode")
    public NetResult verifyCode(@RequestParam String phone,@RequestParam String code){
        if (StringUtil.isEmpty(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if(!RegexUtil.isMobileExact(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号不合法");
        }
        String expiredV = redisService.getValue(phone+phone);

//        String e = new ArrayList(expiredV).get(0).toString();
//        System.out.println(e);
//        System.out.println(expiredV.toString());
//        System.out.println(StringUtil.isNullOrNullStr(expiredV.toString()));
        if(StringUtil.isNullOrNullStr(expiredV)){
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
    @PostMapping("/register")
    public NetResult register(RegisterParam registerParam){
        System.out.println();
        try {
            NetResult netResult = userService.register(registerParam);
            return netResult;
        }catch (Exception e){
            return ResultGenerator.genFailResult("未知异常"+e.getMessage());
        }
    }

    /**
     * 短信发送验证码
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("/getverifycode")
    public NetResult sendCode(@RequestParam String phone) {
        /**
         * 检查手机号是否空
         */
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,Constants.PHONE_IS_NULL);
        }
        /**
         * 检查手机号格式
         */
        if (!RegexUtil.isMobileExact(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号格式不正确");
        }
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "GET";
        String appcode = "dd31c4a2f9014af5b66dd61889cfcfb0";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        String code = RandomCode.getCode() ;
        bodys.put("content", "code:"+code);
        bodys.put("template_id", "CST_ptdie100");
        bodys.put("phone_number",phone);
        //HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            String result = EntityUtils.toString(response.getEntity());
            return ResultGenerator.genSuccessResult(result);
        }catch (Exception e){
            e.printStackTrace();
        }

//        HttpEntity entity = response.getEntity();
//        String result = null;
//        if (entity != null) {
//            try (InputStream inputStream = entity.getContent()) {
//                result = convertStreamToString(inputStream);
//                logger.info(result);
//                // 将新的验证码存入缓存，设置过期时间为60秒
//                redisTemplate.opsForValue().set(phone, code, 300, TimeUnit.SECONDS);
//
//                return ResultGenerator.genSuccessResult(Result.StringToJson(result));
//            } catch (IOException e) {
//                // 处理异常
//                e.printStackTrace();
//            }
//        }
       return ResultGenerator.genFailResult("发送验证码失败");

    }

    //处理流异常的状态
    private String convertStreamToString(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            // 处理异常
            return null;
        }
    }




//    @PostMapping("/userlogin")
//    public NetResult userLogin(@RequestBody LoginParam loginParam){
//        System.out.println(loginParam);
//        try {
//            NetResult netResult = userService.login(loginParam);
//            return netResult;
//        }catch (Exception e){
//            return ResultGenerator.genFailResult("未知异常"+e.getMessage());
//        }
//    }

//    //发验证码的
//    @GetMapping("/getverifycode")
//    public NetResult sendVerifyCode(@RequestParam String phone){
//        return userService.sendRegisterCode(phone);
//
//    }

}
