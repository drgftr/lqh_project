package org.lqh.home.service.impl;

import io.netty.util.internal.StringUtil;
import org.lqh.home.common.Constants;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.service.IUserService;
import org.lqh.home.utils.RegexUtil;
import org.lqh.home.utils.ResultGenerator;
import org.lqh.home.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/6
 **/
@Service
public class UserService implements IUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private  RedisService redisService;

    @Autowired
    public UserService(RedisService redisService){
        this.redisService = redisService;
    }
    @Override
    public NetResult sendRegisterCode(String phone) {
        if (StringUtils.isEmpty(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if(!RegexUtil.isMobileExact(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号不合法");
        }

        //已被注册
        if(!RegexUtil.isMobileExact(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号已注册");
        }
        Long lastSandTime = 0L;
        try {
             lastSandTime = Long.parseLong(this.redisService.getValue(phone));
        }catch (NumberFormatException e){
            logger.error(e.getMessage());
            lastSandTime = 0l;
            redisService.cacheValue(phone,System.currentTimeMillis()+"",60);
        }

        //在不在1分钟内
        if(System.currentTimeMillis() - lastSandTime < 1*60*1000){
            return  ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"调用频率过多");
        }

        String expiredV = redisService.getValue(phone+phone);

        if(StringUtils.isNullOrNullStr(expiredV)){
            String code = "123456"+System.currentTimeMillis();
            redisService.cacheSet(phone+phone,code,60);
//            CodeResBeam
            return  ResultGenerator.genSuccessResult(code);
        }else {
            return  ResultGenerator.genSuccessResult(expiredV);
        }

    }
}
