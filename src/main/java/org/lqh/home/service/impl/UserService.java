package org.lqh.home.service.impl;

import org.lqh.home.entity.User;
import org.lqh.home.mapper.UserMapper;
import org.lqh.home.net.param.RegisterParam;
import org.lqh.home.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/16
 **/
@Service
public class UserService implements IUserService {
    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }
    @Override
    public int add(RegisterParam registerParam) {
        return userMapper.add(registerParam);
    }

    @Override
    public User getUser(String phone, String password) {
        return userMapper.getUser(phone, password);
    }

    @Override
    public User findByPhone(String phone) {
        return userMapper.selectPhone(phone);
    }

    @Override
    public User findById(long id) {
        return userMapper.findById(id);
    }

    @Override
    public int settlementById(double money, long id) {
        return userMapper.settlementById(money, id);
    }
}
