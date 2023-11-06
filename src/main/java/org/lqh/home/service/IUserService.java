package org.lqh.home.service;

import org.lqh.home.net.NetResult;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/6
 **/
public interface IUserService {

    /**
     * 发送二维码
     */
    NetResult sendRegisterCode(String phone);
}
