package org.lqh.home.service;

import org.apache.ibatis.annotations.Param;
import org.lqh.home.entity.User;
import org.lqh.home.net.param.RegisterParam;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/16
 **/
public interface IUserService {
    int add(RegisterParam registerParam);

    User getUser(@Param("phone") String phone, @Param("password") String password);

    User findByPhone(String phone);

    User findById(long id);

    int settlementById(@Param("money") double money,@Param("id") long id);
}
