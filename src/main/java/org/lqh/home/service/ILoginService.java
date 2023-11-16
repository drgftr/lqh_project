package org.lqh.home.service;

import org.apache.ibatis.annotations.Param;
import org.lqh.home.entity.User;
import org.lqh.home.net.NetResult;
import org.lqh.home.net.param.LoginParam;
import org.lqh.home.net.param.RegisterParam;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/6
 **/
public interface ILoginService {

    /**
     * 发送二维码
     */
    NetResult sendRegisterCode(String phone);


    NetResult login(LoginParam loginParam);

    NetResult register(RegisterParam registerParam);


}
